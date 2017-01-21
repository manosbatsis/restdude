// generatedy by JSX compiler 0.9.84 (2014-03-25 11:22:27 +0900; 1e07252cc54a2d7c6b4ab95268995c6656833a80)
var JSX = {};
(function (JSX) {
/**
 * extends the class
 */
function $__jsx_extend(derivations, base) {
	var ctor = function () {};
	ctor.prototype = base.prototype;
	var proto = new ctor();
	for (var i in derivations) {
		derivations[i].prototype = proto;
	}
}

/**
 * copies the implementations from source interface to target
 */
function $__jsx_merge_interface(target, source) {
	for (var k in source.prototype)
		if (source.prototype.hasOwnProperty(k))
			target.prototype[k] = source.prototype[k];
}

/**
 * defers the initialization of the property
 */
function $__jsx_lazy_init(obj, prop, func) {
	function reset(obj, prop, value) {
		delete obj[prop];
		obj[prop] = value;
		return value;
	}

	Object.defineProperty(obj, prop, {
		get: function () {
			return reset(obj, prop, func());
		},
		set: function (v) {
			reset(obj, prop, v);
		},
		enumerable: true,
		configurable: true
	});
}

var $__jsx_imul = Math.imul;
if (typeof $__jsx_imul === "undefined") {
	$__jsx_imul = function (a, b) {
		var ah  = (a >>> 16) & 0xffff;
		var al = a & 0xffff;
		var bh  = (b >>> 16) & 0xffff;
		var bl = b & 0xffff;
		return ((al * bl) + (((ah * bl + al * bh) << 16) >>> 0)|0);
	};
}

/**
 * fused int-ops with side-effects
 */
function $__jsx_ipadd(o, p, r) {
	return o[p] = (o[p] + r) | 0;
}
function $__jsx_ipsub(o, p, r) {
	return o[p] = (o[p] - r) | 0;
}
function $__jsx_ipmul(o, p, r) {
	return o[p] = $__jsx_imul(o[p], r);
}
function $__jsx_ipdiv(o, p, r) {
	return o[p] = (o[p] / r) | 0;
}
function $__jsx_ipmod(o, p, r) {
	return o[p] = (o[p] % r) | 0;
}
function $__jsx_ippostinc(o, p) {
	var v = o[p];
	o[p] = (v + 1) | 0;
	return v;
}
function $__jsx_ippostdec(o, p) {
	var v = o[p];
	o[p] = (v - 1) | 0;
	return v;
}

/**
 * non-inlined version of Array#each
 */
function $__jsx_forEach(o, f) {
	var l = o.length;
	for (var i = 0; i < l; ++i)
		f(o[i]);
}

/*
 * global functions, renamed to avoid conflict with local variable names
 */
var $__jsx_parseInt = parseInt;
var $__jsx_parseFloat = parseFloat;
function $__jsx_isNaN(n) { return n !== n; }
var $__jsx_isFinite = isFinite;

var $__jsx_encodeURIComponent = encodeURIComponent;
var $__jsx_decodeURIComponent = decodeURIComponent;
var $__jsx_encodeURI = encodeURI;
var $__jsx_decodeURI = decodeURI;

var $__jsx_ObjectToString = Object.prototype.toString;
var $__jsx_ObjectHasOwnProperty = Object.prototype.hasOwnProperty;

/*
 * profiler object, initialized afterwards
 */
function $__jsx_profiler() {
}

/*
 * public interface to JSX code
 */
JSX.require = function (path) {
	var m = $__jsx_classMap[path];
	return m !== undefined ? m : null;
};

JSX.profilerIsRunning = function () {
	return $__jsx_profiler.getResults != null;
};

JSX.getProfileResults = function () {
	return ($__jsx_profiler.getResults || function () { return {}; })();
};

JSX.postProfileResults = function (url, cb) {
	if ($__jsx_profiler.postResults == null)
		throw new Error("profiler has not been turned on");
	return $__jsx_profiler.postResults(url, cb);
};

JSX.resetProfileResults = function () {
	if ($__jsx_profiler.resetResults == null)
		throw new Error("profiler has not been turned on");
	return $__jsx_profiler.resetResults();
};
JSX.DEBUG = true;
var GeneratorFunction$0 = 
(function () {
  try {
    return Function('import {GeneratorFunction} from "std:iteration"; return GeneratorFunction')();
  } catch (e) {
    return function GeneratorFunction () {};
  }
})();
var __jsx_generator_object$0 = 
(function () {
  function __jsx_generator_object() {
  	this.__next = 0;
  	this.__loop = null;
  	this.__value = undefined;
  	this.__status = 0;	// SUSPENDED: 0, ACTIVE: 1, DEAD: 2
  }

  __jsx_generator_object.prototype.next = function () {
  	switch (this.__status) {
  	case 0:
  		this.__status = 1;

  		// go next!
  		this.__loop(this.__next);

  		var done = false;
  		if (this.__next != -1) {
  			this.__status = 0;
  		} else {
  			this.__status = 2;
  			done = true;
  		}
  		return { value: this.__value, done: done };
  	case 1:
  		throw new Error("Generator is already running");
  	case 2:
  		throw new Error("Generator is already finished");
  	default:
  		throw new Error("Unexpected generator internal state");
  	}
  };

  return __jsx_generator_object;
}());
function _Main() {
	var $__jsx_profiler_ctx = $__jsx_profiler.enter("_Main#constructor()");
	$__jsx_profiler.exit();
};

$__jsx_extend([_Main], Object);
function _Main$main$AS(args) {
	var $__jsx_profiler_ctx = $__jsx_profiler.enter("_Main.main(:Array.<string>)");
	var i;
	for (i = 1; i <= 100; ++i) {
		if (i % 15 === 0) {
			console.log("FizzBuzz");
		} else if (i % 3 === 0) {
			console.log("Fizz");
		} else if (i % 5 === 0) {
			console.log("Buzz");
		} else {
			console.log(i);
		}
	}
	$__jsx_profiler.exit();
};

_Main.main = _Main$main$AS;
_Main.main$AS = _Main$main$AS;


var $__jsx_classMap = {
	"fixtures/fizzbuzz.jsx": {
		_Main: _Main,
		_Main$: _Main
	}
};


(function () {
	"use strict";

	var Profiler = $__jsx_profiler;

	var getTime;
	if (typeof(performance) != "undefined" && typeof(performance.now) == "function") {
		getTime  = function () { return performance.now() };
	}
	else {
		getTime  = function () { return Date.now() };
	}

	var stack = [ {
		$name: "<<root>>",
		$cur_exclusive: getTime()
	} ];

	Profiler.enter = function (name) {
		var t = getTime();
		var caller = stack[stack.length - 1];
		caller.$cur_exclusive -= t;
		var callee = caller[name];
		if (callee) {
			callee.$cur_inclusive = t;
			callee.$cur_exclusive = t;
		} else {
			callee = caller[name] = {
				$name: name,
				$cur_inclusive: t,
				$cur_exclusive: t,
				$inclusive: 0,
				$exclusive: 0,
				$count: 0
			};
		}
		stack.push(callee);
		return stack.length;
	};

	Profiler.exit = function (retval) {
		var t = getTime();
		var callee = stack.pop();
		++callee.$count;
		callee.$exclusive += t - callee.$cur_exclusive;
		callee.$inclusive += t - callee.$cur_inclusive;
		var caller = stack[stack.length - 1];
		caller.$cur_exclusive += t;
		return retval;
	};

	Profiler.resume = function (context) {
		while (context < stack.length) {
			Profiler.exit();
		}
	};

	Profiler.getResults = function () {
		return stack[0];
	};

	Profiler.postResults = function (url, cb) {
		if (! cb) {
			cb = function (error, message) {
				if (error) {
					console.error("Profiler: " + error.toString());
				}
				else {
					console.log("Profiler: " + message);
				}
			}
		}
		var content = JSON.stringify(Profiler.getResults(), function (k, v) {
			return typeof(v) === "number" ? Math.round(v) : v;
		});
		if (typeof(XMLHttpRequest) !== "undefined") {
			return this._postResultsXHR(url, content, cb);
		}
		if (typeof(require) !== "undefined" && require("http")) {
			return this._postResultsNode(url, content, cb);
		}
		cb(new ReferenceError("XMLHttpRequest is not defined"), null);
	};

	Profiler._postResultsXHR = function (url, content, cb) {
		// post target should support gist-style API
		var xhr = new XMLHttpRequest();
		xhr.onreadystatechange = function () {
			if (xhr.readyState == 4) {
				if (xhr.status == 200 || xhr.status == 201 || xhr.status == 0) {
					cb(null, xhr.getResponseHeader("Location") || xhr.responseText);
				} else {
					cb(new Error("failed to post profiler results, received " + xhr.status + " " + xhr.statusText + " response from server"), null);
				}
			}
		};
		xhr.onerror = function (event) {
			cb(new Error("failed to post profiler results"), null);
		};
		xhr.open("POST", url, /* async: */true);
		xhr.setRequestHeader("Content-Type", "application/json");
		xhr.send(content);
	};

	Profiler._postResultsNode = function (url, content, cb) {
		var http = require("http");
		url = require("url").parse(url);
		var req = http.request({
			method: "POST",
			hostname: url.hostname,
			port: url.port,
			path: url.path,
			headers: {
				'Content-Type': 'application/json',
				'Content-Length': Buffer.byteLength(content, "utf8"),
			},
		}, function (res) {
			res.setEncoding("utf8");
			var data = "";
			res.on("data", function (chunk) {
				data += chunk;
			});
			res.on("end", function () {
				if (res.statusCode == 200 || res.statusCode == 201) {
					cb(null, res.headers.location || data);
				} else {
					cb(new Error("failed to post profiler results, received " + res.statusCode + " response from server"), null);
				}
			});
		}).on('error', function (e) {
			cb(e, null);
		});
		req.write(content, "utf8");
		req.end();
	};

	Profiler.resetResults = function () {
		var t = getTime();
		for (var stackIndex = 0; stackIndex < stack.length; ++stackIndex) {
			var isLeaf = stackIndex == stack.length - 1;
			// reset the counters
			stack[stackIndex].$cur_inclusive = t;
			stack[stackIndex].$cur_exclusive = isLeaf ? t : 0;
			stack[stackIndex].$inclusive = 0;
			stack[stackIndex].$exclusive = 0;
			stack[stackIndex].$count = 0;
			// reset callees
			for (var k in stack[stackIndex]) {
				if (k.charAt(0) != "$") {
					if (! isLeaf && stack[stackIndex][k] == stack[stackIndex + 1]) {
						// preserve the current call path
					} else {
						delete stack[stackIndex][k];
					}
				}
			}
		}
	};

})();
})(JSX);
