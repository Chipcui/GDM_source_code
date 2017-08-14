System.register(["reselect", "@ngrx/store", "ngrx-store-freeze", "./fileitems-reducer", "./treenode-reducer"], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var reselect_1, store_1, ngrx_store_freeze_1, store_2, fromFileItems, fromGobiiTreeNodes, reducers, developmentReducer, productionReducer, getFileItemsState, getAllFileItems, getFileItems, getSelectedFileItems, getSelectedUniqueIds, getUniqueIds, getGobiiTreeNodesState, getAllGobiiTreeNodes, getGobiiTreeNodesForExtractFilter, getSelectedGobiiTreeNodes, getSelectedGobiiTreeNodeIds, getIdsOfActivatedGobiiTreeNodes;
    return {
        setters: [
            function (reselect_1_1) {
                reselect_1 = reselect_1_1;
            },
            function (store_1_1) {
                store_1 = store_1_1;
                store_2 = store_1_1;
            },
            function (ngrx_store_freeze_1_1) {
                ngrx_store_freeze_1 = ngrx_store_freeze_1_1;
            },
            function (fromFileItems_1) {
                fromFileItems = fromFileItems_1;
            },
            function (fromGobiiTreeNodes_1) {
                fromGobiiTreeNodes = fromGobiiTreeNodes_1;
            }
        ],
        execute: function () {
            exports_1("reducers", reducers = {
                fileItems: fromFileItems.fileItemsReducer,
                gobiiTreeNodes: fromGobiiTreeNodes.gobiiTreeNodesReducer,
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
            developmentReducer = store_1.compose(ngrx_store_freeze_1.storeFreeze, store_2.combineReducers)(reducers);
            productionReducer = store_2.combineReducers(reducers);
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
            exports_1("getAllFileItems", getAllFileItems = reselect_1.createSelector(getFileItemsState, fromFileItems.getAll));
            exports_1("getFileItems", getFileItems = reselect_1.createSelector(getFileItemsState, fromFileItems.getFileItems));
            exports_1("getSelectedFileItems", getSelectedFileItems = reselect_1.createSelector(getFileItemsState, fromFileItems.getSelected));
            exports_1("getSelectedUniqueIds", getSelectedUniqueIds = reselect_1.createSelector(getFileItemsState, fromFileItems.getSelectedUniqueIds));
            exports_1("getUniqueIds", getUniqueIds = reselect_1.createSelector(getFileItemsState, fromFileItems.getUniqueIds));
            //
            exports_1("getGobiiTreeNodesState", getGobiiTreeNodesState = function (state) { return state.gobiiTreeNodes; });
            exports_1("getAllGobiiTreeNodes", getAllGobiiTreeNodes = reselect_1.createSelector(getGobiiTreeNodesState, fromGobiiTreeNodes.getAll));
            exports_1("getGobiiTreeNodesForExtractFilter", getGobiiTreeNodesForExtractFilter = reselect_1.createSelector(getGobiiTreeNodesState, fromGobiiTreeNodes.getForSelectedFilter));
            exports_1("getSelectedGobiiTreeNodes", getSelectedGobiiTreeNodes = reselect_1.createSelector(getGobiiTreeNodesState, fromGobiiTreeNodes.getSelected));
            exports_1("getSelectedGobiiTreeNodeIds", getSelectedGobiiTreeNodeIds = reselect_1.createSelector(getGobiiTreeNodesState, fromGobiiTreeNodes.getGobiiTreeItemIds));
            exports_1("getIdsOfActivatedGobiiTreeNodes", getIdsOfActivatedGobiiTreeNodes = reselect_1.createSelector(getGobiiTreeNodesState, fromGobiiTreeNodes.getIdsOfActivated));
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