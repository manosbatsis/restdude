# UI Box

## Overview

The UI Box plugin creates and returns a standard content wrapper element. It provides options for adding content within the main, body, header, and footer areas. 

## Usage

The plugin must be provided with an options object.

```js
var modal.$self =  $.uiBox(options);
```

## Options

A call with no options will return an empty wrapper element.
At minimum, the options must contain HTML to create a wrapper with content:

```js
var myContent = $.uiBox({
    html: '<p>Hello world</p>'
});
```

For shorthand, if you only need to specify the HTML you can just pass a string:

```js
// Equivalent to the previous example
var myContent = $.uiBox('<p>Hello world</p>');
```

### Options

Property | Type | Description
--- | --- | ---
`html` | String | Contents to be displayed (required). Adds content directly inside the main wrapper element.
`header` | Object | Defines the header properties of the uiBox (see below)
`body` | Object | Defines the body properties of the uiBox (see below)
`footer` | Object | Defines the footer properties of the uiBox (see below)
`id` | String | Optional ID to use for the uiBox, otherwise a unique id will be generated.
`css` | Object | Optional inline css to be added to the main wrapper element. Should be in a jQuery-ready format (e.g. `{color: 'red', maxWidth: '40%'}`).
`className` | String | Optional class name(s) to be added to the main wrapper element

### Header options
Creates an inserts a header section into the main content wrapper.

Property | Type | Description
--- | --- | ---
`html` | String | Contents to be displayed (required)
`className` | String | Optional class name(s) to add to the header element
`css` | Object | Optional inline CSS to be added to the header element. Should be in a jQuery-ready format (e.g. `{color: 'red', maxWidth: '40%'}`).


### Body options

Creates an inserts a body section into the main content wrapper.

Property | Type | Description
--- | --- | ---
`html` | String | Contents to be displayed (required)
`className` | String | Optional class name(s) to add to the body element
`css` | Object | Optional inline CSS to be added to the body element. Should be in a jQuery-ready format (e.g. `{color: 'red', maxWidth: '40%'}`).

### Footer options

Creates an inserts a footer section into the main content wrapper.

Property | Type | Description
--- | --- | ---
`html` | String | Contents to be displayed (required)
`className` | String | Optional class name(s) to add to the footer element
`css` | Object | Optional inline CSS to be added to the footer element. Should be in a jQuery-ready format (e.g. `{color: 'red', maxWidth: '40%'}`).


## Example with default values

```js
var myContent = $.uiBox({
    id: '',
    className: '',
    css: {},
    header: {
        html: '',
        className: '',
        css : {},
    },
    body: {
        html: '',
        className: '',
        css : {},
    },
    footer: {
        html: '',
        className: '',
        css : {},
    }
});
```
