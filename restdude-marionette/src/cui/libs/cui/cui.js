/*
=======================================================================
 New York State Department of Taxation and Finance
 User Interface Team
 Core script (mainly utilities) to be used across projects
=======================================================================
*/
define(['jquery', 'lazyLoader'], function ($, lazyLoader) {
    // Create the namespace
    var cui = {};

    var defaults = {
        optIns: {
            iOSFix: false
        }
    };

    var _priv = {
        initilized: false,
        iOSFix: function iOSFix () {
            // Disable zooming on input focus on iOS
            (function () {
                function zoomDisable () {
                    $('head').find('meta[name=viewport]').remove();
                    $('head').prepend('<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0">');
                }

                function zoomEnable () {
                    $('head').find('meta[name=viewport]').remove();
                    $('head').prepend('<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=1">');
                }

                // Disable zooming when focus begins
                $('input[type=text], textarea').on('touchstart', function () {
                    zoomDisable();
                });

                // Re-enable zooming after input has finished getting focus. This is not on blur, but a moment after the user stopped touching the screen.
                $('input[type=text], textarea').on('touchend', function () {
                    setTimeout(zoomEnable, 500);
                });
            }());
        }
    };

    /**
     * Non-destructive implementation for creating namespaces or adding properties inside of them
     *
     * @param   {String}  namespace  Namespace to be registered
     * @param   {Object}  parent     Parent of namespace
     *
     * @return  {Object}             Parent of namespace
     */
    cui.namespace = function _namespace (namespace, parent) {
        var parts = namespace.split('.');
        var i;

        parent = parent || cui;

        // Strip redundant leading global
        if (parts[0] === 'cui') {
            parts = parts.slice(1);
        }

        for (i = 0; i < parts.length; i += 1) {
            // Create a property if it does not exist
            if (typeof parent[parts[i]] === 'undefined') {
                parent[parts[i]] = {};
            }

            parent = parent[parts[i]];
        }

        return parent;
    };

    /**
     * Simple script init function that needs to run on every page that uses the core ui framework
     */
    cui.init = function () {
        // `cui` namespace is now loaded
    };

    // Place the lazyloader into the cui namespace.
    cui.load = lazyLoader.load;

    return cui;
});
