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
                EntityType[EntityType["ANY"] = 1] = "ANY";
                EntityType[EntityType["ANALYSIS"] = 2] = "ANALYSIS";
                EntityType[EntityType["CONTACT"] = 3] = "CONTACT";
                EntityType[EntityType["DATASET"] = 4] = "DATASET";
                EntityType[EntityType["CV"] = 5] = "CV";
                EntityType[EntityType["CVGROUP"] = 6] = "CVGROUP";
                EntityType[EntityType["PROJECT"] = 7] = "PROJECT";
                EntityType[EntityType["ORGANIZATION"] = 8] = "ORGANIZATION";
                EntityType[EntityType["PLATFORM"] = 9] = "PLATFORM";
                EntityType[EntityType["MANIFEST"] = 10] = "MANIFEST";
                EntityType[EntityType["MAPSET"] = 11] = "MAPSET";
                EntityType[EntityType["MARKER_GROUP"] = 12] = "MARKER_GROUP";
                EntityType[EntityType["EXPERIMENT"] = 13] = "EXPERIMENT";
                EntityType[EntityType["REFERENCE"] = 14] = "REFERENCE";
                EntityType[EntityType["ROLE"] = 15] = "ROLE";
                EntityType[EntityType["DISPLAY"] = 16] = "DISPLAY";
                EntityType[EntityType["MARKER"] = 17] = "MARKER";
                EntityType[EntityType["PROTOCOL"] = 18] = "PROTOCOL";
                EntityType[EntityType["VENDOR_PROTOCOL"] = 19] = "VENDOR_PROTOCOL";
                EntityType[EntityType["GERMPLASM"] = 20] = "GERMPLASM";
                EntityType[EntityType["LINKAGE_GROUP"] = 21] = "LINKAGE_GROUP";
                EntityType[EntityType["DNA_SAMPLE"] = 22] = "DNA_SAMPLE";
                EntityType[EntityType["VENDOR"] = 23] = "VENDOR";
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
                        returnVal = EntityType.VENDOR_PROTOCOL;
                        break;
                    case "GERMPLASM":
                        returnVal = EntityType.GERMPLASM;
                        break;
                }
                return returnVal;
            });
            (function (EntitySubType) {
                EntitySubType[EntitySubType["UNKNOWN"] = 0] = "UNKNOWN";
                EntitySubType[EntitySubType["ANY"] = 1] = "ANY";
                EntitySubType[EntitySubType["CONTACT_PRINCIPLE_INVESTIGATOR"] = 2] = "CONTACT_PRINCIPLE_INVESTIGATOR";
                EntitySubType[EntitySubType["CONTACT_SUBMITED_BY"] = 3] = "CONTACT_SUBMITED_BY";
            })(EntitySubType || (EntitySubType = {}));
            exports_1("EntitySubType", EntitySubType);
        }
    };
});
//# sourceMappingURL=type-entity.js.map