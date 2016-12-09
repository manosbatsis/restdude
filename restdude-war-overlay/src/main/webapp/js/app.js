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

define([
        'jquery', 'underscore', 'underscore-inflection', 'backbone', 'bootstrap',
        'calipso', 'modules-config', 'routers/MainRouter'],
    function ($, _, _inflection, Backbone, bootstrap,
              Calipso, modulesConfig, MainRouter) {


        //////////////////////////////////
        // Global backbone error handling
        //////////////////////////////////
        Backbone.ajax = function () {
            // Invoke $.ajaxSetup in the context of Backbone.$
            Backbone.$.ajaxSetup.call(Backbone.$, Calipso.getDefaultFetchOptions());
            return Backbone.$.ajax.apply(Backbone.$, arguments);
        };

        //////////////////////////////////
        // intercept links
        //////////////////////////////////
        $(document).on("click", "a", function (event) {

            var $a = $(this);
            var href = $a.attr("href");

            if (href && href.match(/^\/.*/) && !$(this).attr("target")) {
                Calipso.stopEvent(event);

                if ($a.hasClass("triggerCollapseMenu")) {
                    $a.closest("li.dropdown").removeClass('open');
                    // mobile, collapse hide
                    if ($(window).width() < 544) {
                        $a.closest(".navbar-toggleable-xs").removeClass('in');
                    }
                }
                //Backbone.history.navigate(href, true);
                Calipso.navigate(href, {
                    trigger: true
                })
            }
        });

        //////////////////////////////////
        // Bootstrap: enable tooltips
        //////////////////////////////////
        $(document).ready(function () {
            $(document.body).tooltip({
                selector: "[data-toggle=tooltip]",
                html: true
            });
        });

        //////////////////////////////////
        // Use POST instead of PUT/PATCH/DELETE
        //////////////////////////////////
        Backbone.emulateHTTP = true;

        //////////////////////////////////
        // Start the domain
        //////////////////////////////////
        var initOptions = {
            contextPath: "calipso/",
            routers: {
                main: MainRouter
            }
        };

        Calipso.start(initOptions);

        return Calipso;
    });
