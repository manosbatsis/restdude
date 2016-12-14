# UI Position

## Overview

The UI Position plugin allows for any element to be positioned on the page with absolutely or with respect to another element.  

## Usage

The plugin is called on the object to be positioned and must be provided with an options object.

```js
$('.my-button').uiPosition(options);
```

## Options

At minimum, the options must contain `positionType` to position the element absolutely. If you would like to position with respect to another element the `respectTo` field is also required:

```js
$('.my-button').uiPosition({
    positionType: "center-center",
});
```

For shorthand, you can position absolutely by passing the position as a string:

```js
$('.my-button').uiPosition("center-center");
```

### Options

Property | Type | Description
--- | --- | ---
`positionType` | String | The position location that should be used (required)
`offset` | Number or Object | Can set `offsetX` and `offsetY` individually or pass a string to set both x and y to the same value.
`respectTo` | Object | With relative positioning, specify the element to position around.
`overrideMaxDimensions` | Boolean | Should max dimensions inline styles be overwritten when positioning styles are reset. `Default: true` 

### Position Locations

Absolute | Relative <br/> <small><em>*Requires `respectTo` option</em></small>
--- | --- 
`top-left` | `above-left`
`top-center` | `above-center`
`top-right` | `above-right`
`center-left` | `below-left`
`center-center` | `below-center`
`center-right` | `below-right`
`bottom-left` | `inline-left`
`bottom-center` | `inline-right`
`bottom-right` |


## Absolute postion example

```js
$('.myContent').uiPosition({
    positionType: "top-center", 
    offset: {
        offsetY: 5,
    },
});
```

## Relative postion example

```js
$('.myContent').uiPosition({
    positionType: "above-right", 
    offset: 5,
    respectTo: myContent.$button,
});
```
