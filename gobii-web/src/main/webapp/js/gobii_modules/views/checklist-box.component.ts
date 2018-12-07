import {Component, EventEmitter, KeyValueDiffers} from "@angular/core";
import {GobiiFileItem} from "../model/gobii-file-item";
import {GobiiExtractFilterType} from "../model/type-extractor-filter";
import {FilterParams} from "../model/filter-params";
import {HeaderStatusMessage} from "../model/dto-header-status-message";
import {ExtractorItemType} from "../model//type-extractor-item";
import * as fromRoot from '../store/reducers';
import * as fileAction from '../store/actions/fileitem-action';
import {Store} from "@ngrx/store";
import {Observable} from "rxjs/Observable";
import {NameIdFileItemService} from "../services/core/nameid-file-item-service";
import {FilterParamNames} from "../model/file-item-param-names";
import {FilterService} from "../services/core/filter-service";
import {ViewIdGeneratorService} from "../services/core/view-id-generator-service";
import {TypeControl} from "../services/core/type-control";


@Component({
    selector: 'checklist-box',
    inputs: ['gobiiExtractFilterType',
        'filterParamName'],
    outputs: ['onError'],
    template: `
        <form
                [id]="viewIdGeneratorService.makeCheckboxListBoxId(filterParamName)">
            <div style="overflow:auto; height: 80px; border: 1px solid #336699; padding-left: 5px">
                <div *ngFor="let gobiiFileItem of gobiiFileItems$ | async"
                     (click)=handleItemSelected($event)>
                    <input type="checkbox"
                           (click)=handleItemChecked($event)
                           [checked]="gobiiFileItem.getSelected()"
                           value={{gobiiFileItem.getFileItemUniqueId()}}
                    name="{{gobiiFileItem.getItemName()}}">&nbsp;{{gobiiFileItem.getItemName()}}
                </div>
            </div>
        </form>` // end template

})


export class CheckListBoxComponent {

    differ: any;

    constructor(private store: Store<fromRoot.State>,
                private fileItemService:NameIdFileItemService,
                private filterService:FilterService,
                private differs: KeyValueDiffers,
                public viewIdGeneratorService: ViewIdGeneratorService) {

        this.differ = differs.find({}).create(null);


    } // ctor

    public typeControl:any = TypeControl;

    ngOnInit(): any {
        this.gobiiFileItems$ = this.filterService.getForFilter(this.filterParamName);
    }

    private gobiiExtractFilterType: GobiiExtractFilterType = GobiiExtractFilterType.UNKNOWN;
    private onError: EventEmitter<HeaderStatusMessage> = new EventEmitter();


    public filterParamName: FilterParamNames;
    public gobiiFileItems$: Observable<GobiiFileItem[]>;

    public handleItemChecked(arg) {

        let currentFileItemUniqueId: string = arg.currentTarget.value;

        if (arg.currentTarget.checked) {
            this.store.dispatch(new fileAction.AddToExtractByItemIdAction(currentFileItemUniqueId));
        } else {
            this.store.dispatch(new fileAction.RemoveFromExractByItemIdAction(currentFileItemUniqueId));
        }

    } // handleItemChecked()

    public handleItemSelected(arg) {

//        arg.currentTarget.style = "background-color:#b3d9ff";

    }


    public handleHeaderStatus(headerStatusMessage: HeaderStatusMessage) {

        this.onError.emit(headerStatusMessage);
    }


}
