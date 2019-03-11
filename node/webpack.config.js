'use strict';
var path = require('path');
var root = './'
var webpack = require('webpack');
var CommonsChunkPlugin = webpack.optimize.CommonsChunkPlugin;

module.exports = {
    devtool: "source-map",
    entry: {
        index: './bundles/index.js',
    },
    output: {
        path: __dirname + '/generated.js/',
        publicPath: "/assets/",
        filename: '[name]-bundle.js'
    },
    plugins: [
        new webpack.NoErrorsPlugin(),
        new CommonsChunkPlugin({
            name: "index"
        }),
	new webpack.DefinePlugin({
	    'process.env': {
		NODE_ENV: JSON.stringify(process.env.NODE_ENV),
	    },
	})
    ],
    module: {
        rules: [
            { test: /\.js$/,
                enforce: "pre",
                exclude: /node_modules/,
                loader: "source-map-loader" }
        ],
        loaders:
	[
	    {
		    test: /\.js[x]?$/,
		    include: path.resolve(__dirname, './'),
		    exclude: /node_modules/, loader: 'babel-loader'
	    }
	]
    }
};

