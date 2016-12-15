// Source: https://github.com/litera/jquery-scrollintoview
;(function () {
    var converter = {
        vertical: { x: false, y: true },
        horizontal: { x: true, y: false },
        both: { x: true, y: true },
        x: { x: true, y: false },
        y: { x: false, y: true },
    };

    var scrollValue = {
        auto: true,
        scroll: true,
        visible: false,
        hidden: false
    };

    var rootrx = /^html$/i;

    if (window.$ && window.$.expr) {
        window.$.extend(window.$.expr[':'], {
            scrollable: function (element, index, meta, stack) {
                var direction = converter[typeof (meta[3]) === 'string' && meta[3].toLowerCase()] || converter.both;
                var styles = (document.defaultView && document.defaultView.getComputedStyle ? document.defaultView.getComputedStyle(element, null) : element.currentStyle);
                var overflow = {
                    x: scrollValue[styles.overflowX.toLowerCase()] || false,
                    y: scrollValue[styles.overflowY.toLowerCase()] || false,
                    isRoot: rootrx.test(element.nodeName),
                };

                // check if completely unscrollable (exclude HTML element because it's special)
                if (!overflow.x && !overflow.y && !overflow.isRoot) {
                    return false;
                }

                var size = {
                    height: {
                        scroll: element.scrollHeight,
                        client: element.clientHeight,
                    },
                    width: {
                        scroll: element.scrollWidth,
                        client: element.clientWidth,
                    },

                    // check overflow.x/y because iPad (and possibly other tablets) don't dislay scrollbars
                    scrollableX: function () {
                        return (overflow.x || overflow.isRoot) && this.width.scroll > this.width.client;
                    },
                    scrollableY: function () {
                        return (overflow.y || overflow.isRoot) && this.height.scroll > this.height.client;
                    },
                };

                return direction.y && size.scrollableY() || direction.x && size.scrollableX();
            }
        });
    }
}());
