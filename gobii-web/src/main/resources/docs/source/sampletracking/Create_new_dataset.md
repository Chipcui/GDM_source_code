
Creates a new dataset in the system.

Content Type: application/json

**Request Body**

Any fields other than below fields will be ignored

Field | Type | Required/Optional
------|------|------------
experimentId | String | **Required**
datasetName | String | **Required**
datasetType | String | **Required**
callingAnalysisId | String | **Required**
analysisIds | Array String | *Optional*

**Request Body Example**

```

            {
              "datasetName": "foo",
              "datatypeName": "foo type",
              "experimentId": 1,
              "callingAnalysisId": "4",
              "analysisIds": [
                4
              ]
            }


```

**Successful Operation** will return a [Dataset Resource](#datasetresourceexample) with HTTP Status code 201

