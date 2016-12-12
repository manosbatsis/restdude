'use strict';

module.exports = {
    gulp: {
        tasks: 'core/tasks',
        specs: 'test/tasks'
    },
    lib: {
        base: 'lib',
        sass: 'lib/**/*.scss'
    },
    examples: {
        base: 'examples',
        sass: 'examples/**/*.scss',
        views: 'examples/**/*.html',
        mustache: 'examples/**/*.mustache'
    },
    app: {
        base: 'app',
        sass: 'app/**/sass',
        css: 'app/**/css',
        scripts: 'app/**/scripts',
        views: 'app/**/views'
    },
    prod: {
        base: 'prod'
    },
    temp: {
        base: '.tmp'
    },
    core: {
        base: 'app/core',
        css: 'app/core/css',
        sass: 'app/core/sass',
        scripts: 'app/core/scripts',
        views: 'app/core/views',
        packages: '{{base}}/core/scripts/packages'
    },
    common: {
        base: '{{base}}/common',
        images: '{{base}}/common/assets/images',
        font: '{{base}}/common/assets/fonts',
        css: '{{base}}/common/css',
        sass: '{{base}}/common/sass',
        scripts: '{{base}}/common/scripts',
        views: '{{base}}/common/views'
    },
    teams: {
        example: {
            base: '{{base}}/bandc',
            images: '{{base}}/bandc/assets/images',
            font: '{{base}}/bandc/assets/fonts',
            css: '{{base}}/bandc/css',
            sass: '{{base}}/bandc/sass',
            scripts: '{{base}}/bandc/scripts',
            views: '{{base}}/bandc/views'
        }
    },
    glob: {
        css : '**/*.css',
        sass : '**/*.scss',
        views : '**/*.html',
        scripts : '**/*.js',
        packages : 'packages/**/*.js',
        images : '**/*.{png,jpg}'
    }
};
