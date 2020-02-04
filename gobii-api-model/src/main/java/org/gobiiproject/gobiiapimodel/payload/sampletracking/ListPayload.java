package org.gobiiproject.gobiiapimodel.payload.sampletracking;

import java.util.ArrayList;
import java.util.List;

public class ListPayload<T> {

    private List<T> data = new ArrayList<>();

    public List<T> getData() {
        return this.data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
