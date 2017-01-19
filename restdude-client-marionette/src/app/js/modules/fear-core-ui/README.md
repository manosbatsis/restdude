
Core Sass, fonts and images for Marks and Spencer

## Consuming the Library

The library exposes the following paths:

* sassPaths
* assetPaths
* assetImagePaths
* assetFontPaths

Install the module:

```
npm install fear-core-ui --save
```

Add the following to the gulp sass compile file.

```javascript
var fearCoreUI = require('fear-core-ui');

.pipe(sass({
        includePaths: fearCoreUI.sassPaths
    })
);
```

The following variables need to be set

```sass
$fear-core-ui-font-dir: '/assets/fonts';
$fear-core-ui-images-dir: '/assets/images';
```

Copy fear-core-ui assets to your working directory

```javascript
var fearCoreUI = require('fear-core-ui');

gulp.task('copy-fear-core-ui-assets', function() {
    return gulp.src([fearCoreUI.assetPaths + '/**/*.*'])
        .pipe(gulp.dest('/assets'));
});
```

*Note that the variables set in step 3 should correspond to the location of the assets copied in step 4.*

You can now reference any sass file.

### example:
```sass
@import 'fear-core-ui/base';
@import 'fear-core-ui/typography';
```

## Basic Structure

`fear-core-ui/base` contains all the variables, functions and mixins in the library. 
You can import this multiple times in your SASS code.

`fear-core-ui/extends` contains all the extends rules. 
This should be imported once and ONLY once per generated CSS file. If you import it more than once per generated CSS file you will have duplicate CSS rules.

## SASS / CSS coding standards

We want to make sure that unnecessary CSS is not imported to consuming projects.

## Aggregates
The following can be exposed through aggregate files:

* mixins
* variables
* functions

**Example:** 
`@import utlities.scss;`

```sass
@import 'utilities/functions';
@import 'utilities/mixins';
@import 'utilities/variables';
```
  
This can be done because the sass code in these files do not add CSS unless mixins / extends are explicitly called from the consuming code.

## Explicit imports

When a sass file contains direct CSS it needs to consumed through an explicit import. 

i.e. If I only wanted to use the `buttons.sass` component. Importing an aggregate `components.sass` 
which included all the components CSS. That would create bloat and unused CSS being included in the consuming project.
 
**Example:**
The buttons components contains direct CSS and must be imported through a direct import and not an aggregate sass file.

`@import ui-pattern/buttons;`

```sass
.btn--primary {
  @include create-btn(40px, 15px, $color__brand--green, $color__brand--dark-grey, $color__brand--background-grey, $color__brand--light-grey);
}

.btn--secondary {
  @include create-btn(40px, 15px, $color__brand--grey-40, $color__brand--dark-grey, $color__brand--background-grey, $color__brand--light-grey);
}
```

### Directory structure

```
assets
    |- fonts
    |- images
sass
```

### Directory pattern
```
item.scss
- item // directory
    |- _variables.scss
    |- _extends.scss
    |- _functions.scss
    |- _mixins.scss
    |- _module_itemA.scss
    |- _module_itemB.scss
```

#### item.scss
```sass
@import 'item/variables';
@import 'item/extends';
@import 'item/functions';
@import 'item/mixins';
@import 'item/module_itemA';
@import 'item/module_itemB';
```

### Directory categories
```
|- layouts
    |- _grid.scss
    |- grid
        |- _variables.scss        
        |- _mixins.scss        
        |- _module_grid.scss        
    |- _zindex.scss
|- normalize
    |- _module_mns-normalize.scss
    |- _module_normalize.scss
|- typography
|- ui-pattern
    |- _buttons.scss
    |- buttons
        |- _mixins.scss
        |- _module_icons.scss
        |- _module_buttons.scss
|- utilites
    |- _mixins_measurements.scss
_colors.scss
_sprites.scss
_normalize.scss
```

### Add an icon to the mns-icon fonts
The mns-icons fonts are located in the `lib/assets/fonts` directory.  The font directory contains the generated mns-icon fonts:
- mns-icons.eot
- mns-icons.svg
- mns-icons.ttf
- mns-icons.woff

In the `mns-icons` folder you will find the settings file (`selection.json`) used by icomoon and a demo project to serve up and test the fonts:

The following steps will guide you through the process of adding icons to the mns-icon font

1. Browse to https://icomoon.io/app/
2. Click on Import Icons
3. Browse to the fear-core-ui project folder and select the selection.json file (`lib/assets/fonts/mns-icons/selection.json`)
4. If a message dialog pops up select to import all settings from the file
5. Select the required fonts from the UI
6. When you are done, at the bottom click `Generate Font`
7. Check if the old icons unicode signatures match with the original font (you can achieve this by serving up the `demo.html` file in the demo-files folder.
8. Click download and save the zip file on your local hard drive
9. Unzip this folder and copy the generated font files to `lib/assets/fonts` and the demo files to the `mns-icons` folder
10. Add the icons unicode sign to the following file: `lib/sass/fear-core-ui/ui-pattern/_extends_content.scss` and give it a proper sass variable name
11. Generate the css class in the `lib/sass/fear-core-ui/ui-pattern/_module_icons.scss` file:
```scss
@include('css-class-name', 'icon-variable-name')
```

## **Linting**
```npm test``` runs eslint ```eslint tasks``` and sass lint ```gulp lint-sass```

###**Further reading**

* [Website](http://digitalinnovation.github.io/fear-core)
* [Technical documentation](http://digitalinnovation.github.io/fear-core/docs/)
* [Pattern library](http://patternlibrary.auto.devops.mnscorp.net/#/Core)
* [Wiki](https://github.com/DigitalInnovation/fear-core/wiki)
 
