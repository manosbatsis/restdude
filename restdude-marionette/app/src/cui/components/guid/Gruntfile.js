module.exports = function (grunt) {
    // Component configuration
    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),

        clean: {
            dist: ['dist/**/*.*'],
        },

        copy: {
            scripts: {
                expand: true,
                cwd: 'src/js',
                src: ['**/*.*'],
                dest: 'dist/js',
                filter: 'isFile',
            },
        },

        // https://github.com/gruntjs/grunt-contrib-jshint
        // Supported options: http://jshint.com/docs/
        jshint: {
            options: {
                curly: true,
                eqeqeq: true,
                browser: true,
                unused: 'vars',
            },
            files: [
                'src/js/**/*.js',
            ],
        },

        watch: {
            options: {
                livereload: 35728,
                interrupt: true,
            },

            scripts: {
                files: [
                    'src/**/*.js',
                ],
                tasks: [
                    'jshint',
                    'copy',
                ],
            },
        },

    });

    // Load all Grunt plugins
    require('load-grunt-tasks')(grunt);

    // Default task
    grunt.registerTask('default', ['clean', 'jshint', 'copy']);

    // Development task
    grunt.registerTask('dev', ['default', 'watch']);
};
