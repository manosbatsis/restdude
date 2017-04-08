/*
 *
 * Restdude
 * -------------------------------------------------------------------
 *
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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * @exports Restdude.Model
 */
define(['jquery', 'underscore', 'bloodhound', 'typeahead', "lib/restdudelib/util", "lib/restdudelib/form",
        "lib/restdudelib/uifield", "lib/restdudelib/backgrid", "lib/restdudelib/view", 'handlebars'],
    function ($, _, Bloodhoud, Typeahead, Restdude, RestdudeForm, RestdudeField, RestdudeGrid, RestdudeView, Handlebars) {

        /**
         * A base model implementation to extend for your own models.
         * Provides usecase metadata as a means to declaratively define view
         * hierarchies. that may also map to dynamic (i.e. non-explicit)
         * controller routes.
         * @alias module:Restdude.Model
         * @constructor
         * @augments module:Backbone.Model
         */
        Restdude.Model = Restdude.model.Model.extend(
            /** @lends module:Restdude.Model.prototype */
            {
                skipDefaultSearch: false,

                initialize: function () {
                    Restdude.model.Model.prototype.initialize.apply(this, arguments);
                },
                toString: function () {
                    return this.get(this.constructor.nameProperty) || this.get("name") || this.get(Restdude.config.idAttribute);
                },
                toHtmlSafeString: function () {
                    return Restdude.stripHtml(this.toString());
                },
                /**
                 * Returns the URL for this model, giving precedence  to the collection URL if the model belongs to one,
                 * or a URL based on the model path fragment otherwise.
                 */
                url: function () {
                    var sUrl = this.collection && _.result(this.collection, 'url')
                        ? _.result(this.collection, 'url') : Restdude.getBaseUrl() + this.getBaseFragment() + this.getPathFragment() /*_.result(this, 'urlRoot')*/ || urlError();

                    var ident = this.get("id") || this.get("pk");
                    if (ident) {
                        sUrl = sUrl + (sUrl.charAt(sUrl.length - 1) === '/' ? '' : '/') + encodeURIComponent(ident);
                    }
                    return sUrl;
                },
                sync: function () {
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
                typeName: "Restdude.Model",
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
                create: function (modelAttributes, options) {
                    var httpParams;
                    if (options && options.httpParams) {
                        httpParams = options.httpParams;
                        delete options.httpParams;
                        var params = _.isString(httpParams) ? Restdude.getHttpUrlParams(httpParams) : httpParams;
                        _.extend(modelAttributes, params);

                    }

                    var model = new this(modelAttributes, options);
                    if ((!model.get("pk") && !model.get("id")) && this.getTypeName() != "Restdude.model.UserDetailsModel") {

                        var collectionOptions = {
                            model: this,
                            url: Restdude.getBaseUrl() + this.baseFragment + this.getPathFragment(),
                        };
                        if (httpParams) {
                            collectionOptions.data = httpParams;
                        }
                        // create a model to use as a wrapper for a collection of
                        // instances of the same type, fill it with any given search criteria
                        model.wrappedCollection = Restdude.util.cache.getCollection(collectionOptions);
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
                    labels = Restdude.deepExtend(labels, Restdude.getPathValue(Restdude.labels, "models." + this.getPathFragment(), {}));
                    return labels;
                },
                // TODO: refactor view to region names to
                // allow multiple views config peer layout
                fields: {},
                useCases: {
                    view: {
                        //view: Restdude.view.JsonSchemaLayout,
                        view: Restdude.view.BrowseLayout,
                        viewOptions: {
                            closeModalOnSync: true,
                            formTemplatesKey: "horizontal",
                        }
                    },
                    create: {
                        view: Restdude.view.BrowseLayout,
                        viewOptions: {
                            closeModalOnSync: true,
                            formTemplatesKey: "horizontal",
                        }
                    },
                    update: {
                        view: Restdude.view.BrowseLayout,
                        viewOptions: {
                            closeModalOnSync: true,
                            formTemplatesKey: "horizontal",
                        }
                    },
                    search: {
                        view: Restdude.view.UseCaseSearchLayout,
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
                    var useCaseConfig = (this.superClass && this.superClass._getUseCaseConfig
                        ? this.superClass._getUseCaseConfig(key)
                        : {}) || {};
                    useCaseConfig = $.extend(true, {}, useCaseConfig);
                    // apply own config
                    var ownConfig = ($.isFunction(this.useCases) ? this.useCases()[key] : this.useCases[key]) || {};
                    if ($.isFunction(ownConfig)) {
                        ownConfig = ownConfig();
                    }

                    Restdude.deepExtend(useCaseConfig, ownConfig);
                    return useCaseConfig;
                },
                getUseCaseContext: function (options) {
                    var useCaseConfig = this._getUseCaseConfig(options.key);
                    useCaseConfig.viewOptions = Restdude.deepExtend({},
                        (useCaseConfig.viewOptions || {}),
                        (options.viewOptions || {}));

                    // setup a model instance if needed
                    if(options.model){
                        useCaseConfig.model = options.model;
                    }
                    else{
                        useCaseConfig.model = this.create({
                            id: options.modelId
                        }, {
                            httpParams: options.httpParams
                        });
                    }

                    useCaseConfig.factory = this;
                    useCaseConfig.addToCollection = options.addToCollection;
                    useCaseConfig.key = options.key;
                    useCaseConfig.pathFragment = this.getPathFragment();
                    return new Restdude.UseCaseContext(useCaseConfig);
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
                    Restdude.deepExtend(fields, ownFields);
                    return fields;
                },
                getFieldNames: function () {
                    var fieldNames = [];
                    _.each(this.fields, function (field, key) {
                        fieldNames.push(key);
                    });
                    return fieldNames;
                },
                typeaheadQuery: "?name=%25wildcard%25",
                getTypeaheadSource: function (options) {
                    var _this = this;
                    var config = {
                        query: _this.typeaheadQuery,
                        wildcard: "wildcard",
                        pathFragment: _this.getPathFragment(),
                    };
                    _.extend(config, options);
                    var sourceKey = config.pathFragment + config.wildcard + config.query;
                    // if not lready created
                    if (!_this.typeaheadSources[sourceKey]) {
                        var sourceUrl = Restdude.getBaseUrl() + this.baseFragment + config.pathFragment + config.query;
                        var bloodhound = new Bloodhound({
                            remote: {
                                url: sourceUrl,
                                // replace multiple occurences of wildcard
                                prepare: function(query, settings) {
                                    settings.url = settings.url.split(config.wildcard).join(encodeURIComponent(query));
                                    return settings;
                                },
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