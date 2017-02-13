import Ember from 'ember';

export default Ember.Route.extend({
  redirect: function () {
   // var url = this.router.location.formatURL('/auth/500');
    if (window.location.pathname === '/auth/500') // need change !== url
  {
      this.transitionTo('/auth/500');
    }
  }
});