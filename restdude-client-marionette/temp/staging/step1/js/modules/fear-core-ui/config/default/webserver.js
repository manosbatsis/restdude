'use strict';
var ip = require('ip');

module.exports = {
    'host': ip.address(),
    'port': '8000'
};
