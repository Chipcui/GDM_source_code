import {GobiiFileItemCompoundId} from "../../model/gobii-file-item-compound-id";
import {Pagination} from "../../model/payload/pagination";
import {GobiiExtractFilterType} from "../../model/type-extractor-filter";
import {Payload} from "../../model/payload/payload";
import {PayloadFilter} from "./action-payload-filter";

export class FilterCountState {
    constructor(public flexQueryFilters: Map<string, PayloadFilter>,
                public markerCount: number,
                public sampleCount: number) {

    }

}