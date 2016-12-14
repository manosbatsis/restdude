define(['jquery', 'cui'], function ($, cui) {

    /////////////
    // Globals //
    /////////////
    var NAMESPACE = 'uiPosition';

    var VERSION = '1.0.0';
    var CLASSES = {
        uiPosition: 'cui-' + NAMESPACE,
    };
    var SELECTORS = {};
    var EVENT_NAMES = {};
    var DEFAULTS = {};

    var REL_PADDING = 6;
    var CENTER_PADDING = 10;

    /////////////////////
    // Private methods //
    /////////////////////

    var _priv = {};

    _priv.resetPositioningStyles = function resetPositioningStyles(element, config) {
        var maxWidth = '';
        var maxHeight = '';

        if (config.defaultCSS && config.defaultCSS['max-width']) {
            maxWidth = config.defaultCSS['max-width'];
        }
        if (config.defaultCSS && config.defaultCSS['max-height']) {
            maxHeight = config.defaultCSS['max-height'];
        }

        //Reset styles used for positioning. Resolves any display issues if viewport is resized between shows
        $(element).css({
                        'margin':'auto',
                        'top':'auto',
                        'left':'auto',
                        'right':'auto',
                        'bottom':'auto',
                        'position': '',
                    });

        if (config.overrideMaxDimensions) {
            $(element).css({
                        'max-height': maxHeight,
                        'max-width': maxWidth,
                    });
        }
    };

    _priv.adjustMaxHeight = function adjustMaxHeight(element, config) {
        if (config.overrideMaxDimensions) {
            var maxHeight = '';
            var windowMaxHeight = '';

            if (config.defaultCSS && config.defaultCSS['max-height']) {
                maxHeight = parseInt(config.defaultCSS['max-height']);
            }

            windowMaxHeight = $(window).height() - 2 * CENTER_PADDING;

            if ((typeof maxHeight !== 'number') || (windowMaxHeight<maxHeight)) {
                maxHeight = windowMaxHeight;
            }

            $(element).css({
                            'max-height': maxHeight,
                        });
        }
    };

    _priv.adjustMaxWidth = function adjustMaxWidth(element, config) {
        if (config.overrideMaxDimensions) {
            var maxWidth = '';
            var windowMaxWidth = '';

            if (config.defaultCSS && config.defaultCSS['max-width']) {
                maxWidth = parseInt(config.defaultCSS['max-width']);
            }

            windowMaxWidth = $(window).width() - 2 *  CENTER_PADDING;

            if ((typeof maxWidth !== 'number') || (windowMaxWidth<maxWidth)) {
                maxWidth = windowMaxWidth;
            }

            $(element).css({
                            'max-width': maxWidth + 'px',
                        });
        }
    };

    _priv.positionTopLeft = function positionTopLeft (element, config) {
        $(element).css({
                        'top': config.offset.offsetY,
                        'left': config.offset.offsetX,
                        'bottom': 'auto',
                        'right': 'auto',
                        'margin-top': 'auto',
                        'margin-left': 'auto',
                        'margin-bottom': 'auto',
                        'margin-right': 'auto',
                        'position': 'fixed',
                    });
    };

    _priv.positionTopCenter = function positionTopCenter (element, config) {
        _priv.adjustMaxWidth(element, config);

        $(element).css({
                        'top': config.offset.offsetY,
                        'left': '50%',
                        'bottom': 'auto',
                        'right': 'auto',
                        'margin-top': 'auto',
                        'margin-left': ((-1 * $(element).outerWidth()) / 2) + 'px',
                        'margin-bottom': 'auto',
                        'margin-right': 'auto',
                        'position': 'fixed',
                    });
    };

    _priv.positionTopRight = function _positionTopRight (element, config) {
        $(element).css({
                        'top': config.offset.offsetY,
                        'left': 'auto',
                        'bottom': 'auto',
                        'right': config.offset.offsetX,
                        'margin-top': 'auto',
                        'margin-left': 'auto',
                        'margin-bottom': 'auto',
                        'margin-right': 'auto',
                        'position': 'fixed',
                    });
    };

     _priv.positionCenterLeft = function _positionCenterLeft (element, config) {
        _priv.adjustMaxHeight(element, config);

        $(element).css({
                        'top':'50%',
                        'left': config.offset.offsetX,
                        'bottom': 'auto',
                        'right': 'auto',
                        'margin-top': ((-1 * $(element).outerHeight()) / 2) + 'px',
                        'margin-left': 'auto',
                        'margin-bottom': 'auto',
                        'margin-right': 'auto',
                        'position': 'fixed',
                    });
    };

    _priv.positionCenterCenter = function _positionCenterCenter (element, config) {
        _priv.adjustMaxHeight(element, config);
        _priv.adjustMaxWidth(element, config);

        $(element).css({
                        'top':'50%',
                        'left': '50%',
                        'bottom': 'auto',
                        'right': 'auto',
                        'margin-top': ((-1 * $(element).outerHeight()) / 2) + 'px',
                        'margin-left': ((-1 * $(element).outerWidth()) / 2) + 'px',
                        'margin-bottom': 'auto',
                        'margin-right': 'auto',
                        'position': 'fixed',
                    });
    };

    _priv.positionCenterRight = function _positionCenterRight (element, config) {
        _priv.adjustMaxHeight(element, config);

        $(element).css({
                        'top':'50%',
                        'left': 'auto',
                        'bottom': 'auto',
                        'right': config.offset.offsetX,
                        'margin-top': ((-1 * $(element).outerHeight()) / 2) + 'px',
                        'margin-left': 'auto',
                        'margin-bottom': 'auto',
                        'margin-right': 'auto',
                        'position': 'fixed',
                    });
    };

    _priv.positionBottomLeft = function positionBottomLeft (element, config) {
        $(element).css({
                        'top':'auto',
                        'left': config.offset.offsetX,
                        'bottom':  config.offset.offsetY,
                        'right': 'auto',
                        'margin-top': 'auto',
                        'margin-left': 'auto',
                        'margin-bottom': 'auto',
                        'margin-right': 'auto',
                        'position': 'fixed',
                    });
    };

    _priv.positionBottomCenter = function positionBottomCenter (element, config) {
        _priv.adjustMaxWidth(element, config);

        $(element).css({
                        'top':'auto',
                        'left': '50%',
                        'bottom':  config.offset.offsetY,
                        'right': 'auto',
                        'margin-top': 'auto',
                        'margin-left': ((-1 * $(element).outerWidth()) / 2) + 'px',
                        'margin-bottom': 'auto',
                        'margin-right': 'auto',
                        'position': 'fixed',
                    });
    };

    _priv.positionBottomRight = function positionBottomRight (element, config) {
        $(element).css({
                        'top':'auto',
                        'left': 'auto',
                        'bottom':  config.offset.offsetY,
                        'right': config.offset.offsetX,
                        'margin-top': 'auto',
                        'margin-left': 'auto',
                        'margin-bottom': 'auto',
                        'margin-right': 'auto',
                        'position': 'fixed',
                    });
    };

    _priv.positionRespectTo = function _positionRespectTo (element, positioningElement, config) {
        var location = config.positionType;
        var position = {
            top: 0,
            left: 0,
        };

        var addedRightMargin = false;
        var windowWidth;
        var popoverWidth;
        var popoverHeightActual;
        var popoverHeightWithPadding;
        var buttonOffset;
        var buttonWidth;
        var buttonHeight;
        var difference;
        var relativeMaxHeight;

        var availableSpaceAbove;
        var availableSpaceBelow;

        var offsetX = config.offset.offsetX;
        var offsetY = config.offset.offsetY;

        var __getRelativeMaxHeight = function _getRelativeMaxHeight (top, windowHeight, offset) {
            var maxHeight = windowHeight - top - offset + $(window).scrollTop();

            return maxHeight;
        };

        var __setCurrentPositionData = function __setCurrentPositionData (element, position) {
            $(element).data(NAMESPACE, {currentPosition: position});
        };


        /**
         * Determines the position based on the requested location, detects boundary collisions, and falls back to other locations if necessary
         *
         * @param   {String}  location  Location of the popover
         * @param   {Object}  position  Position definition
         *
         * @return  {Object}            Updated position definition
         */
        var __determinePosition = function __determinePosition (location, position) {

            /**
             * Determines the top and left positioning for the popover
             * This is a very simple, nearly logic-less function that does not do boundary testing or fallbacks
             */
            var __getTopAndLeft = function __getTopAndLeft (placement) {
                // Returns the `top` value when the popover is above the button
                var __getTopWhenAbove = function __getTopWhenAbove () {
                    return buttonOffset.top - popoverHeightActual - REL_PADDING;
                };

                // Returns the `top` value when the popover is below the button
                var __getTopWhenBelow = function __getTopWhenBelow () {
                    return buttonOffset.top + buttonHeight + REL_PADDING;
                };

                if (placement === 'below-left') {
                    position.left = buttonOffset.left + buttonWidth - popoverWidth + (REL_PADDING / 2);
                    position.top = __getTopWhenBelow();
                }
                else if (placement === 'above-left') {
                    position.left = buttonOffset.left + buttonWidth - popoverWidth + (REL_PADDING / 2);
                    position.top = __getTopWhenAbove();
                }
                else if (placement === 'below-right') {
                    position.left = buttonOffset.left;
                    position.top = __getTopWhenBelow();
                }
                else if (placement === 'above-right') {
                    position.left = buttonOffset.left;
                    position.top = __getTopWhenAbove();
                }
                else if (/^(above|below)\-center$/.test(placement)) {
                    // Vertical position is different for each `center` location
                    if (placement === 'below-center') {
                        position.top = __getTopWhenBelow();
                    }
                    else if (placement === 'above-center') {
                        position.top = __getTopWhenAbove();
                    }

                    // Horizontal position is the same for both `center` locations

                    // To determine the `left` value, start at the left edge of the button...
                    position.left = buttonOffset.left;

                    // ...then add half of the difference between the button's width and the popover's width
                    // If the popover is wider than the button, the difference will be a negative number which will actually pull the popover to the right (which is what we'd want to happen)
                    position.left += ((buttonWidth - popoverWidth) / 2);
                }
                else if (/^inline\-(right|left)$/.test(placement)) {
                    // Horizontal position is different for each `inline` location
                    if (placement === 'inline-left') {
                        position.left = buttonOffset.left - popoverWidth - REL_PADDING;
                    }
                    else if (placement === 'inline-right') {
                        position.left = buttonOffset.left + buttonWidth + REL_PADDING;
                    }

                    // Vertical position is the same for both `inline` locations

                    // To determine the `top` value, start at the top edge of the button...
                    position.top = buttonOffset.top;

                    // ...then add half of the difference between the button's height and the popover's height
                    // If the popover is taller than the button, the difference will be a negative number which will actually pull the popover upward (which is what we'd want to happen)
                    position.top += ((buttonHeight - popoverHeightActual) / 2);
                }
            };

            // Start off with a simple guess at the top and left values
            __getTopAndLeft(location);

            // Perform boundary detection and fallbacks based on the requested location
            // Note that not all locations have fallbacks. If they did, then we might create an infinite loop as each test fails and calls another fallback in turn. Instead, some of the locations merely tweak the positioning to find the most practical position for the popover. These locations are marked with a 'safe' comment -- falling back to a safe location will avoid infinite looping. Do not use a 'not safe' location as a fallback.

            // Safe (no recursive fallback)
            if (location === 'below-left') {
                // Clipped by the left edge of the screen
                if (position.left < 0) {
                    // Determine how far it is from the left edge (a negative value means it's being clipped)
                    difference = windowWidth - (position.left + popoverWidth + REL_PADDING);

                    // Shift the popover to the right just enough to fit on-screen
                    position.left = 0;

                    // Add a margin to prevent the popover from butting up against the edge of the screen. We cannot simply change the `left` value to create this gap because if the popover contains wrapping text the text will simply reflow and keep using as much width as possible.
                    $(element).css('margin-right', REL_PADDING + 'px');
                    addedRightMargin = true;
                }
                //Check and set max height

                relativeMaxHeight = __getRelativeMaxHeight(position.top, windowHeight, 2 * REL_PADDING);
                __setCurrentPositionData(element, 'below-left');
            }
            // Not safe (includes recursive fallback)
            else if (location === 'above-left') {
                // We need to verify two things in conjunction: that it's not clipped by the top of the window, and that it's not running off the left edge of the screen

                // Condition: clipped by the top edge of the window
                if (position.top < $(window).scrollTop()) {
                    // It does not matter whether the popover is also clipped by the left edge. While we can fix the `left` value easily (see next condition), our only recourse for `top` is to fallback to a safe location
                    position = __determinePosition('below-left', position);
                }
                // Condition: clipped by the left edge of the window only
                else if (position.left < 0) {
                    // Shift the popover to the right just enough to fit on-screen
                    position.left = 0;

                    // Add a margin to prevent the popover from butting up against the edge of the screen. We cannot simply change the `left` value to create this gap because if the popover contains wrapping text the text will simply reflow and keep using as much width as possible.
                    $(element).css('margin-right', REL_PADDING + 'px');
                    addedRightMargin = true;

                    __setCurrentPositionData(element, 'above-left');
                }
                else{
                    __setCurrentPositionData(element, 'above-left');
                }
            }
            // Safe (no recursive fallback)
            else if (location === 'below-right') {
                // Determine how far it is from the right edge (a negative value means it's being clipped)
                difference = windowWidth - (Math.ceil(position.left) + Math.ceil(popoverWidth) + (2 * REL_PADDING) + 2);
                // difference = windowWidth - (position.left + popoverWidth);

                // Clipped by the right edge
                if (difference < 0) {
                    // Shift the popover to the right just enough to fit on-screen
                    position.left += difference;
                    // position.left -= REL_PADDING;

                    // But make sure we didn't just push it off the left edge of the screen
                    if (position.left < 0) {
                        position.left = 0;

                        // Add a margin to prevent the popover from butting up against the edge of the screen. We cannot simply change the `left` value to create this gap because if the popover contains wrapping text the text will simply reflow and keep using as much width as possible.
                        $(element).css('margin-right', REL_PADDING + 'px');
                        addedRightMargin = true;
                    }
                }

                //Check and set max height
                relativeMaxHeight = __getRelativeMaxHeight(position.top, windowHeight, 2 * REL_PADDING);
                __setCurrentPositionData(element, 'below-right');

            }
            // Not safe (includes recursive fallback)
            else if (location === 'above-right') {
                // We need to verify two things inconjunction: that it's not clipped by the top of the window, and that it's not running off the left edge of the screen

                // Determine how far it is from the right edge (a negative value means it's being clipped)
                // difference = windowWidth - (position.left + popoverWidth + REL_PADDING);
                difference = windowWidth - (Math.ceil(position.left) + Math.ceil(popoverWidth) + (2 * REL_PADDING) + 2);

                // Condition: clipped by the top of the window
                if (position.top < 0) {
                    // It doesn't matter if it is also clipped by the right edge. While we could fix the `left` value easily (see next condition), our only recourse for `top` is to fallback to a safe location
                    position = __determinePosition('below-right', position);
                }
                // Condition: clipped by the right edge of the window only
                else if (difference < 0) {
                    // Shift the popover to the right just enough to fit on-screen
                    position.left += difference;
                    // position.left -= REL_PADDING;

                    // But make sure we didn't just push it off the left edge of the screen
                    if (position.left < 0) {
                        position.left = 0;

                        // Add a margin to prevent the popover from butting up against the edge of the screen. We cannot simply change the `left` value to create this gap because if the popover contains wrapping text the text will simply reflow and keep using as much width as possible.
                        $(element).css('margin-right', REL_PADDING + 'px');
                        addedRightMargin = true;
                    }
                    __setCurrentPositionData(element, 'above-right');
                }
                else{
                    __setCurrentPositionData(element, 'above-right');
                }
            }
            // Not safe (includes recursive fallback)
            else if (location === 'inline-left') {
                // Condition: clipped by the left edge of the screen
                if (position.left < 0) {
                    position = __determinePosition('below-left', position);
                }
                else{
                    __setCurrentPositionData(element, 'inline-left');
                }
            }
            // Not safe (includes recursive fallback)
            else if (location === 'inline-right') {
                // Condition: clipped by the right edge of the screen
                if (position.left + popoverWidth > windowWidth) {
                   __determinePosition('below-right', position);
                }
                else{
                    __setCurrentPositionData(element, 'inline-right');
                }
            }
            // Not safe (includes recursive fallback) unless only the `top` is broken
            else if (location === 'below-center') {
                // There are two bad scenarios: the popover is clipped by the right edge of the screen, or it's clipped by the left edge

                // Condition: clipped by the left edge of the screen
                if (position.left < 0) {
                    // Shift it to the right just enough to be on-screen
                    position.left = 0;

                    // Add a margin to prevent the popover from butting up against the edge of the screen. We cannot simply change the `left` value to create this gap because if the popover contains wrapping text the text will simply reflow and keep using as much width as possible.
                    $(element).css('margin-right', REL_PADDING + 'px');
                    addedRightMargin = true;

                    __setCurrentPositionData(element, 'below-center');

                }
                // Clipped by the right edge
                else if (position.left + popoverWidth > windowWidth) {
                   __determinePosition('below-right', position);
                }
                else{
                    __setCurrentPositionData(element, 'below-center');
                }
            }
            // Not safe (includes recursive fallback) when the `top` is broken
            else if (location === 'above-center') {
                // There are three bad scenarios we need to check for. The popover can be clipped by these edges of the screen:
                // 1. top
                // 2. left
                // 3. right
                // We do not need to check for combinations (e.g. clipped by the right and top edges) because our fallback for `top` will handle any horizontal issues

                // 1. Clipped by the top edge
                if (position.top < 0) {
                    // If the top is broken we are forced to move the popover below the button. There's no point looking into whether it also fails the left or right edge since our fallback will take care of that.
                    __getTopAndLeft('below-center');
                    position = __determinePosition('below-center', position);
                }
                // 2. Clipped by the left edge, but not the top
                else if (position.left < 0) {
                    // Shift it to the right just enough to be on-screen
                    position.left = 0;

                    // Add a margin to prevent the popover from butting up against the edge of the screen. We cannot simply change the `left` value to create this gap because if the popover contains wrapping text the text will simply reflow and keep using as much width as possible.
                    $(element).css('margin-right', REL_PADDING + 'px');
                    addedRightMargin = true;
                    __setCurrentPositionData(element, 'above-center');

                }
                // 3. Clipped by the right edge, but not the top
                else if (position.left + popoverWidth > windowWidth) {
                    // Determine how far it is from the left edge (a negative value means it's being clipped)
                    // difference = windowWidth - (position.left + popoverWidth + REL_PADDING);
                    difference = windowWidth - (Math.ceil(position.left) + Math.ceil(popoverWidth) + (2 * REL_PADDING) + 2);

                    // Shift the popover to the right just enough to fit on-screen
                    position.left += difference;
                    // position.left -= REL_PADDING;

                    // But make sure we didn't just push it off the left edge of the screen
                    if (position.left < 0) {
                        position.left = 0;

                        // Add a margin to prevent the popover from butting up against the edge of the screen. We cannot simply change the `left` value to create this gap because if the popover contains wrapping text the text will simply reflow and keep using as much width as possible.
                        $(element).css('margin-right', REL_PADDING + 'px');
                        addedRightMargin = true;
                    }
                    __setCurrentPositionData(element, 'above-center');
                }
                else{
                    __setCurrentPositionData(element, 'above-center');
                }
            }
            else {

                journal.log({type: 'error', owner: 'UI', module: 'uiPosition', submodule: 'positionRespectTo', func: '__determinePosition'}, 'Unsupported location "', location, '" ', element);

                return null;
            }

            return position;
        };

        // Gather measurements about key elements
        buttonOffset = $(positioningElement).offset();
        // buttonOffset.top = buttonOffset.top - $(window).scrollTop();

        buttonWidth = $(positioningElement).outerWidth();
        buttonHeight = $(positioningElement).outerHeight();

        popoverWidth = $(element).outerWidth() + (REL_PADDING / 2);
        popoverHeightActual = $(element).outerHeight(); // For inline positioning we want the actual height of the popover
        popoverHeightWithPadding = popoverHeightActual + (REL_PADDING / 2); // Above and below the button we want to account for padding, but only half of it because the button already has some visual padding built in

        windowWidth = window.innerWidth;
        windowHeight = window.innerHeight;

        // Get the positioning values for the requested location
        // Hint: this is the 'main' operation of this function and a good place to start for debugging. Most of the real work is done in `__determinePosition()`.

        position = __determinePosition(location, position);

        // No position found (e.g. the location was invalid)
        if (position === null) {
            return false;
        }

        if (relativeMaxHeight) {
            if ((config.defaultCSS && config.defaultCSS['max-height'])) {
                if (parseInt(config.defaultCSS['max-height']>relativeMaxHeight)) {
                    $(element).css('max-height', relativeMaxHeight);
                }
            }
            else{
                $(element).css('max-height', relativeMaxHeight);
            }
        }

        // Remove the margin that may have been added earlier in the page's lifecycle (e.g. before the window was resized)
        if (!addedRightMargin) {
            $(element).get(0).style.removeProperty('margin-right');
        }

        // Apply user-specified offsets. Need to update to either add or subtract offset based on the position to the element
        // if (offsetY > 0) {
            // if (location.toLowerCase().indexOf('below') >= 0) {
            //     position.top -= offsetY;
            // }
            // else{
                position.top += offsetY;
            // }

            // avoid negative margins
            if (position.top < 0) {
                position.top = 0;
            }
        // }

       // if (offsetX > 0) {
            // if (location.toLowerCase().indexOf('right') >= 0) {
            //     position.left -= offsetY;
            // }
            // else{
                position.left += offsetX;
            // }

            // avoid negative margins
            if (position.left < 0) {
                position.left = 0;
            }
        // }

        // Apply the positioning styles
        $(element).css({
                        left: Math.floor(position.left),
                        top: Math.floor(position.top),
                    });
    };


    ////////////////////
    // Public methods //
    ////////////////////

    var UIPosition = function (elem, options) {
        // Store the element to be positioned
        this.elem = elem;

        // Store the options
        this.options = options;
    };

    UIPosition.prototype = {};

    UIPosition.prototype.default = {
        positionType: null,
        offset: {
            offsetX: 0,
            offsetY: 0,
        },
        respectTo: null,
        overrideMaxDimensions: true,
        defaultCSS: null,
    };

    // Init function
    UIPosition.prototype.init = function () {
        // Create the uiPosition reference object
        var uiPosition = this;

        if (typeof this.options === 'string') {
            uiPosition.config = $.extend(true, {}, this.default);
            uiPosition.config.positionType = this.options;
        }
        else {
            uiPosition.config = $.extend(true, {}, this.default, this.options);
        }

        // Update offset if shortcut declaration used
        if (typeof uiPosition.config.offset === 'string' || typeof uiPosition.config.offset === 'number') {
            uiPosition.config.offset = {
                offsetX: this.options.offset,
                offsetY: this.options.offset,
            };
        }

        _priv.resetPositioningStyles(uiPosition.elem, uiPosition.config);

        // parse options and determine what process to return
        if (uiPosition.config.respectTo instanceof $) {
            _priv.positionRespectTo(uiPosition.elem, uiPosition.config.respectTo, uiPosition.config);
        }
        else {
            // Since no reference object was provided, position absolutely.
            if (uiPosition.config.positionType) {
                switch(uiPosition.config.positionType) {
                    case 'top-left':
                        _priv.positionTopLeft(uiPosition.elem, uiPosition.config);
                        break;

                    case 'top-center':
                        _priv.positionTopCenter(uiPosition.elem, uiPosition.config);
                        break;

                    case 'top-right':
                        _priv.positionTopRight(uiPosition.elem, uiPosition.config);
                        break;

                    case 'center-left':
                        _priv.positionCenterLeft(uiPosition.elem, uiPosition.config);
                        break;

                    case 'center-center':
                        _priv.positionCenterCenter(uiPosition.elem, uiPosition.config);
                        break;

                    case 'center-right':
                        _priv.positionCenterRight(uiPosition.elem, uiPosition.config);
                        break;

                    case 'bottom-left':
                        _priv.positionBottomLeft(uiPosition.elem, uiPosition.config);
                        break;

                    case 'bottom-center':
                        _priv.positionBottomCenter(uiPosition.elem, uiPosition.config);
                        break;

                    case 'bottom-right':
                        _priv.positionBottomRight(uiPosition.elem, uiPosition.config);
                        break;

                    default:
                        break;
                }
            }
        }
    };

    // Set the version number
    UIPosition.version = VERSION;

    // Define jQuery plugin with a source element
    $.fn.uiPosition = function (options) {
        return this.each(function () {
            return new UIPosition(this, options).init();
        });
    };
});
