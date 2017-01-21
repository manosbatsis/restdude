'use strict';

var mustacheConfig = require('./mustache');

module.exports = {
    basePath: './',
    preprocessors: {
        'app/common/views/**/*.html': [
            'mustache'
        ],
        'test/**/*.html': ['ng-html2js']
    },
    karmaMustachePreprocessor: {
        channel: 'default',
        mustacheModule: 'connect-mustache-middleware/index',
        mustacheOpts: mustacheConfig,
        defaultsModule: process.cwd() + '/mock/src/channel'
    },
    ngHtml2JsPreprocessor: {
        moduleName: 'templates'
    },
    frameworks: [
        'systemjs',
        'mocha'
    ],
    plugins: [
        'karma-mocha',
        'karma-ng-html2js-preprocessor-with-templates',
        'karma-systemjs',
        'karma-mocha-reporter',
        'karma-mustache-preprocessor',
        'karma-angular',
        'karma-phantomjs-launcher',
        {
            'middleware:node': ['factory', function() {
                var express = require('express');
                var app = express();

                app.use('/base/node_modules', express.static('./node_modules'));
                return app;
            }]
        }
    ],
    middleware: ['node'],
    systemjs: {
        configFile: 'app/common/scripts/system.conf.js',
        config: {
            baseURL: '/',
            map: {
                'sinon': 'node_modules/fear-core-dev/node_modules/sinon/pkg/sinon.js',
                'chai': 'node_modules/fear-core-dev/node_modules/chai/chai.js',
                'sinon-chai': 'node_modules/fear-core-dev/node_modules/sinon-chai/lib/sinon-chai.js'
            }
        },
        serveFiles: [
            'lib/mns-core-ui/**/*.js',
            'lib/mns-core-ui/**/*.html'
        ]
    },
    files: [
        'test/spec/helper/global.js',
        'lib/mns-core-ui/**/*.spec.js'
    ],
    browsers: ['PhantomJS'],
    port: 8080,
    singleRun: true,
    browserDisconnectTimeout: 10000,
    browserDisconnectTolerance: 1,
    browserNoActivityTimeout: 60000
};
