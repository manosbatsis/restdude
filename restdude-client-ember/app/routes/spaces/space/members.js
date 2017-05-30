import Ember from 'ember';

export default Ember.Route.extend({

  model: function() {
    return this.store.findAll('context-membership');
  }
  /*queryParams: {
     space_id: {
       refreshModel: true
     }
   },

  model(params) {
    return this.get('store').findRecord('context-membership', params.space_id);
}*/
});
