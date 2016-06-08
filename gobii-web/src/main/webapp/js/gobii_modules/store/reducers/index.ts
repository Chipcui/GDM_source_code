import '@ngrx/core/add/operator/select';
import 'rxjs/add/operator/switchMap';
import 'rxjs/add/operator/let';
import {Observable} from 'rxjs/Observable';

import {compose} from '@ngrx/core/compose';
import {storeLogger} from 'ngrx-store-logger';
import {combineReducers} from '@ngrx/store';

//import cartReducer, * as fromCart from './cart';
import nameIdsReducer, * as fromNameIds from './reducer-name-ids';

export interface AppState {
    nameIds:fromNameIds.NameIdsState;
    //products: fromProducts.ProductsState;
}

export default compose(storeLogger(), combineReducers)({
    nameIds: nameIdsReducer
//    products: productsReducer,
});


// export function getCartState() {
//     return (state$: Observable<AppState>) => state$
//         .select(s => s.cart);
// }

export function getNameIdState() {
    return (state$:Observable<AppState>) => state$
        .select(s => s.nameIds);
}


export function getNameIds(entityType:string) {
    return compose(fromNameIds
        .getNameIds(entityType), getNameIdState());
}


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