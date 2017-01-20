# Model Driven Development

Some modern frameworks use two or even a single tier to reduce the coding required for exposing models as RESTful services.
The limitation trade-offs however become increasingly apparent as the application grows. Business methods appear in controllers
and repositories exposed to the web tier offer no room for extensibility.

Restdude on the other hand favours a 3-tier architecture without all the unnecessary boilerplate code. Restdude will simply create and register m
the issing Repository, Service and Controller beans on application startup. Need to customize or extend a component beyond SCRUD? Just add your custom
implementation and restdude will figure it out and use that instead of creating it's own.

## Default Implementations

Consider the following model. The runtime class generation is triggered by annotating the class with `@ModelResource`:


```java
@Entity
@Table(name = "country")
@ModelResource(path = "countries", apiName = "Countries", apiDescription = "Operations about countries")
public class Country extends AbstractFormalRegion<Continent> {

    @Id
    @Column(unique = true, nullable = false, length = 2)
    private String id;

    @Column(name = "native_name", unique = true, nullable = true, length = 50)
    private String nativeName;
}
```

### Provided Controller

```java
@RestController
@Api(tags = "Countries", description = "Operations about countries")
@RequestMapping(value = "/api/rest/countries",
	produces = { "application/json", "application/xml" })
public class CountryController extends AbstractModelController<Country, String, CountryService> {

    // actual implementation is in superclass
}
```

### Provided Service Interface

```java
public interface CountryService extends ModelService<Country, String> {
    // just extends super
}
```

### Provided Service Implementation

```java
@Named("countryService")
public class CountryServiceImpl extends AbstractModelServiceImpl<Country, String, CountryRepository> implements CountryService {
    // just extends super
}
```

### Provided Repository

```java
public interface CountryRepository extends ModelRepository<Country, String> {
    // just extends super
}
```


## API Basics

The sections bellow describes the core REST API provided by the above components.

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
