package org.gobiiproject.gobiiapimodel.payload.sampletracking;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BrApiMasterListPayload<T>  extends BrApiMasterPayload {

    private BrApiResult<T> result = new BrApiResult<>();

    public BrApiMasterListPayload() {
    }

    public BrApiMasterListPayload(List<T> listData) {
        this.result.setData(listData);
    }


    public BrApiResult<T> getResult() {
        return this.result;
    }

    public void setResult(BrApiResult<T> result) {
        this.result = result;
    }


}
