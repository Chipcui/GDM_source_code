import {Component, OnInit, OnChanges, SimpleChange, EventEmitter} from "@angular/core";
import {NameId} from "../model/name-id";
import {DtoRequestService} from "../services/core/dto-request.service";
import {DtoRequestItemNameIds} from "../services/app/dto-request-item-nameids";
import {ProcessType} from "../model/type-process";
import {EntityType} from "../model/type-entity";
import {CheckBoxEvent} from "../model/event-checkbox";
import {DtoRequestItemDataSet} from "../services/app/dto-request-item-dataset";
import {DataSet} from "../model/dataset";
import {DtoRequestItemAnalysis} from "../services/app/dto-request-item-analysis";
import {Analysis} from "../model/analysis";
import {EntityFilter} from "../model/type-entity-filter";


@Component({
    selector: 'dataset-checklist-box',
    inputs: ['experimentId', 'checkBoxEventChange'],
    outputs: ['onItemChecked', 'onAddMessage'],
    template: `<checklist-box
                    [checkBoxEventChange] = "checkBoxEventChange"
                    [nameIdList] = "dataSetsNameIdList"
                    (onItemSelected)="handleItemSelected($event)"
                    (onItemChecked)="handleItemChecked($event)"
                    (onAddMessage) = "handleAddMessage($event)">
                </checklist-box>
                <div *ngIf="dataSet">
                    <BR>
                     <fieldset>
                        <b>Name:</b> {{dataSet.name}}<BR>
                        <b>Data Table:</b> {{dataSet.dataTable}}<BR>
                        <b>Data File:</b> {{dataSet.dataFile}}<BR>
                        <b>Quality Table:</b> {{dataSet.qualityTable}}<BR>
                        <b>Quality File:</b> {{dataSet.qualityFile}}<BR>
                        <div *ngIf="analysisNames && (analysisNames.length > 0)">
                            <b>Analyses:</b> <ul style="list-style-type:none">
                                            <li *ngFor= "let analysisName of analysisNames" >{{analysisName}}</li>
                                    </ul>
                        </div>
                        <div *ngIf="analysisTypes && (analysisTypes.length > 0)">
                            <b>Analysis Types:</b> <ul style="list-style-type:none">
                                            <li *ngFor= "let analysisType of analysisTypes" >{{analysisType}}</li>
                                    </ul>
                        </div>
                      </fieldset> 
                </div>                
` // end template

})


export class DataSetCheckListBoxComponent implements OnInit,OnChanges {

    constructor(private _dtoRequestServiceNameId:DtoRequestService<NameId[]>,
                private _dtoRequestServiceDataSetDetail:DtoRequestService<DataSet>,
                private _dtoRequestServiceAnalysisDetail:DtoRequestService<Analysis>) {

    } // ctor

    // useg
    private dataSetsNameIdList:NameId[];
    private checkBoxEvents:CheckBoxEvent[] = [];
    private experimentId:string;
    private onItemChecked:EventEmitter<CheckBoxEvent> = new EventEmitter();
    private onAddMessage:EventEmitter<string> = new EventEmitter();
    private dataSet:DataSet;
    private analysisNames:string[] = [];
    private analysisTypes:string[] = [];
    private nameIdListAnalysisTypes:NameId[];

    private handleItemChecked(arg) {

        this.onItemChecked.emit(arg);

    } // handleItemChecked()

    private handleAddMessage(arg) {

        this.onAddMessage.emit(arg);
    }

    private handleItemSelected(arg) {
        let selectedDataSetId:number = Number(arg.id);
        this.setDatasetDetails(selectedDataSetId);
    }

    private setList():void {

        // we can get this event whenver the item is clicked, not necessarily when the checkbox
        let scope$ = this;

        scope$.dataSetsNameIdList = [];
        scope$.checkBoxEvents = [];
        scope$.setDatasetDetails(undefined);

        if( scope$.experimentId) {

            this._dtoRequestServiceNameId.get(new DtoRequestItemNameIds(
                EntityType.DataSets,
                EntityFilter.BYTYPEID,
                this.experimentId)).subscribe(nameIds => {
                    if (nameIds && ( nameIds.length > 0 )) {

                        scope$.dataSetsNameIdList = nameIds;
                        // scope$.checkBoxEvents = [];
                        // scope$.dataSetsNameIdList.forEach(n => {
                        //     scope$.checkBoxEvents.push(new CheckBoxEvent(
                        //         ProcessType.CREATE,
                        //         n.id,
                        //         n.name,
                        //         false
                        //     ));
                        // });

                        scope$.setDatasetDetails(scope$.dataSetsNameIdList[0].id);

                    } else {
                        scope$.dataSetsNameIdList = [new NameId(0, "<none>")];
                        scope$.setDatasetDetails(undefined);
                    }
                },
                dtoHeaderResponse => {
                    dtoHeaderResponse.statusMessages.forEach(m => scope$.handleAddMessage("Retrieving dataset names by experiment id: "
                        + ": "
                        + m.message))
                });

        } // if we have an experiment id

    } // setList()

    private setDatasetDetails(dataSetId:number):void {

        if( dataSetId ) {
            let scope$ = this;
            scope$._dtoRequestServiceDataSetDetail.get(new DtoRequestItemDataSet(dataSetId))
                .subscribe(dataSet => {

                        if (dataSet) {

                            scope$.dataSet = dataSet;
                            scope$.analysisNames = [];
                            scope$.analysisTypes = [];

                            scope$.dataSet.analysesIds.forEach(
                                analysisId => {
                                    let currentAnalysisId:number = analysisId;
                                    if (currentAnalysisId) {
                                        scope$._dtoRequestServiceAnalysisDetail
                                            .getResult(new DtoRequestItemAnalysis(currentAnalysisId))
                                            .subscribe(analysis => {
                                                    scope$.analysisNames.push(analysis.analysisName);
                                                    if (analysis.anlaysisTypeId && scope$.nameIdListAnalysisTypes) {

                                                        scope$
                                                            .nameIdListAnalysisTypes
                                                            .forEach(t => {
                                                                if (Number(t.id) === analysis.anlaysisTypeId) {
                                                                    scope$.analysisTypes.push(t.name);
                                                                }
                                                            });


                                                    } // if we have an analysis type id
                                                },
                                                dtoHeaderResponse => {
                                                    dtoHeaderResponse.statusMessages.forEach(m => scope$.handleAddMessage(m.message));
                                                });
                                    }
                                }
                            );
                        }
                    },
                    dtoHeaderResponse => {
                        dtoHeaderResponse.statusMessages.forEach(m => scope$.handleAddMessage(m.message));
                    });
        } else {

            this.dataSet = undefined;
            this.analysisNames = [];
            this.analysisTypes = [];

        } // if else we got a dataset id
    } // setList()


    ngOnInit():any {

        let scope$ = this;
        scope$._dtoRequestServiceNameId
            .get(new DtoRequestItemNameIds(EntityType.CvTerms,
                EntityFilter.BYTYPENAME,
                "analysis_type"))
            .subscribe(nameIdList => {
                    scope$.nameIdListAnalysisTypes = nameIdList;
                    if (this.experimentId) {
                        this.setList();
                    }
                },
                dtoHeaderResponse => {
                    dtoHeaderResponse.statusMessages.forEach(m => scope$.handleAddMessage(m.message));
                });

    }

    ngOnChanges(changes:{[propName:string]:SimpleChange}) {

        if (changes['experimentId']) {
            this.experimentId = changes['experimentId'].currentValue;
            this.setList();
        }

    }
}
