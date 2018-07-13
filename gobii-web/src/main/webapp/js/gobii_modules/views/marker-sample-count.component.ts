import {Component, EventEmitter, OnChanges, OnInit, SimpleChange} from "@angular/core";
import {GobiiExtractFilterType} from "../model/type-extractor-filter";
import {FilterParamNames} from "../model/file-item-param-names";
import * as fileItemActions from "../store/actions/fileitem-action";
import {GobiiFileItem} from "../model/gobii-file-item";
import {ProcessType} from "../model/type-process";
import {ExtractorItemType} from "../model/type-extractor-item";
import {EntityType} from "../model/type-entity";
import {Store} from "@ngrx/store";
import * as fromRoot from "../store/reducers";
import {Observable} from "rxjs/Observable";

@Component({
    selector: 'marker-sample-count',
    outputs: [],
    inputs: ['gobiiExtractFilterType'],
    template: `
        <p-panel *ngIf="displayPanel" header="Estimated Marker/Sample Count"
                 [(toggleable)]="panelCollapsed"
                 [(collapsed)]="panelCollapsed"
                 (onBeforeToggle)="onBeforeToggle($event)"
                 (onAfterToggle)="onAfterToggle($event)">
            <p-progressSpinner *ngIf="displaySpinner" [style]="{width: '50px', height: '50px'}"
                               strokeWidth="8" fill="#EEEEEE"
                               animationDuration="3s"></p-progressSpinner>
            <div *ngIf="displayCounts">
                <p>Total Markers: {{markerCount$ | async}}</p>
                <p>Total Samples: {{sampleCount$ | async}}</p>
            </div>
        </p-panel>
    `
})
export class MarkerSampleCountComponent implements OnChanges, OnInit {

    public gobiiExtractFilterType: GobiiExtractFilterType;
    public displayPanel: boolean = false;
    public displayCounts: boolean = true;
    public panelCollapsed: boolean = false;
    public markerCount$: Observable<number> = this.store.select(fromRoot.getCurrentMarkerCount);
    public sampleCount$: Observable<number> = this.store.select(fromRoot.getCurrentSampleCount);
    public displaySpinner: boolean = true;

    constructor(private store: Store<fromRoot.State>) {

        this.markerCount$.subscribe(
            value => {


                if ( value >= 0) {
                    this.displayCounts = true;
                    this.displaySpinner = false;

                } else {
                    this.displayCounts = false;
                    this.displaySpinner = true;
                }

            }
        );

        this.sampleCount$.subscribe(
            value => {


                if ( value >= 0) {
                    this.displayCounts = true;
                    this.displaySpinner = false;
                } else {
                    this.displayCounts = false;
                    this.displaySpinner = true;
                }

            }
        );

    }

    ngOnInit(): void {

    }

    public onBeforeToggle($event) {
    }

    private initCount() {
        this.displayCounts = false;
        this.displaySpinner = true;
    }

    public onAfterToggle($event) {

        // if ($event.collapsed) {
        //
        //     this.initCount();
        // } else {
        //     setTimeout(() => {
        //         this.displayCounts = true;
        //         this.displaySpinner = false;
        //     }, 3000);
        // }
    }

    // gobiiExtractType is not set until you get OnChanges
    ngOnChanges(changes: { [propName: string]: SimpleChange }) {

        if (changes['gobiiExtractFilterType']
            && (changes['gobiiExtractFilterType'].currentValue != null)
            && (changes['gobiiExtractFilterType'].currentValue != undefined)) {

            if ((changes['gobiiExtractFilterType'].currentValue != changes['gobiiExtractFilterType'].previousValue) &&
                (changes['gobiiExtractFilterType'].currentValue === GobiiExtractFilterType.FLEX_QUERY)) {

                this.displayPanel = true;
                this.displaySpinner = true;

                let markerCountItem:GobiiFileItem = GobiiFileItem
                    .build(GobiiExtractFilterType.FLEX_QUERY, ProcessType.CREATE)
                    .setExtractorItemType(ExtractorItemType.ITEM_COUNT)
                    .setEntityType(EntityType.MARKER)
                    .setItemName("Marker Count")
                    .setEntity(Number(0));
                // default count items on load
                let loadActionMarkerCount: fileItemActions.LoadFileItemtAction = new fileItemActions.LoadFileItemtAction(
                    {
                        gobiiFileItem: markerCountItem,
                        selectForExtract: true
                    }
                );
                this.store.dispatch(loadActionMarkerCount);


                let loadActionSampleCount: fileItemActions.LoadFileItemtAction = new fileItemActions.LoadFileItemtAction(
                    {
                        gobiiFileItem: GobiiFileItem
                            .build(GobiiExtractFilterType.FLEX_QUERY, ProcessType.CREATE)
                            .setExtractorItemType(ExtractorItemType.ITEM_COUNT)
                            .setEntityType(EntityType.DNA_SAMPLE)
                            .setItemName("Sample Count")
                            .setEntity(Number(0)),
                        selectForExtract: true
                    }
                );
                this.store.dispatch(loadActionSampleCount);

            } // if we have a new filter type

        } else {
            this.displayPanel = false;
            this.displaySpinner = false;

        }// if-else filter type changed


    } // ngonChanges


}