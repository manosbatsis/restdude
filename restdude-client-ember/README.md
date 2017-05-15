# Super-rentals

Super Rentals is the working repository of the Ember tutorial: https://guides.emberjs.com/v2.9.0/tutorial/ember-cli/

## Prerequisites

You will need the following things properly installed on your computer.

* [Git](http://git-scm.com/)
* [Node.js](http://nodejs.org/) (with npm)
* [Bower](http://bower.io/)
* [Ember CLI](http://www.ember-cli.com/)


## Installation

* `git clone <repository-url>` this repository
* change into the new directory
* `npm install`
* `bower install`


## Running / Development

### Backend

To use the Spring Boot backend for authentication:

a) Fetch the latest
 
```
cd restdude
git pull
```

b) Start the Spring Boot version of restdude:

```
cd restdude-boot
mvn clean install spring-boot:run
```

### Frontend

* `ember s  --proxy http://localhost:8080`
* By default, Mirage is disabled in production, and also in development when using the -proxy option.
* Visit your app at [http://localhost:4200](http://localhost:4200).
* Visit the login page at [http://localhost:4200/auth/login](http://localhost:4200/auth/login).
* Visit the register page at [http://localhost:4200/auth/register](http://localhost:4200/auth/register).
* Visit the forgot-password page at [http://localhost:4200/forgot-password](http://localhost:4200/auth/forgot-password).
* Visit the user page, if you are authenticated, at [http://localhost:4200/user](http://localhost:4200/auth/user).

### Login

The login is made with JWT authentication. If you want a custom JWT authentication, go on controllers/login.js and change it to authenticator = 'authenticator:custom'. Also go to components/login-form.js and change to authenticator:custom. From the app/adapters/application.js we can change the namespace and the host. If we want to use the custom authentication, we change the url on app/authenticators/custom.js

### Register

The register is made with JWT authenticator. In controllers/register.js, there are two different ways to register, save(user) and createUser. The first one uses JWT authenticator and the second one is a simpler way without JWT.

### User

The user page has a logout button that invalidates the session and a search bar for logged users that is slack-based and returns all the users that are registered.

### Environment 

At the config/environment.js we can change the login setting at   ENV['ember-simple-auth'] and ENV['ember-simple-auth-token'].


### Code Generators

Make use of the many generators for code, try `ember help generate` for more details

### Running Tests

* `ember test`
* `ember test --server`

### Building

* `ember build` (development)
* `ember build --environment production` (production)

### Deploying

Specify what it takes to deploy your app.

## Further Reading / Useful Links

* [ember.js](http://emberjs.com/)
* [ember-cli](http://www.ember-cli.com/)
* Development Browser Extensions
  * [ember inspector for chrome](https://chrome.google.com/webstore/detail/ember-inspector/bmdblncegkenkacieihfhpjfppoconhi)
  * [ember inspector for firefox](https://addons.mozilla.org/en-US/firefox/addon/ember-inspector/)

## Project Structure

 * app: This is where folders and files for models, components, routes, templates and styles are stored. 
 The majority of your coding on an Ember project happens in this folder. Here we can find the adapters, authenticators,
 authorizers, components, controllers, helpers, models, routes, services, styles, templates, utils folders. We also can find
 app.js, the index.html, resolver.js, router.js . 
 

        * adapters: An adapter is an object that receives requests from a store and translates them into the appropriate action to take against your persistence layer.
         The persistence layer is usually an HTTP API, but may be anything, such as the browser's local storage. Typically the adapter is not invoked directly instead its functionality is accessed through the store.

        * authenticators: in this folder we can find the authenticators that the app needs such as oauth2, for login/logout, auth_manager and facebook_autheticatore .

        * authorizers: in this folder we can find the authorizers that the app needs such as oauth2 or custom. 

        * components: Describes the working behavior of an interface. It constists of two parts:
           Handlebars template and a matching JavaScript profile. 
        
        * controllers: we can find the JavaScript controllers that run the logic of every page. More specific we can find
          the login.js and signup.js controllers, that define the logic of login/logout.

        * helpers: Helpers allow you to add additional functionality to your templates beyond what is included out-of-the-box in Ember.
          Helpers are most useful for transforming raw values from models and components into a format more appropriate for your users.

        * models: Describes the data being stored. Models in ember can write methods, write questions and perform actions.

        * routes: Determines which model should be loaded along a path. In this folder we can find all the routes for our app. 

        * services: 

        * styles: this folder includes all the css files that we want to be transfered at dist folder, in order the pages can find the stylessheets.

        * templates: Displays loaded properties from the router and model. It is mostly HTML mixed with some Handlebar code.
         In this folder we have all the templates for our route pages. 

        * utils:  

        * router.js : Determines which code should manage actions along a path
 * config: The config directory contains the environment.js where you can configure settings for your app.

 * dist: When we build our app for deployment, the output files will be created here.

 * mirage: Ember Data can be configured to save data in a variety of ways, but often it is setup to work with a backend API server. Therefore we use Mirage. 
   This will allow us to create fake data to work with while developing our app and mimic a running backend server.

 * public: This directory contains assets such as images and fonts.

 * vendor: This directory is where front-end dependencies (such as JavaScript or CSS) that are not managed by Bower go.
 
 * tests / testem.json: Automated tests for our app go in the tests folder, and Ember CLI's test runner testem is configured in testem.json

 * tmp: Ember CLI temporary files live here. 

## Creating new Data 
 * To add a new route, write on terminal : ember g route nameofroute
 * To add a new model : ember g model nameofmodel 
 *  To add a new component : ember g component nameof-component 
