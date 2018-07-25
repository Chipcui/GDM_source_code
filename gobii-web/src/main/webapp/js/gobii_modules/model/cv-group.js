System.register([], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var CvGroup, getCvGroupName;
    return {
        setters: [],
        execute: function () {
            (function (CvGroup) {
                CvGroup[CvGroup["UNKNOWN"] = 0] = "UNKNOWN";
                CvGroup[CvGroup["ANY"] = 1] = "ANY";
                CvGroup[CvGroup["JOBTYPE"] = 2] = "JOBTYPE";
                CvGroup[CvGroup["PAYLOADTYPE"] = 3] = "PAYLOADTYPE";
                CvGroup[CvGroup["JOBSTATUS"] = 4] = "JOBSTATUS";
                CvGroup[CvGroup["ANALYSIS_TYPE"] = 5] = "ANALYSIS_TYPE";
                CvGroup[CvGroup["DATASET_TYPE"] = 6] = "DATASET_TYPE";
                CvGroup[CvGroup["DNARUN_PROP"] = 7] = "DNARUN_PROP";
                CvGroup[CvGroup["DNASAMPLE_PROP"] = 8] = "DNASAMPLE_PROP";
                CvGroup[CvGroup["PROJECT_PROP"] = 9] = "PROJECT_PROP";
                CvGroup[CvGroup["GERMPLASM_PROP"] = 10] = "GERMPLASM_PROP";
                CvGroup[CvGroup["GERMPLASM_SPECIES"] = 11] = "GERMPLASM_SPECIES";
                CvGroup[CvGroup["GERMPLASM_TYPE"] = 12] = "GERMPLASM_TYPE";
                CvGroup[CvGroup["GOBII_DATAWAREHOUSE"] = 13] = "GOBII_DATAWAREHOUSE";
                CvGroup[CvGroup["MAPSET_TYPE"] = 14] = "MAPSET_TYPE";
                CvGroup[CvGroup["MARKER_PROP"] = 15] = "MARKER_PROP";
                CvGroup[CvGroup["MARKER_STRAND"] = 16] = "MARKER_STRAND";
                CvGroup[CvGroup["PLATFORM_TYPE"] = 17] = "PLATFORM_TYPE";
                CvGroup[CvGroup["STATUS"] = 18] = "STATUS";
            })(CvGroup || (CvGroup = {}));
            exports_1("CvGroup", CvGroup);
            ;
            exports_1("getCvGroupName", getCvGroupName = function (cvGroup) {
                var returnVal;
                if (cvGroup === CvGroup.ANALYSIS_TYPE) {
                    returnVal = "analysis_type";
                }
                else if (cvGroup === CvGroup.DATASET_TYPE) {
                    returnVal = "dataset_type";
                }
                else if (cvGroup === CvGroup.JOBSTATUS) {
                    returnVal = "job_status";
                }
                else {
                    returnVal = "UNKNOWN CV GROUP";
                }
                return returnVal;
            });
            // export class CvFilters {
            //
            //
            //     private static cvValues: Map<CvGroup,string> = null;
            //
            //     // These are the cvgroup values for the terms that are to be retrieved
            //     public static get(cvFilterType:CvGroup): string {
            //
            //         if ( this.cvValues === null ) {
            //             this.cvValues = new Map<CvGroup,string>();
            //             this.cvValues[CvGroup.ANALYSIS_TYPE] = "analysis_type";
            //             this.cvValues[CvGroup.DATASET_TYPE] = "dataset_type";
            //             this.cvValues[CvGroup.JOB_STATUS] = "job_status";
            //         }
            //
            //         return this.cvValues[cvFilterType];
            //     }
            //
            // }
        }
    };
});
//# sourceMappingURL=cv-group.js.map