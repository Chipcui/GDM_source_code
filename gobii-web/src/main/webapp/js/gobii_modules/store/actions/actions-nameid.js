System.register(['../actions/action-names', './payload-nameid'], function(exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var ActionNames, payload_nameid_1;
    var getNameIds;
    return {
        setters:[
            function (ActionNames_1) {
                ActionNames = ActionNames_1;
            },
            function (payload_nameid_1_1) {
                payload_nameid_1 = payload_nameid_1_1;
            }],
        execute: function() {
            exports_1("getNameIds", getNameIds = function () {
                return {
                    type: ActionNames.ADD_NAMEIDS,
                    payload: payload_nameid_1.default
                };
            });
        }
    }
});
//# sourceMappingURL=actions-nameid.js.map