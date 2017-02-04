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
define(
    ["lib/restdudelib/util", 'backbone-forms-bootstrap3'],
    function (Restdude, BackboneFormsBootstrap) {

        Restdude.util.formTemplates = {
            horizontal: {
                formClassName: "form-horizontal",
                form: _.template('\
			<form class="form-horizontal" role="form">\
		    <div data-fieldsets></div>\
				<% if (fieldsInitiallyShown) { %>\
				<% } %>\
				<% if (submitButton) { %>\
				<button type="submit" class="submit btn btn-primary"><%= submitButton %></button>\
				<% } %>\
		  </form>'),
                field: _.template('\
		    <div class="form-group field-<%= key %>">\
		      <label class="col-xs-12  col-sm-12  col-md-3 col-lg-3 control-label" for="<%= editorId %>">\
		        <% if (titleHTML){ %><%= titleHTML %>\
		        <% } else { %><%- title %><% } %>\
		      </label>\
		      <div class="col-xs-12  col-sm-12  col-md-9 col-lg-9" data-editor></div>\
		      <p class="col-sm-12" class="help-block" data-error></p>\
		    </div>\
		  '),
            },
            vertical: {
                formClassName: "",
                form: _.template('\
			<form>\
		    <div  data-fieldsets></div>\
				<% if (fieldsInitiallyShown) { %>\
					<button class="btn btn-info btn-sm addLazyField" data-field="firstName">add<button>\
				<% } %>\
				<% if (submitButton) { %>\
				<button type="submit" class="submit btn btn-primary btn-block"><%= submitButton %></button>\
				<% } %>\
		  </form>'),
                field: _.template('\
		    <div class="form-group field-<%= key %>">\
					<% if (!placeHolderLabelsOnly) { %>\
			      <label class="control-label" for="<%= editorId %>">\
			        <% if (titleHTML){ %><%= titleHTML %>\
			        <% } else { %><%- title %><% } %>\
			      </label>\
					<% } %>\
		      <div data-editor></div>\
		      <p class="col-sm-12" class="help-block" data-error></p>\
		    </div>\
		  '),
            },
            inline: {
                formClassName: "form-inline",
                form: _.template('\
			<form class="form-inline">\
		    <span data-fieldsets></span>\
				<% if (fieldsInitiallyShown) { %>\
				<% } %>\
				<% if (submitButton) { %>\
				<span class="form-group bmd-form-group">\
				<button type="submit" class="submit btn btn-primary btn-sm"><%= submitButton %></button>\
				</span>\
				<% } %>\
		  </form>'),
                field: _.template('\
		    <div class="form-group field-<%= key %>">\
		      <label class="control-label" for="<%= editorId %>">\
		        <% if (titleHTML){ %><%= titleHTML %>\
		        <% } else { %><%- title %><% } %>\
		      </label>\
		      <span data-editor></span>\
		      <p class="col-sm-12" class="help-block" data-error></p>\
		    </div>\
		  '),
                fieldset: _.template('\
		    <span data-fields>\
		    </span>\
		  '),
            },
        };
        return Restdude;
    });
