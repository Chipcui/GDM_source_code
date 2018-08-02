System.register(["@angular/core", "../services/core/dto-request.service", "../model/type-process", "../model/gobii-file-item", "../model/server-config", "../model/type-entity", "../services/app/dto-request-item-serverconfigs", "../model/type-extractor-filter", "../model/cv-group", "../model/type-extractor-item", "../model/type-extract-format", "../model/dto-header-status-message", "../model/file_name", "../services/app/dto-request-item-contact", "../services/core/authentication.service", "../model/type-status-level", "@ngrx/store", "../store/reducers", "../store/actions/fileitem-action", "../store/actions/history-action", "../model/file-item-param-names", "../services/core/nameid-file-item-service", "../services/core/instruction-submission-service", "../model/type-extractor-sample-list", "../services/core/entity-file-item-service", "../services/core/filter-service", "../services/core/flex-query-service"], function (exports_1, context_1) {
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
    var core_1, dto_request_service_1, type_process_1, gobii_file_item_1, server_config_1, type_entity_1, dto_request_item_serverconfigs_1, type_extractor_filter_1, cv_group_1, type_extractor_item_1, type_extract_format_1, dto_header_status_message_1, file_name_1, dto_request_item_contact_1, authentication_service_1, type_status_level_1, store_1, fromRoot, fileItemAction, historyAction, file_item_param_names_1, nameid_file_item_service_1, instruction_submission_service_1, type_extractor_sample_list_1, entity_file_item_service_1, filter_service_1, flex_query_service_1, ExtractorRoot;
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
            function (cv_group_1_1) {
                cv_group_1 = cv_group_1_1;
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
            function (nameid_file_item_service_1_1) {
                nameid_file_item_service_1 = nameid_file_item_service_1_1;
            },
            function (instruction_submission_service_1_1) {
                instruction_submission_service_1 = instruction_submission_service_1_1;
            },
            function (type_extractor_sample_list_1_1) {
                type_extractor_sample_list_1 = type_extractor_sample_list_1_1;
            },
            function (entity_file_item_service_1_1) {
                entity_file_item_service_1 = entity_file_item_service_1_1;
            },
            function (filter_service_1_1) {
                filter_service_1 = filter_service_1_1;
            },
            function (flex_query_service_1_1) {
                flex_query_service_1 = flex_query_service_1_1;
            }
        ],
        execute: function () {
            ExtractorRoot = (function () {
                function ExtractorRoot(_dtoRequestServiceContact, _authenticationService, _dtoRequestServiceServerConfigs, store, nameIdFileItemService, entityFileItemService, instructionSubmissionService, changeDetectorRef, filterService, flexQueryService) {
                    var _this = this;
                    this._dtoRequestServiceContact = _dtoRequestServiceContact;
                    this._authenticationService = _authenticationService;
                    this._dtoRequestServiceServerConfigs = _dtoRequestServiceServerConfigs;
                    this.store = store;
                    this.nameIdFileItemService = nameIdFileItemService;
                    this.entityFileItemService = entityFileItemService;
                    this.instructionSubmissionService = instructionSubmissionService;
                    this.changeDetectorRef = changeDetectorRef;
                    this.filterService = filterService;
                    this.flexQueryService = flexQueryService;
                    this.title = 'Gobii Web';
                    // *** You cannot use an Enum directly as a template type parameter, so we need
                    //     to assign them to properties
                    // ************************************************************************
                    //
                    this.nameIdFilterParamTypes = Object.assign({}, file_item_param_names_1.FilterParamNames);
                    this.gobiiExtractFilterTypes = Object.assign({}, type_extractor_filter_1.GobiiExtractFilterType);
                    this.gobiiEntityTypes = Object.assign({}, type_entity_1.EntityType);
                    this.selectedExtractFormat$ = this.store.select(fromRoot.getSelectedFileFormat);
                    this.messages$ = this.store.select(fromRoot.getStatusMessages);
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
                            // this.store.select(fromRoot.getAllFileItems).subscribe(
                            //     all => {
                            //         scope$.currentStatus = "GOBII Server " + gobiiVersion + " ( "+ all.length +" )";
                            //     }
                            // );
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
                            _this.nameIdFileItemService.loadFileItem(gobii_file_item_1.GobiiFileItem.build(scope$.gobiiExtractFilterType, type_process_1.ProcessType.CREATE)
                                .setEntityType(type_entity_1.EntityType.CONTACT)
                                .setEntitySubType(type_entity_1.EntitySubType.CONTACT_SUBMITED_BY)
                                .setCvGroup(cv_group_1.CvGroup.UNKNOWN)
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
                ExtractorRoot.prototype.refreshJobId = function () {
                    var jobId = file_name_1.FileName.makeUniqueFileId();
                    this.nameIdFileItemService.replaceFileItemByCompoundId(gobii_file_item_1.GobiiFileItem.build(this.gobiiExtractFilterType, type_process_1.ProcessType.CREATE)
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
                    else if (tabIndex == 3) {
                        this.gobiiExtractFilterType = type_extractor_filter_1.GobiiExtractFilterType.FLEX_QUERY;
                    }
                    else {
                        this.store.dispatch(new historyAction.AddStatusMessageAction("Unknown tab index for extract filter type " + tabIndex));
                    }
                    this.handleExportTypeSelected(this.gobiiExtractFilterType);
                }; // handleTabPanelChange
                ExtractorRoot.prototype.handleExportTypeSelected = function (arg) {
                    var _this = this;
                    this.filterService.init();
                    //
                    this.store.dispatch(new fileItemAction.RemoveAllFromExtractAction(arg));
                    this.store.dispatch(new fileItemAction.SetExtractType({ gobiiExtractFilterType: arg }));
                    this.flexQueryService.invalidateMarkerSampleCount(true);
                    // this will trigger onchange events in child components
                    this.gobiiExtractFilterType = arg;
                    this.instructionSubmissionService.submitReady(this.gobiiExtractFilterType)
                        .subscribe(function (submistReady) {
                        submistReady ? _this.submitButtonStyle = _this.buttonStyleSubmitReady : _this.submitButtonStyle = _this.buttonStyleSubmitNotReady;
                    });
                    this.refreshJobId();
                    if (this.gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET) {
                        this.nameIdFileItemService.loadNameIdsFromFilterParams(this.gobiiExtractFilterType, file_item_param_names_1.FilterParamNames.CONTACT_PI_FILTER_OPTIONAL, null);
                        this.nameIdFileItemService.loadNameIdsFromFilterParams(this.gobiiExtractFilterType, file_item_param_names_1.FilterParamNames.PROJECT_FILTER_OPTIONAL, null);
                        this.nameIdFileItemService.loadNameIdsFromFilterParams(this.gobiiExtractFilterType, file_item_param_names_1.FilterParamNames.EXPERIMENT_FILTER_OPTIONAL, null);
                        this.filterService.loadFilter(this.gobiiExtractFilterType, file_item_param_names_1.FilterParamNames.DATASET_FILTER_OPTIONAL, null);
                        // this.nameIdFileItemService.setItemLabelType(this.gobiiExtractFilterType,
                        //     FilterParamNames.CONTACT_PI_HIERARCHY_ROOT,
                        //     NameIdLabelType.UNKNOWN);
                    }
                    else if (this.gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE) {
                        this.nameIdFileItemService.loadNameIdsFromFilterParams(this.gobiiExtractFilterType, file_item_param_names_1.FilterParamNames.CONTACT_PI_HIERARCHY_ROOT, null);
                        this.nameIdFileItemService.loadNameIdsFromFilterParams(this.gobiiExtractFilterType, file_item_param_names_1.FilterParamNames.PROJECTS_BY_CONTACT, null);
                        this.nameIdFileItemService.loadNameIdsFromFilterParams(this.gobiiExtractFilterType, file_item_param_names_1.FilterParamNames.CV_DATATYPE, null);
                        this.nameIdFileItemService.loadNameIdsFromFilterParams(this.gobiiExtractFilterType, file_item_param_names_1.FilterParamNames.PLATFORMS, null);
                    }
                    else if (this.gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER) {
                        this.nameIdFileItemService.loadNameIdsFromFilterParams(this.gobiiExtractFilterType, file_item_param_names_1.FilterParamNames.CV_DATATYPE, null);
                        this.nameIdFileItemService.loadNameIdsFromFilterParams(this.gobiiExtractFilterType, file_item_param_names_1.FilterParamNames.PLATFORMS, null);
                    }
                    else if (this.gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.FLEX_QUERY) {
                        this.flexQueryService.loadVertices(file_item_param_names_1.FilterParamNames.FQ_F1_VERTICES);
                    }
                    else {
                        this.store.dispatch(new historyAction.AddStatusMessageAction("Unhandled export filter type: " + type_extractor_filter_1.GobiiExtractFilterType[this.gobiiExtractFilterType]));
                    }
                    this.initializeSubmissionContact();
                    // this.nameIdFileItemService.loadWithFilterParams(this.gobiiExtractFilterType,
                    //     this.nameIdRequestParamsExperiments);
                    this.nameIdFileItemService.loadNameIdsFromFilterParams(this.gobiiExtractFilterType, file_item_param_names_1.FilterParamNames.MAPSETS, null);
                    //changing modes will have nuked the submit as item in the tree, so we need to re-event (sic.) it:
                    var formatItem = gobii_file_item_1.GobiiFileItem
                        .build(this.gobiiExtractFilterType, type_process_1.ProcessType.UPDATE)
                        .setExtractorItemType(type_extractor_item_1.ExtractorItemType.EXPORT_FORMAT)
                        .setItemId(type_extract_format_1.GobiiExtractFormat[type_extract_format_1.GobiiExtractFormat.HAPMAP])
                        .setItemName(type_extract_format_1.GobiiExtractFormat[type_extract_format_1.GobiiExtractFormat[type_extract_format_1.GobiiExtractFormat.HAPMAP]]);
                    this.nameIdFileItemService.replaceFileItemByCompoundId(formatItem);
                    this.nameIdFileItemService
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
                        template: "\n        <BR>\n        <BR>\n        <div class=\"panel panel-default col-md-6 col-md-offset-0\" style=\"width: 95%\">\n            <div class=\"panel-heading\">\n                <img src=\"images/gobii_logo.png\" alt=\"GOBii Project\"/>\n            </div> <!-- panel heading -->\n\n            <div class=\"panel panel-primary\">\n                <div class=\"panel-heading\">\n                    <h3 class=\"panel-title\">{{currentStatus}}\n                        &nbsp;&nbsp;&nbsp;&nbsp;\n                        &nbsp;&nbsp;&nbsp;&nbsp;\n                        &nbsp;&nbsp;&nbsp;&nbsp;\n                        &nbsp;&nbsp;&nbsp;&nbsp;\n                        &nbsp;&nbsp;&nbsp;&nbsp;\n                        &nbsp;&nbsp;&nbsp;&nbsp;\n                        &nbsp;&nbsp;&nbsp;&nbsp;\n                        &nbsp;&nbsp;&nbsp;&nbsp;\n                        &nbsp;&nbsp;&nbsp;&nbsp;\n                        &nbsp;&nbsp;&nbsp;&nbsp;\n                        &nbsp;&nbsp;&nbsp;&nbsp;\n                        &nbsp;&nbsp;&nbsp;&nbsp;\n                        &nbsp;&nbsp;&nbsp;&nbsp;\n                        &nbsp;&nbsp;&nbsp;&nbsp;\n                        &nbsp;&nbsp;&nbsp;&nbsp;\n                        &nbsp;&nbsp;&nbsp;&nbsp;\n                        {{loggedInUser}}</h3>\n                </div> <!-- panel heading  -->\n\n                <BR>\n                <BR>\n                <div class=\"container-fluid\">\n\n                    <div class=\"row\">\n\n                        <div class=\"col-md-8\"> <!-- outer grid column 1: Dataset, Sample, Marker Filtering-->\n\n                            <p-panel header=\"Extract Filtering\">\n                                <!--<p-tabView [style]=\"{'border': '1px solid #336699', 'padding-left': '5px'}\">-->\n                                <p-tabView\n                                        (onChange)=\"handleTabPanelChange($event)\"\n                                        styleClass=\"ui-tabview-panels\">\n                                    <p-tabPanel header=\"By Dataset\">\n                                        <ng-template pTemplate=\"content\"> <!-- lazy-load controls -->\n                                            <div class=\"container-fluid\">\n                                                <div class=\"row\">\n                                                    <div class=\"col-md-12\"> <!-- inner column 2 of main column 1 -->\n                                                        <div class=\"panel panel-primary\">\n                                                            <div class=\"panel-heading\">\n                                                                <h3 class=\"panel-title\">Datasets</h3>\n                                                            </div>\n\n                                                            <div class=\"panel-body\">\n\n                                                                <table class=\"table\">\n                                                                    <tbody>\n                                                                    <tr>\n                                                                        <td>\n                                                                            <label class=\"the-label\">Principle\n                                                                                Investigator:</label><BR>\n                                                                            <name-id-list-box\n                                                                                    [gobiiExtractFilterType]=\"gobiiExtractFilterType\"\n                                                                                    [filterParamName]=\"nameIdFilterParamTypes.CONTACT_PI_FILTER_OPTIONAL\">\n                                                                            </name-id-list-box>\n                                                                        </td>\n                                                                        <td>\n                                                                            <label class=\"the-label\">Project:</label><BR>\n                                                                            <name-id-list-box\n                                                                                    [gobiiExtractFilterType]=\"gobiiExtractFilterType\"\n                                                                                    [filterParamName]=\"nameIdFilterParamTypes.PROJECT_FILTER_OPTIONAL\">\n                                                                            </name-id-list-box>\n                                                                        </td>\n                                                                        <td>\n                                                                            <label class=\"the-label\">Experiment:</label><BR>\n                                                                            <name-id-list-box\n                                                                                    [gobiiExtractFilterType]=\"gobiiExtractFilterType\"\n                                                                                    [filterParamName]=\"nameIdFilterParamTypes.EXPERIMENT_FILTER_OPTIONAL\">\n                                                                            </name-id-list-box>\n                                                                        </td>\n                                                                    </tr>\n                                                                    </tbody>\n                                                                </table>\n\n                                                                <dataset-datatable\n                                                                        [gobiiExtractFilterType]=\"gobiiExtractFilterType\">\n                                                                </dataset-datatable>\n                                                            </div> <!-- panel body dataset datatable -->\n                                                        </div> <!-- panel primary dataset datatable -->\n                                                    </div> <!-- data table column -->\n                                                </div> <!-- ROW  -->\n                                            </div> <!-- container  -->\n                                        </ng-template> <!-- lazy-load controls -->\n                                    </p-tabPanel> <!-- tab panel -- dataset -->\n                                    <p-tabPanel header=\"By Samples\">\n                                        <ng-template pTemplate=\"content\"> <!-- lazy-load controls -->\n                                            <div class=\"container-fluid\">\n                                                <div class=\"row\">\n                                                    <div class=\"col-md-4\"> <!-- inner column 1 of main column 1 -->\n                                                        <div class=\"panel panel-primary\">\n                                                            <div class=\"panel-heading\">\n                                                                <h3 class=\"panel-title\">Filters</h3>\n                                                            </div>\n                                                            <div class=\"panel-body\">\n                                                                <label class=\"the-label\">Principle Investigator:</label><BR>\n                                                                <name-id-list-box\n                                                                        [gobiiExtractFilterType]=\"gobiiExtractFilterType\"\n                                                                        [filterParamName]=\"nameIdFilterParamTypes.CONTACT_PI_HIERARCHY_ROOT\">\n                                                                </name-id-list-box>\n\n                                                                <BR>\n                                                                <BR>\n                                                                <label class=\"the-label\">Projects:</label><BR>\n                                                                <name-id-list-box\n                                                                        [gobiiExtractFilterType]=\"gobiiExtractFilterType\"\n                                                                        [filterParamName]=\"nameIdFilterParamTypes.PROJECTS_BY_CONTACT\">\n                                                                </name-id-list-box>\n\n\n                                                                <BR>\n                                                                <BR>\n                                                                <label class=\"the-label\">Dataset Types:</label><BR>\n                                                                <name-id-list-box\n                                                                        [gobiiExtractFilterType]=\"gobiiExtractFilterType\"\n                                                                        [filterParamName]=\"nameIdFilterParamTypes.CV_DATATYPE\">\n                                                                </name-id-list-box>\n\n                                                                <BR>\n                                                                <BR>\n                                                                <label class=\"the-label\">Platforms:</label><BR>\n                                                                <checklist-box\n                                                                        [filterParamName]=\"nameIdFilterParamTypes.PLATFORMS\"\n                                                                        [gobiiExtractFilterType]=\"gobiiExtractFilterType\">\n                                                                </checklist-box>\n\n                                                            </div> <!-- panel body by sample filters filters -->\n                                                        </div> <!-- panel by sample filters -->\n                                                    </div> <!-- by sample filter column-->\n                                                    <div class=\"col-md-8\"> <!-- inner column 2 of main column 1 -->\n                                                        <div class=\"panel panel-primary\">\n                                                            <div class=\"panel-heading\">\n                                                                <h3 class=\"panel-title\">Included Samples</h3>\n                                                            </div>\n                                                            <div class=\"panel-body\">\n                                                                <sample-list-type\n                                                                        [gobiiExtractFilterType]=\"gobiiExtractFilterType\"\n                                                                        (onHeaderStatusMessage)=\"handleHeaderStatusMessage($event)\">\n                                                                </sample-list-type>\n                                                                <hr style=\"width: 100%; color: black; height: 1px; background-color:black;\"/>\n                                                                <sample-marker-box\n                                                                        [gobiiExtractFilterType]=\"gobiiExtractFilterType\"\n                                                                        [targetEntityType]=\"gobiiEntityTypes.DNA_SAMPLE\"\n                                                                        (onSampleMarkerError)=\"handleHeaderStatusMessage($event)\">\n                                                                </sample-marker-box>\n                                                            </div> <!-- panel body dataset datatable -->\n                                                        </div> <!-- panel primary dataset datatable -->\n                                                    </div> <!-- data table column -->\n                                                </div> <!-- ROW  -->\n                                            </div> <!-- container  -->\n                                        </ng-template> <!-- lazy-load controls -->\n                                    </p-tabPanel> <!-- tab panel -- samples -->\n                                    <p-tabPanel header=\"By Markers\">\n                                        <ng-template pTemplate=\"content\"> <!-- lazy-load controls -->\n                                            <div class=\"container-fluid\">\n                                                <div class=\"row\">\n                                                    <div class=\"col-md-4\"> <!-- inner column 1 of main column 1 -->\n                                                        <div class=\"panel panel-primary\">\n                                                            <div class=\"panel-heading\">\n                                                                <h3 class=\"panel-title\">Filters</h3>\n                                                            </div>\n                                                            <div class=\"panel-body\">\n                                                                <label class=\"the-label\">Dataset Types:</label><BR>\n                                                                <name-id-list-box\n                                                                        [gobiiExtractFilterType]=\"gobiiExtractFilterType\"\n                                                                        [filterParamName]=\"nameIdFilterParamTypes.CV_DATATYPE\">\n                                                                </name-id-list-box>\n\n                                                                <BR>\n                                                                <BR>\n                                                                <label class=\"the-label\">Platforms:</label><BR>\n                                                                <checklist-box\n                                                                        [filterParamName]=\"nameIdFilterParamTypes.PLATFORMS\"\n                                                                        [gobiiExtractFilterType]=\"gobiiExtractFilterType\">\n                                                                </checklist-box>\n\n                                                            </div> <!-- panel body by sample filters filters -->\n                                                        </div> <!-- panel by sample filters -->\n                                                    </div> <!-- by sample filter column-->\n                                                    <div class=\"col-md-8\"> <!-- inner column 2 of main column 1 -->\n                                                        <div class=\"panel panel-primary\">\n                                                            <div class=\"panel-heading\">\n                                                                <h3 class=\"panel-title\">Included Markers</h3>\n                                                            </div>\n                                                            <div class=\"panel-body\">\n                                                                <sample-marker-box\n                                                                        [gobiiExtractFilterType]=\"gobiiExtractFilterType\"\n                                                                        [targetEntityType]=\"gobiiEntityTypes.MARKER\"\n                                                                        (onSampleMarkerError)=\"handleHeaderStatusMessage($event)\">\n                                                                </sample-marker-box>\n                                                            </div> <!-- panel body dataset datatable -->\n                                                        </div> <!-- panel primary dataset datatable -->\n                                                    </div> <!-- data table column -->\n                                                </div> <!-- ROW  -->\n                                            </div> <!-- container  -->\n                                        </ng-template> <!-- lazy-load controls -->\n                                    </p-tabPanel> <!-- tab panel -- markers -->\n                                    <p-tabPanel header=\"Flex Query\">\n                                        <ng-template pTemplate=\"content\"> <!-- lazy-load controls -->\n                                            <div class=\"container-fluid\">\n                                                <div class=\"row\">\n                                                    <div class=\"col-md-3\"> <!-- inner column 1 of row 1: Filter 1 -->\n                                                        <flex-query-filter\n                                                                [filterParamNameVertices]=\"nameIdFilterParamTypes.FQ_F1_VERTICES\"\n                                                                [filterParamNameVertexValues]=\"nameIdFilterParamTypes.FQ_F1_VERTEX_VALUES\"\n                                                                [gobiiExtractFilterType]=\"gobiiExtractFilterType\">\n                                                        </flex-query-filter>\n                                                    </div> <!-- inner column 1 of row 1: fitler 1 -->\n                                                    <div class=\"col-md-3\"> <!-- inner column 1 of row 1: Filter 1 -->\n                                                        <flex-query-filter\n                                                                [filterParamNameVertices]=\"nameIdFilterParamTypes.FQ_F2_VERTICES\"\n                                                                [filterParamNameVertexValues]=\"nameIdFilterParamTypes.FQ_F2_VERTEX_VALUES\"\n                                                                [gobiiExtractFilterType]=\"gobiiExtractFilterType\">\n                                                        </flex-query-filter>\n                                                    </div> <!-- inner column 2 of row 1: fitler 2 -->\n                                                    <div class=\"col-md-3\"> <!-- inner column 1 of row 1: Filter 1 -->\n                                                        <flex-query-filter\n                                                                [filterParamNameVertices]=\"nameIdFilterParamTypes.FQ_F3_VERTICES\"\n                                                                [filterParamNameVertexValues]=\"nameIdFilterParamTypes.FQ_F3_VERTEX_VALUES\"\n                                                                [gobiiExtractFilterType]=\"gobiiExtractFilterType\">\n                                                        </flex-query-filter>\n                                                    </div> <!-- inner column 3 of row 1: fitler 3 -->\n                                                    <div class=\"col-md-3\"> <!-- inner column 1 of row 1: Filter 1 -->\n                                                        <flex-query-filter\n                                                                [filterParamNameVertices]=\"nameIdFilterParamTypes.FQ_F4_VERTICES\"\n                                                                [filterParamNameVertexValues]=\"nameIdFilterParamTypes.FQ_F4_VERTEX_VALUES\"\n                                                                [gobiiExtractFilterType]=\"gobiiExtractFilterType\">\n                                                        </flex-query-filter>\n                                                    </div> <!-- inner column 4 of row 1: fitler 4 -->\n\n                                                </div> <!-- ROW filters -->\n\n                                                <div clas=\"row\"><!-- ROW marker/sample lists-->\n                                                    <!-- intersect controls -->\n                                                    <div class=\"col-md-6\"> <!-- intersect markers -->\n                                                        <div class=\"panel panel-primary\"> <!-- intersect pamel -->\n                                                            <div class=\"panel-heading\">\n                                                                <h3 class=\"panel-title\">Intersect Markers</h3>\n                                                            </div>\n                                                            <div class=\"panel-body\">\n                                                                <sample-marker-box\n                                                                        [gobiiExtractFilterType]=\"gobiiExtractFilterTypes.FLEX_QUERY\"\n                                                                        [targetEntityType]=\"gobiiEntityTypes.MARKER\"\n                                                                        (onSampleMarkerError)=\"handleHeaderStatusMessage($event)\">\n                                                                </sample-marker-box>\n                                                            </div> <!-- panel body dataset datatable -->\n                                                        </div> <!-- intersect pamel -->\n                                                    </div><!-- intersect markers -->\n                                                    <div class=\"col-md-6\"> <!-- intersect samples -->\n                                                        <div class=\"panel panel-primary\"> <!-- intersect pamel -->\n                                                            <div class=\"panel-heading\">\n                                                                <h3 class=\"panel-title\">Intersect Samples</h3>\n                                                            </div>\n                                                            <div class=\"panel-body\">\n                                                                <sample-list-type\n                                                                        [gobiiExtractFilterType]=\"gobiiExtractFilterTypes.FLEX_QUERY\"\n                                                                        (onHeaderStatusMessage)=\"handleHeaderStatusMessage($event)\">\n                                                                </sample-list-type>\n                                                                <hr style=\"width: 100%; color: black; height: 1px; background-color:black;\"/>\n                                                                <sample-marker-box\n                                                                        [gobiiExtractFilterType]=\"gobiiExtractFilterTypes.FLEX_QUERY\"\n                                                                        [targetEntityType]=\"gobiiEntityTypes.DNA_SAMPLE\"\n                                                                        (onSampleMarkerError)=\"handleHeaderStatusMessage($event)\">\n                                                                </sample-marker-box>\n                                                            </div> <!-- panel body dataset datatable -->\n                                                        </div> <!-- intersect pamel -->\n                                                    </div><!-- intersect samples -->\n                                                </div><!-- ROW marker/sample lists-->\n                                            </div> <!-- container  -->\n                                        </ng-template> <!-- lazy-load controls -->\n                                    </p-tabPanel> <!-- tab panel -- flex query -->\n                                </p-tabView> <!-- tabview -->\n                            </p-panel> <!-- panel -->\n\n                        </div>  <!-- outer grid column 1: Dataset, Sample, Marker Filtering-->\n\n\n                        <p-dialog header=\"System Message\"\n                                  [(visible)]=\"display\"\n                                  (onHide)=\"onHideMessageDialog($event)\"\n                                  [contentStyle]=\"{'width': '400px'}\">\n                            <div class=\"panel panel-primary\">\n                                <div class=\"panel-body\">\n                                    {{currentStatusMessage}}\n                                    <!--<status-display [messages$]=\"messages$\"></status-display>-->\n                                </div> <!-- panel body -->\n\n                            </div> <!-- panel primary -->\n                        </p-dialog>\n\n                        <div class=\"col-md-4\"> <!-- outer grid column 2: Criteria summary -->\n\n                            <div class=\"well\">\n                                <table>\n                                    <tr>\n                                        <td colspan=\"2\">\n                                            <export-format\n                                                    [fileFormat$]=\"selectedExtractFormat$\"\n                                                    [gobiiExtractFilterType]=\"gobiiExtractFilterType\">\n                                            </export-format>\n                                        </td>\n                                        <td style=\"vertical-align: top;\">\n                                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\n                                            <name-id-list-box\n                                                    [gobiiExtractFilterType]=\"gobiiExtractFilterType\"\n                                                    [filterParamName]=\"nameIdFilterParamTypes.MAPSETS\">\n                                            </name-id-list-box>\n                                        </td>\n                                    </tr>\n                                </table>\n\n                            </div>\n\n                            <div class=\"panel panel-primary\">\n                                <div class=\"panel-heading\">\n                                    <h3 class=\"panel-title\">Extraction Criteria</h3>\n                                </div>\n                                <div class=\"panel-body\">\n                                    <status-display-tree\n                                            (mouseenter)=\"handleOnMouseOverSubmit($event,true)\"\n                                            (mouseleave)=\"handleOnMouseOverSubmit($event,false)\"\n                                            [fileItemEventChange]=\"treeFileItemEvent\"\n                                            [gobiiExtractFilterTypeEvent]=\"gobiiExtractFilterType\"\n                                            (onAddMessage)=\"handleHeaderStatusMessage($event)\">\n                                    </status-display-tree>\n\n                                    <BR>\n\n                                    <button type=\"submit\"\n                                            [class]=\"submitButtonStyle\"\n                                            [disabled]=\"submitButtonStyle === buttonStyleSubmitNotReady\"\n                                            (click)=\"handleExtractSubmission()\">Submit\n                                    </button>\n\n                                    <button type=\"clear\"\n                                            [class]=\"clearButtonStyle\"\n                                            (click)=\"handleClearTree()\">Clear\n                                    </button>\n                                    <BR>\n                                    <BR>\n                                    <marker-sample-count\n                                            [gobiiExtractFilterType]=\"gobiiExtractFilterType\">\n                                    </marker-sample-count>\n                                </div> <!-- panel body -->\n                            </div> <!-- panel primary -->\n\n                        </div>  <!-- outer grid column 2: Criteria summary -->\n\n\n                    </div> <!-- .row of outer grid -->\n\n                </div>\n            </div> <!-- panel primary -->\n        </div> <!-- panel-default  -->" // end template
                    }) // @Component
                    ,
                    __metadata("design:paramtypes", [dto_request_service_1.DtoRequestService,
                        authentication_service_1.AuthenticationService,
                        dto_request_service_1.DtoRequestService,
                        store_1.Store,
                        nameid_file_item_service_1.NameIdFileItemService,
                        entity_file_item_service_1.EntityFileItemService,
                        instruction_submission_service_1.InstructionSubmissionService,
                        core_1.ChangeDetectorRef,
                        filter_service_1.FilterService,
                        flex_query_service_1.FlexQueryService])
                ], ExtractorRoot);
                return ExtractorRoot;
            }());
            exports_1("ExtractorRoot", ExtractorRoot);
        }
    };
});
//# sourceMappingURL=app.extractorroot.js.map