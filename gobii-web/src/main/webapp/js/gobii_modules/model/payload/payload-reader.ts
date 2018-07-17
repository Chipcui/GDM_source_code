import {PayloadEnvelope} from "./payload-envelope";
import {DtoRequestItem} from "../../services/core/dto-request-item";

export class PayloadReader<T> {

    private payloadEnvelope: PayloadEnvelope;
    private data: T;

    public constructor(private json: any,
                       private dtoRequestItem: DtoRequestItem<T>) {

        this.payloadEnvelope = PayloadEnvelope.fromJSON(json);

        if (this.payloadEnvelope.header.status.succeeded) {
            this.data = this.dtoRequestItem.resultFromJson(this.json);
        }
    }

    public succeeded(): boolean {
        return this.payloadEnvelope.header.status.succeeded;
    }

    public getData(): T {
        return this.data;
    }

    public getError(): string {

        let returnVal: string = "";

        this.payloadEnvelope.header.status.statusMessages.forEach(statusMessage => {
                returnVal += statusMessage.message;
            }
        )

        return returnVal;
    }

}
