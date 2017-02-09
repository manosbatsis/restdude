import Ember from 'ember';

export default Ember.Route.extend({
  redirect: function () {
    var url = this.router.location.formatURL('/error/404');
    if (window.location.pathname !== url) {
      this.transitionTo('/error/404');
    }
  }
});