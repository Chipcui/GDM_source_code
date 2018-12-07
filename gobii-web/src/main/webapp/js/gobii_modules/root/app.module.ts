import {NgModule} from "@angular/core";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HttpModule} from "@angular/http";
import {BrowserModule} from "@angular/platform-browser";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {ExportFormatComponent} from "../views/export-format.component";
import {StatusDisplayComponent} from "../views/status-display-box.component";
import {CheckListBoxComponent} from "../views/checklist-box.component";
import {SampleMarkerBoxComponent} from "../views/sample-marker-box.component";
import {FileDropDirective, FileSelectDirective} from "ng2-file-upload";
import {ExtractorRoot} from "./app.extractorroot";
import {DtoRequestService} from "../services/core/dto-request.service";
import {AuthenticationService} from "../services/core/authentication.service";
import {TextAreaComponent} from "../views/text-area.component";
import {UploaderComponent} from "../views/uploader.component";
import {SampleListTypeComponent} from "../views/sample-list-type.component";
import {
    CheckboxModule, DataTableModule, PanelModule, SharedModule, TreeModule, OverlayPanelModule,
    AccordionModule, ListboxModule, FieldsetModule, TabViewModule, DialogModule, TooltipModule, RadioButtonModule
} from 'primeng/primeng';
import {StatusDisplayTreeComponent} from "../views/status-display-tree.component";
import {NameIdListBoxComponent} from "../views/name-id-list-box.component";
import {NameIdService} from "../services/core/name-id-service";
import {AppComponent} from "./app.component";
import {LoginComponent} from "../views/login.component";
import {routing} from "./app.routing";
import {APP_BASE_HREF} from "@angular/common";
import {AuthGuard} from "../services/core/auth.guard";
import {Button} from "primeng/components/button/button";
import {SearchCriteriaBySamplesComponent} from "./page-by-samples.component";
import {StoreModule} from '@ngrx/store';
import {reducers} from '../store/reducers';
import {StoreDevtoolsModule} from "@ngrx/store-devtools";
import {EffectsModule} from "@ngrx/effects";
import {TreeEffects} from "../store/effects/tree-effects";
import {TreeStructureService} from "../services/core/tree-structure-service";
import {FileItemEffects} from "../store/effects/file-item-effects";
import {FileItemService} from "../services/core/file-item-service";
import {InstructionSubmissionService} from "../services/core/instruction-submission-service";
import {DatasetDatatableComponent} from "../views/dataset-datatable.component";
import {FilterParamsColl} from "../services/core/filter-params-coll";
import {ViewIdGeneratorService} from "../services/core/view-id-generator-service";


@NgModule({
    imports: [BrowserModule,
        HttpModule,
        FormsModule,
        ReactiveFormsModule,
        TreeModule,
        CheckboxModule,
        SharedModule,
        DataTableModule,
        PanelModule,
        OverlayPanelModule,
        AccordionModule,
        ListboxModule,
        FieldsetModule,
        TabViewModule,
        DialogModule,
        TooltipModule,
        RadioButtonModule,
        routing,
        BrowserAnimationsModule,
        StoreModule.forRoot(reducers),
        EffectsModule.forRoot([TreeEffects,FileItemEffects]),
        StoreDevtoolsModule.instrument({
            maxAge: 25 //  Retains last 25 states
        })
    ],
    declarations: [
        AppComponent,
        ExtractorRoot,
        LoginComponent,
        ExportFormatComponent,
        StatusDisplayComponent,
        CheckListBoxComponent,
        SampleMarkerBoxComponent,
        FileSelectDirective,
        FileDropDirective,
        TextAreaComponent,
        UploaderComponent,
        SampleListTypeComponent,
        NameIdListBoxComponent,
        StatusDisplayTreeComponent,
        Button,
        SearchCriteriaBySamplesComponent,
        DatasetDatatableComponent],
    providers: [AuthGuard,
        AuthenticationService,
        DtoRequestService,
        NameIdService,
        TreeStructureService,
        FileItemService,
        ViewIdGeneratorService,
        InstructionSubmissionService,
        FilterParamsColl,
        {provide: APP_BASE_HREF, useValue: './'}],
    bootstrap: [AppComponent]
})

export class AppModule {
}
