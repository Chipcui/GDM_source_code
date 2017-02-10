System.register([], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var GobiiExtractFilterType;
    return {
        setters: [],
        execute: function () {
            (function (GobiiExtractFilterType) {
                GobiiExtractFilterType[GobiiExtractFilterType["WHOLE_DATASET"] = 0] = "WHOLE_DATASET";
                GobiiExtractFilterType[GobiiExtractFilterType["BY_MARKER"] = 1] = "BY_MARKER";
                GobiiExtractFilterType[GobiiExtractFilterType["BY_SAMPLE"] = 2] = "BY_SAMPLE";
            })(GobiiExtractFilterType || (GobiiExtractFilterType = {}));
            exports_1("GobiiExtractFilterType", GobiiExtractFilterType);
        }
    };
});
//# sourceMappingURL=type-extractor-filter.js.map