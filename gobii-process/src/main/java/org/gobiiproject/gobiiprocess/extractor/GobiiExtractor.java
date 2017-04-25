package org.gobiiproject.gobiiprocess.extractor;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.cli.*;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.UriFactory;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiiclient.core.common.ClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiimodel.dto.instructions.GobiiFilePropNameId;
import org.gobiiproject.gobiimodel.headerlesscontainer.QCInstructionsDTO;
import org.gobiiproject.gobiimodel.types.*;
import org.gobiiproject.gobiimodel.utils.DateUtils;
import org.gobiiproject.gobiimodel.utils.email.MailInterface;
import org.gobiiproject.gobiimodel.utils.email.ProcessMessage;
import org.gobiiproject.gobiimodel.utils.HelperFunctions;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;
import org.gobiiproject.gobiiprocess.extractor.flapjack.FlapjackTransformer;
import org.gobiiproject.gobiiprocess.extractor.hapmap.HapmapTransformer;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.CropConfig;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiDataSetExtract;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiExtractorInstruction;
import org.gobiiproject.gobiimodel.utils.FileSystemInterface;

import static org.gobiiproject.gobiimodel.utils.FileSystemInterface.mv;
import static org.gobiiproject.gobiimodel.utils.FileSystemInterface.rmIfExist;
import static org.gobiiproject.gobiimodel.utils.HelperFunctions.*;
import static org.gobiiproject.gobiimodel.utils.error.ErrorLogger.*;

public class GobiiExtractor {
	//Paths
	private static String  pathToHDF5, propertiesFile,pathToHDF5Files;
	private static UriFactory uriFactory;
	private static String lastErrorFile=null;
	private static String errorLogOverride;
	private static boolean verbose;
	private static String rootDir="../";
	private static String markerListOverrideLocation=null;

	public static void main(String[] args) throws Exception {

		Options o = new Options()
         		.addOption("v", "verbose", false, "Verbose output")
         		.addOption("e", "errlog", true, "Error log override location")
         		.addOption("r", "rootDir", true, "Fully qualified path to gobii root directory")
         		.addOption("c","config",true,"Fully qualified path to gobii configuration file")
         		.addOption("h", "hdfFiles", true, "Fully qualified path to hdf files")
				.addOption("m", "markerList", true, "Fully qualified path to marker list files - (Debugging, forces marker list extract)");
        
		CommandLineParser parser = new DefaultParser();
		ProcessMessage pm = new ProcessMessage();
		
        try{
            CommandLine cli = parser.parse( o, args );
            if(cli.hasOption("verbose")) verbose=true;
            if(cli.hasOption("errLog")) errorLogOverride = cli.getOptionValue("errLog");
            if(cli.hasOption("config")) propertiesFile = cli.getOptionValue("config");
            if(cli.hasOption("rootDir")){
                rootDir = cli.getOptionValue("rootDir");
            }
            if(cli.hasOption("hdfFiles")) pathToHDF5Files = cli.getOptionValue("hdfFiles");
            if(cli.hasOption("markerList")) markerListOverrideLocation=cli.getOptionValue("markerList");
            args=cli.getArgs();//Remaining args passed through

        }catch(org.apache.commons.cli.ParseException exp ) {
            new HelpFormatter().printHelp("java -jar Extractor.jar ","Also accepts input file directly after arguments\n"
                    + "Example: java -jar Extractor.jar -c /home/jdl232/customConfig.properties -v /home/jdl232/testLoad.json",o,null,true);

            System.exit(2);
        }
		
     	String extractorScriptPath=rootDir+"extractors/";
    	pathToHDF5=extractorScriptPath+"hdf5/bin/";
    	
    	if(propertiesFile==null)propertiesFile=rootDir+"config/gobii-web.xml";
    	
		boolean success=true;
		ConfigSettings configuration=null;
		try {
			configuration = new ConfigSettings(propertiesFile);
		} catch (Exception e) {
			logError("Extractor","Failure to read Configurations",e);
			return;
		}

		MailInterface mailInterface=new MailInterface(configuration);
		
		String instructionFile=null;
		if(args.length==0 ||args[0].equals("")){
			Scanner s=new Scanner(System.in);
			System.out.println("Enter Extractor Instruction File Location:");
			instructionFile=s.nextLine();
			if(instructionFile.equals("")) instructionFile="D:/GOBII_Dev/Test/2017_04_19_23_34_38.json";
		    s.close();
		}
		else{
			instructionFile=args[0];
		}

		pm.addPath("instruction file",new File(instructionFile).getAbsolutePath());
		ErrorLogger.logInfo("Extractor","Beginning read of "+instructionFile);
		List<GobiiExtractorInstruction> list= parseExtractorInstructionFile(instructionFile);
		if(list==null){
			ErrorLogger.logError("Extractor","No instruction for file "+instructionFile);
			return;
		}

		String logDir=configuration.getFileSystemLog();
		if(logDir!=null) {
			String instructionName=new File(instructionFile).getName();
			instructionName=instructionName.substring(0,instructionName.lastIndexOf('.'));
			String logFile=logDir+"/"+instructionName+".log";
			ErrorLogger.logDebug("Error Logger","Moving error log to "+logFile);
			ErrorLogger.setLogFilepath(logFile);
			ErrorLogger.logDebug("Error Logger","Moved error log to "+logFile);
		}


		for(GobiiExtractorInstruction inst:list){
			String crop = inst.getGobiiCropType();
			if(crop==null) crop=divineCrop(instructionFile);
			Path cropPath = Paths.get(rootDir+"crops/"+crop.toLowerCase());
			if (!(Files.exists(cropPath) &&
				  Files.isDirectory(cropPath))) {
				ErrorLogger.logError("Extractor","Unknown Crop Type: "+crop);
				return;
			}
			CropConfig cropConfig= null;
			try {
				cropConfig = configuration.getCropConfig(crop);
			} catch (Exception e) {
				logError("Extractor","Unknown exception getting crop",e);
				return;
			}
			if (cropConfig == null) {
				logError("Extractor","Unknown Crop Type: "+crop+" in the Configuration File");
				return;
			}
			if(pathToHDF5Files==null)pathToHDF5Files=cropPath.toString()+"/hdf5/";


			Integer mapId;
			List<Integer> mapIds=inst.getMapsetIds();
			if(mapIds.isEmpty() || mapIds.get(0)==null){
				mapId=null;
			}else if(mapIds.size()>1){
				logError("Extraction Instruction","Too many map IDs for extractor. Expected one, recieved "+mapIds.size());
				mapId=null;
			}
			else{
				mapId=mapIds.get(0);
			}
			
			for(GobiiDataSetExtract extract:inst.getDataSetExtracts()){
			
				//TODO: if extractor JSON structures is going to be replica of Loader JSON
//				pm.addIdentifier("PI",extract.getPrincipleInvestigator());
//				pm.addIdentifier("Project",extract.getProject());
//				pm.addIdentifier("Experiment",extract.getExperiment());
//				pm.addIdentifier("Dataset", extract.getDataSet());
//				pm.addIdentifier("Platform", Integer.toString(extract.getPlatformId()));
				
				//TODO: if extractor email notification table should look exactly like confluene idea (http://cbsugobii05.tc.cornell.edu:6084/display/RSD/Extract+Email+Notification)
				pm.addEntity("PI", extract.getPrincipleInvestigator().getName());
				pm.addEntity("Project", extract.getProject().getName());
//				pm.addEntity("Experiment",extract.getExperiment().getName());
				pm.addEntity("Dataset", extract.getDataSet().getName());
				pm.addEntity("Platform", Integer.toString(extract.getPlatformIds().get(0)));
				
				String jobName = getJobName(crop, extract);
				String jobUser=inst.getContactEmail();
				pm.setUser(jobUser);
				
				GobiiExtractFilterType filterType = extract.getGobiiExtractFilterType();
                if(filterType==null) filterType=GobiiExtractFilterType.WHOLE_DATASET;
                if(markerListOverrideLocation!=null)filterType=GobiiExtractFilterType.BY_MARKER;
				String extractDir=extract.getExtractDestinationDirectory()+"/";
				tryExec("rm -f "+extractDir+"*");

				String markerFile=extractDir+"marker.file";
				String extendedMarkerFile=markerFile+".ext";
				String mapsetFile=extractDir+"mapset.file";
				String markerPosFile=markerFile+".pos";
				String sampleFile=extractDir+"sample.file";
				String projectFile=extractDir+"summary.file";
				String chrLengthFile = markerFile+".chr";
				Path mdePath = FileSystems.getDefault().getPath(extractorScriptPath+"postgres/gobii_mde/gobii_mde.py");
				if (!mdePath.toFile().isFile()) {
					ErrorLogger.logDebug("Extractor", mdePath+" does not exist!");
					return;
				}

				String gobiiMDE;//Output of switch

				//Common terms
				String platformTerm,mapIdTerm,markerListLocation,sampleListLocation;
				String samplePosFile="";//Location of sample position indices (see markerList for an example
				platformTerm=mapIdTerm=markerListLocation=sampleListLocation="";
				List<Integer> platforms=extract.getPlatformIds();
				if(platforms!=null && !platforms.isEmpty()){
					platformTerm=" --platformList " + commaFormat(platforms);
				}
				if(mapId!=null) {
					mapIdTerm=" -D "+mapId;
				}
				switch(filterType){
					case WHOLE_DATASET:
						
						pm.addPath("Sample file", new File(sampleFile).getAbsolutePath());
						pm.addPath("Marker file", new File(markerFile).getAbsolutePath());
						pm.addPath("Mapset File", new File(mapsetFile).getAbsolutePath());
						
						gobiiMDE = "python "+ mdePath+
								" -c " + HelperFunctions.getPostgresConnectionString(cropConfig) +
								" --extractByDataset"+
								" -m " + markerFile +
								" -b " + mapsetFile +
								" -s " + sampleFile +
								" -p " + projectFile +
								(mapId==null?"":(" -D "+mapId))+
								" -d " + extract.getDataSet().getId() +
								" -l -v ";
						break;
					case BY_MARKER:
						
						pm.addPath("Sample file", new File(sampleFile).getAbsolutePath());
						pm.addPath("Marker file", new File(markerFile).getAbsolutePath());
						pm.addPath("Mapset File", new File(mapsetFile).getAbsolutePath());
						
						//List takes extra work, as it might be a <List> or a <File>
						//Create a file out of the List if non-null, else use the <File>
						List<String> markerList=extract.getMarkerList();
						if(markerList!=null && !markerList.isEmpty()){
							markerListLocation=" -X "+createTempFileForMarkerList(extractDir,markerList);
						}
						else if(extract.getListFileName()!=null){
							markerListLocation=" -X "+extractDir+extract.getListFileName();
						}
						//else if file is null and list is empty or null - > no term

						if(markerListOverrideLocation!=null) markerListLocation=" -x "+ markerListOverrideLocation;

						//Actually call the thing
						gobiiMDE = "python "+ mdePath+
								" -c " + HelperFunctions.getPostgresConnectionString(cropConfig) +
								" --extractByMarkers"+
								" -m " + markerFile +
								" -b " + mapsetFile +
								" -s " + sampleFile +
								" -p " + projectFile +
                                markerListLocation +
								" --datasetType " + extract.getGobiiDatasetType().getId() +
								mapIdTerm +
								platformTerm +
								" -l -v ";
						break;
					case BY_SAMPLE:
						//TODO:Only one file?? To be confirmed with confluence extractor mail notification page 
						pm.addPath("Sample file", new File(sampleFile).getAbsolutePath());
						
						//List takes extra work, as it might be a <List> or a <File>
						//Create a file out of the List if non-null, else use the <File>
						List<String> sampleList=extract.getSampleList();
						if(sampleList!=null && !sampleList.isEmpty()){
							sampleListLocation=" -Y "+createTempFileForMarkerList(extractDir,sampleList);
						}
						else if(extract.getListFileName()!=null){
							sampleListLocation=" -Y "+extractDir+extract.getListFileName();
						}

						GobiiSampleListType type = extract.getGobiiSampleListType();
						String sampleListTypeTerm=(type==null)?"":"--sampleType "+getNumericType(type);

						String PITerm,projectTerm;
						PITerm=projectTerm="";
						GobiiFilePropNameId PI=extract.getPrincipleInvestigator();
						GobiiFilePropNameId project=extract.getProject();
						if(PI!=null){
							PITerm=" --piId "+PI.getId();
						}
						if(project!=null){
							projectTerm=" --projectId "+project.getId();
						}

						gobiiMDE = "python "+ mdePath+
								" -c " + HelperFunctions.getPostgresConnectionString(cropConfig) +
								" --extractBySamples"+
								" -m " + markerFile +
								" -b " + mapsetFile +
								" -s " + sampleFile +
								" -p " + projectFile +
								sampleListLocation +
								sampleListTypeTerm +
								PITerm +
								projectTerm+
								" --datasetType " + extract.getGobiiDatasetType() +
								mapIdTerm +
								platformTerm +
								" -l -v ";
						break;
					default:
						gobiiMDE="";
						ErrorLogger.logError("GobiiExtractor", "UnknownFilterType " + filterType);
						break;
				}
				samplePosFile=sampleFile+".pos";
				String errorFile=getLogName(extract,cropConfig,extract.getDataSet().getId());
				ErrorLogger.logInfo("Extractor","Executing MDEs");
				tryExec(gobiiMDE, extractDir+"mdeOut", errorFile);
				Integer dataSetId=extract.getDataSet().getId();



				//HDF5
				String tempFolder=extractDir;
				GobiiFileType fileType = extract.getGobiiFileType();
				boolean markerFast=(fileType == GobiiFileType.HAPMAP);

				String genoFile;
				switch(filterType) {
					case WHOLE_DATASET:
						genoFile = getHDF5Genotype(markerFast, errorFile, dataSetId, tempFolder);
						break;
					case BY_MARKER:
						genoFile = getHDF5GenoFromMarkerList(markerFast, errorFile, tempFolder, markerPosFile);
						break;
					case BY_SAMPLE:
						genoFile = getHDF5GenoFromSampleList(markerFast,errorFile,tempFolder,markerPosFile,samplePosFile);
						break;
					default:
						genoFile="";
						ErrorLogger.logError("GobiiExtractor", "UnknownFilterType " + filterType);
						break;
				}
				
				pm.addPath("Genotype file", new File(genoFile).getAbsolutePath());

				// Adding "/" back to the bi-allelic data made from HDF5
				if (extract.getGobiiDatasetType() != null) {
					if (extract.getGobiiDatasetType().getName().toLowerCase().equals("ssr_allele_size")) {
						ErrorLogger.logInfo("Extractor","Adding slashes to bi allelic data in " + genoFile);
						if (addSlashesToBiAllelicData(genoFile, extractDir, extract)) {
							ErrorLogger.logInfo("Extractor","Added slashes to all the bi-allelic data in " + genoFile);
						} else {
							ErrorLogger.logError("Extractor","Not added slashes to all the bi-allelic data in " + genoFile);
						}
					}
				}

				switch(extract.getGobiiFileType()){
					case FLAPJACK:
						String genoOutFile=extractDir+"Dataset.genotype";
						String mapOutFile=extractDir+"Dataset.map";
						lastErrorFile=errorFile;
						
						pm.addPath("FlapJack Genotype file", new File(genoOutFile).getAbsolutePath());
						pm.addPath("FlapJack Map file", new File(mapOutFile).getAbsolutePath());
						
						//Always regenerate requests - may have different parameters
						boolean extended=HelperFunctions.checkFileExistance(extendedMarkerFile);
						//TODO: @Siva write an object for each file extraction and make the ProcessMessage body 
						ErrorLogger.logDebug("GobiiExtractor","Executing FlapJack Map file Generation");
						success &= FlapjackTransformer.generateMapFile(extended?extendedMarkerFile:markerFile, sampleFile, chrLengthFile, dataSetId, tempFolder, mapOutFile, errorFile,extended);
						ErrorLogger.logDebug("GobiiExtractor","Executing FlapJack Genotype file Generation");
						success &= FlapjackTransformer.generateGenotypeFile(markerFile, sampleFile, genoFile, dataSetId, tempFolder, genoOutFile,errorFile);
						//TODO: @Siva create incidents to calculate file size (may be "du -sh"), line count(FileSystemInterface.lineCount)..?? anything more.??
						pm.setBody(jobName,ErrorLogger.getFirstErrorReason(),ErrorLogger.success(),ErrorLogger.getAllErrorStringsHTML());
						mailInterface.send(pm);
						HelperFunctions.completeInstruction(instructionFile,configuration.getProcessingPath(crop, GobiiFileProcessDir.EXTRACTOR_DONE));
						break;
					case HAPMAP:
						String hapmapOutFile = extractDir+"Dataset.hmp.txt";
						
						pm.addPath("Hapmap file", new File(hapmapOutFile).getAbsolutePath());
						
						HapmapTransformer hapmapTransformer = new HapmapTransformer();
						ErrorLogger.logDebug("GobiiExtractor","Executing Hapmap Generation");
						success &= hapmapTransformer.generateFile(markerFile, sampleFile, extendedMarkerFile, genoFile, hapmapOutFile, errorFile);
						//TODO: @Siva create incidents to calculate file size (may be "du -sh"), line count(FileSystemInterface.lineCount)..?? anything more.??
						pm.setBody(jobName,ErrorLogger.getFirstErrorReason(),ErrorLogger.success(),ErrorLogger.getAllErrorStringsHTML());
						mailInterface.send(pm);
						HelperFunctions.completeInstruction(instructionFile,configuration.getProcessingPath(crop, GobiiFileProcessDir.EXTRACTOR_DONE));
						break;
                    case META_DATA:
                    	//What this actually means. Give us some test cases.
                        HelperFunctions.sendEmail(extract.getDataSet().getName()+" Metadata Extract",extractDir,success&&ErrorLogger.success(),errorFile,configuration,inst.getContactEmail());
                        HelperFunctions.completeInstruction(instructionFile,configuration.getProcessingPath(crop, GobiiFileProcessDir.EXTRACTOR_DONE));
                        break;
                    default:
						ErrorLogger.logError("Extractor","Unknown Extract Type "+extract.getGobiiFileType());
						HelperFunctions.sendEmail(extract.getDataSet().getName()+" "+extract.getGobiiFileType()+" Extract",null,false,errorFile,configuration,inst.getContactEmail());
				}
				rmIfExist(genoFile);
				rmIfExist(chrLengthFile);
				rmIfExist(markerPosFile);
				rmIfExist(extendedMarkerFile);
				ErrorLogger.logDebug("Extractor","DataSet "+dataSetId+" Created");

				//QC - Subsection #1 of 1
				if (inst.isQcCheck()) {
					ErrorLogger.logInfo("Extractor", "qcCheck detected");
					ErrorLogger.logInfo("Extractor","Entering into the QC Subsection #1 of 1...");
					QCInstructionsDTO qcInstructionsDTOToSend = new QCInstructionsDTO();
					qcInstructionsDTOToSend.setContactId(inst.getContactId());
					qcInstructionsDTOToSend.setDataFileDirectory(configuration.getProcessingPath(crop, GobiiFileProcessDir.QC_NOTIFICATIONS));
					qcInstructionsDTOToSend.setDataFileName(new StringBuilder("qc_").append(DateUtils.makeDateIdString()).toString());
					qcInstructionsDTOToSend.setDatasetId(dataSetId);
					qcInstructionsDTOToSend.setGobiiJobStatus(GobiiJobStatus.COMPLETED);
					qcInstructionsDTOToSend.setQualityFileName("Report.xls");
					PayloadEnvelope<QCInstructionsDTO> payloadEnvelope = new PayloadEnvelope<>(qcInstructionsDTOToSend, GobiiProcessType.CREATE);
					ClientContext clientContext = ClientContext.getInstance(configuration, crop,GobiiAutoLoginType.USER_RUN_AS);
					if( LineUtils.isNullOrEmpty(clientContext.getUserToken())) {
						ErrorLogger.logError("Digester","Unable to log in with user: " + GobiiAutoLoginType.USER_RUN_AS.toString());
						return;
					}
					String currentCropContextRoot = clientContext.getInstance(null, false).getCurrentCropContextRoot();
					uriFactory = new UriFactory(currentCropContextRoot);
					GobiiEnvelopeRestResource<QCInstructionsDTO> restResourceForPost = new GobiiEnvelopeRestResource<QCInstructionsDTO>(uriFactory.resourceColl(ServiceRequestId.URL_FILE_QC_INSTRUCTIONS));
					PayloadEnvelope<QCInstructionsDTO> qcInstructionFileDTOResponseEnvelope = restResourceForPost.post(QCInstructionsDTO.class,
							                                                                                           payloadEnvelope);
					if (qcInstructionFileDTOResponseEnvelope != null) {
						ErrorLogger.logInfo("Extractor","QC Instructions Request Sent");
					}
					else {
						ErrorLogger.logError("Extractor","Error Sending QC Instructions Request");
					}

					ErrorLogger.logInfo("Extractor","Done with the QC Subsection #1 of 1!");
				}
			}
			HelperFunctions.completeInstruction(instructionFile,configuration.getProcessingPath(crop, GobiiFileProcessDir.EXTRACTOR_DONE));
		}
	}

	private static String getJobName(String cropName, GobiiDataSetExtract extract) {
		//@Siva get confirmation on lowercase crop name?
		cropName=cropName.charAt(0)+cropName.substring(1).toLowerCase();
		String jobName="[GOBII - Extractor]: " + cropName + " - extraction of \"" + extract.getDataSet().getName() + "\"";
		return jobName;
	}
	
	private static String getHDF5GenoFromMarkerList(boolean markerFast, String errorFile, String tempFolder,String posFile) throws FileNotFoundException{
		return getHDF5GenoFromSampleList(markerFast,errorFile,tempFolder,posFile,null);
	}
	private static String getHDF5GenoFromSampleList(boolean markerFast, String errorFile, String tempFolder,String posFile, String samplePosFile) throws FileNotFoundException{
		BufferedReader posR=new BufferedReader(new FileReader(posFile));
		BufferedReader sampR=null;
		if(samplePosFile!=null)sampR=new BufferedReader(new FileReader(samplePosFile));
        StringBuilder genoFileString=new StringBuilder();
		try{
			posR.readLine();//header
			if(sampR!=null)sampR.readLine();
			while(posR.ready()) {
				String[] line = posR.readLine().split("\t");
				String sampLine=null;
				if(sampR!=null) sampLine= sampR.readLine();
				if(line.length < 2){
					ErrorLogger.logDebug("MarkerList","Skipping line " + Arrays.deepToString(line));
					continue;
				}
				int dsID=Integer.parseInt(line[0]);
				String positionList=line[1].replace(',','\n');
				String positionListFileLoc=tempFolder+"position.list";
				FileSystemInterface.rmIfExist(positionListFileLoc);
				FileWriter w = new FileWriter(positionListFileLoc);
				w.write(positionList);
				w.close();
				String sampleList=null;
				if(sampLine!=null){
					sampleList=sampLine.split("\t")[1];
				}
				String genoFile=getHDF5Genotype(markerFast, errorFile,dsID,tempFolder,positionListFileLoc,sampleList);
				genoFileString.append(" "+genoFile);
			}
		}catch(IOException e) {
			ErrorLogger.logError("GobiiExtractor", "MarkerList reading failed", e);
		}

		//Coallate genotype files
		String genoFile=tempFolder+"markerList.genotype";
		logDebug("MarkerList", "Accumulating markers into final genotype file");
		String genotypePartFileIdentifier=genoFileString.toString();
		if(markerFast) {
			tryExec("paste" + genotypePartFileIdentifier, genoFile, errorFile);
		}
		else{
			tryExec("cat" + genotypePartFileIdentifier, genoFile, errorFile);
		}
		return genoFile;
	}

	private static String getHDF5Genotype( boolean markerFast, String errorFile, Integer dataSetId, String tempFolder) {
		return getHDF5Genotype( markerFast, errorFile,dataSetId,tempFolder,null,null);
	}

	/**
	 * If marker list is null, do a dataset extract. Else, do a marker list extract on the dataset. If sampleList is also set, filter by samples afterwards
	 * @param markerFast
	 * @param errorFile
	 * @param dataSetId Dataset ID to be pulled from
	 * @param tempFolder
	 * @param markerList nullable - determines what markers to extract. File containing a list of marker positions, comma separated
	 * @param sampleList nullable - list of comma delimited samples to cut out
	 * @return
	 */
	private static String getHDF5Genotype( boolean markerFast, String errorFile, Integer dataSetId, String tempFolder, String markerList, String sampleList) {
		String genoFile=tempFolder+"DS-"+dataSetId+".genotype";

		String HDF5File=pathToHDF5Files+"DS_"+dataSetId+".h5";
		// %s <orientation> <HDF5 file> <output file>
		String ordering="samples-fast";
		if(markerFast)ordering="markers-fast";

		logDebug("Extractor","HDF5 Ordering is "+ordering);

		if(markerList!=null) {
			String hdf5Extractor=pathToHDF5+"fetchmarkerlist";
			ErrorLogger.logInfo("Extractor","Executing: " + hdf5Extractor+" "+HDF5File+" "+markerList+" "+genoFile);
			HelperFunctions.tryExec(hdf5Extractor + " " +HDF5File+" "+markerList+" "+genoFile, null, errorFile);
		}
		else {
			String hdf5Extractor=pathToHDF5+"dumpdataset";
			ErrorLogger.logInfo("Extractor","Executing: " + hdf5Extractor+" "+ordering+" "+HDF5File+" "+genoFile);
			HelperFunctions.tryExec(hdf5Extractor + " " + ordering + " " + HDF5File + " " + genoFile, null, errorFile);
		}
		if(sampleList!=null){
			filterBySampleList(genoFile,sampleList,markerFast, errorFile);
		}
		ErrorLogger.logDebug("Extractor",(ErrorLogger.success()?"Success ":"Failure " +"Extracting with "+ordering+" "+HDF5File+" "+genoFile));
		return genoFile;
	}

	/**
	 * Filters a matrix passed back by the HDF5 extractor by a sample list
	 * @param filename path to extract naked matrix
	 * @param sampleList Comma separated list of sample positions
	 */
	private static void filterBySampleList(String filename, String sampleList, boolean markerFast, String errorFile){
		String tmpFile=filename+".tmp";
		FileSystemInterface.mv(filename,tmpFile);
		String cutString=getCutString(sampleList);
		if(markerFast) {
			String sedString=cutString.replaceAll(",","p;");//1,2,3 => 1p;2p;3   (p added later)
			tryExec("sed -n "+sedString+"p",filename,errorFile,tmpFile);
		}
		else{
			tryExec("cut -f"+getCutString(sampleList),filename,errorFile,tmpFile);
		}
	}

	/**
	 * Converts a string of 1,2,-1,4,5,6,-1,2 (Arbitrary -1's and NOT -1's into a comma delimited set of lines from 1-N
	 * excluding positions where a -1 exists, ONE BASED.
	 *
	 * Note: Since input is zero-based list anyway, I probably could have removed the -1's and added 1 to every entry. This seemed derpier.
	 *
	 * Examples:
	 * 0,1,2,-1,4,5 -> 1,2,3,5,6
	 * 7,-1,7,-1,7,-1 -> 1,3,5
	 * @param sampleList Input string
	 * @return Output string (see above)
	 */
	private static String getCutString(String sampleList){
		String[] entries=sampleList.split(",");
		StringBuilder cutString=new StringBuilder();//Cutstring -> 1,2,4,5,6
		int i=1;
		for(String entry:entries){
			if( Integer.parseInt(entry) != -1){
				cutString.append(i+",");
			}
			i++;
		}
		cutString.deleteCharAt(cutString.length()-1);
		return cutString.toString();
	}


	public static List<GobiiExtractorInstruction> parseExtractorInstructionFile(String filename){
		ObjectMapper objectMapper = new ObjectMapper();
		GobiiExtractorInstruction[] file = null;
		 
		try {
			file = objectMapper.readValue(new FileInputStream(filename), GobiiExtractorInstruction[].class);
		} catch (Exception e) {
			ErrorLogger.logError("Extractor","ObjectMapper could not read instructions",e);
		}
		if(file==null)return null;
		return Arrays.asList(file);
	}

	/**
	 * Determine crop type by looking at the instruction file's location for the name of a Socrop.
	 * @param instructionFile
	 * @return GobiiCropType
	 */
	private static String divineCrop(String instructionFile) {
		String upper=instructionFile.toUpperCase();
		String from="/CROPS/";
		int fromIndex=upper.indexOf(from)+from.length();
		String crop=upper.substring(fromIndex,upper.indexOf('/',fromIndex));
		return crop;
	}

	
	private static String getLogName(GobiiExtractorInstruction gli, CropConfig config, Integer dsid) {
		return getLogName(gli.getDataSetExtracts().get(0),config,dsid);
	 }

	private static String getLogName(GobiiDataSetExtract gli, CropConfig config, Integer dsid) {
		String cropName=config.getGobiiCropType();
		String destination=gli.getExtractDestinationDirectory();
		return destination +"/"+cropName+"_DS-"+dsid+".log";
	}

	/**
	 * Brute force method to create a list of object,object,object,object
	 * @param inputList Nonempty list of elements
	 * @return the string value of elements comma separated
	 */
	//Dear next guy - yeah, doing a 'one step unroll' then placing in 'comma - object; comma - object' makes more sense.
	//Just be happy I used a StringBuilder
	private static String commaFormat(List inputList){
		StringBuilder sb=new StringBuilder();
		for(Object o:inputList){
			sb.append(o.toString());
			sb.append(",");
		}
		sb.deleteCharAt(sb.length()-1);//Remove final comma
		return sb.toString();
	}

	/**
	 * Turns a list into a newline delimited file.
	 * @param tmpDir File path - will append 'markerList.tmp' and return
	 * @param markerList List to go into file, newline delimited
	 * @return location of new file.
	 */
	private static String createTempFileForMarkerList(String tmpDir,List<String> markerList){
		String tempFileLocation=tmpDir+"markerList.tmp";
		try {
			FileWriter f = new FileWriter(tempFileLocation);
			for(String marker:markerList){
				f.write(marker);
				f.write("\n");
			}
			f.close();
		}
		catch(Exception e){
			ErrorLogger.logError("Extractor","Could not create temp file "+tempFileLocation,e);
		}
		return tempFileLocation;
	}

	private static boolean addSlashesToBiAllelicData(String genoFile, String extractDir, GobiiDataSetExtract extract) throws Exception {
		Path SSRFilePath = Paths.get(genoFile);
		File SSRFile = new File(SSRFilePath.toString());
		if (SSRFile.exists()) {
			Path AddedSSRFilePath = Paths.get(extractDir, (new StringBuilder ("Added")).append(SSRFilePath.getFileName()).toString());
			// Deleting any temporal file existent
			rmIfExist(AddedSSRFilePath.toString());
			File AddedSSRFile = new File(AddedSSRFilePath.toString());
			if (AddedSSRFile.createNewFile()) {
				Scanner scanner = new Scanner(new FileReader(SSRFile));
				FileWriter fileWriter = new FileWriter(AddedSSRFile);
				// Copying the header
				if (scanner.hasNextLine()) {
					fileWriter.write((new StringBuilder (scanner.nextLine())).append(System.lineSeparator()).toString());
				}
				else {
					ErrorLogger.logError("Extractor", "Genotype file emtpy");
					return false;
				}
				if (!(scanner.hasNextLine())) {
					ErrorLogger.logError("Extractor", "No genotype data");
					return false;
				}
				Pattern pattern = Pattern.compile("^[0-9]{1,8}$");
				while (scanner.hasNextLine()) {
					String[] lineParts = scanner.nextLine().split("\\t");
					// The first column does not need to be converted
					StringBuilder addedLineStringBuilder = new StringBuilder(lineParts[0]);
					// Converting each column from the next ones
					for (int index = 1; index < lineParts.length; index++) {
						addedLineStringBuilder.append("\t");
						if (!(pattern.matcher(lineParts[index]).find())) {
							ErrorLogger.logError("Extractor","Incorrect SSR allele size format (1): " + lineParts[index]);
							addedLineStringBuilder.append(lineParts[index]);
						}
						else {
							if ((5 <= lineParts[index].length()) && (lineParts[index].length() <= 8)) {
								int leftNumber = Integer.parseInt(lineParts[index].substring(0, lineParts[index].length() - 4));
								int rightNumber = Integer.parseInt(lineParts[index].substring(lineParts[index].length() - 4));
								if ((leftNumber == 0) && (rightNumber == 0)) {
									addedLineStringBuilder.append("N/N");
								}
								else {
									addedLineStringBuilder.append(leftNumber);
									addedLineStringBuilder.append("/");
									addedLineStringBuilder.append(rightNumber);
								}
							}
							else {
								if ((1 <= lineParts[index].length()) && (lineParts[index].length() <= 4)) {
									int number = Integer.parseInt(lineParts[index]);
									if (number == 0) {
										addedLineStringBuilder.append("N/N");
									}
									else {
										addedLineStringBuilder.append("0/");
										addedLineStringBuilder.append(number);
									}
								}
								else {
										ErrorLogger.logError("Extractor","Incorrect SSR allele size format (2): " + lineParts[index]);
										addedLineStringBuilder.append(lineParts[index]);
								}
							}
						}
					}
					addedLineStringBuilder.append(System.lineSeparator());
					fileWriter.write(addedLineStringBuilder.toString());
				}
				scanner.close();
				fileWriter.close();
				// Deleting any original data backup file existent
				rmIfExist(Paths.get(SSRFilePath.toString() + ".bak").toString());
				// Backing up the original data file
				mv(SSRFilePath.toString(), SSRFilePath.toString() + ".bak");
				// Renaming the converted data file to the original data file name
				mv(AddedSSRFilePath.toString(), SSRFilePath.toString());
			}
			else {
				ErrorLogger.logError("Extractor","Unable to create the added SSR file: "+AddedSSRFilePath.toString());
				return false;
			}
		} else {
			ErrorLogger.logError("Extractor", "No genotype file: " + SSRFilePath.toString());
			return false;
		}

		return true;
	}

	/**
	 * Simple Look-Up Table
	 * @param type Un-numbered Enum
	 * @return Database numeric
	 */
	private static int getNumericType(GobiiSampleListType type){
		switch(type){
			case DNA_SAMPLE:
				return 3;
			case EXTERNAL_CODE:
				return 2;
			case GERMPLASM_NAME:
				return 1;
			default:
				return -1;
		}
	}
}
