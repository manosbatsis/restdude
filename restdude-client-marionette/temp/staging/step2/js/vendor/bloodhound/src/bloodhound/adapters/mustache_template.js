(function() {

function MustacheTemplate(name, source) {
	this.partials = [];
	this.name = name || null;

	if (source) {
		this.setSource(source);
	}
}

MustacheTemplate.prototype = {

	name: null,

	partials: null,

	source: null,

	viewResolver: null,

	constructor: MustacheTemplate,

	getPartials: function getPartials(partials) {
		if (!this.partials.length) {
			return null;
		}

		partials = partials || {};

		var name, template, i = 0,
		    length = this.partials.length;

		for (i; i < length; i++) {
			name = this.partials[i];
			template = this.viewResolver.find(name);
			partials[name] = template.source;
			template.getPartials(partials);
		}

		return partials;
	},

	render: function render(data) {
		return Mustache.render(this.source, data, this.getPartials());
	},

	setSource: function setSource(source) {
		var partials = this.partials;

		this.source = source;

		source.replace(/\{\{>\s*([-\w.\/]+)\s*\}\}/g, function(tag, partial) {
			partials.push(partial);
		});
	},

	setViewResolver: function setViewResolver(viewResolver) {
		this.viewResolver = viewResolver;
		viewResolver = null;
	}

};

Bloodhound.Adapters.MustacheTemplate = MustacheTemplate;

})();
