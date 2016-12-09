/*
 * Copyright (c) 2007 - 2016 Manos Batsis
 *
 * This file is part of Calipso, a software platform by www.Abiss.gr.
 *
 * Calipso is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Calipso is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Calipso. If not, see http://www.gnu.org/licenses/agpl.html
 */
define(['calipso', 'model/resource'],
    function (Calipso, ResourceModel) {
        //Backbone.Form.editors.Markdown
        var TextModel = ResourceModel.extend({
                modelKey: "text",
            },
            // static members
            {
                parent: ResourceModel,
                showInMenu: true,
                label: "Text"
            });

        /**
         * Get the model class URL fragment corresponding this class
         * @returns the URL path fragment as a string
         */
        TextModel.prototype.getPathFragment = function () {
            return "texts";
        }
        TextModel.prototype.getFormSchemas = function () {
            // superclass schema
            var superSchema = TextModel.parent.getFormSchemas();
            // own schema
            var schema = {
                "sourceContentType": {
                    "search": {type: 'Select', options: ['text/plain', 'text/x-markdown', 'text/html']},
                    "default": {
                        type: 'Select',
                        options: ['text/plain', 'text/x-markdown', 'text/html'],
                        validators: ['required']
                    }
                },
                "source": {
                    "search": 'Text',
                    "default": {
                        type: BackboneFormMarkdown,
                        validators: ['required'],
                        editorAttrs: {"data-provide": 'markdown'}
                    },
                }

            };
            // return merged schemas
            return $.extend({}, superSchema, schema);
        }

        TextModel.prototype.getGridSchema = function () {
            var superSchema = ResourceModel.prototype.getGridSchema();
            var localSchema = [/*{
             name : "name",
             label : "name",
             cell : "string"
             }*/];
            // return merged schemas
            return $.extend({}, superSchema, localSchema);
        }
        return TextModel;
    });