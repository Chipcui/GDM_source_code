
Creates a new samples in the system.

The Request content type is *multipart/form-data* with two feilds, 

* sampleMetaData: **Required** field with following json fields. Any fields other than below fields will be ignored by the system.

Field | Type | Description
------|------|------------
projectId | Integer | **Required**
map | Object | *Optional* Object mapping File header to the API Fields. Nested fields can be concatenated with '.' . For example, germplasm resource field germplasmName can be mapped with key, "germplasm.germplasmName". Also, properties resource field ref_sample can be mapped with key, "properties.ref\_sample". If map is not given, the headers will assumed as defined by the samples API. First row of the file will be assumed as header.

* sampleFile: **Required** Sample file. Content Type: text/plain. File should be tab(\\t) delimited.

**Python Example**

```

  import requests
  
  import json
  
  # URL for Sample Upload
  url = "http://cbsugobiixvm11.biohpc.cornell.edu:8081/gobii-dev/sample-tracking/v1/samples/upload"
  
  # Sample Meta Data added in data field with sampleMetaData as key
  sampleMetaData = {"projectId" : 7, "map" :  {}}
  
  data = {"sampleMetaData" : json.dumps(sampleMetaData)}
  
  #Sample File is added in files fields with sampleFile as key
  files = { "sampleFile" : open("/home/user/sample.txt", 'rb')}
  
  response = requests.post(url, data=data, files=files, headers= {"X-Auth-Token" : "123apitoken!"})


```

**Form Data Example**

```

POST /gobii-dev/sample-tracking/v1/samples/upload HTTP/1.1 
Host: http://cbsugobiixvm11.biohpc.cornell.edu:8081
Content-Type: multipart/form-data;boundary="boundary" 

--boundary 
Content-Disposition: form-data; name="sampleMetaData"
Content-Type: application/json

        {
            "projectId": 4,
            "map": {
                "sampleUuid": "sampleUUID",
                "germplasm.germplasmName": "germplasmName",
                "germplasm.properties.pedigree": "germplasmPedigree"
                ....
            }
        }
 
--boundary 
Content-Disposition: form-data; Content-Type: text/plain; name="sampleFile"; filename="foo.txt" 

sampleUuid\tgermplasmName\tgermplasmPedigree.....\n
djsakndkjasdnk\tGermplasm1\tgermplasmPedigreeFoo....\n
....

--boundary-


```

**Successful Operation** will return a json response with HTTP Status code 200. The json response will have jobId. The status of the Job can obtained by GET "/jobs/jobId". Below is the example response,

```
    
    {
       "jobId" : "dd568nfdjskn4_7",
       "destinationType" : "samples",
       "projectId" : 7
    }

```


