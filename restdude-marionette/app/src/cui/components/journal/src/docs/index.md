# Journal

This component collects and categorized logged data. The data can be filtered and output to the browser console at any time.

## Logging an entry

Call `journal.log()` with your argument(s). It's recommended that the first argument is a [settings object](#settings) which will help categorize the entry and guide its display.

### Settings

The settings object is used to "define" an entry so that it may be filtered. The settings are also used to determine how to display the entry in the console.

All properties are optional and the default values are shown. However, it is highly recommended to set the `owner` property so that issues may be triaged efficiently.

```js
{
    type: '',      // Type of message. May be 'error', 'warn', 'info', or 'log'. Defaults to 'log'.
    owner: 'Unknown', // The team whose code is responsible for the entry. Use 'UI' for UI Support, 'FW' for Framework, and 'Java' for JSP developers.
    module: '',    // Name of the component or module, e.g. 'table'
    submodule: '', // Name of the sub-module of the component, e.g. 'sort'
    func: '',      // Name of the function that is logging the entry
    element: '',   // A DOM element or selector that is relevant to the entry
    message: [],   // The main message or contents of the entry. This will be combined with the 2nd..nth arguments that are passed to `journal.log()`.
}
```

Other components may call `journal.getDefault()` to receive a copy of this object.

#### Messages

Note that there is no difference between the following two statements:

```js
journal.log('alpha', 'bravo', 'charlie');
journal.log({message: ['alpha', 'bravo']}, 'charlie');
```

It is often easier to simply pass the arguments directly to `journal.log()` rather than use the `message` property. However, `message` can come in handy when you need to build your list of arguments piecemeal at different points in your code.

## Printing entries

Call `journal.print()` to display all previously-logged entries in the console. You may pass an optional parameter to filter the results:

- String: 'error', 'log', 'warn', 'info'
    + Will only print entries with a matching `type`
- Object with one or more `settings` properties
    + Will only printing entries matching the properties that are defined. For example, `journal.print({owner:'UI'})` will only print entries where the `owner` was set to `'UI'`.

### Filtering by element

Note that when passing a filter object the `element` value may be either a DOM element or a selector string. If you pass a selector it will be matched against all logged entries' elements even if those elements were original given as DOM elements.

For example, suppose the page contains an element `<div id="alpha" class="bravo" data-delta="echo">` and the following entry was logged:

```js
journal.log({element: document.getElementById('alpha')}, 'foo', 'bar');
```

Any of these commands will print that entry:

```js
journal.print({element: document.getElementById('alpha')});
journal.print({element: '#alpha'});
journal.print({element: '.bravo'});
journal.print({element: 'div[data-delta]'});
```

This also applies if the entry was logged with an ID selector (e.g. `#alpha`) rather than a direct reference to the element.

This can come in handy if you wanted to find all entries related to tables:

```js
journal.log({element: '#table1'}, 'Table has no rows');
journal.log({element: '#table2'}, 'Table cannot be sorted');

// Find all table-related entries
journal.print({element: 'table'});
```

## Live mode

With this mode enabled, `journal` will immediately print an entry to the browser console as well as store the entry. The mode can be toggled using `journal.live()`:

```js
journal.live('on'); // Prints all entries as they are logged
journal.live('off'); // Prints only entries marked as `type: error`
journal.live('suppress'); // Does not print any entries, even ones marked as `type: error`
```

The default setting is `off` (only errors are printed live). Calling `journal.live()` with no argument will toggle it between `on` and `off`.

## Clearing entries

Use `journal.clear()` to delete all stored logs. Add the parameter `journal.clear(true)` to also clear the browser console.