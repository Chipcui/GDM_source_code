package org.gobiiproject.gobiimodel.config;


import org.gobiiproject.gobiimodel.types.GobiiDbType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Phil on 5/5/2016.
 */
@Root
public class CropConfig {



    @Element(required = false)
    private String gobiiCropType;

    @Element(required = false)
    private String serviceDomain;

    @Element(required = false)
    private String serviceAppRoot;

    @Element(required = false)
    private Integer servicePort;

    @Element(required = false)
    private String rawUserFilesDirectory;

    @Element(required = false)
    private String loaderInstructionFilesDirectory;

    @Element(required = false)
    private String extractorInstructionFilesDirectory;

    @Element(required = false)
    private String extractorInstructionFilesOutputDirectory;

    @Element(required = false)
    private String intermediateFilesDirectory;

    @Element
    private boolean isActive = false;
    private Map<GobiiDbType, CropDbConfig> dbConfigByDbType = new HashMap<>();

    @ElementList
    private List<CropDbConfig> cropDbConfigForSerialization = new ArrayList<>();

    public CropConfig() {}

    public CropConfig(String gobiiCropType,
                      String serviceDomain,
                      String serviceAppRoot,
                      Integer servicePort,
                      String loaderInstructionFilesDirectory,
                      String extractorInstructionFilesDirectory,
                      String extractorInstructionFilesOutputDirectory,
                      String rawUserFilesDirectory,
                      String intermediateFilesDirectory,
                      boolean isActive) {

        this.gobiiCropType = gobiiCropType;
        this.serviceDomain = serviceDomain;
        this.serviceAppRoot = serviceAppRoot;
        this.servicePort = servicePort;
        this.rawUserFilesDirectory = rawUserFilesDirectory;
        this.loaderInstructionFilesDirectory = loaderInstructionFilesDirectory;
        this.extractorInstructionFilesDirectory = extractorInstructionFilesDirectory;
        this.extractorInstructionFilesOutputDirectory = extractorInstructionFilesOutputDirectory;
        this.intermediateFilesDirectory = intermediateFilesDirectory;
        this.isActive = isActive;
    }

    public Integer getServicePort() {
        return servicePort;
    }

    public String getRawUserFilesDirectory() {
        return rawUserFilesDirectory;
    }

    public String getLoaderInstructionFilesDirectory() {
        return loaderInstructionFilesDirectory;
    }

    public String getExtractorInstructionFilesDirectory() {
        return extractorInstructionFilesDirectory;
    }

    public String getExtractorInstructionFilesOutputDirectory() {
        return extractorInstructionFilesOutputDirectory;
    }

    public String getIntermediateFilesDirectory() {
        return intermediateFilesDirectory;
    }

    public String getServiceDomain() {
        return serviceDomain;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getServiceAppRoot() {
        return serviceAppRoot;
    }

    public void setServiceAppRoot(String serviceAppRoot) {
        this.serviceAppRoot = serviceAppRoot;
    }

    public String getGobiiCropType() {
        return gobiiCropType;
    }

    public void setGobiiCropType(String gobiiCropType) {
        this.gobiiCropType = gobiiCropType;
    }

    public void addCropDbConfig(GobiiDbType gobiiDbTypee, CropDbConfig cropDbConfig) {
        dbConfigByDbType.put(gobiiDbTypee, cropDbConfig);
        cropDbConfigForSerialization.add(cropDbConfig);
    } // addCropDbConfig()

    public CropDbConfig getCropDbConfig(GobiiDbType gobiiDbType) {
        return cropDbConfigForSerialization
                .stream()
                .filter(d -> d.getGobiiDbType().equals(gobiiDbType))
                .collect(Collectors.toList())
                .get(0);
    } // getCropDbConfig()


}
