var express = require('express'),
    compression = require('compression'),
    request = require('request');

var cacheDuration = 86400000, // One day
    apiUrl = 'http://localhost:8080',
    staticDir = '/public/',
    indexFile = 'index.html';

var app = express();

// Use Gzip compression
app.use(compression());

// Serve static content and use cache
app.use(express.static(__dirname + staticDir, { maxAge: cacheDuration }));

// Proxy for backend calls
app.use('/api', function(req, res) {
  var url = apiUrl + req.url;
  req.pipe(request(url)).pipe(res);
});

// URL rewriting
app.get('*', function(request, response, next) {
  response.sendfile(__dirname + staticDir + indexFile);
});

// Start server
app.listen(process.env.PORT || 3000);
