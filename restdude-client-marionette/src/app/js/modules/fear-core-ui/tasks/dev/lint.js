'use strict';

var gulp = require('gulp');
var tasks = require('fear-core-dev');

module.exports = function () {

    var lintSass = tasks.lint.sassOnChange({
        config: '.scss-lint.yml'
    });

    gulp.task('lint-sass', function () {
        return lintSass([
            './examples/**/*.scss',
            './lib/sass/**/*.scss',
            './test/sass/**/*.scss',
            '!./lib/sass/fear-core-ui/sprites/**/*.scss',
            '!./lib/sass/fear-core-ui/_temp_sprites_generated.scss',
            '!examples/assets/fonts/**/*.scss'
        ]);
    });
};
