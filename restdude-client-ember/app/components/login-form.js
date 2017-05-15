// app/components/login-form.js
import Ember from 'ember';

export default Ember.Component.extend({
        auth: Ember.inject.service('session'),
        identification: null,
        password: null,
        actions: {
        authenticate() {
        this.get('auth').authenticate('authenticator:jwt', //or custom
        this.get('identification'),this.get('password')).then(() => {
        alert('Thanks for logging in!');
        this.get('transition')();
        }, () => {
        alert('Wrong user name or password!');
        });
   }
 }
});
