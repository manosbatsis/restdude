var assign = require('object-assign');

var coreUI = {
  registerComponent: function(name, src) {
    if (coreUI[name]) {
      throw new Error('Component ' + name + ' already exists in coreUI. Use a different name.');
    }
    coreUI[name] = src;
    return coreUI;
  },
  unregisterComponent: function(name) {
    delete coreUI[name];
    return coreUI;
  },
  registerComponents: function(obj) {
    Object.keys(obj).map(function(component) {
      coreUI.registerComponent(component, obj[component]);
    });
  },
  getComponents: function() {
    var components = Object.assign({}, coreUI);
    delete components.registerComponent;
    delete components.registerdComponents;
    delete components.getComponents;
    return components;
  } 
};
module.exports = coreUI; 


