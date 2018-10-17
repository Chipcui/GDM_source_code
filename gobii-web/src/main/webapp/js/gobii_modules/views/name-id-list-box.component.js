System.register(["@angular/core", "@ngrx/store", "../store/actions/fileitem-action", "../store/actions/history-action", "../services/core/file-item-service"], function (exports_1, context_1) {
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
    var core_1, store_1, fileAction, historyAction, file_item_service_1, NameIdListBoxComponent;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (store_1_1) {
                store_1 = store_1_1;
            },
            function (fileAction_1) {
                fileAction = fileAction_1;
            },
            function (historyAction_1) {
                historyAction = historyAction_1;
            },
            function (file_item_service_1_1) {
                file_item_service_1 = file_item_service_1_1;
            }
        ],
        execute: function () {
            NameIdListBoxComponent = (function () {
                function NameIdListBoxComponent(store, fileItemService) {
                    this.store = store;
                    this.fileItemService = fileItemService;
                    this.foo = "a foo id";
                    this.previousSelectedItemId = null;
                } // ctor
                NameIdListBoxComponent.prototype.ngOnInit = function () {
                    var _this = this;
                    this.fileItems$ = this.fileItemService.getForFilter(this.filterParamName);
                    this
                        .fileItems$
                        .subscribe(function (items) {
                        if (_this.previousSelectedItemId === null && items && items.length > 0) {
                            _this.previousSelectedItemId = items[0].getFileItemUniqueId();
                        }
                    }, function (error) {
                        _this.store.dispatch(new historyAction.AddStatusMessageAction(error));
                    });
                };
                NameIdListBoxComponent.prototype.handleFileItemSelected = function (arg) {
                    if (!this.gobiiExtractFilterType) {
                        this.store.dispatch(new historyAction.AddStatusMessageAction("The gobiiExtractFilterType property is not set"));
                    }
                    var newFileItemUniqueId = arg.currentTarget.value;
                    var previousFileItemUniqueId = this.previousSelectedItemId;
                    this.store.dispatch(new fileAction.ReplaceByItemIdAction({
                        filterParamName: this.filterParamName,
                        gobiiExtractFilterType: this.gobiiExtractFilterType,
                        itemIdCurrentlyInExtract: previousFileItemUniqueId,
                        itemIdToReplaceItWith: newFileItemUniqueId
                    }));
                    this.previousSelectedItemId = newFileItemUniqueId;
                };
                NameIdListBoxComponent = __decorate([
                    core_1.Component({
                        selector: 'name-id-list-box',
                        inputs: ['gobiiExtractFilterType', 'filterParamName'],
                        outputs: [],
                        template: "\n        <div id=\"{{filterParamName}}\">\n            <select class=\"nameIdListBox\" \n                    (change)=\"handleFileItemSelected($event)\"\n                    id=\"{{filterParamName}}\">\n                <option *ngFor=\"let fileItem of fileItems$ | async\"\n                        [value]=\"fileItem.getFileItemUniqueId()\"\n                        [selected]=\"fileItem.getSelected()\"\n                        title=\"{{fileItem.getItemName()}}\">\n                    {{fileItem.getItemName().length < 34 ? fileItem.getItemName() : fileItem.getItemName().substr(0, 30).concat(\" . . .\")}}\n\n                </option>\n            </select>\n        </div>\n    " // end template
                    }),
                    __metadata("design:paramtypes", [store_1.Store,
                        file_item_service_1.FileItemService])
                ], NameIdListBoxComponent);
                return NameIdListBoxComponent;
            }());
            exports_1("NameIdListBoxComponent", NameIdListBoxComponent);
        }
    };
});
//# sourceMappingURL=name-id-list-box.component.js.map