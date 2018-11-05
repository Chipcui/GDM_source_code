package org.gobiiproject.gobiimodel.dto.instructions.loader;

import org.gobiiproject.gobiimodel.cvnames.JobPayloadType;
import org.gobiiproject.gobiimodel.dto.base.DTOBase;
import org.gobiiproject.gobiimodel.types.GobiiFileType;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Container for file-system specific information used in conversation with the server.
 * Created by Phil on 4/12/2016.
 */
public class MatrixDTO extends DTOBase {

    private Map<JobPayloadType, MatrixUrlDTO> urlsByPayloadType = new HashMap<>();
    private String vendorName = null;
    private String protocolName = null;
    private String projectName = null;
    private String experimentName = null;
    private String datasetName = null;
    private String referenceGenomeName = null;


    @Override
    public Integer getId() {
        return null;
    }

    @Override
    public void setId(Integer id) {

    }

    public Map<JobPayloadType, MatrixUrlDTO> getUrlsByPayloadType() {
        return urlsByPayloadType;
    }

    public void setUrlsByPayloadType(Map<JobPayloadType, MatrixUrlDTO> urlsByPayloadType) {
        this.urlsByPayloadType = urlsByPayloadType;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getProtocolName() {
        return protocolName;
    }

    public void setProtocolName(String protocolName) {
        this.protocolName = protocolName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getExperimentName() {
        return experimentName;
    }

    public void setExperimentName(String experimentName) {
        this.experimentName = experimentName;
    }

    public String getDatasetName() {
        return datasetName;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    public String getReferenceGenomeName() {
        return referenceGenomeName;
    }

    public void setReferenceGenomeName(String referenceGenomeName) {
        this.referenceGenomeName = referenceGenomeName;
    }
}
