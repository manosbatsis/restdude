module.exports = function(config) {
    config.set({
        basePath: "./",

        // Which files to watch and include in the browser
        // use RequireJS to load dependencies
        files: [
            { pattern: "tests/samples/*.spec.js", included: false },
            "tests/main.js"
        ],

        // A list of files to ignore
        exclude: [],

        // A list of frameworks required by Karma
        frameworks: ["jasmine", "requirejs"],

        // Which reporter to use e.g. progress, junit, coverage
        reporters: ["progress", 'junit'],

        // Using the JUnit reporter, store test results in the build folder
        junitReporter: {
            outputFile: "build/reports/test-results.xml"
        },
        
        // web server port
        port: 9876,

        // Use colours in the output
        colors: true,

        // Set log level to info - Use debug for information overload
        logLevel: config.LOG_INFO,

        // We don't want tests running on every save
        autoWatch: false,

        // Run tests in PhantomJS and Chrome
        browsers: ["PhantomJS", "Chrome"],

        // Disconnect if the browser cannot boot and connect to Karma in x ms
        captureTimeout: 20000,

        // Run all tests in a single Karma isntance
        singleRun: true,

        // A collection of plugin Karma requires
        plugins: [
            "karma-jasmine",
            "karma-phantomjs-launcher",
            "karma-chrome-launcher",
            "karma-junit-reporter",
            "karma-requirejs"
        ]
    });
};
