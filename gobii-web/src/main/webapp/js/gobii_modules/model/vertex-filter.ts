import {NameId} from "./name-id";
import {Vertex} from "./vertex";

export class VertexFilterDTO {

    constructor(public destinationVertex: Vertex,
                public filterVertices: Vertex[],
                public vertexValues: NameId[],
                public markerCount: number,
                public sampleCount: number) {
    }

    public static fromJson(json:any):VertexFilterDTO {

        let returnVal: VertexFilterDTO = new VertexFilterDTO(
            json.destinationVertexDTO,
            json.filterVertices,
            json.vertexValues,
            json.markerCount,
            json.sampleCount
        );

        return returnVal;
    }

    public getJson(): any {

        let returnVal: any = {};

        returnVal.destinationVertexDTO = this.destinationVertex;
        returnVal.filterVertices = this.filterVertices;
        returnVal.vertexValues = this.vertexValues;
        returnVal.markerCount = this.markerCount;
        returnVal.sampleCount = this.sampleCount;

        return returnVal;
    }

}