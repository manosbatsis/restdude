// app/routes/user.js
import Ember from 'ember';
import AuthenticatedRouteMixin from 'ember-simple-auth/mixins/authenticated-route-mixin';

export default Ember.Route.extend(AuthenticatedRouteMixin,{
      queryParams: {
        search: {
          refreshModel: true
        }
      },
        setupController: function(controller, models){
          return controller.setProperties(models);
        },
        model(){
            return this.store.findAll('user');
            }


});
/*results(){
  return this.store.query('post', {find: this.get('term')});
}.property('term')*/
