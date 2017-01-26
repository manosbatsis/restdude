(function() {

var _tagRegexes = [
	Template.REGEX_FOREACH,
	Template.REGEX_RENDER,
	Template.REGEX_INCLUDE
];

function SimpleTemplateViewProvider(viewResolver) {
	this.viewResolver = viewResolver || null;
	viewResolver = null;
}

SimpleTemplateViewProvider.prototype.createTemplate = function createTemplate(name, source) {
	var template = new Template(name, source);
	template.viewResolver = this.viewResolver;
	return template;
};

SimpleTemplateViewProvider.prototype.forEachSubTemplate = function forEachSubTemplate(source, callback, context) {
	var i = 0, length = _tagRegexes.length;

	for (i; i < length; i++) {
		source = source.replace(_tagRegexes[i], function(tag, templateName) {
			callback.call(context, templateName);
		});
	}

	callback = context = null;
};

Bloodhound.ViewProviders.SimpleTemplateViewProvider = SimpleTemplateViewProvider;

})();

