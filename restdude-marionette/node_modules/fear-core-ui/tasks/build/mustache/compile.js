'use strict';

module.exports = function() {

    var gulp = require('gulp');
    var mustache = require('gulp-mustache');
    var config = require('fear-core').utils.config();

    var data = {
        config: {
            jsLocation: '/',
            device: config.get('device')
        }
    };

    return gulp.task('build-mustache', function() {
        return gulp.src(['./**/*.mustache', '!./node_modules/**'])
            .pipe(mustache(data, {extension: '.html'}))
            .pipe(gulp.dest('.'));
    });
};

