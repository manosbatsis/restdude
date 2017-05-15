import Ember from 'ember';
import config from './config/environment';

const Router = Ember.Router.extend({
  location: config.locationType,
  rootURL: config.rootURL
});

Router.map(function() {
  this.route('about');
  this.route('contact');
   this.route('not-found', { path: '/*path' });
  this.route('server-error', { path: '/auth/500' });
  this.route('login', { path: '/auth/login' });
  this.route('register', { path: '/auth/register' });
  this.route('user', function() {
    this.route('api/user', { path: '/:user_id' });
  });
  this.route('forgot-password');
  this.route('protected');
  this.route('profile', { path: '/accounts/profile' });

});

export default Router;
