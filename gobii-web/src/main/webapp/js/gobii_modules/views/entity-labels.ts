import {EntityType, EntitySubType} from "../model/type-entity";
import {CvGroup} from "../model/cv-group";
import {GobiiExtractFilterType} from "../model/type-extractor-filter";
import {ExtractorItemType} from "../model//type-extractor-item";
import {GobiiExtractFormat} from "../model/type-extract-format";

export class Labels {


    private static _instance:Labels = null;

    public static instance():Labels {

        if( this._instance === null ) {
            this._instance = new Labels();
        }

        return this._instance;
    }

    private constructor() {
        this.entityNodeLabels[EntityType.UNKNOWN] = "UNKNOWN";
        this.entityNodeLabels[EntityType.ANALYSIS] = "Analysis";
        this.entityNodeLabels[EntityType.CONTACT] = "Contact";
        this.entityNodeLabels[EntityType.DATASET] = "Dataset";
        this.entityNodeLabels[EntityType.CV] = "CV Term";
        this.entityNodeLabels[EntityType.CVGROUP] = "CV Group";
        this.entityNodeLabels[EntityType.PROJECT] = "Project";
        this.entityNodeLabels[EntityType.ORGANIZATION] = "Organization";
        this.entityNodeLabels[EntityType.PLATFORM] = "Platform";
        this.entityNodeLabels[EntityType.MANIFEST] = "Manifest";
        this.entityNodeLabels[EntityType.MAPSET] = "Mapset";
        this.entityNodeLabels[EntityType.MARKER_GROUP] = "Marker Group";
        this.entityNodeLabels[EntityType.EXPERIMENT] = "Experiment";
        this.entityNodeLabels[EntityType.REFERENCE] = "Reference";
        this.entityNodeLabels[EntityType.ROLE] = "Role";
        this.entityNodeLabels[EntityType.DISPLAY] = "Display";
        this.entityNodeLabels[EntityType.MARKER] = "Marker";
        this.entityNodeLabels[EntityType.PROTOCOL] = "Protocol";
        this.entityNodeLabels[EntityType.VENDOR_PROTOCOL] = "Vendor-Protocol";
        this.entityNodeLabels[EntityType.GERMPLASM] = "Germplasm";
        this.entityNodeLabels[EntityType.LINKAGE_GROUP] = "Linkage Group";
        this.entityNodeLabels[EntityType.DNA_SAMPLE] = "DNA Sample";
        this.entityNodeLabels[EntityType.VENDOR] = "Vendor";

        
        this.cvGroupLabels[CvGroup.JOBTYPE] = "Job Type";
        this.cvGroupLabels[CvGroup.PAYLOADTYPE] = "Payload Type";
        this.cvGroupLabels[CvGroup.JOBSTATUS] = "Job Status";
        this.cvGroupLabels[CvGroup.ANALYSIS_TYPE] = "Calling Analysis";
        this.cvGroupLabels[CvGroup.DATASET_TYPE] = "Dataset Type";
        this.cvGroupLabels[CvGroup.DNARUN_PROP] = "DNA Run Prop";
        this.cvGroupLabels[CvGroup.DNASAMPLE_PROP] = "DNA Sample Prop";
        this.cvGroupLabels[CvGroup.PROJECT_PROP] = "Project Prop Prop";
        this.cvGroupLabels[CvGroup.GERMPLASM_PROP] = "Germplasm Prop";
        this.cvGroupLabels[CvGroup.GERMPLASM_SPECIES] = "Germplasm Species";
        this.cvGroupLabels[CvGroup.GERMPLASM_TYPE] = "Germplasm Type";
        this.cvGroupLabels[CvGroup.GOBII_DATAWAREHOUSE] = "GOBii Datawarehouse";
        this.cvGroupLabels[CvGroup.MAPSET_TYPE] = "Mapset Type";
        this.cvGroupLabels[CvGroup.MARKER_PROP] = "Marker Prop";
        this.cvGroupLabels[CvGroup.MARKER_STRAND] = "Marker Strand";
        this.cvGroupLabels[CvGroup.PLATFORM_TYPE] = "Platform Type";
        this.cvGroupLabels[CvGroup.PROJECT_PROP] = "Project Prop";
        this.cvGroupLabels[CvGroup.STATUS] = "Status";


        this.entitySubtypeNodeLabels[EntitySubType.UNKNOWN] = "UNKOWN";
        this.entitySubtypeNodeLabels[EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR] = "Principle Investigator";
        this.entitySubtypeNodeLabels[EntitySubType.CONTACT_SUBMITED_BY] = "Submit As";

        this.extractorFilterTypeLabels[GobiiExtractFilterType.WHOLE_DATASET] = "By Dataset";
        this.extractorFilterTypeLabels[GobiiExtractFilterType.BY_SAMPLE] = "By Sample";
        this.extractorFilterTypeLabels[GobiiExtractFilterType.BY_MARKER] = "By Marker";

        this.treeExtractorTypeLabels[ExtractorItemType.SAMPLE_LIST_ITEM] = "Sample List";
        this.treeExtractorTypeLabels[ExtractorItemType.SAMPLE_INPUT_FILE] = "Sample File";
        this.treeExtractorTypeLabels[ExtractorItemType.MARKER_LIST_ITEM] = "Marker List";
        this.treeExtractorTypeLabels[ExtractorItemType.MARKER_INPUT_FILE] = "Marker File";
        this.treeExtractorTypeLabels[ExtractorItemType.CROP_TYPE] = "Crop Type";
        this.treeExtractorTypeLabels[ExtractorItemType.EXPORT_FORMAT] = "Format";
        this.treeExtractorTypeLabels[ExtractorItemType.JOB_ID] = "Job ID";
        this.treeExtractorTypeLabels[ExtractorItemType.SAMPLE_LIST_TYPE] = "List Type";
        this.treeExtractorTypeLabels[ExtractorItemType.VERTEX] = "Entity Type";

        this.extractFormatTypeLabels[GobiiExtractFormat.HAPMAP] = "Hapmap";
        this.extractFormatTypeLabels[GobiiExtractFormat.FLAPJACK] = "Flapjack";
        this.extractFormatTypeLabels[GobiiExtractFormat.META_DATA_ONLY] = "Meta Data";

    }


    public entityNodeLabels: Map < EntityType, string > = new Map<EntityType,string>();
    public entitySubtypeNodeLabels: Map < EntitySubType, string > = new Map<EntitySubType,string>();
    public cvGroupLabels: Map < CvGroup, string > = new Map<CvGroup,string>();
    public extractorFilterTypeLabels: Map < GobiiExtractFilterType, string > = new Map<GobiiExtractFilterType, string>();
    public treeExtractorTypeLabels: Map<ExtractorItemType,string> = new Map<ExtractorItemType,string>();
    public extractFormatTypeLabels: Map<GobiiExtractFormat,string> = new Map<GobiiExtractFormat,string>();

}