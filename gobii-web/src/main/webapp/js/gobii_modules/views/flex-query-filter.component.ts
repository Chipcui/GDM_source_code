import {Component, OnChanges, OnInit, SimpleChange} from "@angular/core";
import {GobiiFileItem} from "../model/gobii-file-item";
import {GobiiExtractFilterType} from "../model/type-extractor-filter";
import {Store} from "@ngrx/store";
import * as fromRoot from '../store/reducers';
import {Observable} from "rxjs/Observable";
import {FilterParamNames} from "../model/file-item-param-names";
import {NameIdFileItemService} from "../services/core/nameid-file-item-service";
import {FilterService} from "../services/core/filter-service";
import {FlexQueryService} from "../services/core/flex-query-service";
import {EntitySubType, EntityType} from "../model/type-entity";
import {NameIdLabelType} from "../model/name-id-label-type";
import {PayloadFilter} from "../store/actions/action-payload-filter";
import {FilterParams} from "../model/filter-params";
import {FilterParamsColl} from "../services/core/filter-params-coll";
import {CvGroup} from "../model/cv-group";
import {Vertex} from "../model/vertex";


@Component({
    selector: 'flex-query-filter',
    inputs: ['gobiiExtractFilterType', 'filterParamNameVertices', 'filterParamNameVertexValues'],
    outputs: [],
    styleUrls: ["css/extractor-ui.css"],
    template: `
        <div class="panel panel-primary" [ngStyle]="currentStyle">
            <div class="panel-heading">
                <h3 class="panel-title">Filters</h3>
            </div>
            <div class="panel-body">
                <label class="the-label">Entity:</label><BR>
                <p-dropdown [options]="fileItemsVertexNames$ | async"
                            [style]="{'width': '100%'}"
                            optionLabel="_itemName"
                            [(ngModel)]="selectedVertex"
                            (onChange)="handleVertexSelected($event)"
                            [disabled]="currentStyle===disabledStyle">
                </p-dropdown>

                <BR>
                <BR>
                <label class="the-label">Select Entity Values</label><BR>
                <p-listbox [options]="fileItemsEntityValues$ | async"
                           [multiple]="true"
                           [(ngModel)]="selectedVertexValues"
                           [style]="{'width':'100%'}"
                           (onChange)="handleVertexValueSelected($event)"
                           optionLabel="_itemName"
                           [disabled]="currentStyle===disabledStyle"></p-listbox>
            </div>

            <div class="container">
                <p>Count: {{totalValues}} </p>
                <p>Selected: {{selectedVertexValues ? selectedVertexValues.length : 0}}</p>
            </div>
        </div>` // end template

})
export class FlexQueryFilterComponent implements OnInit, OnChanges {


    //these are dummy place holders for now
    public totalValues: string = "0";
    public selectedVertex: GobiiFileItem;
    public selectedVertexValues: GobiiFileItem[];

    public fileItemsVertexNames$: Observable<GobiiFileItem[]>;
    public fileItemsEntityValues$: Observable<GobiiFileItem[]>;
    public JobId$: Observable<GobiiFileItem>;

    public gobiiExtractFilterType: GobiiExtractFilterType;

    private filterParamNameVertices: FilterParamNames;
    private filterParamNameVertexValues: FilterParamNames;

    public enabledStyle = null;
    public disabledStyle = {'background': '#dddddd'};
    public currentStyle = this.disabledStyle;

    constructor(private store: Store<fromRoot.State>,
                private fileItemService: NameIdFileItemService,
                private filterService: FilterService,
                private filterParamsColl: FilterParamsColl,
                private flexQueryService: FlexQueryService) {


    } // ctor


    ngOnInit(): any {

        this.fileItemsVertexNames$ = this.filterService.getForFilter(this.filterParamNameVertices);
        this.fileItemsEntityValues$ = this.filterService.getForFilter(this.filterParamNameVertexValues);
        this.JobId$ = this.store.select(fromRoot.getJobId);


        this
            .fileItemsEntityValues$
            .subscribe(items => {
                this.totalValues = items.length.toString()
            });

        this.setControlState(false);
        this.store.select(fromRoot.getFileItemsFilters)
            .subscribe(
                filters => {


                    // you have to reset from state because this control won't see the sibling control's
                    // change event
                    let thisControlVertexfilterParams: FilterParams = this.filterParamsColl.getFilter(this.filterParamNameVertices, GobiiExtractFilterType.FLEX_QUERY);
                    let currentVertexFilter: PayloadFilter = filters[thisControlVertexfilterParams.getQueryName()];
                    if (currentVertexFilter) {
                        if (!currentVertexFilter.targetEntityFilterValue) {
                            this.selectedVertex = null;
                            this.selectedVertexValues = null;
                        }
                    }


                    if (!thisControlVertexfilterParams.getPreviousSiblingFileItemParams()) {
                        this.setControlState(true);
                    } else if (thisControlVertexfilterParams.getPreviousSiblingFileItemParams().getChildFileItemParams().length > 0) {


                        let vertexValuePreviousVertexSelectorParamName: string = thisControlVertexfilterParams
                            .getPreviousSiblingFileItemParams()
                            .getChildFileItemParams()[0].getQueryName();

                        let previousVertexValuesFilter: PayloadFilter = filters[vertexValuePreviousVertexSelectorParamName];
                        if (previousVertexValuesFilter && previousVertexValuesFilter.targetEntityFilterValue) {
                            this.setControlState(true)
                        } else {
                            this.setControlState(false)
                        }

                    } // if-else there are previous sibling params

                }); // subscribe to select filters()

    } // ngInit()


    private setControlState(enabled: boolean) {

        if (enabled) {
            this.currentStyle = this.enabledStyle;
        } else {
            this.currentStyle = this.disabledStyle;
        }
    }

    public handleVertexSelected(arg) {

        if (arg.value && arg.value._entity) {
            let vertexId: string;
            let entityType: EntityType = EntityType.UNKNOWN;
            let entitySubType: EntitySubType = EntitySubType.UNKNOWN;
            let cvGroup: CvGroup = CvGroup.UNKNOWN;
            let cvTerm: string = null;
            if (arg.value.getNameIdLabelType() === NameIdLabelType.UNKNOWN) {
                vertexId = arg.value.getItemId();
                entityType = arg.value.getEntityType();
                entitySubType = arg.value.getEntitySubType();
                cvGroup = arg.value.getCvGroup();
                cvTerm = arg.value.getCvTerm();

            } else {
                vertexId = null;
                this.selectedVertexValues = [];
                this.previousSelectedVertices = [];
            }

            this.flexQueryService.loadSelectedVertexFilter(this.filterParamNameVertices,
                vertexId,
                entityType,
                entitySubType,
                cvGroup,
                cvTerm);
        }

        this.JobId$.subscribe(
            fileItemJobId => {
                this.flexQueryService.loadVertexValues(fileItemJobId.getItemId(),
                    arg.value,
                    this.filterParamNameVertexValues);
            }
        ).unsubscribe();

    } // end function


    // Technically, we should not be keeping state in this control in this way;
    // However, it turns out to be a lot more complicated and error prone to
    // rely purely on the store
    private previousSelectedVertices: GobiiFileItem[] = [];

    public handleVertexValueSelected(arg) {

        let targetValueVertex: Vertex = (this.selectedVertex && this.selectedVertex.getEntity()) ?
            this.selectedVertex.getEntity():
            null;

        let filterVertex:Vertex;
        if( targetValueVertex) {
            filterVertex = Vertex.fromVertex(targetValueVertex);
            filterVertex.filterVals = this.selectedVertexValues
                .map( vv => Number(vv.getItemId()));
        }

        let newItems: GobiiFileItem[] = this.selectedVertexValues
            .filter(gfi => !this.previousSelectedVertices.find(igfi => igfi.getFileItemUniqueId() === gfi.getFileItemUniqueId()));

        let deselectedItems: GobiiFileItem[] = this.previousSelectedVertices.filter(gfi => !this.selectedVertexValues.find(igfi => igfi.getFileItemUniqueId() === gfi.getFileItemUniqueId()));

        this.JobId$.subscribe(
            fileItemJobId => {

                this.flexQueryService.loadSelectedVertexValueFilters(fileItemJobId.getItemId(),
                    this.filterParamNameVertexValues,
                    newItems,
                    deselectedItems,
                    filterVertex);
            }
        ).unsubscribe();



        this.previousSelectedVertices = this.selectedVertexValues;
    }


    ngOnChanges(changes: {
        [propName: string
            ]: SimpleChange
    }) {

        if (changes['gobiiExtractFilterType']
            && (changes['gobiiExtractFilterType'].currentValue != null)
            && (changes['gobiiExtractFilterType'].currentValue != undefined)) {

            if (changes['gobiiExtractFilterType'].currentValue != changes['gobiiExtractFilterType'].previousValue) {

                if (this.gobiiExtractFilterType === GobiiExtractFilterType.FLEX_QUERY) {

                    // this.flexQueryService.loadVertices(this.filterParamNameVertices);

                }

            } // if we have a new filter type

        } // if filter type changed


    } // ngonChanges

} // class
