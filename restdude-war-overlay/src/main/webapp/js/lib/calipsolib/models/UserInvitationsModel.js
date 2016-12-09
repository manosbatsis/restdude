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
        "lib/calipsolib/uifield", "lib/calipsolib/backgrid", "lib/calipsolib/view", 'handlebars', "lib/calipsolib/models/Model", "lib/calipsolib/models/UserModel"],
    function ($, _, Bloodhoud, Typeahead, Calipso, CalipsoForm, CalipsoField, CalipsoGrid, CalipsoView, Handlebars, Model, UserModel) {

        var RecipientModel = Backbone.Model.extend({
            initialize: function () {
                Backbone.Model.prototype.initialize.apply(this, arguments);
                this.set("@class", "gr.abiss.calipso.model.model.UserDTO");
            },
            schema: {
                "@class": {
                    type: Calipso.backboneform.ClassName,
                    defaultValue: "gr.abiss.calipso.model.model.UserDTO",
                },
                name: {
                    type: 'Text',
                },
                email: {
                    type: 'Text',
                    validators: ['required', 'email']
                },
            },
            //To string is how models in the list will appear in the "editor".
            toString: function () {
                return this.attributes.name + '&lt;' + this.attributes.email + '&gt;';
            }
        });


        Calipso.model.UserInvitationsModel = Calipso.Model.extend(
            /** @lends Calipso.model.RoleModel.prototype */
            {
                toString: function () {
                    return this.get("name");
                }
            }, {
                // static members
                labelIcon: "fa fa-envelope-o fa-fw",
                pathFragment: "invitations",
                typeName: "Calipso.model.UserInvitationsModel",
                menuConfig: null,
                useCases: {
                    create: {
                        view: Calipso.view.UserInvitationsLayout,
                        overrides: {
                            contentRegion: {
                                viewOptions: {
                                    template: Calipso.getTemplate("UseCaseCardFormView"),
                                }
                            }
                        }
                    }
                },
                fields: function () {
                    return {
                        addressLines: {
                            fieldType: "Text",
                        },
                        recepients: {
                            fieldType: "Lov",
                            form: {
                                type: 'List',
                                itemType: 'NestedModel',
                                model: RecipientModel
                            }
                        },
                    };
                },
            });


    });