System.register(['@ngrx/core/add/operator/select', 'rxjs/add/operator/switchMap', 'rxjs/add/operator/let', '@ngrx/core/compose', 'ngrx-store-logger', '@ngrx/store', './reducer-name-ids'], function(exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var compose_1, ngrx_store_logger_1, store_1, reducer_name_ids_1;
    // export function getCartState() {
    //     return (state$: Observable<AppState>) => state$
    //         .select(s => s.cart);
    // }
    function getNameIdState() {
        return function (state$) { return state$
            .select(function (s) { return s.nameIds; }); };
    }
    exports_1("getNameIdState", getNameIdState);
    function getNameIds(entityType) {
        return compose_1.compose(fromNameIds
            .getNameIds(entityType), getNameIdState());
    }
    exports_1("getNameIds", getNameIds);
    return {
        setters:[
            function (_1) {},
            function (_2) {},
            function (_3) {},
            function (compose_1_1) {
                compose_1 = compose_1_1;
            },
            function (ngrx_store_logger_1_1) {
                ngrx_store_logger_1 = ngrx_store_logger_1_1;
            },
            function (store_1_1) {
                store_1 = store_1_1;
            },
            function (reducer_name_ids_1_1) {
                reducer_name_ids_1 = reducer_name_ids_1_1;
            }],
        execute: function() {
            exports_1("default",compose_1.compose(ngrx_store_logger_1.storeLogger(), store_1.combineReducers)({
                nameIds: reducer_name_ids_1.default
            }));
        }
    }
});
// export function getProductsAsArry() {
//     return compose(fromProducts.getProductsAsArry(), getNameIdState());
// }
// export function getCalculatedCartList() {
//     return (state$: Observable<AppState>) => {
//         return Observable
//             .combineLatest(state$.let(getCartState()), state$.let(getProductEntities()))
//             .map((res: any) => {
//                 return res[0].productIds.map(productId => {
//                     return {
//                         title: res[1][productId].title,
//                         price: res[1][productId].price,
//                         quantity: res[0].quantityById[productId]
//                     };
//                 });
//             });
//     };
//} 
//# sourceMappingURL=index.js.map