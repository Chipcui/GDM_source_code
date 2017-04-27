import {TreeNode} from "primeng/components/common/api";
import {EntityType, EntitySubType} from "./type-entity";
import {CvFilterType} from "./cv-filter-type";


export class GobiiTreeNode implements TreeNode {

    constructor(fileModelNodeId: string,
                fileItemId: string,
                required: boolean) {
        this.fileModelNodeId = fileModelNodeId;
        this.fileItemId = fileItemId;
        this.required = required;
    }

    public entityType: EntityType = EntityType.UNKNOWN;
    public entitySubType: EntitySubType = EntitySubType.UNKNOWN;
    public cvFilterType: CvFilterType = CvFilterType.UNKNOWN;
    public label: string;
    public data: any;
    public icon: any;
    public expandedIcon: any;
    public collapsedIcon: any;
    public children: GobiiTreeNode[] = [];
    public leaf: boolean;
    public expanded: boolean;
    public type: string;
    public parent: TreeNode;
    public partialSelected: boolean;
    public fileModelNodeId: string;
    public fileItemId: string;
    public required: boolean = false;

}