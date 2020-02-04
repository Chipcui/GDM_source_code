
List of all Experiments.

**Request Parameters**

Field | Type | Description
------|------|------------
pageToken | String | Page Token to fetch a page. nextPageToken form previous page's meta data should be used. If pageNumber is specified pageToken will be ignored. pageToken can be used to sequentially get pages faster. When an invalid pageToken is given the page will start from beginning.
pageSize | String | Size of the page to be fetched. Default is 1000.
pageNumber | Integer | Page number to be fetched based on page size.

**Response Body**

Field | Type | Description
------|------|------------
metaData | Object | Brapi Metadata with nextPage token if user chose to use pageToken to access data sequentially faster.
result | Array | [List of [Experiment Resource](https://gdmsampletracking.docs.apiary.io/#reference/experiments)]


