// app/controllers/confirmation-email.js
import Ember from "ember";

export default Ember.Controller.extend({
  session: Ember.inject.service(),

  actions: {
    continue: function() {
      var putInfo = this.getProperties('resetPasswordToken','email');
      //var email = this.getProperties('email');

   return new Ember.RSVP.Promise((resolve, reject) => {
    Ember.$.ajax({
      url: 'http://localhost:8080/restdude/api/auth/account',//this.serverTokenEndpoint, //`${config.namespaceConfirm}`
      type: 'PUT',
      data: JSON.stringify({
        //resetPasswordToken: resetPasswordToken,
        //email: email
        putInfo : putInfo

      }),
      contentType: 'application/json;charset=utf-8',
      dataType: 'json'
    }).then(function(response) {
      console.log('Got token: ' + response.token);

      Ember.run(function() {
        resolve({
          token: response.token
        });
      });
    }, function(xhr) {
      var response = xhr.responseText;
      Ember.run(function() {
        reject(response);
      });
    });
  });
}

    }

  });
