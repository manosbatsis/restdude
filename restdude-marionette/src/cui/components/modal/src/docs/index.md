# Modal

## Overview

The modal plugin displays HTML in an overlay on top of the current page. It can be triggered by clicking on an element or by calling the plugin directly. Once created, the modal can be displayed and hidden at will.

The plugin optionally provides an overlay "shield" between the page and the modal to prevent interaction with the page while the modal is displayed.

## Usage

The plugin must be provided with an options object.

### Opening

To open a modal when a button is clicked:

```js
$('.my-button').modal(options);
```

To open a modal programmatically, call it on the `window` object:

```js
var $myModal = $(window).modal(options);
```

### Hiding

There are two ways to hide a modal depending on whether you stored a reference to it.

To store a reference to the modal retreive the data from the button
`var myModal = $('#button').data('modal');`

With the stored reference, call `myModal.hideModal()`.
___
**Without a stored reference, call `$('.my-button').modal().hide()` where `.my-button` is an element that has a modal associated with it.

## Options

At minimum, the options must contain HTML to display:

```js
$('.my-button').modal({
    html: '<p>Hello world</p>'
});
```

For shorthand, if you only need to specify the HTML you can just pass a string:

```js
// Equivalent to the previous example
$('.my-button').modal('<p>Hello world</p>');
```

### Options

Property | Type | Description
--- | --- | ---
`html` | String | Contents to be displayed (required)
`display` | Object | Defines the display properties of the modal (see below)
`eventHandlers` | Object | A collection of custom functions that will be called when certain events occur (see below)
`overlay` | Mixed | An object or boolean that defines the overlay shield. Default: `true` (displays a tranparent overlay). See [overlay options](#overlay-options).
`autoOpen` | Boolean | Whether to automatically open the modal as soon as it is created. Default: `false`.
`hideOnEscape` | Boolean | Whether to hide the modal when the Escape key is pressed. Default: `true`.
`alwaysCenter` | Boolean | Whether to keep the modal centered in the viewport. Default: `true`.
`focusOnShow` | jQuery | An element that will gain focus when the modal is opened. If none is defined, the modal itself will gain focus. Default: `null`.
`focusOnHide` | jQuery | The element that will gain focus when the modal is hidden. If none is defined, focus will be handled by the browser. Default: `null`.
`onHide` | Function | Callback function that will be called just before the modal hides. It will receive the modal object as an argument. Default: `null`.
`onCreate` | Function | Callback function that will be called when the modal is created. It will receive the modal object as an argument. Default: `null`.

### Display options

Property | Type | Description
--- | --- | ---
`width` | String | The width of the modal (must be a CSS-friendly value)
`height` | String | The height of the modal (must be a CSS-friendly value)
`id` | String | Optional ID to be added to the modal element
`className` | String | Optional class name(s) to be added to the modal element
`css` | Object | Optional inline CSS to be added to the modal element. Should be in a jQuery-ready format (e.g. `{color: 'red', maxWidth: '40%'}`).
`closeButton` | Boolean | Display the close button on the modal. Default `true`

### Event handler options

You may provide callbacks for the following events:

Event type | Description
--- | ---
`resize` | Occurs when the window is resized

### Overlay options

Property | Type | Description
--- | --- | ---
`opacity` | Number | Optional opacity for the overlay element, between `0` (completely transparent) and `1` (completely opaque)
`className` | String | Optional class name(s) to add to the overlay DOM element
`closeOnClick` | Boolean | Close the modal when clicking the overlay. Default `true`

### Header options

Property | Type | Description
--- | --- | ---
`html` | String | Contents to be displayed (required)
`height` | String | The min-height of the header (must be a CSS-friendly value). The header will expand as needed based on content.
`className` | String | Optional class name(s) to add to the overlay DOM element
`css` | Object | Optional inline CSS to be added to the header element. Should be in a jQuery-ready format (e.g. `{color: 'red', maxWidth: '40%'}`).

### Footer options

Property | Type | Description
--- | --- | ---
`html` | String | Contents to be displayed (required)
`height` | String | The min-height of the footer (must be a CSS-friendly value). The footer will expand as needed based on content.
`className` | String | Optional class name(s) to add to the overlay DOM element
`css` | Object | Optional inline CSS to be added to the footer element. Should be in a jQuery-ready format (e.g. `{color: 'red', maxWidth: '40%'}`).

## Example with default values

```js
$('.my-button').modal({
    html: '',
    display: {
        width: '90%',
        height: '75%',
        id: '',
        className: '',
        css: {}
    },
    overlay: {
        suppress: false,
        opacity: 0.1,
        className: ''
    }
});
```

## Events

You may listen for the following events to track the modal's behavior:

Event type | Description
--- | --- | ---
`show.cui.modal` | This fires immediately when the popver begins to appear (e.g. when clicking the button or calling `mymodal.show()`)
`shown.cui.modal` | This fires after the popver is fully displayed (i.e. after CSS animations have completed)
`hide.cui.modal` | This fires immediately when the popver begins to hide (e.g. when clicking outside the modal or calling `mymodal.hide()`)
`hidden.cui.modal` | This fires after the popver is fully hidden (i.e. after CSS animations have completed)

All events are fired on both `myModal.$modal` and `window`.

## Specifications

If an overlay is rendered, clicking on the overlay (outside of the modal) will hide the modal and the overlay.

Only one modal may be open at a time. If a modal is open when a second modal is triggered, the first modal is hidden before opening the second modal.

The modal container has the class `.cui-modal`.

![](http://i.imgur.com/ryRnmRS.png)

![](http://i.imgur.com/05k01Hb.png)
