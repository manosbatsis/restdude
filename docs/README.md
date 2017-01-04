


Experiments on a full stack, high level framework for lazy application hackers and scalable developer teams.

## Table of Contents

- [Installation](#installation)
- [Client Stack](#client-stack)
  - [Routing](#routing)
    - [Implicit Routes](#implicit-routes)
      - [Use Case Routes](#use-case-routes)
      - [Search Routes](#search-routes)
      - [Page Routes](#page-routes)
    - [Explicit Routes](#explicit-routes)
  - [Views](#views)
  - [Models](#models)
    - [Type Name](#type-name)
    - [Path Fragment](#path-fragment)
    - [Use Cases](#use-cases)
      - [Base Cases](#base-cases)
      - [Deep Merging](#deep-merging)
      - [Use Case Properties](#use-case-properties)
    - [Fields](#fields)
      - [Built-in Field Types](#built-in-field-types)
  - [Internationalization](#internationalization)
  - [Optimization](#optimization)
- [Server Stack](#server-stack)
  - [Architecture](#architecture)
  - [Model-Driven Tiers](#model-driven-tiers)
    - [RESTful Service URLs](#restful-service-urls)
  - [Custom Tiers](#custom-tiers)
  - [Authentication and Authorization](#authentication-and-authorization)
  - [Persistence](#persistence)
  - [Email](#email)
  - [Internationalization](#internationalization)

## Installation

See the [checkout and build](src/etc/checkout_and_build.md) guide.

## Client Stack

The javascript stack provides a [responsive](https://en.wikipedia.org/wiki/Responsive_web_design),
[SPA](http://en.wikipedia.org/wiki/Single-page_application) client framework based on libraries like
[backbone.marionette](http://marionettejs.com/), [bootstrap](http://getbootstrap.com/) and
[requirejs](http://requirejs.org/).

The stack contributes to productive developers and maintainable code. It allows you to quickly and
consistently implement use cases or other functional requirements declaratively, using [#models](metadata in JSON notation),
making it natural for new code to be added in the form of reusable components.

### Routing

`Calipso.Router` and `Calipso.Controller` extend the respective types of [backbone.marionette](http://marionettejs.com/) to dynamically handle declarative [use cases](#use-cases) defined by models, as well as "normal" explicit routes marionette developers are used to.

#### Implicit Routes

Routers extending `Calipso.AppRouter` can define their own explicit routes but also inherit a set of implicit, dynamic routes described bellow.

##### Use Case Routes

Use case routes are suffixed by "useCases/". Assuming the base webapp URL is `/calipso/client` then
dynamic routes in the form of `/calipso/client/useCases/pathFragment/useCaseName` apply,
where __pathFragment__ matches a model by it's
corresponding static property and __useCaseName__ the use case defined within the model (or a super type)
under the same key. For example, `/calipso/client/useCases/books/publish` mathes the following:


```javascript
var BookModel = Calipso.model.GenericModel.extend({

},
// static members
{
    // Use this model for routes starting with "books"
    pathFragment : "books",
    // Define or override the use cases of this model type. See also  
    // the [Use Cases section](#use-cases) for more details.
    useCases : {
        // Each use case matches it's own URL route, for example
        // this one matches "books/publish"
        publish : {
            // use case configuration...
        }
    }
});

```

##### Search Routes

In absence of a use case key like `/calipso/client/useCases/pathFragment?queryString`, the __search__ use case is used by default, i.e.
`/calipso/client/useCases/pathFragment/search?queryString`. The [queryString](https://en.wikipedia.org/wiki/Query_string) will
be used as search criteria if present.

##### Page Routes

Page routes like `/calipso/client/page/pageFragment` are suffixed by "page" and will render an item view using the template
matched by the __pageFragment__. For example, `/calipso/client/page/support` will use the template in `/calipso/templates/support.hbs`.

The view model in page routes is the current userDetails (logged in user).

#### Explicit Routes

Explicit routes are defined in the usual backbone/marionette way when needed. Your controller only needs to define it's own
and those will extend `Calipso.AppRouter`'s routes, for example:


```javascript
// This is an example of extending Calipso.Approuter in your app.
// Any appRoutes you define here will be merged with
// the super type routes.
var MyRouter = Calipso.AppRouter.extend({

    controller : new MainController(),
    appRoutes : {
        // explicit routes here will be merged with
        // those defined by Calipso.AppRouter automatically
        "release/the/kraken" : "releaseTheKraken"
    }
});

```

### Views

Build-in views extend those of [Marionette](http://marionettejs.com/docs/current/) and include generic layouts, model-driven grids/forms etc.

View templates use [Handlebars](http://handlebarsjs.com/) by default. The build-in template helpers include:

- baseUrl
- ifCond
- ifLoggedIn
- ifLoggedOut
- ifUserInRole
- ifUserNotInRole
- moment
- momentDateTime
- momentFromNow
- getLocale
- getUserDetailsProperty
- getUserDetailsMetadatum
- getValueLabel

### Models

Calipso defines a metadata profile for it's backbone models. The metadata are used for
dynamically handle URL routes, render fields of a form or grid and more.

Metadata are typically defined/overriden declaratively as static properties (e.g. `pathFragment`)
of a model type. The corresponding getter methods can be used as well (e.g. `getPathFragment`).

The following is a typical example of model metadata, in this case `pathFragment`, `fields` and `useCases`:

```javascript
var BookModel = Calipso.Model.extend({

},
// static members
{
    // Use this model for routes starting with "books"
    pathFragment : "books",
    // Define the fields of this model type. See also  
    // the [Fields section](#fields) for more details
    fields : {
        name : {
            "datatype" : "String",
        },
        isbn : {
            // custom field type
            "datatype" : MyIsbnType,
        },
        edit : {
            "datatype" : "Edit",
        },
    },
    // Define or override the use cases of this model type. See also  
    // the [Use Cases section](#use-cases) for more details.
    useCases : {
        // Each use case matches its own URL route, for example
        // this one matches "books/publish"
        publish :{
            // ...
        }
    }
});
```

#### Type Name

This is only useful for debugging (TODO: named constructors)

#### Path Fragment

The path fragment corresponding to the model type. This is used to map URL routes to a specific model and the use cases it defines. For example `useCases/books/publish` maps to

```javascript
var BookModel = Calipso.Model.extend({

},
// static members
{
    // Use this model for routes starting with "books"
    pathFragment : "books",
    // Define or override the use cases of this model type. See also  
    // the [Use Cases section](#use-cases) for more details.
    useCases : {
        // Each use case matches its own URL route, for example
        // this one matches "books/publish"
        publish :{
            // ...
        }
    }
});
```

#### Use Cases

Use cases allow you to declaratively define how a route should be handled.

```javascript
var BookModel = Calipso.Model.extend({

},
// static members
{
  // Define or override the use cases of this model type. See also  
  // the [Use Cases section](#use-cases) for more details.
  useCases : {
    // use case configuration here
  }
});
```

##### Base Cases

All models extending `Calipso.model` inherit the following use cases:

```js
useCases : {
  create : {
    view : Calipso.view.BrowseLayout,
  },
  update : {
    view : Calipso.view.BrowseLayout,
  },
  search : {
    view : Calipso.view.SearchLayout,
  },
},
```

##### Deep Merging

Use cases of a model are *deeply* merged wth the use cases defined by the model it extends, for example:

```js
useCases : {
  create : {
    // No need to define a view as it will be
    // inherited from Calipso.Model
    //view : Calipso.view.BrowseLayout,
    viewOptions : {
      //...
    }
  },
  //...
},
```

##### Use Case Properties

- `fieldIncludes`: Include matching field names. Matches must not be matched by `fieldExcludes`.
- `fieldExcludes`: Exclude matching field names.
- `view`: The view __type__ to render e.g. `Calipso.view.HomeLayout`
- `viewOptions`: Options to merge with the ones given to the view
- `overrides`: Provides nested use case configuration to merge and apply to any view's region name or `schemaType`.
- `fields`: Override the fields used by the region view

Here's an example `useCases` configuration:

```js
useCases : {
  create : {
    view : Calipso.view.UserRegistrationLayout,
    fieldIncludes : [ "firstName", "lastName", "email" ]
  },
  search : {
    view : Calipso.view.SearchLayout,
    overrides : {
      //
      backgridView : {
        fieldIncludes : [ "username", "firstName", "lastName", "edit" ]
      },
      formView : {
        fieldIncludes : [ "username", "firstName", "lastName", "email" ],
        fields : {
          username : {
            "datatype" : "Boolean",
          }
        }
      },
    }
  },
},
```

#### Fields

Fields provide metadata about a model's properties, basically their data type and how they should be rendered in forms, grids or generic HTML markup.

Here is an example model configuration:

```js
Calipso.model.ServerModel = Calipso.Model.extend({},
// static members
{
  fields : {
    "domain" : {
      fieldType : "string",
      backgrid : {
        cell : Calipso.components.backgrid.ViewRowCell,
      }
    },
    "ip" : {
      fieldType : "link",
    },
  },
});
```

Fields may also override a field type's component or component options used for rendering.

Here's an example where  a model field overrides the default fieldType's backgrid configuration to use a custom backgrid cell:

```js
fields : {
  "domain" : {
    fieldType : "string",
    backgrid : {
      cell : Calipso.components.backgrid.ViewRowCell,
    }
  }
}
```
The build-in schemas types include:

- `form`: Uses the backbone.forms [schema definition](https://github.com/powmedia/backbone-forms#schema-definition)
- `backgrid`: Uses backgrid's [column configuration](http://backgridjs.com/index.html#grid

Additional schema types cna be supported by your views by extending `Calipso.view.UseCaseItemView`.

##### Built-in Field Types

Name | Alias(es)
--- | --- | ---
`string` | `String`
`text` | `Text`
`hidden` | `Hidden`
`bool` | `boolean`, `Boolean`
`int` | `integer`, `Integer`
`float` | `decimal`, `Decimal`, `Float`
`money` | `Money`
`datetime` | `Datetime`
`date` | `Date`
`time` | `Time`
`lov` | `Lov`
`list` | `List`
`email` | `Email`
`tel` | `Tel`
`link` | `Link`
`file` | `File`
`img` | `image`, `Image`
`colour` | `Colour`, `color`, `Color`
`json` | `Json`
`md` | `markdown`, `Markdown`
`html` | `Html`
`csv` | `Csv`
`pwd` | `password`, `Password`
`ConfirmPassword` | -
`CurrentPassword` | -
`edit` | `Edit`

### Internationalization

### Optimization

The calipso-hub-web module provides an __optimize__ profile that minifies javascript and CSS resources using [requirejs](http://requirejs.org/) and the [requirejs-maven-plugin](https://github.com/bringking/requirejs-maven-plugin).

```bash
~/git/calipso-hub/calipso-hub-webapp$ mvn clean install jetty:run-war -P optimize
```

## Server Stack

### Architecture

The [stateless](https://en.wikipedia.org/wiki/Stateless_protocol) back-end is build on top of the [Spring Framework](https://projects.spring.io/spring-framework/) and provides dynamic, model driven [RESTful](https://en.wikipedia.org/wiki/Representational_state_transfer) services for your entities, including complete coverage of [SCRUD](https://en.wikipedia.org/wiki/Create,_read,_update_and_delete) use cases.

### Model-Driven Tiers

To have a SCRUD stack automatically generated for your entity model, simply annotate it with `@ModelResource`:


```java
@Entity
@ModelResource(path = "books")
@Table(name = "book")
public class Book extends AbstractAuditable<User> {
  //...
}
```

 During startup, the application will automatically generate and register Conteoller, Service (both interface and impl) and Repository beans for your entity model.


#### RESTful Service URLs

The generated beans will follow REST conventions to expose SCRUD service endpoints
 for your entity models in a regular way. Consider the RESTful URLs for the book an entity model above:

 HTTP Method   | HTTP URL | Action
 ------------- | ---------|--------------
 GET           | api/rest/books/id | [Fetch](#fetch) a book matching the id
 GET           | api/rest/books?foo=bar&foo.subFoo=baz | [Search](#search) books
 POST          | api/rest/books | [Create](#create) a new book
 PUT           | api/rest/books/id | [Update](#update) the book matching the id
 DELETE        | api/rest/books/id | [Delete](#delete) the book matching the id

##### Fetch

 ```
 GET http://localhost:8080/calipso/books/id
 ```

 Find the book having "id" as the id value and return it in the response. Throw an 404 HTTP error if no match is found.

##### Search

 ```
 GET http://localhost:8080/calipso/books?foo=bar&foo.subFoo=baz
 ```

 Get a paginated collection of all books matching the given criteria. No actual java implementation is required for your entity models, their properties are dynamically mapped to the HTTP parameters by the default. additionally, the following predefined parameters are supported:

 Name       | Required | Default | Description
 ---------- | -------- | ------- | --------------
 page       | false    | 0       | Page number starting from 0 (default)
 size       | false    | 10      | Page size, default to 10
 properties | false    | "id"    | Ordered list of comma-separeted property names used for sorting results. Default is "id"
 direction  | false    | "ASC"   | Optional sort direction, either "ASC" or "DESC". Default is "ASC".


##### Create

```
POST http://localhost:8080/calipso/books
```

Create a new book using the request body and return it in the response.


##### Update

```
PUT http://localhost:8080/calipso/books/id
```

Update the book matchign "id" using the request body and return the result in the response. Partial updates are easily supported, just mark your Java entity class using <code>implements PartiallyUpdateable</code> to support partial updates. No actual implementation is needed.


##### Delete

```
DELETE http://localhost:8080/calipso/books/id
```

Delete the book matching "id"


### Custom Tiers

To get an idea of the generated beans or to manually create your own,
just follow the examples given  bellow. No SCRUD related code is needed
other than extending the build-in interfaces or classes as appropriate.


#### Repository

You can implement your repository just by extending the `ModelRepository<T, ID>`
interface, with T and ID being the entity model and id class respectively.

```java
import gr.abiss.calipso.tiers.repository.ModelRepository;

public interface BookRepository extends ModelRepository<Book, String> {

}
```

#### Service

A service requires both interface and implementation classes. However,
no implementation code is actually required.

##### Service Interface

Just extend the `ModelService<T, ID>` interface, with T and ID being
the entity model and id class respectively.

```java
import gr.abiss.calipso.tiers.service.ModelService;

public interface BookService extends ModelService<Book, String> {

}
```

##### Service Implementation

Similarly, extend the `AbstractModelServiceImpl<T, ID, R>` class, with T, ID and R being
the entity model, id class and repository interface types respectively.

```java
@Named("bookService")
@Transactional(readOnly = true)
public class BookServiceImpl extends AbstractModelServiceImpl<Book, String, BookRepository> implements BookService {


}
```

#### Controller

To manually create your own controller, extend `AbstractModelController<T, ID, S>` class, with T, ID and S being
the entity model, id class and service interface types respectively.

```java
@Controller
@RequestMapping(value = "/api/rest/books", produces = { "application/json", "application/xml" })
public class BookController extends AbstractModelController<Book, String, BookService> {

}
```

### Authentication and Authorization

### Persistence

Relational databases are supported by [JPA](https://en.wikipedia.org/wiki/Java_Persistence_API) ([Hibernate](http://hibernate.org/) is used under the hood).
[NoSQL](https://en.wikipedia.org/wiki/NoSQL) stores like [MongoDB](https://www.mongodb.org/), [Cassandra](http://cassandra.apache.org/),
[Couchbase](http://www.couchbase.com) and [Neo4j](http://neo4j.com/) are supported as well, while application instances also contain
their own clusterable [ElasticSearch](https://www.elastic.co/) node by default.

#### Database Migrations

Relational database schema versioning and migration is supported using [Flyways](https://flywaydb.org/).

### Email

Easy email services with i18n support and Thymeleaf templates. Build-in services include email verification, password reset etc.

### Internationalization

Translations can be added in the [i18n folder](tree/master/calipso-hub-webapp/src/main/resources/i18n). File names use the [ISO_639-1 standard](https://en.wikipedia.org/wiki/ISO_639-1). To add Greek for example, copy and translate messages.properties to messages_el.properties.
