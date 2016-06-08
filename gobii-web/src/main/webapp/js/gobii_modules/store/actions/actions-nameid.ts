import {Action} from '@ngrx/store';
import * as ActionNames from '../actions/action-names';
import INameIdPayload from './payload-nameid'

export const getNameIds = () => {
    return <Action>{
        type: ActionNames.ADD_NAMEIDS,
        payload: INameIdPayload
    };
}

