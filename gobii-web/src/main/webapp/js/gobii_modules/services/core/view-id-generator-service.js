System.register(["./type-control", "@angular/core", "../../model/type-extract-format", "../../model/type-extractor-sample-list"], function (exports_1, context_1) {
    "use strict";
    var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
        var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
        if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
        else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
        return c > 3 && r && Object.defineProperty(target, key, r), r;
    };
    var __moduleName = context_1 && context_1.id;
    var type_control_1, core_1, type_extract_format_1, type_extractor_sample_list_1, ViewIdGeneratorService;
    return {
        setters: [
            function (type_control_1_1) {
                type_control_1 = type_control_1_1;
            },
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (type_extract_format_1_1) {
                type_extract_format_1 = type_extract_format_1_1;
            },
            function (type_extractor_sample_list_1_1) {
                type_extractor_sample_list_1 = type_extractor_sample_list_1_1;
            }
        ],
        execute: function () {
            ViewIdGeneratorService = (function () {
                function ViewIdGeneratorService() {
                }
                ViewIdGeneratorService.prototype.makeIdNameIdListBoxId = function (filterParamName) {
                    return type_control_1.TypeControl[type_control_1.TypeControl.NAME_ID_LIST] + "_" + filterParamName;
                };
                ViewIdGeneratorService.prototype.makeCheckboxListBoxId = function (filterParamName) {
                    return type_control_1.TypeControl[type_control_1.TypeControl.CHECKBOX_LIST] + "_" + filterParamName;
                };
                ViewIdGeneratorService.prototype.makeDatasetRowCheckboxId = function (datasetName) {
                    return type_control_1.TypeControl[type_control_1.TypeControl.DATASET_ROW_CHECKBOX] + "_" + datasetName;
                };
                ViewIdGeneratorService.prototype.makeExportFormatRadioButtonId = function (gobiiExtractFormat) {
                    return type_control_1.TypeControl[type_control_1.TypeControl.EXPORT_FORMAT_RADIO_BUTTON] + "_" + type_extract_format_1.GobiiExtractFormat[gobiiExtractFormat];
                }; //makeExportFormatRadioButtonId()
                ViewIdGeneratorService.prototype.makeStandardId = function (typeControl) {
                    return type_control_1.TypeControl[typeControl];
                };
                ViewIdGeneratorService.prototype.makeSampleListTypeId = function (gobiiSampleListType) {
                    return type_control_1.TypeControl[type_control_1.TypeControl.SAMPLE_LIST_TYPE_RADIO_BUTTON] + "_" + type_extractor_sample_list_1.GobiiSampleListType[gobiiSampleListType];
                };
                ViewIdGeneratorService.prototype.makeMarkerSampleListModeRadioButtonId = function (typeLabel) {
                    return type_control_1.TypeControl[type_control_1.TypeControl.MARKER_SAMPLE_LIST_MODE_RADIO_BUTTON] + "_" + typeLabel.toUpperCase().replace(" ", "_");
                };
                ViewIdGeneratorService = __decorate([
                    core_1.Injectable()
                ], ViewIdGeneratorService);
                return ViewIdGeneratorService;
            }());
            exports_1("ViewIdGeneratorService", ViewIdGeneratorService);
        }
    };
});
//# sourceMappingURL=view-id-generator-service.js.map