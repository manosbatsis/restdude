'use strict';
module.exports = function (grunt) {
	grunt.config.set('jade', {
		compile: {
			options: {
				client: false,
				pretty: false
			},
			files: [{
				cwd: 'src/templates/',
				src: '*.jade',
				dest: 'src/templates/html/',
				expand: true,
				ext: '.html'
			}, {
				cwd: 'src/modules/',
				src: '*.jade',
				dest: 'src/modules/html/',
				expand: true,
				ext: '.html'
			}]
		}
	});
};