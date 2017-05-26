import Ember from 'ember';
import ApplicationController from './application';


export default ApplicationController.extend({

  session: Ember.inject.service(),
  actions: {

    /**
     * Attempt to register the new user
     */
    save(){

      // keep a ref to form input
      const registrationInfo = this.getProperties('name', 'email', 'password','passwordConfirmation');

      // create a model for POSTing to /api/auth/accounts
      var account = this.store.createRecord('account', registrationInfo);

      // save to register user account. the user will receive
      // a confirmation email to complete the registration
      account.save().catch((error) => {
        this.set('errorMessage', error);
      })
      .then(() => {

        // TODO: transition to email confirmation code input form
        this.transitionToRoute("/confirmationEmail");

      });
    }

  }
});
