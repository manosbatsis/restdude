import Ember from 'ember';

export default Ember.Route.extend({

  model: function() {
    var spaceId = this.paramsFor('spaces.space').space_id;
    return this.store.query('context-membership', {'context': spaceId});
  }
});
