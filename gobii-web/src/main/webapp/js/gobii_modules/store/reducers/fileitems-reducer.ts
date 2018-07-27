import {createSelector} from 'reselect';
import {GobiiFileItem} from "../../model/gobii-file-item";
import * as gobiiFileItemAction from "../actions/fileitem-action";
import {ExtractorItemType} from "../../model/type-extractor-item";
import {EntitySubType, EntityType} from "../../model/type-entity";
import {FilterParamNames} from "../../model/file-item-param-names";
import {ProcessType} from "../../model/type-process";
import {Labels} from "../../views/entity-labels";
import {GobiiExtractFilterType} from "../../model/type-extractor-filter";
import {GobiiExtractFormat} from "../../model/type-extract-format";
import {CvGroup} from "../../model/cv-group";
import {GobiiSampleListType} from "../../model/type-extractor-sample-list";
import {GobiiFileItemCompoundId} from "../../model/gobii-file-item-compound-id";
import {PayloadFilter} from "../actions/action-payload-filter";
import {NameIdLabelType} from "../../model/name-id-label-type";
import {FilterCountState} from "../actions/action-filter-count-state";
import {VertexNameType} from "../../model/type-vertex-name";


/***
 * By definition, GobiiFileItems are read-only; we only every get them
 * from the server and display them and select them for an extract.
 * CRUD operations on the entities themselves (e.g., Projects, Experiments, etc.)
 * will be handled each in their own fileItemsReducer.
 */
export interface State {
    gobiiExtractFilterType: GobiiExtractFilterType,
    uniqueIdsOfExtractFileItems: string[];
    allFileItems: GobiiFileItem[];
    filters: Map<string, PayloadFilter>;
};

export const initialState: State = {
    gobiiExtractFilterType: GobiiExtractFilterType.UNKNOWN,
    uniqueIdsOfExtractFileItems: [],
    allFileItems: [],
    filters: new Map<string, PayloadFilter>()
};

function addToExtractItems(state: State, gobiiFileItem: GobiiFileItem): State {


    if (!state.allFileItems.find(fi => fi.getFileItemUniqueId() === gobiiFileItem.getFileItemUniqueId())) {

        console.log("Item is not in the collection: " + gobiiFileItem.getItemName() + " of type " + Labels.instance().treeExtractorTypeLabels[gobiiFileItem.getExtractorItemType()]);
    }

    gobiiFileItem.setSelected(true);

    let newSelectedUniqueIdsState: string[] = state.uniqueIdsOfExtractFileItems.slice();

    if (!newSelectedUniqueIdsState.find(id => id === gobiiFileItem.getFileItemUniqueId())) {
        newSelectedUniqueIdsState.push(gobiiFileItem.getFileItemUniqueId());
    }


    let returnVal: State = {
        gobiiExtractFilterType: state.gobiiExtractFilterType,
        allFileItems: state.allFileItems,
        uniqueIdsOfExtractFileItems: newSelectedUniqueIdsState,
        filters: state.filters
    };

    return returnVal;
}

function removeFromExtractItems(state: State, gobiiFileItem: GobiiFileItem): State {

//    gobiiFileItem.setSelected(false);
    let newSelectedUniqueIdsState: string[] = state.uniqueIdsOfExtractFileItems.slice();

    // if we don't find it, we don't raise an error: the intent was to remove, but it just wasn't there,
    // so the state when this function exits is as expected
    let idx: number = newSelectedUniqueIdsState.findIndex(id => id === gobiiFileItem.getFileItemUniqueId())
    if (idx > -1) {
        newSelectedUniqueIdsState.splice(idx, 1);
    }

    let newFileItemState: GobiiFileItem[] = state.allFileItems.slice();
    if (gobiiFileItem.getIsEphemeral()) {
        newFileItemState = newFileItemState.filter(fi => !fi.getIsEphemeral());
    } else {
        let fileItemInNewState: GobiiFileItem = newFileItemState
            .find(fi => fi.getFileItemUniqueId() === gobiiFileItem.getFileItemUniqueId());

        if (fileItemInNewState) {
            fileItemInNewState.setSelected(false);
        }
    }


    let returnVal: State = {
        gobiiExtractFilterType: state.gobiiExtractFilterType,
        allFileItems: newFileItemState,
        uniqueIdsOfExtractFileItems: newSelectedUniqueIdsState,
        filters: state.filters
    };

    return returnVal;

}


export function fileItemsReducer(state: State = initialState, action: gobiiFileItemAction.All): State {

    let returnVal: State = state;

    switch (action.type) {


        case gobiiFileItemAction.LOAD_FILE_ITEM: {
            const gobiiFileItemPayloadItem = action.payload.gobiiFileItem;

            let newFileItemsItemsState: GobiiFileItem[] = state.allFileItems.slice();
            const newSelectedFileItemUniqueIdsState: string[] = state.uniqueIdsOfExtractFileItems.slice();


            let existingItem: GobiiFileItem = newFileItemsItemsState.find(stateItem =>
                stateItem.compoundIdeEquals(gobiiFileItemPayloadItem)
                && stateItem.getItemId() === gobiiFileItemPayloadItem.getItemId()
            );

            if (existingItem) {

                let existingSelectedUniqueId: string = newSelectedFileItemUniqueIdsState
                    .find(id => id === existingItem.getFileItemUniqueId());

                if (existingSelectedUniqueId) {
                    newSelectedFileItemUniqueIdsState.splice(newSelectedFileItemUniqueIdsState.indexOf(existingSelectedUniqueId), 1);
                    newSelectedFileItemUniqueIdsState.push(gobiiFileItemPayloadItem.getFileItemUniqueId());
                }

                newFileItemsItemsState = newFileItemsItemsState.filter(fi => fi.getFileItemUniqueId() != existingItem.getFileItemUniqueId());
                newFileItemsItemsState.push(gobiiFileItemPayloadItem)


            } else {
                newFileItemsItemsState.push(gobiiFileItemPayloadItem);
            }

            returnVal = {
                gobiiExtractFilterType: state.gobiiExtractFilterType,
                uniqueIdsOfExtractFileItems: newSelectedFileItemUniqueIdsState,
                allFileItems: newFileItemsItemsState,
                filters: state.filters
            };

            break;
        } // LOAD_FILE_ITEM

        case gobiiFileItemAction.LOAD_FILE_ITEM_LIST_WITH_FILTER: {
            const gobiiFileItemsPayload = action.payload.gobiiFileItems;
            const filterId = action.payload.filterId.toString();
            const filterValue = action.payload.filter;

            const newGobiiFileItems = gobiiFileItemsPayload.filter(newItem =>
                state
                    .allFileItems
                    .filter(stateItem =>
                        (
                            stateItem.getGobiiExtractFilterType() === newItem.getGobiiExtractFilterType() &&
                            stateItem.compoundIdeEquals(newItem) &&
                            stateItem.getItemId() === newItem.getItemId() &&
                            (
                                (stateItem.getEntity() === null && newItem.getEntity() === null)
                                || (
                                    (stateItem.getEntity() !== null && newItem.getEntity() !== null)
                                    && (stateItem.getEntity().id === newItem.getEntity().id)
                                )
                            )
                        )
                    ).length === 0
            );

            let newFilterState = Object.assign({}, state.filters);
            newFilterState[filterId] = filterValue;


            returnVal = {
                gobiiExtractFilterType: state.gobiiExtractFilterType,
                uniqueIdsOfExtractFileItems: state.uniqueIdsOfExtractFileItems,
                allFileItems: [...state.allFileItems, ...newGobiiFileItems],
                filters: newFilterState
            };

            break;
        } // LOAD_FILE_ITEM_LIST_WITH_FILTER

        case gobiiFileItemAction.LOAD_FILTER: {

            const filterId: string = action.payload.filterId.toString();
            const filterPayload = action.payload.filter;

            let newFilterState = Object.assign({}, state.filters);
            newFilterState[filterId] = filterPayload;


            /***
             * The following lines are intended to solve an oddly thorny problem.
             On the dataset tab, if you don't set the selected label item here in state,
             when you set the PI filter back to All PI’s, the two child lists are filtered
             as they should be, but the top-most item in the list doesn’t change – they
             don’t revert to “All Projects” and “All Experiments.” The problem seems to be
             that once the list has decided what the top item is, if _none_ of the items
             in the list has selected==true, it just stays where it is in the list: there
             needs to be a differentiator. It also matters significantly that you set the
             selected item in the reducer: if you do it elsewhere it does not work
             consistently (as you would expect whenever changing state).

             This particular solution is not ideal. Its conditions are organized around
             the particular details of this problem -- it's not generaelizable. Most imporantly,
             the filter that's applied to figure out the target list of
             items for which the first Label needs to be selected should be exactly the same
             filter as the one that populated the drop-down in question. I think the best way
             to do this is for the content of the filter to be associated with with the Filterparams
             and for the reducer to have access to that. So the selector methods in the reducer would
             just get the FilterParams object and apply its filter semantics. The LOAD_ITEMS action would
             then use exactly the same filter to determine the list of items to focus on. But this would
             be a more substantial change than what's needed to fix the immediate issue.
             Also, the isExtractCriterion condition seems a bit arbitrary. For now, it just means -- this
             group of controls that I happen to know require this treatment.

             */
            let newFileItemState: GobiiFileItem[] = state.allFileItems.slice();

            if (!newFilterState[filterId].targetEntityUniqueId.getIsExtractCriterion()) {
                let gobiiFileItemCompoundId: GobiiFileItemCompoundId = newFilterState[filterId].targetEntityUniqueId;
                let allItemsForFilter: GobiiFileItem[] = newFileItemState
                    .filter(gfi => {
                        return (gfi.getGobiiExtractFilterType() === state.gobiiExtractFilterType
                            && gfi.getExtractorItemType() === ExtractorItemType.ENTITY
                            || gfi.getNameIdLabelType() !== NameIdLabelType.UNKNOWN)
                            && gfi.getProcessType() !== ProcessType.DUMMY
                            && gfi.getEntityType() === gobiiFileItemCompoundId.getEntityType()
                    });

                allItemsForFilter.forEach(gfi => {
                    gfi.setSelected(false)
                })

                if (filterPayload.targetEntityFilterValue && +filterPayload.targetEntityFilterValue > 0) {

                    let selectedItem: GobiiFileItem = allItemsForFilter.find(gfi => gfi.getItemId() === filterPayload.targetEntityFilterValue);
                    if (selectedItem) {
                        selectedItem.setSelected(true);
                    }

                } else {
                    if (allItemsForFilter[0]) {
                        if ((!filterPayload.relatedEntityFilterValue || +filterPayload.relatedEntityFilterValue < 1)) {
                            allItemsForFilter[0].setSelected(true);
                        }
                    }
                }
            }

            returnVal = {
                gobiiExtractFilterType: state.gobiiExtractFilterType,
                uniqueIdsOfExtractFileItems: state.uniqueIdsOfExtractFileItems,
                allFileItems: newFileItemState,
                filters: newFilterState
            };

            break;
        } // LOAD_FILTER

        case
        gobiiFileItemAction.ADD_TO_EXTRACT
        : {

            const gobiiFileItemPayload: GobiiFileItem = action.payload;

            returnVal = addToExtractItems(state, gobiiFileItemPayload);

            break;
        } // ADD_TO_EXTRACT

        case gobiiFileItemAction.ADD_TO_EXTRACT_BY_ITEM_ID: {

            const fileItemUniqueIdPayload: string = action.payload;
            let gobiiFileItem: GobiiFileItem = state
                .allFileItems
                .find(fi => fi.getFileItemUniqueId() === fileItemUniqueIdPayload);

            returnVal = addToExtractItems(state, gobiiFileItem);

            break;

        } //

        case
        gobiiFileItemAction.REPLACE_BY_ITEM_ID: {

            let itemCurrentlyInExtract: GobiiFileItem = state
                .allFileItems
                .find(fi => fi.getFileItemUniqueId() === action.payload.itemIdCurrentlyInExtract);


            if (itemCurrentlyInExtract.getIsExtractCriterion()) {
                let itemToReplaceItWith: GobiiFileItem = state
                    .allFileItems
                    .find(fi => fi.getFileItemUniqueId() === action.payload.itemIdToReplaceItWith);

                let stateAfterRemove: State = removeFromExtractItems(state, itemCurrentlyInExtract);
                returnVal = addToExtractItems(stateAfterRemove, itemToReplaceItWith);
            } else {
                // there is also an effect that does additional things with this action
                return state;
            }
            break;
        }

        case gobiiFileItemAction.REPLACE_ITEM_OF_SAME_COMPOUND_ID: {

            let newItemToAdd: GobiiFileItem = action.payload.gobiiFileitemToReplaceWith;

            let fileItemToReplace: GobiiFileItem = state.allFileItems
                .find(fi => fi.getGobiiExtractFilterType() === newItemToAdd.getGobiiExtractFilterType()
                    && fi.compoundIdeEquals(newItemToAdd));

            let stateWithNewFileItem: State = {
                gobiiExtractFilterType: state.gobiiExtractFilterType,
                allFileItems: state.allFileItems,
                uniqueIdsOfExtractFileItems: state.uniqueIdsOfExtractFileItems,
                filters: state.filters
            };


            // remove existing item if applicable
            if (fileItemToReplace) {
                stateWithNewFileItem.allFileItems =
                    stateWithNewFileItem.allFileItems.filter(fi =>
                        fi.getFileItemUniqueId() !== fileItemToReplace.getFileItemUniqueId());
            }

            // add new item
            stateWithNewFileItem.allFileItems.push(newItemToAdd);

            // now add new item to selection if applicable
            let stateWithItemSelection: State;
            if (newItemToAdd.getIsExtractCriterion()) {
                if (fileItemToReplace) {
                    let oldItemUnselectedState: State = removeFromExtractItems(stateWithNewFileItem, fileItemToReplace);
                    stateWithItemSelection = addToExtractItems(oldItemUnselectedState, newItemToAdd);
                } else {
                    stateWithItemSelection = addToExtractItems(stateWithNewFileItem, newItemToAdd);
                }
            }

            returnVal = stateWithItemSelection;

            break;
        }

        case gobiiFileItemAction.REMOVE_FROM_EXTRACT: {

            let gobiiFileItemPayload: GobiiFileItem = action.payload;
            returnVal = removeFromExtractItems(state, gobiiFileItemPayload);

            break;
        }


        case gobiiFileItemAction.REMOVE_FROM_EXTRACT_BY_ITEM_ID: {

            const fileItemUniqueIdPayload: string = action.payload;

            let gobiiFileItemPayload: GobiiFileItem = state
                .allFileItems
                .find(fi => fi.getFileItemUniqueId() === fileItemUniqueIdPayload);

            returnVal = removeFromExtractItems(state, gobiiFileItemPayload);

            break;

        }

        case gobiiFileItemAction.REMOVE_ALL_FROM_EXTRACT: {

            // only those not of the same extract filter type should remain selected

            let newFIleItemState = state.allFileItems.slice();

            let itemsToDeselect: GobiiFileItem[] =
                newFIleItemState
                    .filter(fi => fi.getGobiiExtractFilterType() === action.payload);

            itemsToDeselect.forEach(fi => fi.setSelected(false));

            let newSelectedItems: string[] = state.uniqueIdsOfExtractFileItems
                .filter(id => !itemsToDeselect
                    .find(fi => fi.getFileItemUniqueId() === id));


            // remove emphemeral items from state
            newFIleItemState = newFIleItemState
                .filter(fi => !fi.getIsEphemeral());

            // remove filters
            let newFilterState = Object.assign({}, state.filters);
            newFilterState = new Map<string, PayloadFilter>();

            returnVal = {
                gobiiExtractFilterType: state.gobiiExtractFilterType,
                allFileItems: newFIleItemState,
                uniqueIdsOfExtractFileItems: newSelectedItems,
                filters: newFilterState
            };

            break;
        }

        case gobiiFileItemAction.SET_EXTRACT_TYPE : {

            const gobiiExtractFilterType = action.payload.gobiiExtractFilterType;

            returnVal = {
                gobiiExtractFilterType: gobiiExtractFilterType,
                allFileItems: state.allFileItems,
                uniqueIdsOfExtractFileItems: state.uniqueIdsOfExtractFileItems,
                filters: state.filters
            };

            break;

        }

    }

    return returnVal;

}

/**
 * Because the data structure is defined within the fileItemsReducer it is optimal to
 * locate our selector functions at this level. If store is to be thought of
 * as a database, and reducers the tables, selectors can be considered the
 * queries into said database. Remember to keep your selectors small and
 * focused so they can be combined and composed to fit each particular
 * use-case.
 *
 * WARNING: as of 4.0.2, ngrx/platform  issue # 208: https://github.com/ngrx/platform/issues/208
 * has _not_ been resulved. So selectors _must_ return non-primtive objects. See my comment
 * in export-format-component.ts
 */

export const getGobiiExtractFilterType = (state: State) => state.gobiiExtractFilterType;

export const getFileItems = (state: State) => state.allFileItems.filter(fi =>
    fi.getGobiiExtractFilterType() === state.gobiiExtractFilterType
);

export const getUniqueIds = (state: State) => state.allFileItems.map(fileItem => fileItem.getFileItemUniqueId());

export const getSelectedUniqueIds = (state: State) => state.uniqueIdsOfExtractFileItems;

export const getFilters = (state: State) => state.filters;

export const getSelected = createSelector(getFileItems, getSelectedUniqueIds, (fileItems, selectedUniqueIds) => {

    let returnVal: GobiiFileItem[] = fileItems
        .filter(fileItem =>
            selectedUniqueIds
                .find(uniqueId => fileItem.getFileItemUniqueId() === uniqueId)
        );


    let foo: string = "foo";

    return returnVal;
});

export const getAll = createSelector(getFileItems, getUniqueIds, (entities, ids) => {
    return entities;
});

// ideally, the redux way of selecting data would be for the selection to be based
// on which entity is "selected" The problem is that we have controlls in which the
// entity type is parameterized -- it is not global state


/// ****************** SYNCHRONOUS METHODS


/// **************** GET SELECTED PER ENTITY TYPE


export const getSelectedFileFormat = createSelector(getFileItems, getSelectedUniqueIds, (fileItems, selectedUniqueIds) => {

    // default
    let returnVal = GobiiFileItem
        .build(this.gobiiExtractFilterType, ProcessType.UPDATE)
        .setExtractorItemType(ExtractorItemType.EXPORT_FORMAT)
        .setItemId(GobiiExtractFormat[GobiiExtractFormat.HAPMAP])
        .setItemName(GobiiExtractFormat[GobiiExtractFormat[GobiiExtractFormat.HAPMAP]]);

    let formatItem: GobiiFileItem = fileItems
        .find(fi => fi.getExtractorItemType() === ExtractorItemType.EXPORT_FORMAT
            && undefined !== selectedUniqueIds.find(id => id === fi.getFileItemUniqueId()));

    if (formatItem) {
        returnVal = formatItem;
    }

    return returnVal;

});

export const getSelectedSampleType = createSelector(getFileItems, getSelectedUniqueIds, (fileItems, selectedUniqueIds) => {

    // default
    let returnVal = GobiiFileItem.build(this.gobiiExtractFilterType, ProcessType.CREATE)
        .setExtractorItemType(ExtractorItemType.SAMPLE_LIST_TYPE)
        .setItemName(GobiiSampleListType[GobiiSampleListType.GERMPLASM_NAME])
        .setItemId(GobiiSampleListType[GobiiSampleListType.GERMPLASM_NAME]);

    let formatItem: GobiiFileItem = fileItems
        .find(fi => fi.getExtractorItemType() === ExtractorItemType.SAMPLE_LIST_TYPE
            && undefined !== selectedUniqueIds.find(id => id === fi.getFileItemUniqueId()));

    if (formatItem) {
        returnVal = formatItem;
    }

    return returnVal;

});

export const getJobId = createSelector(getFileItems, getSelectedUniqueIds, (fileItems, selectedUniqueIds) => {

    // default
    let returnVal = GobiiFileItem
        .build(this.gobiiExtractFilterType, ProcessType.UPDATE)
        .setExtractorItemType(ExtractorItemType.JOB_ID)
        .setItemId("UNSET")
        .setItemName("UNSET");

    let jobIdItem: GobiiFileItem = fileItems
        .find(fi => (fi.getExtractorItemType() === ExtractorItemType.JOB_ID)
            && (selectedUniqueIds.indexOf(fi.getFileItemUniqueId()) > -1));

    if (jobIdItem) {
        returnVal = jobIdItem;
    }

    return returnVal;

});


export const getUploadFiles = createSelector(getFileItems, getSelectedUniqueIds, (fileItems, selectedUniqueIds) => {

    // default
    let returnVal: GobiiFileItem[] = fileItems
        .filter(fi => fi.getExtractorItemType() === ExtractorItemType.MARKER_INPUT_FILE
            || fi.getExtractorItemType() === ExtractorItemType.SAMPLE_INPUT_FILE);


    return returnVal;

});


export const getPiContacts = createSelector(getFileItems, getUniqueIds, (fileItems, ids) => {

    let returnVal: GobiiFileItem[] = fileItems.filter(e =>
        (e.getExtractorItemType() === ExtractorItemType.ENTITY
            || e.getNameIdLabelType() !== NameIdLabelType.UNKNOWN)
        && e.getEntityType() === EntityType.CONTACT
        && e.getEntitySubType() === EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR)
        .map(fi => fi);

    return returnVal;
});


export const getProjects = createSelector(getFileItems, getUniqueIds, (fileItems, ids) => {

    let returnVal: GobiiFileItem[] = [];

    returnVal = fileItems.filter(e =>
        (e.getExtractorItemType() === ExtractorItemType.ENTITY
            || e.getNameIdLabelType() !== NameIdLabelType.UNKNOWN)
        && e.getEntityType() === EntityType.PROJECT
        && e.getProcessType() !== ProcessType.DUMMY)
        .map(fi => fi);

    if (returnVal.length <= 0) {
        returnVal = fileItems.filter(e =>
            (e.getExtractorItemType() === ExtractorItemType.ENTITY)
            && (e.getEntityType() === EntityType.PROJECT)
            && e.getProcessType() === ProcessType.DUMMY)
            .map(fi => fi);
    }

    return returnVal;
});


export const getExperiments = createSelector(getFileItems, getUniqueIds, (fileItems, ids) => {

    return fileItems.filter(e =>
        (e.getExtractorItemType() === ExtractorItemType.ENTITY
            || e.getNameIdLabelType() !== NameIdLabelType.UNKNOWN)
        && e.getEntityType() === EntityType.EXPERIMENT)
        .map(fi => fi);
});


export const getDatasets = createSelector(getFileItems, getUniqueIds, (fileItems, ids) => {

    return fileItems.filter(e =>
        (e.getExtractorItemType() === ExtractorItemType.ENTITY
            || e.getNameIdLabelType() !== NameIdLabelType.UNKNOWN)
        && e.getEntityType() === EntityType.DATASET)
        .map(fi => fi);
});


export const getCvTermsDataType = createSelector(getFileItems, getUniqueIds, (fileItems, ids) => {

    let returnVal: GobiiFileItem[] = fileItems.filter(e =>
        (e.getExtractorItemType() === ExtractorItemType.ENTITY
            || e.getNameIdLabelType() !== NameIdLabelType.UNKNOWN)
        && e.getEntityType() === EntityType.CV
        && e.getCvGroup() === CvGroup.DATASET_TYPE)
        .map(fi => fi);

    return returnVal;
});


export const getCvTermsJobStatus = createSelector(getFileItems, getUniqueIds, (fileItems, ids) => {

    let returnVal: GobiiFileItem[] = fileItems.filter(e =>
        (e.getExtractorItemType() === ExtractorItemType.ENTITY
            || e.getNameIdLabelType() !== NameIdLabelType.UNKNOWN)
        && e.getEntityType() === EntityType.CV
        && e.getCvGroup() === CvGroup.JOBSTATUS)
        .map(fi => fi);

    return returnVal;
});


export const getMapsets = createSelector(getFileItems, getUniqueIds, (fileItems, ids) => {

    return fileItems.filter(e =>
        (e.getExtractorItemType() === ExtractorItemType.ENTITY
            || e.getNameIdLabelType() !== NameIdLabelType.UNKNOWN)
        && e.getEntityType() === EntityType.MAPSET)
        .map(fi => fi);
});


export const getPlatforms = createSelector(getFileItems, getUniqueIds, (fileItems, ids) => {

    return fileItems.filter(e =>
        (e.getExtractorItemType() === ExtractorItemType.ENTITY
            || e.getNameIdLabelType() !== NameIdLabelType.UNKNOWN)
        && e.getEntityType() === EntityType.PLATFORM)
        .map(fi => fi);
});


export const getMarkerGroups = createSelector(getFileItems, getUniqueIds, (fileItems, ids) => {

    return fileItems.filter(e =>
        (e.getExtractorItemType() === ExtractorItemType.ENTITY
            || e.getNameIdLabelType() !== NameIdLabelType.UNKNOWN)
        && e.getEntityType() === EntityType.MARKER_GROUP)
        .map(fi => fi);
});


// **************** GET SELECTED PER ENTITY TYPE
export const getSelectedPiContacts = createSelector(getFileItems, getUniqueIds, (fileItems, ids) => {

    return fileItems.filter(e =>
        ids.find(id => id === e.getFileItemUniqueId())
        && e.getExtractorItemType() === ExtractorItemType.ENTITY
        && e.getEntityType() === EntityType.CONTACT
        && e.getEntitySubType() === EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR)
        .map(fi => fi);
});


// here
export const getProjectsForSelectedPi = createSelector(getFileItems, getFilters, getGobiiExtractFilterType, (fileItems, filters, gobiiExtractFilterType) => {

    let returnVal: GobiiFileItem[] = [];

    let contactId: string = null;
    if (filters[FilterParamNames.PROJECTS_BY_CONTACT]) {
        contactId = filters[FilterParamNames.PROJECTS_BY_CONTACT].relatedEntityFilterValue;
    }

    let projectId

    returnVal = fileItems.filter(
        e =>
            (e.getGobiiExtractFilterType() === gobiiExtractFilterType
                && e.getExtractorItemType() === ExtractorItemType.ENTITY
                || e.getNameIdLabelType() !== NameIdLabelType.UNKNOWN)
            && e.getProcessType() !== ProcessType.DUMMY
            && e.getEntityType() === EntityType.PROJECT
            && ((!contactId || (+contactId < 0)) // state is not filtered -- we don't care, or . . .
            || +e.getItemId() === 0 // Inlcude label "All Projects"
            || (e.getRelatedEntityFilterValue(filters[FilterParamNames.CONTACT_PI_HIERARCHY_ROOT].targetEntityUniqueId) // the item has an fk value
                && e.getRelatedEntityFilterValue(filters[FilterParamNames.CONTACT_PI_HIERARCHY_ROOT].targetEntityUniqueId) === contactId
            )) // and it matches
    ).map(fi => fi);


    if (returnVal.length <= 0) {
        returnVal = fileItems.filter(e =>
            (e.getGobiiExtractFilterType() == gobiiExtractFilterType
                && e.getExtractorItemType() === ExtractorItemType.ENTITY
                && e.getEntityType() === EntityType.PROJECT
                && e.getProcessType() === ProcessType.DUMMY))
            .map(fi => fi);
    }

    return returnVal;


    /*
    let returnVal: GobiiFileItem[] = [];

    if (filters[FilterParamNames.PROJECTS_BY_CONTACT]) {

        let contactId: string = filters[FilterParamNames.PROJECTS_BY_CONTACT].relatedEntityFilterValue;
        returnVal = fileItems.filter(e =>
            (e.getExtractorItemType() === ExtractorItemType.ENTITY)
            && (e.getEntityType() === EntityType.PROJECT)
            && (e.getParentItemId() === contactId)
            && e.getProcessType() !== ProcessType.DUMMY)
            .map(fi => fi);

        if (returnVal.length <= 0) {
            returnVal = fileItems.filter(e =>
                (e.getExtractorItemType() === ExtractorItemType.ENTITY)
                && (e.getEntityType() === EntityType.PROJECT)
                && e.getProcessType() === ProcessType.DUMMY)
                .map(fi => fi);
        }
    }

    return returnVal;
    */
});


export const getExperimentsForSelectedProject = createSelector(getFileItems, getFilters, (fileItems, filters) => {

    let returnVal: GobiiFileItem[] = [];

    if (filters[FilterParamNames.EXPERIMENTS_BY_PROJECT]) {

        let projectId: string = filters[FilterParamNames.EXPERIMENTS_BY_PROJECT].relatedEntityFilterValue;
        returnVal = fileItems.filter(e =>
            (e.getExtractorItemType() === ExtractorItemType.ENTITY)
            && (e.getEntityType() === EntityType.EXPERIMENT)
            && (e.getParentItemId() === projectId)
            && e.getProcessType() !== ProcessType.DUMMY)
            .map(fi => fi);

        if (returnVal.length <= 0) {
            returnVal = fileItems.filter(e =>
                (e.getExtractorItemType() === ExtractorItemType.ENTITY)
                && (e.getEntityType() === EntityType.EXPERIMENT)
                //                && (e.getParentItemId() === projectId )
                && e.getProcessType() === ProcessType.DUMMY)
                .map(fi => fi);
        }
    }

    return returnVal;
});


export const getDatasetsForSelectedExperiment = createSelector(getFileItems, getFilters, (fileItems, filters) => {

    let returnVal: GobiiFileItem[] = [];

    if (filters[FilterParamNames.DATASETS_BY_EXPERIMENT]) {

        let experimentId: string = filters[FilterParamNames.DATASETS_BY_EXPERIMENT].relatedEntityFilterValue;
        returnVal = fileItems.filter(e =>
            (e.getExtractorItemType() === ExtractorItemType.ENTITY
                && e.getEntityType() === EntityType.DATASET
                && e.getParentItemId() === experimentId
                && e.getProcessType() !== ProcessType.DUMMY))
            .map(fi => fi);

        if (returnVal.length <= 0) {
            returnVal = fileItems.filter(e =>
                (e.getExtractorItemType() === ExtractorItemType.ENTITY
                    && e.getEntityType() === EntityType.DATASET
                    //                    && e.getParentItemId() === experimentId
                    && e.getProcessType() === ProcessType.DUMMY))
                .map(fi => fi);
        }
        ``

    }

    return returnVal;
});

export const getDatasetEntities = createSelector(getFileItems, getFilters, (fileItems, filters) => {

    let returnVal: GobiiFileItem[] = [];


    // the child filter has the parent fk value
    let contactId = filters[FilterParamNames.CONTACT_PI_FILTER_OPTIONAL] ?
        filters[FilterParamNames.CONTACT_PI_FILTER_OPTIONAL].targetEntityFilterValue : null;
    let compounUniqueIdForContacts: GobiiFileItemCompoundId =
        filters[FilterParamNames.CONTACT_PI_FILTER_OPTIONAL] ? filters[FilterParamNames.CONTACT_PI_FILTER_OPTIONAL].targetEntityUniqueId : null;

    let projectId = filters[FilterParamNames.PROJECT_FILTER_OPTIONAL] ?
        filters[FilterParamNames.PROJECT_FILTER_OPTIONAL].targetEntityFilterValue : null;
    let compounUniqueIdForProjectsByContact: GobiiFileItemCompoundId =
        filters[FilterParamNames.PROJECT_FILTER_OPTIONAL] ? filters[FilterParamNames.PROJECT_FILTER_OPTIONAL].targetEntityUniqueId : null;

    let experimentId = filters[FilterParamNames.EXPERIMENT_FILTER_OPTIONAL] ?
        filters[FilterParamNames.EXPERIMENT_FILTER_OPTIONAL].targetEntityFilterValue : null;
    let compounUniqueIdForExperimentsByProject: GobiiFileItemCompoundId =
        filters[FilterParamNames.EXPERIMENT_FILTER_OPTIONAL] ? filters[FilterParamNames.EXPERIMENT_FILTER_OPTIONAL].targetEntityUniqueId : null;

    let datasetEntitiesFilteredByExperiment: GobiiFileItem[] = [];

    datasetEntitiesFilteredByExperiment = fileItems.filter(e =>
        (e.getExtractorItemType() === ExtractorItemType.ENTITY
            && e.getEntityType() === EntityType.DATASET
            && ((contactId === null) || +contactId < 1 || compounUniqueIdForContacts === null || e.getRelatedEntityFilterValue(compounUniqueIdForContacts) === contactId)
            && ((projectId === null) || +projectId < 1 || compounUniqueIdForProjectsByContact === null || e.getRelatedEntityFilterValue(compounUniqueIdForProjectsByContact) === projectId)
            && ((experimentId === null) || +experimentId < 1 || compounUniqueIdForExperimentsByProject === null || e.getRelatedEntityFilterValue(compounUniqueIdForExperimentsByProject) === experimentId)
            && e.hasEntity()
            && e.getProcessType() !== ProcessType.DUMMY))
        .map(fi => fi);


    let jobStatusFilterParams = filters[FilterParamNames.DATASET_LIST_STATUS];
    if (
        jobStatusFilterParams
        && jobStatusFilterParams.relatedEntityFilterValue != null) {

        returnVal = datasetEntitiesFilteredByExperiment
            .filter(fi => fi.getEntity().jobStatusName === jobStatusFilterParams.relatedEntityFilterValue
            );
    } else {
        returnVal = datasetEntitiesFilteredByExperiment;
    }

    return returnVal;
});


export const getDatasetEntitiesPaged = createSelector(getFileItems, getFilters, (fileItems, filters) => {

    let intermediateResult: GobiiFileItem[] = getDatasetEntities.resultFunc(fileItems, filters);
    let payloadFilter: PayloadFilter = filters[FilterParamNames.DATASET_LIST_PAGED];

    let returnVal: GobiiFileItem[] = intermediateResult.filter(gfi => gfi.getPageNumber() === payloadFilter.pagination.currentPage);

    return returnVal;

});


export const getPiContactsFilterOptional = createSelector(getFileItems, getGobiiExtractFilterType, (fileItems, gobiiExtractFilterType) => {

    return fileItems.filter(e =>
        (e.getGobiiExtractFilterType() === gobiiExtractFilterType
            && e.getExtractorItemType() === ExtractorItemType.ENTITY
            || e.getNameIdLabelType() !== NameIdLabelType.UNKNOWN)
        && e.getEntityType() === EntityType.CONTACT
        && e.getEntitySubType() === EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR)
        .map(fi => fi);
});

export const getProjectsFilterOptional = createSelector(getFileItems, getFilters, getGobiiExtractFilterType, (fileItems, filters, gobiiExtractFilterType) => {

    let returnVal: GobiiFileItem[] = [];

    // The project filter's value is a contactId. So we want only
    // those projects that have an fk reference to the specified contact.
    let contactId: string = null;
    if (filters[FilterParamNames.PROJECT_FILTER_OPTIONAL]) {
        contactId = filters[FilterParamNames.PROJECT_FILTER_OPTIONAL].relatedEntityFilterValue;
    }

    returnVal = fileItems.filter(
        e =>
            (e.getGobiiExtractFilterType() === gobiiExtractFilterType
                && e.getExtractorItemType() === ExtractorItemType.ENTITY
                || e.getNameIdLabelType() !== NameIdLabelType.UNKNOWN)
            && e.getProcessType() !== ProcessType.DUMMY
            && e.getEntityType() === EntityType.PROJECT
            && ((!contactId || (+contactId < 0)) // state is not filtered -- we don't care, or . . .
            || +e.getItemId() === 0 // Inlcude label "All Projects"
            || (e.getRelatedEntityFilterValue(filters[FilterParamNames.CONTACT_PI_FILTER_OPTIONAL].targetEntityUniqueId) // the item has an fk value
                && e.getRelatedEntityFilterValue(filters[FilterParamNames.CONTACT_PI_FILTER_OPTIONAL].targetEntityUniqueId) === contactId)) // and it matches
    ).map(fi => fi);


    if (returnVal.length <= 0) {
        returnVal = fileItems.filter(e =>
            (e.getGobiiExtractFilterType() == gobiiExtractFilterType
                && e.getExtractorItemType() === ExtractorItemType.ENTITY
                && e.getEntityType() === EntityType.PROJECT
                && e.getProcessType() === ProcessType.DUMMY))
            .map(fi => fi);
    }

    return returnVal;

});


export const getExperimentsFilterOptional = createSelector(getFileItems, getFilters, getGobiiExtractFilterType, (fileItems, filters, gobiiExtractFilterType) => {

    let returnVal: GobiiFileItem[] = [];

    let projectId: string = null;
    if (filters[FilterParamNames.PROJECT_FILTER_OPTIONAL]) {
        projectId = filters[FilterParamNames.PROJECT_FILTER_OPTIONAL].targetEntityFilterValue
    }

    let contactId: string = null;
    if (filters[FilterParamNames.CONTACT_PI_FILTER_OPTIONAL]) {
        contactId = filters[FilterParamNames.CONTACT_PI_FILTER_OPTIONAL].targetEntityFilterValue;
    }


    let projectIds: string [] = [];
    if ((projectId && +projectId > 0)) {

        projectIds.push(projectId);

    } else if (contactId && +contactId) {
        projectIds = fileItems
            .filter(fi => fi.compoundIdeEquals(filters[FilterParamNames.PROJECT_FILTER_OPTIONAL].targetEntityUniqueId)
                && fi.getRelatedEntityFilterValue(filters[FilterParamNames.CONTACT_PI_FILTER_OPTIONAL].targetEntityUniqueId) === contactId)
            .map(fi => fi.getItemId());
    }

    returnVal = fileItems.filter(
        e =>
            (e.getGobiiExtractFilterType() == gobiiExtractFilterType
                && e.getExtractorItemType() === ExtractorItemType.ENTITY
                || e.getNameIdLabelType() !== NameIdLabelType.UNKNOWN)
            && e.getProcessType() !== ProcessType.DUMMY
            && e.getEntityType() === EntityType.EXPERIMENT
            && ((!projectId && !contactId) // state is not filtered -- we don't care, or . . .
                || +e.getItemId() === 0 // Inlcude label "All Projects"
                || (e.getRelatedEntityFilterValue(filters[FilterParamNames.PROJECT_FILTER_OPTIONAL].targetEntityUniqueId) // the item has an fk value
                && projectIds.find(pid => e.getRelatedEntityFilterValue(filters[FilterParamNames.PROJECT_FILTER_OPTIONAL].targetEntityUniqueId) === pid))
            ) // and it matches
    ).map(fi => fi);

    if (returnVal.length <= 0) {
        returnVal = fileItems.filter(e =>
            (e.getExtractorItemType() === ExtractorItemType.ENTITY
                && e.getEntityType() === EntityType.EXPERIMENT
                && e.getProcessType() === ProcessType.DUMMY))
            .map(fi => fi);
    }

    return returnVal;
});

function compareVertices(gfi_a: GobiiFileItem, gfi_b: GobiiFileItem): number {

    let returnVal: number;
    if (gfi_a.getNameIdLabelType() != NameIdLabelType.UNKNOWN) {
        returnVal = -1; // force label item to sort to the top of the list
    } else {
        returnVal = gfi_a.getItemName().localeCompare(gfi_b.getItemName());
    }
    return (returnVal);

}

export const getFqF1Vertices = createSelector(getFileItems, getFilters, getGobiiExtractFilterType, (fileItems, filters, gobiiExtractFilterType) => {

    let returnVal: GobiiFileItem[] = [];

    returnVal = fileItems.filter(
        e =>
            (e.getGobiiExtractFilterType() == gobiiExtractFilterType
                && e.getExtractorItemType() === ExtractorItemType.VERTEX)
            && e.getProcessType() !== ProcessType.DUMMY
            && ( // For now F1 allows only for these vertices, which provide a path to samples
                // without schema changes
                e.getNameIdLabelType() === NameIdLabelType.SELECT_A
                || e.getEntity().vertexNameType === VertexNameType.VERTEX_TYPE_PRINCIPLE_INVESTIGATOR
                || e.getEntity().vertexNameType === VertexNameType.VERTEX_TYPE_PROJECT
                || e.getEntity().vertexNameType === VertexNameType.VERTEX_TYPE_EXPERIMENT
                || e.getEntity().vertexNameType === VertexNameType.VERTEX_TYPE_DATASET
                || e.getEntity().vertexNameType === VertexNameType.VERTEX_TYPE_DATASET_TYPE
                || e.getEntity().vertexNameType === VertexNameType.VERTEX_TYPE_SAMPLING_DATE
                || e.getEntity().vertexNameType === VertexNameType.VERTEX_TYPE_GENOTYPING_PURPOSE
                || e.getEntity().vertexNameType === VertexNameType.VERTEX_TYPE_DIVISION
                || e.getEntity().vertexNameType === VertexNameType.VERTEX_TYPE_ANALYSIS
                || e.getEntity().vertexNameType === VertexNameType.VERTEX_TYPE_ANALYSIS_TYPE
                || e.getEntity().vertexNameType === VertexNameType.VERTEX_TYPE_PLATFORM
                || e.getEntity().vertexNameType === VertexNameType.VERTEX_TYPE_VENDOR
                || e.getEntity().vertexNameType === VertexNameType.VERTEX_TYPE_VENDOR_PROTOCOL
                || e.getEntity().vertexNameType === VertexNameType.VERTEX_TYPE_PROTOCOL
            )
    ).map(fi => fi)
        .sort((gfi_a, gfi_b) => {
            return compareVertices(gfi_a, gfi_b)
        });

    return returnVal;
});


export const getFqF2Vertices = createSelector(getFileItems, getFilters, getGobiiExtractFilterType, (fileItems, filters, gobiiExtractFilterType) => {

    let returnVal: GobiiFileItem[] = [];

    let f1VertexId: string = null;
    if (filters[FilterParamNames.FQ_F1_VERTICES]) {
        f1VertexId = filters[FilterParamNames.FQ_F1_VERTICES].targetEntityFilterValue;
    }


    if (f1VertexId) {

        returnVal = fileItems.filter(
            e =>
                (e.getGobiiExtractFilterType() == gobiiExtractFilterType
                    && e.getExtractorItemType() === ExtractorItemType.VERTEX
                    && (e.getEntity().vertexId != f1VertexId
                        || e.getNameIdLabelType() === NameIdLabelType.SELECT_A))
                && e.getProcessType() !== ProcessType.DUMMY
        ).map(fi => fi)
            .sort((gfi_a, gfi_b) => {
                return compareVertices(gfi_a, gfi_b)
            });

    } else {
        returnVal = fileItems.filter(
            e =>
                (e.getGobiiExtractFilterType() == gobiiExtractFilterType
                    && e.getExtractorItemType() === ExtractorItemType.VERTEX
                    && e.getNameIdLabelType() !== NameIdLabelType.UNKNOWN)
                && e.getProcessType() !== ProcessType.DUMMY
        ).map(fi => fi)
            .sort((gfi_a, gfi_b) => {
                return compareVertices(gfi_a, gfi_b)
            });

    }

    return returnVal;
});


export const getFqF3Vertices = createSelector(getFileItems, getFilters, getGobiiExtractFilterType, (fileItems, filters, gobiiExtractFilterType) => {

    let returnVal: GobiiFileItem[] = [];

    let f2VertexId: string = null;
    if (filters[FilterParamNames.FQ_F2_VERTICES]) {
        f2VertexId = filters[FilterParamNames.FQ_F2_VERTICES].targetEntityFilterValue;
    }

    let f1VertexId: string = null;
    if (filters[FilterParamNames.FQ_F1_VERTICES]) {
        f1VertexId = filters[FilterParamNames.FQ_F1_VERTICES].targetEntityFilterValue;
    }

    if (f1VertexId && f2VertexId) {

        returnVal = fileItems.filter(
            e =>
                (e.getGobiiExtractFilterType() == gobiiExtractFilterType
                    && e.getEntity().vertexId != f2VertexId
                    && e.getEntity().vertexId != f1VertexId
                    && e.getExtractorItemType() === ExtractorItemType.VERTEX
                    || e.getNameIdLabelType() !== NameIdLabelType.UNKNOWN)
                && e.getProcessType() !== ProcessType.DUMMY
        ).map(fi => fi)
            .sort((gfi_a, gfi_b) => {
                return compareVertices(gfi_a, gfi_b)
            });


    } else {
        returnVal = fileItems.filter(
            e =>
                (e.getGobiiExtractFilterType() == gobiiExtractFilterType
                    && e.getExtractorItemType() === ExtractorItemType.VERTEX
                    && e.getNameIdLabelType() !== NameIdLabelType.UNKNOWN)
                && e.getProcessType() !== ProcessType.DUMMY
        ).map(fi => fi)
            .sort((gfi_a, gfi_b) => {
                return compareVertices(gfi_a, gfi_b)
            });
    }

    return returnVal;
});

export const getFqF4Vertices = createSelector(getFileItems, getFilters, getGobiiExtractFilterType, (fileItems, filters, gobiiExtractFilterType) => {

    let returnVal: GobiiFileItem[] = [];

    let f3VertexId: string = null;
    if (filters[FilterParamNames.FQ_F3_VERTICES]) {
        f3VertexId = filters[FilterParamNames.FQ_F3_VERTICES].targetEntityFilterValue;
    }

    let f2VertexId: string = null;
    if (filters[FilterParamNames.FQ_F2_VERTICES]) {
        f2VertexId = filters[FilterParamNames.FQ_F2_VERTICES].targetEntityFilterValue;
    }

    let f1VertexId: string = null;
    if (filters[FilterParamNames.FQ_F1_VERTICES]) {
        f1VertexId = filters[FilterParamNames.FQ_F1_VERTICES].targetEntityFilterValue;
    }


    if (f1VertexId && f2VertexId && f3VertexId) {

        returnVal = fileItems.filter(
            e =>
                (e.getGobiiExtractFilterType() == gobiiExtractFilterType
                    && e.getEntity().vertexId != f3VertexId
                    && e.getEntity().vertexId != f2VertexId
                    && e.getEntity().vertexId != f1VertexId
                    && e.getExtractorItemType() === ExtractorItemType.VERTEX
                    || e.getNameIdLabelType() !== NameIdLabelType.UNKNOWN)
                && e.getProcessType() !== ProcessType.DUMMY
        ).map(fi => fi)
            .sort((gfi_a, gfi_b) => {
                return compareVertices(gfi_a, gfi_b)
            });

    } else {
        returnVal = fileItems.filter(
            e =>
                (e.getGobiiExtractFilterType() == gobiiExtractFilterType
                    && e.getExtractorItemType() === ExtractorItemType.VERTEX
                    && e.getNameIdLabelType() !== NameIdLabelType.UNKNOWN)
                && e.getProcessType() !== ProcessType.DUMMY
        ).map(fi => fi)
            .sort((gfi_a, gfi_b) => {
                return compareVertices(gfi_a, gfi_b)
            });
    }

    return returnVal;
});

export const getFqF1VerticesValues = createSelector(getFileItems, getFilters, getGobiiExtractFilterType, (fileItems, filters, gobiiExtractFilterType) => {

    let returnVal: GobiiFileItem[] = [];

    let filterCompoundUniqueId: GobiiFileItemCompoundId;
    if (filters[FilterParamNames.FQ_F1_VERTEX_VALUES]) {
        filterCompoundUniqueId = filters[FilterParamNames.FQ_F1_VERTEX_VALUES].targetEntityUniqueId;
    }

    if (filterCompoundUniqueId &&
        (filterCompoundUniqueId.getEntityType() != EntityType.UNKNOWN)) {

        returnVal = fileItems.filter(
            e => // you can't use compoundIdeEquals() because the extractor item type won't match the vertex filter
                (e.getGobiiExtractFilterType() == GobiiExtractFilterType.FLEX_QUERY
                    && e.getExtractorItemType() === ExtractorItemType.VERTEX_VALUE
                    && e.getEntityType() === filterCompoundUniqueId.getEntityType()
                    && e.getEntitySubType() === filterCompoundUniqueId.getEntitySubType()
                    && e.getCvGroup() === filterCompoundUniqueId.getCvGroup()
                    && e.getCvTerm() === filterCompoundUniqueId.getCvTerm()
                )
        ).map(fi => fi)
            .sort((gfi_a, gfi_b) => {
                return gfi_a.getItemName().localeCompare(gfi_b.getItemName())
            });
    }

    return returnVal;
});

export const getFqF2VerticesValues = createSelector(getFileItems, getFilters, getGobiiExtractFilterType, (fileItems, filters, gobiiExtractFilterType) => {

    let returnVal: GobiiFileItem[] = [];

    let filterCompoundUniqueId: GobiiFileItemCompoundId;
    if (filters[FilterParamNames.FQ_F2_VERTEX_VALUES]) {
        filterCompoundUniqueId = filters[FilterParamNames.FQ_F2_VERTEX_VALUES].targetEntityUniqueId;
    }


    if (filterCompoundUniqueId &&
        (filterCompoundUniqueId.getEntityType() != EntityType.UNKNOWN)) {

        returnVal = fileItems.filter(
            e =>
                (e.getGobiiExtractFilterType() == GobiiExtractFilterType.FLEX_QUERY
                    && e.getExtractorItemType() === ExtractorItemType.VERTEX_VALUE
                    && e.getEntityType() === filterCompoundUniqueId.getEntityType()
                    && e.getEntitySubType() === filterCompoundUniqueId.getEntitySubType()
                    && e.getCvGroup() === filterCompoundUniqueId.getCvGroup()
                    && e.getCvTerm() === filterCompoundUniqueId.getCvTerm()
                )
        ).map(fi => fi)
            .sort((gfi_a, gfi_b) => {
                return gfi_a.getItemName().localeCompare(gfi_b.getItemName());
            });
    }

    return returnVal;

});

export const getFqF3VerticesValues = createSelector(getFileItems, getFilters, getGobiiExtractFilterType, (fileItems, filters, gobiiExtractFilterType) => {

    let returnVal: GobiiFileItem[] = [];

    let filterCompoundUniqueId: GobiiFileItemCompoundId;
    if (filters[FilterParamNames.FQ_F3_VERTEX_VALUES]) {
        filterCompoundUniqueId = filters[FilterParamNames.FQ_F3_VERTEX_VALUES].targetEntityUniqueId;
    }


    if (filterCompoundUniqueId &&
        (filterCompoundUniqueId.getEntityType() != EntityType.UNKNOWN)) {

        returnVal = fileItems.filter(
            e =>
                (e.getGobiiExtractFilterType() == GobiiExtractFilterType.FLEX_QUERY
                    && e.getExtractorItemType() === ExtractorItemType.VERTEX_VALUE
                    && e.getEntityType() === filterCompoundUniqueId.getEntityType()
                    && e.getEntitySubType() === filterCompoundUniqueId.getEntitySubType()
                    && e.getCvGroup() === filterCompoundUniqueId.getCvGroup()
                    && e.getCvTerm() === filterCompoundUniqueId.getCvTerm()
                )
        ).map(fi => fi)
            .sort((gfi_a, gfi_b) => {
                return gfi_a.getItemName().localeCompare(gfi_b.getItemName());
            });
    }

    return returnVal;
});

export const getFqF4VerticesValues = createSelector(getFileItems, getFilters, getGobiiExtractFilterType, (fileItems, filters, gobiiExtractFilterType) => {

    let returnVal: GobiiFileItem[] = [];

    let filterCompoundUniqueId: GobiiFileItemCompoundId;
    if (filters[FilterParamNames.FQ_F4_VERTEX_VALUES]) {
        filterCompoundUniqueId = filters[FilterParamNames.FQ_F4_VERTEX_VALUES].targetEntityUniqueId;
    }


    if (filterCompoundUniqueId &&
        (filterCompoundUniqueId.getEntityType() != EntityType.UNKNOWN)) {

        returnVal = fileItems.filter(
            e =>
                (e.getGobiiExtractFilterType() == GobiiExtractFilterType.FLEX_QUERY
                    && e.getExtractorItemType() === ExtractorItemType.VERTEX_VALUE
                    && e.getEntityType() === filterCompoundUniqueId.getEntityType()
                    && e.getEntitySubType() === filterCompoundUniqueId.getEntitySubType()
                    && e.getCvGroup() === filterCompoundUniqueId.getCvGroup()
                    && e.getCvTerm() === filterCompoundUniqueId.getCvTerm()
                )
        ).map(fi => fi)
            .sort((gfi_a, gfi_b) => {
                return gfi_a.getItemName().localeCompare(gfi_b.getItemName());
            });
    }

    return returnVal;
});

export const getCurrentMarkerCount = createSelector(getFileItems, getFilters, getGobiiExtractFilterType, (fileItems, filters, gobiiExtractFilterType) => {

    let returnVal: number = 0;

    let countFileItems: GobiiFileItem[] = fileItems.filter(
        e =>
            (e.getGobiiExtractFilterType() == GobiiExtractFilterType.FLEX_QUERY
                && e.getExtractorItemType() === ExtractorItemType.ITEM_COUNT
                && e.getEntityType() === EntityType.MARKER)
    )

    if (countFileItems.length > 0) {
        returnVal = countFileItems[0].getEntity();
    }

    return returnVal;
});

export const getCurrentSampleCount = createSelector(getFileItems, getFilters, getGobiiExtractFilterType, (fileItems, filters, gobiiExtractFilterType) => {

    let returnVal: number = 0;

    let countFileItems: GobiiFileItem[] = fileItems.filter(
        e =>
            (e.getGobiiExtractFilterType() == GobiiExtractFilterType.FLEX_QUERY
                && e.getExtractorItemType() === ExtractorItemType.ITEM_COUNT
                && e.getEntityType() === EntityType.DNA_SAMPLE)
    )

    if (countFileItems.length > 0) {
        returnVal = countFileItems[0].getEntity();
    }

    return returnVal;
});

export const getFilterCountState = createSelector(getFileItems, getFilters, getGobiiExtractFilterType, (fileItems, filters, gobiiExtractFilterType) => {

    let returnVal: FilterCountState = new FilterCountState(new Map<string, PayloadFilter>(), -1, -1);

    Object.keys(filters).forEach(key => {

        if (key === FilterParamNames.FQ_F1_VERTEX_VALUES ||
            key === FilterParamNames.FQ_F2_VERTEX_VALUES ||
            key === FilterParamNames.FQ_F3_VERTEX_VALUES ||
            key === FilterParamNames.FQ_F4_VERTEX_VALUES ||
            key === FilterParamNames.FQ_F1_VERTICES ||
            key === FilterParamNames.FQ_F2_VERTICES ||
            key === FilterParamNames.FQ_F3_VERTICES ||
            key === FilterParamNames.FQ_F4_VERTICES) {

            returnVal.flexQueryFilters.set(key, filters[key]);
        }
    }); //forEach filter

    let dnaSampleCountFileItems: GobiiFileItem[] = fileItems.filter(
        e =>
            (e.getGobiiExtractFilterType() == GobiiExtractFilterType.FLEX_QUERY
                && e.getExtractorItemType() === ExtractorItemType.ITEM_COUNT
                && e.getEntityType() === EntityType.DNA_SAMPLE)
    )

    if (dnaSampleCountFileItems.length > 0) {
        returnVal.sampleCount = dnaSampleCountFileItems[0].getEntity();
    }

    let markerCountFileItems: GobiiFileItem[] = fileItems.filter(
        e =>
            (e.getGobiiExtractFilterType() == GobiiExtractFilterType.FLEX_QUERY
                && e.getExtractorItemType() === ExtractorItemType.ITEM_COUNT
                && e.getEntityType() === EntityType.MARKER)
    )

    if (markerCountFileItems.length > 0) {
        returnVal.markerCount = markerCountFileItems[0].getEntity();
    }

    return returnVal;
});
