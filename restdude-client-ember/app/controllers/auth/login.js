// app/controllers/login.js
import Ember from "ember";

export default Ember.Controller.extend({
  session: Ember.inject.service(),

  actions: {
    authenticate: function() {
      var credentials = this.getProperties('identification', 'password'),
        authenticator = 'authenticator:custom';

      this.get('session').authenticate(authenticator,
        credentials).catch((reason)=>{
        console.log("authentication error:");
        console.log(reason);
        let responseJson = reason.responseJSON;
        this.set('errorMessage', responseJson.title || responseJson.message || reason.error || reason);
      });
    }

  }
});
