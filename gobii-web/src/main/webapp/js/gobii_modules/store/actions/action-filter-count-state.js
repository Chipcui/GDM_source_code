System.register([], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var FilterCountState;
    return {
        setters: [],
        execute: function () {
            FilterCountState = (function () {
                function FilterCountState(flexQueryFilters, markerCount, sampleCount) {
                    this.flexQueryFilters = flexQueryFilters;
                    this.markerCount = markerCount;
                    this.sampleCount = sampleCount;
                }
                return FilterCountState;
            }());
            exports_1("FilterCountState", FilterCountState);
        }
    };
});
//# sourceMappingURL=action-filter-count-state.js.map