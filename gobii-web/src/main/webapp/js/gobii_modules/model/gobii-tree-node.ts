import {TreeNode} from "primeng/components/common/api";
import {EntitySubType, EntityType} from "./type-entity";
import {CvGroup} from "./cv-group";
import {Guid} from "./guid";
import {GobiiExtractFilterType} from "./type-extractor-filter";
import {ExtractorItemType} from "./type-extractor-item";
import {GobiiFileItemCompoundId} from "./gobii-file-item-compound-id";
import {GobiiFileItem} from "./gobii-file-item";


export enum ContainerType {NONE, STRUCTURE, DATA}

export class GobiiTreeNode extends GobiiFileItemCompoundId implements TreeNode {


    private id: string;

    //NG properties
    public label?: string;
    public data?: any;
    public icon?: any;
    public expandedIcon?: any;
    public collapsedIcon?: any;
    public children?: GobiiTreeNode[] = [];
    public leaf?: boolean;
    public expanded?: boolean;
    public type?: string;
    public parent?: GobiiTreeNode;
    public partialSelected?: boolean;
    public styleClass?: string;
    public draggable?: boolean;
    public droppable?: boolean;
    public selectable?: boolean;

    //GOBII UI properties
    private gobiiExtractFilterType: GobiiExtractFilterType = GobiiExtractFilterType.UNKNOWN;
    public genericLabel: string;
    public fileItemId: string;
    public required: boolean = false;
    public active: boolean = false;
    private containerType: ContainerType = ContainerType.NONE;
    private childCompoundUniqueId:GobiiFileItemCompoundId = null;

    constructor(parent: GobiiTreeNode,
                fileItemId: string,
                required: boolean) {

        super();

        this.id = Guid.generateUUID();


        this.parent = parent;
        this.fileItemId = fileItemId;
        this.required = required;
        this.selectable = false; // for now all nodes are not selectable
        //(TreeNode)
    }


    public static build(gobiiExtractFilterType: GobiiExtractFilterType,
                        extractoItemType: ExtractorItemType): GobiiTreeNode {

        let returnVal: GobiiTreeNode = new GobiiTreeNode(
            null,
            null,
            null
        );

        returnVal.gobiiExtractFilterType = gobiiExtractFilterType;
        returnVal.setItemType(extractoItemType);

        return returnVal;

    } //build


    public static fromFileItem(gobiiFileItem:GobiiFileItem) {

        return GobiiTreeNode.build(gobiiFileItem.getGobiiExtractFilterType(),
            gobiiFileItem.getExtractorItemType())
            .setFileItemId(gobiiFileItem.getFileItemUniqueId())
            .setEntityType(gobiiFileItem.getEntityType())
            .setEntitySubType(gobiiFileItem.getEntitySubType())
            .setCvGroup(gobiiFileItem.getCvGroup())
            .setCvTerm(gobiiFileItem.getCvTerm())
            .setSequenceNum(gobiiFileItem.getSequenceNum());
    }

    getId(): string {
        return this.id;
    }

//unique identifiers
    getItemType(): ExtractorItemType {
        return super.getExtractorItemType();
    }

    setItemType(value: ExtractorItemType): GobiiTreeNode {
        super.setExtractorItemType(value);
        return this;
    }

    getEntityType(): EntityType {
        return super.getEntityType();
    }

    setEntityType(value: EntityType): GobiiTreeNode {
        super.setEntityType(value);
        return this;
    }

    getEntitySubType(): EntitySubType {
        return super.getEntitySubType();
    }

    setEntitySubType(value: EntitySubType): GobiiTreeNode {
        super.setEntitySubType(value);
        return this;
    }

    getCvGroup(): CvGroup {
        return super.getCvGroup();
    }

    setCvGroup(value: CvGroup): GobiiTreeNode {
        super.setCvGroup(value);
        return this;
    }

    getCvTerm(): string {
        return super.getCvTerm();
    }

    setCvTerm(value: string): GobiiTreeNode {
        super.setCvTerm(value);
        return this;
    }

    getCvFilterValue(): string {
        return super.getCvFilterValue();
    }

    setCvFilterValue(value: string) {

        super.setCvFilterValue(value);
        return this;
    }


    getSequenceNum(): number {
        return super.getSequenceNum();
    }

    setSequenceNum(value:number) {
        super.setSequenceNum(value);
        return this;
    }

    getIsExtractCriterion(): boolean {
        return super.getIsExtractCriterion();
    }

    setIsExtractCriterion(value: boolean): GobiiFileItemCompoundId {
        super.setIsExtractCriterion(value);
        return this;
    }





    setGobiiExtractFilterType(gobiiExtractFilterType: GobiiExtractFilterType): GobiiTreeNode {
        this.gobiiExtractFilterType = gobiiExtractFilterType;
        return this;
    }

    getGobiiExtractFilterType(): GobiiExtractFilterType {
        return this.gobiiExtractFilterType;
    }

    getActive(): boolean {
        return this.active;
    }

    setActive(value: boolean): GobiiTreeNode {
        this.active = value;
        return this;
    }




    getLabel(): string {
        return this.label;
    }

    setLabel(value: string): GobiiTreeNode {
        this.label = value;
        return this;
    }

    getGenericLabel(): string {
        return this.genericLabel
    }

    setGenericLabel(value: string): GobiiTreeNode {
        this.genericLabel = value;
        return this;
    }

    resetLabel() {
        this.label = this.genericLabel;
    }


    getData(): any {
        return this.data;
    }

    setData(value: any): GobiiTreeNode {
        this.data = value;
        return this;
    }

    getIcon(): any {
        return this.icon;
    }

    setIcon(value: any): GobiiTreeNode {
        this.icon = value;
        return this;
    }

    getExpandedIcon(): any {
        return this.expandedIcon;
    }

    setExpandedIcon(value: any): GobiiTreeNode {
        this.expandedIcon = value;
        return this;
    }

    getCollapsedIcon(): any {
        return this.collapsedIcon;
    }

    setCollapsedIcon(value: any): GobiiTreeNode {
        this.collapsedIcon = value;
        return this;
    }

    getChildren(): GobiiTreeNode[] {
        return this.children;
    }

    setChildren(value: GobiiTreeNode[]): GobiiTreeNode {
        this.children = value;
        return this;
    }

    getLeaf(): boolean {
        return this.leaf;
    }

    setLeaf(value: boolean): GobiiTreeNode {
        this.leaf = value;
        return this;
    }

    getExpanded(): boolean {
        return this.expanded;
    }

    setExpanded(value: boolean): GobiiTreeNode {
        this.expanded = value;
        return this;
    }

    getType(): string {
        return this.type;
    }

    setType(value: string): GobiiTreeNode {
        this.type = value;
        return this;
    }

    getPartialSelected(): boolean {
        return this.partialSelected;
    }

    setPartialSelected(value: boolean): GobiiTreeNode {
        this.partialSelected = value;
        return this;
    }

    getFileItemId(): string {
        return this.fileItemId;
    }

    setFileItemId(value: string): GobiiTreeNode {
        this.fileItemId = value;
        return this;
    }

    getRequired(): boolean {
        return this.required;
    }

    setRequired(value: boolean): GobiiTreeNode {
        this.required = value;
        return this;
    }


    getContainerType(): ContainerType {
        return this.containerType;
    }

    setContainerType(value: ContainerType): GobiiTreeNode {
        this.containerType = value;
        return this;
    }

    getChildCompoundUniqueId():GobiiFileItemCompoundId {

        if(!this.childCompoundUniqueId) {
            this.childCompoundUniqueId = GobiiFileItemCompoundId.fromGobiiFileItemCompoundId(this);
        }

        return this.childCompoundUniqueId;
    }

    setChildCompoundUniqueId(value:GobiiFileItemCompoundId) {
        this.childCompoundUniqueId = value;
        return this;
    }
}