'use strict';

var argv = require('yargs').argv;

/**
 * logger
 * @param error {Object}
 */
function logger (error) {
    if (argv.debug) {
        throw error;
    }
}

/**
 * modulePath
 * @param modulePath {String}
 */
function loadModule (modulePath) {
    try {
        require(modulePath)();
    } catch (e) {
        logger(e);
    }
}

//tasks
loadModule('./tasks/dev');
loadModule('./tasks/build');

//core tasks
loadModule('./tasks/core');
loadModule('./tasks/serve');
