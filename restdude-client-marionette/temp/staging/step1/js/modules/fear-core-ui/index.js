var path = require('path');
var bourbon = require('node-bourbon');
var fearCoreUiEntryPoint;

try {
    fearCoreUiEntryPoint = path.dirname(require.resolve('fear-core-ui'));
} catch (e) {
    fearCoreUiEntryPoint = process.cwd();
}

var sassDir = path.join(fearCoreUiEntryPoint, '/lib/sass');
var coreUIDir = path.join(fearCoreUiEntryPoint, '/lib');
var assetsDir = path.join(fearCoreUiEntryPoint, '/lib/assets');
var assetImageDir = path.join(fearCoreUiEntryPoint, '/lib/assets/images');
var assetFontDir = path.join(fearCoreUiEntryPoint, '/lib/assets/fonts');

module.exports = {
    sassPaths: [sassDir, coreUIDir, bourbon.includePaths],
    assetPaths: assetsDir,
    assetImagePaths: assetImageDir,
    assetFontPaths: assetFontDir
};

