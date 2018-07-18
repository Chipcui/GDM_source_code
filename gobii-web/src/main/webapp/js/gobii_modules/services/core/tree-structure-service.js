System.register(["@angular/core", "../../model/gobii-tree-node", "../../model/type-entity", "../../views/entity-labels", "../../model/type-extractor-item", "../../model/type-extractor-filter", "../../model/cv-group", "../../model/type-extract-format", "../../model/type-process", "../../model/gobii-file-item-compound-id", "../../store/actions/treenode-action", "@ngrx/store"], function (exports_1, context_1) {
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
    var core_1, gobii_tree_node_1, type_entity_1, entity_labels_1, type_extractor_item_1, type_extractor_filter_1, cv_group_1, type_extract_format_1, type_process_1, gobii_file_item_compound_id_1, treeNodeActions, store_1, TreeStructureService;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (gobii_tree_node_1_1) {
                gobii_tree_node_1 = gobii_tree_node_1_1;
            },
            function (type_entity_1_1) {
                type_entity_1 = type_entity_1_1;
            },
            function (entity_labels_1_1) {
                entity_labels_1 = entity_labels_1_1;
            },
            function (type_extractor_item_1_1) {
                type_extractor_item_1 = type_extractor_item_1_1;
            },
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
            },
            function (cv_group_1_1) {
                cv_group_1 = cv_group_1_1;
            },
            function (type_extract_format_1_1) {
                type_extract_format_1 = type_extract_format_1_1;
            },
            function (type_process_1_1) {
                type_process_1 = type_process_1_1;
            },
            function (gobii_file_item_compound_id_1_1) {
                gobii_file_item_compound_id_1 = gobii_file_item_compound_id_1_1;
            },
            function (treeNodeActions_1) {
                treeNodeActions = treeNodeActions_1;
            },
            function (store_1_1) {
                store_1 = store_1_1;
            }
        ],
        execute: function () {
            TreeStructureService = (function () {
                function TreeStructureService(store) {
                    this.store = store;
                }
                TreeStructureService.prototype.makeCommonNodes = function (gobiiExtractFilterType) {
                    var returnVal = [
                        gobii_tree_node_1.GobiiTreeNode.build(gobiiExtractFilterType, type_extractor_item_1.ExtractorItemType.JOB_ID)
                            .setGenericLabel(entity_labels_1.Labels.instance().treeExtractorTypeLabels[type_extractor_item_1.ExtractorItemType.JOB_ID]),
                        gobii_tree_node_1.GobiiTreeNode.build(gobiiExtractFilterType, type_extractor_item_1.ExtractorItemType.ENTITY)
                            .setEntityType(type_entity_1.EntityType.CONTACT)
                            .setEntitySubType(type_entity_1.EntitySubType.CONTACT_SUBMITED_BY)
                            .setGenericLabel(entity_labels_1.Labels.instance().entityNodeLabels[type_entity_1.EntitySubType.CONTACT_SUBMITED_BY]),
                        gobii_tree_node_1.GobiiTreeNode.build(gobiiExtractFilterType, type_extractor_item_1.ExtractorItemType.EXPORT_FORMAT)
                            .setGenericLabel(entity_labels_1.Labels.instance().treeExtractorTypeLabels[type_extractor_item_1.ExtractorItemType.EXPORT_FORMAT]),
                        gobii_tree_node_1.GobiiTreeNode.build(gobiiExtractFilterType, type_extractor_item_1.ExtractorItemType.ENTITY)
                            .setEntityType(type_entity_1.EntityType.MAPSET)
                            .setGenericLabel(entity_labels_1.Labels.instance().entityNodeLabels[type_entity_1.EntityType.MAPSET]),
                    ];
                    return returnVal;
                };
                // Note that we aren't storing these nodes as state -- we only make them
                // and return them. We rely on the store to make them part of state.
                // For now, this is the only context in which container type is ste on tree nodes;
                // tree nodes that are created dynamically will only ever replace or be stored within
                // one of these original structural nodes. See the tree nodes reducer for how the
                // container types are actually used
                TreeStructureService.prototype.getInitialTree = function () {
                    var returnVal = this.makeCommonNodes(type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET).concat([
                        gobii_tree_node_1.GobiiTreeNode.build(type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET, type_extractor_item_1.ExtractorItemType.ENTITY)
                            .setEntityType(type_entity_1.EntityType.DATASET)
                            .setContainerType(gobii_tree_node_1.ContainerType.DATA)
                    ], this.makeCommonNodes(type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE), [
                        gobii_tree_node_1.GobiiTreeNode.build(type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE, type_extractor_item_1.ExtractorItemType.ENTITY)
                            .setEntityType(type_entity_1.EntityType.CV)
                            .setCvGroup(cv_group_1.CvGroup.DATASET_TYPE),
                        gobii_tree_node_1.GobiiTreeNode.build(type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE, type_extractor_item_1.ExtractorItemType.SAMPLE_LIST_TYPE),
                        gobii_tree_node_1.GobiiTreeNode.build(type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE, type_extractor_item_1.ExtractorItemType.ENTITY)
                            .setEntityType(type_entity_1.EntityType.PLATFORM)
                            .setContainerType(gobii_tree_node_1.ContainerType.DATA),
                        gobii_tree_node_1.GobiiTreeNode.build(type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE, type_extractor_item_1.ExtractorItemType.TREE_STRUCTURE)
                            .setContainerType(gobii_tree_node_1.ContainerType.STRUCTURE)
                            .setLabel("Samples Criteria")
                            .setExpanded(true)
                            .setChildren([
                            gobii_tree_node_1.GobiiTreeNode.build(type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE, type_extractor_item_1.ExtractorItemType.ENTITY)
                                .setEntityType(type_entity_1.EntityType.CONTACT)
                                .setEntitySubType(type_entity_1.EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR)
                                .setContainerType(gobii_tree_node_1.ContainerType.NONE),
                            gobii_tree_node_1.GobiiTreeNode.build(type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE, type_extractor_item_1.ExtractorItemType.ENTITY)
                                .setEntityType(type_entity_1.EntityType.PROJECT)
                                .setContainerType(gobii_tree_node_1.ContainerType.NONE),
                            gobii_tree_node_1.GobiiTreeNode.build(type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE, type_extractor_item_1.ExtractorItemType.SAMPLE_FILE),
                            gobii_tree_node_1.GobiiTreeNode.build(type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE, type_extractor_item_1.ExtractorItemType.SAMPLE_LIST_ITEM)
                                .setContainerType(gobii_tree_node_1.ContainerType.DATA),
                        ])
                    ], this.makeCommonNodes(type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER), [
                        gobii_tree_node_1.GobiiTreeNode.build(type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER, type_extractor_item_1.ExtractorItemType.ENTITY)
                            .setEntityType(type_entity_1.EntityType.CV)
                            .setCvGroup(cv_group_1.CvGroup.DATASET_TYPE),
                        gobii_tree_node_1.GobiiTreeNode.build(type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER, type_extractor_item_1.ExtractorItemType.TREE_STRUCTURE)
                            .setContainerType(gobii_tree_node_1.ContainerType.STRUCTURE)
                            .setLabel("Markers Criteria")
                            .setExpanded(true)
                            .setChildren([
                            gobii_tree_node_1.GobiiTreeNode.build(type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER, type_extractor_item_1.ExtractorItemType.ENTITY)
                                .setEntityType(type_entity_1.EntityType.PLATFORM)
                                .setContainerType(gobii_tree_node_1.ContainerType.DATA),
                            gobii_tree_node_1.GobiiTreeNode.build(type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER, type_extractor_item_1.ExtractorItemType.MARKER_FILE),
                            gobii_tree_node_1.GobiiTreeNode.build(type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER, type_extractor_item_1.ExtractorItemType.MARKER_LIST_ITEM)
                                .setContainerType(gobii_tree_node_1.ContainerType.DATA),
                            gobii_tree_node_1.GobiiTreeNode.build(type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER, type_extractor_item_1.ExtractorItemType.ENTITY)
                                .setEntityType(type_entity_1.EntityType.MARKER_GROUP)
                                .setContainerType(gobii_tree_node_1.ContainerType.DATA)
                        ])
                    ], this.makeCommonNodes(type_extractor_filter_1.GobiiExtractFilterType.FLEX_QUERY), [
                        gobii_tree_node_1.GobiiTreeNode.build(type_extractor_filter_1.GobiiExtractFilterType.FLEX_QUERY, type_extractor_item_1.ExtractorItemType.VERTEX)
                            .setSequenceNum(1)
                            .setEntityType(type_entity_1.EntityType.UNKNOWN)
                            .setContainerType(gobii_tree_node_1.ContainerType.DATA)
                            .setChildCompoundUniqueId(new gobii_file_item_compound_id_1.GobiiFileItemCompoundId()
                            .setExtractorItemType(type_extractor_item_1.ExtractorItemType.VERTEX_VALUE)
                            .setEntityType(type_entity_1.EntityType.ANY)
                            .setEntitySubType(type_entity_1.EntitySubType.ANY)
                            .setCvGroup(cv_group_1.CvGroup.ANY)),
                        gobii_tree_node_1.GobiiTreeNode.build(type_extractor_filter_1.GobiiExtractFilterType.FLEX_QUERY, type_extractor_item_1.ExtractorItemType.VERTEX)
                            .setSequenceNum(2)
                            .setEntityType(type_entity_1.EntityType.UNKNOWN)
                            .setContainerType(gobii_tree_node_1.ContainerType.DATA)
                            .setChildCompoundUniqueId(new gobii_file_item_compound_id_1.GobiiFileItemCompoundId()
                            .setExtractorItemType(type_extractor_item_1.ExtractorItemType.VERTEX_VALUE)
                            .setEntityType(type_entity_1.EntityType.ANY)
                            .setEntitySubType(type_entity_1.EntitySubType.ANY)
                            .setCvGroup(cv_group_1.CvGroup.ANY)),
                        gobii_tree_node_1.GobiiTreeNode.build(type_extractor_filter_1.GobiiExtractFilterType.FLEX_QUERY, type_extractor_item_1.ExtractorItemType.VERTEX)
                            .setSequenceNum(3)
                            .setEntityType(type_entity_1.EntityType.UNKNOWN)
                            .setContainerType(gobii_tree_node_1.ContainerType.DATA)
                            .setChildCompoundUniqueId(new gobii_file_item_compound_id_1.GobiiFileItemCompoundId()
                            .setExtractorItemType(type_extractor_item_1.ExtractorItemType.VERTEX_VALUE)
                            .setEntityType(type_entity_1.EntityType.ANY)
                            .setEntitySubType(type_entity_1.EntitySubType.ANY)
                            .setCvGroup(cv_group_1.CvGroup.ANY)),
                        gobii_tree_node_1.GobiiTreeNode.build(type_extractor_filter_1.GobiiExtractFilterType.FLEX_QUERY, type_extractor_item_1.ExtractorItemType.VERTEX)
                            .setSequenceNum(4)
                            .setEntityType(type_entity_1.EntityType.UNKNOWN)
                            .setContainerType(gobii_tree_node_1.ContainerType.DATA)
                            .setChildCompoundUniqueId(new gobii_file_item_compound_id_1.GobiiFileItemCompoundId()
                            .setExtractorItemType(type_extractor_item_1.ExtractorItemType.VERTEX_VALUE)
                            .setEntityType(type_entity_1.EntityType.ANY)
                            .setEntitySubType(type_entity_1.EntitySubType.ANY)
                            .setCvGroup(cv_group_1.CvGroup.ANY)),
                    ]); // array of gobii tree nodes
                    // we know we only have to go one level deep in this case -- no need to recurse
                    returnVal.forEach(function (currentNode, idx, nodes) {
                        currentNode.getChildren().forEach(function (currentChild) {
                            currentChild.parent = currentNode;
                        });
                    });
                    this.setTreeNodeProperties(returnVal);
                    return returnVal;
                }; // getInitialTree()
                TreeStructureService.prototype.setTreeNodeProperties = function (treeNodes) {
                    var _this = this;
                    treeNodes.forEach(function (tn) {
                        if ((tn.children === null) || (tn.children.length <= 0)) {
                            _this.addIconsToNode(tn, false);
                            var label = _this.getLabel(tn.getItemType(), tn.getEntityType(), tn.getEntitySubType(), tn.getCvGroup(), tn.getCvTerm(), tn.getSequenceNum());
                            tn.setLabel(label);
                            tn.setGenericLabel(label);
                        }
                        else {
                            _this.setTreeNodeProperties(tn.children);
                        }
                    });
                };
                TreeStructureService.prototype.getLabel = function (itemType, entityType, entitySubType, cvGroup, cvTerm, sequenceNum) {
                    var labelValue = "";
                    if (itemType === type_extractor_item_1.ExtractorItemType.ENTITY) {
                        labelValue = this.getEntityLabel(entityType, entitySubType, cvGroup);
                    }
                    else if (itemType === type_extractor_item_1.ExtractorItemType.VERTEX || type_extractor_item_1.ExtractorItemType.VERTEX_VALUE) {
                        var separator = "";
                        if (itemType === type_extractor_item_1.ExtractorItemType.VERTEX) {
                            labelValue = "Filter " + sequenceNum.toString();
                            separator = ": ";
                        }
                        if (cvTerm) {
                            var entityLabel = this.getEntityLabel(entityType, entitySubType, cvGroup);
                            labelValue += separator + entityLabel + " " + cvTerm;
                        }
                        else if (entityType !== type_entity_1.EntityType.UNKNOWN
                            || entitySubType !== type_entity_1.EntitySubType.UNKNOWN
                            || cvGroup !== cv_group_1.CvGroup.UNKNOWN) {
                            labelValue += separator + this.getEntityLabel(entityType, entitySubType, cvGroup);
                        }
                    } /* else if(itemType === ExtractorItemType.VERTEX_VALUE) {
            
                        if (cvTerm) {
            
                            let entityLabel: string = this.getEntityLabel(entityType, entitySubType, cvGroup);
            
                            labelValue +  entityLabel + " " + cvTerm;
            
                        } else if (entityType !== EntityType.UNKNOWN
                            || entitySubType !== EntitySubType.UNKNOWN
                            || cvGroup !== CvGroup.UNKNOWN) {
            
                            labelValue +=   this.getEntityLabel(entityType, entitySubType, cvGroup);
                        }
            
                    } */
                    else {
                        labelValue = entity_labels_1.Labels.instance().treeExtractorTypeLabels[itemType];
                    }
                    return labelValue;
                };
                TreeStructureService.prototype.getEntityIcon = function (entityType, cvFilterType) {
                    var icon;
                    var expandedIcon;
                    var collapsedIcon;
                    if (entityType === type_entity_1.EntityType.DATASET) {
                        icon = "fa-database";
                        expandedIcon = "fa-folder-expanded";
                        collapsedIcon = "fa-database";
                    }
                    else if (entityType === type_entity_1.EntityType.CONTACT) {
                        icon = "fa-user-o";
                        expandedIcon = "fa-user-o";
                        collapsedIcon = "fa-user-o";
                    }
                    else if (entityType === type_entity_1.EntityType.MAPSET) {
                        icon = "fa-map-o";
                        expandedIcon = "fa-map-o";
                        collapsedIcon = "fa-map-o";
                    }
                    else if (entityType === type_entity_1.EntityType.PLATFORM) {
                        icon = "fa-calculator";
                        expandedIcon = "fa-calculator";
                        collapsedIcon = "fa-calculator";
                    }
                    else if (entityType === type_entity_1.EntityType.PROJECT) {
                        icon = "fa-clipboard";
                        expandedIcon = "fa-clipboard";
                        collapsedIcon = "fa-clipboard";
                    }
                    else if (entityType === type_entity_1.EntityType.CV && cvFilterType !== null) {
                        if (cvFilterType === cv_group_1.CvGroup.DATASET_TYPE) {
                            icon = "fa-file-excel-o";
                            expandedIcon = "fa-file-excel-o";
                            collapsedIcon = "fa-file-excel-o";
                        }
                    }
                    else if (entityType === type_entity_1.EntityType.MARKER_GROUP) {
                        // if (isParent) {
                        icon = "fa-pencil";
                        expandedIcon = "fa-pencil";
                        collapsedIcon = "fa-pencil";
                    }
                    return { icon: icon, expandedIcon: expandedIcon, collapsedIcon: collapsedIcon };
                };
                TreeStructureService.prototype.getEntityLabel = function (entityType, entitySubType, cvFilterType) {
                    var returnVal;
                    if (entitySubType === type_entity_1.EntitySubType.UNKNOWN) {
                        if (entityType !== type_entity_1.EntityType.CV) {
                            returnVal = entity_labels_1.Labels.instance().entityNodeLabels[entityType];
                        }
                        else {
                            returnVal = entity_labels_1.Labels.instance().cvGroupLabels[cvFilterType];
                        }
                    }
                    else {
                        returnVal = entity_labels_1.Labels.instance().entitySubtypeNodeLabels[entitySubType];
                    }
                    return returnVal;
                };
                TreeStructureService.prototype.getIcons = function (gobiiFileItemCompoundId, isParent) {
                    var icon;
                    var expandedIcon;
                    var collapsedIcon;
                    if (gobiiFileItemCompoundId.getEntityType() != null
                        && gobiiFileItemCompoundId.getEntityType() != type_entity_1.EntityType.UNKNOWN) {
                        var entityIcons = this.getEntityIcon(gobiiFileItemCompoundId.getEntityType(), gobiiFileItemCompoundId.getCvGroup());
                        icon = entityIcons.icon;
                        expandedIcon = entityIcons.expandedIcon;
                        collapsedIcon = entityIcons.collapsedIcon;
                        //this.addEntityIconToNode(gobiiFileItemCompoundId.getEntityType(), gobiiFileItemCompoundId.getCvFilterType(), treeNode);
                    }
                    else if (gobiiFileItemCompoundId.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.EXPORT_FORMAT) {
                        icon = "fa-columns";
                        expandedIcon = "fa-columns";
                        collapsedIcon = "fa-columns";
                    }
                    else if (gobiiFileItemCompoundId.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.SAMPLE_FILE) {
                        icon = "fa-file-text-o";
                        expandedIcon = "fa-file-text-o";
                        collapsedIcon = "fa-file-text-o";
                    }
                    else if (gobiiFileItemCompoundId.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.SAMPLE_LIST_ITEM) {
                        if (isParent) {
                            icon = "fa-list-ul";
                            expandedIcon = "fa-list-ul";
                            collapsedIcon = "fa-list-ul";
                        }
                        else {
                            icon = "fa-eyedropper";
                            expandedIcon = "fa-eyedropper";
                            collapsedIcon = "fa-eyedropper";
                        }
                    }
                    else if (gobiiFileItemCompoundId.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.MARKER_FILE) {
                        icon = "fa-file-text-o";
                        expandedIcon = "fa-file-text-o";
                        collapsedIcon = "fa-file-text-o";
                    }
                    else if (gobiiFileItemCompoundId.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.MARKER_LIST_ITEM) {
                        if (isParent) {
                            icon = "fa-list-ul";
                            expandedIcon = "fa-list-ul";
                            collapsedIcon = "fa-list-ul";
                        }
                        else {
                            icon = "fa-map-marker";
                            expandedIcon = "fa-map-marker";
                            collapsedIcon = "fa-map-marker";
                        }
                    }
                    else if (gobiiFileItemCompoundId.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.JOB_ID) {
                        icon = "fa-info-circle";
                        expandedIcon = "fa-info-circle";
                        collapsedIcon = "fa-info-circle";
                    }
                    else if (gobiiFileItemCompoundId.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.SAMPLE_LIST_TYPE) {
                        icon = "fa-info-circle";
                        expandedIcon = "fa-info-circle";
                        collapsedIcon = "fa-info-circle";
                    }
                    else {
                        //     }
                        // } else if (fileModelNode.getItemType() == ExtractorItemType.CATEGORY ) {
                        icon = "fa-folder";
                        expandedIcon = "fa-folder-expanded";
                        collapsedIcon = "fa-folder";
                    }
                    return { icon: icon, expandedIcon: expandedIcon, collapsedIcon: collapsedIcon };
                };
                TreeStructureService.prototype.addIconsToNode = function (treeNode, isParent) {
                    var icons = this.getIcons(treeNode, isParent);
                    treeNode.icon = icons.icon;
                    treeNode.expandedIcon = icons.expandedIcon;
                    treeNode.collapsedIcon = icons.collapsedIcon;
                };
                TreeStructureService.prototype.makeTreeNodeFromFileItem = function (gobiiFileItem) {
                    var returnVal = gobii_tree_node_1.GobiiTreeNode
                        .build(gobiiFileItem.getGobiiExtractFilterType(), gobiiFileItem.getExtractorItemType())
                        .setFileItemId(gobiiFileItem.getFileItemUniqueId())
                        .setEntityType(gobiiFileItem.getEntityType())
                        .setEntitySubType(gobiiFileItem.getEntitySubType())
                        .setCvGroup(gobiiFileItem.getCvGroup())
                        .setSequenceNum(gobiiFileItem.getSequenceNum());
                    this.addIconsToNode(returnVal, false);
                    var label = this.getLabel(returnVal.getItemType(), returnVal.getEntityType(), returnVal.getEntitySubType(), returnVal.getCvGroup(), returnVal.getCvTerm(), returnVal.getSequenceNum());
                    returnVal.setLabel(label);
                    returnVal.setGenericLabel(label);
                    this.addFileItemNameToNode(returnVal, gobiiFileItem);
                    return returnVal;
                };
                TreeStructureService.prototype.addFileItemNameToNode = function (gobiiTreeNode, gobiiFileItem) {
                    if (gobiiTreeNode.getContainerType() === gobii_tree_node_1.ContainerType.DATA) {
                        gobiiTreeNode.label = gobiiFileItem.getItemName();
                    }
                    else {
                        if (gobiiFileItem.getExtractorItemType() == type_extractor_item_1.ExtractorItemType.EXPORT_FORMAT) {
                            var gobiiExtractFormat = type_extract_format_1.GobiiExtractFormat[gobiiFileItem.getItemId()];
                            gobiiTreeNode.label += ": " + entity_labels_1.Labels.instance().extractFormatTypeLabels[gobiiExtractFormat];
                        }
                        else if (gobiiFileItem.getExtractorItemType() == type_extractor_item_1.ExtractorItemType.JOB_ID) {
                            gobiiTreeNode.label = entity_labels_1.Labels.instance().treeExtractorTypeLabels[type_extractor_item_1.ExtractorItemType.JOB_ID]
                                + ": " + gobiiFileItem.getItemId();
                        }
                        else {
                            if (gobiiFileItem.getProcessType() !== type_process_1.ProcessType.DELETE) {
                                gobiiTreeNode.label += ": " + gobiiFileItem.getItemName();
                            }
                            else {
                                gobiiTreeNode.label = gobiiFileItem.getItemName();
                            }
                        }
                    }
                };
                /***
                 *
                 * @param {GobiiExtractFilterType} gobiiExtractFilterType
                 * @param {GobiiFileItemCompoundId} targetGobiiFileItemCompoundId Determines the node that will be updated
                 * @param {GobiiFileItemCompoundId} childGobiiFileItemCompoundId Determines what types of nodes can be added to the updated node
                 */
                TreeStructureService.prototype.updateTreeNode = function (gobiiExtractFilterType, targetGobiiFileItemCompoundId, childGobiiFileItemCompoundId) {
                    var label = this.getLabel(targetGobiiFileItemCompoundId.getExtractorItemType(), targetGobiiFileItemCompoundId.getEntityType(), targetGobiiFileItemCompoundId.getEntitySubType(), targetGobiiFileItemCompoundId.getCvGroup(), targetGobiiFileItemCompoundId.getCvTerm(), targetGobiiFileItemCompoundId.getSequenceNum());
                    var icons = this.getIcons(targetGobiiFileItemCompoundId, false);
                    this.store.dispatch(new treeNodeActions.SetTreeNodeLook({
                        gobiiExtractFilterType: gobiiExtractFilterType,
                        targetCompoundId: targetGobiiFileItemCompoundId,
                        childCompoundId: childGobiiFileItemCompoundId,
                        icons: icons,
                        label: label,
                        entityType: targetGobiiFileItemCompoundId.getEntityType()
                    }));
                };
                TreeStructureService.prototype.markTreeItemMissing = function (gobiiExtractFilterType, gobiiFileItemCompoundId) {
                    var icon = "fa-share";
                    this.store.dispatch(new treeNodeActions.SetTreeNodeLook({
                        gobiiExtractFilterType: gobiiExtractFilterType,
                        targetCompoundId: gobiiFileItemCompoundId,
                        childCompoundId: null,
                        icons: { icon: icon },
                        label: null,
                        entityType: null
                    }));
                };
                TreeStructureService.prototype.unMarkTreeItemMissing = function (gobiiExtractFilterType, gobiiFileItemCompoundId) {
                    var icons = this.getIcons(gobiiFileItemCompoundId, false);
                    this.store.dispatch(new treeNodeActions.SetTreeNodeLook({
                        gobiiExtractFilterType: gobiiExtractFilterType,
                        targetCompoundId: gobiiFileItemCompoundId,
                        childCompoundId: null,
                        icons: icons,
                        label: null,
                        entityType: null
                    }));
                };
                TreeStructureService = __decorate([
                    core_1.Injectable(),
                    __metadata("design:paramtypes", [store_1.Store])
                ], TreeStructureService);
                return TreeStructureService;
            }());
            exports_1("TreeStructureService", TreeStructureService);
        }
    };
});
//# sourceMappingURL=tree-structure-service.js.map