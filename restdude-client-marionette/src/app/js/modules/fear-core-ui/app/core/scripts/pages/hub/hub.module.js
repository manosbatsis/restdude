define([
    'core/scripts/pages/hub/controller/hub',
    'core/scripts/pages/hub/service/links',
    'core/scripts/pages/hub/service/storage',
    'core/scripts/pages/hub/directive/cache-checkbox'
], function () {
    'use strict';

    return angular.module('comMarksandspencerApp.pages.hub', [
        'comMarksandspencerApp.pages.hub.controller.HubCtrl',
        'comMarksandspencerApp.pages.hub.service.LinkService',
        'comMarksandspencerApp.pages.hub.service.LocalStorageService',
        'comMarksandspencerApp.pages.hub.directive.CacheCheckboxDirective'
    ]);
});
