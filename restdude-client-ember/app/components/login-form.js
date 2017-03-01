// app/components/login-form.js
import Ember from 'ember';
export default Ember.Component.extend({
     //session: Ember.inject.service(),
    authenticator: 'authenticator:oauth2',
    actions: {
        session: Ember.inject.service('session'),
        authenticate: function() {
            var credentials = this.getProperties('identification', 'password');
            this.get('session').authenticate('authenticator:oauth2', credentials).catch((message) => {
                this.set('errorMessage', message);
            });
        }
    }
});