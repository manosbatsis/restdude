define([
    'angular'
], function (angular) {
    'use strict';

    function CacheCheckboxDirective(localStorage) {
        var storageKey = 'comMarksAndSpencer.hub.appAccordion';

        function getHubAccordionSettings() {
            return localStorage.get(storageKey) || {};
        }

        return {
            restrict: 'A',
            scope: { 'mnsCacheCheckbox': '=' },
            link: function (scope, element) {
                var input = element[0];
                var app = scope.mnsCacheCheckbox;

                if(getHubAccordionSettings(storageKey)[app]) {
                    input.checked = true;
                }

                element.on('click', function() {
                    var checked = !!input.checked;
                    var store = getHubAccordionSettings(storageKey);

                    if(checked) {
                        store[app] = true;
                    } else {
                        delete store[app];
                    }

                    localStorage.set(storageKey, store);
                });

            }
        };
    }

    angular
        .module('comMarksandspencerApp.pages.hub.directive.CacheCheckboxDirective', [])
        .directive('mnsCacheCheckbox', ['LocalStorageService', CacheCheckboxDirective]);

    return CacheCheckboxDirective;
});
