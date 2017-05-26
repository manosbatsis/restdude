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
      const registrationInfo = this.getProperties('name', 'legalname', 'email');

      // create a model for POSTing to /api/auth/organizations
      var account = this.store.createRecord('organization', registrationInfo);

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
