System.register(["./type-entity"], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var type_entity_1, FileName;
    return {
        setters: [
            function (type_entity_1_1) {
                type_entity_1 = type_entity_1_1;
            }
        ],
        execute: function () {
            FileName = (function () {
                function FileName() {
                }
                FileName.makeUniqueFileId = function () {
                    var date = new Date();
                    var returnVal = date.getFullYear()
                        + "_"
                        + ('0' + (date.getMonth() + 1)).slice(-2)
                        + "_"
                        + ('0' + date.getDate()).slice(-2)
                        + "_"
                        + ('0' + date.getHours()).slice(-2)
                        + "_"
                        + ('0' + date.getMinutes()).slice(-2)
                        + "_"
                        + ('0' + date.getSeconds()).slice(-2);
                    return returnVal;
                };
                ;
                FileName.makeFileNameFromJobId = function (targetEntityType, jobId) {
                    var returnVal;
                    var suffix = null;
                    if (targetEntityType === type_entity_1.EntityType.MARKER) {
                        suffix = "_markers";
                    }
                    else {
                        suffix = "_samples";
                    }
                    returnVal = jobId + suffix + ".txt";
                    return returnVal;
                };
                return FileName;
            }());
            exports_1("FileName", FileName);
        }
    };
});
//# sourceMappingURL=file_name.js.map