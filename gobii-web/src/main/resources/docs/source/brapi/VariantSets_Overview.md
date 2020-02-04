
VariantSet corresponds to Dataset in GDM system.
A dataset belongs to an experiment (study in BrAPI) and describes the suite of analyses applied to the vendor-delivered data to generate the dataset, as well as the dataset type.

<a name="variantsetresource">**Resource Description**</a>

Field | Type | Description
------|------|------------
variantSetDbId | Integer | Id of the VariantSet. Corresponds to dataset Id.
studyDbId | Integer | Study under which the VariantSet is created. Corresponds to the experiment Id.
variantSetName | String | The name of the dataset. VariantSet name is unique under a study.
analyses | Array | List of Analysis Resource. Lists all the analyses associated with the dataset.
availableFormats | Array | List of Formats in which the data can be extracted.
referenceSetDbId | String | The ID of the reference set that describes the sequences used by the variants in this set.
variantCount | Integer | Number of Variants(Markers or SNPs) in corresponding VariantSet
callSetCount | Integer | Number of CallSets (Dnaruns) in a corresponding VariantSet
createdBy | Integer | Contact Id of the user who created the VariantSet.
created | String | Created date string in UTC
modifiedBy | Integer | Contact Id of the user who modified the VariantSet.
modified | Integer | Modified date string in UTC

<a name="variantsetresourceexample">**Resource Example**</a>

```

      {
        "additionalInfo": {},
        "analyses": [
          {
            "analysisDbId": "6191a6bd",
            "analysisName": "Standard QC",
            "created": "2018-01-01T14:47:23-0600",
            "description": "This is a formal description of a QC methodology. Blah blah blah ...",
            "software": [
              "https://github.com/genotyping/QC"
            ],
            "type": "QC",
            "updated": "2018-01-01T14:47:23-0600"
          }
        ],
        "availableFormats": [
          {
            "dataFormat": "VCF",
            "fileFormat": "application/excel",
            "fileURL": "https://brapi.org/example/VCF_1.xlsx"
          },
          {
            "dataFormat": "VCF",
            "fileFormat": "text/csv",
            "fileURL": "https://brapi.org/example/VCF_2.csv"
          }
        ],
        "callSetCount": 341,
        "referenceSetDbId": "57eae639",
        "studyDbId": "2fc3b034",
        "variantCount": 250,
        "variantSetDbId": "87a6ac1e",
        "variantSetName": "Maize QC DataSet 002334"
      }

```


