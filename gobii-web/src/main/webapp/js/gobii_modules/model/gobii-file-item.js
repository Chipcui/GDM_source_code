System.register(["./type-process", "./guid", "./type-entity", "./cv-group", "./type-extractor-filter", "./type-extractor-item", "./gobii-file-item-compound-id", "./gobii-file-item-entity-relation", "./name-id-label-type"], function (exports_1, context_1) {
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
    var type_process_1, guid_1, type_entity_1, cv_group_1, type_extractor_filter_1, type_extractor_item_1, gobii_file_item_compound_id_1, gobii_file_item_entity_relation_1, name_id_label_type_1, GobiiFileItem;
    return {
        setters: [
            function (type_process_1_1) {
                type_process_1 = type_process_1_1;
            },
            function (guid_1_1) {
                guid_1 = guid_1_1;
            },
            function (type_entity_1_1) {
                type_entity_1 = type_entity_1_1;
            },
            function (cv_group_1_1) {
                cv_group_1 = cv_group_1_1;
            },
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
            },
            function (type_extractor_item_1_1) {
                type_extractor_item_1 = type_extractor_item_1_1;
            },
            function (gobii_file_item_compound_id_1_1) {
                gobii_file_item_compound_id_1 = gobii_file_item_compound_id_1_1;
            },
            function (gobii_file_item_entity_relation_1_1) {
                gobii_file_item_entity_relation_1 = gobii_file_item_entity_relation_1_1;
            },
            function (name_id_label_type_1_1) {
                name_id_label_type_1 = name_id_label_type_1_1;
            }
        ],
        execute: function () {
            GobiiFileItem = (function (_super) {
                __extends(GobiiFileItem, _super);
                function GobiiFileItem(_gobiiExtractFilterType, _processType, _extractorItemType, _entityType, _entitySubType, _cvFilterType, _cvFilterValue, _itemId, _itemName, _selected, _required, _parentItemId, _entity, _entityRelations, _hasEntity, _pageNumber, _isEphemeral, _nameIdLabelType) {
                    if (_entity === void 0) { _entity = null; }
                    if (_entityRelations === void 0) { _entityRelations = []; }
                    if (_hasEntity === void 0) { _hasEntity = false; }
                    if (_isEphemeral === void 0) { _isEphemeral = false; }
                    var _this = _super.call(this, _extractorItemType, _entityType, _entitySubType, _cvFilterType, _cvFilterValue) || this;
                    _this._gobiiExtractFilterType = _gobiiExtractFilterType;
                    _this._processType = _processType;
                    _this._itemId = _itemId;
                    _this._itemName = _itemName;
                    _this._selected = _selected;
                    _this._required = _required;
                    _this._parentItemId = _parentItemId;
                    _this._entity = _entity;
                    _this._entityRelations = _entityRelations;
                    _this._hasEntity = _hasEntity;
                    _this._pageNumber = _pageNumber;
                    _this._isEphemeral = _isEphemeral;
                    _this._nameIdLabelType = _nameIdLabelType;
                    _this._gobiiExtractFilterType = _gobiiExtractFilterType;
                    _this._processType = _processType;
                    _this._itemId = _itemId;
                    _this._itemName = _itemName;
                    _this._selected = _selected;
                    _this._required = _required;
                    _this._parentItemId = _parentItemId;
                    _this._entityRelations = _entityRelations;
                    _this._entity = _entity;
                    _this._hasEntity = _hasEntity;
                    _this._pageNumber = _pageNumber;
                    _this._isEphemeral = _isEphemeral;
                    _this._nameIdLabelType = _nameIdLabelType;
                    _this._fileItemUniqueId = guid_1.Guid.generateUUID();
                    return _this;
                }
                GobiiFileItem.build = function (gobiiExtractFilterType, processType) {
                    var returnVal = new GobiiFileItem(gobiiExtractFilterType, processType, type_extractor_item_1.ExtractorItemType.UNKNOWN, type_entity_1.EntityType.UNKNOWN, type_entity_1.EntitySubType.UNKNOWN, cv_group_1.CvGroup.UNKNOWN, null, null, null, null, null, null, [], [], false, 0, false, name_id_label_type_1.NameIdLabelType.UNKNOWN);
                    return returnVal;
                };
                GobiiFileItem.prototype.setFileItemUniqueId = function (fileItemUniqueId) {
                    this._fileItemUniqueId = fileItemUniqueId;
                    return this;
                };
                GobiiFileItem.prototype.getFileItemUniqueId = function () {
                    return this._fileItemUniqueId;
                };
                GobiiFileItem.prototype.getGobiiExtractFilterType = function () {
                    return this._gobiiExtractFilterType;
                };
                GobiiFileItem.prototype.setGobiiExtractFilterType = function (value) {
                    if (value != null) {
                        this._gobiiExtractFilterType = value;
                    }
                    else {
                        this._gobiiExtractFilterType = type_extractor_filter_1.GobiiExtractFilterType.UNKNOWN;
                    }
                    return this;
                };
                GobiiFileItem.prototype.getProcessType = function () {
                    return this._processType;
                };
                GobiiFileItem.prototype.setProcessType = function (value) {
                    if (value != null) {
                        this._processType = value;
                    }
                    else {
                        this._processType = type_process_1.ProcessType.UNKNOWN;
                    }
                    return this;
                };
                GobiiFileItem.prototype.getExtractorItemType = function () {
                    return _super.prototype.getExtractorItemType.call(this);
                };
                GobiiFileItem.prototype.setExtractorItemType = function (value) {
                    _super.prototype.setExtractorItemType.call(this, value);
                    return this;
                };
                GobiiFileItem.prototype.getEntityType = function () {
                    return _super.prototype.getEntityType.call(this);
                };
                GobiiFileItem.prototype.setEntityType = function (value) {
                    _super.prototype.setEntityType.call(this, value);
                    return this;
                };
                GobiiFileItem.prototype.getEntitySubType = function () {
                    return _super.prototype.getEntitySubType.call(this);
                };
                GobiiFileItem.prototype.setEntitySubType = function (value) {
                    _super.prototype.setEntitySubType.call(this, value);
                    return this;
                };
                GobiiFileItem.prototype.getCvGroup = function () {
                    return _super.prototype.getCvGroup.call(this);
                };
                GobiiFileItem.prototype.setCvGroup = function (value) {
                    _super.prototype.setCvGroup.call(this, value);
                    return this;
                };
                GobiiFileItem.prototype.getCvTerm = function () {
                    return _super.prototype.getCvTerm.call(this);
                };
                GobiiFileItem.prototype.setCvTerm = function (value) {
                    _super.prototype.setCvTerm.call(this, value);
                    return this;
                };
                GobiiFileItem.prototype.getCvFilterValue = function () {
                    return _super.prototype.getCvFilterValue.call(this);
                };
                GobiiFileItem.prototype.setCvFilterValue = function (value) {
                    _super.prototype.setCvFilterValue.call(this, value);
                    return this;
                };
                GobiiFileItem.prototype.getIsExtractCriterion = function () {
                    return _super.prototype.getIsExtractCriterion.call(this);
                };
                GobiiFileItem.prototype.setIsExtractCriterion = function (value) {
                    _super.prototype.setIsExtractCriterion.call(this, value);
                    return this;
                };
                GobiiFileItem.prototype.getSequenceNum = function () {
                    return _super.prototype.getSequenceNum.call(this);
                };
                GobiiFileItem.prototype.setSequenceNum = function (value) {
                    _super.prototype.setSequenceNum.call(this, value);
                    return this;
                };
                GobiiFileItem.prototype.getItemId = function () {
                    return this._itemId;
                };
                GobiiFileItem.prototype.setItemId = function (value) {
                    this._itemId = value;
                    return this;
                };
                GobiiFileItem.prototype.getItemName = function () {
                    return this._itemName;
                };
                GobiiFileItem.prototype.setItemName = function (value) {
                    this._itemName = value;
                    return this;
                };
                GobiiFileItem.prototype.getSelected = function () {
                    return this._selected;
                };
                GobiiFileItem.prototype.setSelected = function (value) {
                    this._selected = value;
                    return this;
                };
                GobiiFileItem.prototype.getRequired = function () {
                    return this._required;
                };
                GobiiFileItem.prototype.setRequired = function (value) {
                    this._required = value;
                    return this;
                };
                GobiiFileItem.prototype.getParentItemId = function () {
                    return this._parentItemId;
                };
                GobiiFileItem.prototype.setParentItemId = function (parentIteIid) {
                    this._parentItemId = parentIteIid;
                    return this;
                };
                GobiiFileItem.prototype.setEntity = function (entity) {
                    this._entity = entity;
                    this._hasEntity = true;
                    return this;
                };
                GobiiFileItem.prototype.getEntity = function () {
                    return this._entity;
                };
                GobiiFileItem.prototype.hasEntity = function () {
                    return this._hasEntity;
                };
                GobiiFileItem.prototype.withRelatedEntityValue = function (gobiiFileItemEntityRelation, relatedId) {
                    var existingGobiiFileItemEntityRelation = this._entityRelations.find(function (er) { return er.compoundIdeEquals(gobiiFileItemEntityRelation); });
                    if (existingGobiiFileItemEntityRelation) {
                        existingGobiiFileItemEntityRelation.setRelatedEntityId(relatedId);
                    }
                    else {
                        this._entityRelations.push((new gobii_file_item_entity_relation_1.GobiiFileItemEntityRelation).setRelatedEntityId(relatedId));
                    }
                    return this;
                };
                GobiiFileItem.prototype.withRelatedEntity = function (newGobiiFileItemEntityRelation) {
                    var existingGobiiFileItemEntityRelation = this._entityRelations.find(function (er) { return er.compoundIdeEquals(newGobiiFileItemEntityRelation); });
                    if (existingGobiiFileItemEntityRelation) {
                        existingGobiiFileItemEntityRelation.setRelatedEntityId(newGobiiFileItemEntityRelation.getRelatedEntityId());
                    }
                    else {
                        this._entityRelations.push(newGobiiFileItemEntityRelation);
                    }
                    return this;
                };
                GobiiFileItem.prototype.getRelatedEntityFilterValue = function (compoundUniqueId) {
                    var returnVal = null;
                    var gobiiFileItemEntityRelation = this._entityRelations.find(function (er) { return er.compoundIdeEquals(compoundUniqueId); });
                    if (gobiiFileItemEntityRelation) {
                        returnVal = gobiiFileItemEntityRelation.getRelatedEntityId();
                    }
                    return returnVal;
                };
                GobiiFileItem.prototype.getRelatedEntities = function () {
                    return this._entityRelations;
                };
                GobiiFileItem.prototype.setPageNumber = function (value) {
                    this._pageNumber = value;
                    return this;
                };
                GobiiFileItem.prototype.getPageNumber = function () {
                    return this._pageNumber;
                };
                GobiiFileItem.prototype.getIsEphemeral = function () {
                    return this._isEphemeral;
                };
                GobiiFileItem.prototype.setIsEphemeral = function (value) {
                    this._isEphemeral = value;
                    return this;
                };
                GobiiFileItem.prototype.getNameIdLabelType = function () {
                    return this._nameIdLabelType;
                };
                GobiiFileItem.prototype.setNameIdLabelType = function (value) {
                    this._nameIdLabelType = value;
                    return this;
                };
                return GobiiFileItem;
            }(gobii_file_item_compound_id_1.GobiiFileItemCompoundId)); // GobiiFileItem()
            exports_1("GobiiFileItem", GobiiFileItem);
        }
    };
});
//# sourceMappingURL=gobii-file-item.js.map