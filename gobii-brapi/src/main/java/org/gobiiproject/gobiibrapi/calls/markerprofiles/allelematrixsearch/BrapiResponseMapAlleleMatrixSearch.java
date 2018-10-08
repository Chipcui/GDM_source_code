package org.gobiiproject.gobiibrapi.calls.markerprofiles.allelematrixsearch;

import org.gobiiproject.gobidomain.services.CvService;
import org.gobiiproject.gobidomain.services.ExtractorInstructionFilesService;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiapimodel.restresources.gobii.GobiiUriFactory;
import org.gobiiproject.gobiiapimodel.types.GobiiControllerType;
import org.gobiiproject.gobiiapimodel.types.RestRequestId;
import org.gobiiproject.gobiibrapi.core.common.BrapiMetaData;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.cvnames.CvGroup;
import org.gobiiproject.gobiimodel.cvnames.DatasetType;
import org.gobiiproject.gobiimodel.cvnames.JobProgressStatusType;
import org.gobiiproject.gobiimodel.dto.entity.children.PropNameId;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.CvDTO;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.ExtractorInstructionFilesDTO;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiDataSetExtract;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiExtractorInstruction;
import org.gobiiproject.gobiimodel.types.GobiiExtractFilterType;
import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;
import org.gobiiproject.gobiimodel.types.GobiiFileType;
import org.gobiiproject.gobiimodel.types.GobiiSampleListType;
import org.gobiiproject.gobiimodel.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Phil on 12/15/2016.
 */
public class BrapiResponseMapAlleleMatrixSearch {

    @Autowired
    private ExtractorInstructionFilesService extractorInstructionFilesService;

    @Autowired
    private CvService cvService;

    private PropNameId getDatatypeIdForName(DatasetType datasetType) {


        List<CvDTO> datasetCvs = cvService.getCvsByGroupName(CvGroup.DATASET_TYPE.getCvGroupName());

        AtomicInteger datasetTypeId = new AtomicInteger(0);
        datasetCvs
                .stream()
                .filter(cv -> cv.getTerm().equals(datasetType.getDatasetTypeName()))
                .forEach(cv -> datasetTypeId.set(cv.getCvId()));

        if (datasetTypeId.get() == 0) {
            throw new GobiiException("Unknown datatype: " + datasetType.getDatasetTypeName());
        }


        return new PropNameId(datasetTypeId.get(), datasetType.getDatasetTypeName());
    }

    private BrapiMetaData createExtractorInstruction(String crop, GobiiDataSetExtract gobiiDataSetExtract) {

        BrapiMetaData brapiMetaData = new BrapiMetaData();

        ExtractorInstructionFilesDTO extractorInstructionFilesDTO = new ExtractorInstructionFilesDTO();
        GobiiExtractorInstruction gobiiExtractorInstruction = new GobiiExtractorInstruction();

        gobiiExtractorInstruction.getDataSetExtracts().add(gobiiDataSetExtract);
        gobiiExtractorInstruction.setContactId(1);
        extractorInstructionFilesDTO.getGobiiExtractorInstructions().add(gobiiExtractorInstruction);

        String jobId = DateUtils.makeDateIdString();
        extractorInstructionFilesDTO.setJobId(jobId);

        ExtractorInstructionFilesDTO extractorInstructionFilesDTONew = extractorInstructionFilesService
                .createInstruction(crop, extractorInstructionFilesDTO);

        brapiMetaData.addStatusMessage("asynchid", extractorInstructionFilesDTONew.getJobId());

        return brapiMetaData;
    }

    public BrapiMetaData searchByMatrixDbId(String crop, String matrixDbId) {

        GobiiDataSetExtract gobiiDataSetExtract = new GobiiDataSetExtract();
        gobiiDataSetExtract.setGobiiFileType(GobiiFileType.FLAPJACK);
        gobiiDataSetExtract.setGobiiExtractFilterType(GobiiExtractFilterType.WHOLE_DATASET);
        gobiiDataSetExtract.setDataSet(new PropNameId(Integer.parseInt(matrixDbId), null));

        return createExtractorInstruction(crop, gobiiDataSetExtract);
    }

    public BrapiMetaData searchByExternalCode(String crop, List<String> externalCodes) {

        GobiiDataSetExtract gobiiDataSetExtract = new GobiiDataSetExtract();
        gobiiDataSetExtract.setGobiiFileType(GobiiFileType.FLAPJACK);
        gobiiDataSetExtract.setGobiiExtractFilterType(GobiiExtractFilterType.BY_SAMPLE);
        gobiiDataSetExtract.setGobiiSampleListType(GobiiSampleListType.EXTERNAL_CODE);
        gobiiDataSetExtract.setSampleList(externalCodes);
        gobiiDataSetExtract.setDataSet(null);
        gobiiDataSetExtract.setProject(null);
        gobiiDataSetExtract.setPrincipleInvestigator(null);
        gobiiDataSetExtract.setGobiiDatasetType(this.getDatatypeIdForName(DatasetType.CV_DATASETTYPE_CO_DOMINANT_NON_NUCLEOTIDE));

        return createExtractorInstruction(crop, gobiiDataSetExtract);
    }

    public BrapiMetaData searchByGermplasm(String crop, List<String> germplasmList) {

        GobiiDataSetExtract gobiiDataSetExtract = new GobiiDataSetExtract();
        gobiiDataSetExtract.setGobiiFileType(GobiiFileType.FLAPJACK);
        gobiiDataSetExtract.setGobiiExtractFilterType(GobiiExtractFilterType.BY_SAMPLE);
        gobiiDataSetExtract.setGobiiSampleListType(GobiiSampleListType.GERMPLASM_NAME);
        gobiiDataSetExtract.setSampleList(germplasmList);
        gobiiDataSetExtract.setDataSet(null);
        return createExtractorInstruction(crop, gobiiDataSetExtract);
    }

    public BrapiMetaData searchByDNASample(String crop, List<String> dnaSamples) {

        GobiiDataSetExtract gobiiDataSetExtract = new GobiiDataSetExtract();
        gobiiDataSetExtract.setGobiiFileType(GobiiFileType.FLAPJACK);
        gobiiDataSetExtract.setGobiiExtractFilterType(GobiiExtractFilterType.BY_SAMPLE);
        gobiiDataSetExtract.setGobiiSampleListType(GobiiSampleListType.DNA_SAMPLE);
        gobiiDataSetExtract.setSampleList(dnaSamples);
        gobiiDataSetExtract.setDataSet(null);
        return createExtractorInstruction(crop, gobiiDataSetExtract);
    }

    public BrapiMetaData getStatus(String crop, String jobId, HttpServletRequest request) throws Exception {
        BrapiMetaData brapiMetaData = new BrapiMetaData();

        ExtractorInstructionFilesDTO extractorInstructionFilesDTONew = extractorInstructionFilesService
                .getStatus(crop, jobId);

        if ((extractorInstructionFilesDTONew
                .getGobiiExtractorInstructions().size() > 0) &&
                (extractorInstructionFilesDTONew
                        .getGobiiExtractorInstructions().get(0).getDataSetExtracts().size() > 0)) {

            GobiiDataSetExtract gobiiDataSetExtract = extractorInstructionFilesDTONew
                    .getGobiiExtractorInstructions()
                    .get(0)
                    .getDataSetExtracts()
                    .get(0);

            JobProgressStatusType jobProgressStatus = gobiiDataSetExtract.getGobiiJobStatus();

            String brapiAsynchStatus = null;
            switch (jobProgressStatus) {

                case CV_PROGRESSSTATUS_PENDING:
                    brapiAsynchStatus = "PENDING";
                    break;

                case CV_PROGRESSSTATUS_INPROGRESS:
                case CV_PROGRESSSTATUS_METADATAEXTRACT:
                case CV_PROGRESSSTATUS_FINALASSEMBLY:
                case CV_PROGRESSSTATUS_QCPROCESSING:
                case CV_PROGRESSSTATUS_TRANSFORMATION:
                case CV_PROGRESSSTATUS_DIGEST:
                case CV_PROGRESSSTATUS_NOSTATUS: // I _think_ this only occurs before the digester/extractor sees the instruction file
                        brapiAsynchStatus = "INPROCESS";
                    break;

                case CV_PROGRESSSTATUS_COMPLETED:
                    brapiAsynchStatus = "FINISHED";
                    break;

                case CV_PROGRESSSTATUS_FAILED:
                case CV_PROGRESSSTATUS_ABORTED:
                case CV_PROGRESSSTATUS_METADATALOAD: // these are load status that do not apply to extract TODO: Add an error message for this condition
                case CV_PROGRESSSTATUS_MATRIXLOAD:
                case CV_PROGRESSSTATUS_VALIDATION:
                    brapiAsynchStatus = "FAILED";
                    break;


            }

            if ((extractorInstructionFilesDTONew.getGobiiExtractorInstructions().size() > 0) &&
                    (extractorInstructionFilesDTONew.getGobiiExtractorInstructions().get(0).getDataSetExtracts().size() > 0)) {

                // Add the extracted files to response only when job is completed.
                if (brapiAsynchStatus.equals("FINISHED")) {
                    if (gobiiDataSetExtract.getExtractedFiles().size() > 0) {

                        for (File currentFile : gobiiDataSetExtract.getExtractedFiles()) {

                            if( currentFile.getName().toLowerCase().contains("dataset.genotype")) {
                                // first make the http link
                                RestUri restUri = new GobiiUriFactory(request.getServerName(), request.getServerPort(),
                                        request.getContextPath(), GobiiControllerType.GOBII)
                                        .resourceColl(RestRequestId.URL_FILES)
                                        .addUriParam("gobiiJobId", jobId)
                                        .addUriParam("destinationType", GobiiFileProcessDir.EXTRACTOR_OUTPUT.toString().toLowerCase())
                                        .addQueryParam("fileName", currentFile.getName());

                                String fileUri = restUri.makeUrlComplete();
                                brapiMetaData.getDatafiles().add(fileUri);
                            }

                            // now the absolute path to the file
//                            String filePath = FilenameUtils.normalize(currentFile.getAbsolutePath());
//                            brapiMetaData.getDatafiles().add(filePath);
                        }
                    } else {
                        brapiMetaData.addStatusMessage("error", "There are no extracted files for the directory: " + gobiiDataSetExtract.getExtractDestinationDirectory());
                    }
                } else if (brapiAsynchStatus.equals("FAILED")) {
                    brapiMetaData.addStatusMessage("error", gobiiDataSetExtract.getLogMessage());
                }
            } else {
                brapiMetaData.addStatusMessage("error", "There are not extractor instructions for job : " + jobId);
            }

            brapiMetaData.addStatusMessage("asynchstatus", brapiAsynchStatus);

        }

        return brapiMetaData;

    } // getStatus()

}