require.config({

    //   list of vendor required
    paths: {
        jquery: "vendor/jquery/jquery",
        backbone: "vendor/backbone/backbone",
        underscore: "vendor/underscore/underscore",
        domReady: "vendor/requirejs-domready/domReady",
        text: "vendor/requirejs-text/text",
        json2: "vendor/json2/json2",
        bootstrap: "vendor/bootstrap/bootstrap",
        handlebars: "vendor/handlebars/handlebars",
        'backbone.marionette': "vendor/backbone.marionette/lib/backbone.marionette",
        'marionette_handlebars': "vendor/backbone.marionette.handlebars/backbone.marionette.handlebars",
        'backbone.babysitter'	: 'vendor/backbone.babysitter/lib/backbone.babysitter',
        'backbone.wreqr'		: 'vendor/backbone.wreqr/lib/backbone.wreqr',
		'bootstrap'				: 'vendor/bootstrap-sass-official/assets/javascripts/bootstrap',
        'cookie'				: 'vendor/jquery.cookie',
		'pace' 					: 'vendor/PACE/pace',
        'jquery-color' 			: 'vendor/jquery-color/jquery.color',
		'intlTelInput'			: 'vendor/intlTelInput/build/js/intlTelInput',
		'q'						: 'vendor/q',
        'underscore-string'		: 'vendor/underscore-strings/src/underscore.strings',
		'resthub'				: 'vendor/resthub',
		'localstorage'			: 'vendor/localstorage/local.storage',
        'text'					: 'vendor/text',
		'i18n'					: 'vendor/i18n',
		'pubsub'				: 'vendor/PubSub/srcpubsub',
        'tether'				: 'vendor/tether/src/js/tether',
		'backbone-validation' : 'vendor/backbone.validation/src/backbone-validation',
		'backbone-bootstrap-modal' : 'vendor/backbone-bootstrap-modals/lib/backbone-bootstrap-modal',
		'backbone-forms-list'   : 'vendor/backbone-forms/test/editors/extra/list',
		'backbone-forms'		: 'vendor/backbone-forms',
		'backbone-forms-bootstrap3'	: 'vendor/backbone-forms/src/templates/bootstrap',
        'backbone-validation'   : 'vendor/backbone.validation/src/backbone-validation',
		'backbone-queryparams'  : 'vendor/backbone-query-parameters',
		'backbone.paginator'    : 'vendor/backbone.paginator/lib/backbone.paginator',
		'backbone.radio'		: 'vendor/backbone.radio/src/backbone.radio',
		'bootstrap-datetimepicker' : 'vendor/bootstrap-datetimepicker/src/js/bootstrap-datetimepicker',
		'bootstrap-markdown'	: 'vendor/bootstrap-markdown/js/bootstrap-markdown',
		'bootstrap-switch'		: 'vendor/bootstrap-switch/src/less/bootstrap2/bootstrap-switch',
		'async'					: 'vendor/async/dist/async',
		'keymaster'				: 'vendor/keymaster/keymaster',
		'moment'				: 'vendor/moment/moment',
        'json2'					: 'vendor/JSON-js/json2',
		'console'				: 'vendor/console/console',
		'backgrid'				: 'vendor/backgrid/lib/backgrid',
		'backgrid-paginator'	: 'vendor/backgrid-paginator/backgrid-paginator',
        'backgrid-moment-cell'	: 'vendor/backgrid-moment-cell/backgrid-moment-cell',
		'backgrid-text-cell'	: 'vendor/backgrid-text-cell/backgrid-text-cell',
		'mocha'				    : 'vendor/mocha/mocha',
		'chai'					: 'vendor/chai/chai',
		'sinon'					: 'vendor/Sinon.JS/lib/sinon',
        'calendar'				: 'vendor/moment/src/lib/moment/calendar',
		'select2'				: 'vendor/select2/dist/js/select2',
		'jquery.raty'					: 'vendor/raty/lib/jquery.raty',
		'bootstrap-fileInput'	: 'vendor/bootstrap-fileinput/js/bootstrap-fileInput',
        'typeahead'				: 'vendor/typeahead.js/src/typeahead/typeahead',
		'bloodhound'			: 'vendor/bloodhound/src/bloodhound',
		'google-maps-loader'	: 'vendor/Js-GoogleMapsLoader',
        'humanize'				: 'vendor/humanize/humanize',
		'chart'					: 'vendor/chartjs',
		'template'				: 'vendor/template/lib/template',
        'SocketIO'              : "cdn.socket.io/socket.io-1.0.6",
        'backbone'				: 'vendor/backbone/backbone',
        'marionette'			: 'vendor/marionette/lib/core/backbone.marionette',
},

    shim: {

        jquery: {
            exports: "jQuery"
        },

        bootstrap: ["jquery"],

        underscore: {
            exports: "_"
        },

        handlebars: {
            exports: 'Handlebars'
        },

        backbone: {
            deps: ["underscore", "jquery"],
            exports: "Backbone"
        },

        "backbone.marionette": {
            deps: ["backbone"],
            exports: "Backbone.Marionette"
        },

        "marionette_handlebars": {
            deps: ["backbone.marionette", "handlebars"]
        },
        templates : {
			deps : ['underscore']
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

    }

});


require([
        "jquery",
        'backbone',
        'underscore',
        'handlebars',
        'modules/TwitterSearchApp',
        'modules/router/TweetAppRouter',
        'modules/controller/TweetController',
        'modules/collections/TwitterCollection',
        'backbone.marionette',
        'marionette_handlebars',
        'bootstrap'
    ],
    function ($,
              Backbone,
              _,
              Handlebars,
              TwitterSearchApp,
              TweetAppRouter,
              TweetController,
              TwitterCollection,
              Marionette,
              MarionetteHandlebars,
              bootstrap) {

        // initialize the controller and app router
        TwitterSearchApp.addInitializer(function (options) {
            // initialize the controller
            var controller = new TweetController({
                region_search: TwitterSearchApp.search
            });
            // initialize the router
            var router = new TweetAppRouter({
                controller: controller
            });
        });


        // initialize the application
        TwitterSearchApp.start({
            root: window.location.pathname,
            path_root: "/"
        });

    });