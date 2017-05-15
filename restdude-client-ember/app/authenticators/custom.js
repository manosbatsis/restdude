import Ember  from 'ember';
import ENV    from '../config/environment';
import Base   from 'ember-simple-auth/authenticators/base';

export default Base.extend({
    tokenEndpoint: `${ENV.apiUrl}authenticate`,
    restore(data) {
        return new Ember.RSVP.Promise(function(resolve, reject) {
            if (!Ember.isEmpty(data.token)) {
                resolve(data);
            } else {
                reject();
            }
        });
    },
    authenticate(credentials) {
        return new Ember.RSVP.Promise((resolve, reject) => {
            Ember.$.ajax({
                url: 'http://localhost:8080/restdude/api/auth/jwt/access',
                type: 'POST',
                data: JSON.stringify({
                    email: credentials.email,
                    password: credentials.password
                }),
                contentType: 'application/json;charset=utf-8',
                dataType: 'json'
            }).then(function(response) {
                Ember.run(function() {
                    resolve({
                        token: response.id_token
                    });
                });
            }, function(xhr) {
                let response = xhr.responseText;
                Ember.run(function() {
                    reject(response);
                });
            });
        });
    },
    invalidate() {
        console.log('invalidate...');
        return Ember.RSVP.resolve();
    }
});
