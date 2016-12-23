module.exports = function(grunt) {
    //"use strict";
	
    // Load grunt plugins
    require("matchdep").filterDev("grunt-*").forEach(grunt.loadNpmTasks);
	var liveReloadInjection =
            '\n(function(){' +
                'var s = document.createElement("script");' +
                's.src="//localhost:35729/livereload.js";' +
                'document.head.appendChild(s);' +
            '}());';

    var jsBanner = '/*! <%= pkg.title %>\n' +
                     ' *  @description  <%= pkg.description %>\n' +
                     ' *  @version      <%= pkg.version %>.REL<%= grunt.template.today("yyyymmdd") %>\n' +
                     ' *  @copyright    <%= grunt.template.today("yyyy") %> ' +
                     '<%= pkg.author.name %>\n */\n';

        // This banner will appear at the top style sheets
    var cssBanner = '@charset "utf-8";\n' + jsBanner;

    // Paths configuration
    var pathConfig     = {};
    pathConfig.rootDir = "./";
    pathConfig.webDir  = pathConfig.rootDir + "www/";

    // Project configuration.
    grunt.initConfig({
		// Expose the banners to the tasks
        jsBanner: jsBanner,
        cssBanner: cssBanner,
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
        concat:{
            options: {
                seperator: ";",
                stripBanners: true,
                banner: '/*! <%= pkg.name %> - v<%=pkg.version %> - '+ '<%=grunt.template.today("yyyy-mm-dd") %> */',
 
            },
            dist: {
                src: [''],
                dest: "",


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
		// https://github.com/gruntjs/grunt-contrib-clean
        clean: {
            options: {
                force:true
            },
            dist: [
                'dist',
                'docs',
            ],
        },
        cssmin:{
            my_target: {
                files: [{
                    expand: true,
                    cwd: "www/css",
                    src: ['*.css', '!*.min.css'],
                    dest: "www/css",
                    ext: '.min.css'
                }]

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
            },
            sass: {
                files: ["<%= pathConfig.rootDir %>preprocess/preprocess/scss/**/*.scss"],
                tasks: ["sass"]
            },
        },
		 // Builds the default javascript CUI library using r.js compiler
        requirejs: {
            main: {
                options: {
                    baseUrl: 'src/', // Where all our resources will be
                    name: '../tasks/libs/requireManager/temp/settings', // Where the generated temp file will be
                    mainConfigFile: "path/to/config.js",
                    paths: {}, // Generate build file
                    include: [
                        'requirejs',
                        'css',
                        'text',
                        'json',
                        'domReady',
                        'lazyLoader',
                        'jquery',
                        'cui',
                    ],
                    optimize: 'uglify2', // 'uglify2',
                    generateSourceMaps: true,
                    preserveLicenseComments: false,
                    out: 'dist/js/main.js', // Where the final project will be output
                },
            },
        },

        // https://github.com/sindresorhus/grunt-sass
        sass: {
            main: {
                options: {
                    sourceMap: true,
                    outputStyle: 'nested', // Options: 'nested', 'compressed' (i.e. minified)
                },
                files: {
                    'dist/css/main.css': 'preprocess/scss/style.scss',
                },
            },
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
	//grunt.registerTask("compile",  [ "copy", "requirejs", "sass"]);
    grunt.registerTask("compile",  [ "copy", "react", "compass"]);
    grunt.registerTask("build",    [ "test", "sca", "compile", "uglify" ]);

    grunt.registerTask("default",  [ "compile"]);

};
