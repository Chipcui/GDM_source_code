System.register(["@angular/core", "../../model/type-extractor-filter", "../../model/gobii-file-item", "../../store/actions/history-action", "../../store/actions/fileitem-action", "../../store/reducers", "@ngrx/store", "../../model/file-item-param-names", "rxjs/add/operator/expand", "rxjs/add/operator/concat", "./filter-params-coll", "../../store/actions/action-payload-filter", "../../model/cv-group", "../../views/entity-labels", "../../model/dto-header-status-message", "../../model/type-process", "../../model/type-extractor-item", "../../model/type-entity", "../../model/name-id-label-type"], function (exports_1, context_1) {
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
    var core_1, type_extractor_filter_1, gobii_file_item_1, historyAction, fileItemActions, fromRoot, store_1, file_item_param_names_1, filter_params_coll_1, action_payload_filter_1, cv_group_1, entity_labels_1, dto_header_status_message_1, type_process_1, type_extractor_item_1, type_entity_1, name_id_label_type_1, FilterService;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
            },
            function (gobii_file_item_1_1) {
                gobii_file_item_1 = gobii_file_item_1_1;
            },
            function (historyAction_1) {
                historyAction = historyAction_1;
            },
            function (fileItemActions_1) {
                fileItemActions = fileItemActions_1;
            },
            function (fromRoot_1) {
                fromRoot = fromRoot_1;
            },
            function (store_1_1) {
                store_1 = store_1_1;
            },
            function (file_item_param_names_1_1) {
                file_item_param_names_1 = file_item_param_names_1_1;
            },
            function (_1) {
            },
            function (_2) {
            },
            function (filter_params_coll_1_1) {
                filter_params_coll_1 = filter_params_coll_1_1;
            },
            function (action_payload_filter_1_1) {
                action_payload_filter_1 = action_payload_filter_1_1;
            },
            function (cv_group_1_1) {
                cv_group_1 = cv_group_1_1;
            },
            function (entity_labels_1_1) {
                entity_labels_1 = entity_labels_1_1;
            },
            function (dto_header_status_message_1_1) {
                dto_header_status_message_1 = dto_header_status_message_1_1;
            },
            function (type_process_1_1) {
                type_process_1 = type_process_1_1;
            },
            function (type_extractor_item_1_1) {
                type_extractor_item_1 = type_extractor_item_1_1;
            },
            function (type_entity_1_1) {
                type_entity_1 = type_entity_1_1;
            },
            function (name_id_label_type_1_1) {
                name_id_label_type_1 = name_id_label_type_1_1;
            }
        ],
        execute: function () {
            FilterService = (function () {
                function FilterService(store, filterParamsColl) {
                    this.store = store;
                    this.filterParamsColl = filterParamsColl;
                } // constructor
                FilterService.prototype.init = function () {
                    this.filterParamsColl.init();
                };
                FilterService.prototype.loadFilter = function (gobiiExtractFilterType, filterParamsName, filterValue) {
                    var filterParams = this.filterParamsColl.getFilter(filterParamsName, gobiiExtractFilterType);
                    if (filterParams) {
                        var loadAction = new fileItemActions.LoadFilterAction({
                            filterId: filterParams.getQueryName(),
                            filter: new action_payload_filter_1.PayloadFilter(gobiiExtractFilterType, filterParams.getTargetEntityUniqueId(), filterParams.getRelatedEntityUniqueId(), null, filterValue, null, null)
                        });
                        this.store.dispatch(loadAction);
                    }
                    else {
                        this.store.dispatch(new historyAction.AddStatusMessageAction("Error loading filter: there is no query params object for query "
                            + filterParamsName
                            + " with extract filter type "
                            + type_extractor_filter_1.GobiiExtractFilterType[gobiiExtractFilterType]));
                    }
                };
                FilterService.prototype.getForFilter = function (filterParamName) {
                    //Wrapping an Observable around the select functions just doesn't work. So at leaset this
                    //function can encapsualte getting the correct selector for the filter
                    var returnVal;
                    switch (filterParamName) {
                        case file_item_param_names_1.FilterParamNames.MARKER_GROUPS:
                            returnVal = this.store.select(fromRoot.getMarkerGroups);
                            break;
                        case file_item_param_names_1.FilterParamNames.PROJECTS:
                            returnVal = this.store.select(fromRoot.getProjects);
                            break;
                        case file_item_param_names_1.FilterParamNames.PROJECTS_BY_CONTACT:
                            returnVal = this.store.select(fromRoot.getProjectsByPI);
                            break;
                        case file_item_param_names_1.FilterParamNames.EXPERIMENTS_BY_PROJECT:
                            returnVal = this.store.select(fromRoot.getExperimentsByProject);
                            break;
                        case file_item_param_names_1.FilterParamNames.EXPERIMENTS:
                            returnVal = this.store.select(fromRoot.getExperiments);
                            break;
                        case file_item_param_names_1.FilterParamNames.DATASETS_BY_EXPERIMENT:
                            returnVal = this.store.select(fromRoot.getDatasetsByExperiment);
                            break;
                        case file_item_param_names_1.FilterParamNames.PLATFORMS:
                            returnVal = this.store.select(fromRoot.getPlatforms);
                            break;
                        case file_item_param_names_1.FilterParamNames.CV_DATATYPE:
                            returnVal = this.store.select(fromRoot.getCvTermsDataType);
                            break;
                        case file_item_param_names_1.FilterParamNames.CV_JOB_STATUS:
                            returnVal = this.store.select(fromRoot.getCvTermsJobStatus);
                            break;
                        case file_item_param_names_1.FilterParamNames.MAPSETS:
                            returnVal = this.store.select(fromRoot.getMapsets);
                            break;
                        case file_item_param_names_1.FilterParamNames.CONTACT_PI_HIERARCHY_ROOT:
                            returnVal = this.store.select(fromRoot.getPiContacts);
                            break;
                        case file_item_param_names_1.FilterParamNames.CONTACT_PI_FILTER_OPTIONAL:
                            returnVal = this.store.select(fromRoot.getPiContactsFilterOptional);
                            break;
                        case file_item_param_names_1.FilterParamNames.PROJECT_FILTER_OPTIONAL:
                            returnVal = this.store.select(fromRoot.getProjectsFilterOptional);
                            break;
                        case file_item_param_names_1.FilterParamNames.EXPERIMENT_FILTER_OPTIONAL:
                            returnVal = this.store.select(fromRoot.getExperimentsFilterOptional);
                            break;
                        //***************************
                        // the FQ filters are for now just placeholders
                        //------- F1 --------------------------------------
                        case file_item_param_names_1.FilterParamNames.FQ_F1_VERTICES:
                            returnVal = this.store.select(fromRoot.getFqF1Vertices);
                            break;
                        case file_item_param_names_1.FilterParamNames.FQ_F1_VERTEX_VALUES:
                            returnVal = this.store.select(fromRoot.getFqF1VerticesValues);
                            break;
                        //------- F2 --------------------------------------
                        case file_item_param_names_1.FilterParamNames.FQ_F2_VERTICES:
                            returnVal = this.store.select(fromRoot.getFqF2Vertices);
                            break;
                        case file_item_param_names_1.FilterParamNames.FQ_F2_VERTEX_VALUES:
                            returnVal = this.store.select(fromRoot.getFqF2VerticesValues);
                            break;
                        //------- F3 --------------------------------------
                        case file_item_param_names_1.FilterParamNames.FQ_F3_VERTICES:
                            returnVal = this.store.select(fromRoot.getFqF3Vertices);
                            break;
                        case file_item_param_names_1.FilterParamNames.FQ_F3_VERTEX_VALUES:
                            returnVal = this.store.select(fromRoot.getFqF3VerticesValues);
                            break;
                        //------- F4 --------------------------------------
                        case file_item_param_names_1.FilterParamNames.FQ_F4_VERTICES:
                            returnVal = this.store.select(fromRoot.getFqF4Vertices);
                            break;
                        case file_item_param_names_1.FilterParamNames.FQ_F4_VERTEX_VALUES:
                            returnVal = this.store.select(fromRoot.getFqF4VerticesValues);
                            break;
                        default:
                            this.store.dispatch(new historyAction.AddStatusMessageAction("There is no selector for filter "
                                + filterParamName));
                            break;
                    }
                    return returnVal;
                }; // getForFilter()
                FilterService.prototype.makeLabelItem = function (gobiiExtractFilterType, filterParamsToLoad) {
                    var returnVal;
                    if (filterParamsToLoad.getNameIdLabelType() != name_id_label_type_1.NameIdLabelType.UNKNOWN) {
                        var entityName = "";
                        if (filterParamsToLoad.getCvGroup() !== cv_group_1.CvGroup.UNKNOWN) {
                            entityName += entity_labels_1.Labels.instance().cvGroupLabels[filterParamsToLoad.getCvGroup()];
                        }
                        else if (filterParamsToLoad.getEntitySubType() !== type_entity_1.EntitySubType.UNKNOWN) {
                            entityName += entity_labels_1.Labels.instance().entitySubtypeNodeLabels[filterParamsToLoad.getEntitySubType()];
                        }
                        else if (filterParamsToLoad.getEntityType() != type_entity_1.EntityType.UNKNOWN) {
                            entityName += entity_labels_1.Labels.instance().entityNodeLabels[filterParamsToLoad.getEntityType()];
                        }
                        else {
                            entityName += entity_labels_1.Labels.instance().treeExtractorTypeLabels[filterParamsToLoad.getExtractorItemType()];
                        }
                        var label = "";
                        switch (filterParamsToLoad.getNameIdLabelType()) {
                            case name_id_label_type_1.NameIdLabelType.SELECT_A:
                                label = "Select a " + entityName;
                                break;
                            // we require that these entity labels all be in the singular
                            case name_id_label_type_1.NameIdLabelType.ALL:
                                label = "All " + entityName + "s";
                                break;
                            case name_id_label_type_1.NameIdLabelType.NO:
                                label = "No " + entityName;
                                break;
                            default:
                                this.store.dispatch(new historyAction.AddStatusAction(new dto_header_status_message_1.HeaderStatusMessage("Unknown label type "
                                    + name_id_label_type_1.NameIdLabelType[filterParamsToLoad.getNameIdLabelType()], null, null)));
                        }
                        returnVal = gobii_file_item_1.GobiiFileItem
                            .build(gobiiExtractFilterType, type_process_1.ProcessType.CREATE)
                            .setEntityType(filterParamsToLoad.getEntityType())
                            .setEntitySubType(filterParamsToLoad.getEntitySubType())
                            .setCvGroup(filterParamsToLoad.getCvGroup())
                            .setExtractorItemType(type_extractor_item_1.ExtractorItemType.UNKNOWN)
                            .setNameIdLabelType(filterParamsToLoad.getNameIdLabelType())
                            .setItemName(label)
                            .setIsExtractCriterion(filterParamsToLoad.getIsExtractCriterion())
                            .setItemId("0");
                    } // if we have a label item type that requires a label item
                    return returnVal;
                }; // makeLabelItem()
                FilterService = __decorate([
                    core_1.Injectable(),
                    __metadata("design:paramtypes", [store_1.Store,
                        filter_params_coll_1.FilterParamsColl])
                ], FilterService);
                return FilterService;
            }());
            exports_1("FilterService", FilterService);
        }
    };
});
//# sourceMappingURL=filter-service.js.map