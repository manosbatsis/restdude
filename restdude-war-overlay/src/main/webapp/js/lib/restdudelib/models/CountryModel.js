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
 * @exports lib/restdudelib/model/CountryModel
 */
define(['jquery', 'underscore', 'bloodhound', 'typeahead', "lib/restdudelib/util", "lib/restdudelib/form",
        "lib/restdudelib/uifield", "lib/restdudelib/backgrid", "lib/restdudelib/view", 'handlebars', "lib/restdudelib/models/Model"],
    function ($, _, Bloodhoud, Typeahead, Restdude, RestdudeForm, RestdudeField, RestdudeGrid, RestdudeView, Handlebars) {

        /**
         * Model for countries
         * @name Caliopso.model.CountryModel
         * @constructor
         * @augments Caliopso.Model
         */
        Restdude.model.CountryModel = Restdude.Model.extend(
            /** @lends Restdude.model.CountryModel.prototype */
            {
                initialize: function () {
                    Restdude.Model.prototype.initialize.apply(this, arguments);
                    this.set("translatedName", Restdude.util.getLabels("countries." + this.get(Restdude.config.idAttribute)));
                },
                toString: function () {
                    return this.get("translatedName") || this.get("name");
                },
                text: function () {
                    return this.get("translatedName") || this.get("name");
                }
                //urlRoot : "/api/rest/users"
            }, {
                // static members
                labelIcon: "fa fa-globe fa-fw",
                pathFragment: "countries",
                typeName: "Restdude.model.CountryModel",
                menuConfig: {
                    rolesIncluded: ["ROLE_ADMIN", "ROLE_SITE_OPERATOR"],
                    rolesExcluded: null,
                },
                fields: {
                    "name": {
                        fieldType: "String",
                        form: {
                            validators: ['required']
                        }
                    },
                    "nativeName": {
                        fieldType: "String",
                        form: {
                            validators: ['required']
                        }
                    },
                    "callingCode": {
                        fieldType: "String",
                        form: {
                            validators: ['required']
                        }
                    },
                    "capital": {
                        fieldType: "String",
                        form: {
                            validators: ['required']
                        }
                    },
                    "currency": {
                        fieldType: "String",
                        form: {
                            validators: ['required']
                        }
                    },
                    "languages": {
                        fieldType: "String",
                        form: {
                            validators: ['required']
                        }
                    },
                },
            });

    });