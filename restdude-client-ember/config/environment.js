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
      usingCors: true,
      corsWithCreds: true

    },
    contentSecurityPolicy: {
      'font-src': "'self' https://fonts.gstatic.com"
    },
    host: 'http://localhost:8080',
    namespace: 'noteblox/api/rest',
    namespaceAuth: 'noteblox/api/auth',
    namespaceConfirm: 'noteblox/api/auth/account',
    authorizer: 'authorizer:custom',
    // moment js
    moment: {
      // If null, undefined, or an empty string are passed as a date to any of the moment helpers
      // then you will get Invalid Date in the output. To avoid this issue globally, you can set
      // the option allowEmpty which all of the helpers respect and will result in nothing being
      // rendered instead of Invalid Date.
      allowEmpty: true, // default: false
      // To cherry-pick specific locale support into your application.
      // Full list of locales: https://github.com/moment/moment/tree/2.10.3/locale
      //includeLocales: ['gr'],
      // This will output _all_ locale scripts to assets/moment-locales
      // this option does not respect includeLocales
      // User locale is loaded by session-account service
      localeOutputPath: 'assets/moment-locales',
      // Timezone options:
      // 'all' - all years, all timezones
      // '2010-2020' - 2010-2020, all timezones
      // 'none' - no data, just timezone API
      includeTimezone: 'all'
    }
  };

  ENV['ember-simple-auth'] = {
    authorizer: ENV.authorizer,
    authenticationRoute: 'auth.login'
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

  else if (environment === 'production') {
    ENV.baseURL = '/project-name';
    ENV.locationType = 'hash';
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
