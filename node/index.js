window.React = require('react');
window.ReactDOM = require('react-dom');
// window.Croppr = require('croppr');
// import {render} from 'react-dom';
// var Croppr = require('croppr');

import React from 'react'

import { Tab, Tabs, TabList, TabPanel } from 'react-tabs';

import sum from './otherFile.js';

console.log(sum(2, 5));

// import 'react-tabs/style/react-tabs.css';


const re= (
    <Tabs>
        <TabList>
            <Tab>Title 1</Tab>
            <Tab>Title 2</Tab>
        </TabList>

        <TabPanel>
            <h2>Any content 1</h2>
        </TabPanel>
        <TabPanel>
            <h2>Any content 2</h2>
        </TabPanel>
    </Tabs>
);

const f= () => {
    console.log('hello')

}

var reactElement2 = (
    <h1
        className="abc"
        style={{ textAlign: "center" }}
        onClick={function() {
            alert("click");
        }}
    >
        Hello, world 42!
    </h1>
);

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

window.ReactDOM.render(re, renderTarget);


