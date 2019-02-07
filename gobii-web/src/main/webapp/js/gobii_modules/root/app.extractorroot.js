System.register(["@angular/core", "../services/core/dto-request.service", "../model/type-process", "../model/gobii-file-item", "../model/server-config", "../model/type-entity", "../services/app/dto-request-item-serverconfigs", "../model/type-extractor-filter", "../model/cv-filter-type", "../model/type-extractor-item", "../model/type-extract-format", "../model/dto-header-status-message", "../model/file_name", "../services/app/dto-request-item-contact", "../services/core/authentication.service", "../model/type-status-level", "@ngrx/store", "../store/reducers", "../store/actions/fileitem-action", "../store/actions/history-action", "../model/file-item-param-names", "../services/core/file-item-service", "../services/core/instruction-submission-service", "../model/type-extractor-sample-list", "../services/core/view-id-generator-service", "../services/core/type-control"], function (exports_1, context_1) {
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
    var core_1, dto_request_service_1, type_process_1, gobii_file_item_1, server_config_1, type_entity_1, dto_request_item_serverconfigs_1, type_extractor_filter_1, cv_filter_type_1, type_extractor_item_1, type_extract_format_1, dto_header_status_message_1, file_name_1, dto_request_item_contact_1, authentication_service_1, type_status_level_1, store_1, fromRoot, fileItemAction, historyAction, file_item_param_names_1, file_item_service_1, instruction_submission_service_1, type_extractor_sample_list_1, view_id_generator_service_1, type_control_1, ExtractorRoot;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (dto_request_service_1_1) {
                dto_request_service_1 = dto_request_service_1_1;
            },
            function (type_process_1_1) {
                type_process_1 = type_process_1_1;
            },
            function (gobii_file_item_1_1) {
                gobii_file_item_1 = gobii_file_item_1_1;
            },
            function (server_config_1_1) {
                server_config_1 = server_config_1_1;
            },
            function (type_entity_1_1) {
                type_entity_1 = type_entity_1_1;
            },
            function (dto_request_item_serverconfigs_1_1) {
                dto_request_item_serverconfigs_1 = dto_request_item_serverconfigs_1_1;
            },
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
            },
            function (cv_filter_type_1_1) {
                cv_filter_type_1 = cv_filter_type_1_1;
            },
            function (type_extractor_item_1_1) {
                type_extractor_item_1 = type_extractor_item_1_1;
            },
            function (type_extract_format_1_1) {
                type_extract_format_1 = type_extract_format_1_1;
            },
            function (dto_header_status_message_1_1) {
                dto_header_status_message_1 = dto_header_status_message_1_1;
            },
            function (file_name_1_1) {
                file_name_1 = file_name_1_1;
            },
            function (dto_request_item_contact_1_1) {
                dto_request_item_contact_1 = dto_request_item_contact_1_1;
            },
            function (authentication_service_1_1) {
                authentication_service_1 = authentication_service_1_1;
            },
            function (type_status_level_1_1) {
                type_status_level_1 = type_status_level_1_1;
            },
            function (store_1_1) {
                store_1 = store_1_1;
            },
            function (fromRoot_1) {
                fromRoot = fromRoot_1;
            },
            function (fileItemAction_1) {
                fileItemAction = fileItemAction_1;
            },
            function (historyAction_1) {
                historyAction = historyAction_1;
            },
            function (file_item_param_names_1_1) {
                file_item_param_names_1 = file_item_param_names_1_1;
            },
            function (file_item_service_1_1) {
                file_item_service_1 = file_item_service_1_1;
            },
            function (instruction_submission_service_1_1) {
                instruction_submission_service_1 = instruction_submission_service_1_1;
            },
            function (type_extractor_sample_list_1_1) {
                type_extractor_sample_list_1 = type_extractor_sample_list_1_1;
            },
            function (view_id_generator_service_1_1) {
                view_id_generator_service_1 = view_id_generator_service_1_1;
            },
            function (type_control_1_1) {
                type_control_1 = type_control_1_1;
            }
        ],
        execute: function () {
            ExtractorRoot = (function () {
                function ExtractorRoot(_dtoRequestServiceContact, _authenticationService, _dtoRequestServiceServerConfigs, store, fileItemService, instructionSubmissionService, changeDetectorRef, viewIdGeneratorService) {
                    var _this = this;
                    this._dtoRequestServiceContact = _dtoRequestServiceContact;
                    this._authenticationService = _authenticationService;
                    this._dtoRequestServiceServerConfigs = _dtoRequestServiceServerConfigs;
                    this.store = store;
                    this.fileItemService = fileItemService;
                    this.instructionSubmissionService = instructionSubmissionService;
                    this.changeDetectorRef = changeDetectorRef;
                    this.viewIdGeneratorService = viewIdGeneratorService;
                    this.title = 'Gobii Web';
                    // *** You cannot use an Enum directly as a template type parameter, so we need
                    //     to assign them to properties
                    // ************************************************************************
                    //
                    this.nameIdFilterParamTypes = Object.assign({}, file_item_param_names_1.FilterParamNames);
                    this.typeControl = type_control_1.TypeControl;
                    this.selectedExtractFormat$ = this.store.select(fromRoot.getSelectedFileFormat);
                    this.messages$ = this.store.select(fromRoot.getStatusMessages);
                    this.gobiiExtractFilterTypeForExpressions = type_extractor_filter_1.GobiiExtractFilterType;
                    this.loggedInUser = null;
                    this.display = false;
                    this.currentStatusMessage = null;
                    // ********************************************************************
                    // ********************************************** MARKER/SAMPLE selection
                    // ********************************************************************
                    // ********************************************** Extract file submission
                    this.submitButtonStyleDefault = "btn btn-primary";
                    this.buttonStyleSubmitReady = "btn btn-success";
                    this.buttonStyleSubmitNotReady = "btn btn-warning";
                    this.submitButtonStyle = this.buttonStyleSubmitNotReady;
                    this.clearButtonStyle = this.submitButtonStyleDefault;
                    this.messages$.subscribe(function (messages) {
                        if (messages.length > 0) {
                            _this.currentStatusMessage = messages[0];
                            _this.showMessagesDialog();
                        }
                    });
                }
                ExtractorRoot.prototype.showMessagesDialog = function () {
                    this.display = true;
                    //workaround for error when using observable in a
                    //p-dialog; see https://github.com/angular/angular/issues/17572
                    this.changeDetectorRef.detectChanges();
                };
                ExtractorRoot.prototype.onHideMessageDialog = function ($event) {
                    this.handleClearMessages();
                };
                ExtractorRoot.prototype.initializeServerConfigs = function () {
                    var _this = this;
                    var scope$ = this;
                    this._dtoRequestServiceServerConfigs.get(new dto_request_item_serverconfigs_1.DtoRequestItemServerConfigs()).subscribe(function (serverConfigs) {
                        if (serverConfigs && (serverConfigs.length > 0)) {
                            scope$.serverConfigList = serverConfigs;
                            var serverCrop_1 = _this._dtoRequestServiceServerConfigs.getGobiiCropType();
                            var gobiiVersion = _this._dtoRequestServiceServerConfigs.getGobbiiVersion();
                            scope$.selectedServerConfig =
                                scope$.serverConfigList
                                    .filter(function (c) {
                                    return c.crop === serverCrop_1;
                                })[0];
                            _this.handleExportTypeSelected(type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET);
                            //                    scope$.initializeSubmissionContact();
                            scope$.currentStatus = "GOBII Server " + gobiiVersion;
                            //scope$.handleAddMessage("Connected to crop config: " + scope$.selectedServerConfig.crop);
                        }
                        else {
                            scope$.serverConfigList = [new server_config_1.ServerConfig("<ERROR NO SERVERS>", "<ERROR>", "<ERROR>", 0, "")];
                        }
                    }, function (dtoHeaderResponse) {
                        dtoHeaderResponse.statusMessages.forEach(function (m) { return scope$.handleAddMessage("Retrieving server configs: "
                            + m.message); });
                    });
                }; // initializeServerConfigs()
                ExtractorRoot.prototype.initializeSubmissionContact = function () {
                    var _this = this;
                    this.loggedInUser = this._authenticationService.getUserName();
                    var scope$ = this;
                    scope$._dtoRequestServiceContact.get(new dto_request_item_contact_1.DtoRequestItemContact(dto_request_item_contact_1.ContactSearchType.BY_USERNAME, this.loggedInUser)).subscribe(function (contact) {
                        var foo = "foo";
                        if (contact && contact.contactId && contact.contactId > 0) {
                            //loggedInUser
                            _this.fileItemService.loadFileItem(gobii_file_item_1.GobiiFileItem.build(scope$.gobiiExtractFilterType, type_process_1.ProcessType.CREATE)
                                .setEntityType(type_entity_1.EntityType.CONTACT)
                                .setEntitySubType(type_entity_1.EntitySubType.CONTACT_SUBMITED_BY)
                                .setCvFilterType(cv_filter_type_1.CvFilterType.UNKNOWN)
                                .setExtractorItemType(type_extractor_item_1.ExtractorItemType.ENTITY)
                                .setItemName(contact.email)
                                .setItemId(contact.contactId.toLocaleString()), true);
                        }
                        else {
                            scope$.handleAddMessage("There is no contact associated with user " + _this.loggedInUser);
                        }
                    }, function (dtoHeaderResponse) {
                        dtoHeaderResponse.statusMessages.forEach(function (m) { return scope$.handleAddMessage("Retrieving contacts for submission: "
                            + m.message); });
                    });
                    //   _dtoRequestServiceContact
                };
                ExtractorRoot.prototype.handleServerSelected = function (arg) {
                    this.selectedServerConfig = arg;
                    var currentPath = window.location.pathname;
                    var currentPage = currentPath.substr(currentPath.lastIndexOf('/') + 1, currentPath.length);
                    var newDestination = "http://"
                        + this.selectedServerConfig.domain
                        + ":"
                        + this.selectedServerConfig.port
                        + this.selectedServerConfig.contextRoot
                        + currentPage;
                    window.location.href = newDestination;
                }; // handleServerSelected()
                ExtractorRoot.prototype.refreshJobId = function () {
                    var jobId = file_name_1.FileName.makeUniqueFileId();
                    this.fileItemService.replaceFileItemByCompoundId(gobii_file_item_1.GobiiFileItem.build(this.gobiiExtractFilterType, type_process_1.ProcessType.CREATE)
                        .setExtractorItemType(type_extractor_item_1.ExtractorItemType.JOB_ID)
                        .setItemId(jobId)
                        .setItemName(jobId)
                        .setIsExtractCriterion(true));
                };
                ExtractorRoot.prototype.handleTabPanelChange = function (event) {
                    var tabIndex = event.index;
                    if (tabIndex === 0) {
                        this.gobiiExtractFilterType = type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET;
                    }
                    else if (tabIndex == 1) {
                        this.gobiiExtractFilterType = type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE;
                    }
                    else if (tabIndex == 2) {
                        this.gobiiExtractFilterType = type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER;
                    }
                    this.handleExportTypeSelected(this.gobiiExtractFilterType);
                }; // handleTabPanelChange
                ExtractorRoot.prototype.handleExportTypeSelected = function (arg) {
                    var _this = this;
                    this.store.dispatch(new fileItemAction.RemoveAllFromExtractAction(arg));
                    this.store.dispatch(new fileItemAction.SetExtractType({ gobiiExtractFilterType: arg }));
                    this.gobiiExtractFilterType = arg;
                    this.instructionSubmissionService.submitReady(this.gobiiExtractFilterType)
                        .subscribe(function (submistReady) {
                        submistReady ? _this.submitButtonStyle = _this.buttonStyleSubmitReady : _this.submitButtonStyle = _this.buttonStyleSubmitNotReady;
                    });
                    this.refreshJobId();
                    if (this.gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET) {
                        this.fileItemService.loadNameIdsFromFilterParams(this.gobiiExtractFilterType, file_item_param_names_1.FilterParamNames.CONTACT_PI_FILTER_OPTIONAL, null);
                        this.fileItemService.loadNameIdsFromFilterParams(this.gobiiExtractFilterType, file_item_param_names_1.FilterParamNames.PROJECT_FILTER_OPTIONAL, null);
                        this.fileItemService.loadNameIdsFromFilterParams(this.gobiiExtractFilterType, file_item_param_names_1.FilterParamNames.EXPERIMENT_FILTER_OPTIONAL, null);
                        this.fileItemService.loadFilter(this.gobiiExtractFilterType, file_item_param_names_1.FilterParamNames.DATASET_FILTER_OPTIONAL, null);
                    }
                    else if (this.gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE) {
                        this.fileItemService.loadNameIdsFromFilterParams(this.gobiiExtractFilterType, file_item_param_names_1.FilterParamNames.CONTACT_PI_HIERARCHY_ROOT, null);
                        this.fileItemService.loadNameIdsFromFilterParams(this.gobiiExtractFilterType, file_item_param_names_1.FilterParamNames.PROJECTS_BY_CONTACT, null);
                        this.fileItemService.loadNameIdsFromFilterParams(this.gobiiExtractFilterType, file_item_param_names_1.FilterParamNames.CV_DATATYPE, null);
                        this.fileItemService.loadNameIdsFromFilterParams(this.gobiiExtractFilterType, file_item_param_names_1.FilterParamNames.PLATFORMS, null);
                    }
                    else if (this.gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER) {
                        this.fileItemService.loadNameIdsFromFilterParams(this.gobiiExtractFilterType, file_item_param_names_1.FilterParamNames.CV_DATATYPE, null);
                        this.fileItemService.loadNameIdsFromFilterParams(this.gobiiExtractFilterType, file_item_param_names_1.FilterParamNames.PLATFORMS, null);
                    }
                    this.initializeSubmissionContact();
                    this.fileItemService.loadNameIdsFromFilterParams(this.gobiiExtractFilterType, file_item_param_names_1.FilterParamNames.MAPSETS, null);
                    //changing modes will have nuked the submit as item in the tree, so we need to re-event (sic.) it:
                    var formatItem = gobii_file_item_1.GobiiFileItem
                        .build(this.gobiiExtractFilterType, type_process_1.ProcessType.UPDATE)
                        .setExtractorItemType(type_extractor_item_1.ExtractorItemType.EXPORT_FORMAT)
                        .setItemId(type_extract_format_1.GobiiExtractFormat[type_extract_format_1.GobiiExtractFormat.HAPMAP])
                        .setItemName(type_extract_format_1.GobiiExtractFormat[type_extract_format_1.GobiiExtractFormat[type_extract_format_1.GobiiExtractFormat.HAPMAP]]);
                    this.fileItemService.replaceFileItemByCompoundId(formatItem);
                    this.fileItemService
                        .replaceFileItemByCompoundId(gobii_file_item_1.GobiiFileItem.build(this.gobiiExtractFilterType, type_process_1.ProcessType.CREATE)
                        .setExtractorItemType(type_extractor_item_1.ExtractorItemType.SAMPLE_LIST_TYPE)
                        .setItemName(type_extractor_sample_list_1.GobiiSampleListType[type_extractor_sample_list_1.GobiiSampleListType.GERMPLASM_NAME])
                        .setItemId(type_extractor_sample_list_1.GobiiSampleListType[type_extractor_sample_list_1.GobiiSampleListType.GERMPLASM_NAME]));
                };
                ExtractorRoot.prototype.handleAddMessage = function (arg) {
                    this.store.dispatch(new historyAction.AddStatusAction(new dto_header_status_message_1.HeaderStatusMessage(arg, type_status_level_1.StatusLevel.OK, undefined)));
                };
                ExtractorRoot.prototype.handleClearMessages = function () {
                    this.store.dispatch(new historyAction.ClearStatusesAction());
                };
                ExtractorRoot.prototype.handleHeaderStatusMessage = function (statusMessage) {
                    if (!statusMessage.statusLevel || statusMessage.statusLevel != type_status_level_1.StatusLevel.WARNING) {
                        this.handleAddMessage(statusMessage.message);
                    }
                    else {
                        console.log(statusMessage.message);
                    }
                };
                ExtractorRoot.prototype.handleOnMouseOverSubmit = function (arg, isEnter) {
                    // this.criteriaInvalid = true;
                    if (isEnter) {
                        this.instructionSubmissionService.markMissingItems(this.gobiiExtractFilterType);
                    }
                    else {
                        this.instructionSubmissionService.unmarkMissingItems(this.gobiiExtractFilterType);
                    }
                };
                ExtractorRoot.prototype.handleClearTree = function () {
                    this.handleExportTypeSelected(this.gobiiExtractFilterType);
                };
                ExtractorRoot.prototype.handleExtractSubmission = function () {
                    var _this = this;
                    this.instructionSubmissionService
                        .submit(this.gobiiExtractFilterType)
                        .subscribe(function (instructions) {
                        _this.refreshJobId();
                        _this.handleClearTree();
                    });
                };
                ExtractorRoot.prototype.ngOnInit = function () {
                    this.initializeServerConfigs();
                }; // ngOnInit()
                ExtractorRoot = __decorate([
                    core_1.Component({
                        selector: 'extractor-root',
                        styleUrls: ['/extractor-ui.css'],
                        template: "\n        <BR>\n        <BR>\n        <div class=\"panel panel-default col-md-6 col-md-offset-0\" style=\"width: 95%\">\n            <div class=\"panel-heading\">\n                <img src=\"images/gobii_logo.png\" alt=\"GOBii Project\"/>\n            </div> <!-- panel heading -->\n\n            <div class=\"panel panel-primary\">\n                <div class=\"panel-heading\">\n                    <h3 class=\"panel-title\">{{currentStatus}}\n                        &nbsp;&nbsp;&nbsp;&nbsp;\n                        &nbsp;&nbsp;&nbsp;&nbsp;\n                        &nbsp;&nbsp;&nbsp;&nbsp;\n                        &nbsp;&nbsp;&nbsp;&nbsp;\n                        &nbsp;&nbsp;&nbsp;&nbsp;\n                        &nbsp;&nbsp;&nbsp;&nbsp;\n                        &nbsp;&nbsp;&nbsp;&nbsp;\n                        &nbsp;&nbsp;&nbsp;&nbsp;\n                        &nbsp;&nbsp;&nbsp;&nbsp;\n                        &nbsp;&nbsp;&nbsp;&nbsp;\n                        &nbsp;&nbsp;&nbsp;&nbsp;\n                        &nbsp;&nbsp;&nbsp;&nbsp;\n                        &nbsp;&nbsp;&nbsp;&nbsp;\n                        &nbsp;&nbsp;&nbsp;&nbsp;\n                        &nbsp;&nbsp;&nbsp;&nbsp;\n                        &nbsp;&nbsp;&nbsp;&nbsp;\n                        {{loggedInUser}}</h3>\n                </div> <!-- panel heading  -->\n\n                <BR>\n                <BR>\n                <div class=\"container-fluid\">\n\n                    <div class=\"row\">\n\n                        <div class=\"col-md-8\"> <!-- outer grid column 1: Dataset, Sample, Marker Filtering-->\n\n                            <p-panel header=\"Extract Filtering\">\n                                <!--<p-tabView [style]=\"{'border': '1px solid #336699', 'padding-left': '5px'}\">-->\n                                <p-tabView\n                                        (onChange)=\"handleTabPanelChange($event)\"\n                                        styleClass=\"ui-tabview-panels\"\n                                [id]=\"viewIdGeneratorService.makeStandardId(typeControl.NAVIGATION_TABS)\">\n                                    <p-tabPanel header=\"By Dataset\">\n                                        <ng-template pTemplate=\"content\"> <!-- lazy-load controls -->\n                                            <div class=\"container-fluid\" *ngIf=\"gobiiExtractFilterType === gobiiExtractFilterTypeForExpressions.WHOLE_DATASET\">\n                                                <div class=\"row\">\n                                                    <div class=\"col-md-12\"> <!-- inner column 2 of main column 1 -->\n                                                        <div class=\"panel panel-primary\">\n                                                            <div class=\"panel-heading\">\n                                                                <h3 class=\"panel-title\">Datasets</h3>\n                                                            </div>\n\n                                                            <div class=\"panel-body\">\n\n                                                                <table class=\"table\">\n                                                                    <tbody>\n                                                                    <tr>\n                                                                        <td>\n                                                                            <label class=\"the-label\">Principal\n                                                                                Investigator:</label><BR>\n                                                                            <name-id-list-box\n                                                                                    [gobiiExtractFilterType]=\"gobiiExtractFilterType\"\n                                                                                    [filterParamName]=\"nameIdFilterParamTypes.CONTACT_PI_FILTER_OPTIONAL\">\n                                                                            </name-id-list-box>\n                                                                        </td>\n                                                                        <td>\n                                                                            <label class=\"the-label\">Project:</label><BR>\n                                                                            <name-id-list-box\n                                                                                    [gobiiExtractFilterType]=\"gobiiExtractFilterType\"\n                                                                                    [filterParamName]=\"nameIdFilterParamTypes.PROJECT_FILTER_OPTIONAL\">\n                                                                            </name-id-list-box>\n                                                                        </td>\n                                                                        <td>\n                                                                            <label class=\"the-label\">Experiment:</label><BR>\n                                                                            <name-id-list-box\n                                                                                    [gobiiExtractFilterType]=\"gobiiExtractFilterType\"\n                                                                                    [filterParamName]=\"nameIdFilterParamTypes.EXPERIMENT_FILTER_OPTIONAL\">\n                                                                            </name-id-list-box>\n                                                                        </td>\n                                                                    </tr>\n                                                                    </tbody>\n                                                                </table>\n\n                                                                <dataset-datatable\n                                                                        [gobiiExtractFilterType]=\"gobiiExtractFilterType\">\n                                                                </dataset-datatable>\n                                                            </div> <!-- panel body dataset datatable -->\n                                                        </div> <!-- panel primary dataset datatable -->\n                                                    </div> <!-- data table column -->\n                                                </div> <!-- ROW  -->\n                                            </div> <!-- container  -->\n                                        </ng-template> <!-- lazy-load controls -->\n                                    </p-tabPanel> <!-- tab panel -- dataset -->\n                                    <p-tabPanel header=\"By Samples\">\n                                        <ng-template pTemplate=\"content\"> <!-- lazy-load controls -->\n                                            <div class=\"container-fluid\" *ngIf=\"gobiiExtractFilterType === gobiiExtractFilterTypeForExpressions.BY_SAMPLE\">\n                                                <div class=\"row\">\n                                                    <div class=\"col-md-4\"> <!-- inner column 1 of main column 1 -->\n                                                        <div class=\"panel panel-primary\">\n                                                            <div class=\"panel-heading\">\n                                                                <h3 class=\"panel-title\">Filters</h3>\n                                                            </div>\n                                                            <div class=\"panel-body\">\n                                                                <label class=\"the-label\">Principal Investigator:</label><BR>\n                                                                <name-id-list-box\n                                                                        [gobiiExtractFilterType]=\"gobiiExtractFilterType\"\n                                                                        [filterParamName]=\"nameIdFilterParamTypes.CONTACT_PI_HIERARCHY_ROOT\">\n                                                                </name-id-list-box>\n\n                                                                <BR>\n                                                                <BR>\n                                                                <label class=\"the-label\">Projects:</label><BR>\n                                                                <name-id-list-box\n                                                                        [gobiiExtractFilterType]=\"gobiiExtractFilterType\"\n                                                                        [filterParamName]=\"nameIdFilterParamTypes.PROJECTS_BY_CONTACT\">\n                                                                </name-id-list-box>\n\n\n                                                                <BR>\n                                                                <BR>\n                                                                <label class=\"the-label\">Dataset Types:</label><BR>\n                                                                <name-id-list-box\n                                                                        [gobiiExtractFilterType]=\"gobiiExtractFilterType\"\n                                                                        [filterParamName]=\"nameIdFilterParamTypes.CV_DATATYPE\">\n                                                                </name-id-list-box>\n\n                                                                <BR>\n                                                                <BR>\n                                                                <label class=\"the-label\">Platforms:</label><BR>\n                                                                <checklist-box\n                                                                        [filterParamName]=\"nameIdFilterParamTypes.PLATFORMS\"\n                                                                        [gobiiExtractFilterType]=\"gobiiExtractFilterType\">\n                                                                </checklist-box>\n\n                                                            </div> <!-- panel body by sample filters filters -->\n                                                        </div> <!-- panel by sample filters -->\n                                                    </div> <!-- by sample filter column-->\n                                                    <div class=\"col-md-8\"> <!-- inner column 2 of main column 1 -->\n                                                        <div class=\"panel panel-primary\">\n                                                            <div class=\"panel-heading\">\n                                                                <h3 class=\"panel-title\">Included Samples</h3>\n                                                            </div>\n                                                            <div class=\"panel-body\">\n                                                                <sample-list-type\n                                                                        [gobiiExtractFilterType]=\"gobiiExtractFilterType\"\n                                                                        (onHeaderStatusMessage)=\"handleHeaderStatusMessage($event)\">\n                                                                </sample-list-type>\n                                                                <hr style=\"width: 100%; color: black; height: 1px; background-color:black;\"/>\n                                                                <sample-marker-box\n                                                                        [gobiiExtractFilterType]=\"gobiiExtractFilterType\"\n                                                                        (onSampleMarkerError)=\"handleHeaderStatusMessage($event)\">\n                                                                </sample-marker-box>\n                                                            </div> <!-- panel body dataset datatable -->\n                                                        </div> <!-- panel primary dataset datatable -->\n                                                    </div> <!-- data table column -->\n                                                </div> <!-- ROW  -->\n                                            </div> <!-- container  -->\n                                        </ng-template> <!-- lazy-load controls -->\n                                    </p-tabPanel> <!-- tab panel -- samples -->\n                                    <p-tabPanel header=\"By Markers\">\n                                        <ng-template pTemplate=\"content\"> <!-- lazy-load controls -->\n                                            <div class=\"container-fluid\" *ngIf=\"gobiiExtractFilterType === gobiiExtractFilterTypeForExpressions.BY_MARKER\">\n                                                <div class=\"row\">\n                                                    <div class=\"col-md-4\"> <!-- inner column 1 of main column 1 -->\n                                                        <div class=\"panel panel-primary\">\n                                                            <div class=\"panel-heading\">\n                                                                <h3 class=\"panel-title\">Filters</h3>\n                                                            </div>\n                                                            <div class=\"panel-body\">\n                                                                <label class=\"the-label\">Dataset Types:</label><BR>\n                                                                <name-id-list-box\n                                                                        [gobiiExtractFilterType]=\"gobiiExtractFilterType\"\n                                                                        [filterParamName]=\"nameIdFilterParamTypes.CV_DATATYPE\">\n                                                                </name-id-list-box>\n\n                                                                <BR>\n                                                                <BR>\n                                                                <label class=\"the-label\">Platforms:</label><BR>\n                                                                <checklist-box\n                                                                        [filterParamName]=\"nameIdFilterParamTypes.PLATFORMS\"\n                                                                        [gobiiExtractFilterType]=\"gobiiExtractFilterType\">\n                                                                </checklist-box>\n\n                                                            </div> <!-- panel body by sample filters filters -->\n                                                        </div> <!-- panel by sample filters -->\n                                                    </div> <!-- by sample filter column-->\n                                                    <div class=\"col-md-8\"> <!-- inner column 2 of main column 1 -->\n                                                        <div class=\"panel panel-primary\">\n                                                            <div class=\"panel-heading\">\n                                                                <h3 class=\"panel-title\">Included Markers</h3>\n                                                            </div>\n                                                            <div class=\"panel-body\">\n                                                                <sample-marker-box\n                                                                        [gobiiExtractFilterType]=\"gobiiExtractFilterType\"\n                                                                        (onSampleMarkerError)=\"handleHeaderStatusMessage($event)\">\n                                                                </sample-marker-box>\n                                                            </div> <!-- panel body dataset datatable -->\n                                                        </div> <!-- panel primary dataset datatable -->\n                                                    </div> <!-- data table column -->\n                                                </div> <!-- ROW  -->\n                                            </div> <!-- container  -->\n                                        </ng-template> <!-- lazy-load controls -->\n                                    </p-tabPanel> <!-- tab panel -- markers -->\n                                </p-tabView> <!-- tabview -->\n                            </p-panel> <!-- panel -->\n\n                        </div>  <!-- outer grid column 1: Dataset, Sample, Marker Filtering-->\n\n\n                        <p-dialog header=\"System Message\"\n                                  modal=\"modal\"\n                                  [(visible)]=\"display\"\n                                  (onHide)=\"onHideMessageDialog($event)\"\n                                  [contentStyle]=\"{'width': '400px'}\">\n                            <div class=\"panel panel-primary\">\n                                <div class=\"panel-body\" [id]=\"viewIdGeneratorService.makeStandardId(typeControl.SYSTEM_STATUS_MESSAGE_BODY)\">\n                                    {{currentStatusMessage}}\n                                    <!--<status-display [messages$]=\"messages$\"></status-display>-->\n                                </div> <!-- panel body -->\n\n                            </div> <!-- panel primary -->\n                        </p-dialog>\n\n                        <div class=\"col-md-4\"> <!-- outer grid column 2: Criteria summary -->\n\n                            <div class=\"well\">\n                                <table>\n                                    <tr>\n                                        <td colspan=\"2\">\n                                            <export-format\n                                                    [fileFormat$]=\"selectedExtractFormat$\"\n                                                    [gobiiExtractFilterType]=\"gobiiExtractFilterType\">\n                                            </export-format>\n                                        </td>\n                                        <td style=\"vertical-align: top;\">\n                                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\n                                            <name-id-list-box\n                                                    [gobiiExtractFilterType]=\"gobiiExtractFilterType\"\n                                                    [filterParamName]=\"nameIdFilterParamTypes.MAPSETS\">\n                                            </name-id-list-box>\n                                        </td>\n                                    </tr>\n                                </table>\n\n                            </div>\n\n                            <div class=\"panel panel-primary\">\n                                <div class=\"panel-heading\">\n                                    <h3 class=\"panel-title\">Extraction Criteria</h3>\n                                </div>\n                                <div class=\"panel-body\">\n                                    <status-display-tree\n                                            [fileItemEventChange]=\"treeFileItemEvent\"\n                                            [gobiiExtractFilterTypeEvent]=\"gobiiExtractFilterType\"\n                                            (onAddMessage)=\"handleHeaderStatusMessage($event)\">\n                                    </status-display-tree>\n\n                                    <BR>\n\n                                    <button type=\"submit\"\n                                            [class]=\"submitButtonStyle\"\n                                            [disabled]=\"submitButtonStyle === buttonStyleSubmitNotReady\"\n                                            (mouseenter)=\"handleOnMouseOverSubmit($event,true)\"\n                                            (mouseleave)=\"handleOnMouseOverSubmit($event,false)\"\n                                            (click)=\"handleExtractSubmission()\"\n                                            [id]=\"viewIdGeneratorService.makeStandardId(typeControl.SUBMIT_BUTTON_EXTRACT)\">Submit\n                                    </button>\n\n                                    <button type=\"clear\"\n                                            [class]=\"clearButtonStyle\"\n                                            (click)=\"handleClearTree()\">Clear\n                                    </button>\n\n                                </div> <!-- panel body -->\n                            </div> <!-- panel primary -->\n\n                        </div>  <!-- outer grid column 2: Criteria summary -->\n\n\n                    </div> <!-- .row of outer grid -->\n\n                </div>\n            </div> <!-- panel primary -->\n        </div> <!-- panel-default  -->" // end template
                    }) // @Component
                    ,
                    __metadata("design:paramtypes", [dto_request_service_1.DtoRequestService,
                        authentication_service_1.AuthenticationService,
                        dto_request_service_1.DtoRequestService,
                        store_1.Store,
                        file_item_service_1.FileItemService,
                        instruction_submission_service_1.InstructionSubmissionService,
                        core_1.ChangeDetectorRef,
                        view_id_generator_service_1.ViewIdGeneratorService])
                ], ExtractorRoot);
                return ExtractorRoot;
            }());
            exports_1("ExtractorRoot", ExtractorRoot);
        }
    };
});
//# sourceMappingURL=app.extractorroot.js.map