require([
// Libraries
        'jquery', 'underscore', 'underscore-inflection', 'backbone', 'bootstrap',
// Main App Object
        'chai',
        '../jstestapp/src/domain.js',
        '../jstestapp/src/models/user.js',
        '../jstest/models/user.test.js',
    ],

    function (// Libraries
              $, _, _inflection, Backbone, bootstrap, chai, app, User, userTest) {


// Use the expect version of chai assertions - http://chaijs.com/api/bdd
        var expect = chai.expect;

        // Tell mocha we want TDD syntax
        mocha.setup('tdd');


    });
