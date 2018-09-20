System.register(["../model/type-entity", "../model/cv-group", "../model/type-extractor-filter", "../model//type-extractor-item", "../model/type-extract-format"], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var type_entity_1, cv_group_1, type_extractor_filter_1, type_extractor_item_1, type_extract_format_1, Labels;
    return {
        setters: [
            function (type_entity_1_1) {
                type_entity_1 = type_entity_1_1;
            },
            function (cv_group_1_1) {
                cv_group_1 = cv_group_1_1;
            },
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
            },
            function (type_extractor_item_1_1) {
                type_extractor_item_1 = type_extractor_item_1_1;
            },
            function (type_extract_format_1_1) {
                type_extract_format_1 = type_extract_format_1_1;
            }
        ],
        execute: function () {
            Labels = (function () {
                function Labels() {
                    this.entityNodeLabels = new Map();
                    this.entitySubtypeNodeLabels = new Map();
                    this.cvGroupLabels = new Map();
                    this.extractorFilterTypeLabels = new Map();
                    this.treeExtractorTypeLabels = new Map();
                    this.extractFormatTypeLabels = new Map();
                    this.entityNodeLabels[type_entity_1.EntityType.UNKNOWN] = "UNKNOWN";
                    this.entityNodeLabels[type_entity_1.EntityType.ANALYSIS] = "Analysis";
                    this.entityNodeLabels[type_entity_1.EntityType.CONTACT] = "Contact";
                    this.entityNodeLabels[type_entity_1.EntityType.DATASET] = "Dataset";
                    this.entityNodeLabels[type_entity_1.EntityType.CV] = "CV Term";
                    this.entityNodeLabels[type_entity_1.EntityType.CVGROUP] = "CV Group";
                    this.entityNodeLabels[type_entity_1.EntityType.PROJECT] = "Project";
                    this.entityNodeLabels[type_entity_1.EntityType.ORGANIZATION] = "Organization";
                    this.entityNodeLabels[type_entity_1.EntityType.PLATFORM] = "Platform";
                    this.entityNodeLabels[type_entity_1.EntityType.MANIFEST] = "Manifest";
                    this.entityNodeLabels[type_entity_1.EntityType.MAPSET] = "Mapset";
                    this.entityNodeLabels[type_entity_1.EntityType.MARKER_GROUP] = "Marker Group";
                    this.entityNodeLabels[type_entity_1.EntityType.EXPERIMENT] = "Experiment";
                    this.entityNodeLabels[type_entity_1.EntityType.REFERENCE] = "Reference";
                    this.entityNodeLabels[type_entity_1.EntityType.ROLE] = "Role";
                    this.entityNodeLabels[type_entity_1.EntityType.DISPLAY] = "Display";
                    this.entityNodeLabels[type_entity_1.EntityType.MARKER] = "Marker";
                    this.entityNodeLabels[type_entity_1.EntityType.PROTOCOL] = "Protocol";
                    this.entityNodeLabels[type_entity_1.EntityType.VENDOR_PROTOCOL] = "Vendor-Protocol";
                    this.entityNodeLabels[type_entity_1.EntityType.GERMPLASM] = "Germplasm";
                    this.entityNodeLabels[type_entity_1.EntityType.LINKAGE_GROUP] = "Linkage Group";
                    this.entityNodeLabels[type_entity_1.EntityType.DNA_SAMPLE] = "DNA Sample";
                    this.entityNodeLabels[type_entity_1.EntityType.VENDOR] = "Vendor";
                    this.cvGroupLabels[cv_group_1.CvGroup.JOBTYPE] = "Job Type";
                    this.cvGroupLabels[cv_group_1.CvGroup.PAYLOADTYPE] = "Payload Type";
                    this.cvGroupLabels[cv_group_1.CvGroup.JOBSTATUS] = "Job Status";
                    this.cvGroupLabels[cv_group_1.CvGroup.ANALYSIS_TYPE] = "Calling Analysis";
                    this.cvGroupLabels[cv_group_1.CvGroup.DATASET_TYPE] = "Dataset Type";
                    this.cvGroupLabels[cv_group_1.CvGroup.DNARUN_PROP] = "DNA Run Prop";
                    this.cvGroupLabels[cv_group_1.CvGroup.DNASAMPLE_PROP] = "DNA Sample Prop";
                    this.cvGroupLabels[cv_group_1.CvGroup.PROJECT_PROP] = "Project Prop Prop";
                    this.cvGroupLabels[cv_group_1.CvGroup.GERMPLASM_PROP] = "Germplasm Prop";
                    this.cvGroupLabels[cv_group_1.CvGroup.GERMPLASM_SPECIES] = "Germplasm Species";
                    this.cvGroupLabels[cv_group_1.CvGroup.GERMPLASM_TYPE] = "Germplasm Type";
                    this.cvGroupLabels[cv_group_1.CvGroup.GOBII_DATAWAREHOUSE] = "GOBii Datawarehouse";
                    this.cvGroupLabels[cv_group_1.CvGroup.MAPSET_TYPE] = "Mapset Type";
                    this.cvGroupLabels[cv_group_1.CvGroup.MARKER_PROP] = "Marker Prop";
                    this.cvGroupLabels[cv_group_1.CvGroup.MARKER_STRAND] = "Marker Strand";
                    this.cvGroupLabels[cv_group_1.CvGroup.PLATFORM_TYPE] = "Platform Type";
                    this.cvGroupLabels[cv_group_1.CvGroup.PROJECT_PROP] = "Project Prop";
                    this.cvGroupLabels[cv_group_1.CvGroup.STATUS] = "Status";
                    this.entitySubtypeNodeLabels[type_entity_1.EntitySubType.UNKNOWN] = "UNKOWN";
                    this.entitySubtypeNodeLabels[type_entity_1.EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR] = "Principle Investigator";
                    this.entitySubtypeNodeLabels[type_entity_1.EntitySubType.CONTACT_SUBMITED_BY] = "Submit As";
                    this.extractorFilterTypeLabels[type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET] = "By Dataset";
                    this.extractorFilterTypeLabels[type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE] = "By Sample";
                    this.extractorFilterTypeLabels[type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER] = "By Marker";
                    this.treeExtractorTypeLabels[type_extractor_item_1.ExtractorItemType.SAMPLE_LIST_ITEM] = "Sample List";
                    this.treeExtractorTypeLabels[type_extractor_item_1.ExtractorItemType.SAMPLE_INPUT_FILE] = "Sample File";
                    this.treeExtractorTypeLabels[type_extractor_item_1.ExtractorItemType.MARKER_LIST_ITEM] = "Marker List";
                    this.treeExtractorTypeLabels[type_extractor_item_1.ExtractorItemType.MARKER_INPUT_FILE] = "Marker File";
                    this.treeExtractorTypeLabels[type_extractor_item_1.ExtractorItemType.CROP_TYPE] = "Crop Type";
                    this.treeExtractorTypeLabels[type_extractor_item_1.ExtractorItemType.EXPORT_FORMAT] = "Format";
                    this.treeExtractorTypeLabels[type_extractor_item_1.ExtractorItemType.JOB_ID] = "Job ID";
                    this.treeExtractorTypeLabels[type_extractor_item_1.ExtractorItemType.SAMPLE_LIST_TYPE] = "List Type";
                    this.treeExtractorTypeLabels[type_extractor_item_1.ExtractorItemType.VERTEX] = "Entity Type";
                    this.extractFormatTypeLabels[type_extract_format_1.GobiiExtractFormat.HAPMAP] = "Hapmap";
                    this.extractFormatTypeLabels[type_extract_format_1.GobiiExtractFormat.FLAPJACK] = "Flapjack";
                    this.extractFormatTypeLabels[type_extract_format_1.GobiiExtractFormat.META_DATA_ONLY] = "Meta Data";
                }
                Labels.instance = function () {
                    if (this._instance === null) {
                        this._instance = new Labels();
                    }
                    return this._instance;
                };
                Labels._instance = null;
                return Labels;
            }());
            exports_1("Labels", Labels);
        }
    };
});
//# sourceMappingURL=entity-labels.js.map