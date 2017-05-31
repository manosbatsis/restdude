import Ember from 'ember';
import { module, test } from 'qunit';
import startApp from '../helpers/start-app';

var application;
var originalConfirm;
var confirmCalledWith;

module('Acceptance: Website', {
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

test('visiting /websites without data', function(assert) {
  visit('/websites');

  andThen(function() {
    assert.equal(currentPath(), 'websites.index');
    assert.equal(find('#blankslate').text().trim(), 'No Websites found');
  });
});

test('visiting /websites with data', function(assert) {
  server.create('website');
  visit('/websites');

  andThen(function() {
    assert.equal(currentPath(), 'websites.index');
    assert.equal(find('#blankslate').length, 0);
    assert.equal(find('table tbody tr').length, 1);
  });
});

test('create a new website', function(assert) {
  visit('/websites');
  click('a:contains(New Website)');

  andThen(function() {
    assert.equal(currentPath(), 'websites.new');

    fillIn('label:contains(Name) input', 'MyString');
    fillIn('label:contains(Title) input', 'MyString');

    click('input:submit');
  });

  andThen(function() {
    assert.equal(find('#blankslate').length, 0);
    assert.equal(find('table tbody tr').length, 1);
  });
});

test('update an existing website', function(assert) {
  server.create('website');
  visit('/websites');
  click('a:contains(Edit)');

  andThen(function() {
    assert.equal(currentPath(), 'websites.edit');

    fillIn('label:contains(Name) input', 'MyString');
    fillIn('label:contains(Title) input', 'MyString');

    click('input:submit');
  });

  andThen(function() {
    assert.equal(find('#blankslate').length, 0);
    assert.equal(find('table tbody tr').length, 1);
  });
});

test('show an existing website', function(assert) {
  server.create('website');
  visit('/websites');
  click('a:contains(Show)');

  andThen(function() {
    assert.equal(currentPath(), 'websites.show');

    assert.equal(find('p strong:contains(Name:)').next().text(), 'MyString');
    assert.equal(find('p strong:contains(Title:)').next().text(), 'MyString');
  });
});

test('delete a website', function(assert) {
  server.create('website');
  visit('/websites');
  click('a:contains(Remove)');

  andThen(function() {
    assert.equal(currentPath(), 'websites.index');
    assert.deepEqual(confirmCalledWith, ['Are you sure?']);
    assert.equal(find('#blankslate').length, 1);
  });
});
