'use strict';

module.exports = function () {
    require('./sass/compile')();
    require('./mustache/compile')();
};