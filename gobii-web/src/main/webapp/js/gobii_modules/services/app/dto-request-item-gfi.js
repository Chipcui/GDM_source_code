System.register(["@angular/core", "../../model/type-process", "../../model/filter-params", "../../model/filter-type", "../../model/file-item-param-names"], function (exports_1, context_1) {
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
    var core_1, type_process_1, filter_params_1, filter_type_1, file_item_param_names_1, DtoRequestItemGfi;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (type_process_1_1) {
                type_process_1 = type_process_1_1;
            },
            function (filter_params_1_1) {
                filter_params_1 = filter_params_1_1;
            },
            function (filter_type_1_1) {
                filter_type_1 = filter_type_1_1;
            },
            function (file_item_param_names_1_1) {
                file_item_param_names_1 = file_item_param_names_1_1;
            }
        ],
        execute: function () {
            DtoRequestItemGfi = (function () {
                function DtoRequestItemGfi(fileItemParams, id, jsonToGfi) {
                    if (id === void 0) { id = null; }
                    this.fileItemParams = fileItemParams;
                    this.id = id;
                    this.jsonToGfi = jsonToGfi;
                    this.processType = type_process_1.ProcessType.READ;
                    this.fileItemParams = fileItemParams;
                    this.id = id;
                    this.jsonToGfi = jsonToGfi;
                    if (this.fileItemParams.getFilterType() !== filter_type_1.FilterType.ENTITY_LIST
                        && this.fileItemParams.getFilterType() !== filter_type_1.FilterType.ENTITY_BY_ID) {
                        throw new Error("The FileItemParams with ID "
                            + this.fileItemParams.getQueryName()
                            + " is not of type " + filter_type_1.FilterType[filter_type_1.FilterType.ENTITY_LIST]
                            + " or " + filter_type_1.FilterType[filter_type_1.FilterType.ENTITY_BY_ID]);
                    }
                }
                DtoRequestItemGfi.prototype.getUrl = function () {
                    var returnVal = "gobii/v1";
                    if (this.fileItemParams.getQueryName() === file_item_param_names_1.FilterParamNames.DATASET_BY_DATASET_ID ||
                        this.fileItemParams.getQueryName() === file_item_param_names_1.FilterParamNames.DATASET_LIST) {
                        returnVal += "/datasets";
                        if (this.fileItemParams.getFilterType() === filter_type_1.FilterType.ENTITY_BY_ID) {
                            returnVal += "/" + this.id;
                        }
                    }
                    else if (this.fileItemParams.getQueryName() === file_item_param_names_1.FilterParamNames.ANALYSES_BY_DATASET_ID) {
                        returnVal += "/datasets/" + this.id + "/analyses";
                    }
                    else if (this.fileItemParams.getQueryName() === file_item_param_names_1.FilterParamNames.FQ_F1_VERTICES) {
                        returnVal += "/vertices";
                    }
                    return returnVal;
                }; // getUrl()
                // this is probably not being used anymore
                DtoRequestItemGfi.prototype.getRequestBody = function () {
                    return JSON.stringify({
                        "processType": type_process_1.ProcessType[this.processType],
                        "id": this.id
                    });
                };
                DtoRequestItemGfi.prototype.resultFromJson = function (json) {
                    var _this = this;
                    var returnVal = [];
                    json.payload.data.forEach(function (jsonItem) {
                        returnVal.push(_this.jsonToGfi.convert(jsonItem));
                    });
                    return returnVal;
                };
                DtoRequestItemGfi = __decorate([
                    core_1.Injectable(),
                    __metadata("design:paramtypes", [filter_params_1.FilterParams, String, Object])
                ], DtoRequestItemGfi);
                return DtoRequestItemGfi;
            }());
            exports_1("DtoRequestItemGfi", DtoRequestItemGfi);
        }
    };
});
//# sourceMappingURL=dto-request-item-gfi.js.map