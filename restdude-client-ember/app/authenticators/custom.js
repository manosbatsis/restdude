import Ember from "ember";
import ENV from "../config/environment";
import Base from "ember-simple-auth/authenticators/base";


const { RSVP: { Promise }, $: { ajax }, run } = Ember;

export default Base.extend({
  tokenEndpoint: `${ENV.host}/${ENV.namespaceAuth}/jwt/access`,

  restore(data) {
    return new Promise((resolve, reject) => {
      // check for a valid user id
      if (!Ember.isEmpty(data.id) || !Ember.isEmpty(data.id)) {
        resolve(data);
      } else {
        reject();
      }
    });
  },
  authenticate(credentials) {
    // submitted credentials
    const data = JSON.stringify({
      username: (credentials.identification || credentials.username || credentials.email),
      password: credentials.password
    });
    // login request

    const requestOptions = {
      url: this.tokenEndpoint,
      type: 'POST',
      data,
      contentType: 'application/json;charset=utf-8',
      dataType: 'json',
      crossDomain: true,
      xhrFields: { withCredentials: true }

    };
    return new Promise((resolve, reject) => {
      ajax(requestOptions).then((response) => {
        // Wrapping aync operation in Ember.run
        run(() => {
          resolve(response);
        });
      }, (error) => {
        // Wrapping aync operation in Ember.run
        run(() => {
          reject(error);
        });
      });
    });
  },

  invalidate(data) {
     const requestOptions = {
      url: this.tokenEndpoint,
      type: 'DELETE',
      data,
      contentType: 'application/json;charset=utf-8',
      dataType: 'json',
      crossDomain: true,
      xhrFields: { withCredentials: true }

    };
    return new Promise((resolve, reject) => {
      ajax(requestOptions).then((response) => {
        // Wrapping aync operation in Ember.run
        run(() => {
          resolve(response);
        });
      }, (error) => {
        // Wrapping aync operation in Ember.run
        run(() => {
          reject(error);
        });
      });
    });

}
});
