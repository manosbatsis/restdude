# Core UI
Core UI is a dead-simple wrapper around your (React) component library, which aims to provide two benefits: simple, flat paths for import statements and 'switchable' presentational components. By abstracting away and decoupling your controls or base React component library of choice (whether that is an in-house library of components or a vendor library like MaterialUI or React-Toolbox) from the 'higher order components,' which depend upon them, you create a cleaner separation between your logic and presentational view layers, allowing you to more easily switch component libraries without needing to modify paths or references within the import statements of your higher order components. This approach remains particularly useful for migrating or extending control suites within existing applications or moving between web-based libs and React Native libs.

On [npm](https://www.npmjs.com/package/core-ui)
On [github](https://github.com/joevbruno/core-ui)

# Usage 

### Step 1: Setup

```
npm install --save core-ui

```

+ Create a registeration file (e.g. `utils/compRegister.js`).
+ Import `registerComponents` from `core-ui`
+ Import all components you wish to register.
+ Create an object from your imported components
+ call registerComponents, passing in your object of components.

```
import { registerComponents } from 'core-ui';
import AppBar from 'react-toolbox/lib/app_bar';
import Autocomplete from 'some/other/place';

....

// little helper from ES6
const appUI = {
  AppBar,
  Autocomplete,
};

// now register the components
registerComponents(appUI);
```


### Step #2: Require the file in your main 'app.js' or 'index.js' file

The file needs to be imported before any register components are needed.

```
import AppComponents from './utils/compRegister';
import React from 'react';
import { render } from 'react-dom';
import { createStore, applyMiddleware } from 'redux';
...

```

### Step #3: Profit

Now we can easily require our components wherever we need them within the application without worring about paths.

```
import React from 'react';
import { AppBar, NavMenu, Hamburger}  from 'core-ui';
...
  render() {
    return (
      <AppBar className={style.appbar}>
        <NavMenu
          isDrawerActive={this.state.isDrawerActive}
          onOverlayClick={this.toggleOffCanvasNav}
          links={links} />
        <Hamburger
          isActive={this.state.isHamburgerActive}
          handleClick={this.toggleOffCanvasNav} />
      </AppBar>
    );
  }
```
### Step #4: Time Changes All Things

Image one of the following situations: 

+ Someone wrote a killer new Table component, and your team intends to use this new Table component throughout our application. 
+ Your project undergoes some file and directory restructoring.

In the latter situation, components using `core-ui` will not be effected because they are not dependent upon relative paths. In the former scenario (introducing a new Component, or replacing an existing one), we simply need to modify our `utils/compRegister.js` file:

```
import Table from 'SOME/NEW/LOCATION';
```

Notice, everything else is the same. We only needed to change one line to start using our new Table component! Also, note that we are simply importing a file. That file can do whatever we need it to do, including any API 'bridging' that may need to take place between the 'old' and 'new' components.

### Step #5: Repeat as Needed.
Sweet!

# TODO 
 + Add example (link to another project using core-ui coming soon)
 + ~~Add tests~~

# Contribute
If you can make this better, please submit a PR.
