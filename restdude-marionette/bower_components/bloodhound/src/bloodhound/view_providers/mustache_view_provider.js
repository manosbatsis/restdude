(function() {

var _partialsRegex = /\{\{>\s*([-\w\/.]+)/g;

function MustacheViewProvider(viewResolver) {
	this.viewResolver = viewResolver || null;
	viewResolver = null;
}

MustacheViewProvider.prototype.createTemplate = function createTemplate(name, source) {
	var template = new Bloodhound.Adapters.MustacheTemplate(name, source);
	template.viewResolver = this.viewResolver;
	return template;
};

MustacheViewProvider.prototype.forEachSubTemplate = function forEachSubTemplate(source, callback, context) {
	source.replace(_partialsRegex, function(tag, templateName) {
		callback.call(context, templateName);
	});
};

Bloodhound.ViewProviders.MustacheViewProvider = MustacheViewProvider;

})();
