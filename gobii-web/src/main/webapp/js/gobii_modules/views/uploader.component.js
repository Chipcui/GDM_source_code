System.register(["@angular/core", "ng2-file-upload", "../services/core/authentication.service", "../model/header-names", "../model/dto-header-status-message", "../model/file_name", "../model/gobii-file-item", "../model/type-extractor-filter", "../model/type-process", "../model//type-extractor-item", "@ngrx/store", "../store/reducers", "../services/core/nameid-file-item-service", "../store/actions/history-action", "../model/type-entity"], function (exports_1, context_1) {
    "use strict";
    var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
        var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
        if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
        else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
        return c > 3 && r && Object.defineProperty(target, key, r), r;
    };
    var __metadata = (this && this.__metadata) || function (k, v) {
        if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
    };
    var __moduleName = context_1 && context_1.id;
    var core_1, ng2_file_upload_1, authentication_service_1, header_names_1, dto_header_status_message_1, file_name_1, gobii_file_item_1, type_extractor_filter_1, type_process_1, type_extractor_item_1, store_1, fromRoot, nameid_file_item_service_1, historyAction, type_entity_1, URL, UploaderComponent;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (ng2_file_upload_1_1) {
                ng2_file_upload_1 = ng2_file_upload_1_1;
            },
            function (authentication_service_1_1) {
                authentication_service_1 = authentication_service_1_1;
            },
            function (header_names_1_1) {
                header_names_1 = header_names_1_1;
            },
            function (dto_header_status_message_1_1) {
                dto_header_status_message_1 = dto_header_status_message_1_1;
            },
            function (file_name_1_1) {
                file_name_1 = file_name_1_1;
            },
            function (gobii_file_item_1_1) {
                gobii_file_item_1 = gobii_file_item_1_1;
            },
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
            },
            function (type_process_1_1) {
                type_process_1 = type_process_1_1;
            },
            function (type_extractor_item_1_1) {
                type_extractor_item_1 = type_extractor_item_1_1;
            },
            function (store_1_1) {
                store_1 = store_1_1;
            },
            function (fromRoot_1) {
                fromRoot = fromRoot_1;
            },
            function (nameid_file_item_service_1_1) {
                nameid_file_item_service_1 = nameid_file_item_service_1_1;
            },
            function (historyAction_1) {
                historyAction = historyAction_1;
            },
            function (type_entity_1_1) {
                type_entity_1 = type_entity_1_1;
            }
        ],
        execute: function () {
            URL = 'gobii/v1/files/{gobiiJobId}/EXTRACTOR_INSTRUCTIONS?fileName=';
            UploaderComponent = (function () {
                function UploaderComponent(_authenticationService, store, fileItemService) {
                    this._authenticationService = _authenticationService;
                    this.store = store;
                    this.fileItemService = fileItemService;
                    this.onUploaderError = new core_1.EventEmitter();
                    this.gobiiExtractFilterType = type_extractor_filter_1.GobiiExtractFilterType.UNKNOWN;
                    this.targetEntityType = type_entity_1.EntityType.UNKNOWN;
                    this.uploadComplete = false;
                    this.hasBaseDropZoneOver = false;
                    this.hasAnotherDropZoneOver = false;
                    this.onClickBrowse = new core_1.EventEmitter();
                } // ctor
                UploaderComponent.prototype.fileOverBase = function (e) {
                    this.hasBaseDropZoneOver = e;
                };
                UploaderComponent.prototype.fileOverAnother = function (e) {
                    this.hasAnotherDropZoneOver = e;
                };
                UploaderComponent.prototype.clearSelectedFile = function () {
                    this.selectedFile.nativeElement.value = '';
                };
                UploaderComponent.prototype.handleClickBrowse = function (event) {
                    this.onClickBrowse.emit(event);
                };
                UploaderComponent.prototype.ngOnInit = function () {
                    var _this = this;
                    var JobId$ = this.store.select(fromRoot.getJobId);
                    JobId$.subscribe(function (fileItemJobId) {
                        var jobId = fileItemJobId.getItemId();
                        var fileUploaderOptions = {};
                        var url = URL.replace("{gobiiJobId}", jobId);
                        var fileName = file_name_1.FileName.makeFileNameFromJobId(_this.targetEntityType, jobId);
                        url += fileName;
                        fileUploaderOptions.url = url;
                        fileUploaderOptions.headers = [];
                        fileUploaderOptions.removeAfterUpload = true;
                        var authHeader = { name: '', value: '' };
                        authHeader.name = header_names_1.HeaderNames.headerToken;
                        var token = _this._authenticationService.getToken();
                        if (token) {
                            authHeader.value = token;
                            fileUploaderOptions.headers.push(authHeader);
                            _this.uploader = new ng2_file_upload_1.FileUploader(fileUploaderOptions);
                            _this.uploader.onBeforeUploadItem = function (fileItem) {
                                fileItem.file.name = fileName;
                            };
                            _this.uploader.onCompleteItem = function (item, response, status, headers) {
                                if (status == 200) {
                                    var listItemType = _this.targetEntityType === type_entity_1.EntityType.MARKER ?
                                        type_extractor_item_1.ExtractorItemType.MARKER_FILE : type_extractor_item_1.ExtractorItemType.SAMPLE_FILE;
                                    _this.fileItemService.loadFileItem(gobii_file_item_1.GobiiFileItem
                                        .build(_this.gobiiExtractFilterType, type_process_1.ProcessType.CREATE)
                                        .setExtractorItemType(listItemType)
                                        .setItemId(item.file.name)
                                        .setItemName(item.file.name)
                                        .setIsEphemeral(true), true);
                                }
                                else {
                                    _this.onUploaderError.emit(new dto_header_status_message_1.HeaderStatusMessage(response, null, null));
                                }
                            };
                        }
                        else {
                            _this.onUploaderError.emit(new dto_header_status_message_1.HeaderStatusMessage("Unauthenticated", null, null));
                        }
                    });
                    this.store.select(fromRoot.getUploadFiles)
                        .subscribe(function (fileFileItems) {
                        if (fileFileItems && fileFileItems.length > 0) {
                            _this.clearSelectedFile();
                            _this.uploadComplete = false;
                        }
                    }, function (errorMessage) {
                        _this.store.dispatch(new historyAction.AddStatusMessageAction(errorMessage));
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
                };
                __decorate([
                    core_1.ViewChild('selectedFile'),
                    __metadata("design:type", Object)
                ], UploaderComponent.prototype, "selectedFile", void 0);
                UploaderComponent = __decorate([
                    core_1.Component({
                        selector: 'uploader',
                        inputs: ['gobiiExtractFilterType', 'targetEntityType'],
                        outputs: ['onUploaderError', 'onClickBrowse'],
                        template: "\n        <style>\n            .my-drop-zone {\n                border: dotted 3px lightgray;\n            }\n\n            .nv-file-over {\n                border: dotted 3px red;\n            }\n\n            /* Default class applied to drop zones on over */\n            .another-file-over-class {\n                border: dotted 3px green;\n            }\n\n            html, body {\n                height: 100%;\n            }\n        </style>\n\n        <div class=\"container\">\n\n            <div class=\"row\">\n\n                <div class=\"col-md-3\">\n\n                    <!-- DROP ZONES AND MULTI-FILE SELECT, UNUSED FOR NOW ====================== \n                    <h3>Select files</h3>\n        \n                    <div ng2FileDrop\n                         [ngClass]=\"{'nv-file-over': hasBaseDropZoneOver}\"\n                         (fileOver)=\"fileOverBase($event)\"\n                         [uploader]=\"uploader\"\n                         class=\"well my-drop-zone\">\n                        Base drop zone\n                    </div>\n        \n                    <div ng2FileDrop\n                         [ngClass]=\"{'another-file-over-class': hasAnotherDropZoneOver}\"\n                         (fileOver)=\"fileOverAnother($event)\"\n                         [uploader]=\"uploader\"\n                         class=\"well my-drop-zone\">\n                        Another drop zone\n                    </div>\n        \n                    Multiple\n                    <input type=\"file\" ng2FileSelect [uploader]=\"uploader\" multiple /><br/>\n                    ================================================================================ -->\n\n\n                    <input #selectedFile\n                           type=\"file\"\n                           ng2FileSelect\n                           [uploader]=\"uploader\"\n                           [disabled]=\"uploadComplete\"\n                           (click)=\"handleClickBrowse($event)\"/>\n                    <!--  IF YOU REINSTATE THE QUEUES BELOW THIS BUTTON WILL BE SUPERFLUOUS -->\n                    <BR>\n                    <button type=\"button\" class=\"btn btn-success\"\n                            (click)=\"uploader.uploadAll()\"\n                            [disabled]=\"!uploader.getNotUploadedItems().length\">\n                        Upload\n                    </button>\n                </div>\n\n                <div class=\"col-md-9\" style=\"margin-bottom: 40px\">\n\n\n                    <!-- UPLOAD QUEUE UNUSED FOR NOW =========================================================\n                    <h3>Upload queue</h3>\n                    <p>Queue length: {{ uploader?.queue?.length }}</p>\n        \n                    <table class=\"table\">\n                        <thead>\n                        <tr>\n                            <th width=\"50%\">Name</th>\n                            <th>Size</th>\n                            <th>Progress</th>\n                            <th>Status</th>\n                            <th>Actions</th>\n                        </tr>\n                        </thead>\n                        <tbody>\n                        <tr *ngFor=\"let item of uploader.queue\">\n                            <td><strong>{{ item?.file?.name }}</strong></td>\n                            <td *ngIf=\"uploader.isHTML5\" nowrap>{{ item?.file?.size/1024/1024 | number:'.2' }} MB</td>\n                            <td *ngIf=\"uploader.isHTML5\">\n                                <div class=\"progress\" style=\"margin-bottom: 0;\">\n                                    <div class=\"progress-bar\" role=\"progressbar\" [ngStyle]=\"{ 'width': item.progress + '%' }\"></div>\n                                </div>\n                            </td>\n                            <td class=\"text-center\">\n                                <span *ngIf=\"item.isSuccess\"><i class=\"glyphicon glyphicon-ok\"></i></span>\n                                <span *ngIf=\"item.isCancel\"><i class=\"glyphicon glyphicon-ban-circle\"></i></span>\n                                <span *ngIf=\"item.isError\"><i class=\"glyphicon glyphicon-remove\"></i></span>\n                            </td>\n                            <td nowrap>\n                                <button type=\"button\" class=\"btn btn-success btn-xs\"\n                                        (click)=\"item.upload()\" [disabled]=\"item.isReady || item.isUploading || item.isSuccess\">\n                                    <span class=\"glyphicon glyphicon-upload\"></span> Upload\n                                </button>\n                                <button type=\"button\" class=\"btn btn-warning btn-xs\"\n                                        (click)=\"item.cancel()\" [disabled]=\"!item.isUploading\">\n                                    <span class=\"glyphicon glyphicon-ban-circle\"></span> Cancel\n                                </button>\n                                <button type=\"button\" class=\"btn btn-danger btn-xs\"\n                                        (click)=\"item.remove()\">\n                                    <span class=\"glyphicon glyphicon-trash\"></span> Remove\n                                </button>\n                            </td>\n                        </tr>\n                        </tbody>\n                    </table>\n        \n                    <div>\n                        <div>\n                            Queue progress:\n                            <div class=\"progress\" style=\"\">\n                                <div class=\"progress-bar\" role=\"progressbar\" [ngStyle]=\"{ 'width': uploader.progress + '%' }\"></div>\n                            </div>\n                        </div>\n                        <button type=\"button\" class=\"btn btn-success btn-s\"\n                                (click)=\"uploader.uploadAll()\" [disabled]=\"!uploader.getNotUploadedItems().length\">\n                            <span class=\"glyphicon glyphicon-upload\"></span> Upload all\n                        </button>\n                        <button type=\"button\" class=\"btn btn-warning btn-s\"\n                                (click)=\"uploader.cancelAll()\" [disabled]=\"!uploader.isUploading\">\n                            <span class=\"glyphicon glyphicon-ban-circle\"></span> Cancel all\n                        </button>\n                        <button type=\"button\" class=\"btn btn-danger btn-s\"\n                                (click)=\"uploader.clearQueue()\" [disabled]=\"!uploader.queue.length\">\n                            <span class=\"glyphicon glyphicon-trash\"></span> Remove all\n                        </button>\n                    </div>\n                    == UPLOAD QUEUE UNUSED FOR NOW ========================================================= -->\n\n\n                </div>\n\n            </div>\n\n        </div>"
                    }),
                    __metadata("design:paramtypes", [authentication_service_1.AuthenticationService,
                        store_1.Store,
                        nameid_file_item_service_1.NameIdFileItemService])
                ], UploaderComponent);
                return UploaderComponent;
            }());
            exports_1("UploaderComponent", UploaderComponent);
        }
    };
});
//# sourceMappingURL=uploader.component.js.map