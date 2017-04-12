///<reference path="../../../../../../typings/index.d.ts"/>
import {Component, OnInit} from "@angular/core";
import {DtoRequestService} from "../services/core/dto-request.service";
import {GobiiDataSetExtract} from "../model/extractor-instructions/data-set-extract";
import {ProcessType} from "../model/type-process";
import {GobiiFileItem} from "../model/gobii-file-item";
import {ServerConfig} from "../model/server-config";
import {EntityType, EntitySubType} from "../model/type-entity";
import {NameId} from "../model/name-id";
import {GobiiFileType} from "../model/type-gobii-file";
import {ExtractorInstructionFilesDTO} from "../model/extractor-instructions/dto-extractor-instruction-files";
import {GobiiExtractorInstruction} from "../model/extractor-instructions/gobii-extractor-instruction";
import {DtoRequestItemExtractorSubmission} from "../services/app/dto-request-item-extractor-submission";
import {DtoRequestItemNameIds} from "../services/app/dto-request-item-nameids";
import {DtoRequestItemServerConfigs} from "../services/app/dto-request-item-serverconfigs";
import {EntityFilter} from "../model/type-entity-filter";
import {SampleMarkerList} from "../model/sample-marker-list";
import {GobiiExtractFilterType} from "../model/type-extractor-filter";
import {GobiiSampleListType} from "../model/type-extractor-sample-list";
import {CvFilters, CvFilterType} from "../model/cv-filter-type";
import {FileModelTreeService} from "../services/core/file-model-tree-service";
import {ExtractorItemType} from "../model/file-model-node";
import {DtoHeaderResponse} from "../model/dto-header-response";
import {GobiiExtractFormat} from "../model/type-extract-format";
import {FileModelState} from "../model/file-model-tree-event";
import forEach = require("core-js/fn/array/for-each");
import {platform} from "os";
import {Header} from "../model/payload/header";
import {HeaderStatusMessage} from "../model/dto-header-status-message";
import {NameIdRequestParams} from "../model/name-id-request-params";
import {FileName} from "../model/file_name";
import {Labels} from "../views/entity-labels";
import {TreeStatusNotification} from "../model/tree-status-notification";
import {Contact} from "../model/contact";
import {DtoRequestItemContact, ContactSearchType} from "../services/app/dto-request-item-contact";
import {AuthenticationService} from "../services/core/authentication.service";
import {FileItem} from "ng2-file-upload";
import {isNullOrUndefined} from "util";

// import { RouteConfig, ROUTER_DIRECTIVES, ROUTER_PROVIDERS } from 'angular2/router';

// GOBii Imports


@Component({
    selector: 'extractor-root',
    styleUrls: ['/extractor-ui.css'],
    template: `<div class = "panel panel-default">
        
           <div class = "panel-heading">
                <img src="images/gobii_logo.png" alt="GOBii Project"/>

                <fieldset class="well the-fieldset">
                    <div class="col-md-2">
                        <crops-list-box
                            [serverConfigList]="serverConfigList"
                            [selectedServerConfig]="selectedServerConfig"
                            (onServerSelected)="handleServerSelected($event)"></crops-list-box>
                    </div>
                    
                    <div class="col-md-3">
                       <export-type
                        (onExportTypeSelected)="handleExportTypeSelected($event)"></export-type>
                     </div>
                     
                    <div class="col-md-7">
                       <p style="text-align: right; font-weight: bold;">{{loggedInUser}}</p>
                     </div>
                     
                </fieldset>
           </div>
           
            <div class="container-fluid">
            
                <div class="row">
                
                    <div class="col-md-4">
                    
                     <fieldset class="well the-fieldset">
                     
                        <legend class="the-legend">Filters</legend><BR>
                        <div *ngIf="displaySelectorPi">
                            <label class="the-label">Principle Investigator:</label><BR>
                            <name-id-list-box
                                [gobiiExtractFilterType] = "gobiiExtractFilterType"
                                [nameIdRequestParams]="nameIdRequestParamsContactsPi"
                                [notifyOnInit]="!firstItemIsLabelPrincipleInvestigators"
                                [firstItemIsLabel]="firstItemIsLabelPrincipleInvestigators"
                                [doTreeNotifications] = "firstItemIsLabelPrincipleInvestigators"
                                (onNameIdSelected)="handleContactForPiSelected($event)"
                                (onError) = "handleHeaderStatusMessage($event)">
                            </name-id-list-box>
                            
                        </div>
                        
                        <div *ngIf="displaySelectorProject">
                            <BR>
                            <BR>
                            <label class="the-label">Project:</label><BR>
                            <project-list-box [primaryInvestigatorId] = "selectedContactIdForPi"
                                [gobiiExtractFilterType] = "gobiiExtractFilterType"
                                (onProjectSelected)="handleProjectSelected($event)"
                                (onAddHeaderStatus)="handleHeaderStatusMessage($event)"></project-list-box>
                        </div>

                        <div *ngIf="displaySelectorDataType">
                            <BR>
                            <BR>
                            <label class="the-label">Dataset Types:</label><BR>
                            <name-id-list-box
                                [gobiiExtractFilterType] = "gobiiExtractFilterType"
                                [notifyOnInit]="false"
                                [firstItemIsLabel] = "true"
                                [nameIdRequestParams]="nameIdRequestParamsDatasetType"
                                (onError) = "handleHeaderStatusMessage($event)">
                            </name-id-list-box>
                        </div>

                        
                        <div *ngIf="displaySelectorExperiment">
                            <BR>
                            <BR>
                            <label class="the-label">Experiment:</label><BR>
                               <name-id-list-box
                                [gobiiExtractFilterType] = "gobiiExtractFilterType"
                                [notifyOnInit]="true"
                                [doTreeNotifications]= "false"
                                [firstItemIsLabel] = "false"
                                [nameIdRequestParams]="nameIdRequestParamsExperiments"
                                (onNameIdSelected) = "handleExperimentSelected($event)"
                                (onError) = "handleHeaderStatusMessage($event)">
                            </name-id-list-box>
                            
                        </div>

                        <div *ngIf="displaySelectorPlatform">
                            <BR>
                            <BR>
                            <label class="the-label">Platforms:</label><BR>
                            <checklist-box
                                [nameIdRequestParams] = "nameIdRequestParamsPlatforms"
                                [gobiiExtractFilterType] = "gobiiExtractFilterType"
                                [retainHistory] = "false"
                                (onAddStatusMessage) = "handleHeaderStatusMessage($event)">
                            </checklist-box>
                         </div>


                        <div *ngIf="displayAvailableDatasets">
                            <BR>
                            <BR>
                            <label class="the-label">Data Sets</label><BR>
                            <dataset-checklist-box
                                [gobiiExtractFilterType] = "gobiiExtractFilterType"
                                [experimentId] = "selectedExperimentId" 
                                (onAddStatusMessage) = "handleHeaderStatusMessage($event)">
                            </dataset-checklist-box>
                        </div>
                    </fieldset>
                       
                       
                    </div>  <!-- outer grid column 1-->
                
                
                
                    <div class="col-md-4"> 

                        <div *ngIf="displaySampleListTypeSelector">
                            <fieldset class="well the-fieldset" style="vertical-align: bottom;">
                                <legend class="the-legend">Included Samples</legend>
                                <sample-list-type
                                    [gobiiExtractFilterType] = "gobiiExtractFilterType"
                                    (onHeaderStatusMessage)="handleHeaderStatusMessage($event)">
                                 </sample-list-type>
                                <hr style="width: 100%; color: black; height: 1px; background-color:black;" />
                                <sample-marker-box 
                                    [gobiiExtractFilterType] = "gobiiExtractFilterType"
                                    (onMarkerSamplesCompleted) = "handleSampleMarkerListComplete($event)"
                                    (onSampleMarkerError)="handleHeaderStatusMessage($event)">
                                </sample-marker-box>
                            </fieldset>
                        </div>
                        
                        <div *ngIf="displaySampleMarkerBox">
                            <fieldset class="well the-fieldset" style="vertical-align: bottom;">
                                <legend class="the-legend">Included Markers</legend>
                                <sample-marker-box 
                                    [gobiiExtractFilterType] = "gobiiExtractFilterType"
                                    (onMarkerSamplesCompleted) = "handleSampleMarkerListComplete($event)"
                                    (onSampleMarkerError)="handleHeaderStatusMessage($event)">
                                </sample-marker-box>
                            </fieldset>
                        </div>

                        
                        <form>
                           <fieldset class="well the-fieldset">
                                <legend class="the-legend">Extract</legend>
                           
                                <export-format
                                    [gobiiExtractFilterType] = "gobiiExtractFilterType"
                                    (onFormatSelected)="handleFormatSelected($event)"
                                ></export-format>
                                <BR>
                           
                                <name-id-list-box
                                    [gobiiExtractFilterType] = "gobiiExtractFilterType"
                                    [nameIdRequestParams]="nameIdRequestParamsMapsets"
                                    [firstItemIsLabel]="true"
                                    (onError) = "handleHeaderStatusMessage($event)">
                                </name-id-list-box>
                            </fieldset>
                        </form>
                        
                        
                    </div>  <!-- outer grid column 2-->
                    
                    
                    <div class="col-md-4">

                        <fieldset class="well the-fieldset" style="vertical-align: bottom;">
                            <legend class="the-legend">Extraction Criteria Summary</legend>
                            <status-display-tree
                                [fileItemEventChange] = "treeFileItemEvent"
                                [gobiiExtractFilterTypeEvent] = "gobiiExtractFilterType"
                                (onAddMessage)="handleHeaderStatusMessage($event)"
                                (onTreeReady)="handleStatusTreeReady($event)">
                            </status-display-tree>
                            
                            <BR>
                            
                            <button type="submit"
                            [ngStyle]="submitButtonStyle"
                            (mouseenter)="handleOnMouseOverSubmit($event,true)"
                            (mouseleave)="handleOnMouseOverSubmit($event,false)"
                            (click)="handleExtractSubmission()">Submit</button>
                               
                        </fieldset>
                            
                        <div>
                            <fieldset class="well the-fieldset" style="vertical-align: bottom;">
                                <legend class="the-legend">Status: {{currentStatus}}</legend>
                                <status-display [messages] = "messages"></status-display>
                            </fieldset>
                        </div>
                            
                                   
                    </div>  <!-- outer grid column 3 (inner grid)-->
                                        
                </div> <!-- .row of outer grid -->
                
                    <div class="row"><!-- begin .row 2 of outer grid-->
                        <div class="col-md-3"><!-- begin column 1 of outer grid -->
                         
                         </div><!-- end column 1 of outer grid -->
                    
                    </div><!-- end .row 2 of outer grid-->
                
            </div>` // end template
}) // @Component

export class ExtractorRoot implements OnInit {
    title = 'Gobii Web';


    // *** You cannot use an Enum directly as a template type parameter, so we need
    //     to assign them to properties
    private nameIdRequestParamsContactsPi: NameIdRequestParams;
    private nameIdRequestParamsExperiments: NameIdRequestParams;
    private nameIdRequestParamsMapsets: NameIdRequestParams;
    private nameIdRequestParamsDatasetType: NameIdRequestParams;
    private nameIdRequestParamsPlatforms: NameIdRequestParams;


    // ************************************************************************

    private treeFileItemEvent: GobiiFileItem;
//    private selectedExportTypeEvent:GobiiExtractFilterType;
    private datasetFileItemEvents: GobiiFileItem[] = [];
    private gobiiDatasetExtracts: GobiiDataSetExtract[] = [];

    private criteriaInvalid: boolean = true;

    private loggedInUser: string = null;


    private messages: string[] = [];


    constructor(private _dtoRequestServiceExtractorFile: DtoRequestService<ExtractorInstructionFilesDTO>,
                private _dtoRequestServiceNameIds: DtoRequestService<NameId[]>,
                private _dtoRequestServiceContact: DtoRequestService<Contact>,
                private _authenticationService: AuthenticationService,
                private _dtoRequestServiceServerConfigs: DtoRequestService<ServerConfig[]>,
                private _fileModelTreeService: FileModelTreeService) {

        this.nameIdRequestParamsContactsPi = NameIdRequestParams
            .build("Contact-PI",
                GobiiExtractFilterType.WHOLE_DATASET,
                EntityType.Contacts)
            .setEntitySubType(EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR);

        this.nameIdRequestParamsExperiments = NameIdRequestParams
            .build("Experiments",
                GobiiExtractFilterType.WHOLE_DATASET,
                EntityType.Experiments)
            .setEntityFilter(EntityFilter.BYTYPEID);

        this.nameIdRequestParamsDatasetType = NameIdRequestParams
            .build("Cv-DataType",
                GobiiExtractFilterType.WHOLE_DATASET,
                EntityType.CvTerms)
            .setCvFilterType(CvFilterType.DATASET_TYPE)
            .setEntityFilter(EntityFilter.BYTYPENAME)
            .setEntityFilterValue(CvFilters.get(CvFilterType.DATASET_TYPE));


        this.nameIdRequestParamsMapsets = NameIdRequestParams
            .build("Mapsets",
                GobiiExtractFilterType.WHOLE_DATASET,
                EntityType.Mapsets);

        this.nameIdRequestParamsPlatforms = NameIdRequestParams
            .build("Platforms",
                GobiiExtractFilterType.WHOLE_DATASET,
                EntityType.Platforms);

    }


    // ****************************************************************
    // ********************************************** SERVER SELECTION
    private selectedServerConfig: ServerConfig;
    private serverConfigList: ServerConfig[];
    private currentStatus: string;

    private initializeServerConfigs() {
        let scope$ = this;
        this._dtoRequestServiceServerConfigs.get(new DtoRequestItemServerConfigs()).subscribe(serverConfigs => {

                if (serverConfigs && ( serverConfigs.length > 0 )) {
                    scope$.serverConfigList = serverConfigs;

                    let serverCrop: String =
                        this._dtoRequestServiceServerConfigs.getGobiiCropType();

                    let gobiiVersion: string = this._dtoRequestServiceServerConfigs.getGobbiiVersion();

                    scope$.selectedServerConfig =
                        scope$.serverConfigList
                            .filter(c => {
                                    return c.crop === serverCrop;
                                }
                            )[0];

                    scope$.initializeSubmissionContact();
                    scope$.currentStatus = "GOBII Server " + gobiiVersion;
                    scope$.handleAddMessage("Connected to database: " + scope$.selectedServerConfig.crop);

                } else {
                    scope$.serverConfigList = [new ServerConfig("<ERROR NO SERVERS>", "<ERROR>", "<ERROR>", 0)];
                }
            },
            dtoHeaderResponse => {
                dtoHeaderResponse.statusMessages.forEach(m => scope$.handleAddMessage("Retrieving server configs: "
                    + m.message))
            }
        )
        ;
    } // initializeServerConfigs()

    private initializeSubmissionContact() {


        this.loggedInUser = this._authenticationService.getUserName();
        let scope$ = this;
        scope$._dtoRequestServiceContact.get(new DtoRequestItemContact(
            ContactSearchType.BY_USERNAME,
            this.loggedInUser)).subscribe(contact => {

                if (contact && contact.contactId && contact.contactId > 0) {

                    //loggedInUser
                    scope$._fileModelTreeService.put(
                        GobiiFileItem.build(scope$.gobiiExtractFilterType, ProcessType.CREATE)
                            .setEntityType(EntityType.Contacts)
                            .setEntitySubType(EntitySubType.CONTACT_SUBMITED_BY)
                            .setCvFilterType(CvFilterType.UNKNOWN)
                            .setExtractorItemType(ExtractorItemType.ENTITY)
                            .setItemName(contact.email)
                            .setItemId(contact.contactId.toLocaleString())).subscribe(
                        null,
                        headerStatusMessage => {
                            this.handleHeaderStatusMessage(headerStatusMessage)
                        }
                    );

                } else {
                    scope$.handleAddMessage("There is no contact associated with user " + this.loggedInUser);
                }

            },
            dtoHeaderResponse => {
                dtoHeaderResponse.statusMessages.forEach(m => scope$.handleAddMessage("Retrieving contacts for submission: "
                    + m.message))
            });

        //   _dtoRequestServiceContact
    }

    private handleServerSelected(arg) {
        this.selectedServerConfig = arg;
        // this._dtoRequestServiceNameIds
        //     .setCropType(GobiiCropType[this.selectedServerConfig.crop]);
        let currentPath = window.location.pathname;
        let currentPage: string = currentPath.substr(currentPath.lastIndexOf('/') + 1, currentPath.length);
        let newDestination = "http://"
            + this.selectedServerConfig.domain
            + ":"
            + this.selectedServerConfig.port
            + this.selectedServerConfig.contextRoot
            + currentPage;
//        console.log(newDestination);
        window.location.href = newDestination;
    } // handleServerSelected()


// ********************************************************************
// ********************************************** EXPORT TYPE SELECTION AND FLAGS


    private displayAvailableDatasets: boolean = true;
    private displaySelectorPi: boolean = true;
    private firstItemIsLabelPrincipleInvestigators: boolean = false;
    private displaySelectorProject: boolean = true;
    private displaySelectorExperiment: boolean = true;
    private displaySelectorDataType: boolean = false;
    private displaySelectorPlatform: boolean = false;
    private displayIncludedDatasetsGrid: boolean = true;
    private displaySampleListTypeSelector: boolean = false;
    private displaySampleMarkerBox: boolean = false;
    private gobiiExtractFilterType: GobiiExtractFilterType;

    private handleExportTypeSelected(arg: GobiiExtractFilterType) {

        let foo: string = "foo";


        this._fileModelTreeService
            .fileItemNotifications()
            .subscribe(fileItem => {
                if (fileItem.getProcessType() === ProcessType.NOTIFY
                    && fileItem.getExtractorItemType() === ExtractorItemType.STATUS_DISPLAY_TREE_READY) {

                    let jobId: string = FileName.makeUniqueFileId();

                    this._fileModelTreeService
                        .put(GobiiFileItem
                            .build(arg, ProcessType.CREATE)
                            .setExtractorItemType(ExtractorItemType.JOB_ID)
                            .setItemId(jobId)
                            .setItemName(jobId))
                        .subscribe(
                            fmte => {
                                this._fileModelTreeService
                                    .getTreeState(this.gobiiExtractFilterType)
                                    .subscribe(
                                        ts => {
                                            this.handleTreeStatusChanged(ts)
                                        },
                                        hsm => {
                                            this.handleHeaderStatusMessage(hsm)
                                        }
                                    )
                            },
                            headerStatusMessage => {
                                this.handleHeaderStatusMessage(headerStatusMessage)
                            }
                        );
                }
            });

        this.gobiiExtractFilterType = arg;

//        let extractorFilterItemType: GobiiFileItem = GobiiFileItem.bui(this.gobiiExtractFilterType)

        if (this.gobiiExtractFilterType === GobiiExtractFilterType.WHOLE_DATASET) {

            this.firstItemIsLabelPrincipleInvestigators = false;
            this.displaySelectorPi = true;
            this.displaySelectorProject = true;
            this.displaySelectorExperiment = true;
            this.displayAvailableDatasets = true;
            this.displayIncludedDatasetsGrid = true;

            this.displaySelectorDataType = false;
            this.displaySelectorPlatform = false;
            this.displaySampleListTypeSelector = false;
            this.displaySampleMarkerBox = false;


        } else if (this.gobiiExtractFilterType === GobiiExtractFilterType.BY_SAMPLE) {

//            this.initializePlatforms();

            this.displaySelectorPi = true;
            this.firstItemIsLabelPrincipleInvestigators = true;
            this.displaySelectorProject = true;
            this.displaySelectorDataType = true;
            this.displaySelectorPlatform = true;
            this.displaySampleListTypeSelector = true;

            this.displaySelectorExperiment = false;
            this.displayAvailableDatasets = false;
            this.displayIncludedDatasetsGrid = false;
            this.displaySampleMarkerBox = false;


        } else if (this.gobiiExtractFilterType === GobiiExtractFilterType.BY_MARKER) {

//            this.initializePlatforms();

            this.displaySelectorDataType = true;
            this.displaySelectorPlatform = true;
            this.displaySampleMarkerBox = true;

            this.displaySelectorPi = false;
            this.firstItemIsLabelPrincipleInvestigators = false;
            this.displaySelectorProject = false;
            this.displaySelectorExperiment = false;
            this.displayAvailableDatasets = false;
            this.displayIncludedDatasetsGrid = false;
            this.displaySampleListTypeSelector = false;

        }

        this.initializeSubmissionContact();

        //changing modes will have nuked the submit as item in the tree, so we need to re-event (sic.) it:


    }


// ********************************************************************
// ********************************************** PI USER SELECTION
    private selectedContactIdForPi: string;

    private handleContactForPiSelected(arg) {
        this.selectedContactIdForPi = arg.id;
        //console.log("selected contact itemId:" + arg);
    }

// ********************************************************************
// ********************************************** HAPMAP SELECTION
    private selectedExtractFormat: GobiiExtractFormat = GobiiExtractFormat.HAPMAP;

    private handleFormatSelected(arg: GobiiExtractFormat) {

        this.selectedExtractFormat = arg;

        let extractFilterTypeFileItem: GobiiFileItem = GobiiFileItem
            .build(this.gobiiExtractFilterType, ProcessType.UPDATE)
            .setExtractorItemType(ExtractorItemType.EXPORT_FORMAT)
            .setItemId(GobiiExtractFormat[arg])
            .setItemName(GobiiExtractFormat[GobiiExtractFormat[arg]]);

        this._fileModelTreeService.put(extractFilterTypeFileItem)
            .subscribe(
                null,
                headerResponse => {
                    this.handleResponseHeader(headerResponse)
                });

        //console.log("selected contact itemId:" + arg);
    }

// ********************************************************************
// ********************************************** PROJECT ID
    private selectedProjectId: string;

    private handleProjectSelected(arg) {
        this.selectedProjectId = arg;
        this.displayExperimentDetail = false;
        this.displayDataSetDetail = false;
        this.nameIdRequestParamsExperiments.setEntityFilterValue(this.selectedProjectId);
    }


// ********************************************************************
// ********************************************** EXPERIMENT ID
    private displayExperimentDetail: boolean = false;

    private experimentNameIdList: NameId[];
    private selectedExperimentId: string;
    private selectedExperimentDetailId: string;

    private handleExperimentSelected(arg: NameId) {
        this.selectedExperimentId = arg.id;
        this.selectedExperimentDetailId = arg.id;
        this.displayExperimentDetail = true;

        //console.log("selected contact itemId:" + arg);
    }


// ********************************************************************
// ********************************************** PLATFORM SELECTION
//     private platformsNameIdList: NameId[];
//     private selectedPlatformId: string;
//
//     private handlePlatformSelected(arg) {
//         this.selectedPlatformId = arg.id;
//     }
//
//     private handlePlatformChecked(fileItemEvent: GobiiFileItem) {
//
//
//         this._fileModelTreeService.put(fileItemEvent).subscribe(
//             null,
//             headerResponse => {
//                 this.handleHeaderStatusMessage(headerResponse)
//             });
//
//     }
//
//     private initializePlatforms() {
//         let scope$ = this;
//         scope$._dtoRequestServiceNameIds.get(new DtoRequestItemNameIds(
//             EntityType.Platforms,
//             EntityFilter.NONE)).subscribe(nameIds => {
//
//                 if (nameIds && ( nameIds.length > 0 )) {
//                     scope$.platformsNameIdList = nameIds;
//                     scope$.selectedPlatformId = scope$.platformsNameIdList[0].id;
//                 } else {
//                     scope$.platformsNameIdList = [new NameId("0", "ERROR NO PLATFORMS", EntityType.Platforms)];
//                 }
//             },
//             dtoHeaderResponse => {
//                 dtoHeaderResponse.statusMessages.forEach(m => scope$.messages.push("Retrieving PlatformTypes: "
//                     + m.message))
//             });
//     }

// ********************************************************************
// ********************************************** DATASET ID
    private displayDataSetDetail: boolean = false;
    private dataSetIdToUncheck: number;

    private handleAddMessage(arg) {
        this.messages.unshift(arg);
    }


    private handleHeaderStatusMessage(statusMessage: HeaderStatusMessage) {

        this.handleAddMessage(statusMessage.message);
    }

    private handleResponseHeader(header: Header) {

        if (header.status !== null && header.status.statusMessages != null) {

            header.status.statusMessages.forEach(statusMessage => {
                this.handleHeaderStatusMessage(statusMessage);
            })
        }
    }

    handleStatusTreeReady(headerStatusMessage: HeaderStatusMessage) {

        //this.handleFormatSelected(GobiiExtractFormat.HAPMAP);

    }

    private makeDatasetExtract() {

        this.gobiiDatasetExtracts.push(new GobiiDataSetExtract(GobiiFileType.GENERIC,
            false,
            Number(this.selectedDatasetId),
            this.selectedDatasetName,
            null,
            this.gobiiExtractFilterType,
            this.markerList,
            this.sampleList,
            this.uploadFileName,
            null,
            null,
            null));

    }


    private selectedDatasetId: string;
    private selectedDatasetName: string;


// ********************************************************************
// ********************************************** MARKER/SAMPLE selection
    private markerList: string[] = null;
    private sampleList: string[] = null;
    private uploadFileName: string = null;

    private handleSampleMarkerListComplete(arg: SampleMarkerList) {

        let sampleMarkerList: SampleMarkerList = arg;


        if (sampleMarkerList.isArray) {
            if (this.gobiiExtractFilterType === GobiiExtractFilterType.BY_SAMPLE) {
                this.sampleList = sampleMarkerList.items;

            } else if (this.gobiiExtractFilterType === GobiiExtractFilterType.BY_MARKER) {
                this.markerList = sampleMarkerList.items;
            }
        } else {
            this.uploadFileName = sampleMarkerList.uploadFileName;
        }

        this.makeDatasetExtract();
    }


    // ********************************************************************
    // ********************************************** Extract file submission
    private treeStatusNotification: TreeStatusNotification = null;
    private submitButtonStyleDefault = {'background-color': '#eee'};
    private buttonStyleSubmitReady = {'background-color': '#99e699'};
    private buttonStyleSubmitNotReady = {'background-color': '#ffad99'};
    private submitButtonStyle = this.buttonStyleSubmitNotReady;

    private handleTreeStatusChanged(treeStatusNotification: TreeStatusNotification) {

        if (treeStatusNotification.gobiiExractFilterType === this.gobiiExtractFilterType) {
            this.treeStatusNotification = treeStatusNotification;
            this.setSubmitButtonState();
        } // does the filter type match
    }


    private setSubmitButtonState(): boolean {

        let returnVal: boolean = false;

        if (this.treeStatusNotification.fileModelState == FileModelState.SUBMISSION_READY) {
            this.submitButtonStyle = this.buttonStyleSubmitReady;
            returnVal = true;
        } else {
            this.submitButtonStyle = this.buttonStyleSubmitNotReady;
            returnVal = false;

        }

        return returnVal;
    }

    private handleOnMouseOverSubmit(arg, isEnter) {

        // this.criteriaInvalid = true;

        if (isEnter) {

            this.setSubmitButtonState()

            this.treeStatusNotification.modelTreeValidationErrors.forEach(mtv => {

                let currentMessage: string;

                if (mtv.fileModelNode.getItemType() === ExtractorItemType.ENTITY) {
                    currentMessage = mtv.fileModelNode.getEntityName();

                } else {
                    currentMessage = Labels.instance().treeExtractorTypeLabels[mtv.fileModelNode.getItemType()];
                }

                currentMessage += ": " + mtv.message;

                this.handleAddMessage(currentMessage);

            });
        }


        // else {
        //     this.submitButtonStyle = this.submitButtonStyleDefault;
        // }

        //#eee


        let foo: string = "foo";
    }

    private handleExtractSubmission() {


        if (this.setSubmitButtonState()) {

            let scope$ = this;

            let gobiiExtractorInstructions: GobiiExtractorInstruction[] = [];
            let gobiiDataSetExtracts: GobiiDataSetExtract[] = [];
            let mapsetIds: number[] = [];
            let submitterContactid: number = null;
            let jobId: string = null;
            let markerFileName: string = null;
            let sampleFileName: string = null;
            let sampleListType: GobiiSampleListType;
            scope$._fileModelTreeService.getFileItems(scope$.gobiiExtractFilterType).subscribe(
                fileItems => {

                    let fileItemJobId: GobiiFileItem = fileItems.find(item => {
                        return item.getExtractorItemType() === ExtractorItemType.JOB_ID
                    });

                    if (fileItemJobId != null) {
                        jobId = fileItemJobId.getItemId();
                    }

                    let fileItemMarkerFile: GobiiFileItem = fileItems.find(item => {
                        return item.getExtractorItemType() === ExtractorItemType.MARKER_FILE
                    });

                    if (fileItemMarkerFile != null) {
                        markerFileName = fileItemMarkerFile.getItemId();
                    }

                    let fileItemSampleFile: GobiiFileItem = fileItems.find(item => {
                        return item.getExtractorItemType() === ExtractorItemType.SAMPLE_FILE
                    });

                    if (fileItemSampleFile != null) {
                        sampleFileName = fileItemSampleFile.getItemId();
                    }

                    let submitterFileItem: GobiiFileItem = fileItems.find(item => {
                        return (item.getEntityType() === EntityType.Contacts)
                            && (item.getEntitySubType() === EntitySubType.CONTACT_SUBMITED_BY)
                    });

                    submitterContactid = Number(submitterFileItem.getItemId());


                    mapsetIds = fileItems
                        .filter(item => {
                            return item.getEntityType() === EntityType.Mapsets
                        })
                        .map(item => {
                            return Number(item.getItemId())
                        });

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


                    let dataTypeFileItem: GobiiFileItem = fileItems.find(item => {
                        return item.getEntityType() === EntityType.CvTerms
                            && item.getCvFilterType() === CvFilterType.DATASET_TYPE
                    });

                    let datasetType: NameId = dataTypeFileItem != null ? new NameId(dataTypeFileItem.getItemId(),
                            dataTypeFileItem.getItemName(),EntityType.CvTerms) : null;

                    let platformFileItems: GobiiFileItem[] = fileItems.filter(item => {
                        return item.getEntityType() === EntityType.Platforms
                    });

                    let platformIds: number[] = platformFileItems.map(item => {
                        return Number(item.getItemId())
                    });

                    let markerList: string[] =
                        fileItems
                            .filter(fi => {
                                return fi.getExtractorItemType() === ExtractorItemType.MARKER_LIST_ITEM
                            })
                            .map(mi => {
                                return mi.getItemId()
                            });

                    let sampleList: string[] =
                        fileItems
                            .filter(fi => {
                                return fi.getExtractorItemType() === ExtractorItemType.SAMPLE_LIST_ITEM
                            })
                            .map(mi => {
                                return mi.getItemId()
                            });

                    let sampleListTypeFileItem: GobiiFileItem = fileItems.find(item => {
                        return item.getExtractorItemType() === ExtractorItemType.SAMPLE_LIST_TYPE;
                    });

                    if (sampleListTypeFileItem != null) {
                        sampleListType = GobiiSampleListType[sampleListTypeFileItem.getItemId()];
                    }

                    if (this.gobiiExtractFilterType === GobiiExtractFilterType.WHOLE_DATASET) {

                        fileItems
                            .filter(item => {
                                return item.getEntityType() === EntityType.DataSets
                            })
                            .forEach(datsetFileItem => {

                                gobiiDataSetExtracts.push(new GobiiDataSetExtract(gobiiFileType,
                                    false,
                                    Number(datsetFileItem.getItemId()),
                                    datsetFileItem.getItemName(),
                                    null,
                                    this.gobiiExtractFilterType,
                                    null,
                                    null,
                                    markerFileName,
                                    null,
                                    datasetType,
                                    platformIds));
                            });
                    } else if (this.gobiiExtractFilterType === GobiiExtractFilterType.BY_MARKER) {
                        gobiiDataSetExtracts.push(new GobiiDataSetExtract(gobiiFileType,
                            false,
                            null,
                            null,
                            null,
                            this.gobiiExtractFilterType,
                            markerList,
                            null,
                            markerFileName,
                            null,
                            datasetType,
                            platformIds));
                    } else if (this.gobiiExtractFilterType === GobiiExtractFilterType.BY_SAMPLE) {
                        gobiiDataSetExtracts.push(new GobiiDataSetExtract(gobiiFileType,
                            false,
                            null,
                            null,
                            null,
                            this.gobiiExtractFilterType,
                            null,
                            sampleList,
                            sampleFileName,
                            sampleListType,
                            datasetType,
                            platformIds));
                    } else {
                        this.handleAddMessage("Unhandled extract filter type: " + GobiiExtractFilterType[this.gobiiExtractFilterType]);
                    }
                }
            );


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

            this._dtoRequestServiceExtractorFile.post(new DtoRequestItemExtractorSubmission(extractorInstructionFilesDTORequest))
                .subscribe(extractorInstructionFilesDTO => {
                        extractorInstructionFilesDTOResponse = extractorInstructionFilesDTO;
                        scope$.handleAddMessage("Extractor instruction file created on server: "
                            + extractorInstructionFilesDTOResponse.getInstructionFileName());

                        let newJobId: string = FileName.makeUniqueFileId();
                        this._fileModelTreeService
                            .put(GobiiFileItem
                                .build(this.gobiiExtractFilterType, ProcessType.CREATE)
                                .setExtractorItemType(ExtractorItemType.JOB_ID)
                                .setItemId(newJobId)
                                .setItemName(newJobId))
                            .subscribe(
                                null,
                                headerStatusMessage => {
                                    this.handleHeaderStatusMessage(headerStatusMessage)
                                }
                            );
                    },
                    headerResponse => {

                        scope$.handleResponseHeader(headerResponse);
                    });

        } // if submission state is READY

    }

    ngOnInit(): any {

        this._fileModelTreeService
            .treeStateNotifications()
            .subscribe(ts => {

                this.handleTreeStatusChanged(ts);
            });

        this.initializeServerConfigs();
        this.handleExportTypeSelected(GobiiExtractFilterType.WHOLE_DATASET);

    } // ngOnInit()


}

