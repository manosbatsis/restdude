import Ember from 'ember';
import { module, test } from 'qunit';
import startApp from '../helpers/start-app';

var application;
var originalConfirm;
var confirmCalledWith;

module('Acceptance: Space-app', {
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

test('visiting /space-apps without data', function(assert) {
  visit('/space-apps');

  andThen(function() {
    assert.equal(currentPath(), 'space-apps.index');
    assert.equal(find('#blankslate').text().trim(), 'No Space-apps found');
  });
});

test('visiting /space-apps with data', function(assert) {
  server.create('space-app');
  visit('/space-apps');

  andThen(function() {
    assert.equal(currentPath(), 'space-apps.index');
    assert.equal(find('#blankslate').length, 0);
    assert.equal(find('table tbody tr').length, 1);
  });
});

test('create a new space-app', function(assert) {
  visit('/space-apps');
  click('a:contains(New Space-app)');

  andThen(function() {
    assert.equal(currentPath(), 'space-apps.new');

    fillIn('label:contains(Name) input', 'MyString');
    fillIn('label:contains(Description) input', 'MyString');

    click('input:submit');
  });

  andThen(function() {
    assert.equal(find('#blankslate').length, 0);
    assert.equal(find('table tbody tr').length, 1);
  });
});

test('update an existing space-app', function(assert) {
  server.create('space-app');
  visit('/space-apps');
  click('a:contains(Edit)');

  andThen(function() {
    assert.equal(currentPath(), 'space-apps.edit');

    fillIn('label:contains(Name) input', 'MyString');
    fillIn('label:contains(Description) input', 'MyString');

    click('input:submit');
  });

  andThen(function() {
    assert.equal(find('#blankslate').length, 0);
    assert.equal(find('table tbody tr').length, 1);
  });
});

test('show an existing space-app', function(assert) {
  server.create('space-app');
  visit('/space-apps');
  click('a:contains(Show)');

  andThen(function() {
    assert.equal(currentPath(), 'space-apps.show');

    assert.equal(find('p strong:contains(Name:)').next().text(), 'MyString');
    assert.equal(find('p strong:contains(Description:)').next().text(), 'MyString');
  });
});

test('delete a space-app', function(assert) {
  server.create('space-app');
  visit('/space-apps');
  click('a:contains(Remove)');

  andThen(function() {
    assert.equal(currentPath(), 'space-apps.index');
    assert.deepEqual(confirmCalledWith, ['Are you sure?']);
    assert.equal(find('#blankslate').length, 1);
  });
});
