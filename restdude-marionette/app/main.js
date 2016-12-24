require.config({ 
	baseUrl: "app"/*"www/js"*/,
    paths: {
		'router'				: 'router',
		'app'					: 'app',
		'templates'				: '../build/templates',
		'jquery'				: '../bower_components/jquery/dist/jquery',
		'backbone'				: '../bower_components/backbone/backbone',
		'underscore'			: '../bower_components/lodash/dist/lodash',
		'marionette'			: '../bower_components/marionette/lib/core/backbone.marionette',
		'backbone.babysitter'	: '../bower_components/backbone.babysitter/lib/backbone.babysitter',
		'backbone.wreqr'		: '../bower_components/backbone.wreqr/lib/backbone.wreqr',
		'bootstrap'				: '../bower_components/bootstrap-sass-official/assets/javascripts/bootstrap',
		'cookie'				: '../bower_components/jquery.cookie',
		'pace' 					: '../bower_components/PACE/pace',
		'jquery-color' 			: '../bower_components/jquery-color/jquery.color',
		'intlTelInput'			: '../bower_components/intlTelInput/build/js/intlTelInput',
		'q'						: '../bower_components/q',
		'underscore'			: '../bower_components/underscore',
		'underscore-string'		: '../bower_components/underscore-strings/src/underscore.strings',
		'resthub'				: '../bower_components/resthub',
		'localstorage'			: '../bower_components/localstorage/local.storage',
		'text'					: '../bower_components/text',
		'i18n'					: '../bower_components/i18n',
		'pubsub'				: '../bower_components/PubSub/srcpubsub',
		'tether'				: '../bower_components/tether/src/js/tether',
		'backbone-validation' : '../bower_components/backbone.validation/src/backbone-validation',
		'backbone-bootstrap-modal' : '../bower_components/backbone-bootstrap-modals/lib/backbone-bootstrap-modal',
		'backbone-forms-list'   : '../bower_components/backbone-forms/test/editors/extra/list',
		'backbone-forms'		: '../bower_components/backbone-forms',
		'backbone-forms-bootstrap3'	: '../bower_components/backbone-forms/src/templates/bootstrap',
		'backbone-validation'   : '../bower_components/backbone.validation/src/backbone-validation',
		'backbone-queryparams'  : '../bower_components/backbone-query-parameters',
		'backbone.paginator'    : '../bower_components/backbone.paginator/lib/backbone.paginator',
		'backbone.radio'		: '../bower_components/backbone.radio/src/backbone.radio',
		'bootstrap-datetimepicker' : '../bower_components/bootstrap-datetimepicker/src/js/bootstrap-datetimepicker',
		'bootstrap-markdown'	: '../bower_components/bootstrap-markdown/js/bootstrap-markdown',
		'bootstrap-switch'		: '../bower_components/bootstrap-switch/src/less/bootstrap2/bootstrap-switch',
		'handlebars'			: '../bower_components/handlebars/handlebars',
		'async'					: '../bower_components/async/dist/async',
		'keymaster'				: '../bower_components/keymaster/keymaster',
		'moment'				: '../bower_components/moment/moment',
		'json2'					: '../bower_components/JSON-js/json2',
		'console'				: '../bower_components/console/console',
		'backgrid'				: '../bower_components/backgrid/lib/backgrid',
		'backgrid-paginator'	: '../bower_components/backgrid-paginator/backgrid-paginator',
		'backgrid-moment-cell'	: '../bower_components/backgrid-moment-cell/backgrid-moment-cell',
		'backgrid-text-cell'	: '../bower_components/backgrid-text-cell/backgrid-text-cell',
		'mocha'				    : '../bower_components/mocha/mocha',
		'chai'					: '../bower_components/chai/chai',
		'sinon'					: '../bower_components/Sinon.JS/lib/sinon',
		'calendar'				: '../bower_components/moment/src/lib/moment/calendar',
		'select2'				: '../bower_components/select2/dist/js/select2',
		'jquery.raty'					: '../bower_components/raty/lib/jquery.raty',
		'bootstrap-fileInput'	: '../bower_components/bootstrap-fileinput/js/bootstrap-fileInput',
		'typeahead'				: '../bower_components/typeahead.js/src/typeahead/typeahead',
		'bloodhound'			: '../bower_components/bloodhound/src/bloodhound',
		'google-maps-loader'	: '../bower_components/Js-GoogleMapsLoader',
		'humanize'				: '../bower_components/humanize/humanize',
		'chart'					: '../bower_components/chartjs',
		'template'				: '../bower_components/template/lib/template',
		'SocketIO'              : "//cdn.socket.io/socket.io-1.0.6"
		},
	shim : {
		jquery : {
			exports : 'jQuery'
		},
		underscore : {
			exports : '_'
		},
		marionette : {
			deps : ['jquery', 'underscore', 'backbone'],
			exports : 'Marionette'
		},
		bootstrap : {
			deps : ['jquery'],
		},
		app : {
			deps : ['jquery', 'underscore', 'backbone', 'marionette'],
		},
		router : {
			deps : ['app'],
		},
		templates : {
			deps : ['underscore']}
		},
		'underscore-string' : {
			deps : [ 'underscore' ]
		},
		'underscore-inflection' : {
			deps : [ 'underscore' ]
		},
		'handlebars-orig' : {
			exports : 'Handlebars'
		},

		'backbone' : {
			deps : [ 'underscore' ],
			exports : function() {
				return this.Backbone;
			}
		},
		'marionette' : {
			deps : [ 'backbone', 'underscore', 'backbone.radio' ],
			exports : 'Marionette'
		},
		'backgrid' : {
			deps : [ 'jquery', 'underscore', 'backbone', 'backbone.paginator' ],
			exports : 'Backgrid',
			init : function(jQuery, underscore, Backbone, PageableCollection) {
				Backbone.PageableCollection = PageableCollection;
			}
		},
		'backbone-bootstrap-modal' : {
			deps : [ 'jquery', 'underscore', 'backbone'],
			exports : 'Backbone.BootstrapModal'
		},
		'backbone-forms' : {
			deps : [ 'jquery', 'underscore', 'backbone' ],
			exports : 'Backbone.Form'
		},
//		'backbone-forms-list' : {
//			deps : [ 'backbone-forms' ],
//			exports : 'Backbone.Form.editors.List'
//		},
		'backbone.paginator' : {
			deps : [ 'underscore', 'backbone' ],
			exports : 'PageableCollection'
		},
		'backbone-forms-bootstrap3' : {
			deps : [ 'jquery', 'underscore', 'backbone', 'backbone-forms' ]
		},
		'backgrid-paginator' : {
			deps : [ 'underscore', 'backbone', 'backgrid', 'backbone.paginator' ],
			exports : 'Backgrid.Extension.Paginator'
		},
		'backgrid-moment' : {
			deps : [ 'backgrid', 'moment' ],
			exports : 'Backgrid.Extension.Moment'
		},
		'backgrid-text' : {
			deps : [ 'backgrid' ],
			exports : 'Backgrid.Extension.Text'
		},
//		'tether' : {
//			deps : [ 'tether-orig' ]
//		},
		'calendar' : {
			deps : [ 'jquery' ]
		},
		'bootstrap-markdown' : {
			deps : [ 'jquery' ],
			exports : 'Markdown'
		},
		'bootstrap-switch' : {
			deps : [ 'jquery' ],
		},
		'keymaster' : {
			exports : 'key'
		},
		'async' : {
			exports : 'async'
		},
		'calipso' : {
			deps : [ "i18n!nls/labels", "i18n!nls/labels-custom", 'underscore', 'handlebars', 'calipso-hbs', 'moment', 'backbone', 'backbone.paginator', 'backbone-forms', 'backbone-forms-bootstrap3', 'backbone-bootstrap-modal', 'backbone-forms-list', 'marionette', 'backgrid', 'backgrid-moment', 'backgrid-text', 'backgrid-paginator', 'bloodhound', 'typeahead', 'bootstrap-datetimepicker', 'bootstrap-switch', 'jquery-color', 'intlTelInput', 'q', 'chart' ],
			exports : 'calipso',
		},

		'cookie' : {
			deps : [ 'jquery' ]
		},
		'chai-jquery' : {
			deps : [ 'jquery', 'chai' ]
		},
		'jquery-color' : {
			deps : [ 'jquery' ],
			exports : 'jQuery.Color'
		},

		'intlTelInputUtil' : {
			deps : [ 'jquery' ]
		},
		'intlTelInput' : {
			deps : [ 'intlTelInputUtil' ]
		},
	   
});

require([
	"jquery",
	"backbone",
	"underscore",
	"marionette",
	"app",
	"router",
	"templates",
	"bootstrap",
],
function(jquery, backbone, underscore, marionette, app, router) {
	app.router = new router();

    Backbone.history.start({ pushState: true });
});
