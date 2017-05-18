// app/components/login-form.js
import Ember from "ember";

const { service } = Ember.inject;

export default Ember.Component.extend({
  session: service('session'),


  actions: {
    submit(){
      let creds = this.get('identification', 'password');
      this.attrs.triggerAuthenticate(creds);
    }
  }

});
