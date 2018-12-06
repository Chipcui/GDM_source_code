System.register(["@angular/core", "../model/type-extractor-filter", "../model/type-extractor-sample-list", "../model/gobii-file-item", "../model/type-process", "../model//type-extractor-item", "../store/reducers", "@ngrx/store", "../services/core/nameid-file-item-service", "../services/core/view-id-generator-service"], function (exports_1, context_1) {
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
    var core_1, type_extractor_filter_1, type_extractor_sample_list_1, gobii_file_item_1, type_process_1, type_extractor_item_1, fromRoot, store_1, nameid_file_item_service_1, view_id_generator_service_1, SampleListTypeComponent;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
            },
            function (type_extractor_sample_list_1_1) {
                type_extractor_sample_list_1 = type_extractor_sample_list_1_1;
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
            function (fromRoot_1) {
                fromRoot = fromRoot_1;
            },
            function (store_1_1) {
                store_1 = store_1_1;
            },
            function (nameid_file_item_service_1_1) {
                nameid_file_item_service_1 = nameid_file_item_service_1_1;
            },
            function (view_id_generator_service_1_1) {
                view_id_generator_service_1 = view_id_generator_service_1_1;
            }
        ],
        execute: function () {
            SampleListTypeComponent = (function () {
                function SampleListTypeComponent(store, fileItemService, viewIdGeneratorService) {
                    this.store = store;
                    this.fileItemService = fileItemService;
                    this.viewIdGeneratorService = viewIdGeneratorService;
                    this.gobiiSampleListType = type_extractor_sample_list_1.GobiiSampleListType;
                    this.onHeaderStatusMessage = new core_1.EventEmitter();
                    this.gobiiExtractFilterType = type_extractor_filter_1.GobiiExtractFilterType.UNKNOWN;
                    this.sampleListType$ = this.store.select(fromRoot.getSelectedSampleType);
                } // ctor
                SampleListTypeComponent.prototype.handleSampleTypeSelected = function (radioValue) {
                    var gobiiSampleListType = type_extractor_sample_list_1.GobiiSampleListType[radioValue];
                    this.fileItemService
                        .replaceFileItemByCompoundId(gobii_file_item_1.GobiiFileItem.build(this.gobiiExtractFilterType, type_process_1.ProcessType.CREATE)
                        .setExtractorItemType(type_extractor_item_1.ExtractorItemType.SAMPLE_LIST_TYPE)
                        .setItemName(type_extractor_sample_list_1.GobiiSampleListType[gobiiSampleListType])
                        .setItemId(type_extractor_sample_list_1.GobiiSampleListType[gobiiSampleListType]));
                };
                SampleListTypeComponent.prototype.ngOnInit = function () {
                };
                SampleListTypeComponent.prototype.ngOnChanges = function (changes) {
                };
                SampleListTypeComponent = __decorate([
                    core_1.Component({
                        selector: 'sample-list-type',
                        inputs: ['gobiiExtractFilterType'],
                        outputs: ['onHeaderStatusMessage'],
                        encapsulation: core_1.ViewEncapsulation.Native,
                        styleUrls: ["js/node_modules/primeng/resources/themes/omega/theme.css",
                            "js/node_modules/primeng/resources/primeng.css",
                            "js/node_modules/bootswatch/cerulean/bootstrap.min.css"],
                        template: "\n        <form>\n            <label class=\"the-legend\">List Item Type:&nbsp;</label>\n            <BR>\n            <div class=\"ui-g\" style=\"width:250px;margin-bottom:5px\">\n                <div class=\"ui-g-12\" style=\"height:30px\">\n                    <p-radioButton\n                            (ngModelChange)=\"handleSampleTypeSelected($event)\"\n                            [ngModel]=\"(sampleListType$ | async).getItemId()\"\n                            name=\"listType\"\n                            value=\"GERMPLASM_NAME\"\n                            label=\"Germplasm Name\"\n                            [id]=\"viewIdGeneratorService.makeSampleListTypeId(gobiiSampleListType.GERMPLASM_NAME)\">\n                    </p-radioButton>\n                </div>\n                <div class=\"ui-g-12\" style=\"height:30px\">\n                    <p-radioButton\n                            (ngModelChange)=\"handleSampleTypeSelected($event)\"\n                            [ngModel]=\"(sampleListType$ | async).getItemId()\"\n                            name=\"listType\"\n                            value=\"EXTERNAL_CODE\"\n                            label=\"External Code\"\n                            [id]=\"viewIdGeneratorService.makeSampleListTypeId(gobiiSampleListType.EXTERNAL_CODE)\">                        \n                    </p-radioButton>\n                </div>\n                <div class=\"ui-g-12\" style=\"height:30px\">\n                    <p-radioButton\n                            (ngModelChange)=\"handleSampleTypeSelected($event)\"\n                            [ngModel]=\"(sampleListType$ | async).getItemId()\"\n                            name=\"listType\"\n                            value=\"DNA_SAMPLE\"\n                            label=\"DNA Sample\"\n                            [id]=\"viewIdGeneratorService.makeSampleListTypeId(gobiiSampleListType.DNA_SAMPLE)\">                        \n                    </p-radioButton>\n                </div>\n            </div>\n        </form>" // end template
                    }),
                    __metadata("design:paramtypes", [store_1.Store,
                        nameid_file_item_service_1.NameIdFileItemService,
                        view_id_generator_service_1.ViewIdGeneratorService])
                ], SampleListTypeComponent);
                return SampleListTypeComponent;
            }());
            exports_1("SampleListTypeComponent", SampleListTypeComponent);
        }
    };
});
//# sourceMappingURL=sample-list-type.component.js.map