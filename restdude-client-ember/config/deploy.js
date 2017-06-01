  /* jshint node: true */

module.exports = function(deployTarget) {
  deployTarget || (deployTarget = 'dev');
  ENV.build = {
  };
  var ENV = {
    // include other plugin configuration that applies to all deploy targets here
    build: {
      environment: 'development'
    }
  };


  if (deployTarget === 'embed') {
    ENV.build.environment = 'embed';
    // configure other plugins for staging deploy target here


    console.log("embedded: "  );
    ENV.APP.rootElement = '#restdude-embedded';
    ENV.locationType = 'none';
  }


  // Note: if you need to build some configuration asynchronously, you can return
  // a promise that resolves with the ENV object instead of returning the
  // ENV object synchronously.
  return ENV;
};
