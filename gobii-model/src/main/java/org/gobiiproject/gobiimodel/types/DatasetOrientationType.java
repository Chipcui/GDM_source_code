package org.gobiiproject.gobiimodel.types;

public enum DatasetOrientationType {
    MARKER_FAST("marker_fast"), SAMPLE_FAST("sample_fast"), NONE("none");

    private String value;

    DatasetOrientationType(String n) {
        this.value = n;
    }

    public DatasetOrientationType flip() {
        switch(this) {
            case MARKER_FAST:
                return SAMPLE_FAST;
            case SAMPLE_FAST:
                return MARKER_FAST;
            case NONE:
                return NONE;
        }
        return null;
    }

    @Override
    public String toString() {
        return value;
    }
}
