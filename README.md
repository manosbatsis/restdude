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

HTTP Method | Path   | Description
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

## Developer's Overview

### Model Driven Development

Usecases in RestDude are typically modeled while broken down to four major tiers: 

- Entity, transfer, or other models
- Controllers providing the relevant RESTful HTTP mappings
- Services providing business logic and integration methods 
- Repositories for data operations like persistance, indexing, messaging etc.

REST is pretty regilar, so <a href="https://en.wikipedia.org/wiki/Create,_read,_update_and_delete">SCRUD</a> support is automatically provided. Models are just POJOs and can be JPA entities, <a href=”https://github.com/elastic/elasticsearch”>ElasticSearch</a> documents etc. Any <a href="http://projects.spring.io/spring-data/">Spring Data</a> module can be easily plugged-in to support other NoSQL datastores. 

Models include annotations and other metadata used to declaratively compose usecase implementation details like HTTP mappings, authorization rules, tier components, validation rules, model-to-model-mappings, applications events handlers etc. 

#### Entity Models

#### Graph Models

#### Generated Components

Support for SCRUD and other utilities is generated automatically as RestController, Service and Repository classes on application startup. Developers can easily extend base components to override defaults or add custom functionality for any tier. RestDude will refrain from generating overlapping components. 

A modern SPA wbapp that automatically adopts to the above and can be manageably customized and extended further is also provided.

#### Validation

<a href="http://beanvalidation.org/">Bean validation</a> is supported.

#### Authentication and Authorization

Very flexible, based on Spring Security. Integrates with practically anything.

#### I18n

Standard Java message property resources are used in the backend by default. Those are placed in the classpath root  and include properties for build-in functionality like registration, confirmation and password-reset emails. JSON labels are used on the client-side by default and similarly include standard Web UI messages for build-in models etc. Client i18n is based on <a href="http://requirejs.org/docs/api.html#i18n">requirejs</a>.

#### Custom Components

Backend is based on <a href="https://projects.spring.io/spring-framework/">Spring 4.x</a>. Support classes include controllers, converters, (de)serializers, services, repositories, base models and much more.

Frontend is based on <a href="http://marionettejs.com/">backbone.marionette</a> and similarly provides base models, model-driven UI components views and layouts (forms, menus etc.), converters, formatters, routes and more.

##### Repositories

JPA repositories work with practically any relational database and support entity graphs. Popular NoSQL data stores can be easily setup thanks to spring-data modules.

##### Services

##### Controllers

##### Binding

### Other Services

#### Authentication

#### Social Networks Integration

Social network support includes transparent registration and sign-in via any major social network. Spring social provides an API for implementing other integrations.

#### Auditing

Basic entity-based auditing is supported via using <a href="http://docs.spring.io/spring-data/data-jpa/docs/1.7.0.DATAJPA-580-SNAPSHOT/reference/html/auditing.html">Spring -data/Hibernate Envers</a> annotations oir interfaces in your models. Complex auditing is supported via <a href="http://javers.org/documentation/">Javers</a>.

#### Error Management

All exceptions occurring as a result of REST processing will generate a SystemError instance, persist it and serialize as the JSON HTTP response. Stacktrace hashes, excluding line numbers, are used to group and efficiently manage and store records. Client applications can utilize the same infrastructure to persist ClientErrors.

Errors can be managed via the REST API or through the web UI.

#### Indexing

Indexing uses ElasticSearch by default.

#### Email

#### Websockets API

Controller message mappings provide access to request-response access and topic subscriptions via the “app” and “topic” destination prefixes respectively. Any service can easily message topics and collection or individual user queues.

### Browser Client

#### Dynamic Components

#### Customization

#### I18n


