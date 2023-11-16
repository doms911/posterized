const { createProxyMiddleware } = require("http-proxy-middleware");

module.exports = function (app) {
  app.use(
    "/api",
    createProxyMiddleware({
      target: 'http://posterized.onrender.com:8080/',
      changeOrigin: true,
    })
  );
};
