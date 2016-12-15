# Popover

## Overview

The popover plugin displays HTML in a container that overlays the page. It is triggered by clicking on a button or other UI element and the container is positioned near the element. The popover can be displayed and hidden at will.

## Usage

### Simple

The plugin must be provided with an options object and a jQuery collection of elements.

```js
$('.my-button').popover(options);
```

Clicking `.my-button` will toggle display of the popover.

To show or close a popover programmatically, trigger a `click` event on the element:

```js
$('.my-button').click();
```

### Advanced

You may store a reference to the popover instance by invoking the plugin this way:

```js
var myPopover = $.popover($('.my-button'), options);
```

The above example will 'link' the popover to the element described by `$('.my-button')`.

To show, hide, (re)position, or destroy a popover programmatically in this case, use these methods:

```js
myPopover.show();
myPopover.hide();
myPopover.position();
myPopover.destroy();
```

All of those methods take an optional callback function as an argument. The function will be called when the task successfully completes and will be provided with the instance of the popover as an argument.

The `.hide()` method also takes an optional second argument, a boolean, which denotes whether to close the popover immediately without animation.

You may also refer to additional properties and elements of the popover instance:

```js
myPopover.$popover // The popover container element, as a jQuery collection
myPopover.$button  // The button element that will toggle the popover when clicked, as a jQuery collection
myPopover.isShown  // Whether the popover is currently shown, as a Boolean
myPopover.id       // A unique ID beginning with `popover_`
```

## Options

At minimum, the options must contain HTML to display:

```js
$('.my-button').popover({
    html: '<p>Hello world</p>'
});
```

For shorthand, if you only need to specify the HTML you can just pass a string:

```js
// Equivalent to the previous example
$('.my-button').popover('<p>Hello world</p>');
```

### Possible options

Property | Type | Description
--- | --- | ---
`html` | String | Contents to be displayed (required)
`display` | Object | Defines the display properties of the popover. See [display options](#display-options) below.
`location` | String | Describes where the popover should be positioned, relative to its button. The default value is `'below-right'`. See [location options](#location-options) below.
`showPop` | Boolean | Defaults to `true`.
`closeOnResize` | Boolean | Specifies whether to close the popover if the window is resized. Defaults to `false`.
`closeOnEscape` | Boolean | Specifies whether to close the popover if the user presses the escape key. Defaults to `true`.
`gainFocus` | Boolean | Specifies whether to set focus to the popover element when it appears. Defaults to `false`.
`isModal` | Boolean | Specifies whether to close other popovers before opening this one. Defaults to `true`. If you have nested popovers it's useful to set this to `false` on the child popover.

### Display options

Property | Type | Description
--- | --- | ---
`css` | Object | A jQuery-style object of CSS properties and values to be applied to the popover container
`width` | String | The width of the popover (must be a CSS-friendly value; default: `auto`)
`height` | String | The height of the popover (must be a CSS-friendly value; default: `auto`)
`id` | String | Optional ID to be assigned to the popover element
`className` | String | Optional class name(s) to be added to the popover

### Location options

The location value is treated as a request rather than a mandate &mdash; that is, if the popover will not fit it in the desired location without being clipped by the browser window, the plugin will find a suitable alternative location. These fallbacks are listed below.

The terms `left` and `right` refer to the direction the popover will be facing. The opposite edge of the popover will be aligned with the button that toggles the popover. For example, the default location `below-right` will extend the popover to the *right* of the button, but the popover's *left* edge will be aligned with the button's left edge.

```
  ┌────────┐
  │ Button │
  └────────┘
  ┌─────────────────────────────────────┐
  | Popover with 'below-right' location |
  └─────────────────────────────────────┘
```

The term `center` means that the popover will be horizontally centered with its button, and `inline` means the popover will be vertically centered with its button.

Note that if your popover is wider than the browser window, the right side of the popover will be clipped from view. Be sure to style your popover such that elements will flow and wrap to avoid being too wide.

#### Acceptable location values

- `below-left`
    + Fallback: When clipped by the left edge of the window, the popover will shift to the right just enough to keep from being clipped by the browser window.
- `below-right`
    + Fallback: When clipped by the right edge of the window, it will shift to the left just enough to keep from being clipped by the browser window.
- `below-center`
    + Fallback: When clipped by the left edge of the window, it will shift right just enough to remain in view. When clipped by the right edge of the window, it will switch to `below-right`.
- `above-left`
    + Fallback: When clipped by the top of the window, it will switch to `below-left`. When clipped by the left edge of the window, it will shift right just enough to remain in view.
- `above-right`
    + Fallback: When clipped by the right edge of the window, it will shift left just enough to remain in view. When clipped by the top edge of the window, it will switch to `below-right`.
- `above-center`
    + Fallback: When clipped by the top of the window, it will switch to `below-center`. When clipped by the left, it will shift right just enough to remain in view. When clipped by the right, it will shift left just enough to remain in view.
- `inline-left`
    + Fallback: When clipped by the left edge of the window it will switch to `below-left`.
- `inline-right`
    + Fallback: When clipped by the right edge of the window it will switch to `below-right`.

## Example with default values

```js
$('.my-button').popover({
    html: '',
    display: {
        id: '',
        className: ''
    },
    location: 'below-right',
    showPop: true,
    closeOnResize: false,
    closeOnEscape: true,
    gainFocus: false,
    isModal: true,
});
```

## Events

You may listen for the following events to track the popover's behavior:

Event type | Description
--- | --- | ---
`show.cui.popover` | This fires immediately when the popver begins to appear (e.g. when clicking the button or calling `myPopover.show()`)
`shown.cui.popover` | This fires after the popver is fully displayed (i.e. after CSS animations have completed)
`hide.cui.popover` | This fires immediately when the popver begins to close (e.g. when clicking outside the popover or calling `myPopover.hide()`)
`hidden.cui.popover` | This fires after the popver is fully closed (i.e. after CSS animations have completed)

All events are fired on both `myPopover.$popover` and `window`.

## Specifications

Clicking outside of an open popover will close it.

Only one popover may be open at a time. If a popover is open when a second popover is triggered, the first popover is closed before opening the second popover.

The popover container has the class `.cui-popover`.
