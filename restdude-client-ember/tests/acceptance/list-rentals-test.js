import { test } from 'qunit';
import moduleForAcceptance from 'super-rentals/tests/helpers/module-for-acceptance';
import Ember from 'ember';

let StubMapsService = Ember.Service.extend({
  getMapElement() {
    return document.createElement('div');
  }
});

moduleForAcceptance('Acceptance | list rentals', {
  beforeEach() {
    this.application.register('service:mockMaps', StubMapsService);
    this.application.inject('component:location-map', 'maps', 'service:mockMaps');
  }
});

test('should redirect to rentals route', function (assert) {
  visit('/');
  andThen(function() {
    assert.equal(currentURL(), '/rentals', 'should redirect automatically');
  });
});


test('should link to about page', function (assert) {
  visit('/');
  click('a:contains("About")');
  andThen(function () {
    assert.equal(currentURL(), '/about', 'should navigate to about');
  });
});

test('should link to contacts page', function (assert) {
  visit('/');
  click('a:contains("Contact")');
  andThen(function () {
    assert.equal(currentURL(), '/contact', 'should navigate to contact');
  });
});

test('should link to forgot-password page', function (assert) {
  visit('/auth/login');
  click('a:contains("Forgot password")');
  andThen(function () {
    assert.equal(currentURL(), '/forgot-password', 'should navigate to forgot-password');
  });
});

test('should link to not-found page', function (assert) {
  visit('/*path');
  //click('a:contains("Forgot Password")');
  andThen(function () {
    assert.equal(currentURL(), '/error/404', 'should navigate to not-found');
  });
});

test('should link to server-error page', function (assert) {
  visit('/auth/500');
  //click('a:contains("Forgot Password")');
  andThen(function () {
    assert.equal(currentURL(), '/auth/500', 'should navigate to not-found');
  });
});

test('should link to login page', function (assert) {
  visit('/auth/login');
  click('a:contains("Login")');
  andThen(function () {
    assert.equal(currentURL(), '/auth/login', 'should navigate to not-found');
  });
});

test('should link to register page', function (assert) {
  visit('/auth/login');
  click('a:contains("Register Now!")');
  andThen(function () {
    assert.equal(currentURL(), '/auth/register', 'should navigate to not-found');
  });
});

test('should link to profile page', function (assert) {
  visit('/accounts/profile');
  //click('a:contains("Register")');
  andThen(function () {
    assert.equal(currentURL(), '/accounts/profile', 'should navigate to not-found');
  });
});

test('should initially list 3 rentals', function (assert) {
  visit('/');
  andThen(function () {
    assert.equal(find('.results .listing').length, 3, 'should display 3 listings');
  });
});

test('should list 1 rental when filtering by Seattle', function (assert) {
  visit('/');
  fillIn('.list-filter input', 'seattle');
  keyEvent('.list-filter input', 'keyup', 69);
  andThen(function () {
    assert.equal(find('.results .listing').length, 1, 'should display 1 listing');
    assert.equal(find('.listing .location:contains("Seattle")').length, 1, 'should contain 1 listing with location Seattle');
  });
});

test('should show details for a specific rental', function (assert) {
  visit('/rentals');
  click('a:contains("Grand Old Mansion")');
  andThen(function() {
    assert.equal(currentURL(), '/rentals/grand-old-mansion', "should navigate to show route");
    assert.equal(find('.show-listing h2').text(), "Grand Old Mansion", 'should list rental title');
    assert.equal(find('.description').length, 1, 'should list a description of the property');
  });
});
