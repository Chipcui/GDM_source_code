System.register([], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var ExtractorItemType;
    return {
        setters: [],
        execute: function () {
            (function (ExtractorItemType) {
                ExtractorItemType[ExtractorItemType["UNKNOWN"] = 0] = "UNKNOWN";
                ExtractorItemType[ExtractorItemType["ENTITY"] = 1] = "ENTITY";
                ExtractorItemType[ExtractorItemType["MARKER_INPUT_FILE"] = 2] = "MARKER_INPUT_FILE";
                ExtractorItemType[ExtractorItemType["MARKER_LIST_ITEM"] = 3] = "MARKER_LIST_ITEM";
                ExtractorItemType[ExtractorItemType["SAMPLE_LIST_ITEM"] = 4] = "SAMPLE_LIST_ITEM";
                ExtractorItemType[ExtractorItemType["SAMPLE_INPUT_FILE"] = 5] = "SAMPLE_INPUT_FILE";
                ExtractorItemType[ExtractorItemType["EXPORT_FORMAT"] = 6] = "EXPORT_FORMAT";
                ExtractorItemType[ExtractorItemType["CROP_TYPE"] = 7] = "CROP_TYPE";
                ExtractorItemType[ExtractorItemType["JOB_ID"] = 8] = "JOB_ID";
                ExtractorItemType[ExtractorItemType["SAMPLE_LIST_TYPE"] = 9] = "SAMPLE_LIST_TYPE";
                ExtractorItemType[ExtractorItemType["LABEL"] = 10] = "LABEL";
                ExtractorItemType[ExtractorItemType["TREE_STRUCTURE"] = 11] = "TREE_STRUCTURE";
                ExtractorItemType[ExtractorItemType["VERTEX"] = 12] = "VERTEX";
                ExtractorItemType[ExtractorItemType["VERTEX_VALUE"] = 13] = "VERTEX_VALUE";
                ExtractorItemType[ExtractorItemType["ITEM_COUNT"] = 14] = "ITEM_COUNT";
                ExtractorItemType[ExtractorItemType["MARKER_RESULT_FILE"] = 15] = "MARKER_RESULT_FILE";
                ExtractorItemType[ExtractorItemType["SAMPLE_RESULT_FILE"] = 16] = "SAMPLE_RESULT_FILE";
            })(ExtractorItemType || (ExtractorItemType = {}));
            exports_1("ExtractorItemType", ExtractorItemType);
        }
    };
});
//# sourceMappingURL=type-extractor-item.js.map