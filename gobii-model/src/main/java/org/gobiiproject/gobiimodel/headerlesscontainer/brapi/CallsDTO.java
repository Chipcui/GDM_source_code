package org.gobiiproject.gobiimodel.headerlesscontainer.brapi;

import org.gobiiproject.gobiimodel.headerlesscontainer.DTOBase;
import org.gobiiproject.gobiimodel.types.RestMethodTypes;
import org.gobiiproject.gobiimodel.types.brapi.BrapiDataTypes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phil on 4/8/2016.
 */
public class CallsDTO extends DTOBase {

    @Override
    public Integer getId() {
        return 1;
    }

    @Override
    public void setId(Integer id) {
    }

    public CallsDTO() {}

    public CallsDTO(String call,
                    List<RestMethodTypes> methods,
                    List<BrapiDataTypes> dataTypes ) {

        this.call = call;
        this.methods = methods;
        this.dataTypes = dataTypes;

    }

    private String call;
    private List<RestMethodTypes> methods = new ArrayList<>();
    private List<BrapiDataTypes> dataTypes = new ArrayList<>();

    public String getCall() {
        return call;
    }

    public void setCall(String call) {
        this.call = call;
    }

    public List<RestMethodTypes> getMethods() {
        return methods;
    }

    public void setMethods(List<RestMethodTypes> methods) {
        this.methods = methods;
    }

    public List<BrapiDataTypes> getDataTypes() {
        return dataTypes;
    }

    public void setDataTypes(List<BrapiDataTypes> dataTypes) {
        this.dataTypes = dataTypes;
    }
}
