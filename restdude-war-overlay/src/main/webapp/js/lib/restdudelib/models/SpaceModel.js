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
/**
 * @exports lib/restdudelib/model/HierarchicalModel
 */
define(['jquery', 'underscore', 'bloodhound', 'typeahead', "lib/restdudelib/util", "lib/restdudelib/form",
        "lib/restdudelib/uifield", "lib/restdudelib/backgrid", "lib/restdudelib/view", 'handlebars', "lib/restdudelib/models/HierarchicalModel"],
    function ($, _, Bloodhoud, Typeahead, Restdude, RestdudeForm, RestdudeField, RestdudeGrid, RestdudeView, Handlebars) {
        /**
         * Model A model representing a context, such as an organization, team, or process type.
         * @name Restdude.model.SpaceModel
         * @constructor
         * @augments Restdude.model.HierarchicalModel
         */
        Restdude.model.SpaceModel = Restdude.model.HierarchicalModel.extend(
            /** @lends Restdude.model.SpaceModel.prototype */
            {
            }, {
                // static members
                labelIcon: "fa fa-server fa-fw",
                pathFragment: "spaces",
                //typeName: "Restdude.model.SpaceModel",
                menuConfig: {
                    rolesIncluded: ["ROLE_ADMIN", "ROLE_SITE_OPERATOR"],
                    rolesExcluded: null,
                },
                fields: {
                    owner: {
                        fieldType: "RelatedModel",
                        pathFragment: "users",
                    },
                    edit: {
                        fieldType: "Edit",
                    },
                },
            });

    });