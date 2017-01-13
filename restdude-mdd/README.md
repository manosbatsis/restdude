## REST API Basics

The sections bellow describe the REST API typically provided by restdude controllers.

### HTTP Methods and URLs

Restdude provides a stateless and, with a few exceptions, completely regular on how they map SCRUD to HTTP verbs, i.e. methods. For example, consider the endpoint for a Book domain entity:


Method     | URL   | Description
------------ | ------ | -------------------
GET  | /api/rest/books/{id} | Fetch the book matching the {id}
GET  | /api/rest/books?foo=bar&foo.subFoo=baz | Search based on any book attributes
POST  | /api/rest/books | Create a new book
PUT   | /api/rest/books/{id} | Update the book matching the id
DELETE | /api/rest/books/{id} | Delete the book matching the id

### Method Overrides

Note that most HTTP servers do not support/allow PUT and DELETE out of the box. Restdude allows clients to emulate those methods by using POST in combination with the "X-HTTP-Method-Override HTTP header, for example:

```
    POST /api/rest/foo/bar
    X-HTTP-Method-Override: PUT
```

### Paged Results

Criteria-based search results support all API model attributes by default and return paged results. The response JSON for such results has the following form:

```javascript
    {
      // the results array
      "content": [
        {
          // 1st item
        },
        //...
        {
          // nth item
        },
      ],
      "first": true, // first page?
      "last": true, // last page?
      "number": 0, // page number
      "numberOfElements": 0, // in current page
      "size": 0, // page size
      "sort": {}, // sort
      "totalElements": 0, // total results
      "totalPages": 0 // total pages
    }
```

#### Page Parameters


Name     | Default   | Description
------------ | ------ | -------------------
page         | 0      | The page number
size         | 10     | The page size
properties   | id     | The sort properties
direction    | ASC    | The sort direction (ASC/DESC)
