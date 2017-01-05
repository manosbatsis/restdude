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
        "lib/restdudelib/uifield", "lib/restdudelib/backgrid", "lib/restdudelib/view", 'handlebars', "lib/restdudelib/models/UserModel"],
    function ($, _, Bloodhoud, Typeahead, Restdude, RestdudeForm, RestdudeField, RestdudeGrid, RestdudeView, Handlebars) {

        Restdude.model.UserAccountModel = Restdude.model.UserModel.extend(
            /** @lends Restdude.model.UserAccountModel.prototype */
            {
                isNew: function () {
                    return this.get("email") && !this.get("resetPasswordToken") ? true : false;
                },
                url: function () {
                    var sUrl = Restdude.getBaseUrl() + this.getBaseFragment() + this.getPathFragment();
                    return sUrl;
                },
                toString: function () {
                    return this.get("username");
                }
                //urlRoot : "/api/rest/users"
            }, {
                // static members
                labelIcon: "fa fa-user fa-fw",
                menuConfig: null,
                public: true,
                pathFragment: "accounts",
                baseFragment: '/api/auth/',
                typeName: "Restdude.model.UserAccountModel",
                useCases: {
                    create: function () {
                        return {
                            view: Restdude.view.UserAccountLayout,
                            fieldIncludes: Restdude.getConfigProperty("registration.forceCodes") ? ["firstName", "lastName", "email", "registrationCode", "password", "passwordConfirmation"] : ["firstName", "lastName", "email", "password", "passwordConfirmation"],
                            defaultNext: "confirmRegistration",
                            overrides: {
                                contentRegion: {
                                    viewOptions: {
                                        template: Restdude.getTemplate("UseCaseCardFormView"),
                                        title: Restdude.util.getLabels("tmpl.userRegistration.titleNewAccount") +
                                        '<a href="#" class="btn btn-secondary btn-sm btn-social btn-facebook  pull-right" title="' +
                                        Restdude.util.getLabels("tmpl.login.fbLinkAlt") +
                                        '"> &nbsp; &nbsp; &nbsp; &nbsp;' + Restdude.util.getLabels("tmpl.login.fbLink") + '</a>',
                                        message: Restdude.util.getLabels("tmpl.userRegistration.formHelpNewAccount"),
                                        placeHolderLabelsOnly: true,
                                        formControlSize: "lg",
                                        submitButton: '<i class="fa fa-user-plus" aria-hidden="true"></i> ' + Restdude.util.getLabels("restdude.words.register")
                                    },
                                },
                            },
                        }
                    },
                    forgotPassword: { // request reset token and link by email
                        view: Restdude.view.UserAccountLayout,
                        fieldIncludes: ["email"],
                        defaultNext: "resetPassword",
                        overrides: {
                            contentRegion: {
                                viewOptions: {
                                    template: Restdude.getTemplate("UseCaseCardFormView"),
                                    title: "<i class='fa fa-lock'></i> " + Restdude.util.getLabels("useCases.userDetails.login.forgotPassword"),
                                    message: Restdude.util.getLabels("useCases.userDetails.forgotPassword.message"),
                                    placeHolderLabelsOnly: true,
                                    formControlSize: "lg",
                                    submitButton: '<i class="fa fa-sign-in" aria-hidden="true"></i> ' + Restdude.util.getLabels("useCases.userDetails.forgotPassword.submitButton")
                                }
                            }
                        },
                    },
                    confirmRegistration: { // confirm registration if a password exists
                        view: Restdude.view.UserAccountLayout,
                        fieldIncludes: ["email", "resetPasswordToken"],
                        overrides: {
                            contentRegion: {
                                viewOptions: {
                                    template: Restdude.getTemplate("UseCaseCardFormView"),
                                    title: "<i class='fa fa-lock'></i> " + Restdude.util.getLabels("useCases.userDetails.confirmRegistration.title"),
                                    message: Restdude.util.getLabels("useCases.userDetails.confirmRegistration.message"),
                                    placeHolderLabelsOnly: true,
                                    formControlSize: "lg",
                                    submitButton: '<i class="fa fa-sign-in" aria-hidden="true"></i> ' + Restdude.util.getLabels("useCases.userDetails.confirmRegistration.submitButton")
                                }
                            }
                        },
                    },
                    resetPassword: { // enter new password
                        view: Restdude.view.UserAccountLayout,
                        fieldIncludes: ["email", "resetPasswordToken", "password", "passwordConfirmation"],
                        overrides: {
                            contentRegion: {
                                viewOptions: {
                                    template: Restdude.getTemplate("UseCaseCardFormView"),
                                    title: "<i class='fa fa-lock'></i> " + Restdude.util.getLabels("useCases.userDetails.resetPassword.title"),
                                    message: Restdude.util.getLabels("useCases.userDetails.resetPassword.message"),
                                    placeHolderLabelsOnly: true,
                                    formControlSize: "lg",
                                    submitButton: '<i class="fa fa-sign-in" aria-hidden="true"></i> ' + Restdude.util.getLabels("useCases.userDetails.resetPassword.submitButton")
                                }
                            }
                        },
                    },
                    changePassword: { // enter new password
                        view: Restdude.view.UserAccountLayout,
                        fieldIncludes: ["currentPassword", "password", "passwordConfirmation"],
                        overrides: {
                            contentRegion: {
                                viewOptions: {
                                    template: Restdude.getTemplate("UseCaseCardFormView"),
                                    title: "<i class='fa fa-lock'></i> " + Restdude.util.getLabels("useCases.userDetails.resetPassword.title"),
                                    message: Restdude.util.getLabels("useCases.userDetails.resetPassword.message"),
                                    placeHolderLabelsOnly: true,
                                    formControlSize: "lg",
                                    submitButton: '<i class="fa fa-sign-in" aria-hidden="true"></i> ' + Restdude.util.getLabels("useCases.userDetails.resetPassword.submitButton")
                                }
                            }
                        },
                    },
                },
                fields: {
                    email: {
                        fieldType: "String",
                        form: {
                            type: "Text",
                            validators: ['required', 'email'],
                        },
                    },
                    email: {
                        fieldType: "String",
                        form: {
                            type: "Text",
                            validators: ['required', 'email'],
                        },
                    },
                    registrationCode: {
                        fieldType: "String",
                        form: {
                            validators: ['required'],
                        }
                    },
                    resetPasswordToken: {
                        fieldType: "String",
                    },
                    currentPassword: {
                        fieldType: "CurrentPassword",
                    },
                    password: {
                        fieldType: "Password",
                    },
                    passwordConfirmation: {
                        fieldType: "ConfirmPassword",
                    }
                },
            });

    });