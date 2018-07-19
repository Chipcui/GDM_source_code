import {Injectable} from "@angular/core";
import {ContainerType, GobiiTreeNode} from "../../model/gobii-tree-node";
import {EntitySubType, EntityType} from "../../model/type-entity";
import {Labels} from "../../views/entity-labels";
import {ExtractorItemType} from "../../model/type-extractor-item";
import {GobiiExtractFilterType} from "../../model/type-extractor-filter";
import {CvGroup, getCvGroupName} from "../../model/cv-group";
import {GobiiFileItem} from "../../model/gobii-file-item";
import {GobiiExtractFormat} from "../../model/type-extract-format";
import {ProcessType} from "../../model/type-process";
import {GobiiFileItemCompoundId} from "../../model/gobii-file-item-compound-id";
import {TypeTreeNodeStatus} from "../../model/type-tree-node-status";
import * as treeNodeActions from '../../store/actions/treenode-action'
import {Store} from "@ngrx/store";
import * as fromRoot from '../../store/reducers';
import {Observable} from "rxjs/Observable";


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
                .setCvGroup(CvGroup.DATASET_TYPE),
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
                        .setEntitySubType(EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR)
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
                .setCvGroup(CvGroup.DATASET_TYPE),
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
                ]),
            // BY FLEX QUERY
            ...this.makeCommonNodes(GobiiExtractFilterType.FLEX_QUERY),
            GobiiTreeNode.build(GobiiExtractFilterType.FLEX_QUERY, ExtractorItemType.VERTEX)
                .setSequenceNum(1)
                .setEntityType(EntityType.UNKNOWN)
                .setContainerType(ContainerType.DATA)
                .setChildCompoundUniqueId(new GobiiFileItemCompoundId()
                    .setExtractorItemType(ExtractorItemType.VERTEX_VALUE)
                    .setEntityType(EntityType.ANY)
                    .setEntitySubType(EntitySubType.ANY)
                    .setCvGroup(CvGroup.ANY)),
            GobiiTreeNode.build(GobiiExtractFilterType.FLEX_QUERY, ExtractorItemType.VERTEX)
                .setSequenceNum(2)
                .setEntityType(EntityType.UNKNOWN)
                .setContainerType(ContainerType.DATA)
                .setChildCompoundUniqueId(new GobiiFileItemCompoundId()
                    .setExtractorItemType(ExtractorItemType.VERTEX_VALUE)
                    .setEntityType(EntityType.ANY)
                    .setEntitySubType(EntitySubType.ANY)
                    .setCvGroup(CvGroup.ANY)),
            GobiiTreeNode.build(GobiiExtractFilterType.FLEX_QUERY, ExtractorItemType.VERTEX)
                .setSequenceNum(3)
                .setEntityType(EntityType.UNKNOWN)
                .setContainerType(ContainerType.DATA)
                .setChildCompoundUniqueId(new GobiiFileItemCompoundId()
                    .setExtractorItemType(ExtractorItemType.VERTEX_VALUE)
                    .setEntityType(EntityType.ANY)
                    .setEntitySubType(EntitySubType.ANY)
                    .setCvGroup(CvGroup.ANY)),
            GobiiTreeNode.build(GobiiExtractFilterType.FLEX_QUERY, ExtractorItemType.VERTEX)
                .setSequenceNum(4)
                .setEntityType(EntityType.UNKNOWN)
                .setContainerType(ContainerType.DATA)
                .setChildCompoundUniqueId(new GobiiFileItemCompoundId()
                    .setExtractorItemType(ExtractorItemType.VERTEX_VALUE)
                    .setEntityType(EntityType.ANY)
                    .setEntitySubType(EntitySubType.ANY)
                    .setCvGroup(CvGroup.ANY)),
        ]; // array of gobii tree nodes

        // we know we only have to go one level deep in this case -- no need to recurse
        returnVal.forEach(function (currentNode, idx, nodes) {

            currentNode.getChildren().forEach(currentChild => {
                currentChild.parent = currentNode
            })
        });

        this.setTreeNodeProperties(returnVal);
        return returnVal;

    } // getInitialTree()

    private setTreeNodeProperties(treeNodes: GobiiTreeNode[]) {

        treeNodes.forEach(tn => {
            if ((tn.children === null) || (tn.children.length <= 0)) {
                this.addIconsToNode(tn, false);
                let label: string = this.getLabel(tn.getItemType(), tn.getEntityType(), tn.getEntitySubType(), tn.getCvGroup(), tn.getCvTerm(), tn.getSequenceNum());
                tn.setLabel(label);
                tn.setGenericLabel(label);
            } else {
                this.setTreeNodeProperties(tn.children);
            }
        })
    }


    private getLabel(itemType: ExtractorItemType,
                     entityType: EntityType,
                     entitySubType: EntitySubType,
                     cvGroup: CvGroup,
                     cvTerm: String,
                     sequenceNum: number): string {

        let labelValue: string = "";

        if (itemType === ExtractorItemType.ENTITY) {

            labelValue = this.getEntityLabel(entityType, entitySubType, cvGroup);

        } else if ((itemType === ExtractorItemType.VERTEX)
            || (itemType == ExtractorItemType.VERTEX_VALUE)) {

            let separator = "";
            if (itemType === ExtractorItemType.VERTEX) {
                labelValue = "Filter " + sequenceNum.toString();
                separator = ": ";
            }

            if (cvTerm) {

                let entityLabel: string = this.getEntityLabel(entityType, entitySubType, cvGroup);

                labelValue += separator + entityLabel + " " + cvTerm;

            } else if (entityType !== EntityType.UNKNOWN
                || entitySubType !== EntitySubType.UNKNOWN
                || cvGroup !== CvGroup.UNKNOWN) {

                labelValue += separator + this.getEntityLabel(entityType, entitySubType, cvGroup);
            }

        } else {
            labelValue = Labels.instance().treeExtractorTypeLabels[itemType];
        }

        return labelValue;

    }

    private getEntityIcon(entityType: EntityType,
                          cvGroup: CvGroup,
                          cvTerm: string): { icon: string, expandedIcon: string, collapsedIcon: string } {

        let icon: string;
        let expandedIcon: string;
        let collapsedIcon: string;

        if (entityType === EntityType.DATASET) {

            icon = "fa-database";
            expandedIcon = "fa-folder-expanded";
            collapsedIcon = "fa-database";

        } else if (entityType === EntityType.CONTACT) {

            icon = "fa-user-o";
            expandedIcon = "fa-user-o";
            collapsedIcon = "fa-user-o";

        } else if (entityType === EntityType.MAPSET) {

            icon = "fa-map-o";
            expandedIcon = "fa-map-o";
            collapsedIcon = "fa-map-o";

        } else if (entityType === EntityType.PLATFORM) {

            icon = "fa-calculator";
            expandedIcon = "fa-calculator";
            collapsedIcon = "fa-calculator";

        } else if (entityType === EntityType.PROJECT) {

            icon = "fa-clipboard";
            expandedIcon = "fa-clipboard";
            collapsedIcon = "fa-clipboard";

        } else if (entityType === EntityType.EXPERIMENT) {

            icon = "fa-flask";
            expandedIcon = "fa-flask";
            collapsedIcon = "fa-flask";

        } else if (entityType === EntityType.ANALYSIS) {

            icon = "fa-line-chart";
            expandedIcon = "fa-line-chart";
            collapsedIcon = "fa-line-chart";

        } else if (entityType === EntityType.LINKAGE_GROUP) {

            icon = "fa-link";
            expandedIcon = "fa-link";
            collapsedIcon = "fa-link";

        } else if (entityType === EntityType.PROTOCOL) {

            icon = "fa-bars";
            expandedIcon = "fa-bars";
            collapsedIcon = "fa-bars";

        } else if (entityType === EntityType.VENDOR) {

            icon = "fa-building";
            expandedIcon = "fa-building";
            collapsedIcon = "fa-building";

        } else if (entityType === EntityType.VENDOR_PROTOCOL) {

            icon = "fa-copyright";
            expandedIcon = "fa-copyright";
            collapsedIcon = "fa-copyright";

        } else if (entityType === EntityType.MARKER_GROUP) {

            // if (isParent) {
            icon = "fa-pencil";
            expandedIcon = "fa-pencil";
            collapsedIcon = "fa-pencil";
        } else if (entityType === EntityType.CV && cvGroup !== null) {

            if (cvGroup === CvGroup.DATASET_TYPE) {
                icon = "fa-file-excel-o";
                expandedIcon = "fa-file-excel-o";
                collapsedIcon = "fa-file-excel-o";
            } else if (cvGroup === CvGroup.ANALYSIS_TYPE) {
                icon = "fa-area-chart";
                expandedIcon = "fa-area-chart";
                collapsedIcon = "fa-area-chart";
            } else if (cvGroup === CvGroup.GERMPLASM_TYPE) {
                icon = "fa-tree";
                expandedIcon = "fa-tree";
                collapsedIcon = "fa-tree";
            } else if (cvGroup === CvGroup.MAPSET_TYPE) {
                icon = "fa-map-pin";
                expandedIcon = "fa-map-pin";
                collapsedIcon = "fa-map-pin";
            }

        } else if (cvTerm) {
            // this condition captures all properties
            // all props within a group will get the same icon
            // technically, cvterm should be an enum; but, to paraphrase
            // Fermat -- I don't have the time for that solution now

            if (cvGroup === CvGroup.GERMPLASM_PROP) {
                icon = "fa-tree";
                expandedIcon = "fa-tree";
                collapsedIcon = "fa-tree";
            }

            if (cvGroup === CvGroup.PROJECT_PROP) {
                icon = "fa-clipboard";
                expandedIcon = "fa-clipboard";
                collapsedIcon = "fa-clipboard";
            }

            if (cvGroup === CvGroup.DNARUN_PROP) {
                icon = "fa-fast-forward";
                expandedIcon = "fa-fast-forward";
                collapsedIcon = "fa-fast-forward";
            }

            if (cvGroup === CvGroup.DNASAMPLE_PROP) {
                icon = "fa-eyedropper";
                expandedIcon = "fa-eyedropper";
                collapsedIcon = "fa-eyedropper";
            }

            if (cvGroup === CvGroup.MARKER_PROP) {
                icon = "fa-pencil";
                expandedIcon = "fa-pencil";
                collapsedIcon = "fa-pencil";
            }



        }

        return {icon: icon, expandedIcon: expandedIcon, collapsedIcon: collapsedIcon};
    }

    private getEntityLabel(entityType: EntityType, entitySubType: EntitySubType, cvFilterType: CvGroup) {

        let returnVal: string;

        if (entitySubType === EntitySubType.UNKNOWN) {

            if (entityType !== EntityType.CV) {
                returnVal = Labels.instance().entityNodeLabels[entityType];
            } else {
                returnVal = Labels.instance().cvGroupLabels[cvFilterType];
            }
        } else {
            returnVal = Labels.instance().entitySubtypeNodeLabels[entitySubType];
        }

        return returnVal;
    }

    private getIcons(gobiiFileItemCompoundId: GobiiFileItemCompoundId, isParent: boolean): { icon: string, expandedIcon: string, collapsedIcon: string } {


        let icon: string;
        let expandedIcon: string;
        let collapsedIcon: string;

        if (gobiiFileItemCompoundId.getEntityType() != null
            && gobiiFileItemCompoundId.getEntityType() != EntityType.UNKNOWN) {

            let entityIcons = this.getEntityIcon(gobiiFileItemCompoundId.getEntityType(),
                gobiiFileItemCompoundId.getCvGroup(),
                gobiiFileItemCompoundId.getCvTerm());
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

        let icons = this.getIcons(treeNode, isParent);
        treeNode.icon = icons.icon;
        treeNode.expandedIcon = icons.expandedIcon;
        treeNode.collapsedIcon = icons.collapsedIcon;

    }

    public makeTreeNodeFromFileItem(gobiiFileItem: GobiiFileItem): GobiiTreeNode {


        let returnVal: GobiiTreeNode = GobiiTreeNode.fromFileItem(gobiiFileItem);

        this.addIconsToNode(returnVal, false);


        let label: string = this.getLabel(returnVal.getItemType(), returnVal.getEntityType(), returnVal.getEntitySubType(), returnVal.getCvGroup(), returnVal.getCvTerm(), returnVal.getSequenceNum());

        returnVal.setLabel(label);
        returnVal.setGenericLabel(label)

        this.addFileItemNameToNode(returnVal, gobiiFileItem);

        return returnVal;
    }

    private addFileItemNameToNode(gobiiTreeNode: GobiiTreeNode, gobiiFileItem: GobiiFileItem) {

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

    /***
     *
     * @param {GobiiExtractFilterType} gobiiExtractFilterType
     * @param {GobiiFileItemCompoundId} targetGobiiFileItemCompoundId Determines the node that will be updated
     * @param {GobiiFileItemCompoundId} childGobiiFileItemCompoundId Determines what types of nodes can be added to the updated node
     */
    public updateTreeNode(gobiiExtractFilterType: GobiiExtractFilterType,
                          targetGobiiFileItemCompoundId: GobiiFileItemCompoundId,
                          childGobiiFileItemCompoundId: GobiiFileItemCompoundId) {


        let label: string = this.getLabel(targetGobiiFileItemCompoundId.getExtractorItemType(),
            targetGobiiFileItemCompoundId.getEntityType(),
            targetGobiiFileItemCompoundId.getEntitySubType(),
            targetGobiiFileItemCompoundId.getCvGroup(),
            targetGobiiFileItemCompoundId.getCvTerm(),
            targetGobiiFileItemCompoundId.getSequenceNum());

        let icons: any = this.getIcons(targetGobiiFileItemCompoundId, false);

        this.store.dispatch(new treeNodeActions.SetTreeNodeLook({
            gobiiExtractFilterType: gobiiExtractFilterType,
            targetCompoundId: targetGobiiFileItemCompoundId,
            childCompoundId: childGobiiFileItemCompoundId,
            icons: icons,
            label: label,
            entityType: targetGobiiFileItemCompoundId.getEntityType()
        }));
    }

    public markTreeItemMissing(gobiiExtractFilterType: GobiiExtractFilterType, gobiiFileItemCompoundId: GobiiFileItemCompoundId) {


        let icon: string = "fa-share";

        this.store.dispatch(new treeNodeActions.SetTreeNodeLook(
            {
                gobiiExtractFilterType: gobiiExtractFilterType,
                targetCompoundId: gobiiFileItemCompoundId,
                childCompoundId: null,
                icons: {icon: icon},
                label: null,
                entityType: null
            }
        ))
    }

    public unMarkTreeItemMissing(gobiiExtractFilterType: GobiiExtractFilterType, gobiiFileItemCompoundId: GobiiFileItemCompoundId) {

        let icons = this.getIcons(gobiiFileItemCompoundId, false);

        this.store.dispatch(new treeNodeActions.SetTreeNodeLook(
            {
                gobiiExtractFilterType: gobiiExtractFilterType,
                targetCompoundId: gobiiFileItemCompoundId,
                childCompoundId: null,
                icons: icons,
                label: null,
                entityType: null
            }
        ))
    }

}
