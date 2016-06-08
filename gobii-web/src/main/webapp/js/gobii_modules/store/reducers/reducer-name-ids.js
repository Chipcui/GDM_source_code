System.register(['../actions/action-names'], function(exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var ActionNames;
    var initialState;
    function default_1(state, action) {
        if (state === void 0) { state = initialState; }
        switch (action.type) {
            case ActionNames.ADD_NAMEIDS:
                //let entityTypeToAdd:string = action.payload.entityType;
                state.nameIds.push({
                    entityType: action.payload.entityType,
                    nameIds: action.payload.nameIds
                });
                return state;
            default:
                return state;
        }
    }
    exports_1("default", default_1);
    function getNameIds(entityType) {
        return function (state$) { return state$
            .map(function (res) { return Object.keys(res).map(function (key) { return res[key]; }); }); };
    }
    exports_1("getNameIds", getNameIds);
    return {
        setters:[
            function (ActionNames_1) {
                ActionNames = ActionNames_1;
            }],
        execute: function() {
            initialState = {
                nameIds: [{}]
            };
            ;
        }
    }
});
// export function getProductEntities() {
//     return (state$:Observable<NameIdsState>) => state$
//         .select(s => s.entities);
// }
//
// export function getProductsAsArry() {
//     return (state$:Observable<NameIdsState>) => state$
//         .let(getProductEntities())
//         .map(res => Object.keys(res).map(key => res[key]));
// }
//# sourceMappingURL=reducer-name-ids.js.map