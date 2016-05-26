package org.gobiiproject.gobiiweb;

import org.gobiiproject.gobiimodel.types.GobiiCropType;
import org.gobiiproject.gobiimodel.types.GobiiHttpHeaderNames;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Phil on 5/25/2016.
 */
public class cropRequestAnalyzer {

    private static Logger LOGGER = LoggerFactory.getLogger(cropRequestAnalyzer.class);

    private static HttpServletRequest getRequest() {

        HttpServletRequest returnVal = null;

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        if (null != requestAttributes && requestAttributes instanceof ServletRequestAttributes) {
            returnVal = ((ServletRequestAttributes) requestAttributes).getRequest();
        }

        return returnVal;
    }


    public static GobiiCropType getCropTypeFromHeaders() {

        GobiiCropType returnVal = null;

        HttpServletRequest request = getRequest();
        if (null != request) {
            String gobiiCrop = request.getHeader(GobiiHttpHeaderNames.HEADER_GOBII_CROP);
            if (!LineUtils.isNullOrEmpty(gobiiCrop)) {

                final String gobiiCropProper = gobiiCrop.toUpperCase();
                if (1 == Arrays.asList(GobiiCropType.values())
                        .stream()
                        .filter(c -> c.equals(gobiiCropProper))
                        .count()) {

                    returnVal = GobiiCropType.valueOf(gobiiCropProper);

                } else {
                    LOGGER.error("The value in header "
                            + GobiiHttpHeaderNames.HEADER_GOBII_CROP
                            + ": "
                            + gobiiCrop
                            + " does not correspond to a known crop type");
                }

            } else {
                LOGGER.error("Request did not include the header " + GobiiHttpHeaderNames.HEADER_GOBII_CROP);
            }
        } else {
            LOGGER.error("Unable to retreive servlet request for crop type analysis from header");
        }

        return returnVal;

    }


    public static GobiiCropType getCropTypeFromUri() {

        GobiiCropType returnVal = null;

        HttpServletRequest request = getRequest();
        if (null != request) {
            String requestUrl = request.getRequestURI();

            List<GobiiCropType> matchedCrops =
                    Arrays.asList(GobiiCropType.values())
                            .stream()
                            .filter(c -> requestUrl.toLowerCase().contains(c.toString().toLowerCase()))
                            .collect(Collectors.toList());

            if (matchedCrops.size() > 0) {

                if (1 == matchedCrops.size()) {
                    returnVal = matchedCrops.get(0);
                } else {
                    LOGGER.error("The current url ("
                            + requestUrl
                            + ") matched more than one one crop");
                }

            } else {

                LOGGER.error("The current url ("
                        + requestUrl
                        + ") did not match any crops");
            }

        } else {
            LOGGER.error("Unable to retreive servlet request for crop type analysis from url");
        }

        return returnVal;
    }
}
