
Creates new samples for a given project Id in the system.

<h4>Request Body</h4>

Any Fields Other than below described fields will be ignored,

Field | Type | Description
------|------|------------
projectId | Integer | **Required** Id of the project under which the samples need to be added.
samples | Array | **Required** List of *Sample Resources*. [Sample Resource Fields](#samplerequestfields) are below,

<a name="samplerequestfields">**Sample Fields**</a>
Field | Type | Required/Optional
------|------|------------
sampleUuid | String | **Required**
sampleName | String | **Required**
sampleNum | String | **Required**
germplasm | Object | **Required** [Germplasm Resouurce Fields](#germplasmrequestfields) are in below table,
plateName | String | *Optional*
wellCol | String | *Optional*
wellRow | String | *Optional*
properties | Object | <ul><li>ref_sample: *Optional*</li><li>sample_group: *Optional*</li><li>sample_group_cycle: *Optional*</li><li>sample_parent_prop: *Optional*</li><li>sample_type: *Optional*</li><li>trial\_name: *Optional*</li></ul>

<a name="germplasmrequestfields">**Germplasm Fields**</a>
Field | Type | Required/Optional
------|------|------------
germplasmName | String | **Required**
externalCode | String | **Required**
speciesName | String Enum | *Optional*
typeName | String Enum | *Optional*
properties | Object | <ul><li>germplasm_heterotic_group: *Optional*</li><li>germplasm_id: *Optional*</li><li>germplasm_subsp: *Optional*</li><li>par1: *Optional*</li><li>par2: *Optional*</li><li>par3: *Optional*</li><li>par4: *Optional*</li><li>pedigree: *Optional*</li><li>seed_source_id: *Optional*</li></ul>

**Request Body Example**

```
    {
        "projectId": 4,
        "samples": [
           {
               "sampleUuid": "fjksdbfbvhjbfhjhjfhj",
               "sampleName": "foo",
               "sampleNum": "23",
               "germplasm": {
                  "germplasmName": "bar",
                  "externalCode": "bar external",
                  "speciesName": "Tripsacum intermedium",
                  "typeName": "f1_hybrid",
                  "properties": {
                      "germplasm_heterotic_group": "bar NSS",
                      "germplasm_id": "bar germplasm id",
                      "germplasm_subsp": "bar germplasm",
                      "par1": "bar par 1",
                      "par2": "bar par 2",
                      "par3": "bar par 3",
                      "par4": "bar par 4",
                      "pedigree": "bar pedigree",
                      "seed_source_id": "bar see"
                  }
               },
               "plateName": "foo plate",
               "wellCol": "1",
               "wellRow": "2",
               "properties": {
                   "ref_sample": "foo ref sample",
                   "sample_group_cycle": "foo sample group",
                   "sample_parent_prop": "foo sample parent",
                   "sample_type": "foo sample type"
                   "trial_name": "foo trial"
               }
           },
           {
               "sampleUuid": "ekjdvhjfdhjkfjhfad",
               "sampleName": "foofoo",
               "sampleNum": "24",
               "germplasm": {
                  "germplasmName": "bar",
                  "externalCode": "bar external",
                  "speciesName": "Tripsacum intermedium",
                  "typeName": "f1_hybrid",
                  "properties": {
                      "germplasm_heterotic_group": "bar NSS",
                      "germplasm_id": "bar germplasm id",
                      "germplasm_subsp": "bar germplasm",
                      "par1": "bar par 1",
                      "par2": "bar par 2",
                      "par3": "bar par 3",
                      "par4": "bar par 4",
                      "pedigree": "bar pedigree",
                      "seed_source_id": "bar see"
                  }
               },
               "plateName": "foo plate",
               "wellCol": "1",
               "wellRow": "3",
               "properties": {
                   "ref_sample": "foo ref sample",
                   "sample_group_cycle": "foo sample group",
                   "sample_parent_prop": "foo sample parent",
                   "sample_type": "foo sample type"
                   "trial_name": "foo trial"
               }
           },
        ]
    }
```

**Successful Operation** will return a json response with HTTP Status code 200. The json response will have jobId. The status of the Job can obtained by GET "/jobs/jobId". Below is the example response,

```
    
    {
       "jobId" : "dd568nfdjskn94_7",
       "destinationType" : "samples",
       "projectId" : 4
    }

```


