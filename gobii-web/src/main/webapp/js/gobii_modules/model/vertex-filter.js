System.register([], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var VertexFilterDTO;
    return {
        setters: [],
        execute: function () {
            VertexFilterDTO = (function () {
                function VertexFilterDTO(destinationVertex, vertices, vertexValues, markerCount, sampleCount) {
                    this.destinationVertex = destinationVertex;
                    this.vertices = vertices;
                    this.vertexValues = vertexValues;
                    this.markerCount = markerCount;
                    this.sampleCount = sampleCount;
                }
                VertexFilterDTO.fromJson = function (json) {
                    var returnVal = new VertexFilterDTO(json.destinationVertexDTO, json.vertices, json.vertexValues, json.markerCount, json.sampleCount);
                    return returnVal;
                };
                VertexFilterDTO.prototype.getJson = function () {
                    var returnVal = {};
                    returnVal.destinationVertexDTO = this.destinationVertex;
                    returnVal.vertices = this.vertices;
                    returnVal.vertexValues = this.vertexValues;
                    returnVal.markerCount = this.markerCount;
                    returnVal.sampleCount = this.sampleCount;
                    return returnVal;
                };
                return VertexFilterDTO;
            }());
            exports_1("VertexFilterDTO", VertexFilterDTO);
        }
    };
});
//# sourceMappingURL=vertex-filter.js.map