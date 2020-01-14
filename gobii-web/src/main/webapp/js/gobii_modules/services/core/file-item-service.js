System.register(["@angular/core", "../../model/type-entity", "../../views/entity-labels", "../../model/type-extractor-item", "../../model/type-extractor-filter", "../../model/cv-filter-type", "../../model/gobii-file-item", "../../model/dto-header-status-message", "../../model/type-process", "./name-id-service", "../../store/actions/history-action", "../../store/actions/fileitem-action", "../../store/reducers", "@ngrx/store", "../../model/name-id-label-type", "../../model/filter-type", "../../model/file-item-param-names", "rxjs/Observable", "rxjs/add/operator/expand", "rxjs/add/operator/concat", "./dto-request.service", "../app/dto-request-item-entity-stats", "./filter-params-coll", "../../model/gobii-file-item-entity-relation", "../../model/type-status-level", "../../store/actions/action-payload-filter"], function (exports_1, context_1) {
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
    var core_1, type_entity_1, entity_labels_1, type_extractor_item_1, type_extractor_filter_1, cv_filter_type_1, gobii_file_item_1, dto_header_status_message_1, type_process_1, name_id_service_1, historyAction, fileItemActions, fromRoot, store_1, name_id_label_type_1, filter_type_1, file_item_param_names_1, Observable_1, dto_request_service_1, dto_request_item_entity_stats_1, filter_params_coll_1, gobii_file_item_entity_relation_1, type_status_level_1, action_payload_filter_1, FileItemService;
    var __moduleName = context_1 && context_1.id;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (type_entity_1_1) {
                type_entity_1 = type_entity_1_1;
            },
            function (entity_labels_1_1) {
                entity_labels_1 = entity_labels_1_1;
            },
            function (type_extractor_item_1_1) {
                type_extractor_item_1 = type_extractor_item_1_1;
            },
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
            },
            function (cv_filter_type_1_1) {
                cv_filter_type_1 = cv_filter_type_1_1;
            },
            function (gobii_file_item_1_1) {
                gobii_file_item_1 = gobii_file_item_1_1;
            },
            function (dto_header_status_message_1_1) {
                dto_header_status_message_1 = dto_header_status_message_1_1;
            },
            function (type_process_1_1) {
                type_process_1 = type_process_1_1;
            },
            function (name_id_service_1_1) {
                name_id_service_1 = name_id_service_1_1;
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
            function (name_id_label_type_1_1) {
                name_id_label_type_1 = name_id_label_type_1_1;
            },
            function (filter_type_1_1) {
                filter_type_1 = filter_type_1_1;
            },
            function (file_item_param_names_1_1) {
                file_item_param_names_1 = file_item_param_names_1_1;
            },
            function (Observable_1_1) {
                Observable_1 = Observable_1_1;
            },
            function (_1) {
            },
            function (_2) {
            },
            function (dto_request_service_1_1) {
                dto_request_service_1 = dto_request_service_1_1;
            },
            function (dto_request_item_entity_stats_1_1) {
                dto_request_item_entity_stats_1 = dto_request_item_entity_stats_1_1;
            },
            function (filter_params_coll_1_1) {
                filter_params_coll_1 = filter_params_coll_1_1;
            },
            function (gobii_file_item_entity_relation_1_1) {
                gobii_file_item_entity_relation_1 = gobii_file_item_entity_relation_1_1;
            },
            function (type_status_level_1_1) {
                type_status_level_1 = type_status_level_1_1;
            },
            function (action_payload_filter_1_1) {
                action_payload_filter_1 = action_payload_filter_1_1;
            }
        ],
        execute: function () {
            FileItemService = /** @class */ (function () {
                function FileItemService(nameIdService, entityStatsService, fileItemRequestService, store, filterParamsColl) {
                    // For non-hierarchically filtered request params, we just create them simply
                    // as we add them to the flat map
                    this.nameIdService = nameIdService;
                    this.entityStatsService = entityStatsService;
                    this.fileItemRequestService = fileItemRequestService;
                    this.store = store;
                    this.filterParamsColl = filterParamsColl;
                    this.NONE_ITEM_ITEM_ID = "-1";
                } // constructor
                FileItemService.prototype.loadFilter = function (gobiiExtractFilterType, filterParamsName, filterValue) {
                    var filterParams = this.filterParamsColl.getFilter(filterParamsName, gobiiExtractFilterType);
                    if (filterParams) {
                        var loadAction = new fileItemActions.LoadFilterAction({
                            filterId: filterParams.getQueryName(),
                            filter: new action_payload_filter_1.PayloadFilter(gobiiExtractFilterType, filterParams.getTargetEtityUniqueId(), filterParams.getRelatedEntityUniqueId(), filterValue, null, null, null)
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
                FileItemService.prototype.getForFilter = function (filterParamName) {
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
                        default:
                            returnVal = this.store.select(fromRoot.getAllFileItems);
                            break;
                    }
                    return returnVal;
                    // THIS IS THE THING THAT DIDN'T WORK; IT'S WORTH KEEPING IT AROUND FOR REFERENCE.
                    // return Observable.create(observer => {
                    //     let filteredItems: GobiiFileItem[] = [];
                    //     this.store
                    //         .select(fromRoot.getAllFileItems)
                    //         .subscribe(fileItems => {
                    //                 let nameIdRequestParams: FileItemParams = this.nameIdRequestParams.get(filterParamNames);
                    //                 if (nameIdRequestParams) {
                    //                     if (!nameIdRequestParams.getIsDynamicFilterValue()) {
                    //                         filteredItems = fileItems.filter(fi => fi.compoundIdeEquals(nameIdRequestParams))
                    //                     } else {
                    //                         this.store.select(fromRoot.getFileItemsFilters)
                    //                             .subscribe(filters => {
                    //                                 if (filters[nameIdRequestParams.getQueryName()]) {
                    //                                     let filterValue: string = filters[nameIdRequestParams.getQueryName()].filterValue;
                    //                                     filteredItems = fileItems.filter(
                    //                                         fi =>
                    //                                             fi.compoundIdeEquals(nameIdRequestParams)
                    //                                             && fi.getParentItemId() === filterValue);
                    //
                    //                                     if (filteredItems.length <= 0) {
                    //                                         filteredItems = fileItems.filter(e =>
                    //                                             ( e.getExtractorItemType() === ExtractorItemType.ENTITY
                    //                                                 && e.getEntityType() === EntityType.DATASET
                    //                                                 //                    && e.getParentItemId() === experimentId
                    //                                                 && e.getProcessType() === ProcessType.DUMMY))
                    //                                             .map(fi => fi);
                    //
                    //                                     }
                    //                                     observer.next(filteredItems)
                    //                                 } // if filters have been populated
                    //                             });
                    //
                    //                     }
                    //                 }
                    //             } //Store.subscribe
                    //         );
                    //
                    //
                    //
                    // }) //Observable.create()
                };
                FileItemService.prototype.setItemLabelType = function (gobiiExtractFilterType, filterParamNames, nameIdLabelType) {
                    var nameIdRequestParamsFromType = this.filterParamsColl.getFilter(filterParamNames, gobiiExtractFilterType);
                    if (nameIdRequestParamsFromType) {
                        nameIdRequestParamsFromType.setNameIdLabelType(nameIdLabelType);
                    }
                    else {
                        this.store.dispatch(new historyAction.AddStatusMessageAction("Error setting label type: there is no query params object for query "
                            + filterParamNames
                            + " with extract filter type "
                            + type_extractor_filter_1.GobiiExtractFilterType[gobiiExtractFilterType]));
                    }
                };
                FileItemService.prototype.loadNameIdsFromFilterParams = function (gobiiExtractFilterType, filterParamName, filterValue) {
                    var _this = this;
                    var nameIdRequestParamsFromType = this.filterParamsColl.getFilter(filterParamName, gobiiExtractFilterType);
                    if (nameIdRequestParamsFromType) {
                        this.makeNameIdLoadActions(gobiiExtractFilterType, filterParamName, filterValue)
                            .subscribe(function (action) {
                            if (action) {
                                _this.store.dispatch(action);
                            }
                        });
                    }
                    else {
                        this.store.dispatch(new historyAction.AddStatusMessageAction("Error loading with filter params: there is no query params object for query "
                            + filterParamName
                            + " with extract filter type "
                            + type_extractor_filter_1.GobiiExtractFilterType[gobiiExtractFilterType]));
                    }
                };
                FileItemService.prototype.loadFileItem = function (gobiiFileItem, selectForExtract) {
                    var loadAction = new fileItemActions.LoadFileItemtAction({
                        gobiiFileItem: gobiiFileItem,
                        selectForExtract: selectForExtract
                    });
                    this.store.dispatch(loadAction);
                };
                /***
                 * This is a hard-repalce: the item is not just removed from the extract, but nuked entirely from
                 * the store
                 * @param {GobiiFileItem} gobiiFileItem
                 * @param {boolean} selectForExtract
                 */
                FileItemService.prototype.replaceFileItemByCompoundId = function (gobiiFileItem) {
                    var loadAction = new fileItemActions.ReplaceItemOfSameCompoundIdAction({
                        gobiiFileitemToReplaceWith: gobiiFileItem
                    });
                    this.store.dispatch(loadAction);
                };
                FileItemService.prototype.unloadFileItemFromExtract = function (gobiiFileItem) {
                    var loadAction = new fileItemActions.RemoveFromExtractAction(gobiiFileItem);
                    this.store.dispatch(loadAction);
                };
                /***
                 * The purpose of this method is to create an observable of filter load actions for a given filter
                 * with a given filter value. Conceptually, the filtername and value passed to this function are the
                 * initial target filter. For example, if the user selects a PI (contact) for the dataset grid, the filter name
                 * and filter value (contactId) will make their way (though the effect for the action) to this method. The contactId filter
                 * value passed in will become the "target" value for the filter. The idea is that the target reflects
                 * the value that the user actually selected for a given filter. The primary observable creates the
                 * action for this filter. The makeChildActions() method with which it is concatenated performs further
                 * processing on the filter's children. The makeChildActions() method will take the first child filter of the
                 * target filter and pass it along with what is now considered the "parent" filter value (still contactId) into the recurseFilters()
                 * method. There the name of the game is to set up the related filter values. For example, the contract filter
                 * will have a child filter for projects. recurseFilters() will set the related filter value of the project
                 * filter to the contact id of the target (parent) filter. The recurseFilters() method will now recursively
                 * process the children of the project filter and its children. For the subsequent children, it will figure
                 * out how to set the related IDs (the new parent ids) based on the results of the filter passed to it. For
                 * example, when the project filter is passed to recurseFilters(), recurseFilters() will also retrieve all
                 * file items of type project with that contact ID as their related entity. It will then use the 0th such
                 * file item's item id as the new parentid of the project filter's child filters. The filter's target filter
                 * value is set in only two places: in this method, and in recruseFilters(). recruseFilters() will set it to null
                 * in the condition where the related filter value has changed. Thus, for example, if the user selects a new
                 * PI value, the contactId in the project filter's related filter value will no longer match. In this circumstance,
                 * the target filter value of the project filter has been invalidated by the new related (parent) id value,
                 * and so the target filter value of the project filter will be set to null. What this means in practice is that
                 * only related IDs are cascaded; target IDs are selected by the user or invalidated by the user's selection of
                 * a new parent ID. Moreover, for the most part the target filter values are used for filtering the content of the
                 * grid whereas the related IDs are used for filtering the content of the grid's drop downs.
                 *
                 * The organization of these nethods is a bit haphazard and represents the atheoretical way in which they evolved.
                 * It should be that there is just one method and one obsevable here that handles everything more elegantly. But
                 * for now this all seems to work.
                 *
                 * @param {GobiiExtractFilterType} gobiiExtractFilterType
                 * @param {FilterParamNames} filterParamName
                 * @param {string} newTargetFilterValue
                 * @returns {Observable<LoadFileItemListWithFilterAction>}
                 */
                FileItemService.prototype.makeFileActionsFromFilterParamName = function (gobiiExtractFilterType, filterParamName, newTargetFilterValue) {
                    var _this = this;
                    return Observable_1.Observable.create(function (observer) {
                        var parentFilterParams = _this.filterParamsColl.getFilter(filterParamName, gobiiExtractFilterType);
                        _this.store
                            .select(fromRoot.getFileItemsFilters)
                            .subscribe(function (filters) {
                            var relatedEntityFiltervalueFromState = null;
                            var stateFilter = filters[filterParamName.toString()];
                            if (stateFilter) {
                                relatedEntityFiltervalueFromState = stateFilter.relatedEntityFilterValue;
                            }
                            if (parentFilterParams) {
                                //parentFilterParams.setTargetEntityFilterValue(parentFilterValue);
                                var parentLoadAction = new fileItemActions.LoadFilterAction({
                                    filterId: parentFilterParams.getQueryName(),
                                    filter: new action_payload_filter_1.PayloadFilter(gobiiExtractFilterType, parentFilterParams.getTargetEtityUniqueId(), parentFilterParams.getRelatedEntityUniqueId(), relatedEntityFiltervalueFromState, newTargetFilterValue, null, //not sure about this
                                    null)
                                });
                                observer.next(parentLoadAction);
                                observer.complete();
                            }
                            else {
                                _this.store.dispatch(new historyAction.AddStatusMessageAction("Undefined FileItemParams filter: "
                                    + filterParamName.toString()
                                    + " for extract type " + type_extractor_filter_1.GobiiExtractFilterType[gobiiExtractFilterType]));
                            }
                        });
                    }).concat(this.makeChildActions(gobiiExtractFilterType, filterParamName, newTargetFilterValue)); // observable
                };
                FileItemService.prototype.makeChildActions = function (gobiiExtractFilterType, filterParamName, parentFilterValue) {
                    var returnVal;
                    var parentFilterParams = this.filterParamsColl.getFilter(filterParamName, gobiiExtractFilterType);
                    if (parentFilterParams) {
                        // Now we process child filters
                        var filterParamsToProcess = null;
                        if (parentFilterParams.getIsDynamicFilterValue() || parentFilterParams.getIsDynamicFilterValue()) {
                            if (parentFilterParams.getChildFileItemParams() && parentFilterParams.getChildFileItemParams().length === 1) {
                                filterParamsToProcess = parentFilterParams.getChildFileItemParams()[0];
                                if (filterParamsToProcess.getIsDynamicDataLoad()) {
                                    returnVal = this.makeFileItemActionsFromNameIds(gobiiExtractFilterType, filterParamsToProcess, parentFilterValue, true);
                                }
                                else {
                                    returnVal = this.recurseFilters(gobiiExtractFilterType, filterParamsToProcess, parentFilterValue, filterParamsToProcess.getIsDynamicFilterValue());
                                }
                            }
                            else {
                                this.store.dispatch(new historyAction.AddStatusMessageAction("The dynamic filter does not have children: "
                                    + filterParamName.toString()
                                    + " for extract type " + type_extractor_filter_1.GobiiExtractFilterType[gobiiExtractFilterType]));
                            }
                        }
                        else {
                            return Observable_1.Observable.of(null);
                        }
                    }
                    else {
                        this.store.dispatch(new historyAction.AddStatusMessageAction("Undefined FileItemParams filter: "
                            + filterParamName.toString()
                            + " for extract type " + type_extractor_filter_1.GobiiExtractFilterType[gobiiExtractFilterType]));
                    }
                    return returnVal;
                };
                FileItemService.prototype.makeNameIdLoadActions = function (gobiiExtractFilterType, filterParamName, filterValue) {
                    var nameIdRequestParamsFromType = this.filterParamsColl.getFilter(filterParamName, gobiiExtractFilterType);
                    if (nameIdRequestParamsFromType) {
                        return this.makeFileItemActionsFromNameIds(gobiiExtractFilterType, nameIdRequestParamsFromType, filterValue, true);
                    }
                    else {
                        this.store.dispatch(new historyAction.AddStatusMessageAction("Undefined FileItemParams filter: "
                            + filterParamName.toString()
                            + " for extract type " + type_extractor_filter_1.GobiiExtractFilterType[gobiiExtractFilterType]));
                    }
                };
                /***
                 * This is the core retrieval method for nameIds, which is the bread-and butter of the extractor UI.
                 * This method uses the /entity/lastmodified/{entityName} and the client retrieval history to determine
                 * whether or not data need to be retrieved from the server or can be retrieved from the local store.
                 * This method does not actually add anything to the store. Rather, it is consumed by other places in the
                 * code that need to add GobiiFileItems for particular entities to the store. So this method returns an
                 * observable of actions. Thus, when the data are retrieved from the server, there are actions that both
                 * add the items to the store and set the filter value in the store. When they are retrieved from the
                 * store, the action that is next()'d to the caller only updates the filter value in the store. Because
                 * the selectors use the filter values in the store, it all works out. It was discovered in the process
                 * of development that changing the content of the history store from could within a select() from the
                 * history store causes extremely strange things to happen. I'm sure this is a general rule with ngrx/store:
                 * Any code within a select() must absolutely not generate side effects that change the result of
                 * the select(). Think of this as a variation on the strange things that happen when you try to modify an
                 * array within a loop that is iterating that same array.
                 * @param {GobiiExtractFilterType} gobiiExtractFilterType
                 * @param {FilterParams} filterParamsToLoad
                 * @param {string} filterValue
                 * @param {boolean} recurse
                 * @returns {Observable<LoadFileItemListWithFilterAction>}
                 */
                FileItemService.prototype.makeFileItemActionsFromNameIds = function (gobiiExtractFilterType, filterParamsToLoad, filterValue, recurse) {
                    var _this = this;
                    return Observable_1.Observable.create(function (observer) {
                        // if (filterParamsToLoad.getIsDynamicFilterValue()) {
                        //     filterParamsToLoad.setRelatedEntityFilterValue(filterValue);
                        // }
                        /***
                         * In the next refactoring we probably want to use the last modified call
                         * that gives the datetime stamps for _all_entities, because I'm sure the setup/teardown of the
                         * http request is a lot more expensive, cumulatively, than the extra payload. The trick there is that we'd need to cache the
                         * entire set of lastmodified dates in such a way that they would get refreshed at the start of a "transaction,"
                         * where a transaction for this purpose is ill-defined. Does it mean when we switch from one extract type to another?
                         * There is actually a way to do this when the export type tabs are controlled by the angular router, because for a
                         * given navigational path transition we could reset the server last modified dates in the store. For that matter,
                         * with that model, the update of the items in the store could be driven from when the collection of lastmodified
                         * dates are modified in the store: that is, when you switch from one extract type to another (or some other
                         * consequential UI event), you dispatch a fresh collection of lastmodified dates, one per each entity-filter type to
                         * the store. There is then an effect from that dispatch action that iterates all the query filters: if the
                         * lastmodified for a given filter's entity type has changed, the effect requests a refresh of the filter's items.
                         */
                        _this.entityStatsService.get(new dto_request_item_entity_stats_1.DtoRequestItemEntityStats(dto_request_item_entity_stats_1.EntityRequestType.LasetUpdated, filterParamsToLoad.getEntityType(), null, null))
                            .subscribe(function (entityStats) {
                            _this.store.select(fromRoot.getFiltersRetrieved)
                                .subscribe(function (filterHistoryItems) {
                                var fileHistoryItem = filterHistoryItems.find(function (fhi) {
                                    return fhi.gobiiExtractFilterType === filterParamsToLoad.getGobiiExtractFilterType()
                                        && fhi.filterId === filterParamsToLoad.getQueryName()
                                        && fhi.filterValue === filterValue;
                                });
                                var disregardDateSensitiveQueryingForNow = false;
                                if (disregardDateSensitiveQueryingForNow ||
                                    ((!fileHistoryItem) ||
                                        (entityStats.lastModified > fileHistoryItem.entityLasteUpdated))) {
                                    // Either the data have never been retrieved at all for a given filter value,
                                    // or the server-side entity has been updated. So we shall refresh the
                                    // data and dispatch both the new filter value and the
                                    //BEGIN: nameIdService.get()
                                    _this.nameIdService.get(filterParamsToLoad, filterValue)
                                        .subscribe(function (nameIds) {
                                        var minEntityLastUpdated;
                                        var fileItems = [];
                                        if (nameIds && (nameIds.length > 0)) {
                                            nameIds.forEach(function (nameIdItem) {
                                                var entityRelation = null;
                                                // the server method for the particular nameid retrieval in question will have had
                                                // to have added the fk entity type and id value
                                                if (nameIdItem.fkEntityType && nameIdItem.fkId) {
                                                    var gobiiFileItemCompoundUniqueId = null;
                                                    if (filterParamsToLoad.getParentFileItemParams()) {
                                                        gobiiFileItemCompoundUniqueId = filterParamsToLoad.getParentFileItemParams().getTargetEtityUniqueId();
                                                        entityRelation = gobii_file_item_entity_relation_1.GobiiFileItemEntityRelation
                                                            .fromGobiiFileItemCompoundId(gobiiFileItemCompoundUniqueId)
                                                            .setRelatedEntityId(nameIdItem.fkId);
                                                    }
                                                    /* else {
                                                                                                               gobiiFileItemCompoundUniqueId = new GobiiFileItemCompoundId(ExtractorItemType.ENTITY,
                                                                                                                   nameIdItem.fkEntityType,
                                                                                                                   EntitySubType.UNKNOWN,
                                                                                                                   CvFilterType.UNKNOWN,
                                                                                                                   null);
                                                                                                           }*/
                                                }
                                                var currentFileItem = gobii_file_item_1.GobiiFileItem.build(gobiiExtractFilterType, type_process_1.ProcessType.CREATE)
                                                    .setExtractorItemType(type_extractor_item_1.ExtractorItemType.ENTITY)
                                                    .setEntityType(filterParamsToLoad.getEntityType())
                                                    .setEntitySubType(filterParamsToLoad.getEntitySubType())
                                                    .setCvFilterType(filterParamsToLoad.getCvFilterType())
                                                    .setItemId(nameIdItem.id)
                                                    .setItemName(nameIdItem.name)
                                                    //.setSelected(false)
                                                    .setRequired(false)
                                                    .setParentItemId(filterValue)
                                                    .setIsExtractCriterion(filterParamsToLoad.getIsExtractCriterion())
                                                    .withRelatedEntity(entityRelation);
                                                fileItems.push(currentFileItem);
                                            });
                                            minEntityLastUpdated = new Date(Math.min.apply(null, nameIds
                                                .map(function (nameId) { return nameId.entityLasetModified; })));
                                            var temp = "foo";
                                            temp = "bar";
                                            if (filterParamsToLoad.getMameIdLabelType() != name_id_label_type_1.NameIdLabelType.UNKNOWN) {
                                                var entityName = "";
                                                if (filterParamsToLoad.getCvFilterType() !== cv_filter_type_1.CvFilterType.UNKNOWN) {
                                                    entityName += entity_labels_1.Labels.instance().cvFilterNodeLabels[filterParamsToLoad.getCvFilterType()];
                                                }
                                                else if (filterParamsToLoad.getEntitySubType() !== type_entity_1.EntitySubType.UNKNOWN) {
                                                    entityName += entity_labels_1.Labels.instance().entitySubtypeNodeLabels[filterParamsToLoad.getEntitySubType()];
                                                }
                                                else {
                                                    entityName += entity_labels_1.Labels.instance().entityNodeLabels[filterParamsToLoad.getEntityType()];
                                                }
                                                var label = "";
                                                switch (filterParamsToLoad.getMameIdLabelType()) {
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
                                                        _this.store.dispatch(new historyAction.AddStatusAction(new dto_header_status_message_1.HeaderStatusMessage("Unknown label type "
                                                            + name_id_label_type_1.NameIdLabelType[filterParamsToLoad.getMameIdLabelType()], null, null)));
                                                }
                                                var labelFileItem = gobii_file_item_1.GobiiFileItem
                                                    .build(gobiiExtractFilterType, type_process_1.ProcessType.CREATE)
                                                    .setEntityType(filterParamsToLoad.getEntityType())
                                                    .setEntitySubType(filterParamsToLoad.getEntitySubType())
                                                    .setCvFilterType(filterParamsToLoad.getCvFilterType())
                                                    .setExtractorItemType(type_extractor_item_1.ExtractorItemType.LABEL)
                                                    .setItemName(label)
                                                    .setIsExtractCriterion(filterParamsToLoad.getIsExtractCriterion())
                                                    .setParentItemId(filterValue)
                                                    .setItemId("0");
                                                fileItems.unshift(labelFileItem);
                                                //.selectedFileItemId = "0";
                                            }
                                        }
                                        //With reference to GSD-70, Not loading None type just for marker groups.
                                        if (filterParamsToLoad.getQueryName() !== file_item_param_names_1.FilterParamNames.MARKER_GROUPS) {
                                            var noneFileItem = gobii_file_item_1.GobiiFileItem
                                                .build(gobiiExtractFilterType, type_process_1.ProcessType.DUMMY)
                                                .setExtractorItemType(type_extractor_item_1.ExtractorItemType.ENTITY)
                                                .setEntityType(filterParamsToLoad.getEntityType())
                                                .setItemId(_this.NONE_ITEM_ITEM_ID)
                                                .setItemName("<none>")
                                                .setIsExtractCriterion(filterParamsToLoad.getIsExtractCriterion())
                                                .setParentItemId(filterValue);
                                            fileItems.push(noneFileItem);
                                        }
                                        var parentId = fileItems[0].getItemId();
                                        //filterParamsToLoad.setTargetEntityFilterValue(parentId && +parentId > 0 ? parentId : null);
                                        var targetEntityFilterValue = parentId && +parentId > 0 ? parentId : null;
                                        var loadAction = new fileItemActions.LoadFileItemListWithFilterAction({
                                            gobiiFileItems: fileItems,
                                            filterId: filterParamsToLoad.getQueryName(),
                                            filter: new action_payload_filter_1.PayloadFilter(gobiiExtractFilterType, filterParamsToLoad.getTargetEtityUniqueId(), filterParamsToLoad.getRelatedEntityUniqueId(), filterValue, targetEntityFilterValue, minEntityLastUpdated, null)
                                        });
                                        observer.next(loadAction);
                                        // if there are children, we will load their data as well
                                        if (recurse) {
                                            if (filterParamsToLoad
                                                .getChildFileItemParams()
                                                .filter(function (rqp) { return rqp.getFilterType() === filter_type_1.FilterType.NAMES_BY_TYPEID; })
                                                .length > 0) {
                                                for (var idx = 0; idx < filterParamsToLoad.getChildFileItemParams().length; idx++) {
                                                    var rqp = filterParamsToLoad.getChildFileItemParams()[idx];
                                                    if (rqp.getFilterType() === filter_type_1.FilterType.NAMES_BY_TYPEID) {
                                                        //rqp.setRelatedEntityFilterValue(parentId);
                                                        _this.makeFileItemActionsFromNameIds(gobiiExtractFilterType, rqp, parentId, true)
                                                            .subscribe(function (fileItems) { return observer.next(fileItems); });
                                                    }
                                                }
                                                ;
                                            }
                                        } // if we are recursing
                                    }, // Observer=>next
                                    function (// Observer=>next
                                    responseHeader) {
                                        _this.store.dispatch(new historyAction.AddStatusAction(responseHeader));
                                    }); // subscribe
                                }
                                else {
                                    // The data for given filter value exist and do not not need to be
                                    // updated. So here we shall dispatch only the new filter value.
                                    // This action will not update the filter history -- only the filter value
                                    //BEGIN: nameIdService.get()
                                    var loadAction = new fileItemActions.LoadFilterAction({
                                        filterId: filterParamsToLoad.getQueryName(),
                                        filter: new action_payload_filter_1.PayloadFilter(gobiiExtractFilterType, filterParamsToLoad.getTargetEtityUniqueId(), filterParamsToLoad.getRelatedEntityUniqueId(), filterValue, null, fileHistoryItem.entityLasteUpdated, null)
                                    });
                                    observer.next(loadAction);
                                    if (recurse) {
                                        if (filterParamsToLoad
                                            .getChildFileItemParams()
                                            .filter(function (rqp) { return rqp.getFilterType() === filter_type_1.FilterType.NAMES_BY_TYPEID; })
                                            .length > 0) {
                                            // we need to set the current filter in state, but with respect to
                                            // gobiiFileItems, it should be a null op
                                            //Because we don't have the data freshly from the sever, we shall need
                                            //to get the "parentId" from the file items we have in the store
                                            _this.store.select(fromRoot.getAllFileItems)
                                                .subscribe(function (allFileItems) {
                                                // Get the parent item id from the store;
                                                // however, this will only work if the parent items have been loaded
                                                // already.
                                                var candidateParentFileItems = allFileItems.filter(function (fi) {
                                                    return filterParamsToLoad.getTargetEtityUniqueId().compoundIdeEquals(fi)
                                                        && fi.getParentItemId() === filterValue;
                                                });
                                                var childItemsFilterValue = null;
                                                if (candidateParentFileItems.length > 0) {
                                                    childItemsFilterValue = candidateParentFileItems[0].getItemId();
                                                }
                                                for (var idx = 0; idx < filterParamsToLoad.getChildFileItemParams().length; idx++) {
                                                    var rqp = filterParamsToLoad.getChildFileItemParams()[idx];
                                                    if (rqp.getFilterType() === filter_type_1.FilterType.NAMES_BY_TYPEID) {
                                                        //rqp.setRelatedEntityFilterValue(childItemsFilterValue);
                                                        _this.makeFileItemActionsFromNameIds(gobiiExtractFilterType, rqp, childItemsFilterValue, true)
                                                            .subscribe(function (fileItems) { return observer.next(fileItems); });
                                                    }
                                                }
                                            }).unsubscribe(); //select all file items
                                        } // if we have child filters
                                    } // if we are recursing
                                } // if-else we need to refresh from server or rely on what's in the store already
                                //END: nameIdService.get()
                            })
                                .unsubscribe(); // get filter history items
                        }); //subscribe get entity stats
                    }); //return Observer.create
                }; // make file items from query
                FileItemService.prototype.recurseFilters = function (gobiiExtractFilterType, filterParamsToLoad, parentFilterValue, recurse) {
                    var _this = this;
                    return Observable_1.Observable.create(function (observer) {
                        // if (filterParamsToLoad.getRelatedEntityFilterValue() !== parentFilterValue) {
                        //     filterParamsToLoad.setRelatedEntityFilterValue(parentFilterValue);
                        //     filterParamsToLoad.setTargetEntityFilterValue(null);
                        // }
                        var loadAction = new fileItemActions.LoadFilterAction({
                            filterId: filterParamsToLoad.getQueryName(),
                            filter: new action_payload_filter_1.PayloadFilter(gobiiExtractFilterType, filterParamsToLoad.getTargetEtityUniqueId(), filterParamsToLoad.getRelatedEntityUniqueId(), parentFilterValue, null, null, //not sure about this
                            null)
                        });
                        observer.next(loadAction);
                        if (recurse) {
                            if (filterParamsToLoad
                                .getChildFileItemParams()
                                //.filter(rqp => rqp.getFilterType() === FilterType.NAMES_BY_TYPEID)
                                .length > 0) {
                                // we need to set the current filter in state, but with respect to
                                // gobiiFileItems, it should be a null op
                                //Because we don't have the data freshly from the sever, we shall need
                                //to get the "parentId" from the file items we have in the store
                                _this.store.select(fromRoot.getAllFileItems)
                                    .subscribe(function (allFileItems) {
                                    // Get the parent item id from the store;
                                    // however, this will only work if the parent items have been loaded
                                    // already.
                                    //let's make everything 100% explicit. Our comments here will use the example of
                                    //filtering projects by selected contact
                                    // these variables are redundant but I want to make this 100% unambigous what it means
                                    // and how we are using it
                                    // For example, the coupound unique ID for Contacts
                                    var parentEntityCompoundUniqueId = _this.filterParamsColl.getFilter(filterParamsToLoad.getParentFileItemParams().getQueryName(), gobiiExtractFilterType).getTargetEtityUniqueId();
                                    // for example, filter to only those file items that:
                                    // 1) have the compoundUniqueId of the items we are loading (project), and
                                    // 2) have an entity relation that has:
                                    //      a) the compoundUniqueId of the parentEntity (contact)
                                    //      b) the relatedEntityId of the parentFilterValue (the contactId) (
                                    var candidateParentFileItems = allFileItems.filter(function (fi) {
                                        return filterParamsToLoad.getTargetEtityUniqueId().compoundIdeEquals(fi)
                                            && fi.getRelatedEntityFilterValue(parentEntityCompoundUniqueId) === parentFilterValue
                                            && fi.getItemId() !== _this.NONE_ITEM_ITEM_ID;
                                    });
                                    // Now we will set the child filter's fkEntityValue to whatever happens to be the first item
                                    // in the list to which we filtered
                                    var childItemsFilterValue = null;
                                    if (candidateParentFileItems.length > 0) {
                                        childItemsFilterValue = candidateParentFileItems[0].getItemId();
                                    }
                                    for (var idx = 0; idx < filterParamsToLoad.getChildFileItemParams().length; idx++) {
                                        var rqp = filterParamsToLoad.getChildFileItemParams()[idx];
                                        //                                if (rqp.getFilterType() === FilterType.NAMES_BY_TYPEID) {
                                        //rqp.setRelatedEntityFilterValue(childItemsFilterValue);
                                        //rqp.setTargetEntityFilterValue(null); //the parent selection invalidates the previous target selection
                                        _this.recurseFilters(gobiiExtractFilterType, rqp, childItemsFilterValue, true)
                                            .subscribe(function (fileItems) { return observer.next(fileItems); });
                                        //                                }
                                    }
                                }).unsubscribe(); //select all file items
                            } // if we have child filters
                        } // if we are recursing
                    }); //return Observer.create
                }; // recurseFilters()
                FileItemService.prototype.loadEntityList = function (gobiiExtractFilterType, fileItemParamName) {
                    var _this = this;
                    var fileItemParams = this.filterParamsColl.getFilter(fileItemParamName, gobiiExtractFilterType);
                    if (fileItemParams && fileItemParams.getFilterType() === filter_type_1.FilterType.ENTITY_LIST) {
                        this.makeFileItemActionsFromEntities(gobiiExtractFilterType, fileItemParams, null, false)
                            .subscribe(function (action) {
                            if (action) {
                                _this.store.dispatch(action);
                            }
                        });
                    }
                }; // loadEntityList()
                FileItemService.prototype.loadPagedEntityList = function (gobiiExtractFilterType, fileItemParamName, paedQueryId, pageSize, pageNum) {
                    var _this = this;
                    var fileItemParams = this.filterParamsColl.getFilter(fileItemParamName, gobiiExtractFilterType);
                    if (fileItemParams.getIsPaged()) {
                        fileItemParams.setPageSize(pageSize);
                        fileItemParams.setPageNum(pageNum);
                        fileItemParams.setPagedQueryId(paedQueryId);
                        if (fileItemParams && fileItemParams.getFilterType() === filter_type_1.FilterType.ENTITY_LIST) {
                            this.makeFileItemActionsFromEntities(gobiiExtractFilterType, fileItemParams, null, false)
                                .subscribe(function (action) {
                                if (action) {
                                    _this.store.dispatch(action);
                                }
                            });
                        }
                    }
                    else {
                        this.store.dispatch(new historyAction.AddStatusAction(new dto_header_status_message_1.HeaderStatusMessage("This filter does not support paging: " + fileItemParamName, type_status_level_1.StatusLevel.ERROR, null)));
                    }
                }; // loadEntityList()
                FileItemService.prototype.makeFileItemActionsFromEntities = function (gobiiExtractFilterType, filterParams, filterValue, recurse) {
                    var _this = this;
                    return Observable_1.Observable.create(function (observer) {
                        try {
                            // if (filterParams.getIsDynamicFilterValue()) {
                            //     filterParams.setRelatedEntityFilterValue(filterValue);
                            // }
                            // note that this method does not do any of the entity dating and checking
                            // thing. It needs to be reworked for paging so that the filter ID also takes
                            // into account the current page -- i.e., so that the datetime stamp pertains to the
                            // specific page. This is going to require some refactoring.
                            if (filterParams.getFilterType() === filter_type_1.FilterType.ENTITY_LIST) {
                                var dtoRequestItem = filterParams.getDtoRequestItem();
                                var dtoRequestService = filterParams.getDtoRequestService();
                                dtoRequestService
                                    .get(dtoRequestItem)
                                    .subscribe(function (entityResult) {
                                    var pagination = null;
                                    var entityItems = [];
                                    if (filterParams.getIsPaged()) {
                                        entityItems = entityResult.gobiiFileItems;
                                        pagination = entityResult.pagination;
                                    }
                                    else {
                                        entityItems = entityResult;
                                    }
                                    entityItems.forEach(function (fi) {
                                        fi.setGobiiExtractFilterType(gobiiExtractFilterType);
                                    });
                                    var date = new Date();
                                    var loadAction = new fileItemActions.LoadFileItemListWithFilterAction({
                                        gobiiFileItems: entityItems,
                                        filterId: filterParams.getQueryName(),
                                        filter: new action_payload_filter_1.PayloadFilter(gobiiExtractFilterType, filterParams.getTargetEtityUniqueId(), filterParams.getRelatedEntityUniqueId(), filterValue, filterValue, date, pagination)
                                    });
                                    observer.next(loadAction);
                                }, function (responseHeader) {
                                    _this.store.dispatch(new historyAction.AddStatusAction(responseHeader));
                                });
                            }
                            else {
                                var extractFilterTypeString = "undefined";
                                if (gobiiExtractFilterType) {
                                    extractFilterTypeString = type_extractor_filter_1.GobiiExtractFilterType[gobiiExtractFilterType];
                                }
                                _this.store.dispatch(new historyAction.AddStatusMessageAction("FileItemParams "
                                    + filterParams.getQueryName()
                                    + " for extract type "
                                    + extractFilterTypeString
                                    + " is not of type "
                                    + filter_type_1.FilterType[filter_type_1.FilterType.ENTITY_LIST]));
                            } // if else filterParams are correct
                        }
                        catch (error) {
                            _this.store.dispatch(new historyAction.AddStatusAction(error));
                        }
                    }); // Observer.create()
                }; //makeFileItemActionsFromEntities()
                FileItemService = __decorate([
                    core_1.Injectable(),
                    __metadata("design:paramtypes", [name_id_service_1.NameIdService,
                        dto_request_service_1.DtoRequestService,
                        dto_request_service_1.DtoRequestService,
                        store_1.Store,
                        filter_params_coll_1.FilterParamsColl])
                ], FileItemService);
                return FileItemService;
            }());
            exports_1("FileItemService", FileItemService);
        }
    };
});
//# sourceMappingURL=file-item-service.js.map