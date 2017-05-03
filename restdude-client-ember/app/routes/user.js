// app/routes/user.js
import Ember from 'ember';
import AuthenticatedRouteMixin from 'ember-simple-auth/mixins/authenticated-route-mixin';

    export default Ember.Route.extend(AuthenticatedRouteMixin,{
    model(){
        return this.store.findAll('user');
        }
});


/*import Ember from 'ember';

export default Ember.Route.extend({
});*/
