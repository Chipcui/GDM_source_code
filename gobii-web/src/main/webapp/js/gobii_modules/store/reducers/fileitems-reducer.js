System.register(["reselect", "../../model/gobii-file-item", "../actions/fileitem-action", "../../model/type-extractor-item", "../../model/type-entity", "../../model/file-item-param-names", "../../model/type-process", "../../views/entity-labels", "../../model/type-extractor-filter", "../../model/type-extract-format", "../../model/cv-group", "../../model/type-extractor-sample-list", "../../model/name-id-label-type", "../actions/action-filter-count-state"], function (exports_1, context_1) {
    "use strict";
    _this = this;
    var __moduleName = context_1 && context_1.id;
    function addToExtractItems(state, gobiiFileItem) {
        if (!state.allFileItems.find(function (fi) { return fi.getFileItemUniqueId() === gobiiFileItem.getFileItemUniqueId(); })) {
            console.log("Item is not in the collection: " + gobiiFileItem.getItemName() + " of type " + entity_labels_1.Labels.instance().treeExtractorTypeLabels[gobiiFileItem.getExtractorItemType()]);
        }
        gobiiFileItem.setSelected(true);
        var newSelectedUniqueIdsState = state.uniqueIdsOfExtractFileItems.slice();
        if (!newSelectedUniqueIdsState.find(function (id) { return id === gobiiFileItem.getFileItemUniqueId(); })) {
            newSelectedUniqueIdsState.push(gobiiFileItem.getFileItemUniqueId());
        }
        var returnVal = {
            gobiiExtractFilterType: state.gobiiExtractFilterType,
            allFileItems: state.allFileItems,
            uniqueIdsOfExtractFileItems: newSelectedUniqueIdsState,
            filters: state.filters
        };
        return returnVal;
    }
    function removeFromExtractItems(state, gobiiFileItem) {
        //    gobiiFileItem.setSelected(false);
        var newSelectedUniqueIdsState = state.uniqueIdsOfExtractFileItems.slice();
        // if we don't find it, we don't raise an error: the intent was to remove, but it just wasn't there,
        // so the state when this function exits is as expected
        var idx = newSelectedUniqueIdsState.findIndex(function (id) { return id === gobiiFileItem.getFileItemUniqueId(); });
        if (idx > -1) {
            newSelectedUniqueIdsState.splice(idx, 1);
        }
        var newFileItemState = state.allFileItems.slice();
        if (gobiiFileItem.getIsEphemeral()) {
            newFileItemState = newFileItemState.filter(function (fi) { return !fi.getIsEphemeral(); });
        }
        else {
            var fileItemInNewState = newFileItemState
                .find(function (fi) { return fi.getFileItemUniqueId() === gobiiFileItem.getFileItemUniqueId(); });
            if (fileItemInNewState) {
                fileItemInNewState.setSelected(false);
            }
        }
        var returnVal = {
            gobiiExtractFilterType: state.gobiiExtractFilterType,
            allFileItems: newFileItemState,
            uniqueIdsOfExtractFileItems: newSelectedUniqueIdsState,
            filters: state.filters
        };
        return returnVal;
    }
    function fileItemsReducer(state, action) {
        if (state === void 0) { state = initialState; }
        var returnVal = state;
        switch (action.type) {
            case gobiiFileItemAction.LOAD_FILE_ITEM: {
                var gobiiFileItemPayloadItem_1 = action.payload.gobiiFileItem;
                var newFileItemsItemsState = state.allFileItems.slice();
                var newSelectedFileItemUniqueIdsState = state.uniqueIdsOfExtractFileItems.slice();
                var existingItem_1 = newFileItemsItemsState.find(function (stateItem) {
                    return stateItem.compoundIdeEquals(gobiiFileItemPayloadItem_1)
                        && stateItem.getItemId() === gobiiFileItemPayloadItem_1.getItemId();
                });
                if (existingItem_1) {
                    var existingSelectedUniqueId = newSelectedFileItemUniqueIdsState
                        .find(function (id) { return id === existingItem_1.getFileItemUniqueId(); });
                    if (existingSelectedUniqueId) {
                        newSelectedFileItemUniqueIdsState.splice(newSelectedFileItemUniqueIdsState.indexOf(existingSelectedUniqueId), 1);
                        newSelectedFileItemUniqueIdsState.push(gobiiFileItemPayloadItem_1.getFileItemUniqueId());
                    }
                    newFileItemsItemsState = newFileItemsItemsState.filter(function (fi) { return fi.getFileItemUniqueId() != existingItem_1.getFileItemUniqueId(); });
                    newFileItemsItemsState.push(gobiiFileItemPayloadItem_1);
                }
                else {
                    newFileItemsItemsState.push(gobiiFileItemPayloadItem_1);
                }
                returnVal = {
                    gobiiExtractFilterType: state.gobiiExtractFilterType,
                    uniqueIdsOfExtractFileItems: newSelectedFileItemUniqueIdsState,
                    allFileItems: newFileItemsItemsState,
                    filters: state.filters
                };
                break;
            } // LOAD_FILE_ITEM
            case gobiiFileItemAction.LOAD_FILE_ITEM_LIST_WITH_FILTER: {
                var gobiiFileItemsPayload = action.payload.gobiiFileItems;
                var filterId = action.payload.filterId.toString();
                var filterValue = action.payload.filter;
                var newGobiiFileItems = gobiiFileItemsPayload.filter(function (newItem) {
                    return state
                        .allFileItems
                        .filter(function (stateItem) {
                        return (stateItem.getGobiiExtractFilterType() === newItem.getGobiiExtractFilterType() &&
                            stateItem.compoundIdeEquals(newItem) &&
                            stateItem.getItemId() === newItem.getItemId() &&
                            ((stateItem.getEntity() === null && newItem.getEntity() === null)
                                || ((stateItem.getEntity() !== null && newItem.getEntity() !== null)
                                    && (stateItem.getEntity().id === newItem.getEntity().id))));
                    }).length === 0;
                });
                var newFilterState = Object.assign({}, state.filters);
                newFilterState[filterId] = filterValue;
                returnVal = {
                    gobiiExtractFilterType: state.gobiiExtractFilterType,
                    uniqueIdsOfExtractFileItems: state.uniqueIdsOfExtractFileItems,
                    allFileItems: state.allFileItems.concat(newGobiiFileItems),
                    filters: newFilterState
                };
                break;
            } // LOAD_FILE_ITEM_LIST_WITH_FILTER
            case gobiiFileItemAction.LOAD_FILTER: {
                var filterId = action.payload.filterId.toString();
                var filterPayload_1 = action.payload.filter;
                var newFilterState = Object.assign({}, state.filters);
                newFilterState[filterId] = filterPayload_1;
                /***
                 * The following lines are intended to solve an oddly thorny problem.
                 On the dataset tab, if you don't set the selected label item here in state,
                 when you set the PI filter back to All PI’s, the two child lists are filtered
                 as they should be, but the top-most item in the list doesn’t change – they
                 don’t revert to “All Projects” and “All Experiments.” The problem seems to be
                 that once the list has decided what the top item is, if _none_ of the items
                 in the list has selected==true, it just stays where it is in the list: there
                 needs to be a differentiator. It also matters significantly that you set the
                 selected item in the reducer: if you do it elsewhere it does not work
                 consistently (as you would expect whenever changing state).
    
                 This particular solution is not ideal. Its conditions are organized around
                 the particular details of this problem -- it's not generaelizable. Most imporantly,
                 the filter that's applied to figure out the target list of
                 items for which the first Label needs to be selected should be exactly the same
                 filter as the one that populated the drop-down in question. I think the best way
                 to do this is for the content of the filter to be associated with with the Filterparams
                 and for the reducer to have access to that. So the selector methods in the reducer would
                 just get the FilterParams object and apply its filter semantics. The LOAD_ITEMS action would
                 then use exactly the same filter to determine the list of items to focus on. But this would
                 be a more substantial change than what's needed to fix the immediate issue.
                 Also, the isExtractCriterion condition seems a bit arbitrary. For now, it just means -- this
                 group of controls that I happen to know require this treatment.
    
                 */
                var newFileItemState = state.allFileItems.slice();
                if (!newFilterState[filterId].targetEntityUniqueId.getIsExtractCriterion()) {
                    var gobiiFileItemCompoundId_1 = newFilterState[filterId].targetEntityUniqueId;
                    var allItemsForFilter = newFileItemState
                        .filter(function (gfi) {
                        return (gfi.getGobiiExtractFilterType() === state.gobiiExtractFilterType
                            && gfi.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.ENTITY
                            || gfi.getNameIdLabelType() !== name_id_label_type_1.NameIdLabelType.UNKNOWN)
                            && gfi.getProcessType() !== type_process_1.ProcessType.DUMMY
                            && gfi.getEntityType() === gobiiFileItemCompoundId_1.getEntityType();
                    });
                    allItemsForFilter.forEach(function (gfi) {
                        gfi.setSelected(false);
                    });
                    if (filterPayload_1.targetEntityFilterValue && +filterPayload_1.targetEntityFilterValue > 0) {
                        var selectedItem = allItemsForFilter.find(function (gfi) { return gfi.getItemId() === filterPayload_1.targetEntityFilterValue; });
                        if (selectedItem) {
                            selectedItem.setSelected(true);
                        }
                    }
                    else {
                        if (allItemsForFilter[0]) {
                            if ((!filterPayload_1.relatedEntityFilterValue || +filterPayload_1.relatedEntityFilterValue < 1)) {
                                allItemsForFilter[0].setSelected(true);
                            }
                        }
                    }
                }
                returnVal = {
                    gobiiExtractFilterType: state.gobiiExtractFilterType,
                    uniqueIdsOfExtractFileItems: state.uniqueIdsOfExtractFileItems,
                    allFileItems: newFileItemState,
                    filters: newFilterState
                };
                break;
            } // LOAD_FILTER
            case gobiiFileItemAction.ADD_TO_EXTRACT:
                {
                    var gobiiFileItemPayload = action.payload;
                    returnVal = addToExtractItems(state, gobiiFileItemPayload);
                    break;
                } // ADD_TO_EXTRACT
            case gobiiFileItemAction.ADD_TO_EXTRACT_BY_ITEM_ID: {
                var fileItemUniqueIdPayload_1 = action.payload;
                var gobiiFileItem = state
                    .allFileItems
                    .find(function (fi) { return fi.getFileItemUniqueId() === fileItemUniqueIdPayload_1; });
                returnVal = addToExtractItems(state, gobiiFileItem);
                break;
            } //
            case gobiiFileItemAction.REPLACE_BY_ITEM_ID:
                {
                    var itemCurrentlyInExtract = state
                        .allFileItems
                        .find(function (fi) { return fi.getFileItemUniqueId() === action.payload.itemIdCurrentlyInExtract; });
                    if (itemCurrentlyInExtract.getIsExtractCriterion()) {
                        var itemToReplaceItWith = state
                            .allFileItems
                            .find(function (fi) { return fi.getFileItemUniqueId() === action.payload.itemIdToReplaceItWith; });
                        var stateAfterRemove = removeFromExtractItems(state, itemCurrentlyInExtract);
                        returnVal = addToExtractItems(stateAfterRemove, itemToReplaceItWith);
                    }
                    else {
                        // there is also an effect that does additional things with this action
                        return state;
                    }
                    break;
                }
            case gobiiFileItemAction.REPLACE_ITEM_OF_SAME_COMPOUND_ID: {
                var newItemToAdd_1 = action.payload.gobiiFileitemToReplaceWith;
                var fileItemToReplace_1 = state.allFileItems
                    .find(function (fi) { return fi.getGobiiExtractFilterType() === newItemToAdd_1.getGobiiExtractFilterType()
                    && fi.compoundIdeEquals(newItemToAdd_1); });
                var stateWithNewFileItem = {
                    gobiiExtractFilterType: state.gobiiExtractFilterType,
                    allFileItems: state.allFileItems,
                    uniqueIdsOfExtractFileItems: state.uniqueIdsOfExtractFileItems,
                    filters: state.filters
                };
                // remove existing item if applicable
                if (fileItemToReplace_1) {
                    stateWithNewFileItem.allFileItems =
                        stateWithNewFileItem.allFileItems.filter(function (fi) {
                            return fi.getFileItemUniqueId() !== fileItemToReplace_1.getFileItemUniqueId();
                        });
                }
                // add new item
                stateWithNewFileItem.allFileItems.push(newItemToAdd_1);
                // now add new item to selection if applicable
                var stateWithItemSelection = void 0;
                if (newItemToAdd_1.getIsExtractCriterion()) {
                    if (fileItemToReplace_1) {
                        var oldItemUnselectedState = removeFromExtractItems(stateWithNewFileItem, fileItemToReplace_1);
                        stateWithItemSelection = addToExtractItems(oldItemUnselectedState, newItemToAdd_1);
                    }
                    else {
                        stateWithItemSelection = addToExtractItems(stateWithNewFileItem, newItemToAdd_1);
                    }
                }
                returnVal = stateWithItemSelection;
                break;
            }
            case gobiiFileItemAction.REMOVE_FROM_EXTRACT: {
                var gobiiFileItemPayload = action.payload;
                returnVal = removeFromExtractItems(state, gobiiFileItemPayload);
                break;
            }
            case gobiiFileItemAction.REMOVE_FROM_EXTRACT_BY_ITEM_ID: {
                var fileItemUniqueIdPayload_2 = action.payload;
                var gobiiFileItemPayload = state
                    .allFileItems
                    .find(function (fi) { return fi.getFileItemUniqueId() === fileItemUniqueIdPayload_2; });
                returnVal = removeFromExtractItems(state, gobiiFileItemPayload);
                break;
            }
            case gobiiFileItemAction.REMOVE_ALL_FROM_EXTRACT: {
                // only those not of the same extract filter type should remain selected
                var newFIleItemState = state.allFileItems.slice();
                var itemsToDeselect_1 = newFIleItemState
                    .filter(function (fi) { return fi.getGobiiExtractFilterType() === action.payload; });
                itemsToDeselect_1.forEach(function (fi) { return fi.setSelected(false); });
                var newSelectedItems = state.uniqueIdsOfExtractFileItems
                    .filter(function (id) { return !itemsToDeselect_1
                    .find(function (fi) { return fi.getFileItemUniqueId() === id; }); });
                // remove emphemeral items from state
                newFIleItemState = newFIleItemState
                    .filter(function (fi) { return !fi.getIsEphemeral(); });
                // remove filters
                var newFilterState = Object.assign({}, state.filters);
                newFilterState = new Map();
                returnVal = {
                    gobiiExtractFilterType: state.gobiiExtractFilterType,
                    allFileItems: newFIleItemState,
                    uniqueIdsOfExtractFileItems: newSelectedItems,
                    filters: newFilterState
                };
                break;
            }
            case gobiiFileItemAction.SET_EXTRACT_TYPE: {
                var gobiiExtractFilterType = action.payload.gobiiExtractFilterType;
                returnVal = {
                    gobiiExtractFilterType: gobiiExtractFilterType,
                    allFileItems: state.allFileItems,
                    uniqueIdsOfExtractFileItems: state.uniqueIdsOfExtractFileItems,
                    filters: state.filters
                };
                break;
            }
        }
        return returnVal;
    }
    exports_1("fileItemsReducer", fileItemsReducer);
    var _this, reselect_1, gobii_file_item_1, gobiiFileItemAction, type_extractor_item_1, type_entity_1, file_item_param_names_1, type_process_1, entity_labels_1, type_extractor_filter_1, type_extract_format_1, cv_group_1, type_extractor_sample_list_1, name_id_label_type_1, action_filter_count_state_1, initialState, getGobiiExtractFilterType, getFileItems, getUniqueIds, getSelectedUniqueIds, getFilters, getSelected, getAll, getSelectedFileFormat, getSelectedSampleType, getJobId, getUploadFiles, getPiContacts, getProjects, getExperiments, getDatasets, getCvTermsDataType, getCvTermsJobStatus, getMapsets, getPlatforms, getMarkerGroups, getSelectedPiContacts, getProjectsForSelectedPi, getExperimentsForSelectedProject, getDatasetsForSelectedExperiment, getDatasetEntities, getDatasetEntitiesPaged, getPiContactsFilterOptional, getProjectsFilterOptional, getExperimentsFilterOptional, getFqF1Vertices, getFqF2Vertices, getFqF3Vertices, getFqF4Vertices, getFqF1VerticesValues, getFqF2VerticesValues, getFqF3VerticesValues, getFqF4VerticesValues, getCurrentMarkerCount, getCurrentSampleCount, getFilterCountState;
    return {
        setters: [
            function (reselect_1_1) {
                reselect_1 = reselect_1_1;
            },
            function (gobii_file_item_1_1) {
                gobii_file_item_1 = gobii_file_item_1_1;
            },
            function (gobiiFileItemAction_1) {
                gobiiFileItemAction = gobiiFileItemAction_1;
            },
            function (type_extractor_item_1_1) {
                type_extractor_item_1 = type_extractor_item_1_1;
            },
            function (type_entity_1_1) {
                type_entity_1 = type_entity_1_1;
            },
            function (file_item_param_names_1_1) {
                file_item_param_names_1 = file_item_param_names_1_1;
            },
            function (type_process_1_1) {
                type_process_1 = type_process_1_1;
            },
            function (entity_labels_1_1) {
                entity_labels_1 = entity_labels_1_1;
            },
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
            },
            function (type_extract_format_1_1) {
                type_extract_format_1 = type_extract_format_1_1;
            },
            function (cv_group_1_1) {
                cv_group_1 = cv_group_1_1;
            },
            function (type_extractor_sample_list_1_1) {
                type_extractor_sample_list_1 = type_extractor_sample_list_1_1;
            },
            function (name_id_label_type_1_1) {
                name_id_label_type_1 = name_id_label_type_1_1;
            },
            function (action_filter_count_state_1_1) {
                action_filter_count_state_1 = action_filter_count_state_1_1;
            }
        ],
        execute: function () {
            ;
            exports_1("initialState", initialState = {
                gobiiExtractFilterType: type_extractor_filter_1.GobiiExtractFilterType.UNKNOWN,
                uniqueIdsOfExtractFileItems: [],
                allFileItems: [],
                filters: new Map()
            });
            /**
             * Because the data structure is defined within the fileItemsReducer it is optimal to
             * locate our selector functions at this level. If store is to be thought of
             * as a database, and reducers the tables, selectors can be considered the
             * queries into said database. Remember to keep your selectors small and
             * focused so they can be combined and composed to fit each particular
             * use-case.
             *
             * WARNING: as of 4.0.2, ngrx/platform  issue # 208: https://github.com/ngrx/platform/issues/208
             * has _not_ been resulved. So selectors _must_ return non-primtive objects. See my comment
             * in export-format-component.ts
             */
            exports_1("getGobiiExtractFilterType", getGobiiExtractFilterType = function (state) { return state.gobiiExtractFilterType; });
            exports_1("getFileItems", getFileItems = function (state) { return state.allFileItems.filter(function (fi) {
                return fi.getGobiiExtractFilterType() === state.gobiiExtractFilterType;
            }); });
            exports_1("getUniqueIds", getUniqueIds = function (state) { return state.allFileItems.map(function (fileItem) { return fileItem.getFileItemUniqueId(); }); });
            exports_1("getSelectedUniqueIds", getSelectedUniqueIds = function (state) { return state.uniqueIdsOfExtractFileItems; });
            exports_1("getFilters", getFilters = function (state) { return state.filters; });
            exports_1("getSelected", getSelected = reselect_1.createSelector(getFileItems, getSelectedUniqueIds, function (fileItems, selectedUniqueIds) {
                var returnVal = fileItems
                    .filter(function (fileItem) {
                    return selectedUniqueIds
                        .find(function (uniqueId) { return fileItem.getFileItemUniqueId() === uniqueId; });
                });
                var foo = "foo";
                return returnVal;
            }));
            exports_1("getAll", getAll = reselect_1.createSelector(getFileItems, getUniqueIds, function (entities, ids) {
                return entities;
            }));
            // ideally, the redux way of selecting data would be for the selection to be based
            // on which entity is "selected" The problem is that we have controlls in which the
            // entity type is parameterized -- it is not global state
            /// ****************** SYNCHRONOUS METHODS
            /// **************** GET SELECTED PER ENTITY TYPE
            exports_1("getSelectedFileFormat", getSelectedFileFormat = reselect_1.createSelector(getFileItems, getSelectedUniqueIds, function (fileItems, selectedUniqueIds) {
                // default
                var returnVal = gobii_file_item_1.GobiiFileItem
                    .build(_this.gobiiExtractFilterType, type_process_1.ProcessType.UPDATE)
                    .setExtractorItemType(type_extractor_item_1.ExtractorItemType.EXPORT_FORMAT)
                    .setItemId(type_extract_format_1.GobiiExtractFormat[type_extract_format_1.GobiiExtractFormat.HAPMAP])
                    .setItemName(type_extract_format_1.GobiiExtractFormat[type_extract_format_1.GobiiExtractFormat[type_extract_format_1.GobiiExtractFormat.HAPMAP]]);
                var formatItem = fileItems
                    .find(function (fi) { return fi.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.EXPORT_FORMAT
                    && undefined !== selectedUniqueIds.find(function (id) { return id === fi.getFileItemUniqueId(); }); });
                if (formatItem) {
                    returnVal = formatItem;
                }
                return returnVal;
            }));
            exports_1("getSelectedSampleType", getSelectedSampleType = reselect_1.createSelector(getFileItems, getSelectedUniqueIds, function (fileItems, selectedUniqueIds) {
                // default
                var returnVal = gobii_file_item_1.GobiiFileItem.build(_this.gobiiExtractFilterType, type_process_1.ProcessType.CREATE)
                    .setExtractorItemType(type_extractor_item_1.ExtractorItemType.SAMPLE_LIST_TYPE)
                    .setItemName(type_extractor_sample_list_1.GobiiSampleListType[type_extractor_sample_list_1.GobiiSampleListType.GERMPLASM_NAME])
                    .setItemId(type_extractor_sample_list_1.GobiiSampleListType[type_extractor_sample_list_1.GobiiSampleListType.GERMPLASM_NAME]);
                var formatItem = fileItems
                    .find(function (fi) { return fi.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.SAMPLE_LIST_TYPE
                    && undefined !== selectedUniqueIds.find(function (id) { return id === fi.getFileItemUniqueId(); }); });
                if (formatItem) {
                    returnVal = formatItem;
                }
                return returnVal;
            }));
            exports_1("getJobId", getJobId = reselect_1.createSelector(getFileItems, getSelectedUniqueIds, function (fileItems, selectedUniqueIds) {
                // default
                var returnVal = gobii_file_item_1.GobiiFileItem
                    .build(_this.gobiiExtractFilterType, type_process_1.ProcessType.UPDATE)
                    .setExtractorItemType(type_extractor_item_1.ExtractorItemType.JOB_ID)
                    .setItemId("UNSET")
                    .setItemName("UNSET");
                var jobIdItem = fileItems
                    .find(function (fi) { return (fi.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.JOB_ID)
                    && (selectedUniqueIds.indexOf(fi.getFileItemUniqueId()) > -1); });
                if (jobIdItem) {
                    returnVal = jobIdItem;
                }
                return returnVal;
            }));
            exports_1("getUploadFiles", getUploadFiles = reselect_1.createSelector(getFileItems, getSelectedUniqueIds, function (fileItems, selectedUniqueIds) {
                // default
                var returnVal = fileItems
                    .filter(function (fi) { return fi.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.MARKER_FILE
                    || fi.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.SAMPLE_FILE; });
                return returnVal;
            }));
            exports_1("getPiContacts", getPiContacts = reselect_1.createSelector(getFileItems, getUniqueIds, function (fileItems, ids) {
                var returnVal = fileItems.filter(function (e) {
                    return (e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.ENTITY
                        || e.getNameIdLabelType() !== name_id_label_type_1.NameIdLabelType.UNKNOWN)
                        && e.getEntityType() === type_entity_1.EntityType.CONTACT
                        && e.getEntitySubType() === type_entity_1.EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR;
                })
                    .map(function (fi) { return fi; });
                return returnVal;
            }));
            exports_1("getProjects", getProjects = reselect_1.createSelector(getFileItems, getUniqueIds, function (fileItems, ids) {
                var returnVal = [];
                returnVal = fileItems.filter(function (e) {
                    return (e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.ENTITY
                        || e.getNameIdLabelType() !== name_id_label_type_1.NameIdLabelType.UNKNOWN)
                        && e.getEntityType() === type_entity_1.EntityType.PROJECT
                        && e.getProcessType() !== type_process_1.ProcessType.DUMMY;
                })
                    .map(function (fi) { return fi; });
                if (returnVal.length <= 0) {
                    returnVal = fileItems.filter(function (e) {
                        return (e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.ENTITY)
                            && (e.getEntityType() === type_entity_1.EntityType.PROJECT)
                            && e.getProcessType() === type_process_1.ProcessType.DUMMY;
                    })
                        .map(function (fi) { return fi; });
                }
                return returnVal;
            }));
            exports_1("getExperiments", getExperiments = reselect_1.createSelector(getFileItems, getUniqueIds, function (fileItems, ids) {
                return fileItems.filter(function (e) {
                    return (e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.ENTITY
                        || e.getNameIdLabelType() !== name_id_label_type_1.NameIdLabelType.UNKNOWN)
                        && e.getEntityType() === type_entity_1.EntityType.EXPERIMENT;
                })
                    .map(function (fi) { return fi; });
            }));
            exports_1("getDatasets", getDatasets = reselect_1.createSelector(getFileItems, getUniqueIds, function (fileItems, ids) {
                return fileItems.filter(function (e) {
                    return (e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.ENTITY
                        || e.getNameIdLabelType() !== name_id_label_type_1.NameIdLabelType.UNKNOWN)
                        && e.getEntityType() === type_entity_1.EntityType.DATASET;
                })
                    .map(function (fi) { return fi; });
            }));
            exports_1("getCvTermsDataType", getCvTermsDataType = reselect_1.createSelector(getFileItems, getUniqueIds, function (fileItems, ids) {
                var returnVal = fileItems.filter(function (e) {
                    return (e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.ENTITY
                        || e.getNameIdLabelType() !== name_id_label_type_1.NameIdLabelType.UNKNOWN)
                        && e.getEntityType() === type_entity_1.EntityType.CV
                        && e.getCvGroup() === cv_group_1.CvGroup.DATASET_TYPE;
                })
                    .map(function (fi) { return fi; });
                return returnVal;
            }));
            exports_1("getCvTermsJobStatus", getCvTermsJobStatus = reselect_1.createSelector(getFileItems, getUniqueIds, function (fileItems, ids) {
                var returnVal = fileItems.filter(function (e) {
                    return (e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.ENTITY
                        || e.getNameIdLabelType() !== name_id_label_type_1.NameIdLabelType.UNKNOWN)
                        && e.getEntityType() === type_entity_1.EntityType.CV
                        && e.getCvGroup() === cv_group_1.CvGroup.JOBSTATUS;
                })
                    .map(function (fi) { return fi; });
                return returnVal;
            }));
            exports_1("getMapsets", getMapsets = reselect_1.createSelector(getFileItems, getUniqueIds, function (fileItems, ids) {
                return fileItems.filter(function (e) {
                    return (e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.ENTITY
                        || e.getNameIdLabelType() !== name_id_label_type_1.NameIdLabelType.UNKNOWN)
                        && e.getEntityType() === type_entity_1.EntityType.MAPSET;
                })
                    .map(function (fi) { return fi; });
            }));
            exports_1("getPlatforms", getPlatforms = reselect_1.createSelector(getFileItems, getUniqueIds, function (fileItems, ids) {
                return fileItems.filter(function (e) {
                    return (e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.ENTITY
                        || e.getNameIdLabelType() !== name_id_label_type_1.NameIdLabelType.UNKNOWN)
                        && e.getEntityType() === type_entity_1.EntityType.PLATFORM;
                })
                    .map(function (fi) { return fi; });
            }));
            exports_1("getMarkerGroups", getMarkerGroups = reselect_1.createSelector(getFileItems, getUniqueIds, function (fileItems, ids) {
                return fileItems.filter(function (e) {
                    return (e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.ENTITY
                        || e.getNameIdLabelType() !== name_id_label_type_1.NameIdLabelType.UNKNOWN)
                        && e.getEntityType() === type_entity_1.EntityType.MARKER_GROUP;
                })
                    .map(function (fi) { return fi; });
            }));
            // **************** GET SELECTED PER ENTITY TYPE
            exports_1("getSelectedPiContacts", getSelectedPiContacts = reselect_1.createSelector(getFileItems, getUniqueIds, function (fileItems, ids) {
                return fileItems.filter(function (e) {
                    return ids.find(function (id) { return id === e.getFileItemUniqueId(); })
                        && e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.ENTITY
                        && e.getEntityType() === type_entity_1.EntityType.CONTACT
                        && e.getEntitySubType() === type_entity_1.EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR;
                })
                    .map(function (fi) { return fi; });
            }));
            // here
            exports_1("getProjectsForSelectedPi", getProjectsForSelectedPi = reselect_1.createSelector(getFileItems, getFilters, getGobiiExtractFilterType, function (fileItems, filters, gobiiExtractFilterType) {
                var returnVal = [];
                var contactId = null;
                if (filters[file_item_param_names_1.FilterParamNames.PROJECTS_BY_CONTACT]) {
                    contactId = filters[file_item_param_names_1.FilterParamNames.PROJECTS_BY_CONTACT].relatedEntityFilterValue;
                }
                var projectId;
                returnVal = fileItems.filter(function (e) {
                    return (e.getGobiiExtractFilterType() === gobiiExtractFilterType
                        && e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.ENTITY
                        || e.getNameIdLabelType() !== name_id_label_type_1.NameIdLabelType.UNKNOWN)
                        && e.getProcessType() !== type_process_1.ProcessType.DUMMY
                        && e.getEntityType() === type_entity_1.EntityType.PROJECT
                        && ((!contactId || (+contactId < 0)) // state is not filtered -- we don't care, or . . .
                            || +e.getItemId() === 0 // Inlcude label "All Projects"
                            || (e.getRelatedEntityFilterValue(filters[file_item_param_names_1.FilterParamNames.CONTACT_PI_HIERARCHY_ROOT].targetEntityUniqueId) // the item has an fk value
                                && e.getRelatedEntityFilterValue(filters[file_item_param_names_1.FilterParamNames.CONTACT_PI_HIERARCHY_ROOT].targetEntityUniqueId) === contactId));
                } // and it matches
                ).map(function (fi) { return fi; });
                if (returnVal.length <= 0) {
                    returnVal = fileItems.filter(function (e) {
                        return (e.getGobiiExtractFilterType() == gobiiExtractFilterType
                            && e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.ENTITY
                            && e.getEntityType() === type_entity_1.EntityType.PROJECT
                            && e.getProcessType() === type_process_1.ProcessType.DUMMY);
                    })
                        .map(function (fi) { return fi; });
                }
                return returnVal;
                /*
                let returnVal: GobiiFileItem[] = [];
            
                if (filters[FilterParamNames.PROJECTS_BY_CONTACT]) {
            
                    let contactId: string = filters[FilterParamNames.PROJECTS_BY_CONTACT].relatedEntityFilterValue;
                    returnVal = fileItems.filter(e =>
                        (e.getExtractorItemType() === ExtractorItemType.ENTITY)
                        && (e.getEntityType() === EntityType.PROJECT)
                        && (e.getParentItemId() === contactId)
                        && e.getProcessType() !== ProcessType.DUMMY)
                        .map(fi => fi);
            
                    if (returnVal.length <= 0) {
                        returnVal = fileItems.filter(e =>
                            (e.getExtractorItemType() === ExtractorItemType.ENTITY)
                            && (e.getEntityType() === EntityType.PROJECT)
                            && e.getProcessType() === ProcessType.DUMMY)
                            .map(fi => fi);
                    }
                }
            
                return returnVal;
                */
            }));
            exports_1("getExperimentsForSelectedProject", getExperimentsForSelectedProject = reselect_1.createSelector(getFileItems, getFilters, function (fileItems, filters) {
                var returnVal = [];
                if (filters[file_item_param_names_1.FilterParamNames.EXPERIMENTS_BY_PROJECT]) {
                    var projectId_1 = filters[file_item_param_names_1.FilterParamNames.EXPERIMENTS_BY_PROJECT].relatedEntityFilterValue;
                    returnVal = fileItems.filter(function (e) {
                        return (e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.ENTITY)
                            && (e.getEntityType() === type_entity_1.EntityType.EXPERIMENT)
                            && (e.getParentItemId() === projectId_1)
                            && e.getProcessType() !== type_process_1.ProcessType.DUMMY;
                    })
                        .map(function (fi) { return fi; });
                    if (returnVal.length <= 0) {
                        returnVal = fileItems.filter(function (e) {
                            return (e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.ENTITY)
                                && (e.getEntityType() === type_entity_1.EntityType.EXPERIMENT)
                                && e.getProcessType() === type_process_1.ProcessType.DUMMY;
                        })
                            .map(function (fi) { return fi; });
                    }
                }
                return returnVal;
            }));
            exports_1("getDatasetsForSelectedExperiment", getDatasetsForSelectedExperiment = reselect_1.createSelector(getFileItems, getFilters, function (fileItems, filters) {
                var returnVal = [];
                if (filters[file_item_param_names_1.FilterParamNames.DATASETS_BY_EXPERIMENT]) {
                    var experimentId_1 = filters[file_item_param_names_1.FilterParamNames.DATASETS_BY_EXPERIMENT].relatedEntityFilterValue;
                    returnVal = fileItems.filter(function (e) {
                        return (e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.ENTITY
                            && e.getEntityType() === type_entity_1.EntityType.DATASET
                            && e.getParentItemId() === experimentId_1
                            && e.getProcessType() !== type_process_1.ProcessType.DUMMY);
                    })
                        .map(function (fi) { return fi; });
                    if (returnVal.length <= 0) {
                        returnVal = fileItems.filter(function (e) {
                            return (e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.ENTITY
                                && e.getEntityType() === type_entity_1.EntityType.DATASET
                                && e.getProcessType() === type_process_1.ProcessType.DUMMY);
                        })
                            .map(function (fi) { return fi; });
                    }
                    "";
                }
                return returnVal;
            }));
            exports_1("getDatasetEntities", getDatasetEntities = reselect_1.createSelector(getFileItems, getFilters, function (fileItems, filters) {
                var returnVal = [];
                // the child filter has the parent fk value
                var contactId = filters[file_item_param_names_1.FilterParamNames.CONTACT_PI_FILTER_OPTIONAL] ?
                    filters[file_item_param_names_1.FilterParamNames.CONTACT_PI_FILTER_OPTIONAL].targetEntityFilterValue : null;
                var compounUniqueIdForContacts = filters[file_item_param_names_1.FilterParamNames.CONTACT_PI_FILTER_OPTIONAL] ? filters[file_item_param_names_1.FilterParamNames.CONTACT_PI_FILTER_OPTIONAL].targetEntityUniqueId : null;
                var projectId = filters[file_item_param_names_1.FilterParamNames.PROJECT_FILTER_OPTIONAL] ?
                    filters[file_item_param_names_1.FilterParamNames.PROJECT_FILTER_OPTIONAL].targetEntityFilterValue : null;
                var compounUniqueIdForProjectsByContact = filters[file_item_param_names_1.FilterParamNames.PROJECT_FILTER_OPTIONAL] ? filters[file_item_param_names_1.FilterParamNames.PROJECT_FILTER_OPTIONAL].targetEntityUniqueId : null;
                var experimentId = filters[file_item_param_names_1.FilterParamNames.EXPERIMENT_FILTER_OPTIONAL] ?
                    filters[file_item_param_names_1.FilterParamNames.EXPERIMENT_FILTER_OPTIONAL].targetEntityFilterValue : null;
                var compounUniqueIdForExperimentsByProject = filters[file_item_param_names_1.FilterParamNames.EXPERIMENT_FILTER_OPTIONAL] ? filters[file_item_param_names_1.FilterParamNames.EXPERIMENT_FILTER_OPTIONAL].targetEntityUniqueId : null;
                var datasetEntitiesFilteredByExperiment = [];
                datasetEntitiesFilteredByExperiment = fileItems.filter(function (e) {
                    return (e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.ENTITY
                        && e.getEntityType() === type_entity_1.EntityType.DATASET
                        && ((contactId === null) || +contactId < 1 || compounUniqueIdForContacts === null || e.getRelatedEntityFilterValue(compounUniqueIdForContacts) === contactId)
                        && ((projectId === null) || +projectId < 1 || compounUniqueIdForProjectsByContact === null || e.getRelatedEntityFilterValue(compounUniqueIdForProjectsByContact) === projectId)
                        && ((experimentId === null) || +experimentId < 1 || compounUniqueIdForExperimentsByProject === null || e.getRelatedEntityFilterValue(compounUniqueIdForExperimentsByProject) === experimentId)
                        && e.hasEntity()
                        && e.getProcessType() !== type_process_1.ProcessType.DUMMY);
                })
                    .map(function (fi) { return fi; });
                var jobStatusFilterParams = filters[file_item_param_names_1.FilterParamNames.DATASET_LIST_STATUS];
                if (jobStatusFilterParams
                    && jobStatusFilterParams.relatedEntityFilterValue != null) {
                    returnVal = datasetEntitiesFilteredByExperiment
                        .filter(function (fi) { return fi.getEntity().jobStatusName === jobStatusFilterParams.relatedEntityFilterValue; });
                }
                else {
                    returnVal = datasetEntitiesFilteredByExperiment;
                }
                return returnVal;
            }));
            exports_1("getDatasetEntitiesPaged", getDatasetEntitiesPaged = reselect_1.createSelector(getFileItems, getFilters, function (fileItems, filters) {
                var intermediateResult = getDatasetEntities.resultFunc(fileItems, filters);
                var payloadFilter = filters[file_item_param_names_1.FilterParamNames.DATASET_LIST_PAGED];
                var returnVal = intermediateResult.filter(function (gfi) { return gfi.getPageNumber() === payloadFilter.pagination.currentPage; });
                return returnVal;
            }));
            exports_1("getPiContactsFilterOptional", getPiContactsFilterOptional = reselect_1.createSelector(getFileItems, getGobiiExtractFilterType, function (fileItems, gobiiExtractFilterType) {
                return fileItems.filter(function (e) {
                    return (e.getGobiiExtractFilterType() === gobiiExtractFilterType
                        && e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.ENTITY
                        || e.getNameIdLabelType() !== name_id_label_type_1.NameIdLabelType.UNKNOWN)
                        && e.getEntityType() === type_entity_1.EntityType.CONTACT
                        && e.getEntitySubType() === type_entity_1.EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR;
                })
                    .map(function (fi) { return fi; });
            }));
            exports_1("getProjectsFilterOptional", getProjectsFilterOptional = reselect_1.createSelector(getFileItems, getFilters, getGobiiExtractFilterType, function (fileItems, filters, gobiiExtractFilterType) {
                var returnVal = [];
                // The project filter's value is a contactId. So we want only
                // those projects that have an fk reference to the specified contact.
                var contactId = null;
                if (filters[file_item_param_names_1.FilterParamNames.PROJECT_FILTER_OPTIONAL]) {
                    contactId = filters[file_item_param_names_1.FilterParamNames.PROJECT_FILTER_OPTIONAL].relatedEntityFilterValue;
                }
                returnVal = fileItems.filter(function (e) {
                    return (e.getGobiiExtractFilterType() === gobiiExtractFilterType
                        && e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.ENTITY
                        || e.getNameIdLabelType() !== name_id_label_type_1.NameIdLabelType.UNKNOWN)
                        && e.getProcessType() !== type_process_1.ProcessType.DUMMY
                        && e.getEntityType() === type_entity_1.EntityType.PROJECT
                        && ((!contactId || (+contactId < 0)) // state is not filtered -- we don't care, or . . .
                            || +e.getItemId() === 0 // Inlcude label "All Projects"
                            || (e.getRelatedEntityFilterValue(filters[file_item_param_names_1.FilterParamNames.CONTACT_PI_FILTER_OPTIONAL].targetEntityUniqueId) // the item has an fk value
                                && e.getRelatedEntityFilterValue(filters[file_item_param_names_1.FilterParamNames.CONTACT_PI_FILTER_OPTIONAL].targetEntityUniqueId) === contactId));
                } // and it matches
                ).map(function (fi) { return fi; });
                if (returnVal.length <= 0) {
                    returnVal = fileItems.filter(function (e) {
                        return (e.getGobiiExtractFilterType() == gobiiExtractFilterType
                            && e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.ENTITY
                            && e.getEntityType() === type_entity_1.EntityType.PROJECT
                            && e.getProcessType() === type_process_1.ProcessType.DUMMY);
                    })
                        .map(function (fi) { return fi; });
                }
                return returnVal;
            }));
            exports_1("getExperimentsFilterOptional", getExperimentsFilterOptional = reselect_1.createSelector(getFileItems, getFilters, getGobiiExtractFilterType, function (fileItems, filters, gobiiExtractFilterType) {
                var returnVal = [];
                var projectId = null;
                if (filters[file_item_param_names_1.FilterParamNames.PROJECT_FILTER_OPTIONAL]) {
                    projectId = filters[file_item_param_names_1.FilterParamNames.PROJECT_FILTER_OPTIONAL].targetEntityFilterValue;
                }
                var contactId = null;
                if (filters[file_item_param_names_1.FilterParamNames.CONTACT_PI_FILTER_OPTIONAL]) {
                    contactId = filters[file_item_param_names_1.FilterParamNames.CONTACT_PI_FILTER_OPTIONAL].targetEntityFilterValue;
                }
                var projectIds = [];
                if ((projectId && +projectId > 0)) {
                    projectIds.push(projectId);
                }
                else if (contactId && +contactId) {
                    projectIds = fileItems
                        .filter(function (fi) { return fi.compoundIdeEquals(filters[file_item_param_names_1.FilterParamNames.PROJECT_FILTER_OPTIONAL].targetEntityUniqueId)
                        && fi.getRelatedEntityFilterValue(filters[file_item_param_names_1.FilterParamNames.CONTACT_PI_FILTER_OPTIONAL].targetEntityUniqueId) === contactId; })
                        .map(function (fi) { return fi.getItemId(); });
                }
                returnVal = fileItems.filter(function (e) {
                    return (e.getGobiiExtractFilterType() == gobiiExtractFilterType
                        && e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.ENTITY
                        || e.getNameIdLabelType() !== name_id_label_type_1.NameIdLabelType.UNKNOWN)
                        && e.getProcessType() !== type_process_1.ProcessType.DUMMY
                        && e.getEntityType() === type_entity_1.EntityType.EXPERIMENT
                        && ((!projectId && !contactId) // state is not filtered -- we don't care, or . . .
                            || +e.getItemId() === 0 // Inlcude label "All Projects"
                            || (e.getRelatedEntityFilterValue(filters[file_item_param_names_1.FilterParamNames.PROJECT_FILTER_OPTIONAL].targetEntityUniqueId) // the item has an fk value
                                && projectIds.find(function (pid) { return e.getRelatedEntityFilterValue(filters[file_item_param_names_1.FilterParamNames.PROJECT_FILTER_OPTIONAL].targetEntityUniqueId) === pid; })));
                } // and it matches
                ).map(function (fi) { return fi; });
                if (returnVal.length <= 0) {
                    returnVal = fileItems.filter(function (e) {
                        return (e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.ENTITY
                            && e.getEntityType() === type_entity_1.EntityType.EXPERIMENT
                            && e.getProcessType() === type_process_1.ProcessType.DUMMY);
                    })
                        .map(function (fi) { return fi; });
                }
                return returnVal;
            }));
            exports_1("getFqF1Vertices", getFqF1Vertices = reselect_1.createSelector(getFileItems, getFilters, getGobiiExtractFilterType, function (fileItems, filters, gobiiExtractFilterType) {
                var returnVal = [];
                returnVal = fileItems.filter(function (e) {
                    return (e.getGobiiExtractFilterType() == gobiiExtractFilterType
                        && e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.VERTEX)
                        && e.getProcessType() !== type_process_1.ProcessType.DUMMY;
                }).map(function (fi) { return fi; })
                    .sort(function (gfi_a, gfi_b) {
                    return gfi_a.getItemName().localeCompare(gfi_b.getItemName());
                });
                ;
                return returnVal;
            }));
            exports_1("getFqF2Vertices", getFqF2Vertices = reselect_1.createSelector(getFileItems, getFilters, getGobiiExtractFilterType, function (fileItems, filters, gobiiExtractFilterType) {
                var returnVal = [];
                var f1VertexId = null;
                if (filters[file_item_param_names_1.FilterParamNames.FQ_F1_VERTICES]) {
                    f1VertexId = filters[file_item_param_names_1.FilterParamNames.FQ_F1_VERTICES].targetEntityFilterValue;
                }
                if (f1VertexId) {
                    returnVal = fileItems.filter(function (e) {
                        return (e.getGobiiExtractFilterType() == gobiiExtractFilterType
                            && e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.VERTEX
                            && (e.getEntity().vertexId != f1VertexId
                                || e.getNameIdLabelType() === name_id_label_type_1.NameIdLabelType.SELECT_A))
                            && e.getProcessType() !== type_process_1.ProcessType.DUMMY;
                    }).map(function (fi) { return fi; });
                }
                else {
                    returnVal = fileItems.filter(function (e) {
                        return (e.getGobiiExtractFilterType() == gobiiExtractFilterType
                            && e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.VERTEX
                            && e.getNameIdLabelType() !== name_id_label_type_1.NameIdLabelType.UNKNOWN)
                            && e.getProcessType() !== type_process_1.ProcessType.DUMMY;
                    }).map(function (fi) { return fi; })
                        .sort(function (gfi_a, gfi_b) {
                        return gfi_a.getItemName().localeCompare(gfi_b.getItemName());
                    });
                    ;
                }
                return returnVal;
            }));
            exports_1("getFqF3Vertices", getFqF3Vertices = reselect_1.createSelector(getFileItems, getFilters, getGobiiExtractFilterType, function (fileItems, filters, gobiiExtractFilterType) {
                var returnVal = [];
                var f2VertexId = null;
                if (filters[file_item_param_names_1.FilterParamNames.FQ_F2_VERTICES]) {
                    f2VertexId = filters[file_item_param_names_1.FilterParamNames.FQ_F2_VERTICES].targetEntityFilterValue;
                }
                var f1VertexId = null;
                if (filters[file_item_param_names_1.FilterParamNames.FQ_F1_VERTICES]) {
                    f1VertexId = filters[file_item_param_names_1.FilterParamNames.FQ_F1_VERTICES].targetEntityFilterValue;
                }
                if (f1VertexId && f2VertexId) {
                    returnVal = fileItems.filter(function (e) {
                        return (e.getGobiiExtractFilterType() == gobiiExtractFilterType
                            && e.getEntity().vertexId != f2VertexId
                            && e.getEntity().vertexId != f1VertexId
                            && e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.VERTEX
                            || e.getNameIdLabelType() !== name_id_label_type_1.NameIdLabelType.UNKNOWN)
                            && e.getProcessType() !== type_process_1.ProcessType.DUMMY;
                    }).map(function (fi) { return fi; });
                }
                else {
                    returnVal = fileItems.filter(function (e) {
                        return (e.getGobiiExtractFilterType() == gobiiExtractFilterType
                            && e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.VERTEX
                            && e.getNameIdLabelType() !== name_id_label_type_1.NameIdLabelType.UNKNOWN)
                            && e.getProcessType() !== type_process_1.ProcessType.DUMMY;
                    }).map(function (fi) { return fi; })
                        .sort(function (gfi_a, gfi_b) {
                        return gfi_a.getItemName().localeCompare(gfi_b.getItemName());
                    });
                    ;
                }
                return returnVal;
            }));
            exports_1("getFqF4Vertices", getFqF4Vertices = reselect_1.createSelector(getFileItems, getFilters, getGobiiExtractFilterType, function (fileItems, filters, gobiiExtractFilterType) {
                var returnVal = [];
                var f3VertexId = null;
                if (filters[file_item_param_names_1.FilterParamNames.FQ_F3_VERTICES]) {
                    f3VertexId = filters[file_item_param_names_1.FilterParamNames.FQ_F3_VERTICES].targetEntityFilterValue;
                }
                var f2VertexId = null;
                if (filters[file_item_param_names_1.FilterParamNames.FQ_F2_VERTICES]) {
                    f2VertexId = filters[file_item_param_names_1.FilterParamNames.FQ_F2_VERTICES].targetEntityFilterValue;
                }
                var f1VertexId = null;
                if (filters[file_item_param_names_1.FilterParamNames.FQ_F1_VERTICES]) {
                    f1VertexId = filters[file_item_param_names_1.FilterParamNames.FQ_F1_VERTICES].targetEntityFilterValue;
                }
                if (f1VertexId && f2VertexId && f3VertexId) {
                    returnVal = fileItems.filter(function (e) {
                        return (e.getGobiiExtractFilterType() == gobiiExtractFilterType
                            && e.getEntity().vertexId != f3VertexId
                            && e.getEntity().vertexId != f2VertexId
                            && e.getEntity().vertexId != f1VertexId
                            && e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.VERTEX
                            || e.getNameIdLabelType() !== name_id_label_type_1.NameIdLabelType.UNKNOWN)
                            && e.getProcessType() !== type_process_1.ProcessType.DUMMY;
                    }).map(function (fi) { return fi; });
                }
                else {
                    returnVal = fileItems.filter(function (e) {
                        return (e.getGobiiExtractFilterType() == gobiiExtractFilterType
                            && e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.VERTEX
                            && e.getNameIdLabelType() !== name_id_label_type_1.NameIdLabelType.UNKNOWN)
                            && e.getProcessType() !== type_process_1.ProcessType.DUMMY;
                    }).map(function (fi) { return fi; })
                        .sort(function (gfi_a, gfi_b) {
                        return gfi_a.getItemName().localeCompare(gfi_b.getItemName());
                    });
                }
                return returnVal;
            }));
            exports_1("getFqF1VerticesValues", getFqF1VerticesValues = reselect_1.createSelector(getFileItems, getFilters, getGobiiExtractFilterType, function (fileItems, filters, gobiiExtractFilterType) {
                var returnVal = [];
                var entityType = type_entity_1.EntityType.UNKNOWN;
                if (filters[file_item_param_names_1.FilterParamNames.FQ_F1_VERTEX_VALUES]) {
                    entityType = filters[file_item_param_names_1.FilterParamNames.FQ_F1_VERTEX_VALUES].targetEntityUniqueId.getEntityType();
                }
                if (entityType != type_entity_1.EntityType.UNKNOWN) {
                    returnVal = fileItems.filter(function (e) {
                        return (e.getGobiiExtractFilterType() == type_extractor_filter_1.GobiiExtractFilterType.FLEX_QUERY
                            && e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.VERTEX_VALUE
                            && e.getEntityType() === entityType);
                    }).map(function (fi) { return fi; })
                        .sort(function (gfi_a, gfi_b) {
                        return gfi_a.getItemName().localeCompare(gfi_b.getItemName());
                    });
                }
                return returnVal;
            }));
            exports_1("getFqF2VerticesValues", getFqF2VerticesValues = reselect_1.createSelector(getFileItems, getFilters, getGobiiExtractFilterType, function (fileItems, filters, gobiiExtractFilterType) {
                var returnVal = [];
                var entityType = type_entity_1.EntityType.UNKNOWN;
                if (filters[file_item_param_names_1.FilterParamNames.FQ_F2_VERTEX_VALUES]) {
                    entityType = filters[file_item_param_names_1.FilterParamNames.FQ_F2_VERTEX_VALUES].targetEntityUniqueId.getEntityType();
                }
                if (entityType != type_entity_1.EntityType.UNKNOWN) {
                    returnVal = fileItems.filter(function (e) {
                        return (e.getGobiiExtractFilterType() == type_extractor_filter_1.GobiiExtractFilterType.FLEX_QUERY
                            && e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.VERTEX_VALUE
                            && e.getEntityType() === entityType);
                    }).map(function (fi) { return fi; })
                        .sort(function (gfi_a, gfi_b) {
                        return gfi_a.getItemName().localeCompare(gfi_b.getItemName());
                    });
                    ;
                }
                return returnVal;
            }));
            exports_1("getFqF3VerticesValues", getFqF3VerticesValues = reselect_1.createSelector(getFileItems, getFilters, getGobiiExtractFilterType, function (fileItems, filters, gobiiExtractFilterType) {
                var returnVal = [];
                var entityType = type_entity_1.EntityType.UNKNOWN;
                if (filters[file_item_param_names_1.FilterParamNames.FQ_F3_VERTEX_VALUES]) {
                    entityType = filters[file_item_param_names_1.FilterParamNames.FQ_F3_VERTEX_VALUES].targetEntityUniqueId.getEntityType();
                }
                if (entityType != type_entity_1.EntityType.UNKNOWN) {
                    returnVal = fileItems.filter(function (e) {
                        return (e.getGobiiExtractFilterType() == type_extractor_filter_1.GobiiExtractFilterType.FLEX_QUERY
                            && e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.VERTEX_VALUE
                            && e.getEntityType() === entityType);
                    }).map(function (fi) { return fi; })
                        .sort(function (gfi_a, gfi_b) {
                        return gfi_a.getItemName().localeCompare(gfi_b.getItemName());
                    });
                    ;
                }
                return returnVal;
            }));
            exports_1("getFqF4VerticesValues", getFqF4VerticesValues = reselect_1.createSelector(getFileItems, getFilters, getGobiiExtractFilterType, function (fileItems, filters, gobiiExtractFilterType) {
                var returnVal = [];
                var entityType = type_entity_1.EntityType.UNKNOWN;
                if (filters[file_item_param_names_1.FilterParamNames.FQ_F4_VERTEX_VALUES]) {
                    entityType = filters[file_item_param_names_1.FilterParamNames.FQ_F4_VERTEX_VALUES].targetEntityUniqueId.getEntityType();
                }
                if (entityType != type_entity_1.EntityType.UNKNOWN) {
                    returnVal = fileItems.filter(function (e) {
                        return (e.getGobiiExtractFilterType() == type_extractor_filter_1.GobiiExtractFilterType.FLEX_QUERY
                            && e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.VERTEX_VALUE
                            && e.getEntityType() === entityType);
                    }).map(function (fi) { return fi; })
                        .sort(function (gfi_a, gfi_b) {
                        return gfi_a.getItemName().localeCompare(gfi_b.getItemName());
                    });
                    ;
                }
                return returnVal;
            }));
            exports_1("getCurrentMarkerCount", getCurrentMarkerCount = reselect_1.createSelector(getFileItems, getFilters, getGobiiExtractFilterType, function (fileItems, filters, gobiiExtractFilterType) {
                var returnVal = -1;
                var countFileItems = fileItems.filter(function (e) {
                    return (e.getGobiiExtractFilterType() == type_extractor_filter_1.GobiiExtractFilterType.FLEX_QUERY
                        && e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.ITEM_COUNT
                        && e.getEntityType() === type_entity_1.EntityType.MARKER);
                });
                if (countFileItems.length > 0) {
                    returnVal = countFileItems[0].getEntity();
                }
                return returnVal;
            }));
            exports_1("getCurrentSampleCount", getCurrentSampleCount = reselect_1.createSelector(getFileItems, getFilters, getGobiiExtractFilterType, function (fileItems, filters, gobiiExtractFilterType) {
                var returnVal = -1;
                var countFileItems = fileItems.filter(function (e) {
                    return (e.getGobiiExtractFilterType() == type_extractor_filter_1.GobiiExtractFilterType.FLEX_QUERY
                        && e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.ITEM_COUNT
                        && e.getEntityType() === type_entity_1.EntityType.DNA_SAMPLE);
                });
                if (countFileItems.length > 0) {
                    returnVal = countFileItems[0].getEntity();
                }
                return returnVal;
            }));
            exports_1("getFilterCountState", getFilterCountState = reselect_1.createSelector(getFileItems, getFilters, getGobiiExtractFilterType, function (fileItems, filters, gobiiExtractFilterType) {
                var returnVal = new action_filter_count_state_1.FilterCountState(new Map(), -1, -1);
                Object.keys(filters).forEach(function (key) {
                    if (key === file_item_param_names_1.FilterParamNames.FQ_F1_VERTEX_VALUES ||
                        key === file_item_param_names_1.FilterParamNames.FQ_F2_VERTEX_VALUES ||
                        key === file_item_param_names_1.FilterParamNames.FQ_F3_VERTEX_VALUES ||
                        key === file_item_param_names_1.FilterParamNames.FQ_F4_VERTEX_VALUES ||
                        key === file_item_param_names_1.FilterParamNames.FQ_F1_VERTICES ||
                        key === file_item_param_names_1.FilterParamNames.FQ_F2_VERTICES ||
                        key === file_item_param_names_1.FilterParamNames.FQ_F3_VERTICES ||
                        key === file_item_param_names_1.FilterParamNames.FQ_F4_VERTICES) {
                        returnVal.flexQueryFilters.set(key, filters[key]);
                    }
                }); //forEach filter
                var dnaSampleCountFileItems = fileItems.filter(function (e) {
                    return (e.getGobiiExtractFilterType() == type_extractor_filter_1.GobiiExtractFilterType.FLEX_QUERY
                        && e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.ITEM_COUNT
                        && e.getEntityType() === type_entity_1.EntityType.DNA_SAMPLE);
                });
                if (dnaSampleCountFileItems.length > 0) {
                    returnVal.sampleCount = dnaSampleCountFileItems[0].getEntity();
                }
                var markerCountFileItems = fileItems.filter(function (e) {
                    return (e.getGobiiExtractFilterType() == type_extractor_filter_1.GobiiExtractFilterType.FLEX_QUERY
                        && e.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.ITEM_COUNT
                        && e.getEntityType() === type_entity_1.EntityType.MARKER);
                });
                if (markerCountFileItems.length > 0) {
                    returnVal.markerCount = markerCountFileItems[0].getEntity();
                }
                return returnVal;
            }));
        }
    };
});
//# sourceMappingURL=fileitems-reducer.js.map