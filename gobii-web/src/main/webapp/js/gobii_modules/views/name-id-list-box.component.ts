import {Component, DoCheck, EventEmitter, KeyValueDiffers, OnChanges, OnInit, SimpleChange} from "@angular/core";
import {NameId} from "../model/name-id";
import {GobiiFileItem} from "../model/gobii-file-item";
import {GobiiExtractFilterType} from "../model/type-extractor-filter";
import {NameIdService} from "../services/core/name-id-service";
import {NameIdRequestParams} from "../model/name-id-request-params";
import {HeaderStatusMessage} from "../model/dto-header-status-message";
import {Store} from "@ngrx/store";
import * as fromRoot from '../store/reducers';
import * as fileAction from '../store/actions/fileitem-action';
import {Observable} from "rxjs/Observable";
import {FileItemService} from "../services/core/file-item-service";
import {ProcessType} from "../model/type-process";
import {ExtractorItemType} from "../model/file-model-node";


@Component({
    selector: 'name-id-list-box',
    inputs: ['fileItems$',
        'gobiiExtractFilterType',
        'notifyOnInit',
        'nameIdRequestParams',
        'doTreeNotifications'],
    outputs: ['onNameIdSelected', 'onError'],
    template: `<select (change)="handleFileItemSelected($event)">
        <option *ngFor="let fileItem of fileItems$ | async"
                [value]="fileItem.getFileItemUniqueId()">{{fileItem.getItemName()}}
        </option>
    </select>
    ` // end template

})


export class NameIdListBoxComponent implements OnInit, OnChanges, DoCheck {


    // keep all of these until we change the interface
    public fileItems$: Observable<GobiiFileItem[]>;
    private gobiiExtractFilterType: GobiiExtractFilterType = GobiiExtractFilterType.UNKNOWN;
    private notifyOnInit: boolean = false;
    private nameIdRequestParams: NameIdRequestParams;
    private doTreeNotifications: boolean = true;
    public onNameIdSelected: EventEmitter<NameId> = new EventEmitter();
    public onError: EventEmitter<HeaderStatusMessage> = new EventEmitter();

    //private uniqueId:string;

    differ: any;



    constructor(private store: Store<fromRoot.State>,
                private _nameIdService: NameIdService,
                private fileItemService:FileItemService,

                //                private _fileModelTreeService: FileModelTreeService,
                private differs: KeyValueDiffers) {


        this.differ = differs.find({}).create(null);

        //      this.fileItems$ = store.select(fromRoot.getAllFileItems);
//       this.fileItems$ = store.select(fromRoot.getMapsets);


    } // ctor

    // private notificationSent = false;

    ngOnInit(): any {

    }

    private handleHeaderStatus(headerStatusMessage: HeaderStatusMessage) {

        this.onError.emit(headerStatusMessage);
    }


    previousSelectedItemId:string = null;
    public handleFileItemSelected(arg) {

        let currentFileItemUniqueId:string = arg.currentTarget.value;

        this.store.select(fromRoot.getAllFileItems)
            .subscribe(all => {
                let selectedFileItem: GobiiFileItem = all.find(fi => fi.getFileItemUniqueId() === currentFileItemUniqueId);
                if(( selectedFileItem.getProcessType() !== ProcessType.DUMMY )
                    && (selectedFileItem.getExtractorItemType() !== ExtractorItemType.LABEL) ) {

                    this.previousSelectedItemId = currentFileItemUniqueId;
                    this.store.dispatch(new fileAction.SelectForExtractAction(selectedFileItem));

                    this.onNameIdSelected
                        .emit(new NameId(selectedFileItem.getItemId(),
                            selectedFileItem.getItemName(),
                            selectedFileItem.getEntityType()));

                } else {
                    if( this.previousSelectedItemId ) {

                        let previousItem: GobiiFileItem = all.find(fi => fi.getFileItemUniqueId() === this.previousSelectedItemId);
                        this.store.dispatch(new fileAction.DeSelectForExtractAction(previousItem));

                        this.onNameIdSelected
                            .emit(new NameId(previousItem.getItemId(),
                                previousItem.getItemName(),
                                previousItem.getEntityType()));
                    }
                }
            }).unsubscribe(); //unsubscribe or else this subscribe() keeps the state collection locked and the app freezes really badly


        // if (this.currentSelection.getItemId() !== "0") {
        //     this.currentSelection.setProcessType(ProcessType.DELETE);
        //     this.updateTreeService(this.currentSelection);
        // }


//        let gobiiFileItem: GobiiFileItem = this.fileItemList[arg.srcElement.selectedIndex]
//         let gobiiFileItem: GobiiFileItem = this.fileItemList.find(fi => {
//             return fi.getItemId() === this.selectedFileItemId
//         });
//
//         if (gobiiFileItem.getItemId() != "0") {
//             gobiiFileItem.setProcessType(ProcessType.UPDATE);
//             this.updateTreeService(gobiiFileItem);
//         }
//
//         this.currentSelection = gobiiFileItem;

    }


    ngOnChanges(changes: { [propName: string]: SimpleChange }) {


        if (changes['gobiiExtractFilterType']
            && ( changes['gobiiExtractFilterType'].currentValue != null )
            && ( changes['gobiiExtractFilterType'].currentValue != undefined )) {

            // this.fileItems$.subscribe(null, null, () => {
            //
            // });


            if (changes['gobiiExtractFilterType'].currentValue != changes['gobiiExtractFilterType'].previousValue) {

                //this.notificationSent = false;

                //this.nameIdRequestParams.setGobiiExtractFilterType(this.gobiiExtractFilterType);

                // let scope$ = this;
                // this._fileModelTreeService
                //     .fileItemNotifications()
                //     .subscribe(fileItem => {
                //         if (fileItem.getProcessType() === ProcessType.NOTIFY
                //             && fileItem.getExtractorItemType() === ExtractorItemType.STATUS_DISPLAY_TREE_READY) {
                //
                //             scope$.initializeFileItems();
                //
                //
                //         }
                //     });

            } // if we have a new filter type

        } // if filter type changed


        if (changes['nameIdRequestParams']
            && ( changes['nameIdRequestParams'].currentValue != null )
            && ( changes['nameIdRequestParams'].currentValue != undefined )) {

        }

    } // ngonChanges


    // angular change detection does not do deep comparison of objects that are
    // template properties. So we need to do some specialized change detection
    // references:
    //   https://juristr.com/blog/2016/04/angular2-change-detection/
    //   https://angular.io/docs/ts/latest/api/core/index/DoCheck-class.html
    //   http://blog.angular-university.io/how-does-angular-2-change-detection-really-work/
    //   https://blog.thoughtram.io/angular/2016/02/22/angular-2-change-detection-explained.html#what-causes-change
    ngDoCheck(): void {

        var changes = this.differ.diff(this.nameIdRequestParams);

        if (changes) {
            if (this._nameIdService.validateRequest(this.nameIdRequestParams)) {
                //this.initializeFileItems();
            }
        }
    }
} // class
