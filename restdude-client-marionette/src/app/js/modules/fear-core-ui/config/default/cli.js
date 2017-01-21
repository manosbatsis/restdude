'use strict';

var argv = require('yargs').argv;

module.exports = {
    channel : argv.channel ? argv.channel : 'default',
    product : argv.product ? argv.product : 'all',
    page    : argv.page ?  argv.page : 'hub',
    size    : argv.size ?  argv.size : 'xsmall',
    folder  : argv.folder ?  argv.folder : 'app'
};
