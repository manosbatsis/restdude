import Ember from 'ember';
import ApplicationController from './application';


export default ApplicationController.extend({
  actions: {

    save(){
      var credentials = this.getProperties('username', 'email', 'password', 'passwordConfirmation');


      var account = this.store.createRecord('account', credentials);


      account.save().catch((error) => {
        this.set('errorMessage', error);
      })
        .then(() => {
          this.get('session')
            .authenticate('authenticator:custom',
              account.get('email'), account.get('password', credentials))
            .catch((reason) => {
              this.set('errorMessage', reason.error || reason);
            });
          this.transitionToRoute("/");
        });
    },
    createUser: function () {
      var username = this.get('username');
      var email = this.get('email');
      var password = this.get('password');
      var passwordConfirmation = this.get('passwordConfirmation');
      var user = this.store.createRecord('account', {
        username: username,
        email: email,
        password: password,
        passwordConfirmation: passwordConfirmation
      });
      user.save();
    }
  }
});
