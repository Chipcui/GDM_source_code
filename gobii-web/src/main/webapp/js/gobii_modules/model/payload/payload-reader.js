System.register(["./payload-envelope"], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var payload_envelope_1, PayloadReader;
    return {
        setters: [
            function (payload_envelope_1_1) {
                payload_envelope_1 = payload_envelope_1_1;
            }
        ],
        execute: function () {
            PayloadReader = (function () {
                function PayloadReader(json, dtoRequestItem, rawBody) {
                    this.json = json;
                    this.dtoRequestItem = dtoRequestItem;
                    this.rawBody = rawBody;
                    if (json) {
                        this.payloadEnvelope = payload_envelope_1.PayloadEnvelope.fromJSON(json);
                        if (this.payloadEnvelope.header.status.succeeded) {
                            this.data = this.dtoRequestItem.resultFromJson(this.json);
                        }
                    }
                }
                PayloadReader.prototype.succeeded = function () {
                    return this.payloadEnvelope && this.payloadEnvelope.header.status.succeeded;
                };
                PayloadReader.prototype.getData = function () {
                    return this.data;
                };
                PayloadReader.prototype.getError = function () {
                    var returnVal = "";
                    if (this.payloadEnvelope) {
                        this.payloadEnvelope.header.status.statusMessages.forEach(function (statusMessage) {
                            returnVal += statusMessage.message;
                        });
                    }
                    else {
                        returnVal = this.rawBody;
                    }
                    return returnVal;
                };
                return PayloadReader;
            }());
            exports_1("PayloadReader", PayloadReader);
        }
    };
});
//# sourceMappingURL=payload-reader.js.map