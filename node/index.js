window.React = require('react');
window.ReactDOM = require('react-dom');
// window.Croppr = require('croppr');
// import {render} from 'react-dom';
// var Croppr = require('croppr');

import React from 'react'

import {Tab, Tabs, TabList, TabPanel} from 'react-tabs';

import sum from './otherFile.js';

console.log(sum(2, 5));

import 'react-tabs/style/react-tabs.css';
import TagsInput from 'react-tagsinput'
import 'react-tagsinput/react-tagsinput.css' // If using WebPack and style-loader.

window.TagsInput = TagsInput

const re = (
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

const f = () => {
    console.log('hello')

}

import ReactCrop from 'react-image-crop';

import 'react-image-crop/dist/ReactCrop.css';

const c = {
    crop: {
        unit: 'px', // default, can be 'px' or '%'
        x: 130,
        y: 50,
        width: 200,
        height: 200
    }
}

class Bork {
    static a = 'foo';
    static b;

    x = 'bar';
    y;
} // todo make this work

class Demo extends React.PureComponent{


    // state = {
    //     src: null,
    //     crop: {
    //         unit: '%',
    //         width: 30,
    //         aspect: 16 / 9,
    //     },
    // };
    // onCropChange = (crop, percentCrop) => {
    //     // You could also use percentCrop:
    //     // this.setState({ crop: percentCrop });
    //     this.setState({ crop });
    // };
}

var reactElement2 = (
    <div>
        <h1
            className="abc"
            style={{textAlign: "center"}}
            onClick={function () {
                alert("click");
            }}
        >
            Hello, world 42!
        </h1>

        <ReactCrop src={"6a7e6ec8-daf8-4773-b977-76d6e27e5591.jpeg"} onChange={newCrop => console.log(newCrop)}/>
    </div>
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

window.ReactDOM.render(reactElement2, renderTarget);


