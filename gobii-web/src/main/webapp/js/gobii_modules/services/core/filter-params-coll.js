System.register(["@angular/core", "../../model/type-entity", "../../model/type-extractor-filter", "../../model/cv-filter-type", "../../model/filter-params", "../../store/actions/history-action", "@ngrx/store", "../../model/name-id-label-type", "../../model/filter-type", "../../model/file-item-param-names", "rxjs/add/operator/expand", "../../model/type-extractor-item", "../../store/actions/fileitem-action", "../../model/gobii-file-item-compound-id", "./dto-request.service", "../app/jsontogfi/json-to-gfi-dataset", "../app/dto-request-item-gfi", "../app/dto-request-item-gfi-paged", "../../store/actions/action-payload-filter"], function (exports_1, context_1) {
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
    var core_1, type_entity_1, type_extractor_filter_1, cv_filter_type_1, filter_params_1, historyAction, store_1, name_id_label_type_1, filter_type_1, file_item_param_names_1, type_extractor_item_1, fileAction, gobii_file_item_compound_id_1, dto_request_service_1, json_to_gfi_dataset_1, dto_request_item_gfi_1, dto_request_item_gfi_paged_1, action_payload_filter_1, FilterParamsColl;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (type_entity_1_1) {
                type_entity_1 = type_entity_1_1;
            },
            function (type_extractor_filter_1_1) {
                type_extractor_filter_1 = type_extractor_filter_1_1;
            },
            function (cv_filter_type_1_1) {
                cv_filter_type_1 = cv_filter_type_1_1;
            },
            function (filter_params_1_1) {
                filter_params_1 = filter_params_1_1;
            },
            function (historyAction_1) {
                historyAction = historyAction_1;
            },
            function (store_1_1) {
                store_1 = store_1_1;
            },
            function (name_id_label_type_1_1) {
                name_id_label_type_1 = name_id_label_type_1_1;
            },
            function (filter_type_1_1) {
                filter_type_1 = filter_type_1_1;
            },
            function (file_item_param_names_1_1) {
                file_item_param_names_1 = file_item_param_names_1_1;
            },
            function (_1) {
            },
            function (type_extractor_item_1_1) {
                type_extractor_item_1 = type_extractor_item_1_1;
            },
            function (fileAction_1) {
                fileAction = fileAction_1;
            },
            function (gobii_file_item_compound_id_1_1) {
                gobii_file_item_compound_id_1 = gobii_file_item_compound_id_1_1;
            },
            function (dto_request_service_1_1) {
                dto_request_service_1 = dto_request_service_1_1;
            },
            function (json_to_gfi_dataset_1_1) {
                json_to_gfi_dataset_1 = json_to_gfi_dataset_1_1;
            },
            function (dto_request_item_gfi_1_1) {
                dto_request_item_gfi_1 = dto_request_item_gfi_1_1;
            },
            function (dto_request_item_gfi_paged_1_1) {
                dto_request_item_gfi_paged_1 = dto_request_item_gfi_paged_1_1;
            },
            function (action_payload_filter_1_1) {
                action_payload_filter_1 = action_payload_filter_1_1;
            }
        ],
        execute: function () {
            FilterParamsColl = (function () {
                function FilterParamsColl(store, pagedDatasetRequestService, fileItemRequestService) {
                    this.store = store;
                    this.pagedDatasetRequestService = pagedDatasetRequestService;
                    this.fileItemRequestService = fileItemRequestService;
                    this.filterParams = [];
                    // ************************************************************************
                    // **************************** GENERAL  *********************************
                    var cvJobStatusCompoundUniqueId = new gobii_file_item_compound_id_1.GobiiFileItemCompoundId(type_extractor_item_1.ExtractorItemType.ENTITY, type_entity_1.EntityType.CV, type_entity_1.EntitySubType.UNKNOWN, cv_filter_type_1.CvFilterType.JOB_STATUS, cv_filter_type_1.CvFilters.get(cv_filter_type_1.CvFilterType.JOB_STATUS));
                    // ************************************************************************
                    // **************************** BY SAMPLE *********************************
                    this.addFilter(filter_params_1.FilterParams
                        .build(file_item_param_names_1.FilterParamNames.CV_DATATYPE, type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE, type_entity_1.EntityType.CV)
                        .setIsDynamicFilterValue(false)
                        .setCvFilterType(cv_filter_type_1.CvFilterType.DATASET_TYPE)
                        .setCvFilterValue(cv_filter_type_1.CvFilters.get(cv_filter_type_1.CvFilterType.DATASET_TYPE))
                        .setFilterType(filter_type_1.FilterType.NAMES_BY_TYPE_NAME)
                        .setNameIdLabelType(name_id_label_type_1.NameIdLabelType.SELECT_A));
                    this.addFilter(filter_params_1.FilterParams
                        .build(file_item_param_names_1.FilterParamNames.MAPSETS, type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE, type_entity_1.EntityType.MAPSET)
                        .setIsDynamicFilterValue(false)
                        .setNameIdLabelType(name_id_label_type_1.NameIdLabelType.NO));
                    this.addFilter(filter_params_1.FilterParams
                        .build(file_item_param_names_1.FilterParamNames.PLATFORMS, type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE, type_entity_1.EntityType.PLATFORM)
                        .setIsDynamicFilterValue(false));
                    this.addFilter(filter_params_1.FilterParams
                        .build(file_item_param_names_1.FilterParamNames.CONTACT_PI_HIERARCHY_ROOT, type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE, type_entity_1.EntityType.CONTACT)
                        .setExtractorItemType(type_extractor_item_1.ExtractorItemType.ENTITY)
                        .setCvFilterType(cv_filter_type_1.CvFilterType.UNKNOWN)
                        .setIsDynamicFilterValue(true)
                        .setIsDynamicDataLoad(false)
                        .setEntitySubType(type_entity_1.EntitySubType.CONTACT_PRINCIPAL_INVESTIGATOR)
                        .setNameIdLabelType(name_id_label_type_1.NameIdLabelType.ALL)
                        .setIsExtractCriterion(true)
                        .setFilterType(filter_type_1.FilterType.NAMES_BY_TYPE_NAME)
                        .setCvFilterValue("PI"));
                    this.addFilter(filter_params_1.FilterParams
                        .build(file_item_param_names_1.FilterParamNames.PROJECTS_BY_CONTACT, type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE, type_entity_1.EntityType.PROJECT)
                        .setExtractorItemType(type_extractor_item_1.ExtractorItemType.ENTITY)
                        .setRelatedEntityUniqueId(new gobii_file_item_compound_id_1.GobiiFileItemCompoundId(type_extractor_item_1.ExtractorItemType.ENTITY, type_entity_1.EntityType.CONTACT, type_entity_1.EntitySubType.CONTACT_PRINCIPAL_INVESTIGATOR, cv_filter_type_1.CvFilterType.UNKNOWN, null).setIsExtractCriterion(true))
                        .setIsDynamicFilterValue(false)
                        .setIsDynamicDataLoad(false)
                        .setNameIdLabelType(name_id_label_type_1.NameIdLabelType.ALL)
                        .setIsExtractCriterion(true));
                    this.getFilter(file_item_param_names_1.FilterParamNames.CONTACT_PI_HIERARCHY_ROOT, type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE)
                        .setChildNameIdRequestParams([this.getFilter(file_item_param_names_1.FilterParamNames.PROJECTS_BY_CONTACT, type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE)]);
                    this.getFilter(file_item_param_names_1.FilterParamNames.PROJECTS_BY_CONTACT, type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE)
                        .setParentFileItemParams(this.getFilter(file_item_param_names_1.FilterParamNames.CONTACT_PI_HIERARCHY_ROOT, type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE));
                    this.addFilter(filter_params_1.FilterParams
                        .build(file_item_param_names_1.FilterParamNames.PROJECTS, type_extractor_filter_1.GobiiExtractFilterType.BY_SAMPLE, type_entity_1.EntityType.PROJECT)
                        .setIsDynamicFilterValue(false)
                        .setNameIdLabelType(name_id_label_type_1.NameIdLabelType.ALL));
                    // ************************************************************************
                    // **************************** BY MARKER  *********************************
                    this.addFilter(filter_params_1.FilterParams
                        .build(file_item_param_names_1.FilterParamNames.CV_DATATYPE, type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER, type_entity_1.EntityType.CV)
                        .setIsDynamicFilterValue(false)
                        .setCvFilterType(cv_filter_type_1.CvFilterType.DATASET_TYPE)
                        .setCvFilterValue(cv_filter_type_1.CvFilters.get(cv_filter_type_1.CvFilterType.DATASET_TYPE))
                        .setFilterType(filter_type_1.FilterType.NAMES_BY_TYPE_NAME)
                        .setNameIdLabelType(name_id_label_type_1.NameIdLabelType.SELECT_A));
                    this.addFilter(filter_params_1.FilterParams
                        .build(file_item_param_names_1.FilterParamNames.MAPSETS, type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER, type_entity_1.EntityType.MAPSET)
                        .setIsDynamicFilterValue(false)
                        .setNameIdLabelType(name_id_label_type_1.NameIdLabelType.NO));
                    this.addFilter(filter_params_1.FilterParams
                        .build(file_item_param_names_1.FilterParamNames.PLATFORMS, type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER, type_entity_1.EntityType.PLATFORM)
                        .setIsDynamicFilterValue(false));
                    this.addFilter(filter_params_1.FilterParams
                        .build(file_item_param_names_1.FilterParamNames.MARKER_GROUPS, type_extractor_filter_1.GobiiExtractFilterType.BY_MARKER, type_entity_1.EntityType.MARKER_GROUP)
                        .setIsDynamicFilterValue(false));
                    // ************************************************************************
                    // **************************** BY DATASET *********************************
                    this.addFilter(filter_params_1.FilterParams
                        .build(file_item_param_names_1.FilterParamNames.MAPSETS, type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET, type_entity_1.EntityType.MAPSET)
                        .setIsDynamicFilterValue(false)
                        .setNameIdLabelType(name_id_label_type_1.NameIdLabelType.NO));
                    this.addFilter(filter_params_1.FilterParams
                        .build(file_item_param_names_1.FilterParamNames.CV_JOB_STATUS, type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET, cvJobStatusCompoundUniqueId.getEntityType())
                        .setIsDynamicFilterValue(true)
                        .setCvFilterType(cvJobStatusCompoundUniqueId.getCvFilterType())
                        .setCvFilterValue(cvJobStatusCompoundUniqueId.getCvFilterValue())
                        .setFilterType(filter_type_1.FilterType.NAMES_BY_TYPE_NAME)
                        .setNameIdLabelType(name_id_label_type_1.NameIdLabelType.ALL)
                        .setOnLoadFilteredItemsAction(function (fileItems, payloadFilter) {
                        /***
                         * This event will cause the initially selected job status to be completed and the
                         *  dataset grid items to be filtered accordingly.
                         *
                         * I am a little uneasy with the implementation. For one thing, it sets the
                         * completedItem's selected property. Ideally, the semantics of the action
                         * should be such that the reducer knows to set the selected property. We're sort
                         * of monkeying with state here. In essence,
                         * we really need a new action and corresponding reducer code to handle this;
                         * I'm also not in comfortable with the fact that we are testing for a filter value
                         * to determine whether or not to apply the initial select state and filter value. Here again,
                         * there should be semantics in the load filtered items action or something that would indicate
                         * that it's an initial load. But that will make things more complicated.
                         *
                         * For now this is fine. If we expand this type of thing to include other types of initial select
                         * actions, we will probably want to revisit the design.
                         *
                         */
                        var returnVal = null;
                        if (!payloadFilter) {
                            var completedItem = fileItems.find(function (fi) { return fi.getItemName() === "completed"; });
                            var labelItem = fileItems.find(function (fi) { return fi.getExtractorItemType() === type_extractor_item_1.ExtractorItemType.LABEL; });
                            if (completedItem && labelItem) {
                                returnVal = new fileAction.LoadFilterAction({
                                    filterId: file_item_param_names_1.FilterParamNames.CV_JOB_STATUS,
                                    filter: new action_payload_filter_1.PayloadFilter(type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET, cvJobStatusCompoundUniqueId, null, completedItem.getItemId(), null, payloadFilter.entityLasteUpdated, payloadFilter.pagination)
                                });
                            }
                        }
                        return returnVal;
                    }));
                    var cvDatasetCompoundUniqueId = new gobii_file_item_compound_id_1.GobiiFileItemCompoundId(type_extractor_item_1.ExtractorItemType.ENTITY, type_entity_1.EntityType.DATASET, type_entity_1.EntitySubType.UNKNOWN, cv_filter_type_1.CvFilterType.UNKNOWN, cv_filter_type_1.CvFilters.get(cv_filter_type_1.CvFilterType.UNKNOWN));
                    this.addFilter(filter_params_1.FilterParams
                        .build(file_item_param_names_1.FilterParamNames.DATASET_LIST, type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET, type_entity_1.EntityType.DATASET)
                        .setFilterType(filter_type_1.FilterType.ENTITY_LIST)
                        .setOnLoadFilteredItemsAction(function (fileItems, payloadFilter) {
                        var returnVal = null;
                        if (!payloadFilter || !payloadFilter.relatedEntityFilterValue) {
                            returnVal = new fileAction.LoadFilterAction({
                                filterId: file_item_param_names_1.FilterParamNames.DATASET_LIST_STATUS,
                                filter: new action_payload_filter_1.PayloadFilter(type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET, cvDatasetCompoundUniqueId, null, "completed", null, payloadFilter.entityLasteUpdated, payloadFilter.pagination)
                            });
                        }
                        return returnVal;
                    }));
                    // add dto request to DATASET_LIST filter
                    this.getFilter(file_item_param_names_1.FilterParamNames.DATASET_LIST, type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET)
                        .setDtoRequestItem(new dto_request_item_gfi_1.DtoRequestItemGfi(this.getFilter(file_item_param_names_1.FilterParamNames.DATASET_LIST, type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET), null, new json_to_gfi_dataset_1.JsonToGfiDataset(this.getFilter(file_item_param_names_1.FilterParamNames.DATASET_LIST, type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET), this)))
                        .setDtoRequestService(this.fileItemRequestService);
                    // same as previous except configured for paging
                    this.addFilter(filter_params_1.FilterParams
                        .build(file_item_param_names_1.FilterParamNames.DATASET_LIST_PAGED, type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET, type_entity_1.EntityType.DATASET)
                        .setFilterType(filter_type_1.FilterType.ENTITY_LIST)
                        .setOnLoadFilteredItemsAction(function (fileItems, payloadFilter) {
                        var returnVal = null;
                        if (!payloadFilter || !payloadFilter.relatedEntityFilterValue) {
                            returnVal = new fileAction.LoadFilterAction({
                                filterId: file_item_param_names_1.FilterParamNames.DATASET_LIST_STATUS,
                                filter: new action_payload_filter_1.PayloadFilter(type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET, cvDatasetCompoundUniqueId, null, "completed", null, payloadFilter.entityLasteUpdated, payloadFilter.pagination)
                            });
                        }
                        return returnVal;
                    })
                        .setIsPaged(true));
                    // add dto request to DATASET_LIST_PAGED filter
                    this.getFilter(file_item_param_names_1.FilterParamNames.DATASET_LIST_PAGED, type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET)
                        .setDtoRequestItem(new dto_request_item_gfi_paged_1.DtoRequestItemGfiPaged(this.getFilter(file_item_param_names_1.FilterParamNames.DATASET_LIST_PAGED, type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET), null, new json_to_gfi_dataset_1.JsonToGfiDataset(this.getFilter(file_item_param_names_1.FilterParamNames.DATASET_LIST_PAGED, type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET), this)))
                        .setDtoRequestService(this.pagedDatasetRequestService);
                    this.addFilter(filter_params_1.FilterParams
                        .build(file_item_param_names_1.FilterParamNames.DATASET_BY_DATASET_ID, type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET, type_entity_1.EntityType.DATASET)
                        .setFilterType(filter_type_1.FilterType.ENTITY_BY_ID));
                    this.addFilter(filter_params_1.FilterParams
                        .build(file_item_param_names_1.FilterParamNames.ANALYSES_BY_DATASET_ID, type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET, type_entity_1.EntityType.ANALYSIS)
                        .setFilterType(filter_type_1.FilterType.ENTITY_BY_ID));
                    this.addFilter(filter_params_1.FilterParams
                        .build(file_item_param_names_1.FilterParamNames.CONTACT_PI_FILTER_OPTIONAL, type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET, type_entity_1.EntityType.CONTACT)
                        .setExtractorItemType(type_extractor_item_1.ExtractorItemType.ENTITY)
                        .setIsDynamicFilterValue(true)
                        .setIsDynamicDataLoad(false)
                        .setEntitySubType(type_entity_1.EntitySubType.CONTACT_PRINCIPAL_INVESTIGATOR)
                        .setNameIdLabelType(name_id_label_type_1.NameIdLabelType.ALL)
                        .setIsExtractCriterion(false)
                        .setFilterType(filter_type_1.FilterType.NAMES_BY_TYPE_NAME)
                        .setCvFilterValue("PI"));
                    // relate this filter to CONTACT_PI_FILTER_OPTIONAL as parent
                    this.addFilter(filter_params_1.FilterParams
                        .build(file_item_param_names_1.FilterParamNames.PROJECT_FILTER_OPTIONAL, type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET, type_entity_1.EntityType.PROJECT)
                        .setExtractorItemType(type_extractor_item_1.ExtractorItemType.ENTITY)
                        .setRelatedEntityUniqueId(new gobii_file_item_compound_id_1.GobiiFileItemCompoundId(type_extractor_item_1.ExtractorItemType.ENTITY, type_entity_1.EntityType.CONTACT, type_entity_1.EntitySubType.UNKNOWN, cv_filter_type_1.CvFilterType.UNKNOWN, null))
                        .setIsDynamicFilterValue(true)
                        .setIsDynamicDataLoad(false)
                        .setNameIdLabelType(name_id_label_type_1.NameIdLabelType.ALL)
                        .setIsExtractCriterion(false));
                    // relate this filter to PROJECT_FILTER_OPTIONAL as pa\rent
                    this.addFilter(filter_params_1.FilterParams
                        .build(file_item_param_names_1.FilterParamNames.EXPERIMENT_FILTER_OPTIONAL, type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET, type_entity_1.EntityType.EXPERIMENT)
                        .setRelatedEntityUniqueId(new gobii_file_item_compound_id_1.GobiiFileItemCompoundId(type_extractor_item_1.ExtractorItemType.ENTITY, type_entity_1.EntityType.PROJECT, type_entity_1.EntitySubType.UNKNOWN, cv_filter_type_1.CvFilterType.UNKNOWN, null))
                        .setIsDynamicFilterValue(true)
                        .setIsDynamicDataLoad(false)
                        .setExtractorItemType(type_extractor_item_1.ExtractorItemType.ENTITY)
                        .setNameIdLabelType(name_id_label_type_1.NameIdLabelType.ALL)
                        .setIsExtractCriterion(false));
                    this.addFilter(filter_params_1.FilterParams
                        .build(file_item_param_names_1.FilterParamNames.DATASET_FILTER_OPTIONAL, type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET, type_entity_1.EntityType.DATASET)
                        .setRelatedEntityUniqueId(new gobii_file_item_compound_id_1.GobiiFileItemCompoundId(type_extractor_item_1.ExtractorItemType.ENTITY, type_entity_1.EntityType.EXPERIMENT, type_entity_1.EntitySubType.UNKNOWN, cv_filter_type_1.CvFilterType.UNKNOWN, null))
                        .setIsDynamicFilterValue(true)
                        .setIsDynamicDataLoad(false)
                        .setExtractorItemType(type_extractor_item_1.ExtractorItemType.ENTITY));
                    //Set up hierarchy
                    this.getFilter(file_item_param_names_1.FilterParamNames.CONTACT_PI_FILTER_OPTIONAL, type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET)
                        .setChildNameIdRequestParams([this.getFilter(file_item_param_names_1.FilterParamNames.PROJECT_FILTER_OPTIONAL, type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET)]);
                    this.getFilter(file_item_param_names_1.FilterParamNames.PROJECT_FILTER_OPTIONAL, type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET)
                        .setParentFileItemParams(this.getFilter(file_item_param_names_1.FilterParamNames.CONTACT_PI_FILTER_OPTIONAL, type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET))
                        .setChildNameIdRequestParams([this.getFilter(file_item_param_names_1.FilterParamNames.EXPERIMENT_FILTER_OPTIONAL, type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET)]);
                    this.getFilter(file_item_param_names_1.FilterParamNames.EXPERIMENT_FILTER_OPTIONAL, type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET)
                        .setParentFileItemParams(this.getFilter(file_item_param_names_1.FilterParamNames.PROJECT_FILTER_OPTIONAL, type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET))
                        .setChildNameIdRequestParams([this.getFilter(file_item_param_names_1.FilterParamNames.DATASET_FILTER_OPTIONAL, type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET)]);
                    //for hierarchical items, we need to crate the nameid requests separately from the
                    //flat map: they _will_ need to be in the flat map; however, they all need to be
                    //useed to set up the filtering hierarchy
                    var nameIdRequestParamsContactsPi = filter_params_1.FilterParams
                        .build(file_item_param_names_1.FilterParamNames.CONTACT_PI_HIERARCHY_ROOT, type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET, type_entity_1.EntityType.CONTACT)
                        .setIsDynamicFilterValue(true)
                        .setEntitySubType(type_entity_1.EntitySubType.CONTACT_PRINCIPAL_INVESTIGATOR);
                    var nameIdRequestParamsExperiments = filter_params_1.FilterParams
                        .build(file_item_param_names_1.FilterParamNames.EXPERIMENTS_BY_PROJECT, type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET, type_entity_1.EntityType.EXPERIMENT)
                        .setIsDynamicFilterValue(true)
                        .setFilterType(filter_type_1.FilterType.NAMES_BY_TYPEID);
                    var nameIdRequestParamsDatasets = filter_params_1.FilterParams
                        .build(file_item_param_names_1.FilterParamNames.DATASETS_BY_EXPERIMENT, type_extractor_filter_1.GobiiExtractFilterType.WHOLE_DATASET, type_entity_1.EntityType.DATASET)
                        .setIsDynamicFilterValue(true)
                        .setFilterType(filter_type_1.FilterType.NAMES_BY_TYPEID);
                    //add the individual requests to the map
                    // this.addFilter(nameIdRequestParamsContactsPi);
                    // this.addFilter(nameIdRequestParamsProjectByPiContact);
                    // this.addFilter(nameIdRequestParamsExperiments);
                    // this.addFilter(nameIdRequestParamsDatasets);
                    //build the parent-child request params graph
                    // nameIdRequestParamsContactsPi
                    //     .setChildNameIdRequestParams(
                    //         [nameIdRequestParamsProjectByPiContact
                    //             .setParentFileItemParams(nameIdRequestParamsContactsPi)
                    //             .setChildNameIdRequestParams([nameIdRequestParamsExperiments
                    //                 .setParentFileItemParams(nameIdRequestParamsProjectByPiContact)
                    //                 .setChildNameIdRequestParams([nameIdRequestParamsDatasets
                    //                     .setParentFileItemParams(nameIdRequestParamsExperiments)
                    //                 ])
                    //             ])
                    //         ]);
                } // constructor
                FilterParamsColl.prototype.addFilter = function (filterParamsToAdd) {
                    var existingFilterParams = this.filterParams
                        .find(function (ffp) {
                        return ffp.getQueryName() === filterParamsToAdd.getQueryName()
                            && ffp.getGobiiExtractFilterType() === filterParamsToAdd.getGobiiExtractFilterType();
                    });
                    if (!existingFilterParams) {
                        this.filterParams.push(filterParamsToAdd);
                    }
                    else {
                        this.store.dispatch(new historyAction.AddStatusMessageAction("The query "
                            + filterParamsToAdd.getQueryName()
                            + " because there is already a filter by that name for this extract type "
                            + type_extractor_filter_1.GobiiExtractFilterType[filterParamsToAdd.getQueryName()]));
                    }
                };
                FilterParamsColl.prototype.getFilter = function (nameIdFilterParamTypes, gobiiExtractFilterType) {
                    return this.filterParams.find(function (ffp) {
                        return ffp.getQueryName() === nameIdFilterParamTypes &&
                            ffp.getGobiiExtractFilterType() === gobiiExtractFilterType;
                    });
                };
                FilterParamsColl = __decorate([
                    core_1.Injectable(),
                    __metadata("design:paramtypes", [store_1.Store,
                        dto_request_service_1.DtoRequestService,
                        dto_request_service_1.DtoRequestService])
                ], FilterParamsColl);
                return FilterParamsColl;
            }());
            exports_1("FilterParamsColl", FilterParamsColl);
        }
    };
});
//# sourceMappingURL=filter-params-coll.js.map