
Upload data to the dataset.

The Request content type is *multipart/form-data* with two feilds, 

* datasetMetaData: **Required** field with following json fields. Any fields other than below fields will be ignored by the system.

Field | Type | Description
------|------|------------
datasetId | Integer | **Required**
vendorFileUrls | Object | *Optional* if the data is uploaded as file. No data file and no vendor file urls will result in operation failure. The Vendorfile urls format and key values should be as per definitions in vendor protocol for this particular experiment. 

* genotypeFile: *Optional* if vendor file urls are provided. No data file and no vendor file urls will result in operation failure. If more than one file are there, they should be zipped and uploaded as one file object.

**Form Data Example**

```

POST /gobii-dev/sample-tracking/v1/dataset/{datasetId}/data HTTP/1.1 
Host: http://cbsugobiixvm11.biohpc.cornell.edu:8081
Content-Type: multipart/form-data;boundary="boundary" 

--boundary 
Content-Disposition: form-data; name="datasetMetaData"
Content-Type: application/json

        {
            "datasetId": 4,
            "vendorFileUrls": {
                "genotypeFile": "https://www.examplevendor.org/files/genotype.csv",
                "markerMap": "https://www.examplevendor.org/files/markermap.txt",
                ...
            }
        }
 
--boundary 
Content-Disposition: form-data; name="genotypeFile"; Content-Type: application/zip; filename="genotypes.data.zip" 

**..contents of zip file..\*\*
....

--boundary


```

**Successful Operation** will return a json response with HTTP Status code 200. The json response will have jobId. The status of the Job can obtained by GET "/jobs/jobId". Below is the example response,

```
    
    {
       "jobId" : "dd568nfdjskn4_7",
       "destinationType" : "dataset",
       "datasetId" : 4
    }

```

