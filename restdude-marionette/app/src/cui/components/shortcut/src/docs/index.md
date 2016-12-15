# Shorcut

## Overview

The shortcut plugin allows components to register keyboard shortcuts that initiate actions.

## Usage

Shortcuts are registered by calling `shortcut.register()` and specifying at least the key combination and a callback function. A given key combination may only be used once; any further attempts to register that combination will be rejected.

### Standard usage

The `register()` method accepts a [settings object](#settings):

```js
shortcut.register({
    keys: 'shift+e',
    callback: function () { alert('You pressed Shift+E'); },
    description: 'Displays a simple alert'
});
```

### Shorthand usage

For simple usage you may supply only the key combination, callback function, and an optional (but strongly recommended) description:

```js
shortcut.register('shift+f', function () { alert('You pressed Shift+F'); }, 'Displays a simple alert');
```

## Settings

Property | Type | Description
--- | --- | ---
`keys` | String | The key combination with individual keystrokes separated by a `+` symbol. Required.
`callback` | Function | A function which will be called when the key combo is pressed by the user. This function will be provided with up to two parameters: the `Event` object, and a data object if you provided once when registering the shortcut. Required.
`description` | String | Describes to the user what will happen when the key combo is pressed. Optional, but strongly recommended.
`type` | String | The type of Event to listen for. Must be `keydown`, `keyup`, or `keypress`. Will default to `keyup` unless another valid type is specified.
`scope` | Mixed | Describes a DOM node within which the key combo must be pressed. It may be a selector (string), DOM element, or jQuery collection. Defaults to `document.body`. Optional.
`data` | Mixed | Any data that you wish to be passed along to your callback function. This object is not parsed by the plugin. Optional.

## Disabling and enabling shortcuts

You may disable and re-enable shortcuts from other components by using the methods below. Note that this must be done with extreme caution and consideration for other components. For example, you might need to temporarily disable shortcuts only while your component is in active use.

```js
shortcut.disable('shift+f'); // Disables the `shift+f` shortcut
shortcut.enable('shift+f');  // Enables the `shift+f` shortcut
shortcut.disableAll(); // Disables all shortcuts
shortcut.enableAll();  // Enables all shortcuts
shortcut.disableAll('shift+f'); // Enables all shortcuts except for `shift+f`
shortcut.enableAll('shift+f');  // Enables all shortcuts except for `shift+f`
```

## Notes

At any time the user may press `?` to see a list of all registered shortcuts.

The tab key may not be used as part of any key combination.
