var expect = require('chai').expect;
var coreUI = require('../index.js');

var Drawer = function() {};
var Button = function() {};
var Input = function() {};

var UI = {
  "Drawer": Drawer,
  "Button": Button
};

describe('core-ui', function() {
  describe('registerComponent', function () {
    it('should add a component to the coreUI object', function () {
      expect(coreUI).to.not.have.property('Input');
      coreUI.registerComponent("Input", Input);
      expect(coreUI).to.have.property('Input');
    });
  });
  describe('registerComponents', function () {
    it('should add multiple components to the coreUI object', function () {
      expect(coreUI).to.not.have.property('Drawer');
      coreUI.registerComponents(UI);
      expect(coreUI).to.have.property('Drawer');
      expect(coreUI).to.have.property('Input');
    });
  });
  describe('unregisterComponent', function () {
    it('should remove a component from coreUI object', function () {
      expect(coreUI).to.have.property('Drawer');
      coreUI.unregisterComponent("Drawer");
      expect(coreUI).to.not.have.property('Drawer');
    });
  });
  describe('getComponents', function () {
    it('should return all components', function () {
      var comps = coreUI.getComponents();
      expect(coreUI).to.have.property('registerComponents');
      expect(comps).to.have.property('Button');
    });
  });
});