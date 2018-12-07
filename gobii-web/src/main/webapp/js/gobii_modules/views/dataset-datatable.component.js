System.register(["@angular/core", "@ngrx/store", "../store/reducers", "../store/actions/history-action", "../model/type-extractor-filter", "../services/core/file-item-service", "../model/file-item-param-names", "../store/actions/fileitem-action", "../services/core/dto-request.service", "../services/app/jsontogfi/json-to-gfi-dataset", "../services/core/filter-params-coll", "../services/app/dto-request-item-gfi", "../services/app/jsontogfi/json-to-gfi-analysis", "../model/cv-filter-type", "../model/type-entity", "../model/gobii-file-item-compound-id", "../model/type-extractor-item", "rxjs/Subject", "rxjs/add/operator/withLatestFrom", "../store/actions/action-payload-filter", "../services/core/view-id-generator-service"], function (exports_1, context_1) {
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
    var core_1, store_1, fromRoot, historyAction, type_extractor_filter_1, file_item_service_1, file_item_param_names_1, fileAction, dto_request_service_1, json_to_gfi_dataset_1, filter_params_coll_1, dto_request_item_gfi_1, json_to_gfi_analysis_1, cv_filter_type_1, type_entity_1, gobii_file_item_compound_id_1, type_extractor_item_1, Subject_1, action_payload_filter_1, view_id_generator_service_1, DatasetDatatableComponent;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (store_1_1) {
                store_1 = store_1_1;
            },
            function (fromRoot_1) {
                fromRoot = fromRoot_1;
            },
            function (historyAction_1) {
                historyAction = historyAction_1;
            },
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
            },
            function (file_item_service_1_1) {
                file_item_service_1 = file_item_service_1_1;
            },
            function (file_item_param_names_1_1) {
                file_item_param_names_1 = file_item_param_names_1_1;
            },
            function (fileAction_1) {
                fileAction = fileAction_1;
            },
            function (dto_request_service_1_1) {
                dto_request_service_1 = dto_request_service_1_1;
            },
            function (json_to_gfi_dataset_1_1) {
                json_to_gfi_dataset_1 = json_to_gfi_dataset_1_1;
            },
            function (filter_params_coll_1_1) {
                filter_params_coll_1 = filter_params_coll_1_1;
            },
            function (dto_request_item_gfi_1_1) {
                dto_request_item_gfi_1 = dto_request_item_gfi_1_1;
            },
            function (json_to_gfi_analysis_1_1) {
                json_to_gfi_analysis_1 = json_to_gfi_analysis_1_1;
            },
            function (cv_filter_type_1_1) {
                cv_filter_type_1 = cv_filter_type_1_1;
            },
            function (type_entity_1_1) {
                type_entity_1 = type_entity_1_1;
            },
            function (gobii_file_item_compound_id_1_1) {
                gobii_file_item_compound_id_1 = gobii_file_item_compound_id_1_1;
            },
            function (type_extractor_item_1_1) {
                type_extractor_item_1 = type_extractor_item_1_1;
            },
            function (Subject_1_1) {
                Subject_1 = Subject_1_1;
            },
            function (_1) {
            },
            function (action_payload_filter_1_1) {
                action_payload_filter_1 = action_payload_filter_1_1;
            },
            function (view_id_generator_service_1_1) {
                view_id_generator_service_1 = view_id_generator_service_1_1;
            }
        ],
        execute: function () {
            DatasetDatatableComponent = (function () {
                function DatasetDatatableComponent(store, fileItemService, filterParamsColl, fileItemRequestService, viewIdGeneratorService) {
                    var _this = this;
                    this.store = store;
                    this.fileItemService = fileItemService;
                    this.filterParamsColl = filterParamsColl;
                    this.fileItemRequestService = fileItemRequestService;
                    this.viewIdGeneratorService = viewIdGeneratorService;
                    this.foo = "foo";
                    this.onClickForNextPage$ = new Subject_1.Subject();
                    this.doPaging = false;
                    this.datasetAnalysesNames = [];
                    this.nameIdFilterParamTypes = Object.assign({}, file_item_param_names_1.FilterParamNames);
                    this.analysisPanelCollapsed = true;
                    this.analysisPanelToggle = true;
                    this.filterToExtractReady = true;
                    this.disableFilterToExtractReadyCheckbox = false;
                    this.page = 0;
                    if (this.doPaging) {
                        this.datasetsFileItems$ = this.store.select(fromRoot.getDatsetEntitiesPaged);
                    }
                    else {
                        this.datasetsFileItems$ = this.store.select(fromRoot.getDatsetEntities);
                    }
                    this.onClickForNextPage$
                        .withLatestFrom(this.store)
                        .subscribe(function (_a) {
                        var data = _a[0], state = _a[1];
                        if (state.fileItems.filters[file_item_param_names_1.FilterParamNames.DATASET_LIST_PAGED]) {
                            var pagination = state.fileItems.filters[file_item_param_names_1.FilterParamNames.DATASET_LIST_PAGED].pagination;
                            if (pagination) {
                                _this.fileItemService.loadPagedEntityList(_this.gobiiExtractFilterType, file_item_param_names_1.FilterParamNames.DATASET_LIST_PAGED, pagination.pagedQueryId, pagination.pageSize, ++_this.page);
                            }
                        }
                    });
                }
                DatasetDatatableComponent.prototype.handleFilterToExtractReadyChecked = function (event) {
                    var filterValue;
                    if (event === true) {
                        filterValue = "completed";
                    }
                    else {
                        filterValue = null;
                    }
                    this.store.dispatch(new fileAction.LoadFilterAction({
                        filterId: file_item_param_names_1.FilterParamNames.DATASET_LIST_STATUS,
                        filter: new action_payload_filter_1.PayloadFilter(type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET, new gobii_file_item_compound_id_1.GobiiFileItemCompoundId(type_extractor_item_1.ExtractorItemType.ENTITY, type_entity_1.EntityType.DATASET, type_entity_1.EntitySubType.UNKNOWN, cv_filter_type_1.CvFilterType.UNKNOWN, cv_filter_type_1.CvFilters.get(cv_filter_type_1.CvFilterType.UNKNOWN)), null, filterValue, null, null, null)
                    }));
                };
                DatasetDatatableComponent.prototype.handleHideOverlayPanel = function ($event) {
                    this.datasetAnalysesNames = [];
                    this.analysisPanelCollapsed = true;
                };
                /***
                 * Lazy load dataset when the dataset pane is opened. Notice that we don't dispatch the dataset to the store.
                 * There are a couple of things to note here:
                 * 1) Keeping data local to the component breaks the store model, because we are effectively keeping some state locally.
                 *    I would argue that this issues is mitigated by the fact the data are only used in that pop up and then they go away;
                 * 2) Consequently, if the user returns over and over again to the same dataset, we are taking on the otherwise unnecessary
                 *    expense of repeating the same query. However, it is my judgement that that scenario will happen infrequently enough
                 *    that we don't need to worry about this for now.
                 * @param event
                 * @param {GobiiFileItem} dataSeItem
                 * @param {OverlayPanel} datasetOverlayPanel
                 */
                DatasetDatatableComponent.prototype.selectDataset = function (event, dataSeItem, datasetOverlayPanel) {
                    var _this = this;
                    var datasetId = dataSeItem.getEntity().id;
                    var filterParams = this.filterParamsColl.getFilter(file_item_param_names_1.FilterParamNames.DATASET_BY_DATASET_ID, type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET);
                    var dtoRequestItemGfi = new dto_request_item_gfi_1.DtoRequestItemGfi(filterParams, datasetId.toString(), new json_to_gfi_dataset_1.JsonToGfiDataset(filterParams, this.filterParamsColl));
                    this.fileItemRequestService
                        .get(dtoRequestItemGfi)
                        .subscribe(function (entityItems) {
                        if (entityItems.length === 1 && entityItems[0].getEntity()) {
                            _this.selectedDatasetDetailEntity = entityItems[0].getEntity();
                            _this.analysisPanelToggle = _this.selectedDatasetDetailEntity.analysesIds.length > 0;
                            datasetOverlayPanel.toggle(event);
                        }
                        else {
                            _this.store
                                .dispatch(new historyAction.AddStatusMessageAction("There is no dataset data for dataset id "
                                + datasetId.toString()));
                        }
                    });
                };
                /***
                 * Lazy-load analyses if there are any.
                 * The note about not putting these data in the store with regard to the dataset entity applies to
                 * the analyses.
                 * @param event
                 */
                DatasetDatatableComponent.prototype.handleOpenAnalysesTab = function (event) {
                    var _this = this;
                    if (this.selectedDatasetDetailEntity) {
                        var datasetId = this.selectedDatasetDetailEntity.id;
                        var filterParams = this.filterParamsColl.getFilter(file_item_param_names_1.FilterParamNames.ANALYSES_BY_DATASET_ID, type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET);
                        var dtoRequestItemGfi = new dto_request_item_gfi_1.DtoRequestItemGfi(filterParams, datasetId.toString(), new json_to_gfi_analysis_1.JsonToGfiAnalysis(filterParams, this.filterParamsColl));
                        this.fileItemRequestService
                            .get(dtoRequestItemGfi)
                            .subscribe(function (entityItems) {
                            _this.datasetAnalysesNames = entityItems
                                .map(function (gfi) { return gfi.getItemName(); });
                        });
                    } // if we have a selected datset entity
                };
                DatasetDatatableComponent.prototype.handleRowChecked = function (checked, selectedDatasetFileItem) {
                    this.handleItemChecked(selectedDatasetFileItem.getFileItemUniqueId(), checked);
                };
                DatasetDatatableComponent.prototype.handleRowSelect = function (event) {
                    var selectedDatasetFileItem = event.data;
                    this.handleItemChecked(selectedDatasetFileItem.getFileItemUniqueId(), event.originalEvent.checked);
                };
                DatasetDatatableComponent.prototype.handleRowUnSelect = function (event) {
                    var selectedDatasetFileItem = event.data;
                    this.handleItemChecked(selectedDatasetFileItem.getFileItemUniqueId(), event.originalEvent.checked);
                };
                DatasetDatatableComponent.prototype.handleOnRowClick = function (event) {
                    var selectedDataset = event.data;
                };
                DatasetDatatableComponent.prototype.handleItemChecked = function (currentFileItemUniqueId, isChecked) {
                    if (isChecked) {
                        this.store.dispatch(new fileAction.AddToExtractByItemIdAction(currentFileItemUniqueId));
                    }
                    else {
                        this.store.dispatch(new fileAction.RemoveFromExractByItemIdAction(currentFileItemUniqueId));
                    }
                }; // handleItemChecked()
                DatasetDatatableComponent.prototype.ngOnInit = function () {
                    //this.handleLoadPage(1);
                }; // ngOnInit()
                // gobiiExtractType is not set until you get OnChanges
                DatasetDatatableComponent.prototype.ngOnChanges = function (changes) {
                    if (changes['gobiiExtractFilterType']
                        && (changes['gobiiExtractFilterType'].currentValue != null)
                        && (changes['gobiiExtractFilterType'].currentValue != undefined)) {
                        if (changes['gobiiExtractFilterType'].currentValue != changes['gobiiExtractFilterType'].previousValue) {
                            if (this.gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET) {
                                this.filterToExtractReady = true;
                                if (this.doPaging) {
                                    this.fileItemService.loadPagedEntityList(this.gobiiExtractFilterType, file_item_param_names_1.FilterParamNames.DATASET_LIST_PAGED, null, 5, 0);
                                }
                                else {
                                    this.fileItemService.loadEntityList(this.gobiiExtractFilterType, file_item_param_names_1.FilterParamNames.DATASET_LIST);
                                }
                                this.fileItemService.loadNameIdsFromFilterParams(this.gobiiExtractFilterType, file_item_param_names_1.FilterParamNames.CV_JOB_STATUS, null);
                            }
                        } // if we have a new filter type
                    } // if filter type changed
                }; // ngonChanges
                __decorate([
                    core_1.Input(),
                    __metadata("design:type", Number)
                ], DatasetDatatableComponent.prototype, "gobiiExtractFilterType", void 0);
                DatasetDatatableComponent = __decorate([
                    core_1.Component({
                        selector: 'dataset-datatable',
                        inputs: [],
                        outputs: [],
                        template: "\n        <div style=\"border: 1px solid #336699; padding-left: 5px\">\n            <BR>\n            <p-checkbox binary=\"true\"\n                        [(ngModel)]=\"filterToExtractReady\"\n                        (onChange)=\"handleFilterToExtractReadyChecked($event)\"\n                        [disabled]=\"disableFilterToExtractReadyCheckbox\">\n            </p-checkbox>\n            <label class=\"the-legend\">Extract-Ready&nbsp;</label>\n            <BR>\n            <div class=\"container-fluid\">\n\n\n                <!--<name-id-list-box-->\n                <!--[gobiiExtractFilterType]=\"gobiiExtractFilterType\"-->\n                <!--[filterParamName]=\"nameIdFilterParamTypes.CV_JOB_STATUS\">-->\n                <!--</name-id-list-box>-->\n\n            </div> <!--status selector row -->\n            <p-dataTable [value]=\"datasetsFileItems$ | async\"\n                         [(selection)]=\"selectedDatasets\"\n                         (onRowSelect)=\"handleRowSelect($event)\"\n                         (onRowUnselect)=\"handleRowUnSelect($event)\"\n                         (onRowClick)=\"handleOnRowClick($event)\"\n                         dataKey=\"_entity.id\"\n                         resizableColumns=\"true\"\n                         scrollable=\"true\"\n                         scrollHeight=\"700px\"\n                         scrollWidth=\"100%\"\n                         columnResizeMode=\"expand\">\n                <p-column field=\"_entity.id\" header=\"Id\" hidden=\"true\"></p-column>\n                <p-column [style]=\"{'width':'5%','text-align':'center'}\">\n                    <ng-template let-col let-fi=\"rowData\" pTemplate=\"body\">\n                        <p-checkbox binary=\"true\"\n                                    [ngModel]=\"fi.getSelected()\"\n                                    (onChange)=\"handleRowChecked($event, fi)\"\n                                    [hidden]=\"fi.getEntity().jobStatusName !== 'completed'\"\n                                    [id]=\"viewIdGeneratorService.makeDatasetRowCheckboxId(fi._entity.datasetName)\">\n                        </p-checkbox>\n\n                    </ng-template>\n                </p-column>\n                <p-column [style]=\"{'width':'5%','text-align':'center'}\">\n                    <ng-template let-col=\"rowData\" pTemplate=\"body\">\n                        <button type=\"button\"\n                                pButton (click)=\"selectDataset($event,col,datasetOverlayPanel);\"\n                                icon=\"fa-bars\"\n                                style=\"font-size: 10px\"></button>\n                    </ng-template>\n                </p-column>\n                <p-column field=\"_entity.datasetName\"\n                          header=\"Name\"\n                          [style]=\"{'width': '18%'}\"\n                          [sortable]=\"true\">\n                    <ng-template pTemplate=\"body\" let-col let-fi=\"rowData\">\n                        <span pTooltip=\"{{fi._entity.datasetName}}\" tooltipPosition=\"left\"\n                              tooltipStyleClass=\"tableTooltip\"> {{fi._entity.datasetName}} </span>\n                    </ng-template>\n                </p-column>\n                <p-column field=\"_entity.projectName\"\n                          header=\"Project\"\n                          [style]=\"{'width': '18%'}\"\n                          [sortable]=\"true\">\n                    <ng-template pTemplate=\"body\" let-col let-fi=\"rowData\">\n                        <span pTooltip=\"{{fi._entity.projectName}}\" tooltipPosition=\"left\"\n                              tooltipStyleClass=\"tableTooltip\"> {{fi._entity.projectName}} </span>\n                    </ng-template>\n                </p-column>\n                <p-column field=\"_entity.experimentName\"\n                          header=\"Experiment\"\n                          [style]=\"{'width': '18%'}\"\n                          [sortable]=\"true\">\n                    <ng-template pTemplate=\"body\" let-col let-fi=\"rowData\">\n                        <span pTooltip=\"{{fi._entity.experimentName}}\" tooltipPosition=\"left\"\n                              tooltipStyleClass=\"tableTooltip\"> {{fi._entity.experimentName}} </span>\n                    </ng-template>\n                </p-column>\n                <p-column field=\"_entity.piLastName\"\n                          header=\"PI\"\n                          [style]=\"{'width': '18%'}\"\n                          [sortable]=\"true\">\n                    <ng-template pTemplate=\"body\" let-col let-fi=\"rowData\">\n                        <span pTooltip=\"{{fi._entity.piLastName}}, {{fi._entity.piFirstName}}\" tooltipPosition=\"left\"\n                              tooltipStyleClass=\"tableTooltip\"> {{fi._entity.piLastName}}, {{fi._entity.piFirstName}} </span>\n                    </ng-template>\n                </p-column>\n                <!--<p-column field=\"_entity.jobStatusName\" header=\"Status\"></p-column>-->\n                <!--<p-column field=\"_entity.jobTypeName\" header=\"Type\"></p-column>-->\n                <p-column field=\"_entity.loadedDate\"\n                          header=\"Loaded\"\n                          [style]=\"{'width': '18%'}\"\n                          [sortable]=\"true\">\n                    <ng-template let-col let-fi=\"rowData\" pTemplate=\"body\">\n                        {{fi._entity.loadedDate | date:'yyyy-MM-dd' }}\n                    </ng-template>\n                </p-column>\n            </p-dataTable>\n            <p-overlayPanel #datasetOverlayPanel\n                            appendTo=\"body\"\n                            showCloseIcon=\"true\"\n                            (onBeforeHide)=\"handleHideOverlayPanel($event)\">\n\n\n                <!-- you have to  -->\n                <legend>Details:\n                    {{ selectedDatasetDetailEntity ? selectedDatasetDetailEntity.datasetName : null}}\n                </legend>\n\n\n                <div class=\"panel panel-default\">\n                    <table class=\"table table-striped table-hover\">\n                        <!--<table class=\"table table-striped table-hover table-bordered\">-->\n                        <tbody>\n                        <tr>\n                            <td><b>Principle Investigator</b></td>\n                            <td>\n                                {{ selectedDatasetDetailEntity ? selectedDatasetDetailEntity.piLastName + \", \" + selectedDatasetDetailEntity.piFirstName : null}}\n                            </td>\n                        </tr>\n                        <tr>\n                            <td><b>Loaded By</b></td>\n                            <td>{{ selectedDatasetDetailEntity ? selectedDatasetDetailEntity.loaderLastName : null}}\n                                {{ (selectedDatasetDetailEntity && selectedDatasetDetailEntity.loaderFirstName) ? \", \" : null}}\n                                {{ selectedDatasetDetailEntity ? selectedDatasetDetailEntity.loaderFirstName : null}}\n                            </td>\n                        </tr>\n\n                        <tr>\n                            <td><b>Loaded Date</b></td>\n                            <td>\n                                {{ selectedDatasetDetailEntity ? (selectedDatasetDetailEntity.loadedDate | date:'yyyy-MM-dd') : null}}\n                            </td>\n                        </tr>\n\n                        <tr>\n                            <td><b>Project</b></td>\n                            <td>\n                                {{ selectedDatasetDetailEntity ? selectedDatasetDetailEntity.projectName : null}}\n                            </td>\n                        </tr>\n\n\n                        <tr>\n                            <td><b>Data Type</b></td>\n                            <td>\n                                {{ selectedDatasetDetailEntity ? selectedDatasetDetailEntity.datatypeName : null}}\n                            </td>\n                        </tr>\n\n\n                        <tr>\n                            <td><b>Calling Analysis</b></td>\n                            <td>\n                                {{ selectedDatasetDetailEntity ? selectedDatasetDetailEntity.callingAnalysisName : null}}\n                            </td>\n                        </tr>\n\n                        <tr>\n                            <td><b>Total Samples</b></td>\n                            <td>\n                                {{ selectedDatasetDetailEntity ? selectedDatasetDetailEntity.totalSamples : null}}\n                            </td>\n                        </tr>\n\n\n                        <tr>\n                            <td><b>Total Markers</b></td>\n                            <td>\n                                {{ selectedDatasetDetailEntity ? selectedDatasetDetailEntity.totalMarkers : null}}\n                            </td>\n                        </tr>\n\n                        </tbody>\n                    </table>\n                </div>\n\n\n                <div class=\"panel panel-default\">\n                    <div class=\"panel-heading\" style=\"font-size: medium\">\n                        <b>Experiment:\n                            {{ selectedDatasetDetailEntity ? selectedDatasetDetailEntity.experimentName : null}}</b>\n\n                    </div>\n                    <div class=\"card text-white bg-info\">\n\n                        <div class=\"card-body\">\n                            <table class=\"table table-striped table-hover\">\n                                <!--<table class=\"table table-striped table-hover table-bordered\">-->\n                                <tbody>\n                                <tr>\n                                    <td>Platform:</td>\n                                    <td>{{ selectedDatasetDetailEntity ? selectedDatasetDetailEntity.platformName : null}}\n                                    </td>\n                                </tr>\n                                <tr>\n                                    <td>Protocol:</td>\n                                    <td>{{ selectedDatasetDetailEntity ? selectedDatasetDetailEntity.protocolName : null}}\n                                    </td>\n                                </tr>\n                                </tbody>\n                            </table>\n\n                        </div>\n                    </div>\n                </div>\n                <BR>\n                <div>\n\n                    <p-panel\n                            header=\"{{ selectedDatasetDetailEntity ? selectedDatasetDetailEntity.analysesIds.length : null}} Analyses\"\n                            (onBeforeToggle)=\"handleOpenAnalysesTab($event)\"\n                            [(toggleable)]=\"analysisPanelToggle\"\n                            [(collapsed)]=\"analysisPanelCollapsed\">\n                        <p *ngFor=\"let name of datasetAnalysesNames\">\n                            {{ name }}\n                        </p>\n                    </p-panel>\n                </div>\n\n            </p-overlayPanel>\n\n            <div *ngIf=\"doPaging\">\n                <button pButton type=\"button\" (click)=\"onClickForNextPage$.next($event)\" label=\"Test Paging\"></button>\n            </div>\n        </div> <!-- enclosing box  -->\n    " // end template
                    }),
                    __metadata("design:paramtypes", [store_1.Store,
                        file_item_service_1.FileItemService,
                        filter_params_coll_1.FilterParamsColl,
                        dto_request_service_1.DtoRequestService,
                        view_id_generator_service_1.ViewIdGeneratorService])
                ], DatasetDatatableComponent);
                return DatasetDatatableComponent;
            }());
            exports_1("DatasetDatatableComponent", DatasetDatatableComponent);
        }
    };
});
//# sourceMappingURL=dataset-datatable.component.js.map