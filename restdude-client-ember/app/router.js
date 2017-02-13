import Ember from 'ember';
import config from './config/environment';

const Router = Ember.Router.extend({
  location: config.locationType,
  rootURL: config.rootURL
});

Router.map(function() {
  this.route('about');
  this.route('contact');
  this.route('rentals', function() {
    this.route('show', { path: '/:rental_id' });
  });
  this.route('not-found', { path: '/*path' });
  this.route('server-error');
  this.route('login');
  this.route('register');
  this.route('user');
  this.route('forgot-password');
  this.route('protected');
});

export default Router;
