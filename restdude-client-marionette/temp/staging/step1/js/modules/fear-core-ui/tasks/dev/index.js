'use strict';

module.exports = function () {
    require('./lint')();
    require('./sprites')();
    require('./unit')();
    require('./watch')();
};
