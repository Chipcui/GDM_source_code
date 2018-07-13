System.register(["../../../model/gobii-file-item", "../../../model/type-entity", "../../../model/type-extractor-item", "../../../model/type-process", "../../../model/type-extractor-filter", "../../../model/vertex", "../../../model/type-vertex", "../../../model/cv-group", "../../../model/type-vertex-name"], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var gobii_file_item_1, type_entity_1, type_extractor_item_1, type_process_1, type_extractor_filter_1, vertex_1, type_vertex_1, cv_group_1, type_vertex_name_1, JsonToGfiVertex;
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
            function (vertex_1_1) {
                vertex_1 = vertex_1_1;
            },
            function (type_vertex_1_1) {
                type_vertex_1 = type_vertex_1_1;
            },
            function (cv_group_1_1) {
                cv_group_1 = cv_group_1_1;
            },
            function (type_vertex_name_1_1) {
                type_vertex_name_1 = type_vertex_name_1_1;
            }
        ],
        execute: function () {
            JsonToGfiVertex = (function () {
                function JsonToGfiVertex(_filterParams, _filterParamsColl) {
                    this._filterParams = _filterParams;
                    this._filterParamsColl = _filterParamsColl;
                    this._filterParams = _filterParams;
                    this._filterParamsColl = _filterParamsColl;
                } //ctor
                JsonToGfiVertex.prototype.convert = function (jsonItem) {
                    var vertex = new vertex_1.Vertex(jsonItem.vertexId, type_vertex_name_1.VertexNameType[jsonItem.vertexNameType], type_vertex_1.VertexType[jsonItem.gobiiVertexType], jsonItem.vertexName, type_entity_1.EntityType[jsonItem.entityType], type_entity_1.EntitySubType[jsonItem.entitySubType], cv_group_1.CvGroup[jsonItem.cvGroup], jsonItem.cvTerm, []);
                    var returnVal = gobii_file_item_1.GobiiFileItem.build(type_extractor_filter_1.GobiiExtractFilterType.UNKNOWN, type_process_1.ProcessType.READ)
                        .setExtractorItemType(type_extractor_item_1.ExtractorItemType.VERTEX)
                        .setEntityType(vertex.entityType)
                        .setEntitySubType(vertex.entitySubType)
                        .setCvGroup(vertex.cvGroup)
                        .setCvTerm(jsonItem.cvTerm)
                        .setItemName(vertex.vertexName)
                        .setItemId(vertex.vertexId.toString())
                        .setEntity(vertex);
                    return returnVal;
                };
                return JsonToGfiVertex;
            }());
            exports_1("JsonToGfiVertex", JsonToGfiVertex);
        }
    };
});
//# sourceMappingURL=json-to-gfi-vertex.js.map