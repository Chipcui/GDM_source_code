System.register(["@angular/core", "../../model/type-entity", "../../model/type-extractor-item", "../../model/type-extractor-filter", "../../model/cv-group", "../../model/type-extract-format", "../../store/reducers", "../../store/actions/history-action", "@ngrx/store", "../../model/name-id", "rxjs/Observable", "../../model/type-extractor-sample-list", "../../model/extractor-instructions/data-set-extract", "../../model/extractor-instructions/gobii-extractor-instruction", "../../model/extractor-instructions/dto-extractor-instruction-files", "../app/dto-request-item-extractor-submission", "../../model/type-gobii-file", "./dto-request.service", "../../model/gobii-file-item-compound-id", "../../model/gobii-file-item-criterion", "./tree-structure-service", "./flex-query-service", "../../model/file-item-param-names"], function (exports_1, context_1) {
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
    var core_1, type_entity_1, type_extractor_item_1, type_extractor_filter_1, cv_group_1, type_extract_format_1, fromRoot, historyAction, store_1, name_id_1, Observable_1, type_extractor_sample_list_1, data_set_extract_1, gobii_extractor_instruction_1, dto_extractor_instruction_files_1, dto_request_item_extractor_submission_1, type_gobii_file_1, dto_request_service_1, gobii_file_item_compound_id_1, gobii_file_item_criterion_1, tree_structure_service_1, flex_query_service_1, file_item_param_names_1, InstructionSubmissionService;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (type_entity_1_1) {
                type_entity_1 = type_entity_1_1;
            },
            function (type_extractor_item_1_1) {
                type_extractor_item_1 = type_extractor_item_1_1;
            },
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
            },
            function (cv_group_1_1) {
                cv_group_1 = cv_group_1_1;
            },
            function (type_extract_format_1_1) {
                type_extract_format_1 = type_extract_format_1_1;
            },
            function (fromRoot_1) {
                fromRoot = fromRoot_1;
            },
            function (historyAction_1) {
                historyAction = historyAction_1;
            },
            function (store_1_1) {
                store_1 = store_1_1;
            },
            function (name_id_1_1) {
                name_id_1 = name_id_1_1;
            },
            function (Observable_1_1) {
                Observable_1 = Observable_1_1;
            },
            function (type_extractor_sample_list_1_1) {
                type_extractor_sample_list_1 = type_extractor_sample_list_1_1;
            },
            function (data_set_extract_1_1) {
                data_set_extract_1 = data_set_extract_1_1;
            },
            function (gobii_extractor_instruction_1_1) {
                gobii_extractor_instruction_1 = gobii_extractor_instruction_1_1;
            },
            function (dto_extractor_instruction_files_1_1) {
                dto_extractor_instruction_files_1 = dto_extractor_instruction_files_1_1;
            },
            function (dto_request_item_extractor_submission_1_1) {
                dto_request_item_extractor_submission_1 = dto_request_item_extractor_submission_1_1;
            },
            function (type_gobii_file_1_1) {
                type_gobii_file_1 = type_gobii_file_1_1;
            },
            function (dto_request_service_1_1) {
                dto_request_service_1 = dto_request_service_1_1;
            },
            function (gobii_file_item_compound_id_1_1) {
                gobii_file_item_compound_id_1 = gobii_file_item_compound_id_1_1;
            },
            function (gobii_file_item_criterion_1_1) {
                gobii_file_item_criterion_1 = gobii_file_item_criterion_1_1;
            },
            function (tree_structure_service_1_1) {
                tree_structure_service_1 = tree_structure_service_1_1;
            },
            function (flex_query_service_1_1) {
                flex_query_service_1 = flex_query_service_1_1;
            },
            function (file_item_param_names_1_1) {
                file_item_param_names_1 = file_item_param_names_1_1;
            }
        ],
        execute: function () {
            InstructionSubmissionService = (function () {
                function InstructionSubmissionService(store, dtoRequestServiceExtractorFile, treeStructureService, flexQueryService) {
                    this.store = store;
                    this.dtoRequestServiceExtractorFile = dtoRequestServiceExtractorFile;
                    this.treeStructureService = treeStructureService;
                    this.flexQueryService = flexQueryService;
                    this.datasetCriterion = new gobii_file_item_criterion_1.GobiiFileItemCriterion(new gobii_file_item_compound_id_1.GobiiFileItemCompoundId(type_extractor_item_1.ExtractorItemType.ENTITY, type_entity_1.EntityType.DATASET, type_entity_1.EntitySubType.UNKNOWN, cv_group_1.CvGroup.UNKNOWN, null), false);
                    this.sampleItemCriterion = new gobii_file_item_criterion_1.GobiiFileItemCriterion(new gobii_file_item_compound_id_1.GobiiFileItemCompoundId(type_extractor_item_1.ExtractorItemType.SAMPLE_LIST_ITEM, type_entity_1.EntityType.UNKNOWN, type_entity_1.EntitySubType.UNKNOWN, cv_group_1.CvGroup.UNKNOWN, null), false);
                    this.samplefileCriterion = new gobii_file_item_criterion_1.GobiiFileItemCriterion(new gobii_file_item_compound_id_1.GobiiFileItemCompoundId(type_extractor_item_1.ExtractorItemType.SAMPLE_FILE, type_entity_1.EntityType.UNKNOWN, type_entity_1.EntitySubType.UNKNOWN, cv_group_1.CvGroup.UNKNOWN, null), false);
                    this.piContactCriterion = new gobii_file_item_criterion_1.GobiiFileItemCriterion(new gobii_file_item_compound_id_1.GobiiFileItemCompoundId(type_extractor_item_1.ExtractorItemType.ENTITY, type_entity_1.EntityType.CONTACT, type_entity_1.EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR, cv_group_1.CvGroup.UNKNOWN, null), false);
                    this.projectsCriterion = new gobii_file_item_criterion_1.GobiiFileItemCriterion(new gobii_file_item_compound_id_1.GobiiFileItemCompoundId(type_extractor_item_1.ExtractorItemType.ENTITY, type_entity_1.EntityType.PROJECT, type_entity_1.EntitySubType.UNKNOWN, cv_group_1.CvGroup.UNKNOWN, null), false);
                    this.datasetTypesCriterion = new gobii_file_item_criterion_1.GobiiFileItemCriterion(new gobii_file_item_compound_id_1.GobiiFileItemCompoundId(type_extractor_item_1.ExtractorItemType.ENTITY, type_entity_1.EntityType.CV, type_entity_1.EntitySubType.UNKNOWN, cv_group_1.CvGroup.DATASET_TYPE, null), false);
                    this.markerListItemCriterion = new gobii_file_item_criterion_1.GobiiFileItemCriterion(new gobii_file_item_compound_id_1.GobiiFileItemCompoundId(type_extractor_item_1.ExtractorItemType.MARKER_LIST_ITEM, type_entity_1.EntityType.UNKNOWN, type_entity_1.EntitySubType.UNKNOWN, cv_group_1.CvGroup.UNKNOWN, null), false);
                    this.markerListFileCriterion = new gobii_file_item_criterion_1.GobiiFileItemCriterion(new gobii_file_item_compound_id_1.GobiiFileItemCompoundId(type_extractor_item_1.ExtractorItemType.MARKER_FILE, type_entity_1.EntityType.UNKNOWN, type_entity_1.EntitySubType.UNKNOWN, cv_group_1.CvGroup.UNKNOWN, null), false);
                    this.markergGroupCriterion = new gobii_file_item_criterion_1.GobiiFileItemCriterion(new gobii_file_item_compound_id_1.GobiiFileItemCompoundId(type_extractor_item_1.ExtractorItemType.ENTITY, type_entity_1.EntityType.MARKER_GROUP, type_entity_1.EntitySubType.UNKNOWN, cv_group_1.CvGroup.UNKNOWN, null), false);
                    this.platformCriterion = new gobii_file_item_criterion_1.GobiiFileItemCriterion(new gobii_file_item_compound_id_1.GobiiFileItemCompoundId(type_extractor_item_1.ExtractorItemType.ENTITY, type_entity_1.EntityType.PLATFORM, type_entity_1.EntitySubType.UNKNOWN, cv_group_1.CvGroup.UNKNOWN, null), false);
                }
                InstructionSubmissionService.prototype.isItemPresent = function (gobiiFileItems, gobiiFileItemCriterion) {
                    return gobiiFileItems.filter(function (fi) { return gobiiFileItemCriterion.compoundIdeEquals(fi); }).length > 0;
                };
                InstructionSubmissionService.prototype.unmarkMissingItems = function (gobiiExtractFilterType) {
                    var _this = this;
                    if (gobiiExtractFilterType) {
                        this.store.select(fromRoot.getSelectedFileItems)
                            .subscribe(function (all) {
                            if (gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET) {
                                if (!_this.isItemPresent(all, _this.datasetCriterion)) {
                                    _this.treeStructureService.unMarkTreeItemMissing(gobiiExtractFilterType, _this.datasetCriterion);
                                }
                            }
                            else if (gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE) {
                                _this.treeStructureService.unMarkTreeItemMissing(gobiiExtractFilterType, _this.samplefileCriterion);
                                _this.treeStructureService.unMarkTreeItemMissing(gobiiExtractFilterType, _this.sampleItemCriterion);
                                _this.treeStructureService.unMarkTreeItemMissing(gobiiExtractFilterType, _this.projectsCriterion);
                                _this.treeStructureService.unMarkTreeItemMissing(gobiiExtractFilterType, _this.piContactCriterion);
                                _this.treeStructureService.unMarkTreeItemMissing(gobiiExtractFilterType, _this.datasetTypesCriterion);
                            }
                            else if (gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER) {
                                _this.treeStructureService.unMarkTreeItemMissing(gobiiExtractFilterType, _this.markerListItemCriterion);
                                _this.treeStructureService.unMarkTreeItemMissing(gobiiExtractFilterType, _this.markerListFileCriterion);
                                _this.treeStructureService.unMarkTreeItemMissing(gobiiExtractFilterType, _this.markergGroupCriterion);
                                _this.treeStructureService.unMarkTreeItemMissing(gobiiExtractFilterType, _this.platformCriterion);
                                _this.treeStructureService.unMarkTreeItemMissing(gobiiExtractFilterType, _this.datasetTypesCriterion);
                            }
                            else if (gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.FLEX_QUERY) {
                            }
                            else {
                                _this.store.dispatch(new historyAction.AddStatusMessageAction("Unhandled extract filter type: " + type_extractor_filter_1.GobiiExtractFilterType[gobiiExtractFilterType ? gobiiExtractFilterType : type_extractor_filter_1.GobiiExtractFilterType.UNKNOWN]));
                            }
                        }).unsubscribe();
                    } // if we have an extract filter type
                };
                InstructionSubmissionService.prototype.markMissingItems = function (gobiiExtractFilterType) {
                    var _this = this;
                    if (gobiiExtractFilterType) {
                        this.store.select(fromRoot.getSelectedFileItems)
                            .subscribe(function (all) {
                            if (!_this.areCriteriaMet(all, gobiiExtractFilterType)) {
                                if (gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET) {
                                    if (!_this.isItemPresent(all, _this.datasetCriterion)) {
                                        _this.treeStructureService.markTreeItemMissing(gobiiExtractFilterType, _this.datasetCriterion);
                                    }
                                }
                                else if (gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE) {
                                    if (!_this.isItemPresent(all, _this.samplefileCriterion)) {
                                        _this.treeStructureService.markTreeItemMissing(gobiiExtractFilterType, _this.samplefileCriterion);
                                    }
                                    if (!_this.isItemPresent(all, _this.sampleItemCriterion)) {
                                        _this.treeStructureService.markTreeItemMissing(gobiiExtractFilterType, _this.sampleItemCriterion);
                                    }
                                    if (!_this.isItemPresent(all, _this.projectsCriterion)) {
                                        _this.treeStructureService.markTreeItemMissing(gobiiExtractFilterType, _this.projectsCriterion);
                                    }
                                    if (!_this.isItemPresent(all, _this.piContactCriterion)) {
                                        _this.treeStructureService.markTreeItemMissing(gobiiExtractFilterType, _this.piContactCriterion);
                                    }
                                    if (!_this.isItemPresent(all, _this.datasetTypesCriterion)) {
                                        _this.treeStructureService.markTreeItemMissing(gobiiExtractFilterType, _this.datasetTypesCriterion);
                                    }
                                }
                                else if (gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER) {
                                    if (!_this.isItemPresent(all, _this.markerListItemCriterion)) {
                                        _this.treeStructureService.markTreeItemMissing(gobiiExtractFilterType, _this.markerListItemCriterion);
                                    }
                                    if (!_this.isItemPresent(all, _this.markerListFileCriterion)) {
                                        _this.treeStructureService.markTreeItemMissing(gobiiExtractFilterType, _this.markerListFileCriterion);
                                    }
                                    if (!_this.isItemPresent(all, _this.markergGroupCriterion)) {
                                        _this.treeStructureService.markTreeItemMissing(gobiiExtractFilterType, _this.markergGroupCriterion);
                                    }
                                    if (!_this.isItemPresent(all, _this.platformCriterion)) {
                                        _this.treeStructureService.markTreeItemMissing(gobiiExtractFilterType, _this.platformCriterion);
                                    }
                                    if (!_this.isItemPresent(all, _this.datasetTypesCriterion)) {
                                        _this.treeStructureService.markTreeItemMissing(gobiiExtractFilterType, _this.datasetTypesCriterion);
                                    }
                                }
                                else if (gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.FLEX_QUERY) {
                                    if (!_this.isItemPresent(all, _this.markerListItemCriterion)) {
                                        _this.treeStructureService.markTreeItemMissing(gobiiExtractFilterType, _this.markerListItemCriterion);
                                    }
                                    if (!_this.isItemPresent(all, _this.markerListFileCriterion)) {
                                        _this.treeStructureService.markTreeItemMissing(gobiiExtractFilterType, _this.markerListFileCriterion);
                                    }
                                    if (!_this.isItemPresent(all, _this.markergGroupCriterion)) {
                                        _this.treeStructureService.markTreeItemMissing(gobiiExtractFilterType, _this.markergGroupCriterion);
                                    }
                                    if (!_this.isItemPresent(all, _this.platformCriterion)) {
                                        _this.treeStructureService.markTreeItemMissing(gobiiExtractFilterType, _this.platformCriterion);
                                    }
                                    if (!_this.isItemPresent(all, _this.datasetTypesCriterion)) {
                                        _this.treeStructureService.markTreeItemMissing(gobiiExtractFilterType, _this.datasetTypesCriterion);
                                    }
                                }
                                else {
                                    _this.store.dispatch(new historyAction.AddStatusMessageAction("Unhandled extract filter type: " + type_extractor_filter_1.GobiiExtractFilterType[gobiiExtractFilterType ? gobiiExtractFilterType : type_extractor_filter_1.GobiiExtractFilterType.UNKNOWN]));
                                }
                            }
                        }).unsubscribe();
                    } // if we have an extract filter type
                }; // markMissingItems()
                InstructionSubmissionService.prototype.areCriteriaMet = function (all, gobiiExtractFilterType) {
                    var _this = this;
                    var returnVal = false;
                    if (gobiiExtractFilterType) {
                        if (gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET) {
                            returnVal = this.isItemPresent(all, this.datasetCriterion);
                        }
                        else if (gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE) {
                            var samplesArePresent = this.isItemPresent(all, this.samplefileCriterion)
                                || this.isItemPresent(all, this.sampleItemCriterion);
                            var projectIsPresent = this.isItemPresent(all, this.projectsCriterion);
                            var pIIsPresent = this.isItemPresent(all, this.piContactCriterion);
                            var datasetTypeIsPresent = this.isItemPresent(all, this.datasetTypesCriterion);
                            returnVal =
                                datasetTypeIsPresent &&
                                    (samplesArePresent
                                        || projectIsPresent
                                        || pIIsPresent);
                        }
                        else if (gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER) {
                            var markersArePresent = this.isItemPresent(all, this.markerListItemCriterion)
                                || this.isItemPresent(all, this.markerListFileCriterion);
                            var markerGroupIsPresent = this.isItemPresent(all, this.markergGroupCriterion);
                            var platformIsPresent = this.isItemPresent(all, this.platformCriterion);
                            var datasetTypeIsPresent = this.isItemPresent(all, this.datasetTypesCriterion);
                            returnVal =
                                datasetTypeIsPresent
                                    && (markersArePresent
                                        || markerGroupIsPresent
                                        || platformIsPresent);
                        }
                        else if (gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.FLEX_QUERY) {
                            // submission can only take place when there is at leaset one vertex value filter
                            // set and when there is a marker sample sample count
                            this.flexQueryService.getVertexFilters(file_item_param_names_1.FilterParamNames.FQ_F4_VERTEX_VALUES)
                                .subscribe(function (filters) {
                                if (filters && filters.length > 0) {
                                    var filterVals = filters
                                        .filter(function (vertex) { return (vertex.filterVals && vertex.filterVals.length > 0); });
                                    if (filterVals && filterVals.length > 0) {
                                        _this.store.select(fromRoot.getCurrentMarkerCount)
                                            .subscribe(function (markerCount) {
                                            if (markerCount > 0) {
                                                _this.store.select(fromRoot.getCurrentSampleCount)
                                                    .subscribe(function (sampleCount) {
                                                    if (sampleCount > 0) {
                                                        returnVal = true;
                                                    } // if there is a sample count
                                                }).unsubscribe(); // subscribe/unsubscribe to sample count
                                            } // if there is a marker count
                                        }).unsubscribe(); // subscribe/unsubscribe to marker count
                                    } // if there are filters
                                }
                            })
                                .unsubscribe(); // subscribe/unsubscribe to vertex filters
                        }
                        else {
                            this.store.dispatch(new historyAction.AddStatusMessageAction("Unhandled extract filter type: " + type_extractor_filter_1.GobiiExtractFilterType[gobiiExtractFilterType ? gobiiExtractFilterType : type_extractor_filter_1.GobiiExtractFilterType.UNKNOWN]));
                        }
                    } // if we have an extract type
                    return returnVal;
                };
                InstructionSubmissionService.prototype.submitReady = function (gobiiExtractFilterType) {
                    var _this = this;
                    return Observable_1.Observable.create(function (observer) {
                        _this.store.select(fromRoot.getSelectedFileItems)
                            .subscribe(function (all) {
                            var submistReady = _this.areCriteriaMet(all, gobiiExtractFilterType);
                            observer.next(submistReady);
                        }); // inner subscribe
                    } //observer lambda
                    ); // Observable.crate
                }; // function()
                InstructionSubmissionService.prototype.submit = function (gobiiExtractFilterType) {
                    var _this = this;
                    return Observable_1.Observable.create(function (observer) {
                        var gobiiDataSetExtracts = [];
                        var mapsetIds = [];
                        var submitterContactid = null;
                        var jobId = null;
                        var markerFileName = null;
                        var sampleFileName = null;
                        var sampleListType;
                        _this.store.select(fromRoot.getSelectedFileItems)
                            .subscribe(function (fileItems) {
                            // ******** JOB ID
                            var fileItemJobId = fileItems.find(function (item) {
                                return item.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.JOB_ID;
                            });
                            if (fileItemJobId != null) {
                                jobId = fileItemJobId.getItemId();
                            }
                            // ******** MARKER FILE
                            var fileItemMarkerFile = fileItems.find(function (item) {
                                return item.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.MARKER_FILE;
                            });
                            if (fileItemMarkerFile != null) {
                                markerFileName = fileItemMarkerFile.getItemId();
                            }
                            // ******** SAMPLE FILE
                            var fileItemSampleFile = fileItems.find(function (item) {
                                return item.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.SAMPLE_FILE;
                            });
                            if (fileItemSampleFile != null) {
                                sampleFileName = fileItemSampleFile.getItemId();
                            }
                            // ******** SUBMITTER CONTACT
                            var submitterFileItem = fileItems.find(function (item) {
                                return (item.getEntityType() === type_entity_1.EntityType.CONTACT)
                                    && (item.getEntitySubType() === type_entity_1.EntitySubType.CONTACT_SUBMITED_BY);
                            });
                            submitterContactid = Number(submitterFileItem.getItemId());
                            // ******** MAPSET IDs
                            var mapsetFileItems = fileItems
                                .filter(function (item) {
                                return item.getEntityType() === type_entity_1.EntityType.MAPSET;
                            });
                            mapsetIds = mapsetFileItems
                                .map(function (item) {
                                return Number(item.getItemId());
                            });
                            // ******** EXPORT FORMAT
                            var exportFileItem = fileItems.find(function (item) {
                                return item.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.EXPORT_FORMAT;
                            });
                            // these probably should be just one enum
                            var gobiiFileType = null;
                            var extractFormat = type_extract_format_1.GobiiExtractFormat[exportFileItem.getItemId()];
                            if (extractFormat === type_extract_format_1.GobiiExtractFormat.FLAPJACK) {
                                gobiiFileType = type_gobii_file_1.GobiiFileType.FLAPJACK;
                            }
                            else if (extractFormat === type_extract_format_1.GobiiExtractFormat.HAPMAP) {
                                gobiiFileType = type_gobii_file_1.GobiiFileType.HAPMAP;
                            }
                            else if (extractFormat === type_extract_format_1.GobiiExtractFormat.META_DATA_ONLY) {
                                gobiiFileType = type_gobii_file_1.GobiiFileType.META_DATA;
                            }
                            // ******** DATA SET TYPE
                            var dataTypeFileItem = fileItems.find(function (item) {
                                return item.getEntityType() === type_entity_1.EntityType.CV
                                    && item.getCvGroup() === cv_group_1.CvGroup.DATASET_TYPE;
                            });
                            var datasetType = dataTypeFileItem != null ? new name_id_1.NameId(dataTypeFileItem.getItemId(), null, dataTypeFileItem.getItemName(), type_entity_1.EntityType.CV, null, null) : null;
                            // ******** PRINCIPLE INVESTIGATOR CONCEPT
                            var principleInvestigatorFileItem = fileItems.find(function (item) {
                                return item.getEntityType() === type_entity_1.EntityType.CONTACT
                                    && item.getEntitySubType() === type_entity_1.EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR;
                            });
                            var principleInvestigator = principleInvestigatorFileItem != null ? new name_id_1.NameId(principleInvestigatorFileItem.getItemId(), null, principleInvestigatorFileItem.getItemName(), type_entity_1.EntityType.CONTACT, null, null) : null;
                            // ******** PROJECT
                            var projectFileItem = fileItems.find(function (item) {
                                return item.getEntityType() === type_entity_1.EntityType.PROJECT;
                            });
                            var project = projectFileItem != null ? new name_id_1.NameId(projectFileItem.getItemId(), null, projectFileItem.getItemName(), type_entity_1.EntityType.PROJECT, null, null) : null;
                            // ******** PLATFORM
                            var platformFileItems = fileItems.filter(function (item) {
                                return item.getEntityType() === type_entity_1.EntityType.PLATFORM;
                            });
                            var platforms = platformFileItems.map(function (item) {
                                return new name_id_1.NameId(item.getItemId(), null, item.getItemName(), type_entity_1.EntityType.PLATFORM, null, null);
                            });
                            var markerGroupItems = fileItems.filter(function (item) {
                                return item.getEntityType() === type_entity_1.EntityType.MARKER_GROUP;
                            });
                            var markerGroups = markerGroupItems.map(function (item) {
                                return new name_id_1.NameId(item.getItemId(), null, item.getItemName(), type_entity_1.EntityType.MARKER_GROUP, null, null);
                            });
                            // ******** MARKERS
                            var markerListItems = fileItems
                                .filter(function (fi) {
                                return fi.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.MARKER_LIST_ITEM;
                            });
                            var markerList = markerListItems
                                .map(function (mi) {
                                return mi.getItemId();
                            });
                            // ******** SAMPLES
                            var sampleListItems = fileItems
                                .filter(function (fi) {
                                return fi.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.SAMPLE_LIST_ITEM;
                            });
                            var sampleList = sampleListItems
                                .map(function (mi) {
                                return mi.getItemId();
                            });
                            var sampleListTypeFileItem = fileItems.find(function (item) {
                                return item.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.SAMPLE_LIST_TYPE;
                            });
                            if (sampleListTypeFileItem != null) {
                                sampleListType = type_extractor_sample_list_1.GobiiSampleListType[sampleListTypeFileItem.getItemId()];
                            }
                            // *** NOW PUT TOGETHER THE EXTRACT INSTRUCTIONS
                            if (gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET) {
                                var dataSetItems = fileItems
                                    .filter(function (item) {
                                    return item.getEntityType() === type_entity_1.EntityType.DATASET;
                                });
                                dataSetItems.forEach(function (datsetFileItem) {
                                    var dataSet = new name_id_1.NameId(datsetFileItem.getItemId(), null, datsetFileItem.getItemName(), type_entity_1.EntityType.CV, null, null);
                                    gobiiDataSetExtracts.push(new data_set_extract_1.GobiiDataSetExtract(gobiiFileType, false, null, gobiiExtractFilterType, null, null, markerFileName, null, datasetType, platforms, null, null, dataSet, null, []));
                                }); // iterate dataset items
                                _this.post(jobId, gobiiDataSetExtracts, submitterContactid, mapsetIds)
                                    .subscribe(function (extractorInstructions) {
                                    observer.next(extractorInstructions);
                                    observer.complete();
                                });
                            }
                            else if (gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER) {
                                gobiiDataSetExtracts.push(new data_set_extract_1.GobiiDataSetExtract(gobiiFileType, false, null, gobiiExtractFilterType, markerList, null, markerFileName, null, datasetType, platforms, null, null, null, markerGroups, []));
                                _this.post(jobId, gobiiDataSetExtracts, submitterContactid, mapsetIds)
                                    .subscribe(function (extractorInstructions) {
                                    observer.next(extractorInstructions);
                                    observer.complete();
                                });
                            }
                            else if (gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE) {
                                gobiiDataSetExtracts.push(new data_set_extract_1.GobiiDataSetExtract(gobiiFileType, false, null, gobiiExtractFilterType, null, sampleList, sampleFileName, sampleListType, datasetType, platforms, principleInvestigator, project, null, null, []));
                                _this.post(jobId, gobiiDataSetExtracts, submitterContactid, mapsetIds)
                                    .subscribe(function (extractorInstructions) {
                                    observer.next(extractorInstructions);
                                    observer.complete();
                                });
                            }
                            else if (gobiiExtractFilterType === type_extractor_filter_1.GobiiExtractFilterType.FLEX_QUERY) {
                                _this.flexQueryService.getVertexFilters(file_item_param_names_1.FilterParamNames.FQ_F4_VERTEX_VALUES)
                                    .subscribe(function (vertices) {
                                    if (vertices && vertices.length > 0) {
                                        // for the other extract types, fileitems on which the extract is based constitute
                                        // the content of the extract directly. In the case of flex query, the vertices
                                        // from the filters could theoretically not match the "selected" file items, which would
                                        // mean that the content of the extract would not be the same as what's displayed in the
                                        // tree. This condition _should_ not ever happen. But it's vitally important that this information
                                        // be reported correctly, so we double check here.
                                        var verticesMatchFileItems = true;
                                        var _loop_1 = function (idx) {
                                            var currentVertex = vertices[idx];
                                            var valueCompoundId = gobii_file_item_compound_id_1.GobiiFileItemCompoundId.fromGobiiFileItemCompoundId(currentVertex).setExtractorItemType(type_extractor_item_1.ExtractorItemType.VERTEX_VALUE);
                                            var selectedFileItems = fileItems.filter(function (gfi) { return gfi.compoundIdeEquals(valueCompoundId); });
                                            if (selectedFileItems && selectedFileItems.length > 0) {
                                                var itemIds = selectedFileItems
                                                    .map(function (gfi) { return Number(gfi.getItemId()); });
                                                verticesMatchFileItems = itemIds.length === currentVertex.filterVals.length && itemIds.every(function (v, i) { return v === currentVertex.filterVals[i].id; });
                                            }
                                        };
                                        for (var idx = 0; (idx < vertices.length) && verticesMatchFileItems; idx++) {
                                            _loop_1(idx);
                                        }
                                        if (verticesMatchFileItems) {
                                            gobiiDataSetExtracts.push(new data_set_extract_1.GobiiDataSetExtract(gobiiFileType, false, null, gobiiExtractFilterType, null, sampleList, sampleFileName, sampleListType, datasetType, platforms, principleInvestigator, project, null, null, vertices));
                                            _this.post(jobId, gobiiDataSetExtracts, submitterContactid, mapsetIds)
                                                .subscribe(function (extractorInstructions) {
                                                observer.next(extractorInstructions);
                                                observer.complete();
                                            });
                                        }
                                        else {
                                            _this.store.dispatch(new historyAction.AddStatusMessageAction("The vertex filter values do not align with the selected vertex file items"));
                                            observer.complete();
                                        }
                                    }
                                    else {
                                        _this.store.dispatch(new historyAction.AddStatusMessageAction("There are no vertex filters for this submission"));
                                        observer.complete();
                                    }
                                })
                                    .unsubscribe(); // from get vertex filters
                            }
                            else {
                                _this.store.dispatch(new historyAction.AddStatusMessageAction("Unhandled extract filter type: " + type_extractor_filter_1.GobiiExtractFilterType[gobiiExtractFilterType]));
                                observer.complete();
                            }
                        }).unsubscribe(); // from select file items
                    });
                }; // submit()
                InstructionSubmissionService.prototype.post = function (jobId, gobiiDataSetExtracts, submitterContactId, mapsetIds) {
                    // let extractorInstructionFilesDTORequest: ExtractorInstructionFilesDTO =
                    //     new ExtractorInstructionFilesDTO(gobiiExtractorInstructions,
                    //         jobId);
                    var _this = this;
                    //let extractorInstructionFilesDTOResponse: ExtractorInstructionFilesDTO = null;
                    return Observable_1.Observable.create(function (observer) {
                        _this.dtoRequestServiceExtractorFile.post(new dto_request_item_extractor_submission_1.DtoRequestItemExtractorSubmission(new dto_extractor_instruction_files_1.ExtractorInstructionFilesDTO([
                            new gobii_extractor_instruction_1.GobiiExtractorInstruction(gobiiDataSetExtracts, submitterContactId, null, mapsetIds)
                        ], jobId))).subscribe(function (payloadReader) {
                            if (payloadReader.succeeded()) {
                                _this.store.dispatch(new historyAction
                                    .AddStatusMessageAction("Extractor instruction file created on server: "
                                    + jobId));
                                observer.next(payloadReader.getData().getGobiiExtractorInstructions());
                                observer.complete();
                            }
                            else {
                                _this.store.dispatch(new historyAction.AddStatusMessageAction("Error submitting extract insturctions: " +
                                    payloadReader.getError()));
                            }
                        }); //
                    }); // Observable.create()
                }; // function()
                InstructionSubmissionService = __decorate([
                    core_1.Injectable(),
                    __metadata("design:paramtypes", [store_1.Store,
                        dto_request_service_1.DtoRequestService,
                        tree_structure_service_1.TreeStructureService,
                        flex_query_service_1.FlexQueryService])
                ], InstructionSubmissionService);
                return InstructionSubmissionService;
            }());
            exports_1("InstructionSubmissionService", InstructionSubmissionService);
        }
    };
});
//# sourceMappingURL=instruction-submission-service.js.map