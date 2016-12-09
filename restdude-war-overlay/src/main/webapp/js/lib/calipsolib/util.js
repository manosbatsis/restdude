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
/**
 * @exports module:Calipso
 */
define(
    ["i18n!nls/labels", "i18n!nls/labels-custom", 'underscore', 'handlebars', 'calipso-hbs', 'moment', 'backbone', 'backbone.paginator',
        'backbone-forms',
        'backbone-bootstrap-modal', 'backbone-forms-list', 'marionette', 'backgrid', 'backgrid-moment', 'backgrid-text', 'backgrid-paginator',
        /*'metis-menu', 'morris', */'bloodhound', 'typeahead', 'bootstrap-datetimepicker', 'bootstrap-switch', 'jquery-color', 'intlTelInput', 'q', 'chart'],
    function (labels, labelsCustom, _, Handlebars, calipsoTemplates, moment, Backbone, PageableCollection, BackboneForms,
              BackboneFormsBootstrapModal, BackboneFormsList, BackboneMarionette, Backgrid, BackgridMoment, BackgridText, BackgridPaginator,
              /*MetisMenu, */Morris, Bloodhoud, Typeahead, BackboneDatetimepicker, BootstrapSwitch, jqueryColor, intlTelInput, q, chartjs) {


        /**
         * Calipso namespace
         * @namespace
         */
        var Calipso = {
            /**
             * @namespace
             */
            config: {},
            /**
             * @namespace
             */
            util: {
                getLocale: function () {
                    var locale = localStorage.getItem('locale');
                    return locale;
                },
            },
            /**
             * @namespace
             */
            components: {},
            /**
             * @namespace
             */
            collection: {},
            /**
             * @namespace
             */
            model: {},
            /**
             * @namespace
             */
            customModel: {},
            /**
             * @namespace
             */
            view: {},
            /**
             * @namespace
             */
            controller: {},
            /**
             * @namespace
             */
            hbs: {},
            labels: $.extend(true, {}, labels, labelsCustom)
        };
        var Marionette = Backbone.Marionette;
        // plumbing for handlebars template helpers
        // Also provides i18n labels
        Marionette.View.prototype.mixinTemplateContext = function (target) {
            var self = this;
            var templateContext = Marionette.getOption(self, "templateContext");
            // add i18n labels from requirejs i18n
            var result = {
                labels: Calipso.util.getLabels(),
                useCase: self.useCaseContext,
            };
            if (self.model && self.model.getSuperClass()) {
                result.labels.models[self.model.getPathFragment()] = self.model.getLabels();
            }


            target = target || {};

            if (_.isFunction(templateContext)) {
                templateContext = templateContext.call(self);
            }

            // This _.each block is what we're adding
            _.each(templateContext, function (helper, index) {
                if (_.isFunction(helper)) {
                    result[index] = helper.call(self);
                } else {
                    result[index] = helper;
                }
            });

            return _.extend(target, result);
        };

        // baseComponents pretty much says how we want types to be exteneded, or rather,
        // how we want member hashes of super and subtypes to be merged instead of
        // replaced or explicitly repeated
        var baseComponents = {
            view: {
                "View": Backbone.Marionette.View,
                "CompositeView": Backbone.Marionette.CompositeView,
                "CollectionView": Backbone.Marionette.CollectionView,
            },
            model: {
                "Model": Backbone.Model
            },
            backboneform: {
                "Text": Backbone.Form.editors.Text
            }
        }
        _.each(baseComponents, function (packageComponents, packageName, list) {
            Calipso[packageName] || (Calipso[packageName] = {});
            _.each(packageComponents, function (BaseType, newClassName, list) {
                console.log("packageName: " + packageName + ", newClassName: " + newClassName)
                var extendOptions = {
                    initialize: function (models, options) {
                        BaseType.prototype.initialize.apply(this, arguments);
                    },
                    getTypeName: function () {
                        return this.constructor.getTypeName();
                    },
                    getSuperClass: function () {
                        return this.constructor.getSuperClass();
                    }
                };
                var extendStaticOptions = {
                    typeName: "Calipso." + packageName + "." + newClassName,
                    getTypeName: function () {
                        return this.typeName;
                    },
                    getSuperClass: function () {
                        return this.superClass;
                    }
                };

                // add view defaults
                if (packageName == "view") {
                    _.extend(extendOptions, {
                        taName: "div",
                        useCaseContext: null,
                        skipSrollToTop: false,
                        title: "",
                        message: "",
                        mergableOptions: ['useCaseContext', 'model', 'closeModalOnSync', 'regionPath', 'regionName', 'title', 'message', 'fields', 'formOptions'],
                        getTitle: function () {
                            return Marionette.getOption(this, "title");
                        },
                        getMessage: function () {
                            return Marionette.getOption(this, "message");
                        },
                        templateContext: {
                            viewId: function () {
                                return Marionette.getOption(this, "id");
                            },
                            viewTitle: function () {
                                return this.getTitle();
                            },
                            viewMessage: function () {
                                return this.getMessage();
                            },
                        },
                        initialize: function (options) {
                            BaseType.prototype.initialize.apply(this, arguments);
                            this.mergeOptions(options, this.mergableOptions);
                            if (!this.skipSrollToTop) {
                                $(window).scrollTop(0);
                            }
                        }
                    });
                    if (newClassName == "View") {
                        extendOptions.events = {
                            // TODO: move to layouts
                            "click .btn-social": "socialLogin",
                            // TODO: move to layouts
                            "click .open-modal-page": "openModalPage"
                        };
                        extendOptions.openModalPage = function (e) {
                            Calipso.stopEvent(e);
                            var $a = $(e.currentTarget);
                            var pageView = new Calipso.view.TemplateBasedItemView({
                                template: Calipso.getTemplate($a.data("page")),
                                tagName: "div"
                            });
                            // TODO

                            Calipso.vent.trigger('modal:showInLayout', {
                                view: pageView,
                                title: $a.data("title")
                            });

                        };
                        extendOptions.socialLogin = function (e) {
                            Calipso.socialLogin(e);
                        };
                        extendOptions.addRegions = function (regions) {
                            var _this = this;
                            BaseType.prototype.addRegions.apply(this, arguments);
                            _.each(_.keys(regions), function (regionName) {
                                _this.briefRegion(regionName);
                            });
                        };
                        extendOptions.briefRegion = function (regionName) {
                            var region = this.getRegion(regionName);
                            region.regionName = regionName;
                            region.regionPath = this.getRegionPath() + "." + regionName;
                        };
                        extendOptions.regionPath = null;
                        extendOptions.getRegionPath = function () {
                            return this.regionPath;
                        };
                    }
                }

                Calipso[packageName][newClassName] = BaseType.extend(extendOptions, extendStaticOptions);
                /**
                 * Encance the extend function to add a reference to super and properly extend events
                 */
                Calipso[packageName][newClassName].extend = function (protoProps, staticProps) {
                    staticProps || (staticProps = {});
                    var _this = staticProps.superClass = this;

                    //	return Backbone.Collection.prototype.fetch.call(this, options);
                    var NewClass = BaseType.extend.call(this, protoProps, staticProps);
                    // properly extend prototype hashes like events
                    _.each(["events", "triggers", "templateContext"], function (mergableProp) {
                        if (_this.prototype[mergableProp]) {
                            NewClass.prototype[mergableProp] = _.extend({}, _this.prototype[mergableProp], protoProps[mergableProp] || {});
                        }
                    });
                    _.each(["mergableOptions"], function (mergableProp) {
                        if (_this.prototype[mergableProp]) {
                            NewClass.prototype[mergableProp] = $.merge(_this.prototype[mergableProp], protoProps[mergableProp] || []);
                        }
                    });
                    // let the conditioned kiddo through
                    return NewClass;
                };
            });
        });

        // default locale is set in
        moment.locale(Calipso.util.getLocale());

        Calipso.util.getLabels = function (path) {
            // return a copy, not the actual object
            var labels;
            if (!path) {
                labels = Calipso.labels;
            } else {
                labels = Calipso.getPathValue(Calipso.labels, path, {})
            }
            ;
            return JSON.parse(JSON.stringify(labels));
        }
        Calipso.util.getLabel = function (path, labels) {
            // return a copy, not the actual object
            labels || (labels = Calipso.labels);
            if (!path) {
                throw "Apath is required"
            }
            var s = Calipso.getPathValue(labels, path, null);
            return _.isString(s) ? s : null;
        }
        // Get the DOM manipulator for later use
        Calipso.$ = Backbone.$;
        Calipso.decodeParamRegex = /\+/g;
        Calipso.decodeParam = function (s) {
            return decodeURIComponent(s.replace(Calipso.decodeParamRegex, " "));
        };


        Calipso.isSpecificValue = function (val) {
            return (val instanceof Date
                || val instanceof RegExp
            ) ? true : false;
        }

        Calipso.cloneSpecificValue = function (val) {
            if (val instanceof Date) {
                return new Date(val.getTime());
            } else if (val instanceof RegExp) {
                return new RegExp(val);
            } else {
                throw new Error('Unexpected situation');
            }
        }

        /**
         * Recursive cloning array.
         */
        Calipso.deepCloneArray = function (arr) {
            var clone = [];
            arr.forEach(function (item, index) {
                if (typeof item === 'object' && item !== null) {
                    if (Array.isArray(item)) {
                        clone[index] = Calipso.deepCloneArray(item);
                    } else if (Calipso.isSpecificValue(item)) {
                        clone[index] = Calipso.cloneSpecificValue(item);
                    } else {
                        clone[index] = Calipso.deepExtend({}, item);
                    }
                } else {
                    clone[index] = item;
                }
            });
            return clone;
        }

        /**
         * Extening object that entered in first argument.
         *
         * Returns extended object or false if have no target object or incorrect type.
         *
         * If you wish to clone source object (without modify it), just use empty new
         * object as first argument, like this:
         *   deepExtend({}, yourObj_1, [yourObj_N]);
         */
        Calipso.deepExtend = function (/*obj_1, [obj_2], [obj_N]*/) {
            if (arguments.length < 1 || typeof arguments[0] !== 'object') {
                return false;
            }
            if (arguments.length < 2) {
                return arguments[0];
            }
            var target = arguments[0];
            // convert arguments to array and cut off target object
            var args = Array.prototype.slice.call(arguments, 1);
            var val, src, clone;

            args.forEach(function (obj) {
                // skip argument if it is array or isn't object
                if (typeof obj !== 'object' || Array.isArray(obj)) {
                    return;
                }

                Object.keys(obj).forEach(function (key) {
                    src = target[key]; // source value
                    val = obj[key]; // new value
                    // recursion prevention
                    if (val === target) {
                        return;
                    }
                    // reuse object references of platform components
                    else if (val && (val.extend || _.isFunction(val))) {
                        target[key] = val;
                        return;
                        /**
                         * if new value isn't object then just overwrite by new value
                         * instead of extending.
                         */
                    } else if (typeof val !== 'object' || val === null) {
                        target[key] = val;
                        return;

                        // just clone arrays (and recursive clone objects inside)
                    } else if (Array.isArray(val)) {
                        target[key] = val;//Calipso.deepCloneArray(val);
                        return;

                        // custom cloning and overwrite for specific objects
                    } else if (Calipso.isSpecificValue(val)) {
                        target[key] = Calipso.cloneSpecificValue(val);
                        return;

                        // overwrite by new value if source isn't object or array
                    } else if (typeof src !== 'object' || src === null || Array.isArray(src)) {
                        target[key] = Calipso.deepExtend({}, val);
                        return;
                        // source value and new value is objects both, extending...
                    } else {
                        target[key] = Calipso.deepExtend(src, val);
                        return;
                    }
                });
            });

            return target;
        }

        Calipso.getHttpUrlParams = function (url) {
            var urlParams = {};
            if (!url) {
                url = window.location.href;
                url = url.indexOf("?") > -1 ? url.substring(url.indexOf("?") + 1) : "";
            }
            var keyValuePairs = url.split('&');
            for (var i in keyValuePairs) {
                var keyValuePair = keyValuePairs[i].split('=');
                urlParams[Calipso.decodeParam(keyValuePair[0])] = (keyValuePair.length > 1) ? this.decodeParam(keyValuePair[1]) : null;
            }
            delete urlParams[""];
            return urlParams;
        };


        $(document).ajaxError(function (event, request, settings, error) {
            Calipso.stopEvent(event);
            console.log("Ajax error, url: " + settings.url + ", error: " + error);
        });
        Calipso.getDefaultFetchOptions = function () {
            return {
                // use traditional HTTP params
                traditional: true,
                // handle status codes
                statusCode: {
                    401: function () {
                        //Calipso.loginIfUnauthorized();
                    },
                    403: function () {
                        //Calipso.loginIfUnauthorized();
                    }
                }
            };

        }

        /**
         * Utility method to stop events.
         * @param  {event} e
         * @return {void}
         */
        Calipso.stopEvent = function (e) {
            var event = e ? e : window.event;
            if (event.preventDefault) {
                event.preventDefault();
            } else {
                event.returnValue = false;
                event.stop();
            }
            if (event.stopPropagation) {
                event.stopPropagation();
            } else {
                event.cancelBubble = true;
            }
        };
        /**
         * Update bootstrap badges
         * @param  {[String]} the jquery selector to use
         * @param  {[stStringring]} the new badge text
         * @return {[type]}
         */
        Calipso.updateBadges = function (selector, text) {
            // e.g. update visual notification counters
            //console.log("Notifications count: " + text);
            if (text) {
                //console.log("Showing notification counters...");
                $(selector).text(text).removeClass("hidden").show();
            } else {
                //console.log("Hiding notification counters...");
                $(selector).text(text).hide();
            }
        };
        /**
         * Get a conbfiguration property
         * @param  {[String]} the property name
         * @return {[type]}
         */
        Calipso.getConfigProperty = function (propertyName, defaultValue) {
            return Calipso.getPathValue(Calipso.config, propertyName, defaultValue);
        };
        Calipso._chartColors = ["91, 144, 191", "163, 190, 140", "171, 121, 103", "208, 135, 112", "180, 142, 173", "235, 203, 139", "39, 165, 218", "250, 164, 58", "96, 189, 104", "241, 124, 176", "178, 145, 47", "178, 118, 178", "222, 207, 63", "241, 88, 84", "77, 77, 77", "0, 0, 0",];
        Calipso.getThemeColor = function (index) {
            if (index + 1 > Calipso._chartColors.length) {
                throw "No more colours supported";
            }
            return Calipso._chartColors[index];
        };
        /**
         * Get a conbfiguration property
         * @param  {[String]} the property name
         * @return {[type]}
         */
        Calipso.socialLogin = function (e) {
            Calipso.stopEvent(e);
            var clicked = $(e.currentTarget);

            var providerNames = ["facebook", "linkedin", "twitter", "google"];
            var providerName;

            for (var i = 0; i < providerNames.length; i++) {
                if (clicked.hasClass("btn-" + providerNames[i])) {
                    providerName = providerNames[i];
                    break;
                }
            }

            if (!providerName) {
                throw "Clicked element does not match a social provider";
            }
            // target='SignIn'
            var formHtml = "<form class='social-signin-form' action='" + Calipso.getBaseUrl() +
                "/api/auth/oauth/signin/" + providerName + "' method='POST' role='form'>" +
                //"<input type='hidden' name='scope' value='email' />" +
                //"<input type='hidden' name='scope' value='emailure' />" +
                //"<input type='hidden' name='topWindowDomain' value='" + window.location.hostname + "' />" +
                "</form>";
            $("div.social-form-container").html(formHtml);
            $("form.social-signin-form").submit();
            return false;
        };
        /**
         * Change the user locale and reload
         * @param  {[String]} the desired locale
         */
        Calipso.changeLocale = function (newLocale) {
            if (newLocale) {
                var currentLocale = localStorage.getItem('locale');
                if (!currentLocale || currentLocale != newLocale) {
                    var applyLocale = function () {
                        localStorage.setItem('locale', newLocale);
                        window.location.reload();
                    }
                    // if logged in user, persist locale settings
                    if (Calipso.util.isAuthenticated()) {
                        var userModel = new Calipso.model.UserModel({
                            id: Calipso.session.userDetails.get("id")
                        });
                        userModel.save({locale: newLocale}, {
                            success: function (model, response) {
                                applyLocale();
                            },
                            error: function () {
                                alert("Failed updating locale preferences");
                                applyLocale();
                            }
                        });
                    }
                    // if anonymous user, just apply and reload
                    else {
                        applyLocale();
                    }
                }
            }
        };
        /**
         * Use the MainRouter to navigate to the given route
         * @param  {[String]} the route URL
         * @param  {[Object]} the options hash
         * @return {[type]}
         */
        Calipso.navigate = function (url, options) {
            Calipso.app.routers["MainRouter"].navigate(url, options);
        };

        Calipso.walk = function (currentStepValue, pathSteps, stepIndex) {
            var value;
            if (stepIndex == undefined) {
                stepIndex = 0;
            }
            var propName = pathSteps[stepIndex];
            if (currentStepValue && propName) {
                value = Calipso.getObjectProperty(currentStepValue, propName);
                stepIndex++;
                if (value && stepIndex < pathSteps.length) {
                    value = Calipso.walk(value, pathSteps, stepIndex);
                }
            }
            return value;
        };

        Calipso.setPathValue = function (obj, path, value) {
            var pathOrig = path;
            if (path.indexOf(".") >= 0 || path.indexOf("[") >= 0) {
                path = path.replace(/\[(.*?)\]/g, '.$1');
            }
            var steps = path.split(".");
            var targetProp = steps.pop();
            if (steps.length > 0) {
                obj = Calipso.walk(obj, steps);
            }
            if (!obj) {
                throw "Calipso.setPathValue: invalid path " + pathOrig;
            }
            if (obj.set) {
                obj.set(targetProp, value);
            } else {
                obj[targetProp] = value;
            }
        };
        Calipso.getPathValue = function (obj, path, defaultValue) {
            if (path.indexOf(".") >= 0 || path.indexOf("[") >= 0) {
                path = path.replace(/\[(.*?)\]/g, '.$1');
            }
            var value = Calipso.walk(obj, path.split("."));
            if (defaultValue && (_.isUndefined(value) || _.isNull(value))) {
                value = defaultValue;
            }
            return value;
        };

        Calipso.getObjectProperty = function (obj, propName, defaultValue) {
            var prop;
            if (obj) {
                if (obj.get && !_.isUndefined(obj.get(propName))) {
                    prop = obj.get(propName);
                } else if (!_.isUndefined(obj[propName])) {
                    prop = obj[propName];
                } else if (!_.isUndefined(defaultValue)) {
                    prop = defaultValue;
                }
            }
            return prop;
        };
        Calipso.getTemplate = function (name) {
            return calipsoTemplates[name];
        }
        Calipso.isUserInAnyRole = function (inputRoles) {
            var hasRole = false;
            if (!_.isArray(inputRoles)) {
                inputRoles = [inputRoles];
            }
            // only process if the user is authenticated
            if (Calipso.session.userDetails) {
                var ownedRoles = Calipso.session.getRoles();
                var inputRole;
                for (var j = 0; j < inputRoles.length && hasRole == false; j++) {
                    inputRole = inputRoles[j];
                    for (var k = 0; k < ownedRoles.length && hasRole == false; k++) {
                        var ownedRole = ownedRoles[k];
                        if (inputRole == ownedRole.name) {
                            hasRole = true;
                        }
                    }
                }
            }
            return hasRole;
        }


        Calipso.util.isAuthenticated = function () {
            return Calipso.session && Calipso.session.isAuthenticated();
        }

        /*****************************************************
         Applicartion initialization
         *****************************************************/
        Calipso._initializeVent = function () {
            Calipso.vent = Backbone.Radio.channel('calipso');
            Calipso.vent.on('domain:show', function (appView, navigateToUrl) {

                var $wrapper = $("#container");
                if (appView.containerClass && $wrapper && appView.containerClass != $wrapper.attr("class")) {
                    $wrapper.attr("class", appView.containerClass);
                }
                Calipso.app.view.showChildView("mainContentRegion", appView);
            });

            Calipso.vent.on('session:social-popup', function (providerId) {
                // remove any pre-existing form
                $("#calipso-social-signin-form").remove();
                var oForm = document.getElementById("calipso-social-signin-form");
                var wrapper = $('#hiddenWrapper');
                // create form if it doesn't exist
                if (!oForm) {
                    var formHtml = "<form id='calipso-social-signin-form' action='" + window.calipsoBasePath + "/signin/" + providerId + "'  method='POST' role='form'>" +
                        //"<input type='hidden' name='scope' value='email' />" +
                        //"<input type='hidden' name='scope' value='emailure' />" +
                        "<input type='hidden' name='topWindowDomain' value='" + window.location.hostname + "' />" + "</form>";
                    wrapper.append(formHtml);
                    oForm = document.getElementById("calipso-social-signin-form");
                }
                // figure out position
                var w = 500;
                var h = 400;
                // Fixes dual-screen position for most browsers
                var dualScreenLeft = window.screenLeft != undefined ? window.screenLeft : screen.left;
                var dualScreenTop = window.screenTop != undefined ? window.screenTop : screen.top;
                var left = ((screen.width / 2) - (w / 2)) + dualScreenLeft;
                var top = ((screen.height / 2) - (h / 2)) + dualScreenTop;
                // open popup window and POST to it
                var calipsoSocialSignInWin = window.open("", "SignIn", "width=" + w + ",height=" + h + ",toolbar=0,scrollbars=0,status=0,resizable=0,location=0,menuBar=0,left=" + left + ",top=" + top);

                var interval = window.setInterval(function () {
                    //console.log("in interval");
                    try {
                        if (calipsoSocialSignInWin == null || calipsoSocialSignInWin.closed) {
                            //console.log("window closed");
                            window.clearInterval(interval);
                            //closeCallback(win);
                            calipso.tryRememberMe();
                        }
                    } catch (e) {
                    }
                }, 1000);

                calipsoSocialSignInWin.focus();
                oForm.submit();
                // remove form
                $("#calipso-social-signin-form").remove();
                return false;

            });
            Calipso.vent.on('session:destroy', function (userDetails) {
                Calipso.session.destroy();
                Calipso.app.updateHeaderFooter();
                Calipso.navigate("home", {
                    trigger: true
                });
            });
            Calipso.vent.on('nav-menu:change', function (modelkey) {
                // console.log("vent event nav-menu:change");
            });

            Calipso.vent.on('modal:show', function (view) {
                Calipso.app.view.getRegion("modalRegion").show(view);
            });

            /**
             * @example Calipso.vent.trigger('modal:showInLayout', {view: someView, template: someTemplate, title: "My title"});
             */
            Calipso.vent.on('modal:showInLayout', function (properties) {
                // make sure a view is provided
                if (!properties.view) {
                    throw "A 'view' property is required on vent trigger 'modal:showInLayout'.";
                }
                // assemble properties
                var layoutProperties = {
                    childView: properties.view,
                    model: properties.model,
                    title: properties.title
                };
                if (!properties.template) {
                    layoutProperties.template = properties.template;
                }
                // show
                var modalLayoutView = new Calipso.view.ModalLayout(layoutProperties);
                Calipso.app.view.showChildView("modalRegion", modalLayoutView);
            });

            Calipso.vent.on('modal:showUseCaseContext', function (useCaseContext) {
                var layoutProperties = {
                    useCaseContext: useCaseContext,
                    childView: useCaseContext.createView({modal: true}),
                    model: useCaseContext.model,
                };
                /*if (!properties.template) {
                 layoutProperties.template = properties.template;
                 }*/
                // show
                Calipso.app.view.showChildView("modalRegion", new Calipso.view.DefaulfModalLayout(layoutProperties));
            });


            Calipso.vent.on('modal:destroy', function () {
                // console.log("vent event modal:destroy");
                Calipso.app.view.getRegion("modalRegion").closeModal();
            });
        }

        Calipso._initializeAppConfig = function (customConfig) {
            // set Calipso.config object
            customConfig = customConfig || {};
            var config = {
                appName: "Calipso",
                footer: "Copyright 2016 Geekologue",
                contextPath: "/",
                //apiAuthPath: "/api/auth",
                headerViewType: Calipso.view.HeaderView,
                sidebarViewType: Calipso.view.SidebarView,
                footerViewType: Calipso.view.FooterView,
                sessionType: Calipso.util.Session,
                // URL > Constructor map
                useCaseFactories: {},
            };
            Calipso.config = _.defaults(customConfig, config);
        }


        Calipso._startHistory = function () {

            var pushStateSupported = _.isFunction(history.pushState);
            var contextPath = Calipso.getConfigProperty("contextPath");

            // console.log("Calipso.domain.on start, contextPath: " + contextPath);
            if (contextPath.length > 1) {
                // add leading slash if missing
                if (contextPath.indexOf("/") != 0) {
                    //console.log("Calipso.domain.on start, adding slash prefix");
                    contextPath = "/" + contextPath;
                }
                // add ending slash if missing
                if (contextPath.substr(-1) != '/') {
                    //console.log("Calipso.domain.on start, adding slash suffix");
                    contextPath += '/';
                }

            }
            var startRoot = contextPath + "client/";
            Backbone.history.start({
                root: startRoot,
                pushState: pushStateSupported
            });
        }

        Calipso.stripHtml = function (s) {
            return s ? s.replace(/<[^>]+>/g, "") : s;
        };

        Calipso.Application = Marionette.Application.extend({
            config: {},
            started: false,
            browseMenu: {},
            region: "body",
            regionClass: Backbone.Marionette.Region.extend({
                el: 'body',
                replaceElement: true
            }),
            mainRouter: null,
            routers: {},
            onBeforeStart: function (onBeforeStartOptions) {
                onBeforeStartOptions || (onBeforeStartOptions = {});
                var options = onBeforeStartOptions.options || {};
                //Marionette.Application.prototype.onBeforeStart.apply(this, arguments);
                var _this = this;
                // set routers map
                _(options.routers).each(function (routerClass) {
                    var router = new routerClass();
                    _this.routers[routerClass.getTypeName()] = router;
                    if (!_this.mainRouter) {
                        _this.mainRouter = router;
                    }
                });
                console.log("Calipso.Application#onBeforeStart, configured routers: ");
                console.log(_this.routers);
                // set model types map
                Calipso.useCaseFactoriesMap = _.extend({}, Calipso.config.useCaseFactories || {});
                var allModelLabels = Calipso.util.getLabels("models");
                var parseModel = function (ModelType) {
                    // setup model-based usecase factories
                    if (ModelType.getTypeName() != "Calipso.model.Model" &&
                        ModelType.getTypeName() != "Calipso.model.UserRegistrationModel" &&
                        ModelType.getTypeName() != "Calipso.model.GenericModel") {
                        Calipso.useCaseFactoriesMap[ModelType.viewFragment ? ModelType.viewFragment : ModelType.getPathFragment()] = ModelType;
                    }
                };
                _(Calipso.model).each(parseModel);
                _(Calipso.customModel).each(parseModel);
            },
            onStart: function () {
                this.view = new Calipso.view.AppRootView();
                this.showView(this.view);
                //console.log("Calipso.domain started");
                this.updateHeaderFooter();
                // setup vent
                Calipso._initializeVent();
                // start backbone history
                Calipso._startHistory();
                // flag started
                this.started = true;
            },
            initialize: function (options) {
                Marionette.Application.prototype.initialize.apply(this, arguments);

            },
            isStarted: function () {
                return this.started;
            },
            updateHeaderFooter: function () {
                // prepare browse menu
                Calipso.session.userDetails.buildBrowseMenu();
                // render basic structure
                this.view.showChildView("headerRegion",
                    new (Calipso.getConfigProperty("headerViewType"))({
                        model: Calipso.session.userDetails
                    }));

                // add/remove navbar
                if (Calipso.util.isAuthenticated() && Calipso.session.userDetails.get("browseMenu")) {
                    $("body").addClass("sidebar-nav");
                    $(".layout-toggler").show();
                }
                else {
                    $("body").removeClass("sidebar-nav");
                    $(".layout-toggler").hide();
                }
                this.view.showChildView("sidebarRegion",
                    new (Calipso.getConfigProperty("sidebarViewType"))({
                        model: Calipso.session.userDetails
                    }));

                this.view.showChildView("footerRegion",
                    new (Calipso.getConfigProperty("footerViewType"))({
                        model: Calipso.session.userDetails
                    }));
            },
        });

        Calipso.start = function (initOptions) {
            // prepare config
            Calipso._initializeAppConfig(initOptions);
            // init session
            var SessionType = Calipso.getConfigProperty("sessionType");
            Calipso.session = new SessionType();
            // set Calipso.domain object
            Calipso.app = new Calipso.Application(Calipso.config);
            // start it
            Calipso.session.start();
        }

        // //////////////////////////////////////
        // Region
        // //////////////////////////////////////
        Calipso.view.ModalRegion = Marionette.Region.extend(/** @lends Calipso.view.ModalRegion.prototype */
            {
                el: "#calipsoModalRegion",
                initialize: function (options) {

                    // listen to the modal region
                    var showHandler = function (e) {
                        $('.modal .modal-body').css('overflow-y', 'auto').css('max-height', $(window).height() * 0.7).find('input[type=text],textarea,select').filter(':visible:enabled:first').focus();
                    };

                    var $el = $(this.el);
                    $el.on('shown.bs.modal', showHandler);
                    $el.on('show.bs.modal', showHandler);
                },
                onShow: function (view, region, options) {
                    //view.on("destroy", this.hideModal, this);
                    this.$el.modal('show');
                },
                hideModal: function () {
                    this.$el.modal('hide');
                },
                closeModal: function () {
                    this.hideModal();
                    this.currentView.destroy();
                    this.reset();
                }
            });

        // //////////////////////////////////////
        // Collection
        // //////////////////////////////////////
        Calipso.collection.GenericCollection = PageableCollection.extend(
            /** @lends Calipso.collection.GenericCollection.prototype */
            {
                mode: "server",
                data: {},

                /**
                 * Initial pagination states
                 */
                state: {
                    /**
                     * The first page index. Set to 0 if your server API uses 0-based indices.
                     */
                    firstPage: 0,
                    currentPage: 0,
                    pageSize: 10,
                },
                /**
                 A translation map to convert PageableCollection state attributes
                 to the query parameters accepted by your server API.

                 You can override the default state by extending this class or specifying
                 them in `options.queryParams` object hash to the constructor.

                 @property {Object} queryParams
                 @property {string} [queryParams.currentPage="number"]
                 @property {string} [queryParams.pageSize="size"]
                 @property {string} [queryParams.totalPages="totalPages"]
                 @property {string} [queryParams.totalRecords="totalElements"]
                 @property {string} [queryParams.sortKey="properties"]
                 @property {string} [queryParams.order="sort"]
                 @property {string} [queryParams.directions={"-1": "ASC", "1": "DESC"}] A
                 map for translating a PageableCollection#state.order constant to
                 the ones your server API accepts.
                 */
                queryParams: {
                    totalPages: "totalPages",
                    pageSize: "size",
                    currentPage: "page",
                    totalRecords: "totalElements",
                    sortKey: "properties",
                    order: "direction",//"direction"?
                    directions: {
                        "-1": "ASC",
                        "1": "DESC"
                    }
                },
                getTypeName: function () {
                    return this.constructor.getTypeName();
                },
                initialize: function (attributes, options) {
                    PageableCollection.prototype.initialize.apply(this, arguments);
                    options || (options = {});
                    if (options.model && options.model.getTypeName()) {
                        this.model = options.model;
                    } else {
                        throw "GenericCollection#initialize: options.model is required and must be a subtype of Genericmodel";
                    }
                    if (!options.url) {
                        this.url = Calipso.getBaseUrl() + '/api/rest/' + this.model.getPathFragment();
                    }
                    // use given grid columns if provided, or the
                    // default model columns otherwise
                    if (options.schemaForGrid) {
                        this.schemaForGrid = options.schemaForGrid;
                    }

                    if (options.data) {
                        if (options.data[""] || options.data[""] == null) {
                            delete options.data[""];
                        }
                        this.data = options.data;
                        this.state = this.parseState(this.data, this.queryParams, this.state, {});

                    }

                    if (options.url) {
                        this.url = options.url;
                    }
                },
                fetch: function (options) {
                    if (options && options.data) {
                        this.state = this.parseState(options.data, this.queryParams, this.state, {});
                    }
                    return PageableCollection.prototype.fetch.apply(this, arguments);
                },
                hasCriteria: function () {
                    var hasCriteria = false;
                    var ignoredCriteria = ["page", "size", "direction"];
                    for (var i = 0; i < ignoredCriteria.length; i++) {
                        if (this.data[ignoredCriteria[i]] != undefined) {
                            hasCriteria = true;
                        }
                    }
                    return hasCriteria;
                },
                getGridSchema: function () {
                    // use explicit configuration if available
                    var configuredSchema = this.schemaForGrid;
                    // try obtaining the grid schema from the model otherwise
                    if (!configuredSchema && this.model && this.model.getGridSchema) {
                        configuredSchema = this.model.getGridSchema();
                    }

                    // ensure proper configuration is available
                    if (!configuredSchema) {
                        throw "A grid schema has not been given and the collection model does not offer one or is undefined";
                    }
                    return configuredSchema;
                },
                getPathFragment: function () {
                    return this.constructor.getPathFragment();
                },
                parseState: function (resp, queryParams, state, options) {
                    if (resp) {
                        var newState = _.clone(state);
                        var serverState = resp;

                        var intKeys = ["firstPage", "currentPage", "pageSize", "totalPages", "totalRecords"];
                        _.each(_.pairs(_.omit(queryParams, "directions")), function (kvp) {
                            var k = kvp[0], v = kvp[1];
                            var serverVal = serverState[v];
                            if (!_.isUndefined(serverVal) && !_.isNull(serverVal)) {
                                newState[k] = serverVal;
                                // enforce integers when applicable

                                if ($.inArray(k, intKeys) > -1) {
                                    if (typeof serverVal == "string") {
                                        newState[k] = parseInt(serverVal) || 0;
                                    }
                                }
                            }
                        });
                        //ize:10, number:0, sort:[{direction:"ASC", property:"price", ignoreCase:false, ascending:true}],
                        // totalElements:10, lastPage:true, totalPages:1, numberOfElements:10, firstPage:true})

                        if (serverState.order) {
                            newState.order = _.invert(queryParams.directions)[serverState.order.toUpperCase()] * 1;
                        } else if (serverState.sort && serverState.sort.direction) {
                            newState.order = _.invert(queryParams.directions)[serverState.sort.direction.toUpperCase()] * 1;
                        } else if (serverState.direction) {
                            newState.order = _.invert(queryParams.directions)[serverState.direction.toUpperCase()] * 1;
                        }

                        return newState;
                    }
                },
                parseRecords: function (response, options) {
                    var _self = this;
                    var itemsArray = response;
                    if (response.content) {
                        itemsArray = response.content;
                    } else {
                        this.total = itemsArray.length;
                        this.totalPages = 1;
                    }
                    return itemsArray;
                }

            });
        /**
         * Get the name of this class
         * @returns the class name as a string
         */
        Calipso.collection.GenericCollection.getTypeName = function (instance) {
            return "Calipso.collection.GenericCollection";
        };

        Calipso.collection.AllCollection = Backbone.Collection.extend({
            initialize: function (attributes, options) {
                if (options) {
                    if (options.url) {
                        this.url = options.url;
                    }
                }
            },
            fetch: function (options) {
                options || (options = {});
                var data = (options.data || {});
                options.data = {
                    page: "no"
                };
                return Backbone.Collection.prototype.fetch.call(this, options);
            }

        });
        Calipso.collection.AllCollection.getTypeName = function (instance) {
            return "Calipso.collection.AllCollection";
        };
        /**
         *
         * backbone-polling v1.0.0
         * https://github.com/pedrocatre/backbone-polling
         *
         * Copyright (c) 2013 Pedro Catré
         *
         * Licensed under the MIT License
         */
        Calipso.collection.PollingCollection = Calipso.collection.GenericCollection.extend(/** @lends Calipso.collection.PollingCollection.prototype */
            {

                /**
                 * Id returned by the setTimeout function that the plugin uses to specify a delay between fetch requests to the
                 * data source
                 */
                _backbonePollTimeoutId: undefined,

                /**
                 * Control variable used to stop fetch requests
                 */
                _backbonePollEnabled: false,

                /**
                 * Default settings for the plugin
                 */
                _backbonePollSettings: {
                    refresh: 60000, // rate at which the plugin fetches data, default one minute
                    fetchOptions: Calipso.getDefaultFetchOptions(), // options for the fetch request
                    retryRequestOnFetchFail: true
                    // automatically retry request on fetch failure
                },

                /**
                 * Specify custom options for the plugin
                 * @param pollOptions object used to customize the plugin’s behavior
                 */
                configure: function (pollOptions) {
                    this._backbonePollSettings = $.extend(true, {}, this._backbonePollSettings, pollOptions);
                },

                /**
                 * Starts the process of polling data from the server
                 * @returns {*}
                 */
                startFetching: function () {
                    this._backbonePollEnabled = true;
                    this._refresh(1);
                    return this;
                },

                /**
                 * Periodically fetch data from a data source
                 * @param refreshRateMs rate in milliseconds at which the plugin fetches data
                 * @returns {*}
                 * @private
                 */
                _refresh: function (refreshRateMs) {
                    this._backbonePollTimeoutId = setTimeout(_.bind(function () {
                        if (this._backbonePollTimeoutId) {
                            clearTimeout(this._backbonePollTimeoutId);
                        }
                        // Return if _refresh was called but the fetching is stopped
                        // should not go this far since the timeout is cleared when fetching is stopped.
                        if (!this._backbonePollEnabled) {
                            return;
                        }

                        this.fetchRequest = this.fetch(this._backbonePollSettings.fetchOptions);

                        this.fetchRequest.done(_.bind(function () {
                            this.trigger('refresh:loaded');
                            this._refresh(this._backbonePollSettings.refresh);
                        }, this)).fail(_.bind(function () {
                            this.trigger('refresh:fail');

                            // If retryRequestOnFetchFail is true automatically retry request
                            if (this._backbonePollSettings.retryRequestOnFetchFail) {
                                this._refresh(this._backbonePollSettings.refresh);
                            } else {
                                this.stopFetching();
                            }
                        }, this)).always(_.bind(function () {
                            this.trigger('refresh:always');
                        }, this));
                    }, this), refreshRateMs);
                    return this;
                },

                /**
                 * Abort pending fetch requests
                 * @returns {*}
                 */
                abortPendingFetchRequests: function () {
                    if (!_.isUndefined(this.fetchRequest) && !_.isUndefined(this.fetchRequest['abort'])) {
                        this.fetchRequest.abort();
                    }
                    return this;
                },

                /**
                 * Checks to see if the plugin is polling data from a data source
                 * @returns {boolean} true if is fetching, false if it is not fetching
                 */
                isFetching: function () {
                    return !(_.isUndefined(this._backbonePollTimeoutId));
                },

                /**
                 * Stops the process of polling data from the server
                 * @returns {*}
                 */
                stopFetching: function () {
                    this._backbonePollEnabled = false;
                    if (this.isFetching()) {
                        if (this._backbonePollTimeoutId) {
                            clearTimeout(this._backbonePollTimeoutId);
                        }
                        this._backbonePollTimeoutId = undefined;
                    }
                    this.abortPendingFetchRequests();
                    return this;
                }

            });

        /**
         * Get the name of this class
         * @returns the class name as a string
         */
        Calipso.collection.PollingCollection.getTypeName = function (instance) {
            return "Calipso.collection.PollingCollection";
        };


        Calipso._baseUrl = false;
        Calipso.getBaseUrl = function () {
            if (!Calipso._baseUrl) {
                var calipsoMainScript = document.getElementById("calipso-script-main");
                // calipso in host page
                if (calipsoMainScript) {
                    var basePathEnd = calipsoMainScript.src.indexOf("/js/lib/require.js");
                    Calipso._baseUrl = calipsoMainScript.src.substring(0, basePathEnd);
                } else {
                    // fallback, will only work with a root web application context
                    Calipso._baseUrl = window.location.protocol + "//" + window.location.host;
                }
            }
            return Calipso._baseUrl;
        }


        /**
         * Get the model type corresponding to the given
         * business key/URI componenent
         */
        Calipso.util.getUseCaseFactory = function (modelTypeKey) {
            var d = $.Deferred();
            if (Calipso.useCaseFactoriesMap[modelTypeKey]) {
                d.resolve(Calipso.useCaseFactoriesMap[modelTypeKey]);
            }
            else {
                $.get(Calipso.getBaseUrl() + '/api/rest/' + modelTypeKey + "/uischema").done(function (staticMembers) {
                    staticMembers.pathFragment = modelTypeKey;
                    Calipso.useCaseFactoriesMap[modelTypeKey] = Calipso.Model.extend({}, staticMembers);
                    d.resolve(Calipso.useCaseFactoriesMap[modelTypeKey]);
                }).fail(d.reject);
            }
            ;
            return d;
        };


        // //////////////////////////////////////
        // Search cache
        // //////////////////////////////////////
        Calipso.util.cache = {
            collections: {},
            /**
             * Returns a cache entry key as
             * <code>collectionOptions.model.getPathFragment + "/" + (collectionOptions.useCase ? collectionOptions.useCase : "search")</code>
             * @param collectionOptions the options used to create the collection and cache entry key
             */
            buildCacheEntryKey: function (collectionOptions) {
                var key = collectionOptions.pathFragment ? collectionOptions.pathFragment : collectionOptions.model.getPathFragment() + "/" + (collectionOptions.useCase ? collectionOptions.useCase : "search");
                //consolelog("Calipso.util.cache#buildCacheEntryKey: " + key);
                return key;
            },
            /**
             * Obtain a cached  for the given model type, criteria and use case.
             * The method will return the cached collection if a match is available
             * with the same search criteria or a new one otherwise.
             *
             * @param collectionOptions the options used to create the collection
             * @return the collection created or matching the given options
             */
            getCollection: function (collectionOptions) {
                if (!collectionOptions) {
                    throw "Calipso.cache.getCollection: options  are required";
                }

                if (!collectionOptions.model || !collectionOptions.model.getTypeName) {
                    throw "Calipso.cache.getCollection: options.model is required and must be a GenericModel subtype";
                }
                var key = this.buildCacheEntryKey(collectionOptions);
                //consolelog("Calipso.util.cache.getCollection for key: " + key);
                var collection = this.collections[key];
                // create a fresh collection when no cache entry is found,
                // or when the model doesn't want caching for it's collections,
                // or when the criteria have changed
                if (!collection || !collectionOptions.model.prototype.isCollectionCacheable() || !this.compareSearchCriteria(collection.data, collectionOptions.data)) {
                    //consolelog("Calipso.util.cache#getCollection creating fresh collection for key: " + key);
                    collection = new Calipso.collection.GenericCollection([], collectionOptions);
                    this.collections[key] = collection;
                } else {
                    //consolelog("Calipso.util.cache#getCollection returning cached collection for key: " + key);
                }
                return collection;
            },
            /**
             * Remove a collection entry from the cache if a match is found.
             * @param collectionOptions the options used to create the collection
             * @return the removed collection, if any
             */
            removeCollection: function (collectionOptions) {
                var key = this.buildCacheEntryKey(collectionOptions);
                var collection = this.collections[key];
                if (collection) {
                    this.collections[key] = null;
                }
                //consolelog("Calipso.util.cache#removeCollection for key: " + key);
                return collection;
            },
            /**
             * Create a fresh collection with the given options and replace the
             * corresponding cache entry if one exists.
             * @param collectionOptions the options used to create the collection
             */
            getFreshCollection: function (options) {
                this.removeCollection(options);
                return this.getCollection(options);
            },
            /**
             * Perform a single-level property comparison
             * of objects that correspond to HTTP parameters
             */
            compareSearchCriteria: function (o1, o2) {
                //consolelog("Calipso.util.cache#compareSearchCriteria, o1: " + o1 + ", o2: " + o2);
                //consolelog(o1);
                //consolelog(o2);
                for (var p in o1) {
                    if (o1.hasOwnProperty(p)) {
                        if (o1[p] + "" !== o2[p] + "") {
                            //consolelog("Calipso.util.cache#compareSearchCriteria returns false");
                            return false;
                        }
                    }
                }
                for (var p in o2) {
                    if (o2.hasOwnProperty(p)) {
                        if (o1[p] + "" !== o2[p] + "") {
                            //consolelog("Calipso.util.cache#compareSearchCriteria returns false");
                            return false;
                        }
                    }
                }
                //consolelog("Calipso.util.cache#compareSearchCriteria returns true");
                return true;
            }
        };

        Calipso.UseCaseContext = Marionette.Object.extend(
            {
                key: null,
                model: null,
                //schemaType : null, specfied by view instead
                roleIncludes: null,
                roleExcludes: null,
                fields: null,
                fieldIncludes: null,
                fieldExcludes: null,
                fieldMasks: null,
                view: null,
                viewOptions: {},
                mergableOptions: ['key', 'pathFragment', 'className', 'title', 'titleHtml', 'description', 'descriptionHtml', 'defaultNext', 'model', 'factory',
                    'view', 'viewOptions', 'childViewOptions',
                    'roleIncludes', 'roleExcludes',
                    "template", "formTemplate", "formFieldTemplate", "formControlSize",
                    'fields', 'fieldIncludes', 'fieldExcludes', 'fieldMasks', 'overrides'],
                inheritableOptions: ['key', 'pathFragment', 'title', 'defaultNext', 'model',
                    'roleIncludes', 'roleExcludes',
                    "formTemplate", "formFieldTemplate", "formControlSize",
                    'fields', 'fieldIncludes', 'fieldExcludes', 'fieldMasks'],
                initialize: function (options) {
                    Marionette.Object.prototype.initialize.apply(this, arguments);
                    this.mergeOptions(options, this.mergableOptions);
                    this.parentContext = options.parentContext;
                    this.addToCollection = options.addToCollection;
                    if (this.addToCollection) {
                        var _this = this;
                        this.listenToOnce(this.model, "sync", function () {
                            _this.addToCollection.add(_this.model);
                            _this.model.trigger("added");
                        });
                    }
                },
                createView: function (options) {
                    options || (options = {});
                    var viewOptions = Calipso.deepExtend({}, this.viewOptions || {}, options);
                    viewOptions.useCaseContext = this;
                    viewOptions.model = this.model;
                    viewOptions.addToCollection = this.addToCollection;
                    return new this.view(viewOptions);
                },
                getRouteUrl: function () {
                    var s = "/useCases/" + this.pathFragment;
                    if (!this.model.isNew()) {
                        s += ("/" + this.model.get("id"));
                    }
                    s += ("/" + this.key);
                    return s;
                },
                getFetchable: function () {
                    return this.key.indexOf("search") == 0 ? this.model.wrappedCollection : this.model
                },
                getFields: function () {
                    // if not given, pick them up from model
                    var fields = _.extend({}, this.model.getFields() || {}, this.fields || {});
                    var fieldLabels = this.model.getLabels();
                    var caseFields = {};
                    var _this = this;

                    _.each(fields, function (field, key, list) {
                        // if included, not excluded and not filed type excluded
                        if ((!_this.fieldIncludes || $.inArray(key, _this.fieldIncludes) > -1)
                            && !(_this.fieldExcludes && $.inArray(key, _this.fieldExcludes) > -1)
                            && !(_this.fieldTypeExcludes && $.inArray(key, _this.fieldTypeExcludes) > -1)) {
                            // switch to hidden if appropriate
                            if (field.hideNonEmpty && _this.model.get(key)) {
                                field.datatype = "Hidden";
                            }
                            // resolve label
                            if (!field.label && fieldLabels[key]) {
                                field.label = fieldLabels[key].label;
                            }
                            if (!field.label) {
                                var labelKeys = [
                                    field.labelKey,
                                    "models." + _this.model.getPathFragment() + "." + key + ".label",
                                    "models." + (field.pathFragment || key) + ".singular.label",
                                    "calipso.words" + (field.pathFragment || key)
                                ];
                                for (var i = 0; !field.label && i < labelKeys.length; i++) {
                                    labelKeys[i] && (field.label = Calipso.util.getLabel(labelKeys[i]));
                                }
                                // last resort is the raw field key
                                field.label || (field.label = key);
                            }
                            caseFields[key] = field;
                        }
                    });
                    return caseFields;
                },
                getChildContext: function (regionName, DefaultViewType) {
                    var _this = this;
                    var schemaTypeOverrideKey;
                    // set the layout's region view type as default
                    var childOptions = {view: DefaultViewType};
                    // self as base config
                    _.each(this.inheritableOptions, function (inheritableOption) {
                        if (_this[inheritableOption]) {
                            childOptions[inheritableOption] = _this[inheritableOption];
                        }
                    });
                    // apply ovverrides?
                    if (_this.overrides) {
                        // apply child usecase overrides based on region name,
                        // NOTE: may update view option
                        Calipso.deepExtend(childOptions, _this.overrides[regionName]);
                        // apply child usecase overrides based on scehame type
                        schemaTypeOverrideKey = childOptions.view.getSchemaType();
                        Calipso.deepExtend(childOptions, _this.overrides[schemaTypeOverrideKey]);
                        Calipso.deepExtend(childOptions, _this.overrides[regionName + '-' + schemaTypeOverrideKey]);
                    }
                    // Luke... I am your father
                    childOptions.parentContext = this;
                    return new Calipso.UseCaseContext(childOptions);
                }

            }
        );


        return Calipso;

    });
