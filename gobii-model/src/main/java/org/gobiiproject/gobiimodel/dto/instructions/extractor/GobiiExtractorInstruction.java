package org.gobiiproject.gobiimodel.dto.instructions.extractor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phil on 6/6/2016.
 */
public class GobiiExtractorInstruction {

    String extractDestinationDirectory = null;
    List<GobiiDataSetExtract> dataSetExtracts = new ArrayList<>();

    public String getExtractDestinationDirectory() {
        return extractDestinationDirectory;
    }

    public void setExtractDestinationDirectory(String extractDestinationDirectory) {
        this.extractDestinationDirectory = extractDestinationDirectory;
    }

    public List<GobiiDataSetExtract> getDataSetExtracts() {
        return dataSetExtracts;
    }

    public void setDataSetExtracts(List<GobiiDataSetExtract> dataSetExtracts) {
        this.dataSetExtracts = dataSetExtracts;
    }
}
