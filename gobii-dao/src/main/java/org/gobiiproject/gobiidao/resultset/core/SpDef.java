package org.gobiiproject.gobiidao.resultset.core;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phil on 4/18/2016.
 */
public abstract class SpDef {

    public SpDef(String callString) {
        this.callString = callString;
    }

    private String callString;
    private List<SpParamDef> spParamDefs = new ArrayList<>();

    public SpParamDef addParamDef(String paramName, Type paramType) {

        SpParamDef returnVal = new SpParamDef(paramName,paramType);
        spParamDefs.add(returnVal);
        returnVal.setOrderIdx(spParamDefs.size()); // sp indices are 1 based anyway
        return returnVal;

    }

    public String getCallString() {
        return callString;
    }

    public List<SpParamDef> getSpParamDefs() {
        return spParamDefs;
    }
}
