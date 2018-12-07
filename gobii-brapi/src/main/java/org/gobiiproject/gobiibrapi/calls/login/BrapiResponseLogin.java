package org.gobiiproject.gobiibrapi.calls.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.gobiiproject.gobiibrapi.core.responsemodel.BrapiResponseEnvelope;

/**
 * Created by Phil on 12/15/2016.
 */
public class BrapiResponseLogin extends BrapiResponseEnvelope {


    private String accessToken;
    private String expiresIn;
    private String userDisplayName;

    @JsonProperty("access_token")
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @JsonProperty("expires_in")
    public String getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(String expiresIn) {
        this.expiresIn = expiresIn;
    }

    @JsonProperty("userDisplayName")
    public String getUserDisplayName() {
        return userDisplayName;
    }

    public void setUserDisplayName(String userDisplayName) {
        this.userDisplayName = userDisplayName;
    }

}

/*
*
* {
  "access_token": "R6gKDBRxM4HLj6eGi4u5HkQjYoIBTPfvtZzUD8TUzg4",
  "expires_in": "The lifetime in seconds of the access token",
  "metadata": {
    "datafiles": [],
    "pagination": {
      "currentPage": 0,
      "pageSize": 0,
      "totalCount": 0,
      "totalPages": 0
    },
    "status": []
  },
  "userDisplayName": "John Smith"
}
*
* */