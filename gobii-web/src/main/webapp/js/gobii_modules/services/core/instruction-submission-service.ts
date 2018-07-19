import {Injectable} from "@angular/core";
import {EntitySubType, EntityType} from "../../model/type-entity";
import {ExtractorItemType} from "../../model/type-extractor-item";
import {GobiiExtractFilterType} from "../../model/type-extractor-filter";
import {CvGroup} from "../../model/cv-group";
import {GobiiFileItem} from "../../model/gobii-file-item";
import {GobiiExtractFormat} from "../../model/type-extract-format";
import * as fromRoot from '../../store/reducers';
import * as historyAction from '../../store/actions/history-action';

import {Store} from "@ngrx/store";
import {NameId} from "../../model/name-id";
import {Observable} from "rxjs/Observable";
import {GobiiSampleListType} from "../../model/type-extractor-sample-list";
import {GobiiDataSetExtract} from "../../model/extractor-instructions/data-set-extract";
import {GobiiExtractorInstruction} from "../../model/extractor-instructions/gobii-extractor-instruction";
import {ExtractorInstructionFilesDTO} from "../../model/extractor-instructions/dto-extractor-instruction-files";
import {DtoRequestItemExtractorSubmission} from "../app/dto-request-item-extractor-submission";
import {GobiiFileType} from "../../model/type-gobii-file";
import {DtoRequestService} from "./dto-request.service";
import {GobiiFileItemCompoundId} from "../../model/gobii-file-item-compound-id";
import {GobiiFileItemCriterion} from "../../model/gobii-file-item-criterion";
import {TreeStructureService} from "./tree-structure-service";
import {FlexQueryService} from "./flex-query-service";
import {FilterParamNames} from "../../model/file-item-param-names";
import {Vertex} from "../../model/vertex";
import {Observer} from "rxjs/Observer";
import {AddStatusMessageAction} from "../../store/actions/history-action";

@Injectable()
export class InstructionSubmissionService {


    private datasetCriterion: GobiiFileItemCriterion = new GobiiFileItemCriterion(new GobiiFileItemCompoundId(
        ExtractorItemType.ENTITY,
        EntityType.DATASET,
        EntitySubType.UNKNOWN,
        CvGroup.UNKNOWN,
        null
    ), false);

    private sampleItemCriterion: GobiiFileItemCriterion = new GobiiFileItemCriterion(new GobiiFileItemCompoundId(
        ExtractorItemType.SAMPLE_LIST_ITEM,
        EntityType.UNKNOWN,
        EntitySubType.UNKNOWN,
        CvGroup.UNKNOWN,
        null
    ), false);

    private samplefileCriterion: GobiiFileItemCriterion = new GobiiFileItemCriterion(new GobiiFileItemCompoundId(
        ExtractorItemType.SAMPLE_FILE,
        EntityType.UNKNOWN,
        EntitySubType.UNKNOWN,
        CvGroup.UNKNOWN,
        null
    ), false);

    private piContactCriterion: GobiiFileItemCriterion = new GobiiFileItemCriterion(new GobiiFileItemCompoundId(
        ExtractorItemType.ENTITY,
        EntityType.CONTACT,
        EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR,
        CvGroup.UNKNOWN,
        null
    ), false);

    private projectsCriterion: GobiiFileItemCriterion = new GobiiFileItemCriterion(new GobiiFileItemCompoundId(
        ExtractorItemType.ENTITY,
        EntityType.PROJECT,
        EntitySubType.UNKNOWN,
        CvGroup.UNKNOWN,
        null
    ), false);

    private datasetTypesCriterion: GobiiFileItemCriterion = new GobiiFileItemCriterion(new GobiiFileItemCompoundId(
        ExtractorItemType.ENTITY,
        EntityType.CV,
        EntitySubType.UNKNOWN,
        CvGroup.DATASET_TYPE,
        null
    ), false);


    private markerListItemCriterion: GobiiFileItemCriterion = new GobiiFileItemCriterion(new GobiiFileItemCompoundId(
        ExtractorItemType.MARKER_LIST_ITEM,
        EntityType.UNKNOWN,
        EntitySubType.UNKNOWN,
        CvGroup.UNKNOWN,
        null
    ), false);


    private markerListFileCriterion: GobiiFileItemCriterion = new GobiiFileItemCriterion(new GobiiFileItemCompoundId(
        ExtractorItemType.MARKER_FILE,
        EntityType.UNKNOWN,
        EntitySubType.UNKNOWN,
        CvGroup.UNKNOWN,
        null
    ), false);


    private markergGroupCriterion: GobiiFileItemCriterion = new GobiiFileItemCriterion(new GobiiFileItemCompoundId(
        ExtractorItemType.ENTITY,
        EntityType.MARKER_GROUP,
        EntitySubType.UNKNOWN,
        CvGroup.UNKNOWN,
        null
    ), false);

    private platformCriterion: GobiiFileItemCriterion = new GobiiFileItemCriterion(new GobiiFileItemCompoundId(
        ExtractorItemType.ENTITY,
        EntityType.PLATFORM,
        EntitySubType.UNKNOWN,
        CvGroup.UNKNOWN,
        null
    ), false);


    constructor(private store: Store<fromRoot.State>,
                private dtoRequestServiceExtractorFile: DtoRequestService<ExtractorInstructionFilesDTO>,
                private treeStructureService: TreeStructureService,
                private flexQueryService: FlexQueryService) {


    }

    private isItemPresent(gobiiFileItems: GobiiFileItem[], gobiiFileItemCriterion: GobiiFileItemCriterion) {

        return gobiiFileItems.filter(fi => gobiiFileItemCriterion.compoundIdeEquals(fi)).length > 0;

    }

    public unmarkMissingItems(gobiiExtractFilterType: GobiiExtractFilterType) {

        if (gobiiExtractFilterType) {

            this.store.select(fromRoot.getSelectedFileItems)
                .subscribe(all => {

                    if (gobiiExtractFilterType === GobiiExtractFilterType.WHOLE_DATASET) {

                        if (!this.isItemPresent(all, this.datasetCriterion)) {

                            this.treeStructureService.unMarkTreeItemMissing(gobiiExtractFilterType, this.datasetCriterion);
                        }

                    } else if (gobiiExtractFilterType === GobiiExtractFilterType.BY_SAMPLE) {

                        this.treeStructureService.unMarkTreeItemMissing(gobiiExtractFilterType, this.samplefileCriterion);
                        this.treeStructureService.unMarkTreeItemMissing(gobiiExtractFilterType, this.sampleItemCriterion);
                        this.treeStructureService.unMarkTreeItemMissing(gobiiExtractFilterType, this.projectsCriterion);
                        this.treeStructureService.unMarkTreeItemMissing(gobiiExtractFilterType, this.piContactCriterion);
                        this.treeStructureService.unMarkTreeItemMissing(gobiiExtractFilterType, this.datasetTypesCriterion);


                    } else if (gobiiExtractFilterType === GobiiExtractFilterType.BY_MARKER) {

                        this.treeStructureService.unMarkTreeItemMissing(gobiiExtractFilterType, this.markerListItemCriterion);
                        this.treeStructureService.unMarkTreeItemMissing(gobiiExtractFilterType, this.markerListFileCriterion);
                        this.treeStructureService.unMarkTreeItemMissing(gobiiExtractFilterType, this.markergGroupCriterion);
                        this.treeStructureService.unMarkTreeItemMissing(gobiiExtractFilterType, this.platformCriterion);
                        this.treeStructureService.unMarkTreeItemMissing(gobiiExtractFilterType, this.datasetTypesCriterion);

                    } else if (gobiiExtractFilterType === GobiiExtractFilterType.FLEX_QUERY) {


                    } else {
                        this.store.dispatch(new historyAction.AddStatusMessageAction("Unhandled extract filter type: " + GobiiExtractFilterType[gobiiExtractFilterType ? gobiiExtractFilterType : GobiiExtractFilterType.UNKNOWN]));

                    }
                }).unsubscribe();

        } // if we have an extract filter type
    }

    public markMissingItems(gobiiExtractFilterType: GobiiExtractFilterType) {


        if (gobiiExtractFilterType) {

            this.store.select(fromRoot.getSelectedFileItems)
                .subscribe(all => {

                    if (!this.areCriteriaMet(all, gobiiExtractFilterType)) {

                        if (gobiiExtractFilterType === GobiiExtractFilterType.WHOLE_DATASET) {
                            if (!this.isItemPresent(all, this.datasetCriterion)) {

                                this.treeStructureService.markTreeItemMissing(gobiiExtractFilterType, this.datasetCriterion);

                            }
                        } else if (gobiiExtractFilterType === GobiiExtractFilterType.BY_SAMPLE) {

                            if (!this.isItemPresent(all, this.samplefileCriterion)) {
                                this.treeStructureService.markTreeItemMissing(gobiiExtractFilterType, this.samplefileCriterion);
                            }


                            if (!this.isItemPresent(all, this.sampleItemCriterion)) {
                                this.treeStructureService.markTreeItemMissing(gobiiExtractFilterType, this.sampleItemCriterion);
                            }

                            if (!this.isItemPresent(all, this.projectsCriterion)) {
                                this.treeStructureService.markTreeItemMissing(gobiiExtractFilterType, this.projectsCriterion);

                            }

                            if (!this.isItemPresent(all, this.piContactCriterion)) {
                                this.treeStructureService.markTreeItemMissing(gobiiExtractFilterType, this.piContactCriterion);

                            }

                            if (!this.isItemPresent(all, this.datasetTypesCriterion)) {
                                this.treeStructureService.markTreeItemMissing(gobiiExtractFilterType, this.datasetTypesCriterion);

                            }

                        } else if (gobiiExtractFilterType === GobiiExtractFilterType.BY_MARKER) {

                            if (!this.isItemPresent(all, this.markerListItemCriterion)) {
                                this.treeStructureService.markTreeItemMissing(gobiiExtractFilterType, this.markerListItemCriterion);
                            }

                            if (!this.isItemPresent(all, this.markerListFileCriterion)) {
                                this.treeStructureService.markTreeItemMissing(gobiiExtractFilterType, this.markerListFileCriterion);
                            }

                            if (!this.isItemPresent(all, this.markergGroupCriterion)) {
                                this.treeStructureService.markTreeItemMissing(gobiiExtractFilterType, this.markergGroupCriterion);
                            }

                            if (!this.isItemPresent(all, this.platformCriterion)) {
                                this.treeStructureService.markTreeItemMissing(gobiiExtractFilterType, this.platformCriterion);
                            }

                            if (!this.isItemPresent(all, this.datasetTypesCriterion)) {
                                this.treeStructureService.markTreeItemMissing(gobiiExtractFilterType, this.datasetTypesCriterion);
                            }

                        } else if (gobiiExtractFilterType === GobiiExtractFilterType.FLEX_QUERY) {

                            if (!this.isItemPresent(all, this.markerListItemCriterion)) {
                                this.treeStructureService.markTreeItemMissing(gobiiExtractFilterType, this.markerListItemCriterion);
                            }

                            if (!this.isItemPresent(all, this.markerListFileCriterion)) {
                                this.treeStructureService.markTreeItemMissing(gobiiExtractFilterType, this.markerListFileCriterion);
                            }

                            if (!this.isItemPresent(all, this.markergGroupCriterion)) {
                                this.treeStructureService.markTreeItemMissing(gobiiExtractFilterType, this.markergGroupCriterion);
                            }

                            if (!this.isItemPresent(all, this.platformCriterion)) {
                                this.treeStructureService.markTreeItemMissing(gobiiExtractFilterType, this.platformCriterion);
                            }

                            if (!this.isItemPresent(all, this.datasetTypesCriterion)) {
                                this.treeStructureService.markTreeItemMissing(gobiiExtractFilterType, this.datasetTypesCriterion);
                            }

                        } else {
                            this.store.dispatch(new historyAction.AddStatusMessageAction("Unhandled extract filter type: " + GobiiExtractFilterType[gobiiExtractFilterType ? gobiiExtractFilterType : GobiiExtractFilterType.UNKNOWN]));

                        }

                    }
                }).unsubscribe();

        } // if we have an extract filter type

    } // markMissingItems()

    private areCriteriaMet(all: GobiiFileItem[], gobiiExtractFilterType: GobiiExtractFilterType): boolean {

        let returnVal: boolean = false;

        if (gobiiExtractFilterType) {

            if (gobiiExtractFilterType === GobiiExtractFilterType.WHOLE_DATASET) {

                returnVal = this.isItemPresent(all, this.datasetCriterion);

            } else if (gobiiExtractFilterType === GobiiExtractFilterType.BY_SAMPLE) {

                let samplesArePresent: boolean = this.isItemPresent(all, this.samplefileCriterion)
                    || this.isItemPresent(all, this.sampleItemCriterion);
                let projectIsPresent: boolean = this.isItemPresent(all, this.projectsCriterion);
                let pIIsPresent: boolean = this.isItemPresent(all, this.piContactCriterion);
                let datasetTypeIsPresent: boolean = this.isItemPresent(all, this.datasetTypesCriterion);

                returnVal =
                    datasetTypeIsPresent &&
                    (samplesArePresent
                        || projectIsPresent
                        || pIIsPresent);

            } else if (gobiiExtractFilterType === GobiiExtractFilterType.BY_MARKER) {

                let markersArePresent: boolean = this.isItemPresent(all, this.markerListItemCriterion)
                    || this.isItemPresent(all, this.markerListFileCriterion);
                let markerGroupIsPresent: boolean = this.isItemPresent(all, this.markergGroupCriterion);
                let platformIsPresent: boolean = this.isItemPresent(all, this.platformCriterion);
                let datasetTypeIsPresent: boolean = this.isItemPresent(all, this.datasetTypesCriterion);


                returnVal =
                    datasetTypeIsPresent
                    && (markersArePresent
                    || markerGroupIsPresent
                    || platformIsPresent);

            } else if (gobiiExtractFilterType === GobiiExtractFilterType.FLEX_QUERY) {

                // submission can only take place when there is at leaset one vertex value filter
                // set and when there is a marker sample sample count
                this.flexQueryService.getVertexFilters(FilterParamNames.FQ_F4_VERTEX_VALUES)
                    .subscribe(filters => {

                        if (filters && filters.length > 0) {
                            let filterVals: Vertex[] =
                                filters
                                    .filter(vertex => (vertex.filterVals && vertex.filterVals.length > 0));

                            if (filterVals && filterVals.length > 0) {

                                this.store.select(fromRoot.getCurrentMarkerCount)
                                    .subscribe(markerCount => {

                                        if (markerCount > 0) {

                                            this.store.select(fromRoot.getCurrentSampleCount)
                                                .subscribe(sampleCount => {

                                                    if (sampleCount > 0) {

                                                        returnVal = true;
                                                    } // if there is a sample count

                                                }).unsubscribe(); // subscribe/unsubscribe to sample count
                                        } // if there is a marker count

                                    }).unsubscribe(); // subscribe/unsubscribe to marker count
                            } // if there are filters
                        }
                    })
                    .unsubscribe(); // subscribe/unsubscribe to vertex filters

            } else {

                this.store.dispatch(new historyAction.AddStatusMessageAction("Unhandled extract filter type: " + GobiiExtractFilterType[gobiiExtractFilterType ? gobiiExtractFilterType : GobiiExtractFilterType.UNKNOWN]));

            }

        } // if we have an extract type

        return returnVal;
    }

    public submitReady(gobiiExtractFilterType: GobiiExtractFilterType): Observable<boolean> {


        return Observable.create(observer => {
                this.store.select(fromRoot.getSelectedFileItems)
                    .subscribe(all => {


                            let submistReady: boolean = this.areCriteriaMet(all, gobiiExtractFilterType);
                            observer.next(submistReady);

                        }
                    ) // inner subscribe
            } //observer lambda
        ); // Observable.crate

    } // function()

    public submit(gobiiExtractFilterType: GobiiExtractFilterType): Observable<GobiiExtractorInstruction> {

        return Observable.create(observer => {

                let gobiiDataSetExtracts: GobiiDataSetExtract[] = [];
                let mapsetIds: number[] = [];
                let submitterContactid: number = null;
                let jobId: string = null;
                let markerFileName: string = null;
                let sampleFileName: string = null;
                let sampleListType: GobiiSampleListType;

                this.store.select(fromRoot.getSelectedFileItems)
                    .subscribe(fileItems => {


                            // ******** JOB ID
                            let fileItemJobId: GobiiFileItem = fileItems.find(item => {
                                return item.getExtractorItemType() === ExtractorItemType.JOB_ID
                            });

                            if (fileItemJobId != null) {
                                jobId = fileItemJobId.getItemId();
                            }

                            // ******** MARKER FILE
                            let fileItemMarkerFile: GobiiFileItem = fileItems.find(item => {
                                return item.getExtractorItemType() === ExtractorItemType.MARKER_FILE
                            });

                            if (fileItemMarkerFile != null) {
                                markerFileName = fileItemMarkerFile.getItemId();
                            }

                            // ******** SAMPLE FILE
                            let fileItemSampleFile: GobiiFileItem = fileItems.find(item => {
                                return item.getExtractorItemType() === ExtractorItemType.SAMPLE_FILE
                            });

                            if (fileItemSampleFile != null) {
                                sampleFileName = fileItemSampleFile.getItemId();
                            }

                            // ******** SUBMITTER CONTACT
                            let submitterFileItem: GobiiFileItem = fileItems.find(item => {
                                return (item.getEntityType() === EntityType.CONTACT)
                                    && (item.getEntitySubType() === EntitySubType.CONTACT_SUBMITED_BY)
                            });

                            submitterContactid = Number(submitterFileItem.getItemId());


                            // ******** MAPSET IDs
                            let mapsetFileItems: GobiiFileItem[] = fileItems
                                .filter(item => {
                                    return item.getEntityType() === EntityType.MAPSET
                                });
                            mapsetIds = mapsetFileItems
                                .map(item => {
                                    return Number(item.getItemId())
                                });

                            // ******** EXPORT FORMAT
                            let exportFileItem: GobiiFileItem = fileItems.find(item => {
                                return item.getExtractorItemType() === ExtractorItemType.EXPORT_FORMAT
                            });

                            // these probably should be just one enum
                            let gobiiFileType: GobiiFileType = null;
                            let extractFormat: GobiiExtractFormat = GobiiExtractFormat[exportFileItem.getItemId()];
                            if (extractFormat === GobiiExtractFormat.FLAPJACK) {
                                gobiiFileType = GobiiFileType.FLAPJACK;
                            } else if (extractFormat === GobiiExtractFormat.HAPMAP) {
                                gobiiFileType = GobiiFileType.HAPMAP;
                            } else if (extractFormat === GobiiExtractFormat.META_DATA_ONLY) {
                                gobiiFileType = GobiiFileType.META_DATA;
                            }


                            // ******** DATA SET TYPE
                            let dataTypeFileItem: GobiiFileItem = fileItems.find(item => {
                                return item.getEntityType() === EntityType.CV
                                    && item.getCvGroup() === CvGroup.DATASET_TYPE
                            });
                            let datasetType: NameId = dataTypeFileItem != null ? new NameId(dataTypeFileItem.getItemId(), null,
                                dataTypeFileItem.getItemName(), EntityType.CV, null, null) : null;


                            // ******** PRINCIPLE INVESTIGATOR CONCEPT
                            let principleInvestigatorFileItem: GobiiFileItem = fileItems.find(item => {
                                return item.getEntityType() === EntityType.CONTACT
                                    && item.getEntitySubType() === EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR
                            });
                            let principleInvestigator: NameId = principleInvestigatorFileItem != null ? new NameId(principleInvestigatorFileItem.getItemId(), null,
                                principleInvestigatorFileItem.getItemName(), EntityType.CONTACT, null, null) : null;


                            // ******** PROJECT
                            let projectFileItem: GobiiFileItem = fileItems.find(item => {
                                return item.getEntityType() === EntityType.PROJECT
                            });
                            let project: NameId = projectFileItem != null ? new NameId(projectFileItem.getItemId(), null,
                                projectFileItem.getItemName(), EntityType.PROJECT, null, null) : null;


                            // ******** PLATFORM
                            let platformFileItems: GobiiFileItem[] = fileItems.filter(item => {
                                return item.getEntityType() === EntityType.PLATFORM
                            });

                            let platforms: NameId[] = platformFileItems.map(item => {
                                return new NameId(item.getItemId(), null, item.getItemName(), EntityType.PLATFORM, null, null)
                            });

                            let markerGroupItems: GobiiFileItem[] = fileItems.filter(item => {
                                return item.getEntityType() === EntityType.MARKER_GROUP
                            });

                            let markerGroups: NameId[] = markerGroupItems.map(item => {
                                return new NameId(item.getItemId(), null, item.getItemName(), EntityType.MARKER_GROUP, null, null)
                            });

                            // ******** MARKERS
                            let markerListItems: GobiiFileItem[] =
                                fileItems
                                    .filter(fi => {
                                        return fi.getExtractorItemType() === ExtractorItemType.MARKER_LIST_ITEM
                                    });

                            let markerList: string[] = markerListItems
                                .map(mi => {
                                    return mi.getItemId()
                                });


                            // ******** SAMPLES
                            let sampleListItems: GobiiFileItem[] =
                                fileItems
                                    .filter(fi => {
                                        return fi.getExtractorItemType() === ExtractorItemType.SAMPLE_LIST_ITEM
                                    });

                            let sampleList: string[] = sampleListItems
                                .map(mi => {
                                    return mi.getItemId()
                                });


                            let sampleListTypeFileItem: GobiiFileItem = fileItems.find(item => {
                                return item.getExtractorItemType() === ExtractorItemType.SAMPLE_LIST_TYPE;
                            });

                            if (sampleListTypeFileItem != null) {
                                sampleListType = GobiiSampleListType[sampleListTypeFileItem.getItemId()];
                            }


                            // *** NOW PUT TOGETHER THE EXTRACT INSTRUCTIONS
                            if (gobiiExtractFilterType === GobiiExtractFilterType.WHOLE_DATASET) {

                                let dataSetItems: GobiiFileItem[] = fileItems
                                    .filter(item => {
                                        return item.getEntityType() === EntityType.DATASET
                                    });


                                dataSetItems.forEach(datsetFileItem => {

                                    let dataSet: NameId = new NameId(datsetFileItem.getItemId(), null,
                                        datsetFileItem.getItemName(), EntityType.CV, null, null);


                                    gobiiDataSetExtracts.push(new GobiiDataSetExtract(gobiiFileType,
                                        false,
                                        null,
                                        gobiiExtractFilterType,
                                        null,
                                        null,
                                        markerFileName,
                                        null,
                                        datasetType,
                                        platforms,
                                        null,
                                        null,
                                        dataSet,
                                        null,
                                        []));
                                }); // iterate dataset items


                                this.post(jobId, gobiiDataSetExtracts, submitterContactid, mapsetIds)
                                    .subscribe(extractorInstructions => {
                                        observer.next(extractorInstructions);
                                        observer.complete();
                                    });

                            } else if (gobiiExtractFilterType === GobiiExtractFilterType.BY_MARKER) {

                                gobiiDataSetExtracts.push(new GobiiDataSetExtract(gobiiFileType,
                                    false,
                                    null,
                                    gobiiExtractFilterType,
                                    markerList,
                                    null,
                                    markerFileName,
                                    null,
                                    datasetType,
                                    platforms,
                                    null,
                                    null,
                                    null,
                                    markerGroups,
                                    []));

                                this.post(jobId, gobiiDataSetExtracts, submitterContactid, mapsetIds)
                                    .subscribe(extractorInstructions => {
                                        observer.next(extractorInstructions);
                                        observer.complete();
                                    });

                            } else if (gobiiExtractFilterType === GobiiExtractFilterType.BY_SAMPLE) {

                                gobiiDataSetExtracts.push(new GobiiDataSetExtract(gobiiFileType,
                                    false,
                                    null,
                                    gobiiExtractFilterType,
                                    null,
                                    sampleList,
                                    sampleFileName,
                                    sampleListType,
                                    datasetType,
                                    platforms,
                                    principleInvestigator,
                                    project,
                                    null,
                                    null,
                                    []));

                                this.post(jobId, gobiiDataSetExtracts, submitterContactid, mapsetIds)
                                    .subscribe(extractorInstructions => {
                                        observer.next(extractorInstructions);
                                        observer.complete();
                                    });

                            } else if (gobiiExtractFilterType === GobiiExtractFilterType.FLEX_QUERY) {

                                this.flexQueryService.getVertexFilters(FilterParamNames.FQ_F4_VERTEX_VALUES)
                                    .subscribe(vertices => {

                                        if (vertices && vertices.length > 0) {

                                            // for the other extract types, fileitems on which the extract is based constitute
                                            // the content of the extract directly. In the case of flex query, the vertices
                                            // from the filters could theoretically not match the "selected" file items, which would
                                            // mean that the content of the extract would not be the same as what's displayed in the
                                            // tree. This condition _should_ not ever happen. But it's vitally important that this information
                                            // be reported correctly, so we double check here.
                                            let verticesMatchFileItems: boolean = true;
                                            for (let idx: number = 0;
                                                 (idx < vertices.length) && verticesMatchFileItems;
                                                 idx++) {

                                                let currentVertex: Vertex = vertices[idx];
                                                let valueCompoundId: GobiiFileItemCompoundId = GobiiFileItemCompoundId.fromGobiiFileItemCompoundId(
                                                    currentVertex
                                                ).setExtractorItemType(ExtractorItemType.VERTEX_VALUE);

                                                let selectedFileItems = fileItems.filter(gfi => gfi.compoundIdeEquals(valueCompoundId));
                                                if (selectedFileItems && selectedFileItems.length > 0) {
                                                    let itemIds: any[] = selectedFileItems
                                                        .map(gfi => Number(gfi.getItemId()));
                                                    verticesMatchFileItems = itemIds.length === currentVertex.filterVals.length && itemIds.every((v, i) => v === currentVertex.filterVals[i].id);
                                                }
                                            }

                                            if (verticesMatchFileItems) {
                                                gobiiDataSetExtracts.push(new GobiiDataSetExtract(gobiiFileType,
                                                    false,
                                                    null,
                                                    gobiiExtractFilterType,
                                                    null,
                                                    sampleList,
                                                    sampleFileName,
                                                    sampleListType,
                                                    datasetType,
                                                    platforms,
                                                    principleInvestigator,
                                                    project,
                                                    null,
                                                    null,
                                                    vertices));

                                                this.post(jobId, gobiiDataSetExtracts, submitterContactid, mapsetIds)
                                                    .subscribe(extractorInstructions => {
                                                        observer.next(extractorInstructions);
                                                        observer.complete();
                                                    });
                                            } else {
                                                this.store.dispatch(new historyAction.AddStatusMessageAction("The vertex filter values do not align with the selected vertex file items"));
                                                observer.complete();
                                            }
                                        } else {
                                            this.store.dispatch(new historyAction.AddStatusMessageAction("There are no vertex filters for this submission"));
                                            observer.complete();
                                        }
                                    })
                                    .unsubscribe(); // from get vertex filters

                            }
                            else {
                                this.store.dispatch(new historyAction.AddStatusMessageAction("Unhandled extract filter type: " + GobiiExtractFilterType[gobiiExtractFilterType]));
                                observer.complete();
                            }

                        }
                    ).unsubscribe(); // from select file items


            }
        )

    } // submit()


    private post(jobId: string,
                 gobiiDataSetExtracts: GobiiDataSetExtract[],
                 submitterContactId: number,
                 mapsetIds: number[]): Observable<GobiiExtractorInstruction> {

        // let extractorInstructionFilesDTORequest: ExtractorInstructionFilesDTO =
        //     new ExtractorInstructionFilesDTO(gobiiExtractorInstructions,
        //         jobId);

        //let extractorInstructionFilesDTOResponse: ExtractorInstructionFilesDTO = null;
        return Observable.create(observer => {

            this.dtoRequestServiceExtractorFile.post(new DtoRequestItemExtractorSubmission(
                new ExtractorInstructionFilesDTO([
                        new GobiiExtractorInstruction(
                            gobiiDataSetExtracts,
                            submitterContactId,
                            null,
                            mapsetIds)
                    ],
                    jobId)
            )).subscribe(extractorInstructionFilesDTOResponse => {


                if (extractorInstructionFilesDTOResponse.succeeded()) {
                    this.store.dispatch(new historyAction
                        .AddStatusMessageAction("Extractor instruction file created on server: "
                            + jobId));

                    observer.next(extractorInstructionFilesDTOResponse.getData().getGobiiExtractorInstructions());
                    observer.complete();
                } else {
                    this.store.dispatch(new historyAction.AddStatusMessageAction("Error submitting extract insturctions: " +
                        extractorInstructionFilesDTOResponse.getError()));
                }


            }); //
        }); // Observable.create()
    } // function()

}
