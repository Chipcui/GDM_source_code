System.register([], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var EntityType, entityTypefromString, EntitySubType;
    return {
        setters: [],
        execute: function () {
            // these have to match what's on the server
            (function (EntityType) {
                EntityType[EntityType["UNKNOWN"] = 0] = "UNKNOWN";
                EntityType[EntityType["ANALYSIS"] = 1] = "ANALYSIS";
                EntityType[EntityType["CONTACT"] = 2] = "CONTACT";
                EntityType[EntityType["DATASET"] = 3] = "DATASET";
                EntityType[EntityType["CV"] = 4] = "CV";
                EntityType[EntityType["CVGROUP"] = 5] = "CVGROUP";
                EntityType[EntityType["PROJECT"] = 6] = "PROJECT";
                EntityType[EntityType["ORGANIZATION"] = 7] = "ORGANIZATION";
                EntityType[EntityType["PLATFORM"] = 8] = "PLATFORM";
                EntityType[EntityType["MANIFEST"] = 9] = "MANIFEST";
                EntityType[EntityType["MAPSET"] = 10] = "MAPSET";
                EntityType[EntityType["MARKER_GROUP"] = 11] = "MARKER_GROUP";
                EntityType[EntityType["EXPERIMENT"] = 12] = "EXPERIMENT";
                EntityType[EntityType["REFERENCE"] = 13] = "REFERENCE";
                EntityType[EntityType["ROLE"] = 14] = "ROLE";
                EntityType[EntityType["DISPLAY"] = 15] = "DISPLAY";
                EntityType[EntityType["MARKER"] = 16] = "MARKER";
                EntityType[EntityType["PROTOCOL"] = 17] = "PROTOCOL";
                EntityType[EntityType["VENDORS_PROTOCOL"] = 18] = "VENDORS_PROTOCOL";
            })(EntityType || (EntityType = {}));
            exports_1("EntityType", EntityType);
            exports_1("entityTypefromString", entityTypefromString = function (entityTypeName) {
                var returnVal = EntityType.UNKNOWN;
                switch (entityTypeName) {
                    case "ANALYSIS":
                        returnVal = EntityType.ANALYSIS;
                        break;
                    case "CONTACT":
                        returnVal = EntityType.CONTACT;
                        break;
                    case "DATASET":
                        returnVal = EntityType.DATASET;
                        break;
                    case "CV":
                        returnVal = EntityType.CV;
                        break;
                    case "CVGROUP":
                        returnVal = EntityType.CVGROUP;
                        break;
                    case "PROJECT":
                        returnVal = EntityType.PROJECT;
                        break;
                    case "ORGANIZATION":
                        returnVal = EntityType.ORGANIZATION;
                        break;
                    case "PLATFORM":
                        returnVal = EntityType.PLATFORM;
                        break;
                    case "MANIFEST":
                        returnVal = EntityType.MANIFEST;
                        break;
                    case "MAPSET":
                        returnVal = EntityType.MAPSET;
                        break;
                    case "MARKER_GROUP":
                        returnVal = EntityType.MARKER_GROUP;
                        break;
                    case "EXPERIMENT":
                        returnVal = EntityType.EXPERIMENT;
                        break;
                    case "REFERENCE":
                        returnVal = EntityType.REFERENCE;
                        break;
                    case "ROLE":
                        returnVal = EntityType.ROLE;
                        break;
                    case "DISPLAY":
                        returnVal = EntityType.DISPLAY;
                        break;
                    case "MARKER":
                        returnVal = EntityType.MARKER;
                        break;
                    case "PROTOCOL":
                        returnVal = EntityType.PROTOCOL;
                        break;
                    case "VENDOR_PROTOCOL":
                        returnVal = EntityType.VENDORS_PROTOCOL;
                        break;
                }
                return returnVal;
            });
            (function (EntitySubType) {
                EntitySubType[EntitySubType["UNKNOWN"] = 0] = "UNKNOWN";
                EntitySubType[EntitySubType["CONTACT_PRINCIPAL_INVESTIGATOR"] = 1] = "CONTACT_PRINCIPAL_INVESTIGATOR";
                EntitySubType[EntitySubType["CONTACT_SUBMITED_BY"] = 2] = "CONTACT_SUBMITED_BY";
            })(EntitySubType || (EntitySubType = {}));
            exports_1("EntitySubType", EntitySubType);
        }
    };
});
//# sourceMappingURL=type-entity.js.map