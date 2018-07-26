import {NameId} from "./name-id";
import {Vertex} from "./vertex";

export class VertexFilterDTO {

    constructor(public destinationVertex: Vertex,
                public filterVertices: Vertex[],
                public vertexValues: NameId[],
                public markerCount: number,
                public markerFileFqpn: string,
                public markerCountMs: number,
                public sampleCount: number,
                public sampleFileFqpn: string,
                public sampleCountMs: number) {
    }

    public static fromJson(json: any): VertexFilterDTO {

        let returnVal: VertexFilterDTO = new VertexFilterDTO(
            json.destinationVertexDTO,
            json.filterVertices,
            json.vertexValues,
            json.markerCount,
            json.markerFileFqpn,
            json.markerCountMs,
            json.sampleCount,
            json.sampleFileFqpn,
            json.sampleCountMs
        );

        return returnVal;
    }

    public getJson(): any {

        let returnVal: any = {};

        returnVal.destinationVertexDTO = this.destinationVertex;
        returnVal.filterVertices = this.filterVertices;
        returnVal.vertexValues = this.vertexValues;
        returnVal.markerCount = this.markerCount;
        returnVal.markerFileFqpn = this.markerFileFqpn;
        returnVal.markerCountMs = this.markerCountMs;
        returnVal.sampleCount = this.sampleCount;
        returnVal.sampleFileFqpn = this.sampleFileFqpn;
        returnVal.sampleCountMs = this.sampleCountMs;

        return returnVal;
    }

}