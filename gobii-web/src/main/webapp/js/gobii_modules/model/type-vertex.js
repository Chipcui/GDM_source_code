System.register([], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var VertexType;
    return {
        setters: [],
        execute: function () {
            (function (VertexType) {
                VertexType[VertexType["UNKNOWN"] = 0] = "UNKNOWN";
                VertexType[VertexType["ENTITY"] = 1] = "ENTITY";
                VertexType[VertexType["SUBENTITY"] = 2] = "SUBENTITY";
                VertexType[VertexType["CVGROUP"] = 3] = "CVGROUP";
                VertexType[VertexType["CVTERM"] = 4] = "CVTERM";
            })(VertexType || (VertexType = {}));
            exports_1("VertexType", VertexType);
            // export const vertexTypeFromString = function (vertexTypeName: String): VertexType {
            //
            //     let returnVal: VertexType = VertexType.UNKNOWN;
            //
            //     return returnVal;
            //
            // }
        }
    };
});
//# sourceMappingURL=type-vertex.js.map