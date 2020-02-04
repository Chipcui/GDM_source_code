
A project consists of a group of samples that are, or will be, genotyped. A project belongs to a Principal Investigator (PI), also called a PI contact.

**Resource Description**

Below table describes the Project Resource with their paramters name, type and
description.

Field | Type | Description
------|------|------------
projectId | Integer | Id of the Project in GDM.
piContactId | Integer | Id of PI (Principal investigator) of the project in GDM system. The PI contact can be searched using /contacts API.
projectName | String | Name of the project. The project name must be unique.
projectDescription | String | Field to describe the project.
properties | Object | An object with System defined properties and User defined properties. The System defined properties are non editable by the user. User can define their own properties using /projects/properties endpoint. (Endpoint description is not added yet in this document. It will be added later.) Below are the system defined properties, <ul><li>data\_sampled: The date or approximate date the tissue was sampled. Note, this is not a standard date field. It is important that the curators for the crop community define how dates are entered for this field.</li><li>division: The department or group where the project was initiated.</li><li>genotyping\_purpose: The purpose of the project. For example, MABC or MAIC. It is important that the crop community define standards for entering data into this field.</li><li>study\_name: The larger study name that the project is associated with.</li></ul>
projectStatus | String | Denotes current status of the project. Will be one of the three values <ul><li>new</li><li>modified</li><li>deleted</li></ul>
projectCode | String | GDM generated code for project.
createdBy | Integer | Contact Id of the user who created the project.
createdDate | String | Created date string in UTC
modifiedBy | Integer | Contact Id of the user who modified the project.
modifiedData | Integer | Modified date string in UTC

<a name="projectresourceexample">**Resource Example**</a>

```
    {
        "createdBy": 1,
        "createdDate": "2019-07-25T04:00:00",
        "modifiedBy": 1,
        "projectId": 1,
        "piContactId": 34,
        "projectName": "foo_proj_01",
        "projectStatus": "new",
        "projectCode": "foo_proj_01",
        "projectDescription": "foo 01 project",
        "properties": {
            "division": "bar_division",
            "study_name": "bar_study_name",
            "date_sampled": "06/01/2018",
            "genotyping_purpose": "bar_genotyp_purp"
        }
    }
```


