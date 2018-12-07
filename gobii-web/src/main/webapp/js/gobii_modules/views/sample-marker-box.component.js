System.register(["@angular/core", "../model/dto-header-status-message", "../model/type-extractor-filter", "../model/gobii-file-item", "../model/type-process", "../model//type-extractor-item", "./entity-labels", "../model/file-item-param-names", "../store/reducers", "../services/core/file-item-service", "@ngrx/store", "../model/type-status-level", "../store/actions/history-action", "../model/type-entity", "../services/core/view-id-generator-service"], function (exports_1, context_1) {
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
    var core_1, dto_header_status_message_1, type_extractor_filter_1, gobii_file_item_1, type_process_1, type_extractor_item_1, entity_labels_1, file_item_param_names_1, fromRoot, file_item_service_1, store_1, type_status_level_1, historyAction, type_entity_1, view_id_generator_service_1, SampleMarkerBoxComponent;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (dto_header_status_message_1_1) {
                dto_header_status_message_1 = dto_header_status_message_1_1;
            },
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
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
            function (entity_labels_1_1) {
                entity_labels_1 = entity_labels_1_1;
            },
            function (file_item_param_names_1_1) {
                file_item_param_names_1 = file_item_param_names_1_1;
            },
            function (fromRoot_1) {
                fromRoot = fromRoot_1;
            },
            function (file_item_service_1_1) {
                file_item_service_1 = file_item_service_1_1;
            },
            function (store_1_1) {
                store_1 = store_1_1;
            },
            function (type_status_level_1_1) {
                type_status_level_1 = type_status_level_1_1;
            },
            function (historyAction_1) {
                historyAction = historyAction_1;
            },
            function (type_entity_1_1) {
                type_entity_1 = type_entity_1_1;
            },
            function (view_id_generator_service_1_1) {
                view_id_generator_service_1 = view_id_generator_service_1_1;
            }
        ],
        execute: function () {
            SampleMarkerBoxComponent = (function () {
                function SampleMarkerBoxComponent(store, fileItemService, viewIdGeneratorService) {
                    this.store = store;
                    this.fileItemService = fileItemService;
                    this.viewIdGeneratorService = viewIdGeneratorService;
                    this.nameIdFilterParamTypesMarkerGroup = file_item_param_names_1.FilterParamNames.MARKER_GROUPS;
                    this.extractorItemType = type_extractor_item_1.ExtractorItemType;
                    this.ITEM_FILE_TYPE = "ITEM_FILE_TYPE";
                    this.ITEM_LIST_TYPE = "ITEM_LIST_TYPE";
                    this.MARKER_GROUP_TYPE = "MARKER_GROUP_TYPE";
                    this.maxListItems = 200;
                    this.displayMaxItemsExceeded = false;
                    this.displayChoicePrompt = false;
                    this.selectedListType = this.ITEM_FILE_TYPE;
                    this.displayUploader = true;
                    this.displayListBox = false;
                    this.displayMarkerGroupBox = false;
                    this.displayMarkerGroupRadio = false;
                    this.gobiiExtractFilterType = type_extractor_filter_1.GobiiExtractFilterType.UNKNOWN;
                    this.onSampleMarkerError = new core_1.EventEmitter();
                    this.currentFileItems = [];
                }
                // private handleUserSelected(arg) {
                //     this.onUserSelected.emit(this.nameIdList[arg.srcElement.selectedIndex].id);
                // }
                //
                // constructor(private _dtoRequestService:DtoRequestService<NameId[]>) {
                //
                // } // ctor
                SampleMarkerBoxComponent.prototype.handleTextBoxDataSubmitted = function (items) {
                    var _this = this;
                    if (items.length <= this.maxListItems) {
                        var nonDuplicateItems_1 = [];
                        items.forEach(function (item) {
                            if (!nonDuplicateItems_1.find(function (ii) { return ii === item; })) {
                                nonDuplicateItems_1.push(item);
                            }
                        });
                        var listItemType_1 = this.gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER ?
                            type_extractor_item_1.ExtractorItemType.MARKER_LIST_ITEM : type_extractor_item_1.ExtractorItemType.SAMPLE_LIST_ITEM;
                        nonDuplicateItems_1.forEach(function (listItem) {
                            if (listItem && listItem !== "") {
                                _this.fileItemService
                                    .loadFileItem(gobii_file_item_1.GobiiFileItem.build(_this.gobiiExtractFilterType, type_process_1.ProcessType.CREATE)
                                    .setExtractorItemType(listItemType_1)
                                    .setItemId(listItem)
                                    .setItemName(listItem)
                                    .setIsEphemeral(true), true);
                            }
                        });
                    }
                    else {
                        if (this.gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER) {
                            this.maxExceededTypeLabel = entity_labels_1.Labels.instance().treeExtractorTypeLabels[type_extractor_item_1.ExtractorItemType.MARKER_LIST_ITEM];
                        }
                        else if (this.gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE) {
                            this.maxExceededTypeLabel = entity_labels_1.Labels.instance().treeExtractorTypeLabels[type_extractor_item_1.ExtractorItemType.SAMPLE_LIST_ITEM];
                        }
                        else {
                            this.handleStatusHeaderMessage(new dto_header_status_message_1.HeaderStatusMessage("This control does not handle the currently selected item type: "
                                + type_extractor_filter_1.GobiiExtractFilterType[this.gobiiExtractFilterType], null, null));
                        }
                        this.displayMaxItemsExceeded = true;
                    }
                };
                SampleMarkerBoxComponent.prototype.makeLabel = function (inputType) {
                    var returnVal;
                    if (this.gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE) {
                        if (inputType === this.ITEM_FILE_TYPE) {
                            returnVal = entity_labels_1.Labels.instance().treeExtractorTypeLabels[type_extractor_item_1.ExtractorItemType.SAMPLE_FILE];
                        }
                        else if (inputType === this.ITEM_LIST_TYPE) {
                            returnVal = entity_labels_1.Labels.instance().treeExtractorTypeLabels[type_extractor_item_1.ExtractorItemType.SAMPLE_LIST_ITEM];
                        }
                        else {
                            this.store.dispatch(new historyAction.AddStatusAction(new dto_header_status_message_1.HeaderStatusMessage("Unhandled input type making input type label "
                                + inputType
                                + " for extract type "
                                + type_extractor_filter_1.GobiiExtractFilterType[this.gobiiExtractFilterType], type_status_level_1.StatusLevel.ERROR, null)));
                        }
                    }
                    else if (this.gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER) {
                        if (inputType === this.ITEM_FILE_TYPE) {
                            returnVal = entity_labels_1.Labels.instance().treeExtractorTypeLabels[type_extractor_item_1.ExtractorItemType.MARKER_FILE];
                        }
                        else if (inputType === this.ITEM_LIST_TYPE) {
                            returnVal = entity_labels_1.Labels.instance().treeExtractorTypeLabels[type_extractor_item_1.ExtractorItemType.MARKER_LIST_ITEM];
                        }
                        else if (inputType === this.MARKER_GROUP_TYPE) {
                            returnVal = entity_labels_1.Labels.instance().entityNodeLabels[type_entity_1.EntityType.MARKER_GROUP];
                        }
                        else {
                            this.store.dispatch(new historyAction.AddStatusAction(new dto_header_status_message_1.HeaderStatusMessage("Unhandled input type making input type label "
                                + inputType
                                + " for extract type "
                                + type_extractor_filter_1.GobiiExtractFilterType[this.gobiiExtractFilterType], type_status_level_1.StatusLevel.ERROR, null)));
                        }
                    }
                    else {
                        this.store.dispatch(new historyAction.AddStatusAction(new dto_header_status_message_1.HeaderStatusMessage("This component is not intended to be used in extract type: " + type_extractor_filter_1.GobiiExtractFilterType[this.gobiiExtractFilterType], type_status_level_1.StatusLevel.ERROR, null)));
                    }
                    return returnVal;
                };
                SampleMarkerBoxComponent.prototype.handleSampleMarkerChoicesExist = function (previousInputType, proposedInputType) {
                    var _this = this;
                    var returnVal = false;
                    this.store.select(fromRoot.getSelectedFileItems)
                        .subscribe(function (fileItems) {
                        var extractorItemTypeListToFind = type_extractor_item_1.ExtractorItemType.UNKNOWN;
                        var extractorItemTypeFileToFind = type_extractor_item_1.ExtractorItemType.UNKNOWN;
                        if (_this.gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE) {
                            extractorItemTypeListToFind = type_extractor_item_1.ExtractorItemType.SAMPLE_LIST_ITEM;
                            extractorItemTypeFileToFind = type_extractor_item_1.ExtractorItemType.SAMPLE_FILE;
                        }
                        else if (_this.gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER) {
                            extractorItemTypeListToFind = type_extractor_item_1.ExtractorItemType.MARKER_LIST_ITEM;
                            extractorItemTypeFileToFind = type_extractor_item_1.ExtractorItemType.MARKER_FILE;
                        }
                        _this.currentFileItems = fileItems.filter(function (item) {
                            return ((item.getExtractorItemType() === extractorItemTypeListToFind) ||
                                (item.getExtractorItemType() === extractorItemTypeFileToFind) ||
                                (item.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.ENTITY &&
                                    item.getEntityType() === type_entity_1.EntityType.MARKER_GROUP));
                        });
                        if (_this.currentFileItems.length > 0) {
                            _this.extractTypeLabelProposed = _this.makeLabel(proposedInputType);
                            _this.extractTypeLabelExisting = _this.makeLabel(previousInputType);
                            _this.displayChoicePrompt = true;
                            returnVal = true;
                            // it does not seem that the PrimeNG dialog really blocks in the usual sense;
                            // so we have to chain what we do next off of the click events on the dialog.
                            // see handleUserChoice()
                        }
                    }, function (hsm) {
                        _this.handleStatusHeaderMessage(hsm);
                    }).unsubscribe();
                    return returnVal;
                };
                SampleMarkerBoxComponent.prototype.handleUserChoice = function (userChoice) {
                    var _this = this;
                    this.displayChoicePrompt = false;
                    if (this.currentFileItems.length > 0 && userChoice === true) {
                        this.setDisplayFlags(this.extractTypeLabelProposed);
                        this.currentFileItems.forEach(function (currentFileItem) {
                            currentFileItem.setProcessType(type_process_1.ProcessType.DELETE);
                            _this.fileItemService
                                .unloadFileItemFromExtract(currentFileItem);
                        });
                    }
                    else {
                        this.setDisplayFlags(this.extractTypeLabelExisting);
                    } // if-else user answered "yes"
                };
                SampleMarkerBoxComponent.prototype.setDisplayFlags = function (labelValue) {
                    if (labelValue === this.makeLabel(this.ITEM_LIST_TYPE)) {
                        this.displayListBox = true;
                        this.displayUploader = false;
                        this.displayMarkerGroupBox = false;
                        this.selectedListType = this.ITEM_LIST_TYPE;
                    }
                    else if (labelValue === this.makeLabel(this.ITEM_FILE_TYPE)) {
                        this.displayListBox = false;
                        this.displayUploader = true;
                        this.displayMarkerGroupBox = false;
                        this.selectedListType = this.ITEM_FILE_TYPE;
                    }
                    else if (labelValue === this.makeLabel(this.MARKER_GROUP_TYPE)) {
                        this.displayListBox = false;
                        this.displayUploader = false;
                        this.displayMarkerGroupBox = true;
                        this.selectedListType = this.MARKER_GROUP_TYPE;
                    }
                    else {
                        this.store.dispatch(new historyAction.AddStatusAction(new dto_header_status_message_1.HeaderStatusMessage("Unhandled input type setting display flags for label type "
                            + labelValue
                            + " for extract type "
                            + type_extractor_filter_1.GobiiExtractFilterType[this.gobiiExtractFilterType], type_status_level_1.StatusLevel.ERROR, null)));
                    }
                }; // setDisplayFlags()
                SampleMarkerBoxComponent.prototype.getPreviousInputType = function () {
                    var returnVal;
                    if (this.displayListBox) {
                        returnVal = this.ITEM_LIST_TYPE;
                    }
                    else if (this.displayUploader) {
                        returnVal = this.ITEM_FILE_TYPE;
                    }
                    else if (this.displayMarkerGroupBox) {
                        returnVal = this.MARKER_GROUP_TYPE;
                    }
                    return returnVal;
                };
                SampleMarkerBoxComponent.prototype.handleTextBoxChanged = function (event) {
                    // if there is no existing selected list or file, then this is just a simple setting
                    if (this.handleSampleMarkerChoicesExist(this.getPreviousInputType(), this.ITEM_LIST_TYPE) === false) {
                        this.displayListBox = true;
                        this.displayUploader = false;
                        this.displayMarkerGroupBox = false;
                        // this.displayListBox = true;
                        // this.displayUploader = false;
                    }
                };
                SampleMarkerBoxComponent.prototype.handleOnClickBrowse = function ($event) {
                    if (this.handleSampleMarkerChoicesExist(this.getPreviousInputType(), this.ITEM_FILE_TYPE) === false) {
                        this.displayListBox = false;
                        this.displayUploader = true;
                        this.displayMarkerGroupBox = false;
                        // this.displayListBox = false;
                        // this.displayUploader = true;
                    }
                };
                SampleMarkerBoxComponent.prototype.handleMarkerGroupChanged = function ($event) {
                    if (this.handleSampleMarkerChoicesExist(this.getPreviousInputType(), this.MARKER_GROUP_TYPE) === false) {
                        this.displayListBox = false;
                        this.displayUploader = false;
                        this.displayMarkerGroupBox = true;
                    }
                };
                SampleMarkerBoxComponent.prototype.handleStatusHeaderMessage = function (statusMessage) {
                    this.onSampleMarkerError.emit(statusMessage);
                };
                SampleMarkerBoxComponent.prototype.ngOnInit = function () {
                    if (this.gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER) {
                        this.fileItemService.loadNameIdsFromFilterParams(this.gobiiExtractFilterType, file_item_param_names_1.FilterParamNames.MARKER_GROUPS, null);
                    }
                    return null;
                };
                SampleMarkerBoxComponent.prototype.ngOnChanges = function (changes) {
                    if (changes['gobiiExtractFilterType']
                        && (changes['gobiiExtractFilterType'].currentValue != null)
                        && (changes['gobiiExtractFilterType'].currentValue != undefined)) {
                        if (changes['gobiiExtractFilterType'].currentValue != changes['gobiiExtractFilterType'].previousValue) {
                            //this.notificationSent = false;
                            if (this.gobiiExtractFilterType == type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER) {
                                this.displayMarkerGroupRadio = true;
                            }
                            else {
                                this.displayMarkerGroupRadio = false;
                            }
                        } // if we have a new filter type
                    } // if filter type changed
                }; // ngonChanges
                SampleMarkerBoxComponent = __decorate([
                    core_1.Component({
                        selector: 'sample-marker-box',
                        inputs: ['gobiiExtractFilterType'],
                        outputs: ['onSampleMarkerError'],
                        encapsulation: core_1.ViewEncapsulation.Native,
                        styleUrls: ["js/node_modules/primeng/resources/themes/omega/theme.css",
                            "js/node_modules/primeng/resources/primeng.css",
                            "js/node_modules/bootswatch/cerulean/bootstrap.min.css"],
                        template: "\n        <div class=\"container-fluid\">\n\n            <div class=\"row\">\n\n                <p-radioButton\n                        (click)=\"handleOnClickBrowse($event)\"\n                        name=\"listType\"\n                        value=\"ITEM_FILE_TYPE\"\n                        [(ngModel)]=\"selectedListType\"\n                        [id]=\"viewIdGeneratorService.makeMarkerSampleListModeRadioButtonId(makeLabel(ITEM_FILE_TYPE))\">\n                </p-radioButton>\n                <label class=\"the-legend\">File&nbsp;</label>\n                <p-radioButton\n                        (click)=\"handleTextBoxChanged($event)\"\n                        name=\"listType\"\n                        value=\"ITEM_LIST_TYPE\"\n                        [(ngModel)]=\"selectedListType\"\n                        [id]=\"viewIdGeneratorService.makeMarkerSampleListModeRadioButtonId(makeLabel(ITEM_LIST_TYPE))\">\n                </p-radioButton>\n                <label class=\"the-legend\">List&nbsp;</label>\n                <p-radioButton *ngIf=\"displayMarkerGroupRadio\"\n                               (click)=\"handleMarkerGroupChanged($event)\"\n                               name=\"listType\"\n                               value=\"MARKER_GROUP_TYPE\"\n                               [(ngModel)]=\"selectedListType\"\n                               [id]=\"viewIdGeneratorService.makeMarkerSampleListModeRadioButtonId(makeLabel(MARKER_GROUP_TYPE))\">\n                </p-radioButton>\n                <label *ngIf=\"displayMarkerGroupRadio\"\n                       class=\"the-legend\">Marker Groups&nbsp;</label>\n\n            </div>\n\n            <div class=\"row\">\n\n                <div *ngIf=\"displayUploader\" class=\"col-md-8\">\n                    <uploader\n                            [gobiiExtractFilterType]=\"gobiiExtractFilterType\"\n                            (onUploaderError)=\"handleStatusHeaderMessage($event)\"></uploader>\n                </div>\n\n                <div *ngIf=\"displayListBox\" class=\"col-md-8\">\n                    <text-area\n                            (onTextboxDataComplete)=\"handleTextBoxDataSubmitted($event)\"></text-area>\n                </div>\n                <div *ngIf=\"displayListBox\" class=\"col-md-4\">\n                    <p class=\"text-warning\">{{maxListItems}} maximum</p>\n                </div>\n\n                <div *ngIf=\"selectedListType == MARKER_GROUP_TYPE\" class=\"col-md-8\">\n                    <checklist-box\n                            [filterParamName]=\"nameIdFilterParamTypesMarkerGroup\"\n                            [gobiiExtractFilterType]=\"gobiiExtractFilterType\">\n                    </checklist-box>\n                </div>\n\n            </div>\n\n            <div>\n                <p-dialog header=\"{{extractTypeLabelExisting}} Already Selelected\" [(visible)]=\"displayChoicePrompt\"\n                          modal=\"modal\" width=\"300\" height=\"300\" responsive=\"true\">\n                    <p>{{extractTypeLabelExisting}} already selected. Specify {{extractTypeLabelProposed}}\n                        instead?</p>\n                    <p-footer>\n                        <div class=\"ui-dialog-buttonpane ui-widget-content ui-helper-clearfix\">\n                            <button type=\"button\" pButton icon=\"fa-close\" (click)=\"handleUserChoice(false)\"\n                                    label=\"No\"></button>\n                            <button type=\"button\" pButton icon=\"fa-check\" (click)=\"handleUserChoice(true)\"\n                                    label=\"Yes\"></button>\n                        </div>\n                    </p-footer>\n                </p-dialog>\n            </div>\n            <div>\n                <p-dialog header=\"Maximum {{maxExceededTypeLabel}} Items Exceeded\" [(visible)]=\"displayMaxItemsExceeded\"\n                          modal=\"modal\" width=\"300\" height=\"300\" responsive=\"true\">\n                    <p>You attempted to paste more than {{maxListItems}} {{maxExceededTypeLabel}} items; Please reduce\n                        the size of the list</p>\n                </p-dialog>\n            </div>"
                    }),
                    __metadata("design:paramtypes", [store_1.Store,
                        file_item_service_1.FileItemService,
                        view_id_generator_service_1.ViewIdGeneratorService])
                ], SampleMarkerBoxComponent);
                return SampleMarkerBoxComponent;
            }());
            exports_1("SampleMarkerBoxComponent", SampleMarkerBoxComponent);
        }
    };
});
//# sourceMappingURL=sample-marker-box.component.js.map