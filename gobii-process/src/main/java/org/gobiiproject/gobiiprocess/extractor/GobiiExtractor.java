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
import org.gobiiproject.gobiiapimodel.restresources.RestUri;
import org.gobiiproject.gobiiapimodel.restresources.UriFactory;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiiclient.core.common.ClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiimodel.dto.instructions.GobiiQCComplete;
import org.gobiiproject.gobiimodel.headerlesscontainer.QCInstructionsDTO;
import org.gobiiproject.gobiimodel.types.*;
import org.gobiiproject.gobiimodel.utils.DateUtils;
import org.gobiiproject.gobiimodel.utils.HelperFunctions;
import org.gobiiproject.gobiimodel.utils.email.QCMessage;
import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;
import org.gobiiproject.gobiiprocess.extractor.flapjack.FlapjackTransformer;
import org.gobiiproject.gobiiprocess.extractor.hapmap.HapmapTransformer;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.CropConfig;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiDataSetExtract;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiExtractorInstruction;

import static org.gobiiproject.gobiimodel.utils.FileSystemInterface.mv;
import static org.gobiiproject.gobiimodel.utils.FileSystemInterface.rm;
import static org.gobiiproject.gobiimodel.utils.FileSystemInterface.rmIfExist;
import static org.gobiiproject.gobiimodel.utils.HelperFunctions.*;
import static org.gobiiproject.gobiimodel.utils.error.ErrorLogger.logError;

public class GobiiExtractor {
	//Paths
	private static String  pathToHDF5, propertiesFile,pathToHDF5Files;
	
	private static String lastErrorFile=null;
	private static String errorLogOverride;
	private static boolean verbose;
	private static String rootDir="../";
	private static UriFactory uriFactory;

	public static void main(String[] args) throws Exception {
		Options o = new Options()
         		.addOption("v", "verbose", false, "Verbose output")
         		.addOption("e", "errlog", true, "Error log override location")
         		.addOption("r", "rootDir", true, "Fully qualified path to gobii root directory")
         		.addOption("c","config",true,"Fully qualified path to gobii configuration file")
         		.addOption("h", "hdfFiles", true, "Fully qualified path to hdf files");
        
		 CommandLineParser parser = new DefaultParser();
         try{
               CommandLine cli = parser.parse( o, args );
               if(cli.hasOption("verbose")) verbose=true;
               if(cli.hasOption("errLog")) errorLogOverride = cli.getOptionValue("errLog");
               if(cli.hasOption("config")) propertiesFile = cli.getOptionValue("config");
                      if(cli.hasOption("rootDir")){
                rootDir = cli.getOptionValue("rootDir");
               }
               if(cli.hasOption("hdfFiles")) pathToHDF5Files = cli.getOptionValue("hdfFiles");
               args=cli.getArgs();//Remaining args passed through
                
         }catch(org.apache.commons.cli.ParseException exp ) {
               new HelpFormatter().printHelp("java -jar Extractor.jar ","Also accepts input file directly after arguments\n"
               		+ "Example: java -jar Extractor.jar -c /home/jdl232/customConfig.properties -v /home/jdl232/testLoad.json",o,null,true);
               
               System.exit(2);
         }
		
     	String extractorScriptPath=rootDir+"extractors/";
    	pathToHDF5=extractorScriptPath+"hdf5/bin/";
    	
    	if(propertiesFile==null)propertiesFile=rootDir+"config/gobii-web.properties";
		
		boolean success=true;
		ConfigSettings configuration=null;
		try {
			configuration = new ConfigSettings(propertiesFile);
		} catch (Exception e) {
			logError("Extractor","Failure to read Configurations",e);
			return;
		}
		String logDir=configuration.getFileSystemLog();
		ErrorLogger.setLogFilepath(logDir);
		String instructionFile=null;
		if(args.length==0 ||args[0]==""){
			Scanner s=new Scanner(System.in);
			System.out.println("Enter Extractor Instruction File Location:");
			instructionFile=s.nextLine();
		    if(instructionFile.equals("")) instructionFile="scripts//jdl232_01_pretty.json";
		    s.close();
		}
		else{
			instructionFile=args[0];
		}

		List<GobiiExtractorInstruction> list= parseExtractorInstructionFile(instructionFile);
		if(list==null){
			ErrorLogger.logError("Extractor","No instruction for file "+instructionFile);
			return;
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
			if(mapIds.isEmpty() || mapIds.get(0).equals(null)){
				mapId=null;
			}else if(mapIds.size()>1){
				logError("Extraction Instruction","Too many map IDs for extractor. Expected one, recieved "+mapIds.size());
				mapId=null;
			}
			else{
				mapId=mapIds.get(0);
			}

			
			for(GobiiDataSetExtract extract:inst.getDataSetExtracts()){
				String extractDir=extract.getExtractDestinationDirectory();
				tryExec("rm -f "+extractDir+"*");
				//TODO: Fix underlying permissions issues
				//tryExec("chmod -R 777 " +extractDir.substring(0, extractDir.lastIndexOf('/')));
				String markerFile=extractDir+"marker.file";
				String extendedMarkerFile=markerFile+".ext";
				String mapsetFile=extractDir+"mapset.file";
				String markerPosFile=markerFile+".pos";
				String sampleFile=extractDir+"sample.file";
				String projectFile=extractDir+"summary.file";
				String chrLengthFile = markerFile+".chr";
				Path mdePath = FileSystems.getDefault().getPath(new StringBuilder(extractorScriptPath).append("postgres/gobii_mde/gobii_mde.py").toString());
				if (!(mdePath.toFile().exists() &&
					  mdePath.toFile().isFile())) {
					ErrorLogger.logDebug("Extractor", new StringBuilder(mdePath.toString()).append(" does not exist!").toString());
					return;
				}
				String gobiiMDE = "python "+ mdePath+
						" -c " + HelperFunctions.getPostgresConnectionString(cropConfig) +
						" -m " + markerFile +
						" -b " + mapsetFile +
						" -s " + sampleFile +
						" -p " + projectFile +
						(mapId==null?"":(" -D "+mapId))+
						" -d " + extract.getDataSetId() +
						" -l -v ";
				String errorFile=getLogName(extract,cropConfig,extract.getDataSetId());
				ErrorLogger.logInfo("Extractor","Executing MDEs");
				ErrorLogger.logDebug("Extractor",gobiiMDE);
				tryExec(gobiiMDE, null, errorFile);
				Integer dataSetId=extract.getDataSetId();

				//HDF5
				String tempFolder=extractDir;

				String genoFile=tempFolder+"DS-"+dataSetId+".genotype";
				String hdf5Extractor=pathToHDF5+"dumpdataset";
				String HDF5File=pathToHDF5Files+"DS_"+dataSetId+".h5";
				// %s <orientation> <HDF5 file> <output file>
				boolean markerFast=false;
				if(extract.getGobiiFileType()==GobiiFileType.HAPMAP)markerFast=true;
				String ordering="samples-fast";
				if(markerFast)ordering="markers-fast";
				ErrorLogger.logDebug("Extractor","HDF5 Ordering is "+ordering);
				ErrorLogger.logInfo("Extractor","Executing: " + hdf5Extractor+" "+ordering+" "+HDF5File+" "+genoFile);
				HelperFunctions.tryExec(hdf5Extractor+" "+ordering+" "+HDF5File+" "+genoFile,null,errorFile);
				success&=ErrorLogger.success();
				ErrorLogger.logDebug("Extractor",(success?"Success ":"Failure " + hdf5Extractor+" "+ordering+" "+HDF5File+" "+genoFile));
				
				switch(extract.getGobiiFileType()){

				case FLAPJACK:
					String genoOutFile=extractDir+"DS"+dataSetId+".genotype";
					String mapOutFile=extractDir+"DS"+dataSetId+".map";
					lastErrorFile=errorFile;
					//Always regenerate requests - may have different parameters
					FlapjackTransformer.generateMapFile(extendedMarkerFile, sampleFile, chrLengthFile, dataSetId, tempFolder, mapOutFile, errorFile);
					HelperFunctions.sendEmail(extract.getDataSetName()+ " Map Extract", mapOutFile, success&&ErrorLogger.success(), errorFile, configuration, inst.getContactEmail());
					FlapjackTransformer.generateGenotypeFile(markerFile, sampleFile, genoFile, dataSetId, tempFolder, genoOutFile,errorFile);
					HelperFunctions.sendEmail(extract.getDataSetName()+ " Genotype Extract", genoOutFile, success&&ErrorLogger.success(), errorFile, configuration, inst.getContactEmail());
					break;
				
				case HAPMAP:
					String hapmapOutFile = extractDir+"DS"+dataSetId+".hmp.txt";
					HapmapTransformer hapmapTransformer = new HapmapTransformer();
					System.out.println("Executing HapMap creation");
					if (hapmapTransformer.generateFile(markerFile,
							                           sampleFile,
							                           mapsetFile,
							                           genoFile,
							                           hapmapOutFile,
							                           errorFile)) {
						rm(genoFile);
						rmIfExist(chrLengthFile);
						HelperFunctions.sendEmail(extract.getDataSetName()+" Hapmap Extract",hapmapOutFile,success&&ErrorLogger.success(),errorFile,configuration,inst.getContactEmail());
					}
					else {
						ErrorLogger.logError("Extractor","Exception in HapMap creation");
					}
					break;

					default:
						ErrorLogger.logError("Extractor","Unknown Extract Type "+extract.getGobiiFileType());
						HelperFunctions.sendEmail(extract.getDataSetName()+" "+extract.getGobiiFileType()+" Extract",null,false,errorFile,configuration,inst.getContactEmail());
				}
				rmIfExist(markerPosFile);
				rmIfExist(extendedMarkerFile);
				rmIfExist(mapsetFile);
				ErrorLogger.logDebug("Extractor","DataSet "+dataSetId+" Created");

				// Adding "/" back to the bi-allelic data
				if (extract.getGobiiDatasetType().equals(DataSetType.SSR_ALLELE_SIZE.toString())) {
					Path SSRFilePath = Paths.get(extractDir+"DS"+dataSetId+".genotype");
					File SSRFile = new File(SSRFilePath.toString());
					if (SSRFile.exists()) {
						Path AddedSSRFilePath = Paths.get(extractDir, "Added" + SSRFilePath.getFileName());
						// Deleting any temporal file existent
						rmIfExist(AddedSSRFilePath.toString());
						File AddedSSRFile = new File(AddedSSRFilePath.toString());
						if (AddedSSRFile.createNewFile()) {
							Scanner scanner = new Scanner(new FileReader(SSRFile));
							FileWriter fileWriter = new FileWriter(AddedSSRFile);
							// Copying the header
							if (scanner.hasNextLine()) {
								fileWriter.write(scanner.nextLine() + System.lineSeparator());
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
										ErrorLogger.logError("Extractor","Incorrect SSR allele size format (1): "+lineParts[index]);
										addedLineStringBuilder.append(lineParts[index]);
									}
									else {
										if ((5 <= lineParts[index].length()) && (lineParts[index].length() <= 8)) {
											addedLineStringBuilder.append(Integer.parseInt(lineParts[index].substring(0, lineParts[index].length() - 4)));
											addedLineStringBuilder.append("/");
											addedLineStringBuilder.append(Integer.parseInt(lineParts[index].substring(lineParts[index].length() - 4)));
										}
										else {
											if ((1 < lineParts[index].length()) && (lineParts[index].length() <= 4)) {
												addedLineStringBuilder.append("0/");
												addedLineStringBuilder.append(Integer.parseInt(lineParts[index]));
											}
											else {
												if (lineParts[index].length() == 1) {
													Integer digit = Integer.parseInt(lineParts[index]);
													if (digit != 0) {
														addedLineStringBuilder.append("0/");
														addedLineStringBuilder.append(digit);
													} else {
														addedLineStringBuilder.append("N/N");
													}
												}
												else {
													ErrorLogger.logError("Extractor","Incorrect SSR allele size format (2): "+lineParts[index]);
													addedLineStringBuilder.append(lineParts[index]);
												}
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
						}
					} else {
						ErrorLogger.logError("Extractor","No genotype file: "+SSRFilePath.toString());
					}
				}

				//QC - Subsection #1 of 1
				if (inst.isQcCheck()) {
					ErrorLogger.logInfo("Extractor", "qcCheck detected");
					ErrorLogger.logInfo("Extractor","Entering into the QC Subsection #1 of 1...");
					QCInstructionsDTO qcInstructionsDTOToSend = new QCInstructionsDTO();
					qcInstructionsDTOToSend.setContactId(inst.getContactId());
					qcInstructionsDTOToSend.setDataFileDirectory(extractDir);
					qcInstructionsDTOToSend.setDataFileName(new StringBuilder("qc_").append(DateUtils.makeDateIdString()).toString());
					qcInstructionsDTOToSend.setDatasetId(dataSetId);
					qcInstructionsDTOToSend.setGobiiJobStatus(GobiiJobStatus.COMPLETED);
					//qcInstructionsDTOToSend.setId();
					qcInstructionsDTOToSend.setQualityFileName("Report.xls");
					PayloadEnvelope<QCInstructionsDTO> payloadEnvelope = new PayloadEnvelope<>(qcInstructionsDTOToSend, GobiiProcessType.CREATE);
					ClientContext clientContext = ClientContext.getInstance(configuration, crop);
					SystemUserDetail SystemUserDetail = (new SystemUsers()).getDetail(SystemUserNames.USER_READER.toString());
					if(!(clientContext.login(SystemUserDetail.getUserName(), SystemUserDetail.getPassword()))) {
						ErrorLogger.logError("Digester","Login Error:" + SystemUserDetail.getUserName() + "-" + SystemUserDetail.getPassword());
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

					QCInstructionsDTO qcInstructionsDTO = new QCInstructionsDTO();
					qcInstructionsDTO.setDataFileDirectory(extractDir);
					//String currentQCContextRoot = ClientContext.getInstance("http://gobiilab03.bti.cornell.edu:8080/kdcompute/", true);
					//uriFactory = new UriFactory(currentCropContextRoot);
					RestUri restUri = new RestUri("http://gobiilab03.bti.cornell.edu:8080/kdcompute/qcStart");
					restUri.addUriParam("datasetId");
					restUri.setParamValue("datasetId", dataSetId.toString());
					restUri.addUriParam("directory");
					restUri.setParamValue("directory", extractDir);
					GobiiEnvelopeRestResource<QCStart> restResourceForGet = new GobiiEnvelopeRestResource<QCStart>(restUri);
					PayloadEnvelope<QCStart> qcStartResponseEnvelope = restResourceForGet.get(QCStart.class);

					if (qcStartResponseEnvelope != null) {
						ErrorLogger.logInfo("Extractor","QC Start Done");
					}
					else {
						ErrorLogger.logError("Extractor","QC Start Null");
					}

					ErrorLogger.logInfo("Extractor","Done with the QC Subsection #1 of 1!");
				}
			}
			HelperFunctions.completeInstruction(instructionFile,configuration.getProcessingPath(crop, GobiiFileProcessDir.EXTRACTOR_DONE));
		}
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
	
	private static String divineCrop(String instructionFile) {
		String upper=instructionFile.toUpperCase();
		String crop = null;
		for(GobiiCropType c:GobiiCropType.values()){
				if(upper.contains(c.toString())){
					crop=c.name();
					break;
				}
		}
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
}
