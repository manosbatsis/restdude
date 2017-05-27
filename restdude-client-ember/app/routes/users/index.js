import BaseAuthenticated  from '../base-authenticated';

export default BaseAuthenticated.extend( {
  actions: {
    remove: function(model) {
      if(confirm('Are you sure?')) {
        model.destroyRecord();
      }
    }
  },
  model: function() {
    return this.store.findAll('user');
  }
});
