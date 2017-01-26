//template.test.js

suite('template', function() {

  test('exists', function() {
    assert.equal(typeof template, 'function');
  });

  test('variable', function() {
    var out = template('<%= name %>', { name: 'Bob' });
    assert.equal(out, 'Bob');
  });

  test('if statement', function() {
    var out = template('<% if (test) { %><%= name %><% } %>', { test: true, name: 'Bob' });
    assert.equal(out, 'Bob');
    out = template('<% if (test) { %><%= name %><% } %>', { test: false, name: 'Bob' });
    assert.equal(out, '');
  });

  test('for loop', function() {
    var out = template('<% for (var i = 0; i < names.length; i++) { %><%= names[i] %>, <% } %>', { names: ['Bob', 'Jane'] });
    assert.equal(out, 'Bob, Jane, ');
  });

  test('noConflict', function() {
    var t = template.noConflict();
    assert.equal(typeof template, 'undefined');
    //adding back for other tests
    window.template = t;
  });

  test('custom open/close tags', function() {
    template.options.openTag = '{{';
    template.options.closeTag = '}}';
    var out = template('{{= name }}', { name: 'Bob' });
    assert.equal(out, 'Bob');
  });

  test('custom tags with if statement', function() {
    template.options.openTag = '{{';
    template.options.closeTag = '}}';
    var out = template('{{ if (test) { }}{{= name }}{{ } }}', { test: true, name: 'Bob' });
    assert.equal(out, 'Bob');
    out = template('{{ if (test) { }}{{= name }}{{ } }}', { test: false, name: 'Bob' });
    assert.equal(out, '');
  });
});
