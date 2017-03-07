

# Restdude

[![Build Status](https://travis-ci.org/manosbatsis/restdude.svg?branch=master)](https://travis-ci.org/manosbatsis/restdude)

Full stack, high level framework for horizontal, model-driven application hackers.


## Technology Stack

- Choose between Spring MVC or Spring Boot
- Support a [3rd level](https://martinfowler.com/articles/richardsonMaturityModel.html#level3) RESTful API out of the box with [HATEOAS](https://en.wikipedia.org/wiki/HATEOAS)
- Allow clients to use plain model-based REST or [JSON API 1.x](http://jsonapi.org/format) 
- Enjoy effortless, model-driven search endpoints with sorting, paging and dynamic criteria support for simple URL params or [RSQL/FIQL](https://manosbatsis.github.io/restdude/rsql.html)
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

## Why?

Contrary to other frameworks that squeeze everything within two (i.e. Controller, Repository) or even a single tier, restdude provides effortless SCRUD services by generating controller, service and repository components  
for you classes during application startup. 

This provides an extensible 3-tier architecture without any need for boilerplate code and allows to replace and extend the generated components with your own at any time when custom or 
otherwise additional functionality is needed. 

Besides automating SCRUD, restdude provides other conveniences with a focus on hypermedia, like dynamic generation of HATEOAS/JSON-API links and controller request mappings 
based on entity model relationships. 


## Example Model

```java
@Entity
@Table(name = "host")
@ModelResource(
        path = "hosts", 
        apiName = "Hosts", 
        apiDescription = "Operations about hosts")
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
    
	@OneToMany(mappedBy = "host")
    private List<Site> sites;
    
    // other properties, getters/setters etc.
}
```

                                                                                          -                               

### Generated Components

Based on the above model, restdude will generate any missing components like a `Controller`, `Service` and `Repository` 
to give you the following architecture without the need for boilerplate code. You can replace or extend those with your 
custom components at any time. 


<pre>
                    +---------------------------+ +---------------------+   +------------------+
 Browser, App,      | RESTful SCRUD             | | Content negotiation |   | Websockets       | 
 or other Client    +-------------+-------------+ +-----------+---------+   +-------+----------+
                                  |                           |                     |           
----------------------------------|---------------------------|---------------------|------------
                                  |                           |                     |            
                      JSON+HATEOAS or JSON-API 1.x            |                     |            
 Network              with RSQL/FIQL or URL params            |                   STOMP          
                                  |                           |                     |            
                                  |                           |                     |            
----------------------------------|---------------------------|---------------------|------------
                                  |                           |                     |            
                    +-------------+--------------------------+-----------+  +------------------+    
                    | <strong>HostController</strong>                                     +--+ Message Broker   |
                    +--------------------------+-------------------------+  +------+-----------+
                                               |                                   |            
 Restdude                                      |                                   |            
                    +--------------------------+-----------------------------------+-----------+
                    |                                <strong>HostService</strong>                               |
                    +--------+--------------------+---------------------+----------------+-----+
                             |                    |                     |                |      
                             |                    |                     |                |      
                    +--------|-------+ +----------|-----------+ +-------|-------+ +------|-----+
                    | <strong>HostRepository</strong> | | FileService (FS, S3) | | EmailService  | | Misc Util  |
                    +----------------+ +----------------------+ +---------------+ +------------+
</pre>
                               

### Generated Services

Some of the RESTful services provided out of the box for the above model:


Method  | Path    | Description
------------ | ------- | -------------------
GET  | /api/rest/hosts/{id} | Fetch the host matching the id
GET  | /api/rest/hosts/{id}/relationships/country | Fetch the country of the host matching the id
GET  | /api/rest/hosts/{id}/relationships/sites | Search the sites of the host matching the id, see search endpoints bellow for filtering etc (paged)
GET  | /api/rest/hosts?country.code=GR&name=%25startsWith | Search based on model properties using simple URL params (paged)
GET  | /api/rest/hosts?filter=country.code=in=(GR,UK);name==%25startsWith | Search based on model properties using [RSQL or FIQL](https://manosbatsis.github.io/restdude/rsql.html) (paged)
POST | /api/rest/hosts | Create a new host
PUT  | /api/rest/hosts/{id} | Update the host matching the id
PATCH  | /api/rest/hosts/{id} | Partially update the host matching the id
DELETE | /api/rest/hosts/{id} | Delete the host matching the id
GET    | /api/rest/hosts/jsonschema | Get the JSONSchema for hosts

The endpoints support the following content types:

 - `application/json` and `application/hal+json` for HATEOAS Resource response and plain JSON request bodies, with both being based on model structure
 - `application/vnd.api+json` for [JSON-API](http://jsonapi.org/format)-compliant request/response bodies

## Documentation

See https://manosbatsis.github.io/restdude/

## License
    
Restdude is distributed under the <a href="https://www.gnu.org/licenses/lgpl-3.0-standalone.html">GNU Lesser General Public License</a> <img src="docs/assets/images/agplv3-155x51.png" alt="LGPL Logo" />?
