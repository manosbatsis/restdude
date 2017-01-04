# RestDude

Full stack, high level framework for horizontal, model-driven application hackers.

## Example Model

```java
@Entity
@Table(name = "host")
@ModelResource(path = "hosts", controllerSuperClass = AbstractModelController.class, apiName = "Hosts", apiDescription = "Operations about hosts")
public class Host extends AbstractSystemUuidPersistable {

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @NotNull
    @Column(name = "description", length = 500, nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "country_id", referencedColumnName = "id")
    private Country country;
    
    // other properties, getters/setters etc.
}
```
## Generated Services

Some of the resulting services:

HTTP Methjod | Path   | Description
------------ | ------ | -------------------
GET  | /api/rest/hosts/{id} | Fetch the host matching the id
GET  | /api/rest/hosts?country.code=GR&name=%25startsWith | Search based on model properties (paged)
POST | /api/rest/hosts | Create a new host
PUT  | /api/rest/hosts/{id} | Update the host matching the id
PATCH  | /api/rest/hosts/{id} | Partially update the host matching the id
DELETE | /api/rest/hosts/{id} | Delete the host matching the id
GET    | /api/rest/hosts/jsonschema | Get the JSONSchema for hosts

## Generated UI

Example screens for managing hosts: TODO

## Reference

### Model Driven Services

#### Entity Models

#### Graph Models

#### Validation

#### Authorization

#### Custom Components

##### Repositories

##### Sercvices

##### Controllers

##### Binding

### Other Services

#### Authentication

#### Social Networks Integration

#### Auditing

#### Indexing

#### Websockets API






