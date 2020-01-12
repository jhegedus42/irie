'use strict';
var path = require('path');
var root = './'
var webpack = require('webpack');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');

module.exports = {
    devtool: "source-map",
    entry: {
        bundle: './index.js',
        test: './test.js'
    },
    output: {
        path: __dirname + '/generated.js/',
        publicPath: "/assets/",
        filename: '[name].js'
    },
    module: {
        rules: [
            {
                test: /\.js$/,
                enforce: "pre",
                exclude: /node_modules/,
                loader: "source-map-loader"

            },
            {
                test: /\.js$/,
                exclude: /node_modules/,
                loader: "babel-loader"

            },
            {
                test: /\.(css)$/,
                use: ['style-loader', 'css-loader']
            },


        ],

    }
};

