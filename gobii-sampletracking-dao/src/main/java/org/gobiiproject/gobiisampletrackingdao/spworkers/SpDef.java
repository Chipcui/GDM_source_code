package org.gobiiproject.gobiisampletrackingdao.spworkers;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SpDef {

    public SpDef(String callString, boolean returnsKey)
    {
        this.callString = callString;
        this.returnsKey = returnsKey;
    } // SpDef()

    public SpDef(String callString) {
        this.callString = callString;
        this.returnsKey = true;
    }


    protected String callString;

    private boolean returnsKey = true;

    private List<SpParamDef> spParamDefs = new ArrayList<>();

    public SpDef addParamDef(Integer paramIndex, Type paramType, Object paramValue) {

        SpParamDef returnVal = new SpParamDef(paramType);

        returnVal.setCurrentValue(paramValue);

        returnVal.setOrderIdx(paramIndex);

        spParamDefs.add(returnVal);

        return this;

    }

    public String getCallString() {
        return callString;
    }

    public List<SpParamDef> getSpParamDefs() {
        return spParamDefs;
    }

    public boolean isReturnsKey() {
        return returnsKey;
    }

}
