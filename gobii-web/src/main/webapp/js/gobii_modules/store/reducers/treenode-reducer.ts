import {createSelector} from 'reselect';
import * as gobiiTreeNodeAction from "../actions/treenode-action";
import {ContainerType, GobiiTreeNode} from "../../model/gobii-tree-node";
import {GobiiExtractFilterType} from "../../model/type-extractor-filter";
import {GobiiFileItemCompoundId} from "../../model/gobii-file-item-compound-id";
import {EntityType} from "../../model/type-entity";


export interface State {
    gobiiExtractFilterType: GobiiExtractFilterType;
    gobiiTreeNodesActive: string[];
    gobiiTreeNodes: GobiiTreeNode[];
};

export const initialState: State = {
    gobiiExtractFilterType: GobiiExtractFilterType.UNKNOWN,
    gobiiTreeNodesActive: [],
    gobiiTreeNodes: [],
};


function placeNodeInTree(nodeToPlace: GobiiTreeNode, treeNodes: GobiiTreeNode[], gobiiExtractFilterType: GobiiExtractFilterType): boolean {

    let returnVal: boolean = false;

    for (let idx: number = 0; !returnVal && (idx < treeNodes.length); idx++) {

        let currentTreenode: GobiiTreeNode = treeNodes[idx];
        if (currentTreenode.getGobiiExtractFilterType() === gobiiExtractFilterType &&
            (currentTreenode.compoundIdeEquals(nodeToPlace)
                || currentTreenode.getChildCompoundUniqueId().compoundIdeEquals(nodeToPlace))
            && currentTreenode.getContainerType() !== ContainerType.STRUCTURE
        ) {
            if (currentTreenode.getContainerType() === ContainerType.NONE) {
                treeNodes[idx] = nodeToPlace;
            } else if (currentTreenode.getContainerType() === ContainerType.DATA) {
                let containerNode: GobiiTreeNode = treeNodes[idx];
                nodeToPlace.parent = containerNode;
                containerNode.getChildren().push(nodeToPlace);
                containerNode.expanded = true;
            }

            returnVal = true;

        } else {
            returnVal = placeNodeInTree(nodeToPlace, currentTreenode.getChildren(), gobiiExtractFilterType);
        }
    }

    return returnVal;
}

function findTreeNodeByFIleItemId(treeNodes: GobiiTreeNode[], fileItemUniqueId: string): GobiiTreeNode {

    let returnVal: GobiiTreeNode = null;

    //use an oldfashioned for loop because the lambda based semantics don't have a break;
    for (let idx: number = 0; (idx < treeNodes.length) && !returnVal; idx++) {

        let currentTreeNode: GobiiTreeNode = treeNodes[idx];
        if (currentTreeNode.getFileItemId() === fileItemUniqueId) {
            returnVal = currentTreeNode;
        } else {
            returnVal = findTreeNodeByFIleItemId(currentTreeNode.getChildren(), fileItemUniqueId);
        }
    }

    return returnVal;
}

function findTreeNodeByCompoundId(treeNodes: GobiiTreeNode[],
                                  gobiiExtractFilterType: GobiiExtractFilterType,
                                  gobiiFileItemCompoundId: GobiiFileItemCompoundId): GobiiTreeNode {

    let returnVal: GobiiTreeNode = null;


    for (let idx: number = 0; (idx < treeNodes.length) && !returnVal; idx++) {

        let currentTreeNode: GobiiTreeNode = treeNodes[idx];

        // in the condition, we are using the sequence number to distinguish the flex query
        // parent filter nodes from all other nodes. This is not a great way to express these
        // semantics. Ideally, there should be an explicit category property to indicate that the
        // node is this type of node. But for now this works reasonably well.
        if ((
                currentTreeNode.getSequenceNum() === 0
                && currentTreeNode.getGobiiExtractFilterType() === gobiiExtractFilterType
                && gobiiFileItemCompoundId.compoundIdeEquals(currentTreeNode))
            || (
                currentTreeNode.getSequenceNum() > 0 &&
                currentTreeNode.getSequenceNum() === gobiiFileItemCompoundId.getSequenceNum()
            )) {
            returnVal = currentTreeNode;
        } else {
            returnVal = findTreeNodeByCompoundId(currentTreeNode.getChildren(), gobiiExtractFilterType, gobiiFileItemCompoundId);
        }
    }

    return returnVal;
}

export function gobiiTreeNodesReducer(state: State = initialState, action: gobiiTreeNodeAction.All): State {

    let returnVal: State = state;

    switch (action.type) {

        case gobiiTreeNodeAction.INIT_TREE: {
            const gobiigobiiTreeItemsPayload = action.payload;

            returnVal = {
                gobiiExtractFilterType: state.gobiiExtractFilterType,
                gobiiTreeNodesActive: state.gobiiTreeNodesActive,
                gobiiTreeNodes: gobiigobiiTreeItemsPayload
            };

            break;
        } // INIT_TREE


        case gobiiTreeNodeAction.PLACE_TREE_NODE: {

            const gobiiTreeNodePayload: GobiiTreeNode = action.payload;

            // copy the existing
            const newTreeNodes = state.gobiiTreeNodes.slice();
            if (placeNodeInTree(gobiiTreeNodePayload, newTreeNodes, state.gobiiExtractFilterType)) {

                returnVal = {
                    gobiiExtractFilterType: state.gobiiExtractFilterType,
                    gobiiTreeNodesActive: state.gobiiTreeNodesActive,
                    gobiiTreeNodes: newTreeNodes
                };
            }

            break;
        } // INIT_TREE


        case gobiiTreeNodeAction.ACTIVATE: {

            const fileItemUniqueId: string = action.payload;


            returnVal = {
                gobiiExtractFilterType: state.gobiiExtractFilterType,
                gobiiTreeNodesActive: [...state.gobiiTreeNodesActive, ...[fileItemUniqueId]],
                gobiiTreeNodes: state.gobiiTreeNodes
            };

            break;

        } // ADD_TO_EXTRACT

        case gobiiTreeNodeAction.DEACTIVATE: {

            const gobiiFileItemUniqueId = action.payload;
            const newTreeNodes = state.gobiiTreeNodes.slice();

            const gobiiTreeNodeToDeActivate: GobiiTreeNode = findTreeNodeByFIleItemId(newTreeNodes
                    .filter(tn => tn.getGobiiExtractFilterType() === state.gobiiExtractFilterType),
                gobiiFileItemUniqueId);

            // not all file items are in the tree
            if (gobiiTreeNodeToDeActivate) {

                let containerType: ContainerType = gobiiTreeNodeToDeActivate.parent ?
                    gobiiTreeNodeToDeActivate.parent.getContainerType() :
                    ContainerType.NONE;

                if (containerType === ContainerType.NONE
                    || containerType === ContainerType.STRUCTURE) {
                    gobiiTreeNodeToDeActivate.resetLabel();
                } else {
                    let children: GobiiTreeNode[] = gobiiTreeNodeToDeActivate.parent.getChildren();
                    children.splice(children.indexOf(gobiiTreeNodeToDeActivate, 0), 1);
                }


                let newAcxtiveNodeState = state.gobiiTreeNodesActive
                    .filter(gtn => gtn !== gobiiTreeNodeToDeActivate.getId());

                returnVal = {
                    gobiiExtractFilterType: state.gobiiExtractFilterType,
                    gobiiTreeNodesActive: newAcxtiveNodeState,
                    gobiiTreeNodes: newTreeNodes
                };
            } else {
                returnVal = {
                    gobiiExtractFilterType: state.gobiiExtractFilterType,
                    gobiiTreeNodesActive: state.gobiiTreeNodesActive,
                    gobiiTreeNodes: state.gobiiTreeNodes
                };
            }

            break;

        } // ADD_TO_EXTRACT

        case gobiiTreeNodeAction.SELECT_EXTRACT_TYPE: {

            returnVal = {
                gobiiExtractFilterType: action.payload,
                gobiiTreeNodesActive: state.gobiiTreeNodesActive,
                gobiiTreeNodes: state.gobiiTreeNodes
            };

            break;

        } // SELECT_EXTRACT_TYPE

        case gobiiTreeNodeAction.SET_NODE_STATUS: {

            const gobiiExtractFilterType: GobiiExtractFilterType = action.payload.gobiiExtractFilterType;
            const gobiiFileItemCompoundId: GobiiFileItemCompoundId = action.payload.targetCompoundId;
            const newTreeNodesState = state.gobiiTreeNodes.slice();
            let treeNodeToMutate: GobiiTreeNode = findTreeNodeByCompoundId(newTreeNodesState,
                gobiiExtractFilterType,
                gobiiFileItemCompoundId);

            if (treeNodeToMutate) {

                const icon: string = action.payload.icons.icon;
                const expandedIcon: string = action.payload.icons.expandedIcon;
                const collapsedIcon: string = action.payload.icons.collapsedIcon;
                const label: string = action.payload.label;
                const entityType: EntityType = action.payload.entityType;


                treeNodeToMutate.icon = icon ? icon : treeNodeToMutate.icon;
                treeNodeToMutate.expandedIcon = expandedIcon ? expandedIcon : treeNodeToMutate.expandedIcon;
                treeNodeToMutate.collapsedIcon = collapsedIcon ? collapsedIcon : treeNodeToMutate.collapsedIcon;
                treeNodeToMutate.label = label ? label : treeNodeToMutate.label;
                treeNodeToMutate.genericLabel = treeNodeToMutate.label;
                //treeNodeToMutate.setEntityType(entityType ? entityType : treeNodeToMutate.getEntityType());

                if (action.payload.childCompoundId) {
                    treeNodeToMutate.setChildCompoundUniqueId(action.payload.childCompoundId)
                }

                returnVal = {
                    gobiiExtractFilterType: state.gobiiExtractFilterType,
                    gobiiTreeNodesActive: state.gobiiTreeNodesActive,
                    gobiiTreeNodes: newTreeNodesState
                };

            } else {

                returnVal = {
                    gobiiExtractFilterType: state.gobiiExtractFilterType,
                    gobiiTreeNodesActive: state.gobiiTreeNodesActive,
                    gobiiTreeNodes: state.gobiiTreeNodes
                };

            }

            break;

        }

        case gobiiTreeNodeAction.CLEAR_ALL: {

            let newSelectedNodes =
                state
                    .gobiiTreeNodesActive
                    .filter(id => state
                        .gobiiTreeNodes
                        .find(gtn => gtn.getFileItemId() === id
                            && gtn.getGobiiExtractFilterType() != action.payload));

            returnVal = {
                gobiiExtractFilterType: state.gobiiExtractFilterType,
                gobiiTreeNodesActive: newSelectedNodes,
                gobiiTreeNodes: state.gobiiTreeNodes
            };

            break;
        }
    }

    return returnVal;

}

export const getGobiiTreeNodes = (state: State) => state.gobiiTreeNodes;

export const getGobiiTreeItemIds = (state: State) => state.gobiiTreeNodes.map(gti => gti.getId());

export const getIdsOfActivated = (state: State) => state.gobiiTreeNodesActive;

export const getExtractFilterType = (state: State) => state.gobiiExtractFilterType;

export const getSelected = createSelector(getGobiiTreeNodes, getIdsOfActivated, getExtractFilterType, (gobiiTreeNodes, selectedUniqueIds, getExtractFilterType) => {

        let returnVal: GobiiTreeNode[] = [];

        gobiiTreeNodes
            .forEach(function find(gtn) {
                if (selectedUniqueIds.filter(id => id === gtn.getId()).length > 0) {
                    returnVal.push(gtn);
                }
                gtn.getChildren().forEach(find);
            });


        return returnVal;

    }
);


export const getAll = createSelector(getGobiiTreeNodes, getGobiiTreeItemIds, (treeItems, ids) => {
    return ids.map(id => treeItems[id]);
});

export const getForSelectedFilter = createSelector(getGobiiTreeNodes, getExtractFilterType, (treeItems, extractFilterType) => {
    return treeItems.filter(ti => ti.getGobiiExtractFilterType() === extractFilterType);
});