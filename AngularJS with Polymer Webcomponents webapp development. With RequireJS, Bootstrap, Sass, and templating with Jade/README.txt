*** missing from file bower_components and node_modules ---> will be installed from the commands below***

Requeriments

NodeJS

For update npm

sudo npm install npm -g
Bower

npm install -g bower
Sass

sudo gem install sass

----------------------------

> npm install -g yo
npm install -g generator-base-angulymerjs

//or
sudo npm install -g generator-base-angulymerjs

> Finally, initiate the generator:

yo base-angulymerjs

----------------------------------
If you have error on install try to update dependences manually:

sudo npm update
bower update

-----------------------------------

Usage

Develop code on folder /src

/src
    /data
    /images
    /modules
    /scripts
        /controllers
        /directives
    /styles
    /templates
-------------------------------
Compile code

Use grunt task to compile code and deploy webapp

grunt serve
THis will launch server on port 9000

http://localhost:9000/
Distribute code is compileded on forder /dist

/dist
    /css
    /data
    /images
    /js
    /lib
    /modules
    /templates
----------------------------------
Routing

Put the routes for your app into file /script/router.js.

define(['controllers', 'directives'], function() {
    'use strict';
    window.app.config(['$routeProvider',
        function($routeProvider) {
            $routeProvider.
            when('/', {
                templateUrl: 'templates/index.html',
                controller: 'indexController'
            }).
            otherwise({
                redirectTo: '/'
            });
        }
    ]);
});
---------------------------------------------------------------------
Styling

Sass files (.sass, *.scss) must be located on */src/styles** folder root.

Grunt task sass.js will process the files into CSS files to folder /src/styles/css.
Grunt task copy.js will copy all CSS files into /src/styles/css to folder /dist/css for ditribution.
You can also create and edit CSS files in /src/styles/css.


 