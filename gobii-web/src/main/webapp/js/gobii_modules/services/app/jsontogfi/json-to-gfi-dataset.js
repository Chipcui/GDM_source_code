System.register(["../../../model/gobii-file-item", "../../../model/type-entity", "../../../model/type-extractor-item", "../../../model/type-process", "../../../model/type-extractor-filter", "../../../model/dataset", "../../../model/gobii-file-item-entity-relation", "../../../model/file-item-param-names"], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var gobii_file_item_1, type_entity_1, type_extractor_item_1, type_process_1, type_extractor_filter_1, dataset_1, gobii_file_item_entity_relation_1, file_item_param_names_1, JsonToGfiDataset;
    return {
        setters: [
            function (gobii_file_item_1_1) {
                gobii_file_item_1 = gobii_file_item_1_1;
            },
            function (type_entity_1_1) {
                type_entity_1 = type_entity_1_1;
            },
            function (type_extractor_item_1_1) {
                type_extractor_item_1 = type_extractor_item_1_1;
            },
            function (type_process_1_1) {
                type_process_1 = type_process_1_1;
            },
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
            },
            function (dataset_1_1) {
                dataset_1 = dataset_1_1;
            },
            function (gobii_file_item_entity_relation_1_1) {
                gobii_file_item_entity_relation_1 = gobii_file_item_entity_relation_1_1;
            },
            function (file_item_param_names_1_1) {
                file_item_param_names_1 = file_item_param_names_1_1;
            }
        ],
        execute: function () {
            JsonToGfiDataset = (function () {
                function JsonToGfiDataset(_filterParams, _filterParamsColl) {
                    this._filterParams = _filterParams;
                    this._filterParamsColl = _filterParamsColl;
                    this._filterParams = _filterParams;
                    this._filterParamsColl = _filterParamsColl;
                } //ctor
                JsonToGfiDataset.prototype.convert = function (jsonItem) {
                    var returnVal;
                    var dataset = new dataset_1.DataSet(jsonItem.id, jsonItem.piContactId, jsonItem.projectId, jsonItem.experimentId, jsonItem.datasetName, jsonItem.callingAnalysisId, jsonItem.dataTable, jsonItem.dataFile, jsonItem.qualityTable, jsonItem.qualityFile, jsonItem.status, jsonItem.datatypeId, jsonItem.analysesIds, new Date(jsonItem.createdDate), jsonItem.jobStatusId, jsonItem.jobStatusName, jsonItem.jobTypeId, jsonItem.jobTypeName, jsonItem.jobSubmittedDate ? new Date(jsonItem.jobSubmittedDate) : null, jsonItem.experimentName, jsonItem.projectName, jsonItem.protocolId, jsonItem.protocolName, jsonItem.platformId, jsonItem.platformName, jsonItem.callingAnalysisName, jsonItem.piEmail, jsonItem.piFirstName, jsonItem.piLastName, jsonItem.datatypeName, jsonItem.totalSamples, jsonItem.totalMarkers, jsonItem.modifiedDate ? new Date(jsonItem.modifiedDate) : null, jsonItem.loaderLastName, jsonItem.loaderFirstName);
                    var piContactRelation = gobii_file_item_entity_relation_1.GobiiFileItemEntityRelation
                        .fromGobiiFileItemCompoundId(this._filterParamsColl.getFilter(file_item_param_names_1.FilterParamNames.CONTACT_PI_FILTER_OPTIONAL, type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET).getTargetEntityUniqueId())
                        .setRelatedEntityId(dataset.piContactId != null ? dataset.piContactId.toString() : null);
                    var projectRelation = gobii_file_item_entity_relation_1.GobiiFileItemEntityRelation
                        .fromGobiiFileItemCompoundId(this._filterParamsColl.getFilter(file_item_param_names_1.FilterParamNames.PROJECT_FILTER_OPTIONAL, type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET).getTargetEntityUniqueId())
                        .setRelatedEntityId(dataset.projectId != null ? dataset.projectId.toString() : null);
                    var experimentRelation = gobii_file_item_entity_relation_1.GobiiFileItemEntityRelation
                        .fromGobiiFileItemCompoundId(this._filterParamsColl.getFilter(file_item_param_names_1.FilterParamNames.EXPERIMENT_FILTER_OPTIONAL, type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET).getTargetEntityUniqueId())
                        .setRelatedEntityId(dataset.experimentId != null ? dataset.experimentId.toString() : null);
                    var statusRelation = gobii_file_item_entity_relation_1.GobiiFileItemEntityRelation
                        .fromGobiiFileItemCompoundId(this._filterParamsColl.getFilter(file_item_param_names_1.FilterParamNames.CV_JOB_STATUS, type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET).getTargetEntityUniqueId())
                        .setRelatedEntityId(dataset.jobStatusId != null ? dataset.jobStatusId.toString() : null);
                    // note that we are the parent id to the job status ID. This is because
                    // the primary filtering use case is by job status.
                    returnVal = gobii_file_item_1.GobiiFileItem.build(type_extractor_filter_1.GobiiExtractFilterType.UNKNOWN, type_process_1.ProcessType.READ).setExtractorItemType(type_extractor_item_1.ExtractorItemType.ENTITY)
                        .setEntityType(type_entity_1.EntityType.DATASET)
                        .setItemName(dataset.datasetName)
                        .setItemId(dataset.id.toString())
                        .setEntity(dataset)
                        .withRelatedEntity(piContactRelation)
                        .withRelatedEntity(projectRelation)
                        .withRelatedEntity(experimentRelation)
                        .withRelatedEntity(statusRelation);
                    return returnVal;
                };
                return JsonToGfiDataset;
            }());
            exports_1("JsonToGfiDataset", JsonToGfiDataset);
        }
    };
});
//# sourceMappingURL=json-to-gfi-dataset.js.map