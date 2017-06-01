import Ember from 'ember';

export default Ember.Route.extend({
  sessionAccount: service('session-account'),
  model: function() {
    var userId =  session.account.id;
    return this.store.query('space', {'owner': userId});
  }

});


/*import BaseAuthenticated  from '../base-authenticated';
import SaveModelMixin from 'super-rentals/mixins/spaces/save-model-mixin';

export default BaseAuthenticated.extend(SaveModelMixin, {
  model: function() {
    var
    return this.store.createRecord('space');
  }
});

 query gia ton owner == current user
 */
