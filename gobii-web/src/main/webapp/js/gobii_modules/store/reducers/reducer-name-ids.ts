//import '@ngrx/core/add/operator/select';
import {Action} from '@ngrx/store';
import {Observable} from 'rxjs/Observable';
import * as ActionNames from '../actions/action-names';
import {NameId} from '../../model/name-id';


export interface NameIdsState {
    nameIds:[{ [entityType:string]:[NameId[]] } ];
}

const initialState:NameIdsState = {
    nameIds: [{}]
};

export default function (state = initialState, action:Action):NameIdsState {

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
};


export function getNameIds(entityType:string) {
    return (state$:Observable<NameIdsState>) => state$
        .map(res => Object.keys(res).map(key => res[key]));
}


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
