package org.gobiiproject.gobiiweb.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.IOUtils;

import javax.naming.AuthenticationException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * The need for this class was occasioned by the implementation of Authentication for BRAPI.
 * The BRAPI API uses the body of the request to specify the username and password rather than
 * the request headers, as is done by the native GOBII API. The Spring security model calls for encapsulating
 * token creation in the filter chain; for GOBII, the class of interest is TokenAuthenticationFilter.
 * In the case of BRAPI authentication, the TokenAuthenticationFilter has to read the
 * body of the request in order to get the username and password. It get access to the input
 * stream quite easily; however, once the input stream is read, the stream's input buffer
 * is emptied, and subsequent processing of the filter chain results in an error because the request is
 * malformed (the source of the error is not easy to pinpoint unless you know what's going on with the filter).
 *
 * The goal of this class, therefore, is to provide a means by which the TokenAuthentication filter
 * can read the body of the request without emptying the input stream buffer.
 * The approach taken here follows that taken by the kind soul who posted this solution
 * from https://coderanch.com/t/364591/java/read-request-body-filter
 * The strategy is to to wrap a request so as to capture the input stream, such that it can be
 * read once and later be read again in a pseudo fashion by virtue of keeping the original payload.
 * A significant difference between the solution suggested there and what we need to do here is
 * that we need to preserve the input stream as a byte array: if we only preserve the request payload
 * as a string, the technique fails for binary files (e.g., excel spreadsheet).
 * Accordingly, we need to save the input stream to a local byte buffer, and create a new input stream
 * from the buffer as needed.
 *
 * However, if we copy the input stream to a local buffer for every request, we are incurring
 * unnecessary overhead for copying and storing the data; in the case of large file payloads,
 * the effect of this copying could be significant.
 *
 * The approach taken here is to only copy the the input stream to a buffer in the
 * case where there was a need to pre-read the body as a string in the TokenAuthenticationFilter.
 * This mechanism breaks down in the case where someone calls getRequestBodyAsString()
 * after calling getInputStream(). We shall proceed, for now, with the assumption that getRequestBodyAsString()
 * will only get called as it is now in TokenAuthenticationFilter, i.e.,
 * upon initial authentication through the BRAPI API.
 */
public class AuthenticationRequestWrapper
        extends HttpServletRequestWrapper {

    Logger LOGGER = LoggerFactory.getLogger(AuthenticationRequestWrapper.class);

    private String requestBody = null;

    private byte[] savedBytes = null;

    public AuthenticationRequestWrapper(HttpServletRequest request)
            throws AuthenticationException {

        super(request);

    }

    /**
     * Per the description of this class, above, if this method happens to get called before getRequestBodyAsString(),
     * the request's input stream will get cleared; if getRequestBodyAsString() is called subsequently, it will fail.
     */
    @Override
    public ServletInputStream getInputStream()
            throws IOException {

        if (this.savedBytes != null) {

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.savedBytes);

            return new ServletInputStream() {
                public int read() throws IOException {
                    return byteArrayInputStream.read();
                }
            };

        } else {
            return super.getInputStream();
        }
    }

    /***
     * See comments on getInputStream() and the class as a whole, above.
     */
    public String getRequestBodyAsString() throws Exception {

        if (this.requestBody == null) {

            if (this.savedBytes == null) {

                if(super.getInputStream() == null ) {
                    throw new Exception("There is no input stream to create a body from");
                }

                this.savedBytes = org.apache.commons.io.IOUtils.toByteArray(super.getInputStream());
            }

            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader bufferedReader = null;
            try {
                // read the payload into the StringBuilder
                InputStream inputStream = this.getInputStream();
                if (inputStream != null) {
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    char[] charBuffer = new char[128];
                    int bytesRead = -1;
                    while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                        stringBuilder.append(charBuffer, 0, bytesRead);
                    }
                } else {
                    // make an empty string since there is no payload
                    stringBuilder.append("");
                }
            } catch (Exception ex) {
                LOGGER.error("Error reading the request payload", ex);
                throw new Exception("Error reading request payload; see log file for exception details");
            } finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException iox) {
                        // ignore
                    }
                }
            }

            this.requestBody = stringBuilder.toString();
        }

        return requestBody;
    }
}