import {HeaderNames} from "./header-names";
import {HttpHeaders} from "@angular/common/http";

export class HttpValues {

    public static S_FORBIDDEN = 403;

    public static makeTokenHeaders(token: string, gobiiCropType: string): HttpHeaders {

        let returnVal = this.makeContentHeaders();

        if (token) {
            returnVal = returnVal.append(HeaderNames.headerToken, token)
                .append(HeaderNames.headerGobiiCrop, gobiiCropType)
        }

        return returnVal;
    }

    public static makeContentHeaders(): HttpHeaders {
        let returnVal = new HttpHeaders()
            .append('Content-Type', 'application/json')
            .append('Accept', 'application/json');
        return returnVal;
    }

    public static makeLoginHeaders(userName: string, password): HttpHeaders {
        let returnVal: HttpHeaders = this.makeContentHeaders()
            .append(HeaderNames.headerUserName, userName)
            .append(HeaderNames.headerPassword, password);
        return returnVal;
    }
}