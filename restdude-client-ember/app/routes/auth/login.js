import Ember from "ember";
import UnauthenticatedRouteMixin from "ember-simple-auth/mixins/unauthenticated-route-mixin";

export default Ember.Route.extend(UnauthenticatedRouteMixin, {

//export default Ember.Route.extend({
  /*redirect: function () {
    var url = this.router.location.formatURL('auth.login');
    if (window.location.pathname === url) {
      this.transitionTo('auth.login');
    }
  },*/

  beforeModel: function(){
    let controller = this.controllerFor(this.routeName);
    controller.set('errorMessage', null);
  }

});
