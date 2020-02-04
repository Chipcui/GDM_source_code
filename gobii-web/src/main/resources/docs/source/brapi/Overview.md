GDM Brapi WebService

<a name="brapiresponsetemplate">**BrAPI Response Template**</a>

```

{
  "metadata" : {
    "pagination" : {},
    "status" : [ ],
    "datafiles" : [ ]
  },
  "result" : {
    "key0": "master",
    "key1": 20,
    "key2": [ "foo", "bar", "baz" ]
  }
}

```

<a name="brapilistresponsetemplate">**BrAPI List Response Template**</a>

```
{
  "metadata" : {
    "pagination" : {
      "totalCount" : 20,
      "pageSize" : 3,
      "totalPages" : 7,
      "currentPage" : 0
    },
    "status" : [ ],
    "datafiles" : [ ]
  },
  "result" : {
    "data" : [ 
      {
        "detailKey0" : "detail0",
        "detailKey1" : [ "foo", "bar" ]
      }, 
      {
        "detailKey0" : "detail1",
        "detailKey1" : [ "bar", "baz" ]
      }, 
      {
        "detailKey0" : "detail2",
        "detailKey1" : [ "baz", "foo" ]
      },
    ]
  }
}

```

