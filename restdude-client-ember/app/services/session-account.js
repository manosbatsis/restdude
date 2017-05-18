import Ember from "ember";

const { inject: { service }, RSVP } = Ember;

export default Ember.Service.extend({
  session: service('session'),
  store: service(),

  loadCurrentUser() {
    return new RSVP.Promise((resolve, reject) => {
      const sessionData = this.get('session.data');
      console.log("loadCurrentUser, sessionData: ");
      console.log(sessionData);
      const sessionDataAuthenticated = this.get('session.data.authenticated');
      console.log("loadCurrentUser, sessionDataAuthenticated: ");
      console.log(sessionDataAuthenticated);
      const userId = sessionDataAuthenticated.id || sessionDataAuthenticated.pk;
      if (!Ember.isEmpty(userId)) {
        this.set('account', sessionDataAuthenticated);
        /*return this.get('store').findRecord('user', 'current-user').then((user) => {
          this.set('account', user);
          resolve();
        }, reject);*/
        resolve();
      } else {
        resolve();
      }
    });
  }
});
