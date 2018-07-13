System.register(["./type-extractor-item", "./gobii-file-item-compound-id"], function (exports_1, context_1) {
    "use strict";
    var __extends = (this && this.__extends) || (function () {
        var extendStatics = Object.setPrototypeOf ||
            ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
            function (d, b) { for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p]; };
        return function (d, b) {
            extendStatics(d, b);
            function __() { this.constructor = d; }
            d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
        };
    })();
    var __moduleName = context_1 && context_1.id;
    var type_extractor_item_1, gobii_file_item_compound_id_1, Vertex;
    return {
        setters: [
            function (type_extractor_item_1_1) {
                type_extractor_item_1 = type_extractor_item_1_1;
            },
            function (gobii_file_item_compound_id_1_1) {
                gobii_file_item_compound_id_1 = gobii_file_item_compound_id_1_1;
            }
        ],
        execute: function () {
            Vertex = (function (_super) {
                __extends(Vertex, _super);
                function Vertex(vertexId, vertexNameType, gobiiVertexType, vertexName, entityType, entitySubType, cvGroup, cvTerm, filterVals) {
                    var _this = _super.call(this, type_extractor_item_1.ExtractorItemType.VERTEX, entityType, entitySubType, cvGroup, cvTerm) || this;
                    _this.vertexId = vertexId;
                    _this.vertexNameType = vertexNameType;
                    _this.gobiiVertexType = gobiiVertexType;
                    _this.vertexName = vertexName;
                    _this.entityType = entityType;
                    _this.entitySubType = entitySubType;
                    _this.cvGroup = cvGroup;
                    _this.cvTerm = cvTerm;
                    _this.filterVals = filterVals;
                    return _this;
                }
                Vertex.fromVertex = function (vertexToCopy) {
                    return new Vertex(vertexToCopy.vertexId, vertexToCopy.vertexNameType, vertexToCopy.gobiiVertexType, vertexToCopy.vertexName, vertexToCopy.entityType, vertexToCopy.entitySubType, vertexToCopy.cvGroup, vertexToCopy.cvTerm, vertexToCopy.filterVals);
                };
                return Vertex;
            }(gobii_file_item_compound_id_1.GobiiFileItemCompoundId));
            exports_1("Vertex", Vertex);
        }
    };
});
//# sourceMappingURL=vertex.js.map