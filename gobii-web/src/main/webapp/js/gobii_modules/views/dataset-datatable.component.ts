import {Component, Input, OnChanges, OnInit, SimpleChange} from "@angular/core";
import {Store} from "@ngrx/store";
import * as fromRoot from '../store/reducers';
import * as historyAction from '../store/actions/history-action';
import {GobiiExtractFilterType} from "../model/type-extractor-filter";
import {FileItemService} from "../services/core/file-item-service";
import {FilterParamNames} from "../model/file-item-param-names";
import {Observable} from "rxjs/Observable";
import {DataSet} from "../model/dataset";
import {GobiiFileItem} from "../model/gobii-file-item";
import * as fileAction from '../store/actions/fileitem-action';
import {OverlayPanel} from "primeng/primeng";
import {DtoRequestService} from "../services/core/dto-request.service";
import {JsonToGfiDataset} from "../services/app/jsontogfi/json-to-gfi-dataset";
import {FilterParamsColl} from "../services/core/filter-params-coll";
import {DtoRequestItemGfi} from "../services/app/dto-request-item-gfi";
import {FilterParams} from "../model/file-item-params";
import {JsonToGfiAnalysis} from "../services/app/jsontogfi/json-to-gfi-analysis";
import {CvFilters, CvFilterType} from "../model/cv-filter-type";
import {EntitySubType, EntityType} from "../model/type-entity";
import {GobiiFileItemCompoundId} from "../model/gobii-file-item-compound-id";
import {ExtractorItemType} from "../model/type-extractor-item";
// import {TooltipModule} from "/primeng/primeng";
import {PagedFileItemList} from "../model/payload/paged-item-list";
import {Pagination} from "../model/payload/pagination";
import {Subject} from "rxjs/Subject";
import 'rxjs/add/operator/withLatestFrom'

@Component({
    selector: 'dataset-datatable',
    inputs: [],
    outputs: [],
    template: `
        <div style="border: 1px solid #336699; padding-left: 5px">
            <BR>
            <p-checkbox binary="true"
                        [ngModel]="filterToExtractReady"
                        (onChange)="handleFilterToExtractReadyChecked($event)"
                        [disabled]="disableFilterToExtractReadyCheckbox">
            </p-checkbox>
            <label class="the-legend">Extract-Ready&nbsp;</label>
            <BR>
            <div class="container-fluid">


                <!--<name-id-list-box-->
                <!--[gobiiExtractFilterType]="gobiiExtractFilterType"-->
                <!--[filterParamName]="nameIdFilterParamTypes.CV_JOB_STATUS">-->
                <!--</name-id-list-box>-->

            </div> <!--status selector row -->
            <p-dataTable [value]="datasetsFileItems$ | async"
                         [(selection)]="selectedDatasets"
                         (onRowSelect)="handleRowSelect($event)"
                         (onRowUnselect)="handleRowUnSelect($event)"
                         (onRowClick)="handleOnRowClick($event)"
                         dataKey="_entity.id"
                         resizableColumns="true"
                         scrollable="true"
                         scrollHeight="700px"
                         scrollWidth="100%"
                         columnResizeMode="expand">
                <p-column field="_entity.id" header="Id" hidden="true"></p-column>
                <p-column [style]="{'width':'5%','text-align':'center'}">
                    <ng-template let-col let-fi="rowData" pTemplate="body">
                        <p-checkbox binary="true"
                                    [ngModel]="fi.getSelected()"
                                    (onChange)="handleRowChecked($event, fi)"
                                    [hidden]="fi.getEntity().jobStatusName !== 'completed'">
                        </p-checkbox>

                    </ng-template>
                </p-column>
                <p-column [style]="{'width':'5%','text-align':'center'}">
                    <ng-template let-col="rowData" pTemplate="body">
                        <button type="button"
                                pButton (click)="selectDataset($event,col,datasetOverlayPanel);"
                                icon="fa-bars"
                                style="font-size: 10px"></button>
                    </ng-template>
                </p-column>
                <p-column field="_entity.datasetName"
                          header="Name"
                          [style]="{'width': '18%'}">
                    <ng-template pTemplate="body" let-col let-fi="rowData">
                        <span pTooltip="{{fi._entity.datasetName}}" tooltipPosition="left"
                              tooltipStyleClass="tableTooltip"> {{fi._entity.datasetName}} </span>
                    </ng-template>
                </p-column>
                <p-column field="_entity.projectName"
                          header="Project"
                          [style]="{'width': '18%'}">
                    <ng-template pTemplate="body" let-col let-fi="rowData">
                        <span pTooltip="{{fi._entity.projectName}}" tooltipPosition="left"
                              tooltipStyleClass="tableTooltip"> {{fi._entity.projectName}} </span>
                    </ng-template>
                </p-column>
                <p-column field="_entity.experimentName"
                          header="Experiment"
                          [style]="{'width': '18%'}">
                    <ng-template pTemplate="body" let-col let-fi="rowData">
                        <span pTooltip="{{fi._entity.experimentName}}" tooltipPosition="left"
                              tooltipStyleClass="tableTooltip"> {{fi._entity.experimentName}} </span>
                    </ng-template>
                </p-column>
                <p-column field="_entity.piEmail"
                          header="PI"
                          [style]="{'width': '18%'}">
                    <ng-template pTemplate="body" let-col let-fi="rowData">
                        <span pTooltip="{{fi._entity.piLastName}}, {{fi._entity.piFirstName}}" tooltipPosition="left" tooltipStyleClass="tableTooltip"> {{fi._entity.piLastName}}, {{fi._entity.piFirstName}} </span>
                    </ng-template>
                </p-column>
                <!--<p-column field="_entity.jobStatusName" header="Status"></p-column>-->
                <!--<p-column field="_entity.jobTypeName" header="Type"></p-column>-->
                <p-column field="jobSubmittedDate"
                          header="Status"
                          [style]="{'width': '18%'}">
                    <ng-template let-col let-fi="rowData" pTemplate="body">
                        {{fi._entity.jobTypeName === "load" ? "loaded on " : fi._entity.jobTypeName === "extract" ? "extracted on " : "unprocessed"}}
                        {{fi._entity[col.field] | date:'yyyy-MM-dd HH:mm' }}
                    </ng-template>
                </p-column>
            </p-dataTable>
            <p-overlayPanel #datasetOverlayPanel
                            appendTo="body"
                            showCloseIcon="true"
                            (onBeforeHide)="handleHideOverlayPanel($event)">


                <!-- you have to  -->
                <legend>Details:
                    {{ selectedDatasetDetailEntity ? selectedDatasetDetailEntity.datasetName : null}}
                </legend>


                <div class="panel panel-default">
                    <table class="table table-striped table-hover">
                        <!--<table class="table table-striped table-hover table-bordered">-->
                        <tbody>
                        <tr>
                            <td><b>Principle Investigator</b></td>
                            <td>{{ selectedDatasetDetailEntity ? selectedDatasetDetailEntity.piEmail : null}}</td>
                        </tr>

                        <tr>
                            <td><b>Project</b></td>
                            <td>
                                {{ selectedDatasetDetailEntity ? selectedDatasetDetailEntity.projectName : null}}
                            </td>
                        </tr>


                        <tr>
                            <td><b>Data Type</b></td>
                            <td>
                                {{ selectedDatasetDetailEntity ? selectedDatasetDetailEntity.datatypeName : null}}
                            </td>
                        </tr>


                        <tr>
                            <td><b>Calling Analysis</b></td>
                            <td>
                                {{ selectedDatasetDetailEntity ? selectedDatasetDetailEntity.callingAnalysisName : null}}
                            </td>
                        </tr>

                        <tr>
                            <td><b>Total Samples</b></td>
                            <td>
                                {{ selectedDatasetDetailEntity ? selectedDatasetDetailEntity.totalSamples : null}}
                            </td>
                        </tr>


                        <tr>
                            <td><b>Total Markers</b></td>
                            <td>
                                {{ selectedDatasetDetailEntity ? selectedDatasetDetailEntity.totalMarkers : null}}
                            </td>
                        </tr>

                        </tbody>
                    </table>
                </div>


                <div class="panel panel-default">
                    <div class="panel-heading" style="font-size: medium">
                        <b>Experiment:
                            {{ selectedDatasetDetailEntity ? selectedDatasetDetailEntity.experimentName : null}}</b>

                    </div>
                    <div class="card text-white bg-info">

                        <div class="card-body">
                            <table class="table table-striped table-hover">
                                <!--<table class="table table-striped table-hover table-bordered">-->
                                <tbody>
                                <tr>
                                    <td>Platform:</td>
                                    <td>{{ selectedDatasetDetailEntity ? selectedDatasetDetailEntity.platformName : null}}
                                    </td>
                                </tr>
                                <tr>
                                    <td>Protocol:</td>
                                    <td>{{ selectedDatasetDetailEntity ? selectedDatasetDetailEntity.protocolName : null}}
                                    </td>
                                </tr>
                                </tbody>
                            </table>

                        </div>
                    </div>
                </div>
                <BR>
                <div>

                    <p-panel
                            header="{{ selectedDatasetDetailEntity ? selectedDatasetDetailEntity.analysesIds.length : null}} Analyses"
                            (onBeforeToggle)="handleOpenAnalysesTab($event)"
                            [(toggleable)]="analysisPanelToggle"
                            [(collapsed)]="analysisPanelCollapsed">
                        <p *ngFor="let name of datasetAnalysesNames">
                            {{ name }}
                        </p>
                    </p-panel>
                </div>

            </p-overlayPanel>

            <div *ngIf="doPaging">
                <button pButton type="button" (click)="onClickForNextPage$.next($event)" label="Test Paging"></button>
            </div>
        </div> <!-- enclosing box  -->
    ` // end template

})

export class DatasetDatatableComponent implements OnInit, OnChanges {


    public onClickForNextPage$ = new Subject<Pagination>();

    constructor(private store: Store<fromRoot.State>,
                private fileItemService: FileItemService,
                private filterParamsColl: FilterParamsColl,
                private fileItemRequestService: DtoRequestService<GobiiFileItem[]>) {

        if (this.doPaging) {
            this.datasetsFileItems$ = this.store.select(fromRoot.getDatsetEntitiesPaged);
        } else {
            this.datasetsFileItems$ = this.store.select(fromRoot.getDatsetEntities);
        }

        this.onClickForNextPage$
            .withLatestFrom(this.store)
            .subscribe(([data, state]) => {
                if (state.fileItems.filters[FilterParamNames.DATASET_LIST_PAGED]) {
                    let pagination: Pagination = state.fileItems.filters[FilterParamNames.DATASET_LIST_PAGED].pagination;
                    if (pagination) {
                        this.fileItemService.loadPagedEntityList(this.gobiiExtractFilterType,
                            FilterParamNames.DATASET_LIST_PAGED,
                            pagination.pagedQueryId,
                            pagination.pageSize,
                            ++this.page)
                    }
                }
            });
    }

    public doPaging = false;
//    public datasetsFileItems$: Observable<GobiiFileItem[]> = this.store.select(fromRoot.getDatsetEntities);
    //public datasetsFileItems$: Observable<GobiiFileItem[]> = this.store.select(fromRoot.getDatsetEntitiesPaged);
    public datasetsFileItems$: Observable<GobiiFileItem[]>;
    public selectedDatasets: GobiiFileItem[];
    public datasetAnalysesNames: string[] = [];
    public nameIdFilterParamTypes: any = Object.assign({}, FilterParamNames);
    public selectedDatasetDetailEntity: DataSet;
    public analysisPanelCollapsed: boolean = true;
    public analysisPanelToggle: boolean = true;

    public filterToExtractReady: boolean = true;
    public disableFilterToExtractReadyCheckbox: boolean = false;


    public handleFilterToExtractReadyChecked(event) {


        let filterValue: string;
        if (event === true) {

            filterValue = "completed";
        } else {
            filterValue = null;
        }

        this.store.dispatch(new fileAction.LoadFilterAction(
            {
                filterId: FilterParamNames.DATASET_LIST_STATUS,
                filter: {
                    gobiiExtractFilterType: GobiiExtractFilterType.WHOLE_DATASET,
                    gobiiCompoundUniqueId: new GobiiFileItemCompoundId(ExtractorItemType.ENTITY,
                        EntityType.DATASET,
                        EntitySubType.UNKNOWN,
                        CvFilterType.UNKNOWN,
                        CvFilters.get(CvFilterType.UNKNOWN)),
                    filterValue: filterValue,
                    entityLasteUpdated: null,
                    pagination: null
                }
            }
        ))

    }


    public handleHideOverlayPanel($event) {

        this.datasetAnalysesNames = [];
        this.analysisPanelCollapsed = true;
    }

    /***
     * Lazy load dataset when the dataset pane is opened. Notice that we don't dispatch the dataset to the store.
     * There are a couple of things to note here:
     * 1) Keeping data local to the component breaks the store model, because we are effectively keeping some state locally.
     *    I would argue that this issues is mitigated by the fact the data are only used in that pop up and then they go away;
     * 2) Consequently, if the user returns over and over again to the same dataset, we are taking on the otherwise unnecessary
     *    expense of repeating the same query. However, it is my judgement that that scenario will happen infrequently enough
     *    that we don't need to worry about this for now.
     * @param event
     * @param {GobiiFileItem} dataSeItem
     * @param {OverlayPanel} datasetOverlayPanel
     */
    selectDataset(event, dataSeItem: GobiiFileItem, datasetOverlayPanel: OverlayPanel) {

        let datasetId: number = dataSeItem.getEntity().id;
        let filterParams: FilterParams = this.filterParamsColl.getFilter(FilterParamNames.DATASET_BY_DATASET_ID, GobiiExtractFilterType.WHOLE_DATASET);

        let dtoRequestItemGfi: DtoRequestItemGfi = new DtoRequestItemGfi(filterParams,
            datasetId.toString(),
            new JsonToGfiDataset(filterParams, this.filterParamsColl));

        this.fileItemRequestService
            .get(dtoRequestItemGfi)
            .subscribe(entityItems => {
                if (entityItems.length === 1 && entityItems[0].getEntity()) {
                    this.selectedDatasetDetailEntity = entityItems[0].getEntity();
                    this.analysisPanelToggle = this.selectedDatasetDetailEntity.analysesIds.length > 0;
                    datasetOverlayPanel.toggle(event);
                } else {
                    this.store
                        .dispatch(new historyAction.AddStatusMessageAction("There is no dataset data for dataset id "
                            + datasetId.toString()));

                }
            });
    }


    /***
     * Lazy-load analyses if there are any.
     * The note about not putting these data in the store with regard to the dataset entity applies to
     * the analyses.
     * @param event
     */
    public handleOpenAnalysesTab(event) {

        if (this.selectedDatasetDetailEntity) {

            let datasetId: number = this.selectedDatasetDetailEntity.id;
            let filterParams: FilterParams = this.filterParamsColl.getFilter(FilterParamNames.ANALYSES_BY_DATASET_ID, GobiiExtractFilterType.WHOLE_DATASET);

            let dtoRequestItemGfi: DtoRequestItemGfi = new DtoRequestItemGfi(filterParams,
                datasetId.toString(),
                new JsonToGfiAnalysis(filterParams, this.filterParamsColl));

            this.fileItemRequestService
                .get(dtoRequestItemGfi)
                .subscribe(entityItems => {

                    this.datasetAnalysesNames = entityItems
                        .map(gfi => gfi.getItemName())


                });

        } // if we have a selected datset entity

    }


    private page: number = 0;

    public handleRowChecked(checked: boolean, selectedDatasetFileItem: GobiiFileItem) {
        this.handleItemChecked(selectedDatasetFileItem.getFileItemUniqueId(), checked);

    }


    public handleRowSelect(event) {
        let selectedDatasetFileItem: GobiiFileItem = event.data;
        this.handleItemChecked(selectedDatasetFileItem.getFileItemUniqueId(), event.originalEvent.checked);
    }

    public handleRowUnSelect(event) {
        let selectedDatasetFileItem: GobiiFileItem = event.data;
        this.handleItemChecked(selectedDatasetFileItem.getFileItemUniqueId(), event.originalEvent.checked);
    }

    public handleOnRowClick(event) {
        let selectedDataset: GobiiFileItem = event.data;

    }

    public handleItemChecked(currentFileItemUniqueId: string, isChecked: boolean) {

        if (isChecked) {
            this.store.dispatch(new fileAction.AddToExtractByItemIdAction(currentFileItemUniqueId));
        } else {
            this.store.dispatch(new fileAction.RemoveFromExractByItemIdAction(currentFileItemUniqueId));
        }

    } // handleItemChecked()


    @Input()
    public gobiiExtractFilterType: GobiiExtractFilterType;

    ngOnInit() {

        //this.handleLoadPage(1);
    } // ngOnInit()

    // gobiiExtractType is not set until you get OnChanges
    ngOnChanges(changes: { [propName: string]: SimpleChange }) {

        if (changes['gobiiExtractFilterType']
            && (changes['gobiiExtractFilterType'].currentValue != null)
            && (changes['gobiiExtractFilterType'].currentValue != undefined)) {

            if (changes['gobiiExtractFilterType'].currentValue != changes['gobiiExtractFilterType'].previousValue) {

                if (this.gobiiExtractFilterType === GobiiExtractFilterType.WHOLE_DATASET) {

                    if( this.doPaging ) {
                        this.fileItemService.loadPagedEntityList(this.gobiiExtractFilterType,
                            FilterParamNames.DATASET_LIST_PAGED,
                            null,
                            5,
                            0);
                        this.fileItemService.loadNameIdsFromFilterParams(this.gobiiExtractFilterType,
                            FilterParamNames.CV_JOB_STATUS,
                            null);

                    } else {
                        this.fileItemService.loadEntityList(this.gobiiExtractFilterType, FilterParamNames.DATASET_LIST);
                    }
                }

            } // if we have a new filter type

        } // if filter type changed


    } // ngonChanges
}