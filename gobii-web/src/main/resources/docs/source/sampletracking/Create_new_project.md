
Creates a new project.

**Request Body**

Any fields other than the below fields will be ignored by the system.

Field | Type | Required/Optioanl
------|------|------------
piContactId | Integer | **Required** 
projectName | String | **Required** 
projectDescription | String | *Optional* 
properties | Object | <ul><li>data\_sampled: *Optioanl*</li><li>division: *Optional*</li><li>genotyping\_purpose: *Optional*</li><li>study\_name: *Optional*</li></ul>

**Request Body Example**

```
    {
        "piContactId": 34,
        "projectName": "foo_proj_01",
        "projectDescription": "foo 01 project",
        "properties": {
            "division": "bar_division",
            "study_name": "bar_study_name",
            "date_sampled": "06/01/2018",
            "genotyping_purpose": "bar_genotyp_purp"
        }
    }
```

**Python Example**

```
   
  import requests
  
  import json
  
  # URL for Sample Upload
  url = "http://cbsugobiixvm11.biohpc.cornell.edu:8081/gobii-dev/sample-tracking/v1/projects"
  
  # Project to be created
  data = {
                 "piContactId": 34,
                 "projectName": "foo_proj_01",
                 "projectDescription": "foo 01 project",
                 "properties": {
                     "division": "bar_division",
                     "study_name": "bar_study_name",
                     "date_sampled": "06/01/2018",
                     "genotyping_purpose": "bar_genotyp_purp"
               }
          }

  # Successful creation will have http status code 201
  response = requests.post(url, json=data, headers= {"X-Auth-Token" : "123apiKey!"})   
   
```

**Successful Operation** will return created [Project Resource](#projectresourceexample) with HTTP Status code 201


