System.register(["@ngrx/store", "ngrx-store-freeze", "./fileitems-reducer", "./treenode-reducer", "./history-reducer"], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var store_1, store_2, ngrx_store_freeze_1, store_3, fromFileItems, fromGobiiTreeNodes, fromHistory, reducers, developmentReducer, productionReducer, getFileItemsState, getFileItemsFilters, getAllFileItems, getSelectedFileItems, getPiContacts, getProjects, getExperiments, getDatasets, getCvTermsDataType, getCvTermsJobStatus, getMapsets, getPlatforms, getMarkerGroups, getProjectsByPI, getExperimentsByProject, getDatasetsByExperiment, getSelectedPiContacts, getDatsetEntities, getDatsetEntitiesPaged, getPiContactsFilterOptional, getProjectsFilterOptional, getExperimentsFilterOptional, getSelectedFileFormat, getSelectedSampleType, getJobId, getUploadFiles, getGobiiTreeNodesState, getAllGobiiTreeNodes, getGobiiTreeNodesForExtractFilter, getSelectedGobiiTreeNodes, getSelectedGobiiTreeNodeIds, getIdsOfActivatedGobiiTreeNodes, gobiiHistoryState, getStatusMessages, getLasetStatusMessage, getFiltersRetrieved, getFqF1Vertices, getFqF2Vertices, getFqF3Vertices, getFqF4Vertices, getFqF1VerticesValues, getFqF2VerticesValues, getFqF3VerticesValues, getFqF4VerticesValues, getCurrentMarkerCount, getCurrentSampleCount, getFilterCountState;
    return {
        setters: [
            function (store_1_1) {
                store_1 = store_1_1;
                store_2 = store_1_1;
                store_3 = store_1_1;
            },
            function (ngrx_store_freeze_1_1) {
                ngrx_store_freeze_1 = ngrx_store_freeze_1_1;
            },
            function (fromFileItems_1) {
                fromFileItems = fromFileItems_1;
            },
            function (fromGobiiTreeNodes_1) {
                fromGobiiTreeNodes = fromGobiiTreeNodes_1;
            },
            function (fromHistory_1) {
                fromHistory = fromHistory_1;
            }
        ],
        execute: function () {
            exports_1("reducers", reducers = {
                fileItems: fromFileItems.fileItemsReducer,
                gobiiTreeNodes: fromGobiiTreeNodes.gobiiTreeNodesReducer,
                history: fromHistory.historyReducer
            });
            /**
             * Because metareducers take a fileItemsReducer function and return a new fileItemsReducer,
             * we can use our compose helper to chain them together. Here we are
             * using combineReducers to make our top level fileItemsReducer, and then
             * wrapping that in storeLogger. Remember that compose applies
             * the result from right to left.
             */
            // const reducers = {
            //     fileItems: fromFileItems.fileItemsReducer
            //     // books: fromBooks.fileItemsReducer,
            //     // collection: fromCollection.fileItemsReducer,
            //     // layout: fromLayout.fileItemsReducer,
            //     // router: fromRouter.routerReducer,
            // };
            developmentReducer = store_2.compose(ngrx_store_freeze_1.storeFreeze, store_3.combineReducers)(reducers);
            productionReducer = store_3.combineReducers(reducers);
            // export function reducer(state: any, action: any) {
            //     if (environmentSettings.production) {
            //         return productionReducer(state, action);
            //     } else {
            //         return developmentReducer(state, action);
            //     }
            //  }
            /**
             * A selector function is a map function factory. We pass it parameters and it
             * returns a function that maps from the larger state tree into a smaller
             * piece of state. This selector simply selects the `books` state.
             *
             * Selectors are used with the `select` operator.
             *
             * ```ts
             * class MyComponent {
             * 	constructor(state$: Observable<State>) {
             * 	  this.booksState$ = state$.select(getFileItemsState);
             * 	}
             * }
             * ```
             */
            exports_1("getFileItemsState", getFileItemsState = function (state) { return state.fileItems; });
            /**
             * Every fileItemsReducer module exports selector functions, however child reducers
             * have no knowledge of the overall state tree. To make them useable, we
             * need to make new selectors that wrap them.
             *
             * The createSelector function from the reselect library creates
             * very efficient selectors that are memoized and only recompute when arguments change.
             * The created selectors can also be composed together to select different
             * pieces of state.
             */
            exports_1("getFileItemsFilters", getFileItemsFilters = store_1.createSelector(getFileItemsState, fromFileItems.getFilters));
            exports_1("getAllFileItems", getAllFileItems = store_1.createSelector(getFileItemsState, fromFileItems.getAll));
            exports_1("getSelectedFileItems", getSelectedFileItems = store_1.createSelector(getFileItemsState, fromFileItems.getSelected));
            exports_1("getPiContacts", getPiContacts = store_1.createSelector(getFileItemsState, fromFileItems.getPiContacts));
            exports_1("getProjects", getProjects = store_1.createSelector(getFileItemsState, fromFileItems.getProjects));
            exports_1("getExperiments", getExperiments = store_1.createSelector(getFileItemsState, fromFileItems.getExperiments));
            exports_1("getDatasets", getDatasets = store_1.createSelector(getFileItemsState, fromFileItems.getDatasets));
            exports_1("getCvTermsDataType", getCvTermsDataType = store_1.createSelector(getFileItemsState, fromFileItems.getCvTermsDataType));
            exports_1("getCvTermsJobStatus", getCvTermsJobStatus = store_1.createSelector(getFileItemsState, fromFileItems.getCvTermsJobStatus));
            exports_1("getMapsets", getMapsets = store_1.createSelector(getFileItemsState, fromFileItems.getMapsets));
            exports_1("getPlatforms", getPlatforms = store_1.createSelector(getFileItemsState, fromFileItems.getPlatforms));
            exports_1("getMarkerGroups", getMarkerGroups = store_1.createSelector(getFileItemsState, fromFileItems.getMarkerGroups));
            exports_1("getProjectsByPI", getProjectsByPI = store_1.createSelector(getFileItemsState, fromFileItems.getProjectsForSelectedPi));
            exports_1("getExperimentsByProject", getExperimentsByProject = store_1.createSelector(getFileItemsState, fromFileItems.getExperimentsForSelectedProject));
            exports_1("getDatasetsByExperiment", getDatasetsByExperiment = store_1.createSelector(getFileItemsState, fromFileItems.getDatasetsForSelectedExperiment));
            exports_1("getSelectedPiContacts", getSelectedPiContacts = store_1.createSelector(getFileItemsState, fromFileItems.getSelectedPiContacts));
            exports_1("getDatsetEntities", getDatsetEntities = store_1.createSelector(getFileItemsState, fromFileItems.getDatasetEntities));
            exports_1("getDatsetEntitiesPaged", getDatsetEntitiesPaged = store_1.createSelector(getFileItemsState, fromFileItems.getDatasetEntitiesPaged));
            exports_1("getPiContactsFilterOptional", getPiContactsFilterOptional = store_1.createSelector(getFileItemsState, fromFileItems.getPiContactsFilterOptional));
            exports_1("getProjectsFilterOptional", getProjectsFilterOptional = store_1.createSelector(getFileItemsState, fromFileItems.getProjectsFilterOptional));
            exports_1("getExperimentsFilterOptional", getExperimentsFilterOptional = store_1.createSelector(getFileItemsState, fromFileItems.getExperimentsFilterOptional));
            exports_1("getSelectedFileFormat", getSelectedFileFormat = store_1.createSelector(getFileItemsState, fromFileItems.getSelectedFileFormat));
            exports_1("getSelectedSampleType", getSelectedSampleType = store_1.createSelector(getFileItemsState, fromFileItems.getSelectedSampleType));
            exports_1("getJobId", getJobId = store_1.createSelector(getFileItemsState, fromFileItems.getJobId));
            exports_1("getUploadFiles", getUploadFiles = store_1.createSelector(getFileItemsState, fromFileItems.getUploadFiles));
            exports_1("getGobiiTreeNodesState", getGobiiTreeNodesState = function (state) { return state.gobiiTreeNodes; });
            exports_1("getAllGobiiTreeNodes", getAllGobiiTreeNodes = store_1.createSelector(getGobiiTreeNodesState, fromGobiiTreeNodes.getAll));
            exports_1("getGobiiTreeNodesForExtractFilter", getGobiiTreeNodesForExtractFilter = store_1.createSelector(getGobiiTreeNodesState, fromGobiiTreeNodes.getForSelectedFilter));
            exports_1("getSelectedGobiiTreeNodes", getSelectedGobiiTreeNodes = store_1.createSelector(getGobiiTreeNodesState, fromGobiiTreeNodes.getSelected));
            exports_1("getSelectedGobiiTreeNodeIds", getSelectedGobiiTreeNodeIds = store_1.createSelector(getGobiiTreeNodesState, fromGobiiTreeNodes.getGobiiTreeItemIds));
            exports_1("getIdsOfActivatedGobiiTreeNodes", getIdsOfActivatedGobiiTreeNodes = store_1.createSelector(getGobiiTreeNodesState, fromGobiiTreeNodes.getIdsOfActivated));
            exports_1("gobiiHistoryState", gobiiHistoryState = function (state) { return state.history; });
            exports_1("getStatusMessages", getStatusMessages = store_1.createSelector(gobiiHistoryState, fromHistory.getStatusMessages));
            exports_1("getLasetStatusMessage", getLasetStatusMessage = store_1.createSelector(gobiiHistoryState, fromHistory.getLastMessage));
            exports_1("getFiltersRetrieved", getFiltersRetrieved = store_1.createSelector(gobiiHistoryState, fromHistory.getFiltersRetrieved));
            exports_1("getFqF1Vertices", getFqF1Vertices = store_1.createSelector(getFileItemsState, fromFileItems.getFqF1Vertices));
            exports_1("getFqF2Vertices", getFqF2Vertices = store_1.createSelector(getFileItemsState, fromFileItems.getFqF2Vertices));
            exports_1("getFqF3Vertices", getFqF3Vertices = store_1.createSelector(getFileItemsState, fromFileItems.getFqF3Vertices));
            exports_1("getFqF4Vertices", getFqF4Vertices = store_1.createSelector(getFileItemsState, fromFileItems.getFqF4Vertices));
            exports_1("getFqF1VerticesValues", getFqF1VerticesValues = store_1.createSelector(getFileItemsState, fromFileItems.getFqF1VerticesValues));
            exports_1("getFqF2VerticesValues", getFqF2VerticesValues = store_1.createSelector(getFileItemsState, fromFileItems.getFqF2VerticesValues));
            exports_1("getFqF3VerticesValues", getFqF3VerticesValues = store_1.createSelector(getFileItemsState, fromFileItems.getFqF3VerticesValues));
            exports_1("getFqF4VerticesValues", getFqF4VerticesValues = store_1.createSelector(getFileItemsState, fromFileItems.getFqF4VerticesValues));
            exports_1("getCurrentMarkerCount", getCurrentMarkerCount = store_1.createSelector(getFileItemsState, fromFileItems.getCurrentMarkerCount));
            exports_1("getCurrentSampleCount", getCurrentSampleCount = store_1.createSelector(getFileItemsState, fromFileItems.getCurrentSampleCount));
            exports_1("getFilterCountState", getFilterCountState = store_1.createSelector(getFileItemsState, fromFileItems.getFilterCountState));
            /**
             * Just like with the books selectors, we also have to compose the search
             * fileItemsReducer's and collection fileItemsReducer's selectors.
             */
            // export const getSearchState = (state: State) => state.search;
            // export const getSearchBookIds = createSelector(getSearchState, fromSearch.getGobiiTreeItemIds);
            // export const getSearchQuery = createSelector(getSearchState, fromSearch.getQuery);
            // export const getSearchLoading = createSelector(getSearchState, fromSearch.getLoading);
            /**
             * Some selector functions create joins across parts of state. This selector
             * composes the search result IDs to return an array of books in the store.
             */
            // export const getSearchResults = createSelector(getAllFileItems, getSearchBookIds, (books, searchIds) => {
            //     return searchIds.map(id => books[id]);
            // });
            // export const getCollectionState = (state: State) => state.collection;
            //
            // export const getCollectionLoaded = createSelector(getCollectionState, fromCollection.getLoaded);
            // export const getCollectionLoading = createSelector(getCollectionState, fromCollection.getLoading);
            // export const getCollectionBookIds = createSelector(getCollectionState, fromCollection.getGobiiTreeItemIds);
            //
            // export const getBookCollection = createSelector(getAllFileItems, getCollectionBookIds, (entities, ids) => {
            //     return ids.map(id => entities[id]);
            // });
            //
            // export const isSelectedBookInCollection = createSelector(getCollectionBookIds, getFileItems, (ids, selected) => {
            //     return ids.indexOf(selected) > -1;
            // });
            /**
             * Layout Reducers
             */
            // export const getLayoutState = (state: State) => state.layout;
            //
            // export const getShowSidenav = createSelector(getLayoutState, fromLayout.getShowSidenav);
        }
    };
});
//# sourceMappingURL=index.js.map