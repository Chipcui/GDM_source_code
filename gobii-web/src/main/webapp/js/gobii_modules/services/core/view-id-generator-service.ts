import {FilterParamNames} from "../../model/file-item-param-names";
import {TypeControl} from "./type-control";
import {Injectable} from "@angular/core";
import {GobiiExtractFormat} from "../../model/type-extract-format";
import {GobiiSampleListType} from "../../model/type-extractor-sample-list";

@Injectable()
export class ViewIdGeneratorService {

    public makeIdNameIdListBoxId(filterParamName:FilterParamNames): string {
        return TypeControl[TypeControl.NAME_ID_LIST] + "_" + filterParamName;
    }

    public makeDatasetRowCheckboxId(datasetName:String): string {
        return TypeControl[TypeControl.DATASET_ROW_CHECKBOX] + "_" + datasetName ;
    }

    public makeExportFormatRadioButtonId(gobiiExtractFormat:GobiiExtractFormat) {
        return TypeControl[TypeControl.EXPORT_FORMAT_RADIO_BUTTON] + "_" + GobiiExtractFormat[gobiiExtractFormat];
    } //makeExportFormatRadioButtonId()

    public makeStandardId(typeControl:TypeControl): string {
        return TypeControl[typeControl];
    }

    public makeSampleListTypeId(gobiiSampleListType:GobiiSampleListType): string {
        return TypeControl[TypeControl.SAMPLE_LIST_TYPE_RADIO_BUTTON] + "_" + GobiiSampleListType[gobiiSampleListType];
    }

}