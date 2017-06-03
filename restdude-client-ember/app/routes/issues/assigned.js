import Ember from 'ember';

export default Ember.Route.extend({
  model() {
    return [{
      username: 'admin',
      description: 'one issue one issue one issue one issue '
    }, {
      username: 'alex',
      description: 'two issue two issue two issue two issue '
    }, {
      username: 'tom',
      description: 'three issue three issue three issue three issue'
    }];
  }
});
