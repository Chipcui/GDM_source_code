// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-24
// ************************************************************************
package org.gobiiproject.gobiimodel.dto;

import org.gobiiproject.gobiimodel.dto.header.DtoHeaderAuth;
import org.gobiiproject.gobiimodel.dto.header.DtoHeaderResponse;

import java.io.Serializable;
import java.sql.SQLType;

/**
 * Created by Phil on 3/24/2016.
 */
abstract public class DtoMetaData implements Serializable {

    public DtoMetaData() {}

    public DtoMetaData(ProcessType processType) {
        this.processType = processType;
    }

    public enum ProcessType {CREATE, READ, UPDATE, DELETE}

    private ProcessType processType = ProcessType.READ;
    private DtoHeaderAuth dtoHeaderAuth = new DtoHeaderAuth();
    private DtoHeaderResponse dtoHeaderResponse = new DtoHeaderResponse();

    // in order for the private properties to be serialized into the JSON,
    // we must use the proper bean naming convention for these getters --
    // this is how the Jackson serializer knows that it should serialize what
    // would other wise be private propeties.
    public DtoHeaderAuth getDtoHeaderAuth() {
        return dtoHeaderAuth;
    }

    public DtoHeaderResponse getDtoHeaderResponse() {
        return dtoHeaderResponse;
    }

    public ProcessType getProcessType() {
        return processType;
    }

    public void setProcessType(ProcessType processType) {
        this.processType = processType;
    }


} // DtoMetaData
