System.register(["@angular/core", "../../model/type-extractor-filter", "../../store/actions/history-action", "../../store/actions/fileitem-action", "../../store/reducers", "@ngrx/store", "./dto-request.service", "../../model/vertex-filter", "./entity-file-item-service", "../../model/file-item-param-names", "../../model/vertex", "../app/dto-request-item-vertex-filter", "../../model/gobii-file-item", "../../model/type-process", "../../model/type-extractor-item", "../../store/actions/action-payload-filter", "./filter-params-coll", "../../model/gobii-file-item-compound-id", "../../model/type-entity", "../../model/name-id-label-type", "../../model/cv-group", "./filter-service", "./tree-structure-service", "../../model/type-vertex-name", "rxjs/Observable", "../../model/type-vertex"], function (exports_1, context_1) {
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
    var core_1, type_extractor_filter_1, historyAction, fileItemActions, fromRoot, store_1, dto_request_service_1, vertex_filter_1, entity_file_item_service_1, file_item_param_names_1, vertex_1, dto_request_item_vertex_filter_1, gobii_file_item_1, type_process_1, type_extractor_item_1, action_payload_filter_1, filter_params_coll_1, gobii_file_item_compound_id_1, type_entity_1, name_id_label_type_1, cv_group_1, filter_service_1, tree_structure_service_1, type_vertex_name_1, Observable_1, type_vertex_1, FlexQueryService;
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
            function (file_item_param_names_1_1) {
                file_item_param_names_1 = file_item_param_names_1_1;
            },
            function (vertex_1_1) {
                vertex_1 = vertex_1_1;
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
            },
            function (type_vertex_name_1_1) {
                type_vertex_name_1 = type_vertex_name_1_1;
            },
            function (Observable_1_1) {
                Observable_1 = Observable_1_1;
            },
            function (type_vertex_1_1) {
                type_vertex_1 = type_vertex_1_1;
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
                FlexQueryService.prototype.loadSelectedVertexFilter = function (eventedFilterParamsName, eventedVertexId, eventedEntityType, eventedEntitySubType, eventedCvGroup, eventedCvTerm, jobId, previousSelectionExisted) {
                    this.resetVertexFilters(eventedFilterParamsName, eventedVertexId, eventedEntityType, eventedEntitySubType, eventedCvGroup, eventedCvTerm, jobId);
                    // I am a bit uneasy about recalculating here. In theory, there is a race condition between dispatch of the actions
                    // performed in resetVertexFilters() and retrieving the filter values to do the count. The correct way to do this
                    // is to use an effect. I have now littered the file-item-effects.ts code with yet another attempt to call a web
                    // service (in this case the post() to the vertex service) and commented it out. I have made some progress since
                    // the last time I tried to do this: the core of the problem appears to be that within an observable chain, there
                    // is something I should be doing with the observable around which the http call is wrapped. I commented
                    // more about this where I have the code commented out and there's an article I found that might point int he
                    // direction of a solution.
                    // See FlexQueryFilterComponent::ngOnInit() for a note about another issue that
                    // might be resolved by calling the recount from an effect.
                    if (previousSelectionExisted) {
                        var currentVertexFilterParams = this.filterParamsColl.getFilter(eventedFilterParamsName, type_extractor_filter_1.GobiiExtractFilterType.FLEX_QUERY);
                        if (currentVertexFilterParams.getPreviousSiblingFileItemParams()
                            && currentVertexFilterParams.getPreviousSiblingFileItemParams().getChildFileItemParams().length > 0) {
                            this.recalcMarkerSampleCount(currentVertexFilterParams
                                .getPreviousSiblingFileItemParams()
                                .getChildFileItemParams()[0].getQueryName(), jobId);
                        }
                    }
                }; // end function
                /***
                 * Recall that in the FlexQuery universe of discourse, there are two focii of interest: vertices, and vertex values.
                 * Each of the four flex query controls consists of a list of vertices and a list of corresponding vertex values
                 * that is populated  when the user selects a vertex value. The tree is affected in two ways by by the control:
                 *  1) When a new value is selected, the node corresponding to the filter in the tree is updated to receive a name
                 *     that indicates the type (e.g., Analysis, Dataset, etc.);
                 *  2) When a user clicks a value from the value list, the selected value is added under the corresponding tree
                 *     node (e.g., if the user had selected Analysis as the vertex for filter 2, such that the Filter 2 node in the
                 *     the tree will now say "Filter 2: Analysis," when the user now selects "My fine analysis" from the value list,
                 *     it (and any others that are selected) will also be added under the "Filter 2: Aanlysis" node.
                 * The tree will be updated in this way as the user selects vertices and vertex values for each of the filters.
                 * When the user selects a new value on a flilter to the left of any other filter (i.e., filters 1 through 3),
                 * all the filter to right need to be "cleared" in the following way:
                 *  1) The filter's tree node needs to be reset for the newly selected entity;
                 *  2) The child filters of the filter's tree node need to be cleared;
                 *  3) The filter control's vertex selector needs to be reset to "Select A";
                 *  4) The filter control's vertex values need to be cleared.
                 * A word should be said here about the way that the FilterParam objects underlying this mechanism are organized.
                 * Each FlexQuery filter control is associated with two FilterParams object -- one for the vertices and another
                 * for the vertex values. The vertex values are arranged as a list, such that there are previous and next sibling
                 * methods: given a FilterParams instance, you can figure out whether you have a filter to the left or right.
                 * (FlexQuery so far is the only feature that uses sibling filter relationships). Each vertex filter params's object
                 * has a child filter, which is the filter for the vertex values.
                 * With all that as background, it will now be intelligible to explain that this method is called by the FlexQuery
                 * filter component when a vertex value change, and that it is responsible for ensuring that the filter control and
                 * tree behaviors described are carried out.
                 * This method conceives of two different vertices:
                 *      * the "evented" vertex is the one that was selected by the user's actions, and the values of which are passed
                 *        in as the method's parameter values;
                 *       * the "sibling" vertices are the ones to the right of the the evented vertex, as identified by vertex filter's
                 *         "next sibling"
                 * What this method does is as follows:
                 *   * For the evented vertex:
                 *      * Set the vertex's filter to the id of the evented vertex so that the vertex value list controlled
                 *        by that filter is reset (if the user selected "Select A", the vertexId will be null and so no vertex values
                 *        will be displayed;
                 *      * change the label of the filter node for the filter to the newly selected vertex value (or blank if null);
                 *    * For the sibling vertices:
                 *       * Set the vertex's filter to null;
                 *       * Clear the filter node's label
                 *   * For the evented and sibling vertices: remove the previously selected vertex values from the tree;
                 *
                 *   The algorithm is otherwise self-documenting. However, a few points should be kept in mind:
                 *      * We are using the FilterParam objects to maintain the filter's selected entity values, whilst the filter
                 *        value itself is kept in the store so that the selectors for the vertices and vertex value lists will work
                 *        out correctly; technically, all of the filter state should be kept the store. The reason it is not is because
                 *        consuming the store values in this context requir3es a method that generates a series of dispatch actions
                 *        that would increase the complexity of the method; for now, it seems to work; in the future, it may need to
                 *        be refactored;
                 *      * This method relies very heavily on the compound IDs for the entities. Pre FlexQuery functionality does not
                 *        have dynamic entity types and so did not need to operate in this way. It will be noded that the CompoundUnqiueID
                 *        class now has a "from" method to copy an existing one: in this method, we almost always want to make copies
                 *        in this way; otherwise, we are copying references to instances and that does not do what we want.
                 *
                 * @param {FilterParamNames} eventedFilterParamsName
                 * @param {string} eventedVertexId
                 * @param {EntityType} eventedEntityType
                 * @param {EntitySubType} eventedEntitySubType
                 * @param {CvGroup} eventedCvGroup
                 * @param {string} eventedCvTerm
                 */
                FlexQueryService.prototype.resetVertexFilters = function (eventedFilterParamsName, eventedVertexId, eventedEntityType, eventedEntitySubType, eventedCvGroup, eventedCvTerm, jobId) {
                    var currentVertexId = eventedVertexId;
                    var currentVertexFilterParams = this.filterParamsColl.getFilter(eventedFilterParamsName, type_extractor_filter_1.GobiiExtractFilterType.FLEX_QUERY);
                    // the filterParams passed in should exist
                    if (!currentVertexFilterParams) {
                        this.store.dispatch(new historyAction.AddStatusMessageAction("Error loading filter: there is no query params object for query "
                            + eventedFilterParamsName
                            + " with extract filter type "
                            + type_extractor_filter_1.GobiiExtractFilterType.FLEX_QUERY[type_extractor_filter_1.GobiiExtractFilterType.FLEX_QUERY]));
                    }
                    var previousTargetCompoundId = gobii_file_item_compound_id_1.GobiiFileItemCompoundId
                        .fromGobiiFileItemCompoundId(currentVertexFilterParams.getTargetEntityUniqueId());
                    var newVertexFilterTargetCompoundId = gobii_file_item_compound_id_1.GobiiFileItemCompoundId
                        .fromGobiiFileItemCompoundId(currentVertexFilterParams.getTargetEntityUniqueId())
                        .setEntityType(eventedEntityType)
                        .setEntitySubType(eventedEntitySubType)
                        .setCvGroup(eventedCvGroup)
                        .setCvTerm(eventedCvTerm);
                    var nullTargetCompoundIdOfVertex = gobii_file_item_compound_id_1.GobiiFileItemCompoundId
                        .fromGobiiFileItemCompoundId(currentVertexFilterParams.getTargetEntityUniqueId())
                        .setEntityType(type_entity_1.EntityType.UNKNOWN)
                        .setEntitySubType(type_entity_1.EntitySubType.UNKNOWN)
                        .setCvGroup(cv_group_1.CvGroup.UNKNOWN)
                        .setCvTerm(null);
                    var nullTargetCompoundIdOfVertexValue = gobii_file_item_compound_id_1.GobiiFileItemCompoundId
                        .fromGobiiFileItemCompoundId(nullTargetCompoundIdOfVertex)
                        .setExtractorItemType(type_extractor_item_1.ExtractorItemType.VERTEX_VALUE);
                    while (currentVertexFilterParams) {
                        // dispatch target entity ID values for newly selected vertex
                        currentVertexFilterParams.setTargetEntityUniqueId(newVertexFilterTargetCompoundId);
                        var loadFilterActionForVertex = new fileItemActions.LoadFilterAction({
                            filterId: currentVertexFilterParams.getQueryName(),
                            filter: new action_payload_filter_1.PayloadFilter(type_extractor_filter_1.GobiiExtractFilterType.FLEX_QUERY, newVertexFilterTargetCompoundId, currentVertexFilterParams.getRelatedEntityUniqueId(), null, currentVertexId, null, null)
                        });
                        this.store.dispatch(loadFilterActionForVertex);
                        // do the same for the filter's tree node
                        this.treeStructureService.updateTreeNode(type_extractor_filter_1.GobiiExtractFilterType.FLEX_QUERY, newVertexFilterTargetCompoundId, gobii_file_item_compound_id_1.GobiiFileItemCompoundId
                            .fromGobiiFileItemCompoundId(newVertexFilterTargetCompoundId)
                            .setExtractorItemType(type_extractor_item_1.ExtractorItemType.VERTEX_VALUE));
                        //reset the selected vertex's value list
                        if (currentVertexFilterParams.getChildFileItemParams()
                            && currentVertexFilterParams.getChildFileItemParams().length > 0) {
                            var currentVertexValuesFilterParams = currentVertexFilterParams.getChildFileItemParams()[0];
                            // clear any selected nodes from selected items collection and from tree
                            this.deSelectVertexValueFilters(gobii_file_item_compound_id_1.GobiiFileItemCompoundId
                                .fromGobiiFileItemCompoundId(previousTargetCompoundId)
                                .setExtractorItemType(type_extractor_item_1.ExtractorItemType.VERTEX_VALUE));
                            var loadFilterActionForVertexValue = new fileItemActions.LoadFilterAction({
                                filterId: currentVertexValuesFilterParams.getQueryName(),
                                filter: new action_payload_filter_1.PayloadFilter(type_extractor_filter_1.GobiiExtractFilterType.FLEX_QUERY, nullTargetCompoundIdOfVertexValue, currentVertexValuesFilterParams.getRelatedEntityUniqueId(), null, null, null, null)
                            });
                            this.store.dispatch(loadFilterActionForVertexValue);
                        } // if there is a values filter (child filter)
                        currentVertexFilterParams = currentVertexFilterParams.getNextSiblingFileItemParams();
                        if (currentVertexFilterParams) {
                            previousTargetCompoundId = currentVertexFilterParams.getTargetEntityUniqueId();
                            newVertexFilterTargetCompoundId = gobii_file_item_compound_id_1.GobiiFileItemCompoundId.fromGobiiFileItemCompoundId(nullTargetCompoundIdOfVertex)
                                .setSequenceNum(currentVertexFilterParams.getSequenceNum());
                        } // no else necessary because null currentVertexFilterParams will end execution
                        currentVertexId = null; // by definition, we always null out the children
                    } // while we have another filter value
                }; // end function
                FlexQueryService.prototype.loadSelectedVertexValueFilters = function (jobId, filterParamsName, newlySelectedValuesGfis, previousValuesGfis, targetValueVertex) {
                    var _this = this;
                    previousValuesGfis.forEach(function (gfi) {
                        var loadAction = new fileItemActions.RemoveFromExtractAction(gfi);
                        _this.store.dispatch(loadAction);
                    });
                    newlySelectedValuesGfis.forEach(function (gfi) {
                        var loadAction = new fileItemActions.LoadFileItemtAction({
                            gobiiFileItem: gfi,
                            selectForExtract: true
                        });
                        _this.store.dispatch(loadAction);
                    });
                    var targetFilterParams = this.filterParamsColl.getFilter(filterParamsName, type_extractor_filter_1.GobiiExtractFilterType.FLEX_QUERY);
                    if (targetFilterParams) {
                        targetFilterParams.setTargetEntityUniqueId(gobii_file_item_compound_id_1.GobiiFileItemCompoundId.fromGobiiFileItemCompoundId(targetValueVertex));
                    }
                    this.filterService.loadFilter(type_extractor_filter_1.GobiiExtractFilterType.FLEX_QUERY, filterParamsName, targetValueVertex);
                    // null out filters to the right
                    var nextSiblingFilter = this.filterParamsColl
                        .getFilter(filterParamsName, type_extractor_filter_1.GobiiExtractFilterType.FLEX_QUERY)
                        .getParentFileItemParams()
                        .getNextSiblingFileItemParams();
                    while (nextSiblingFilter) {
                        this.resetVertexFilters(nextSiblingFilter.getQueryName(), null, type_entity_1.EntityType.UNKNOWN, type_entity_1.EntitySubType.UNKNOWN, cv_group_1.CvGroup.UNKNOWN, null, jobId);
                        nextSiblingFilter = nextSiblingFilter.getParentFileItemParams() ?
                            nextSiblingFilter.getParentFileItemParams().getNextSiblingFileItemParams()
                            : null;
                    }
                    this.recalcMarkerSampleCount(filterParamsName, jobId);
                }; // function
                FlexQueryService.prototype.recalcMarkerSampleCount = function (filterParamsName, jobId) {
                    var _this = this;
                    // now get counts per current filter values
                    this.invalidateMarkerSampleCount(false);
                    this.getVertexFilters(filterParamsName)
                        .subscribe(function (vertexFiltersForCount) {
                        var dummyVertex = new vertex_1.Vertex(0, type_vertex_name_1.VertexNameType.MARKER, type_vertex_1.VertexType.ENTITY, "countonly", type_entity_1.EntityType.MARKER, type_entity_1.EntitySubType.UNKNOWN, cv_group_1.CvGroup.UNKNOWN, null, []);
                        var vertexFilterDTO = new vertex_filter_1.VertexFilterDTO(dummyVertex, // the server should ignore this because it's a count query
                        vertexFiltersForCount, [], null, null, null, null, null, null);
                        var vertexFilterDtoResponse = null;
                        _this.dtoRequestServiceVertexFilterDTO.post(new dto_request_item_vertex_filter_1.DtoRequestItemVertexFilterDTO(vertexFilterDTO, jobId, true)).subscribe(function (payloadReader) {
                            if (payloadReader.succeeded()) {
                                vertexFilterDtoResponse = payloadReader.getData();
                                var markerCountItem = gobii_file_item_1.GobiiFileItem
                                    .build(type_extractor_filter_1.GobiiExtractFilterType.FLEX_QUERY, type_process_1.ProcessType.CREATE)
                                    .setExtractorItemType(type_extractor_item_1.ExtractorItemType.ITEM_COUNT)
                                    .setEntityType(type_entity_1.EntityType.MARKER)
                                    .setItemName("Marker Count")
                                    .setEntity(vertexFilterDtoResponse.markerCount)
                                    .setIsEphemeral(false);
                                // default count items on load
                                var loadActionMarkerCount = new fileItemActions.LoadFileItemtAction({
                                    gobiiFileItem: markerCountItem,
                                    selectForExtract: true
                                });
                                _this.store.dispatch(loadActionMarkerCount);
                                var loadActionSampleCount = new fileItemActions.LoadFileItemtAction({
                                    gobiiFileItem: gobii_file_item_1.GobiiFileItem
                                        .build(type_extractor_filter_1.GobiiExtractFilterType.FLEX_QUERY, type_process_1.ProcessType.CREATE)
                                        .setExtractorItemType(type_extractor_item_1.ExtractorItemType.ITEM_COUNT)
                                        .setEntityType(type_entity_1.EntityType.DNA_SAMPLE)
                                        .setItemName("Sample Count")
                                        .setEntity(vertexFilterDtoResponse.sampleCount)
                                        .setIsEphemeral(false),
                                    selectForExtract: true
                                });
                                _this.store.dispatch(loadActionSampleCount);
                                console.log(jobId + ": marker count time is " + vertexFilterDtoResponse.markerCountMs + " ms.");
                                console.log(jobId + ": sample count time is " + vertexFilterDtoResponse.sampleCountMs + " ms.");
                                var markerResultFileName = vertexFilterDtoResponse.markerFileFqpn;
                                var markerResultFileItem = gobii_file_item_1.GobiiFileItem
                                    .build(type_extractor_filter_1.GobiiExtractFilterType.FLEX_QUERY, type_process_1.ProcessType.CREATE)
                                    .setExtractorItemType(type_extractor_item_1.ExtractorItemType.MARKER_RESULT_FILE)
                                    .setEntityType(type_entity_1.EntityType.MARKER)
                                    .setItemName("Marker file: " + markerResultFileName)
                                    .setEntity(markerResultFileName)
                                    .setIsEphemeral(false);
                                // default count items on load
                                var replaceMarkerResultFile = new fileItemActions.ReplaceItemOfSameCompoundIdAction({
                                    gobiiFileitemToReplaceWith: markerResultFileItem
                                });
                                _this.store.dispatch(replaceMarkerResultFile);
                                var sampleResultFileName = vertexFilterDtoResponse.sampleFileFqpn;
                                var sampleResultFileItem = gobii_file_item_1.GobiiFileItem
                                    .build(type_extractor_filter_1.GobiiExtractFilterType.FLEX_QUERY, type_process_1.ProcessType.CREATE)
                                    .setExtractorItemType(type_extractor_item_1.ExtractorItemType.SAMPLE_RESULT_FILE)
                                    .setEntityType(type_entity_1.EntityType.DNA_SAMPLE)
                                    .setItemName("Sample file: " + sampleResultFileName)
                                    .setEntity(sampleResultFileName)
                                    .setIsEphemeral(false);
                                // default count items on load
                                var replaceSampleResultFile = new fileItemActions.ReplaceItemOfSameCompoundIdAction({
                                    gobiiFileitemToReplaceWith: sampleResultFileItem
                                });
                                _this.store.dispatch(replaceSampleResultFile);
                            }
                            else {
                                _this.store.dispatch(new historyAction.AddStatusMessageAction("Error submitting extract insturctions: " +
                                    payloadReader.getError()));
                            }
                        });
                    }).unsubscribe();
                };
                FlexQueryService.prototype.getVertexFilters = function (vertexValuesFilterPararamName) {
                    var _this = this;
                    return Observable_1.Observable.create(function (observer) {
                        // the starting filter params must be for the controls that list the vertex values
                        if (vertexValuesFilterPararamName === file_item_param_names_1.FilterParamNames.FQ_F1_VERTEX_VALUES
                            || vertexValuesFilterPararamName === file_item_param_names_1.FilterParamNames.FQ_F2_VERTEX_VALUES
                            || vertexValuesFilterPararamName === file_item_param_names_1.FilterParamNames.FQ_F3_VERTEX_VALUES
                            || vertexValuesFilterPararamName === file_item_param_names_1.FilterParamNames.FQ_F4_VERTEX_VALUES) {
                            _this.store
                                .select(fromRoot.getFileItemsFilters)
                                .subscribe(function (filters) {
                                var filterVertices = [];
                                var filtterChildFilterParams = _this.filterParamsColl.getFilter(vertexValuesFilterPararamName, type_extractor_filter_1.GobiiExtractFilterType.FLEX_QUERY);
                                while (filtterChildFilterParams) {
                                    var vertexValueFilterFromState = filtterChildFilterParams ? filters[filtterChildFilterParams.getQueryName()] : null;
                                    if (vertexValueFilterFromState && vertexValueFilterFromState.targetEntityFilterValue) {
                                        var filterValuesFromState = vertexValueFilterFromState.targetEntityFilterValue;
                                        filterVertices.push(filterValuesFromState);
                                    } // if we found vertex value filter in state
                                    if (filtterChildFilterParams.getParentFileItemParams()
                                        && filtterChildFilterParams.getParentFileItemParams().getPreviousSiblingFileItemParams()
                                        && filtterChildFilterParams.getParentFileItemParams().getPreviousSiblingFileItemParams().getChildFileItemParams()
                                        && filtterChildFilterParams.getParentFileItemParams().getPreviousSiblingFileItemParams().getChildFileItemParams().length > 0) {
                                        filtterChildFilterParams = filtterChildFilterParams.getParentFileItemParams().getPreviousSiblingFileItemParams().getChildFileItemParams()[0];
                                    }
                                    else {
                                        filtterChildFilterParams = null;
                                    }
                                } // while there are filter params
                                filterVertices.reverse();
                                observer.next(filterVertices);
                                observer.complete();
                            }); // subscribe get filters
                        }
                        else {
                            _this.store.dispatch(new historyAction.AddStatusMessageAction("The specified filter is not a child values filter: "
                                + vertexValuesFilterPararamName));
                            observer.complete();
                        }
                    }); // observable create
                }; // get vertex filters
                FlexQueryService.prototype.loadVertexValues = function (jobId, vertexFileItem, vertexValuesFilterPararamName, eventedEntityType, eventedEntitySubType, eventedCvGroup, eventedCvTerm) {
                    var _this = this;
                    var targetChildFilterParams = this.filterParamsColl.getFilter(vertexValuesFilterPararamName, type_extractor_filter_1.GobiiExtractFilterType.FLEX_QUERY);
                    var targetChildFilterParamsToLoad = gobii_file_item_compound_id_1.GobiiFileItemCompoundId
                        .fromGobiiFileItemCompoundId(targetChildFilterParams.getTargetEntityUniqueId())
                        .setEntityType(eventedEntityType)
                        .setEntitySubType(eventedEntitySubType)
                        .setCvGroup(eventedCvGroup)
                        .setCvTerm(eventedCvTerm)
                        .setExtractorItemType(type_extractor_item_1.ExtractorItemType.VERTEX_VALUE);
                    if (vertexFileItem.getNameIdLabelType() == name_id_label_type_1.NameIdLabelType.UNKNOWN) {
                        this.getVertexFilters(vertexValuesFilterPararamName)
                            .subscribe(function (vertices) {
                            var targetVertex = vertexFileItem.getEntity();
                            var vertexFilterDTO = new vertex_filter_1.VertexFilterDTO(targetVertex, vertices, [], null, null, null, null, null, null);
                            var vertexFilterDtoResponse = null;
                            var cvTermIdVal = 0;
                            _this.dtoRequestServiceVertexFilterDTO.post(new dto_request_item_vertex_filter_1.DtoRequestItemVertexFilterDTO(vertexFilterDTO, jobId, false)).subscribe(function (payloadReader) {
                                if (payloadReader.succeeded()) {
                                    vertexFilterDtoResponse = payloadReader.getData();
                                    // note that we are setting the entity type, sub type, cvgroup, and cvterm
                                    // based on our request -- on the target vertex. In theory, the server could
                                    // be responding with NameId items that do not fit this. But this is the
                                    // way we handle other types of requests, basing our entity types and so forth
                                    // largely on the content of the request request.
                                    var vertexFileItems_1 = [];
                                    payloadReader.getData().vertexValues.forEach(function (item) {
                                        // property-based items do not have their own ID;
                                        // in order for the UI to work proerly, they must
                                        // have an id
                                        var itemId;
                                        if (targetVertex.gobiiVertexType === type_vertex_1.VertexType.CVTERM) {
                                            itemId = (++cvTermIdVal).toString();
                                        }
                                        else {
                                            itemId = item.id;
                                        }
                                        var currentFileItem = gobii_file_item_1.GobiiFileItem.build(type_extractor_filter_1.GobiiExtractFilterType.FLEX_QUERY, type_process_1.ProcessType.CREATE)
                                            .setExtractorItemType(type_extractor_item_1.ExtractorItemType.VERTEX_VALUE)
                                            .setEntityType(targetVertex.entityType)
                                            .setEntitySubType(targetVertex.entitySubType)
                                            .setCvGroup(targetVertex.cvGroup)
                                            .setCvTerm(targetVertex.cvTerm)
                                            .setItemId(itemId)
                                            .setItemName(item.name)
                                            .setRequired(false)
                                            .setIsEphemeral(false)
                                            .setSequenceNum(targetChildFilterParams.getSequenceNum());
                                        //.setParentItemId(filterValue)
                                        //.setIsExtractCriterion(filterParamsToLoad.getIsExtractCriterion())
                                        //.withRelatedEntity(entityRelation);
                                        vertexFileItems_1.push(currentFileItem);
                                    });
                                    // for flex query the "filter value" is not an actual id but a new entity type
                                    // our selectors "just know" to look for the filter's target entity type as the thing to filter on
                                    var targetCompoundUniqueId = targetChildFilterParams.getTargetEntityUniqueId();
                                    targetCompoundUniqueId.setExtractorItemType(type_extractor_item_1.ExtractorItemType.VERTEX_VALUE);
                                    targetCompoundUniqueId.setEntityType(targetVertex.entityType);
                                    var loadAction = new fileItemActions.LoadFileItemListWithFilterAction({
                                        gobiiFileItems: vertexFileItems_1,
                                        filterId: targetChildFilterParams.getQueryName(),
                                        filter: new action_payload_filter_1.PayloadFilter(type_extractor_filter_1.GobiiExtractFilterType.FLEX_QUERY, targetChildFilterParamsToLoad, targetChildFilterParams.getRelatedEntityUniqueId(), null, null, null, null),
                                        repalceByTarget: true
                                    });
                                    _this.store.dispatch(loadAction);
                                    //observer.next(vertexFileItems);
                                    //observer.complete();
                                }
                                else {
                                    _this.store.dispatch(new historyAction.AddStatusMessageAction("Error submitting extract insturctions: " +
                                        payloadReader.getError()));
                                }
                            });
                        }); // subscribe to get vertex filters
                    }
                    else
                        this.store.dispatch(new fileItemActions.LoadFilterAction({
                            filterId: vertexValuesFilterPararamName,
                            filter: new action_payload_filter_1.PayloadFilter(type_extractor_filter_1.GobiiExtractFilterType.FLEX_QUERY, new gobii_file_item_compound_id_1.GobiiFileItemCompoundId(type_extractor_item_1.ExtractorItemType.VERTEX_VALUE, type_entity_1.EntityType.UNKNOWN, // effectively "null out" the selected entity type
                            type_entity_1.EntitySubType.UNKNOWN, cv_group_1.CvGroup.UNKNOWN, cv_group_1.getCvGroupName(cv_group_1.CvGroup.UNKNOWN)), null, null, null, null, null)
                        }));
                }; // if-else file item type was label
                //} );//return observer create
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
                FlexQueryService.prototype.invalidateMarkerSampleCount = function (setToZero) {
                    var markerCountItem = gobii_file_item_1.GobiiFileItem
                        .build(type_extractor_filter_1.GobiiExtractFilterType.FLEX_QUERY, type_process_1.ProcessType.CREATE)
                        .setExtractorItemType(type_extractor_item_1.ExtractorItemType.ITEM_COUNT)
                        .setEntityType(type_entity_1.EntityType.MARKER)
                        .setItemName("Marker Count")
                        .setEntity(setToZero ? 0 : -1);
                    // default count items on load
                    var loadActionMarkerCount = new fileItemActions.LoadFileItemtAction({
                        gobiiFileItem: markerCountItem,
                        selectForExtract: true
                    });
                    this.store.dispatch(loadActionMarkerCount);
                    var loadActionSampleCount = new fileItemActions.LoadFileItemtAction({
                        gobiiFileItem: gobii_file_item_1.GobiiFileItem
                            .build(type_extractor_filter_1.GobiiExtractFilterType.FLEX_QUERY, type_process_1.ProcessType.CREATE)
                            .setExtractorItemType(type_extractor_item_1.ExtractorItemType.ITEM_COUNT)
                            .setEntityType(type_entity_1.EntityType.DNA_SAMPLE)
                            .setItemName("Sample Count")
                            .setEntity(setToZero ? 0 : -1),
                        selectForExtract: true
                    });
                    this.store.dispatch(loadActionSampleCount);
                }; // function: invalidate marker sample count
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