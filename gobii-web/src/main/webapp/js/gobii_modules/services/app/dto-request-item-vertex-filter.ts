import {Injectable} from "@angular/core";
import {DtoRequestItem} from "./../core/dto-request-item";
import {ProcessType} from "../../model/type-process";
import {ExtractorInstructionFilesDTO} from "../../model/extractor-instructions/dto-extractor-instruction-files"
import {PayloadEnvelope} from "../../model/payload/payload-envelope";
import {VertexFilterDTO} from "../../model/vertex-filter";


@Injectable()
export class DtoRequestItemVertexFilterDTO implements DtoRequestItem<VertexFilterDTO> {

    private isCountRequest: boolean;
    private jobId: string;

    public constructor(private vertexFilterDTO: VertexFilterDTO,
                       jobId: string,
                       isCountRequest: boolean) {
        this.vertexFilterDTO = vertexFilterDTO;
        this.jobId = jobId;
        this.isCountRequest = isCountRequest;
    }

    public getUrl(): string {

        let returnVal: string;

        if (this.isCountRequest) {
            returnVal = "gobii/v1/vertices/" + this.jobId + "/count";
        } else {
            returnVal = "gobii/v1/vertices/" + this.jobId + "/values";
        }

        return returnVal;

    } // getUrl()

    private processType: ProcessType = ProcessType.CREATE;

    public getRequestBody(): string {


        let rawJsonExtractorInstructionFileDTO: any = this.vertexFilterDTO.getJson();

        let payloadEnvelope = PayloadEnvelope.wrapSingleDTOInJSON(rawJsonExtractorInstructionFileDTO);

        let returnVal: string = JSON.stringify(payloadEnvelope);

        return returnVal;
    }

    public resultFromJson(json): VertexFilterDTO {

        let returnVal: VertexFilterDTO = VertexFilterDTO.fromJson(json.payload.data[0]);

        return returnVal;
    }


} // DtoRequestItemNameIds()


