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
define([
        'jquery', 'underscore', 'underscore-inflection', 'backbone', 'bootstrap',
        'restdude', 'modules-config', 'routers/MainRouter'],
    function ($, _, _inflection, Backbone, bootstrap,
              Restdude, modulesConfig, MainRouter) {


        //////////////////////////////////
        // Global backbone error handling
        //////////////////////////////////
        Backbone.ajax = function () {
            // Invoke $.ajaxSetup in the context of Backbone.$
            Backbone.$.ajaxSetup.call(Backbone.$, Restdude.getDefaultFetchOptions());
            return Backbone.$.ajax.apply(Backbone.$, arguments);
        };

        //////////////////////////////////
        // intercept links
        //////////////////////////////////
        $(document).on("click", "a", function (event) {

            var $a = $(this);
            var href = $a.attr("href");

            if (href && href.match(/^\/.*/) && !$(this).attr("target")) {
                Restdude.stopEvent(event);

                if ($a.hasClass("triggerCollapseMenu")) {
                    $a.closest("li.dropdown").removeClass('open');
                    // mobile, collapse hide
                    if ($(window).width() < 544) {
                        $a.closest(".navbar-toggleable-xs").removeClass('in');
                    }
                }
                //Backbone.history.navigate(href, true);
                Restdude.navigate(href, {
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
            contextPath: "restdude/",
            routers: {
                main: MainRouter
            }
        };

        Restdude.start(initOptions);

        return Restdude;
    });
