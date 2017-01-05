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
define(['jquery', 'underscore', 'bloodhound', 'typeahead', "lib/restdudelib/util", "lib/restdudelib/form",
        "lib/restdudelib/uifield", "lib/restdudelib/backgrid", "lib/restdudelib/view", 'handlebars', "lib/restdudelib/models/Model"],
    function ($, _, Bloodhoud, Typeahead, Restdude, RestdudeForm, RestdudeField, RestdudeGrid, RestdudeView, Handlebars) {


        Restdude.model.UserDetailsModel = Restdude.Model.extend(
            /** @lends Restdude.model.UserDetailsModel.prototype */
            {
                defaults: {
                    locale: "en"
                },
                browseMenu: null,
                initialize: function () {
                    Restdude.Model.prototype.initialize.apply(this, arguments);
                    var _this = this;
                    this.set("translatedName", Restdude.util.getLabels("countries." + this.get("id")));
                    this.on('sync', function (model, response, options) {
                        _this.onLogin(model, response, options);
                    });
                    this.on('error', function (model, response, options) {
                        _this.onLogin(this, response, options);
                    });
                },
                url: function () {
                    var sUrl = Restdude.getBaseUrl() + this.getBaseFragment() + this.getPathFragment();
                    return sUrl;
                },
                onLogin: function (model, response, options) {
                    console.log("onLogin")
                    // send logged in user on their way
                    var fw = "home";
                    if (Restdude.app.fw) {
                        fw = Restdude.app.fw;
                        Restdude.app.fw = null;
                    }

                    // reload the domain if locale needs to be changed
                    var userLocale = this.get("locale");
                    var oldLocale = localStorage.getItem("locale");

                    // change locale?
                    if (oldLocale && oldLocale != userLocale) {
                        localStorage.setItem("locale", this.get("locale"));
                        window.location.href = Restdude.getBaseUrl() + "/client/" + fw;
                    } else {
                        // is the application started?
                        if (Restdude.app.isStarted()) {
                            console.log("onLogin, app is started")
                            this.browseMenu = null;
                            if (this.get("id")) {
                                Restdude.app.updateHeaderFooter();
                                Restdude.navigate(fw, {
                                    trigger: true
                                });
                            } else {
                                alert("Invalid credentials")
                            }
                        } else {
                            console.log("onLogin, app is NOT started")
                            Restdude.app.start(Restdude.getConfigProperty("startOptions"));
                        }
                    }

                },
                buildBrowseMenu: function () {
                    var _this = this;
                    var allModelLabels = Restdude.util.getLabels("models");
                    var browseMenu = null;
                    var parseModel = function (ModelType) {
                        // setup model-based usecase factories
                        if (ModelType.getTypeName() != "Restdude.model.Model" &&
                            ModelType.getTypeName() != "Restdude.model.UserRegistrationModel" &&
                            ModelType.getTypeName() != "Restdude.model.UserDetailsModel" &&
                            ModelType.getTypeName() != "Restdude.model.GenericModel") {

                            // build "browse" menu
                            if (ModelType.menuConfig) {
                                var rolesIncluded = ModelType.menuConfig.rolesIncluded;
                                var rolesExcluded = ModelType.menuConfig.rolesExcluded || {};
                                // if inclusions are passed or empty
                                if (!rolesIncluded || Restdude.isUserInAnyRole(rolesIncluded)) {
                                    // and exclusions have no match
                                    if (!Restdude.isUserInAnyRole(rolesExcluded)) {
                                        browseMenu || (browseMenu = {});
                                        var modelLabels = allModelLabels[ModelType.getPathFragment()] || {};
                                        browseMenu[ModelType.getPathFragment()] = {
                                            label: ModelType.label || Restdude.util.getLabel(ModelType.getPathFragment() + ".plural.label", allModelLabels),
                                            labelIcon: ModelType.labelIcon,
                                        }
                                    }
                                }
                            }

                        }
                    };
                    _(Restdude.model).each(parseModel);
                    _(Restdude.customModel).each(parseModel);
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
                typeName: "Restdude.model.UserDetailsModel",
                useCases: {
                    login: {
                        view: Restdude.view.UserDetailsLayout,
                        fieldIncludes: ["email", "password"],
                        overrides: {
                            contentRegion: {
                                viewOptions: {
                                    template: Restdude.getTemplate("LoginFormView"),
                                    title: '<i class="fa fa-lock"></i> ' + Restdude.util.getLabels("useCases.userDetails.login.title") +
                                    '<div class="btn-group btn-group-sm pull-right" role="group">\
              <a class="btn btn-secondary" href="/useCases/accounts/forgotPassword">' +
                                    Restdude.util.getLabels("useCases.userDetails.login.forgotPassword") + '</a>\
                  <a class="btn btn-secondary" href="/register">' +
                                    Restdude.util.getLabels("useCases.userDetails.login.newUser") + '</a>\
                </div>',
                                    message: Restdude.util.getLabels("useCases.userDetails.login.message"),
                                    placeHolderLabelsOnly: true,
                                    formControlSize: "lg",
                                    submitButton: '<i class="fa fa-sign-in" aria-hidden="true"></i> ' + Restdude.util.getLabels("restdude.words.login")
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