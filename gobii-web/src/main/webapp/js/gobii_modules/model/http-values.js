System.register(["./header-names", "@angular/common/http"], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var header_names_1, http_1, HttpValues;
    return {
        setters: [
            function (header_names_1_1) {
                header_names_1 = header_names_1_1;
            },
            function (http_1_1) {
                http_1 = http_1_1;
            }
        ],
        execute: function () {
            HttpValues = (function () {
                function HttpValues() {
                }
                HttpValues.makeTokenHeaders = function (token, gobiiCropType) {
                    var returnVal = this.makeContentHeaders();
                    returnVal.append(header_names_1.HeaderNames.headerToken, token);
                    returnVal.append(header_names_1.HeaderNames.headerGobiiCrop, gobiiCropType);
                    return returnVal;
                };
                HttpValues.makeContentHeaders = function () {
                    var returnVal = new http_1.HttpHeaders()
                        .append('Content-Type', 'application/json')
                        .append('Accept', 'application/json');
                    return returnVal;
                };
                HttpValues.makeLoginHeaders = function (userName, password) {
                    var returnVal = this.makeContentHeaders()
                        .append(header_names_1.HeaderNames.headerUserName, userName)
                        .append(header_names_1.HeaderNames.headerPassword, password);
                    return returnVal;
                };
                HttpValues.S_FORBIDDEN = 403;
                return HttpValues;
            }());
            exports_1("HttpValues", HttpValues);
        }
    };
});
//# sourceMappingURL=http-values.js.map