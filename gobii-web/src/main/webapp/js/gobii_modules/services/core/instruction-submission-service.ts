import {Injectable} from "@angular/core";
import {EntitySubType, EntityType} from "../../model/type-entity";
import {ExtractorItemType} from "../../model/type-extractor-item";
import {GobiiExtractFilterType} from "../../model/type-extractor-filter";
import {CvFilterType} from "../../model/cv-filter-type";
import {GobiiFileItem} from "../../model/gobii-file-item";
import {GobiiExtractFormat} from "../../model/type-extract-format";
import * as fromRoot from '../../store/reducers';
import * as historyAction from '../../store/actions/history-action';
import * as fromTreeNodeActions from '../../store/actions/treenode-action';

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
import {TypeTreeNodeStatus} from "../../model/type-tree-node-status";
import {GobiiFileItemCriterion} from "../../model/gobii-file-item-criterion";
import {TreeStructureService} from "./tree-structure-service";

@Injectable()
export class InstructionSubmissionService {


    private datasetCriterion: GobiiFileItemCriterion = new GobiiFileItemCriterion(new GobiiFileItemCompoundId(
        ExtractorItemType.ENTITY,
        EntityType.DataSet,
        EntitySubType.UNKNOWN,
        CvFilterType.UNKNOWN
    ), false);

    private sampleItemCriterion: GobiiFileItemCriterion = new GobiiFileItemCriterion(new GobiiFileItemCompoundId(
        ExtractorItemType.SAMPLE_LIST_ITEM,
        EntityType.UNKNOWN,
        EntitySubType.UNKNOWN,
        CvFilterType.UNKNOWN
    ), false);

    private samplefileCriterion: GobiiFileItemCriterion = new GobiiFileItemCriterion(new GobiiFileItemCompoundId(
        ExtractorItemType.SAMPLE_FILE,
        EntityType.UNKNOWN,
        EntitySubType.UNKNOWN,
        CvFilterType.UNKNOWN
    ), false);

    private piContactCriterion: GobiiFileItemCriterion = new GobiiFileItemCriterion(new GobiiFileItemCompoundId(
        ExtractorItemType.ENTITY,
        EntityType.Contact,
        EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR,
        CvFilterType.UNKNOWN
    ), false);

    private projectsCriterion: GobiiFileItemCriterion = new GobiiFileItemCriterion(new GobiiFileItemCompoundId(
        ExtractorItemType.ENTITY,
        EntityType.Project,
        EntitySubType.UNKNOWN,
        CvFilterType.UNKNOWN
    ), false);

    private datasetTypesCriterion: GobiiFileItemCriterion = new GobiiFileItemCriterion(new GobiiFileItemCompoundId(
        ExtractorItemType.ENTITY,
        EntityType.Cv,
        EntitySubType.UNKNOWN,
        CvFilterType.DATASET_TYPE
    ), false);


    private markerListItemCriterion: GobiiFileItemCriterion = new GobiiFileItemCriterion(new GobiiFileItemCompoundId(
        ExtractorItemType.MARKER_LIST_ITEM,
        EntityType.UNKNOWN,
        EntitySubType.UNKNOWN,
        CvFilterType.UNKNOWN
    ), false);


    private markerListFileCriterion: GobiiFileItemCriterion = new GobiiFileItemCriterion(new GobiiFileItemCompoundId(
        ExtractorItemType.MARKER_FILE,
        EntityType.UNKNOWN,
        EntitySubType.UNKNOWN,
        CvFilterType.UNKNOWN
    ), false);


    private markergGroupCriterion: GobiiFileItemCriterion = new GobiiFileItemCriterion(new GobiiFileItemCompoundId(
        ExtractorItemType.ENTITY,
        EntityType.Marker_Group,
        EntitySubType.UNKNOWN,
        CvFilterType.UNKNOWN
    ), false);

    private platformCriterion: GobiiFileItemCriterion = new GobiiFileItemCriterion(new GobiiFileItemCompoundId(
        ExtractorItemType.ENTITY,
        EntityType.Platform,
        EntitySubType.UNKNOWN,
        CvFilterType.UNKNOWN
    ), false);


    constructor(private store: Store<fromRoot.State>,
                private dtoRequestServiceExtractorFile: DtoRequestService<ExtractorInstructionFilesDTO>,
                private treeStructureService: TreeStructureService) {


    }

    private isItemPresent(gobiiFileItems: GobiiFileItem[], gobiiFileItemCriterion: GobiiFileItemCriterion) {

        return gobiiFileItems.filter(fi => gobiiFileItemCriterion.compoundIdeEquals(fi)).length > 0;

    }

    public unmarkMissingItems(gobiiExtractFilterType: GobiiExtractFilterType) {
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

                } else {
                    this.store.dispatch(new historyAction.AddStatusMessageAction("Unhandled extract filter type: " + GobiiExtractFilterType[gobiiExtractFilterType]));

                }
            });

    }

    public markMissingItems(gobiiExtractFilterType: GobiiExtractFilterType) {

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

                    } else {
                        this.store.dispatch(new historyAction.AddStatusMessageAction("Unhandled extract filter type: " + GobiiExtractFilterType[gobiiExtractFilterType]));

                    }

                }
            });


    } // markMissingItems()

    private areCriteriaMet(all: GobiiFileItem[], gobiiExtractFilterType: GobiiExtractFilterType): boolean {

        let returnVal: boolean;
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
                ( samplesArePresent
                    || projectIsPresent
                    || pIIsPresent );

        } else if (gobiiExtractFilterType === GobiiExtractFilterType.BY_MARKER) {

            let markersArePresent: boolean = this.isItemPresent(all, this.markerListItemCriterion)
                || this.isItemPresent(all, this.markerListFileCriterion);
            let markerGroupIsPresent: boolean = this.isItemPresent(all, this.markergGroupCriterion);
            let platformIsPresent: boolean = this.isItemPresent(all, this.platformCriterion);
            let datasetTypeIsPresent: boolean = this.isItemPresent(all, this.datasetTypesCriterion);


            returnVal =
                datasetTypeIsPresent
                && ( markersArePresent
                || markerGroupIsPresent
                || platformIsPresent );

        } else {

            this.store.dispatch(new historyAction.AddStatusMessageAction("Unhandled extract filter type: " + GobiiExtractFilterType[gobiiExtractFilterType]));

        }

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

            let gobiiExtractorInstructions: GobiiExtractorInstruction[] = [];
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
                            return (item.getEntityType() === EntityType.Contact)
                                && (item.getEntitySubType() === EntitySubType.CONTACT_SUBMITED_BY)
                        });

                        submitterContactid = Number(submitterFileItem.getItemId());


                        // ******** MAPSET IDs
                        let mapsetFileItems: GobiiFileItem[] = fileItems
                            .filter(item => {
                                return item.getEntityType() === EntityType.Mapset
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
                            return item.getEntityType() === EntityType.Cv
                                && item.getCvFilterType() === CvFilterType.DATASET_TYPE
                        });
                        let datasetType: NameId = dataTypeFileItem != null ? new NameId(dataTypeFileItem.getItemId(),
                            dataTypeFileItem.getItemName(), EntityType.Cv) : null;


                        // ******** PRINCIPLE INVESTIGATOR CONCEPT
                        let principleInvestigatorFileItem: GobiiFileItem = fileItems.find(item => {
                            return item.getEntityType() === EntityType.Contact
                                && item.getEntitySubType() === EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR
                        });
                        let principleInvestigator: NameId = principleInvestigatorFileItem != null ? new NameId(principleInvestigatorFileItem.getItemId(),
                            principleInvestigatorFileItem.getItemName(), EntityType.Contact) : null;


                        // ******** PROJECT
                        let projectFileItem: GobiiFileItem = fileItems.find(item => {
                            return item.getEntityType() === EntityType.Project
                        });
                        let project: NameId = projectFileItem != null ? new NameId(projectFileItem.getItemId(),
                            projectFileItem.getItemName(), EntityType.Project) : null;


                        // ******** PLATFORM
                        let platformFileItems: GobiiFileItem[] = fileItems.filter(item => {
                            return item.getEntityType() === EntityType.Platform
                        });

                        let platforms: NameId[] = platformFileItems.map(item => {
                            return new NameId(item.getItemId(), item.getItemName(), EntityType.Platform)
                        });

                        let markerGroupItems: GobiiFileItem[] = fileItems.filter(item => {
                            return item.getEntityType() === EntityType.Marker_Group
                        });

                        let markerGroups: NameId[] = markerGroupItems.map(item => {
                            return new NameId(item.getItemId(), item.getItemName(), EntityType.Marker_Group)
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

                        if (gobiiExtractFilterType === GobiiExtractFilterType.WHOLE_DATASET) {

                            let dataSetItems: GobiiFileItem[] = fileItems
                                .filter(item => {
                                    return item.getEntityType() === EntityType.DataSet
                                });


                            dataSetItems.forEach(datsetFileItem => {

                                let dataSet: NameId = new NameId(datsetFileItem.getItemId(),
                                    datsetFileItem.getItemName(), EntityType.Cv);


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
                                    null));
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
                                markerGroups));
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
                                null));
                        } else {
                            this.store.dispatch(new historyAction.AddStatusMessageAction("Unhandled extract filter type: " + GobiiExtractFilterType[gobiiExtractFilterType]));
                        }
                    }
                ).unsubscribe();


            gobiiExtractorInstructions.push(
                new GobiiExtractorInstruction(
                    gobiiDataSetExtracts,
                    submitterContactid,
                    null,
                    mapsetIds)
            );


            let fileName: string = jobId;

            let extractorInstructionFilesDTORequest: ExtractorInstructionFilesDTO =
                new ExtractorInstructionFilesDTO(gobiiExtractorInstructions,
                    fileName);

            let extractorInstructionFilesDTOResponse: ExtractorInstructionFilesDTO = null;

            this.dtoRequestServiceExtractorFile.post(new DtoRequestItemExtractorSubmission(extractorInstructionFilesDTORequest))
                .subscribe(extractorInstructionFilesDTO => {
                        extractorInstructionFilesDTOResponse = extractorInstructionFilesDTO;
                        this.store.dispatch(new historyAction
                            .AddStatusMessageAction("Extractor instruction file created on server: "
                                + extractorInstructionFilesDTOResponse.getInstructionFileName()));

                        observer.next(extractorInstructionFilesDTORequest.getGobiiExtractorInstructions());
                        observer.complete();
                    },
                    headerResponse => {
                        headerResponse.status.statusMessages.forEach(statusMessage => {
                            this.store.dispatch(new historyAction.AddStatusAction(statusMessage));
                        });
                        observer.complete();
                    });

        });//return observer create
    }


}
