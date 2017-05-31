import Ember from 'ember';
import { module, test } from 'qunit';
import startApp from '../helpers/start-app';

var application;
var originalConfirm;
var confirmCalledWith;

module('Acceptance: Host', {
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

test('visiting /hosts without data', function(assert) {
  visit('/hosts');

  andThen(function() {
    assert.equal(currentPath(), 'hosts.index');
    assert.equal(find('#blankslate').text().trim(), 'No Hosts found');
  });
});

test('visiting /hosts with data', function(assert) {
  server.create('host');
  visit('/hosts');

  andThen(function() {
    assert.equal(currentPath(), 'hosts.index');
    assert.equal(find('#blankslate').length, 0);
    assert.equal(find('table tbody tr').length, 1);
  });
});

test('create a new host', function(assert) {
  visit('/hosts');
  click('a:contains(New Host)');

  andThen(function() {
    assert.equal(currentPath(), 'hosts.new');

    fillIn('label:contains(Name) input', 'MyString');
    fillIn('label:contains(Description) input', 'MyString');

    click('input:submit');
  });

  andThen(function() {
    assert.equal(find('#blankslate').length, 0);
    assert.equal(find('table tbody tr').length, 1);
  });
});

test('update an existing host', function(assert) {
  server.create('host');
  visit('/hosts');
  click('a:contains(Edit)');

  andThen(function() {
    assert.equal(currentPath(), 'hosts.edit');

    fillIn('label:contains(Name) input', 'MyString');
    fillIn('label:contains(Description) input', 'MyString');

    click('input:submit');
  });

  andThen(function() {
    assert.equal(find('#blankslate').length, 0);
    assert.equal(find('table tbody tr').length, 1);
  });
});

test('show an existing host', function(assert) {
  server.create('host');
  visit('/hosts');
  click('a:contains(Show)');

  andThen(function() {
    assert.equal(currentPath(), 'hosts.show');

    assert.equal(find('p strong:contains(Name:)').next().text(), 'MyString');
    assert.equal(find('p strong:contains(Description:)').next().text(), 'MyString');
  });
});

test('delete a host', function(assert) {
  server.create('host');
  visit('/hosts');
  click('a:contains(Remove)');

  andThen(function() {
    assert.equal(currentPath(), 'hosts.index');
    assert.deepEqual(confirmCalledWith, ['Are you sure?']);
    assert.equal(find('#blankslate').length, 1);
  });
});
