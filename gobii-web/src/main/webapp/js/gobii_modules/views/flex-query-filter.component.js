System.register(["@angular/core", "../model/type-extractor-filter", "@ngrx/store", "../store/reducers", "../services/core/nameid-file-item-service", "../services/core/filter-service", "../services/core/flex-query-service", "../model/type-entity", "../model/name-id-label-type", "../services/core/filter-params-coll", "../model/cv-group", "../model/vertex", "../model/name-id"], function (exports_1, context_1) {
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
    var core_1, type_extractor_filter_1, store_1, fromRoot, nameid_file_item_service_1, filter_service_1, flex_query_service_1, type_entity_1, name_id_label_type_1, filter_params_coll_1, cv_group_1, vertex_1, name_id_1, FlexQueryFilterComponent;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
            },
            function (store_1_1) {
                store_1 = store_1_1;
            },
            function (fromRoot_1) {
                fromRoot = fromRoot_1;
            },
            function (nameid_file_item_service_1_1) {
                nameid_file_item_service_1 = nameid_file_item_service_1_1;
            },
            function (filter_service_1_1) {
                filter_service_1 = filter_service_1_1;
            },
            function (flex_query_service_1_1) {
                flex_query_service_1 = flex_query_service_1_1;
            },
            function (type_entity_1_1) {
                type_entity_1 = type_entity_1_1;
            },
            function (name_id_label_type_1_1) {
                name_id_label_type_1 = name_id_label_type_1_1;
            },
            function (filter_params_coll_1_1) {
                filter_params_coll_1 = filter_params_coll_1_1;
            },
            function (cv_group_1_1) {
                cv_group_1 = cv_group_1_1;
            },
            function (vertex_1_1) {
                vertex_1 = vertex_1_1;
            },
            function (name_id_1_1) {
                name_id_1 = name_id_1_1;
            }
        ],
        execute: function () {
            FlexQueryFilterComponent = (function () {
                function FlexQueryFilterComponent(store, fileItemService, filterService, filterParamsColl, flexQueryService, cd) {
                    this.store = store;
                    this.fileItemService = fileItemService;
                    this.filterService = filterService;
                    this.filterParamsColl = filterParamsColl;
                    this.flexQueryService = flexQueryService;
                    this.cd = cd;
                    //these are dummy place holders for now
                    this.totalValues = "0";
                    this.enabledStyle = null;
                    this.disabledStyle = { 'background': '#dddddd' };
                    this.currentStyle = this.disabledStyle;
                    // Technically, we should not be keeping state in this control in this way;
                    // However, it turns out to be a lot more complicated and error prone to
                    // rely purely on the store
                    this.previousSelectedVertices = [];
                } // ctor
                FlexQueryFilterComponent.prototype.ngAfterViewInit = function () {
                    // this.cd.detectChanges();
                };
                FlexQueryFilterComponent.prototype.ngOnInit = function () {
                    var _this = this;
                    this.fileItemsVertexNames$ = this.filterService.getForFilter(this.filterParamNameVertices);
                    this.fileItemsEntityValues$ = this.filterService.getForFilter(this.filterParamNameVertexValues);
                    this.JobId$ = this.store.select(fromRoot.getJobId);
                    this
                        .fileItemsEntityValues$
                        .subscribe(function (items) {
                        _this.totalValues = items.length.toString();
                    });
                    this.store.select(fromRoot.getFilterCountState)
                        .subscribe(function (filterCountState) {
                        // calling detectChanges() here is necessary to deal with the
                        // ExpressionChangedAfterItHasBeenCheckedError. This error only
                        // occurs when handleVertexSelected() is called and when there is
                        // a previous sibling filter. In other words, it happens when
                        // the code path goes through the else if below. The problem is that
                        // the eventing change to the filter collection is within a change
                        // detection cycle, and within that same cycle the count changes.
                        // This results in the value being reset within the change cycle.
                        // This is probably a design issue that needs to be solved more
                        // elegantly. It might be that the solution is, per the comment on
                        // on FlexQueryService::loadSelectedVertexFilter(), to issue the
                        // recount properly, as an effect of the filter value changes. Per
                        // the notes there and in the file item effects code, this will
                        // require wrapping the http subscribe with a switchMap() (I _think_)
                        _this.cd.detectChanges();
                        var thisControlVertexfilterParams = _this.filterParamsColl.getFilter(_this.filterParamNameVertices, type_extractor_filter_1.GobiiExtractFilterType.FLEX_QUERY);
                        var currentVertexFilter = filterCountState.flexQueryFilters.get(thisControlVertexfilterParams.getQueryName());
                        if (currentVertexFilter) {
                            if (!currentVertexFilter.targetEntityFilterValue) {
                                _this.selectedVertex = null;
                                _this.selectedVertexValues = null;
                            }
                        }
                        if (!thisControlVertexfilterParams.getPreviousSiblingFileItemParams()) {
                            if (filterCountState.sampleCount >= 0
                                && filterCountState.markerCount >= 0) {
                                _this.setControlState(true);
                            }
                            else {
                                _this.setControlState(false);
                            }
                        }
                        else if (thisControlVertexfilterParams.getPreviousSiblingFileItemParams().getChildFileItemParams().length > 0) {
                            var vertexValuePreviousVertexSelectorParamName = thisControlVertexfilterParams
                                .getPreviousSiblingFileItemParams()
                                .getChildFileItemParams()[0].getQueryName();
                            var previousVertexValuesFilter = filterCountState.flexQueryFilters.get(vertexValuePreviousVertexSelectorParamName);
                            if (previousVertexValuesFilter
                                && previousVertexValuesFilter.targetEntityFilterValue
                                && filterCountState.sampleCount >= 0
                                && filterCountState.markerCount >= 0) {
                                _this.setControlState(true);
                            }
                            else {
                                _this.setControlState(false);
                            }
                        } // if-else there are previous sibling params
                    }); //subscribe to filter count state
                }; // ngInit()
                FlexQueryFilterComponent.prototype.setControlState = function (enabled) {
                    if (enabled) {
                        this.currentStyle = this.enabledStyle;
                    }
                    else {
                        this.currentStyle = this.disabledStyle;
                    }
                }; // setControlState()
                FlexQueryFilterComponent.prototype.handleVertexSelected = function (arg) {
                    var _this = this;
                    if (arg.value && arg.value._entity) {
                        var vertexId_1;
                        var entityType_1 = type_entity_1.EntityType.UNKNOWN;
                        var entitySubType_1 = type_entity_1.EntitySubType.UNKNOWN;
                        var cvGroup_1 = cv_group_1.CvGroup.UNKNOWN;
                        var cvTerm_1 = null;
                        var previousSelectionExisted_1 = (this.selectedVertexValues && this.selectedVertexValues.length > 0)
                            || (this.previousSelectedVertices && this.previousSelectedVertices.length > 0);
                        if (arg.value.getNameIdLabelType() === name_id_label_type_1.NameIdLabelType.UNKNOWN) {
                            vertexId_1 = arg.value.getItemId();
                            entityType_1 = arg.value.getEntityType();
                            entitySubType_1 = arg.value.getEntitySubType();
                            cvGroup_1 = arg.value.getCvGroup();
                            cvTerm_1 = arg.value.getCvTerm();
                        }
                        else {
                            vertexId_1 = null;
                            this.selectedVertexValues = [];
                            this.previousSelectedVertices = [];
                        }
                        this.selectedVertexValues = [];
                        this.JobId$.subscribe(function (fileItemJobId) {
                            var jobId = fileItemJobId.getItemId();
                            _this.flexQueryService.loadVertexValues(jobId, arg.value, _this.filterParamNameVertexValues);
                            _this.flexQueryService.loadSelectedVertexFilter(_this.filterParamNameVertices, vertexId_1, entityType_1, entitySubType_1, cvGroup_1, cvTerm_1, jobId, previousSelectionExisted_1);
                        }).unsubscribe();
                    }
                }; // end function
                FlexQueryFilterComponent.prototype.handleVertexValueSelected = function (arg) {
                    var _this = this;
                    var targetValueVertex = (this.selectedVertex && this.selectedVertex.getEntity()) ?
                        this.selectedVertex.getEntity() :
                        null;
                    var filterVertex;
                    if (targetValueVertex) {
                        filterVertex = vertex_1.Vertex.fromVertex(targetValueVertex);
                        filterVertex.filterVals = this.selectedVertexValues
                            .map(function (vv) {
                            return new name_id_1.NameId(vv.getItemId(), null, vv.getItemName(), filterVertex.entityType, null, null);
                        });
                    }
                    var newItems = this.selectedVertexValues
                        .filter(function (gfi) { return !_this.previousSelectedVertices.find(function (igfi) { return igfi.getFileItemUniqueId() === gfi.getFileItemUniqueId(); }); });
                    var deselectedItems = this.previousSelectedVertices.filter(function (gfi) { return !_this.selectedVertexValues.find(function (igfi) { return igfi.getFileItemUniqueId() === gfi.getFileItemUniqueId(); }); });
                    this.JobId$.subscribe(function (fileItemJobId) {
                        _this.flexQueryService.loadSelectedVertexValueFilters(fileItemJobId.getItemId(), _this.filterParamNameVertexValues, newItems, deselectedItems, filterVertex);
                    }).unsubscribe();
                    this.previousSelectedVertices = this.selectedVertexValues;
                };
                FlexQueryFilterComponent.prototype.ngOnChanges = function (changes) {
                    if (changes['gobiiExtractFilterType']
                        && (changes['gobiiExtractFilterType'].currentValue != null)
                        && (changes['gobiiExtractFilterType'].currentValue != undefined)) {
                        if (changes['gobiiExtractFilterType'].currentValue != changes['gobiiExtractFilterType'].previousValue) {
                            if (this.gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.FLEX_QUERY) {
                                // this.flexQueryService.loadVertices(this.filterParamNameVertices);
                            }
                        } // if we have a new filter type
                    } // if filter type changed
                }; // ngonChanges
                FlexQueryFilterComponent = __decorate([
                    core_1.Component({
                        selector: 'flex-query-filter',
                        inputs: ['gobiiExtractFilterType', 'filterParamNameVertices', 'filterParamNameVertexValues'],
                        outputs: [],
                        styleUrls: ["css/extractor-ui.css"],
                        template: "\n        <div class=\"panel panel-primary\" [ngStyle]=\"currentStyle\">\n            <div class=\"panel-heading\">\n                <h3 class=\"panel-title\">Filters</h3>\n            </div>\n            <div class=\"panel-body\">\n                <label class=\"the-label\">Entity:</label><BR>\n                <p-dropdown [options]=\"fileItemsVertexNames$ | async\"\n                            [style]=\"{'width': '100%'}\"\n                            optionLabel=\"_itemName\"\n                            [(ngModel)]=\"selectedVertex\"\n                            (onChange)=\"handleVertexSelected($event)\"\n                            [disabled]=\"currentStyle===disabledStyle\">\n                </p-dropdown>\n\n                <BR>\n                <BR>\n                <label class=\"the-label\">Select Entity Values</label><BR>\n                <p-listbox [options]=\"fileItemsEntityValues$ | async\"\n                           [multiple]=\"true\"\n                           [(ngModel)]=\"selectedVertexValues\"\n                           [style]=\"{'width':'100%'}\"\n                           (onChange)=\"handleVertexValueSelected($event)\"\n                           optionLabel=\"_itemName\"\n                           [disabled]=\"currentStyle===disabledStyle\"></p-listbox>\n            </div>\n\n            <div class=\"container\">\n                <p>Count: {{totalValues}} </p>\n                <p>Selected: {{selectedVertexValues ? selectedVertexValues.length : 0}}</p>\n            </div>\n        </div>" // end template
                    }),
                    __metadata("design:paramtypes", [store_1.Store,
                        nameid_file_item_service_1.NameIdFileItemService,
                        filter_service_1.FilterService,
                        filter_params_coll_1.FilterParamsColl,
                        flex_query_service_1.FlexQueryService,
                        core_1.ChangeDetectorRef])
                ], FlexQueryFilterComponent);
                return FlexQueryFilterComponent;
            }());
            exports_1("FlexQueryFilterComponent", FlexQueryFilterComponent);
        }
    };
});
//# sourceMappingURL=flex-query-filter.component.js.map