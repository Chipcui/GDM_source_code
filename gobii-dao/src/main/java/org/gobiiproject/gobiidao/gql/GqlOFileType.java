package org.gobiiproject.gobiidao.gql;

/**
 * Created by Phil on 5/13/2016.
 * These are intended to codify cv term names. You cannot add new values unless there exists a corresponding cv term in the database
 *
 */


public enum GqlOFileType {

    NONE("none"),
    IO_FILE_STD_OUT(".out"),
    IO_FILE_STD_ERR(".err");

    private String ioName;

    GqlOFileType(String ioName) {
        this.ioName = ioName;
    }

    public String getIoName() {
        return this.ioName;
    }

    public static GqlOFileType byValue(String val) {

        GqlOFileType returnVal = null;

        for (GqlOFileType currentVertexNameType : values()) {
            if (currentVertexNameType.getIoName().equals(val)) {
                returnVal =  currentVertexNameType;
                break;
            }
        }

        return returnVal;

    } // end byValue()

} // end enum
