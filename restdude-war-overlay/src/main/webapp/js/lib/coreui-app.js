define([
        'jquery', 'pace'],
    function ($, Pace) {


        /*****
         * CONFIGURATION
         */
        //Main navigation
        $.navigation = $('nav > ul.nav');

        $.panelIconOpened = 'icon-arrow-up';
        $.panelIconClosed = 'icon-arrow-down';

        //Default colours
        $.brandPrimary = '#20a8d8';
        $.brandSuccess = '#4dbd74';
        $.brandInfo = '#63c2de';
        $.brandWarning = '#f8cb00';
        $.brandDanger = '#f86c6b';

        $.grayDark = '#2a2c36';
        $.gray = '#55595c';
        $.grayLight = '#818a91';
        $.grayLighter = '#d1d4d7';
        $.grayLightest = '#f8f9fa';

        'use strict';

        /****
         * MAIN NAVIGATION
         */


        /****
         * CARDS ACTIONS
         */

        $(document).on('click', '.card-actions a', function (e) {
            e.preventDefault();

            if ($(this).hasClass('btn-close')) {
                $(this).parent().parent().parent().fadeOut();
            } else if ($(this).hasClass('btn-minimize')) {
                var $target = $(this).parent().parent().next('.card-block');
                if (!$(this).hasClass('collapsed')) {
                    $('i', $(this)).removeClass($.panelIconOpened).addClass($.panelIconClosed);
                } else {
                    $('i', $(this)).removeClass($.panelIconClosed).addClass($.panelIconOpened);
                }

            } else if ($(this).hasClass('btn-setting')) {
                $('#myModal').modal('show');
            }

        });

        function capitalizeFirstLetter(string) {
            return string.charAt(0).toUpperCase() + string.slice(1);
        }

        function init(url) {

            /* ---------- Tooltip ---------- */
            $('[rel="tooltip"],[data-rel="tooltip"]').tooltip({"placement": "bottom", delay: {show: 400, hide: 200}});

            /* ---------- Popover ---------- */
            $('[rel="popover"],[data-rel="popover"],[data-toggle="popover"]').popover();

        }


    });
