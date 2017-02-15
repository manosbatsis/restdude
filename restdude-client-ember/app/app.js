import Ember from 'ember';
import Resolver from './resolver';
import loadInitializers from 'ember-load-initializers';
import config from './config/environment';

let App;

Ember.MODEL_FACTORY_INJECTIONS = true;

App = Ember.Application.extend({
  modulePrefix: config.modulePrefix,
  podModulePrefix: config.podModulePrefix,
  Resolver
});

loadInitializers(App, config.modulePrefix);
//////////
/*App = Ember.Application.create({
  rootElement: "root-element-id",

  registerAuthManager: function() {
    this.register("auth:manager", App.AuthManager, { singleton: true, instantiate: true });
    this.inject("controller", "authManager", "auth:manager");
    this.inject("route", "authManager", "auth:manager");
    this.inject("component:facebook-login", "authManager", "auth:manager");
  },

  ready: function() {
    this.registerAuthManager();
  }
});
App.deferReadiness(); */

/////////
export default App;
