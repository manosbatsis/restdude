/**
 * @jsx React.DOM
 */

define(["React"], function(React) {

    var ERROR_DOM = document.getElementById("error-area");

    var ERROR_MSG = "It doesn't look like you have started the node server :(";

    /** React - Error box */
    var ErrorMsg = React.createClass({displayName: 'ErrorMsg',
        render: function() {
            return ( 
                React.DOM.div({id: "error"}, this.props.error)
            );
        }
    });

    return function(socket) {

        socket.on("connect_error", function() {
            React.renderComponent(ErrorMsg({error: ERROR_MSG}), ERROR_DOM);
        });

        socket.on("connect", function() {
            React.unmountComponentAtNode(ERROR_DOM); 
        });

    }
});
