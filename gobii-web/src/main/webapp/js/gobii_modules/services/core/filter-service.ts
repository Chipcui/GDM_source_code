import {Injectable} from "@angular/core";
import {GobiiExtractFilterType} from "../../model/type-extractor-filter";
import {GobiiFileItem} from "../../model/gobii-file-item";
import {FilterParams} from "../../model/filter-params";
import * as historyAction from '../../store/actions/history-action';
import * as fileItemActions from '../../store/actions/fileitem-action'
import * as fromRoot from '../../store/reducers';

import {Store} from "@ngrx/store";
import {FilterParamNames} from "../../model/file-item-param-names";
import {Observable} from "rxjs/Observable";
import "rxjs/add/operator/expand"
import "rxjs/add/operator/concat"
import {FilterParamsColl} from "./filter-params-coll";
import {PayloadFilter} from "../../store/actions/action-payload-filter";
import {CvGroup} from "../../model/cv-group";
import {Labels} from "../../views/entity-labels";
import {HeaderStatusMessage} from "../../model/dto-header-status-message";
import {ProcessType} from "../../model/type-process";
import {ExtractorItemType} from "../../model/type-extractor-item";
import {EntitySubType, EntityType} from "../../model/type-entity";
import {NameIdLabelType} from "../../model/name-id-label-type";

/***
 * There is something going on in this class that I have concerns about.
 * In essence, filters in the filter params collection are being modified
 * in real-time. This means, in essence, that we are storing some kind of
 * state in the filter params collection rather than in the ngrx/store. All state
 * should be kept in the store and retrieved with a selector. There is a symptom
 * of this problem that I am working around by introducing an init() method to
 * filter params collection and calling it whenever the extract type is changed.
 * Without that change, what was happening was that if you selected, say, Principle
 * Investigator in FB1, and then hit the clear button, you would see the an an
 * item in the vertex list called "Select a Principle Investigator." If you
 * look at th code in entity file item service that is loading the vertices, you'll
 * see that the label-making method creates the "Select a Principle Investigator"
 * label item because it thinks that the FB1 filter is of entity type Principle Investigator.
 * In other words, the change we make to FB1 filter in in this class persists
 * after you do a clear (the clear simply triggering a change extract type event).
 * Making the init() method can calling it in the extract type change method
 * solves the problem for now. So eventually, to really make this filtering code
 * robust, we need to move that init() code back to the constructor of the params coll
 * class, and treat it as the canary in the coal mine: when that init code is only
 * ever called once, this class should be modified so that filter state is not persisted in the
 * filter params coll, and then it should be that selecting a vertex does not result in
 * creating a rogue label item.
 *
 *
 */
@Injectable()
export class FilterService {

    constructor(private store: Store<fromRoot.State>,
                private filterParamsColl: FilterParamsColl) {

    } // constructor

    public init() {
        this.filterParamsColl.init();
    }

    public loadFilter(gobiiExtractFilterType: GobiiExtractFilterType,
                      filterParamsName: FilterParamNames,
                      filterValue: any) {

        let filterParams: FilterParams = this.filterParamsColl.getFilter(filterParamsName, gobiiExtractFilterType);

        if (filterParams) {

            let loadAction: fileItemActions.LoadFilterAction = new fileItemActions.LoadFilterAction(
                {
                    filterId: filterParams.getQueryName(),
                    filter: new PayloadFilter(
                        gobiiExtractFilterType,
                        filterParams.getTargetEntityUniqueId(),
                        filterParams.getRelatedEntityUniqueId(),
                        null,
                        filterValue,
                        null,
                        null
                    )
                }
            );

            this.store.dispatch(loadAction)

        } else {
            this.store.dispatch(new historyAction.AddStatusMessageAction("Error loading filter: there is no query params object for query "
                + filterParamsName
                + " with extract filter type "
                + GobiiExtractFilterType[gobiiExtractFilterType]));
        }
    }

    public getForFilter(filterParamName: FilterParamNames): Observable<GobiiFileItem[]> {

        //Wrapping an Observable around the select functions just doesn't work. So at leaset this
        //function can encapsualte getting the correct selector for the filter
        let returnVal: Observable<GobiiFileItem[]>;

        switch (filterParamName) {

            case FilterParamNames.MARKER_GROUPS:
                returnVal = this.store.select(fromRoot.getMarkerGroups);
                break;

            case FilterParamNames.PROJECTS:
                returnVal = this.store.select(fromRoot.getProjects);
                break;

            case FilterParamNames.PROJECTS_BY_CONTACT:
                returnVal = this.store.select(fromRoot.getProjectsByPI);
                break;

            case FilterParamNames.EXPERIMENTS_BY_PROJECT:
                returnVal = this.store.select(fromRoot.getExperimentsByProject);
                break;

            case FilterParamNames.EXPERIMENTS:
                returnVal = this.store.select(fromRoot.getExperiments);
                break;

            case FilterParamNames.DATASETS_BY_EXPERIMENT:
                returnVal = this.store.select(fromRoot.getDatasetsByExperiment);
                break;

            case FilterParamNames.PLATFORMS:
                returnVal = this.store.select(fromRoot.getPlatforms);
                break;

            case FilterParamNames.CV_DATATYPE:
                returnVal = this.store.select(fromRoot.getCvTermsDataType);
                break;

            case FilterParamNames.CV_JOB_STATUS:
                returnVal = this.store.select(fromRoot.getCvTermsJobStatus);
                break;

            case FilterParamNames.MAPSETS:
                returnVal = this.store.select(fromRoot.getMapsets);
                break;

            case FilterParamNames.CONTACT_PI_HIERARCHY_ROOT:
                returnVal = this.store.select(fromRoot.getPiContacts);
                break;

            case FilterParamNames.CONTACT_PI_FILTER_OPTIONAL:
                returnVal = this.store.select(fromRoot.getPiContactsFilterOptional);
                break;

            case FilterParamNames.PROJECT_FILTER_OPTIONAL:
                returnVal = this.store.select(fromRoot.getProjectsFilterOptional);
                break;

            case FilterParamNames.EXPERIMENT_FILTER_OPTIONAL:
                returnVal = this.store.select(fromRoot.getExperimentsFilterOptional);
                break;

            //***************************
            // the FQ filters are for now just placeholders

            //------- F1 --------------------------------------
            case FilterParamNames.FQ_F1_VERTICES:
                returnVal = this.store.select(fromRoot.getFqF1Vertices);
                break;

            case FilterParamNames.FQ_F1_VERTEX_VALUES:
                returnVal = this.store.select(fromRoot.getFqF1VerticesValues);
                break;

            //------- F2 --------------------------------------
            case FilterParamNames.FQ_F2_VERTICES:
                returnVal = this.store.select(fromRoot.getFqF2Vertices);
                break;

            case FilterParamNames.FQ_F2_VERTEX_VALUES:
                returnVal = this.store.select(fromRoot.getFqF2VerticesValues);
                break;

            //------- F3 --------------------------------------
            case FilterParamNames.FQ_F3_VERTICES:
                returnVal = this.store.select(fromRoot.getFqF3Vertices);
                break;

            case FilterParamNames.FQ_F3_VERTEX_VALUES:
                returnVal = this.store.select(fromRoot.getFqF3VerticesValues);
                break;

            //------- F4 --------------------------------------
            case FilterParamNames.FQ_F4_VERTICES:
                returnVal = this.store.select(fromRoot.getFqF4Vertices);
                break;

            case FilterParamNames.FQ_F4_VERTEX_VALUES:
                returnVal = this.store.select(fromRoot.getFqF4VerticesValues);
                break;

            default:
                this.store.dispatch(new historyAction.AddStatusMessageAction("There is no selector for filter "
                    + filterParamName));
                break;

        }

        return returnVal;

    } // getForFilter()

    public makeLabelItem(gobiiExtractFilterType: GobiiExtractFilterType, filterParamsToLoad: FilterParams): GobiiFileItem {

        let returnVal: GobiiFileItem;

        if (filterParamsToLoad.getNameIdLabelType() != NameIdLabelType.UNKNOWN) {

            let entityName: string = "";
            if (filterParamsToLoad.getCvGroup() !== CvGroup.UNKNOWN) {
                entityName += Labels.instance().cvGroupLabels[filterParamsToLoad.getCvGroup()];
            } else if (filterParamsToLoad.getEntitySubType() !== EntitySubType.UNKNOWN) {
                entityName += Labels.instance().entitySubtypeNodeLabels[filterParamsToLoad.getEntitySubType()];
            } else if (filterParamsToLoad.getEntityType() != EntityType.UNKNOWN) {
                entityName += Labels.instance().entityNodeLabels[filterParamsToLoad.getEntityType()];
            } else {
                entityName += Labels.instance().treeExtractorTypeLabels[filterParamsToLoad.getExtractorItemType()];
            }

            let label: string = "";
            switch (filterParamsToLoad.getNameIdLabelType()) {

                case NameIdLabelType.SELECT_A:
                    label = "Select a " + entityName;
                    break;

                // we require that these entity labels all be in the singular
                case NameIdLabelType.ALL:
                    label = "All " + entityName + "s";
                    break;

                case NameIdLabelType.NO:
                    label = "No " + entityName;
                    break;

                default:
                    this.store.dispatch(new historyAction.AddStatusAction(new HeaderStatusMessage("Unknown label type "
                        + NameIdLabelType[filterParamsToLoad.getNameIdLabelType()], null, null)));

            }


            returnVal = GobiiFileItem
                .build(gobiiExtractFilterType, ProcessType.CREATE)
                .setEntityType(filterParamsToLoad.getEntityType())
                .setEntitySubType(filterParamsToLoad.getEntitySubType())
                .setCvGroup(filterParamsToLoad.getCvGroup())
                .setExtractorItemType(ExtractorItemType.UNKNOWN)
                .setNameIdLabelType(filterParamsToLoad.getNameIdLabelType())
                .setItemName(label)
                .setIsExtractCriterion(filterParamsToLoad.getIsExtractCriterion())
                .setItemId("0");

        } // if we have a label item type that requires a label item

        return returnVal;
    } // makeLabelItem()

}
