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

## Developer's Overview

### Model Driven Development

Usecases in RestDude are largely broken down to four major parts: 

- Entity, transfer, or other models
- RESTful HTTP mappings
- Business methods
- Data operations: persistance, indexing, messaging etc.

REST is pretty regilar, so <a href="https://en.wikipedia.org/wiki/Create,_read,_update_and_delete">SCRUD</a> support is automatically provided. Models are just POJOs and can be JPA entities, ElasticSearch documents etc. They caN also include annotations and other metadata to declaratevly compose usecase implementaion details like HTTP mappings, authorization rules, , tier components, validation rules, model-to-model-mappings, applications events handlers etc. 

#### Entity Models

#### Graph Models

#### Generated Components

Support for SCRUD and other utilities is generated automatically as RestController, Service and Repository classes on application startup. Developers can easily provide custom implementations for any of the tiers by simply extending the coresponding implementations; Restude will refrain from generating overlaping components. 

A modern SPA wbapp that automatically adopts to the above and can be manageably customized and extended further is also provided.

#### Validation

<a href="http://beanvalidation.org/">Bean validation</a> is supported.

#### Authorization



#### I18n

Standard Java property resources are used in the backend and include properties for stabdard functionality like registration, confirmation and password-reset emails. JSON labels are used on the client-side by default and similarly include standard Web UI messages.Client i18n is based on <a href="http://requirejs.org/docs/api.html#i18n">requirejs</a>.

#### Custom Components

Backend is based on <a href="https://projects.spring.io/spring-framework/">Spring 4.x</a>. Support classes include controllers, converters, (de)serializers, services, repositories, base models and more.

Frontend is based on <a href="http://marionettejs.com/">backbone.marionette</a> and similarly provides base models, model-driven UI components views and layouts (forms, menus etc.), converters, formatters, routes and more.

##### Repositories

JPA repositories work with practically any relational database. NoSQL databases etc. can also be easily setup thanks to spring-data modules.

##### Sercvices

##### Controllers

##### Binding

### Other Services

#### Authentication

#### Social Networks Integration

Social network support includes transparent registration and sign-in via any major social network. Spring social provides an API for implementing other integrations.

#### Auditing

Basic entity-based auditing is supported via using <a href="http://docs.spring.io/spring-data/data-jpa/docs/1.7.0.DATAJPA-580-SNAPSHOT/reference/html/auditing.html">Spring -data/Hibernate Envers</a> annotations oir interfaces in your models. Complex auditing is supported via <a href="http://javers.org/documentation/">Javers</a>.

#### Error Management

#### Indexing

#### Email

#### Websockets API

### Browser Client

#### Dynamic Componets

#### Customization

#### I18n



