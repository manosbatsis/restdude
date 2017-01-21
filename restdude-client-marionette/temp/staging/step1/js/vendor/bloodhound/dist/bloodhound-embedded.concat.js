/*! bloodhound 2014-05-14 */
(function() {

function EmbeddedRenderingEngine(viewResolver) {
	if (viewResolver) {
		this.setViewResolver(viewResolver);
	}
}

EmbeddedRenderingEngine.prototype.render = function render(name, data, elementOrId) {
	var promise = new Bloodhound.RenderPromise(this),
	    template = this.viewResolver.find(name),
	    result = "",
	    element = null,
	    doc = this.viewResolver.getDocument();

	if (!template) {
		throw new Error("Could not find template named: " + name);
	}

	result = template.render(data);

	if (elementOrId) {
		element = typeof elementOrId === "string"
		        ? doc.getElementById(elementOrId)
		        : elementOrId;

		if (!element) {
			throw new Error("Cannot find element: " + elementOrId);
		}

		element.innerHTML = result;
	}

	promise.fulfill("done", result, template, element);

	element = template = data = elementOrId = null;

	return promise;
};

EmbeddedRenderingEngine.prototype.setViewResolver = function setViewResolver(viewResolver) {
	this.viewResolver = viewResolver;
};

Bloodhound.RenderingEngines.EmbeddedRenderingEngine = EmbeddedRenderingEngine;

})();

(function() {

/**
 * class Bloodhound.ViewResolvers.EmbeddedViewResolver < Bloodhound.ViewResolvers.IViewResolver
 *
 * This class resolves views that are embedded in the current HTML document
 * inside special <script type="text/html"/> tags. The templateNameAttribute
 * property is used to match a <script/> tag to the name of a view.
 *
 * For example:
 *
 *     <script type="text/html" data-template-name="foo">
 *         <p>Foo: {{foo}}</p>
 *     </script>
 *
 *     <script type="text/javascript">
 *         var provider = new Bloodhound.ViewProviders.MustacheViewProvider();
 *         var viewResolver = new Bloodhound.ViewResolvers.EmbeddedViewResolver(document, provider);
 *
 *         var template = viewResolver.find("foo");
 *     </script>
 *
 * This will return an implementation of Bloodhound.ITemplate where the
 * source property is taken from the innerHTML of the script tag above.
 *
 * All method calls in this class are synchronous, and all views used
 * on the current page are required to exist in the HTML document inside
 * the <script type="text/html"/> tags.
 **/
function EmbeddedViewResolver(container, provider) {

	// Public Properties

	this.templateNameAttribute = "data-template-name";

	// Public Methods

	this.addTemplate = addTemplate;
	this.find = find;
	this.getDocument = getDocument;
	this.getTemplate = getTemplate;
	this.getTemplateCache = getTemplateCache;
	this.setContainer = setContainer;
	this.setProvider = setProvider;

	// Private Properties

	var self = this,
	    _container = null,
	    _provider = null,
	    _sourceNodeCache = {},
	    _templates = {};

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

	function find(name) {
		if (!_templates[name]) {
			var node = getSourceNode(name);

			if (!node) {
				throw new Error("No source node found for view named: " + name);
			}

			_templates[name] = _provider.createTemplate(name, node.innerHTML);
		}

		return _templates[name];
	}

	function getDocument() {
		return self.container.ownerDocument || self.container;
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

}

Bloodhound.ViewResolvers.EmbeddedViewResolver = EmbeddedViewResolver;

})();
