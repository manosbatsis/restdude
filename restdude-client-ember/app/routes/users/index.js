import Ember from 'ember';
import Base from '../application';

import SaveModelMixin from 'super-rentals/mixins/users/save-model-mixin';
import AuthenticatedRouteMixin from 'ember-simple-auth/mixins/authenticated-route-mixin';



export default Base.extend(SaveModelMixin,AuthenticatedRouteMixin, {

  modelTitleProperty: 'username',
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
