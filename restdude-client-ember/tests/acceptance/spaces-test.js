import Ember from 'ember';
import { module, test } from 'qunit';
import startApp from '../helpers/start-app';

var application;
var originalConfirm;
var confirmCalledWith;

module('Acceptance: Space', {
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

test('visiting /spaces without data', function(assert) {
  visit('/spaces');

  andThen(function() {
    assert.equal(currentPath(), 'spaces.index');
    assert.equal(find('#blankslate').text().trim(), 'No Spaces found');
  });
});

test('visiting /spaces with data', function(assert) {
  server.create('space');
  visit('/spaces');

  andThen(function() {
    assert.equal(currentPath(), 'spaces.index');
    assert.equal(find('#blankslate').length, 0);
    assert.equal(find('table tbody tr').length, 1);
  });
});

test('create a new space', function(assert) {
  visit('/spaces');
  click('a:contains(New Space)');

  andThen(function() {
    assert.equal(currentPath(), 'spaces.new');

    fillIn('label:contains(Name) input', 'MyString');
    fillIn('label:contains(Title) input', 'MyString');
    fillIn('label:contains(Description) input', 'MyString');
    fillIn('label:contains(Avatarurl) input', 'MyString');
    fillIn('label:contains(Bannerurl) input', 'MyString');
    fillIn('label:contains(Visibility) input', 'MyString');

    click('input:submit');
  });

  andThen(function() {
    assert.equal(find('#blankslate').length, 0);
    assert.equal(find('table tbody tr').length, 1);
  });
});

test('update an existing space', function(assert) {
  server.create('space');
  visit('/spaces');
  click('a:contains(Edit)');

  andThen(function() {
    assert.equal(currentPath(), 'spaces.edit');

    fillIn('label:contains(Name) input', 'MyString');
    fillIn('label:contains(Title) input', 'MyString');
    fillIn('label:contains(Description) input', 'MyString');
    fillIn('label:contains(Avatarurl) input', 'MyString');
    fillIn('label:contains(Bannerurl) input', 'MyString');
    fillIn('label:contains(Visibility) input', 'MyString');

    click('input:submit');
  });

  andThen(function() {
    assert.equal(find('#blankslate').length, 0);
    assert.equal(find('table tbody tr').length, 1);
  });
});

test('show an existing space', function(assert) {
  server.create('space');
  visit('/spaces');
  click('a:contains(Show)');

  andThen(function() {
    assert.equal(currentPath(), 'spaces.show');

    assert.equal(find('p strong:contains(Name:)').next().text(), 'MyString');
    assert.equal(find('p strong:contains(Title:)').next().text(), 'MyString');
    assert.equal(find('p strong:contains(Description:)').next().text(), 'MyString');
    assert.equal(find('p strong:contains(Avatarurl:)').next().text(), 'MyString');
    assert.equal(find('p strong:contains(Bannerurl:)').next().text(), 'MyString');
    assert.equal(find('p strong:contains(Visibility:)').next().text(), 'MyString');
  });
});

test('delete a space', function(assert) {
  server.create('space');
  visit('/spaces');
  click('a:contains(Remove)');

  andThen(function() {
    assert.equal(currentPath(), 'spaces.index');
    assert.deepEqual(confirmCalledWith, ['Are you sure?']);
    assert.equal(find('#blankslate').length, 1);
  });
});
