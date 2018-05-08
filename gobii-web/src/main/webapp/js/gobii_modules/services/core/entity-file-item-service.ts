import {Injectable} from "@angular/core";
import {EntitySubType} from "../../model/type-entity";
import {Labels} from "../../views/entity-labels";
import {ExtractorItemType} from "../../model/type-extractor-item";
import {GobiiExtractFilterType} from "../../model/type-extractor-filter";
import {CvFilterType} from "../../model/cv-filter-type";
import {GobiiFileItem} from "../../model/gobii-file-item";
import {HeaderStatusMessage} from "../../model/dto-header-status-message";
import {ProcessType} from "../../model/type-process";
import {NameIdService} from "./name-id-service";
import {FilterParams} from "../../model/filter-params";
import * as historyAction from '../../store/actions/history-action';
import * as fileItemActions from '../../store/actions/fileitem-action'
import * as fromRoot from '../../store/reducers';

import {Store} from "@ngrx/store";
import {NameIdLabelType} from "../../model/name-id-label-type";
import {FilterType} from "../../model/filter-type";
import {FilterParamNames} from "../../model/file-item-param-names";
import {Observable} from "rxjs/Observable";
import "rxjs/add/operator/expand"
import "rxjs/add/operator/concat"
import {EntityStats} from "../../model/entity-stats";
import {DtoRequestService} from "./dto-request.service";
import {DtoRequestItemEntityStats, EntityRequestType} from "../app/dto-request-item-entity-stats";
import {FilterHistory} from "../../store/reducers/history-reducer";
import {DtoRequestItemGfi} from "../app/dto-request-item-gfi";
import {JsonToGfiDataset} from "../app/jsontogfi/json-to-gfi-dataset";
import {FilterParamsColl} from "./filter-params-coll";
import {GobiiFileItemEntityRelation} from "../../model/gobii-file-item-entity-relation";
import {GobiiFileItemCompoundId} from "../../model/gobii-file-item-compound-id";
import {StatusLevel} from "../../model/type-status-level";
import {DtoRequestItem} from "./dto-request-item";
import {PagedFileItemList} from "../../model/payload/paged-item-list";
import {Pagination} from "../../model/payload/pagination";
import {PayloadFilter} from "../../store/actions/action-payload-filter";

@Injectable()
export class EntityFileItemService {

    private readonly NONE_ITEM_ITEM_ID: string = "-1";

    constructor(private nameIdService: NameIdService,
                private entityStatsService: DtoRequestService<EntityStats>,
                private fileItemRequestService: DtoRequestService<GobiiFileItem[]>,
                private store: Store<fromRoot.State>,
                private filterParamsColl: FilterParamsColl) {

        // For non-hierarchically filtered request params, we just create them simply
        // as we add them to the flat map

    } // constructor


    public loadEntityList(gobiiExtractFilterType: GobiiExtractFilterType,
                          fileItemParamName: FilterParamNames) {

        let fileItemParams: FilterParams = this.filterParamsColl.getFilter(fileItemParamName, gobiiExtractFilterType);
        if (fileItemParams && fileItemParams.getFilterType() === FilterType.ENTITY_LIST) {
            this.makeFileItemActionsFromEntities(gobiiExtractFilterType, fileItemParams, null, false)
                .subscribe(action => {
                    if (action) {
                        this.store.dispatch(action);
                    }
                });
        }
    } // loadEntityList()


    public loadPagedEntityList(gobiiExtractFilterType: GobiiExtractFilterType,
                               fileItemParamName: FilterParamNames,
                               paedQueryId: string,
                               pageSize: number,
                               pageNum: number) {

        let fileItemParams: FilterParams = this.filterParamsColl.getFilter(fileItemParamName, gobiiExtractFilterType);

        if (fileItemParams.getIsPaged()) {
            fileItemParams.setPageSize(pageSize);
            fileItemParams.setPageNum(pageNum);
            fileItemParams.setPagedQueryId(paedQueryId);
            if (fileItemParams && fileItemParams.getFilterType() === FilterType.ENTITY_LIST) {
                this.makeFileItemActionsFromEntities(gobiiExtractFilterType, fileItemParams, null, false)
                    .subscribe(action => {
                        if (action) {
                            this.store.dispatch(action);
                        }
                    });
            }
        } else {
            this.store.dispatch(new historyAction.AddStatusAction(new HeaderStatusMessage("This filter does not support paging: " + fileItemParamName,
                StatusLevel.ERROR,
                null)));
        }

    } // loadEntityList()


    private makeFileItemActionsFromEntities(gobiiExtractFilterType: GobiiExtractFilterType,
                                            filterParams: FilterParams,
                                            filterValue: string,
                                            recurse: boolean): Observable<fileItemActions.LoadFileItemListWithFilterAction> {

        return Observable.create(observer => {

            try {

                // if (filterParams.getIsDynamicFilterValue()) {
                //     filterParams.setRelatedEntityFilterValue(filterValue);
                // }

                // note that this method does not do any of the entity dating and checking
                // thing. It needs to be reworked for paging so that the filter ID also takes
                // into account the current page -- i.e., so that the datetime stamp pertains to the
                // specific page. This is going to require some refactoring.

                if (filterParams.getFilterType() === FilterType.ENTITY_LIST) {


                    let dtoRequestItem: DtoRequestItem<any> = filterParams.getDtoRequestItem();


                    let dtoRequestService: DtoRequestService<any> = filterParams.getDtoRequestService();

                    dtoRequestService
                        .get(dtoRequestItem)
                        .subscribe(entityResult => {

                                let pagination: Pagination = null;
                                let entityItems: GobiiFileItem[] = [];
                                if (filterParams.getIsPaged()) {

                                    entityItems = entityResult.gobiiFileItems;
                                    pagination = entityResult.pagination;

                                } else {

                                    entityItems = entityResult;
                                }


                                entityItems.forEach(fi => {
                                    fi.setGobiiExtractFilterType(gobiiExtractFilterType);
                                });

                                let date: Date = new Date();
                                let loadAction: fileItemActions.LoadFileItemListWithFilterAction =
                                    new fileItemActions.LoadFileItemListWithFilterAction(
                                        {
                                            gobiiFileItems: entityItems,
                                            filterId: filterParams.getQueryName(),
                                            filter: new PayloadFilter(
                                                gobiiExtractFilterType,
                                                filterParams.getTargetEtityUniqueId(),
                                                filterParams.getRelatedEntityUniqueId(),
                                                filterValue,
                                                filterValue,
                                                date,
                                                pagination
                                            )
                                        }
                                    );

                                observer.next(loadAction);

                            },
                            responseHeader => {
                                this.store.dispatch(new historyAction.AddStatusAction(responseHeader));

                            });

                } else {

                    let extractFilterTypeString: string = "undefined";
                    if (gobiiExtractFilterType) {
                        extractFilterTypeString = GobiiExtractFilterType[gobiiExtractFilterType];
                    }
                    this.store.dispatch(new historyAction.AddStatusMessageAction("FileItemParams "
                        + filterParams.getQueryName()
                        + " for extract type "
                        + extractFilterTypeString
                        + " is not of type "
                        + FilterType[FilterType.ENTITY_LIST]
                    ));


                } // if else filterParams are correct

            } catch (error) {
                this.store.dispatch(new historyAction.AddStatusAction(error));
            }

        }); // Observer.create()

    }//makeFileItemActionsFromEntities()

}
