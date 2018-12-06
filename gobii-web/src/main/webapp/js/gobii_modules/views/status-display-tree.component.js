System.register(["@angular/core", "../model/type-extractor-filter", "../store/reducers", "@ngrx/store", "../services/core/view-id-generator-service", "../services/core/type-control"], function (exports_1, context_1) {
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
    var core_1, type_extractor_filter_1, fromRoot, store_1, view_id_generator_service_1, type_control_1, StatusDisplayTreeComponent;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
            },
            function (fromRoot_1) {
                fromRoot = fromRoot_1;
            },
            function (store_1_1) {
                store_1 = store_1_1;
            },
            function (view_id_generator_service_1_1) {
                view_id_generator_service_1 = view_id_generator_service_1_1;
            },
            function (type_control_1_1) {
                type_control_1 = type_control_1_1;
            }
        ],
        execute: function () {
            StatusDisplayTreeComponent = (function () {
                function StatusDisplayTreeComponent(store, viewIdGeneratorService) {
                    this.store = store;
                    this.viewIdGeneratorService = viewIdGeneratorService;
                    this.typeControl = type_control_1.TypeControl;
                    this.containerCollapseThreshold = 10;
                    this.onAddMessage = new core_1.EventEmitter();
                    this.onTreeReady = new core_1.EventEmitter();
                    // *****************************************************************
                    // *********************  TREE NODE DATA STRUCTURES AND EVENTS
                    this.demoTreeNodes = [];
                    this.selectedDemoNodes = [];
                    this.selectedGobiiNodes = [];
                    // ********************************************************************************
                    // ********************* CHECKBOX/TREE NODE CONVERSION FUNCTIONS
                    // ********************************************************************************
                    // ********************* CHECKBOX (GOBII-SPECIFIC)  NODE DATA STRUCTURES AND EVENTS
                    this.gobiiExtractFilterType = type_extractor_filter_1.GobiiExtractFilterType.UNKNOWN;
                    this.onItemChecked = new core_1.EventEmitter();
                    this.onItemSelected = new core_1.EventEmitter();
                    this.gobiiTreeNodesFromStore$ = store
                        .select(fromRoot.getGobiiTreeNodesForExtractFilter);
                    this.gobiiSelectedNodesFromStore$ = store
                        .select(fromRoot.getSelectedGobiiTreeNodes);
                }
                StatusDisplayTreeComponent.prototype.ngOnInit = function () {
                };
                StatusDisplayTreeComponent.prototype.nodeSelect = function (event) {
                    // for now the selectable property on nodes is set to false by default, so we aren't using this yet
                };
                StatusDisplayTreeComponent.prototype.nodeUnselect = function (event) {
                };
                StatusDisplayTreeComponent.prototype.nodeExpand = function (event) {
                    if (event.node) {
                    }
                };
                StatusDisplayTreeComponent.prototype.nodeCollapse = function (event) {
                    if (event.node) {
                    }
                };
                StatusDisplayTreeComponent.prototype.addCountToContainerNode = function (node) {
                    var foo = "foo";
                    var parenPosition = node.label.indexOf("(");
                    if (parenPosition > 0) {
                        node.label = node.label.substring(0, parenPosition);
                    }
                    if (node.children.length > 0) {
                        node.label += " (" + node.children.length + ")";
                    }
                };
                StatusDisplayTreeComponent.prototype.expandRecursive = function (node, isExpand) {
                    var _this = this;
                    node.expanded = isExpand;
                    if (node.children) {
                        node.children.forEach(function (childNode) {
                            _this.expandRecursive(childNode, isExpand);
                        });
                    }
                };
                StatusDisplayTreeComponent.prototype.ngOnChanges = function (changes) {
                    if (changes['fileItemEventChange'] && changes['fileItemEventChange'].currentValue) {
                    }
                    else if (changes['gobiiExtractFilterTypeEvent']
                        && (changes['gobiiExtractFilterTypeEvent'].currentValue != null)
                        && (changes['gobiiExtractFilterTypeEvent'].currentValue != undefined)) {
                    }
                };
                StatusDisplayTreeComponent.prototype.makeDemoTreeNodes = function () {
                    this.demoTreeNodes = [
                        {
                            "label": "Documents",
                            "data": "Documents Folder",
                            "expandedIcon": "fa-folder-open",
                            "collapsedIcon": "fa-folder",
                            "children": [{
                                    "label": "Work",
                                    "data": "Work Folder",
                                    "expandedIcon": "fa-folder-open",
                                    "collapsedIcon": "fa-folder",
                                    "children": [{
                                            "label": "Expenses.doc",
                                            "icon": "fa-file-word-o",
                                            "data": "Expenses Document"
                                        }, { "label": "Resume.doc", "icon": "fa-file-word-o", "data": "Resume Document" }]
                                },
                                {
                                    "label": "Home",
                                    "data": "Home Folder",
                                    "expandedIcon": "fa-folder-open",
                                    "collapsedIcon": "fa-folder",
                                    "children": [{
                                            "label": "Invoices.txt",
                                            "icon": "fa-file-word-o",
                                            "data": "Invoices for this month"
                                        }]
                                }]
                        },
                        {
                            "label": "Pictures",
                            "data": "Pictures Folder",
                            "expandedIcon": "fa-folder-open",
                            "collapsedIcon": "fa-folder",
                            "children": [
                                { "label": "barcelona.jpg", "icon": "fa-file-image-o", "data": "Barcelona Photo" },
                                { "label": "logo.jpg", "icon": "fa-file-image-o", "data": "PrimeFaces Logo" },
                                { "label": "primeui.png", "icon": "fa-file-image-o", "data": "PrimeUI Logo" }
                            ]
                        },
                        {
                            "label": "Movies",
                            "data": "Movies Folder",
                            "expandedIcon": "fa-folder-open",
                            "collapsedIcon": "fa-folder",
                            "children": [{
                                    "label": "Al Pacino",
                                    "data": "Pacino Movies",
                                    "children": [{
                                            "label": "Scarface",
                                            "icon": "fa-file-video-o",
                                            "data": "Scarface Movie"
                                        }, { "label": "Serpico", "icon": "fa-file-video-o", "data": "Serpico Movie" }]
                                },
                                {
                                    "label": "Robert De Niro",
                                    "data": "De Niro Movies",
                                    "children": [{
                                            "label": "Goodfellas",
                                            "icon": "fa-file-video-o",
                                            "data": "Goodfellas Movie"
                                        }, {
                                            "label": "Untouchables",
                                            "icon": "fa-file-video-o",
                                            "data": "Untouchables Movie"
                                        }]
                                }]
                        }
                    ];
                    this.selectedDemoNodes.push(this.demoTreeNodes[1].children[0]);
                    this.demoTreeNodes[1].partialSelected = true;
                    this.demoTreeNodes[1].expanded = true;
                };
                StatusDisplayTreeComponent = __decorate([
                    core_1.Component({
                        selector: 'status-display-tree',
                        inputs: ['fileItemEventChange', 'gobiiExtractFilterTypeEvent'],
                        outputs: ['onItemSelected', 'onItemChecked', 'onAddMessage', 'onTreeReady'],
                        template: "\n        <p-tree [value]=\"gobiiTreeNodesFromStore$ | async\"\n                selectionMode=\"checkbox\"\n                propagateSelectionUp=\"false\"\n                propagateSelectionDown=\"false\"\n                [selection]=\"gobiiSelectedNodesFromStore$ | async\"\n                (onNodeUnselect)=\"nodeUnselect($event)\"\n                (onNodeSelect)=\"nodeSelect($event)\"\n                (onNodeExpand)=\"nodeExpand($event)\"\n                (onNodeCollapse)=\"nodeCollapse($event)\"\n                [style]=\"{'width':'100%'}\"\n                styleClass=\"criteria-tree\"\n                [id]=\"viewIdGeneratorService.makeStandardId(typeControl.CRITERIA_TREE)\"></p-tree>\n        <!--<p-tree [value]=\"demoTreeNodes\" selectionMode=\"checkbox\" [(selection)]=\"selectedDemoNodes\"></p-tree>-->\n        <!--<div>Selected Nodes: <span *ngFor=\"let file of selectedFiles2\">{{file.label}} </span></div>-->\n    "
                    }),
                    __metadata("design:paramtypes", [store_1.Store,
                        view_id_generator_service_1.ViewIdGeneratorService])
                ], StatusDisplayTreeComponent);
                return StatusDisplayTreeComponent;
            }());
            exports_1("StatusDisplayTreeComponent", StatusDisplayTreeComponent);
        }
    };
});
//# sourceMappingURL=status-display-tree.component.js.map