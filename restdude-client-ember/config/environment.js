/* jshint node: true */

module.exports = function(environment) {
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
    }
  };

  ENV['ember-simple-auth'] = {
  authorizer: 'authorizer:token'
};
ENV['ember-simple-auth-token'] = {
  serverTokenEndpoint: 'http://localhost:8080/restdude/api/auth/jwt/access',
  identificationField: 'username',
  passwordField: 'password',
  tokenPropertyName: 'token',
  refreshTokenPropertyName: 'refresh_token',
  authorizationPrefix: 'Bearer ',
  authorizationHeaderName: 'Authorization',
  headers: {},
  refreshAccessTokens: true,
  serverTokenRefreshEndpoint: 'http://localhost:8080/restdude/api/auth/jwt/refresh',
  tokenExpireName: 'exp',
  refreshLeeway: 0,
  //serverTokenEndpoint: 'http://localhost:8080/api/auth/jwt/access',
};


  if (environment === 'development') {
    // ENV.APP.LOG_RESOLVER = true;
    // ENV.APP.LOG_ACTIVE_GENERATION = true;
    // ENV.APP.LOG_TRANSITIONS = true;
    // ENV.APP.LOG_TRANSITIONS_INTERNAL = true;
    // ENV.APP.LOG_VIEW_LOOKUPS = true;
  }

  if (environment === 'test') {
    // Testem prefers this...
    ENV.locationType = 'none';

    // keep test console output quieter
    ENV.APP.LOG_ACTIVE_GENERATION = false;
    ENV.APP.LOG_VIEW_LOOKUPS = false;

    ENV.APP.rootElement = '#ember-testing';
  }

  if (environment === 'production') {

  }

  return ENV;
};
