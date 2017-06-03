import Ember from 'ember';
import { module, test } from 'qunit';
import startApp from '../helpers/start-app';

var application;
var originalConfirm;
var confirmCalledWith;

module('Acceptance: Issue-comment', {
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

test('visiting /issue-comments without data', function(assert) {
  visit('/issue-comments');

  andThen(function() {
    assert.equal(currentPath(), 'issue-comments.index');
    assert.equal(find('#blankslate').text().trim(), 'No Issue-comments found');
  });
});

test('visiting /issue-comments with data', function(assert) {
  server.create('issue-comment');
  visit('/issue-comments');

  andThen(function() {
    assert.equal(currentPath(), 'issue-comments.index');
    assert.equal(find('#blankslate').length, 0);
    assert.equal(find('table tbody tr').length, 1);
  });
});

test('create a new issue-comment', function(assert) {
  visit('/issue-comments');
  click('a:contains(New Issue-comment)');

  andThen(function() {
    assert.equal(currentPath(), 'issue-comments.new');

    fillIn('label:contains(Name) input', 'MyString');
    fillIn('label:contains(Title) input', 'MyString');
    fillIn('label:contains(Description) input', 'MyString');
    fillIn('label:contains(Visibility) input', 'MyString');

    click('input:submit');
  });

  andThen(function() {
    assert.equal(find('#blankslate').length, 0);
    assert.equal(find('table tbody tr').length, 1);
  });
});

test('update an existing issue-comment', function(assert) {
  server.create('issue-comment');
  visit('/issue-comments');
  click('a:contains(Edit)');

  andThen(function() {
    assert.equal(currentPath(), 'issue-comments.edit');

    fillIn('label:contains(Name) input', 'MyString');
    fillIn('label:contains(Title) input', 'MyString');
    fillIn('label:contains(Description) input', 'MyString');
    fillIn('label:contains(Visibility) input', 'MyString');

    click('input:submit');
  });

  andThen(function() {
    assert.equal(find('#blankslate').length, 0);
    assert.equal(find('table tbody tr').length, 1);
  });
});

test('show an existing issue-comment', function(assert) {
  server.create('issue-comment');
  visit('/issue-comments');
  click('a:contains(Show)');

  andThen(function() {
    assert.equal(currentPath(), 'issue-comments.show');

    assert.equal(find('p strong:contains(Name:)').next().text(), 'MyString');
    assert.equal(find('p strong:contains(Title:)').next().text(), 'MyString');
    assert.equal(find('p strong:contains(Description:)').next().text(), 'MyString');
    assert.equal(find('p strong:contains(Visibility:)').next().text(), 'MyString');
  });
});

test('delete a issue-comment', function(assert) {
  server.create('issue-comment');
  visit('/issue-comments');
  click('a:contains(Remove)');

  andThen(function() {
    assert.equal(currentPath(), 'issue-comments.index');
    assert.deepEqual(confirmCalledWith, ['Are you sure?']);
    assert.equal(find('#blankslate').length, 1);
  });
});
