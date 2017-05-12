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
        .authenticate('authenticator:jwt',
          newUser.get('email'), newUser.get('password'))
        .catch((reason) => {
          this.set('errorMessage', reason.error ||reason);
        });
  this.transitionToRoute("/");
      });
    },
    /*save() {
     let self = this; // create a new variable called 'self' that points to the current 'this', which is the controller
     Ember.$.ajax({
       url: ENV.host + "/users",
       type: "POST",
       data: JSON.stringify({
         "user": {
           "name": this.get('nameInput'),
           "email": this.get('emailInput'), // you're getting this property from the controller now
           "password": this.get('passwordInput'),
           "password_confirmation": this.get('passwordConfirmationInput'),
         }
       })
     }).then(() => {
       // Transition
     }).catch(function(error) {
       // self points at the OLD definition of 'this', aka the controller, which is what we want
       self.set('errorMessage', error.error || error);
     });
   },*/
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
