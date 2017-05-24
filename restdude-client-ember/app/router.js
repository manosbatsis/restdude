import Ember from "ember";
import config from "./config/environment";

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
  this.route('user');
  this.route('forgot-password');
  this.route('protected');
  this.route('profile', { path: '/accounts/profile' });

  this.route('application-embed');
  this.route('account');
  this.route('confirmationEmail');
  this.route('organizations', function() {
    this.route('new');

    this.route('edit', {
      path: ':organization_id/edit'
    });

    this.route('show', {
      path: ':organization_id'
    });
  });
  this.route('users', function() {
    this.route('new');

    this.route('edit', {
      path: ':user_id/edit'
    });

    this.route('show', {
      path: ':user_id'
    });
  });
  this.route('home');
  this.route('accountandprofile');
  this.route('configureapps');
  this.route('statistics');
  this.route('systemsettings');
  this.route('clientplans');
  this.route('billing');
});

export default Router;
