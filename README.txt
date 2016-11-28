*** missing from file bower_components and node_modules ---> will be installed from the commands below***
Prerequisites

> npm install -g yo
>Node/Npm - http://nodejs.org/download/
>Grunt installed globally (npm install -g grunt-cli) - http://gruntjs.com/getting-started (Grunt is also installed as a dev dependency if you'd rather use that)
>Compass (gem install compass) - http://compass-style.org/install/
>Sass (gem install sass) - http://sass-lang.com/install


if you have any warns try this: npm install -g grunt-cli bower yo generator-react-require

1. npm install -g generator-react-require
2. Make a new directory, and cd into it:

mkdir my-react-proj && cd $_

yo react-require
How to use

Start the Node Express server using Grunt - This is used for persistent storage of your todo list entries.

grunt backend
Start the frontend server using Grunt - This is the Javascript application. Your terminal will tell you to point your browser at 0.0.0.0:9000 so see the application.

grunt frontend
You can then point as many tabs/browsers at 0.0.0.0:9000 as you like. Use the form to add items to your todo list, you will see your changes appear on the other tabs in real time. Thanks to Socket IO.