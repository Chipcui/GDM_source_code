import {EntitySubType, EntityType} from "./type-entity";
import {CvGroup} from "./cv-group";
import {GobiiExtractFilterType} from "./type-extractor-filter";
import {ExtractorItemType} from "./type-extractor-item";

export class GobiiFileItemCompoundId {

    constructor(private _extractorItemType: ExtractorItemType = ExtractorItemType.UNKNOWN,
                private _entityType: EntityType = EntityType.UNKNOWN,
                private _entitySubType: EntitySubType = EntitySubType.UNKNOWN,
                private _cvGroup: CvGroup = CvGroup.UNKNOWN,
                private _cvTerm: string = null,
                private _cvFilterValue: string = null,
                private _isExtractCriterion: boolean = true,
                private _sequenceNum: number = 0) {

        if (this._cvGroup === null) {
            this._cvGroup = CvGroup.UNKNOWN;
        }

        if (this._extractorItemType == null) {
            this._extractorItemType = ExtractorItemType.UNKNOWN;
        }

        if (this._entityType == null) {
            this._entityType = EntityType.UNKNOWN;
        }

        if (this._entitySubType == null) {
            this._entitySubType = EntitySubType.UNKNOWN;
        }

    }

    public static fromGobiiFileItemCompoundId(gobiiFileItemCompoundId: GobiiFileItemCompoundId): GobiiFileItemCompoundId {

        return new GobiiFileItemCompoundId(
            gobiiFileItemCompoundId.getExtractorItemType(),
            gobiiFileItemCompoundId.getEntityType(),
            gobiiFileItemCompoundId.getEntitySubType(),
            gobiiFileItemCompoundId.getCvGroup(),
            gobiiFileItemCompoundId.getCvTerm(),
            gobiiFileItemCompoundId.getCvFilterValue(),
            gobiiFileItemCompoundId.getIsExtractCriterion(),
            gobiiFileItemCompoundId.getSequenceNum()
        );

    }

    public compoundIdeEquals(gobiiFileItemCompoundId: GobiiFileItemCompoundId): boolean {
        return (

            // (this.getSequenceNum() > 0 &&
            //     this.getSequenceNum() === gobiiFileItemCompoundId.getSequenceNum())
            // ||
            (this.getExtractorItemType() === gobiiFileItemCompoundId.getExtractorItemType() // for FlexQuery filter items
                && (this.getEntityType() === gobiiFileItemCompoundId.getEntityType())
                && (this.getEntitySubType() === gobiiFileItemCompoundId.getEntitySubType())
                && (this.getCvGroup() === gobiiFileItemCompoundId.getCvGroup())
                && this.getCvFilterValue() === gobiiFileItemCompoundId.getCvFilterValue()
                && this.getCvTerm() === gobiiFileItemCompoundId.getCvTerm())
        )
    }

    getExtractorItemType(): ExtractorItemType {
        return this._extractorItemType;
    }

    setExtractorItemType(value: ExtractorItemType): GobiiFileItemCompoundId {
        if (value != null) {
            this._extractorItemType = value;
        } else {
            this._extractorItemType = ExtractorItemType.UNKNOWN;
        }

        return this;
    }

    getEntityType(): EntityType {
        return this._entityType;
    }

    setEntityType(value: EntityType): GobiiFileItemCompoundId {

        if (value != null) {
            this._entityType = value;
        } else {
            this._entityType = EntityType.UNKNOWN;
        }

        return this;
    }

    getEntitySubType(): EntitySubType {
        return this._entitySubType;
    }

    setEntitySubType(value: EntitySubType): GobiiFileItemCompoundId {

        if (value != null) {
            this._entitySubType = value;
        } else {
            this._entitySubType = EntitySubType.UNKNOWN;
        }

        return this;
    }

    getCvGroup(): CvGroup {
        return this._cvGroup;
    }

    setCvGroup(value: CvGroup): GobiiFileItemCompoundId {
        if (value != null) {
            this._cvGroup = value;
        } else {
            this._cvGroup = CvGroup.UNKNOWN;
        }
        return this;
    }

    getCvTerm(): string {
        return this._cvTerm;
    }

    setCvTerm(value: string): GobiiFileItemCompoundId {
        this._cvTerm = value;
        return this;
    }

    getCvFilterValue(): string {
        return this._cvFilterValue;
    }

    setCvFilterValue(value: string): GobiiFileItemCompoundId {

        this._cvFilterValue = value;
        return this;
    }


    getIsExtractCriterion(): boolean {
        return this._isExtractCriterion;
    }

    setIsExtractCriterion(value: boolean): GobiiFileItemCompoundId {
        this._isExtractCriterion = value;
        return this;
    }

    getSequenceNum(): number {
        return this._sequenceNum;
    }

    setSequenceNum(value: number): GobiiFileItemCompoundId {
        this._sequenceNum = value;
        return this;
    }
}
