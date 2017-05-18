// app/services/current-user.js
import Ember from 'ember';

const { inject: { service }, isEmpty, RSVP } = Ember;

export default Ember.Service.extend({
  session: service('session'),
  store: service(),
  //metrics: service(),

  load() {
    if (this.get('session.isAuthenticated')) {
      return this.get('store').queryRecord('user', { me: true }).then((user) => {
        this.set('user', user);
      });
    } else {
      return Ember.RSVP.resolve();
    }
  }
//////////////////////////////////////////////
  /*_identifyUser(user) {

      this.get('metrics').identify({
        distinctId: this.get(user, 'id'),
        pk: this.get(user, 'pk'),
        firstName: this.get(user, 'firstName'),
        email: this.get(user, 'email'),
        lastName: this.get(user, 'lastName'),
        username: this.get(user, 'username')
      });
  }*/
});
/////////////////////////////////////////////////////
/*if we want to load a user with its id we use the code following:

load() {
let userId = this.get('session.data.authenticated.user_id');
if (!isEmpty(userId)) {
return this.get('store').findRecord('user', userId).then((user) => {
  this.set('user', user);
});
} else {
return Ember.RSVP.resolve();
}
}

*/
