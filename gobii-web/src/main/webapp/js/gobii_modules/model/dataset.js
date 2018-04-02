System.register([], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var DataSet;
    return {
        setters: [],
        execute: function () {
            DataSet = (function () {
                function DataSet(id, piContactId, projectId, experimentId, datasetName, callingAnalysisId, dataTable, dataFile, qualityTable, qualityFile, status, datatypeId, analysesIds, createdDate, jobStatusId, jobStatusName, jobTypeId, jobTypeName, jobSubmittedDate, experimentName, projectName, protocolId, protocolName, platformId, platformName, callingAnalysisName, piEmail, piFirstName, piLastName, datatypeName, totalSamples, totalMarkers) {
                    this.id = id;
                    this.piContactId = piContactId;
                    this.projectId = projectId;
                    this.experimentId = experimentId;
                    this.datasetName = datasetName;
                    this.callingAnalysisId = callingAnalysisId;
                    this.dataTable = dataTable;
                    this.dataFile = dataFile;
                    this.qualityTable = qualityTable;
                    this.qualityFile = qualityFile;
                    this.status = status;
                    this.datatypeId = datatypeId;
                    this.analysesIds = analysesIds;
                    this.createdDate = createdDate;
                    this.jobStatusId = jobStatusId;
                    this.jobStatusName = jobStatusName;
                    this.jobTypeId = jobTypeId;
                    this.jobTypeName = jobTypeName;
                    this.jobSubmittedDate = jobSubmittedDate;
                    this.experimentName = experimentName;
                    this.projectName = projectName;
                    this.protocolId = protocolId;
                    this.protocolName = protocolName;
                    this.platformId = platformId;
                    this.platformName = platformName;
                    this.callingAnalysisName = callingAnalysisName;
                    this.piEmail = piEmail;
                    this.piFirstName = piFirstName;
                    this.piLastName = piLastName;
                    this.datatypeName = datatypeName;
                    this.totalSamples = totalSamples;
                    this.totalMarkers = totalMarkers;
                }
                return DataSet;
            }());
            exports_1("DataSet", DataSet);
        }
    };
});
//# sourceMappingURL=dataset.js.map