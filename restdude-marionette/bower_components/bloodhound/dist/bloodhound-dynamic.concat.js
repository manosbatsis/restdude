/*! bloodhound 2014-05-14 */
(function() {

function DynamicRenderingEngine(viewResolver) {
	this.viewResolver = viewResolver || null;
}

DynamicRenderingEngine.prototype.viewResolver = null;

DynamicRenderingEngine.prototype.render = function render(name, data, elementOrId) {
	var promise = new Bloodhound.RenderPromise(this),
	    element = null,
	    doc = this.viewResolver.getDocument();

	if (elementOrId) {
		if (typeof elementOrId === "string") {
			element = doc.getElementById(elementOrId);

			if (!element) {
				throw new Error("Failed to find element '" + elementOrId + "' in document: " + (doc.documentURI || doc.location));
			}
		}
		else {
			element = elementOrId;
		}
	}

	this.viewResolver.find(name, function(template) {
		var result = template.render(data);

		if (element) {
			element.innerHTML = result;
		}

		promise.fulfill("done", result, template, element);

		element = template = data = null;
	});

	return promise;
};

Bloodhound.RenderingEngines.DynamicRenderingEngine = DynamicRenderingEngine;

})();

(function() {

/**
 * class Bloodhound.ViewResolvers.DynamicViewResolver < Bloodhound.ViewResolvers.IDynamicViewResolver
 *
 * This class allows you to resolve views that are either embedded in the
 * HTML document, or fetched in via AJAX.
 *
 * Example (Embedded Templates, inherited from Bloodhound.ViewResolvers.IViewResolver)
 *
 *     <script type="text/html" data-template-name="foo">
 *         <p>Foo: {{foo}}</p>
 *     </script>
 *
 * You may also trigger an AJAX call to an arbitrary URL by utilizing the
 * data-template-url attribute on the <script/> tag:
 *
 * Example (AJAX templates, custom URL):
 *
 *     <script type="text/html" data-template-name="foo"
 *         data-template-url="/path/to/foo.tpl"></script>
 *
 * The last method involves using a convention based mapping of view names
 * to URLs:
 *
 * Example (AJAX templates with URL convention):
 *
 *     viewResolver.find("blogs/posts/detail", function(template) {
 *         // use "template"
 *     });
 *
 * The view named "blogs/posts/detail" by default makes an AJAX call to
 * /js/app/views/blogs/posts/detail.tpl to fetch the source code for that
 * template. You can override the base URL and file extension, which we
 * will discuss later.
 *
 * Below shows how you can use this view resolver and customize it:
 *
 *     <script type="text/javascript">
 *         var provider = new Bloodhound.ViewProviders.MustacheViewProvider();
 *         var viewResolver = new Bloodhound.ViewResolvers.DynamicViewResolver(document, provider);
 *
 *         // Use a non standard base URL for AJAX'd in templates:
 *         viewResolver.templateUrlBase = "/my/custom/url/base";
 *
 *         // Use a non standard file extension for all AJAX'd in templates:
 *         viewResolver.templateExtension = ".mustache";
 *
 *         // Synchronous method call (inherited from Bloodhound.ViewProviders.IViewProvider):
 *         var template = viewResolver.find("foo");
 *
 *         // Asynchronous method call:
 *         viewResolver.find("foo", function(template) {
 *             // do something with "template"
 *         });
 *
 *         // Asynchronous method call, setting value of "this" in callback:
 *         viewResolver.find("foo", function(template) {
 *             // do something with "template"
 *         }, this);
 *     </script>
 *
 * When finding a new template, the steps below are executed:

 * 1) Find the view. If a data-template-url is provided, make a request
 *    to that URL, otherwise build a URL based on the templateUrlBase,
 *    and templateExtension properties and the template name.
 * 2) Make the AJAX request. If a 200 status is returned from the server,
 *    then pass the responseText and name into the viewProvider and get
 *    back an implementation of Bloodhound.ITemplate. If a non 200 HTTP
 *    status is returned, throw an error.
 * 3) Have the view provider inspect the template source code for sub
 *    templates or partials. If they exist, fetch those from the server.
 * 4) Keep fetching sub templates for every sub template fetched until
 *    all sub templates have been fetched and cached as template objects.
 * 5) After all sub templates have been fetched and cached, invoke the
 *    callback, passing in the original template object for the requested
 *    view.
 *
 * Subsequent calls for the same view will return the cached template
 * objects. You should always provide a callback function when finding
 * views.
 **/
function DynamicViewResolver(container, provider) {

	// Public Properties

	this.httpMethod = "GET";
	this.httpMethodAttribute = "data-template-method";
	this.templateNameAttribute = "data-template-name";
	this.templateExtension = ".tpl";
	this.templateUrlAttribute = "data-template-url";
	this.templateUrlBase = "/js/app/views";

	// Public Methods

	this.addTemplate = addTemplate;
	this.find = find;
	this.getDocument = getDocument;
	this.getTemplate = getTemplate;
	this.getTemplateCache = getTemplateCache;
	this.setContainer = setContainer;
	this.setProvider = setProvider;

	// Private Properties

	var _templates = {},
	    _container = null,
	    _provider = null,
	    _sourceNodeCache = {},
	    self = this;

	// Private Methods

	function initialize(container, provider) {
		if (container) {
			this.setContainer(container);
		}

		if (provider) {
			this.setProvider(provider);
		}
	}

	function addTemplate(name, template) {
		if (_templates[name]) {
			throw new Error("A template named '" + name + "' already exists.")
		}

		_templates[name] = template;
	}

	function fetch(name, callback, context) {
		if (_templates[name]) {
			callback.call(context || null, _templates[name]);
			return;
		}

		var uri = getTemplateURI(name),
		    url = uri.url,
		    method = uri.method,
		    xhr = new XMLHttpRequest();

		var readyStateChanged = function readyStateChanged() {
			if (xhr.readyState !== 4) {
				return;
			}
			else if (xhr.status === 200) {
				fetchSubTemplates(xhr.responseText, function() {
					_templates[name] = _provider.createTemplate(name, xhr.responseText);
					callback.call(context || null, _templates[name]);
					complete();
				});
			}
			else {
				complete();
				throw new Error("Request to fetch template '" + name + "' from URL '" + method + " " + url + "' failed with status: " + this.status);
			}
		};

		var complete = function complete() {
			xhr.onreadystatechange = null;
			xhr = callback = context = uri = null;
		};

		xhr.onreadystatechange = readyStateChanged;
		xhr.open(method, url);
		xhr.setRequestHeader("X-Requested-With", "XMLHttpRequest");
		xhr.send(null);
	}

	function fetchSubTemplates(source, callback) {
		var count = 0;

		var handleTemplateFetched = function(template) {
			count--;
			checkCount();
			template = null;
		};

		var checkCount = function() {
			if (!count) {
				callback.call();
				callback = handleTemplateFetched = checkCount = null;
			}
		};

		_provider.forEachSubTemplate(source, function(name) {
			count++;
			fetch(name, handleTemplateFetched);
		});

		checkCount();
	}

	function find(name, callback, context) {
		context = context || null;

		if (_templates[name]) {
			if (callback) {
				callback.call(context, _templates[name]);
				return;
			}
			else {
				return _templates[name];
			}
		}
		else if (!callback) {
			throw new Error("Cannot find uncached template: " + name);
		}

		var node = getSourceNode(name);

		if (node && !node.getAttribute(self.templateUrlAttribute)) {
			fetchSubTemplates(node.innerHTML, function() {
				_templates[name] = _provider.createTemplate(name, node.innerHTML);
				callback.call(context, _templates[name]);
			});
		}
		else {
			fetch(name, function(template) {
				callback.call(context, template);
			});
		}
	}

	function getDocument() {
		return _container.ownerDocument || _container;
	}

	function getSourceNode(name, container) {
		container = container || _container;

		if (_sourceNodeCache[name]) {
			return _sourceNodeCache[name];
		}

		var scripts = container.getElementsByTagName("script"),
		    i = scripts.length;

		while (i--) {
			if (scripts[i].getAttribute(self.templateNameAttribute) === name) {
				_sourceNodeCache[name] = scripts[i];
				break;
			}
		}

		scripts = container = null;

		return _sourceNodeCache[name] || null;
	}

	function getTemplate(name) {
		return _templates[name] || null;
	}

	function getTemplateCache() {
		return _templates;
	}

	function getTemplateURI(name) {
		var url = "",
		    method = self.httpMethod,
		    node = getSourceNode(name);

		if (node) {
			url = node.getAttribute(self.templateUrlAttribute);

			if (!url) {
				throw new Error("Missing required attribute " + self.templateUrlAttribute + " on <script type='text/html' " + self.templateNameAttribute + "='" + name + "' />");
			}

			method = node.getAttribute(self.httpMethodAttribute) || self.httpMethod;
		}
		else {
			url = self.templateUrlBase
			    + (/^\//.test(name) ? name : "/" + name)
			    + self.templateExtension;
		}

		url += (/^\?/.test(url) ? "&" : "?") + "__cache__=" + DynamicViewResolver.cacheBuster;

		return {
			url: url,
			method: method.toUpperCase()
		};
	}

	function setContainer(elementOrId) {
		_container = typeof elementOrId === "string"
		           ? document.getElementById(elementOrId)
		           : elementOrId;
	}

	function setProvider(provider) {
		_provider = provider;
		_provider.viewResolver = self;
	}

	// call constructor
	initialize.call(this, container, provider);

};

DynamicViewResolver.cacheBuster = new Date().getTime();

Bloodhound.ViewResolvers.DynamicViewResolver = DynamicViewResolver;

})();
