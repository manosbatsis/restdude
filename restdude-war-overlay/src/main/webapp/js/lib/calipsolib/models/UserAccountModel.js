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
        "lib/calipsolib/uifield", "lib/calipsolib/backgrid", "lib/calipsolib/view", 'handlebars', "lib/calipsolib/models/UserModel"],
    function ($, _, Bloodhoud, Typeahead, Calipso, CalipsoForm, CalipsoField, CalipsoGrid, CalipsoView, Handlebars) {

        Calipso.model.UserAccountModel = Calipso.model.UserModel.extend(
            /** @lends Calipso.model.UserAccountModel.prototype */
            {
                isNew: function () {
                    return this.get("registrationEmail") ? true : false;
                },
                url: function () {
                    var sUrl = Calipso.getBaseUrl() + this.getBaseFragment() + this.getPathFragment();
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
                typeName: "Calipso.model.UserAccountModel",
                useCases: {
                    create: function () {
                        return {
                            view: Calipso.view.UserAccountLayout,
                            fieldIncludes: Calipso.getConfigProperty("registration.forceCodes") ? ["firstName", "lastName", "registrationEmail", "registrationCode"] : ["firstName", "lastName", "registrationEmail"],
                            defaultNext: "resetPassword",
                            overrides: {
                                contentRegion: {
                                    viewOptions: {
                                        template: Calipso.getTemplate("UseCaseCardFormView"),
                                        title: Calipso.util.getLabels("tmpl.userRegistration.titleNewAccount") +
                                        '<a href="#" class="btn btn-secondary btn-sm btn-social btn-facebook  pull-right" title="' +
                                        Calipso.util.getLabels("tmpl.login.fbLinkAlt") +
                                        '"> &nbsp; &nbsp; &nbsp; &nbsp;' + Calipso.util.getLabels("tmpl.login.fbLink") + '</a>',
                                        message: Calipso.util.getLabels("tmpl.userRegistration.formHelpNewAccount"),
                                        placeHolderLabelsOnly: true,
                                        formControlSize: "lg",
                                        submitButton: '<i class="fa fa-user-plus" aria-hidden="true"></i> ' + Calipso.util.getLabels("calipso.words.register")
                                    },
                                },
                            },
                        }
                    },
                    forgotPassword: { // request reset token and link by email
                        view: Calipso.view.UserAccountLayout,
                        fieldIncludes: ["email"],
                        defaultNext: "resetPassword",
                        overrides: {
                            contentRegion: {
                                viewOptions: {
                                    template: Calipso.getTemplate("UseCaseCardFormView"),
                                    title: "<i class='fa fa-lock'></i> " + Calipso.util.getLabels("useCases.userDetails.login.forgotPassword"),
                                    message: Calipso.util.getLabels("useCases.userDetails.forgotPassword.message"),
                                    placeHolderLabelsOnly: true,
                                    formControlSize: "lg",
                                    submitButton: '<i class="fa fa-sign-in" aria-hidden="true"></i> ' + Calipso.util.getLabels("useCases.userDetails.forgotPassword.submitButton")
                                }
                            }
                        },
                    },
                    confirmRegistration: { // confirm registration if a password exists
                        view: Calipso.view.UserAccountLayout,
                        fieldIncludes: ["email", "resetPasswordToken"],
                        overrides: {
                            contentRegion: {
                                viewOptions: {
                                    template: Calipso.getTemplate("UseCaseCardFormView"),
                                    title: "<i class='fa fa-lock'></i> " + Calipso.util.getLabels("useCases.userDetails.confirmRegistration.title"),
                                    message: Calipso.util.getLabels("useCases.userDetails.confirmRegistration.message"),
                                    placeHolderLabelsOnly: true,
                                    formControlSize: "lg",
                                    submitButton: '<i class="fa fa-sign-in" aria-hidden="true"></i> ' + Calipso.util.getLabels("useCases.userDetails.confirmRegistration.submitButton")
                                }
                            }
                        },
                    },
                    resetPassword: { // enter new password
                        view: Calipso.view.UserAccountLayout,
                        fieldIncludes: ["email", "resetPasswordToken", "password", "passwordConfirmation"],
                        overrides: {
                            contentRegion: {
                                viewOptions: {
                                    template: Calipso.getTemplate("UseCaseCardFormView"),
                                    title: "<i class='fa fa-lock'></i> " + Calipso.util.getLabels("useCases.userDetails.resetPassword.title"),
                                    message: Calipso.util.getLabels("useCases.userDetails.resetPassword.message"),
                                    placeHolderLabelsOnly: true,
                                    formControlSize: "lg",
                                    submitButton: '<i class="fa fa-sign-in" aria-hidden="true"></i> ' + Calipso.util.getLabels("useCases.userDetails.resetPassword.submitButton")
                                }
                            }
                        },
                    },
                    changePassword: { // enter new password
                        view: Calipso.view.UserAccountLayout,
                        fieldIncludes: ["currentPassword", "password", "passwordConfirmation"],
                        overrides: {
                            contentRegion: {
                                viewOptions: {
                                    template: Calipso.getTemplate("UseCaseCardFormView"),
                                    title: "<i class='fa fa-lock'></i> " + Calipso.util.getLabels("useCases.userDetails.resetPassword.title"),
                                    message: Calipso.util.getLabels("useCases.userDetails.resetPassword.message"),
                                    placeHolderLabelsOnly: true,
                                    formControlSize: "lg",
                                    submitButton: '<i class="fa fa-sign-in" aria-hidden="true"></i> ' + Calipso.util.getLabels("useCases.userDetails.resetPassword.submitButton")
                                }
                            }
                        },
                    },
                },
                fields: {
                    registrationEmail: {
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