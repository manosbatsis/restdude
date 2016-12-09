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
define(['backbone-forms'],
    function (BackboneForm) {
        Backbone.Form = Form = BackboneForm;
        var BackboneFormMarkdown = Backbone.Form.Editor.extend({

            tagName: 'textarea',

            defaultValue: '',

            previousValue: '',

            events: {
                'keyup': 'determineChange',
                'keypress': function (event) {
                    var self = this;
                    setTimeout(function () {
                        self.determineChange();
                    }, 0);
                },
                'select': function (event) {
                    this.trigger('select', this);
                },
                'focus': function (event) {
                    this.trigger('focus', this);
                },
                'blur': function (event) {
                    this.trigger('blur', this);
                }
            },

            initialize: function (options) {
                Form.editors.Base.prototype.initialize.call(this, options);
            },

            /**
             * Adds the editor to the DOM
             */
            render: function () {
                this.setValue(this.value);
                this.trigger('render', this);
                return this;
            },

            determineChange: function (event) {
                var currentValue = this.$el.val();
                var changed = (currentValue !== this.previousValue);

                if (changed) {
                    this.previousValue = currentValue;

                    this.trigger('change', this);
                }
            },

            /**
             * Returns the current editor value
             *
             * @return {String}
             */
            getValue: function () {
                return this.$el.val();
            },

            /**
             * Sets the value of the form element
             *
             * @param {String}
             */
            setValue: function (value) {
                this.$el.val(value);
            },

            focus: function () {
                if (this.hasFocus)
                    return;

                this.$el.focus();
            },

            blur: function () {
                if (!this.hasFocus)
                    return;

                this.$el.blur();
            },

            select: function () {
                this.$el.select();
            }

        });

        // Exports
        Backbone.Form.editors.Markdown = BackboneFormMarkdown;

        return BackboneFormMarkdown;
    });