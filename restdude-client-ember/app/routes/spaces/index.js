import BaseAuthenticated  from '../base-authenticated';

export default BaseAuthenticated.extend( {
  actions: {
    remove: function(model) {
      if(confirm('Are you sure?')) {
        model.destroyRecord();
      }
    },
    show: function(model) {
      this.transitionTo('spaces.show', model);
    },
    edit: function(model) {
      this.transitionTo('spaces.edit', model);
    },
    settings: function(model) {
      this.transitionTo('spaces.settings', model);
    }
  },
  model: function() {
    return this.store.findAll('space');
  }
});
