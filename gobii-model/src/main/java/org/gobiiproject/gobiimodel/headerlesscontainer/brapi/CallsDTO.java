package org.gobiiproject.gobiimodel.headerlesscontainer.brapi;

import org.gobiiproject.gobiimodel.headerlesscontainer.DTOBase;
import org.gobiiproject.gobiimodel.types.RestMethodTypes;

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


    private String call;
    private List<RestMethodTypes> methods;


}
