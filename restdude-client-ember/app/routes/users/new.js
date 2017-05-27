import Ember from 'ember';
import Base from '../application';

import SaveModelMixin from 'super-rentals/mixins/users/save-model-mixin';
import AuthenticatedRouteMixin from 'ember-simple-auth/mixins/authenticated-route-mixin';



export default Base.extend(SaveModelMixin,AuthenticatedRouteMixin, {
  model: function() {
    return this.store.createRecord('user');
  }
});
