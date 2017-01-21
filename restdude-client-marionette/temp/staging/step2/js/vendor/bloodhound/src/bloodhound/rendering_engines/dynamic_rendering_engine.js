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
