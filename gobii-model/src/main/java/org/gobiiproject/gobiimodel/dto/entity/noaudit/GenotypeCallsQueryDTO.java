package org.gobiiproject.gobiimodel.dto.entity.noaudit;

import java.util.ArrayList;
import java.util.List;

public class GenotypeCallsQueryDTO {


    private List<String> callSetDbIds = new ArrayList<>();

    private List<String> variantDbIds = new ArrayList<>();

    private List<String> germplasmDbIds = new ArrayList<>();

    private List<String> germplasmExternalCodes = new ArrayList<>();

    public List<String> getCallSetDbIds() {
        return callSetDbIds;
    }

    public void setCallSetDbIds(List<String> callSetDbIds) {
        this.callSetDbIds = callSetDbIds;
    }

    public List<String> getVariantDbIds() {
        return variantDbIds;
    }

    public void setVariantDbIds(List<String> variantDbIds) {
        this.variantDbIds = variantDbIds;
    }

    public List<String> getGermplasmDbIds() {
        return germplasmDbIds;
    }

    public void setGermplasmDbIds(List<String> germplasmDbIds) {
        this.germplasmDbIds = germplasmDbIds;
    }

    public List<String> getGermplasmExternalCodes() {
        return germplasmExternalCodes;
    }

    public void setGermplasmExternalCodes(List<String> germplasmExternalCodes) {
        this.germplasmExternalCodes = germplasmExternalCodes;
    }
}
