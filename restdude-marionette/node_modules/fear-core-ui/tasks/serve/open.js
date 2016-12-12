'use strict';

var config = require('fear-core').utils.config();
var channel = config.get('cli.channel');
var page = config.get('cli.page');

module.exports = function () {

    var serve = require('fear-core').serve;
    var gulp = require('gulp');
    var webserver = config.get('webserver');
    var options;

    function getChannelQueryString() {
        if (channel !== 'default') {
            return 'channel=' + channel;
        }
    }

    options = {
        protocol : 'http://',
        host : webserver.host,
        port : webserver.port,
        page : page,
        queryString : getChannelQueryString()
    };

    return gulp.task('serve', ['start-server', 'watch'], serve.openUrl(options));
};
