package org.gobiiproject.gobiimodel.dto.system;

import java.util.ArrayList;
import java.util.List;

public class PagedListByCursor<T> {

    private List<T> listData = new ArrayList<>();
    private String nextPageToken;

    public List<T> getListData() {
        return listData;
    }

    public void setListData(List<T> listData) {
        this.listData = listData;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }
}
