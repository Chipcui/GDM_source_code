package org.gobiiproject.gobiibrapi.core.common;

public class BrapiAsynchStatus {

    public String getAsynchId() {
        return asynchId;
    }

    public void setAsynchId(String asynchId) {
        this.asynchId = asynchId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTIme() {
        return endTIme;
    }

    public void setEndTIme(String endTIme) {
        this.endTIme = endTIme;
    }

    public String getPercentComplete() {
        return percentComplete;
    }

    public void setPercentComplete(String percentComplete) {
        this.percentComplete = percentComplete;
    }

    private String asynchId;
    private String status;
    private String startTime;
    private String endTIme;
    private String percentComplete;



}
