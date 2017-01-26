/**
 * interface Bloodhound.IRenderPromise
 *
 * This interface is used for promise objects returned by asynchronous
 * render operations.
 **/
Bloodhound.IRenderPromise = {

	/**
	 * new Bloodhound.IRenderPromise(promiser = null)
	 * - promiser (Object): The object making the promise, in this case an instance
	 *                      of Bloodhound.RenderingEngines.IDynamicRenderingEngine.
	 **/
	constructor: function(promiser) {},

	/**
	 * Bloodhound.IRenderPromise#done(callback[, context]) -> Bloodhound.IRenderPromise
	 * - callback (Function): The callback function invoked when the promise is fulfilled
	 * - context (Object): Optional object that "this" refers to in the callback.
	 *
	 * The callback invoked when this asynchronous rendering operation is complete. The
	 * callback takes the following arguments:
	 *   > result, String: The rendered template
	 *   > template, Bloodhound.ITemplate: The template object
	 *   > element, HtmlElement|null: The optional HTML element receiving the new markup
	 *   > promiser, Bloodhound.RenderingEngines.IDynamicRenderingEngine: The rendering engine
	 *   > promise, Bloodhound.IRenderPromise: This promise
	 **/
	done: function(callback, context) {}

};

/**
 * interface Bloodhound.ITemplate
 *
 * This interface is used for template objects that wrap template source
 * code, and the name of this template used by view resolvers.
 **/
Bloodhound.ITemplate = {

	/**
	 * Bloodhound.ITemplate#name -> String
	 *
	 * Name of this template used by view resolvers to find a view.
	 **/
	name: "...",

	/**
	 * Bloodhound.ITemplate#source -> String
	 *
	 * The raw source code for this template.
	 **/
	source: "...",

	/**
	 * new Bloodhound.ITemplate([name[, source]])
	 * - name (String): Optional name for this template
	 * - source (String): Optional source code for this template
	 **/
	constructor: function(name, source) {},

	/**
	 * Bloodhound.ITemplate#render(data) -> String
	 * - data (Object): The object used to render the data
	 *
	 * Render this template with the given data and return the transformed string.
	 **/
	render: function(data) {},

	/**
	 * Bloodhound.ITemplate#setSource(source)
	 * - source (String): The source code for this template
	 **/
	setSource: function(source) {},

	/**
	 * Bloodhound.ITemplate#setViewResolver(viewResolver)
	 * - viewResolver (Bloodhound.ViewResolvers.IViewResolver): The view resolver allowing this template to look up
	 *                                                          sub templates or partials during rendering.
	 **/
	setViewResolver: function(viewResolver) {}

};

/**
 * interface Bloodhound.RenderingEngines.IRenderingEngine
 *
 * This is the interface that all Bloodhound rendering engines must implement.
 **/
Bloodhound.RenderingEngines.IRenderingEngine = {

	/**
	 * new Bloodhound.RenderingEngines.IRenderingEngine(viewResolver = null)
	 * - viewResolver (Bloodhound.ViewResolvers.IViewResolver): The view resolver responsible for looking up views
	 **/
	constructor: function(viewResolver) {},

	/**
	 * Bloodhound.RenderingEngines.IRenderingEngine#render(name, data[, elementOrId]) -> Bloodhound.IRenderPromise
	 * - name (String): The name of the view to render
	 * - data (Object): The object used to render the data
	 * - elementOrId (String|HTMLElement): An HTML tag Id or node in which to render the output
	 *
	 * Render a view with the given data and return the rendered string. If an element or
	 * Id is given, set the innerHTML of that element and return the rendered string.
	 * Returns a Bloodhound.IRenderPromise.
	 **/
	render: function(name, data, elementOrId) {},

	/**
	 * Bloodhound.RenderingEngines.IRenderingEngine#setViewResolver(viewResolver)
	 * - viewResolver (Bloodhound.ViewResolvers.IViewResolver): The view resolver for this rendering engine.
	 **/
	setViewResolver: function(viewResolver) {}

};

/**
 * interface Bloodhound.RenderingEngines.IDynamicRenderingEngine < Bloodhound.RenderingEngines.IRenderingEngine
 *
 * This is the interface that all dynamic Bloodhound rendering engines must
 * implement. This must be used in conjunction with
 * Bloodhound.ViewResolvers.IDynamicViewResolver as resolving views becomes
 * an asynchronous process.
 **/
Bloodhound.RenderingEngines.IDynamicRenderingEngine = {

	/**
	 * new Bloodhound.RenderingEngines.IDynamicRenderingEngine(viewResolver = null)
	 * - viewResolver (Bloodhound.ViewResolvers.IDynamicViewResolver): The view resolver responsible for looking up views
	 **/
	constructor: function(viewResolver) {},

	/**
	 * Bloodhound.RenderingEngines.IDynamicRenderingEngine#render(name, data[, elementOrId]) -> Bloodhound.IRenderPromise
	 * - name (String): The name of the view to render
	 * - data (Object): The object used to render the data
	 * - elementOrId (String|HTMLElement): An HTML tag Id or node in which to render the output
	 *
	 * Render a view with the given data and returns a Bloodhound.IRenderPromise. If an
	 * element or Id is given, set the innerHTML of that element.
	 **/
	render: function(name, data, elementOrId) {},

	/**
	 * Bloodhound.RenderingEngines.IDynamicRenderingEngine#setViewResolver(viewResolver)
	 * - viewResolver (Bloodhound.ViewResolvers.IDynamicViewResolver): The view resolver for this rendering engine.
	 **/
	setViewResolver: function(viewResolver) {}

};

/**
 * interface Bloodhound.ViewProviders.IViewProvider
 *
 * This is the interface that all Bloodhound view providers must implement. A view
 * provider is responsible for creating new template objects that the view
 * resolvers then cache, and inspecting template source allowing the view resolver
 * to find the names of all parials or sub templates within that source code.
 **/
Bloodhound.ViewProviders.IViewProvider = {

	/**
	 * new Bloodhound.ViewProviders.IViewProvider()
	 **/
	constructor: function() {},

	/**
	 * Bloodhound.ViewProviders.IViewProvider#createTemplate(name, source) -> Bloodhound.ITemplate
	 * - name (String): Name of the template
	 * - source (String): Template source code
	 *
	 * Creates a new template object given the view's name and source code.
	 **/
	createTemplate: function(name, source) {},

	/**
	 * Bloodhound.ViewProviders.IViewProvider#forEachSubTemplate(source, callback[, context])
	 * - source (String): Source code for a template
	 * - callback (Function): A callback function invoked for every sub template name.
	 *                        Arguments: templateName (String).
	 * - context (Object): The object that the "this" variable refers to in the callback
	 *
	 * Given the source code, invokes the callback each time a sub template or partial name
	 * is found.
	 **/
	forEachSubTemplate: function(source, callback, context) {}

};

/**
 * interface Bloodhound.ViewResolvers.IViewResolver
 *
 * This is the interface that all Bloodhound view resolvers must implement. A view
 * resolver is responsible for managing cached template objects, looking up new
 * views by name and generating new template objects using a view provider.
 **/
Bloodhound.ViewResolvers.IViewResolver = {

	/**
	 * Bloodhound.ViewResolvers.IViewResolver#templateNameAttribute -> String
	 *
	 * Name of the HTML attribute holding the template name. Used to look up
	 * templates by name when they exist in <script type="text/html"/> tags.
	 **/
	templateNameAttribute: "...",

	/**
	 * new Bloodhound.ViewResolvers.IViewResolver([elementOrId[, provider]])
	 * - elementOrId (String|HtmlElement): The HTML tag or node that contains all the <script type="text/html"/>
	 *                                     tags that contain template source code.
	 * - provider (Bloodhound.ViewProviders.IViewProvider): The view provider for this view resolver.
	 **/
	constructor: function(elementOrId, provider) {},

	/**
	 * Bloodhound.ViewResolvers.IViewResolver#addTemplate(name, template)
	 * - name (String): Name of this new template
	 * - template (Bloodhound.ITemplate): The template object to add to the template cache
	 *
	 * Add a template object to the template cache. Throws an error if a template with a
	 * duplicate name gets added.
	 **/
	addTemplate: function(name, template) {},

	/**
	 * Bloodhound.ViewResolvers.IViewResolver#find(name) -> Bloodhound.ITemplate
	 * - name (String): The name of the view to find
	 *
	 * Find a cached template object and return it. If a template by that name
	 * does not exist in the cache, an error gets thrown.
	 **/
	find: function(name) {},

	/**
	 * Bloodhound.ViewResolvers.IViewResolver#getDocument() -> HtmlDocument
	 *
	 * Gets the document object for this view resolvers container element.
	 **/
	getDocument: function() {},

	/**
	 * Bloodhound.ViewResolvers.IViewResolver#getTemplate(name) -> Bloodhound.ITemplate
	 * - name (String): Name of a cached template
	 *
	 * Get a template object from the cache. Returns null if the "name" is not found.
	 **/
	getTemplate: function(name) {},

	/**
	 * Bloodhound.ViewResolvers.IViewResolver#getTemplateCache() -> Object
	 *
	 * Returns a key-value object containing the cache Bloodhound.ITemplate objects.
	 **/
	getTemplateCache: function() {},

	/**
	 * Bloodhound.ViewResolvers.IViewResolver#setContainer(elementOrId)
	 * - elementOrId (String|HtmlElement): The HTML tag or node that contains all the <script type="text/html"/>
	 *                                     tags that contain template source code.
	 **/
	setContainer: function(elementOrId) {},

	/**
	 * Bloodhound.ViewResolvers.IViewResolver#setProvider(provider)
	 * - provider (Bloodhound.ViewProviders.IViewProvider): The view provider for this view resolver.
	 **/
	setProvider: function(provider) {}

};

/**
 * interface Bloodhound.ViewResolvers.IDynamicViewResolver < Bloodhound.ViewResolvers.IViewResolver
 *
 * This is the interface that all Bloodhound dynamic view resolvers must implement.
 * A dynamic view resolver is responsible for managing cached template objects,
 * looking up new views by name, and fetching template code then generating new
 * template objects using a view provider. Some method calls may be asynchronous.
 **/
Bloodhound.ViewResolvers.IDynamicViewResolver = {

	/**
	 * Bloodhound.ViewResolvers.IDynamicViewResolver#templateExtension -> String
	 *
	 * The file extension appended to convention based URLs for views. Defaults to ".tpl".
	 **/
	templateExtension: "...",

	/**
	 * Bloodhound.ViewResolvers.IDynamicViewResolver#templateUrlAttribute -> String
	 *
	 * The HTML tag attribute that holds the custom URL for a view.
	 *
	 * e.g. <script type="text/html"
	 *          data-template-name="foo"
	 *          data-template-url="/path/to/foo.tpl"></script>
	 **/
	templateUrlAttribute: "...",

	/**
	 * Bloodhound.ViewResolvers.IDynamicViewResolver#templateUrlBase -> String
	 *
	 * The base URL for views fetched by name convention.
	 **/
	templateUrlBase: "...",

	/**
	 * Bloodhound.ViewResolvers.IDynamicViewResolver#find(name) -> Bloodhound.ITemplate
	 * - name (String): The name of the view to find
	 *
	 * Bloodhound.ViewResolvers.IDynamicViewResolver#find(name, callback[, context])
	 * - name (String): The name of the view to find
	 * - callback (Function): The function invoked when the source code for the view
	 *                        and all sub views has been loaded. Takes a single
	 *                        argument: template (Bloodhound.ITemplate).
	 * - context (Object): Optional argument setting what "this" references inside the
	 *                     callback function.
	 *
	 * Find a template by the given name. This method has two overrides.
	 *
	 * If no callback is given, this call is synchronous and returns an
	 * ITemplate. If no template exists in the template cache, an error is
	 * thrown.
	 *
	 * In the second override, a callback and optional context is passed. This
	 * method call becomes asynchronous with no return value. If there are any
	 * problems fetching the source code for the view or sub view, then an
	 * error is thrown and the callback is never invoked.
	 **/
	find: function(name, callback, context) {},

};
