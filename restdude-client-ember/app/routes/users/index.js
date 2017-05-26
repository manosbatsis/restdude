import Ember from 'ember';
import Base from '../application';

import SaveModelMixin from 'super-rentals/mixins/users/save-model-mixin';


export default Base.extend(SaveModelMixin, {

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
