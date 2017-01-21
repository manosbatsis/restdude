'use strict';

module.exports = function () {

    var gulp = require('gulp');
    var config = require('fear-core').utils.config();

    /**
     * create-app-config
     * create application config for runtime
     */
    gulp.task('create-app-config', function () {

        var gulp = require('gulp');
        var gutil = require('gulp-util');
        var Readable = require('stream').Readable;
        var content = config.getAppConfigTpl().replace('__JSON_CONFIG__', JSON.stringify(config.get('app')));
        var src = new Readable({
            objectMode: true
        });

        // create source file content
        src._read = function () {
            this.push(new gutil.File({
                cwd: '',
                base: '',
                path: 'config.js',
                contents: new Buffer(content)
            }));
            this.push(null);
        };

        // move file to destination
        return src.pipe(gulp.dest(config.get('paths.common.scripts', {base : config.get('paths.app.base')})));
    });
};