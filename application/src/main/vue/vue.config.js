const path = require("path");

module.exports = {
  outputDir: path.resolve(__dirname, "../../../build/resources/main/META-INF/resources"),
  transpileDependencies: ["vuetify"],
  pages: {
    index: {
      entry: 'src/main.ts',
      template: 'public/index.html',
      filename: 'index.html',
      title: 'Tichu Counter',
      chunks: ['chunk-vendors', 'chunk-common', 'index']
    }
  }
};
