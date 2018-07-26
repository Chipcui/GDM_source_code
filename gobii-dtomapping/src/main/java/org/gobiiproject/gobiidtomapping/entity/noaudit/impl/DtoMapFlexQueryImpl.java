package org.gobiiproject.gobiidtomapping.entity.noaudit.impl;

import ch.qos.logback.core.util.FileUtil;
import com.sun.javafx.scene.shape.PathUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.StopWatch;
import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.gql.GqlOFileType;
import org.gobiiproject.gobiidao.gql.GqlDestinationFileType;
import org.gobiiproject.gobiidao.gql.GqlText;
import org.gobiiproject.gobiidao.gql.GqlWrapper;
import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiidtomapping.entity.auditable.impl.DtoMapDataSetImpl;
import org.gobiiproject.gobiidtomapping.entity.noaudit.DtoMapFlexQuery;
import org.gobiiproject.gobiimodel.dto.entity.children.NameIdDTO;
import org.gobiiproject.gobiimodel.dto.entity.flex.VertexDTO;
import org.gobiiproject.gobiimodel.dto.entity.flex.VertexFilterDTO;
import org.gobiiproject.gobiimodel.dto.entity.flex.Vertices;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiExtractorInstruction;
import org.gobiiproject.gobiimodel.utils.InstructionFileAccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by Phil on 4/22/2016.
 */
public class DtoMapFlexQueryImpl implements DtoMapFlexQuery {


    private Integer maxVertexValues = 500;
    private Integer maxCount = 100000;

    Logger LOGGER = LoggerFactory.getLogger(DtoMapDataSetImpl.class);
    private InstructionFileAccess<GobiiExtractorInstruction> instructionFileAccess = new InstructionFileAccess<>(GobiiExtractorInstruction.class);


    /**
     * This method returns a list of vertices. Nominally, the vertices returned are those defined in the vertex table.
     * However, the intention of this method is to return only those vertices for which is_entry is true -- i.e., the
     * vertices that are intended to be supplied, by ID, as inputs to the search algorithm. The semantics
     * of the vertex table are table are intended to be consumed primarily by the the search algorithm, the purview
     * of which lies outside the immediate scope of web services. Rather, it is the purpose of this method to provide
     * to the web client the vertex list that it requires to, in effect, enable the user to formulate a search that
     * can be serviced by the algorithm. Accordingly, the VertexDTO represents a vertex in semantics that are necessary for
     * proper display in the web application. To this end, the list returned by this method is essentially static. The only
     * relationship that the VertexDTO list need have with the vertex table is that the vertex IDs must match the values
     * in the vertex table for the intended entity. The downside to this approach is that when new vertices for which is_entry
     * is true are added, this code will ened to be updated manually. The infrequency of such changes makes this approach
     * more economical than would be the engineering effort of correlating the vertex table's semantics with those that
     * are required by the web application.
     *
     * @return
     * @throws GobiiDtoMappingException
     */
    public List<VertexDTO> getVertices() throws GobiiDtoMappingException {
        return Vertices.getAll();
    }


    private void makeOutputDirectory(String outputFileDirectory) throws Exception {

        if (!instructionFileAccess.doesPathExist(outputFileDirectory)) {

            instructionFileAccess.makeDirectory(outputFileDirectory);

        } else {
            instructionFileAccess.verifyDirectoryPermissions(outputFileDirectory);
        }
    }

    @Override
    public VertexFilterDTO getVertexValues(String cropType, String jobId, VertexFilterDTO vertexFilterDTO) throws GobiiDtoMappingException {

        VertexFilterDTO returnVal = vertexFilterDTO;

        try {

            GqlText gqlText = new GqlText(cropType, jobId);
            String outputFileDirectory = gqlText.makeGqlJobPath();
            this.makeOutputDirectory(outputFileDirectory);

            String outputFileFqpn = gqlText.makeGqlJobFileFqpn(GqlOFileType.NONE, GqlDestinationFileType.DST_VALUES);
            String stdOutFileFqpn = gqlText.makeGqlJobFileFqpn(GqlOFileType.IO_FILE_STD_OUT, GqlDestinationFileType.DST_VALUES);
            String stdErrFileFqpn = gqlText.makeGqlJobFileFqpn(GqlOFileType.IO_FILE_STD_ERR, GqlDestinationFileType.DST_VALUES);

            String gqlScriptCommandLine = gqlText.makeCommandLine(outputFileFqpn,
                    vertexFilterDTO.getFilterVertices(),
                    vertexFilterDTO.getDestinationVertexDTO(),
                    maxVertexValues);

            Integer valuesRunReturn = GqlWrapper.run(gqlScriptCommandLine, stdOutFileFqpn, stdErrFileFqpn);
            if (valuesRunReturn.equals(GqlWrapper.GQL_RETURN_SUCCESS)) {
                List<NameIdDTO> values = gqlText.makeValues(outputFileFqpn, vertexFilterDTO.getDestinationVertexDTO());
                returnVal.getVertexValues().addAll(values);
            } else {
                throw new GobiiDaoException(GqlWrapper.message());
            }

        } catch (Exception e) {
            LOGGER.error("Gobii Maping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;

    } // getVertexValues()

    public VertexFilterDTO getVertexValuesCounts(String cropType, String jobId, VertexFilterDTO vertexFilterDTO) throws GobiiDtoMappingException {

        VertexFilterDTO returnVal = vertexFilterDTO;

        try {

            GqlText gqlText = new GqlText(cropType, jobId);
            String outputFileDirectory = gqlText.makeGqlJobPath();
            this.makeOutputDirectory(outputFileDirectory);

            String markerOutputFileFqpn = gqlText.makeGqlJobFileFqpn(GqlOFileType.NONE, GqlDestinationFileType.DST_COUNT_MARKER);
            String sampleOutputFileFqpn = gqlText.makeGqlJobFileFqpn(GqlOFileType.NONE, GqlDestinationFileType.DST_COUNT_SAMPLE);
            String stdOutFileFqpnMarkers = gqlText.makeGqlJobFileFqpn(GqlOFileType.IO_FILE_STD_OUT, GqlDestinationFileType.DST_COUNT_MARKER);
            String stdErrFileFqpnMarkers = gqlText.makeGqlJobFileFqpn(GqlOFileType.IO_FILE_STD_ERR, GqlDestinationFileType.DST_COUNT_MARKER);
            String stdOutFileFqpnSamples = gqlText.makeGqlJobFileFqpn(GqlOFileType.IO_FILE_STD_OUT, GqlDestinationFileType.DST_COUNT_SAMPLE);
            String stdErrFileFqpnSamples = gqlText.makeGqlJobFileFqpn(GqlOFileType.IO_FILE_STD_ERR, GqlDestinationFileType.DST_COUNT_SAMPLE);

            VertexDTO destinationVertexMarkers = Vertices.makeMarkerCountVertex();
            VertexDTO destinationVertexSamples = Vertices.makeSampleCountVertex();

            // ******* GET MARKER COUNT
            String gqlScriptCommandLineMarkers = gqlText.makeCommandLine(markerOutputFileFqpn,
                    vertexFilterDTO.getFilterVertices(),
                    destinationVertexMarkers,
                    maxCount);
            Long markerCount = 0L;

            StopWatch stopWatchMarkerCount = new StopWatch();
            stopWatchMarkerCount.start();
            Integer markerRunReturn = GqlWrapper.run(gqlScriptCommandLineMarkers, stdOutFileFqpnMarkers, stdErrFileFqpnMarkers);
            if (markerRunReturn.equals(GqlWrapper.GQL_RETURN_SUCCESS)
                    || markerRunReturn.equals(GqlWrapper.GQL_RETURN_NO_FILTERS_APPLIED_TO_TARGET)) {
                if (!markerRunReturn.equals(GqlWrapper.GQL_RETURN_NO_FILTERS_APPLIED_TO_TARGET)) {
                    markerCount = Files.lines(Paths.get(markerOutputFileFqpn)).count();
                }
            } else {
                throw new GobiiDaoException(GqlWrapper.message());
            }
            stopWatchMarkerCount.stop();
            Integer markerCountMs = Math.toIntExact(stopWatchMarkerCount.getTime());

            // ******* GET SAMPLE COUNT
            String gqlScriptCommandLineSamples = gqlText.makeCommandLine(sampleOutputFileFqpn,
                    vertexFilterDTO.getFilterVertices(),
                    destinationVertexSamples,
                    maxCount);
            Long sampleCount = 0L;
            StopWatch stopWatchSampleCount = new StopWatch();
            stopWatchSampleCount.start();
            Integer sampleRunReturn = GqlWrapper.run(gqlScriptCommandLineSamples, stdOutFileFqpnSamples, stdErrFileFqpnSamples);
            if (sampleRunReturn.equals(GqlWrapper.GQL_RETURN_SUCCESS)
                    || sampleRunReturn.equals(GqlWrapper.GQL_RETURN_NO_FILTERS_APPLIED_TO_TARGET)) {
                if (!sampleRunReturn.equals(GqlWrapper.GQL_RETURN_NO_FILTERS_APPLIED_TO_TARGET)) {
                    sampleCount = Files.lines(Paths.get(sampleOutputFileFqpn)).count();
                }
            } else {
                throw new GobiiDaoException(GqlWrapper.message());
            }
            stopWatchSampleCount.stop();
            Integer sampleCountMs = Math.toIntExact(stopWatchSampleCount.getTime());

            if (markerCount > Integer.MAX_VALUE) {
                throw new GobiiDtoMappingException("Number of markers is too large to fit in an Integer: " + markerCount);
            }

            if (sampleCount > Integer.MAX_VALUE) {
                throw new GobiiDtoMappingException("Number of samples is too large to fit in an Integer: " + sampleCount);
            }


            vertexFilterDTO.setMarkerCount(markerCount.intValue(), Paths.get(markerOutputFileFqpn).getFileName().toString(), markerCountMs);
            vertexFilterDTO.setSampleCount(sampleCount.intValue(), Paths.get(sampleOutputFileFqpn).getFileName().toString(), sampleCountMs);

        } catch (Exception e) {
            LOGGER.error("Gobii Maping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }

}
