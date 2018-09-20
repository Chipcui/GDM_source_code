import {GobiiFileItemCompoundId} from "../../model/gobii-file-item-compound-id";
import {Pagination} from "../../model/payload/pagination";
import {GobiiExtractFilterType} from "../../model/type-extractor-filter";
import {Payload} from "../../model/payload/payload";

export class PayloadFilter {
    constructor(public gobiiExtractFilterType: GobiiExtractFilterType,
                public targetEntityUniqueId:GobiiFileItemCompoundId,
                public relatedEntityUniqueId:GobiiFileItemCompoundId,
                public relatedEntityFilterValue: string,
                public targetEntityFilterValue: any,
                public entityLasteUpdated: Date,
                public pagination: Pagination) {

    }

    static fromPayloadFilter(payloadFilterToCopy:PayloadFilter) : PayloadFilter {

        return new PayloadFilter(
            payloadFilterToCopy.gobiiExtractFilterType,
            payloadFilterToCopy.targetEntityUniqueId,
            payloadFilterToCopy.relatedEntityUniqueId,
            payloadFilterToCopy.relatedEntityFilterValue,
            payloadFilterToCopy.targetEntityFilterValue,
            payloadFilterToCopy.entityLasteUpdated,
            payloadFilterToCopy.pagination
        );
    }
}