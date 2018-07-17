package org.gobiiproject.gobiimodel.dto.entity.flex;

import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.cvnames.CvGroup;
import org.gobiiproject.gobiimodel.cvnames.VertexNameType;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.gobiiproject.gobiimodel.types.GobiiEntitySubType;
import org.gobiiproject.gobiimodel.types.GobiiVertexType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Vertices {

    private static List<VertexDTO> vertexList = new ArrayList<>(
            Arrays.asList(

                    new VertexDTO(1,
                            VertexNameType.VERTEX_TYPE_PROJECT,
                            GobiiVertexType.ENTITY,
                            "Projects",
                            GobiiEntityNameType.PROJECT,
                            GobiiEntitySubType.UNKNOWN,
                            CvGroup.UNKNOWN,
                            null),

                    new VertexDTO(2,
                            VertexNameType.VERTEX_TYPE_EXPERIMENT,
                            GobiiVertexType.ENTITY,
                            "Experiment",
                            GobiiEntityNameType.EXPERIMENT,
                            GobiiEntitySubType.UNKNOWN,
                            CvGroup.UNKNOWN,
                            null),

                    new VertexDTO(3,
                            VertexNameType.VERTEX_TYPE_ANALYSIS,
                            GobiiVertexType.ENTITY,
                            "Analysis",
                            GobiiEntityNameType.ANALYSIS,
                            GobiiEntitySubType.UNKNOWN,
                            CvGroup.UNKNOWN,
                            null),

                    new VertexDTO(4,
                            VertexNameType.VERTEX_TYPE_MAPSET_TYPE,
                            GobiiVertexType.CVGROUP,
                            "Mapset Type",
                            GobiiEntityNameType.CV,
                            GobiiEntitySubType.UNKNOWN,
                            CvGroup.MAPSET_TYPE,
                            null),


                    new VertexDTO(5,
                            VertexNameType.VERTEX_TYPE_ANALYSIS_TYPE,
                            GobiiVertexType.CVGROUP,
                            "Calling Analysis",
                            GobiiEntityNameType.CV,
                            GobiiEntitySubType.UNKNOWN,
                            CvGroup.ANALYSIS_TYPE,
                            null),

                    new VertexDTO(7,
                            VertexNameType.VERTEX_TYPE_VENDOR_PROTOCOL,
                            GobiiVertexType.ENTITY,
                            "Vendor Protocols",
                            GobiiEntityNameType.VENDOR_PROTOCOL,
                            GobiiEntitySubType.UNKNOWN,
                            CvGroup.UNKNOWN,
                            null),


                    new VertexDTO(8,
                            VertexNameType.VERTEX_TYPE_PLATFORM,
                            GobiiVertexType.ENTITY,
                            "Platforms",
                            GobiiEntityNameType.PLATFORM,
                            GobiiEntitySubType.UNKNOWN,
                            CvGroup.UNKNOWN,
                            null),

                    new VertexDTO(9,
                            VertexNameType.VERTEX_TYPE_MAPSET,
                            GobiiVertexType.ENTITY,
                            "Mapset",
                            GobiiEntityNameType.MAPSET,
                            GobiiEntitySubType.UNKNOWN,
                            CvGroup.UNKNOWN,
                            null),

                    new VertexDTO(10,
                            VertexNameType.VERTEX_TYPE_LINKAGE_GROUP,
                            GobiiVertexType.ENTITY,
                            "Linkage Group",
                            GobiiEntityNameType.LINKAGE_GROUP,
                            GobiiEntitySubType.UNKNOWN,
                            CvGroup.UNKNOWN,
                            null),


                    new VertexDTO(11,
                            VertexNameType.VERTEX_TYPE_PRINCIPLE_INVESTIGATOR,
                            GobiiVertexType.SUBENTITY,
                            "Principal Investigator",
                            GobiiEntityNameType.CONTACT,
                            GobiiEntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR,
                            CvGroup.UNKNOWN,
                            null),


                    new VertexDTO(12,
                            VertexNameType.VERTEX_TYPE_GERMPLAM_SUBSPECIES,
                            GobiiVertexType.CVTERM,
                            "Germplasm Subspecies",
                            GobiiEntityNameType.GERMPLASM,
                            GobiiEntitySubType.UNKNOWN,
                            CvGroup.GERMPLASM_PROP,
                            "germplasm_subsp"),

                    new VertexDTO(13,
                            VertexNameType.VERTEX_TYPE_GERMPLASM_TYPE,
                            GobiiVertexType.CVGROUP,
                            "Germplasm Type",
                            GobiiEntityNameType.CV,
                            GobiiEntitySubType.UNKNOWN,
                            CvGroup.GERMPLASM_TYPE,
                            null),


                    new VertexDTO(14,
                            VertexNameType.VERTEX_TYPE_REFERENCE_SAMPLE,
                            GobiiVertexType.CVTERM,
                            "DNA Ref Sample",
                            GobiiEntityNameType.DNA_SAMPLE,
                            GobiiEntitySubType.UNKNOWN,
                            CvGroup.DNASAMPLE_PROP,
                            "ref_sample"),

                    new VertexDTO(15,
                            VertexNameType.VERTEX_TYPE_TRIAL_NAME,
                            GobiiVertexType.CVTERM,
                            "DNA Trial",
                            GobiiEntityNameType.DNA_SAMPLE,
                            GobiiEntitySubType.UNKNOWN,
                            CvGroup.DNASAMPLE_PROP,
                            "trial_name"),

                    new VertexDTO(16,
                            VertexNameType.VERTEX_TYPE_SAMPLING_DATE,
                            GobiiVertexType.CVTERM,
                            "Date Sampled",
                            GobiiEntityNameType.PROJECT,
                            GobiiEntitySubType.UNKNOWN,
                            CvGroup.PROJECT_PROP,
                            "date_sampled"),

                    new VertexDTO(17,
                            VertexNameType.VERTEX_TYPE_GENOTYPING_PURPOSE,
                            GobiiVertexType.CVTERM,
                            "Genotyping Purpose",
                            GobiiEntityNameType.PROJECT,
                            GobiiEntitySubType.UNKNOWN,
                            CvGroup.PROJECT_PROP,
                            "genotyping_purpose"),

                    new VertexDTO(18,
                            VertexNameType.VERTEX_TYPE_DIVISION,
                            GobiiVertexType.CVTERM,
                            "Division",
                            GobiiEntityNameType.PROJECT,
                            GobiiEntitySubType.UNKNOWN,
                            CvGroup.PROJECT_PROP,
                            "division"),

                    new VertexDTO(19,
                            VertexNameType.VERTEX_TYPE_DATASET_TYPE,
                            GobiiVertexType.CVGROUP,
                            "Dataset Type",
                            GobiiEntityNameType.CV,
                            GobiiEntitySubType.UNKNOWN,
                            CvGroup.DATASET_TYPE,
                            null),

                    new VertexDTO(20,
                            VertexNameType.VERTEX_TYPE_REFERENCE_SAMPLE,
                            GobiiVertexType.CVTERM,
                            "Reference Sample",
                            GobiiEntityNameType.DNA_SAMPLE,
                            GobiiEntitySubType.UNKNOWN,
                            CvGroup.DNASAMPLE_PROP,
                            "ref_sample"),

                    new VertexDTO(21,
                            VertexNameType.VERTEX_TYPE_VENDOR,
                            GobiiVertexType.ENTITY,
                            "Vendor",
                            GobiiEntityNameType.VENDOR,
                            GobiiEntitySubType.UNKNOWN,
                            CvGroup.UNKNOWN,
                            null),

                    new VertexDTO(22,
                            VertexNameType.VERTEX_TYPE_PROTOCOL,
                            GobiiVertexType.ENTITY,
                            "Protocol",
                            GobiiEntityNameType.PROTOCOL,
                            GobiiEntitySubType.UNKNOWN,
                            CvGroup.UNKNOWN,
                            null),

                    new VertexDTO(23,
                            VertexNameType.VERTEX_TYPE_GERMPLAM_SUBSPECIES,
                            GobiiVertexType.CVTERM,
                            "Germplasm Species",
                            GobiiEntityNameType.GERMPLASM,
                            GobiiEntitySubType.UNKNOWN,
                            CvGroup.GERMPLASM_PROP,
                            "germplasm_species"),

                    new VertexDTO(24,
                            VertexNameType.VERTEX_TYPE_DATASET,
                            GobiiVertexType.ENTITY,
                            "Dataset",
                            GobiiEntityNameType.DATASET,
                            GobiiEntitySubType.UNKNOWN,
                            CvGroup.UNKNOWN,
                            null)
            )
    );


    /***
     * Must be separate from the main collection because it is isEntry=false
     * @return
     */
    public static VertexDTO makeMarkerVertex() {

        return new VertexDTO(25,
                VertexNameType.VERTEX_TYPE_MARKER,
                GobiiVertexType.ENTITY,
                "marker",
                GobiiEntityNameType.DATASET,
                GobiiEntitySubType.UNKNOWN,
                CvGroup.UNKNOWN,
                null);
    }

    /***
     * Must be separate from the main collection because it is isEntry=false
     * @return
     */
    public static VertexDTO makeSampleVertex() {

        return new VertexDTO(26,
                VertexNameType.VERTEX_TYPE_DNASAMPLE,
                GobiiVertexType.ENTITY,
                "dnasample",
                GobiiEntityNameType.DATASET,
                GobiiEntitySubType.UNKNOWN,
                CvGroup.UNKNOWN,
                null);
    }

    public static List<VertexDTO> getAll() {
        return vertexList;
    }

    public static VertexDTO getByVertexName(VertexNameType vertexNameType) {

        VertexDTO returnVal = null;

        List<VertexDTO> matching =
                vertexList.stream()
                        .filter(v -> v.getVertexNameType().getVertexName().equals(vertexNameType.getVertexName()))
                        .collect(Collectors.toList());

        if (matching.size() == 1) {
            returnVal = matching.get(0);
        } else if (matching.size() > 1) {
            throw (new GobiiException("There are more than one vertices defined for type " + vertexNameType.getVertexName()));
        }

        return returnVal;
    }


}
