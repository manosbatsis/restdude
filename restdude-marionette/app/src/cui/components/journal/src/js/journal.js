define(['jquery', 'kind'], function ($, kind) {
    var VERSION = '0.0.1';

    var DEFAULT_SETTINGS = {
            type: 'log',   // Method of `console`
            owner: 'Unknown',   // 'UI'
            module: '',    // 'table',
            submodule: '', // tableSort
            func: '',      // `alphaDescending()`
            element: '',   // '#table-1',
            message: [],   // Main message or contents of the entry
        };

    var _storage = [];
    var _isLive = false;
    var _isLiveErrors = true;

    // Private function namespace
    var _priv = {};

    // Add log entry into storage array
    _priv.processEntry = function _processEntry (entry) {
        // Store the log
        _storage.push(entry);

        // Print the entry if live is enabled
        if (_isLive) {
            _priv.printEntry(entry);
        }
        // Print the entry if it's an error, unless we were told to suppress errors
        else if (_isLiveErrors && entry.type === 'error') {
            // Push all errors to the console regardless
            _priv.printEntry(entry);
        }
        // Otherwise, do not print anything right now
    };

    /**
     * Prints an entry to the browser console
     *
     * @param   {Object}  entry  Entry object
     */
    _priv.printEntry = function _printEntry (entry) {
        var contents = Array.prototype.slice.call(entry.message);
        var prefix = '';
        var hasCodeInfo = (entry.module || entry.submodule || entry.func);

        // Prepend any defined properties
        if (entry.owner) {
            prefix += entry.owner;
        }

        // Opening bracket for module, sub-module, and/or function
        if (hasCodeInfo) {
            // Separate it from the owner
            if (entry.owner) {
                prefix += ' ';
            }

            prefix += '[';
        }

        // Module
        if (entry.module) {
            prefix += entry.module;

            // Space between this and the function
            if (!entry.submodule && entry.func) {
                prefix += ' ';
            }
        }

        // Submodule
        if (entry.submodule) {
            prefix += '.' + entry.submodule;

            // Space between this and the function
            if (entry.func) {
                prefix += ' ';
            }
        }

        // Function
        if (entry.func) {
            prefix += '=> ' + entry.func;

            // Add parens to function name, if missing
            if (!/\(.*\)$/.test(entry.func)) {
                prefix += '()';
            }
        }

        // Closing bracket
        if (hasCodeInfo) {
            prefix += ']';
        }
        // No module information, but there is an owner
        else if (entry.owner) {
            // Insert a separator between the owner and the message
            prefix += ' |';
        }

        // Element
        // Add this before adding the prefix so the prefix is still first in the array
        if (typeof entry.element !== 'undefined') {
            if (typeof entry.element === 'string' && entry.element.length) {
                try {
                    contents.unshift($(entry.element));
                }
                catch (e) {
                    contents.unshift('element@' + entry.element);
                }
            }
            else {
                contents.unshift(entry.element);
            }
        }

        // Add the prefix and element to the message contents so it's all printed together
        if (prefix.length !== 0) {
            contents.unshift(prefix);
        }

        switch (entry.type) {
            case 'error':
                console.error.apply(console, contents);

                break;

            case 'warn':
                console.warn.apply(console, contents);

                break;

            case 'info':
                console.info.apply(console, contents);

                break;

            default:
                console.log.apply(console, contents);

                break;
        }
    };

    ////////////////////////////////////
    // Constructor and public methods //
    ////////////////////////////////////

    var journal = function _journal () {};

    // Extend the base
    journal.prototype = {};

    // Function to actual register the log event
    journal.log = function _log () {
        var entry;
        var contents;
        var options;

        // Filter out any log posts with no arguments
        if (arguments.length === 0) {
            return false;
        }

        // Turn `arguments` into a proper array
        contents = Array.apply(null, arguments);

        // Check for a settings object in the first argument
        if (contents.length > 1) {
            options = contents[0];

            // Check to see if the first parameter is an options object
            // We will assume it's an options object if any of the standard properties were defined
            if (kind(options) === 'object' && (options.type || options.owner || options.module || options.submodule || options.func || options.element || options.message)) {
                // Standardize the settings
                entry = $.extend({}, DEFAULT_SETTINGS, options);

                // Normalize the type
                if (entry.type === 'warning') {
                    entry.type = 'warn';
                }

                // Remove the options object from the list of contents
                contents.shift();
            }
            // No options were provided
            else {
                // Use the default settings
                entry = $.extend({}, DEFAULT_SETTINGS);
            }
        }
        // No settings were provided, just the contents
        else {
            // Use the default settings
            entry = $.extend({}, DEFAULT_SETTINGS);
        }

        // Add the contents to the entry
        entry.message = entry.message.concat(contents);

        // console.log('Entry logged. Settings: ', entry, ', message: ', entry.message);
        _priv.processEntry(entry);

        return true;
    };

    // Function to flush the current log buffer buffer
    journal.clear = function _clear (doClearConsole) {
        _storage = [];

        if (doClearConsole) {
            console.clear();
        }
    };

    // Function prints out all stored logs
    journal.print = function _print (filters) {
        var filterProps;
        var entry;
        var prop;
        var i;
        var j;
        var len;
        var jLen;

        // No filters specified, so just print everything
        if (typeof filters === 'undefined') {
            for (i = 0, len = _storage.length; i < len; i++) {
                _priv.printEntry(_storage[i]);
            }
        }
        // Filters were supplied, so only print what was requested
        else {
            if (typeof filters === 'string') {

                switch (filters.toLowerCase()) {
                    // Supported log types
                    case 'error':
                    case 'warn':
                    case 'info':
                    case 'log':
                        for (i = 0, len = _storage.length; i < len; i++) {
                            if (_storage[i].type === filters) {
                                _priv.printEntry(_storage[i]);
                            }
                        }

                        break;

                    // Print all logs
                    case 'all':
                        for (i = 0, len = _storage.length; i < len; i++) {
                            _priv.printEntry(_storage[i]);
                        }

                        break;

                    // No type was requested
                    default:

                        console.log(filters);

                        journal.log({type: 'error', owner: 'UI', module: 'journal', submodule: 'print'}, 'Journal only supports text strings that match specific message types, or "all". The provided value "' + filters.toLowerCase() + '" is not supported.');

                        break;
                }
            }
            else if (kind(filters) === 'object') {
                filterProps = Object.keys(filters);

                entryLoop:
                for (i = 0, len = _storage.length; i < len; i++) {
                    entry = _storage[i];

                    filterLoop:
                    for (j = 0, jLen = filterProps.length; j < jLen; j++) {

                        prop = filterProps[j];

                        // Handle elements differently -- the user may pass a string representing a selector which we will match against DOM elements
                        if (prop === 'element') {
                            // Entry doesn't have an element
                            if (!entry.element) {
                                continue entryLoop;
                            }

                            // The filter is a string, and the entry is either an element or a selector that points to a single element
                            // We can test if the filter is a selector that matches the element
                            if (typeof filters[prop] === 'string' && (kind(entry.element) === 'element' || (typeof entry.element === 'string' && /^\w*#\w+$/.test(entry.element)))) {
                                // Use a try/catch because jQuery will throw an exception if the selector is malformed
                                try {
                                    if (!$(entry.element).is(filters[prop])) {
                                        continue entryLoop;
                                    }
                                }
                                catch (e) {
                                    continue entryLoop;
                                }
                            }
                            // The filter is some other type of object, but it doesn't really matter to us what type it is, we just need to see if it's the same value as the entry
                            else if (entry.element !== filters[prop]) {
                                continue entryLoop;
                            }
                        }
                        // Check to see if this entry has the property; if not, skip to the next entry without printing
                        else if (entry[prop] === undefined || entry[prop] !== filters[prop]) {
                            continue entryLoop;
                        }
                    }

                    // Log has met the filter requirements
                    _priv.printEntry(_storage[i]);
                }
            }
            else {
                journal.log({type: 'error', owner: 'UI', module: 'journal', submodule: 'print'}, 'Unsupported filter provided: ', filter);
            }
        }
    };

    // Dumps the stored objects to the console unceremoniously
    // Use `journal.print` for nicer printing
    journal.showLog = function _showLog () {
        console.log(_storage);
    };

    // Enables or disables live logging
    // The plugin will continue to store the logs regardless
    journal.live = function _live (mode) {
        // Make sure we know what `mode` to use
        if (typeof mode !== 'string' || !/^on$|^off$|^suppress$/.test(mode)) {
            // Set `mode` so that it will toggle the current state
            if (_isLive) {
                mode = 'off';
            }
            else {
                mode = 'on';
            }
        }

        if (mode === 'suppress') {
            // Turn live mode off
            _isLive = false;

            // Disable live printing of errors
            _isLiveErrors = false;
            console.warn('UI [journal.live] Live printing of logs has been disabled, including errors');
        }
        else if (mode === 'off') {
            // Turn live mode off
            _isLive = false;

            // Re-enable live printing of errors
            _isLiveErrors = true;
            console.warn('UI [journal.live] Live printing of logs has been disabled, except for errors');
        }
        else if (mode === 'on') {
            // Turn live mode on
            _isLive = true;

            // Re-enable live printing of errors
            _isLiveErrors = true;
            console.info('UI [journal.live] Live printing of logs has been enabled');
        }
    };

    // Simplified way to pass a usable boilerplate to functions.
    journal.getDefault = function _getDefault () {
        return $.extend({}, DEFAULT_SETTINGS);
    };

    /**
     * Displays the version number
     * @return  {String}  Semantic version
     */
    journal.getVersion = function _getVersion () {
        return VERSION;
    };

    // Check to make sure the journal is not exposed at the window level and expose it if its not.
    if (!window.journal) {
        window.journal = journal;
    }
    else {
        console.warn('UI [journal] Journal component has not been defined because `window.journal` is already in use: ', window.journal);
    }

    // This module returns nothing, developers should interact with it via the `window.journal` layer.
});
