/*
 * calipso-hub-webapp - A full stack, high level framework for lazy application hackers.
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
define(["lib/calipsolib/util", 'underscore', 'handlebars', 'moment'],
    function (Calipso, _, Handlebars, moment) {
        // register calipso helpers for handlebars
        Handlebars.registerHelper("getLocale", function (propName, options) {
            return Calipso.util.getLocale();
        });
        Handlebars.registerHelper("getUserDetailsProperty", function (propName, options) {
            var prop = "";
            if (Calipso.util.isAuthenticated()) {
                prop = Calipso.util.userDetails.get(propName);
            }
            return (prop);
        });
        Handlebars.registerHelper("getUserDetailsMetadatum", function (metaName, options) {
            var metaValue = "";
            if (Calipso.util.isAuthenticated() && Calipso.util.userDetails.get("metadata")) {
                metaValue = Calipso.util.userDetails.get("metadata")[metaName];
            }
            return (metaValue);
        });
        /**
         * Check if the loggedin user has any of the given roles. Any numberof roles can be passed to the helper.
         * @example
         *  {{#ifUserInRole "ROLE_MANAGER" "ROLE_ADMIN"}}  <p>User is either a Manager or an Administrator! </p>{{/ifUserInRole}}
         */
        // TODO: move these helpers to root scope
        // and replace _this.userDetails with Calipso.util.userDetails
        Handlebars.registerHelper("ifUserInRole", function () {
            var options = arguments[arguments.length - 1];
            // now get input roles, the ones to check for just a single match
            var inputRoles = [];
            for (var i = 0; i < arguments.length - 1; i++) {
                inputRoles.push(arguments[i]);
            }
            return Calipso.isUserInAnyRole(inputRoles) ? options.fn(this) : options.inverse(this);
        });

        /**
         * Check if the loggedin user has none of the given roles. Any numberof roles can be passed to the helper.
         * @example
         *  {{#ifUserInRole "ROLE_MANAGER" "ROLE_ADMIN"}}  <p>User is either a Manager or an Administrator! </p>{{/ifUserInRole}}
         */
        // TODO: move these helpers to root scope
        // and replace _this.userDetails with Calipso.util.userDetails
        Handlebars.registerHelper("ifUserNotInRole", function () {
            var options = arguments[arguments.length - 1];
            // now get input roles, the ones to check for just a single match
            var inputRoles = [];
            for (var i = 0; i < arguments.length - 1; i++) {
                inputRoles.push(arguments[i]);
            }
            return !Calipso.isUserInAnyRole(inputRoles) ? options.fn(this) : options.inverse(this);
        });

        /**
         * Calculate "from" now using the given date
         * @example {{momentFromNow someDate}}
         */
        Handlebars.registerHelper('momentFromNow', function (date) {
            return moment(date).fromNow();
        });

        /**
         * Calculate "from" now using the given date
         * @example {{momentFromNow someDate}}
         */
        Handlebars.registerHelper('momentDateTime', function (date) {
            return moment(date).format("MMMM Do YYYY, h:mm:ss a");
        });

        /**
         * Calculate "from" now using the given date
         * @example {{-+momentFromNow someDate}}
         */
        Handlebars.registerHelper('moment', function (date, format) {
            // "MMMM Do YYYY"
            return moment(date).format(format);
        });

        /**
         * @example
         * {{#ifLoggedIn}} <p>User is logged in! </p>{{/ifLoggedIn}}
         */
        Handlebars.registerHelper("ifLoggedIn", function (options) {
            var loggedIn = false;
            if (Calipso.util.isAuthenticated()) {
                loggedIn = true;
            }
            //consolelog("Helper ifLoggedIn returns "+loggedIn);
            return loggedIn ? options.fn(this) : options.inverse(this);
        });
        /**
         * @example
         * {{#ifLoggedOut}} <p>User is NOT logged in!</p> {{/ifLoggedOut}}
         */
        Handlebars.registerHelper("ifLoggedOut", function (options) {
            var loggedOut = true;
            if (Calipso.util.isAuthenticated()) {
                loggedOut = false;
            }
            //consolelog("Helper ifLoggedOut returns "+loggedOut);
            return loggedOut ? options.fn(this) : options.inverse(this);
        });

        Handlebars.registerHelper("configProperty", function (propertyName) {
            return Calipso.getConfigProperty(propertyName);
        });

        /**
         * Translates the given value or value.id by looking for a match
         * in the labels or labels.options for that path
         * @example
         * {{getValueLabel contextAttribute 'labels.path'}}
         */
        Handlebars.registerHelper('getValueLabel', function (value, labelsPath) {
            // if value exists
            if (!_.isNull(value) && !_.isUndefined(value)) {
                //normalize if necessary
                value = value instanceof Object && value.id ? value.id + "" : value + "";

                // get labels
                var labels = Calipso.util.getLabels(labelsPath);
                //normalize if necessary
                labels = labels && labels.options ? labels.options : labels;

                console.log("getValueLabel, value: " + value + ", labelsPath: " + labelsPath + ", labels: ");
                console.log(labels);

                // if labels exist
                if (labels) {

                    // try direct match, then search, then fallback
                    value = labels[value] || _.findWhere(labels, {
                            val: value
                        }) || value;

                }
            }
            return value;
        });

        // register a handlebars helper for menuentries
        Handlebars.registerHelper("baseUrl", function () {
            return Calipso.getBaseUrl();
        });
        Handlebars.registerHelper("menuEntries", function () {
            // console.log("menu entries...");

            var menuEntries = {};
            var modelTypesMap = Calipso.modelTypesMap;
            var modelType;
            for (var modelKey in modelTypesMap) {
                modelType = modelTypesMap[modelKey];
                //TODO
                if (true) {
                    menuEntries[modelType.getPathFragment()] = {
                        label: modelType.label,
                        modelKey: modelType.modelKey
                    };
                }
            }
            return (menuEntries);
        });

        // register comparison helper
        Handlebars.registerHelper('ifCond', function (v1, operator, v2, options) {

            switch (operator) {
                case '==':
                    return (v1 == v2) ? options.fn(this) : options.inverse(this);
                case '===':
                    return (v1 === v2) ? options.fn(this) : options.inverse(this);
                case '<':
                    return (v1 < v2) ? options.fn(this) : options.inverse(this);
                case '<=':
                    return (v1 <= v2) ? options.fn(this) : options.inverse(this);
                case '>':
                    return (v1 > v2) ? options.fn(this) : options.inverse(this);
                case '>=':
                    return (v1 >= v2) ? options.fn(this) : options.inverse(this);
                case '&&':
                    return (v1 && v2) ? options.fn(this) : options.inverse(this);
                case '||':
                    return (v1 || v2) ? options.fn(this) : options.inverse(this);
                default:
                    return options.inverse(this);
            }
        });
        return Handlebars;
    });
