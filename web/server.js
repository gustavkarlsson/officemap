var port = 9090;

var connect = require("connect");
var http = require("http");
var proxy = require("proxy-middleware");
var modrewrite = require("connect-modrewrite");
var serveStatic = require("serve-static");
var livereload = require("livereload");

var app = connect();

// Proxy API requests
app.use("/api", proxy("http://localhost:8080/api"));

// Rewrite non-asset urls
app.use(modrewrite([
  "^/(?!((bower_components/.*)|(images/.*)|(scripts/.*)|(styles/.*)|(index.html$)|(404.html$)|(favicon.ico$)|(robots.txt$)|(.htaccess$))).*$ /index.html"
]));

// Serve static content
app.use(serveStatic(__dirname + "/.tmp"));
app.use(serveStatic(__dirname + "/app"));
app.use("/bower_components", serveStatic(__dirname + "/bower_components"));

// Create HTTP server
http.createServer(app).listen(port);

// Create livereload server
var liveReload = livereload.createServer();
liveReload.watch([__dirname + "/app", __dirname + "/.tmp"]);
console.log("Server running on port: " + port);
