package org.gobiiproject.gobidomain.services.impl.sampletracking;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.*;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.cvnames.CvGroup;
import org.gobiiproject.gobiimodel.cvnames.JobPayloadType;
import org.gobiiproject.gobiimodel.cvnames.JobProgressStatusType;
import org.gobiiproject.gobiimodel.cvnames.JobType;
import org.gobiiproject.gobiimodel.dto.entity.auditable.ContactDTO;
import org.gobiiproject.gobiimodel.dto.entity.auditable.sampletracking.DnaSampleDTO;
import org.gobiiproject.gobiimodel.dto.entity.auditable.sampletracking.ProjectDTO;
import org.gobiiproject.gobiimodel.dto.entity.children.PropNameId;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.JobDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.JobStatusDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.ProjectSamplesDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.SampleMetadataDTO;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiFileColumn;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderMetadata;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderProcedure;
import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiimodel.modelmapper.EntityFieldBean;
import org.gobiiproject.gobiimodel.modelmapper.ModelMapper;
import org.gobiiproject.gobiimodel.types.*;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.gobiiproject.gobiisampletrackingdao.CvDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.*;
import java.util.*;

public class DnaSampleServiceImpl implements  DnaSampleService {

    Logger LOGGER = LoggerFactory.getLogger(DnaSampleServiceImpl.class);

    @Autowired
    private CvDao cvDao;

    @Autowired
    private ProjectService sampleTrackingProjectService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private FilesService fileService = null;

    @Autowired
    private JobService jobService = null;

    @Override
    public ProjectSamplesDTO createSamples(ProjectSamplesDTO projectSamplesDTO)  throws GobiiDomainException {

        ProjectSamplesDTO returnVal = null;

        try {

            return projectSamplesDTO;

        } catch (Exception e) {

            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);

        }
    }

    /**
     * Upload sample input file to the GDM crop database.
     * An instruction file for the sample upload job is created and posted in the instruction file folder
     * which will be fetched by the Cron Job running in compute node.
     * An entry for the job is also added to the backend database to keep track of the job.
     *
     * Input file should be a tab delimited file with header as its first row.
     *
     * @param sampleInputBytes - input file byte array for sample
     * @param sampleMetadata -  Meta data for sample upload, projectId and map object to map the
     *                       input file headers to the gdm system sample properties.
     * @param cropType - The crop database to which the samples needs to be uploaded
     * @return Job sttatus object with job id and other relevant details
     */
    @Override
    public JobDTO uploadSamples(
            byte[] sampleInputBytes,
            SampleMetadataDTO sampleMetadata,
            String cropType) {

        final String sampleFileDelimiter = "\t";

        BufferedReader br;

        GobiiLoaderProcedure gobiiLoaderProcedure = new GobiiLoaderProcedure();

        GobiiLoaderMetadata gobiiLoaderMetadata = new GobiiLoaderMetadata();

        List<GobiiLoaderInstruction> gobiiLoaderInstructionList = new ArrayList<>();

        JobDTO dnasampleLoadJob  = new JobStatusDTO();

        try {

            String userName = SecurityContextHolder.getContext().getAuthentication().getName();

            ContactDTO userContact = this.contactService.getContactByUserName(userName);

            gobiiLoaderMetadata.setContactEmail(userContact.getEmail());

            dnasampleLoadJob.setSubmittedBy(userContact.getContactId());

            dnasampleLoadJob.setSubmittedDate(new Date(new Date().getTime()));

            this.createDnaSampleUploadJob(dnasampleLoadJob);

            String sourceFileName = "samples.txt";

            String sourceFilePath = fileService.writeJobFileForCrop(
                    cropType, dnasampleLoadJob.getJobName(),
                    sourceFileName, GobiiFileProcessDir.RAW_USER_FILES,
                    sampleInputBytes);

            String destinationDir = fileService.makeDirInProcessDir(
                    cropType, dnasampleLoadJob.getJobName(),
                    GobiiFileProcessDir.LOADER_INTERMEDIATE_FILES);

            gobiiLoaderMetadata.setGobiiCropType(cropType);
            gobiiLoaderMetadata.getGobiiFile().setGobiiFileType(GobiiFileType.GENERIC);
            gobiiLoaderMetadata.getGobiiFile().setSource(sourceFilePath);
            gobiiLoaderMetadata.getGobiiFile().setDestination(destinationDir);
            gobiiLoaderMetadata.getGobiiFile().setDelimiter(sampleFileDelimiter);

            gobiiLoaderMetadata.setJobPayloadType(JobPayloadType.CV_PAYLOADTYPE_SAMPLES);

            PropNameId projectPropName = new PropNameId();
            if(sampleMetadata.getProjectId() == null) {
                throw new GobiiDomainException(
                        GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.MISSING_REQUIRED_VALUE,
                        "Project id is required");
            }
            ProjectDTO projectDTO = (ProjectDTO) sampleTrackingProjectService.getProjectById(
                    sampleMetadata.getProjectId());
            projectPropName.setId(projectDTO.getProjectId());
            projectPropName.setName(projectDTO.getProjectName());
            gobiiLoaderMetadata.setProject(projectPropName);

            InputStream sampleInputStream = new ByteArrayInputStream(sampleInputBytes);
            br = new BufferedReader(new InputStreamReader(sampleInputStream, "UTF-8"));
            String fileHeader = br.readLine();

            Map<String, List<GobiiFileColumn>> fileColumnsByTableName = this.mapFileCoulumnToGobiiTable(
                    fileHeader,
                    sampleMetadata);

            for(String tableName : fileColumnsByTableName.keySet()) {
                GobiiLoaderInstruction gobiiLoaderInstruction = new GobiiLoaderInstruction();
                gobiiLoaderInstruction.setTable(tableName);
                gobiiLoaderInstruction.setGobiiFileColumns(fileColumnsByTableName.get(tableName));
                gobiiLoaderInstructionList.add(gobiiLoaderInstruction);
            }

            gobiiLoaderProcedure.setMetadata(gobiiLoaderMetadata);
            gobiiLoaderProcedure.setInstructions(gobiiLoaderInstructionList);

            //Instruction file name needs to be same as job name
            String fileName = dnasampleLoadJob.getJobName() + ".json";
            this.writeDnasampleLoaderInstruction(gobiiLoaderProcedure, fileName, cropType);

            return dnasampleLoadJob;

        }
        catch(GobiiException gE) {
            throw gE;
        }
        catch(Exception e) {

            LOGGER.error(e.getMessage(), e);

            throw new GobiiDomainException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.UNKNOWN, "Server error");

        }
    }

    private JobDTO createDnaSampleUploadJob(JobDTO jobDto) {


        try {

            //UUID for Jobname. would be used for tracking job details
            String jobName = UUID.randomUUID().toString().replace("-", "");

            jobDto.setJobName(jobName);
            jobDto.setMessage("Submitted the job for sample upload");
            jobDto.setPayloadType(JobPayloadType.CV_PAYLOADTYPE_SAMPLES.getCvName());
            jobDto.setType(JobType.CV_JOBTYPE_LOAD.getCvName());
            jobDto.setStatus(JobProgressStatusType.CV_PROGRESSSTATUS_PENDING.getCvName());

            jobDto = jobService.createJob(jobDto);

            return jobDto;

        }
        catch(GobiiException gE) {
            throw gE;
        }
        catch(Exception e) {

            LOGGER.error(e.getMessage(), e);

            throw new GobiiDomainException(
                    GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    "Unable to submit Dnasample load job");

        }
    }

    private void writeDnasampleLoaderInstruction(
            GobiiLoaderProcedure gobiiLoaderProcedure, String fileName, String cropType) {

        ObjectMapper jsonMapper = new ObjectMapper();

        try {
            if (gobiiLoaderProcedure != null) {

                jsonMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

                jsonMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

                String instruction = jsonMapper.writeValueAsString(gobiiLoaderProcedure);

                byte[] instructionBytes = instruction.getBytes("UTF-8");

                fileService.writeFileToProcessDir(
                        cropType,
                        fileName,
                        GobiiFileProcessDir.LOADER_INSTRUCTIONS,
                        instructionBytes);
            }
        }
        catch(Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new GobiiDomainException(
                    GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN, "Unable to write instruction file");
        }

    }


    /**
     * File columns in the input file headers need to mapped to Goibii Table File columns object
     * used for building instruction file.
     *
     * In persistence layer, DNA sample and germplasm table has a jsonb column called "prop" for additional properties,
     * Instruction file demands those column be defined as a seperate Table object with {tableName}_prop as name.
     * Each prop value is mapped to their respective tuples using columns which could uniquely identify them.
     * The required fields for the prop tables are fetched from required_columns.json in config folder.
     * TODO: The file required_columns.json was created by referring to dnasample_prop.nmap and
     *   germplasm_prop.nmap file used by the ifl(scripts to aid data loading).
     *   This might cause problems in future as there are two source of reference for required fields and
     *   both needs to be updated when there is a change. A request need to be raised to make Loader UI and IFL
     *   to use the same properties file in config folder.
     *
     * @param fileHeader tab separated file header with each token representing the column header
     * @param sampleMetadata - Object with projectId to identify the project to which samples belongs to. map object
     *                       to map input file headers to the GDM defined sample properties.
     * @return map of instruction file table to their gobii file columns
     */
    private Map<String, List<GobiiFileColumn>> mapFileCoulumnToGobiiTable(
            String fileHeader,
            SampleMetadataDTO sampleMetadata) {

        Map<String, List<GobiiFileColumn>> fileColumnsByTableName = new LinkedHashMap<>();

        Map<String, List<GobiiFileColumn>> propTableIdFields = new HashMap<>();

        //Instrction file needs tables in below order or it will fail
        final String[] tableOrder = new String[]{"germplasm", "germplasm_prop", "dnasample", "dnasample_prop"};

        try {

            Map<String, EntityFieldBean> dtoEntityMap = ModelMapper.getDtoEntityMap(DnaSampleDTO.class);

            Map<String, Object> requiredFieldProps = this.getPropTableRequiredFields();

            //Adding table entries in order
            for(String tableName : tableOrder) {
                fileColumnsByTableName.put(tableName, new LinkedList<>());
            }

            //dnasample prop table needs projectId
            propTableIdFields.put("dnasample_prop", new LinkedList<>());

            GobiiFileColumn projectIdColumn = new GobiiFileColumn();
            projectIdColumn.setGobiiColumnType(GobiiColumnType.CONSTANT);
            projectIdColumn.setSubcolumn(false);
            projectIdColumn.setConstantValue(sampleMetadata.getProjectId().toString());
            projectIdColumn.setName(dtoEntityMap.get("projectId").getColumnName());


            fileColumnsByTableName.get("dnasample").add(projectIdColumn);
            propTableIdFields.get("dnasample_prop").add(projectIdColumn);

            // Set the Status of the project as newly created by getting it respective cvId
            List<Cv> statusCvList = cvDao.getCvsByCvTermAndCvGroup(
                    "new", CvGroup.CVGROUP_STATUS.getCvGroupName(),
                    GobiiCvGroupType.GROUP_TYPE_SYSTEM);

            //As CV term is unique under its CV group, there should be only
            //one cv for term "new" under group "status"
            if(statusCvList.size() > 0) {
                Cv statusCv = statusCvList.get(0);
                GobiiFileColumn statusColumn = new GobiiFileColumn();
                statusColumn.setGobiiColumnType(GobiiColumnType.CONSTANT);
                statusColumn.setSubcolumn(false);
                statusColumn.setConstantValue(statusCv.getCvId().toString());
                statusColumn.setName("status");
                fileColumnsByTableName.get("dnasample").add(statusColumn);
                fileColumnsByTableName.get("germplasm").add(statusColumn);
            }

            String[] fileHeaderList = fileHeader.split("\t");


            //Get Germplasm prop cvs TODO: Below cv mapping could be moved to a generalized function.
            //TODO: Create Cv service function to achieve the same.
            List<Cv> germplasmPropCvs = cvDao.getCvListByCvGroup(
                    CvGroup.CVGROUP_GERMPLASM_PROP.getCvGroupName(),
                    null);

            Map<String, Cv> germplasmPropByCvTerm = new HashMap<>();

            for(Cv germplasmPropCv : germplasmPropCvs) {
                if(germplasmPropCv.getTerm() != null) {
                    germplasmPropByCvTerm.put(germplasmPropCv.getTerm(), germplasmPropCv);
                }
            }

            //Get dnasample prop cvs
            List<Cv> dnasamplePropCvs = cvDao.getCvListByCvGroup(
                    CvGroup.CVGROUP_DNASAMPLE_PROP.getCvGroupName(),
                    null);

            Map<String, Cv> dnasamplePropByCvTerm = new HashMap<>();

            for(Cv dnasamplePropCv : dnasamplePropCvs) {
                if(dnasamplePropCv.getTerm() != null) {
                    dnasamplePropByCvTerm.put(dnasamplePropCv.getTerm(), dnasamplePropCv);
                }
            }


            for (int i = 0; i < fileHeaderList.length; i++) {


                String columnHeader = fileHeaderList[i];

                List<EntityFieldBean> entityFields = new ArrayList<>();

                if (sampleMetadata.getMap().containsKey(columnHeader)) {

                    List<String> dtoProps = sampleMetadata.getMap().get(columnHeader);

                    for (String dtoProp : dtoProps) {

                        if (dtoEntityMap.containsKey(dtoProp) && dtoEntityMap.get(dtoProp) != null) {

                            entityFields.add(dtoEntityMap.get(dtoProp));

                        } else {

                            EntityFieldBean entityField = new EntityFieldBean();

                            String propField = dtoProp.substring(0, dtoProp.lastIndexOf("."));

                            //TODO: More generalized solution where you can map properties to the instruction files
                            // needs to be figured
                            if (propField.equals("germplasm.properties")) {

                                String germplasmPropField = dtoProp.substring(dtoProp.lastIndexOf(".") + 1);

                                if (germplasmPropByCvTerm.containsKey(germplasmPropField)) {

                                    entityField.setColumnName(
                                            germplasmPropByCvTerm.get(germplasmPropField).getCvId().toString());
                                    entityField.setTableName(CvGroup.CVGROUP_GERMPLASM_PROP.getCvGroupName());

                                }

                            } else if (propField.equals("properties")) {

                                String dnasamplePropField = dtoProp.substring(dtoProp.lastIndexOf(".") + 1);

                                if (dnasamplePropByCvTerm.containsKey(dnasamplePropField)) {

                                    entityField.setColumnName(dnasamplePropByCvTerm.get(
                                            dnasamplePropField).getCvId().toString());
                                    entityField.setTableName(CvGroup.CVGROUP_DNASAMPLE_PROP.getCvGroupName());

                                }

                            } else if (dtoProp.equals("germplasm.germplasmSpecies")) {

                                entityField.setColumnName("species_name");

                                entityField.setTableName("germplasm");

                            } else if (dtoProp.equals("germplasm.germplasmType")) {

                                entityField.setColumnName("type_name");

                                entityField.setTableName("germplasm");

                            } else {
                                entityField = null;
                            }

                            entityFields.add(entityField);
                        }

                    }
                }
                else if (dtoEntityMap.containsKey(columnHeader)) {
                    entityFields.add(dtoEntityMap.get(columnHeader));
                }

                for(EntityFieldBean entityField : entityFields) {
                    if (entityField != null && entityField.getTableName() != null) {

                        if (entityField.getTableName().endsWith("prop")) {

                            GobiiFileColumn gobiiFileKeyColumn = new GobiiFileColumn();

                            if (fileColumnsByTableName.get(entityField.getTableName()).size() > 0) {
                                gobiiFileKeyColumn.setName("comma" + entityField.getColumnName());
                                gobiiFileKeyColumn.setSubcolumn(true);
                                gobiiFileKeyColumn.setConstantValue("," + entityField.getColumnName()+":");
                            } else {
                                //The key name needs to be props other wise loading will fail for first entry
                                gobiiFileKeyColumn.setName("props");
                                gobiiFileKeyColumn.setSubcolumn(false);
                                gobiiFileKeyColumn.setConstantValue(entityField.getColumnName()+":");
                            }

                            gobiiFileKeyColumn.setGobiiColumnType(GobiiColumnType.CONSTANT);
                            fileColumnsByTableName.get(entityField.getTableName()).add(gobiiFileKeyColumn);

                            //Add Value column
                            //Add a comma gobii file column as required by instruction file
                            GobiiFileColumn valueColumn = new GobiiFileColumn();

                            valueColumn.setGobiiColumnType(GobiiColumnType.CSV_COLUMN);
                            valueColumn.setSubcolumn(true);
                            valueColumn.setName("value" + entityField.getColumnName());
                            //The file header needs to be the first line.
                            // TODO: file header position hard coded for now. Will be made configurable at some point
                            valueColumn.setRCoord(1);
                            valueColumn.setCCoord(i);

                            fileColumnsByTableName.get(entityField.getTableName()).add(valueColumn);


                        } else {

                            GobiiFileColumn gobiiFileColumn = new GobiiFileColumn();

                            //The file header needs to be the first line.
                            // TODO: file header position hard coded for now. Will be made configurable at some point
                            gobiiFileColumn.setRCoord(1);
                            gobiiFileColumn.setCCoord(i);

                            gobiiFileColumn.setName(entityField.getColumnName());
                            gobiiFileColumn.setGobiiColumnType(GobiiColumnType.CSV_COLUMN);
                            gobiiFileColumn.setSubcolumn(false);

                            if (!fileColumnsByTableName.containsKey(entityField.getTableName())) {
                                fileColumnsByTableName.put(entityField.getTableName(), new LinkedList<>());
                            }

                            fileColumnsByTableName.get(entityField.getTableName()).add(gobiiFileColumn);

                            GobiiFileColumn requiredFieldColumn = new GobiiFileColumn();
                            GobiiFileColumn.copy(gobiiFileColumn, requiredFieldColumn);

                            if (requiredFieldProps.containsKey(entityField.getTableName())
                                    && ((HashMap) requiredFieldProps.get(
                                    entityField.getTableName())).containsKey(entityField.getColumnName())) {

                                //TODO: Add condition to check type before assignment
                                List<EntityFieldBean> dependentFields = (ArrayList) ((HashMap) requiredFieldProps.get(
                                        entityField.getTableName())).get(entityField.getColumnName());

                                for (EntityFieldBean dependentField : dependentFields) {

                                    String propTableName = dependentField.getTableName();

                                    if (dependentField.getColumnName() != null && !dependentField.getColumnName().isEmpty()) {
                                        requiredFieldColumn.setName(dependentField.getColumnName());
                                    }

                                    if (!propTableIdFields.containsKey(propTableName)) {
                                        propTableIdFields.put(propTableName, new LinkedList<>());
                                    }

                                    propTableIdFields.get(propTableName).add(requiredFieldColumn);
                                }

                            }

                        }
                    }
                }
            }

            for(String tableName : fileColumnsByTableName.keySet()) {

                if(propTableIdFields.containsKey(tableName)) {

                    List<GobiiFileColumn> propFileColumns =  fileColumnsByTableName.get(tableName);

                    List<GobiiFileColumn> requiredFileColumns = propTableIdFields.get(tableName);

                    requiredFileColumns.addAll(propFileColumns);

                    fileColumnsByTableName.put(tableName, requiredFileColumns);
                }

            }

            return fileColumnsByTableName;

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new GobiiDomainException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.UNKNOWN, "Server error");
        }
    }

    /**
     * Returns the prop table required properties for Germplasm and Dnasample table.
     * @return map object with table name to which prop column belong to as key and
     * a hash set of columns to uniquely identify the  table ot which it belongs to.
     */
    private Map<String, Object> getPropTableRequiredFields() {


        Map<String, Object> dependencyMap = new HashMap<>();

        try {

            ConfigSettings configSettings = new ConfigSettings();

            String fileSystemRoot = configSettings.getFileSystemRoot();

            ObjectMapper objMapper = new ObjectMapper();

            /**
             * TODO: Make the file path configurable
             */

            //Create config required field objects from json file
            String requiredFieldsFilePath = LineUtils.terminateDirectoryPath(fileSystemRoot)
                    + "/config/required_columns.json";

            InputStream jsonInputStream = new FileInputStream(requiredFieldsFilePath);

            Map<String, List<EntityFieldBean> > requiredFieldMap = objMapper.readValue(
                    jsonInputStream, new TypeReference<Map<String, List<EntityFieldBean>>>(){});


            for(String dependentTableName : requiredFieldMap.keySet()) {

                for(EntityFieldBean refIdField : requiredFieldMap.get(dependentTableName)) {

                    Map dependentFieldsMap =  new HashMap();

                    if(dependencyMap.containsKey(refIdField.getTableName())) {
                        dependentFieldsMap = (HashMap) dependencyMap.get(refIdField.getTableName());
                    }
                    else {
                        dependencyMap.put(refIdField.getTableName(), dependentFieldsMap);
                    }

                    List<EntityFieldBean> dependentFields = new ArrayList<>();

                    if(dependentFieldsMap.containsKey(refIdField.getColumnName())) {
                        dependentFields = (ArrayList) dependentFieldsMap.get(refIdField.getColumnName());
                    }
                    else {
                        dependentFieldsMap.put(refIdField.getColumnName(), dependentFields);
                    }

                    EntityFieldBean dependentField = new EntityFieldBean();
                    dependentField.setTableName(dependentTableName);
                    if(refIdField.getInstructionFileColumnName() == null ||
                            refIdField.getInstructionFileColumnName().isEmpty()) {
                        dependentField.setColumnName(refIdField.getColumnName());
                    }
                    else {
                        dependentField.setColumnName(refIdField.getInstructionFileColumnName());
                    }

                    dependentFields.add(dependentField);
                }
            }

        }
        catch(Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new GobiiDomainException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.UNKNOWN, "Server error");
        }

        return dependencyMap;
    }
}
