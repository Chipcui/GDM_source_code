package org.gobiiproject.gobiiapimodel.payload.sampletracking;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by VCalaminos on 7/22/2019.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BrApiResult<T> {

    private List<T> data = new ArrayList<T>();

    public List<T> getData() {
        return this.data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
