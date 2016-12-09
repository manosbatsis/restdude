/*
 * calipso-hub-webapp - A full stack, high level framework for lazy application hackers.
 * Copyright © 2005 Manos Batsis (manosbatsis gmail)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * @exports Calipso.Model
 */
define(['jquery', 'underscore', 'bloodhound', 'typeahead', "lib/calipsolib/util", "lib/calipsolib/form",
        "lib/calipsolib/uifield", "lib/calipsolib/backgrid", "lib/calipsolib/view", 'handlebars'],
    function ($, _, Bloodhoud, Typeahead, Calipso, CalipsoForm, CalipsoField, CalipsoGrid, CalipsoView, Handlebars) {

        /**
         * A base model implementation to extend for your own models.
         * Provides usecase metadata as a means to declaratively define view
         * hierarchies. that may also map to dynamic (i.e. non-explicit)
         * controller routes.
         * @alias module:Calipso.Model
         * @constructor
         * @augments module:Backbone.Model
         */
        Calipso.Model = Calipso.model.Model.extend(
            /** @lends module:Calipso.Model.prototype */
            {
                skipDefaultSearch: false,

                initialize: function () {
                    Calipso.model.Model.prototype.initialize.apply(this, arguments);
                },
                toString: function () {
                    return this.get(this.constructor.nameProperty) || this.get("name") || this.get("id");
                },
                toHtmlSafeString: function () {
                    return Calipso.stripHtml(this.toString());
                },
                /**
                 * Returns the URL for this model, giving precedence  to the collection URL if the model belongs to one,
                 * or a URL based on the model path fragment otherwise.
                 */
                url: function () {
                    var sUrl = this.collection && _.result(this.collection, 'url') ? _.result(this.collection, 'url') : Calipso.getBaseUrl() + this.getBaseFragment() + this.getPathFragment() /*_.result(this, 'urlRoot')*/ || urlError();
                    if (!this.isNew()) {
                        sUrl = sUrl + (sUrl.charAt(sUrl.length - 1) === '/' ? '' : '/') + encodeURIComponent(this.get("id"));
                    }
                    return sUrl;
                },
                sync: function () {
                    console.log("Calipso.Model#sync");
                    // apply partial update hints
                    if (!this.isNew()) {
                        var changed = this.changedAttributes();
                        if (changed != false) {
                            this.set("changedAttributes", _.keys(changed));
                        }
                    }
                    return Backbone.Model.prototype.sync.apply(this, arguments);
                },
                isPublic: function () {
                    return this.constructor.isPublic(this);
                },
                getUseCaseContext: function (options) {
                    options.model = this;
                    return this.constructor.getUseCaseContext(options);
                },
                hasUseCase: function (key) {
                    return this.constructor.hasUseCase(key);
                },
                getFields: function () {
                    return this.constructor.getFields();
                },
                /**
                 * Retusn true if the model is just a search collection wrapper, false otherwise
                 */
                isSearchModel: function () {
                    return this.wrappedCollection ? true : false;
                },
                getLabels: function () {
                    return this.constructor.getLabels(this);
                },
                getBaseFragment: function () {
                    return this.constructor.getBaseFragment(this);
                },
                /**
                 * Get the URL path fragment for this model. Calls the prototype method with the same name.
                 * @returns the URL path fragment as a string
                 */
                getPathFragment: function () {
                    return this.constructor.getPathFragment();
                },
                /**
                 *  Check if the model wants search result collections of it's type to be cached.
                 *  Calls the prototype method with the same name.
                 */
                isCollectionCacheable: function () {
                    return this.constructor.isCollectionCacheable && this.constructor.isCollectionCacheable();
                },
                getTypeaheadSource: function (options) {
                    return this.constructor.getTypeaheadSource(options);
                },
            }, {
                // static members
                /** (Default) 0Do not retrieve the form schema from the server */
                FORM_SCHEMA_CACHE_CLIENT: "FORM_SCHEMA_CACHE_CLIENT",
                /** Retrieve the form schema only once for all model instances */
                FORM_SCHEMA_CACHE_STATIC: "FORM_SCHEMA_CACHE_STATIC",
                /** Retrieve the form schema only once per model instance */
                FORM_SCHEMA_CACHE_INSTANCE: "FORM_SCHEMA_CACHE_INSTANCE",
                /** Retrieve the form schema every time it is accessed */
                FORM_SCHEMA_CACHE_NONE: "FORM_SCHEMA_CACHE_NONE",
                formSchemaCacheMode: this.FORM_SCHEMA_CACHE_CLIENT,
                typeName: "Calipso.Model",
                superClass: null,
                labelIcon: "fa fa-list fa-fw",
                public: false,
                nameProperty: "name",
                baseFragment: '/api/rest/',
                typeaheadSources: {},
                menuConfig: {
                    rolesIncluded: ["ROLE_ADMIN", "ROLE_SITE_OPERATOR"],
                    rolesExcluded: null,
                },
                /**
                 * Returns whether the model is public
                 * @returns {boolean}
                 */
                isPublic: function () {
                    return this.public || false;
                },
                create: function (attrs, options) {
                    var modelAttributes = attrs;

                    if (options && options.httpParams) {
                        var params = _.isString(options.httpParams) ? Calipso.getHttpUrlParams(options.httpParams) : options.httpParams;
                        _.extend(modelAttributes, params);
                    }
                    var model = new this(modelAttributes, options);
                    if (!modelAttributes.id && this.getTypeName() != "Calipso.model.UserDetailsModel") {
                        var collectionOptions = {
                            model: this,
                            url: Calipso.getBaseUrl() + this.baseFragment + this.getPathFragment(),
                        };
                        if (options.httpParams) {
                            collectionOptions.data = options.httpParams;
                        }
                        // create a model to use as a wrapper for a collection of
                        // instances of the same type, fill it with any given search criteria
                        model.wrappedCollection = Calipso.util.cache.getCollection(collectionOptions);
                    }
                    return model;
                },
                isPublic: function () {
                    return this.public;
                },
                isCollectionCacheable: function () {
                    return false;
                },
                getBaseFragment: function () {
                    return this.baseFragment;
                },
                /**
                 * Get the path fragment of this class
                 * @returns the the path fragment as a string
                 */
                getPathFragment: function (instance) {
                    return this.pathFragment;
                },
                getLabels: function (instance) {
                    var labels = this.superClass && this.superClass.getLabels ? this.superClass.getLabels() : {};
                    labels = Calipso.deepExtend(labels, Calipso.getPathValue(Calipso.labels, "models." + this.getPathFragment(), {}));
                    return labels;
                },
                // TODO: refactor view to region names to
                // allow multiple views config peer layout
                fields: {},
                useCases: {
                    view: {
                        view: Calipso.view.BrowseLayout,
                        viewOptions: {
                            closeModalOnSync: true,
                            formTemplatesKey: "horizontal",
                        }
                    },
                    create: {
                        view: Calipso.view.BrowseLayout,
                        viewOptions: {
                            closeModalOnSync: true,
                            formTemplatesKey: "horizontal",
                        }
                    },
                    update: {
                        view: Calipso.view.BrowseLayout,
                        viewOptions: {
                            closeModalOnSync: true,
                            formTemplatesKey: "horizontal",
                        }
                    },
                    search: {
                        view: Calipso.view.UseCaseSearchLayout,
                        viewOptions: {
                            formTemplatesKey: "vertical",
                        },
                        overrides: {
                            formRegion: {
                                viewOptions: {
                                    className: "card"
                                }
                            },
                            contentRegion: {
                                viewOptions: {
                                    className: "card"
                                }
                            },
                        }
                    },
                },
                _getUseCaseConfig: function (key) {
                    // get superclass config
                    var useCaseConfig = this.superClass && this.superClass._getUseCaseConfig ? this.superClass._getUseCaseConfig(key) : {};
                    // apply own config
                    var ownConfig = $.isFunction(this.useCases) ? this.useCases()[key] : this.useCases[key];
                    if ($.isFunction(ownConfig)) {
                        ownConfig = ownConfig();
                    }
                    ownConfig && Calipso.deepExtend(useCaseConfig, ownConfig);
                    return useCaseConfig;
                },
                getUseCaseContext: function (options) {
                    var useCaseConfig = this._getUseCaseConfig(options.key);
                    Calipso.deepExtend(useCaseConfig.viewOptions, options.viewOptions);
                    // setup a model instance if needed
                    useCaseConfig.model = options.model ? options.model : this.create({
                        id: options.modelId
                    }, {
                        httpParams: options.httpParams
                    });

                    useCaseConfig.factory = this;
                    useCaseConfig.addToCollection = options.addToCollection;
                    useCaseConfig.key = options.key;
                    useCaseConfig.pathFragment = this.getPathFragment();
                    return new Calipso.UseCaseContext(useCaseConfig);
                },
                hasUseCase: function (key) {
                    var has = false;
                    if (this.useCases[key] || (this.superClass && this.superClass.hasUseCase && this.superClass.hasUseCase(key))) {
                        has = true;
                    }
                    return has;
                },
                getFields: function () {

                    var fields = this.superClass && this.superClass.getFields ? this.superClass.getFields() : {};
                    var ownFields = this.fields || {};
                    if ($.isFunction(ownFields)) {
                        ownFields = this.fields();
                    }
                    else {
                        ownFields = _.clone(this.fields);
                    }
                    Calipso.deepExtend(fields, ownFields);
                    return fields;
                },
                getFieldNames: function () {
                    var fieldNames = [];
                    _.each(this.fields, function (field, key) {
                        fieldNames.push(key);
                    });
                    return fieldNames;
                },
                getTypeaheadSource: function (options) {
                    var _this = this;
                    var config = {
                        query: "?name=%25wildcard%25",
                        wildcard: "wildcard",
                        pathFragment: _this.getPathFragment(),
                    };
                    _.extend(config, options);
                    var sourceKey = config.pathFragment + config.wildcard + config.query;
                    // if not lready created
                    if (!_this.typeaheadSources[sourceKey]) {
                        var sourceUrl = Calipso.getBaseUrl() + this.baseFragment + config.pathFragment + config.query;
                        var bloodhound = new Bloodhound({
                            remote: {
                                url: sourceUrl,
                                wildcard: config.wildcard,
                                transform: function (response) {
                                    return response.content;
                                }
                            },
                            identify: function (obj) {
                                return obj.id;
                            },
                            queryTokenizer: Bloodhound.tokenizers.whitespace,
                            datumTokenizer: function (d) {
                                return Bloodhound.tokenizers.whitespace(d.name);
                            },
                        });

                        bloodhound.initialize();
                        _this.typeaheadSources[sourceKey] = bloodhound.ttAdapter();
                    }

                    return _this.typeaheadSources[sourceKey];
                },
            });

    });