import {Component} from "@angular/core";
import {GobiiFileItem} from "../model/gobii-file-item";
import {GobiiExtractFilterType} from "../model/type-extractor-filter";
import {Store} from "@ngrx/store";
import * as fromRoot from '../store/reducers';
import * as fileAction from '../store/actions/fileitem-action';
import * as historyAction from '../store/actions/history-action';
import {Observable} from "rxjs/Observable";
import {FilterParamNames} from "../model/file-item-param-names";
import {FileItemService} from "../services/core/file-item-service";


@Component({
    selector: 'flex-query-filter',
    inputs: ['gobiiExtractFilterType', 'filterParamNameEntities', 'filterParamNameEntityValues'],
    outputs: [],
    styleUrls: ["css/extractor-ui.css"],
    template: `
        <div class="panel panel-primary">
            <div class="panel-heading">
                <h3 class="panel-title">Filters</h3>
            </div>
            <div class="panel-body">
                <label class="the-label">Entity:</label><BR>
                <p-dropdown [options]="fileItemsEntityNames$ | async"
                            [(ngModel)]="selectedAllowableEntities"
                            [style]="{'width': '100%'}"
                            optionLabel="_itemName"></p-dropdown>

                <BR>
                <BR>
                <label class="the-label">Select Entity Values</label><BR>
                <p-listbox [options]="fileItemsEntityValues$ | async"
                           [multiple]="true" 
                           [(ngModel)]="selectedEntityValues" [style]="{'width':'100%'}"
                           optionLabel="_itemName"></p-listbox>
            </div>
        </div>` // end template

})
export class FlexQueryFilterComponent {


    //these are dummy place holders for now
    public selectedAllowableEntities: GobiiFileItem;

    public selectedEntityValues: GobiiFileItem[];

    public fileItemsEntityNames$: Observable<GobiiFileItem[]>;
    public fileItemsEntityValues$: Observable<GobiiFileItem[]>;

    public gobiiExtractFilterType: GobiiExtractFilterType;

    private filterParamNameEntities: FilterParamNames;
    private filterParamNameEntityValues: FilterParamNames;

    constructor(private store: Store<fromRoot.State>,
                private fileItemService: FileItemService) {


    } // ctor


    ngOnInit(): any {

        this.fileItemsEntityNames$ = this.fileItemService.getForFilter(this.filterParamNameEntities)
        this.fileItemsEntityValues$= this.fileItemService.getForFilter(this.filterParamNameEntityValues)

        this
            .fileItemsEntityNames$
            .subscribe(items => {
                    if (this.previousSelectedItemId === null && items && items.length > 0) {
                        this.previousSelectedItemId = items[0].getFileItemUniqueId()
                    }

                },
                error => {
                    this.store.dispatch(new historyAction.AddStatusMessageAction(error))
                });

    }

    previousSelectedItemId: string = null;

    public handleFileItemSelected(arg) {

        if (!this.gobiiExtractFilterType) {
            this.store.dispatch(new historyAction.AddStatusMessageAction("The gobiiExtractFilterType property is not set"))
        }


        let newFileItemUniqueId: string = arg.currentTarget.value;
        let previousFileItemUniqueId: string = this.previousSelectedItemId;

        this.store.dispatch(new fileAction.ReplaceByItemIdAction({
            filterParamName: this.filterParamNameEntities,
            gobiiExtractFilterType: this.gobiiExtractFilterType,
            itemIdCurrentlyInExtract: previousFileItemUniqueId,
            itemIdToReplaceItWith: newFileItemUniqueId
        }));

        this.previousSelectedItemId = newFileItemUniqueId;

    }


} // class
