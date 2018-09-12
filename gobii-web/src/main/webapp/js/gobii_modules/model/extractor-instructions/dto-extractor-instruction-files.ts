import {GobiiExtractorInstruction} from "./gobii-extractor-instruction";

export class ExtractorInstructionFilesDTO {

    constructor(private gobiiExtractorInstructions: GobiiExtractorInstruction[],
                private jobId: string) {

        this.gobiiExtractorInstructions = gobiiExtractorInstructions;
        this.jobId= jobId;

    } // ctor

    public getGobiiExtractorInstructions(): any {
        return this.gobiiExtractorInstructions;
    }

    public setGobiiExtractorInstructions(value: any) {
        this.gobiiExtractorInstructions = value;
    }

    public getjobId(): string {
        return this.jobId;
    }

    public setjobId(value: string) {
        this.jobId = value;
    }


    public getJson(): any {

        let returnVal: any = {};

        returnVal.jobId = this.jobId;
        returnVal.gobiiExtractorInstructions = [];

        this.gobiiExtractorInstructions.forEach(i => {
            returnVal.gobiiExtractorInstructions.push(i.getJson());
        });

        return returnVal;

    } // getJson()

    public static fromJson(json: any): ExtractorInstructionFilesDTO {

        let gobiiExtractorInstructions: GobiiExtractorInstruction[] = [];

        json.gobiiExtractorInstructions.forEach(i =>
            gobiiExtractorInstructions.push(GobiiExtractorInstruction.fromJson(i)));

        let returnVal: ExtractorInstructionFilesDTO = new ExtractorInstructionFilesDTO(
            gobiiExtractorInstructions,
            json.jobId
        );

        return returnVal;

    }
}
