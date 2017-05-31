import Ember from "ember";
import moment from 'moment';

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
      const userId = sessionDataAuthenticated.id || sessionDataAuthenticated.id;
      if (!Ember.isEmpty(userId)) {
        this.set('account', sessionDataAuthenticated);
        /*return this.get('store').findRecord('user', 'current-user').then((user) => {
          this.set('account', user);
          resolve();
        }, reject);*/

        // moment config
        /*
        const moment = this.get('moment');
        // update the global locale?
        if(sessionDataAuthenticated.locale && sessionDataAuthenticated.locale !== moment.locale()){
          // load the locale if needed
          if(moment.locales().indexOf(sessionDataAuthenticated.locale) === -1){
            Ember.$.getScript(`/assets/moment-locales/${sessionDataAuthenticated.locale}.js`);
          }
          // update locale
          moment.setLocale(sessionDataAuthenticated.locale);
          // Globally set Timezone
          // moment.setTimeZone('America/Los_Angeles');
        }
*/

        resolve();
      } else {
        resolve();
      }
    });
  }
});
