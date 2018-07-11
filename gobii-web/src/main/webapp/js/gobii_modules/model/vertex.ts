import {EntitySubType, EntityType} from "./type-entity";
import {VertexType} from "./type-vertex";
import {CvGroup} from "./cv-group";
import {VertexNameType} from "./type-vertex-name";
import {ExtractorItemType} from "./type-extractor-item";
import {GobiiFileItemCompoundId} from "./gobii-file-item-compound-id";
import {NameId} from "./name-id";

export class Vertex extends GobiiFileItemCompoundId {

    constructor(public vertexId: number,
                public vertexNameType: VertexNameType,
                public gobiiVertexType: VertexType,
                public vertexName: string,
                public entityType: EntityType,
                public entitySubType: EntitySubType,
                public cvGroup: CvGroup,
                public cvTerm: string,
                public filterVals: NameId[]) {

        super(ExtractorItemType.VERTEX, entityType, entitySubType, cvGroup, cvTerm);
    }

    static fromVertex(vertexToCopy: Vertex): Vertex {
        return new Vertex(
            vertexToCopy.vertexId,
            vertexToCopy.vertexNameType,
            vertexToCopy.gobiiVertexType,
            vertexToCopy.vertexName,
            vertexToCopy.entityType,
            vertexToCopy.entitySubType,
            vertexToCopy.cvGroup,
            vertexToCopy.cvTerm,
            vertexToCopy.filterVals
        );
    }
}