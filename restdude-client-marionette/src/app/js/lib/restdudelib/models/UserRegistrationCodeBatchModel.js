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

        Restdude.model.UserRegistrationCodeBatchModel = Restdude.Model.extend(
            /** @lends Restdude.model.UserRegistrationCodeBatchModel.prototype */
            {
                toString: function () {
                    return this.get("name");
                }
            }, {
                // static members
                labelIcon: "fa fa-server fa-fw",
                pathFragment: "registrationCodeBatches",
                typeName: "Restdude.model.UserRegistrationCodeBatchModel",
                /*
                 menuConfig: {
                 rolesIncluded: ["ROLE_ADMIN", "ROLE_SITE_OPERATOR"],
                 rolesExcluded: null,
                 },
                 */
                useCases: {
                    search: {
                        fieldIncludes: ["name", "description", "batchSize", "available", "createdDate", "expirationDate", "csvExport", "edit"],
                        rolesIncluded: ["ROLE_ADMIN", "ROLE_SITE_OPERATOR"],
                    },
                    create: {
                        fieldIncludes: ["name", "description", "batchSize", "expirationDate"],
                        rolesIncluded: ["ROLE_ADMIN", "ROLE_SITE_OPERATOR"],
                    },
                    update: {
                        fieldIncludes: ["description", "expirationDate"],
                        rolesIncluded: ["ROLE_ADMIN", "ROLE_SITE_OPERATOR"],
                    },
                },
                fields: {
                    name: {
                        fieldType: "String",
                    },
                    description: {
                        fieldType: "Text",
                    },
                    batchSize: {
                        fieldType: "Integer",
                    },
                    available: {
                        fieldType: "Integer",
                    },
                    createdDate: {
                        fieldType: "Date",
                    },
                    expirationDate: {
                        fieldType: "Date",
                    },
                    csvExport: {
                        fieldType: "CsvExport",
                    },
                    edit: {
                        fieldType: "Edit",
                    },
                },
            });

    });