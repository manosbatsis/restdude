// http://www.labor.ny.gov/ux/code-keyboard-shortcuts.html
// https://github.com/madrobby/keymaster
define(['jquery', 'cui'], function ($, cui) {
    ///////////////
    // Constants //
    ///////////////
    var VERSION = '0.1.0';

    var DEFAULT_SETTINGS = {
        // Provided by the user:

        // Required
        keys: '',
        callback: null,

        // Not required
        description: '(No description provided)',
        type: 'keyup',
        scope: document.body,
        data: null,

        // For internal use:
        pieces: [],
        $scope: null,
        code: '',
        enabled: true,
    };

    var RESERVED_KEYS = ['Tab'];

    var EVENT_TYPES = [
            'keyup',
            'keydown',
            'keypress',
        ];

    // Keys that can not be consistently decoded using `charCodeAt()`
    var SPECIAL_KEY_CODES = {
        '/': '191',
        '?': '010191',
        'Ctrl+,': '100188',
    };

    var priv = {}; // Namespace for private methods and variables
    var dataStore = {}; // Associative array of all registered shortcuts
    var $body = $('body');
    var $window = $(window);
    var $legend;
    var $legendTbody;
    var hasBeenInitialized = false;
    var ua = navigator.userAgent;

    var legendVisible = false;

    ////////////////////
    // Public methods //
    ////////////////////

    /**
     * Processes incoming shortcut requests and initializes them if they're valid
     *
     * @param   {Mixed}  param1  Shorthand usage: a string representing the keystroke. Longhand usage: a settings object
     * @param   {Mixed}  param2  Shorthand usage: callback function for the keystroke. Longhand usage: undefined
     * @param   {Mixed}  param3  Shorthand usage: description for the keystroke (string). Longhand usage: undefined
     *
     * @return  {Boolean}        Whether the shortcut was valid and has been initialized
     */
    var _register = function _register (param1, param2, param3) {
        var settings;

        // Setup the plugin if it hasn't been done already
        if (!hasBeenInitialized) {
            priv.initPlugin();
        }

        // Determine what the arguments are:

        // Simple: keys + callback
        if (typeof param1 === 'string' && typeof param2 === 'function') {
            settings = $.extend(true, {}, DEFAULT_SETTINGS);
            settings.keys = param1;
            settings.callback = param2;

            if (typeof param3 === 'string') {
                settings.description = param3;
            }
        }
        // Advanced: settings object
        else if (typeof param1 === 'object' && param1 !== null) {
            settings = $.extend(true, {}, DEFAULT_SETTINGS, param1);
        }
        else {
            journal.log({type: 'error', owner: 'UI', module: 'shortcut', func: 'register'}, 'Invalid arguments: ', arguments);

            return false;
        }

        return priv.setupShortcut(settings);
    };

    var _disable = function _disable (keys) {
        if (dataStore.hasOwnProperty(keys)) {
            dataStore[keys].enabled = false;
        }
    };

    var _enable = function _enable (keys) {
        if (dataStore.hasOwnProperty(keys)) {
            dataStore[keys].enabled = true;
        }
    };

    var _disableAll = function _disableAll (exceptThisOne) {
        Object.keys(dataStore).forEach(function (keys) {
            if (keys !== exceptThisOne) {
                dataStore[keys].enabled = false;
            }
        });
    };

    var _enableAll = function _enableAll (exceptThisOne) {
        Object.keys(dataStore).forEach(function (keys) {
            if (keys !== exceptThisOne) {
                dataStore[keys].enabled = true;
            }
        });
    };

    var _getVersion = function _getVersion () {
        return VERSION;
    };

    /////////////////////
    // Private methods //
    /////////////////////

    /**
     * Initialize this plugin
     *
     * Use pseudo-global `hasBeenInitialized` flag to ensure this only runs once
     */
    priv.initPlugin = function _initPlugin () {
        // Prevent this function from running again
        hasBeenInitialized = true;

        // Add event listener for key strokes
        $window.on(EVENT_TYPES.join(' '), priv.onKeystroke);

        // Register the legend keystroke
        _register({
            keys: '?',
            callback: priv.showLegend,
            description: 'Display this legend',
            type: 'keydown',
        });

        // Pre-load the modal plugin
        cui.load('modal');
    };

    /**
     * Validates and adds a shortcut
     *
     * @param   {Object}   settings  Settings object
     *
     * @return  {Boolean}            Success/failure
     */
    priv.setupShortcut = function _setupShortcut (settings) {
        var badKeyFound = false;
        var lastPiece;

        // Validate the settings:

        // Callback function
        if (typeof settings.callback !== 'function') {
            journal.log({type: 'error', owner: 'UI', module: 'shortcut', func: 'setupShortcut'}, 'No callback defined');

            return false;
        }

        // Event type
        settings.type = settings.type.toLowerCase().trim();

        if (EVENT_TYPES.indexOf(settings.type) === -1) {
            journal.log({type: 'error', owner: 'UI', module: 'shortcut', func: 'setupShortcut'}, 'Unsupported event type: "', settings.type, '"');

            return false;
        }

        // Key combination

        // Normalize key combo so that `ctrl + x` becomes `Ctrl+x` for consistency
        settings.keys = priv.normalizeKeyName(settings.keys);

        // Make sure a keystroke was supplied
        if (!settings.keys.length) {
            journal.log({type: 'error', owner: 'UI', module: 'shortcut', func: 'setupShortcut'}, 'No key defined ', param1, param2);

            return false;
        }

        // Separate the individual keys that make up the shortcut:
        if (settings.keys.indexOf('++') === -1) {
            // One of the keys is the plus sign, so we can't simply `split` on that character
            // Temporarily change the character to our own special word, 'PLUS'
            settings.pieces = settings.keys.replace('++', '+PLUS').split('+');

            // Revert our 'PLUS' keyword back to an actual plus sign
            settings.pieces = settings.pieces.map(function (str) {
                if (str === 'PLUS') {
                    return '+';
                }
                else {
                    return str;
                }
            });
        }
        else {
            // No plus sign in the shortcut
            settings.pieces = settings.keys.split('+');
        }

        // Make sure none of the keys are reserved
        settings.pieces.forEach(function (piece) {
            if (RESERVED_KEYS.indexOf(settings.keys) !== -1) {
                journal.log({type: 'error', owner: 'UI', module: 'shortcut', func: 'setupShortcut'}, 'Reserved key may not be used: ' + RESERVED_KEYS[RESERVED_KEYS.indexOf(settings.keys)]);
                badKeyFound = true;
            }
        });

        if (badKeyFound) {
            return false;
        }

        // Convert the keystroke combination into our own internal code
        // First three digits represent the modifier keys with 1/0 for on/off
        //     1. Control
        //     2. Shift
        //     3. Alt
        // Fourth digit onward is the ASCII code

        settings.code = '';

        // Check for special key combos that we can't always detect consistently
        if (SPECIAL_KEY_CODES.hasOwnProperty(settings.keys)) {
            settings.code = SPECIAL_KEY_CODES[settings.keys];
        }
        // Non-special key combo
        else {
            // Control/Command
            if (settings.pieces.indexOf('Control') !== -1 || settings.pieces.indexOf('Ctrl') !== -1 || settings.pieces.indexOf('Command') !== -1 || settings.pieces.indexOf('Cmd') !== -1 || settings.pieces.indexOf('⌘') !== -1 || settings.pieces.indexOf('⌃') !== -1 || settings.pieces.indexOf('^') !== -1) {
                settings.code += '1';
            }
            else {
                settings.code += '0';
            }

            // Shift
            if (settings.pieces.indexOf('Shift') !== -1 || settings.pieces.indexOf('⇧') !== -1) {
                settings.code += '1';
            }
            else {
                settings.code += '0';
            }

            // Alt/Option
            if (settings.pieces.indexOf('Alt') !== -1 || settings.pieces.indexOf('⌥') !== -1) {
                settings.code += '1';
            }
            else {
                settings.code += '0';
            }

            // Check the final part of the key combination, which should be a simple key and not a modifier
            lastPiece = settings.pieces[settings.pieces.length - 1];

            // Convert human-readable character to ASCII code
            // If the shift key was used with a letter, make sure we're using the uppercase code for that letter
            if (settings.pieces.indexOf('Shift') !== -1 && /[a-z]/.test(lastPiece)) {
                settings.code += lastPiece.toUpperCase().charCodeAt(0);
            }
            else {
                // Check for special character codes
                if (SPECIAL_KEY_CODES.hasOwnProperty(lastPiece)) {
                    settings.code += SPECIAL_KEY_CODES[lastPiece];
                }
                else {
                    settings.code += lastPiece.charCodeAt(0);
                }
            }
        }

        // Add event listener
        settings.$scope = $(settings.scope);

        // Add to data store
        if (dataStore.hasOwnProperty(settings.code)) {
            journal.log({type: 'error', owner: 'UI', module: 'shortcut', func: 'setupShortcut'}, 'Key combination has already been registered: "' + settings.keys + '"', settings);

            return false;
        }
        else {
            dataStore[settings.code] = settings;
        }

        return true;
    };

    /**
     * Handles all keystrokes and looks for a shortcut associated with them
     *
     * @param   {Event}  evt  Key event (could be any type listed in `EVENT_TYPES`)
     */
    priv.onKeystroke = function _onKeystroke (evt) {
        var $target = $(evt.target);
        var code;
        var settings;

        // Make sure we're not in an editable field
        if ($target.closest('input, select, textarea').length) {
            // console.warn('Inside an input, ignoring');
            return true;
        }

        // Convert the code to our custom format
        code = priv.getCustomKeyCode(evt);

        // Make sure we have a registration for this key combination
        if (!dataStore.hasOwnProperty(code)) {
            // console.log('No registration for ' + code + '\n    Code: ', evt.keyCode, '\n    Event: ', evt);
            return true;
        }

        // Retrieve settings
        settings = dataStore[code];

        // Make sure it's enabled
        if (!settings.enabled) {
            // console.warn('Keystroke is disabled ', settings.type);
            return true;
        }

        // Make sure this event matches the settings' requirements:

        // Correct event type
        if (settings.type !== evt.type) {
            // console.warn('Wrong event type, wanted "' + settings.type + '" but the event is "' + evt.type + '"');
            return true;
        }

        // Within the scope
        if ($(evt.target).closest(settings.$scope).length < 1) {
            // console.warn('Wrong scope\n    Desired scope: ', settings.$scope.get(0), '\n    evt.target: ', evt.target);
            return true;
        }

        // We're all good, so go ahead and execute the callback
        settings.callback(evt, settings.data);
    };

    /**
     * Converts a keyboard event into our own internal format
     *
     * @param   {Event}   evt  Key event that triggered `priv.onKeystroke()`
     *
     * @return  {String}       Custom formatted code, e.g. '10177' for `ctrl+alt+m`
     */
    priv.getCustomKeyCode = function _getCustomKeyCode (evt) {
        var code = '';

        if (evt.ctrlKey || priv.isCommandKey(evt.keyCode)) {
            code += '1';
        }
        else {
            code += '0';
        }

        if (evt.shiftKey) {
            code += '1';
        }
        else {
            code += '0';
        }

        if (evt.altKey) {
            code += '1';
        }
        else {
            code += '0';
        }

        // ASCII code
        code += evt.keyCode;

        return code;
    };

    /**
     * Determines whether the key code refers to the ⌘ Command key
     *
     * See: http://stackoverflow.com/a/3922353/348995
     *
     * @param   {Number}   code  Key code from an Event object
     * @return  {Boolean}        Whether or not it's the Command key
     */
    priv.isCommandKey = function _isCommandKey (code) {
        // Firefox
        // Tested: version 46.0, OS X 10.11.4, on 4/29/2016
        if (code === 224 && /firefox/i.test(ua)) {
            return true;
        }
        // Opera
        //TODO: UNTESTED - write down browser and OS versions when testing is complete
        else if (code === 17 && /opera/i.test(ua)) {
            return true;
        }
        // WebKit (Safari/Chrome), refers to left and right keys, respectively
        // Tested: Chrome version 50.0.2661.94 (64-bit), OS X 10.11.4, on 4/29/2016
        // Tested: Safari version 9.1 (11601.5.17.1), OS X 10.11.4, on 4/29/2016
        else if ((code === 91 || code === 93) && /webkit|blink/i.test(ua)) {
            return true;
        }

        return false;
    };

    /**
     * Normalizes key combos into a standard format
     *
     * Each meta key is converted to Title Case, single letters are capitalized, and extra spaces are stripped.
     * For example, `ctrl + shift + s` becomes `Ctrl+Shift+S`
     *
     * @param   {String}   str          Key combination in human-readable form
     * @param   {boolean}  isForLegend  Whether this is being used for the legend
     *
     * @return  {String}                Normalized string
     */
    priv.normalizeKeyName = function _normalizeKeyName (str, isForLegend) {
        // Reference for string replacement functions used below: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/String/replace#Specifying_a_function_as_a_parameter

        if (isForLegend) {
            // Trim leading and trailing spaces only
            str = str.trim();

            str = str
                    // Standardize on "Control" and "Command" rather than abbreviations
                    .replace(/\bctrl\b/gi, 'Control')
                    .replace(/\bcmd\b/gi, 'Command')

                    // Make sure there are spaces around all `+` characters
                    .replace(/(\S)\+/g, function _normalizeKeyName_replacer_space1 (match, p1 /*, offset, string*/) {
                        return p1 + ' +';
                    })
                    .replace(/\+(\S)/g, function _normalizeKeyName_replacer_space2 (match, p1 /*, offset, string*/) {
                        return '+ ' + p1;
                    })

                    // Replace punctuation marks with their name to make it easier to read
                    .replace(/\s\,/, ' comma');
        }
        else {
            // Strip all spaces, even between words
            str = str.replace(/\s/g, '');
        }

        // Title Case for meta key names (i.e. any part with 3+ consecutive characters)
        str = str.replace(/\b(\w)(\w+)/g, function _normalizeKeyName_replacer_meta (match, p1, p2 /*, offset, string*/) {
                        return p1.toUpperCase() + p2;
                    });

        // Capitalize single letters
        str = str.replace(/\b([a-z])\b/g, function _normalizeKeyName_replacer_letters (match, p1 /*, offset, string*/) {
                        return p1.toUpperCase();
                    });

        return str;
    };

    ////////////////////
    // User interface //
    ////////////////////

    priv.showLegend = function _showLegend () {
        
        if(!legendVisible){
            var __createLegend = function __createLegend () {
                var $header;
                var $table;

                $legend = $('<div/>');

                // Header
                $header = $('<div/>')
                            .append(
                                $('<h1/>')
                                    .text('Keyboard Shortcuts')
                            );

                $legend.append($header);

                // Table and header
                $table = $('<table/>')
                            .append(
                                $('<tr/>')
                                    .append(
                                        $('<th/>')
                                            .text('Key Combination')
                                    )
                                    .append(
                                        $('<th/>')
                                            .text('Description')
                                    )
                            );

                // Table body
                $legendTbody = $('<tbody/>');
                $table.append($legendTbody);

                $legend.append($table);
            };

            // Create legend if it doesn't already exist
            if (!$legend) {
                __createLegend();
            }
            else {
                // Empty the existing table so we can display a fresh set of shortcuts
                $legendTbody.empty();
            }

            // Populate table body with all shortcuts
            Object.keys(dataStore).forEach(function (keys) {
                var shortcut = dataStore[keys];

                // Skip the shortcut that shows the legend since that goes at the bottom
                if (shortcut.keys === '?') {
                    return false;
                }

                $legendTbody
                    .append(
                        $('<tr/>')
                            .append(
                                $('<td/>')
                                    .text(
                                        // Capitalize meta keys
                                        priv.normalizeKeyName(shortcut.keys, true)
                                    )
                            )
                            .append(
                                $('<td/>')
                                    .html(shortcut.description)
                            )
                    );
            });

            // Display the shortcut for this legend
            $legendTbody
                .append(
                    $('<tr/>')
                        .append(
                            $('<td/>')
                                .text('?')
                        )
                        .append(
                            $('<td/>')
                                .text('Display this legend')
                        )
                );

            // Create and display modal
            $.modal({
                html: $legend,
                display: {
                    width: '75%',
                },
                overlay: {
                    opacity: 0.25,
                },
                closeDestroy: true,
                hideDestroy:true,
                closeOnEscape: true,
                focusOnClose: $body,
                onDestroy: priv.hideLegend
            }).show();

            legendVisible = true;
        }
    };

    priv.hideLegend = function _hideLegend(){
        legendVisible = false;
    };

    //////////////////////////////////////////
    // Expose public properties and methods //
    //////////////////////////////////////////

    return {
        register: _register,
        disableAll: _disableAll,
        enableAll: _enableAll,
        disable: _disable,
        enable: _enable,
        getVersion: _getVersion,
    };
});
