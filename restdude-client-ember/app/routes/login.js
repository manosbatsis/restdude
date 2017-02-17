import Ember from 'ember';

export default Ember.Route.extend({
  redirect: function () {
    var url = this.router.location.formatURL('/auth/login');
    if (window.location.pathname === url) {
      this.transitionTo('/auth/login');
    }
  }
});

export default Ember.Route.extend({
actions: {
  signInViaTwitter: function() {
    var route = this;
    this.get('session').open('twitter').then(function() {
      route.transitionTo('index');
    }, function() {
      console.log('auth failed');
    });
  }
}
});

export default Ember.Route.extend({
actions: {
  signInViaFacebook: function() {
    var route = this;
    this.get('session').open('facebook').then(function() {
      route.transitionTo('index');
    }, function() {
      console.log('auth failed');
    });
  }
}
});