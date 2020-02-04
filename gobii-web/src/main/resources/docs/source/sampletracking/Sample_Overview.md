
Samples are the unit of material to be genotyped by a vendor. Samples have germplasm metadata (higher level information on the germplasm such as accession name or pedigree), and sample metadata (plate or sample grouping information)

<a name="sampleresource">**Sample Resource**</a>
Field | Type | Description
------|------|------------
sampleId | Integer | Sample Id in the GDM system
sampleUuid | String | Sample UUID for the sample
sampleName | String | The name of the sample that gets sent to the lab for processing. We assume that the sample is in a plate or in tubes Note: A unique sample in the GOBII system is defined by the unique combination of the project_name, dnasample_name, and dnasample_number so the dnasample_name does not need to be unique within a project or across projects.
sampleNum | String | Numerical order of the sample within a project. For example, 1-96 for a 96 well plate. Each sample needs to have a unique number within a project, unless the sample names are each unique within the project. Even in this case, it is always good practice to assign consecutive sample numbers to the samples in a project for ease of post-processing sorting.
projectId | Integer | Project Id to which sample belongs
germplasm | Object | Germplasm to which sample belongs to. [Germplasm Resouurce](#germplasmresource)
plateName | String | The plate name that the sample is in. This can be a number (1,2,3,4, etc.), or a name given by the lab.
wellCol | String | The plate column coordinates for the sample. For example, 1-12 for a 96 well plate.
wellRow | String | The plate row coordinates for the sample. For example, A-H for a 96 well plate.
properties | Object | An object with System defined properties and User defined properties. The System defined properties are non editable by the user. User can define their own properties using /samples/properties endpoint. (Endpoint description is not added yet in this document. It will be added later.) Below are the system defined properties, <ul><li>ref_sample: A standard "Reference" sample aginst which all other germplasms are compared. This is also called a "gold standard".</li><li>sample_group: The grouping of germplasm that has the utility to the breeder for analysis purposes. For example, a population and its parents that need to be grouped for data analysis.</li><li>sample_group_cycle: The cycle of germplasm grouping. For example, different generations of a population and its parents that allow for further grouping withing the germplasm group.</li><li>sample_parent_prop: This can be used to describe the type of parent, for example, female/male or DP/RP (donor/recurrent parent)</li><li>sample_type: The type of tissue sampled. For example, leaf, seed, bulk seed, or bulk plant</li><li>trial\_name: The trial name for the field experiment that the sample is coming from, or fieldbook.</li></ul>
createdBy | Integer | Contact Id of the user who created the sample.
createdDate | String | Created date string in UTC
modifiedBy | Integer | Contact Id of the user who modified the sample.
modifiedData | Integer | Modified date string in UTC

<a name="germplasmresource">**Germplasm Resource**</a>
Field | Type | Description
------|------|------------
germplasmId | Integer | Id of the germplasm in GDM system.
germplasmName | String | The name most commonly used, or the default name. The germplasm table has a one to many relationship with the dnasample table. For example, many dnasamples can be associated with a single germplasm name.
externalCode | String | The code used to identify the observational unit in adjacent breeding management or sample tracking database systems. For example, this could be a plot or plant ID. A germplasm name can have several external codes.
speciesName | String Enum | Value should be one of the CV terms for species name in GDM. Could be obtained by GET "/germplasm/cvterms/speciesnames"
typeName | String Enum | The type or generation of germplasm. Value should be one of the CV term for type name. Could be obtained by GET "/germplasm/cvterms/typename"
properties | Object | An object with System defined properties and User defined properties. The System defined properties are non editable by the user. User can define their own properties using /samples/properties endpoint. (Endpoint description is not added yet in this document. It will be added later.) Below are the system defined properties, <ul><li>germplasm_heterotic_group: The germplasm groups within species. For example, NSS, SSS, A, or B for maize.</li><li>germplasm_id: A higher level of ID. For example, MGID.</li><li>germplasm_subsp: Sub species grouping of germplasm. This could be different for each crop, but, for example, would be dent, flint, sweet, or pop for maize, indica, and japonica for rice, breed wheat, durum wheat for wheat etc.</li><li>par1: The germplasm name for parent 1 of the germplasm (in a biparental cross this would be the female).</li><li>par2: The germplasm name for parent 2 of the germplasm (in a biparental cross this would be the male).</li><li>par3: The germplasm name for parent 3 of the germplasm.</li><li>par4: The germplasm name for parent 4 of the germplasm.</li><li>pedigree: The pedigree for the germplasm name.</li><li>seed_source_id: Seed source ID for the germplasm.</li></ul>

<a name="sampleresourceexample">**Resource Example**</a>

```
    {
        "sampleId": 7,
        "sampleUuid": "fjksdbfbvhjbfhjhjfhj",
        "sampleName": "foo",
        "sampleNum": "23",
        "projectId": 5,
        "germplasm": {
            "germplasmId": 7,
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
            },
            "createdBy": 2,
            "createdDate": "2019-07-25T04:00:00",
            "modifiedBy": 3,
            "modifiedData": "2019-07-29T04:00:00"
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
        "createdBy": 2,
        "createdDate": "2019-07-25T04:00:00",
        "modifiedBy": 3,
        "modifiedData": "2019-07-29T04:00:00"
    }
```


