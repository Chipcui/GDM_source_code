package org.gobiiproject.gobiiclient.core.common;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.gobiiproject.gobiiapimodel.restresources.common.ResourceParam;
import org.gobiiproject.gobiimodel.types.RestMethodTypes;
import org.gobiiproject.gobiimodel.types.GobiiHttpHeaderNames;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

/**
 * This class provides generic HTTP rest-oriented client functionality.
 * For example, it takes a plain string for a post and converts it to
 * an HTTP entity as the POST body. It is vital that this class not contain
 * any GOBII specific functionality. For example, serialization and deserialization
 * of GOBII POJOs is handled by another class that consumes this class. We
 * want this class to be generic so that it can serve as the workhorse for
 * all client operations performed by GOBII clients with arbitrary web services,
 * not just GOBII ones.
 *
 */
public class HttpCore {

    private String host = null;
    private Integer port = null;
    private boolean logJson = false;


    public HttpCore(String host,
                    Integer port) {

        this.host = host;
        this.port = port;
    }


    Logger LOGGER = LoggerFactory.getLogger(HttpCore.class);


    String token = null;

    public String getToken() {
        return token;
    }

    URIBuilder getBaseBuilder() throws Exception {
        return (new URIBuilder().setScheme("http")
                .setHost(host)
                .setPort(port));
    }

    private URI makeUri(RestUri restUri) throws Exception {

        URI returnVal;

        URIBuilder baseBuilder = getBaseBuilder()
                .setPath(restUri.makeUrl());

        for (ResourceParam currentParam : restUri.getRequestParams()) {
            baseBuilder.addParameter(currentParam.getName(), currentParam.getValue());
        }

        returnVal = baseBuilder.build();

        return (returnVal);

    }

    private void setAuthenticationHeaders(HttpUriRequest httpUriRequest,
                                          String userName,
                                          String password) {

        httpUriRequest.addHeader(GobiiHttpHeaderNames.HEADER_USERNAME, userName);
        httpUriRequest.addHeader(GobiiHttpHeaderNames.HEADER_PASSWORD, password);
    }

    private void setTokenHeader(HttpUriRequest httpUriRequest) {

        httpUriRequest.addHeader(GobiiHttpHeaderNames.HEADER_TOKEN, this.token);

    }

    private HttpResponse submitUriRequest(HttpUriRequest httpUriRequest) throws Exception {

        httpUriRequest.addHeader("Content-Type", "application/json");
        httpUriRequest.addHeader("Accept", "application/json");

        return (HttpClientBuilder.create().build().execute(httpUriRequest));

    }// submitUriRequest()


    private Header getHeader(Header[] headers, String headerName) {
        Header returnVal = null;

        boolean foundHeader = false;
        for (int idx = 0; idx < headers.length && foundHeader == false; idx++) {
            Header currentHeader = headers[idx];

            if (headerName.equals(currentHeader.getName())) {
                returnVal = currentHeader;
                foundHeader = true;
            }
        }//iterate headers

        return (returnVal);

    }// getHeader()

    private HttpResponse authenticateWithUser(URI uri, String userName, String password) throws Exception {

        HttpResponse returnVal = null;

        HttpPost postRequest = new HttpPost(uri);
        this.setHttpBody(postRequest, "empty");
        this.setAuthenticationHeaders(postRequest, userName, password);
        returnVal = this.submitUriRequest(postRequest);

        if (HttpStatus.SC_OK != returnVal.getStatusLine().getStatusCode()) {
            throw new Exception("Request did not succeed with http status code "
                    + returnVal.getStatusLine().getStatusCode()
                    + "; the url is: "
                    + uri.toString());
        }


        return (returnVal);

    }//authenticateWithUser()

    public boolean authenticate(RestUri restUri, String userName, String password) throws Exception {

        boolean returnVal = true;

        URI uri = makeUri(restUri);
        HttpResponse response = authenticateWithUser(uri, userName, password);

        Header tokenHeader = getHeader(response.getAllHeaders(), GobiiHttpHeaderNames.HEADER_TOKEN);
        this.token = tokenHeader.getValue();

        returnVal = (false == LineUtils.isNullOrEmpty(this.token));

        if (returnVal == false) {
            LOGGER.error("Unable to get authentication token for user " + userName);
        }

        return returnVal;

    } // authenticate()


    private HttpMethodResult submitHttpMethod(HttpRequestBase httpRequestBase,
                                              RestUri restUri) throws Exception {

        HttpMethodResult returnVal = new HttpMethodResult();

        HttpResponse httpResponse;

        URI uri = makeUri(restUri);
        httpRequestBase.setURI(uri);


        this.setTokenHeader(httpRequestBase);
        httpResponse = submitUriRequest(httpRequestBase);

        int responseCode = httpResponse.getStatusLine().getStatusCode();
        String reasonPhrase = httpResponse.getStatusLine().getReasonPhrase();
        returnVal.setResponse(responseCode, reasonPhrase, uri);


        if (HttpStatus.SC_NOT_FOUND != responseCode &&
                HttpStatus.SC_BAD_REQUEST != responseCode &&
                HttpStatus.SC_METHOD_NOT_ALLOWED != responseCode &&
                HttpStatus.SC_UNAUTHORIZED != responseCode) {

            InputStream inputStream = httpResponse.getEntity().getContent();
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(inputStream));


            StringBuilder stringBuilder = new StringBuilder();
            String currentLine = null;
            while ((currentLine = bufferedReader.readLine()) != null) {
                stringBuilder.append(currentLine);
            }


            JsonParser parser = new JsonParser();

            String jsonAsString = stringBuilder.toString();

            JsonObject jsonObject = parser.parse(jsonAsString).getAsJsonObject();

            returnVal.setPayLoad(jsonObject);
        }


        ///returnVal.setPayLoad(getJsonFromInputStream(httpResponse.getEntity().getContent()));

        return returnVal;
    }

    private void setHttpBody(HttpEntityEnclosingRequestBase httpEntityEnclosingRequestBase,
                             String body) throws Exception {

        if (!LineUtils.isNullOrEmpty(body)) {
            StringEntity input = new StringEntity(body);
            httpEntityEnclosingRequestBase.setEntity(input);
        }

    }

    private void logRequest(RestMethodTypes restMethodType,
                            RestUri restUri,
                            String body,
                            HttpMethodResult httpMethodResult) throws Exception {

        if (logJson) {

            System.out.println("=========method: " + restMethodType.toString() + " on resource: " + restUri.makeUrl());

            if (!LineUtils.isNullOrEmpty(body)) {

                System.out.println("body:");
                System.out.println(body);
            }

            System.out.println("Response: ");

            if (httpMethodResult.getPayLoad() != null) {
                System.out.println(httpMethodResult.getPayLoad().toString());
            } else {
                System.out.println("Null payload");
            }

            System.out.println();
            System.out.println();
        }

    }

    public HttpMethodResult get(RestUri restUri) throws Exception {

        HttpMethodResult returnVal = this.submitHttpMethod(new HttpGet(), restUri);
        this.logRequest(RestMethodTypes.GET, restUri, null, returnVal);
        return returnVal;

    }

    public HttpMethodResult post(RestUri restUri,
                                 String body) throws Exception {

        HttpMethodResult returnVal;

        HttpPost httpPost = new HttpPost();
        this.setHttpBody(httpPost, body);
        returnVal = this.submitHttpMethod(httpPost, restUri);
        this.logRequest(RestMethodTypes.POST, restUri, body, returnVal);

        return returnVal;
    }

    public HttpMethodResult put(RestUri restUri,
                                String body) throws Exception {

        HttpMethodResult returnVal;
        HttpPut httpPut = new HttpPut();
        this.setHttpBody(httpPut, body);
        returnVal = this.submitHttpMethod(httpPut, restUri);
        this.logRequest(RestMethodTypes.PUT, restUri, body, returnVal);
        return returnVal;
    }

    public HttpMethodResult patch(RestUri restUri,
                                  String body) throws Exception {

        HttpPatch httpPatch = new HttpPatch();
        this.setHttpBody(httpPatch, body);
        return this.submitHttpMethod(httpPatch, restUri);
    }


    public HttpMethodResult delete(RestUri restUri) throws Exception {

        return this.submitHttpMethod(new HttpDelete(), restUri);
    }

}
