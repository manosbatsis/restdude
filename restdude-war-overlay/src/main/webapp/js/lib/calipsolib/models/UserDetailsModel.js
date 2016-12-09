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
define(['jquery', 'underscore', 'bloodhound', 'typeahead', "lib/calipsolib/util", "lib/calipsolib/form",
        "lib/calipsolib/uifield", "lib/calipsolib/backgrid", "lib/calipsolib/view", 'handlebars', "lib/calipsolib/models/Model"],
    function ($, _, Bloodhoud, Typeahead, Calipso, CalipsoForm, CalipsoField, CalipsoGrid, CalipsoView, Handlebars) {


        Calipso.model.UserDetailsModel = Calipso.Model.extend(
            /** @lends Calipso.model.UserDetailsModel.prototype */
            {
                defaults: {
                    locale: "en"
                },
                browseMenu: null,
                initialize: function () {
                    Calipso.Model.prototype.initialize.apply(this, arguments);
                    var _this = this;
                    this.set("translatedName", Calipso.util.getLabels("countries." + this.get("id")));
                    this.on('sync', function (model, response, options) {
                        _this.onLogin(model, response, options);
                    });
                    this.on('error', function (model, response, options) {
                        _this.onLogin(this, response, options);
                    });
                },
                url: function () {
                    var sUrl = Calipso.getBaseUrl() + this.getBaseFragment() + this.getPathFragment();
                    return sUrl;
                },
                onLogin: function (model, response, options) {
                    console.log("onLogin")
                    // send logged in user on their way
                    var fw = "home";
                    if (Calipso.app.fw) {
                        fw = Calipso.app.fw;
                        Calipso.app.fw = null;
                    }

                    // reload the domain if locale needs to be changed
                    var userLocale = this.get("locale");
                    var oldLocale = localStorage.getItem("locale");

                    // change locale?
                    if (oldLocale && oldLocale != userLocale) {
                        localStorage.setItem("locale", this.get("locale"));
                        window.location.href = Calipso.getBaseUrl() + "/client/" + fw;
                    } else {
                        // is the application started?
                        if (Calipso.app.isStarted()) {
                            console.log("onLogin, app is started")
                            this.browseMenu = null;
                            if (this.get("id")) {
                                Calipso.app.updateHeaderFooter();
                                Calipso.navigate(fw, {
                                    trigger: true
                                });
                            } else {
                                alert("Invalid credentials")
                            }
                        } else {
                            console.log("onLogin, app is NOT started")
                            Calipso.app.start(Calipso.getConfigProperty("startOptions"));
                        }
                    }

                },
                buildBrowseMenu: function () {
                    var _this = this;
                    var allModelLabels = Calipso.util.getLabels("models");
                    var browseMenu = null;
                    var parseModel = function (ModelType) {
                        // setup model-based usecase factories
                        if (ModelType.getTypeName() != "Calipso.model.Model" &&
                            ModelType.getTypeName() != "Calipso.model.UserRegistrationModel" &&
                            ModelType.getTypeName() != "Calipso.model.UserDetailsModel" &&
                            ModelType.getTypeName() != "Calipso.model.GenericModel") {

                            // build "browse" menu
                            if (ModelType.menuConfig) {
                                var rolesIncluded = ModelType.menuConfig.rolesIncluded;
                                var rolesExcluded = ModelType.menuConfig.rolesExcluded || {};
                                // if inclusions are passed or empty
                                if (!rolesIncluded || Calipso.isUserInAnyRole(rolesIncluded)) {
                                    // and exclusions have no match
                                    if (!Calipso.isUserInAnyRole(rolesExcluded)) {
                                        browseMenu || (browseMenu = {});
                                        var modelLabels = allModelLabels[ModelType.getPathFragment()] || {};
                                        browseMenu[ModelType.getPathFragment()] = {
                                            label: ModelType.label || Calipso.util.getLabel(ModelType.getPathFragment() + ".plural.label", allModelLabels),
                                            labelIcon: ModelType.labelIcon,
                                        }
                                    }
                                }
                            }

                        }
                    };
                    _(Calipso.model).each(parseModel);
                    _(Calipso.customModel).each(parseModel);
                    _this.set("browseMenu", browseMenu);
                },
                // TODO: move to usecases/labels
                getViewTitle: function () {
                    var schemaKey = this.getFormSchemaKey();
                    var title = "";
                    if (schemaKey == "create") {
                        title += "Login ";
                    } else if (schemaKey.indexOf("update") == 0) {
                        title += "Change Password ";
                    }
                    return title;
                },
                toString: function () {
                    return this.get("username");
                },
            },
            // static members
            {
                public: true,
                pathFragment: "userDetails",
                baseFragment: '/api/auth/',
                typeName: "Calipso.model.UserDetailsModel",
                useCases: {
                    login: {
                        view: Calipso.view.UserDetailsLayout,
                        fieldIncludes: ["email", "password"],
                        overrides: {
                            contentRegion: {
                                viewOptions: {
                                    template: Calipso.getTemplate("LoginFormView"),
                                    title: '<i class="fa fa-lock"></i> ' + Calipso.util.getLabels("useCases.userDetails.login.title") +
                                    '<div class="btn-group btn-group-sm pull-right" role="group">\
              <a class="btn btn-secondary" href="/useCases/accounts/forgotPassword">' +
                                    Calipso.util.getLabels("useCases.userDetails.login.forgotPassword") + '</a>\
                  <a class="btn btn-secondary" href="/register">' +
                                    Calipso.util.getLabels("useCases.userDetails.login.newUser") + '</a>\
                </div>',
                                    message: Calipso.util.getLabels("useCases.userDetails.login.message"),
                                    placeHolderLabelsOnly: true,
                                    formControlSize: "lg",
                                    submitButton: '<i class="fa fa-sign-in" aria-hidden="true"></i> ' + Calipso.util.getLabels("calipso.words.login")
                                }
                            }
                        },
                    },
                },
                fields: {
                    id: {
                        fieldType: "Hidden",
                    },
                    email: {
                        fieldType: "String",
                    },
                    password: {
                        fieldType: "Password",
                    },
                },
                create: function (options) {
                    if (this._instance === undefined) {
                        this._instance = new this(options);
                    } else {
                        this._instance.clear();
                    }
                    this._instance.set(options);
                    return this._instance;
                },
            });

    });