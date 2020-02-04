
An experiment defines the protocol and vendor used to generate the genotyping data. More than one experiment can belong to a project.

**Resource Desciption**

Field | Type | Description
------|------|------------
experimentId | Integer | Id of the experiment
name | String | Name of the experiment
projectId | Integer | Project Id unde which experiment is created.
vendorProtocolId | Integer | Id of the vendor protocol in GDM. The combination of the vendor and protocol used to generate the genotyping data.
manifestId | Integer | A standard group of markers run as a unit. For example, an Illumina chip. New manifest can be added using /manifest endpoint
dataFileUrl | String | Url to download dataFiles associated with the experiment.
status | String | Denotes current status of the project. Will be one of the three values <ul><li>new</li><li>modified</li><li>deleted</li></ul>
code | String | GDM generated code for experiment.
createdBy | Integer | Contact Id of the user who created the experiment.
createdDate | String | Created date string in UTC
modifiedBy | Integer | Contact Id of the user who modified the experiment.
modifiedData | Integer | Modified date string in UTC

<a name="experimentresourceexample">**Resource Example**</a>

```
        {
            "createdBy": 1,
            "createdDate": "2019-07-26T04:00:00",
            "modifiedBy": 1,
            "modifiedData" : ""2019-07-29T04:00:00""
            "name": "foo",
            "code": "bar code",
            "projectId": 7,
            "vendorProtocolId": 4,
            "status": "modified",
            "experimentId": 8,
            "manifestId": 7,
            "dataFileUrl": "/experiment/8/foo.zip"
        }
```



