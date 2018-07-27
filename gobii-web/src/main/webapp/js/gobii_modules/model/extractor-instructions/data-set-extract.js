System.register([], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var GobiiDataSetExtract;
    return {
        setters: [],
        execute: function () {
            GobiiDataSetExtract = (function () {
                function GobiiDataSetExtract(gobiiFileType, accolate, extractDestinationDirectory, gobiiExtractFilterType, markerList, sampleList, markerListFileName, sampleListFileName, gobiiSampleListType, gobiiDatasetType, platforms, principleInvestigator, project, dataSet, markerGroups, vertices, gqlMarkerResultFileName, gqlSampleResultFileName) {
                    this.gobiiFileType = gobiiFileType;
                    this.accolate = accolate;
                    this.extractDestinationDirectory = extractDestinationDirectory;
                    this.gobiiExtractFilterType = gobiiExtractFilterType;
                    this.markerList = markerList;
                    this.sampleList = sampleList;
                    this.markerListFileName = markerListFileName;
                    this.sampleListFileName = sampleListFileName;
                    this.gobiiSampleListType = gobiiSampleListType;
                    this.gobiiDatasetType = gobiiDatasetType;
                    this.platforms = platforms;
                    this.principleInvestigator = principleInvestigator;
                    this.project = project;
                    this.dataSet = dataSet;
                    this.markerGroups = markerGroups;
                    this.vertices = vertices;
                    this.gqlMarkerResultFileName = gqlMarkerResultFileName;
                    this.gqlSampleResultFileName = gqlSampleResultFileName;
                } // ctor
                GobiiDataSetExtract.prototype.getgobiiFileType = function () {
                    return this.gobiiFileType;
                };
                GobiiDataSetExtract.prototype.setgobiiFileType = function (value) {
                    this.gobiiFileType = value;
                };
                GobiiDataSetExtract.prototype.getaccolate = function () {
                    return this.accolate;
                };
                GobiiDataSetExtract.prototype.setaccolate = function (value) {
                    this.accolate = value;
                };
                GobiiDataSetExtract.prototype.getextractDestinationDirectory = function () {
                    return this.extractDestinationDirectory;
                };
                GobiiDataSetExtract.prototype.setextractDestinationDirectory = function (value) {
                    this.extractDestinationDirectory = value;
                };
                GobiiDataSetExtract.prototype.getgobiiExtractFilterType = function () {
                    return this.gobiiExtractFilterType;
                };
                GobiiDataSetExtract.prototype.setgobiiExtractFilterType = function (value) {
                    this.gobiiExtractFilterType = value;
                };
                GobiiDataSetExtract.prototype.getmarkerList = function () {
                    return this.markerList;
                };
                GobiiDataSetExtract.prototype.setmarkerList = function (value) {
                    this.markerList = value;
                };
                GobiiDataSetExtract.prototype.getsampleList = function () {
                    return this.sampleList;
                };
                GobiiDataSetExtract.prototype.setsampleList = function (value) {
                    this.sampleList = value;
                };
                GobiiDataSetExtract.prototype.getlistFileName = function () {
                    return this.markerListFileName;
                };
                GobiiDataSetExtract.prototype.setlistFileName = function (value) {
                    this.markerListFileName = value;
                };
                GobiiDataSetExtract.prototype.getgobiiSampleListType = function () {
                    return this.gobiiSampleListType;
                };
                GobiiDataSetExtract.prototype.setgobiiSampleListType = function (value) {
                    this.gobiiSampleListType = value;
                };
                GobiiDataSetExtract.prototype.getgobiiDatasetType = function () {
                    return this.gobiiDatasetType;
                };
                GobiiDataSetExtract.prototype.setgobiiDatasetType = function (value) {
                    this.gobiiDatasetType = value;
                };
                GobiiDataSetExtract.prototype.getplatforms = function () {
                    return this.platforms;
                };
                GobiiDataSetExtract.prototype.setplatforms = function (value) {
                    this.platforms = value;
                };
                GobiiDataSetExtract.prototype.getJson = function () {
                    var returnVal = {};
                    returnVal.gobiiFileType = this.gobiiFileType;
                    returnVal.accolate = this.accolate;
                    returnVal.extractDestinationDirectory = this.extractDestinationDirectory;
                    returnVal.gobiiExtractFilterType = this.gobiiExtractFilterType;
                    returnVal.markerList = this.markerList;
                    returnVal.sampleList = this.sampleList;
                    returnVal.markerListFileName = this.markerListFileName;
                    returnVal.sampleListFileName = this.sampleListFileName;
                    returnVal.gobiiSampleListType = this.gobiiSampleListType;
                    returnVal.gobiiDatasetType = this.gobiiDatasetType;
                    returnVal.platforms = this.platforms;
                    returnVal.principleInvestigator = this.principleInvestigator;
                    returnVal.project = this.project;
                    returnVal.dataSet = this.dataSet;
                    returnVal.markerGroups = this.markerGroups;
                    returnVal.vertices = this.vertices;
                    returnVal.gqlMarkerResultFileName = this.gqlMarkerResultFileName;
                    returnVal.gqlSampleResultFileName = this.gqlSampleResultFileName;
                    return returnVal;
                };
                GobiiDataSetExtract.fromJson = function (json) {
                    var returnVal = new GobiiDataSetExtract(json.gobiiFileType, json.accolate, json.extractDestinationDirectory, json.gobiiExtractFilterType, json.markerList, json.sampleList, json.markerListFileName, json.sampleListFileName, json.gobiiSampleListType, json.gobiiDatasetType, json.platforms, json.principleInvestigator, json.project, json.dataSet, json.markerGroups, json.vertices, json.gqlMarkerResultFileName, json.gqlSampleResultFileName);
                    return returnVal;
                };
                return GobiiDataSetExtract;
            }());
            exports_1("GobiiDataSetExtract", GobiiDataSetExtract);
        }
    };
});
//# sourceMappingURL=data-set-extract.js.map