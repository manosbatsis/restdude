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
        console.log("document a.on.click, href: " + href + ", a: ");
        console.log($a);
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

/****
 * MAIN NAVIGATION
 */

$(document).ready(function($){

    // Add class .active to current link - AJAX Mode off
    $.navigation.find('a').each(function(){

        var cUrl = String(window.location).split('?')[0];

        if (cUrl.substr(cUrl.length - 1) == '#') {
            cUrl = cUrl.slice(0,-1);
        }

        if ($($(this))[0].href==cUrl) {
            $(this).addClass('active');

            $(this).parents('ul').add(this).each(function(){
                $(this).parent().addClass('open');
            });
        }
    });

    // Dropdown Menu
    $.navigation.on('click', 'a', function(e){

        if ($.ajaxLoad) {
            e.preventDefault();
        }

        if ($(this).hasClass('nav-dropdown-toggle')) {
            $(this).parent().toggleClass('open');
            resizeBroadcast();
        }
    });

    function resizeBroadcast() {

        var timesRun = 0;
        var interval = setInterval(function(){
            timesRun += 1;
            if(timesRun === 5){
                clearInterval(interval);
            }
            window.dispatchEvent(new Event('resize'));
        }, 62.5);
    }

    /* ---------- Main Menu Open/Close, Min/Full ---------- */
    $('.navbar-toggler').click(function(){

        if ($(this).hasClass('sidebar-toggler')) {
            $('body').toggleClass('sidebar-hidden');
            resizeBroadcast();
        }

        if ($(this).hasClass('sidebar-minimizer')) {
            $('body').toggleClass('sidebar-minimized');
            resizeBroadcast();
        }

        if ($(this).hasClass('aside-menu-toggler')) {
            $('body').toggleClass('aside-menu-hidden');
            resizeBroadcast();
        }

        if ($(this).hasClass('mobile-sidebar-toggler')) {
            $('body').toggleClass('sidebar-mobile-show');
            resizeBroadcast();
        }

    });

    $('.sidebar-close').click(function(){
        $('body').toggleClass('sidebar-opened').parent().toggleClass('sidebar-opened');
    });

    /* ---------- Disable moving to top ---------- */
    $('a[href="#"][data-top!=true]').click(function(e){
        e.preventDefault();
    });

});

/****
 * CARDS ACTIONS
 */

$(document).on('click', '.card-actions a', function(e){
    e.preventDefault();

    if ($(this).hasClass('btn-close')) {
        $(this).parent().parent().parent().fadeOut();
    } else if ($(this).hasClass('btn-minimize')) {
        var $target = $(this).parent().parent().next('.card-block');
        if (!$(this).hasClass('collapsed')) {
            $('i',$(this)).removeClass($.panelIconOpened).addClass($.panelIconClosed);
        } else {
            $('i',$(this)).removeClass($.panelIconClosed).addClass($.panelIconOpened);
        }

    } else if ($(this).hasClass('btn-setting')) {
        $('#myModal').modal('show');
    }

});