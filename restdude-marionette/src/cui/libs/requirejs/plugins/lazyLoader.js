define(['require'], function (require) {

  // Module Object
  var lazyLoader = {};

  // Create the function used to load scripts inline.
  lazyLoader.load = function(request, requestCb) {

    var processor = function(request, requestCb) {

      // Check to see if the requested library is already been defined in requirejs
      if (require.defined(request)) {

        // The item being requested already exists in requie. Just call its callback
        if (typeof(requestCb) === 'function') {
          requestCb();
        }

      } else {

          require([request], requestCb);

      }

    };

    // Check what type of request we have string single load vs array for multiload.
    if (typeof(request) === "string") {

      // Handle the one off request.
      processor(request, requestCb);

    } else {

      // We have a array so loop through and request each item in the array
      for (var i = 0, len = request.length; i < len; i++) {
        processor(request[i]);
      }

    }


  };

  return lazyLoader;

});
