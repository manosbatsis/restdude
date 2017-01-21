'use strict';

var config = require('fear-core').utils.config();
var path = require('path');

/**
 * toProcessHelper
 * @type {{getPaths: Function, getAssetPath: Function, shouldAddProduct: Function}}
 */
var toProcessHelper = {

    /**
     * getPaths
     * @param product
     * @param source eg .tmp
     * @param globPrefix
     * @returns {Array}
     */
    getPaths : function (product, source, globPrefix) {

        var toProcess = [];
        var teams = config.get('paths.teams');

        globPrefix = globPrefix || '';

        for (var team in teams) {
            if (teams.hasOwnProperty(team) && this.shouldAddProduct(product, team) && source) {
                toProcess.push(
                    path.join(
                        this.getAssetPath(team, source.type, source.base),
                        globPrefix,
                        config.get('paths.glob.' + source.type)
                    )
                );
            }
        }

        return toProcess;
    },

    /**
     * getAssetPath
     * @param product
     * @param type
     * @param folder
     * @returns {string}
     */
    getAssetPath : function (product, type, folder) {
        return config.get('paths.teams.' + product + '.' + type, {base : folder});
    },

    /**
     * shouldAddProduct
     * @param product
     * @param configProduct
     * @returns {boolean}
     */
    shouldAddProduct : function (product, configProduct) {
        return product === 'all' || product === configProduct;
    }
};

module.exports = toProcessHelper;