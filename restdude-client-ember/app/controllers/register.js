import Ember from 'ember';
export default Ember.Controller.extend({
  session: Ember.inject.service('session'),



  actions: {
    save(user){
      let newUser = null;
       newUser = user;
      newUser.save().catch((error) => {
        this.set('errorMessage', error);
      })
      .then(()=>{
        this.get('session')
        .authenticate('authenticator:jwt', //jwt
          newUser.get('email'), newUser.get('password'))
        .catch((reason) => {
          this.set('errorMessage', reason.error ||reason);
        });
  this.transitionToRoute("/");
      });
    },
    createUser: function(){
      var username = this.get('username');
      var email = this.get('email');
      var password = this.get('password');
      var passwordConfirmation = this.get('passwordConfirmation');
      var user = this.store.createRecord('user', {
        username: username,
        email: email,
        password: password,
        passwordConfirmation: passwordConfirmation
      });
        user.save();
    }
  }
});
