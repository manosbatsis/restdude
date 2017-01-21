'use strict';

var gulp = require('gulp');
var tasks = require('fear-core-dev');
var config = require('fear-core').utils.config();

module.exports = function () {
    gulp.task('test-unit', function (done) {
        return tasks.test.karmaRunOnce(config.get('karma'), done)();
    });
};
