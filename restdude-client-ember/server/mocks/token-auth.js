module.exports = function(app){

    var express = require('express');
    var router = express.Router();
    var jwt = require('jsonwebtoken');
    var user = {

        username: 'admin',
        password: '123'

    };

    router.post('/login',function(req, res){ //req=require res=respond

        if(req.body.username === user.username && req.body.password ===user.password){
            var token;

            token = jwt.sign({username: user.username}, 'secretkey');
            res.send({
                token: token

            });

        }else {

            res.status(401).end();
        }

    });

    app.use('/api/token-auth',router);

};