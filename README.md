

# Restdude

[![Build Status](https://travis-ci.org/manosbatsis/restdude.svg?branch=master)](https://travis-ci.org/manosbatsis/restdude)

Full stack, high level framework for horizontal, model-driven application hackers.


## Technology Stack

- Choose between Spring MVC or Spring Boot
- Support a [3rd level](https://martinfowler.com/articles/richardsonMaturityModel.html#level3) RESTful API out of the box with [HATEOAS](https://en.wikipedia.org/wiki/HATEOAS)
- Allow clients to use plain model-based REST or [JSON API 1.x](http://jsonapi.org/format) 
- Use a 3-tier architecture without the boilerplate with [restdude-mdd](restdude-mdd)
- Annotate your models to configure API documentation, authorization, validation and auditing
- Sleep better with built-in [error-management](restdude-error)
- Customize and integrate according to your needs with Spring Security
- Interact with your users via websockets and STOMP
- Persist to relational databases with JPA
- Free your persisted data structures with ElasticSearch, Solr, MongoDB, Redis, Cassandra, Counchbase, Neo4J, Hazelcast and more
- Automate basic or fine grained auditing using <a href="http://docs.spring.io/spring-data/data-jpa/docs/current/reference/html/auditing.html">Envers</a> and <a href="http://javers.org/">Javers</a>
- Publish REST API documentation effortlesly thanks to <a href="http://springfox.github.io/springfox">Springfox</a> and <a href="http://swagger.io/swagger-ui">Swagger UI</a>
- Optional web UI and admin panel provided out-of the-box based on bootstrap 4, backbone.marionette, core-UI, requirejs and more.

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

The endpoints support both `application/json` and `application/vnd.api+json` content types, with request/response bodies being model-based or [JSON-API-compliant](http://jsonapi.org/format) JSON structures respectively.

## Behind the Schenes

Contrary to other frameworks like spring-data-rest and jhipster, restdude provides effortless SCRUD services by generating controller, service and repository classes during application startup. 

This provides an extensible 3-tier architecture without any need for boilerplate code and allows to replace and extend the generated components with your own at any time when custom or 
otherwise additional functionality is needed. Restdude will simply not generate components that overlap with those already available.


## Documentation

See https://manosbatsis.github.io/restdude/
