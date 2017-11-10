import {EntityFilter} from "./type-entity-filter";
import {EntityType, EntitySubType} from "./type-entity";
import {CvFilterType} from "./cv-filter-type";
import {GobiiExtractFilterType} from "./type-extractor-filter";
import {NameIdLabelType} from "./name-id-label-type";
import {ExtractorItemType} from "./type-extractor-item";
import {GobiiFileItemCompoundId} from "./gobii-file-item-compound-id";

/**
 * Created by Phil on 3/9/2017.
 */

/***
 * This class is used extensively for the purpose of retrieving and
 * managing the results of queries to the GOBII /names/{entity} service. The primary
 * use case is in the NameIdService's get() method, where values from this class
 * are used to set up the GET request to the /names/{entityResource}. Of particular
 * note is the use fo the _fkEntityFilterValue value for the purpose of retrieving
 * names for a given entity when that entity must be filtered according to a foreign key.
 * For example, when retrieving projects by contact_id (i.e., by principle investigator contact
 * id), the _fkEntityFilterValue will be the value of the PI according to which the project names
 * should be filtered.
 * The _parentFileItemParams and _childFileItemParams can be used to create a tree of instances
 * of this class that can be used for hierarchical filtering. That is to say, the parent/child
 * relationships of FileItemParam instances corresponds to the primary/foreign key relationships of the
 * tables involved in generating the query. In our example, the project-by-contact FileFilterParams would be a
 * child of the contact FileFilterParams.
 * When an array of GobiiFileItem instances is created from a query resulting from a FileFilterParams,
 * their parentItemId value is set to the _fkEntityFilterValue value of the FileFilterParams. Moreover,
 * for all filters, the _fkEntityFilterValue for the current state of the UI is preserved in the store.
 * Thus, for any given state of the store, with a given filter value, a selector can retrieve the
 * entities for a given filter value. For example, when projects are retrieved for a given contact id,
 * the project query's filter is set to that contact when the project file items are added to the store.
 * When we want to get the "currently selected" projects from the store (i.e., the projects filtered for
 * the pi who is currently selected in the UI), the selector returns the file items whose parent id
 * matches current contact ID in state.
 *
 */
export class FileItemParams extends GobiiFileItemCompoundId {

    private constructor(private _queryName: string = null,
                        _entityType: EntityType = EntityType.UNKNOWN,
                        private _entityFilter: EntityFilter = EntityFilter.NONE,
                        private _fkEntityFilterValue: string = null,
                        private _selectedItemId: string = null,
                        _entitySubType: EntitySubType = EntitySubType.UNKNOWN,
                        _cvFilterType: CvFilterType = CvFilterType.UNKNOWN,
                        private _gobiiExtractFilterType: GobiiExtractFilterType = GobiiExtractFilterType.UNKNOWN,
                        private _nameIdLabelType: NameIdLabelType,
                        _extractorItemType: ExtractorItemType,
                        private _parentFileItemParams: FileItemParams,
                        private _childFileItemParams: FileItemParams[],
                        private _isDynamicFilterValue:boolean) {

        super(_extractorItemType,_entityType,_entitySubType,_cvFilterType);


    }

    public static build(queryName: string,
                        gobiiExtractFilterType: GobiiExtractFilterType,
                        entityType: EntityType): FileItemParams {
        return ( new FileItemParams(queryName,
            entityType,
            EntityFilter.NONE,
            null,
            null,
            EntitySubType.UNKNOWN,
            CvFilterType.UNKNOWN,
            gobiiExtractFilterType,
            NameIdLabelType.UNKNOWN,
            ExtractorItemType.ENTITY,
            null,
            [],
            true));
    }

    getQueryName(): string {
        return this._queryName;
    }


    getExtractorItemType(): ExtractorItemType {
        return super.getExtractorItemType();
    }

    setExtractorItemType(value: ExtractorItemType): FileItemParams {

        super.setExtractorItemType(value);
        return this;
    }

    getEntityType(): EntityType {
        return super.getEntityType();
    }

    setEntityType(value: EntityType): FileItemParams {

        super.setEntityType(value);
        return this;
    }

    getEntitySubType(): EntitySubType {
        return super.getEntitySubType();
    }

    setEntitySubType(value: EntitySubType): FileItemParams {

        super.setEntitySubType(value);
        return this;
    }

    getCvFilterType(): CvFilterType {
        return super.getCvFilterType();
    }

    setCvFilterType(value: CvFilterType): FileItemParams {
        super.setCvFilterType(value);
        return this;
    }

    getEntityFilter(): EntityFilter {
        return this._entityFilter;
    }

    setEntityFilter(value: EntityFilter): FileItemParams {
        this._entityFilter = value;
        return this;
    }

    getFkEntityFilterValue(): string {
        return this._fkEntityFilterValue;
    }

    setFkEntityFilterValue(value: string): FileItemParams {
        this._fkEntityFilterValue = value;
        return this;
    }

    getGobiiExtractFilterType(): GobiiExtractFilterType {
        return this._gobiiExtractFilterType;
    }

    setGobiiExtractFilterType(value: GobiiExtractFilterType): FileItemParams {
        this._gobiiExtractFilterType = value;
        return this;
    }


    setNameIdLabelType(nameIdLabelType: NameIdLabelType) {
        this._nameIdLabelType = nameIdLabelType;
        return this;
    }

    getMameIdLabelType(): NameIdLabelType {
        return this._nameIdLabelType;
    }

    setParentFileItemParams(fileItemParams: FileItemParams): FileItemParams {
        this._parentFileItemParams = fileItemParams;
        return this;
    }

    getChildFileItemParams(): FileItemParams[] {
        return this._childFileItemParams;
    }

    setChildNameIdRequestParams(childNameIdRequestParams: FileItemParams[]): FileItemParams {
        this._childFileItemParams = childNameIdRequestParams;
        return this;
    }

    setIsDynamicFilterValue(dynamicFilterValue:boolean): FileItemParams {
        this._isDynamicFilterValue = dynamicFilterValue;
        return this;
    }

    getIsDynamicFilterValue(): boolean {
        return this._isDynamicFilterValue;
    }


}