System.register([], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var FilterParamNames;
    return {
        setters: [],
        execute: function () {
            FilterParamNames = (function () {
                // The problem with using an enum is that in the debugger
                // you cannot readily see what the enum type value means
                // Knowing the nameidfilterparam type on debugger inspection is
                // particularly important because of the generic way in which nameids
                // are treated in the components and services
                function FilterParamNames() {
                }
                // WARNING: If you change the value of one of these variables, you will
                // seriously break the Selenium unit tests: any changes to these values
                // will have to be cascaded to the unit tests!!!
                FilterParamNames.UNKNOWN = "UNKNOWN";
                FilterParamNames.CONTACT_PI_HIERARCHY_ROOT = "Contact-PI-Hierarchy-Root";
                FilterParamNames.EXPERIMENTS = "Experiments";
                FilterParamNames.EXPERIMENTS_BY_PROJECT = "Experiments-by-project";
                FilterParamNames.CV_DATATYPE = "Cv-DataType";
                FilterParamNames.CV_JOB_STATUS = "Cv-JobStatus";
                FilterParamNames.MAPSETS = "Mapsets";
                FilterParamNames.PLATFORMS = "Platforms";
                FilterParamNames.DATASETS_BY_EXPERIMENT = "Datasets-by-experiment";
                FilterParamNames.DATASET_LIST = "Dataset-List";
                FilterParamNames.DATASET_LIST_PAGED = "Dataset-List-Paged";
                FilterParamNames.DATASET_LIST_STATUS = "Dataset-List-Status";
                FilterParamNames.DATASET_BY_DATASET_ID = "Dataset-List-By-DatasetId";
                FilterParamNames.ANALYSES_BY_DATASET_ID = "Analysis-List-By-DatasetId";
                FilterParamNames.PROJECTS_BY_CONTACT = "Projects-by-contact";
                FilterParamNames.PROJECTS = "Project";
                FilterParamNames.MARKER_GROUPS = "Marker Groups";
                FilterParamNames.CONTACT_PI_FILTER_OPTIONAL = "Contact-Pi-Filter-Optional";
                FilterParamNames.PROJECT_FILTER_OPTIONAL = "Project-Filter-Optional";
                FilterParamNames.EXPERIMENT_FILTER_OPTIONAL = "Experiment-Filter-Optional";
                FilterParamNames.DATASET_FILTER_OPTIONAL = "Dataset-Filter-Optional";
                FilterParamNames.FQ_F1_VERTICES = "Flex-Query_F1_Vertices";
                FilterParamNames.FQ_F1_VERTEX_VALUES = "Flex-Query_F1_VertexValues";
                FilterParamNames.FQ_F2_VERTICES = "Flex-Query_F2_Vertices";
                FilterParamNames.FQ_F2_VERTEX_VALUES = "Flex-Query_F2_VertexValues";
                FilterParamNames.FQ_F3_VERTICES = "Flex-Query_F3_Vertices";
                FilterParamNames.FQ_F3_VERTEX_VALUES = "Flex-Query_F3_VertexValues";
                FilterParamNames.FQ_F4_VERTICES = "Flex-Query_F4_Vertices";
                FilterParamNames.FQ_F4_VERTEX_VALUES = "Flex-Query_F4_VertexValues";
                return FilterParamNames;
            }());
            exports_1("FilterParamNames", FilterParamNames);
        }
    };
});
//# sourceMappingURL=file-item-param-names.js.map