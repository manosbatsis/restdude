define(['jquery', 'cui', 'guid', 'withinviewport', 'uiBox', 'uiPosition'], function ($, cui, guid, withinviewport) {
    ///////////////
    // Constants //
    ///////////////

    var VERSION = '2.0.2';
    var NAMESPACE = 'popover';

    var EVENT_NAMES = {
        show:   'show.cui.' + NAMESPACE,
        shown:  'shown.cui.' + NAMESPACE,
        hide:   'hide.cui.' + NAMESPACE,
        hidden: 'hidden.cui.' + NAMESPACE,
    };

    var CLASSES = {
        popover: 'cui-' + NAMESPACE,
        toggle: 'cui-' + NAMESPACE + '-toggle',
        closeButton: 'cui-' + NAMESPACE + '-hide',
        useArrow: 'cui-' + NAMESPACE + '-use-arrow',
        arrow: 'cui-' + NAMESPACE + '-arrow',
        popoverBody: 'cui-' + NAMESPACE + '-body',
        mobileBreakpoint: 'cui-breakpoint-mobile',
    };

    var MOBILE_BREAKPOINT = 600;
    var MOBILE_PADDING = 6;

    var priv = {};
    var popoverList = {};
    var $body = $('body');
    var $window = $(window);

    /////////////////
    // Constructor //
    /////////////////

    var Popover = function _Popover (elem, options) {
        // Create both a jQuery copy and a regular DOM copy of the element
        if (elem instanceof $) {
            this.$button = elem;
            this.button = elem.get(0);
        }
        else if (elem instanceof HTMLElement) {
            this.button = elem;
            this.$button = $(elem);
        }

        // Store the options
        this.options = options;

        // Extract data attribute options
        this.metadata = this.$button.data('popover-options');

        return this;
    };

    //////////////////////
    // Plugin prototype //
    //////////////////////

    Popover.prototype = {};

    // Default user options
    Popover.prototype.defaults = {
        html: '',
        display: {
            css: {
            },
            className: '',
            offset: {
                top: 0,
                left: 0,
            },
        },
        location: 'below-right',
        showPop: true,
        hideOnResize: false,
        hideOnEscape: true,
        gainFocus: false,
        isModal: true,
        useArrow: true,
        resizeMobile: true,
        hideOnScroll: true,
    };

    /**
     * Initializes the plugin
     * May be called multiple times
     */
    Popover.prototype.init = function _Popover_init () {
        var popover;
        var isInPageLink = (this.button.hasAttribute('href') && /^\#/.test(this.button.getAttribute('href')));

        // Introduce defaults that can be extended either globally or using an object literal
        if (typeof this.options === 'string') {
            this.config = $.extend(true, {}, this.defaults);
            this.config.html = this.options;
        }
        else {
            this.config = $.extend(true, {}, this.defaults, this.options, this.metadata);
        }

        // Create new popover object using this instance
        popover = this;

        popover.id = NAMESPACE + '_' + guid();
        popover.isShown = false;

        if (popover.config.html === '' && popover.$button.attr('title')) {
            popover.config.html = '<span>' + popover.$button.attr('title') + '</span>';
        }

        // Add a class to the button so we can tell whether it was clicked in `priv.onBodyClick()`
        popover.$button.addClass(CLASSES.toggle);

        // Create the popover element
        popover.$popover = priv.createPopover(popover);

        // Set up event listeners

        // Show/hide the popover when its button is clicked
        popover.$button.on('click', function _popover_onClick (evt) {
            // Prevent the page from jumping when the button links to another element
            if (isInPageLink) {
                evt.preventDefault();
            }

            if (popover.isShown === false) {
                priv.showPopover(popover);
            }
            else {
                priv.hidePopover(popover);
            }
        }.bind(popover));

        // Show/hide the popover when the user clicks outside of it
        // We need to give this function a name so it can be referenced later since we will turn it on and off. Other event listeners (e.g. window resize) are only ever turned on so we can just use anonymous functions without storing them.
        popover.onBodyClick = function _popover_onBodyClick (evt) {
            priv.onBodyClick(evt, popover);
        }.bind(popover);

        // Find the scrollable parent of the button
        popover.$scrollableContainer = popover.$button.closest(':scrollable');

        if (popover.$scrollableContainer.length) {
            // If the container happens to be the root, watch for `window` scrolling instead
            if (popover.$scrollableContainer.is('html')) {
                popover.$scrollableContainer = $window;
            }
            // Otherwise, scrolling parent is some other element
            else {
                // Unless `hideOnScroll` was explicitly set, force it to `always`. This is due to a limitation in `withinviewport` where it can only use the window as the viewport
                //TODO: Update the `withinviewport` lib to allow for any element to be considered the viewport, not just `window`
                if (typeof this.options !== 'object' || !this.options.hasOwnProperty('hideOnScroll')) {
                    popover.config.hideOnScroll = 'always';
                }
            }

            // Define `scroll` event handler for this popover, which will only be in effect while the popover is open
            popover.onParentScroll = function _popover_onParentScroll (evt) {
                priv.onParentScroll(evt, popover);
            }.bind(popover);
        }

        // Hide the popover when the Escape key is pressed
        if (popover.config.hideOnEscape) {
            $window.on('keyup', function _popover_onKeyup (evt) {
                priv.onWindowKeyup(evt, popover);
            }.bind(popover));
        }

        // Keep the popover aligned properly when window is resized
        popover.onWindowResize = function _popover_onResize (evt) {
            priv.onWindowResize(evt, popover);
        }.bind(popover);

        $window.on('resize', popover.onResize);

        // Adds this Popover instance to our list so we can track all of them
        popoverList[popover.id] = popover;

        // Return this instance of the plugin
        return popover;
    };

    /**
     * Hides the popover
     *
     * @param   {Function}  callback         Optional function to run after closing the popover. It will receive the Popover instance as an argument.
     * @param   {Boolean}   hideImmediately  Set to `true` to skip animation and event triggering
     */
    Popover.prototype.hide = function _Popover_hide (callback, hideImmediately) {
        priv.hidePopover(this, hideImmediately);

        // Check to see if the caller included a callback function
        if (typeof callback === 'function') {
            callback(this);
        }
    };

    /**
     * Display the popover
     *
     * @param   {Function}  callback  Optional function to run after closing the popover. It will receive the Popover instance as an argument.
     */
    Popover.prototype.show = function _Popover_show (callback) {
        priv.showPopover(this);

        // Check to see if the caller included a callback function
        if (typeof callback === 'function') {
            callback(this);
        }
    };

    /**
     * (Re)position the popover
     *
     * @param   {Function}  callback  Optional function to run after closing the popover. It will receive the Popover instance as an argument.
     */
    Popover.prototype.position = function _Popover_position (callback) {
        priv.positionPopover(this);

        // Check to see if the caller included a callback function
        if (typeof callback === 'function') {
            callback(this);
        }
    };

    /**
     * Destroy the popover
     *
     * @param   {Function}  callback  Optional function to run after closing the popover. It will receive the Popover instance as an argument.
     */
    Popover.prototype.destroy = function _Popover_destroy (callback) {
        var popover = this;
        var index = -1;

        // Hide it
        if (popover.isShown) {
            // Pass the "hide immediately" flag. A few lines below here we will remove the element so we don't want it to awkwardly disappear during the closing animation
            priv.hidePopover(popover, true);
        }

        // Undo any changes to the button
        popover.$button
            .removeClass(CLASSES.toggle)
            .off('click');

        // Remove the element
        popover.$popover
            .empty()
            .remove();

        // Remove event listeners from other elements

        if (popover.config.hideOnEscape) {
            $window.off('keyup');
        }

        $window.off('resize', popover.onWindowResize);

        $body.off('click', popover.onBodyClick);
        popover.onBodyClick = null;

        if (popover.onParentScroll) {
            popover.$scrollableContainer.off('scroll', popover.onParentScroll);
        }

        // Remove this Popover instance from our list
        delete popoverList[popover.id];

        // Check to see if the caller included a callback function
        if (typeof callback === 'function') {
            callback(popover);
        }

        return popover;
    };

    /////////////////////
    // Private methods //
    /////////////////////

    // Opens a new popover window
    priv.showPopover = function _showPopover (popover) {
        // Hide other popovers
        if (popover.config.isModal) {
            priv.hideAllPopovers();
        }

        // Position it
        priv.positionPopover(popover);

        // Reveal it
        popover.$popover
            .animate(
                {opacity: 1},
                400,
                function _showPopover_animate () {
                    if (popover.gainFocus) {
                        $(this).focus();
                    }

                    popover.$popover.trigger(EVENT_NAMES.shown);
                    $window.trigger(EVENT_NAMES.shown);
                }
            );

        popover.isShown = true;

        // Add event listeners
        $body.on('click', popover.onBodyClick);

        if (popover.onParentScroll) {
            popover.$scrollableContainer.on('scroll', popover.onParentScroll);
        }

        popover.$popover.trigger(EVENT_NAMES.show);
        $window.trigger(EVENT_NAMES.show);
    };

    // Hides all popover instances
    priv.hideAllPopovers = function _hideAllPopovers () {
        Object.keys(popoverList).forEach(function (id) {
            priv.hidePopover(popoverList[id], true);
        });
    };

    /**
     * Hides a popover element
     *
     * @param   {Object}   popover           Popover instance
     * @param   {Boolean}  hideImmediately  Set to `true` to skip animation and event triggering
     */
    priv.hidePopover = function _hidePopover (popover, hideImmediately) {
        // Hide with animation and fire an event
        // This usually happens when a single popover is dismissed
        if (!hideImmediately) {
            // Animate it to hidden
            popover.$popover
                .animate(
                    {opacity: 0},
                    400,
                    function _hidePopover_animate () {
                        this.style.opacity = '0';
                        // Reset the position so that it doesn't cover other elements while invisible
                        this.style.top = '0';
                        this.style.left = '-9999em';

                        popover.$popover.trigger(EVENT_NAMES.hidden);
                        $window.trigger(EVENT_NAMES.hidden);
                    }
                )
                .trigger(EVENT_NAMES.hide);

            $window.trigger(EVENT_NAMES.hide);
        }
        // Hide it immediately without animation or events
        // This usually means we're closing all popovers before opening a new one and we don't want to create a delay
        else {
            popover.$popover
                .css({
                    opacity: 0,
                    top: '0',
                    left: '-9999em',
                })
                .trigger(EVENT_NAMES.hide)
                .trigger(EVENT_NAMES.hidden);

            $window.trigger(EVENT_NAMES.hide);
            $window.trigger(EVENT_NAMES.hidden);
        }

        popover.isShown = false;

        $body.off('click', popover.onBodyClick);

        if (popover.onParentScroll) {
            popover.$scrollableContainer.off('scroll', popover.onParentScroll);
        }

        priv.enablePageScrolling();
    };

    // Create the popover container element
    priv.createPopover = function _createPopover (popover) {
        var boxOptions = {};

        boxOptions.className = CLASSES.popover + ' ' + popover.config.display.className;
        boxOptions.css = {opacity: '0'};

        if (popover.config.display.css) {
            $.extend(boxOptions.css, popover.config.display.css);
        }

        if (popover.config.useArrow) {
            boxOptions.body = {};
            boxOptions.body.html = popover.config.html;
            boxOptions.body.className = CLASSES.popoverBody;

            boxOptions.className += ' ' + CLASSES.useArrow;
        }
        else {
            boxOptions.html = popover.config.html;
        }

        var $popoverBox = $.uiBox(boxOptions);

        // Used on mobile sizes
        if (popover.config.resizeMobile) {
            popover.$close = $('<button/>', {
                                'class': CLASSES.closeButton,
                                'tabindex': '1',
                            })
                            .text('Close Popover')
                            .on('click', function (evt) {
                                evt.preventDefault();
                                priv.hidePopover(popover);
                            });

            $popoverBox.append(popover.$close);
        }

        $popoverBox.appendTo(document.body);

        return $popoverBox;
    };

    // Function that will position the popover on the page using uiPosition
    priv.positionPopover = function _positionPopover (popover) {
        if (popover.config.useArrow) {
            priv.removeArrow(popover);
            priv.resetInnerContentHeight(popover);
        }

        var popoverOffset = {};
        var popoverDefaultCSS = {};

        // Convert popover offset call into uiPosition config call
        if (popover.config.display.offset) {
            if (popover.config.display.offset.top) {
                popoverOffset.offsetY = popover.config.display.offset.top;
            }

            if (popover.config.display.offset.left) {
                popoverOffset.offsetX = popover.config.display.offset.left;
            }
        }

        if (popover.config.display && popover.config.display.css) {
            popoverDefaultCSS = popover.config.display.css;
        }

        if (!popover.config.resizeMobile || (popover.config.resizeMobile && (window.innerWidth > MOBILE_BREAKPOINT))) {
            // Remove mobile class
            popover.$popover.removeClass(CLASSES.mobileBreakpoint);
            priv.resetSize(popover);

            priv.enablePageScrolling();

            var positionOptions = {
                positionType: popover.config.location,
                respectTo: popover.$button,
                offset: popoverOffset,
                defaultCSS: popoverDefaultCSS,
            };

            popover.$popover.uiPosition(positionOptions);

            if (popover.config.useArrow) {
                priv.positionArrow(popover);
                priv.setInnerContentHeight(popover);
            }
        }
        else {
            // Add mobile class
            popover.$popover.addClass(CLASSES.mobileBreakpoint);
            priv.setFullSize(popover);

            priv.disablePageScrolling();

            popover.$popover.uiPosition({
                positionType: 'center-center',
            });
        }
    };

    priv.positionArrow = function _positionArrow (popover) {
        var positionData = popover.$popover.data('uiPosition');
        var currentPosition = '';
        var validPosition = false;

        if (positionData) {
            currentPosition = positionData.currentPosition;
        }

        if (currentPosition) {
            var adjustedTop;
            var arrowHeight = 7;
            var adjustedHeight;

            var buttonOffset = popover.$button.offset();
            var buttonWidth = popover.$button.outerWidth();
            var buttonHeight = popover.$button.outerHeight();

            var buttonCenterX = buttonOffset.left + buttonWidth / 2;
            var buttonCenterY = buttonOffset.top + buttonHeight / 2;

            var popoverLeft = parseInt(popover.$popover.css('left'));

            var arrowLeft = buttonCenterX - popoverLeft;

            popover.$arrow = $('<div/>', {
                                'class': CLASSES.arrow,
                            });

            var popoverBackground = popover.$popover.css('background-color');

            if ( (currentPosition === 'below-left') || (currentPosition === 'below-center') || (currentPosition === 'below-right') ) {
                popover.$arrow.css({
                                    left: arrowLeft + 'px',
                                    bottom: '100%',
                                    borderColor: 'transparent transparent ' + popoverBackground + ' transparent',
                                });

                adjustedTop = parseInt(popover.$popover.css('top')) + arrowHeight;
                popover.$popover.css({top: adjustedTop+'px'});

                validPosition = true;
            }
            else if ( (currentPosition === 'above-left') || (currentPosition === 'above-center') || (currentPosition === 'above-right') ) {
                popover.$arrow.css({
                                    left: arrowLeft + 'px',
                                    bottom: (2*-arrowHeight) + 'px',
                                    borderColor: popoverBackground + ' transparent transparent transparent',
                                });

                adjustedTop = parseInt(popover.$popover.css('top')) - arrowHeight;
                popover.$popover.css({top: adjustedTop + 'px'});

                validPosition = true;
            }
            else if (currentPosition === 'inline-left') {
                popover.$arrow.css({
                                    left: 'auto',
                                    right: (2*-arrowHeight) + 'px',
                                    bottom: '50%',
                                    transform: 'translate(0, 50%)',
                                    borderColor: 'transparent transparent transparent ' + popoverBackground,
                                });

                adjustedLeft = parseInt(popover.$popover.css('left')) - arrowHeight;
                popover.$popover.css({left: adjustedLeft + 'px'});

                validPosition = true;
            }
            else if (currentPosition === 'inline-right') {
                popover.$arrow.css({
                                    left: -arrowHeight + 'px',
                                    bottom: '50%',
                                    transform: 'translate(0, 50%)',
                                    borderColor: 'transparent ' + popoverBackground + ' transparent transparent',
                                });

                adjustedLeft = parseInt(popover.$popover.css('left'), 10) + arrowHeight;
                popover.$popover.css({left: adjustedLeft + 'px'});

                validPosition = true;
            }

            if (validPosition) {
                popover.$popover.append(popover.$arrow);

                adjustedHeight = parseInt(popover.$popover.css('max-height'), 10) - 5;

                popover.$popover.css({'max-height': adjustedHeight + 'px'});
            }
        }
    };

    priv.removeArrow = function _removeArrow (popover) {
        if (popover.$arrow) {
            popover.$arrow.remove();
        }
    };

    priv.resetInnerContentHeight = function _resetInnerContentHeight (popover) {
        var popoverBody = popover.$popover.find('.'+CLASSES.popoverBody);

        popoverBody.css({
                        maxHeight: '',
                        height: '',
                    });
    };

    priv.setInnerContentHeight = function _setInnerContentHeight (popover) {
        var popoverBody = popover.$popover.find('.'+CLASSES.popoverBody);
        var popoverHeight = popover.$popover.height();

        popoverBody.css({
                        maxHeight: popoverHeight + 'px',
                        height: popoverHeight + 'px',
                    });
    };

    priv.setFullSize = function _setFullSize (popover) {
        var maxWidth = $(window).width() - MOBILE_PADDING * 2;
        var maxHeight = $(window).height() - MOBILE_PADDING * 2;

        popover.$popover.css({height: maxHeight, width: maxWidth});
    };

    priv.resetSize = function _resetSize (popover) {
        var defaultWidth = 'auto';
        var defaultHeight = 'auto';

        if (popover.config.display.css.width) {
            defaultWidth = popover.config.display.css.width;
        }

        if (popover.config.display.css.height) {
            defaultHeight = popover.config.display.css.height;
        }

        popover.$popover.css({
                            height: defaultWidth,
                            width: defaultHeight,
                        });
    };

    /**
     * Disables scrolling on the page
     *
     * If the popover has scrollable content, when you reach the bottom or top of the popover's content and keep scrolling the body itself will begin scrolling. These styles will prevent that from happening which means the user won't lose their place.
     */
    priv.disablePageScrolling = function _disablePageScrolling () {
        document.body.style.height = '100%';
        document.body.style.overflow = 'hidden';
    };

    /**
     * Allows the page body to scroll
     */
    priv.enablePageScrolling = function _enablePageScrolling () {
        document.body.style.removeProperty('height');
        document.body.style.removeProperty('overflow');
    };

    ////////////
    // Events //
    ////////////

    // Handles clicks away from the popover
    priv.onBodyClick = function _onBodyClick (evt, popover) {
        var $target = $(evt.target);

        // Make sure the user didn't click in/on the toggle button, or on the popover itself
        if (evt.target !== popover.$button.get(0) && !$target.closest('.' + CLASSES.popover + ', .' + CLASSES.toggle).length) {
            if (popover.isShown) {
                priv.hidePopover(popover);
            }
        }
    };

    // Handles the window resize event
    priv.onWindowResize = function _onWindowResize (evt, popover) {
        if (popover.isShown) {
            if (popover.config.hideOnResize) {
                priv.hidePopover(popover);
            }
            // Check if the popover is now outside the viewport and was configured to hide in this scenario
            else if (popover.config.hideOnScroll === true && !withinviewport(popover.$popover.get(0))) {
                priv.hidePopover(popover);
            }
            else {
                priv.positionPopover(popover);
            }
        }
    };

    // Watches for the escape key to be pressed and hides any open popover with the relevant setting
    priv.onWindowKeyup = function _onWindowKeyup (evt, popover) {
        // Escape key was pressed
        if (popover.isShown && evt.keyCode === 27) {
            priv.hidePopover(popover);
        }
    };

    priv.onParentScroll = function _onParentScroll (evt, popover) {
        // Popover is currently visible and it's supposed to hide when scrolling
        if (popover.isShown && popover.config.hideOnScroll) {
            // Hide popover upon any scroll event
            if (popover.config.hideOnScroll === 'always') {
                priv.hidePopover(popover);
            }
            // Hide popover only if it's (at least partially) outside the viewport
            else if (!withinviewport(popover.$popover.get(0))) {
                priv.hidePopover(popover);
            }
        }
    };

    //////////////////////////////////////////
    // Expose public properties and methods //
    //////////////////////////////////////////

    Popover.defaults = Popover.prototype.defaults;

    Popover.version = VERSION;

    // Define jQuery plugin
    window.$.fn.popover = function $_fn_popover (options) {
        return this.each(function $_fn_popover_each () {
            new Popover(this, options).init();
        });
    };

    window.$.popover = function $_popover (toggler, options) {
        return new Popover(toggler, options).init();
    };
});
