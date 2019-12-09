package org.gobiiproject.gobiiprocess.digester;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.gobiiproject.gobiiapimodel.payload.Header;
import org.gobiiproject.gobiiapimodel.payload.HeaderStatusMessage;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiapimodel.restresources.gobii.GobiiUriFactory;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.GobiiCropConfig;
import org.gobiiproject.gobiimodel.config.RestResourceId;
import org.gobiiproject.gobiimodel.cvnames.JobProgressStatusType;
import org.gobiiproject.gobiimodel.dto.Marshal;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.DataSetDTO;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.ExtractorInstructionFilesDTO;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiDataSetExtract;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiExtractorInstruction;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiFile;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderMetadata;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderProcedure;
import org.gobiiproject.gobiimodel.types.DatasetOrientationType;
import org.gobiiproject.gobiimodel.types.GobiiAutoLoginType;
import org.gobiiproject.gobiimodel.types.GobiiExtractFilterType;
import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;
import org.gobiiproject.gobiimodel.types.GobiiFileType;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.gobiiproject.gobiimodel.types.ServerType;
import org.gobiiproject.gobiimodel.utils.DateUtils;
import org.gobiiproject.gobiimodel.utils.FileSystemInterface;
import org.gobiiproject.gobiimodel.utils.HelperFunctions;
import org.gobiiproject.gobiimodel.utils.InstructionFileValidator;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.gobiiproject.gobiimodel.utils.SimpleTimer;
import org.gobiiproject.gobiimodel.utils.email.MailInterface;
import org.gobiiproject.gobiimodel.utils.email.ProcessMessage;
import org.gobiiproject.gobiimodel.utils.error.Logger;
import org.gobiiproject.gobiiprocess.HDF5Interface;
import org.gobiiproject.gobiiprocess.JobStatus;
import org.gobiiproject.gobiiprocess.digester.HelperFunctions.MobileTransform;
import org.gobiiproject.gobiiprocess.digester.HelperFunctions.SequenceInPlaceTransform;
import org.gobiiproject.gobiiprocess.digester.csv.CSVFileReaderV2;
import org.gobiiproject.gobiiprocess.digester.utils.validation.DigestFileValidator;
import org.gobiiproject.gobiiprocess.digester.utils.validation.ValidationConstants;
import org.gobiiproject.gobiiprocess.digester.utils.validation.errorMessage.ValidationError;
import static org.gobiiproject.gobiimodel.utils.FileSystemInterface.rmIfExist;
import static org.gobiiproject.gobiimodel.utils.HelperFunctions.getDestinationFile;
import static org.gobiiproject.gobiimodel.utils.HelperFunctions.getWebserviceConnectionString;
import static org.gobiiproject.gobiimodel.utils.error.Logger.logError;

/**
 * Base class for processing instruction files. Start of chain of control for Digester. Takes first argument as instruction file, or promts user.
 * The File Reader runs off the Instruction Files, which tell it where the input files are, and how to process them.
 * {@link CSVFileReaderV2} and deal with specific file formats. Overall logic and program flow come from this class.
 * <p>
 * This class deals with external commands and scripts, and coordinates uploads to the IFL and directly talks to HDF5 and MonetDB.
 *
 * @author jdl232 Josh L.S.
 */
public class GobiiFileReader {
    private static String rootDir = "../";
    private static String loaderScriptPath;
    private static String extractorScriptPath;
    private static final String VARIANT_CALL_TABNAME = "matrix";
    private static final String LINKAGE_GROUP_TABNAME = "linkage_group";
    private static final String GERMPLASM_PROP_TABNAME = "germplasm_prop";
    private static final String GERMPLASM_TABNAME = "germplasm";
    private static final String MARKER_TABNAME = "marker";
    private static final String DS_MARKER_TABNAME = "dataset_marker";
    private static final String DS_SAMPLE_TABNAME = "dataset_dnarun";
    private static final String SAMPLE_TABNAME = "dnarun";
    private static String pathToHDF5Files;
    private static boolean verbose;
    private static String errorLogOverride;
    private static String propertiesFile;
    private static GobiiUriFactory gobiiUriFactory;
    private static GobiiExtractorInstruction qcExtractInstruction = null;

    //Trinary - was this load marker fast(true), sample fast(false), or unknown/not applicable(null)
    public static Boolean isMarkerFast=null;

    /**
     * Main class of Digester Jar file. Uses command line parameters to determine instruction file, and runs whole program.
     *
     * @param args See Digester.jar -? to get a list of arguments
     * throws FileNotFoundException, IOException, ParseException, InterruptedException
     */
    public static void main(String[] args) throws Exception {
        //Section - Setup
        Options o = new Options()
                .addOption("v", "verbose", false, "Verbose output")
                .addOption("e", "errlog", true, "Error log override location")
                .addOption("r", "rootDir", true, "Fully qualified path to gobii root directory")
                .addOption("c", "config", true, "Fully qualified path to gobii configuration file")
                .addOption("h", "hdfFiles", true, "Fully qualified path to hdf files");
        LoaderGlobalConfigs.addOptions(o);
        ProcessMessage pm = new ProcessMessage();
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cli = parser.parse(o, args);
            if (cli.hasOption("rootDir")) rootDir = cli.getOptionValue("rootDir");
            if (cli.hasOption("verbose")) verbose = true;
            if (cli.hasOption("errLog")) errorLogOverride = cli.getOptionValue("errLog");
            if (cli.hasOption("config")) propertiesFile = cli.getOptionValue("config");
            if (cli.hasOption("hdfFiles")) HDF5Interface.setPathToHDF5Files(cli.getOptionValue("hdfFiles"));
            LoaderGlobalConfigs.setFromFlags(cli);
            args = cli.getArgs();//Remaining args passed through

        } catch (org.apache.commons.cli.ParseException exp) {
            new HelpFormatter().printHelp("java -jar Digester.jar ", "Also accepts input file directly after arguments\n" +
                    "Example: java -jar Digester.jar -c /home/jdl232/customConfig.properties -v /home/jdl232/testLoad.json", o, null, true);
            System.exit(2);
        }

        extractorScriptPath = rootDir + "extractors/";
        loaderScriptPath = rootDir + "loaders/";
        HDF5Interface.setPathToHDF5(loaderScriptPath + "hdf5/bin/");

        if (propertiesFile == null) propertiesFile = rootDir + "config/gobii-web.xml";

        boolean success = true;
        Map<String, File> loaderInstructionMap = new HashMap<>();//Map of Key to filename
        List<String> loaderInstructionList = new ArrayList<>(); //Ordered list of loader instructions to execute, Keys to loaderInstructionMap

        ConfigSettings configuration = null;
        try {
            configuration = new ConfigSettings(propertiesFile);
            Logger.logDebug("Config file path", "Opened config settings at " + propertiesFile);

        } catch (Exception e1) {
            e1.printStackTrace();
        }

        MailInterface mailInterface = new MailInterface(configuration);

        String instructionFile;
        if (args.length == 0 || "".equals(args[0])) {
            Scanner s = new Scanner(System.in);
            System.out.println("Enter Loader Instruction File Location:");
            instructionFile = s.nextLine();
        } else {
            instructionFile = args[0];
        }

        //Error logs go to a file based on crop (for human readability) and
        Logger.logInfo("Digester", "Beginning read of " + instructionFile);

        final String instructionFileContents = HelperFunctions.readFile(instructionFile);

        GobiiLoaderProcedure procedure = Marshal.unmarshalGobiiLoaderProcedure(instructionFileContents);

        if (procedure == null || procedure.getInstructions() == null || procedure.getInstructions().isEmpty()) {
            logError("Digester", "No instruction for file " + instructionFile);
            return;
        }


        Integer dataSetId = procedure.getMetadata().getDataset().getId();

        if (procedure.getMetadata().getGobiiCropType() == null)
            procedure.getMetadata().setGobiiCropType(divineCrop(instructionFile));

        //Job Id is the 'name' part of the job file  /asd/de/name.json
        String filename = new File(instructionFile).getName();
        String jobFileName = filename.substring(0, filename.lastIndexOf('.'));
        JobStatus jobStatus = null;
        try {
            jobStatus = new JobStatus(configuration, procedure.getMetadata().getGobiiCropType(), jobFileName);
        } catch (Exception e) {
            Logger.logError("GobiiFileReader", "Error Checking Status", e);
        }

        pm.addIdentifier("Project", procedure.getMetadata().getProject());
        pm.addIdentifier("Platform", procedure.getMetadata().getPlatform());
        pm.addIdentifier("Experiment", procedure.getMetadata().getExperiment());
        pm.addIdentifier("Dataset", procedure.getMetadata().getDataset());
        pm.addIdentifier("Mapset", procedure.getMetadata().getMapset());
        pm.addIdentifier("Dataset Type", procedure.getMetadata().getDatasetType());

        jobStatus.set(JobProgressStatusType.CV_PROGRESSSTATUS_INPROGRESS.getCvName(), "Beginning Digest");
        String dstFilePath = getDestinationFile(procedure, procedure.getInstructions().get(0));//Intermediate 'file'
        File dstDir = new File(dstFilePath);
        if (!dstDir.isDirectory()) { //Note: if dstDir is a non-existant
            dstDir = new File(dstFilePath.substring(0, dstFilePath.lastIndexOf("/")));
        }
        pm.addFolderPath("Destination Directory", dstDir.getAbsolutePath()+"/",configuration);//Convert to directory
        pm.addFolderPath("Input Directory", procedure.getMetadata().getGobiiFile().getSource()+"/", configuration);

        Path cropPath = Paths.get(rootDir + "crops/" + procedure.getMetadata().getGobiiCropType().toLowerCase());
        if (!(Files.exists(cropPath) &&
                Files.isDirectory(cropPath))) {
            logError("Digester", "Unknown Crop Type: " + procedure.getMetadata().getGobiiCropType());
            return;
        }
        GobiiCropConfig gobiiCropConfig;
        try {
            gobiiCropConfig = configuration.getCropConfig(procedure.getMetadata().getGobiiCropType());
        } catch (Exception e) {
            logError("Digester", "Unknown loading error", e);
            return;
        }
        if (gobiiCropConfig == null) {
            logError("Digester", "Unknown Crop Type: " + procedure.getMetadata().getGobiiCropType() + " in the Configuration File");
            return;
        }
        if (HDF5Interface.getPathToHDF5Files() == null)
            HDF5Interface.setPathToHDF5Files(cropPath.toString() + "/hdf5/");

        String errorPath = getLogName(procedure, procedure.getMetadata().getGobiiCropType());


        jobStatus.set(JobProgressStatusType.CV_PROGRESSSTATUS_VALIDATION.getCvName(), "Beginning Validation");
        // Instruction file Validation
        InstructionFileValidator instructionFileValidator = new InstructionFileValidator(procedure);
        instructionFileValidator.processInstructionFile();
        String validationStatus = instructionFileValidator.validateMarkerUpload();
        if (validationStatus != null) {
            Logger.logError("Marker validation failed.", validationStatus);
        }

        validationStatus = instructionFileValidator.validateSampleUpload();
        if (validationStatus != null) {
            Logger.logError("Sample validation failed.", validationStatus);
        }

        validationStatus = instructionFileValidator.validate();
        if (validationStatus != null) {
            Logger.logError("Validation failed.", validationStatus);
        }


        //TODO: HACK - Job's name is
        String jobName = getJobReadableIdentifier(procedure.getMetadata().getGobiiCropType(), procedure);
        pm.setUser(procedure.getMetadata().getContactEmail());

        String logDir = configuration.getFileSystemLog();
        String logFile = null;
        if (logDir != null) {
            String instructionName = new File(instructionFile).getName();
            instructionName = instructionName.substring(0, instructionName.lastIndexOf('.'));
            logFile = logDir + "/" + instructionName + ".log";
            String oldLogFile = Logger.getLogFilepath();
            Logger.logDebug("Error Logger", "Moving error log to " + logFile);
            Logger.setLogFilepath(logFile);
            Logger.logDebug("Error Logger", "Moved error log to " + logFile);
            FileSystemInterface.rmIfExist(oldLogFile);
        }

        SimpleTimer.start("FileRead");

        boolean qcCheck;//  zero.isQcCheck();
//		if (qcCheck) {//QC - Subsection #1 of 3
//			qcExtractInstruction = createQCExtractInstruction(zero, crop);
//		}



        jobStatus.set(JobProgressStatusType.CV_PROGRESSSTATUS_DIGEST.getCvName(), "Beginning file digest");
        //Pre-processing - make sure all files exist, find the cannonical dataset id
        for (GobiiLoaderInstruction inst : procedure.getInstructions()) {
            if (inst == null) {
                logError("Digester", "Missing or malformed instruction in " + instructionFile);
                continue;
            }
        }

        if (procedure.getMetadata().getGobiiFile() == null) {
            logError("Digester", "Instruction " + instructionFile + " has bad 'file' column");
        }
        GobiiFileType instructionFileType = procedure.getMetadata().getGobiiFile().getGobiiFileType();
        if (instructionFileType == null) {
            logError("Digester", "Instruction " + instructionFile + " has missing file format");
        }


        //Section - Processing
        Logger.logTrace("Digester", "Beginning List Processing");
        success = true;
        switch (procedure.getMetadata().getGobiiFile().getGobiiFileType()) { //All instructions should have the same file type, all file types go through CSVFileReader(V2)
            case HAPMAP:
                //INTENTIONAL FALLTHROUGH
            case VCF:
                //INTENTIONAL FALLTHROUGH
            case GENERIC:
                CSVFileReaderV2.parseInstructionFile(procedure, loaderScriptPath);
                break;
            default:
                System.err.println("Unable to deal with file type " + procedure.getMetadata().getGobiiFile().getGobiiFileType());
                break;
        }

        //Database Validation
        jobStatus.set(JobProgressStatusType.CV_PROGRESSSTATUS_VALIDATION.getCvName(), "Database Validation");
        databaseValidation(loaderInstructionMap, procedure.getMetadata(), gobiiCropConfig);

        boolean sendQc = false;

        qcCheck = procedure.getMetadata().isQcCheck();

        boolean isVCF = GobiiFileType.VCF.equals(procedure.getMetadata().getGobiiFile().getGobiiFileType());

        for (GobiiLoaderInstruction inst : procedure.getInstructions()) {

            //Section - Matrix Post-processing
            //Dataset is the first non-empty dataset type

            //Switch used for VCF transforms is currently a change in dataset type. See 'why is VCF a data type' GSD
            if (isVCF) {
                procedure.getMetadata().getDataset().setName("VCF");
            }


            String fromFile = getDestinationFile(procedure, inst);
            SequenceInPlaceTransform intermediateFile = new SequenceInPlaceTransform(fromFile, errorPath);
            if (procedure.getMetadata().getDatasetType().getName() != null
                    && inst.getTable().equals(VARIANT_CALL_TABNAME)) {
                errorPath = getLogName(procedure.getMetadata(), procedure.getMetadata().getGobiiCropType(), "Matrix_Processing"); //Temporary Error File Name

                if (DatasetOrientationType.SAMPLE_FAST.equals(procedure.getMetadata().getDatasetOrientationType())) {
                    //Rotate to marker fast before loading it - all data is marker fast in the system
                    File transposeDir = new File(new File(fromFile).getParentFile(), "transpose");
                    intermediateFile.transform(MobileTransform.getTransposeMatrix(transposeDir.getPath()));
                    isMarkerFast=false;
                }else{
                    isMarkerFast=true;
                }
            }
            jobStatus.set(JobProgressStatusType.CV_PROGRESSSTATUS_TRANSFORMATION.getCvName(), "Metadata Transformation");
            String instructionName = inst.getTable();
            loaderInstructionMap.put(instructionName, new File(getDestinationFile(procedure, inst)));
            loaderInstructionList.add(instructionName);//TODO Hack - for ordering
            if (LINKAGE_GROUP_TABNAME.equals(instructionName) || GERMPLASM_TABNAME.equals(instructionName) || GERMPLASM_PROP_TABNAME.equals(instructionName)) {
                success &= HelperFunctions.tryExec(loaderScriptPath + "LGduplicates.py -i " + getDestinationFile(procedure, inst));
            }
            if (MARKER_TABNAME.equals(instructionName)) {//Convert 'alts' into a jsonb array
                intermediateFile.transform(MobileTransform.PGArray);
            }

            intermediateFile.returnFile(); // replace intermediateFile where it came from

            //DONE WITH TRANSFORMS

            if (qcCheck) {//QC - Subsection #2 of 3
                qcExtractInstruction = createQCExtractInstruction(procedure.getMetadata(), procedure.getMetadata().getGobiiCropType());
                setQCExtractPaths(procedure.getMetadata());
                sendQc = success;
            }

        }


        //Validation logic before loading any metadata
        String baseConnectionString = getWebserviceConnectionString(gobiiCropConfig);

        GobiiClientContext gobiiClientContext = GobiiClientContext.getInstance(configuration, procedure.getMetadata().getGobiiCropType(), GobiiAutoLoginType.USER_RUN_AS);
        if (LineUtils.isNullOrEmpty(gobiiClientContext.getUserToken())) {
            Logger.logError("Digester", "Unable to log in with user " + GobiiAutoLoginType.USER_RUN_AS.toString());
            return;
        }
        String currentCropContextRoot = GobiiClientContext.getInstance(null, false).getCurrentCropContextRoot();

        GobiiUriFactory guf = new GobiiUriFactory(currentCropContextRoot);
        guf.resourceColl(RestResourceId.GOBII_CALLS);

        String user = configuration.getLdapUserForBackendProcs();
        String password = configuration.getLdapPasswordForBackendProcs();
        String directory = dstDir.getAbsolutePath();

        //Metadata Validation
        boolean reportedValidationFailures = false;
        if(LoaderGlobalConfigs.getValidation()) {
            DigestFileValidator digestFileValidator = new DigestFileValidator(directory, baseConnectionString,user, password);
            digestFileValidator.performValidation(gobiiCropConfig);
            //Call validations here, update 'success' to false with any call to ErrorLogger.logError()
            List<Path> pathList =
                    Files.list(Paths.get(directory))
                            .filter(Files::isRegularFile).filter(path -> String.valueOf(path.getFileName()).endsWith(".json")).collect(Collectors.toList());
            if (pathList.size() < 1) {
                Logger.logError("Validation","Unable to find validation checks");
            }
            ValidationError[] fileErrors = new ObjectMapper().readValue(pathList.get(0).toFile(), ValidationError[].class);
            boolean hasAnyFailedStatuses=false;
            for(ValidationError status : fileErrors){
                if(status.status.equalsIgnoreCase(ValidationConstants.FAILURE)){
                    hasAnyFailedStatuses=true;
                }
            }
            for (ValidationError status : fileErrors) {
                if (status.status.equalsIgnoreCase(ValidationConstants.FAILURE)) {
                    if(!reportedValidationFailures){//Lets only add this to the error log once
                        Logger.logError("Validation", "Validation failures");
                        reportedValidationFailures=true;
                    }
                    for (int i = 0; i < status.failures.size(); i++)
                        pm.addValidateTableElement(status.fileName, status.status, status.failures.get(i).reason, status.failures.get(i).columnName, status.failures.get(i).values);
                }
                if(status.status.equalsIgnoreCase(ValidationConstants.SUCCESS)){
                    //If any failed statii(statuses) exist, we should have this table, otherwise it should not exist
                    if(hasAnyFailedStatuses) {
                        pm.addValidateTableElement(status.fileName, status.status);
                    }
                }
            }
        }
        if (success && Logger.success()) {
            jobStatus.set(JobProgressStatusType.CV_PROGRESSSTATUS_METADATALOAD.getCvName(), "Loading Metadata");
            errorPath = getLogName(procedure.getMetadata(), procedure.getMetadata().getGobiiCropType(), "IFLs");
            String pathToIFL = loaderScriptPath + "postgres/gobii_ifl/gobii_ifl.py";
            String connectionString = " -c " + HelperFunctions.getPostgresConnectionString(gobiiCropConfig);

            //Load PostgreSQL
            boolean loadedData = false;
            for (String key : loaderInstructionList) {
                if (!VARIANT_CALL_TABNAME.equals(key)) {
                    String inputFile = " -i " + loaderInstructionMap.get(key);
                    String outputFile = " -o " + dstDir.getAbsolutePath() + "/"; //Output here is temporary files, needs terminal /

                    Logger.logInfo("Digester", "Running IFL: " + pathToIFL + " <conntection string> " + inputFile + outputFile);
                    //Lines affected returned by method call - THIS IS NOW IGNORED
                    HelperFunctions.tryExec(pathToIFL + connectionString + inputFile + outputFile + " -l", verbose ? dstDir.getAbsolutePath() + "/iflOut" : null, errorPath);

                    IFLLineCounts counts = calculateTableStats(pm, loaderInstructionMap, dstDir, key);

                    if (counts.loadedData == 0) {
                        Logger.logDebug("FileReader", "No data loaded for table " + key);
                    } else {
                        loadedData = true;
                    }
                    if (counts.invalidData > 0 && !isVariableLengthTable(key)) {
                        Logger.logWarning("FileReader", "Invalid data in table " + key);
                    } else {
                        if (LoaderGlobalConfigs.getDeleteIntermediateFiles()) {
                            deleteIFLFiles(dstDir, key);
                        }
                    }

                }


            }
            if (!loadedData) {
                Logger.logError("FileReader", "No new data was uploaded.");
            }
            //Load Monet/HDF5
            errorPath = getLogName(procedure.getMetadata(), procedure.getMetadata().getGobiiCropType(), "Matrix_Upload");
            String variantFilename = "DS" + dataSetId;
            File variantFile = loaderInstructionMap.get(VARIANT_CALL_TABNAME);

            if (variantFile != null && dataSetId == null) {
                logError("Digester", "Data Set ID is null for variant call");
            }
            if ((variantFile != null) && dataSetId != null) { //Create an HDF5 and a Monet
                jobStatus.set(JobProgressStatusType.CV_PROGRESSSTATUS_MATRIXLOAD.getCvName(), "Matrix Upload");
                boolean HDF5Success = HDF5Interface.createHDF5FromDataset(pm, procedure.getMetadata().getDatasetType().getName(),
                        configuration, dataSetId, procedure.getMetadata().getGobiiCropType(), errorPath, variantFilename, variantFile);
                rmIfExist(variantFile.getPath());
                success &= HDF5Success;
            }
            if (success && Logger.success()) {
                Logger.logInfo("Digester", "Successful Data Upload");
                if (sendQc) {
                    jobStatus.set(JobProgressStatusType.CV_PROGRESSSTATUS_QCPROCESSING.getCvName(), "Processing QC Job");
                    sendQCExtract(configuration, procedure.getMetadata().getGobiiCropType());
                } else {
                    jobStatus.set(JobProgressStatusType.CV_PROGRESSSTATUS_COMPLETED.getCvName(), "Successful Data Load");
                }

            } else { //endIf(success)
                Logger.logWarning("Digester", "Unsuccessful Upload");
                sendQc = false;//Files failed = bad.
                jobStatus.setError("Unsuccessfully Uploaded Files");
            }
        }//endif Digest section
        else {
            Logger.logWarning("Digester", "Aborted - Unsuccessfully Generated Files");
            jobStatus.setError("Unsuccessfully Generated Files - No Data Upload");
        }

        //Send Email
        finalizeProcessing(pm, configuration, mailInterface, instructionFile,
                procedure, procedure.getMetadata().getGobiiCropType(), jobName, logFile);


    }

    /**
     * Finalize processing step
     * *Include log files
     * *Send Email
     * *update status
     *
     * @param pm
     * @param configuration
     * @param mailInterface
     * @param instructionFile
     * @param crop
     * @param jobName
     * @param logFile
     * @throws Exception
     */
    private static void finalizeProcessing(ProcessMessage pm, ConfigSettings configuration, MailInterface mailInterface, String instructionFile, GobiiLoaderProcedure procedure, String crop, String jobName, String logFile) throws Exception {
            String instructionFilePath = HelperFunctions.completeInstruction(instructionFile, configuration.getProcessingPath(crop, GobiiFileProcessDir.LOADER_DONE));
                try {
            GobiiFileType loadType = procedure.getMetadata().getGobiiFile().getGobiiFileType();
            String loadTypeName = "";//No load type name if default
            if (loadType != GobiiFileType.GENERIC) loadTypeName = loadType.name();
            pm.addPath("Instruction File", instructionFilePath, configuration, false);
            pm.addPath("Error Log", logFile, configuration, false);
            pm.setBody(jobName, loadTypeName, SimpleTimer.stop("FileRead"), Logger.getFirstErrorReason(), Logger.success(), Logger.getAllErrorStringsHTML());
            mailInterface.send(pm);
        } catch (Exception e) {
            Logger.logError("MailInterface", "Error Sending Mail", e);
        }

    }

    private static void databaseValidation(Map<String, File> loaderInstructionMap, GobiiLoaderMetadata metadata, GobiiCropConfig gobiiCropConfig) {
        DatabaseQuerier querier = new DatabaseQuerier(gobiiCropConfig.getServer(ServerType.GOBII_PGSQL));

        //If we're doing a DS upload and there is no DS_Marker
        if (loaderInstructionMap.containsKey(VARIANT_CALL_TABNAME) && loaderInstructionMap.containsKey(DS_MARKER_TABNAME) && !loaderInstructionMap.containsKey(MARKER_TABNAME)) {
            querier.checkMarkerInPlatform(loaderInstructionMap.get(DS_MARKER_TABNAME), metadata.getPlatform().getId());
        }
        //If we're doing a DS upload and there is no DS_Sample
        if (loaderInstructionMap.containsKey(VARIANT_CALL_TABNAME) && loaderInstructionMap.containsKey(DS_SAMPLE_TABNAME) && !loaderInstructionMap.containsKey(SAMPLE_TABNAME)) {
            querier.checkDNARunInExperiment(loaderInstructionMap.get(DS_SAMPLE_TABNAME), metadata.getExperiment().getId());
        }

        if (loaderInstructionMap.containsKey(MARKER_TABNAME)) {
            querier.checkMarkerExistence(loaderInstructionMap.get(MARKER_TABNAME));
        }
        if (loaderInstructionMap.containsKey(GERMPLASM_TABNAME)) {
            querier.checkGermplasmTypeExistence(loaderInstructionMap.get(GERMPLASM_TABNAME));
            querier.checkGermplasmSpeciesExistence(loaderInstructionMap.get(GERMPLASM_TABNAME));
        }
        querier.close();
    }

    private static GobiiExtractorInstruction createQCExtractInstruction(GobiiLoaderMetadata metadata, String crop) {
        GobiiExtractorInstruction gobiiExtractorInstruction;
        Logger.logInfo("Digester", "qcCheck detected");
        Logger.logInfo("Digester", "Entering into the QC Subsection #1 of 3...");
        gobiiExtractorInstruction = new GobiiExtractorInstruction();
        gobiiExtractorInstruction.setContactEmail(metadata.getContactEmail());
        gobiiExtractorInstruction.setContactId(metadata.getContactId());
        gobiiExtractorInstruction.setGobiiCropType(crop);
        gobiiExtractorInstruction.getMapsetIds().add(metadata.getMapset().getId());
        gobiiExtractorInstruction.setQcCheck(true);
        Logger.logInfo("Digester", "Done with the QC Subsection #1 of 3!");
        return gobiiExtractorInstruction;
    }

    private static void setQCExtractPaths(GobiiLoaderMetadata metadata) {
        Logger.logInfo("Digester", "Entering into the QC Subsection #2 of 3...");
        GobiiDataSetExtract gobiiDataSetExtract = new GobiiDataSetExtract();
        gobiiDataSetExtract.setAccolate(false);  // It is unused/unsupported at the moment
        gobiiDataSetExtract.setDataSet(metadata.getDataset());
        gobiiDataSetExtract.setGobiiDatasetType(metadata.getDatasetType());

        // According to Liz, the Gobii extract filter type is always "WHOLE_DATASET" for any QC job
        gobiiDataSetExtract.setGobiiExtractFilterType(GobiiExtractFilterType.WHOLE_DATASET);
        gobiiDataSetExtract.setGobiiFileType(GobiiFileType.HAPMAP);
        // It is going to be set by the Gobii web services
        gobiiDataSetExtract.setGobiiJobStatus(null);
        qcExtractInstruction.getDataSetExtracts().add(gobiiDataSetExtract);
        Logger.logInfo("Digester", "Done with the QC Subsection #2 of 3!");
    }

    private static void sendQCExtract(ConfigSettings configuration, String crop) throws Exception {
        Logger.logInfo("Digester", "Entering into the QC Subsection #3 of 3...");
        ExtractorInstructionFilesDTO extractorInstructionFilesDTOToSend = new ExtractorInstructionFilesDTO();
        extractorInstructionFilesDTOToSend.getGobiiExtractorInstructions().add(qcExtractInstruction);
        extractorInstructionFilesDTOToSend.setInstructionFileName("extractor_" + DateUtils.makeDateIdString());
        GobiiClientContext gobiiClientContext = GobiiClientContext.getInstance(configuration, crop, GobiiAutoLoginType.USER_RUN_AS);
        if (LineUtils.isNullOrEmpty(gobiiClientContext.getUserToken())) {
            Logger.logError("Digester", "Unable to log in with user " + GobiiAutoLoginType.USER_RUN_AS.toString());
            return;
        }
        String currentCropContextRoot = GobiiClientContext.getInstance(null, false).getCurrentCropContextRoot();
        gobiiUriFactory = new GobiiUriFactory(currentCropContextRoot);
        PayloadEnvelope<ExtractorInstructionFilesDTO> payloadEnvelope = new PayloadEnvelope<>(extractorInstructionFilesDTOToSend, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<ExtractorInstructionFilesDTO, ExtractorInstructionFilesDTO> gobiiEnvelopeRestResourceForPost = new GobiiEnvelopeRestResource<>(gobiiUriFactory
                .resourceColl(RestResourceId.GOBII_FILE_EXTRACTOR_INSTRUCTIONS));
        PayloadEnvelope<ExtractorInstructionFilesDTO> extractorInstructionFileDTOResponseEnvelope = gobiiEnvelopeRestResourceForPost.post(ExtractorInstructionFilesDTO.class,
                payloadEnvelope);

        if (extractorInstructionFileDTOResponseEnvelope != null) {

            Header header = extractorInstructionFileDTOResponseEnvelope.getHeader();
            if (header.getStatus().isSucceeded()) {
                Logger.logInfo("Digester", "Extractor Request Sent");

            } else {

                String messages = extractorInstructionFileDTOResponseEnvelope.getHeader().getStatus().messages();

                for (HeaderStatusMessage currentStatusMesage : header.getStatus().getStatusMessages()) {
                    messages += (currentStatusMesage.getMessage()) + "; ";
                }

                Logger.logError("Digester", "Error sending extract request: " + messages);

            }
        } else {
            Logger.logInfo("Digester", "Error Sending Extractor Request");
        }
        Logger.logInfo("Digester", "Done with the QC Subsection #3 of 3!");
    }

    /**
     * Read ppd and nodups files to determine their length, and add the row corresponding to the key to the digester message status.
     * Assumes IFL was run with output of dstDir on key in instructionMap.
     *
     * @param pm                   ProcessMessage to record data to
     * @param loaderInstructionMap Map of key/location of loader instructions
     * @param dstDir               Destination directory for IFL call run on key's table
     * @param key                  Key in loaderInstructionMap
     * @return
     */
    private static IFLLineCounts calculateTableStats(ProcessMessage pm, Map<String, File> loaderInstructionMap, File dstDir, String key) {

        String ppdFile = new File(dstDir, "ppd_digest." + key).getAbsolutePath();
        //If there is a deduplicated PPD file, use it instead of the ppd file
        String ddpPpdFile = new File(dstDir, "ddp_ppd_digest." + key).getAbsolutePath();
        if (new File(ddpPpdFile).exists()) {
            ppdFile = ddpPpdFile;
        }

        String noDupsFile = new File(dstDir, "nodups_ppd_digest." + key).getAbsolutePath();
        //If there is a deduplicated nodups file, use it instead of the nodups file
        String ddpNoDupsFile = new File(dstDir, "nodups_ddp_ppd_digest." + key).getAbsolutePath();
        if (new File(ddpNoDupsFile).exists()) {
            noDupsFile = ddpNoDupsFile;
        }


        //Default to 'we had an error'
        String totalLinesVal, linesLoadedVal, existingLinesVal, invalidLinesVal;
        totalLinesVal = linesLoadedVal = existingLinesVal = invalidLinesVal = "error";

        //-1 lines for header
        int totalLines = FileSystemInterface.lineCount(loaderInstructionMap.get(key).getAbsolutePath()) - 1;
        int ppdLines = FileSystemInterface.lineCount(ppdFile) - 1;
        int noDupsLines = FileSystemInterface.lineCount(noDupsFile) - 1;
        //They're -1 if the file is missing.
        if (totalLines < 0) totalLines = 0;
        if (ppdLines < 0) ppdLines = 0;
        if (noDupsLines < 0) noDupsLines = 0;

        boolean noDupsFileExists = new File(noDupsFile).exists();
        if (!noDupsFileExists) noDupsLines = ppdLines;
        //Begin Business Logic Zone
        int loadedLines = noDupsLines;
        int existingLines = ppdLines - noDupsLines;
        int invalidLines = totalLines - ppdLines;
        //End Business Logic Zone - regular logic can resume

        //If total lines/file lines less than 0, something's wrong. Also if total lines is < changed, something's wrong.


        if (isVariableLengthTable(key)) {
            totalLinesVal = totalLines + "";
            linesLoadedVal = loadedLines + "";
            //Existing and Invalid may be absolutely random numbers in EAV JSON objects
            //Also, loaded may be waaaay above total, this is normal. So lets not report these two fields at all
            existingLinesVal = "";
            invalidLinesVal = "";

            //We can still warn people if no lines were loaded
            if (loadedLines == 0) {
                linesLoadedVal = "<b style=\"background-color:yellow\">" + loadedLines + "</b>";
            }
        } else {
            totalLinesVal = totalLines + "";
            linesLoadedVal = loadedLines + "";//Header
            existingLinesVal = existingLines + "";
            invalidLinesVal = invalidLines + "";
            if (!noDupsFileExists) {
                existingLinesVal = "";
            }
            if (invalidLines != 0) {
                invalidLinesVal = "<b style=\"background-color:red\">" + invalidLines + "</b>";
            }
            if (loadedLines == 0) {
                linesLoadedVal = "<b style=\"background-color:yellow\">" + loadedLines + "</b>";
            }
        }
        IFLLineCounts counts = new IFLLineCounts();
        counts.loadedData = loadedLines;
        counts.existingData = existingLines;
        counts.invalidData = invalidLines;
        pm.addEntry(key, totalLinesVal, linesLoadedVal, existingLinesVal, invalidLinesVal);
        return counts;
    }

    /**
     * Returns a human readable name for the job.
     *
     * @param cropName Name of the crop being run
     * @return a human readable name for the job
     */
    private static String getJobReadableIdentifier(String cropName, GobiiLoaderProcedure procedure) {
        cropName = cropName.charAt(0) + cropName.substring(1).toLowerCase();// MAIZE -> Maize
        String jobName = "[GOBII - Loader]: " + cropName + " - digest of \"" + getSourceFileName(procedure.getMetadata().getGobiiFile()) + "\"";
        return jobName;
    }

    /**
     * Converts the File input into the FIRST of the source files.
     *
     * @param file Reference to Instruction's File object.
     * @return String representation of first of source files
     */
    public static String getSourceFileName(GobiiFile file) {
        String source = file.getSource();
        File sourceFolder = new File(source);
        File[] f = sourceFolder.listFiles();
        if (f.length != 0) source = f[0].getName();
        else {
            source = sourceFolder.getName();//Otherwise we get full paths in source.
        }
        return source;
    }

    /**
     * Generates a log file location given a crop name, crop type, and process ID. (Given by the process calling this method).
     * <p>
     * Currently works by placing logs in the intermediate file directory.
     *
     * @return The logfile location for this process
     */
    private static String getLogName(GobiiLoaderProcedure procedure, String cropName) {
        String destination = procedure.getMetadata().getGobiiFile().getDestination();
        String table = procedure.getInstructions().get(0).getTable();
        return destination + "/" + cropName + "_Table-" + table + ".log";
    }

    /**
     * Generates a log file location given a crop name, crop type, and process ID. (Given by the process calling this method).
     * <p>
     * Currently works by placing logs in the intermediate file directory.
     *
     * @return The logfile location for this process
     */
    private static String getLogName(GobiiLoaderMetadata metadata, String cropName, String process) {
        String destination = metadata.getGobiiFile().getDestination();
        return destination + "/" + cropName + "_Process-" + process + ".log";
    }

    /**
     * Determine crop type by looking at the intruction file's location for the name of a crop.
     *
     * @param instructionFile
     * @return GobiiCropType
     */
    private static String divineCrop(String instructionFile) {
        String upper = instructionFile.toUpperCase();
        String from = "/CROPS/";
        int fromIndex = upper.indexOf(from) + from.length();
        String crop = upper.substring(fromIndex, upper.indexOf('/', fromIndex));
        return crop;
    }

    /**
     * Updates Postgresql through the webservices to update the DataSet's monetDB and HDF5File references.
     *
     * @param config         Configuration settings, used to determine connections
     * @param cropName       Name of the crop
     * @param dataSetId      Data set to update
     * @param monetTableName Name of the table in the monetDB database for this dataset.
     * @param hdfFileName    Name of the HDF5 file for this dataset (Note, these should be obvious)
     */
    public static void updateValues(ConfigSettings config, String cropName, Integer dataSetId, String monetTableName, String hdfFileName) {
        try {
            // set up authentication and so forth
            // you'll need to get the current from the instruction file
            GobiiClientContext context = GobiiClientContext.getInstance(config, cropName, GobiiAutoLoginType.USER_RUN_AS);

            if (LineUtils.isNullOrEmpty(context.getUserToken())) {
                logError("Digester", "Unable to login with user " + GobiiAutoLoginType.USER_RUN_AS.toString());
                return;
            }

            String currentCropContextRoot = context.getInstance(null, false).getCurrentCropContextRoot();
            GobiiUriFactory gobiiUriFactory = new GobiiUriFactory(currentCropContextRoot);

            RestUri projectsUri = gobiiUriFactory
                    .resourceByUriIdParam(RestResourceId.GOBII_DATASETS);
            projectsUri.setParamValue("id", dataSetId.toString());
            GobiiEnvelopeRestResource<DataSetDTO, DataSetDTO> gobiiEnvelopeRestResourceForDatasets = new GobiiEnvelopeRestResource<>(projectsUri);
            PayloadEnvelope<DataSetDTO> resultEnvelope = gobiiEnvelopeRestResourceForDatasets
                    .get(DataSetDTO.class);

            DataSetDTO dataSetResponse;
            if (!resultEnvelope.getHeader().getStatus().isSucceeded()) {
                System.out.println();
                logError("Digester", "Data set response response errors");
                for (HeaderStatusMessage currentStatusMesage : resultEnvelope.getHeader().getStatus().getStatusMessages()) {
                    logError("HeaderError", currentStatusMesage.getMessage());
                }
                return;
            } else {
                dataSetResponse = resultEnvelope.getPayload().getData().get(0);
            }

            dataSetResponse.setDataTable(monetTableName);
            dataSetResponse.setDataFile(hdfFileName);

            resultEnvelope = gobiiEnvelopeRestResourceForDatasets
                    .put(DataSetDTO.class, new PayloadEnvelope<>(dataSetResponse, GobiiProcessType.UPDATE));


            //dataSetResponse = dtoProcessor.process(dataSetResponse);
            // if you didn't succeed, do not pass go, but do log errors to your log file
            if (!resultEnvelope.getHeader().getStatus().isSucceeded()) {
                logError("Digester", "Data set response response errors");
                for (HeaderStatusMessage currentStatusMesage : resultEnvelope.getHeader().getStatus().getStatusMessages()) {
                    logError("HeaderError", currentStatusMesage.getMessage());
                }
                return;
            }
        } catch (Exception e) {
            logError("Digester", "Exception while referencing data sets in Postgresql", e);
            return;
        }
    }

    private static String getJDBCConnectionString(GobiiCropConfig config) {
        return HelperFunctions.getJdbcConnectionString(config);
    }

    /**
     * Given a string key, determine if the table is one-to-one with relation to the input file size.
     * If not, several metrics become meaningless.
     *
     * @param tableKey
     * @return true if the table will have different PPD rows than input rows
     */
    private static boolean isVariableLengthTable(String tableKey) {
        return tableKey.contains("_prop");
    }

    /**
     * Deletes all files in directory that contain '.tablename' suffix
     *
     * @param directory
     * @param tableName
     */
    private static void deleteIFLFiles(File directory, String tableName) {
        File[] fileList = directory.listFiles();
        if (fileList == null) return;
        for (File f : fileList) {
            if (f.getName().endsWith("." + tableName)) {
                rmIfExist(f);
            }
        }
    }
}

class IFLLineCounts {
    int loadedData, existingData, invalidData;
}
