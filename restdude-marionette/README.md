Quick Build and Run

Follow the steps bellow to build.

Prerequisites

Before you Start make sure that you have installed Node/Npm. Otherwise download it
from here: Node/Npm - http://nodejs.org/download/

                          
a) In order to install Grunt run:
        
        npm install -g grunt-cli


b) Install Sass:
        gem install sass

In order to install Compass and Sass you must have installed "rubygems". If there are some problems installing
compass Follow the next steps: 1. Download rubygems-update-2.6.7.gem . Please download the file in a directory that you can later point to
 (eg. the root of your harddrive, C:\)                              
                               
                               2.  Now, using your Command Prompt: run C:\>gem install --local C:\rubygems-update-2.6.7
 and after finishing run C:\>update_rubygems --no-ri --no-rdoc

                                3. After this, gem --version should report the new update version.

                                4. Run the update_rubygems command

                                5. Run gem install sass

1. Clone the repository:

 git clone https://github.com/manosbatsis/restdude/tree/master/restdude-marionette

2. Change to the project's root directory.

3. Install project dependencies(node_modules files) by running from the command promt or terminal :
        npm install

   If you want to remove any dependencies just run: npm remove dependencie_tobe_deleted --save

4. Run Grunt to build the project with: grunt  

5. Browse the app client: http://localhost:9000       

 > How to use

 In order to run the Project you have to: 

        1) Run to a terminal : grunt backend

        This command starts the local server

        2) Run to a different terminal the command : grunt frontend
        
        This command starts the frontend server using Grunt 

        3) You can then point as many tabs/browsers at localhost:9000 as you like. 
        Use the form to add items to your todo list, you will see your changes appear on the other tabs in real time. Thanks to Socket IO.

Documentation

A typical setup will involve adding two files to your project: package.json and the Gruntfile.

package.json: This file is used by npm to store metadata for projects published as npm modules.
You will list grunt and the Grunt plugins your project needs as devDependencies in this file.

Gruntfile: This file is named Gruntfile.js or Gruntfile.coffee and is used to configure or define tasks and load Grunt plugins.
When this documentation mentions a Gruntfile it is talking about a file, which is either a Gruntfile.js or a Gruntfile.coffee.

The easiest way to add Grunt and gruntplugins to an existing package.json is with the command npm install <module> --save-dev.
Not only will this install <module> locally, but it will automatically be added to the devDependencies section, using a tilde version range.

A Gruntfile is comprised of the following parts:

>The "wrapper" function
>Project and task configuration
>Loading Grunt plugins and tasks
>Custom tasks

The "wrapper" function

Every Gruntfile (and gruntplugin) uses this basic format, and all of your Grunt code must be specified inside this function:

module.exports = function(grunt) {
  // Do grunt-related things in here
};

Task configuration: 
                1. Concat: this task is configured in order the files to be Concatenated. 

                2. Less: this task is configured in order to compile LESS files of Core-UI to CSS. Run this task with the grunt less command.
                Options: paths: It is the directory of input file. If you specify a function the source filepath will be the first argument. You can return either a string or 
                an array of paths to be used. 
                Options: files: first part is the path to the "result.css" and the second part is the path to "source.less".

                3. Imagemin: this task is configured in order minify the images of the project(PNG, JPEG, GIF and SVG images). We need the src(source) and the dest(destination)
                of the images that we want to minify.

                4.Clean: this task is configured in order the files and the folders to get cleaned. Run this task with the grunt clean command.

                5. Cssmin: this task is configured in order minify the css files of the project.
                 my_target: files: here we specify the src, dest and the extension that the new file will have (here '.min.css').

                6. Uglify: this task is configured in order minify JavaScript files. Run this task with the grunt uglify command.
                Options: compression: Turn on or off source compression with default options.

                7. Watch: this task is configured in order to run predefined tasks whenever watched file patterns are added, changed or deleted.
                Run this task with the grunt watch command. At this task we include everything that we want to watch for changes like sass and css files.

                8. Requirejs: this task is configured in order to optimize RequireJS projects using r.js. Run this task with the grunt requirejs command.
                It builds the default javascript Core-UI library using r.js compiler.

                9. Sass: this task is configured in order to Compile Sass to CSS. Run this task with the grunt sass command. 
                Sass is a preprocessor that adds nested rules, variables, mixins and functions, selector inheritance, and more to CSS.
                Sass files compile into well-formatted, standard CSS to use in your site or application.This task requires you to have Ruby and Sass installed.
                Main: options: outputStyle: the output style can be nested, compact, compressed, expanded.

                > Sass Customization : in order to customize the sass files of the core-ui you need to copy the .scss stylesheets to your sass directory
                and remove the components tha you do not need. Therefore, you make the changes you want and then you compile the Sass file to Css  using grunt-contrib-sass.
                Also we are using the grunt-contrib-watch plugin during development to automatically run the compilation whenever a SCSS file changes.

                10. Core-UI configuration: we are using the CoreUI-Free-Bootstrap-Admin-Template/AJAX_Full_Project_GULP. In order to use it we updated the package.json,
                with all the dependencies core-ui needs. Also we added to the preprocessor file the views, scss and js file of the core-ui project. Moreover, at the www file 
                we added the index.html file, the libraries for the core-ui (lib & the coreui-app.js) , the images(img), the css files, the fonts and the javascript files. In order
                to run we need to configure gulp task at the Gruntfile.js.   