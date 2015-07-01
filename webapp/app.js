var express = require('express'),
    compression = require('compression'),
    request = require('request');

var oneDay = 86400000,
    apiUrl = 'http://localhost:8080',
    staticDir = '/public';

var app = express();

app.use(compression());
app.use(express.static(__dirname + staticDir, { maxAge: oneDay }));
app.use('/api', function(req, res) {
  var url = apiUrl + req.url;
  req.pipe(request(url)).pipe(res);
});

app.listen(process.env.PORT || 3000);
