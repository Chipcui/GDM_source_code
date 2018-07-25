System.register([], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var FilterType;
    return {
        setters: [],
        execute: function () {
            (function (FilterType) {
                FilterType[FilterType["NONE"] = 0] = "NONE";
                FilterType[FilterType["NAMES_BY_TYPEID"] = 1] = "NAMES_BY_TYPEID";
                FilterType[FilterType["NAMES_BY_TYPE_NAME"] = 2] = "NAMES_BY_TYPE_NAME";
                FilterType[FilterType["NAMES_BY_NAME_LIST"] = 3] = "NAMES_BY_NAME_LIST";
                FilterType[FilterType["ENTITY_LIST"] = 4] = "ENTITY_LIST";
                FilterType[FilterType["ENTITY_BY_ID"] = 5] = "ENTITY_BY_ID";
                FilterType[FilterType["FLEX_QUERY_VERTICES"] = 6] = "FLEX_QUERY_VERTICES";
                FilterType[FilterType["FLEX_QUERY_VALUES"] = 7] = "FLEX_QUERY_VALUES";
            })(FilterType || (FilterType = {}));
            exports_1("FilterType", FilterType);
        }
    };
});
//# sourceMappingURL=filter-type.js.map