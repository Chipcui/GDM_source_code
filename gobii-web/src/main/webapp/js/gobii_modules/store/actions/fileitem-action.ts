import {Action} from '@ngrx/store';
import {GobiiFileItem} from "../../model/gobii-file-item";
import {NameIdRequestParams} from "../../model/name-id-request-params";
import {GobiiExtractFilterType} from "../../model/type-extractor-filter";

export const LOAD = '[GobiiFileItem] Load';
export const SELECT_FOR_EXTRACT = '[GobiiFileItem] Select';
export const DESELECT_FOR_EXTRACT = '[GobiiFileItem] DeSelect';
export const SET_ENTITY_FILTER = '[GobiiFileItem] SetEntityFilter';
export const LOAD_FILTERED_ITEMS = '[GobiiFileItem] LoadFilteredItems';


/**
 * Every action is comprised of at least a type and an optional
 * payload. Expressing actions as classes enables powerful
 * type checking in fileItemsReducer functions.
 *
 * See Discriminated Unions: https://www.typescriptlang.org/docs/handbook/advanced-types.html#discriminated-unions
 */
export class LoadAction implements Action {
    readonly type = LOAD;

    constructor(public payload: GobiiFileItem[]) {
    }
}

export class LoadFilteredItemsAction implements Action {
    readonly type = LOAD_FILTERED_ITEMS;

    constructor(public payload: {
        gobiiFileItems: GobiiFileItem[],
        nameIdRequestParams: NameIdRequestParams
    }) {
    }
}

export class DeSelectForExtractAction implements Action {
    readonly type = DESELECT_FOR_EXTRACT;

    constructor(public payload: GobiiFileItem) {
    }
}

export class SelectForExtractAction implements Action {
    readonly type = SELECT_FOR_EXTRACT;

    constructor(public payload: GobiiFileItem) {
    }
}

export class SetEntityFilter implements Action {
    readonly type = SET_ENTITY_FILTER;

    constructor(public payload: {
        gobiiExtractFilterType: GobiiExtractFilterType,
        nameIdRequestParams: NameIdRequestParams
    }) {
    }
}


/**
 * Export a type alias of all actions in this action group
 * so that reducers can easily compose action types
 */
export type All
    = LoadAction
    | LoadFilteredItemsAction
    | SelectForExtractAction
    | DeSelectForExtractAction
    | SetEntityFilter;

