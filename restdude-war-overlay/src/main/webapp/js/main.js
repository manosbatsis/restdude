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
    // main.js is the application entry point
    deps: ['main'],
    waitSeconds: 0,

    paths: {
        'alpaca': 'lib/alpaca/alpaca',
        'cookie': 'lib/jquery.cookie',
        'jquery': 'lib/jquery',
        'pace': 'lib/pace',
        'coreui-app': 'lib/coreui-app',
        'jquery-color': 'lib/jquery.color.plus-names',
        "intlTelInput": 'lib/intlTelInput',
        "intlTelInputUtil": 'lib/intlTelInputUtil',
        'q': 'lib/q',
        'underscore': 'lib/underscore',
        'underscore-string': 'lib/underscore-string',
        'underscore-inflection': 'lib/underscore-inflection',
        'backbone': 'lib/backbone',
        'resthub': 'lib/resthub/resthub',
        'localstorage': 'lib/localstorage',
        'text': 'lib/text',
        'i18n': 'lib/i18n',
        'pubsub': 'lib/resthub/pubsub',
        'bootstrap': 'lib/bootstrap',
        // tmp fix until bootstrap devs fix their module deps
        'tether-orig': 'lib/tether-orig',
        'tether': 'lib/tether',
        'backbone-validation-orig': 'lib/backbone-validation',
        'backbone-bootstrap-modal': 'lib/backbone-bootstrap-modal',
        'backbone-forms-list': 'lib/backbone-forms-list',
        'backbone-forms': 'lib/backbone-forms',
        'backbone-forms-bootstrap3': 'lib/backbone-forms-bootstrap3',
        'backbone-validation': 'lib/resthub/backbone-validation-ext',
        'backbone-queryparams': 'lib/backbone-queryparams',
        'backbone.paginator': 'lib/backbone.paginator',
        'backbone.radio': 'lib/backbone.radio',
        'marionette': 'lib/backbone.marionette',
        'bootstrap-datetimepicker': 'lib/bootstrap-datetimepicker',
        'bootstrap-markdown': 'lib/bootstrap-markdown',
        'bootstrap-switch': 'lib/bootstrap-switch',
        'handlebars-orig': 'lib/handlebars',
        'handlebars': 'lib/resthub/handlebars-helpers',
        'async': 'lib/async',
        'keymaster': 'lib/keymaster',
        'moment': 'lib/moment',
        'json2': 'lib/json2',
        'console': 'lib/resthub/console',
        'backgrid': 'lib/backgrid/backgrid',
        "backgrid-paginator": 'lib/backgrid/extensions/paginator/backgrid-paginator',
        "backgrid-moment": 'lib/backgrid/extensions/moment/backgrid-moment-cell',
        "backgrid-text": 'lib/backgrid/extensions/text/backgrid-text-cell',
        "restdude": 'lib/restdude',
        "restdude-hbs": 'lib/restdude-hbs',
        // Mocha testing
        'mocha': 'lib/mocha/mocha',
        'chai': 'lib/chai/chai',
        //'chai-jquery' : 'lib/chai/chai-jquery',
        'sinon': 'lib/sinon/chai-jquery',
        'calendar': 'lib/calendar',
        'select2': 'lib/select2',
        'raty': 'lib/jquery.raty-fa',
        'bootstrap-fileInput': 'lib/fileinput',
        'typeahead': 'lib/typeahead.jquery',
        'bloodhound': 'lib/bloodhound',
        'google-maps-loader': 'lib/google-maps-loader',
        'humanize': 'lib/humanize-duration',
        'chart': 'lib/Chart',
        'template': '../template',

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

        'underscore': {
            exports: '_'
        },
        'underscore-string': {
            deps: ['underscore']
        },
        'underscore-inflection': {
            deps: ['underscore']
        },
        'handlebars-orig': {
            exports: 'Handlebars'
        },

        'backbone': {
            deps: ['underscore'],
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
