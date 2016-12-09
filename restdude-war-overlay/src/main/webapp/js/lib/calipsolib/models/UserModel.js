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

        Calipso.model.UserModel = Calipso.Model.extend(
            /** @lends Calipso.model.UserModel.prototype */
            {
                toString: function () {
                    return this.get("username");
                }
                //urlRoot : "/api/rest/users"
            }, {
                // static members
                labelIcon: "fa fa-user fa-fw",
                public: true,
                pathFragment: "users",
                typeName: "Calipso.model.UserModel",
                menuConfig: {
                    rolesIncluded: ["ROLE_ADMIN", "ROLE_SITE_OPERATOR"],
                    rolesExcluded: null,
                },
                useCases: {
                    view: {
                        view: Calipso.view.UserProfileLayout,
                    },
                    register: {
                        view: Calipso.view.UserRegistrationLayout,
                        fieldIncludes: ["firstName", "lastName", "email"],
                        overrides: {
                            contentRegion: {
                                viewOptions: {
                                    template: Calipso.getTemplate("UseCaseCardFormView"),
                                    title: Calipso.util.getLabels("tmpl.userRegistration.titleNewAccount") +
                                    '<a href="#" class="btn btn-secondary btn-sm btn-social btn-facebook  pull-right" title="' +
                                    Calipso.util.getLabels("tmpl.login.fbLinkAlt") +
                                    '"><i class="fa fa-facebook-f"></i> ' + Calipso.util.getLabels("tmpl.login.fbLink") + '</a>',
                                    message: Calipso.util.getLabels("tmpl.userRegistration.formHelpNewAccount"),
                                    placeHolderLabelsOnly: true,
                                    formControlSize: "lg",
                                    submitButton: '<i class="fa fa-user-plus" aria-hidden="true"></i> ' + Calipso.util.getLabels("calipso.words.register")
                                },
                            },
                        },
                    },
                    search: {
                        view: Calipso.view.UseCaseSearchLayout,
                        viewOptions: {
                            formTemplatesKey: "vertical",
                            //fieldsSearchBox: ["username", "firstName", "lastName", "email"],
                        },
                        overrides: {
                            backgrid: {
                                fieldIncludes: ["username", "firstName", "lastName", "edit"],
                            },
                            form: {
                                fieldIncludes: ["username", "firstName", "lastName", "email"],
                                //viewOptions : {
                                //  fieldsInitiallyShown : [ "username", "email" ],
                                //},
                            },
                        }
                    },
                },
                /*

                 id : {
                 fieldType : "Hidden",
                 },
                 email : {
                 fieldType : "String",
                 },
                 resetPasswordToken : {
                 fieldType : "String",
                 },
                 currentPassword : {
                 fieldType : "CurrentPassword",
                 },
                 password : {
                 fieldType : "Password",
                 },
                 passwordConfirmation : {
                 fieldType : "ConfirmPassword",
                 }
                 */
                fields: {
                    username: {
                        fieldType: "String",
                        backgrid: {
                            cell: Calipso.components.backgrid.ViewRowCell,
                        }
                    },

                    firstName: {
                        fieldType: "String",
                    },
                    lastName: {
                        fieldType: "String",
                    },
                    email: {
                        fieldType: "Email",
                    },
                    telephone: {
                        fieldType: "Tel",
                    },
                    cellphone: {
                        fieldType: "Tel",
                    },
                    active: {
                        fieldType: "Boolean",
                    },
                    /*roles : {
                     fieldType : "List",
                     //"form" : {
                     //	"listModel" : Calipso.model.RoleModel
                     //}
                     },*/
                    edit: {
                        fieldType: "Edit",
                    },
                },
            });

    });