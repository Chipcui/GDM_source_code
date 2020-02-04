
Gets the list of BrAPI endpoints in the GDM system.

Successful operation will return below response with HTTP Status Code 200 Ok.

**Response Body Example**

```

{
    "metadata": {
        "pagination": {
            "totalCount": 0,
            "pageSize": 0,
            "totalPages": 0,
            "currentPage": 0
        },
        "status": [],
        "datafiles": []
    },
    "result": {
        "data": [
            {
                "call": "serverinfo",
                "methods": [
                    "GET"
                ],
                "datatypes": [
                    "JSON"
                ]
            },
            {
                "call": "maps",
                "methods": [
                    "GET"
                ],
                "datatypes": [
                    "JSON"
                ]
            },
            {
                "call": "studies-search",
                "methods": [
                    "POST"
                ],
                "datatypes": [
                    "JSON"
                ]
            },
            {
                "call": "allelematrices",
                "methods": [
                    "GET"
                ],
                "datatypes": [
                    "JSON"
                ]
            },
            {
                "call": "allelematrix-search",
                "methods": [
                    "GET",
                    "POST"
                ],
                "datatypes": [
                    "FLAPJACK"
                ]
            }
        ]
    }
}
        
```






