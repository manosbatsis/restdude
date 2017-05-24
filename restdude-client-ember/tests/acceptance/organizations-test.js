import Ember from 'ember';
import { module, test } from 'qunit';
import startApp from '../helpers/start-app';

var application;
var originalConfirm;
var confirmCalledWith;

module('Acceptance: Organization', {
  beforeEach: function() {
    application = startApp();
    originalConfirm = window.confirm;
    window.confirm = function() {
      confirmCalledWith = [].slice.call(arguments);
      return true;
    };
  },
  afterEach: function() {
    Ember.run(application, 'destroy');
    window.confirm = originalConfirm;
    confirmCalledWith = null;
  }
});

test('visiting /organizations without data', function(assert) {
  visit('/organizations');

  andThen(function() {
    assert.equal(currentPath(), 'organizations.index');
    assert.equal(find('#blankslate').text().trim(), 'No Organizations found');
  });
});

test('visiting /organizations with data', function(assert) {
  server.create('organization');
  visit('/organizations');

  andThen(function() {
    assert.equal(currentPath(), 'organizations.index');
    assert.equal(find('#blankslate').length, 0);
    assert.equal(find('table tbody tr').length, 1);
  });
});

test('create a new organization', function(assert) {
  visit('/organizations');
  click('a:contains(New Organization)');

  andThen(function() {
    assert.equal(currentPath(), 'organizations.new');

    fillIn('label:contains(Name) input', 'MyString');
    fillIn('label:contains(Legalname) input', 'MyString');
    fillIn('label:contains(Email) input', 'MyString');

    click('input:submit');
  });

  andThen(function() {
    assert.equal(find('#blankslate').length, 0);
    assert.equal(find('table tbody tr').length, 1);
  });
});

test('update an existing organization', function(assert) {
  server.create('organization');
  visit('/organizations');
  click('a:contains(Edit)');

  andThen(function() {
    assert.equal(currentPath(), 'organizations.edit');

    fillIn('label:contains(Name) input', 'MyString');
    fillIn('label:contains(Legalname) input', 'MyString');
    fillIn('label:contains(Email) input', 'MyString');

    click('input:submit');
  });

  andThen(function() {
    assert.equal(find('#blankslate').length, 0);
    assert.equal(find('table tbody tr').length, 1);
  });
});

test('show an existing organization', function(assert) {
  server.create('organization');
  visit('/organizations');
  click('a:contains(Show)');

  andThen(function() {
    assert.equal(currentPath(), 'organizations.show');

    assert.equal(find('p strong:contains(Name:)').next().text(), 'MyString');
    assert.equal(find('p strong:contains(Legalname:)').next().text(), 'MyString');
    assert.equal(find('p strong:contains(Email:)').next().text(), 'MyString');
  });
});

test('delete a organization', function(assert) {
  server.create('organization');
  visit('/organizations');
  click('a:contains(Remove)');

  andThen(function() {
    assert.equal(currentPath(), 'organizations.index');
    assert.deepEqual(confirmCalledWith, ['Are you sure?']);
    assert.equal(find('#blankslate').length, 1);
  });
});
