/*jshint unused: vars, white:false */

define([
    'angular'
], function (angular) {
    'use strict';

    return angular.module('hubApp', [])
        .config(['$interpolateProvider', function ($interpolateProvider) {
            // override angular double curly braces syntax
            $interpolateProvider.startSymbol('{[{').endSymbol('}]}');
        }]);
});
