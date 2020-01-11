window.React = require('react');
window.ReactDOM = require('react-dom');
// window.Croppr = require('croppr');
// import {render} from 'react-dom';
// var Croppr = require('croppr');

// import Croppr from 'croppr';
var reactElement = React.createElement(
    "h1",
    {
        className: "abc",

        style: {
            textAlign: "center"
        },

        onClick: function () {
            alert("click");
        }
    },
    "Hello, world!"
);

// The second argument is the property object,
// it has to be null if empty
var anotherElement = React.createElement(
    "p",
    null,
    "A nice text paragraph."
);

var renderTarget = document.getElementById("testComp");
window.ReactDOM.render(reactElement, renderTarget);


