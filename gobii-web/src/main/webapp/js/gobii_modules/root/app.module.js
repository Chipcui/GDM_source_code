System.register(["@angular/core", "@angular/forms", "@angular/http", "@angular/platform-browser", "@angular/platform-browser/animations", "../views/export-format.component", "../views/status-display-box.component", "../views/checklist-box.component", "../views/sample-marker-box.component", "ng2-file-upload", "./app.extractorroot", "../services/core/dto-request.service", "../services/core/authentication.service", "../views/text-area.component", "../views/uploader.component", "../views/sample-list-type.component", "primeng/primeng", "../views/status-display-tree.component", "../views/name-id-list-box.component", "../services/core/name-id-service", "./app.component", "../views/login.component", "./app.routing", "@angular/common", "../services/core/auth.guard", "primeng/components/button/button", "./page-by-samples.component", "@ngrx/store", "../store/reducers", "@ngrx/store-devtools", "@ngrx/effects", "../store/effects/tree-effects", "../services/core/tree-structure-service", "../store/effects/file-item-effects", "../services/core/nameid-file-item-service", "../services/core/instruction-submission-service", "../views/dataset-datatable.component", "../services/core/filter-params-coll", "../services/core/view-id-generator-service", "../views/flex-query-filter.component", "../views/marker-sample-count.component", "../services/core/entity-file-item-service", "../services/core/filter-service", "../services/core/flex-query-service"], function (exports_1, context_1) {
    "use strict";
    var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
        var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
        if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
        else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
        return c > 3 && r && Object.defineProperty(target, key, r), r;
    };
    var __moduleName = context_1 && context_1.id;
    var core_1, forms_1, http_1, platform_browser_1, animations_1, export_format_component_1, status_display_box_component_1, checklist_box_component_1, sample_marker_box_component_1, ng2_file_upload_1, app_extractorroot_1, dto_request_service_1, authentication_service_1, text_area_component_1, uploader_component_1, sample_list_type_component_1, primeng_1, status_display_tree_component_1, name_id_list_box_component_1, name_id_service_1, app_component_1, login_component_1, app_routing_1, common_1, auth_guard_1, button_1, page_by_samples_component_1, store_1, reducers_1, store_devtools_1, effects_1, tree_effects_1, tree_structure_service_1, file_item_effects_1, nameid_file_item_service_1, instruction_submission_service_1, dataset_datatable_component_1, filter_params_coll_1, view_id_generator_service_1, flex_query_filter_component_1, marker_sample_count_component_1, entity_file_item_service_1, filter_service_1, flex_query_service_1, AppModule;
    return {
        setters: [
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (forms_1_1) {
                forms_1 = forms_1_1;
            },
            function (http_1_1) {
                http_1 = http_1_1;
            },
            function (platform_browser_1_1) {
                platform_browser_1 = platform_browser_1_1;
            },
            function (animations_1_1) {
                animations_1 = animations_1_1;
            },
            function (export_format_component_1_1) {
                export_format_component_1 = export_format_component_1_1;
            },
            function (status_display_box_component_1_1) {
                status_display_box_component_1 = status_display_box_component_1_1;
            },
            function (checklist_box_component_1_1) {
                checklist_box_component_1 = checklist_box_component_1_1;
            },
            function (sample_marker_box_component_1_1) {
                sample_marker_box_component_1 = sample_marker_box_component_1_1;
            },
            function (ng2_file_upload_1_1) {
                ng2_file_upload_1 = ng2_file_upload_1_1;
            },
            function (app_extractorroot_1_1) {
                app_extractorroot_1 = app_extractorroot_1_1;
            },
            function (dto_request_service_1_1) {
                dto_request_service_1 = dto_request_service_1_1;
            },
            function (authentication_service_1_1) {
                authentication_service_1 = authentication_service_1_1;
            },
            function (text_area_component_1_1) {
                text_area_component_1 = text_area_component_1_1;
            },
            function (uploader_component_1_1) {
                uploader_component_1 = uploader_component_1_1;
            },
            function (sample_list_type_component_1_1) {
                sample_list_type_component_1 = sample_list_type_component_1_1;
            },
            function (primeng_1_1) {
                primeng_1 = primeng_1_1;
            },
            function (status_display_tree_component_1_1) {
                status_display_tree_component_1 = status_display_tree_component_1_1;
            },
            function (name_id_list_box_component_1_1) {
                name_id_list_box_component_1 = name_id_list_box_component_1_1;
            },
            function (name_id_service_1_1) {
                name_id_service_1 = name_id_service_1_1;
            },
            function (app_component_1_1) {
                app_component_1 = app_component_1_1;
            },
            function (login_component_1_1) {
                login_component_1 = login_component_1_1;
            },
            function (app_routing_1_1) {
                app_routing_1 = app_routing_1_1;
            },
            function (common_1_1) {
                common_1 = common_1_1;
            },
            function (auth_guard_1_1) {
                auth_guard_1 = auth_guard_1_1;
            },
            function (button_1_1) {
                button_1 = button_1_1;
            },
            function (page_by_samples_component_1_1) {
                page_by_samples_component_1 = page_by_samples_component_1_1;
            },
            function (store_1_1) {
                store_1 = store_1_1;
            },
            function (reducers_1_1) {
                reducers_1 = reducers_1_1;
            },
            function (store_devtools_1_1) {
                store_devtools_1 = store_devtools_1_1;
            },
            function (effects_1_1) {
                effects_1 = effects_1_1;
            },
            function (tree_effects_1_1) {
                tree_effects_1 = tree_effects_1_1;
            },
            function (tree_structure_service_1_1) {
                tree_structure_service_1 = tree_structure_service_1_1;
            },
            function (file_item_effects_1_1) {
                file_item_effects_1 = file_item_effects_1_1;
            },
            function (nameid_file_item_service_1_1) {
                nameid_file_item_service_1 = nameid_file_item_service_1_1;
            },
            function (instruction_submission_service_1_1) {
                instruction_submission_service_1 = instruction_submission_service_1_1;
            },
            function (dataset_datatable_component_1_1) {
                dataset_datatable_component_1 = dataset_datatable_component_1_1;
            },
            function (filter_params_coll_1_1) {
                filter_params_coll_1 = filter_params_coll_1_1;
            },
            function (view_id_generator_service_1_1) {
                view_id_generator_service_1 = view_id_generator_service_1_1;
            },
            function (flex_query_filter_component_1_1) {
                flex_query_filter_component_1 = flex_query_filter_component_1_1;
            },
            function (marker_sample_count_component_1_1) {
                marker_sample_count_component_1 = marker_sample_count_component_1_1;
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
            AppModule = (function () {
                function AppModule() {
                }
                AppModule = __decorate([
                    core_1.NgModule({
                        imports: [platform_browser_1.BrowserModule,
                            http_1.HttpModule,
                            forms_1.FormsModule,
                            forms_1.ReactiveFormsModule,
                            primeng_1.TreeModule,
                            primeng_1.CheckboxModule,
                            primeng_1.SharedModule,
                            primeng_1.DataTableModule,
                            primeng_1.PanelModule,
                            primeng_1.OverlayPanelModule,
                            primeng_1.AccordionModule,
                            primeng_1.ListboxModule,
                            primeng_1.FieldsetModule,
                            primeng_1.TabViewModule,
                            primeng_1.DialogModule,
                            primeng_1.TooltipModule,
                            primeng_1.RadioButtonModule,
                            primeng_1.ListboxModule,
                            primeng_1.DropdownModule,
                            primeng_1.ProgressSpinnerModule,
                            app_routing_1.routing,
                            animations_1.BrowserAnimationsModule,
                            store_1.StoreModule.forRoot(reducers_1.reducers),
                            effects_1.EffectsModule.forRoot([tree_effects_1.TreeEffects, file_item_effects_1.FileItemEffects]),
                            store_devtools_1.StoreDevtoolsModule.instrument({
                                maxAge: 25 //  Retains last 25 states
                            })
                        ],
                        declarations: [
                            app_component_1.AppComponent,
                            app_extractorroot_1.ExtractorRoot,
                            login_component_1.LoginComponent,
                            export_format_component_1.ExportFormatComponent,
                            status_display_box_component_1.StatusDisplayComponent,
                            checklist_box_component_1.CheckListBoxComponent,
                            sample_marker_box_component_1.SampleMarkerBoxComponent,
                            ng2_file_upload_1.FileSelectDirective,
                            ng2_file_upload_1.FileDropDirective,
                            text_area_component_1.TextAreaComponent,
                            uploader_component_1.UploaderComponent,
                            sample_list_type_component_1.SampleListTypeComponent,
                            name_id_list_box_component_1.NameIdListBoxComponent,
                            status_display_tree_component_1.StatusDisplayTreeComponent,
                            button_1.Button,
                            page_by_samples_component_1.SearchCriteriaBySamplesComponent,
                            dataset_datatable_component_1.DatasetDatatableComponent,
                            flex_query_filter_component_1.FlexQueryFilterComponent,
                            marker_sample_count_component_1.MarkerSampleCountComponent
                        ],
                        providers: [auth_guard_1.AuthGuard,
                            authentication_service_1.AuthenticationService,
                            dto_request_service_1.DtoRequestService,
                            name_id_service_1.NameIdService,
                            tree_structure_service_1.TreeStructureService,
                            nameid_file_item_service_1.NameIdFileItemService,
                            entity_file_item_service_1.EntityFileItemService,
                            filter_service_1.FilterService,
                            view_id_generator_service_1.ViewIdGeneratorService,
                            instruction_submission_service_1.InstructionSubmissionService,
                            filter_params_coll_1.FilterParamsColl,
                            flex_query_service_1.FlexQueryService,
                            { provide: common_1.APP_BASE_HREF, useValue: './' }],
                        bootstrap: [app_component_1.AppComponent]
                    })
                ], AppModule);
                return AppModule;
            }());
            exports_1("AppModule", AppModule);
        }
    };
});
//# sourceMappingURL=app.module.js.map