/*
 * Copyright (c) 2007 - 2016 Manos Batsis
 *
 * This file is part of Restdude, a software platform by www.Abiss.gr.
 *
 * Restdude is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Restdude is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Restdude. If not, see http://www.gnu.org/licenses/agpl.html
 */
define(['restdude'],
    function (Restdude, EditInTabCell, ViewRowCell) {
        var ResourceModel = Restdude.model.GenericModel.extend({
                modelKey: "resource",
                apiPath: "/api/rest/resource/",

            },
            // static members
            {
                parent: Restdude.model.GenericModel,
                showInMenu: false,
                label: "User"
            });

        /**
         * Get the model class URL fragment corresponding this class
         * @returns the URL path fragment as a string
         */
        ResourceModel.prototype.getPathFragment = function () {
            return "resources";
        }
        ResourceModel.prototype.getFormSchemas = function () {
            var schema = {//
                "host": {
                    "default": {
                        type: 'NestedModel',
                        model: Restdude.model.HostModel,
                    }
                },
                "name": {
                    "search": 'Text',
                    "default": {
                        type: 'Text',
                        validators: ['required']
                    }
                },
                "pathName": {
                    "search": null,
                    "default": {
                        type: 'Text',
                        validators: ['required']
                    }
                },

            };
            return schema;
        }
        ResourceModel.prototype.getGridSchema = function () {
            return [
                {
                    name: "name",
                    label: "name",
                    editable: false,
                    cell: Restdude.components.ViewRowCell
                },
                {
                    name: "pathName",
                    label: "path name",
                    editable: false,
                    cell: "string"
                }, {
                    name: "edit",
                    label: "",
                    editable: false,
                    cell: Restdude.components.ViewRowCell
                }];
        }
        return ResourceModel;
    });