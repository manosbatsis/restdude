global.sinon = require('sinon');
global.chai = require('chai');

global.chai.use(require('sinon-chai'));
global.expect = global.chai.expect;
global.chai.should();