import {Component, OnInit, SimpleChange, EventEmitter} from "@angular/core";
import {SampleMarkerList} from "../model/sample-marker-list";
import {HeaderStatusMessage} from "../model/dto-header-status-message";
import {GobiiExtractFilterType} from "../model/type-extractor-filter";
import {FileModelTreeService} from "../services/core/file-model-tree-service";
import {GobiiFileItem} from "../model/gobii-file-item";
import {ProcessType} from "../model/type-process";
import {ExtractorItemType} from "../model/file-model-node";
import {Labels} from "./entity-labels";

@Component({
    selector: 'sample-marker-box',
    inputs: ['gobiiExtractFilterType'],
    outputs: ['onSampleMarkerError'],
    template: `<div class="container-fluid">
            
                <div class="row">
                      <form>
                          <label class="the-legend">File:&nbsp;</label>
                            <input type="radio" 
                                (change)="handleOnClickBrowse($event)" 
                                name="listType" 
                                value="itemFile" 
                                [checked]="uploadSelected">
                          <label class="the-legend">List:&nbsp;</label>
                            <input type="radio" 
                                (change)="handleTextBoxChanged($event)" 
                                name="listType" 
                                value="itemArray"
                                [checked]="listSelected">
                        </form>

                    
                 </div>
                 
                <div class="row">
                
                    <div *ngIf="displayUploader" class="col-md-8">
                        <uploader
                        [gobiiExtractFilterType] = "gobiiExtractFilterType"
                        (onUploaderError)="handleStatusHeaderMessage($event)"
                        (onClickBrowse)="handleOnClickBrowse($event)"></uploader>
                    </div> 
                    
                    <div *ngIf="displayListBox" class="col-md-8">
                        <text-area
                        (onTextboxDataComplete)="handleTextBoxDataSubmitted($event)"
                        (onTextboxClicked)="handleTextBoxChanged($event)"></text-area>
                    </div> 
                    
                 </div>
                 <div>
                    <p-dialog header="{{extractTypeLabelExisting}} Already Selelected" [(visible)]="displayChoicePrompt" modal="modal" width="300" height="300" responsive="true">
                        <p>A {{extractTypeLabelExisting}} is already selected. Do you want to remove it and specify a {{extractTypeLabelProposed}} instead?</p>
                            <p-footer>
                                <div class="ui-dialog-buttonpane ui-widget-content ui-helper-clearfix">
                                    <button type="button" pButton icon="fa-close" (click)="handleUserChoice(false)" label="No"></button>
                                    <button type="button" pButton icon="fa-check" (click)="handleUserChoice(true)" label="Yes"></button>
                                </div>
                            </p-footer>
                    </p-dialog>
                  </div>
`

})

export class SampleMarkerBoxComponent implements OnInit {

    public constructor(private _fileModelTreeService: FileModelTreeService) {

    }

    private displayChoicePrompt: boolean = false;
    private displayListBox:boolean = false;
    private displayUploader:boolean = true;

    private uploadSelected: boolean = true;
    private listSelected: boolean = false;

    private gobiiExtractFilterType: GobiiExtractFilterType = GobiiExtractFilterType.UNKNOWN;
    private onSampleMarkerError: EventEmitter<HeaderStatusMessage> = new EventEmitter();
    private onMarkerSamplesCompleted: EventEmitter<SampleMarkerList> = new EventEmitter();

    private extractTypeLabelExisting: string;
    private extractTypeLabelProposed: string;

    // private handleUserSelected(arg) {
    //     this.onUserSelected.emit(this.nameIdList[arg.srcElement.selectedIndex].id);
    // }
    //
    // constructor(private _dtoRequestService:DtoRequestService<NameId[]>) {
    //
    // } // ctor

    private handleTextBoxDataSubmitted(items: string[]) {

        let listItemType: ExtractorItemType =
            this.gobiiExtractFilterType === GobiiExtractFilterType.BY_MARKER ?
                ExtractorItemType.MARKER_LIST_ITEM : ExtractorItemType.SAMPLE_LIST_ITEM;

        items.forEach(listItem => {

            if (listItem && listItem !== "") {

                this._fileModelTreeService
                    .put(GobiiFileItem.build(this.gobiiExtractFilterType, ProcessType.CREATE)
                        .setExtractorItemType(listItemType)
                        .setItemId(listItem)
                        .setItemName(listItem))
                    .subscribe(null, headerStatusMessage => {
                        this.handleStatusHeaderMessage(headerStatusMessage)
                    });
            }
        });
    }


    private currentFileItems: GobiiFileItem[] = [];

    handleSampleMarkerChoicesExist(): boolean {

        let returnVal: boolean = false;

        this._fileModelTreeService.getFileItems(this.gobiiExtractFilterType).subscribe(
            fileItems => {

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
                    return ( ( item.getExtractorItemType() === extractorItemTypeListToFind ) ||
                    (item.getExtractorItemType() === extractorItemTypeFileToFind) )
                });

                if (this.currentFileItems.length > 0) {

                    this.extractTypeLabelExisting = Labels.instance().treeExtractorTypeLabels[this.currentFileItems[0].getExtractorItemType()];

                    if (this.currentFileItems[0].getExtractorItemType() === ExtractorItemType.SAMPLE_LIST_ITEM) {

                        this.extractTypeLabelProposed = Labels.instance().treeExtractorTypeLabels[ExtractorItemType.SAMPLE_FILE];

                    } else if (this.currentFileItems[0].getExtractorItemType() === ExtractorItemType.MARKER_LIST_ITEM) {

                        this.extractTypeLabelProposed = Labels.instance().treeExtractorTypeLabels[ExtractorItemType.MARKER_FILE];

                    } else if (this.currentFileItems[0].getExtractorItemType() === ExtractorItemType.SAMPLE_FILE) {

                        this.extractTypeLabelProposed = Labels.instance().treeExtractorTypeLabels[ExtractorItemType.MARKER_LIST_ITEM];

                    } else if (this.currentFileItems[0].getExtractorItemType() === ExtractorItemType.MARKER_FILE) {

                        this.extractTypeLabelProposed = Labels.instance().treeExtractorTypeLabels[ExtractorItemType.MARKER_LIST_ITEM];
                    }

                    this.displayChoicePrompt = true;
                    returnVal = true;
                    // it does not seem that the PrimeNG dialog really blocks in the usual sense; 
                    // so we have to chain what we do next off of the click events on the dialog.
                    // see handleUserChoice() 

                } else {

                }
            },
            hsm => {
                this.handleStatusHeaderMessage(hsm)
            }
        );

        // if (event.currentTarget.defaultValue === "itemArray") {
        //
        // } else if (event.currentTarget.defaultValue == "itemFile") {
        //
        // }

        return returnVal;

    }

    handleUserChoice(userChoice: boolean) {

        this.displayChoicePrompt = false;
        
        if (this.currentFileItems.length > 0 && userChoice === true) {

            // based on what _was_ the current item, we now make the current selection the other one
            if (this.currentFileItems[0].getExtractorItemType() === ExtractorItemType.MARKER_LIST_ITEM
                || this.currentFileItems[0].getExtractorItemType() === ExtractorItemType.SAMPLE_LIST_ITEM) {

                this.listSelected = false;
                this.uploadSelected = true;

                this.displayListBox = false;
                this.displayUploader = true;

            } else if (this.currentFileItems[0].getExtractorItemType() === ExtractorItemType.MARKER_FILE
                || this.currentFileItems[0].getExtractorItemType() === ExtractorItemType.SAMPLE_FILE) {

                this.listSelected = true;
                this.uploadSelected = false;

                this.displayListBox = true;
                this.displayUploader = false;

            }
            
            this.currentFileItems.forEach(currentFileItem => {

                currentFileItem.setProcessType(ProcessType.DELETE);
                this._fileModelTreeService
                    .put(currentFileItem)
                    .subscribe(fmte => {

                    }, headerStatusMessage => {
                        this.handleStatusHeaderMessage(headerStatusMessage)
                    });
            });
        }
    }

    private handleTextBoxChanged(event) {

        // if there is no existing selected list or file, then this is just a simple setting
        if (this.handleSampleMarkerChoicesExist() === false) {

            this.listSelected = true;
            this.uploadSelected = false;

            this.displayListBox = true;
            this.displayUploader = false;

        }
    }

    private handleOnClickBrowse($event) {

        if (this.handleSampleMarkerChoicesExist() === false) {

            this.listSelected = false;
            this.uploadSelected = true;

            this.displayListBox = false;
            this.displayUploader = true;

        }
    }

    private handleStatusHeaderMessage(statusMessage: HeaderStatusMessage) {

        this.onSampleMarkerError.emit(statusMessage);
    }

    ngOnInit(): any {

//        this.extractTypeLabel = Labels.instance().extractorFilterTypeLabels[this.gobiiExtractFilterType];
        return null;
    }

}
