const ExtractTextPlugin = require('extract-text-webpack-plugin');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const Webpack = require('webpack');
const packageJSON = require('./package.json');
const path = require('path');

const PATHS = {
  build: path.join(__dirname, 'target', 'classes', 'META-INF', 'resources', 'webjars', packageJSON.name, packageJSON.version)
};

module.exports = {
  entry: [
    'webpack-dev-server/client?http://localhost:8080/',
    'webpack/hot/dev-server',
    './app/index.js'
  ],
  output: {
    path: PATHS.build,
    filename: 'index.js'
  },
  module: {
    loaders: [
      {
        test: /\.js$/,
        exclude: /node_modules/,
        loaders: [
          'babel?sourceMap'
        ]
      },
      {
        test: /\.html$/,
        loader: 'html'
      },
      {
        test: /\.less$/,
        loader: ExtractTextPlugin.extract('css?sourceMap!less?sourceMap')
      },
      {
        test: /\.(jpeg|png)/,
        loader: 'url'
      }
    ]
  },
  devtool: 'source-map',
  devServer: {
    contentBase: './',
    hot: true,
    proxy: {
      "/api": {
        target: "http://localhost:8080/mock",
        pathRewrite: {"^([^.?]+)(.*)$" : "$1$2.json"}
      }
    }
  },
  noInfo: true,
  plugins: [
    new Webpack.HotModuleReplacementPlugin(),
    new HtmlWebpackPlugin({
      inject: 'body',
      template: './app/index.html'
    }),
    new ExtractTextPlugin('index.css')
  ]
};
