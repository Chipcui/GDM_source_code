package org.gobiiproject.gobiidtomapping.entity.noaudit.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.gobiiproject.gobiidao.resultset.core.listquery.DtoListQueryColl;
import org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId;
import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiidtomapping.entity.noaudit.DtoMapGenotypeCalls;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.*;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiisampletrackingdao.hdf5.HDF5Interface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.util.*;

public class DtoMapGenotypeCallsImpl implements DtoMapGenotypeCalls {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapGenotypeCallsImpl.class);

    @Autowired
    private DtoListQueryColl dtoListQueryColl;

    @Autowired
    private HDF5Interface hdf5Interface;


    private String nextPageOffset;

    private String nextColumnOffset;

    @Override
    public String getNextPageOffset() {
        return nextPageOffset;
    }

    @Override
    public String getNextColumnOffset() {
        return nextColumnOffset;
    }

    public List<GenotypeCallsMarkerMetadataDTO> getMarkerMetaDataByMarkerIdLimit(
            Integer datasetId,
            Integer markerId,
            Integer pageSize) throws GobiiDtoMappingException {

        List<GenotypeCallsMarkerMetadataDTO> returnVal;

        try {

            Map<String, Object> sqlParams = new HashMap<>();

            if(datasetId != null) {
                sqlParams.put("datasetId", datasetId);
            }
            else {

                LOGGER.error("datasetId is null");

                throw new GobiiDtoMappingException(
                        GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.BAD_REQUEST,
                        "Invalid Dataset Id");
            }

            if (markerId != null) {
                sqlParams.put("markerId", markerId);
            }

            if (pageSize != null) {
                sqlParams.put("pageSize", pageSize);
            }


            returnVal = (List<GenotypeCallsMarkerMetadataDTO>) dtoListQueryColl.getList(
                    ListSqlId.QUERY_ID_GENOTYPE_CALLS_MARKER_METADATA,
                    null,
                    sqlParams
            );

            if (returnVal == null) {
                return new ArrayList<>();
            }

        }
        catch (GobiiException gE) {

            LOGGER.error(gE.getMessage(), gE);

            throw new GobiiDtoMappingException(
                    gE.getGobiiStatusLevel(),
                    gE.getGobiiValidationStatusType(),
                    gE.getMessage());
        }
        catch (Exception e) {
            LOGGER.error("Gobii Mapping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }


    public List<GenotypeCallsMarkerMetadataDTO> getMarkerMetaDataByDatasetId(
            Integer datasetId,
            Integer pageOffset,
            Integer pageSize) throws GobiiDtoMappingException {

        List<GenotypeCallsMarkerMetadataDTO> returnVal;

        try {

            Map<String, Object> sqlParams = new HashMap<>();

            if(datasetId != null) {
                sqlParams.put("datasetId", datasetId);
            }
            else {

                LOGGER.error("datasetId is null");

                throw new GobiiDtoMappingException(
                        GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.BAD_REQUEST,
                        "Invalid Dataset Id");
            }

            if (pageSize != null) {
                sqlParams.put("pageSize", pageSize);
            }
            else {

                LOGGER.error("page size is null");

                throw new GobiiDtoMappingException(
                        GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.BAD_REQUEST,
                        "Page Size is required");
            }

            if(pageOffset != null) {
                sqlParams.put("pageOffset", pageOffset);
            }


            returnVal = (List<GenotypeCallsMarkerMetadataDTO>) dtoListQueryColl.getList(
                    ListSqlId.QUERY_ID_MARKER_METADATA_BY_DATASET,
                    null,
                    sqlParams
            );

            if (returnVal == null) {
                return new ArrayList<>();
            }

        }
        catch (GobiiException gE) {

            LOGGER.error(gE.getMessage(), gE);

            throw new GobiiDtoMappingException(
                    gE.getGobiiStatusLevel(),
                    gE.getGobiiValidationStatusType(),
                    gE.getMessage());
        }
        catch (Exception e) {
            LOGGER.error("Gobii Mapping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }

    public List<GenotypeCallsDnarunMetadataDTO> getDnarunMetaDataByDatasetId(
            Integer datasetId,
            Integer pageOffset,
            Integer pageSize) throws GobiiDtoMappingException {

        List<GenotypeCallsDnarunMetadataDTO> returnVal;

        try {

            Map<String, Object> sqlParams = new HashMap<>();

            if(datasetId != null) {
                sqlParams.put("datasetId", datasetId);
            }
            else {

                LOGGER.error("datasetId is null");

                throw new GobiiDtoMappingException(
                        GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.BAD_REQUEST,
                        "Invalid Dataset Id");
            }

            if (pageSize != null) {
                sqlParams.put("pageSize", pageSize);
            }
            else {

                LOGGER.error("page size is null");

                throw new GobiiDtoMappingException(
                        GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.BAD_REQUEST,
                        "Page Size is required");
            }

            if(pageOffset != null) {
                sqlParams.put("pageOffset", pageOffset);
            }


            returnVal = (List<GenotypeCallsDnarunMetadataDTO>) dtoListQueryColl.getList(
                    ListSqlId.QUERY_ID_DNARUN_METADATA_BY_DATASET,
                    null,
                    sqlParams
            );

            if (returnVal == null) {
                return new ArrayList<>();
            }

        }
        catch (GobiiException gE) {

            LOGGER.error(gE.getMessage(), gE);

            throw new GobiiDtoMappingException(
                    gE.getGobiiStatusLevel(),
                    gE.getGobiiValidationStatusType(),
                    gE.getMessage());
        }
        catch (Exception e) {
            LOGGER.error("Gobii Mapping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }


    public List<GenotypeCallsDnarunMetadataDTO> getDnarunMetaDataByDnarunIdLimit(
            Integer datasetId,
            Integer dnarunId,
            Integer pageSize) throws GobiiDtoMappingException {

        List<GenotypeCallsDnarunMetadataDTO> returnVal;

        try {

            Map<String, Object> sqlParams = new HashMap<>();

            if(datasetId != null) {
                sqlParams.put("datasetId", datasetId);
            }
            else {

                LOGGER.error("datasetId is null");

                throw new GobiiDtoMappingException(
                        GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.BAD_REQUEST,
                        "Invalid Dataset Id");
            }

            if (dnarunId != null) {
                sqlParams.put("dnarunId", dnarunId);
            }

            if (pageSize != null) {
                sqlParams.put("pageSize", pageSize);
            }


            returnVal = (List<GenotypeCallsDnarunMetadataDTO>) dtoListQueryColl.getList(
                    ListSqlId.QUERY_ID_GENOTYPE_CALLS_DNARUN_METADATA,
                    null,
                    sqlParams
            );

            if (returnVal == null) {
                return new ArrayList<>();
            }

        }
        catch (GobiiException gE) {

            LOGGER.error(gE.getMessage(), gE);

            throw new GobiiDtoMappingException(
                    gE.getGobiiStatusLevel(),
                    gE.getGobiiValidationStatusType(),
                    gE.getMessage());
        }
        catch (Exception e) {
            LOGGER.error("Gobii Mapping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }

    public List<GenotypeCallsMarkerMetadataDTO> getMarkerMetaDataByMarkerIdList(
            List<Integer> markerIdList) throws GobiiDtoMappingException {

        List<GenotypeCallsMarkerMetadataDTO> returnVal;

        try {

            Map<String, Object> sqlParams = new HashMap<>();

            if(markerIdList != null) {
                sqlParams.put("markerIdList", markerIdList);
            }
            else {

                LOGGER.error("Marker Id list is null");

                throw new GobiiDtoMappingException(
                        GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.BAD_REQUEST,
                        "Invalid Marker Id list");
            }


            returnVal = (List<GenotypeCallsMarkerMetadataDTO>) dtoListQueryColl.getList(
                    ListSqlId.QUERY_ID_MARKER_METADATA_BY_MARKER_LIST,
                    null,
                    sqlParams
            );

            if (returnVal == null) {
                return new ArrayList<>();
            }

        }
        catch (GobiiException gE) {

            LOGGER.error(gE.getMessage(), gE);

            throw new GobiiDtoMappingException(
                    gE.getGobiiStatusLevel(),
                    gE.getGobiiValidationStatusType(),
                    gE.getMessage());
        }
        catch (Exception e) {
            LOGGER.error("Gobii Mapping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }

    public List<GenotypeCallsDnarunMetadataDTO> getDnarunMetaDataByDnarunIdList(
            List<Integer> dnarunIdList) throws GobiiDtoMappingException {

        List<GenotypeCallsDnarunMetadataDTO> returnVal;

        try {

            Map<String, Object> sqlParams = new HashMap<>();

            if(dnarunIdList != null) {
                sqlParams.put("dnarunIdList", dnarunIdList);
            }
            else {

                LOGGER.error("Dnarun Id list is null");

                throw new GobiiDtoMappingException(
                        GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.BAD_REQUEST,
                        "Invalid Dnarun Id list");
            }


            returnVal = (List<GenotypeCallsDnarunMetadataDTO>) dtoListQueryColl.getList(
                    ListSqlId.QUERY_ID_DNARUN_METADATA_BY_DNARUN_LIST,
                    null,
                    sqlParams
            );

            if (returnVal == null) {
                return new ArrayList<>();
            }

        }
        catch (GobiiException gE) {

            LOGGER.error(gE.getMessage(), gE);

            throw new GobiiDtoMappingException(
                    gE.getGobiiStatusLevel(),
                    gE.getGobiiValidationStatusType(),
                    gE.getMessage());
        }
        catch (Exception e) {
            LOGGER.error("Gobii Mapping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }



    /**
     * Gets the list of Genotypes Calls for given datasetId.
     * @param datasetId
     * @return
     */
    @Override
    public Map<String, String> getGenotypeCallsAsString(Integer datasetId, String pageToken) {

        Map<String, String> returnVal = new HashMap<>();

        Integer pageOffset = null;

        List<GenotypeCallsMarkerMetadataDTO> markerMetadataList = new ArrayList<>();

        List<GenotypeCallsDnarunMetadataDTO> dnarunMetadataList = new ArrayList<>();

        Map<String, ArrayList<String>> markerHdf5IndexMap= new HashMap<>();

        Map<String, ArrayList<String>> dnarunHdf5IndexMap = new HashMap<>();

        try {

            if(pageToken != null) {
                try {

                    pageOffset = Integer.parseInt(pageToken);
                }
                catch(Exception e) {
                    pageOffset = null;
                }
            }

            dnarunMetadataList = this.getDnarunMetaDataByDnarunIdLimit(datasetId, null, 10000);

            markerMetadataList = this.getMarkerMetaDataByDatasetId(datasetId, pageOffset, 10000);

            //HDF5 Index map for markers
            for(GenotypeCallsMarkerMetadataDTO markerMetadata : markerMetadataList) {

                if(!markerHdf5IndexMap.containsKey(datasetId.toString())) {
                    markerHdf5IndexMap.put(datasetId.toString(), new ArrayList<>());
                }
                markerHdf5IndexMap.get(
                        datasetId.toString()).add(
                        markerMetadata.getHdf5MarkerIdx(datasetId.toString()));
            }

            //HDF5 Index for dnaruns
            for(GenotypeCallsDnarunMetadataDTO dnarunMetadata : dnarunMetadataList) {
                if(!dnarunHdf5IndexMap.containsKey(datasetId.toString())) {
                    dnarunHdf5IndexMap.put(
                            datasetId.toString(),
                            new ArrayList<>());
                }
                dnarunHdf5IndexMap.get(
                        datasetId.toString()).add(
                        dnarunMetadata.getHdf5DnarunIdx(datasetId.toString()));
            }


            Integer nextPageOffset = 0;

            if(pageOffset != null) {
                nextPageOffset = pageOffset + 10000;
            }
            else {
                nextPageOffset = 10000;
            }

            if(markerMetadataList.size() >= 10000) {
                returnVal.put("nextPageOffset", nextPageOffset.toString());
            }
            else {
                returnVal.put("nextPageOffset", null);
            }

            String extractFilePath = this.extractGenotypes(markerHdf5IndexMap, dnarunHdf5IndexMap);

            returnVal.put("genotypes", this.readHdf5GenotypesFromMatrix(
                    extractFilePath, markerMetadataList, dnarunMetadataList
            ));

            if(extractFilePath != null && extractFilePath.endsWith("result.genotypes")) {
                File extractFile = new File(extractFilePath);
                String parent = extractFile.getParent();
                File extractFolder = new File(parent);
                extractFolder.delete();
            }
        }
        catch(GobiiException ge) {
            throw ge;
        }
        catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new GobiiException(
                    GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    "Failed to extract genotypes. " +
                            "Please try again. If error persists, try contacting the System administrator");
        }

        return returnVal;
    }

    /**
     * Gets the list of Genotypes Calls for given datasetId.
     * @param datasetId
     * @param pageToken
     * @param pageSize
     * @return
     */
    @Override
    public List<GenotypeCallsDTO> getGenotypeCallsList(
            Integer datasetId, String pageToken,
            Integer pageSize) {

        List<GenotypeCallsDTO> returnVal = new ArrayList<>();

        Integer pageOffset = null;
        Integer columnOffset = 0;
        Integer markerPageSize = 0;

        List<GenotypeCallsMarkerMetadataDTO> markerMetadataList = new ArrayList<>();

        List<GenotypeCallsDnarunMetadataDTO> dnarunMetadataList = new ArrayList<>();

        Map<String, ArrayList<String>> markerHdf5IndexMap= new HashMap<>();

        Map<String, ArrayList<String>> dnarunHdf5IndexMap = new HashMap<>();

        SortedMap<Integer, Integer> dnarunHdf5OrderMap = new TreeMap<Integer, Integer>();


        try {


            dnarunMetadataList = this.getDnarunMetaDataByDnarunIdLimit(datasetId, pageOffset, pageSize);

            if(pageToken != null) {

                String[] pageTokenSplit = pageToken.split("-", 2);

                if (pageTokenSplit.length == 2) {

                    try {

                        pageOffset = Integer.parseInt(Arrays.asList(pageTokenSplit).get(0));

                        columnOffset = Integer.parseInt(Arrays.asList(pageTokenSplit).get(1));

                        if(columnOffset > dnarunMetadataList.size()) {
                            pageOffset = null;
                            columnOffset = 0;
                        }
                    } catch (Exception e) {
                        pageOffset = null;
                        columnOffset = 0;
                    }
                }
            }

            Integer previousPageExcess = 0;

            if(columnOffset > 0) {
                previousPageExcess = dnarunMetadataList.size() - columnOffset;
            }

            markerPageSize = (int) Math.ceil(
                    ((double)pageSize - (double)previousPageExcess)
                            / (double)dnarunMetadataList.size());

            if (columnOffset > 0) {
                pageOffset -= 1;
                markerPageSize += 1;
            }


            markerMetadataList = this.getMarkerMetaDataByDatasetId(datasetId, pageOffset, markerPageSize);

            //HDF5 index map for markers
            for(GenotypeCallsMarkerMetadataDTO markerMetadata : markerMetadataList) {

                if(!markerHdf5IndexMap.containsKey(datasetId.toString())) {
                    markerHdf5IndexMap.put(datasetId.toString(), new ArrayList<>());
                }
                markerHdf5IndexMap.get(
                        datasetId.toString()).add(
                        markerMetadata.getHdf5MarkerIdx(datasetId.toString()));

            }

            Integer orderIndex = 0;

            //HDF5 index for dnaruns
            for(GenotypeCallsDnarunMetadataDTO dnarunMetadata : dnarunMetadataList) {

                if(!dnarunHdf5IndexMap.containsKey(datasetId.toString())) {
                    dnarunHdf5IndexMap.put(
                            datasetId.toString(),
                            new ArrayList<>());
                }

                dnarunHdf5IndexMap.get(
                        datasetId.toString()).add(
                        dnarunMetadata.getHdf5DnarunIdx(datasetId.toString()));

                dnarunHdf5OrderMap.put(
                        Integer.parseInt(dnarunMetadata.getHdf5DnarunIdx(datasetId.toString())),
                        orderIndex);

                orderIndex++;

            }


            Integer nextPageOffset = markerPageSize;

            if(pageOffset != null) {
                nextPageOffset += pageOffset;
            }

            this.setNextPageOffset(nextPageOffset.toString());

            if(markerHdf5IndexMap.size() == 0 || dnarunHdf5IndexMap.size() == 0) {
                throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                        "Genotype calls does not exists");
            }

            String extractFilePath = this.extractGenotypes(markerHdf5IndexMap, dnarunHdf5IndexMap);

            this.readHdf5GenotypesFromMatrix(
                    returnVal, extractFilePath,
                    pageSize, datasetId,
                    columnOffset, markerMetadataList, dnarunMetadataList,
                    new ArrayList<>(dnarunHdf5OrderMap.values()));

            if(extractFilePath != null && extractFilePath.endsWith("result.genotypes")) {
                File extractFile = new File(extractFilePath);
                String parent = extractFile.getParent();
                File extractFolder = new File(parent);
                extractFolder.delete();
            }

        }
        catch(GobiiException ge) {
            throw ge;
        }
        catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new GobiiException(
                    GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    "Failed to extract genotypes. " +
                            "Please try again. If error persists, try contacting the System administrator");
        }

        return returnVal;
    }


    @Override
    public List<GenotypeCallsDTO> getGenotypeCallsListByExtractQuery(
            String extractQueryFilePath, String pageToken,
            Integer pageSize) {

        List<GenotypeCallsDTO> returnVal = new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> extractQuery = new HashMap<String, Object>();

        List<GenotypeCallsMarkerMetadataDTO> markerMetadataList = new ArrayList<>();
        List<GenotypeCallsDnarunMetadataDTO> dnarunMetadataList = new ArrayList<>();

        List<Integer> markerIdList = new ArrayList<Integer>();
        List<Integer> dnarunIdList = new ArrayList<Integer>();

        Map<String, ArrayList<String>> dnarunHdf5IndexMap = new HashMap<>();
        Map<String, List<GenotypeCallsDnarunMetadataDTO>> mapDnarunMetadataByDatasetId = new HashMap<>();

        Map<String, ArrayList<String>> markerHdf5IndexMap = new HashMap<>();
        Map<String, List<GenotypeCallsMarkerMetadataDTO>> mapMarkerMetadataByDatasetId = new HashMap<>();

        SortedSet<Integer> markerDatasets = new TreeSet<>();

        SortedMap<Integer, Integer> dnarunHdf5OrderMap = new TreeMap<Integer, Integer>();


        try {

            extractQuery = mapper.readValue(new File(extractQueryFilePath), Map.class);

            for(String variantDbId : (List<String>)extractQuery.get("variantDbIds")) {
                try {
                    markerIdList.add(Integer.parseInt(variantDbId));
                }
                catch (Exception e) {
                    continue;
                }
            }

            for(String callsetDbId : (List<String>)extractQuery.get("callSetDbIds")) {
                try {
                    dnarunIdList.add(Integer.parseInt(callsetDbId));
                }
                catch (Exception e) {
                    continue;
                }
            }

            markerMetadataList = this.getMarkerMetaDataByMarkerIdList(markerIdList);

            dnarunMetadataList = this.getDnarunMetaDataByDnarunIdList(dnarunIdList);


            Integer orderIndex = 0;

            for(GenotypeCallsDnarunMetadataDTO dnarunMetadata : dnarunMetadataList) {

                for(String datasetId : dnarunMetadata.getDatasetDnarunIndex().keySet()) {

                    if(!dnarunHdf5IndexMap.containsKey(datasetId)) {
                        dnarunHdf5IndexMap.put(
                                datasetId,
                                new ArrayList<>());
                    }

                    if(!mapDnarunMetadataByDatasetId.containsKey(datasetId)) {
                        mapDnarunMetadataByDatasetId.put(
                                datasetId,
                                new ArrayList<>());
                    }

                    mapDnarunMetadataByDatasetId.get(datasetId).add(dnarunMetadata);

                    dnarunHdf5IndexMap.get(datasetId).add(dnarunMetadata.getHdf5DnarunIdx(datasetId));


                    dnarunHdf5OrderMap.put(
                            Integer.parseInt(dnarunMetadata.getHdf5DnarunIdx(datasetId.toString())),
                            orderIndex);

                    orderIndex++;

                }

            }

            for(GenotypeCallsMarkerMetadataDTO markerMetadata : markerMetadataList) {

                for(String datasetId : markerMetadata.getDatasetMarkerIndex().keySet()) {

                    if(!markerHdf5IndexMap.containsKey(datasetId)) {
                        markerHdf5IndexMap.put(
                                datasetId,
                                new ArrayList<>());
                    }

                    if(!mapMarkerMetadataByDatasetId.containsKey(datasetId)) {
                        mapMarkerMetadataByDatasetId.put(
                                datasetId,
                                new ArrayList<>());
                    }

                    mapMarkerMetadataByDatasetId.get(datasetId).add(markerMetadata);

                    markerHdf5IndexMap.get(datasetId).add(markerMetadata.getHdf5MarkerIdx(datasetId));

                    try {
                        markerDatasets.add(Integer.parseInt(datasetId));
                    }
                    catch(Exception e) {
                        continue;
                    }
                }
            }

            for(Integer datasetId : markerDatasets) {

                Map<String, ArrayList<String>> markerIndexMap = new HashMap<>();

                Map<String, ArrayList<String>> dnarunIndexMap = new HashMap<>();

                markerIndexMap.put(datasetId.toString(),
                        markerHdf5IndexMap.get(datasetId.toString()));

                dnarunIndexMap.put(datasetId.toString(),
                        dnarunHdf5IndexMap.get(datasetId.toString()));

                String extractFilePath = this.extractGenotypes(
                        markerIndexMap,
                        dnarunIndexMap);

                List<GenotypeCallsDTO> datasetGenotypes = new ArrayList<>();

                this.readHdf5GenotypesFromMatrix(
                        datasetGenotypes, extractFilePath,
                        pageSize, datasetId,
                        0, mapMarkerMetadataByDatasetId.get(datasetId.toString()),
                        mapDnarunMetadataByDatasetId.get(datasetId.toString()),
                        new ArrayList<>(dnarunHdf5OrderMap.values()));

                returnVal.addAll(datasetGenotypes);

                if(returnVal.size() >=  pageSize) {
                    break;
                }
                else {
                    pageSize = pageSize -returnVal.size();
                }

                if(extractFilePath != null && extractFilePath.endsWith("result.genotypes")) {
                    File extractFile = new File(extractFilePath);
                    String parent = extractFile.getParent();
                    File extractFolder = new File(parent);
                    extractFolder.delete();
                }
            }


        }
        catch(Exception e) {
            throw new GobiiException(
                    GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    "Failed to extract genotypes. " +
                            "Please try again. If error persists, try contacting the System administrator");
        }

        return returnVal;
    }

    /**
     * Extracts genotypes from the hdf5.
     *
     */
    private String extractGenotypes(Map<String, ArrayList<String>> markerHdf5IndexMap,
                                    Map<String, ArrayList<String>> sampleHdf5IndexMap) throws Exception {

        String tempFolder = UUID.randomUUID().toString();

        String genotypCallsFilePath = hdf5Interface.getHDF5Genotypes(true,
                markerHdf5IndexMap,
                sampleHdf5IndexMap,
                tempFolder);

        return genotypCallsFilePath;

    }


    public void setNextColumnOffset(String columnOffset) {
        this.nextColumnOffset = columnOffset;
    }

    public void setNextPageOffset(String pageOffset) {
        this.nextPageOffset = pageOffset;
    }


    private void readHdf5GenotypesFromMatrix (
            List<GenotypeCallsDTO> returnVal,
            String genotypeMatrixFilePath,
            Integer pageSize,
            Integer datasetId,
            Integer columnOffset,
            List<GenotypeCallsMarkerMetadataDTO> markerMetadataList,
            List<GenotypeCallsDnarunMetadataDTO> dnarunMetadataList,
            List<Integer> dnarunOrder)  throws Exception {


        File genotypCallsFile = new File(genotypeMatrixFilePath);

        FileInputStream fstream = new FileInputStream(genotypCallsFile);

        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

        Integer i = 0; // row index
        Integer j = 0; // column index
        int k = 0; // genotypes count



        int chrEach;

        StringBuilder genotype = new StringBuilder();

        while ((chrEach = br.read()) != -1 && k < pageSize) {


            char genotypesChar = (char) chrEach;

            if(genotypesChar == '\t' || genotypesChar == '\n') {

                if(j < columnOffset) {
                    j += 1;
                    genotype.setLength(0);
                    continue;
                }

                columnOffset = 0;

                GenotypeCallsDTO genotypeCall = new GenotypeCallsDTO();

                genotypeCall.setCallSetDbId(dnarunMetadataList.get(dnarunOrder.get(j)).getDnarunId());
                genotypeCall.setCallSetName(dnarunMetadataList.get(dnarunOrder.get(j)).getDnarunName());
                genotypeCall.setVariantDbId(markerMetadataList.get(i).getMarkerId());
                genotypeCall.setVariantName(markerMetadataList.get(i).getMarkerName());
                genotypeCall.setVariantSetDbId(datasetId);

                genotypeCall.setGenotype(new HashMap<>());
                String[] genotypeValues = new String[] {genotype.toString()};
                genotypeCall.getGenotype().put("values", genotypeValues);

                returnVal.add(genotypeCall);


                if(genotypesChar == '\t') {
                    j++;
                }
                else {
                    i++;
                    j = 0;
                }

                k++;

                genotype.setLength(0);

            }
            else {
                genotype.append(genotypesChar);
                if(genotype.length() == 2) {
                    genotype.insert(1, '/');
                }
            }

        }

        this.setNextColumnOffset(j.toString());

        fstream.close();

    }

    private String readHdf5GenotypesFromMatrix (
            String genotypeMatrixFilePath,
            List<GenotypeCallsMarkerMetadataDTO> markerMetadataList,
            List<GenotypeCallsDnarunMetadataDTO> dnarunMetadataList)  throws Exception {


        File genotypCallsFile = new File(genotypeMatrixFilePath);

        FileInputStream fstream = new FileInputStream(genotypCallsFile);

        int i = 0;

        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

        int chrEach;

        StringBuilder genotypes = new StringBuilder();

        genotypes.append(markerMetadataList.get(i).getMarkerId());
        genotypes.append(',');
        while ((chrEach = br.read()) != -1) {


            char genotypesChar = (char) chrEach;

            if(genotypesChar == '\t') {
                genotypes.append(',');
            }
            else if(genotypesChar == '\n') {
                i++;
                genotypes.append('\n');
                if(i < markerMetadataList.size()) {
                    genotypes.append(markerMetadataList.get(i).getMarkerId());
                    genotypes.append(',');
                }
            }
            else {
                genotypes.append(genotypesChar);
            }

        }


        fstream.close();

        return genotypes.toString();

    }
}
