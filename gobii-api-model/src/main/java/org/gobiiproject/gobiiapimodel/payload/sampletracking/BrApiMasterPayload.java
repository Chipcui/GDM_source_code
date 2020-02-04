package org.gobiiproject.gobiiapimodel.payload.sampletracking;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BrApiMasterPayload<T> {

    private BrApiMetaData metadata = new BrApiMetaData();
    private T result;

    public BrApiMasterPayload() {
    }

    public BrApiMasterPayload(T result) {
        this.result = result;
    }


    public T getResult() {
        return this.result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public BrApiMetaData getMetadata() {
        return this.metadata;
    }

    public void setMetadata(BrApiMetaData metaData) {
        this.metadata = metaData;
    }

}
