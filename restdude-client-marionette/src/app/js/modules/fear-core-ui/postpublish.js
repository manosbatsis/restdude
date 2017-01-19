'use strict';

var spawn = require('child_process').spawn;
var ghpages = require('gh-pages');
var path = require('path');

function getLatestCommitHash() {
    var git = spawn('git', ['rev-parse', 'HEAD']);

    return new Promise(function(resolve, reject) {
        var hash = '';

        git.stdout.on('data', function (data) {
            hash = data.toString();
        });

        git.on('exit', function(exitCode){
            if(exitCode === 0){
                resolve(hash);
            }else {
                reject('Problem getting hash from latest commit');
            }
        });
    });
}

function publishGhPages(src, options) {
    options = options || {};
    return new Promise(function(resolve, reject) {
        ghpages.publish(src, options, function(err) {
            err ? reject(err) : resolve(arguments);
        });
    });
}

function jekyllBuild() {
    var jekyll = spawn('jekyll', ['build']);
    return new Promise(function(resolve, reject) {
        jekyll.on('exit', function(exitCode) {
            if(exitCode === 0){
                resolve();
            }else {
                reject('Problem generating js docs');
            }
        });
    });
}

function buildJsDocs() {
    var npm = spawn('npm', ['run', 'docs']);

    return new Promise(function(resolve, reject){
        npm.on('exit', function(exitCode) {
            if(exitCode === 0){
                resolve();
            }else {
                reject('Problem generating js docs');
            }
        });
    });
}

var src = path.join(__dirname, '_gh_pages');
var log = console.log.bind(console);

jekyllBuild()
    .then(function() {
        return buildJsDocs();
    })
    .then(function() {
        return getLatestCommitHash();
    })
    .then(function(hash){
        log('Updating gh-pages to: ' + hash);
        return publishGhPages(src, { message: 'Updating to: ' + hash });
    })
    .then(function(){
        log('All done, gh-pages up to date with master!');
    })
    .catch(function() {
        log('Error: there was a problem in updating the gh-pages branch');
    });
