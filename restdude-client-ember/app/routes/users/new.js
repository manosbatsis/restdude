import Ember from 'ember';
import Base from '../application';

import SaveModelMixin from 'super-rentals/mixins/users/save-model-mixin';


export default Base.extend(SaveModelMixin, {
  model: function() {
    return this.store.createRecord('user');
  }
});
