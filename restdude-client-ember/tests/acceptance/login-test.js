import { test } from 'qunit';
//import moduleForAcceptance from 'super-rentals/tests/helpers/module-for-acceptance';
import Ember from 'ember';


/*import {
  currentSession,
  invalidateSession ,
  authenticateSession
} from 'super-rentals/tests/helpers/ember-simple-auth'; */

/*test('if a user is logged in, they see a logout button', function(assert) {
  authenticateSession(this.application);
  visit('/user');

  andThen(function() {
    assert.equal(currentURL(), '/user');
    const logoutBtnPresent = this.$('.logoutBtn').length > 0 ? true : false;
    assert.equal(
      logoutBtnPresent,
      true,
      'An authenticated user should see the logout button'
    );

    const loginFormPresent = find('#loginForm').length > 0 ? true : false;
    assert.equal(
      loginFormPresent,
      false,
      'An authenticated user should not see the login form'
    );
  });
});

test('user can logout', function(assert) {
  authenticateSession(this.application);
  visit('/user');
  click('.logoutBtn');

  andThen(() => {
    const sesh = currentSession(this.application);
    const isAuthed = Ember.get(sesh, 'isAuthenticated');
    assert.equal(
      isAuthed,
      false,
      'After clicking logout, the user is no longer logged in'
    );

  });
});
*/
test('user can login', function(assert) {
  invalidateSession(this.application);
  visit('/auth/login');

  fillIn('#identification.input', 'admin');
  fillIn('#password.input', 'admin');
  click('#login-btn');

  andThen(function() {
    assert.equal(currentURL(), '/');
  });

  /*andThen(() => {
    const sesh = currentSession(this.application);
    const isAuthed = Ember.get(sesh, 'isAuthenticated');
    assert.equal(
      isAuthed,
      true,
      'after a user submits good creds to login form, they are logged in'
    );

  });*/
});

/*test('If a user puts in the wrong login credentials, they see a login error', function(assert) {
  invalidateSession(this.application);
  visit('/auth/login');

  fillIn('#identification', 'lester@test.com');
  fillIn('#password', 'wrongPassword');
  click('#login-btn');

  andThen(() => {
    const sesh = currentSession(this.application);
    const isAuthed = Ember.get(sesh, 'isAuthenticated');
    assert.equal(
      isAuthed,
      false,
      'User submits bad username and password, fails'
    );

    const loginFormPresent = find('#loginForm').length > 0 ? true : false;
    assert.equal(
      loginFormPresent,
      true,
      'and we can still see the login form'
    );
  });
});

test('visiting /login', function(assert) {
  visit('/auth/login');

  andThen(function() {
    assert.equal(currentURL(), '/auth/login');
  });
});
*/
