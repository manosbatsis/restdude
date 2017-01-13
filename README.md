# Restdude

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

HTTP Method | Path   | Description
------------ | ------ | -------------------
GET  | /api/rest/hosts/{id} | Fetch the host matching the id
GET  | /api/rest/hosts?country.code=GR&name=%25startsWith | Search based on model properties (paged)
POST | /api/rest/hosts | Create a new host
PUT  | /api/rest/hosts/{id} | Update the host matching the id
PATCH  | /api/rest/hosts/{id} | Partially update the host matching the id
DELETE | /api/rest/hosts/{id} | Delete the host matching the id
GET    | /api/rest/hosts/jsonschema | Get the JSONSchema for hosts


## Behind the Schenes

Contrary to other frameworks like spring-data-rest and jhipster, restdude generates controller, service and repository classes during application startup to provide an extensible 3-tier architecture. You can replace/extend those implicit componenets with your own at any time to provide custom or otherwise additional functionality. 

## Technology Stack

# Restdude

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

HTTP Method | Path   | Description
------------ | ------ | -------------------
GET  | /api/rest/hosts/{id} | Fetch the host matching the id
GET  | /api/rest/hosts?country.code=GR&name=%25startsWith | Search based on model properties (paged)
POST | /api/rest/hosts | Create a new host
PUT  | /api/rest/hosts/{id} | Update the host matching the id
PATCH  | /api/rest/hosts/{id} | Partially update the host matching the id
DELETE | /api/rest/hosts/{id} | Delete the host matching the id
GET    | /api/rest/hosts/jsonschema | Get the JSONSchema for hosts


## Behind the Schenes

Contrary to other frameworks like spring-data-rest and jhipster, restdude generates controller, service and repository classes during application startup to provide an extensible 3-tier architecture. You can replace/extend those implicit componenets with your own at any time to provide custom or otherwise additional functionality. 

## Technology Stack

- Choose between Spring MVC or Spring Boot
- Annotate your models to configure API documentation, authorization, validation and auditing
- Sleep better with built-in <a href="https://github.com/manosbatsis/restdude/tree/master/restdude-error">error-management</a> and reporting
- Customize and integrate security according to your needs with Spring Security
- Use websockets via Spring Websockets/STOMP
- Persist to relational databases with Spring Data JPA
- Use the right NoSQL datastore for your project-specific needs, including ElasticSearch, Solr, MongoDB, Redis, Cassandra, Counchbase, Neo4J, Hazelcast and more
- Automate basic or fine grained auditing using <a href="http://docs.spring.io/spring-data/data-jpa/docs/current/reference/html/auditing.html">Envers</a> and <a href="http://javers.org/">Javers</a>
- Publish REST API documentation effortlesly thanks to <a href="http://springfox.github.io/springfox">Springfox</a> and <a href="http://swagger.io/swagger-ui">Swagger UI</a>
- Optional web UI and admin panel provided out-of the-box based on bootstrap 4, backbone.marionette, core-UI, requirejs and more.

## Documentation

See https://manosbatsis.github.io/restdude/

## Documentation

See https://manosbatsis.github.io/restdude/
