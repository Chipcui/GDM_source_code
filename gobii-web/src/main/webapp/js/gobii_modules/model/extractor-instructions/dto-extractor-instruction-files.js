System.register(["./gobii-extractor-instruction"], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var gobii_extractor_instruction_1, ExtractorInstructionFilesDTO;
    return {
        setters: [
            function (gobii_extractor_instruction_1_1) {
                gobii_extractor_instruction_1 = gobii_extractor_instruction_1_1;
            }
        ],
        execute: function () {
            ExtractorInstructionFilesDTO = (function () {
                function ExtractorInstructionFilesDTO(gobiiExtractorInstructions, jobId) {
                    this.gobiiExtractorInstructions = gobiiExtractorInstructions;
                    this.jobId = jobId;
                    this.gobiiExtractorInstructions = gobiiExtractorInstructions;
                    this.jobId = jobId;
                } // ctor
                ExtractorInstructionFilesDTO.prototype.getGobiiExtractorInstructions = function () {
                    return this.gobiiExtractorInstructions;
                };
                ExtractorInstructionFilesDTO.prototype.setGobiiExtractorInstructions = function (value) {
                    this.gobiiExtractorInstructions = value;
                };
                ExtractorInstructionFilesDTO.prototype.getjobId = function () {
                    return this.jobId;
                };
                ExtractorInstructionFilesDTO.prototype.setjobId = function (value) {
                    this.jobId = value;
                };
                ExtractorInstructionFilesDTO.prototype.getJson = function () {
                    var returnVal = {};
                    returnVal.jobId = this.jobId;
                    returnVal.gobiiExtractorInstructions = [];
                    this.gobiiExtractorInstructions.forEach(function (i) {
                        returnVal.gobiiExtractorInstructions.push(i.getJson());
                    });
                    return returnVal;
                }; // getJson()
                ExtractorInstructionFilesDTO.fromJson = function (json) {
                    var gobiiExtractorInstructions = [];
                    json.gobiiExtractorInstructions.forEach(function (i) {
                        return gobiiExtractorInstructions.push(gobii_extractor_instruction_1.GobiiExtractorInstruction.fromJson(i));
                    });
                    var returnVal = new ExtractorInstructionFilesDTO(gobiiExtractorInstructions, json.jobId);
                    return returnVal;
                };
                return ExtractorInstructionFilesDTO;
            }());
            exports_1("ExtractorInstructionFilesDTO", ExtractorInstructionFilesDTO);
        }
    };
});
//# sourceMappingURL=dto-extractor-instruction-files.js.map