System.register(["@angular/core", "../../model/type-process", "../../model/payload/payload-envelope", "../../model/vertex-filter"], function (exports_1, context_1) {
    "use strict";
    var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
        var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
        if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
        else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
        return c > 3 && r && Object.defineProperty(target, key, r), r;
    };
    var __metadata = (this && this.__metadata) || function (k, v) {
        if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
    };
    var __moduleName = context_1 && context_1.id;
    var core_1, type_process_1, payload_envelope_1, vertex_filter_1, DtoRequestItemVertexFilterDTO;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (type_process_1_1) {
                type_process_1 = type_process_1_1;
            },
            function (payload_envelope_1_1) {
                payload_envelope_1 = payload_envelope_1_1;
            },
            function (vertex_filter_1_1) {
                vertex_filter_1 = vertex_filter_1_1;
            }
        ],
        execute: function () {
            DtoRequestItemVertexFilterDTO = (function () {
                function DtoRequestItemVertexFilterDTO(vertexFilterDTO, jobId, isCountRequest) {
                    this.vertexFilterDTO = vertexFilterDTO;
                    this.processType = type_process_1.ProcessType.CREATE;
                    this.vertexFilterDTO = vertexFilterDTO;
                    this.jobId = jobId;
                    this.isCountRequest = isCountRequest;
                }
                DtoRequestItemVertexFilterDTO.prototype.getUrl = function () {
                    var returnVal;
                    if (this.isCountRequest) {
                        returnVal = "gobii/v1/vertices/" + this.jobId + "/count";
                    }
                    else {
                        returnVal = "gobii/v1/vertices/" + this.jobId + "/values";
                    }
                    return returnVal;
                }; // getUrl()
                DtoRequestItemVertexFilterDTO.prototype.getRequestBody = function () {
                    var rawJsonExtractorInstructionFileDTO = this.vertexFilterDTO.getJson();
                    var payloadEnvelope = payload_envelope_1.PayloadEnvelope.wrapSingleDTOInJSON(rawJsonExtractorInstructionFileDTO);
                    var returnVal = JSON.stringify(payloadEnvelope);
                    return returnVal;
                };
                DtoRequestItemVertexFilterDTO.prototype.resultFromJson = function (json) {
                    var returnVal = vertex_filter_1.VertexFilterDTO.fromJson(json.payload.data[0]);
                    return returnVal;
                };
                DtoRequestItemVertexFilterDTO = __decorate([
                    core_1.Injectable(),
                    __metadata("design:paramtypes", [vertex_filter_1.VertexFilterDTO, String, Boolean])
                ], DtoRequestItemVertexFilterDTO);
                return DtoRequestItemVertexFilterDTO;
            }());
            exports_1("DtoRequestItemVertexFilterDTO", DtoRequestItemVertexFilterDTO);
        }
    };
});
//# sourceMappingURL=dto-request-item-vertex-filter.js.map