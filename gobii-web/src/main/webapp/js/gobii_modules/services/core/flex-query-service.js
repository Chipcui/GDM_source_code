System.register(["@angular/core", "../../model/type-extractor-filter", "../../store/actions/history-action", "../../store/actions/fileitem-action", "../../store/reducers", "@ngrx/store", "./dto-request.service", "../../model/vertex-filter", "./entity-file-item-service", "../app/dto-request-item-vertex-filter", "../../model/gobii-file-item", "../../model/type-process", "../../model/type-extractor-item", "../../store/actions/action-payload-filter", "./filter-params-coll", "../../model/gobii-file-item-compound-id", "../../model/type-entity", "../../model/name-id-label-type", "../../model/cv-group", "./filter-service", "./tree-structure-service"], function (exports_1, context_1) {
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
    var core_1, type_extractor_filter_1, historyAction, fileItemActions, fromRoot, store_1, dto_request_service_1, vertex_filter_1, entity_file_item_service_1, dto_request_item_vertex_filter_1, gobii_file_item_1, type_process_1, type_extractor_item_1, action_payload_filter_1, filter_params_coll_1, gobii_file_item_compound_id_1, type_entity_1, name_id_label_type_1, cv_group_1, filter_service_1, tree_structure_service_1, FlexQueryService;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
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
            function (dto_request_service_1_1) {
                dto_request_service_1 = dto_request_service_1_1;
            },
            function (vertex_filter_1_1) {
                vertex_filter_1 = vertex_filter_1_1;
            },
            function (entity_file_item_service_1_1) {
                entity_file_item_service_1 = entity_file_item_service_1_1;
            },
            function (dto_request_item_vertex_filter_1_1) {
                dto_request_item_vertex_filter_1 = dto_request_item_vertex_filter_1_1;
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
            function (action_payload_filter_1_1) {
                action_payload_filter_1 = action_payload_filter_1_1;
            },
            function (filter_params_coll_1_1) {
                filter_params_coll_1 = filter_params_coll_1_1;
            },
            function (gobii_file_item_compound_id_1_1) {
                gobii_file_item_compound_id_1 = gobii_file_item_compound_id_1_1;
            },
            function (type_entity_1_1) {
                type_entity_1 = type_entity_1_1;
            },
            function (name_id_label_type_1_1) {
                name_id_label_type_1 = name_id_label_type_1_1;
            },
            function (cv_group_1_1) {
                cv_group_1 = cv_group_1_1;
            },
            function (filter_service_1_1) {
                filter_service_1 = filter_service_1_1;
            },
            function (tree_structure_service_1_1) {
                tree_structure_service_1 = tree_structure_service_1_1;
            }
        ],
        execute: function () {
            FlexQueryService = (function () {
                function FlexQueryService(store, entityFileItemService, dtoRequestServiceVertexFilterDTO, filterParamsColl, filterService, treeStructureService) {
                    this.store = store;
                    this.entityFileItemService = entityFileItemService;
                    this.dtoRequestServiceVertexFilterDTO = dtoRequestServiceVertexFilterDTO;
                    this.filterParamsColl = filterParamsColl;
                    this.filterService = filterService;
                    this.treeStructureService = treeStructureService;
                }
                FlexQueryService.prototype.loadVertices = function (filterParamNames) {
                    this.entityFileItemService.loadEntityList(type_extractor_filter_1.GobiiExtractFilterType.FLEX_QUERY, filterParamNames);
                }; // loadVertices()
                FlexQueryService.prototype.loadSelectedVertexFilter = function (filterParamsName, vertexId, entityType, entitySubType, cvGroup, cvTerm) {
                    var filterParams = this.filterParamsColl.getFilter(filterParamsName, type_extractor_filter_1.GobiiExtractFilterType.FLEX_QUERY);
                    // the filterParams passed in should exist
                    if (!filterParams) {
                        this.store.dispatch(new historyAction.AddStatusMessageAction("Error loading filter: there is no query params object for query "
                            + filterParamsName
                            + " with extract filter type "
                            + type_extractor_filter_1.GobiiExtractFilterType.FLEX_QUERY[type_extractor_filter_1.GobiiExtractFilterType.FLEX_QUERY]));
                    }
                    while (filterParams) {
                        filterParams.getTargetEntityUniqueId().setEntityType(entityType);
                        filterParams.getTargetEntityUniqueId().setEntitySubType(entitySubType);
                        filterParams.getTargetEntityUniqueId().setCvGroup(cvGroup);
                        filterParams.getTargetEntityUniqueId().setCvTerm(cvTerm);
                        var targetFilterloadAction = new fileItemActions.LoadFilterAction({
                            filterId: filterParams.getQueryName(),
                            filter: new action_payload_filter_1.PayloadFilter(type_extractor_filter_1.GobiiExtractFilterType.FLEX_QUERY, filterParams.getTargetEntityUniqueId(), filterParams.getRelatedEntityUniqueId(), null, vertexId, null, null)
                        });
                        this.store.dispatch(targetFilterloadAction);
                        this.treeStructureService.updateTreeNode(type_extractor_filter_1.GobiiExtractFilterType.FLEX_QUERY, filterParams.getTargetEntityUniqueId(), new gobii_file_item_compound_id_1.GobiiFileItemCompoundId()
                            .setExtractorItemType(type_extractor_item_1.ExtractorItemType.VERTEX_VALUE)
                            .setEntityType(entityType)
                            .setEntitySubType(entitySubType)
                            .setCvGroup(cvGroup)
                            .setCvTerm(cvTerm));
                        // propagate null filter to child
                        if (!vertexId
                            && filterParams.getChildFileItemParams()
                            && filterParams.getChildFileItemParams().length > 0) {
                            var childFilterParams = filterParams.getChildFileItemParams()[0];
                            // clear any selected nodes from selected items collection and from tree
                            this.deSelectVertexValueFilters(childFilterParams.getTargetEntityUniqueId());
                            var childFilterLoadAction = new fileItemActions.LoadFilterAction({
                                filterId: childFilterParams.getQueryName(),
                                filter: new action_payload_filter_1.PayloadFilter(type_extractor_filter_1.GobiiExtractFilterType.FLEX_QUERY, childFilterParams.getTargetEntityUniqueId().setEntityType(type_entity_1.EntityType.UNKNOWN), childFilterParams.getRelatedEntityUniqueId(), null, vertexId, null, null)
                            });
                            this.store.dispatch(childFilterLoadAction);
                        } // if the vertexId is null
                        // if the current filter is getting nulled, we need to null the siblings as well
                        // but we dont' need to cascade filter values here
                        // note that for now this is only really relevant to FlexQuery filters
                        if (!vertexId) {
                            filterParams = filterParams.getNextSiblingFileItemParams();
                        }
                        else {
                            filterParams = null;
                        }
                    } // while we have another filter value
                };
                FlexQueryService.prototype.loadSelectedVertexValueFilters = function (filterParamsName, currentValuesGfis, previousValuesGfis) {
                    var _this = this;
                    var vertexValues = currentValuesGfis.map(function (gfi) { return gfi.getItemId(); });
                    var vertexValueIdsCsv = null;
                    var filterParams = this.filterParamsColl.getFilter(filterParamsName, type_extractor_filter_1.GobiiExtractFilterType.FLEX_QUERY);
                    if (filterParams && vertexValues && vertexValues.length > 0) {
                        vertexValueIdsCsv = "";
                        vertexValues.forEach(function (vv) { return vertexValueIdsCsv += vv + ","; });
                    } // if we have new vertex values
                    previousValuesGfis.forEach(function (gfi) {
                        var loadAction = new fileItemActions.RemoveFromExtractAction(gfi);
                        _this.store.dispatch(loadAction);
                    });
                    currentValuesGfis.forEach(function (gfi) {
                        var loadAction = new fileItemActions.LoadFileItemtAction({
                            gobiiFileItem: gfi,
                            selectForExtract: true
                        });
                        _this.store.dispatch(loadAction);
                    });
                    this.filterService.loadFilter(type_extractor_filter_1.GobiiExtractFilterType.FLEX_QUERY, filterParamsName, vertexValueIdsCsv);
                    // let gobiiTreeNodes: GobiiTreeNode[] = currentValuesGfis
                    //     .map(gfi => this.treeStructureService.makeTreeNodeFromFileItem(gfi));
                    //
                    // gobiiTreeNodes.forEach(gtn => {
                    //     gtn.setSequenceNum(filterParams.getSequenceNum());
                    //     gtn.setItemType(ExtractorItemType.VERTEX_VALUE);
                    //     // gtn.setItemType(ExtractorItemType.VERTEX); // the three node we're adding has to be of type VERTEX
                    //     //                                            // in order to added to the VERTEX nodes
                    //     //                                            // this is probably bad
                    // });
                    //
                    // gobiiTreeNodes.forEach(tn => {
                    //     this.store.dispatch(new treeNodeActions.PlaceTreeNodeAction(tn));
                    // });
                };
                FlexQueryService.prototype.loadVertexValues = function (jobId, vertexFileItem, filterParamName) {
                    var _this = this;
                    //        return Observable.create(observer => {
                    var filterParams = this.filterParamsColl.getFilter(filterParamName, type_extractor_filter_1.GobiiExtractFilterType.FLEX_QUERY);
                    if (vertexFileItem.getNameIdLabelType() == name_id_label_type_1.NameIdLabelType.UNKNOWN) {
                        var targetVertex_1 = vertexFileItem.getEntity();
                        var vertexFilterDTO = new vertex_filter_1.VertexFilterDTO(targetVertex_1, [], [], null, null);
                        var vertexFilterDtoResponse_1 = null;
                        this.dtoRequestServiceVertexFilterDTO.post(new dto_request_item_vertex_filter_1.DtoRequestItemVertexFilterDTO(vertexFilterDTO, jobId, false)).subscribe(function (vertexFilterDto) {
                            vertexFilterDtoResponse_1 = vertexFilterDto;
                            var vertexFileItems = [];
                            vertexFilterDto.vertexValues.forEach(function (item) {
                                var currentFileItem = gobii_file_item_1.GobiiFileItem.build(type_extractor_filter_1.GobiiExtractFilterType.FLEX_QUERY, type_process_1.ProcessType.CREATE)
                                    .setExtractorItemType(type_extractor_item_1.ExtractorItemType.VERTEX_VALUE)
                                    .setEntityType(targetVertex_1.entityType)
                                    .setItemId(item.id)
                                    .setItemName(item.name)
                                    .setRequired(false)
                                    .setSequenceNum(filterParams.getSequenceNum());
                                //.setParentItemId(filterValue)
                                //.setIsExtractCriterion(filterParamsToLoad.getIsExtractCriterion())
                                //.withRelatedEntity(entityRelation);
                                vertexFileItems.push(currentFileItem);
                            });
                            // for flex query the "filter value" is not an actual id but a new entity type
                            // our selectors "just know" to look for the filter's target entity type as the thing to filter on
                            var targetCompoundUniqueId = filterParams.getTargetEntityUniqueId();
                            targetCompoundUniqueId.setExtractorItemType(type_extractor_item_1.ExtractorItemType.VERTEX_VALUE);
                            targetCompoundUniqueId.setEntityType(targetVertex_1.entityType);
                            var loadAction = new fileItemActions.LoadFileItemListWithFilterAction({
                                gobiiFileItems: vertexFileItems,
                                filterId: filterParams.getQueryName(),
                                filter: new action_payload_filter_1.PayloadFilter(type_extractor_filter_1.GobiiExtractFilterType.FLEX_QUERY, targetCompoundUniqueId, filterParams.getRelatedEntityUniqueId(), null, null, null, null)
                            });
                            _this.store.dispatch(loadAction);
                            //observer.next(vertexFileItems);
                            //observer.complete();
                        }, function (headerResponse) {
                            headerResponse.status.statusMessages.forEach(function (statusMessage) {
                                _this.store.dispatch(new historyAction.AddStatusAction(statusMessage));
                            });
                            //observer.complete();
                        });
                    }
                    else {
                        this.store.dispatch(new fileItemActions.LoadFilterAction({
                            filterId: filterParamName,
                            filter: new action_payload_filter_1.PayloadFilter(type_extractor_filter_1.GobiiExtractFilterType.FLEX_QUERY, new gobii_file_item_compound_id_1.GobiiFileItemCompoundId(type_extractor_item_1.ExtractorItemType.VERTEX_VALUE, type_entity_1.EntityType.UNKNOWN, // effectively "null out" the selected entity type
                            type_entity_1.EntitySubType.UNKNOWN, cv_group_1.CvGroup.UNKNOWN, cv_group_1.getCvGroupName(cv_group_1.CvGroup.UNKNOWN)), null, null, null, null, null)
                        }));
                    } // if-else file item type was label
                    //} );//return observer create
                };
                FlexQueryService.prototype.deSelectVertexValueFilters = function (compoundUniquueId) {
                    var _this = this;
                    this.store.select(fromRoot.getSelectedFileItems)
                        .subscribe(function (gfi) {
                        var itemsToDeselect = gfi.filter(function (sgfi) { return sgfi.compoundIdeEquals(compoundUniquueId); });
                        itemsToDeselect.forEach(function (itr) {
                            var loadAction = new fileItemActions.RemoveFromExtractAction(itr);
                            _this.store.dispatch(loadAction);
                        });
                    }).unsubscribe();
                };
                FlexQueryService = __decorate([
                    core_1.Injectable(),
                    __metadata("design:paramtypes", [store_1.Store,
                        entity_file_item_service_1.EntityFileItemService,
                        dto_request_service_1.DtoRequestService,
                        filter_params_coll_1.FilterParamsColl,
                        filter_service_1.FilterService,
                        tree_structure_service_1.TreeStructureService])
                ], FlexQueryService);
                return FlexQueryService;
            }());
            exports_1("FlexQueryService", FlexQueryService);
        }
    };
});
//# sourceMappingURL=flex-query-service.js.map