define([
    'angular',
    'common/scripts/config'
], function (angular) {
    'use strict';

    function HubController(LinkService) {
        var vm = this;
        vm.documentation = LinkService.get('documentation');
        vm.applications = LinkService.get('applications');
    }

    angular
        .module(
            'comMarksandspencerApp.pages.hub.controller.HubCtrl',
            ['comMarksandspencerApp.pages.hub.service.LinkService']
        )
        .controller('HubCtrl', ['LinkService', HubController]);

    return HubController;
});
