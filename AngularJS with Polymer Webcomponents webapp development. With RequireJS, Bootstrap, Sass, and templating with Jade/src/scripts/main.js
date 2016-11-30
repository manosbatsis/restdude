'use strict';
require.config({
	paths: {
		router: './router',
		text: '../lib/text',
		angular: '../lib/angular',
		angularrouter: '../lib/angular-route',
		webcomponents: '../lib/webcomponents',
		jquery: '../lib/jquery',
		bootstrap: '../lib/bootstrap',
		underscore: '../lib/lodash',
	}
});
window.app = {};
require(['angular', 'webcomponents', 'jquery', 'underscore'], function() {
	require(['angularrouter', 'bootstrap'], function() {
		window.app = angular.module('App', ['ngRoute']);
		require(['router'], function() {
			angular.bootstrap(document, ['App']);
		});
	});
});