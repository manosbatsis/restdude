/*
 *
 * Restdude
 * -------------------------------------------------------------------
 * Module restdude-war-overlay, https://manosbatsis.github.io/restdude/restdude-war-overlay
 *
 * Full stack, high level framework for horizontal, model-driven application hackers.
 *
 * Copyright © 2005 Manos Batsis (manosbatsis gmail)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
// set console api if missing
if (!window.console) {
    console = {};
    console.log = function () {
    };
}
if (!window.Tether) {
    window.Tether = function () {
    };
}
//establish a locale
function initLocale() {
    // get "remembered" locale if exists
    var locale = localStorage.getItem('locale');

    // guess and set remembered otherwise
    if (!locale && navigator) {
        var browserLanguagePropertyKeys = ['languages', 'language', 'browserLanguage', 'userLanguage', 'systemLanguage'];
        var prop;
        for (var i = 0; !locale && i < browserLanguagePropertyKeys.length; i++) {
            prop = navigator[browserLanguagePropertyKeys[i]];
            // pick first if array
            if (prop && prop.constructor === Array) {
                prop = prop[0];
            }
            // no wildcards
            if (prop && prop != "*") {
                // set it
                locale = prop;
            }
        }

        if (!locale) {
            locale = "en";
        }
        // no sub-locale
        else if (locale.length > 2) {
            locale = locale.substring(0, 2);
        }
        localStorage.setItem('locale', locale.toLowerCase());
    }
}
initLocale();

require.config({

    //   list of vendor required
    paths: {
        //'alpaca': "lib/alpaca/alpaca",//lib
        'cookie': "vendor/jquery.cookie",
        'jquery': "vendor/jquery/jquery",
        'pace': 'vendor/PACE/pace',
        'coreui-app': 'lib/coreui-app',//lib
        'jquery-color': 'vendor/jquery-color/jquery.color',
        "intlTelInput": 'vendor/intlTelInput/build/js/intlTelInput',
        "intlTelInputUtil": 'vendor/intlTelInput/lib/libphonenumber/build/utils',
        'q': 'vendor/q',
        'underscore': 'vendor/underscore/underscore',
        'underscore-string': 'vendor/underscore.string/lib/underscore.string',
        'underscore-inflection': 'lib/underscore-inflection',
        'backbone': 'vendor/backbone/backbone',
        //'resthub': 'lib/resthub/resthub', //lib?
        'localstorage': 'vendor/localstorage/dist/local-storage',
        'text': 'vendor/text/text',
        'i18n': 'lib/i18n',
        'pubsub': 'vendor/PubSub/src/pubsub',
        'bootstrap': 'vendor/bootstrap/dist/js/bootstrap',
        // tmp fix until bootstrap devs fix their module deps
        'tether-orig': 'lib/tether-orig',
        'tether': 'lib/tether',
        'backbone-validation': 'vendor/backbone.validation/dist/backbone-validation-amd',
        'backbone-bootstrap-modal': 'backbone-bootstrap-modals', // lib
        'backbone-forms-list': 'lib/backbone-forms-list',// lib
        'backbone-forms': 'vendor/backbone-forms/distribution.amd/backbone-forms',
        'backbone-forms-bootstrap3': 'lib/backbone-forms-bootstrap3', //lib
        'backbone-queryparams': 'lib/backbone-queryparams',//lib
        'backbone.paginator': 'vendor/backbone.paginator/lib/backbone.paginatorr',
        'backbone.radio': 'vendor/backbone.radio/build/backbone.radio',
        'marionette': 'lib/backbone.marionette',
        'bootstrap-datetimepicker': 'vendor/bootstrap-datetimepicker/src/js/bootstrap-datetimepicker',
        'bootstrap-markdown': 'vendor/bootstrap-markdown/js/bootstrap-markdown',
        'bootstrap-switch': 'vendor/bootstrap-switch/dist/js/bootstrap-switch',
        //'handlebars-orig': 'lib/handlebars',//lib resthub
        'handlebars': 'vendor/handlebars/handlebars.runtime.amd',
        'async': 'vendor/async/dist/async',
        'keymaster': 'vendor/keymaster',
        'moment': 'vendor/moment/moment',
        'json2': 'vendor/json2/json2',
        'console': 'vendor/console/console',
        'backgrid': 'vendor/backgrid/lib/backgrid',
        "backgrid-paginator": 'vendor/backgrid-paginator/backgrid-paginator',
        "backgrid-moment": 'vendor/backgrid-moment-cell/backgrid-moment-cell',
        "backgrid-text": 'vendor/backgrid-text-cell/backgrid-text-cell',
        //"restdude": 'lib/restdude',//lib?
        //"restdude-hbs": 'lib/restdude-hbs',//lib?
        // Mocha testing
        'mocha': 'vendor/mocha/mocha',
        'chai': 'vendor/chai/chai',
        'sinon': 'lib/sinon',
        'calendar': 'vendor/bootstrap-calendar/js/calendar',
        'select2': 'vendor/select2/dist/js/select2',
        'raty': 'lib/jquery.raty-fa',
        'bootstrap-fileInput': 'vendor/bootstrap-fileinput/js/fileinput',
        'typeahead': 'vendor/typeahead.js/dist/typeahead.jquery',
        'bloodhound': 'vendor/typeahead.js/dist/bloodhound',
        //'google-maps-loader': 'lib/google-maps-loader',//lib
        'humanize': 'vendor/humanize/humanize',
        //'chart': 'lib/Chart',//lib
        'template': '../template',//?
    },
    wrapShim: true,
    packages: [{
        // Include hbs as a package, so it will find hbs-builder when needed
        name: "hbs",
        location: "lib/hbs",
        main: "hbs",
    }],
    hbs: {
        templateExtension: ".hbs",
    },
    shim: {
        'json2'       : { exports : "JSON" },
        'jquery'      : { exports : "jQuery" },
        'underscore'  : { exports : "_" },
        'underscore-string': {
            deps: ['underscore']
        },
        //'underscore-inflection': {
        //    deps: ['underscore']
        //},
        'handlebars-orig': {
            exports: 'Handlebars'
        },
        'backbone'    : {
            deps : ["jquery", "underscore", "json2"],
            exports: function () {
                return this.Backbone;
            }
        },
        'marionette': {
            deps: ['backbone', 'underscore', 'backbone.radio'],
            exports: 'Marionette'
        },
        'backgrid': {
            deps: ['jquery', 'underscore', 'backbone', 'backbone.paginator'],
            exports: 'Backgrid',
            init: function (jQuery, underscore, Backbone, PageableCollection) {
                Backbone.PageableCollection = PageableCollection;
            }
        },
        'backbone-bootstrap-modal': {
            deps: ['jquery', 'underscore', 'backbone'],
            exports: 'Backbone.BootstrapModal'
        },
        'backbone-forms': {
            deps: ['jquery', 'underscore', 'backbone'],
            exports: 'Backbone.Form'
        },
//		'backbone-forms-list' : {
//			deps : [ 'backbone-forms' ],
//			exports : 'Backbone.Form.editors.List'
//		},
        'backbone.paginator': {
            deps: ['underscore', 'backbone'],
            exports: 'PageableCollection'
        },
        'backbone-forms-bootstrap3': {
            deps: ['jquery', 'underscore', 'backbone', 'backbone-forms']
        },
        'backgrid-paginator': {
            deps: ['underscore', 'backbone', 'backgrid', 'backbone.paginator'],
            exports: 'Backgrid.Extension.Paginator'
        },
        'backgrid-moment': {
            deps: ['backgrid', 'moment'],
            exports: 'Backgrid.Extension.Moment'
        },
        'backgrid-text': {
            deps: ['backgrid'],
            exports: 'Backgrid.Extension.Text'
        },
//		'tether' : {
//			deps : [ 'tether-orig' ]
//		},
        'bootstrap': {
            deps: ['jquery', 'tether']
        },
        'calendar': {
            deps: ['jquery']
        },
        'bootstrap-markdown': {
            deps: ['jquery'],
            exports: 'Markdown'
        },
        'bootstrap-switch': {
            deps: ['jquery'],
        },
        'keymaster': {
            exports: 'key'
        },
        'async': {
            exports: 'async'
        },
        'restdude': {
            deps: ["i18n!nls/labels", "i18n!nls/labels-custom", 'underscore', 'handlebars', 'restdude-hbs', 'moment', 'backbone', 'backbone.paginator', 'backbone-forms', 'backbone-forms-bootstrap3', 'backbone-bootstrap-modal', 'backbone-forms-list', 'marionette', 'backgrid', 'backgrid-moment', 'backgrid-text', 'backgrid-paginator', 'bloodhound', 'typeahead', 'bootstrap-datetimepicker', 'bootstrap-switch', 'jquery-color', 'intlTelInput', 'q', 'chart'],
            exports: 'restdude',
        },

        'cookie': {
            deps: ['jquery']
        },
        'chai-jquery': {
            deps: ['jquery', 'chai']
        },
        'jquery-color': {
            deps: ['jquery'],
            exports: 'jQuery.Color'
        },

        'intlTelInputUtil': {
            deps: ['jquery']
        },
        'intlTelInput': {
            deps: ['intlTelInputUtil']
        },
    }

});

// r.js does only reads the above config. this one is
// merged with the first while on the browser by requirejs.
require.config({
    waitSeconds: 0,
    config: {
        i18n: {
            locale: localStorage.getItem('locale') || "de"
        }
    },
});
//Load our domain module and pass it to our definition function
require(['app']);
