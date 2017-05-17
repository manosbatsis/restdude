import {moduleForComponent, test} from "ember-qunit";
import hbs from "htmlbars-inline-precompile";

moduleForComponent('annotate-it', 'Integration | Component | annotate it', {
  integration: true
});

test('it renders', function(assert) {

  // Set any properties with this.set('myProperty', 'value');
  // Handle any actions with this.on('myAction', function(val) { ... });

  this.render(hbs`{{annotate-it}}`);

  assert.equal(this.$().text().trim(), '');

  // Template block usage:
  this.render(hbs`
    {{#annotate-it}}
      template block text
    {{/annotate-it}}
  `);

  assert.equal(this.$().text().trim(), 'template block text');
});
