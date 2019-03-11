module.exports = function (config) {
    config.set({
        preprocessors: {
            '**/*.js': ['sourcemap']
        },
        plugins: ['karma-chrome-launcher', 'karma-scalajs-scalatest','karma-sourcemap-loader'],
        reporters: ['progress'],
        frameworks: ['scalajs-scalatest'],
        files: [
            'js/target/scala-2.11/im-test-jsdeps.js',
            'js/target/scala-2.11/im-test-fastopt.js'//,
            // 'js/target/scala-2.11/im-test-fastopt.js.map'
        ],

        // browsers: process.env.TRAVIS ? ['Firefox'] : ['Chrome'],
        //browsers: process.env.TRAVIS ? ['Firefox'] : ['Safari'],

        autoWatch: false,

        client: {
            tests: [
                // "app.client.rest.ClientUpdateEntityDynTest",
                // "app.client.rest.ClientGetEntityDynTest",
                // "app.client.rest.ClientGetEntityTest",
                // "app.client.rest.ClientGetAllEntityTest",
                // "app.client.rest.ResetDBStateTest",
                "app.client.rest_take3.ClientGetEntityTest"
            ]
        }
    });
};
