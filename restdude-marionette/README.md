Follow the steps bellow to run the generator:

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

e) Now run: npm install -g grunt-cli bower yo generator-react-require

f) Make a new directory, and cd into it:
    
        mkdir my-react-proj && cd $_

g) Run the command:  yo react-require

 > How to use

Start the Node Express server using Grunt - This is used for persistent storage of your todo list entries.

Run : grunt backend

Start the frontend server using Grunt - This is the Javascript application. Your terminal will tell you to point your browser at 0.0.0.0:9000 so see the application.

Run : grunt frontend

You can then point as many tabs/browsers at 0.0.0.0:9000 as you like. Use the form to add items to your todo list, you will see your changes appear on the other tabs in real time. Thanks to Socket IO.