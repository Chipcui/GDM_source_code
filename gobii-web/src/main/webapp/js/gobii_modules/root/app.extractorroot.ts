////<reference path="../../../../../../typings/index.d.ts"/>
import {ChangeDetectorRef, Component, OnInit} from "@angular/core";
import {DtoRequestService} from "../services/core/dto-request.service";
import {ProcessType} from "../model/type-process";
import {GobiiFileItem} from "../model/gobii-file-item";
import {ServerConfig} from "../model/server-config";
import {EntitySubType, EntityType} from "../model/type-entity";
import {DtoRequestItemServerConfigs} from "../services/app/dto-request-item-serverconfigs";
import {GobiiExtractFilterType} from "../model/type-extractor-filter";
import {CvGroup} from "../model/cv-group";
import {ExtractorItemType} from "../model/type-extractor-item";
import {GobiiExtractFormat} from "../model/type-extract-format";
import {HeaderStatusMessage} from "../model/dto-header-status-message";
import {FileName} from "../model/file_name";
import {Contact} from "../model/contact";
import {ContactSearchType, DtoRequestItemContact} from "../services/app/dto-request-item-contact";
import {AuthenticationService} from "../services/core/authentication.service";
import {StatusLevel} from "../model/type-status-level";
import {Store} from "@ngrx/store";
import * as fromRoot from '../store/reducers';
import * as fileItemAction from '../store/actions/fileitem-action';
import * as historyAction from '../store/actions/history-action';
import {FilterParamNames} from "../model/file-item-param-names";
import {NameIdFileItemService} from "../services/core/nameid-file-item-service";
import {Observable} from "rxjs/Observable";
import {InstructionSubmissionService} from "../services/core/instruction-submission-service";
import {GobiiSampleListType} from "../model/type-extractor-sample-list";
import {EntityFileItemService} from "../services/core/entity-file-item-service";
import {FilterService} from "../services/core/filter-service";
import {FlexQueryService} from "../services/core/flex-query-service";

// import { RouteConfig, ROUTER_DIRECTIVES, ROUTER_PROVIDERS } from 'angular2/router';

// GOBii Imports


@Component({
    selector: 'extractor-root',
    styleUrls: ['/extractor-ui.css'],
    template: `
        <BR>
        <BR>
        <div class="panel panel-default col-md-6 col-md-offset-0" style="width: 95%">
            <div class="panel-heading">
                <img src="images/gobii_logo.png" alt="GOBii Project"/>
            </div> <!-- panel heading -->

            <div class="panel panel-primary">
                <div class="panel-heading">
                    <h3 class="panel-title">{{currentStatus}}
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        {{loggedInUser}}</h3>
                </div> <!-- panel heading  -->

                <BR>
                <BR>
                <div class="container-fluid">

                    <div class="row">

                        <div class="col-md-8"> <!-- outer grid column 1: Dataset, Sample, Marker Filtering-->

                            <p-panel header="Extract Filtering">
                                <!--<p-tabView [style]="{'border': '1px solid #336699', 'padding-left': '5px'}">-->
                                <p-tabView
                                        (onChange)="handleTabPanelChange($event)"
                                        styleClass="ui-tabview-panels">
                                    <p-tabPanel header="By Dataset">
                                        <ng-template pTemplate="content"> <!-- lazy-load controls -->
                                            <div class="container-fluid">
                                                <div class="row">
                                                    <div class="col-md-12"> <!-- inner column 2 of main column 1 -->
                                                        <div class="panel panel-primary">
                                                            <div class="panel-heading">
                                                                <h3 class="panel-title">Datasets</h3>
                                                            </div>

                                                            <div class="panel-body">

                                                                <table class="table">
                                                                    <tbody>
                                                                    <tr>
                                                                        <td>
                                                                            <label class="the-label">Principle
                                                                                Investigator:</label><BR>
                                                                            <name-id-list-box
                                                                                    [gobiiExtractFilterType]="gobiiExtractFilterType"
                                                                                    [filterParamName]="nameIdFilterParamTypes.CONTACT_PI_FILTER_OPTIONAL">
                                                                            </name-id-list-box>
                                                                        </td>
                                                                        <td>
                                                                            <label class="the-label">Project:</label><BR>
                                                                            <name-id-list-box
                                                                                    [gobiiExtractFilterType]="gobiiExtractFilterType"
                                                                                    [filterParamName]="nameIdFilterParamTypes.PROJECT_FILTER_OPTIONAL">
                                                                            </name-id-list-box>
                                                                        </td>
                                                                        <td>
                                                                            <label class="the-label">Experiment:</label><BR>
                                                                            <name-id-list-box
                                                                                    [gobiiExtractFilterType]="gobiiExtractFilterType"
                                                                                    [filterParamName]="nameIdFilterParamTypes.EXPERIMENT_FILTER_OPTIONAL">
                                                                            </name-id-list-box>
                                                                        </td>
                                                                    </tr>
                                                                    </tbody>
                                                                </table>

                                                                <dataset-datatable
                                                                        [gobiiExtractFilterType]="gobiiExtractFilterType">
                                                                </dataset-datatable>
                                                            </div> <!-- panel body dataset datatable -->
                                                        </div> <!-- panel primary dataset datatable -->
                                                    </div> <!-- data table column -->
                                                </div> <!-- ROW  -->
                                            </div> <!-- container  -->
                                        </ng-template> <!-- lazy-load controls -->
                                    </p-tabPanel> <!-- tab panel -- dataset -->
                                    <p-tabPanel header="By Samples">
                                        <ng-template pTemplate="content"> <!-- lazy-load controls -->
                                            <div class="container-fluid">
                                                <div class="row">
                                                    <div class="col-md-4"> <!-- inner column 1 of main column 1 -->
                                                        <div class="panel panel-primary">
                                                            <div class="panel-heading">
                                                                <h3 class="panel-title">Filters</h3>
                                                            </div>
                                                            <div class="panel-body">
                                                                <label class="the-label">Principle Investigator:</label><BR>
                                                                <name-id-list-box
                                                                        [gobiiExtractFilterType]="gobiiExtractFilterType"
                                                                        [filterParamName]="nameIdFilterParamTypes.CONTACT_PI_HIERARCHY_ROOT">
                                                                </name-id-list-box>

                                                                <BR>
                                                                <BR>
                                                                <label class="the-label">Projects:</label><BR>
                                                                <name-id-list-box
                                                                        [gobiiExtractFilterType]="gobiiExtractFilterType"
                                                                        [filterParamName]="nameIdFilterParamTypes.PROJECTS_BY_CONTACT">
                                                                </name-id-list-box>


                                                                <BR>
                                                                <BR>
                                                                <label class="the-label">Dataset Types:</label><BR>
                                                                <name-id-list-box
                                                                        [gobiiExtractFilterType]="gobiiExtractFilterType"
                                                                        [filterParamName]="nameIdFilterParamTypes.CV_DATATYPE">
                                                                </name-id-list-box>

                                                                <BR>
                                                                <BR>
                                                                <label class="the-label">Platforms:</label><BR>
                                                                <checklist-box
                                                                        [filterParamName]="nameIdFilterParamTypes.PLATFORMS"
                                                                        [gobiiExtractFilterType]="gobiiExtractFilterType">
                                                                </checklist-box>

                                                            </div> <!-- panel body by sample filters filters -->
                                                        </div> <!-- panel by sample filters -->
                                                    </div> <!-- by sample filter column-->
                                                    <div class="col-md-8"> <!-- inner column 2 of main column 1 -->
                                                        <div class="panel panel-primary">
                                                            <div class="panel-heading">
                                                                <h3 class="panel-title">Included Samples</h3>
                                                            </div>
                                                            <div class="panel-body">
                                                                <sample-list-type
                                                                        [gobiiExtractFilterType]="gobiiExtractFilterType"
                                                                        (onHeaderStatusMessage)="handleHeaderStatusMessage($event)">
                                                                </sample-list-type>
                                                                <hr style="width: 100%; color: black; height: 1px; background-color:black;"/>
                                                                <sample-marker-box
                                                                        [gobiiExtractFilterType]="gobiiExtractFilterType"
                                                                        (onSampleMarkerError)="handleHeaderStatusMessage($event)">
                                                                </sample-marker-box>
                                                            </div> <!-- panel body dataset datatable -->
                                                        </div> <!-- panel primary dataset datatable -->
                                                    </div> <!-- data table column -->
                                                </div> <!-- ROW  -->
                                            </div> <!-- container  -->
                                        </ng-template> <!-- lazy-load controls -->
                                    </p-tabPanel> <!-- tab panel -- samples -->
                                    <p-tabPanel header="By Markers">
                                        <ng-template pTemplate="content"> <!-- lazy-load controls -->
                                            <div class="container-fluid">
                                                <div class="row">
                                                    <div class="col-md-4"> <!-- inner column 1 of main column 1 -->
                                                        <div class="panel panel-primary">
                                                            <div class="panel-heading">
                                                                <h3 class="panel-title">Filters</h3>
                                                            </div>
                                                            <div class="panel-body">
                                                                <label class="the-label">Dataset Types:</label><BR>
                                                                <name-id-list-box
                                                                        [gobiiExtractFilterType]="gobiiExtractFilterType"
                                                                        [filterParamName]="nameIdFilterParamTypes.CV_DATATYPE">
                                                                </name-id-list-box>

                                                                <BR>
                                                                <BR>
                                                                <label class="the-label">Platforms:</label><BR>
                                                                <checklist-box
                                                                        [filterParamName]="nameIdFilterParamTypes.PLATFORMS"
                                                                        [gobiiExtractFilterType]="gobiiExtractFilterType">
                                                                </checklist-box>

                                                            </div> <!-- panel body by sample filters filters -->
                                                        </div> <!-- panel by sample filters -->
                                                    </div> <!-- by sample filter column-->
                                                    <div class="col-md-8"> <!-- inner column 2 of main column 1 -->
                                                        <div class="panel panel-primary">
                                                            <div class="panel-heading">
                                                                <h3 class="panel-title">Included Markers</h3>
                                                            </div>
                                                            <div class="panel-body">
                                                                <sample-marker-box
                                                                        [gobiiExtractFilterType]="gobiiExtractFilterType"
                                                                        (onSampleMarkerError)="handleHeaderStatusMessage($event)">
                                                                </sample-marker-box>
                                                            </div> <!-- panel body dataset datatable -->
                                                        </div> <!-- panel primary dataset datatable -->
                                                    </div> <!-- data table column -->
                                                </div> <!-- ROW  -->
                                            </div> <!-- container  -->
                                        </ng-template> <!-- lazy-load controls -->
                                    </p-tabPanel> <!-- tab panel -- markers -->
                                    <p-tabPanel header="Flex Query">
                                        <ng-template pTemplate="content"> <!-- lazy-load controls -->
                                            <div class="container-fluid">
                                                <div class="row">
                                                    <div class="col-md-3"> <!-- inner column 1 of row 1: Filter 1 -->
                                                        <flex-query-filter
                                                                [filterParamNameVertices]="nameIdFilterParamTypes.FQ_F1_VERTICES"
                                                                [filterParamNameVertexValues]="nameIdFilterParamTypes.FQ_F1_VERTEX_VALUES"
                                                                [gobiiExtractFilterType]="gobiiExtractFilterType">
                                                        </flex-query-filter>
                                                    </div> <!-- inner column 1 of row 1: fitler 1 -->
                                                    <div class="col-md-3"> <!-- inner column 1 of row 1: Filter 1 -->
                                                        <flex-query-filter
                                                                [filterParamNameVertices]="nameIdFilterParamTypes.FQ_F2_VERTICES"
                                                                [filterParamNameVertexValues]="nameIdFilterParamTypes.FQ_F2_VERTEX_VALUES"
                                                                [gobiiExtractFilterType]="gobiiExtractFilterType">
                                                        </flex-query-filter>
                                                    </div> <!-- inner column 2 of row 1: fitler 2 -->
                                                    <div class="col-md-3"> <!-- inner column 1 of row 1: Filter 1 -->
                                                        <flex-query-filter
                                                                [filterParamNameVertices]="nameIdFilterParamTypes.FQ_F3_VERTICES"
                                                                [filterParamNameVertexValues]="nameIdFilterParamTypes.FQ_F3_VERTEX_VALUES"
                                                                [gobiiExtractFilterType]="gobiiExtractFilterType">
                                                        </flex-query-filter>
                                                    </div> <!-- inner column 3 of row 1: fitler 3 -->
                                                    <div class="col-md-3"> <!-- inner column 1 of row 1: Filter 1 -->
                                                        <flex-query-filter
                                                                [filterParamNameVertices]="nameIdFilterParamTypes.FQ_F4_VERTICES"
                                                                [filterParamNameVertexValues]="nameIdFilterParamTypes.FQ_F4_VERTEX_VALUES"
                                                                [gobiiExtractFilterType]="gobiiExtractFilterType">
                                                        </flex-query-filter>
                                                    </div> <!-- inner column 4 of row 1: fitler 4 -->

                                                </div> <!-- ROW filters -->

                                                <div clas="row"><!-- ROW marker/sample lists-->
                                                    <!-- intersect controls -->
                                                    <div class="col-md-6"> <!-- intersect markers -->
                                                        <div class="panel panel-primary"> <!-- intersect pamel -->
                                                            <div class="panel-heading">
                                                                <h3 class="panel-title">Intersect Markers</h3>
                                                            </div>
                                                            <div class="panel-body">
                                                                <sample-marker-box
                                                                        [gobiiExtractFilterType]="gobiiExtractFilterTypes.BY_MARKER"
                                                                        (onSampleMarkerError)="handleHeaderStatusMessage($event)">
                                                                </sample-marker-box>
                                                            </div> <!-- panel body dataset datatable -->
                                                        </div> <!-- intersect pamel -->
                                                    </div><!-- intersect markers -->
                                                    <div class="col-md-6"> <!-- intersect samples -->
                                                        <div class="panel panel-primary"> <!-- intersect pamel -->
                                                            <div class="panel-heading">
                                                                <h3 class="panel-title">Intersect Samples</h3>
                                                            </div>
                                                            <div class="panel-body">
                                                                <sample-list-type
                                                                        [gobiiExtractFilterType]="gobiiExtractFilterTypes.BY_SAMPLE"
                                                                        (onHeaderStatusMessage)="handleHeaderStatusMessage($event)">
                                                                </sample-list-type>
                                                                <hr style="width: 100%; color: black; height: 1px; background-color:black;"/>
                                                                <sample-marker-box
                                                                        [gobiiExtractFilterType]="gobiiExtractFilterTypes.BY_SAMPLE"
                                                                        (onSampleMarkerError)="handleHeaderStatusMessage($event)">
                                                                </sample-marker-box>
                                                            </div> <!-- panel body dataset datatable -->
                                                        </div> <!-- intersect pamel -->
                                                    </div><!-- intersect samples -->
                                                </div><!-- ROW marker/sample lists-->
                                            </div> <!-- container  -->
                                        </ng-template> <!-- lazy-load controls -->
                                    </p-tabPanel> <!-- tab panel -- flex query -->
                                </p-tabView> <!-- tabview -->
                            </p-panel> <!-- panel -->

                        </div>  <!-- outer grid column 1: Dataset, Sample, Marker Filtering-->


                        <p-dialog header="System Message"
                                  [(visible)]="display"
                                  (onHide)="onHideMessageDialog($event)"
                                  [contentStyle]="{'width': '400px'}">
                            <div class="panel panel-primary">
                                <div class="panel-body">
                                    {{currentStatusMessage}}
                                    <!--<status-display [messages$]="messages$"></status-display>-->
                                </div> <!-- panel body -->

                            </div> <!-- panel primary -->
                        </p-dialog>

                        <div class="col-md-4"> <!-- outer grid column 2: Criteria summary -->

                            <div class="well">
                                <table>
                                    <tr>
                                        <td colspan="2">
                                            <export-format
                                                    [fileFormat$]="selectedExtractFormat$"
                                                    [gobiiExtractFilterType]="gobiiExtractFilterType">
                                            </export-format>
                                        </td>
                                        <td style="vertical-align: top;">
                                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                            <name-id-list-box
                                                    [gobiiExtractFilterType]="gobiiExtractFilterType"
                                                    [filterParamName]="nameIdFilterParamTypes.MAPSETS">
                                            </name-id-list-box>
                                        </td>
                                    </tr>
                                </table>

                            </div>

                            <div class="panel panel-primary">
                                <div class="panel-heading">
                                    <h3 class="panel-title">Extraction Criteria</h3>
                                </div>
                                <div class="panel-body">
                                    <status-display-tree
                                            (mouseenter)="handleOnMouseOverSubmit($event,true)"
                                            (mouseleave)="handleOnMouseOverSubmit($event,false)"
                                            [fileItemEventChange]="treeFileItemEvent"
                                            [gobiiExtractFilterTypeEvent]="gobiiExtractFilterType"
                                            (onAddMessage)="handleHeaderStatusMessage($event)">
                                    </status-display-tree>

                                    <BR>

                                    <button type="submit"
                                            [class]="submitButtonStyle"
                                            [disabled]="submitButtonStyle === buttonStyleSubmitNotReady"
                                            (click)="handleExtractSubmission()">Submit
                                    </button>

                                    <button type="clear"
                                            [class]="clearButtonStyle"
                                            (click)="handleClearTree()">Clear
                                    </button>
                                    <BR>
                                    <BR>
                                    <marker-sample-count
                                            [gobiiExtractFilterType]="gobiiExtractFilterType">
                                    </marker-sample-count>
                                </div> <!-- panel body -->
                            </div> <!-- panel primary -->

                        </div>  <!-- outer grid column 2: Criteria summary -->


                    </div> <!-- .row of outer grid -->

                </div>
            </div> <!-- panel primary -->
        </div> <!-- panel-default  -->` // end template
}) // @Component

export class ExtractorRoot implements OnInit {
    title = 'Gobii Web';


    // *** You cannot use an Enum directly as a template type parameter, so we need
    //     to assign them to properties
    // ************************************************************************

    //

    nameIdFilterParamTypes: any = Object.assign({}, FilterParamNames);
    gobiiExtractFilterTypes: any = Object.assign({}, GobiiExtractFilterType);

    selectedExtractFormat$: Observable<GobiiFileItem> = this.store.select(fromRoot.getSelectedFileFormat);


    public messages$: Observable<string[]> = this.store.select(fromRoot.getStatusMessages);


    // ************************************************************************

    public treeFileItemEvent: GobiiFileItem;

    public loggedInUser: string = null;


    constructor(private _dtoRequestServiceContact: DtoRequestService<Contact>,
                private _authenticationService: AuthenticationService,
                private _dtoRequestServiceServerConfigs: DtoRequestService<ServerConfig[]>,
                private store: Store<fromRoot.State>,
                private nameIdFileItemService: NameIdFileItemService,
                private entityFileItemService: EntityFileItemService,
                private instructionSubmissionService: InstructionSubmissionService,
                private changeDetectorRef: ChangeDetectorRef,
                private filterService: FilterService,
                private flexQueryService: FlexQueryService) {

        this.messages$.subscribe(
            messages => {
                if (messages.length > 0) {

                    this.currentStatusMessage = messages[0];
                    this.showMessagesDialog();
                }
            }
        );

    }

    public display: boolean = false;
    public currentStatusMessage: string = null;

    showMessagesDialog() {
        this.display = true;

        //workaround for error when using observable in a
        //p-dialog; see https://github.com/angular/angular/issues/17572
        this.changeDetectorRef.detectChanges();
    }

    public onHideMessageDialog($event) {
        this.handleClearMessages();
    }

    // ****************************************************************
    // ********************************************** SERVER SELECTION
    public selectedServerConfig: ServerConfig;
    public serverConfigList: ServerConfig[];
    public currentStatus: string;

    private initializeServerConfigs() {
        let scope$ = this;
        this._dtoRequestServiceServerConfigs.get(new DtoRequestItemServerConfigs()).subscribe(serverConfigs => {

                if (serverConfigs && (serverConfigs.length > 0)) {
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
                    this.handleExportTypeSelected(GobiiExtractFilterType.WHOLE_DATASET);
//                    scope$.initializeSubmissionContact();

                    scope$.currentStatus = "GOBII Server " + gobiiVersion;
                    // this.store.select(fromRoot.getAllFileItems).subscribe(
                    //     all => {
                    //         scope$.currentStatus = "GOBII Server " + gobiiVersion + " ( "+ all.length +" )";
                    //     }
                    // );

                    //scope$.handleAddMessage("Connected to crop config: " + scope$.selectedServerConfig.crop);

                } else {
                    scope$.serverConfigList = [new ServerConfig("<ERROR NO SERVERS>", "<ERROR>", "<ERROR>", 0, "")];
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


                let foo: string = "foo";

                if (contact && contact.contactId && contact.contactId > 0) {

                    //loggedInUser
                    this.nameIdFileItemService.loadFileItem(GobiiFileItem.build(scope$.gobiiExtractFilterType, ProcessType.CREATE)
                            .setEntityType(EntityType.CONTACT)
                            .setEntitySubType(EntitySubType.CONTACT_SUBMITED_BY)
                            .setCvGroup(CvGroup.UNKNOWN)
                            .setExtractorItemType(ExtractorItemType.ENTITY)
                            .setItemName(contact.email)
                            .setItemId(contact.contactId.toLocaleString()),
                        true);

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

// ********************************************************************
// ********************************************** EXPORT TYPE SELECTION AND FLAGS


    public gobiiExtractFilterType: GobiiExtractFilterType;

    private refreshJobId() {

        let jobId: string = FileName.makeUniqueFileId();
        this.nameIdFileItemService.replaceFileItemByCompoundId(
            GobiiFileItem.build(this.gobiiExtractFilterType, ProcessType.CREATE)
                .setExtractorItemType(ExtractorItemType.JOB_ID)
                .setItemId(jobId)
                .setItemName(jobId)
                .setIsExtractCriterion(true))
    }


    public handleTabPanelChange(event) {

        let tabIndex: number = event.index
        if (tabIndex === 0) { // By Dataset
            this.gobiiExtractFilterType = GobiiExtractFilterType.WHOLE_DATASET;
        } else if (tabIndex == 1) {

            this.gobiiExtractFilterType = GobiiExtractFilterType.BY_SAMPLE;

        } else if (tabIndex == 2) {

            this.gobiiExtractFilterType = GobiiExtractFilterType.BY_MARKER;

        } else if (tabIndex == 3) {

            this.gobiiExtractFilterType = GobiiExtractFilterType.FLEX_QUERY;

        } else {
            this.store.dispatch(new historyAction.AddStatusMessageAction(
                "Unknown tab index for extract filter type " + tabIndex));

        }

        this.handleExportTypeSelected(this.gobiiExtractFilterType);

    }// handleTabPanelChange

    private handleExportTypeSelected(arg: GobiiExtractFilterType) {


        //
        this.store.dispatch(new fileItemAction.RemoveAllFromExtractAction(arg));
        this.store.dispatch(new fileItemAction.SetExtractType({gobiiExtractFilterType: arg}));
        this.flexQueryService.invalidateMarkerSampleCount(true);

        // this will trigger onchange events in child components
        this.gobiiExtractFilterType = arg;

        this.instructionSubmissionService.submitReady(this.gobiiExtractFilterType)
            .subscribe(submistReady => {
                submistReady ? this.submitButtonStyle = this.buttonStyleSubmitReady : this.submitButtonStyle = this.buttonStyleSubmitNotReady;
            })


        this.refreshJobId();

        if (this.gobiiExtractFilterType === GobiiExtractFilterType.WHOLE_DATASET) {

            this.nameIdFileItemService.loadNameIdsFromFilterParams(this.gobiiExtractFilterType,
                FilterParamNames.CONTACT_PI_FILTER_OPTIONAL,
                null);

            this.nameIdFileItemService.loadNameIdsFromFilterParams(this.gobiiExtractFilterType,
                FilterParamNames.PROJECT_FILTER_OPTIONAL,
                null);

            this.nameIdFileItemService.loadNameIdsFromFilterParams(this.gobiiExtractFilterType,
                FilterParamNames.EXPERIMENT_FILTER_OPTIONAL,
                null);

            this.filterService.loadFilter(this.gobiiExtractFilterType, FilterParamNames.DATASET_FILTER_OPTIONAL, null);

            // this.nameIdFileItemService.setItemLabelType(this.gobiiExtractFilterType,
            //     FilterParamNames.CONTACT_PI_HIERARCHY_ROOT,
            //     NameIdLabelType.UNKNOWN);

        } else if (this.gobiiExtractFilterType === GobiiExtractFilterType.BY_SAMPLE) {

            this.nameIdFileItemService.loadNameIdsFromFilterParams(this.gobiiExtractFilterType,
                FilterParamNames.CONTACT_PI_HIERARCHY_ROOT,
                null);

            this.nameIdFileItemService.loadNameIdsFromFilterParams(this.gobiiExtractFilterType,
                FilterParamNames.PROJECTS_BY_CONTACT,
                null);

            this.nameIdFileItemService.loadNameIdsFromFilterParams(this.gobiiExtractFilterType,
                FilterParamNames.CV_DATATYPE,
                null);

            this.nameIdFileItemService.loadNameIdsFromFilterParams(this.gobiiExtractFilterType,
                FilterParamNames.PLATFORMS,
                null);


        } else if (this.gobiiExtractFilterType === GobiiExtractFilterType.BY_MARKER) {

            this.nameIdFileItemService.loadNameIdsFromFilterParams(this.gobiiExtractFilterType,
                FilterParamNames.CV_DATATYPE,
                null);

            this.nameIdFileItemService.loadNameIdsFromFilterParams(this.gobiiExtractFilterType,
                FilterParamNames.PLATFORMS,
                null);

        } else if (this.gobiiExtractFilterType === GobiiExtractFilterType.FLEX_QUERY) {

            this.flexQueryService.loadVertices(FilterParamNames.FQ_F1_VERTICES);

        } else {
            this.store.dispatch(new historyAction.AddStatusMessageAction(
                "Unhandled export filter type: " + GobiiExtractFilterType[this.gobiiExtractFilterType]));

        }


        this.initializeSubmissionContact();


        // this.nameIdFileItemService.loadWithFilterParams(this.gobiiExtractFilterType,
        //     this.nameIdRequestParamsExperiments);

        this.nameIdFileItemService.loadNameIdsFromFilterParams(this.gobiiExtractFilterType,
            FilterParamNames.MAPSETS,
            null);


        //changing modes will have nuked the submit as item in the tree, so we need to re-event (sic.) it:
        let formatItem: GobiiFileItem = GobiiFileItem
            .build(this.gobiiExtractFilterType, ProcessType.UPDATE)
            .setExtractorItemType(ExtractorItemType.EXPORT_FORMAT)
            .setItemId(GobiiExtractFormat[GobiiExtractFormat.HAPMAP])
            .setItemName(GobiiExtractFormat[GobiiExtractFormat[GobiiExtractFormat.HAPMAP]]);
        this.nameIdFileItemService.replaceFileItemByCompoundId(formatItem);


        this.nameIdFileItemService
            .replaceFileItemByCompoundId(GobiiFileItem.build(this.gobiiExtractFilterType, ProcessType.CREATE)
                .setExtractorItemType(ExtractorItemType.SAMPLE_LIST_TYPE)
                .setItemName(GobiiSampleListType[GobiiSampleListType.GERMPLASM_NAME])
                .setItemId(GobiiSampleListType[GobiiSampleListType.GERMPLASM_NAME]));


    }


    public handleAddMessage(arg) {
        this.store.dispatch(new historyAction.AddStatusAction(new HeaderStatusMessage(arg, StatusLevel.OK, undefined)))
    }

    public handleClearMessages() {
        this.store.dispatch(new historyAction.ClearStatusesAction())
    }

    public handleHeaderStatusMessage(statusMessage: HeaderStatusMessage) {

        if (!statusMessage.statusLevel || statusMessage.statusLevel != StatusLevel.WARNING) {
            this.handleAddMessage(statusMessage.message);
        } else {
            console.log(statusMessage.message);
        }
    }


// ********************************************************************
// ********************************************** MARKER/SAMPLE selection
    // ********************************************************************
    // ********************************************** Extract file submission
    public submitButtonStyleDefault = "btn btn-primary";
    public buttonStyleSubmitReady = "btn btn-success";
    public buttonStyleSubmitNotReady = "btn btn-warning";
    public submitButtonStyle = this.buttonStyleSubmitNotReady;
    public clearButtonStyle = this.submitButtonStyleDefault;


    public handleOnMouseOverSubmit(arg, isEnter) {

        // this.criteriaInvalid = true;

        if (isEnter) {

            this.instructionSubmissionService.markMissingItems(this.gobiiExtractFilterType)
        } else {
            this.instructionSubmissionService.unmarkMissingItems(this.gobiiExtractFilterType)

        }

    }

    public handleClearTree() {

        this.handleExportTypeSelected(this.gobiiExtractFilterType);


    }


    public handleExtractSubmission() {

        this.instructionSubmissionService
            .submit(this.gobiiExtractFilterType)
            .subscribe(instructions => {
                this.refreshJobId();

                this.handleClearTree()
            });
    }

    ngOnInit(): any {

        this.initializeServerConfigs();


    } // ngOnInit()


}

