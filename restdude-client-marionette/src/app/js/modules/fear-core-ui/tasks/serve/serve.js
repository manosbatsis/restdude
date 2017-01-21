'use strict';

module.exports = function () {

    var serve = require('fear-core').serve;
    var config = require('fear-core').utils.config();
    var gulp = require('gulp');
    var mustacheConfig = config.get('mustache');
    var connectServer;
    var baseFolder = require('yargs').argv.folder || 'app';

    //first path must be the base folder
    var staticPaths = [
        config.get('paths.' + config.get('cli.folder') + '.base'),
        config.get('paths.temp.base'),
        'node_modules',
        'lib',
        'examples'
    ];

    var channelDefaults = require(process.cwd() + '/mock/src/channel');

    function isDev() {
        return config.env() === 'development';
    }

    function isTestsRunning() {
        return global.testsRunning;
    }

    function liveReloadConditions() {
        return isDev() && !isTestsRunning();
    }

    gulp.task('start-server', ['compile-sass', 'build-mustache', 'create-app-config'], function () {

        var express = require('express');
        var app = express();

        //Only add middleware when serving from app folder
        if (baseFolder === 'app') {
            app.use('/node_modules', express.static('node_modules'));
            app.use('/examples', express.static('examples'));
            app.use('/lib', express.static('lib'));
        }

        connectServer = serve.startServer(
            config.get('webserver.host'),
            config.get('webserver.port'),
            staticPaths,
            liveReloadConditions,
            mustacheConfig,
            channelDefaults,
            [app],
            ['^\/core\/([a-zA-Z-_]*)\/([a-zA-Z-_]*)$ /core/views/default/pages/$1/index.html?$1=$2 [L,QSA]']
        );
    });

    gulp.task('stop-server', function () {
        connectServer.serverClose();
    });
};
