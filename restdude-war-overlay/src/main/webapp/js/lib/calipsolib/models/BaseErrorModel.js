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

        Calipso.model.BaseErrorModel = Calipso.Model.extend(
            /** @lends Calipso.model.BaseErrorModel.prototype */
            {
                toString: function () {
                    return this.get("message");
                }
            }, {
                // static members
                labelIcon: "fa fa-user fa-fw",
                public: true,
                pathFragment: "allErrors",
                typeName: "Calipso.model.BaseErrorModel",
                menuConfig: {
                    rolesIncluded: ["ROLE_ADMIN", "ROLE_SITE_OPERATOR"],
                    rolesExcluded: null,
                },
                fields: {
                    user: {
                        fieldType: "RelatedModel",
                        pathFragment: "users",
                    },
                    message: {
                        fieldType: "String",
                    },
                    createdDate: {
                        fieldType: "Date",
                    },
                    remoteAddress: {
                        fieldType: "String",
                    },
                    stackTraceId: {
                        fieldType: "String",
                    },
                },
            });

    });