
Creates a new experiment in the system.

**Request Body**

The Request content type is *multipart/form-data* with two feilds, 

* experimentMetaData: **Required** field with following json fields. Any fields other than below fields will be ignored by the system.

Field | Type | Description
------|------|------------
name | String | **Required** 
projectId | Integer | **Required**
vendorProtocolId | Integer | *Optional*
manifestId | Integer | *Optional*

* dataFile: *Optional* assciated data file to be uploaded

**Form Data Example**

```

POST /gobii-dev/sample-tracking/v1/experiments HTTP/1.1 
Host: http://cbsugobiixvm11.biohpc.cornell.edu:8081
Content-Type: multipart/form-data;boundary="boundary" 

--boundary 
Content-Disposition: form-data; name="experimentMetaData"
Content-Type: application/json

        {
            "name": "foo",
            "code": "bar code",
            "projectId": 7,
            "vendorProtocolId": 4,
            "manifestId": 8
        }
 
--boundary 
Content-Disposition: form-data; name="dataFile"; filename="foo.txt" 

... contents of foo.txt ...

--boundary-


```

**Successful Operation** will return created [Experiment Resource](#experimentresourceexample) with HTTP Status code 201


