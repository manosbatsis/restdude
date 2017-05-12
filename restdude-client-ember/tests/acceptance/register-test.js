import { test } from 'qunit';
import moduleForAcceptance from 'super-rentals/tests/helpers/module-for-acceptance';
//import signupPage from '../pages/register';


moduleForAcceptance('Acceptance | Signup');


/*test('Successful signup', function(assert) {
  assert.expect(6);

  visit('/auth/register');

  andThen(function() {
    fillIn('#username', 'username');
    fillIn('#email', 'restdude@restdude.com');
    fillIn('#password', 'password');
    fillIn('#passwordConfirmation', 'password');
    click('#register-btn');

    //signupPage.form.username('username').email('email@example.com').password('password').save();
  });

  let signUpDone = assert.async();

  server.post('/user/', (db, request) => {
    let params = JSON.parse(request.requestBody).data.attributes;
    params.state = 'signed_up';

    assert.equal(params.username, 'username');
    assert.equal(params.email, 'restdude@restdude.com');
    assert.equal(params.password, 'password');
    assert.equal(params.passwordConfirmation, 'password');

    signUpDone();

    return db.create('user', params);
  });

  let signInDone = assert.async();

  server.post('/token-auth', function(db, request) {
    let json = request.requestBody;

    assert.ok(json.indexOf('"username":"admin"') > -1);
    assert.ok(json.indexOf('"password":"password"') > -1);

    signInDone();


    return {
      token: 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJwYXNzd29yZCI6InBhc3N3b3JkIiwidXNlcm5hbWUiOiJqb3NoQGNvZGVybHkuY29tIiwidXNlcl9pZCI6MSwiZXhwIjo3MjAwfQ.QVDyAznECIWL6DjDs9iPezvMmoPuzDqAl4bQ6CY-fCQ',
      user_id: 1
    };
  });

  andThen(() => {
    assert.equal(currentURL(), '/about');
  });
});
*/
test('Failed signup due to invalid data stays on same page', function(assert) {
  assert.expect(1);

  visit('/auth/register');

  //andThen(() => signupPage.form.save());
  fillIn('#username', '');
  fillIn('#email', '');
  fillIn('#password', '');
  fillIn('#passwordConfirmation', '');
  click('#register-btn');

  andThen(() => assert.equal(currentURL(), '/signup'));
});
