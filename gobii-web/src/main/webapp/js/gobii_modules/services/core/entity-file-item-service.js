System.register(["@angular/core", "../../model/type-extractor-filter", "../../model/dto-header-status-message", "./name-id-service", "../../store/actions/history-action", "../../store/actions/fileitem-action", "@ngrx/store", "../../model/filter-type", "rxjs/Observable", "rxjs/add/operator/expand", "rxjs/add/operator/concat", "./dto-request.service", "./filter-params-coll", "../../model/type-status-level", "../../store/actions/action-payload-filter", "./filter-service"], function (exports_1, context_1) {
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
    var core_1, type_extractor_filter_1, dto_header_status_message_1, name_id_service_1, historyAction, fileItemActions, store_1, filter_type_1, Observable_1, dto_request_service_1, filter_params_coll_1, type_status_level_1, action_payload_filter_1, filter_service_1, EntityFileItemService;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
            },
            function (dto_header_status_message_1_1) {
                dto_header_status_message_1 = dto_header_status_message_1_1;
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
            function (store_1_1) {
                store_1 = store_1_1;
            },
            function (filter_type_1_1) {
                filter_type_1 = filter_type_1_1;
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
            function (filter_params_coll_1_1) {
                filter_params_coll_1 = filter_params_coll_1_1;
            },
            function (type_status_level_1_1) {
                type_status_level_1 = type_status_level_1_1;
            },
            function (action_payload_filter_1_1) {
                action_payload_filter_1 = action_payload_filter_1_1;
            },
            function (filter_service_1_1) {
                filter_service_1 = filter_service_1_1;
            }
        ],
        execute: function () {
            EntityFileItemService = (function () {
                function EntityFileItemService(nameIdService, entityStatsService, fileItemRequestService, filterService, store, filterParamsColl) {
                    // For non-hierarchically filtered request params, we just create them simply
                    // as we add them to the flat map
                    this.nameIdService = nameIdService;
                    this.entityStatsService = entityStatsService;
                    this.fileItemRequestService = fileItemRequestService;
                    this.filterService = filterService;
                    this.store = store;
                    this.filterParamsColl = filterParamsColl;
                } // constructor
                EntityFileItemService.prototype.loadEntityList = function (gobiiExtractFilterType, fileItemParamName) {
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
                EntityFileItemService.prototype.loadPagedEntityList = function (gobiiExtractFilterType, fileItemParamName, paedQueryId, pageSize, pageNum) {
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
                EntityFileItemService.prototype.makeFileItemActionsFromEntities = function (gobiiExtractFilterType, filterParams, filterValue, recurse) {
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
                                    var labelFileItem = _this.filterService.makeLabelItem(gobiiExtractFilterType, filterParams);
                                    if (labelFileItem) {
                                        labelFileItem.setExtractorItemType(filterParams.getExtractorItemType());
                                        entityItems.unshift(labelFileItem);
                                    }
                                    var date = new Date();
                                    var loadAction = new fileItemActions.LoadFileItemListWithFilterAction({
                                        gobiiFileItems: entityItems,
                                        filterId: filterParams.getQueryName(),
                                        filter: new action_payload_filter_1.PayloadFilter(gobiiExtractFilterType, filterParams.getTargetEntityUniqueId(), filterParams.getRelatedEntityUniqueId(), filterValue, filterValue, date, pagination),
                                        repalceByTarget: false
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
                EntityFileItemService = __decorate([
                    core_1.Injectable(),
                    __metadata("design:paramtypes", [name_id_service_1.NameIdService,
                        dto_request_service_1.DtoRequestService,
                        dto_request_service_1.DtoRequestService,
                        filter_service_1.FilterService,
                        store_1.Store,
                        filter_params_coll_1.FilterParamsColl])
                ], EntityFileItemService);
                return EntityFileItemService;
            }());
            exports_1("EntityFileItemService", EntityFileItemService);
        }
    };
});
//# sourceMappingURL=entity-file-item-service.js.map