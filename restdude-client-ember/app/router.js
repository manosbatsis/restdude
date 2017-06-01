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



  this.route('auth', function() {
    this.route('login');
    this.route('register');
    this.route('forgot-password');
  });

  this.route('photo', { path: '/photo/:photo_id' }, function() {
    this.route('comment', { path: '/comment/:comment_id' });
  });

  this.route('spaces', function() {
    this.route('new');
    this.route('space', {path: ':space_id'}, function() {
      this.route('settings');
      this.route('members');
      this.route('billing');
      this.route('membership-requests');
      this.route('apps');
      this.route('websites');
    });
    this.route('joined');
    this.route('owned');
  });
  this.route('context-membership');
  this.route('context-membership-request');
  this.route('website');
  this.route('hosts', function() {
    this.route('new');

    this.route('edit', {
      path: ':host_id/edit'
    });

    this.route('show', {
      path: ':host_id'
    });
  });
  this.route('space-apps', function() {
    this.route('new');

    this.route('edit', {
      path: ':space-app_id/edit'
    });

    this.route('show', {
      path: ':space-app_id'
    });
  });
  this.route('websites', function() {
    this.route('new');

    this.route('edit', {
      path: ':website_id/edit'
    });

    this.route('show', {
      path: ':website_id'
    });
  });
});

export default Router;
