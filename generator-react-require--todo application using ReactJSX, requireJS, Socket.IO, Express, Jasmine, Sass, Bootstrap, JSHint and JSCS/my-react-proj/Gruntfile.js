module.exports = function(grunt) {
    "use strict";

    // Load grunt plugins
    require("matchdep").filterDev("grunt-*").forEach(grunt.loadNpmTasks);

    // Paths configuration
    var pathConfig     = {};
    pathConfig.rootDir = "./";
    pathConfig.webDir  = pathConfig.rootDir + "www/";

    // Project configuration.
    grunt.initConfig({
        pkg: grunt.file.readJSON("package.json"),
        pathConfig: pathConfig,
        compass: {
            dist: {
                options: {
                    sassDir: "<%= pathConfig.rootDir %>preprocess/scss",
                    cssDir: "<%= pathConfig.webDir %>css",
                    outputStyle: "compressed"
                }
            }
        },
        copy: {
            js: {
                expand: true,
                cwd: "<%= pathConfig.rootDir %>preprocess/js",
                src: "**/**",
                dest: "<%= pathConfig.webDir %>js"
            },
            server: {
                expand: true,
                cwd: "<%= pathConfig.rootDir %>preprocess/server",
                src: "**/**",
                dest: "<%= pathConfig.rootDir %>server"
            }
        },
        uglify: {
            options: {
                compress: true
            },
            js: {
                files: [
                    {
                        expand: true,
                        cwd: "www/js",
                        src: "**.js",
                        dest: "www/js"
                    }
                ]
            },
            jsx: {
                files: [
                    {
                        expand: true,
                        cwd: "www/js",
                        src: "**/**.js",
                        dest: "www/js"
                    }
                ]
            }
        },
        watch: {
            js: {
                files: ["<%= pathConfig.rootDir %>preprocess/js/**/**"],
                tasks: ["copy:js"],
                options: {
                    spawn: false
                }
            },
            jsx: {
                files: ["<%= pathConfig.rootDir %>preprocess/jsx/**/**"],
                tasks: ["react"],
                options: {
                    spawn: false
                }
            },
            server: {
                files: ["<%= pathConfig.rootDir %>preprocess/server/**"],
                tasks: ["copy:server"],
                options: {
                    spawn: false
                }
            },
            css: {
                files: ["<%= pathConfig.rootDir %>preprocess/scss/**.scss"],
                tasks: ["compass"]
            }
        },
        jshint: {
            all: {
                src: [
                    "*.js",
                    "*.json",
                    "preprocess/js/**"
                ],
                options: {
                    jshintrc: true
                }
            }
        },
        jscs: {
            src: [
                "Gruntfile.js",
                "preprocess/js/**"
            ]
        },
        react: {
            dynamicMappings: {
                files: [
                    {
                        expand: true,
                        cwd: "<%= pathConfig.rootDir %>preprocess/jsx/",
                        src: ["**/**.jsx"],
                        dest: "<%= pathConfig.webDir %>js/",
                        ext: ".js"
                    }
                ]
            }
        },
        karma: {
            unit: {
                configFile: "karma.conf.js",
                singleRun: true
            }
        },
        connect: {
            frontend: {
                options: {
                    port: 9000,
                    hostname: "0.0.0.0",
                    base: "<%= pathConfig.webDir %>",
                    keepalive: true
                }
            }
        },
        express: {
            dev: {
                options: {
                    script: "server/server.js"
                }
            }
        }
    });

    // Tasks
    grunt.registerTask("test",     [ "karma" ]);
    grunt.registerTask("frontend", [ "connect" ]);
    grunt.registerTask("backend",  [ "express:dev" ]);
    grunt.registerTask("sca",      [ "jshint", "jscs" ]);
    grunt.registerTask("compile",  [ "copy", "react", "compass"]);
    grunt.registerTask("build",    [ "test", "sca", "compile", "uglify" ]);

    grunt.registerTask("default",  [ "compile"]);

};
