System.register([], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var NameIdFilterParamTypes;
    return {
        setters: [],
        execute: function () {
            NameIdFilterParamTypes = (function () {
                // The problem with using an enum is that in the debugger
                // you cannot readily see what the enum type value means
                // Knowing the nameidfilterparam type on debugger inspection is
                // particularly important because of the generic way in which nameids
                // are treated in the components and services
                function NameIdFilterParamTypes() {
                }
                NameIdFilterParamTypes.CONTACT_PI = "Contact-PI";
                NameIdFilterParamTypes.EXPERIMENTS = "Experiments";
                NameIdFilterParamTypes.CV_DATATYPE = "Cv-DataType";
                NameIdFilterParamTypes.MAPSETS = "Mapsets";
                NameIdFilterParamTypes.PLATFORMS = "Platforms";
                NameIdFilterParamTypes.DATASETS_BY_EXPERIUMENT = "Datasets-by-experiment";
                NameIdFilterParamTypes.PROJECTS = "Projects";
                NameIdFilterParamTypes.MARKER_GROUPS = "Marker Groups";
                return NameIdFilterParamTypes;
            }());
            exports_1("NameIdFilterParamTypes", NameIdFilterParamTypes);
        }
    };
});
//# sourceMappingURL=type-nameid-filter-params.js.map