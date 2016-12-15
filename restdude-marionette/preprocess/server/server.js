// Dependencies
var jf         = require('jsonfile');
var bodyParser = require('body-parser');
var app        = require("express")();
var http       = require("http").Server(app);
var io         = require('socket.io')(http);

// Config
var filePath = "server/json/todo.json";

// Allow x-www-form-urlencoded requests
app.use(bodyParser.urlencoded({ extended: true }));

// Allow cross origin resource sharing (CORS)
app.all('/*', function(req, res, next) {
    res.header("Access-Control-Allow-Origin", "*");
    res.header("Access-Control-Allow-Headers", "X-Requested-With");
    next();
});

io.on('connection', function(socket) {

    // add new item to the todo list
    socket.on("add", function(msg) {
        jf.readFile(filePath, function(err, objs) {

            objs = (objs === null) ? [] : objs;
            objs.push({ "description": msg });
            jf.writeFile(filePath, objs);

            io.sockets.emit("new-todo-list", objs);

        });
    });

    // On the initial connection, return the todo list
    jf.readFile(filePath, function(err, objs) {
        socket.emit("new-todo-list", objs);
    });

});

http.listen(1337);
