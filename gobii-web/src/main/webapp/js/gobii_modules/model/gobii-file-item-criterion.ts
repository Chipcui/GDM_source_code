import {EntitySubType, EntityType} from "./type-entity";
import {CvGroup} from "./cv-group";
import {GobiiExtractFilterType} from "./type-extractor-filter";
import {ExtractorItemType} from "./type-extractor-item";
import {GobiiFileItemCompoundId} from "./gobii-file-item-compound-id";

export class GobiiFileItemCriterion extends GobiiFileItemCompoundId {

    constructor(gobiiFileItemCompoundId: GobiiFileItemCompoundId,
                private _isPresent = false) {
        super(gobiiFileItemCompoundId.getExtractorItemType(),
            gobiiFileItemCompoundId.getEntityType(),
            gobiiFileItemCompoundId.getEntitySubType(),
            gobiiFileItemCompoundId.getCvGroup(),
            gobiiFileItemCompoundId.getCvFilterValue());
    }

    public isPresent(): boolean {
        return this._isPresent;
    }

    public setIPresent(value: boolean) {
        this._isPresent = value;
    }

}
