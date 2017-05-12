/*jshint node:true*/
module.exports = function(app) {
  var express = require('express');
  var loginRouter = express.Router();

  loginRouter.get('/', function(req, res) {
    res.send({
      'login': []
    });
  });

  loginRouter.post('/', function(req, res) {
    res.status(201).end();
  });

  loginRouter.get('/:id', function(req, res) {
    res.send({
      'login': {
        id: req.params.id
      }
    });
  });

  loginRouter.put('/:id', function(req, res) {
    res.send({
      'login': {
        id: req.params.id
      }
    });
  });

  loginRouter.delete('/:id', function(req, res) {
    res.status(204).end();
  });

  // The POST and PUT call will not contain a request body
  // because the body-parser is not included by default.
  // To use req.body, run:

  //    npm install --save-dev body-parser

  // After installing, you need to `use` the body-parser for
  // this mock uncommenting the following line:
  //
  //app.use('/api/login', require('body-parser').json());
  app.use('/api/login', loginRouter);
};
