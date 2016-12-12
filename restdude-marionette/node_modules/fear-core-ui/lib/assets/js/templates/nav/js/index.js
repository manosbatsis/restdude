System.register('templates/nav/js/default.js', ['node_modules/systemjs-plugin-babel/babel-helpers/classCallCheck.js', 'node_modules/systemjs-plugin-babel/babel-helpers/createClass.js'], function (_export, _context) {
    "use strict";

    var _classCallCheck, _createClass, Navigation;

    return {
        setters: [function (_node_modulesSystemjsPluginBabelBabelHelpersClassCallCheckJs) {
            _classCallCheck = _node_modulesSystemjsPluginBabelBabelHelpersClassCallCheckJs.default;
        }, function (_node_modulesSystemjsPluginBabelBabelHelpersCreateClassJs) {
            _createClass = _node_modulesSystemjsPluginBabelBabelHelpersCreateClassJs.default;
        }],
        execute: function () {
            Navigation = function () {
                function Navigation() {
                    _classCallCheck(this, Navigation);

                    this.analytics();
                }

                _createClass(Navigation, [{
                    key: 'analytics',
                    value: function (_analytics) {
                        function analytics() {
                            return _analytics.apply(this, arguments);
                        }

                        analytics.toString = function () {
                            return _analytics.toString();
                        };

                        return analytics;
                    }(function () {

                        var analyticActions = [{
                            functionName: 'analyticsTopNav',
                            event: 'click'
                        }];

                        analytics.bind(document.querySelectorAll('.nav__wrapper .nav > li > a'), analyticActions);
                    })
                }]);

                return Navigation;
            }();

            _export('default', Navigation);
        }
    };
});
System.register("node_modules/systemjs-plugin-babel/babel-helpers/classCallCheck.js", [], function (_export, _context) {
  "use strict";

  return {
    setters: [],
    execute: function () {
      _export("default", function (instance, Constructor) {
        if (!(instance instanceof Constructor)) {
          throw new TypeError("Cannot call a class as a function");
        }
      });
    }
  };
});
System.register("node_modules/systemjs-plugin-babel/babel-helpers/createClass.js", [], function (_export, _context) {
  "use strict";

  return {
    setters: [],
    execute: function () {
      _export("default", function () {
        function defineProperties(target, props) {
          for (var i = 0; i < props.length; i++) {
            var descriptor = props[i];
            descriptor.enumerable = descriptor.enumerable || false;
            descriptor.configurable = true;
            if ("value" in descriptor) descriptor.writable = true;
            Object.defineProperty(target, descriptor.key, descriptor);
          }
        }

        return function (Constructor, protoProps, staticProps) {
          if (protoProps) defineProperties(Constructor.prototype, protoProps);
          if (staticProps) defineProperties(Constructor, staticProps);
          return Constructor;
        };
      }());
    }
  };
});
System.register('templates/nav/js/mobile.js', ['node_modules/systemjs-plugin-babel/babel-helpers/classCallCheck.js', 'node_modules/systemjs-plugin-babel/babel-helpers/createClass.js'], function (_export, _context) {
    "use strict";

    var _classCallCheck, _createClass, SCROLL_INTO_VIEW_DELAY, MobileNav;

    return {
        setters: [function (_node_modulesSystemjsPluginBabelBabelHelpersClassCallCheckJs) {
            _classCallCheck = _node_modulesSystemjsPluginBabelBabelHelpersClassCallCheckJs.default;
        }, function (_node_modulesSystemjsPluginBabelBabelHelpersCreateClassJs) {
            _createClass = _node_modulesSystemjsPluginBabelBabelHelpersCreateClassJs.default;
        }],
        execute: function () {
            SCROLL_INTO_VIEW_DELAY = 160;

            MobileNav = function () {
                function MobileNav() {
                    var windowReference = arguments.length <= 0 || arguments[0] === undefined ? window : arguments[0];

                    _classCallCheck(this, MobileNav);

                    this.window = windowReference;
                    this.lastTimeout = null;

                    window.addEventListener('resize', function () {
                        loadMobileJS.call(MobileNav);
                    });

                    function loadMobileJS() {
                        if (window.innerWidth < 900) {
                            this.bind();
                        }
                    }

                    loadMobileJS.call(this);
                }

                _createClass(MobileNav, [{
                    key: 'bind',
                    value: function bind() {
                        var _this = this;

                        if (this.window.document.getElementsByClassName) {
                            var checkboxes = this.window.document.getElementsByClassName('menu__mobile-level3');

                            for (var iCheckbox = 0; iCheckbox < checkboxes.length; iCheckbox++) {
                                checkboxes[iCheckbox].onchange = function (e) {
                                    _this.level3Change(e);
                                };
                            }

                            var radioButtons = this.window.document.getElementsByName('menu');

                            for (var iRadio = 0; iRadio < radioButtons.length; iRadio++) {
                                radioButtons[iRadio].onchange = function (e) {
                                    _this.menuChange(e);
                                };
                            }
                        }
                    }
                }, {
                    key: 'isElementVisible',
                    value: function isElementVisible(el) {

                        var rect = el.getBoundingClientRect();

                        return rect.top >= 0 && rect.left >= 0 && rect.bottom <= (this.window.innerHeight || this.window.document.documentElement.clientHeight) && rect.right <= (this.window.innerWidth || this.window.document.documentElement.clientWidth);
                    }
                }, {
                    key: 'scrollCurrentLevel3IntoView',
                    value: function scrollCurrentLevel3IntoView(level3Id) {
                        var _this2 = this;

                        this.window.clearTimeout(this.lastTimeout);

                        this.lastTimeout = this.window.setTimeout(function () {
                            var label = _this2.window.document.querySelector('[for=' + level3Id + ']');

                            if (!_this2.isElementVisible(label)) {
                                label.scrollIntoView();
                            }
                        }, SCROLL_INTO_VIEW_DELAY);
                    }
                }, {
                    key: 'deSelectLevel3Checkboxes',
                    value: function deSelectLevel3Checkboxes(selectedCheckboxId) {
                        var checkboxes = this.window.document.querySelectorAll('.menu__mobile-level3:checked:not(#' + selectedCheckboxId + ')');

                        for (var i = 0; i < checkboxes.length; i++) {
                            checkboxes[i].checked = false;
                        }
                    }
                }, {
                    key: 'level3Change',
                    value: function level3Change(e) {
                        this.deSelectLevel3Checkboxes(e.target.id);
                        this.scrollCurrentLevel3IntoView(e.target.id);
                    }
                }, {
                    key: 'menuChange',
                    value: function menuChange(e) {
                        this.deSelectLevel3Checkboxes(e.target.id);
                    }
                }]);

                return MobileNav;
            }();

            _export('default', MobileNav);
        }
    };
});
System.register('templates/nav/js/index.js', ['./default', './mobile'], function (_export, _context) {
  "use strict";

  var Navigation, Mobile;
  return {
    setters: [function (_default) {
      Navigation = _default.default;
    }, function (_mobile) {
      Mobile = _mobile.default;
    }],
    execute: function () {

      new Navigation();
      new Mobile();
    }
  };
});
//# sourceMappingURL=index.js.map