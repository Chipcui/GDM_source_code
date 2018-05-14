System.register(["@angular/core", "../model/type-extract-format", "../model/gobii-file-item", "../model/type-process", "../model//type-extractor-item", "../model/type-extractor-filter", "@ngrx/store", "../services/core/nameid-file-item-service", "rxjs/Observable"], function (exports_1, context_1) {
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
    var core_1, type_extract_format_1, gobii_file_item_1, type_process_1, type_extractor_item_1, type_extractor_filter_1, store_1, nameid_file_item_service_1, Observable_1, ExportFormatComponent;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (type_extract_format_1_1) {
                type_extract_format_1 = type_extract_format_1_1;
            },
            function (gobii_file_item_1_1) {
                gobii_file_item_1 = gobii_file_item_1_1;
            },
            function (type_process_1_1) {
                type_process_1 = type_process_1_1;
            },
            function (type_extractor_item_1_1) {
                type_extractor_item_1 = type_extractor_item_1_1;
            },
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
            },
            function (store_1_1) {
                store_1 = store_1_1;
            },
            function (nameid_file_item_service_1_1) {
                nameid_file_item_service_1 = nameid_file_item_service_1_1;
            },
            function (Observable_1_1) {
                Observable_1 = Observable_1_1;
            }
        ],
        execute: function () {
            ExportFormatComponent = (function () {
                function ExportFormatComponent(store, fileItemService) {
                    this.store = store;
                    this.fileItemService = fileItemService;
                    this.onFormatSelected = new core_1.EventEmitter();
                    this.onError = new core_1.EventEmitter();
                } // ctor
                // private nameIdList: NameId[];
                // private selectedNameId: string = null;
                ExportFormatComponent.prototype.ngOnInit = function () {
                };
                ExportFormatComponent.prototype.handleFormatSelected = function (arg) {
                    this.updateItemService(arg);
                };
                ExportFormatComponent.prototype.updateItemService = function (arg) {
                    var formatItem = gobii_file_item_1.GobiiFileItem
                        .build(this.gobiiExtractFilterType, type_process_1.ProcessType.UPDATE)
                        .setExtractorItemType(type_extractor_item_1.ExtractorItemType.EXPORT_FORMAT)
                        .setItemId(arg)
                        .setItemName(type_extract_format_1.GobiiExtractFormat[type_extract_format_1.GobiiExtractFormat[arg]]);
                    this.fileItemService.replaceFileItemByCompoundId(formatItem);
                };
                ExportFormatComponent.prototype.ngOnChanges = function (changes) {
                    if (changes['gobiiExtractFilterType']
                        && (changes['gobiiExtractFilterType'].currentValue != null)
                        && (changes['gobiiExtractFilterType'].currentValue != undefined)) {
                        if (changes['gobiiExtractFilterType'].currentValue != changes['gobiiExtractFilterType'].previousValue) {
                            var labelSuffix = " Metadata";
                            if (this.gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET) {
                                this.metaDataExtractname = "Dataset" + labelSuffix;
                            }
                            else if (this.gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER) {
                                this.metaDataExtractname = "Marker" + labelSuffix;
                            }
                            else if (this.gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE) {
                                this.metaDataExtractname = "Sample" + labelSuffix;
                            }
                        } // if we have a new filter type
                    } // if filter type changed
                }; // ngonChanges
                __decorate([
                    core_1.Input(),
                    __metadata("design:type", Observable_1.Observable)
                ], ExportFormatComponent.prototype, "fileFormat$", void 0);
                ExportFormatComponent = __decorate([
                    core_1.Component({
                        selector: 'export-format',
                        outputs: ['onFormatSelected', 'onError'],
                        inputs: ['gobiiExtractFilterType'],
                        encapsulation: core_1.ViewEncapsulation.Native,
                        styleUrls: ["js/node_modules/primeng/resources/themes/omega/theme.css",
                            "js/node_modules/primeng/resources/primeng.css",
                            "js/node_modules/bootswatch/dist/cerulean/bootstrap.css"],
                        template: "\n        <form>\n            <label class=\"the-legend\">Select Format:&nbsp;</label>\n            <BR>\n            <div class=\"ui-g\" style=\"width:250px;margin-bottom:5px\">\n                <div class=\"ui-g-12\" style=\"height:30px\">\n                    <p-radioButton\n                            (ngModelChange)=\"handleFormatSelected($event)\"\n                            [ngModel]=\"(fileFormat$  | async).getItemId()\"\n                            name=\"fileFormat\"\n                            value=\"HAPMAP\"\n                            label=\"Hapmap\">\n                    </p-radioButton>\n                </div>\n                <div class=\"ui-g-12\" style=\"height:30px\">\n                    <p-radioButton\n                            (ngModelChange)=\"handleFormatSelected($event)\"\n                            [ngModel]=\"(fileFormat$  | async).getItemId()\"\n                            name=\"fileFormat\"\n                            label=\"Flapjack\"\n                            value=\"FLAPJACK\">\n                    </p-radioButton>\n                </div>\n                <div class=\"ui-g-12\" style=\"height:30px\">\n                    <p-radioButton\n                            (ngModelChange)=\"handleFormatSelected($event)\"\n                            [ngModel]=\"(fileFormat$  | async).getItemId()\"\n                            name=\"fileFormat\"\n                            label=\"Sample Metadata\"\n                            value=\"META_DATA_ONLY\">\n                    </p-radioButton>\n                </div>\n            </div>\n        </form>" // end template
                    })
                    /**
                     * In the template you will notice some slight of hand to get the property value of the
                     * GobiiFileItem for ngModel. In my original implementation, the selector from the reducer
                     * returned a scalar string value. From that selector I would get the initial state value
                     * but not subsequent values, even though the select itself was executing under the debugger.
                     * I spent many hours trying to track this issue down and even created the reproduce-radio
                     * project to try to isolate the issue. But it seemed to be working. Then I found ngrx/platform
                     * issue # 208: https://github.com/ngrx/platform/issues/208
                     * This is precisely the issue that was reported. My versions match those of the test project so I don't
                     * know why this is happening. I will try to reproduce the issue in the test project as a good citizen.
                     * In the meantime, we need to stick to returning actual state objects rather than scalar primtive values
                     * from it.
                     */
                    ,
                    __metadata("design:paramtypes", [store_1.Store,
                        nameid_file_item_service_1.NameIdFileItemService])
                ], ExportFormatComponent);
                return ExportFormatComponent;
            }());
            exports_1("ExportFormatComponent", ExportFormatComponent);
        }
    };
});
//# sourceMappingURL=export-format.component.js.map