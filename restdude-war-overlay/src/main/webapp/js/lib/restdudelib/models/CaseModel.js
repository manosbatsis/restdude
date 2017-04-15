/*
 *
 * Restdude
 * -------------------------------------------------------------------
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
define(['jquery', 'underscore', 'bloodhound', 'typeahead', "lib/restdudelib/util", "lib/restdudelib/form","lib/restdudelib/backgrid",
        "lib/restdudelib/uifield", "lib/restdudelib/view", 'handlebars', "lib/restdudelib/models/Model"],
function ($, _, Bloodhoud, Typeahead, Restdude, RestdudeForm, RestdudeBackgrid, RestdudeField, RestdudeView, Handlebars) {

    Restdude.model.CaseModel = Restdude.Model.extend(
        /** @lends Restdude.model.BaseErrorModel.prototype */
        {
            toString: function () {
                return this.get("title");
            }
        }, {
            // static members
            labelIcon: "fa fa-user fa-fw",
            public: true,
            pathFragment: "topics",
            typeName: "Restdude.model.TopicModel",
            menuConfig: false,
            fields: {
                "name": {
                    fieldType: "ViewModel",
                },
                "title": {
                    fieldType: "ViewModel",
                },
                "detail": {
                    fieldType: "Text",
                },
                "createdDate": {
                    fieldType: "Datetime",
                },
                "createdBy": {
                    fieldType: "RelatedModel",
                    pathFragment: "users",
                },
                "lastModifiedDate": {
                    fieldType: "Datetime",
                },
                "lastModifiedBy": {
                    fieldType: "RelatedModel",
                    pathFragment: "users",
                },
            },
            useCases: function(){
                return {
                    view: {
                        view: Restdude.view.CaseDetailsLayout,
                        overrides: {
                            contentRegion: {
                                view: Restdude.view.View,
                                viewOptions: {
                                    template: Restdude.getTemplate("CaseDetails")
                                },
                            },
                        },
                    },
                    search: {
                        overrides: {
                            backgrid: {
                                fields: {

                                    createdDate: {
                                        fieldType: Restdude.fields.CalendarTime,
                                    },
                                    lastModifiedDate: {
                                        fieldType: Restdude.fields.CalendarTime,
                                    },

                                },
                                fieldExcludes: ["detail", "createdDate", "lastModifiedBy", "remoteAddress"],
                            },
                        },
                    },
                };
            },
        });

});