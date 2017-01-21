System.config({
    baseURL: '/',
    defaultJSExtensions: true,
    transpiler: 'plugin-babel',
    babelOptions: {
        'optional': [
            'runtime'
        ]
    },
    meta: {
        'angular': {
            'format': 'global',
            'exports': 'angular'
        }
    },
    map: {
        'angular': 'node_modules/angular/angular',
        'systemjs': 'node_modules/systemjs/dist/system.js',
        'system-polyfills': 'node_modules/systemjs/dist/system-polyfills.js',
        'systemjs-babel-build': 'node_modules/systemjs-plugin-babel/systemjs-babel-browser',
        'plugin-babel': 'node_modules/systemjs-plugin-babel/plugin-babel.js',
        'core-js': 'node_modules/babel-runtime/core-js',
        'core-js/library': 'node_modules/babel-runtime/node_modules/core-js/library',
        'classList-polyfill': 'node_modules/classlist-polyfill/src/index',
        'es6-promise': 'node_modules/es6-promise/dist/es6-promise',
        'xhttp': 'node_modules/xhttp/dist/xhttp',
        'text': 'node_modules/system-text/text'
    }
});
