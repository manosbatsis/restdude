import Ember from 'ember';

export default Ember.Route.extend({
  redirect: function () {
    var url = this.router.location.formatURL('/auth/login');
    if (window.location.pathname === url) {
      this.transitionTo('/auth/login');
    }
  }
});