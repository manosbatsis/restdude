Quick Build and Run

Follow the steps bellow to build.

Prerequisites

Before you Start make sure that you have installed Node/Npm. Otherwise download it
from here: Node/Npm - http://nodejs.org/download/

a) Open a cmd promt or a terminal and run: 
        
        npm install -g yo
                          
b) In order to install Grunt run:
        
        npm install -g grunt-cli

c) Install Compass:
        gem install compass

d) Install Sass:
        gem install sass

In order to install Compass and Sass you must have installed "rubygems". If there are some problems installing
compass Follow the next steps: 1. Download rubygems-update-2.6.7.gem . Please download the file in a directory that you can later point to
 (eg. the root of your harddrive, C:\)                              
                               
                               2.  Now, using your Command Prompt: run C:\>gem install --local C:\rubygems-update-2.6.7
 and after finishing run C:\>update_rubygems --no-ri --no-rdoc

                                3. After this, gem --version should report the new update version.

                                4. Run the update_rubygems command

                                5. Run gem install compass

1. Clone the repository:

 git clone https://github.com/manosbatsis/restdude/tree/master/restdude-marionette

2. Change to the project's root directory.

3. Install project dependencies by running from the command promt or terminal :
        npm install

4. Run Grunt with: grunt  

5. Browse the app client: http://localhost:9000       

 > How to use

Start the Node Express server using Grunt - This is used for persistent storage of your todo list entries.

Run : grunt backend

Start the frontend server using Grunt - This is the Javascript application. Your terminal will tell you to point your browser at 0.0.0.0:9000 so see the application.

Run : grunt frontend

You can then point as many tabs/browsers at 0.0.0.0:9000 as you like. Use the form to add items to your todo list, you will see your changes appear on the other tabs in real time. Thanks to Socket IO.

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