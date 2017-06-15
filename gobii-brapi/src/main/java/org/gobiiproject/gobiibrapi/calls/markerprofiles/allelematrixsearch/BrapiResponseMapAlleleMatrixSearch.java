package org.gobiiproject.gobiibrapi.calls.markerprofiles.allelematrixsearch;

import org.gobiiproject.gobidomain.services.ExtractorInstructionFilesService;
import org.gobiiproject.gobiibrapi.calls.markerprofiles.allelematrices.BrapiResponseAlleleMatrices;
import org.gobiiproject.gobiibrapi.calls.markerprofiles.allelematrices.BrapiResponseAlleleMatricesItem;
import org.gobiiproject.gobiibrapi.core.common.BrapiMetaData;
import org.gobiiproject.gobiimodel.dto.instructions.GobiiFilePropNameId;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiDataSetExtract;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiExtractorInstruction;
import org.gobiiproject.gobiimodel.headerlesscontainer.ExtractorInstructionFilesDTO;
import org.gobiiproject.gobiimodel.types.GobiiExtractFilterType;
import org.gobiiproject.gobiimodel.types.GobiiFileType;
import org.gobiiproject.gobiimodel.types.GobiiJobStatus;
import org.gobiiproject.gobiimodel.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Phil on 12/15/2016.
 */
public class BrapiResponseMapAlleleMatrixSearch {

    @Autowired
    private ExtractorInstructionFilesService extractorInstructionFilesService;

    public BrapiMetaData search(String crop, String matrixDbId) {

        BrapiMetaData brapiMetaData = new BrapiMetaData();

        Integer dataSetId = Integer.parseInt(matrixDbId);

        ExtractorInstructionFilesDTO extractorInstructionFilesDTO = new ExtractorInstructionFilesDTO();
        GobiiExtractorInstruction gobiiExtractorInstruction = new GobiiExtractorInstruction();
        GobiiDataSetExtract gobiiDataSetExtract = new GobiiDataSetExtract();
        gobiiDataSetExtract.setGobiiFileType(GobiiFileType.FLAPJACK);
        gobiiDataSetExtract.setGobiiExtractFilterType(GobiiExtractFilterType.WHOLE_DATASET);
        gobiiDataSetExtract.setDataSet(new GobiiFilePropNameId(dataSetId, null));
        gobiiExtractorInstruction.getDataSetExtracts().add(gobiiDataSetExtract);
        gobiiExtractorInstruction.setContactId(1);
        extractorInstructionFilesDTO.getGobiiExtractorInstructions().add(gobiiExtractorInstruction);

        String jobId = DateUtils.makeDateIdString();
        extractorInstructionFilesDTO.setInstructionFileName(jobId);



        ExtractorInstructionFilesDTO extractorInstructionFilesDTONew = extractorInstructionFilesService
                .createInstruction(crop, extractorInstructionFilesDTO);

        brapiMetaData.addStatusMessage("asynchid", extractorInstructionFilesDTONew.getJobId());

        return brapiMetaData;
    }

    public BrapiMetaData getStatus(String crop, String jobId) {

        BrapiMetaData brapiMetaData = new BrapiMetaData();

        ExtractorInstructionFilesDTO extractorInstructionFilesDTONew = extractorInstructionFilesService
                .getStatus(crop,jobId);

        GobiiJobStatus gobiiJobStatus = extractorInstructionFilesDTONew
                .getGobiiExtractorInstructions()
                .get(0)
                .getDataSetExtracts()
                .get(0)
                .getGobiiJobStatus();

        String brapiAsynchStatus = null;
        switch (gobiiJobStatus) {

            case FAILED:
                brapiAsynchStatus = "FAILED";
                break;

            case STARTED:
                brapiAsynchStatus = "PENDING";
                break;

            case COMPLETED:
                brapiAsynchStatus = "FINISHED";
                break;

            case IN_PROGRESS:
                brapiAsynchStatus = "INPROCESS";
                break;

        }


        brapiMetaData.addStatusMessage("asynchstatus", brapiAsynchStatus);

        return brapiMetaData;
    }

}
