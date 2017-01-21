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
