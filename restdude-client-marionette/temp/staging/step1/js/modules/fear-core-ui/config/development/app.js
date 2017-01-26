'use strict';

exports.config = {
    defaultPage: 'hub',
    pages: {
        'applications': require('./pages/application')(),
        'documentation': require('./pages/core')()
    }
};

