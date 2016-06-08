import {NameId} from '../../model/name-id';




export interface INameIdPayload {
    entityType:string;
    nameIds: NameId[];
}
