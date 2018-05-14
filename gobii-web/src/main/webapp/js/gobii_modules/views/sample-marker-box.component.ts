import {Component, EventEmitter, OnChanges, OnInit, SimpleChange, ViewEncapsulation} from "@angular/core";
import {HeaderStatusMessage} from "../model/dto-header-status-message";
import {GobiiExtractFilterType} from "../model/type-extractor-filter";
import {GobiiFileItem} from "../model/gobii-file-item";
import {ProcessType} from "../model/type-process";
import {ExtractorItemType} from "../model//type-extractor-item";
import {Labels} from "./entity-labels";
import {FilterParamNames} from "../model/file-item-param-names";
import * as fromRoot from '../store/reducers';
import {NameIdFileItemService} from "../services/core/nameid-file-item-service";
import {Store} from "@ngrx/store";
import {Observable} from "rxjs/Observable";
import {StatusLevel} from "../model/type-status-level";
import * as historyAction from "../store/actions/history-action";
import {EntityType} from "../model/type-entity";


@Component({
    selector: 'sample-marker-box',
    inputs: ['gobiiExtractFilterType'],
    outputs: ['onSampleMarkerError'],
    encapsulation: ViewEncapsulation.Native,
    styleUrls: ["js/node_modules/primeng/resources/themes/omega/theme.css",
        "js/node_modules/primeng/resources/primeng.css",
        "js/node_modules/bootswatch/dist/cerulean/bootstrap.css"],
    template: `
        <div class="container-fluid">

            <div class="row">

                <p-radioButton
                        (click)="handleOnClickBrowse($event)"
                        name="listType"
                        value="ITEM_FILE_TYPE"
                        [(ngModel)]="selectedListType">
                </p-radioButton>
                <label class="the-legend">File&nbsp;</label>
                <p-radioButton
                        (click)="handleTextBoxChanged($event)"
                        name="listType"
                        value="ITEM_LIST_TYPE"
                        [(ngModel)]="selectedListType">
                </p-radioButton>
                <label class="the-legend">List&nbsp;</label>
                <p-radioButton *ngIf="displayMarkerGroupRadio"
                               (click)="handleMarkerGroupChanged($event)"
                               name="listType"
                               value="MARKER_GROUP_TYPE"
                               [(ngModel)]="selectedListType">
                </p-radioButton>
                <label *ngIf="displayMarkerGroupRadio"
                       class="the-legend">Marker Groups&nbsp;</label>

            </div>

            <div class="row">

                <div *ngIf="displayUploader" class="col-md-8">
                    <uploader
                            [gobiiExtractFilterType]="gobiiExtractFilterType"
                            (onUploaderError)="handleStatusHeaderMessage($event)"></uploader>
                </div>

                <div *ngIf="displayListBox" class="col-md-8">
                    <text-area
                            (onTextboxDataComplete)="handleTextBoxDataSubmitted($event)"></text-area>
                </div>
                <div *ngIf="displayListBox" class="col-md-4">
                    <p class="text-warning">{{maxListItems}} maximum</p>
                </div>

                <div *ngIf="selectedListType == MARKER_GROUP_TYPE" class="col-md-8">
                    <checklist-box
                            [filterParamName]="nameIdFilterParamTypesMarkerGroup"
                            [gobiiExtractFilterType]="gobiiExtractFilterType">
                    </checklist-box>
                </div>

            </div>

            <div>
                <p-dialog header="{{extractTypeLabelExisting}} Already Selelected" [(visible)]="displayChoicePrompt"
                          modal="modal" width="300" height="300" responsive="true">
                    <p>{{extractTypeLabelExisting}} already selected. Specify {{extractTypeLabelProposed}}
                        instead?</p>
                    <p-footer>
                        <div class="ui-dialog-buttonpane ui-widget-content ui-helper-clearfix">
                            <button type="button" pButton icon="fa-close" (click)="handleUserChoice(false)"
                                    label="No"></button>
                            <button type="button" pButton icon="fa-check" (click)="handleUserChoice(true)"
                                    label="Yes"></button>
                        </div>
                    </p-footer>
                </p-dialog>
            </div>
            <div>
                <p-dialog header="Maximum {{maxExceededTypeLabel}} Items Exceeded" [(visible)]="displayMaxItemsExceeded"
                          modal="modal" width="300" height="300" responsive="true">
                    <p>You attempted to paste more than {{maxListItems}} {{maxExceededTypeLabel}} items; Please reduce
                        the size of the list</p>
                </p-dialog>
            </div>`

})

export class SampleMarkerBoxComponent implements OnInit, OnChanges {

    public constructor(private store: Store<fromRoot.State>,
                       private fileItemService: NameIdFileItemService) {

    }

    public nameIdFilterParamTypesMarkerGroup: FilterParamNames = FilterParamNames.MARKER_GROUPS;


    public readonly ITEM_FILE_TYPE = "ITEM_FILE_TYPE";
    public readonly ITEM_LIST_TYPE = "ITEM_LIST_TYPE";
    public readonly MARKER_GROUP_TYPE = "MARKER_GROUP_TYPE";

    public maxListItems: number = 200;
    public displayMaxItemsExceeded: boolean = false;
    public maxExceededTypeLabel: string;

    public displayChoicePrompt: boolean = false;
    public selectedListType: string = this.ITEM_FILE_TYPE;

    public displayUploader: boolean = true;
    public displayListBox: boolean = false;
    public displayMarkerGroupBox: boolean = false;
    public displayMarkerGroupRadio: boolean = false;

    public gobiiExtractFilterType: GobiiExtractFilterType = GobiiExtractFilterType.UNKNOWN;
    public onSampleMarkerError: EventEmitter<HeaderStatusMessage> = new EventEmitter();

    public extractTypeLabelExisting: string;
    public extractTypeLabelProposed: string;


    // private handleUserSelected(arg) {
    //     this.onUserSelected.emit(this.nameIdList[arg.srcElement.selectedIndex].id);
    // }
    //
    // constructor(private _dtoRequestService:DtoRequestService<NameId[]>) {
    //
    // } // ctor

    public handleTextBoxDataSubmitted(items: string[]) {

        if (items.length <= this.maxListItems) {

            let nonDuplicateItems: string[] = [];
            items.forEach(item => {

                if (!nonDuplicateItems.find(ii => ii === item)) {
                    nonDuplicateItems.push(item);
                }


            });

            let listItemType: ExtractorItemType =
                this.gobiiExtractFilterType === GobiiExtractFilterType.BY_MARKER ?
                    ExtractorItemType.MARKER_LIST_ITEM : ExtractorItemType.SAMPLE_LIST_ITEM;

            nonDuplicateItems.forEach(listItem => {

                if (listItem && listItem !== "") {

                    this.fileItemService
                        .loadFileItem(GobiiFileItem.build(this.gobiiExtractFilterType, ProcessType.CREATE)
                            .setExtractorItemType(listItemType)
                            .setItemId(listItem)
                            .setItemName(listItem)
                            .setIsEphemeral(true), true);
                }
            });

        } else {

            if (this.gobiiExtractFilterType === GobiiExtractFilterType.BY_MARKER) {
                this.maxExceededTypeLabel = Labels.instance().treeExtractorTypeLabels[ExtractorItemType.MARKER_LIST_ITEM];
            } else if (this.gobiiExtractFilterType === GobiiExtractFilterType.BY_SAMPLE) {
                this.maxExceededTypeLabel = Labels.instance().treeExtractorTypeLabels[ExtractorItemType.SAMPLE_LIST_ITEM];
            } else {
                this.handleStatusHeaderMessage(new HeaderStatusMessage("This control does not handle the currently selected item type: "
                    + GobiiExtractFilterType[this.gobiiExtractFilterType]
                    , null, null))
            }

            this.displayMaxItemsExceeded = true;
        }
    }


    private makeLabel(inputType: string) {

        let returnVal: string;


        if (this.gobiiExtractFilterType === GobiiExtractFilterType.BY_SAMPLE) {

            if (inputType === this.ITEM_FILE_TYPE) {

                returnVal = Labels.instance().treeExtractorTypeLabels[ExtractorItemType.SAMPLE_FILE];

            } else if (inputType === this.ITEM_LIST_TYPE) {
                returnVal = Labels.instance().treeExtractorTypeLabels[ExtractorItemType.SAMPLE_LIST_ITEM];

            } else {

                this.store.dispatch(new historyAction.AddStatusAction(new HeaderStatusMessage(
                    "Unhandled input type making input type label "
                    + inputType
                    + " for extract type "
                    + GobiiExtractFilterType[this.gobiiExtractFilterType],
                    StatusLevel.ERROR,
                    null)));
            }

        } else if (this.gobiiExtractFilterType === GobiiExtractFilterType.BY_MARKER) {

            if (inputType === this.ITEM_FILE_TYPE) {

                returnVal = Labels.instance().treeExtractorTypeLabels[ExtractorItemType.MARKER_FILE];

            } else if (inputType === this.ITEM_LIST_TYPE) {

                returnVal = Labels.instance().treeExtractorTypeLabels[ExtractorItemType.MARKER_LIST_ITEM];

            } else if (inputType === this.MARKER_GROUP_TYPE) {

                returnVal = Labels.instance().entityNodeLabels[EntityType.MARKER_GROUP];

            } else {

                this.store.dispatch(new historyAction.AddStatusAction(new HeaderStatusMessage(
                    "Unhandled input type making input type label "
                    + inputType
                    + " for extract type "
                    + GobiiExtractFilterType[this.gobiiExtractFilterType],
                    StatusLevel.ERROR,
                    null)));
            }

        } else {
            this.store.dispatch(new historyAction.AddStatusAction(new HeaderStatusMessage(
                "This component is not intended to be used in extract type: " + GobiiExtractFilterType[this.gobiiExtractFilterType],
                StatusLevel.ERROR,
                null)));
        }


        return returnVal;
    }

    private currentFileItems: GobiiFileItem[] = [];

    handleSampleMarkerChoicesExist(previousInputType: string, proposedInputType: string): boolean {

        let returnVal: boolean = false;

        this.store.select(fromRoot.getSelectedFileItems)
            .subscribe(fileItems => {

                    let extractorItemTypeListToFind: ExtractorItemType = ExtractorItemType.UNKNOWN;
                    let extractorItemTypeFileToFind: ExtractorItemType = ExtractorItemType.UNKNOWN;

                    if (this.gobiiExtractFilterType === GobiiExtractFilterType.BY_SAMPLE) {
                        extractorItemTypeListToFind = ExtractorItemType.SAMPLE_LIST_ITEM;
                        extractorItemTypeFileToFind = ExtractorItemType.SAMPLE_FILE;
                    } else if (this.gobiiExtractFilterType === GobiiExtractFilterType.BY_MARKER) {

                        extractorItemTypeListToFind = ExtractorItemType.MARKER_LIST_ITEM;
                        extractorItemTypeFileToFind = ExtractorItemType.MARKER_FILE;
                    }

                    this.currentFileItems = fileItems.filter(item => {
                        return ((item.getExtractorItemType() === extractorItemTypeListToFind) ||
                            (item.getExtractorItemType() === extractorItemTypeFileToFind) ||
                            (item.getExtractorItemType() === ExtractorItemType.ENTITY &&
                                item.getEntityType() === EntityType.MARKER_GROUP))
                    });

                    if (this.currentFileItems.length > 0) {

                        this.extractTypeLabelProposed = this.makeLabel(proposedInputType);
                        this.extractTypeLabelExisting = this.makeLabel(previousInputType);
                        this.displayChoicePrompt = true;
                        returnVal = true;
                        // it does not seem that the PrimeNG dialog really blocks in the usual sense;
                        // so we have to chain what we do next off of the click events on the dialog.
                        // see handleUserChoice()

                    }
                },
                hsm => {
                    this.handleStatusHeaderMessage(hsm)
                }
            ).unsubscribe();

        return returnVal;

    }

    handleUserChoice(userChoice: boolean) {

        this.displayChoicePrompt = false;

        if (this.currentFileItems.length > 0 && userChoice === true) {

            this.setDisplayFlags(this.extractTypeLabelProposed);
            this.currentFileItems.forEach(currentFileItem => {

                currentFileItem.setProcessType(ProcessType.DELETE);
                this.fileItemService
                    .unloadFileItemFromExtract(currentFileItem);
            });

        } else {


            this.setDisplayFlags(this.extractTypeLabelExisting);

        } // if-else user answered "yes"
    }


    private setDisplayFlags(labelValue: string) {


        if (labelValue === this.makeLabel(this.ITEM_LIST_TYPE)) {

            this.displayListBox = true;
            this.displayUploader = false;
            this.displayMarkerGroupBox = false;

            this.selectedListType = this.ITEM_LIST_TYPE;

        } else if (labelValue === this.makeLabel(this.ITEM_FILE_TYPE)) {

            this.displayListBox = false;
            this.displayUploader = true;
            this.displayMarkerGroupBox = false;

            this.selectedListType = this.ITEM_FILE_TYPE;

        } else if (labelValue === this.makeLabel(this.MARKER_GROUP_TYPE)) {
            this.displayListBox = false;
            this.displayUploader = false;
            this.displayMarkerGroupBox = true;

            this.selectedListType = this.MARKER_GROUP_TYPE;

        } else {

            this.store.dispatch(new historyAction.AddStatusAction(new HeaderStatusMessage(
                "Unhandled input type setting display flags for label type "
                + labelValue
                + " for extract type "
                + GobiiExtractFilterType[this.gobiiExtractFilterType],
                StatusLevel.ERROR,
                null)));
        }

    } // setDisplayFlags()

    private getPreviousInputType(): string {

        let returnVal: string;

        if (this.displayListBox) {

            returnVal = this.ITEM_LIST_TYPE;

        } else if (this.displayUploader) {

            returnVal = this.ITEM_FILE_TYPE;

        } else if (this.displayMarkerGroupBox) {

            returnVal = this.MARKER_GROUP_TYPE;

        }

        return returnVal;
    }

    private handleTextBoxChanged(event) {

        // if there is no existing selected list or file, then this is just a simple setting
        if (this.handleSampleMarkerChoicesExist(this.getPreviousInputType(), this.ITEM_LIST_TYPE) === false) {

            this.displayListBox = true;
            this.displayUploader = false;
            this.displayMarkerGroupBox = false;

            // this.displayListBox = true;
            // this.displayUploader = false;

        }
    }

    private handleOnClickBrowse($event) {

        if (this.handleSampleMarkerChoicesExist(this.getPreviousInputType(), this.ITEM_FILE_TYPE) === false) {

            this.displayListBox = false;
            this.displayUploader = true;
            this.displayMarkerGroupBox = false;

            // this.displayListBox = false;
            // this.displayUploader = true;

        }
    }

    handleMarkerGroupChanged($event) {

        if (this.handleSampleMarkerChoicesExist(this.getPreviousInputType(), this.MARKER_GROUP_TYPE) === false) {

            this.displayListBox = false;
            this.displayUploader = false;
            this.displayMarkerGroupBox = true;
        }
    }

    private handleStatusHeaderMessage(statusMessage: HeaderStatusMessage) {

        this.onSampleMarkerError.emit(statusMessage);
    }


    ngOnInit(): any {

        if (this.gobiiExtractFilterType === GobiiExtractFilterType.BY_MARKER) {
            this.fileItemService.loadNameIdsFromFilterParams(this.gobiiExtractFilterType,
                FilterParamNames.MARKER_GROUPS,
                null);
        }

        return null;
    }


    ngOnChanges(changes: { [propName: string]: SimpleChange }) {

        if (changes['gobiiExtractFilterType']
            && (changes['gobiiExtractFilterType'].currentValue != null)
            && (changes['gobiiExtractFilterType'].currentValue != undefined)) {

            if (changes['gobiiExtractFilterType'].currentValue != changes['gobiiExtractFilterType'].previousValue) {

                //this.notificationSent = false;

                if (this.gobiiExtractFilterType == GobiiExtractFilterType.BY_MARKER) {
                    this.displayMarkerGroupRadio = true;
                } else {
                    this.displayMarkerGroupRadio = false
                }

            } // if we have a new filter type

        } // if filter type changed


    } // ngonChanges

}
