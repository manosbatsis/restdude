// app/routes/application.js

import Ember from "ember";
import ApplicationRouteMixin from "ember-simple-auth/mixins/application-route-mixin";

const { service } = Ember.inject;

export default Ember.Route.extend(ApplicationRouteMixin, {
  sessionAccount: service('session-account'),



  beforeModel() {
    // widget mode?
    if(document.getElementById("restdude-embedded")){
      console.logout("Switching to embed mode...");
      this.transitionTo('application-embed');
    }
  },

  beforeModel() {
    return this._loadCurrentUser();
  },

  sessionAuthenticated() {
    this._loadCurrentUser().then(()=>{
      this.transitionTo('/');
    }).catch(() => this.get('session').invalidate());
  },

  _loadCurrentUser() {
    return this.get('sessionAccount').loadCurrentUser();
  },

  actions: {
    invalidateSession: function() {
      this.get('session').invalidate();
    },
    logout: function() {
      this.get('session').close();
    }
  }
});
