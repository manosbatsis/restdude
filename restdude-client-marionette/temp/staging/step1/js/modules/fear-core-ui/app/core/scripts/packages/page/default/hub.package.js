define([
    'core/scripts/app',
    'core/scripts/pages/hub/hub.module'
], function(app) {
    'use strict';

    app.requires.push('comMarksandspencerApp.pages.hub');
    return app;
});

