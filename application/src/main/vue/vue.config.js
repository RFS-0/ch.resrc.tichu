const path = require("path");

module.exports = {
  outputDir: path.resolve(
    __dirname,
    "../../../build/resources/main/META-INF/resources"
  ),
  transpileDependencies: ["vuetify"],
  pages: {
    index: {
      entry: 'src/main.ts',
      template: 'public/index.html',
      filename: 'index.html',
      title: 'Tichu Counter',
      chunks: ['chunk-vendors', 'chunk-common', 'index']
    }
  },
  devServer: {
    open: process.platform === 'darwin',
    host: '0.0.0.0',
    port: 4200,
    https: false,
    hotOnly: false,
    proxy: {
      '^/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false,
        pathRewrite: {'^/api': '/api'},
        logLevel: 'debug'
      },
    }
  },
  configureWebpack: {
    devtool: 'source-map'
  }
};
