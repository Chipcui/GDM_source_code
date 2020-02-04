
A dataset belongs to an experiment and describes the suite of analyss applied to the vendor-delivered data to generate the dataset, as well as the dataset type.

**Resource Description**

Field | Type | Description
------|------|------------
datasetId | Integer | Id of the dataset in GDM
experimentId | Integer | Experiment under which the dataset is created
datasetName | String | The name of the dataset. The dataset name must be unique within an experiment.
datasetType | String Enum | Describing the "type" of data within the dataset which refers to the text format of the genotypic data calls such as IUPAC or 2-letter nucleotide. It should be one of GDM dataset types. which can be obtained by GET "/dataset/datasetTypes"
callingAnalysisId | String | Id of the genotype calling analysis applied to the genotyping data to produce the dataset. If the vendor has not applied any specific calling analysis, then you can use a generic calling analysis eg "none". New analysis can be created using POST "/analysis". Existing analyisis can be obtained using GET "/analysis"
analysisIds | Array String | Id of the analysis that were applied to the data to generate the dataset for imputation or data cleaning steps. New analysis can be created using POST "/analysis". Old analysis can be created using GET "/analysis".
createdBy | Integer | Contact Id of the user who created the experiment.
createdDate | String | Created date string in UTC
modifiedBy | Integer | Contact Id of the user who modified the experiment.
modifiedData | Integer | Modified date string in UTC

<a name="datasetresourceexample">**Resource Example**</a>

```
            {
              "dataSetId": 1,
              "datasetName": "foo",
              "datatypeName": "foo type",
              "experimentId": 1,
              "callingAnalysisId": "4",
              "createdBy": 2,
              "createdDate": "2019-08-28T15:20:44",
              "modifiedBy": 3,
              "modifiedDate": "2019-08-28T15:20:44",
              "analysisIds": [
                4
              ]
            }
```


