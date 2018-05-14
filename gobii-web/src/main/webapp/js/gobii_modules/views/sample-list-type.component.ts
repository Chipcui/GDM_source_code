import {Component, EventEmitter, OnChanges, OnInit, SimpleChange, ViewEncapsulation} from '@angular/core';
import {GobiiExtractFilterType} from "../model/type-extractor-filter";
import {GobiiSampleListType} from "../model/type-extractor-sample-list";
import {HeaderStatusMessage} from "../model/dto-header-status-message";
import {GobiiFileItem} from "../model/gobii-file-item";
import {ProcessType} from "../model/type-process";
import {ExtractorItemType} from "../model//type-extractor-item";
import {Observable} from "rxjs/Observable";
import * as fromRoot from '../store/reducers';
import {Store} from "@ngrx/store";
import {NameIdFileItemService} from "../services/core/nameid-file-item-service";

/***
 * The RadioButton module is not compatabile with font-awesome. So
 * we have to turn out native ViewEncapsulation (i.e., put a protective
 * CSS bubble around our component's corner of the DOM) and manually include our
 * style sheets. Yes, I tried removing front-awesome from index.html and including it
 * explicitly in the status-display-tree-component, which is the only place it is used.
 * There should be a way to get that to work, but I was not able to.
 */
@Component({
    selector: 'sample-list-type',
    inputs: ['gobiiExtractFilterType'],
    outputs: ['onHeaderStatusMessage'],
    encapsulation: ViewEncapsulation.Native,
    styleUrls: ["js/node_modules/primeng/resources/themes/omega/theme.css",
        "js/node_modules/primeng/resources/primeng.css",
        "js/node_modules/bootswatch/dist/cerulean/bootstrap.css"],
    template: `
        <form>
            <label class="the-legend">List Item Type:&nbsp;</label>
            <BR>
            <div class="ui-g" style="width:250px;margin-bottom:5px">
                <div class="ui-g-12" style="height:30px">
                    <p-radioButton
                            (ngModelChange)="handleSampleTypeSelected($event)"
                            [ngModel]="(sampleListType$ | async).getItemId()"
                            name="listType"
                            value="GERMPLASM_NAME"
                            label="Germplasm Name">
                    </p-radioButton>
                </div>
                <div class="ui-g-12" style="height:30px">
                    <p-radioButton
                            (ngModelChange)="handleSampleTypeSelected($event)"
                            [ngModel]="(sampleListType$ | async).getItemId()"
                            name="listType"
                            value="EXTERNAL_CODE"
                            label="External Code"></p-radioButton>
                </div>
                <div class="ui-g-12" style="height:30px">
                    <p-radioButton
                            (ngModelChange)="handleSampleTypeSelected($event)"
                            [ngModel]="(sampleListType$ | async).getItemId()"
                            name="listType"
                            value="DNA_SAMPLE"
                            label="DNA Sample"></p-radioButton>
                </div>
            </div>
        </form>` // end template
})

export class SampleListTypeComponent implements OnInit, OnChanges {

    constructor(private store: Store<fromRoot.State>,
                private fileItemService: NameIdFileItemService) {
    } // ctor

    private onHeaderStatusMessage: EventEmitter<HeaderStatusMessage> = new EventEmitter();
    private gobiiExtractFilterType: GobiiExtractFilterType = GobiiExtractFilterType.UNKNOWN;
    public sampleListType$: Observable<GobiiFileItem> = this.store.select(fromRoot.getSelectedSampleType);

    public handleSampleTypeSelected(radioValue: string) {

        let gobiiSampleListType: GobiiSampleListType = GobiiSampleListType[radioValue];

        this.fileItemService
            .replaceFileItemByCompoundId(GobiiFileItem.build(this.gobiiExtractFilterType, ProcessType.CREATE)
                .setExtractorItemType(ExtractorItemType.SAMPLE_LIST_TYPE)
                .setItemName(GobiiSampleListType[gobiiSampleListType])
                .setItemId(GobiiSampleListType[gobiiSampleListType]));

    }


    ngOnInit(): any {
    }


    ngOnChanges(changes: { [propName: string]: SimpleChange }) {
    }
}
