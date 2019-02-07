System.register(["@angular/core", "@angular/router", "@ngrx/effects", "rxjs/add/operator/switchMap", "rxjs/add/observable/of", "rxjs/add/operator/concat", "../actions/fileitem-action", "../actions/treenode-action", "../../services/core/tree-structure-service", "../reducers", "../../store/actions/history-action", "../../model/type-extractor-item", "rxjs/Observable", "@ngrx/store", "../../services/core/file-item-service", "../../model/file-item-param-names", "rxjs/add/operator/mergeMap", "../../services/core/filter-params-coll"], function (exports_1, context_1) {
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
    var core_1, router_1, effects_1, fileItemActions, treeNodeActions, tree_structure_service_1, fromRoot, historyAction, type_extractor_item_1, Observable_1, store_1, file_item_service_1, file_item_param_names_1, filter_params_coll_1, FileItemEffects;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (router_1_1) {
                router_1 = router_1_1;
            },
            function (effects_1_1) {
                effects_1 = effects_1_1;
            },
            function (_1) {
            },
            function (_2) {
            },
            function (_3) {
            },
            function (fileItemActions_1) {
                fileItemActions = fileItemActions_1;
            },
            function (treeNodeActions_1) {
                treeNodeActions = treeNodeActions_1;
            },
            function (tree_structure_service_1_1) {
                tree_structure_service_1 = tree_structure_service_1_1;
            },
            function (fromRoot_1) {
                fromRoot = fromRoot_1;
            },
            function (historyAction_1) {
                historyAction = historyAction_1;
            },
            function (type_extractor_item_1_1) {
                type_extractor_item_1 = type_extractor_item_1_1;
            },
            function (Observable_1_1) {
                Observable_1 = Observable_1_1;
            },
            function (store_1_1) {
                store_1 = store_1_1;
            },
            function (file_item_service_1_1) {
                file_item_service_1 = file_item_service_1_1;
            },
            function (file_item_param_names_1_1) {
                file_item_param_names_1 = file_item_param_names_1_1;
            },
            function (_4) {
            },
            function (filter_params_coll_1_1) {
                filter_params_coll_1 = filter_params_coll_1_1;
            }
        ],
        execute: function () {
            FileItemEffects = (function () {
                // @Effect()
                // setEntityFilter$ = this.actions$
                //     .ofType(fileItemActions.SET_FILTER_VALUE)
                //     .switchMap((action: fileItemActions.SetFilterValueAction)  => {
                //
                //         let payload = action.payload;
                //
                //         return Observable.create(observer => {
                //
                //
                //             this.nameIdService.get(payload.nameIdRequestParams)
                //                 .subscribe(nameIds => {
                //                         if (nameIds && ( nameIds.length > 0 )) {
                //
                //
                //                             nameIds.forEach(n => {
                //                                 let currentFileItem: GobiiFileItem =
                //                                     GobiiFileItem.build(
                //                                         payload.gobiiExtractFilterType,
                //                                         ProcessType.CREATE)
                //                                         .setExtractorItemType(ExtractorItemType.ENTITY)
                //                                         .setEntityType(payload.nameIdRequestParams.getEntityType())
                //                                         .setCvFilterType(CvFilterType.UNKNOWN)
                //                                         .setItemId(n.id)
                //                                         .setItemName(n.name)
                //                                         .setSelected(false)
                //                                         .setRequired(false)
                //                                         .setParentEntityType(payload.nameIdRequestParams.getRefTargetEntityType())
                //                                         .setParentItemId(payload.nameIdRequestParams.getFkEntityFilterValue());
                //
                //                                 //fileItems.push(currentFileItem);
                //                                 observer.next(currentFileItem);
                //
                //                             });
                //
                //                             //new fileItemActions.LoadAction(fileItems);
                //                         }
                //                     },
                //                     responseHeader => {
                //                         console.log(responseHeader);
                //                     });
                //
                //         }).map( gfi => {
                //             return new fileItemActions.LoadAction([gfi]);
                //         })
                //
                //
                //     }); //switch map
                function FileItemEffects(actions$, treeStructureService, fileItemService, store, filterParamsColl, router) {
                    var _this = this;
                    this.actions$ = actions$;
                    this.treeStructureService = treeStructureService;
                    this.fileItemService = fileItemService;
                    this.store = store;
                    this.filterParamsColl = filterParamsColl;
                    this.router = router;
                    /**
                     * The canonical use case for effects is when you want to add the results of an asynchronous
                     * operation to the store. In Angular/Redux speak, it is said that doing so in a reducer
                     * would be adding a side effect. I think of this as meaning that if you have an asynch operation,
                     * you are not guaranteeing that when the reducer function returns, the state will be as you believe
                     * it should be based on the action you submitted -- not until the subscribe's callback gets called
                     * in any case. So, the idea is that the effects are like extensions to the reducer, such that actions
                     * get seen here as well as in the reducer. Thus, if you have an action that is handled _only_ by an
                     * effect, the effect can subscribe, for example, to an http request and once all the data have been
                     * retrieved, then the effect will do an old fashioned dispatch where the action is handled directly
                     * by the reducer.
                     * The effects in this project look a little different. First of call, the FileItemSErvice class does what
                     * you would normally do in an effect -- does an asynchronous call to web services and, upon fulfillment
                     * of the subscription, adds the items to the store. The only article I can find that deals with this issue head on is
                     * https://medium.com/@flashMasterJim/the-basics-of-ngrx-effects-effect-and-async-middleware-for-ngrx-store-in-angular-2-f25587493329
                     * by Jim Lunch. He says "You might be thinking, “What if you have the smart component just communicate with
                     * another service that calls for async data, and then when that call comes back the service dispatches an event to
                     * the store with the returned data as a payload?”, and in a way you’d be right!" So, am I right, or not? Only
                     * time will tell: so far,this seems to be working ok.
                     * Now, secondly, it turns out that you actually can handle an action in both an effect _and_ a reducer.
                     * Based on experimentation and googling, I found that the effect should get called _after_ the
                     * reducer, and this is by the design. In other words, another type of side effect is where you want to
                     * do something as a result of adding something to state, but would violate the reducer contract by doing
                     * so in the reducer. I use this pattern all over the palce. For example, when I set a file item to be
                     * in the extract, I want the tree state to change along with it. This may be a design flaw -- making
                     * file items more complex by combining them with the TreeNode type would have the payoff of being able to
                     * maintain one state. I suspect that in the future we will want the clean separation, and in any case, this
                     * is how it is for now. I think in a sense that this pattern fits with the canonical use case because really,
                     * we want to do something to the tree, but the thing we want to do to it depends on the result of an asynchronous
                     * action -- that of adding stuff to the file item store. I asked Gerald sans about this second usage
                     * at the Angular Summit and he didn't seem to get my question: he said unequivocally that the only
                     * purpose for efffects is what I describe. But I suspect that if I showed him what I am trying to do
                     * he would agree. Again, I am quite sure that what I am doing here is an intended use pattern (there
                     * was even an ngrx/pltform issue to ensure that an action in an effect is called after the same action
                     * in a reducer.
                     *
                     *
                     * @type {Observable<PlaceTreeNodeAction>}
                     */
                    // this effect acts on the content of the tree nodes (e.g., their names and icons so forth)
                    // and then dispatches them to the tree node reducer. The tree node reducer holds the nodes
                    // in state. Thus, the hierarchical arrangement of nodes is managed by the reducer in
                    // accordance with how the nodes are defined by the tree service.
                    this.selectForExtract$ = this.actions$
                        .ofType(fileItemActions.ADD_TO_EXTRACT)
                        .map(function (action) {
                        var treeNode = _this.treeStructureService.makeTreeNodeFromFileItem(action.payload);
                        return new treeNodeActions.PlaceTreeNodeAction(treeNode);
                    });
                    this.replaceSameByCompoundId$ = this.actions$
                        .ofType(fileItemActions.REPLACE_ITEM_OF_SAME_COMPOUND_ID)
                        .map(function (action) {
                        var treeNode = _this.treeStructureService.makeTreeNodeFromFileItem(action.payload.gobiiFileitemToReplaceWith);
                        return new treeNodeActions.PlaceTreeNodeAction(treeNode);
                    });
                    this.loadFileItemListWithFilter$ = this.actions$
                        .ofType(fileItemActions.LOAD_FILE_ITEM_LIST_WITH_FILTER)
                        .switchMap(function (action) {
                        return Observable_1.Observable.create(function (observer) {
                            var addFilterSubmittedAction = new historyAction
                                .AddFilterRetrieved({
                                gobiiExtractFilterType: action.payload.filter.gobiiExtractFilterType,
                                filterId: action.payload.filterId,
                                filterValue: action.payload.filter.relatedEntityFilterValue,
                                entityLasteUpdated: action.payload.filter.entityLasteUpdated
                            });
                            observer.next(addFilterSubmittedAction);
                            var filterParams = _this.filterParamsColl.getFilter(action.payload.filterId, action.payload.filter.gobiiExtractFilterType);
                            var gobiiFileItems = action.payload.gobiiFileItems;
                            var payloadFilter = action.payload.filter;
                            if (filterParams && filterParams.getOnLoadFilteredItemsAction() !== null) {
                                var action_1 = filterParams.getOnLoadFilteredItemsAction()(gobiiFileItems, payloadFilter);
                                if (action_1) {
                                    observer.next(action_1);
                                }
                            }
                        }).mergeMap(function (actions) {
                            return Observable_1.Observable.of(actions);
                        });
                    });
                    /***
                     * The LOAD_FILTER action was a good idea: you would first LOAD the filter into the store.
                     * There would then be an effect, as we have here, that would actually load the file items
                     * corresponding to the filter. This, it was reasoned, would make it easier to do hierarchical
                     * filtering with modification-date-sensitive updates: to do hierarchial filtering, you have to
                     * know the result of the parent entity's query, all of which was not working well with the
                     * existing algorithm. However, I could not get the retrieval of file items from an effect to
                     * work correctly. When you have a series of such actions -- that is, where the LOAD_FILTER
                     * action hits the store, say, four times, observer.next() is hit four times, but the map
                     * handing of the Observer.create() method only gets hit once. The code that's commented out
                     * here was trying to manually boil this problem down to its simplest expression rather than
                     * using the file action creating methods of the FileItemService. I spent a day trying to
                     * get this to work and could not figure it out. So for now I am abaonding this approach.
                     * From my recent experience with retrieval history in the FileItemService::loadFileItems() method,
                     * I now suspect that somewhere something is generating a side effect that causes the store to
                     * be updated in such a way that the subsequent actions don't go through. That somehow the observer
                     * works get gummed up.
                     *
                     */
                    // @Effect()
                    // loadFileItemsForFilter$ = this.actions$
                    //     .ofType(fileItemActions.LOAD_FILTER)
                    //     .switchMap((action: fileItemActions.LoadFilterAction) => {
                    //
                    //         return Observable.create(observer => {
                    //
                    //             let nameIdRequestParamsToLoad: FileItemParams = FileItemParams
                    //                 .build(NameIdFilterParamTypes.CONTACT_PI,
                    //                     GobiiExtractFilterType.WHOLE_DATASET,
                    //                     EntityType.CONTACT)
                    //                 .setIsDynamicFilterValue(true)
                    //                 .setEntitySubType(EntitySubType.CONTACT_PRINCIPAL_INVESTIGATOR);
                    //
                    //                     this.nameIdService.get(nameIdRequestParamsToLoad)
                    //                         .subscribe(nameIds => {
                    //
                    //                             let minEntityLastUpdated: Date;
                    //                             let fileItems: GobiiFileItem[] = [];
                    //                             if (nameIds && ( nameIds.length > 0 )) {
                    //
                    //                                 nameIds.forEach(n => {
                    //                                     let currentFileItem: GobiiFileItem =
                    //                                         GobiiFileItem.build(
                    //                                             GobiiExtractFilterType.WHOLE_DATASET,
                    //                                             ProcessType.CREATE)
                    //                                             .setExtractorItemType(ExtractorItemType.ENTITY)
                    //                                             .setEntityType(nameIdRequestParamsToLoad.getEntityType())
                    //                                             .setEntitySubType(nameIdRequestParamsToLoad.getEntitySubType())
                    //                                             .setCvFilterType(nameIdRequestParamsToLoad.getCvFilterType())
                    //                                             .setItemId(n.id)
                    //                                             .setItemName(n.name)
                    //                                             .setSelected(false)
                    //                                             .setRequired(false)
                    //                                             .setParentItemId(nameIdRequestParamsToLoad.getFkEntityFilterValue());
                    //
                    //
                    //                                     fileItems.push(currentFileItem);
                    //                                 });
                    //                                 observer.next(fileItems);
                    //                             }
                    //                         });//subscsribe
                    //
                    //         }).map(fileItems =>
                    //             new fileItemActions.LoadFileItemListAction({gobiiFileItems: fileItems})
                    //         ).catch(error => {
                    //             console.log(error);
                    //         })
                    //
                    //         // return Observable.create(observer => {
                    //         //     this.fileItemService.makeFileLoadActions(action.payload.filter.gobiiExtractFilterType,
                    //         //         action.payload.filterId,
                    //         //         action.payload.filter.filterValue)
                    //         //         .subscribe(loadFileItemsAction => {
                    //         //
                    //         //             observer.next(loadFileItemsAction.payload.gobiiFileItems);
                    //         //             observer.complete();
                    //         //         });
                    //         //     //subscribe --> loadFIleItemsFromFilter()
                    //         // }).map( fileItems => new fileItemActions.LoadFileItemListAction({gobiiFileItems: fileItems}));
                    //     });// switchMap()
                    this.replaceInExtract$ = this.actions$
                        .ofType(fileItemActions.REPLACE_BY_ITEM_ID)
                        .switchMap(function (action) {
                        //  This action is triggered by the ubiguitous NameIdListBoxComponent
                        // as such, there are business behaviors that must be implemented here.
                        // you cannot trigger an ASYNCH requrest such as loadWithFilterParams() from within
                        // the subscribe of a reducer.select(): if you do, you end up with an infinite loop
                        // There is a flaw here: this action assigns a filter type for further processing baseed on
                        // entity name; but that's incorrect: not all replace actions should result in further processing
                        // -- only the ones involved with hierarchical queries do. The ReplaceInExtractByItemIdAction
                        // should include the filterId and then delegate this part of the processing to FileItemService,
                        // which specializes in the filter types.
                        return Observable_1.Observable.create(function (observer) {
                            var fileItemToReplaceWithUniqueId = action.payload.itemIdToReplaceItWith;
                            var fileItemCurrentlyInExtractUniqueId = action.payload.itemIdCurrentlyInExtract;
                            var filterParamName = action.payload.filterParamName;
                            _this.store.select(fromRoot.getAllFileItems)
                                .subscribe(function (all) {
                                var fileItemToReplaceWith = all.find(function (fi) { return fi.getFileItemUniqueId() === fileItemToReplaceWithUniqueId; });
                                // RUN FILTERED QUERY TO GET CHILD ITEMS WHEN NECESSARY
                                //If item ID is 0, is a label item, and so for filtering purposes it's null
                                var filterValue = (fileItemToReplaceWith.getItemId() && Number(fileItemToReplaceWith.getItemId()) > 0) ? fileItemToReplaceWith.getItemId() : null;
                                if (filterParamName !== file_item_param_names_1.FilterParamNames.UNKNOWN) {
                                    _this.fileItemService.makeFileActionsFromFilterParamName(action.payload.gobiiExtractFilterType, filterParamName, filterValue).subscribe(function (loadFileItemListAction) {
                                        if (loadFileItemListAction) {
                                            if (loadFileItemListAction) {
                                                observer.next(loadFileItemListAction);
                                            }
                                        }
                                    }, function (error) {
                                        _this.store.dispatch(new historyAction.AddStatusMessageAction(error));
                                    });
                                } // if we had a filter to dispatch
                                // LOAD THE CORRESPONDING TREE NODE FOR THE SELECTED ITEM
                                if (fileItemToReplaceWith.getIsExtractCriterion()) {
                                    if (fileItemToReplaceWith.getExtractorItemType() != type_extractor_item_1.ExtractorItemType.LABEL) {
                                        var treeNode = _this.treeStructureService.makeTreeNodeFromFileItem(fileItemToReplaceWith);
                                        observer.next(new treeNodeActions.PlaceTreeNodeAction(treeNode));
                                    }
                                    else {
                                        observer.next(new fileItemActions.RemoveFromExractByItemIdAction(fileItemCurrentlyInExtractUniqueId));
                                    }
                                }
                            }, function (error) {
                                _this.store.dispatch(new historyAction.AddStatusMessageAction(error));
                            }).unsubscribe(); // fromRoot.getAllFileItems()
                        }).mergeMap(function (actions) {
                            // I think that if the loadWithFilterParams() returned an observable, such that it was
                            // "finished" after everything was dispatched, this would work. Or something like it
                            // something like this might work: https://github.com/Reactive-Extensions/RxJS/blob/master/doc/api/core/operators/expand.md
                            // In other words, loadWithFilterParams has to really return an observable of an array of GobiiFileItems,
                            // such that the load action would actually be done _here_
                            // we need to police every other place where loadWithFilterParams is used (if anywhere):
                            // those will have to drive effects, as well.
                            // the call to
                            return Observable_1.Observable.of(actions);
                        });
                    } //switchMap()
                    );
                    this.selectForExtractByFileItemId$ = this.actions$
                        .ofType(fileItemActions.ADD_TO_EXTRACT_BY_ITEM_ID)
                        .switchMap(function (action) {
                        // this is scary. The store is the single source of truth. The only way to get the fileItem for
                        // the fileitem id is to get it from the store, and for that to work here, we need to wrap the
                        // select in an Observable.
                        return Observable_1.Observable.create(function (observer) {
                            var fileItemUniqueId = action.payload;
                            _this.store.select(fromRoot.getAllFileItems)
                                .subscribe(function (all) {
                                var fileItem = all.find(function (fi) { return fi.getFileItemUniqueId() === fileItemUniqueId; });
                                var treeNode = _this.treeStructureService.makeTreeNodeFromFileItem(fileItem);
                                observer.next(treeNode);
                                observer.complete();
                            }, function (error) {
                                _this.store.dispatch(new historyAction.AddStatusMessageAction(error));
                            }).unsubscribe();
                        }).map(function (gfi) {
                            return new treeNodeActions.PlaceTreeNodeAction(gfi);
                        });
                    } //switchMap()
                    );
                    this.deSelectFromExtract$ = this.actions$
                        .ofType(fileItemActions.REMOVE_FROM_EXTRACT)
                        .map(function (action) {
                        return new treeNodeActions.DeActivateFromExtractAction(action.payload.getFileItemUniqueId());
                    });
                    this.deSelectFromExtractById$ = this.actions$
                        .ofType(fileItemActions.REMOVE_FROM_EXTRACT_BY_ITEM_ID)
                        .map(function (action) {
                        return new treeNodeActions.DeActivateFromExtractAction(action.payload);
                    });
                    this.deselectAll$ = this.actions$
                        .ofType(fileItemActions.REMOVE_ALL_FROM_EXTRACT)
                        .map(function (action) {
                        return new treeNodeActions.ClearAll(action.payload);
                    });
                    this.loadFileItem = this.actions$
                        .ofType(fileItemActions.LOAD_FILE_ITEM)
                        .map(function (action) {
                        if (action.payload.selectForExtract) {
                            return new fileItemActions.AddToExtractAction(action.payload.gobiiFileItem);
                        }
                    });
                    this.setExtractType = this.actions$
                        .ofType(fileItemActions.SET_EXTRACT_TYPE)
                        .map(function (action) {
                        return new treeNodeActions.SelectExtractType(action.payload.gobiiExtractFilterType);
                    });
                }
                __decorate([
                    effects_1.Effect(),
                    __metadata("design:type", Object)
                ], FileItemEffects.prototype, "selectForExtract$", void 0);
                __decorate([
                    effects_1.Effect(),
                    __metadata("design:type", Object)
                ], FileItemEffects.prototype, "replaceSameByCompoundId$", void 0);
                __decorate([
                    effects_1.Effect(),
                    __metadata("design:type", Object)
                ], FileItemEffects.prototype, "loadFileItemListWithFilter$", void 0);
                __decorate([
                    effects_1.Effect(),
                    __metadata("design:type", Object)
                ], FileItemEffects.prototype, "replaceInExtract$", void 0);
                __decorate([
                    effects_1.Effect(),
                    __metadata("design:type", Object)
                ], FileItemEffects.prototype, "selectForExtractByFileItemId$", void 0);
                __decorate([
                    effects_1.Effect(),
                    __metadata("design:type", Object)
                ], FileItemEffects.prototype, "deSelectFromExtract$", void 0);
                __decorate([
                    effects_1.Effect(),
                    __metadata("design:type", Object)
                ], FileItemEffects.prototype, "deSelectFromExtractById$", void 0);
                __decorate([
                    effects_1.Effect(),
                    __metadata("design:type", Object)
                ], FileItemEffects.prototype, "deselectAll$", void 0);
                __decorate([
                    effects_1.Effect(),
                    __metadata("design:type", Object)
                ], FileItemEffects.prototype, "loadFileItem", void 0);
                __decorate([
                    effects_1.Effect(),
                    __metadata("design:type", Object)
                ], FileItemEffects.prototype, "setExtractType", void 0);
                FileItemEffects = __decorate([
                    core_1.Injectable(),
                    __metadata("design:paramtypes", [effects_1.Actions,
                        tree_structure_service_1.TreeStructureService,
                        file_item_service_1.FileItemService,
                        store_1.Store,
                        filter_params_coll_1.FilterParamsColl,
                        router_1.Router])
                ], FileItemEffects);
                return FileItemEffects;
            }());
            exports_1("FileItemEffects", FileItemEffects);
            /*
            
            
                @Effect()
                setEntityFilter$ = this.actions$
                    .ofType(fileItemActions.SET_FILTER_VALUE)
                    .map((action: fileItemActions.SetFilterValueAction) => {
            
                        }
                    );
            
            *
            *                 this.nameIdService.get(nameIdRequestParams)
                            .subscribe(nameIds => {
                                    if (nameIds && ( nameIds.length > 0 )) {
            
                                        let fileItems: GobiiFileItem[] = [];
            
                                        nameIds.forEach(n => {
                                            let currentFileItem: GobiiFileItem =
                                                GobiiFileItem.build(
                                                    gobiiExtractFilterType,
                                                    ProcessType.CREATE)
                                                    .setExtractorItemType(ExtractorItemType.ENTITY)
                                                    .setEntityType(nameIdRequestParams.getEntityType())
                                                    .setCvFilterType(CvFilterType.UNKNOWN)
                                                    .setItemId(n.id)
                                                    .setItemName(n.name)
                                                    .setSelected(false)
                                                    .setRequired(false)
                                                    .setParentEntityType(nameIdRequestParams.getRefTargetEntityType())
                                                    .setParentItemId(nameIdRequestParams.getFkEntityFilterValue());
            
                                            fileItems.push(currentFileItem);
            
                                        });
            
                                        return new fileItemActions.LoadAction(fileItems);
                                    }
                                },
                                responseHeader => {
                                    console.log(responseHeader);
                                });
            */
        }
    };
});
//# sourceMappingURL=file-item-effects.js.map