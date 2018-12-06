package org.gobiiproject.gobiibrapi.calls.login;

import org.gobiiproject.gobiiapimodel.types.GobiiHttpHeaderNames;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by Phil on 12/18/2016.
 */
public class BrapiResponseMapLogin {


    public BrapiResponseLogin getLoginInfo(BrapiRequestLogin brapiRequestLogin, HttpServletResponse response) throws Exception {

        BrapiResponseLogin returnVal = new BrapiResponseLogin();

        String tokenHeaderVal = response.getHeader(GobiiHttpHeaderNames.HEADER_NAME_TOKEN);

        returnVal.setAccessToken(tokenHeaderVal);
        returnVal.setUserDisplayName(brapiRequestLogin.getUserName());
        returnVal.setExpiresIn("3600");

        return returnVal;
    }


}
