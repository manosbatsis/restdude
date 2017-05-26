import Ember from 'ember';
import ApplicationController from './application';

export default ApplicationController.extend({

  session: Ember.inject.service(),
  actions: {

    /**
     * Attempt to save the new organization
     */
    save(){

      // keep a ref to form input
      const registrationInfo = this.getProperties('pk', 'username', 'firstname','lastname','email','password','passwordconfirmation');

      // create a model for POSTing to /api/auth/users
      var account = this.store.createRecord('user', registrationInfo);

      // save to register organization account.
      account.save().catch((error) => {
        this.set('errorMessage', error);
      })
      .then(() => {

        // TODO: transition to email confirmation code input form
       // this.transitionToRoute("/");

      });
    }

  }
});
