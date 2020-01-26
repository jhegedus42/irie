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

window.ReactCrop = ReactCrop

const c = {
    crop: {
        unit: 'px', // default, can be 'px' or '%'
        x: 130,
        y: 50,
        width: 200,
        height: 200
    }
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

        <ReactCrop
            src={"6a7e6ec8-daf8-4773-b977-76d6e27e5591.jpeg"}
            onChange={newCrop => console.log(newCrop)}
        />
    </div>
);


class Clock extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            date: new Date(),
            crop: {
                unit: 'px', // default, can be 'px' or '%'
                x: 130,
                y: 50,
                width: 200,
                height: 200
            }
        };
    }

    render() {
        return (
            <div>
                <h1>Hello, world!</h1>
                <h2>It is {this.state.date.toLocaleTimeString()}.</h2>
                <ReactCrop
                    src={"6a7e6ec8-daf8-4773-b977-76d6e27e5591.jpeg"}
                    onChange={crop => {
                        this.setState({crop})
                    }}
                    crop={this.state.crop}
                />
            </div>
        );
    }
}

const list =
    React.createElement('div', {},
        React.createElement('h1', {}, 'My favorite ice cream flavors'),
        React.createElement('ul', {},
            [
                React.createElement('li', {}, 'Chocolate'),
                React.createElement('li', {}, 'Vanilla'),
                React.createElement('li', {}, 'Banana')
            ]
        ),
        // React.createElement(ReactCrop,{
        //    src:"6a7e6ec8-daf8-4773-b977-76d6e27e5591.jpeg",
        //    onChange:handler
        // }),
        React.createElement(Clock, {date: new Date()})
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

// var renderTarget = document.getElementById("testComp");

// window.ReactDOM.render(list, renderTarget);


