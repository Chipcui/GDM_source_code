package org.gobiiproject.gobiiprocess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.gobiiproject.gobiiapimodel.payload.HeaderStatusMessage;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiapimodel.restresources.gobii.GobiiUriFactory;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.RestResourceId;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.DataSetDTO;
import org.gobiiproject.gobiimodel.types.GobiiAutoLoginType;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.gobiiproject.gobiimodel.utils.FileSystemInterface;
import org.gobiiproject.gobiimodel.utils.HelperFunctions;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.gobiiproject.gobiimodel.utils.email.ProcessMessage;
import org.gobiiproject.gobiimodel.utils.error.Logger;

import static java.util.stream.Collectors.toList;
import static org.gobiiproject.gobiimodel.utils.FileSystemInterface.rmIfExist;
import static org.gobiiproject.gobiimodel.utils.HelperFunctions.checkFileExistence;
import static org.gobiiproject.gobiimodel.utils.HelperFunctions.tryExec;
import static org.gobiiproject.gobiimodel.utils.error.Logger.*;

/**
 * A repository of methods designed to interface with HDF5 files, both in the creation and in the execution of 
 */
public class HDF5Interface {


    private String pathToHDF5;
    private String pathToHDF5Files;
    //Paths

    /**
     * Creates an HDF5 for a dataset given an existing file path. Will return false if the process fails (generally due to *nix OS failures) which also will set the error state to false.
     * @param dm Email message object - for direct writing
     * @param dst DataSetType (obviously)
     * @param configuration configurations - for reading if a configruation is set correctly
     * @param dataSetId ID of dataset to create
     * @param crop crop to create the dataset for
     * @param errorPath Place to store temporary files in case of needing temporary files
     * @param variantFilename Name of the dataset (Only used to set the postgres name [probably a bug)
     * @param variantFile Location of the file to use for creating the dataset
     * @return if the process succeeded
     */
    public boolean createHDF5FromDataset(ProcessMessage dm, String dst, ConfigSettings configuration, Integer dataSetId, String crop, String errorPath, String variantFilename, File variantFile) throws Exception {
        //HDF-5
        //Usage: %s <datasize> <input file> <output HDF5 file
        String loadHDF5= getPathToHDF5() +"loadHDF5";
        dm.addPath("matrix directory", pathToHDF5Files, configuration, false);
        String HDF5File= getFileLoc(dataSetId);
        int size=8;
        switch(dst.toUpperCase()){
            case "NUCLEOTIDE_4_LETTER": size = 4; break;
            case "NUCLEOTIDE_2_LETTER": case "IUPAC":case "VCF":
                size=2;break;
            case "SSR_ALLELE_SIZE":size=8;break;
            case "CO_DOMINANT_NON_NUCLEOTIDE":
            case "DOMINANT_NON_NUCLEOTIDE":size=1;break;
            default:
                logError("Digester","Unknown type "+dst.toString());return false;
        }
        Logger.logInfo("Digester","Running HDF5 Loader. HDF5 Generating at "+HDF5File);
        boolean success=HelperFunctions.tryExec(loadHDF5+" "+size+" "+variantFile.getPath()+" "+HDF5File,null,errorPath);
        if(!success){
            //TODO - if not successful - remove HDF5 file, do not update GobiiFileReader's state
            rmIfExist(HDF5File);
            return false;
        }
        updateValues(configuration, crop, dataSetId,variantFilename, HDF5File);
        return true;
    }

    /**
     * Updates Postgresql through the webservices to update the DataSet's monetDB and HDF5File references.
     *
     * @param config         Configuration settings, used to determine connections
     * @param cropName       Name of the crop
     * @param dataSetId      Data set to update
     * @param monetTableName Name of the table in the monetDB database for this dataset.
     * @param hdfFileName    Name of the HDF5 file for this dataset (Note, these should be obvious)
     */
    public void updateValues(ConfigSettings config, String cropName, Integer dataSetId, String monetTableName, String hdfFileName) {
        try {
            // set up authentication and so forth
            // you'll need to get the current from the instruction file
            GobiiClientContext context = GobiiClientContext.getInstance(config, cropName, GobiiAutoLoginType.USER_RUN_AS);

            if (LineUtils.isNullOrEmpty(context.getUserToken())) {
                logError("Digester", "Unable to login with user " + GobiiAutoLoginType.USER_RUN_AS.toString());
                return;
            }

            String currentCropContextRoot = context.getInstance(null, false).getCurrentCropContextRoot();
            GobiiUriFactory gobiiUriFactory = new GobiiUriFactory(currentCropContextRoot);

            RestUri projectsUri = gobiiUriFactory
                    .resourceByUriIdParam(RestResourceId.GOBII_DATASETS);
            projectsUri.setParamValue("id", dataSetId.toString());
            GobiiEnvelopeRestResource<DataSetDTO, DataSetDTO> gobiiEnvelopeRestResourceForDatasets = new GobiiEnvelopeRestResource<>(projectsUri);
            PayloadEnvelope<DataSetDTO> resultEnvelope = gobiiEnvelopeRestResourceForDatasets
                    .get(DataSetDTO.class);

            DataSetDTO dataSetResponse;
            if (!resultEnvelope.getHeader().getStatus().isSucceeded()) {
                System.out.println();
                logError("Digester", "Data set response response errors");
                for (HeaderStatusMessage currentStatusMesage : resultEnvelope.getHeader().getStatus().getStatusMessages()) {
                    logError("HeaderError", currentStatusMesage.getMessage());
                }
                return;
            } else {
                dataSetResponse = resultEnvelope.getPayload().getData().get(0);
            }

            dataSetResponse.setDataTable(monetTableName);
            dataSetResponse.setDataFile(hdfFileName);

            resultEnvelope = gobiiEnvelopeRestResourceForDatasets
                    .put(DataSetDTO.class, new PayloadEnvelope<>(dataSetResponse, GobiiProcessType.UPDATE));


            //dataSetResponse = dtoProcessor.process(dataSetResponse);
            // if you didn't succeed, do not pass go, but do log errors to your log file
            if (!resultEnvelope.getHeader().getStatus().isSucceeded()) {
                logError("Digester", "Data set response response errors");
                for (HeaderStatusMessage currentStatusMesage : resultEnvelope.getHeader().getStatus().getStatusMessages()) {
                    logError("HeaderError", currentStatusMesage.getMessage());
                }
                return;
            }
        } catch (Exception e) {
            logError("Digester", "Exception while referencing data sets in Postgresql", e);
            return;
        }
    }

    public String getPathToHDF5() {
        return pathToHDF5;
    }

    public void setPathToHDF5(String pathToHDF5) {
        this.pathToHDF5 = pathToHDF5;
    }

    public String getPathToHDF5Files() {
        return pathToHDF5Files;
    }

    public void setPathToHDF5Files(String pathToHDF5Files) {
        this.pathToHDF5Files = pathToHDF5Files;
    }

    /**
     * Given a marker list extracts genotyping data from it. See getHDF5GenoFromSampleList for more information.
     * @param markerFast if the file is extracted in 'marker fast' orientation
     * @param errorFile Where to put errors
     * @param tempFolder folder for temporary files
     * @param posFile the place for a positional file
     * @return location of output
     * @throws FileNotFoundException if it can't find a file related
     */
    public String getHDF5GenoFromMarkerList(boolean markerFast, String errorFile, String tempFolder, String posFile) throws FileNotFoundException {
        return getHDF5GenoFromSampleList(markerFast,errorFile,tempFolder,posFile,null);
    }

    private HashMap<String,String> getSamplePosFromFile(String inputFile) throws FileNotFoundException {
        HashMap<String, String> map = new HashMap<String, String>();
        BufferedReader sampR = new BufferedReader(new FileReader(inputFile));
        try{
            while (sampR.ready()) {
                    String sampLine = sampR.readLine();
                    if (sampLine != null) {
                        String[] sampSplit = sampLine.split("\t");
                        if(sampSplit.length>1) {
                            map.put(sampSplit[0], sampSplit[1]);
                        }
                    }
                }
            }
        catch(Exception e){
            Logger.logError("GobiiExtractor", "Unexpected error in reading sample file",e);
        }
        return map;
    }

    /**
     * Gets a pared down list of markers and samples based on position file and sample position file
     * @param markerFast if the output is 'markerFast' or sample fast
     * @param errorFile Location of temporary error file (mostly but not entirely ignored
     * @param tempFolder Location of folder to store temporary files
     * @param posFile the marker position list, known as a posfile. Each line contains a dataset ID and a list of marker positions, where
     *                the positions refer to the positions in the HDF5 file created for that dataset. The datasets are known by name as their
     *                name in the FS is based on their id in the system.
     * @param samplePosFile As in posFile, this is a list of dataset -> sample position sets. If null, performas a marker list extract unfiltered
     *                      Otherwise, only datasets with lines in here AND in posFile are actually extracted.
     * @return String location of the output file on the filesystem.
     * @throws FileNotFoundException if the datasets provided contain an invalid dataset, or the temporary file folder is badly chmodded
     */
    public String getHDF5GenoFromSampleList(boolean markerFast, String errorFile, String tempFolder, String posFile, String samplePosFile) throws FileNotFoundException{
        if(!new File(posFile).exists()){
            Logger.logError("Genotype Matrix","No positions generated - Likely no data");
            return null;
        }
        BufferedReader posR=new BufferedReader(new FileReader(posFile));
        BufferedReader sampR=null;
        boolean hasSampleList=false;
        HashMap<String,String> samplePos=null;
        if(checkFileExistence(samplePosFile)){
            hasSampleList=true;
            sampR=new BufferedReader(new FileReader(samplePosFile));
            samplePos=getSamplePosFromFile(samplePosFile);
        }
        StringBuilder genoFileString=new StringBuilder();
        try{
            posR.readLine();//header
            if(sampR!=null)sampR.readLine();
            while(posR.ready()) {
                String[] line = posR.readLine().split("\t");
                if(line.length < 2){
                    Logger.logDebug("MarkerList","Skipping line " + Arrays.deepToString(line));
                    continue;
                }
                int dsID=Integer.parseInt(line[0]);

                String positionList=line[1].replace(',','\n');

                String positionListFileLoc=tempFolder+"position.list";

                FileSystemInterface.rmIfExist(positionListFileLoc);

                FileWriter w = new FileWriter(positionListFileLoc);

                w.write(positionList);
                w.close();

                String sampleList=null;
                if(hasSampleList){
                    sampleList=samplePos.get(line[0]);
                }
                String genoFile=null;
                if(!hasSampleList || (sampleList!=null)) {

                    genoFile = getHDF5Genotype(markerFast, errorFile, dsID, tempFolder, positionListFileLoc, sampleList);

                    if(genoFile==null)return null;
                }
                else{
                    //We have a marker position but not a sample position. Do not create a genotype file in the first place
                }
                if(genoFile!=null){
                    genoFileString.append(" "+genoFile);
                }
            }
        }catch(IOException e) {
            Logger.logError("GobiiExtractor", "MarkerList reading failed", e);
        }

        //Coallate genotype files
        String genoFile=tempFolder+"markerList.genotype";
        logDebug("MarkerList", "Accumulating markers into final genotype file");
        if(genoFileString.length() == 0){
            Logger.logError("HDF5Interface","No genotype data to extract");
            return null;
        }
        String genotypePartFileIdentifier=genoFileString.toString();

        if(markerFast) {
            tryExec("paste" + genotypePartFileIdentifier, genoFile, errorFile);
        }
        else{
            tryExec("cat" + genotypePartFileIdentifier, genoFile, errorFile);
        }
        for(String tempGenoFile:genotypePartFileIdentifier.split(" ")) {
            rmIfExist(tempGenoFile);
        }
        return genoFile;
    }

    /**
     * Convenience method for getHDF5Genotype(boolean, String, Integer, String, String, String).
     * MarkerList and sampleList are passed in as null
     * @return see getHDF5Genotype(boolean,String, Integer, String, String, String)
     */
    public String getHDF5Genotype(boolean markerFast, String errorFile, Integer dataSetId, String tempFolder) {
        return getHDF5Genotype( markerFast, errorFile,dataSetId,tempFolder,null,null);
    }

    /**
     * Performs the basic genotype extraction on a dataset given by dataSetId, filtered by the string entry from the marker list
     * and sample list files.
     * If marker list is null, do a dataset extract. Else, do a marker list extract on the dataset. If sampleList is also set, filter by samples afterwards
     * @param markerFast if the output is 'markerFast' or sample fast
     * @param errorFile where error logs can be stored temporarily
     * @param dataSetId Dataset ID to be pulled from
     * @param tempFolder folder to store intermediate results
     * @param markerList nullable - determines what markers to extract. File containing a list of marker positions, comma separated
     * @param sampleList nullable - list of comma delimited samples to cut out
     * @return file location of the dataset output.
     */
    private String getHDF5Genotype( boolean markerFast, String errorFile, Integer dataSetId, String tempFolder, String markerList, String sampleList) {
        String genoFile=tempFolder+"DS-"+dataSetId+".genotype";

        String HDF5File= getFileLoc(dataSetId);
        // %s <orientation> <HDF5 file> <output file>
        String ordering="samples-fast";
        if(markerFast)ordering="markers-fast";

        logDebug("Extractor","HDF5 Ordering is "+ordering);

        if(markerList!=null) {
            String hdf5Extractor=pathToHDF5+"fetchmarkerlist";
            Logger.logInfo("Extractor","Executing: " + hdf5Extractor+" "+ ordering +" "+HDF5File+" "+markerList+" "+genoFile);
            boolean success=HelperFunctions.tryExec(hdf5Extractor + " " + ordering+" " + HDF5File+" "+markerList+" "+genoFile, null, errorFile);
            if(!success){
                rmIfExist(genoFile);
                return null;
            }
        }
        else {
            String hdf5Extractor=pathToHDF5+"dumpdataset";
            Logger.logInfo("Extractor","Executing: " + hdf5Extractor+" "+ordering+" "+HDF5File+" "+genoFile);
            boolean success=HelperFunctions.tryExec(hdf5Extractor + " " + ordering + " " + HDF5File + " " + genoFile, null, errorFile);
            if(!success){
                rmIfExist(genoFile);
                return null;
            }

        }
        if(sampleList!=null){
            filterDirectional(genoFile,sampleList,markerFast);
        }
        Logger.logDebug("Extractor",(Logger.success()?"Success ":"Failure " +"Extracting with "+ordering+" "+HDF5File+" "+genoFile));
        return genoFile;
    }

    private String getFileLoc(Integer dataSetId) {
        return pathToHDF5Files + "DS_" + dataSetId + ".h5";
    }

    /**
     * Filter out a list of elements across a direction. If markerFast is true, removes all rows not in the element list.
     * If markerFast is false, removes all columns not in the element list.
     * Element list is sorted, and all negative numbers are ignored, to correspond to existing functionality.
     *
     * 2,-1,0 will return an output file with the first[0th] column followed by the third[2nd] column, for example.
     * @param filename File to be modified. Will move to a 'filename.tmp', and then modify the original, deleting the temp
     * @param elementList String containing comma separated list of integers, corresponding to 0 based row/column entities
     * @param markerFast if true, filter by rows. Corresponds to the 'sample list' marker fast nature
     */
    private void filterDirectional(String filename, String elementList, boolean markerFast){
        //Take all positive integers from this list as cut values. See cutString.
        //Ex 0,1,2,-1,4,5 -> 0,1,2,4,5
        List<Integer> elements;
            elements = Arrays.stream(elementList.split(","))
                    .map(String::trim)
                    .map(Integer::parseInt)
                    .filter(n -> n > -1)
                    .sorted().collect(toList());

        String tmpFileStr=filename+".tmp";

        //Move original to temp
        FileSystemInterface.mv(filename,tmpFileStr);

        File tmpFile = new File(tmpFileStr);
        File outFile = new File(filename);

        try {
            if (markerFast) {
                projectRows(tmpFile,outFile,elements);
            }
            else{
                filterLines(tmpFile, outFile, elements);
            }
        }
        catch(Exception e) {
            Logger.logError("HDF5 Interface","Error filtering",e);
        }
        rmIfExist(tmpFile);
    }

    private void filterLines(File from, File to, List<Integer> elements) throws IOException {
        try(BufferedReader in = new BufferedReader(new FileReader(from));
        BufferedWriter out = new BufferedWriter(new FileWriter(to))) {
            String line = in.readLine();

            Iterator<Integer> iter = elements.iterator();
            int next = iter.next();
            for (int i = 0; line != null; i++) {
                if (i == next) {
                    out.write(line);
                    out.newLine();
                    if (!iter.hasNext()) {
                        break;
                    }
                    next = iter.next();
                }
                line = in.readLine();
            }
        }
    }

    private final String TAB="\t";

    /**
     * Projects a subset of rows onto the output file (cutting out vertical slices)
     * @from source file
     * @to destination of output projection
     * @throws IOException
     */
    private void projectRows(File from, File to, List<Integer> elements) throws IOException {
        try(BufferedReader in = new BufferedReader(new FileReader(from));
            BufferedWriter out = new BufferedWriter(new FileWriter(to))) {
            String line = in.readLine();
            while (line != null) {
                String[] split = line.split(TAB);

                //Elements in specified order separated by tabs
                out.write(elements.stream().map(i -> split[i]).collect(Collectors.joining(TAB)));
                out.newLine();
                line = in.readLine();
            }
        }
    }
}
