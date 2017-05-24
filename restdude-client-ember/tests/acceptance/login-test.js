import {test} from "qunit";
import moduleForAcceptance from "super-rentals/tests/helpers/module-for-acceptance";
import Ember from "ember";
// These test helps are included with ESA, and
// are absolutely critical for sane testing.
import {authenticateSession, currentSession, invalidateSession} from "super-rentals/tests/helpers/ember-simple-auth";

moduleForAcceptance('Acceptance | login');


moduleForAcceptance('Acceptance | login ');


test('If a user is not logged in, they see a login form', function (assert) {
  invalidateSession(this.application);
  visit('/auth/login');

  andThen(function () {
    const loginFormPresent = find('#loginForm').length > 0 ? true : false;
    assert.equal(loginFormPresent, true);
  });
});

test('if a user is logged in, they see a logout button', function (assert) {
  authenticateSession(this.application);
  visit('/about');

  andThen(function () {
    assert.equal(currentURL(), '/about');
    const logoutBtnPresent = find('.logoutBtn').length > 0 ? true : false;

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

test('user can logout', function (assert) {
  authenticateSession(this.application);
  visit('/');
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

test('user can login', function (assert) {
  invalidateSession(this.application);
  visit('/auth/login');

  fillIn('#identification', 'admin');
  fillIn('#password', 'admin');
  click('#login-btn');

  andThen(() => {
    const sesh = currentSession(this.application);
    const isAuthed = Ember.get(sesh, 'isAuthenticated');
    assert.equal(
      isAuthed,
      true,
      'after a user submits good creds to login form, they are logged in'
    );

    const loginFormPresent = find('#loginForm').length > 0 ? true : false;
    assert.equal(
      loginFormPresent,
      false,
      'after we login, the login form disappears'
    );
  });
});

test('If a user puts in the wrong login credentials, they see a login error', function (assert) {
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

    /*const loginFormPresent = find('#loginForm').length > 0 ? true : false;
    assert.equal(
      loginFormPresent,
      true,
      'and we can still see the login form'
    );*/
  });
});
/*test('When authenticated, redirects from login', function(assert) {
 assert.expect(1);

 let user = server.create('user');
 authenticateSession(this.application, { user_id: user.id });

 visit('/auth/login');

 andThen(() => {
 assert.equal(currentURL(), '/user');
 });
 });

 test('When authenticated, redirects from signup', function(assert) {
 assert.expect(1);

 let user = server.create('user');
 authenticateSession(this.application, { user_id: user.id });

 visit('/auth/register');

 andThen(() => {
 assert.equal(currentURL(), '/projects');
 });
 });
 */
test('visiting /login', function (assert) {
  visit('/auth/login');

  andThen(function () {
    assert.equal(currentURL(), '/auth/login');
  });
});
