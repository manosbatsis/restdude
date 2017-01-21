'use strict';

module.exports = function() {

    var gulp = require('gulp');
    var tasks = require('fear-core-build');
    var config = require('fear-core').utils.config();

    var ui = require('../../../index');
    var path = require('path');

    var autoPrefixOptions = {
        browsers: ['last 20 version', 'Explorer >= 8', 'Android >= 2'],
        cascade: false
    };

    var includePaths = JSON.parse(JSON.stringify(ui.sassPaths));
    includePaths.push(path.join(process.cwd(), 'app/core/sass'));
    includePaths.push(path.join(process.cwd(), 'lib'));

    var defaultOptions = Object.freeze({
        autoPrefix: autoPrefixOptions,
        includePaths: includePaths
    });

    function compileOptions(options) {
        var temp = JSON.parse(JSON.stringify(defaultOptions));
        return Object.assign(temp, options);
    }

    /**
     * compile-core-sass
     */
    var toProcessCore = path.join(config.get('paths.core.sass'), config.get('paths.glob.sass'));

    gulp.task('compile-core-sass', tasks.sass.compile(toProcessCore, compileOptions({destination: 'css'})));

    /**
     * compile-module-sass
     */
    gulp.task('compile-core-ui-sass', tasks.sass.compile('./lib/mns-core-ui/**/*.scss', compileOptions({destination: 'assets/css'})));

    /**
     * compile-examples-sass
     */
    gulp.task('compile-examples-sass', tasks.sass.compile(['./examples/**/*.scss'], compileOptions({destination: 'examples/css'})));

    return gulp.task('compile-sass', [
        'compile-core-sass',
        'compile-core-ui-sass',
        'compile-examples-sass'
    ], function() {
        return gulp.src(path.join(config.get('paths.core.css'), config.get('paths.glob.css')))
            .pipe(gulp.dest(config.get('paths.temp.base')));
    });
};
