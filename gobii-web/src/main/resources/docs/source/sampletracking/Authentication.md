
Authenticates the user name and password and returns an authentication token to be used as an API key for the APIs.

Header Field | Type | Description
-------------|------|------------------
X-Username   | String | **Required** User name

X-Password   | String | **Required** Password

Successful operation will return below response with HTTP Status Code 200 Ok.

**Response Body Example**

```

        {
            "userName" : "foo",
            "token" : "token!",
            "gobiiCropType" : "bar"
            
        }
        
```

The "token" value from the response can be used as the value for X-Auth-Token header field for accessing other APIs




