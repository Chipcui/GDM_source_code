System.register(["@angular/core", "../model/type-entity", "../model/type-extractor-filter", "../model/cv-filter-type", "../store/reducers", "@ngrx/store"], function (exports_1, context_1) {
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
    var core_1, type_entity_1, type_extractor_filter_1, cv_filter_type_1, fromRoot, store_1, StatusDisplayTreeComponent;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (type_entity_1_1) {
                type_entity_1 = type_entity_1_1;
            },
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
            },
            function (cv_filter_type_1_1) {
                cv_filter_type_1 = cv_filter_type_1_1;
            },
            function (fromRoot_1) {
                fromRoot = fromRoot_1;
            },
            function (store_1_1) {
                store_1 = store_1_1;
            }
        ],
        execute: function () {
            StatusDisplayTreeComponent = (function () {
                function StatusDisplayTreeComponent(store) {
                    this.store = store;
                    this.containerCollapseThreshold = 10;
                    this.onAddMessage = new core_1.EventEmitter();
                    this.onTreeReady = new core_1.EventEmitter();
                    // *****************************************************************
                    // *********************  TREE NODE DATA STRUCTURES AND EVENTS
                    this.demoTreeNodes = [];
                    this.selectedDemoNodes = [];
                    this.selectedGobiiNodes = [];
                    this.treeIsInitialized = false;
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
                    // Unless a node already is checked such that it has data, we don't allow checking
                    // something because it has no meaning without data in it; these would typically
                    // by CONTAINER type nodes: once they have children they're selected, and it which
                    // point we deal with check events in nodeUnselect()
                    // yes this is a bit of a kludge; version 4 of PrimeNG will add a selectable proeprty
                    // to TreeNode which will enable us to approch selectability of nodes in general in
                    // a more systematic and elegant way
                    var _this = this;
                    var selectedGobiiTreeNode = event.node;
                    selectedGobiiTreeNode.children.forEach(function (childNode) {
                        _this.removeItemFromSelectedNodes(childNode);
                    });
                    this.removeItemFromSelectedNodes(selectedGobiiTreeNode);
                };
                // we need to disable partial selection because when you click
                // a node that's partially selected, you don't get the unselect event
                // which breaks everything
                StatusDisplayTreeComponent.prototype.unsetPartialSelect = function (gobiiTreeNode) {
                    var thereAreSelectedChildren = false;
                    if (gobiiTreeNode.partialSelected) {
                        gobiiTreeNode.partialSelected = false;
                        var foo = "foo";
                        var _loop_1 = function (idx) {
                            var currentTreeNode = gobiiTreeNode.children[idx];
                            thereAreSelectedChildren = this_1.selectedGobiiNodes.find(function (fi) {
                                return fi
                                    && fi.fileItemId
                                    && (fi.fileItemId === currentTreeNode.fileItemId);
                            }) != undefined;
                        };
                        var this_1 = this;
                        for (var idx = 0; (idx < gobiiTreeNode.children.length) && !thereAreSelectedChildren; idx++) {
                            _loop_1(idx);
                        }
                        if (thereAreSelectedChildren) {
                            this.selectedGobiiNodes.push(gobiiTreeNode);
                        }
                    }
                    if ((gobiiTreeNode.parent !== null)
                        && (gobiiTreeNode.parent !== undefined)) {
                        this.unsetPartialSelect(gobiiTreeNode.parent);
                    }
                };
                StatusDisplayTreeComponent.prototype.nodeUnselect = function (event) {
                    var _this = this;
                    // this funditonality is nearly working;
                    // but it breaks down in the marker criteria section of the
                    // tree. There is no more time to work on this. It must just
                    // effectively disabled for now: you can only select and deselect
                    // from the controls outside the tree
                    var unselectedTreeNode = event.node;
                    this.unsetPartialSelect(unselectedTreeNode);
                    this.selectedGobiiNodes.push(unselectedTreeNode);
                    unselectedTreeNode.children.forEach(function (tn) {
                        _this.selectedGobiiNodes.push(tn);
                    });
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
                // ********************************************************************************
                // ********************* CHECKBOX/TREE NODE CONVERSION FUNCTIONS
                StatusDisplayTreeComponent.prototype.addEntityIconToNode = function (entityType, cvFilterType, treeNode) {
                    if (entityType === type_entity_1.EntityType.DataSets) {
                        treeNode.icon = "fa-database";
                        treeNode.expandedIcon = "fa-folder-expanded";
                        treeNode.collapsedIcon = "fa-database";
                    }
                    else if (entityType === type_entity_1.EntityType.Contacts) {
                        treeNode.icon = "fa-user-o";
                        treeNode.expandedIcon = "fa-user-o";
                        treeNode.collapsedIcon = "fa-user-o";
                    }
                    else if (entityType === type_entity_1.EntityType.Mapsets) {
                        treeNode.icon = "fa-map-o";
                        treeNode.expandedIcon = "fa-map-o";
                        treeNode.collapsedIcon = "fa-map-o";
                    }
                    else if (entityType === type_entity_1.EntityType.Platforms) {
                        treeNode.icon = "fa-calculator";
                        treeNode.expandedIcon = "fa-calculator";
                        treeNode.collapsedIcon = "fa-calculator";
                    }
                    else if (entityType === type_entity_1.EntityType.Projects) {
                        treeNode.icon = "fa-clipboard";
                        treeNode.expandedIcon = "fa-clipboard";
                        treeNode.collapsedIcon = "fa-clipboard";
                    }
                    else if (entityType === type_entity_1.EntityType.CvTerms) {
                        if (cvFilterType === cv_filter_type_1.CvFilterType.DATASET_TYPE) {
                            treeNode.icon = "fa-file-excel-o";
                            treeNode.expandedIcon = "fa-file-excel-o";
                            treeNode.collapsedIcon = "fa-file-excel-o";
                        }
                    }
                    else if (entityType === type_entity_1.EntityType.MarkerGroups) {
                        // if (isParent) {
                        treeNode.icon = "fa-pencil";
                        treeNode.expandedIcon = "fa-pencil";
                        treeNode.collapsedIcon = "fa-pencil";
                        // } else {
                        //     treeNode.icon = "fa-map-marker";
                        //     treeNode.expandedIcon = "fa-map-marker";
                        //     treeNode.collapsedIcon = "fa-map-marker";
                        // }
                    }
                };
                StatusDisplayTreeComponent.prototype.removeItemFromSelectedNodes = function (gobiiTreeNode) {
                    if (gobiiTreeNode) {
                        var idxOfSelectedNodeParentNode = this.selectedGobiiNodes.indexOf(gobiiTreeNode);
                        if (idxOfSelectedNodeParentNode >= 0) {
                            var deleted = this.selectedGobiiNodes.splice(idxOfSelectedNodeParentNode, 1);
                            var foo = "foo";
                        }
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
                        template: "\n        <p-tree [value]=\"gobiiTreeNodesFromStore$ | async\"\n                selectionMode=\"checkbox\"\n                propagateSelectionUp=\"false\"\n                propagateSelectionDown=\"false\"\n                [selection]=\"gobiiSelectedNodesFromStore$ | async\"\n                (onNodeUnselect)=\"nodeUnselect($event)\"\n                (onNodeSelect)=\"nodeSelect($event)\"\n                (onNodeExpand)=\"nodeExpand($event)\"\n                (onNodeCollapse)=\"nodeCollapse($event)\"\n                [style]=\"{'width':'100%'}\"\n                styleClass=\"criteria-tree\"></p-tree>\n        <!--<p-tree [value]=\"demoTreeNodes\" selectionMode=\"checkbox\" [(selection)]=\"selectedDemoNodes\"></p-tree>-->\n        <!--<div>Selected Nodes: <span *ngFor=\"let file of selectedFiles2\">{{file.label}} </span></div>-->\n    "
                    }),
                    __metadata("design:paramtypes", [store_1.Store])
                ], StatusDisplayTreeComponent);
                return StatusDisplayTreeComponent;
            }());
            exports_1("StatusDisplayTreeComponent", StatusDisplayTreeComponent);
        }
    };
});
//# sourceMappingURL=status-display-tree.component.js.map