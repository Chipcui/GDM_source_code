System.register(["./guid", "./type-extractor-filter", "./gobii-file-item-compound-id"], function (exports_1, context_1) {
    "use strict";
    var __extends = (this && this.__extends) || (function () {
        var extendStatics = Object.setPrototypeOf ||
            ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
            function (d, b) { for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p]; };
        return function (d, b) {
            extendStatics(d, b);
            function __() { this.constructor = d; }
            d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
        };
    })();
    var __moduleName = context_1 && context_1.id;
    var guid_1, type_extractor_filter_1, gobii_file_item_compound_id_1, ContainerType, GobiiTreeNode;
    return {
        setters: [
            function (guid_1_1) {
                guid_1 = guid_1_1;
            },
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
            },
            function (gobii_file_item_compound_id_1_1) {
                gobii_file_item_compound_id_1 = gobii_file_item_compound_id_1_1;
            }
        ],
        execute: function () {
            (function (ContainerType) {
                ContainerType[ContainerType["NONE"] = 0] = "NONE";
                ContainerType[ContainerType["STRUCTURE"] = 1] = "STRUCTURE";
                ContainerType[ContainerType["DATA"] = 2] = "DATA";
            })(ContainerType || (ContainerType = {}));
            exports_1("ContainerType", ContainerType);
            GobiiTreeNode = (function (_super) {
                __extends(GobiiTreeNode, _super);
                function GobiiTreeNode(parent, fileItemId, required) {
                    var _this = _super.call(this) || this;
                    _this.children = [];
                    //GOBII UI properties
                    _this.gobiiExtractFilterType = type_extractor_filter_1.GobiiExtractFilterType.UNKNOWN;
                    _this.required = false;
                    _this.active = false;
                    _this.containerType = ContainerType.NONE;
                    _this.childCompoundUniqueId = null;
                    _this.id = guid_1.Guid.generateUUID();
                    _this.parent = parent;
                    _this.fileItemId = fileItemId;
                    _this.required = required;
                    _this.selectable = false; // for now all nodes are not selectable
                    return _this;
                    //(TreeNode)
                }
                GobiiTreeNode.build = function (gobiiExtractFilterType, extractoItemType) {
                    var returnVal = new GobiiTreeNode(null, null, null);
                    returnVal.gobiiExtractFilterType = gobiiExtractFilterType;
                    returnVal.setItemType(extractoItemType);
                    return returnVal;
                }; //build
                GobiiTreeNode.fromFileItem = function (gobiiFileItem) {
                    return GobiiTreeNode.build(gobiiFileItem.getGobiiExtractFilterType(), gobiiFileItem.getExtractorItemType())
                        .setFileItemId(gobiiFileItem.getFileItemUniqueId())
                        .setEntityType(gobiiFileItem.getEntityType())
                        .setEntitySubType(gobiiFileItem.getEntitySubType())
                        .setCvGroup(gobiiFileItem.getCvGroup())
                        .setCvTerm(gobiiFileItem.getCvTerm())
                        .setSequenceNum(gobiiFileItem.getSequenceNum());
                };
                GobiiTreeNode.prototype.getId = function () {
                    return this.id;
                };
                //unique identifiers
                GobiiTreeNode.prototype.getItemType = function () {
                    return _super.prototype.getExtractorItemType.call(this);
                };
                GobiiTreeNode.prototype.setItemType = function (value) {
                    _super.prototype.setExtractorItemType.call(this, value);
                    return this;
                };
                GobiiTreeNode.prototype.getEntityType = function () {
                    return _super.prototype.getEntityType.call(this);
                };
                GobiiTreeNode.prototype.setEntityType = function (value) {
                    _super.prototype.setEntityType.call(this, value);
                    return this;
                };
                GobiiTreeNode.prototype.getEntitySubType = function () {
                    return _super.prototype.getEntitySubType.call(this);
                };
                GobiiTreeNode.prototype.setEntitySubType = function (value) {
                    _super.prototype.setEntitySubType.call(this, value);
                    return this;
                };
                GobiiTreeNode.prototype.getCvGroup = function () {
                    return _super.prototype.getCvGroup.call(this);
                };
                GobiiTreeNode.prototype.setCvGroup = function (value) {
                    _super.prototype.setCvGroup.call(this, value);
                    return this;
                };
                GobiiTreeNode.prototype.getCvTerm = function () {
                    return _super.prototype.getCvTerm.call(this);
                };
                GobiiTreeNode.prototype.setCvTerm = function (value) {
                    _super.prototype.setCvTerm.call(this, value);
                    return this;
                };
                GobiiTreeNode.prototype.getCvFilterValue = function () {
                    return _super.prototype.getCvFilterValue.call(this);
                };
                GobiiTreeNode.prototype.setCvFilterValue = function (value) {
                    _super.prototype.setCvFilterValue.call(this, value);
                    return this;
                };
                GobiiTreeNode.prototype.getSequenceNum = function () {
                    return _super.prototype.getSequenceNum.call(this);
                };
                GobiiTreeNode.prototype.setSequenceNum = function (value) {
                    _super.prototype.setSequenceNum.call(this, value);
                    return this;
                };
                GobiiTreeNode.prototype.getIsExtractCriterion = function () {
                    return _super.prototype.getIsExtractCriterion.call(this);
                };
                GobiiTreeNode.prototype.setIsExtractCriterion = function (value) {
                    _super.prototype.setIsExtractCriterion.call(this, value);
                    return this;
                };
                GobiiTreeNode.prototype.setGobiiExtractFilterType = function (gobiiExtractFilterType) {
                    this.gobiiExtractFilterType = gobiiExtractFilterType;
                    return this;
                };
                GobiiTreeNode.prototype.getGobiiExtractFilterType = function () {
                    return this.gobiiExtractFilterType;
                };
                GobiiTreeNode.prototype.getActive = function () {
                    return this.active;
                };
                GobiiTreeNode.prototype.setActive = function (value) {
                    this.active = value;
                    return this;
                };
                GobiiTreeNode.prototype.getLabel = function () {
                    return this.label;
                };
                GobiiTreeNode.prototype.setLabel = function (value) {
                    this.label = value;
                    return this;
                };
                GobiiTreeNode.prototype.getGenericLabel = function () {
                    return this.genericLabel;
                };
                GobiiTreeNode.prototype.setGenericLabel = function (value) {
                    this.genericLabel = value;
                    return this;
                };
                GobiiTreeNode.prototype.resetLabel = function () {
                    this.label = this.genericLabel;
                };
                GobiiTreeNode.prototype.getData = function () {
                    return this.data;
                };
                GobiiTreeNode.prototype.setData = function (value) {
                    this.data = value;
                    return this;
                };
                GobiiTreeNode.prototype.getIcon = function () {
                    return this.icon;
                };
                GobiiTreeNode.prototype.setIcon = function (value) {
                    this.icon = value;
                    return this;
                };
                GobiiTreeNode.prototype.getExpandedIcon = function () {
                    return this.expandedIcon;
                };
                GobiiTreeNode.prototype.setExpandedIcon = function (value) {
                    this.expandedIcon = value;
                    return this;
                };
                GobiiTreeNode.prototype.getCollapsedIcon = function () {
                    return this.collapsedIcon;
                };
                GobiiTreeNode.prototype.setCollapsedIcon = function (value) {
                    this.collapsedIcon = value;
                    return this;
                };
                GobiiTreeNode.prototype.getChildren = function () {
                    return this.children;
                };
                GobiiTreeNode.prototype.setChildren = function (value) {
                    this.children = value;
                    return this;
                };
                GobiiTreeNode.prototype.getLeaf = function () {
                    return this.leaf;
                };
                GobiiTreeNode.prototype.setLeaf = function (value) {
                    this.leaf = value;
                    return this;
                };
                GobiiTreeNode.prototype.getExpanded = function () {
                    return this.expanded;
                };
                GobiiTreeNode.prototype.setExpanded = function (value) {
                    this.expanded = value;
                    return this;
                };
                GobiiTreeNode.prototype.getType = function () {
                    return this.type;
                };
                GobiiTreeNode.prototype.setType = function (value) {
                    this.type = value;
                    return this;
                };
                GobiiTreeNode.prototype.getPartialSelected = function () {
                    return this.partialSelected;
                };
                GobiiTreeNode.prototype.setPartialSelected = function (value) {
                    this.partialSelected = value;
                    return this;
                };
                GobiiTreeNode.prototype.getFileItemId = function () {
                    return this.fileItemId;
                };
                GobiiTreeNode.prototype.setFileItemId = function (value) {
                    this.fileItemId = value;
                    return this;
                };
                GobiiTreeNode.prototype.getRequired = function () {
                    return this.required;
                };
                GobiiTreeNode.prototype.setRequired = function (value) {
                    this.required = value;
                    return this;
                };
                GobiiTreeNode.prototype.getContainerType = function () {
                    return this.containerType;
                };
                GobiiTreeNode.prototype.setContainerType = function (value) {
                    this.containerType = value;
                    return this;
                };
                GobiiTreeNode.prototype.getChildCompoundUniqueId = function () {
                    if (!this.childCompoundUniqueId) {
                        this.childCompoundUniqueId = gobii_file_item_compound_id_1.GobiiFileItemCompoundId.fromGobiiFileItemCompoundId(this);
                    }
                    return this.childCompoundUniqueId;
                };
                GobiiTreeNode.prototype.setChildCompoundUniqueId = function (value) {
                    this.childCompoundUniqueId = value;
                    return this;
                };
                return GobiiTreeNode;
            }(gobii_file_item_compound_id_1.GobiiFileItemCompoundId));
            exports_1("GobiiTreeNode", GobiiTreeNode);
        }
    };
});
//# sourceMappingURL=gobii-tree-node.js.map