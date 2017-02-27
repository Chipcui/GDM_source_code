import {Injectable} from "@angular/core";
import {FileItem} from "../../model/file-item";
import {FileModelState, FileModelTreeEvent} from "../../model/file-model-tree-event";
import {FileModelNode, ExtractorItemType, ExtractorCategoryType, CardinalityType} from "../../model/file-model-node";
import {GobiiExtractFilterType} from "../../model/type-extractor-filter";
import {EntityType, EntitySubType} from "../../model/type-entity";
import {CvFilterType} from "../../model/cv-filter-type";
import {Subject} from 'rxjs/Subject';
import {Observable} from 'rxjs/Observable';


@Injectable()
export class FileModelTreeService {


    constructor() {
    }

    fileModelNodeTree: Map < GobiiExtractFilterType, Array < FileModelNode >> =
        new Map<GobiiExtractFilterType,Array<FileModelNode>>();


    entityNodeLabels: Map < EntityType, string > = new Map<EntityType,string>();
    entitySubtypeNodeLabels: Map < EntitySubType, string > = new Map<EntitySubType,string>();
    cvFilterNodeLabels: Map < CvFilterType, string > = new Map<CvFilterType,string>();
    extractorFilterTypeLabels: Map < GobiiExtractFilterType, string > = new Map<GobiiExtractFilterType, string>();
    treeCategoryLabels: Map<ExtractorCategoryType,string> = new Map<ExtractorCategoryType,string>();

    private getFileModelNodes(gobiiExtractFilterType: GobiiExtractFilterType): FileModelNode[] {

        if (this.fileModelNodeTree.size === 0 ) {

            this.entityNodeLabels[EntityType.DataSets] = "Data Sets";
            this.entityNodeLabels[EntityType.Platforms] = "Platforms";
            this.entityNodeLabels[EntityType.Mapsets] = "Mapsets";

            this.cvFilterNodeLabels[CvFilterType.DATASET_TYPE] = "Dataset Type";

            this.entitySubtypeNodeLabels[EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR] = "Principle Investigator";
            this.entitySubtypeNodeLabels[EntitySubType.CONTACT_SUBMITED_BY] = "Submitted By";

            this.extractorFilterTypeLabels[GobiiExtractFilterType.WHOLE_DATASET] = "Extract by Dataset";
            this.extractorFilterTypeLabels[GobiiExtractFilterType.BY_SAMPLE] = "Extract by Sample";
            this.extractorFilterTypeLabels[GobiiExtractFilterType.BY_MARKER] = "Extract by Marker";

            // **** FOR ALL EXTRACTION TYPES
            let submissionItemsForAll: FileModelNode[] = [];
            submissionItemsForAll.push(FileModelNode.build(ExtractorItemType.ENTITY)
                .setCategoryType(ExtractorCategoryType.LEAF)
                .setEntityType(EntityType.Contacts)
                .setEntityName(this.entitySubtypeNodeLabels[EntitySubType.CONTACT_SUBMITED_BY])
                .setCardinality(CardinalityType.ONE_ONLY)
            );

            submissionItemsForAll.push(FileModelNode.build(ExtractorItemType.EXPORT_FORMAT)
                .setCategoryType(ExtractorCategoryType.LEAF)
                .setEntityName("Export Formats")
                .setCardinality(CardinalityType.ONE_ONLY)
            );

            submissionItemsForAll.push(FileModelNode.build(ExtractorItemType.ENTITY)
                .setCategoryType(ExtractorCategoryType.LEAF)
                .setEntityType(EntityType.Mapsets)
                .setEntityName(this.entityNodeLabels[EntityType.Mapsets])
                .setCardinality(CardinalityType.ZERO_OR_ONE)
            );


            // ******** SET UP extract by dataset
            // -- Data set type
            let submissionItemsForDataSet: FileModelNode[] = [];
            submissionItemsForDataSet = submissionItemsForDataSet.concat(submissionItemsForAll);
            submissionItemsForDataSet.push(
                FileModelNode.build(ExtractorItemType.CATEGORY)
                    .setCategoryType(ExtractorCategoryType.ENTITY_CONTAINER)
                    .setEntityType(EntityType.DataSets)
                    .setCategoryName(this.entityNodeLabels[EntityType.DataSets]));

            this.fileModelNodeTree.set(GobiiExtractFilterType.WHOLE_DATASET, submissionItemsForDataSet);

            // ******** SET UP extract by samples
            // -- Data set type
            let submissionItemsForBySample: FileModelNode[] = [];
            submissionItemsForBySample = submissionItemsForBySample.concat(submissionItemsForAll);
            submissionItemsForBySample.push(
                FileModelNode.build(ExtractorItemType.ENTITY)
                    .setCategoryType(ExtractorCategoryType.LEAF)
                    .setEntityType(EntityType.CvTerms)
                    .setCvFilterType(CvFilterType.DATASET_TYPE)
                    .setEntityName(this.cvFilterNodeLabels[CvFilterType.DATASET_TYPE])
                    .setCardinality(CardinalityType.ONE_ONLY)
            );

            // -- Platforms
            submissionItemsForBySample.push(
                FileModelNode.build(ExtractorItemType.CATEGORY)
                    .setCategoryType(ExtractorCategoryType.ENTITY_CONTAINER)
                    .setCategoryName(this.entityNodeLabels[EntityType.Platforms])
                    .setCardinality(CardinalityType.ZERO_OR_MORE)
                    .addChild(
                        FileModelNode.build(ExtractorItemType.ENTITY)
                            .setCategoryType(ExtractorCategoryType.LEAF)
                            .setEntityType(EntityType.Platforms)
                            .setEntityName(this.entityNodeLabels[EntityType.Platforms])
                            .setCardinality(CardinalityType.ZERO_OR_MORE)
                    )
            );

            // -- Samples
            submissionItemsForBySample.push(
                FileModelNode.build(ExtractorItemType.CATEGORY)
                    .setCategoryType(ExtractorCategoryType.CONTAINER)
                    .setCategoryName("Sample Crieria")
                    .setCardinality(CardinalityType.ONE_OR_MORE)
                    .setAlternatePeerTypes([EntityType.Projects, EntityType.Contacts])
                    .addChild(FileModelNode.build(ExtractorItemType.ENTITY)
                        .setCategoryType(ExtractorCategoryType.LEAF)
                        .setEntityType(EntityType.Contacts)
                        .setEntityName("Principle Investigator")
                        .setCardinality(CardinalityType.ZERO_OR_ONE)
                    )
                    .addChild(FileModelNode.build(ExtractorItemType.ENTITY)
                        .setEntityType(EntityType.Projects)
                        .setEntityName(this.entityNodeLabels[EntityType.Projects])
                        .setCardinality(CardinalityType.ZERO_OR_MORE)
                    )
                    .addChild(FileModelNode.build(ExtractorItemType.SAMPLE_LIST)
                        .setEntityName("Sample List")
                        .setCategoryName(this.entityNodeLabels[EntityType.Platforms])
                        .setCardinality(CardinalityType.ZERO_OR_MORE)
                    )
            );
            this.fileModelNodeTree.set(GobiiExtractFilterType.BY_SAMPLE, submissionItemsForBySample);

        }

        return this.fileModelNodeTree.get(gobiiExtractFilterType);
    }


    private mutate(fileItem: FileItem): FileModelTreeEvent {

        let returnVal: FileModelTreeEvent = null;

        if (fileItem.gobiiExtractFilterType != GobiiExtractFilterType.UNKNOWN) {

            let fileModelNodes: FileModelNode[] = this.getFileModelNodes(fileItem.gobiiExtractFilterType);
            let fileModelNodeForFileItem: FileModelNode = this.placeNodeInTree(fileModelNodes, fileItem);

            returnVal = new FileModelTreeEvent(fileItem, fileModelNodeForFileItem, FileModelState.NOT_COMPLETE, null);

        } else {
            returnVal = new FileModelTreeEvent(fileItem,
                null,
                FileModelState.ERROR,
                "An invalid extract filter type was specified");
        }


        return returnVal;
    }


    findFileModelNode(fileModelNodes: FileModelNode[], extractorItemType: ExtractorItemType, entityType: EntityType) {

        let returnVal: FileModelNode = null;

        for (let idx: number = 0; ( idx < fileModelNodes.length) && (returnVal == null ); idx++) {
            let currentTemplate: FileModelNode = fileModelNodes[idx];
            returnVal = this.findTemplateByCriteria(currentTemplate, extractorItemType, entityType);
        }

        return returnVal;

    }


    findTemplateByCriteria(statusTreeTemplate: FileModelNode,
                           extractorItemType: ExtractorItemType, entityType: EntityType): FileModelNode {

        let returnVal: FileModelNode = null;

        if (statusTreeTemplate.getChildren() != null) {

            for (let idx: number = 0; ( idx < statusTreeTemplate.getChildren().length) && (returnVal == null ); idx++) {

                let currentTemplate: FileModelNode = statusTreeTemplate.getChildren()[idx];
                returnVal = this.findTemplateByCriteria(currentTemplate, extractorItemType, entityType);
            }
        }

        if (returnVal === null) {

            if (extractorItemType == statusTreeTemplate.getItemType()
                && entityType == statusTreeTemplate.getEntityType()) {

                returnVal = statusTreeTemplate;
            }
        }

        return returnVal;

    }


    private placeNodeInTree(fileModelNodes: FileModelNode[], fileItemEvent: FileItem): FileModelNode {

        let fileModelNode: FileModelNode =
            this.findFileModelNode(fileModelNodes, ExtractorItemType.CATEGORY, fileItemEvent.entityType);


        if (fileModelNode.getCategoryType() === ExtractorCategoryType.LEAF) {

            if (fileModelNode.getFileItems().length === 0) {
                fileModelNode.getFileItems().push(fileItemEvent);
            } else {
                fileModelNode.getFileItems()[0] = fileItemEvent;
            }

        } else if (fileModelNode.getCategoryType() === ExtractorCategoryType.ENTITY_CONTAINER) {

            let existingItems: FileItem[] = fileModelNode.getFileItems().filter(
                item => {
                    return item.fileItemUniqueId === fileItemEvent.fileItemUniqueId;
                }
            )

            if (existingItems.length === 0) {
                fileModelNode.getFileItems().push(fileItemEvent);
            } else {
                let idx: number = fileModelNode.getFileItems().indexOf(existingItems[0]);
                fileModelNode.getFileItems()[idx] = fileItemEvent;
            }

        } else {
            // this.reportMessage("The node of category  "
            //     + fileModelNode.getCategoryType()
            //     + " for checkbox event " + fileItemEvent.itemName
            //     + " could not be placed in the tree ");
        }


        return fileModelNode;

    } //


    public subject: Subject<FileModelTreeEvent> = new Subject<FileModelTreeEvent>();



    public put(fileItem: FileItem): Observable < FileModelTreeEvent > {

        return Observable.create(observer => {

            let fileTreeEvent: FileModelTreeEvent = this.mutate(fileItem);


            observer.next(fileTreeEvent);
            observer.complete();

            this.subject.next(fileTreeEvent);
        });
    }

    public get(gobiiExtractFilterType:GobiiExtractFilterType): Observable <FileModelNode[]> {

        return Observable.create( observer => {
            let nodesForFilterType : FileModelNode[] = this.getFileModelNodes(gobiiExtractFilterType);
            observer.next(nodesForFilterType);
            observer.complete();
        } );
    }

}
