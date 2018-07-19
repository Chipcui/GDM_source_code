System.register(["./type-entity", "./cv-group", "./type-extractor-item"], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var type_entity_1, cv_group_1, type_extractor_item_1, GobiiFileItemCompoundId;
    return {
        setters: [
            function (type_entity_1_1) {
                type_entity_1 = type_entity_1_1;
            },
            function (cv_group_1_1) {
                cv_group_1 = cv_group_1_1;
            },
            function (type_extractor_item_1_1) {
                type_extractor_item_1 = type_extractor_item_1_1;
            }
        ],
        execute: function () {
            GobiiFileItemCompoundId = (function () {
                function GobiiFileItemCompoundId(_extractorItemType, _entityType, _entitySubType, _cvGroup, _cvTerm, _cvFilterValue, _isExtractCriterion, _sequenceNum) {
                    if (_extractorItemType === void 0) { _extractorItemType = type_extractor_item_1.ExtractorItemType.UNKNOWN; }
                    if (_entityType === void 0) { _entityType = type_entity_1.EntityType.UNKNOWN; }
                    if (_entitySubType === void 0) { _entitySubType = type_entity_1.EntitySubType.UNKNOWN; }
                    if (_cvGroup === void 0) { _cvGroup = cv_group_1.CvGroup.UNKNOWN; }
                    if (_cvTerm === void 0) { _cvTerm = null; }
                    if (_cvFilterValue === void 0) { _cvFilterValue = null; }
                    if (_isExtractCriterion === void 0) { _isExtractCriterion = true; }
                    if (_sequenceNum === void 0) { _sequenceNum = 0; }
                    this._extractorItemType = _extractorItemType;
                    this._entityType = _entityType;
                    this._entitySubType = _entitySubType;
                    this._cvGroup = _cvGroup;
                    this._cvTerm = _cvTerm;
                    this._cvFilterValue = _cvFilterValue;
                    this._isExtractCriterion = _isExtractCriterion;
                    this._sequenceNum = _sequenceNum;
                    if (this._cvGroup === null) {
                        this._cvGroup = cv_group_1.CvGroup.UNKNOWN;
                    }
                    if (this._extractorItemType == null) {
                        this._extractorItemType = type_extractor_item_1.ExtractorItemType.UNKNOWN;
                    }
                    if (this._entityType == null) {
                        this._entityType = type_entity_1.EntityType.UNKNOWN;
                    }
                    if (this._entitySubType == null) {
                        this._entitySubType = type_entity_1.EntitySubType.UNKNOWN;
                    }
                }
                GobiiFileItemCompoundId.fromGobiiFileItemCompoundId = function (gobiiFileItemCompoundId) {
                    return new GobiiFileItemCompoundId(gobiiFileItemCompoundId.getExtractorItemType(), gobiiFileItemCompoundId.getEntityType(), gobiiFileItemCompoundId.getEntitySubType(), gobiiFileItemCompoundId.getCvGroup(), gobiiFileItemCompoundId.getCvTerm(), gobiiFileItemCompoundId.getCvFilterValue(), gobiiFileItemCompoundId.getIsExtractCriterion(), gobiiFileItemCompoundId.getSequenceNum());
                };
                GobiiFileItemCompoundId.prototype.compoundIdeEquals = function (gobiiFileItemCompoundId) {
                    return (
                    // (this.getSequenceNum() > 0 &&
                    //     this.getSequenceNum() === gobiiFileItemCompoundId.getSequenceNum())
                    // ||
                    (this.getExtractorItemType() === gobiiFileItemCompoundId.getExtractorItemType() // for FlexQuery filter items
                        && (this.getEntityType() === gobiiFileItemCompoundId.getEntityType())
                        && (this.getEntitySubType() === gobiiFileItemCompoundId.getEntitySubType())
                        && (this.getCvGroup() === gobiiFileItemCompoundId.getCvGroup())
                        && this.getCvFilterValue() === gobiiFileItemCompoundId.getCvFilterValue()));
                };
                GobiiFileItemCompoundId.prototype.getExtractorItemType = function () {
                    return this._extractorItemType;
                };
                GobiiFileItemCompoundId.prototype.setExtractorItemType = function (value) {
                    if (value != null) {
                        this._extractorItemType = value;
                    }
                    else {
                        this._extractorItemType = type_extractor_item_1.ExtractorItemType.UNKNOWN;
                    }
                    return this;
                };
                GobiiFileItemCompoundId.prototype.getEntityType = function () {
                    return this._entityType;
                };
                GobiiFileItemCompoundId.prototype.setEntityType = function (value) {
                    if (value != null) {
                        this._entityType = value;
                    }
                    else {
                        this._entityType = type_entity_1.EntityType.UNKNOWN;
                    }
                    return this;
                };
                GobiiFileItemCompoundId.prototype.getEntitySubType = function () {
                    return this._entitySubType;
                };
                GobiiFileItemCompoundId.prototype.setEntitySubType = function (value) {
                    if (value != null) {
                        this._entitySubType = value;
                    }
                    else {
                        this._entitySubType = type_entity_1.EntitySubType.UNKNOWN;
                    }
                    return this;
                };
                GobiiFileItemCompoundId.prototype.getCvGroup = function () {
                    return this._cvGroup;
                };
                GobiiFileItemCompoundId.prototype.setCvGroup = function (value) {
                    if (value != null) {
                        this._cvGroup = value;
                    }
                    else {
                        this._cvGroup = cv_group_1.CvGroup.UNKNOWN;
                    }
                    return this;
                };
                GobiiFileItemCompoundId.prototype.getCvTerm = function () {
                    return this._cvTerm;
                };
                GobiiFileItemCompoundId.prototype.setCvTerm = function (value) {
                    this._cvTerm = value;
                    return this;
                };
                GobiiFileItemCompoundId.prototype.getCvFilterValue = function () {
                    return this._cvFilterValue;
                };
                GobiiFileItemCompoundId.prototype.setCvFilterValue = function (value) {
                    this._cvFilterValue = value;
                    return this;
                };
                GobiiFileItemCompoundId.prototype.getIsExtractCriterion = function () {
                    return this._isExtractCriterion;
                };
                GobiiFileItemCompoundId.prototype.setIsExtractCriterion = function (value) {
                    this._isExtractCriterion = value;
                    return this;
                };
                GobiiFileItemCompoundId.prototype.getSequenceNum = function () {
                    return this._sequenceNum;
                };
                GobiiFileItemCompoundId.prototype.setSequenceNum = function (value) {
                    this._sequenceNum = value;
                    return this;
                };
                return GobiiFileItemCompoundId;
            }());
            exports_1("GobiiFileItemCompoundId", GobiiFileItemCompoundId);
        }
    };
});
//# sourceMappingURL=gobii-file-item-compound-id.js.map