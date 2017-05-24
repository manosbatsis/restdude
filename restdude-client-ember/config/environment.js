/* jshint node: true */

module.exports = function (environment) {
  var ENV = {
    modulePrefix: 'super-rentals',
    environment: environment,
    rootURL: '/',
    locationType: 'auto',
    EmberENV: {
      FEATURES: {
        // Here you can enable experimental features on an ember canary build
        // e.g. 'with-controller': true
      },
      EXTEND_PROTOTYPES: {
        // Prevent Ember Data from overriding Date.parse
        Date: false
      }

    },

    APP: {
      // Here you can pass flags/options to your application instance
      // when it is created


    },
    contentSecurityPolicy: {
      'font-src': "'self' https://fonts.gstatic.com"
    },
    host: 'http://localhost:8080',
    namespace: 'restdude/api/rest',
    namespaceAuth: 'restdude/api/auth',
    namespaceConfirm: 'restdude/api/auth/account',
    authorizer: 'authorizer:custom',
  };

  ENV['ember-simple-auth'] = {
    authorizer: ENV.authorizer
    //  routeAfterAuthentication: '/'
  };

  if (environment === 'development') {

    //TODO:  disable mirage so that it won’t intercept Ember
    // requests to other servers
    //ENV['ember-cli-mirage'] = {enabled: false};


    // Debugging settings
    // ENV.APP.LOG_RESOLVER = true;
    // ENV.APP.LOG_RESOLVER = true;
    ENV.APP.LOG_ACTIVE_GENERATION = true;
    ENV.APP.LOG_TRANSITIONS = true;
    ENV.APP.LOG_TRANSITIONS_INTERNAL = true;
    ENV.APP.LOG_VIEW_LOOKUPS = true;
  }
  else if (environment === 'test') {
    // Testem prefers this...
    ENV.locationType = 'none';

    // keep test console output quieter
    ENV.APP.LOG_ACTIVE_GENERATION = false;
    ENV.APP.LOG_VIEW_LOOKUPS = false;

    ENV.APP.rootElement = '#ember-testing';
  }
  else if (environment === 'production') {

  }
  else{

    ENV.APP.rootElement = '#restdude-embedded, body';
    ENV.locationType = 'none';
  }


  return ENV;
};
