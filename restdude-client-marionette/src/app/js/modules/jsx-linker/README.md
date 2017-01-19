jsx-linker
===========================================

Synopsis
---------------

JSX postprocessor to support various environments.

Motivation
---------------

JavaScript is an infrastructure language for many environments (like SQL, Lua, Visual Basic etc...).
Each environment uses a different code structure and assumptions because JavaScript doesn't have a standard module system or entry points.

This program converts the resulting code of JSX into the environment specific code.

Supported Output
----------------------

You can select the output format with `-t` option. This tool supports the following output formats:

### commonjs-lib, amd-lib, export-global

Add `exports` or `define` statements. `export-global` exports classes to global namespace. The classes that have an `__export__` qualifier are exported.

#### Your Library

```js
__export__ class Fib {
    static function calc(value : int) : int {
        // some code;
    }
}
```

#### Client Code

```js
var Fib = require('fib').Fib;
Fib.calc(value);
```

### extjs-app (experimental)

It supports `config` parameter, entry point function (`main`), and callback funcion for updating (`onUpdate`):

```js
class _Main
{
    static const config = {
        name: 'sencha-touch-app',
        icon: {
            '57': 'resources/icons/Icon.png',
            '72': 'resources/icons/Icon~ipad.png',
            '114': 'resources/icons/Icon@2x.png',
            '144': 'resources/icons/Icon~ipad@2x.png'
        },
        isIconPrecomposed: true,
        startupImage: {
            '320x460': 'resources/startup/320x460.jpg',
            '640x920': 'resources/startup/640x920.png',
            '768x1004': 'resources/startup/768x1004.png',
            '748x1024': 'resources/startup/748x1024.png',
            '1536x2008': 'resources/startup/1536x2008.png',
            '1496x2048': 'resources/startup/1496x2048.png'
        },
        requires: [
            'Ext.MessageBox'
        ],
        views: [] : string[]
    };

    static function main (argv : string[]) : void
    {
        // Destroy the #appLoadingIndicator element
        Ext.fly('appLoadingIndicator').destroy();
        // Initialize the main view
        var view = new MainView();
        Ext.Viewport.add(view as Ext.Component);
    }

    static function onUpdate () : void
    {
        Ext.Msg.confirm(
            "Application Update",
            "This application has just successfully been updated to the latest version. Reload now?",
            (buttonId, value, opt) -> {
                if (buttonId == 'yes') {
                    dom.window.location.reload();
                }
            }
        );
    }
}
```

### WebWorker

```js
import "js/web.jsx";
import "webworker.jsx";

class _Main
{
    static function main(argv : string[]) : void
    {
        // called when this worker is initialized
    }

    __export static function onmessage(event : MessageEvent) : void
    {
        // called when `postMessage` is called from main script
        self.postMessage("message");
    }
}
```

### ngCore

```js
import "ngcore.jsx";

class _Main
{
    static var game : Game;
    static var hud : DebugControl;

    static function main (argv : string[]) : void
    {
        var glView = new UI.GLView();
        glView.setOpenGLESVersion(UI.Commands.OpenGLESVersion.OpenGLES2);

        var w = Core.Capabilities.getScreenWidth();
        var h = Core.Capabilities.getScreenHeight();

        glView.onload = function() {
            _Main.hud = new DebugControl(glView);
            Core.UpdateEmitter.setTickRate(1/60);
            _Main.game = new Game();
            _Main.game.start();
        };

        glView.setAttribute('frame', [0, 0, w, h]);
        glView.setAttribute('active', true);
    }
}
```

Installation
---------------

```sh
$ npm install -g jsx-linker
```

Usage
---------------

```sh
$ jsx-linker [option] [inputjsfile]
```

### option

*   `-t templateName`, `--tempalte=templateName`

    Select template name. You can see all possible template names on the help description.

*   `-s`, `--stdin`

    Read source code from standard input.

*   `-o`, `--output`

    Output file name. If this option is not specified, it dumps resulting code to standard output.

*   `-h`, `--help``

    Display help

Usage Sample
-------------------

### Direct conversion with JSX

```sh
$ jsx --minify sample-class.jsx | jsx-linker -s -t nodejs-lib > sample-nodelib.js
```

### Convert from file

```sh
$ jsx-linker -s -t nodejs-lib -o sample-nodelib.js sample.js
```

### Show Help

```sh
$ jsx-linker --help
```

Development
-------------

## Repository

* Repository: git://github.com/shibukawa/jsx-linker.git
* Issues: https://github.com/shibukawa/jsx-linker/issues

## Run Test

```sh
$ grunt test
```

## Build

```sh
# Build application or library for JS project
$ grunt build

# Generate API reference
$ grunt doc

```

Author
---------

* shibukawa / yoshiki@shibu.jp

License
------------

MIT

Complete license is written in `LICENSE.md`.
