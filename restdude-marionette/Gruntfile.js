module.exports = function(grunt) {
    //"use strict";
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
    // Load grunt plugins
   // require("matchdep").filterDev("grunt-*").forEach(grunt.loadNpmTasks);
   
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
                    livereload: true,
                    interrupt: true,
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
             concat: {
            css: {
                options: {
                    banner: cssBanner,
                },
                src: ['dist/css/main.css'],
                dest: 'dist/css/main.css',
            },
            js: {
                options: {
                    banner: jsBanner,
                },
                src: ['dist/js/main.js'],
                dest: 'dist/js/main.js',
            }
        },
            copy: {
            fonts: {
                files: [
                    {
                        expand: true,
                        cwd: 'src/cui/fonts/',
                        src: ['**'],
                        dest: 'dist/fonts',
                        filter: 'isFile',
                    },
                    {
                        expand: true,
                        cwd: 'src/project/fonts/',
                        src: ['**'],
                        dest: 'dist/fonts',
                        filter: 'isFile',
                    },
                ],
            },
            server: {
                files: ["<%= pathConfig.rootDir %>preprocess/server/**"],
                tasks: ["copy:server"],
                options: {
                    spawn: false
                }
            },
            css: {
               // files: ["<%= pathConfig.rootDir %>preprocess/scss/**.scss"],
                //tasks: ["compass"]
                files: '**/*.scss',
				tasks: ['sass']
            },
            // Project styles
            styles: {
                files: [
                    'src/cui/scss/**/*.scss',
                    'src/project/scss/**/*.scss',
                ],
                tasks: ['sass:main'],
            },

            // Core component styles
            styles: {
                files: [
                    'src/cui/components/**/*.scss',
                ],
                tasks: [
                    'clean',
                    // 'md2html',
                    // 'componentFinder',
                     'copy',
                    // 'svgmin',
                    'sass',
                     'requirejs',
                    'concat:css',
                     'copy',
                    'usebanner',
                ],
            },

            // Core component scripts
            styles: {
                files: [
                    'src/cui/components/**/*.scss',
                ],
                tasks: [
                    'clean',
                    // 'md2html',
                    'componentFinder',
                     'copy',
                    // 'svgmin',
                    // 'sass',
                    'requirejs',
                    'concat:js',
                     'copy',
                    'usebanner',
                ],
            },
        // Project HTML
            html: {
                files: [
                    'src/cui/html/**/*.html',
                    'src/project/html/**/*.html',
                ],
                tasks: ['copy:html'],
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
                    },
                ],
            },
        },
        karma: {
            unit: {
                configFile: "karma.conf.js",
                singleRun: true
            },
        },
        

         // Builds the default javascript CUI library using r.js compiler
        requirejs: {
            main: {
                options: {
                    baseUrl: 'src/', // Where all our resources will be
                    name: '../tasks/libs/requireManager/temp/settings', // Where the generated temp file will be
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
                    'dist/css/main.css': 'src/project/scss/project.scss',
                },
            },
        },


        connect: {
            frontend: {
                options: {
                    port: 9000,
                    hostname: "0.0.0.0",
                    base: "<%= pathConfig.webDir %>",
                    keepalive: true
                },
            },
        },
        express: {
            dev: {
                options: {
                    script: "server/server.js"
                },
            },
             fileblocks: {  
            options: {
                templates: {
                    'js': '<script data-main="app/main" src="${file}"></script>',
                },
                removeFiles : true
            },                    
            prod: {
                src: 'index.html',
                blocks: {
                    'app': { src: 'build/prod.js' }
                }
            },
            develop: {
                src: 'index.html',
                blocks: {
                    'app': { src: 'bower_components/requirejs/require.js' }
                }
            },             
        },
        },
    
    },
},//end of watch {}
});

     grunt.registerTask('prod', 'Production', function (args) {

        // Change some setting to optimize the build

        // =================
        // == Build Flag ===
        // =================
        grunt.config.set('prod', true);

        // ===========
        // == SASS ===
        // ===========
        var sass = grunt.config.get('sass');

       // sass.options.sourceMap = false;
       // sass.options.outputStyle = "compressed";

        grunt.config.set('sass', sass);

        // ================
        // == RequireJS ===
        // ================

        var requireJS = grunt.config.get('requirejs');

       // requireJS.compile.options.generateSourceMaps = false;
       // requireJS.compile.options.optimize = "uglify2";

        grunt.config.set('requirejs', requireJS);

        // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // Other tasks like uglify and cssmin are handled by the requireManager build process.
        // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

        grunt.task.run([
            //'clean',
            //'copy',
            //'sass',
            //'requirejs',
            //'concat',
            //'copy',
        ]);
    });
    grunt.loadNpmTasks('grunt-contrib-clean');
	//grunt.loadNpmTasks('grunt-contrib-connect');
	//grunt.loadNpmTasks('grunt-contrib-cssmin');
	//grunt.loadNpmTasks('grunt-contrib-concat');	
	grunt.loadNpmTasks('grunt-contrib-requirejs');
	//grunt.loadNpmTasks('grunt-contrib-watch');
	//grunt.loadNpmTasks('grunt-contrib-jst');
    grunt.loadNpmTasks('grunt-contrib-sass');
    //grunt.loadNpmTasks('grunt-contrib-jasmine');
    //grunt.loadNpmTasks('grunt-file-blocks');
   // grunt.loadNpmTasks('grunt-contrib-jshint');
	//grunt.registerTask('default',['watch']);
   
    grunt.registerTask("sass",     [ "jshint" ]);    
    grunt.registerTask("test",     [ "karma" ]);
    grunt.registerTask("frontend", [ "connect" ]);
    grunt.registerTask("backend",  [ "express:dev" ]);
    grunt.registerTask("sca",      [ "jshint", "jscs" ]);
    grunt.registerTask("compile",  [ "copy", "react", "compass"]);
    grunt.registerTask("build",    [ "test", "sca", "compile", "uglify" ]);

    grunt.registerTask("default",  [ "prod"]);
    //grunt.registerTask("default",  [ "requirejs", "sass"]);
  //  grunt.registerTask('build', [
       // 'jshint',
       // 'clean:dist',
       // 'jst',
      //  'sass',
      //  'requirejs',
     //   'cssmin',
      //  'jasmine',
 //]);

    //grunt.registerTask('develop', ['build', 'fileblocks:develop', 'watch']);

   // grunt.registerTask('release', ['build', 'fileblocks:prod']);
};
