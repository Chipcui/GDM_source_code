import {Component, EventEmitter, OnInit, ViewChild} from "@angular/core";
import {FileItem, FileUploader, FileUploaderOptions, Headers} from 'ng2-file-upload';
import {AuthenticationService} from "../services/core/authentication.service";
import {HeaderNames} from "../model/header-names";
import {HeaderStatusMessage} from "../model/dto-header-status-message";
import {FileName} from "../model/file_name";
import {GobiiFileItem} from "../model/gobii-file-item";
import {GobiiExtractFilterType} from "../model/type-extractor-filter";
import {ProcessType} from "../model/type-process";
import {ExtractorItemType} from "../model//type-extractor-item";
import {Store} from "@ngrx/store";
import * as fromRoot from '../store/reducers';
import {Observable} from "rxjs/Observable";
import {NameIdFileItemService} from "../services/core/nameid-file-item-service";
import * as historyAction from '../store/actions/history-action';
import {EntityType} from "../model/type-entity";

const URL = 'gobii/v1/files/{gobiiJobId}/EXTRACTOR_INSTRUCTIONS?fileName=';

@Component({
    selector: 'uploader',
    inputs: ['gobiiExtractFilterType','targetEntityType'],
    outputs: ['onUploaderError', 'onClickBrowse'],
    template: `
        <style>
            .my-drop-zone {
                border: dotted 3px lightgray;
            }

            .nv-file-over {
                border: dotted 3px red;
            }

            /* Default class applied to drop zones on over */
            .another-file-over-class {
                border: dotted 3px green;
            }

            html, body {
                height: 100%;
            }
        </style>

        <div class="container">

            <div class="row">

                <div class="col-md-3">

                    <!-- DROP ZONES AND MULTI-FILE SELECT, UNUSED FOR NOW ====================== 
                    <h3>Select files</h3>
        
                    <div ng2FileDrop
                         [ngClass]="{'nv-file-over': hasBaseDropZoneOver}"
                         (fileOver)="fileOverBase($event)"
                         [uploader]="uploader"
                         class="well my-drop-zone">
                        Base drop zone
                    </div>
        
                    <div ng2FileDrop
                         [ngClass]="{'another-file-over-class': hasAnotherDropZoneOver}"
                         (fileOver)="fileOverAnother($event)"
                         [uploader]="uploader"
                         class="well my-drop-zone">
                        Another drop zone
                    </div>
        
                    Multiple
                    <input type="file" ng2FileSelect [uploader]="uploader" multiple /><br/>
                    ================================================================================ -->


                    <input #selectedFile
                           type="file"
                           ng2FileSelect
                           [uploader]="uploader"
                           [disabled]="uploadComplete"
                           (click)="handleClickBrowse($event)"/>
                    <!--  IF YOU REINSTATE THE QUEUES BELOW THIS BUTTON WILL BE SUPERFLUOUS -->
                    <BR>
                    <button type="button" class="btn btn-success"
                            (click)="uploader.uploadAll()"
                            [disabled]="!uploader.getNotUploadedItems().length">
                        Upload
                    </button>
                </div>

                <div class="col-md-9" style="margin-bottom: 40px">


                    <!-- UPLOAD QUEUE UNUSED FOR NOW =========================================================
                    <h3>Upload queue</h3>
                    <p>Queue length: {{ uploader?.queue?.length }}</p>
        
                    <table class="table">
                        <thead>
                        <tr>
                            <th width="50%">Name</th>
                            <th>Size</th>
                            <th>Progress</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr *ngFor="let item of uploader.queue">
                            <td><strong>{{ item?.file?.name }}</strong></td>
                            <td *ngIf="uploader.isHTML5" nowrap>{{ item?.file?.size/1024/1024 | number:'.2' }} MB</td>
                            <td *ngIf="uploader.isHTML5">
                                <div class="progress" style="margin-bottom: 0;">
                                    <div class="progress-bar" role="progressbar" [ngStyle]="{ 'width': item.progress + '%' }"></div>
                                </div>
                            </td>
                            <td class="text-center">
                                <span *ngIf="item.isSuccess"><i class="glyphicon glyphicon-ok"></i></span>
                                <span *ngIf="item.isCancel"><i class="glyphicon glyphicon-ban-circle"></i></span>
                                <span *ngIf="item.isError"><i class="glyphicon glyphicon-remove"></i></span>
                            </td>
                            <td nowrap>
                                <button type="button" class="btn btn-success btn-xs"
                                        (click)="item.upload()" [disabled]="item.isReady || item.isUploading || item.isSuccess">
                                    <span class="glyphicon glyphicon-upload"></span> Upload
                                </button>
                                <button type="button" class="btn btn-warning btn-xs"
                                        (click)="item.cancel()" [disabled]="!item.isUploading">
                                    <span class="glyphicon glyphicon-ban-circle"></span> Cancel
                                </button>
                                <button type="button" class="btn btn-danger btn-xs"
                                        (click)="item.remove()">
                                    <span class="glyphicon glyphicon-trash"></span> Remove
                                </button>
                            </td>
                        </tr>
                        </tbody>
                    </table>
        
                    <div>
                        <div>
                            Queue progress:
                            <div class="progress" style="">
                                <div class="progress-bar" role="progressbar" [ngStyle]="{ 'width': uploader.progress + '%' }"></div>
                            </div>
                        </div>
                        <button type="button" class="btn btn-success btn-s"
                                (click)="uploader.uploadAll()" [disabled]="!uploader.getNotUploadedItems().length">
                            <span class="glyphicon glyphicon-upload"></span> Upload all
                        </button>
                        <button type="button" class="btn btn-warning btn-s"
                                (click)="uploader.cancelAll()" [disabled]="!uploader.isUploading">
                            <span class="glyphicon glyphicon-ban-circle"></span> Cancel all
                        </button>
                        <button type="button" class="btn btn-danger btn-s"
                                (click)="uploader.clearQueue()" [disabled]="!uploader.queue.length">
                            <span class="glyphicon glyphicon-trash"></span> Remove all
                        </button>
                    </div>
                    == UPLOAD QUEUE UNUSED FOR NOW ========================================================= -->


                </div>

            </div>

        </div>`

})

export class UploaderComponent implements OnInit {

    private onUploaderError: EventEmitter<HeaderStatusMessage> = new EventEmitter();
    private gobiiExtractFilterType: GobiiExtractFilterType = GobiiExtractFilterType.UNKNOWN;
    private targetEntityType:EntityType = EntityType.UNKNOWN;
    public uploadComplete = false;

    constructor(private _authenticationService: AuthenticationService,
                private store: Store<fromRoot.State>,
                private fileItemService: NameIdFileItemService,) {


    } // ctor


    public uploader: FileUploader;

    public hasBaseDropZoneOver: boolean = false;
    public hasAnotherDropZoneOver: boolean = false;

    public fileOverBase(e: any): void {
        this.hasBaseDropZoneOver = e;
    }

    public fileOverAnother(e: any): void {
        this.hasAnotherDropZoneOver = e;
    }

    @ViewChild('selectedFile') selectedFile: any;

    clearSelectedFile() {
        this.selectedFile.nativeElement.value = '';
    }

    private onClickBrowse: EventEmitter<any> = new EventEmitter();

    private handleClickBrowse(event: any) {

        this.onClickBrowse.emit(event);

    }

    ngOnInit(): any {

        let JobId$: Observable<GobiiFileItem> = this.store.select(fromRoot.getJobId);

        JobId$.subscribe(
            fileItemJobId => {

                let jobId: string = fileItemJobId.getItemId();
                let fileUploaderOptions: FileUploaderOptions = {}
                let url: string = URL.replace("{gobiiJobId}", jobId);
                let fileName = FileName.makeFileNameFromJobId(this.targetEntityType, jobId);

                url += fileName;
                fileUploaderOptions.url = url;
                fileUploaderOptions.headers = [];
                fileUploaderOptions.removeAfterUpload = true;

                let authHeader: Headers = {name: '', value: ''};
                authHeader.name = HeaderNames.headerToken;

                let token: string = this._authenticationService.getToken();

                if (token) {
                    authHeader.value = token;

                    fileUploaderOptions.headers.push(authHeader);

                    this.uploader = new FileUploader(fileUploaderOptions);

                    this.uploader.onBeforeUploadItem = (fileItem: FileItem) => {

                        fileItem.file.name = fileName;

                    }

                    this.uploader.onCompleteItem = (item: any, response: any, status: any, headers: any) => {

                        if (status == 200) {
                            let listItemType: ExtractorItemType =
                                this.targetEntityType === EntityType.MARKER ?
                                    ExtractorItemType.MARKER_FILE : ExtractorItemType.SAMPLE_FILE;

                            this.fileItemService.loadFileItem(GobiiFileItem
                                    .build(this.gobiiExtractFilterType, ProcessType.CREATE)
                                    .setExtractorItemType(listItemType)
                                    .setItemId(item.file.name)
                                    .setItemName(item.file.name)
                                    .setIsEphemeral(true),
                                true);

                        } else {

                            this.onUploaderError.emit(new HeaderStatusMessage(response, null, null));

                        }

                    };
                } else {
                    this.onUploaderError.emit(new HeaderStatusMessage("Unauthenticated", null, null));
                }
            });


        this.store.select(fromRoot.getUploadFiles)
            .subscribe(fileFileItems => {


                if (fileFileItems && fileFileItems.length > 0) {
                    this.clearSelectedFile();
                    this.uploadComplete = false;
                }
            }, errorMessage => {
                this.store.dispatch(new historyAction.AddStatusMessageAction(errorMessage));

            });

        // this._fileModelTreeService
        //     .fileItemNotifications()
        //     .subscribe(eventedFileItem => {
        //             if (eventedFileItem.getProcessType() === ProcessType.DELETE) {
        //                 let currentItemType: ExtractorItemType = ExtractorItemType.UNKNOWN;
        //                 if (this.gobiiExtractFilterType === GobiiExtractFilterType.BY_SAMPLE) {
        //                     currentItemType = ExtractorItemType.SAMPLE_FILE;
        //                 } else if (this.gobiiExtractFilterType === GobiiExtractFilterType.BY_MARKER) {
        //                     currentItemType = ExtractorItemType.MARKER_FILE;
        //                 }
        //
        //                 if ((eventedFileItem.getGobiiEventOrigin() === GobiiUIEventOrigin.CRITERIA_TREE)
        //                     && (eventedFileItem.getExtractorItemType() === currentItemType )) {
        //                     this.clearSelectedFile();
        //                     this.uploadComplete = false;
        //                 }
        //             }
        //         },
        //         responseHeader => {
        //             this.onUploaderError.emit(responseHeader);
        //         });
    }
}
