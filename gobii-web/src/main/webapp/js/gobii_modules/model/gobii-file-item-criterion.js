System.register(["./gobii-file-item-compound-id"], function (exports_1, context_1) {
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
    var gobii_file_item_compound_id_1, GobiiFileItemCriterion;
    return {
        setters: [
            function (gobii_file_item_compound_id_1_1) {
                gobii_file_item_compound_id_1 = gobii_file_item_compound_id_1_1;
            }
        ],
        execute: function () {
            GobiiFileItemCriterion = (function (_super) {
                __extends(GobiiFileItemCriterion, _super);
                function GobiiFileItemCriterion(gobiiFileItemCompoundId, _isPresent) {
                    if (_isPresent === void 0) { _isPresent = false; }
                    var _this = _super.call(this, gobiiFileItemCompoundId.getExtractorItemType(), gobiiFileItemCompoundId.getEntityType(), gobiiFileItemCompoundId.getEntitySubType(), gobiiFileItemCompoundId.getCvGroup(), gobiiFileItemCompoundId.getCvFilterValue()) || this;
                    _this._isPresent = _isPresent;
                    return _this;
                }
                GobiiFileItemCriterion.prototype.isPresent = function () {
                    return this._isPresent;
                };
                GobiiFileItemCriterion.prototype.setIPresent = function (value) {
                    this._isPresent = value;
                };
                return GobiiFileItemCriterion;
            }(gobii_file_item_compound_id_1.GobiiFileItemCompoundId));
            exports_1("GobiiFileItemCriterion", GobiiFileItemCriterion);
        }
    };
});
//# sourceMappingURL=gobii-file-item-criterion.js.map