import {HeaderNames} from "./header-names";
import {HttpHeaders} from "@angular/common/http";

export class HttpValues {

    public static S_FORBIDDEN = 403;

    public static makeTokenHeaders(token:string,gobiiCropType:string): HttpHeaders {

        let returnVal = this.makeContentHeaders();
        returnVal.append(HeaderNames.headerToken, token);
        returnVal.append(HeaderNames.headerGobiiCrop, gobiiCropType);
        return returnVal;
    }
    
    public static makeContentHeaders(): HttpHeaders {
        let returnVal = new HttpHeaders();
        returnVal.append('Content-Type', 'application/json');
        returnVal.append('Accept', 'application/json');
        return returnVal;
    }
    
    public static makeLoginHeaders(userName:string,password): HttpHeaders {
        let returnVal: HttpHeaders = this.makeContentHeaders();
        returnVal.append(HeaderNames.headerUserName, userName);
        returnVal.append(HeaderNames.headerPassword, password);
        return returnVal;
    }
}