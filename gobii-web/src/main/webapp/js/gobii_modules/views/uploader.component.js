System.register(["@angular/core", "ng2-file-upload", "../services/core/authentication.service", "../model/header-names", "../model/dto-header-status-message", "../model/file_name", "../services/core/file-model-tree-service", "../model/gobii-file-item", "../model/type-extractor-filter", "../model/type-process", "../model/file-model-node"], function (exports_1, context_1) {
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
    var core_1, ng2_file_upload_1, authentication_service_1, header_names_1, dto_header_status_message_1, file_name_1, file_model_tree_service_1, gobii_file_item_1, type_extractor_filter_1, type_process_1, file_model_node_1, URL, UploaderComponent;
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
            function (file_model_tree_service_1_1) {
                file_model_tree_service_1 = file_model_tree_service_1_1;
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
            function (file_model_node_1_1) {
                file_model_node_1 = file_model_node_1_1;
            }
        ],
        execute: function () {
            URL = 'gobii/v1/uploadfile?gobiiExtractFilterType=BY_MARKER';
            UploaderComponent = (function () {
                function UploaderComponent(_authenticationService, _fileModelTreeService) {
                    var _this = this;
                    this._authenticationService = _authenticationService;
                    this._fileModelTreeService = _fileModelTreeService;
                    this.onUploaderError = new core_1.EventEmitter();
                    this.gobiiExtractFilterType = type_extractor_filter_1.GobiiExtractFilterType.UNKNOWN;
                    this.hasBaseDropZoneOver = false;
                    this.hasAnotherDropZoneOver = false;
                    var fileUploaderOptions = {};
                    fileUploaderOptions.url = URL;
                    fileUploaderOptions.headers = [];
                    var authHeader = { name: '', value: '' };
                    authHeader.name = header_names_1.HeaderNames.headerToken;
                    var token = _authenticationService.getToken();
                    if (token) {
                        authHeader.value = token;
                        fileUploaderOptions.headers.push(authHeader);
                        this.uploader = new ng2_file_upload_1.FileUploader(fileUploaderOptions);
                        this.uploader.onBeforeUploadItem = function (fileItem) {
                            _this._fileModelTreeService.getFileItems(_this.gobiiExtractFilterType).subscribe(function (fileItems) {
                                var fileItemJobId = fileItems.find(function (item) {
                                    return item.getExtractorItemType() === file_model_node_1.ExtractorItemType.JOB_ID;
                                });
                                var jobId = fileItemJobId.getItemId();
                                fileItem.file.name = file_name_1.FileName.makeFileNameFromJobId(_this.gobiiExtractFilterType, jobId);
                            });
                        };
                        this.uploader.onCompleteItem = function (item, response, status, headers) {
                            if (status == 200) {
                                var listItemType = _this.gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER ?
                                    file_model_node_1.ExtractorItemType.MARKER_FILE : file_model_node_1.ExtractorItemType.SAMPLE_FILE;
                                _fileModelTreeService.put(gobii_file_item_1.GobiiFileItem
                                    .build(_this.gobiiExtractFilterType, type_process_1.ProcessType.CREATE)
                                    .setExtractorItemType(listItemType)
                                    .setItemId(item.file.name)
                                    .setItemName(item.file.name))
                                    .subscribe(null, function (headerStatusMessage) {
                                    _this.onUploaderError.emit(new dto_header_status_message_1.HeaderStatusMessage(headerStatusMessage, null, null));
                                });
                            }
                            else {
                                _this.onUploaderError.emit(new dto_header_status_message_1.HeaderStatusMessage(response, null, null));
                            }
                        };
                    }
                    else {
                        this.onUploaderError.emit(new dto_header_status_message_1.HeaderStatusMessage("Unauthenticated", null, null));
                    }
                } // ctor
                UploaderComponent.prototype.fileOverBase = function (e) {
                    this.hasBaseDropZoneOver = e;
                };
                UploaderComponent.prototype.fileOverAnother = function (e) {
                    this.hasAnotherDropZoneOver = e;
                };
                UploaderComponent.prototype.ngOnInit = function () {
                    return null;
                };
                return UploaderComponent;
            }());
            UploaderComponent = __decorate([
                core_1.Component({
                    selector: 'uploader',
                    inputs: ['gobiiExtractFilterType'],
                    outputs: ['onUploaderError'],
                    template: "<style>\n    .my-drop-zone { border: dotted 3px lightgray; }\n    .nv-file-over { border: dotted 3px red; } /* Default class applied to drop zones on over */\n    .another-file-over-class { border: dotted 3px green; }\n\n    html, body { height: 100%; }\n</style>\n\n<div class=\"container\">\n\n    <div class=\"row\">\n\n        <div class=\"col-md-3\">\n\n            <!-- DROP ZONES AND MULTI-FILE SELECT, UNUSED FOR NOW ====================== \n            <h3>Select files</h3>\n\n            <div ng2FileDrop\n                 [ngClass]=\"{'nv-file-over': hasBaseDropZoneOver}\"\n                 (fileOver)=\"fileOverBase($event)\"\n                 [uploader]=\"uploader\"\n                 class=\"well my-drop-zone\">\n                Base drop zone\n            </div>\n\n            <div ng2FileDrop\n                 [ngClass]=\"{'another-file-over-class': hasAnotherDropZoneOver}\"\n                 (fileOver)=\"fileOverAnother($event)\"\n                 [uploader]=\"uploader\"\n                 class=\"well my-drop-zone\">\n                Another drop zone\n            </div>\n\n            Multiple\n            <input type=\"file\" ng2FileSelect [uploader]=\"uploader\" multiple  /><br/>\n            ================================================================================ -->\n\n            \n            <input type=\"file\" ng2FileSelect [uploader]=\"uploader\" />\n            <!--  IF YOU REINSTATE THE QUEUES BELOW THIS BUTTON WILL BE SUPERFLUOUS -->\n            <button type=\"button\" class=\"btn btn-success btn-s\"\n                        (click)=\"uploader.uploadAll()\" [disabled]=\"!uploader.getNotUploadedItems().length\">\n                    <span class=\"glyphicon glyphicon-upload\"></span> Upload\n            </button>\n\n        </div>\n\n        <div class=\"col-md-9\" style=\"margin-bottom: 40px\">\n\n\n            <!-- UPLOAD QUEUE UNUSED FOR NOW =========================================================\n            <h3>Upload queue</h3>\n            <p>Queue length: {{ uploader?.queue?.length }}</p>\n\n            <table class=\"table\">\n                <thead>\n                <tr>\n                    <th width=\"50%\">Name</th>\n                    <th>Size</th>\n                    <th>Progress</th>\n                    <th>Status</th>\n                    <th>Actions</th>\n                </tr>\n                </thead>\n                <tbody>\n                <tr *ngFor=\"let item of uploader.queue\">\n                    <td><strong>{{ item?.file?.name }}</strong></td>\n                    <td *ngIf=\"uploader.isHTML5\" nowrap>{{ item?.file?.size/1024/1024 | number:'.2' }} MB</td>\n                    <td *ngIf=\"uploader.isHTML5\">\n                        <div class=\"progress\" style=\"margin-bottom: 0;\">\n                            <div class=\"progress-bar\" role=\"progressbar\" [ngStyle]=\"{ 'width': item.progress + '%' }\"></div>\n                        </div>\n                    </td>\n                    <td class=\"text-center\">\n                        <span *ngIf=\"item.isSuccess\"><i class=\"glyphicon glyphicon-ok\"></i></span>\n                        <span *ngIf=\"item.isCancel\"><i class=\"glyphicon glyphicon-ban-circle\"></i></span>\n                        <span *ngIf=\"item.isError\"><i class=\"glyphicon glyphicon-remove\"></i></span>\n                    </td>\n                    <td nowrap>\n                        <button type=\"button\" class=\"btn btn-success btn-xs\"\n                                (click)=\"item.upload()\" [disabled]=\"item.isReady || item.isUploading || item.isSuccess\">\n                            <span class=\"glyphicon glyphicon-upload\"></span> Upload\n                        </button>\n                        <button type=\"button\" class=\"btn btn-warning btn-xs\"\n                                (click)=\"item.cancel()\" [disabled]=\"!item.isUploading\">\n                            <span class=\"glyphicon glyphicon-ban-circle\"></span> Cancel\n                        </button>\n                        <button type=\"button\" class=\"btn btn-danger btn-xs\"\n                                (click)=\"item.remove()\">\n                            <span class=\"glyphicon glyphicon-trash\"></span> Remove\n                        </button>\n                    </td>\n                </tr>\n                </tbody>\n            </table>\n\n            <div>\n                <div>\n                    Queue progress:\n                    <div class=\"progress\" style=\"\">\n                        <div class=\"progress-bar\" role=\"progressbar\" [ngStyle]=\"{ 'width': uploader.progress + '%' }\"></div>\n                    </div>\n                </div>\n                <button type=\"button\" class=\"btn btn-success btn-s\"\n                        (click)=\"uploader.uploadAll()\" [disabled]=\"!uploader.getNotUploadedItems().length\">\n                    <span class=\"glyphicon glyphicon-upload\"></span> Upload all\n                </button>\n                <button type=\"button\" class=\"btn btn-warning btn-s\"\n                        (click)=\"uploader.cancelAll()\" [disabled]=\"!uploader.isUploading\">\n                    <span class=\"glyphicon glyphicon-ban-circle\"></span> Cancel all\n                </button>\n                <button type=\"button\" class=\"btn btn-danger btn-s\"\n                        (click)=\"uploader.clearQueue()\" [disabled]=\"!uploader.queue.length\">\n                    <span class=\"glyphicon glyphicon-trash\"></span> Remove all\n                </button>\n            </div>\n            == UPLOAD QUEUE UNUSED FOR NOW ========================================================= -->\n\n\n        </div>\n\n    </div>\n\n</div>"
                }),
                __metadata("design:paramtypes", [authentication_service_1.AuthenticationService,
                    file_model_tree_service_1.FileModelTreeService])
            ], UploaderComponent);
            exports_1("UploaderComponent", UploaderComponent);
        }
    };
});
//# sourceMappingURL=uploader.component.js.map