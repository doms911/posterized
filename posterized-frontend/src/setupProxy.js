const { createProxyMiddleware } = require("http-proxy-middleware");

module.exports = function (app) {
  app.use(
    "/api",
    createProxyMiddleware({
      target: 'https://posterized-be.onrender.com',
      changeOrigin: true,
    })
  );
};
