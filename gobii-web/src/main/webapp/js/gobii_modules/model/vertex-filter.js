System.register([], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var VertexFilterDTO;
    return {
        setters: [],
        execute: function () {
            VertexFilterDTO = (function () {
                function VertexFilterDTO(destinationVertex, filterVertices, vertexValues, markerCount, markerFileFqpn, markerCountMs, sampleCount, sampleFileFqpn, sampleCountMs) {
                    this.destinationVertex = destinationVertex;
                    this.filterVertices = filterVertices;
                    this.vertexValues = vertexValues;
                    this.markerCount = markerCount;
                    this.markerFileFqpn = markerFileFqpn;
                    this.markerCountMs = markerCountMs;
                    this.sampleCount = sampleCount;
                    this.sampleFileFqpn = sampleFileFqpn;
                    this.sampleCountMs = sampleCountMs;
                }
                VertexFilterDTO.fromJson = function (json) {
                    var returnVal = new VertexFilterDTO(json.destinationVertexDTO, json.filterVertices, json.vertexValues, json.markerCount, json.markerFileFqpn, json.markerCountMs, json.sampleCount, json.sampleFileFqpn, json.sampleCountMs);
                    return returnVal;
                };
                VertexFilterDTO.prototype.getJson = function () {
                    var returnVal = {};
                    returnVal.destinationVertexDTO = this.destinationVertex;
                    returnVal.filterVertices = this.filterVertices;
                    returnVal.vertexValues = this.vertexValues;
                    returnVal.markerCount = this.markerCount;
                    returnVal.markerFileFqpn = this.markerFileFqpn;
                    returnVal.markerCountMs = this.markerCountMs;
                    returnVal.sampleCount = this.sampleCount;
                    returnVal.sampleFileFqpn = this.sampleFileFqpn;
                    returnVal.sampleCountMs = this.sampleCountMs;
                    return returnVal;
                };
                return VertexFilterDTO;
            }());
            exports_1("VertexFilterDTO", VertexFilterDTO);
        }
    };
});
//# sourceMappingURL=vertex-filter.js.map