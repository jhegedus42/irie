
// rebeam: Improves click/tap performance, see
//   https://github.com/callemall/material-ui#react-tap-event-plugin
//   http://stackoverflow.com/a/34015469/988941
// var injectTapEventPlugin = require('react-tap-event-plugin');
// injectTapEventPlugin();

// rebeam: Original index.js bundle did not include material, but for now
// it's easier to just require immediately rather than loading async. However
// we could load async if we start up a _learning.spray_examples.rest.simple UI without this.
window.mui          = require("material-ui");
window.mui.Styles   = require("material-ui/styles");
window.mui.SvgIcons = require('material-ui/svg-icons/index');

window.vkbeautify          = require("vkbeautify");

window.objectUnfreeze          = require("object-unfreeze");

// rebeam: react-sortable-hoc
window.Sortable = require('react-sortable-hoc');
window.SortableContainer = window.Sortable.SortableContainer;
window.SortableElement = window.Sortable.SortableElement;
window.SortableHandle = window.Sortable.SortableHandle;

window.React = require('react');
window.ReactDOM = require('react-dom');

window.InlineEdit= require('react-edit-inline');

// import InlineEdit from 'react-edit-inline';

import {render} from 'react-dom';

// import {InLineDemoComponent} from './ReactInlineEditDemo.js';


{/*render(<InLineDemoComponent></InLineDemoComponent> , document.getElementById("jsReactComp"));*/}

// import {ReactCrop} from 'react-image-crop';
window.ReactCrop = require('react-image-crop');

//render(<ReactCrop src="ct.jpg" /> , document.getElementById("jsReactCrop"));

