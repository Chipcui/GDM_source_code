import {Injectable} from "@angular/core";
import {ContainerType, GobiiTreeNode} from "../../model/GobiiTreeNode";
import {EntitySubType, EntityType} from "../../model/type-entity";
import {Labels} from "../../views/entity-labels";
import {ExtractorItemType} from "../../model/type-extractor-item";
import {GobiiExtractFilterType} from "../../model/type-extractor-filter";
import {CvFilterType} from "../../model/cv-filter-type";
import {GobiiFileItem} from "../../model/gobii-file-item";
import {GobiiExtractFormat} from "../../model/type-extract-format";
import {ProcessType} from "../../model/type-process";
import {GobiiFileItemCompoundId} from "../../model/gobii-file-item-compound-id";
import {TypeTreeNodeStatus} from "../../model/type-tree-node-status";
import * as treeNodeActions from '../../store/actions/treenode-action'
import {Store} from "@ngrx/store";
import * as fromRoot from '../../store/reducers';


@Injectable()
export class TreeStructureService {

    constructor(private store: Store<fromRoot.State>) {
    }


    private makeCommonNodes(gobiiExtractFilterType: GobiiExtractFilterType): GobiiTreeNode[] {

        let returnVal: GobiiTreeNode[] = [

            GobiiTreeNode.build(gobiiExtractFilterType, ExtractorItemType.JOB_ID)
                .setGenericLabel(Labels.instance().treeExtractorTypeLabels[ExtractorItemType.JOB_ID]),
            GobiiTreeNode.build(gobiiExtractFilterType, ExtractorItemType.ENTITY)
                .setEntityType(EntityType.CONTACT)
                .setEntitySubType(EntitySubType.CONTACT_SUBMITED_BY)
                .setGenericLabel(Labels.instance().entityNodeLabels[EntitySubType.CONTACT_SUBMITED_BY]),
            GobiiTreeNode.build(gobiiExtractFilterType, ExtractorItemType.EXPORT_FORMAT)
                .setGenericLabel(Labels.instance().treeExtractorTypeLabels[ExtractorItemType.EXPORT_FORMAT]),
            GobiiTreeNode.build(gobiiExtractFilterType, ExtractorItemType.ENTITY)
                .setEntityType(EntityType.MAPSET)
                .setGenericLabel(Labels.instance().entityNodeLabels[EntityType.MAPSET]),
        ]

        return returnVal;
    }


    // Note that we aren't storing these nodes as state -- we only make them
    // and return them. We rely on the store to make them part of state.
    // For now, this is the only context in which container type is ste on tree nodes;
    // tree nodes that are created dynamically will only ever replace or be stored within
    // one of these original structural nodes. See the tree nodes reducer for how the
    // container types are actually used
    public getInitialTree(): GobiiTreeNode[] {

        let returnVal: GobiiTreeNode[] = [

            // BY DATASET
            ...this.makeCommonNodes(GobiiExtractFilterType.WHOLE_DATASET),
            GobiiTreeNode.build(GobiiExtractFilterType.WHOLE_DATASET, ExtractorItemType.ENTITY)
                .setEntityType(EntityType.DATASET)
                .setContainerType(ContainerType.DATA)
            ,

            // BY SAMPLE
            ...this.makeCommonNodes(GobiiExtractFilterType.BY_SAMPLE),
            GobiiTreeNode.build(GobiiExtractFilterType.BY_SAMPLE, ExtractorItemType.ENTITY)
                .setEntityType(EntityType.CV)
                .setCvFilterType(CvFilterType.DATASET_TYPE),
            GobiiTreeNode.build(GobiiExtractFilterType.BY_SAMPLE, ExtractorItemType.SAMPLE_LIST_TYPE),
            GobiiTreeNode.build(GobiiExtractFilterType.BY_SAMPLE, ExtractorItemType.ENTITY)
                .setEntityType(EntityType.PLATFORM)
                .setContainerType(ContainerType.DATA),
            GobiiTreeNode.build(GobiiExtractFilterType.BY_SAMPLE, ExtractorItemType.TREE_STRUCTURE)
                .setContainerType(ContainerType.STRUCTURE)
                .setLabel("Samples Criteria")
                .setExpanded(true)
                .setChildren([
                    GobiiTreeNode.build(GobiiExtractFilterType.BY_SAMPLE, ExtractorItemType.ENTITY)
                        .setEntityType(EntityType.CONTACT)
                        .setEntitySubType(EntitySubType.CONTACT_PRINCIPAL_INVESTIGATOR)
                        .setContainerType(ContainerType.NONE),
                    GobiiTreeNode.build(GobiiExtractFilterType.BY_SAMPLE, ExtractorItemType.ENTITY)
                        .setEntityType(EntityType.PROJECT)
                        .setContainerType(ContainerType.NONE),
                    GobiiTreeNode.build(GobiiExtractFilterType.BY_SAMPLE, ExtractorItemType.SAMPLE_FILE),
                    GobiiTreeNode.build(GobiiExtractFilterType.BY_SAMPLE, ExtractorItemType.SAMPLE_LIST_ITEM)
                        .setContainerType(ContainerType.DATA),
                ]),


            // BY MARKER
            ...this.makeCommonNodes(GobiiExtractFilterType.BY_MARKER),
            GobiiTreeNode.build(GobiiExtractFilterType.BY_MARKER, ExtractorItemType.ENTITY)
                .setEntityType(EntityType.CV)
                .setCvFilterType(CvFilterType.DATASET_TYPE),
            GobiiTreeNode.build(GobiiExtractFilterType.BY_MARKER, ExtractorItemType.TREE_STRUCTURE)
                .setContainerType(ContainerType.STRUCTURE)
                .setLabel("Markers Criteria")
                .setExpanded(true)
                .setChildren([
                    GobiiTreeNode.build(GobiiExtractFilterType.BY_MARKER, ExtractorItemType.ENTITY)
                        .setEntityType(EntityType.PLATFORM)
                        .setContainerType(ContainerType.DATA),
                    GobiiTreeNode.build(GobiiExtractFilterType.BY_MARKER, ExtractorItemType.MARKER_FILE),
                    GobiiTreeNode.build(GobiiExtractFilterType.BY_MARKER, ExtractorItemType.MARKER_LIST_ITEM)
                        .setContainerType(ContainerType.DATA),
                    GobiiTreeNode.build(GobiiExtractFilterType.BY_MARKER, ExtractorItemType.ENTITY)
                        .setEntityType(EntityType.MARKER_GROUP)
                        .setContainerType(ContainerType.DATA)
                ])
        ];

        // we know we only have to go one level deep in this case -- no need to recurse
        returnVal.forEach(function (currentNode, idx, nodes) {

            currentNode.getChildren().forEach(currentChild => {
                currentChild.parent = currentNode
            })
        });

        this.setTreeNodeProperties(returnVal);
        return returnVal;

    }

    private applyLabel(gobiiTreeNode: GobiiTreeNode) {

        let labelValue: string = null;

        if (gobiiTreeNode.getItemType() === ExtractorItemType.ENTITY) {

            if (gobiiTreeNode.getEntitySubType() === EntitySubType.UNKNOWN) {

                if (gobiiTreeNode.getEntityType() !== EntityType.CV) {
                    labelValue = Labels.instance().entityNodeLabels[gobiiTreeNode.getEntityType()];
                } else {
                    labelValue = Labels.instance().cvFilterNodeLabels[gobiiTreeNode.getCvFilterType()];
                }
            } else {
                labelValue = Labels.instance().entitySubtypeNodeLabels[gobiiTreeNode.getEntitySubType()];
            }

        } else {
            labelValue = Labels.instance().treeExtractorTypeLabels[gobiiTreeNode.getItemType()];
        }

        gobiiTreeNode.setGenericLabel(labelValue);
        gobiiTreeNode.setLabel(labelValue);

    }

    private setTreeNodeProperties(treeNodes: GobiiTreeNode[]) {

        treeNodes.forEach(tn => {
            if (( tn.children === null ) || ( tn.children.length <= 0  )) {
                this.addIconsToNode(tn, false);
                this.applyLabel(tn);
            } else {
                this.setTreeNodeProperties(tn.children);
            }
        })

    }
    
    private getEntityIcon(gobiiFileItemCompoundId:GobiiFileItemCompoundId):{ icon: string, expandedIcon: string, collapsedIcon: string } {

        let icon: string;
        let expandedIcon: string;
        let collapsedIcon: string;
        
        if (gobiiFileItemCompoundId.getEntityType() === EntityType.DATASET) {

            icon = "fa-database";
            expandedIcon = "fa-folder-expanded";
            collapsedIcon = "fa-database";

        } else if (gobiiFileItemCompoundId.getEntityType() === EntityType.CONTACT) {

            icon = "fa-user-o";
            expandedIcon = "fa-user-o";
            collapsedIcon = "fa-user-o";

        } else if (gobiiFileItemCompoundId.getEntityType() === EntityType.MAPSET) {

            icon = "fa-map-o";
            expandedIcon = "fa-map-o";
            collapsedIcon = "fa-map-o";

        } else if (gobiiFileItemCompoundId.getEntityType() === EntityType.PLATFORM) {

            icon = "fa-calculator";
            expandedIcon = "fa-calculator";
            collapsedIcon = "fa-calculator";

        } else if (gobiiFileItemCompoundId.getEntityType() === EntityType.PROJECT) {

            icon = "fa-clipboard";
            expandedIcon = "fa-clipboard";
            collapsedIcon = "fa-clipboard";

        } else if (gobiiFileItemCompoundId.getEntityType() === EntityType.CV) {

            if (gobiiFileItemCompoundId.getCvFilterType() === CvFilterType.DATASET_TYPE) {
                icon = "fa-file-excel-o";
                expandedIcon = "fa-file-excel-o";
                collapsedIcon = "fa-file-excel-o";
            }

        } else if (gobiiFileItemCompoundId.getEntityType() === EntityType.MARKER_GROUP) {

            // if (isParent) {
            icon = "fa-pencil";
            expandedIcon = "fa-pencil";
            collapsedIcon = "fa-pencil";

        }

        return {icon: icon, expandedIcon: expandedIcon, collapsedIcon: collapsedIcon};
    }


    private addEntityIconToNode(entityType: EntityType, cvFilterType: CvFilterType, treeNode: GobiiTreeNode) {

        if (entityType === EntityType.DATASET) {

            treeNode.icon = "fa-database";
            treeNode.expandedIcon = "fa-folder-expanded";
            treeNode.collapsedIcon = "fa-database";

        } else if (entityType === EntityType.CONTACT) {

            treeNode.icon = "fa-user-o";
            treeNode.expandedIcon = "fa-user-o";
            treeNode.collapsedIcon = "fa-user-o";

        } else if (entityType === EntityType.MAPSET) {

            treeNode.icon = "fa-map-o";
            treeNode.expandedIcon = "fa-map-o";
            treeNode.collapsedIcon = "fa-map-o";

        } else if (entityType === EntityType.PLATFORM) {

            treeNode.icon = "fa-calculator";
            treeNode.expandedIcon = "fa-calculator";
            treeNode.collapsedIcon = "fa-calculator";

        } else if (entityType === EntityType.PROJECT) {

            treeNode.icon = "fa-clipboard";
            treeNode.expandedIcon = "fa-clipboard";
            treeNode.collapsedIcon = "fa-clipboard";

        } else if (entityType === EntityType.CV) {

            if (cvFilterType === CvFilterType.DATASET_TYPE) {
                treeNode.icon = "fa-file-excel-o";
                treeNode.expandedIcon = "fa-file-excel-o";
                treeNode.collapsedIcon = "fa-file-excel-o";
            }

        } else if (entityType === EntityType.MARKER_GROUP) {

            // if (isParent) {
            treeNode.icon = "fa-pencil";
            treeNode.expandedIcon = "fa-pencil";
            treeNode.collapsedIcon = "fa-pencil";
          
        }
    }

    private getIcons(gobiiFileItemCompoundId: GobiiFileItemCompoundId, isParent:boolean): { icon: string, expandedIcon: string, collapsedIcon: string } {


        let icon: string;
        let expandedIcon: string;
        let collapsedIcon: string;

        if (gobiiFileItemCompoundId.getEntityType() != null
            && gobiiFileItemCompoundId.getEntityType() != EntityType.UNKNOWN) {

            let entityIcons = this.getEntityIcon(gobiiFileItemCompoundId);
            icon = entityIcons.icon;
            expandedIcon = entityIcons.expandedIcon;
            collapsedIcon = entityIcons.collapsedIcon;


            //this.addEntityIconToNode(gobiiFileItemCompoundId.getEntityType(), gobiiFileItemCompoundId.getCvFilterType(), treeNode);

        } else if (gobiiFileItemCompoundId.getExtractorItemType() === ExtractorItemType.EXPORT_FORMAT) {
            icon = "fa-columns";
            expandedIcon = "fa-columns";
            collapsedIcon = "fa-columns";
        } else if (gobiiFileItemCompoundId.getExtractorItemType() === ExtractorItemType.SAMPLE_FILE) {
            icon = "fa-file-text-o";
            expandedIcon = "fa-file-text-o";
            collapsedIcon = "fa-file-text-o";
        } else if (gobiiFileItemCompoundId.getExtractorItemType() === ExtractorItemType.SAMPLE_LIST_ITEM) {
            if (isParent) {
                icon = "fa-list-ul";
                expandedIcon = "fa-list-ul";
                collapsedIcon = "fa-list-ul";
            } else {
                icon = "fa-eyedropper";
                expandedIcon = "fa-eyedropper";
                collapsedIcon = "fa-eyedropper";
            }
        } else if (gobiiFileItemCompoundId.getExtractorItemType() === ExtractorItemType.MARKER_FILE) {
            icon = "fa-file-text-o";
            expandedIcon = "fa-file-text-o";
            collapsedIcon = "fa-file-text-o";
        } else if (gobiiFileItemCompoundId.getExtractorItemType() === ExtractorItemType.MARKER_LIST_ITEM) {

            if (isParent) {
                icon = "fa-list-ul";
                expandedIcon = "fa-list-ul";
                collapsedIcon = "fa-list-ul";
            } else {
                icon = "fa-map-marker";
                expandedIcon = "fa-map-marker";
                collapsedIcon = "fa-map-marker";
            }
        } else if (gobiiFileItemCompoundId.getExtractorItemType() === ExtractorItemType.JOB_ID) {
            icon = "fa-info-circle";
            expandedIcon = "fa-info-circle";
            collapsedIcon = "fa-info-circle";
        } else if (gobiiFileItemCompoundId.getExtractorItemType() === ExtractorItemType.SAMPLE_LIST_TYPE) {
            icon = "fa-info-circle";
            expandedIcon = "fa-info-circle";
            collapsedIcon = "fa-info-circle";
        } else {
            //     }
            // } else if (fileModelNode.getItemType() == ExtractorItemType.CATEGORY ) {
            icon = "fa-folder";
            expandedIcon = "fa-folder-expanded";
            collapsedIcon = "fa-folder";
        }

        return {icon: icon, expandedIcon: expandedIcon, collapsedIcon: collapsedIcon};
    }

    private addIconsToNode(treeNode: GobiiTreeNode, isParent: boolean) {

        let icons = this.getIcons(treeNode,isParent);
        treeNode.icon = icons.icon;
        treeNode.expandedIcon = icons.expandedIcon;
        treeNode.collapsedIcon = icons.collapsedIcon;

        // if (treeNode.getEntityType() != null
        //     && treeNode.getEntityType() != EntityType.UNKNOWN) {
        //
        //     this.addEntityIconToNode(treeNode.getEntityType(), treeNode.getCvFilterType(), treeNode);
        //
        // } else if (treeNode.getItemType() === ExtractorItemType.EXPORT_FORMAT) {
        //     treeNode.icon = "fa-columns";
        //     treeNode.expandedIcon = "fa-columns";
        //     treeNode.collapsedIcon = "fa-columns";
        // } else if (treeNode.getItemType() === ExtractorItemType.SAMPLE_FILE) {
        //     treeNode.icon = "fa-file-text-o";
        //     treeNode.expandedIcon = "fa-file-text-o";
        //     treeNode.collapsedIcon = "fa-file-text-o";
        // } else if (treeNode.getItemType() === ExtractorItemType.SAMPLE_LIST_ITEM) {
        //     if (isParent) {
        //         treeNode.icon = "fa-list-ul";
        //         treeNode.expandedIcon = "fa-list-ul";
        //         treeNode.collapsedIcon = "fa-list-ul";
        //     } else {
        //         treeNode.icon = "fa-eyedropper";
        //         treeNode.expandedIcon = "fa-eyedropper";
        //         treeNode.collapsedIcon = "fa-eyedropper";
        //     }
        // } else if (treeNode.getItemType() === ExtractorItemType.MARKER_FILE) {
        //     treeNode.icon = "fa-file-text-o";
        //     treeNode.expandedIcon = "fa-file-text-o";
        //     treeNode.collapsedIcon = "fa-file-text-o";
        // } else if (treeNode.getItemType() === ExtractorItemType.MARKER_LIST_ITEM) {
        //
        //     if (isParent) {
        //         treeNode.icon = "fa-list-ul";
        //         treeNode.expandedIcon = "fa-list-ul";
        //         treeNode.collapsedIcon = "fa-list-ul";
        //     } else {
        //         treeNode.icon = "fa-map-marker";
        //         treeNode.expandedIcon = "fa-map-marker";
        //         treeNode.collapsedIcon = "fa-map-marker";
        //     }
        // } else if (treeNode.getItemType() === ExtractorItemType.JOB_ID) {
        //     treeNode.icon = "fa-info-circle";
        //     treeNode.expandedIcon = "fa-info-circle";
        //     treeNode.collapsedIcon = "fa-info-circle";
        // } else if (treeNode.getItemType() === ExtractorItemType.SAMPLE_LIST_TYPE) {
        //     treeNode.icon = "fa-info-circle";
        //     treeNode.expandedIcon = "fa-info-circle";
        //     treeNode.collapsedIcon = "fa-info-circle";
        // } else {
        //     //     }
        //     // } else if (fileModelNode.getItemType() == ExtractorItemType.CATEGORY ) {
        //     treeNode.icon = "fa-folder";
        //     treeNode.expandedIcon = "fa-folder-expanded";
        //     treeNode.collapsedIcon = "fa-folder";
        // }
    }

    public makeTreeNodeFromFileItem(gobiiFileItem: GobiiFileItem): GobiiTreeNode {


        let returnVal: GobiiTreeNode = GobiiTreeNode
            .build(gobiiFileItem.getGobiiExtractFilterType(), gobiiFileItem.getExtractorItemType())
            .setFileItemId(gobiiFileItem.getFileItemUniqueId())
            .setEntityType(gobiiFileItem.getEntityType())
            .setEntitySubType(gobiiFileItem.getEntitySubType())
            .setCvFilterType(gobiiFileItem.getCvFilterType());

        this.addIconsToNode(returnVal, false);
        this.applyLabel(returnVal);
        this.addFileItemNameToNode(returnVal, gobiiFileItem);

        return returnVal;
    }

    addFileItemNameToNode(gobiiTreeNode: GobiiTreeNode, gobiiFileItem: GobiiFileItem) {

        if (gobiiTreeNode.getContainerType() === ContainerType.DATA) {
            gobiiTreeNode.label = gobiiFileItem.getItemName();
        } else { // coves the LEAF node use case
            if (gobiiFileItem.getExtractorItemType() == ExtractorItemType.EXPORT_FORMAT) {
                let gobiiExtractFormat: GobiiExtractFormat = <GobiiExtractFormat> GobiiExtractFormat[gobiiFileItem.getItemId()];
                gobiiTreeNode.label += ": " + Labels.instance().extractFormatTypeLabels[gobiiExtractFormat];
            } else if (gobiiFileItem.getExtractorItemType() == ExtractorItemType.JOB_ID) {
                gobiiTreeNode.label = Labels.instance().treeExtractorTypeLabels[ExtractorItemType.JOB_ID]
                    + ": " + gobiiFileItem.getItemId();
            } else {
                if (gobiiFileItem.getProcessType() !== ProcessType.DELETE) {
                    gobiiTreeNode.label += ": " + gobiiFileItem.getItemName();
                } else {
                    gobiiTreeNode.label = gobiiFileItem.getItemName();
                }
            }
        }
    }

    public markTreeItemMissing(gobiiExtractFilterType: GobiiExtractFilterType, gobiiFileItemCompoundId: GobiiFileItemCompoundId) {


        //let icon: string = "fa-chevron-circle-right";
        let icon: string = "fa-share";
        //let icon: string = "fa-chevron-right";

        this.store.dispatch(new treeNodeActions.SetTreeNodeLook(
            {
                gobiiExtractFilterType: gobiiExtractFilterType,
                gobiiFileItemCompoundId: gobiiFileItemCompoundId,
                icon: icon
            }
        ))
    }

    public unMarkTreeItemMissing(gobiiExtractFilterType: GobiiExtractFilterType, gobiiFileItemCompoundId: GobiiFileItemCompoundId) {

        let icons = this.getIcons(gobiiFileItemCompoundId, false);

        this.store.dispatch(new treeNodeActions.SetTreeNodeLook(
            {
                gobiiExtractFilterType: gobiiExtractFilterType,
                gobiiFileItemCompoundId: gobiiFileItemCompoundId,
                icon: icons.icon
            }
        ))
    }

}
