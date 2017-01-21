module.exports = function(grunt) {
  var files = grunt.file.readJSON("build/files.json");

  grunt.initConfig({
    pkg: grunt.file.readJSON("package.json"),

    concat: {
      options: {
        banner: "/*! <%= pkg.name %> <%= grunt.template.today(\"yyyy-mm-dd\") %> */\n"
      },

      base: {
        src: files.base,
        dest: "dist/<%= pkg.name %>-base.concat.js"
      },
      embedded: {
        src: files.embedded,
        dest: "dist/<%= pkg.name %>-embedded.concat.js"
      },
      dynamic: {
        src: files.dynamic,
        dest: "dist/<%= pkg.name %>-dynamic.concat.js"
      },
      providers_mustache: {
        src: files.providers.mustache,
        dest: "dist/<%= pkg.name %>-providers-mustache.concat.js"
      },
      providers_simple_template: {
        src: files.providers.mustache,
        dest: "dist/<%= pkg.name %>-providers-simple_template.concat.js"
      }
    },

    min: {
      base: {
        src: "dist/<%= pkg.name %>-base.concat.js",
        dest: "dist/<%= pkg.name %>-base.min.js"
      },
      embedded: {
        src: "dist/<%= pkg.name %>-embedded.concat.js",
        dest: "dist/<%= pkg.name %>-embedded.min.js"
      },
      dynamic: {
        src: "dist/<%= pkg.name %>-dynamic.concat.js",
        dest: "dist/<%= pkg.name %>-dynamic.min.js"
      },
      providers_mustache: {
        src: "dist/<%= pkg.name %>-providers-mustache.concat.js",
        dest: "dist/<%= pkg.name %>-providers-mustache.min.js"
      },
      providers_simple_template: {
        src: "dist/<%= pkg.name %>-providers-simple_template.concat.js",
        dest: "dist/<%= pkg.name %>-providers-simple_template.min.js"
      }
    }
  });

  // Load the plugin that provides the "concat" task.
  grunt.loadNpmTasks('grunt-contrib-concat');

  // Load the plugin that provides the "min" task.
  grunt.loadNpmTasks('grunt-yui-compressor');

  // Default task(s).
  grunt.registerTask('default', ['concat', 'min']);
}