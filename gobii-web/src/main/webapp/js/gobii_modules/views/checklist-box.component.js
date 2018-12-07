System.register(["@angular/core", "../model/type-extractor-filter", "../store/actions/fileitem-action", "@ngrx/store", "../services/core/nameid-file-item-service", "../services/core/filter-service", "../services/core/view-id-generator-service", "../services/core/type-control"], function (exports_1, context_1) {
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
    var core_1, type_extractor_filter_1, fileAction, store_1, nameid_file_item_service_1, filter_service_1, view_id_generator_service_1, type_control_1, CheckListBoxComponent;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
            },
            function (fileAction_1) {
                fileAction = fileAction_1;
            },
            function (store_1_1) {
                store_1 = store_1_1;
            },
            function (nameid_file_item_service_1_1) {
                nameid_file_item_service_1 = nameid_file_item_service_1_1;
            },
            function (filter_service_1_1) {
                filter_service_1 = filter_service_1_1;
            },
            function (view_id_generator_service_1_1) {
                view_id_generator_service_1 = view_id_generator_service_1_1;
            },
            function (type_control_1_1) {
                type_control_1 = type_control_1_1;
            }
        ],
        execute: function () {
            CheckListBoxComponent = (function () {
                function CheckListBoxComponent(store, fileItemService, filterService, differs, viewIdGeneratorService) {
                    this.store = store;
                    this.fileItemService = fileItemService;
                    this.filterService = filterService;
                    this.differs = differs;
                    this.viewIdGeneratorService = viewIdGeneratorService;
                    this.typeControl = type_control_1.TypeControl;
                    this.gobiiExtractFilterType = type_extractor_filter_1.GobiiExtractFilterType.UNKNOWN;
                    this.onError = new core_1.EventEmitter();
                    this.differ = differs.find({}).create(null);
                } // ctor
                CheckListBoxComponent.prototype.ngOnInit = function () {
                    this.gobiiFileItems$ = this.filterService.getForFilter(this.filterParamName);
                };
                CheckListBoxComponent.prototype.handleItemChecked = function (arg) {
                    var currentFileItemUniqueId = arg.currentTarget.value;
                    if (arg.currentTarget.checked) {
                        this.store.dispatch(new fileAction.AddToExtractByItemIdAction(currentFileItemUniqueId));
                    }
                    else {
                        this.store.dispatch(new fileAction.RemoveFromExractByItemIdAction(currentFileItemUniqueId));
                    }
                }; // handleItemChecked()
                CheckListBoxComponent.prototype.handleItemSelected = function (arg) {
                    //        arg.currentTarget.style = "background-color:#b3d9ff";
                };
                CheckListBoxComponent.prototype.handleHeaderStatus = function (headerStatusMessage) {
                    this.onError.emit(headerStatusMessage);
                };
                CheckListBoxComponent = __decorate([
                    core_1.Component({
                        selector: 'checklist-box',
                        inputs: ['gobiiExtractFilterType',
                            'filterParamName'],
                        outputs: ['onError'],
                        template: "\n        <form\n                [id]=\"viewIdGeneratorService.makeCheckboxListBoxId(filterParamName)\">\n            <div style=\"overflow:auto; height: 80px; border: 1px solid #336699; padding-left: 5px\">\n                <div *ngFor=\"let gobiiFileItem of gobiiFileItems$ | async\"\n                     (click)=handleItemSelected($event)>\n                    <input type=\"checkbox\"\n                           (click)=handleItemChecked($event)\n                           [checked]=\"gobiiFileItem.getSelected()\"\n                           value={{gobiiFileItem.getFileItemUniqueId()}}\n                    name=\"{{gobiiFileItem.getItemName()}}\">&nbsp;{{gobiiFileItem.getItemName()}}\n                </div>\n            </div>\n        </form>" // end template
                    }),
                    __metadata("design:paramtypes", [store_1.Store,
                        nameid_file_item_service_1.NameIdFileItemService,
                        filter_service_1.FilterService,
                        core_1.KeyValueDiffers,
                        view_id_generator_service_1.ViewIdGeneratorService])
                ], CheckListBoxComponent);
                return CheckListBoxComponent;
            }());
            exports_1("CheckListBoxComponent", CheckListBoxComponent);
        }
    };
});
//# sourceMappingURL=checklist-box.component.js.map