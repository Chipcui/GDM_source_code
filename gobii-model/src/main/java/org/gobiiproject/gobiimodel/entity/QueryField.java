package org.gobiiproject.gobiimodel.entity;


import org.hibernate.type.Type;

public class QueryField {

    public QueryField(String parameterName, Object parameterValue) {
        this.parameterName = parameterName;
        this.parameterValue = parameterValue;
    }

    public QueryField(String parameterName, Object parameterValue, Type parameterType) {
        this(parameterName, parameterValue);
        this.paramterType = parameterType;
    }

    private String parameterName;
    private Object parameterValue;
    private Type paramterType;

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public Object getParameterValue() {
        return parameterValue;
    }

    public void setParameterValue(Object parameterValue) {
        this.parameterValue = parameterValue;
    }

    public Type getParamterType() {
        return paramterType;
    }

    public void setParamterType(Type paramterType) {
        this.paramterType = paramterType;
    }
}
