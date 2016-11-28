Prerequisites

> npm install -g yo

if you have any warns(unmet peer dependency) try this: npm install -g yo generator-backbone-marionette

Create a new directory and jump in there.

mkdir my-app-name && cd $_

maybe it needs grunt-bower-requirejs to be installed
Run:

yo backbone-marionette

Once you've got the generator, run one of the following grunt commands. The main difference being that develop will include a watch task and won't using almond.js to build a single js file. Make sure you use release for production, as you don't want require making a bunch of http requests.

grunt develop

grunt release

Once you've done this, open up the index.html file that's been created and you should template being rendered by your backbone.marionette app :)