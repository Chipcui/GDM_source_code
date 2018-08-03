System.register(["@angular/core", "../model/type-extractor-filter", "@ngrx/store", "../store/reducers"], function (exports_1, context_1) {
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
    var core_1, type_extractor_filter_1, store_1, fromRoot, MarkerSampleCountComponent;
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
            }
        ],
        execute: function () {
            MarkerSampleCountComponent = (function () {
                function MarkerSampleCountComponent(store) {
                    var _this = this;
                    this.store = store;
                    this.displayPanel = false;
                    this.displayCounts = true;
                    this.panelCollapsed = false;
                    this.markerCount$ = this.store.select(fromRoot.getCurrentMarkerCount);
                    this.sampleCount$ = this.store.select(fromRoot.getCurrentSampleCount);
                    this.displaySpinner = false;
                    this.markerCount$.subscribe(function (value) {
                        if (value >= 0) {
                            _this.displayCounts = true;
                            _this.displaySpinner = false;
                        }
                        else {
                            _this.displayCounts = false;
                            _this.displaySpinner = true;
                        }
                    });
                    this.sampleCount$.subscribe(function (value) {
                        if (value >= 0) {
                            _this.displayCounts = true;
                            _this.displaySpinner = false;
                        }
                        else {
                            _this.displayCounts = false;
                            _this.displaySpinner = true;
                        }
                    });
                }
                MarkerSampleCountComponent.prototype.ngOnInit = function () {
                };
                MarkerSampleCountComponent.prototype.onBeforeToggle = function ($event) {
                };
                MarkerSampleCountComponent.prototype.onAfterToggle = function ($event) {
                    // if ($event.collapsed) {
                    //
                    //     this.initCount();
                    // } else {
                    //     setTimeout(() => {
                    //         this.displayCounts = true;
                    //         this.displaySpinner = false;
                    //     }, 3000);
                    // }
                };
                // gobiiExtractType is not set until you get OnChanges
                MarkerSampleCountComponent.prototype.ngOnChanges = function (changes) {
                    if (changes['gobiiExtractFilterType']
                        && (changes['gobiiExtractFilterType'].currentValue != null)
                        && (changes['gobiiExtractFilterType'].currentValue != undefined)) {
                        if ((changes['gobiiExtractFilterType'].currentValue != changes['gobiiExtractFilterType'].previousValue) &&
                            (changes['gobiiExtractFilterType'].currentValue === type_extractor_filter_1.GobiiExtractFilterType.FLEX_QUERY)) {
                            this.displayPanel = true;
                            this.displaySpinner = false;
                            // let markerCountItem:GobiiFileItem = GobiiFileItem
                            //     .build(GobiiExtractFilterType.FLEX_QUERY, ProcessType.CREATE)
                            //     .setExtractorItemType(ExtractorItemType.ITEM_COUNT)
                            //     .setEntityType(EntityType.MARKER)
                            //     .setItemName("Marker Count")
                            //     .setEntity(Number(0));
                            // // default count items on load
                            // let loadActionMarkerCount: fileItemActions.LoadFileItemtAction = new fileItemActions.LoadFileItemtAction(
                            //     {
                            //         gobiiFileItem: markerCountItem,
                            //         selectForExtract: true
                            //     }
                            // );
                            // this.store.dispatch(loadActionMarkerCount);
                            // let loadActionSampleCount: fileItemActions.LoadFileItemtAction = new fileItemActions.LoadFileItemtAction(
                            //     {
                            //         gobiiFileItem: GobiiFileItem
                            //             .build(GobiiExtractFilterType.FLEX_QUERY, ProcessType.CREATE)
                            //             .setExtractorItemType(ExtractorItemType.ITEM_COUNT)
                            //             .setEntityType(EntityType.DNA_SAMPLE)
                            //             .setItemName("Sample Count")
                            //             .setEntity(Number(0)),
                            //         selectForExtract: true
                            //     }
                            // );
                            // this.store.dispatch(loadActionSampleCount);
                        }
                        else {
                            this.displayPanel = false;
                            this.displaySpinner = false;
                        } // if we have a new filter type
                    }
                    else {
                        this.displayPanel = false;
                        this.displaySpinner = false;
                    } // if-else filter type changed
                }; // ngonChanges
                MarkerSampleCountComponent = __decorate([
                    core_1.Component({
                        selector: 'marker-sample-count',
                        outputs: [],
                        inputs: ['gobiiExtractFilterType'],
                        template: "\n        <p-panel *ngIf=\"displayPanel\" header=\"Estimated Marker/Sample Count\"\n                 [(toggleable)]=\"panelCollapsed\"\n                 [(collapsed)]=\"panelCollapsed\"\n                 (onBeforeToggle)=\"onBeforeToggle($event)\"\n                 (onAfterToggle)=\"onAfterToggle($event)\">\n            <p-progressSpinner *ngIf=\"displaySpinner\" [style]=\"{width: '50px', height: '50px'}\"\n                               strokeWidth=\"8\" fill=\"#EEEEEE\"\n                               animationDuration=\"3s\"></p-progressSpinner>\n            <div *ngIf=\"displayCounts\">\n                <p>Total Markers: {{markerCount$ | async}}</p>\n                <p>Total Samples: {{sampleCount$ | async}}</p>\n            </div>\n        </p-panel>\n    "
                    }),
                    __metadata("design:paramtypes", [store_1.Store])
                ], MarkerSampleCountComponent);
                return MarkerSampleCountComponent;
            }());
            exports_1("MarkerSampleCountComponent", MarkerSampleCountComponent);
        }
    };
});
//# sourceMappingURL=marker-sample-count.component.js.map