package org.gobiiproject.gobiidao.gql;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.entity.children.NameIdDTO;
import org.gobiiproject.gobiimodel.dto.entity.flex.VertexDTO;
import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;
import org.gobiiproject.gobiimodel.types.GobiiVertexType;
import org.gobiiproject.gobiimodel.utils.HelperFunctions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class GqlText {


    private final String connectionString;
    private String mdePathFqpn;
    private String cropType;
    private String jobId;
    private boolean isServer = false;

    public GqlText(String cropType, String jobId) throws GobiiException {
        try {

            this.jobId = jobId;
            this.cropType = cropType;
            ConfigSettings configSettings = new ConfigSettings();
            this.connectionString = HelperFunctions.getPostgresConnectionString(configSettings.getCropConfig(this.cropType));

            this.mdePathFqpn = configSettings
                    .getFullyQualifiedFilePath(null, GobiiFileProcessDir.CODE_EXTRACTORS_POSTGRES_MDE)
                    + GQL_SCRIPT_NAME;

            if (!new File(this.mdePathFqpn).exists()) {

                this.isServer = false;
                System.out.println("Unable to find gql script " + this.mdePathFqpn + " reverting to test script");
                this.mdePathFqpn = configSettings
                        .getFullyQualifiedFilePath(null, GobiiFileProcessDir.CODE_EXTRACTORS_POSTGRES_MDE)
                        + GQL_DUMMY_SCRIPT_NAME;

                File dummyScriptFile = new File(this.mdePathFqpn);
                if (!dummyScriptFile.exists()) {
                    String resourcePath = GQL_DUMMY_SCRIPT_NAME;
                    InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath);
                    if (inputStream != null) {
                        FileUtils.copyInputStreamToFile(inputStream, dummyScriptFile);
                    } else {
                        throw new GobiiDaoException("Unable to make input stream for " + resourcePath);
                    }
                }
            } else {
                this.isServer = true;
            } // if-else gql script is present

        } catch (Exception e) {
            throw new GobiiException(e);
        }
    }

    private final static String GQL_SCRIPT_NAME = "gobii_gql.py";
    private final String GQL_DUMMY_SCRIPT_NAME = "gobii_gql_placeholder.py";


    private final String GQL_PARM_CONNECTION_STRING = "-c";
    private final String GQL_PARM_OUTPUT_FILE_PATH = "-o";
    private final String GQL_PARM_SUB_GRAPH_PATH = "-g"; // OPTIONAL: JSON string of key-value-pairs of this format: {vertex_name1:[value_id1, value_id2], vertex_name2:[value_id1], ...}.
    private final String GQL_PARM_TARGET_VERTEX_NAME = "-t";
    private final String GQL_PARM_TARGET_VERTEX_COLUMNS = "-f"; // OPTIONAL
    private final String GQL_PARM_COUNT_LIMIT = "-l";
    private final String GQL_PARM_UNIQUE = "-u";

    public String makeGqlJobPath() throws GobiiDaoException {

        String returnVal;

        try {

            returnVal = (new ConfigSettings()).getFullyQualifiedFilePath(this.cropType, GobiiFileProcessDir.GQL_PROCESS);
            returnVal += "/" + this.jobId + "/";
        } catch (Exception e) {
            throw new GobiiDaoException(e);
        }

        return returnVal;
    }


    public String makeGqlJobFileFqpn(GqlOFileType gqlOFileType, GqlDestinationFileType gqlDestinationFileType) {

        String returnVal = this.makeGqlJobPath();

        String fileName = gqlDestinationFileType.getDestination();
        if (gqlOFileType.getIoName().equals(GqlOFileType.NONE.getIoName())) {
            fileName += ".txt";
        } else {
            fileName += gqlOFileType.getIoName();
        }

        returnVal = this.makeNextFileName(fileName, 3);
        return returnVal;
    }

    private String makeArg(String gqlFlag, String argVal, boolean addSingleQuotes) {

        String quoteChar = addSingleQuotes ? "'" : "";
        String space = " ";
        return gqlFlag + space + quoteChar + argVal + quoteChar + space;
    }

    //python gobii_gql.py /temp/filter3.out {principal_investigator:[67,69,70], project:[3,25,30], division:[25,30]} experiment ['name']
    public String makeCommandLine(String outputFileFqpn,
                                  List<VertexDTO> subGraphVertices,
                                  VertexDTO destinationVertex,
                                  Integer maxResult) throws Exception {

        String returnVal;

        StringBuilder commandLineBuilder = new StringBuilder();

        // path to gql script
        commandLineBuilder.append("python " + mdePathFqpn + " ");

        commandLineBuilder.append(this.makeArg(GQL_PARM_CONNECTION_STRING,
                this.connectionString,
                false));

        // output fqpn
        Path path = Paths.get(outputFileFqpn);
        Path parent = path.getParent();
        if (!new File(parent.toUri()).exists()) {
            throw new GobiiDaoException("The specified output directory does not exist: " + parent);
        }


        commandLineBuilder.append(this.makeArg(GQL_PARM_OUTPUT_FILE_PATH,
                outputFileFqpn,
                false));

        // filter path
        if (subGraphVertices.size() > 0) {
            StringBuilder subGraphPath = new StringBuilder();
            subGraphPath.append("{");
            Iterator<VertexDTO> filterPathIterator = subGraphVertices.iterator();
            while (filterPathIterator.hasNext()) {

                subGraphPath.append(filterPathIterator.next().toGqlSubPathElement());
                if (filterPathIterator.hasNext()) {
                    subGraphPath.append(",");
                }
            }
            subGraphPath.append("}");
            commandLineBuilder.append(this.makeArg(GQL_PARM_SUB_GRAPH_PATH,
                    subGraphPath.toString(),
                    true));
        } // if there is a filter path

        commandLineBuilder.append(this.makeArg(GQL_PARM_TARGET_VERTEX_NAME,
                destinationVertex.getVertexNameType().getVertexName(),
                false));

        StringBuilder columnList = new StringBuilder();
        columnList.append("[");
        Iterator<String> columnsIterator = destinationVertex.getVertexColumns().getColumnNames().iterator();
        if (columnsIterator.hasNext()) {
            columnsIterator.next(); // first column should always be ID -- which we don't need in our arg list
        }
        while (columnsIterator.hasNext()) {
            columnList.append("\"" + columnsIterator.next() + "\"");
            if (columnsIterator.hasNext()) {
                columnList.append(",");
            }
        }
        columnList.append("]");
        commandLineBuilder.append(this.makeArg(GQL_PARM_TARGET_VERTEX_COLUMNS,
                columnList.toString(),
                true));


        commandLineBuilder.append(this.makeArg(GQL_PARM_COUNT_LIMIT,
                maxResult.toString(),
                false));

        if (destinationVertex.getGobiiVertexType().equals(GobiiVertexType.CVTERM)) {
            commandLineBuilder.append(this.makeArg(GQL_PARM_UNIQUE,
                    "",
                    false));
        }

        String plainCommand = commandLineBuilder.toString();

        if (this.isServer) {
            returnVal = plainCommand.replace("'", "");

            //plainCommand = plainCommand.replace("\"", "\\\"");
//            returnVal = "ssh -tt gadm@cbsugobii03.tc.cornell.edu -p 2222 \""
//                    + plainCommand
//                    + "\"";
        } else {
            returnVal = plainCommand;
        }

        this.writeCommandlineFile(returnVal, outputFileFqpn);

        return returnVal;
    }


    private String makeNextFileName(String fileName, Integer incrementLength) throws GobiiDaoException {

        String returnVal;

        String currentDirectory = this.makeGqlJobPath();

        String extension = FilenameUtils.getExtension(fileName);
        String fileNameStem =
                FilenameUtils.removeExtension(new File(fileName).getName());

        Integer currentIncrement;
        File[] fileArray = new File(currentDirectory).listFiles();
        if (fileArray != null) {
            Arrays.sort(fileArray, new Comparator<File>() {
                public int compare(File f1, File f2) {
                    return Long.compare(f2.lastModified(), f1.lastModified());
                }
            });
            List<File> fileList = Arrays.asList(fileArray);
            List<File> matchingFile = fileList
                    .stream()
                    .filter(file -> file.getName().contains(fileNameStem))
                    .collect(Collectors.toList());


            if (matchingFile.size() > 0) {

                File mostRecentFile = matchingFile.get(0);
                String mostRecentFileName = mostRecentFile.getName();
                Integer segmentBeginIdx = mostRecentFileName.length() - extension.length() - incrementLength - 1;
                String mostRecentIncrementSegment = mostRecentFileName.substring(
                        segmentBeginIdx,
                        segmentBeginIdx + incrementLength
                );

                currentIncrement = Integer.parseInt(mostRecentIncrementSegment);

            } else {
                currentIncrement = 0;
            }

        } else {
            currentIncrement = 0;
        }

        String formatString = "%0" + incrementLength + "d";
        String incrementSegment = String.format(formatString, currentIncrement + 1);
        returnVal = currentDirectory + fileNameStem + "_" + incrementSegment + "." + extension;
        return returnVal;
    }


    private void writeCommandlineFile(String commandline, String outputFileFqpn) throws GobiiDaoException {

        try {
            String fileName = FilenameUtils.removeExtension(outputFileFqpn) + "_cmd.txt";
            FileUtils.writeStringToFile(new File(fileName), commandline);

        } catch (IOException e) {
            throw new GobiiDaoException(e);
        }

    } // writeCommandlineFile()

    public List<NameIdDTO> makeValues(String fqpn, VertexDTO destinationVertex) throws Exception {

        List<NameIdDTO> returnVal = new ArrayList<>();

        File file = new File(fqpn);
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        StringBuffer stringBuffer = new StringBuffer();
        String line;
        bufferedReader.readLine(); // consume first header line
        while ((line = bufferedReader.readLine()) != null) {

            NameIdDTO currentNameIdDto = destinationVertex.getVertexColumns().vertexValueFromLine(line);
            currentNameIdDto.setGobiiEntityNameType(destinationVertex.getEntityType());
            returnVal.add(currentNameIdDto);
        }
        fileReader.close();

        return returnVal;

    }
}
