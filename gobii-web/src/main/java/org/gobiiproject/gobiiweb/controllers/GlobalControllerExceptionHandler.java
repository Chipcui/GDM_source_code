package org.gobiiproject.gobiiweb.controllers;

import com.fasterxml.jackson.databind.JsonMappingException;
import org.apache.maven.wagon.ResourceDoesNotExistException;
import org.gobiiproject.gobiiapimodel.payload.sampletracking.ErrorPayload;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;


/**
 * Handles the exceptions thrown in all the Controllers
 * in this project and returns appropriate HTTP response.
 */
@ControllerAdvice(assignableTypes = {
        SampleTrackingController.class,
        BRAPIIControllerV1.class,
        BRAPIIControllerV2.class
})
public class GlobalControllerExceptionHandler {

    Logger LOGGER = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    /**
     * Handles Gobii exceptions.
     * Based on the ValidationStatusType in GobiiException, handler returns HTTP response
     * with appropriate HTTP status code and error message.
     *
     * @param gEx - GobiiException thrown by the controller.
     * @return ResponseEntity - Http Response with HTTP status code and body defined.
     */
    @ExceptionHandler(GobiiException.class)
    public ResponseEntity GobiiExceptionHandler(GobiiException gEx) {
        ErrorPayload errorPayload = new ErrorPayload();
        errorPayload.setError(gEx.getMessage());
        HttpStatus errorStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        switch (gEx.getGobiiValidationStatusType()) {

            case ENTITY_DOES_NOT_EXIST: {
                errorStatus = HttpStatus.NOT_FOUND;
                break;
            }
            case ENTITY_ALREADY_EXISTS: {
                errorStatus = HttpStatus.CONFLICT;
                break;
            }
            case BAD_REQUEST: {
                errorStatus = HttpStatus.BAD_REQUEST;
                break;
            }
            default: {
                errorStatus = HttpStatus.INTERNAL_SERVER_ERROR;
                errorPayload.setError("System error. Please contact the web service administrator.");
            }

        }

        LOGGER.error(gEx.getMessage());

        return ResponseEntity.status(errorStatus).body(errorPayload);
    }

    /**
     * Handles exceptions from request body not meeting the API endpoint specifications.
     * This handler exclusively serves json request body exceptions.
     *
     * @param httpEx - Exception with json request body.
     * @return ResponseEntity with Http Status code and Error payload with appropriate error message.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity NotReadableRequestBodyException(HttpMessageNotReadableException httpEx) {
        JsonMappingException jmEx = (JsonMappingException) httpEx.getCause();
        ErrorPayload errorPayload = new ErrorPayload();
        errorPayload.setError("Request does not comply with Input specification. " +
                "Please refer to API document to make sure request body complies to API specification");
        LOGGER.error(jmEx.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorPayload);
    }

    /**
     * HTTP request method not supported exception handler.
     *
     * @param e - exception object.
     * @return ResponseEntity with Internal server error as status code and Server error as message
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity HttpMethodNotSupportedExceptionHandler(Exception e) {

        ErrorPayload errorPayload = new ErrorPayload();
        errorPayload.setError("Request method not supported!");
        LOGGER.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorPayload);
    }


    /**
     * Handles all the other exceptions not handled by the other handlers.
     *
     * @param e - exception object.
     * @return ResponseEntity with Internal server error as status code and Server error as message
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity GeneralExceptionHandler(Exception e) {
        ErrorPayload errorPayload = new ErrorPayload();
        errorPayload.setError("Server Error");
        LOGGER.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorPayload);
    }

    /**
     * Handles exceptions when the Request Parameters and Path Variables
     * are of type different than the expected.
     * For example, String argument is passed to a pageNumber argument when Integer is expected.
     *
     * @param e - exception object.
     * @return ResponseEntity with "Bad Request" as status code and as message
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity InvalidParameterTypeExceptionHandler(MethodArgumentTypeMismatchException e) {
        ErrorPayload errorPayload = new ErrorPayload();
        errorPayload.setError("Invalid Request Arguments");
        LOGGER.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorPayload);
    }

    @ExceptionHandler({NullPointerException.class,ResourceDoesNotExistException.class})
    public ResponseEntity NullPointerExceptionHandler(NullPointerException e) {
        ErrorPayload errorPayload = new ErrorPayload();
        errorPayload.setError("Resource not found");
        LOGGER.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorPayload);
    }


}
