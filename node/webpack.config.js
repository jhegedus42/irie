'use strict';
var path = require('path');
var root = './'
var webpack = require('webpack');

module.exports = {
    devtool: "source-map",
    entry: {
        index: './index.js',
    },
    output: {
        path: __dirname + '/generated.js/',
        publicPath: "/assets/",
        filename: 'bundle.js'
    },
    module: {
        rules: [
            { test: /\.js$/,
                enforce: "pre",
                exclude: /node_modules/,
                loader: "source-map-loader" }
        ]
    }
};

