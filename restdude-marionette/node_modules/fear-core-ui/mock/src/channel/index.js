'use strict';

var core = require('fear-core');
var config = core.utils.config({root : 'mock/src/channel'});

var cached = {};

function Defaults () {

    Defaults.channel = {
        DEFAULT: 'default',
        TSOP: 'tsop',
        BUSER: 'buser',
        MOBILEAPP: 'mobileapp'
    };

    function getValues(channel, type) {

        var key = channel + '-' + type;

        var value = cached[key];

        if (value === undefined) {
            cached[key] = config.get(type, channel);
        }

        return cached[key];
    }

    return {
        CHANNEL: Defaults.channel,
        get: function (channel, type) {
            return getValues(channel, type);
        }
    };
}

module.exports = new Defaults();
