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
define(
    ["lib/restdudelib/util", 'underscore', 'handlebars', 'backbone', 'marionette', 'moment', 'backbone-forms', 'backgrid', 'alpaca'],
    function (Restdude, _, Handlebars, Backbone, BackboneMarionette, moment, BackboneForms, Backgrid, Alpaca) {

        var Marionette = Backbone.Marionette;

        // The recursive tree view
        //	var TreeView = Backbone.Marionette.CompositeView.extend({
        //	    template: "#node-template",
        //
        //	    tagName: "li",
        //
        //	    initialize: function(){
        //	        // grab the child collection from the parent model
        //	        // so that we can render the collection as children
        //	        // of this parent node
        //	        this.collection = this.model.nodes;
        //	    },
        //
        //	    appendHtml: function(cv, iv){
        //	        cv.$("ul:first").append(iv.el);
        //	    },
        //	    onRender: function() {
        //	        if(_.isUndefined(this.collection)){
        //	            this.$("ul:first").remove();
        //	        }
        //	    }
        //	});
        //


        Restdude.view.TemplateBasedItemView = Restdude.view.View.extend(
            /** @lends Restdude.view.TemplateBasedItemView.prototype */
            {
                template: Restdude.getTemplate("templateBasedItemView"),//_.template('{{#if url}}<a href="{{url}}">{{/if}}{{#if name}}<h5>{{name}}</h5>{{else}}{{#if title}}<h5>{{title}}</h5>{{/if}}{{/if}}{{#if description}}{{description}}{{/if}}{{#if url}}</a>{{/if}}'),
                tagName: "li",
                initialize: function (models, options) {
                    Restdude.view.View.prototype.initialize.apply(this, arguments);
                },
                attributes: function () {
                    return this.getOption("attributes");
                },
                getTemplate: function () {
                    return this.getOption("template");
                },
            }, {
                typeName: "Restdude.view.TemplateBasedItemView",
            });


        Restdude.view.NotFoundView = Restdude.view.View.extend(
            /** @lends Restdude.view.NotFoundView.prototype */
            {
                className: 'container span8 home',
                template: Restdude.getTemplate('notfound')
            },
            // static members
            {
                typeName: "Restdude.view.NotFoundView",
            });
        Restdude.view.FooterView = Marionette.View.extend(
            /** @lends Restdude.view.FooterView.prototype */
            {
                className: "container",
                template: Restdude.getTemplate('footer'),
                className: "col-sm-12"
            },
            // static members
            {
                typeName: "Restdude.view.FooterView",
            });

        Restdude.view.UseCaseItemView = Restdude.view.View.extend({

            initialize: function (options) {
                Restdude.view.View.prototype.initialize.apply(this, arguments);
            },
            getSchemaType: function () {
                return this.constructor.getSchemaType();
            },
            onBeforeRender: function () {
                //TODO: roles, masks, nested usecases
                this.schema = this.buildSchema();
            },
            /** builds a UI-specific schema for the given fields */
            buildSchema: function () {
                var _this = this, schemaType = this.getSchemaType();
                var isArray = schemaType == "backgrid", schema = null;

                var fields = this.fields || this.useCaseContext.getFields();
                if (fields) {
                    var schemaEntry, baseSchemaEntry, overrideSchemaEntry;

                    schema = isArray ? [] : {};
                    _.each(fields, function (field, key) {
                        // type and instance schemas
                        baseSchemaEntry = _.isObject(field.fieldType)
                            ? field.fieldType[schemaType]
                            : Restdude.fields[field.fieldType][schemaType];

                        overrideSchemaEntry = field[schemaType];

                        // if a schema entry exists, add it
                        if (baseSchemaEntry || overrideSchemaEntry) {

                            // merge to new object
                            schemaEntry = Restdude.deepExtend(
                                {pathFragment: field.pathFragment},
                                (baseSchemaEntry || {}),
                                (overrideSchemaEntry || {}));

                            //TODO: also labelPropertyCopy
                            if (_this.keyPropertyCopy) {
                                schemaEntry[_this.keyPropertyCopy] = key;
                            }
                            if (_this.labelPropertyCopy) {
                                schemaEntry[_this.labelPropertyCopy] = field.label;
                            }

                            // if expected schema is of type array, push
                            if (isArray) {
                                schema.push(schemaEntry);
                            }// if expected schema is of type objet, add
                            else {
                                schema[key] = schemaEntry;
                            }
                        }

                    });
                }
                return schema;
            },
        }, {
            typeName: "Restdude.view.UseCaseItemView",
            schemaType: null,
            getSchemaType: function () {
                return this.schemaType;
            },
        });

        Restdude.view.UseCaseGridView = Restdude.view.UseCaseItemView.extend(
            /**
             * @param options object members:
             *  - collection or model.wrappedCollection
             *  - callCollectionFetch: whether to fetch the collection from the server
             * @lends Restdude.view.UseCaseGridView.prototype
             * */
            {
                keyPropertyCopy: "name",
                labelPropertyCopy: "label",

                collection: null,
                backgrid: null,
                // Define view template
                template: Restdude.getTemplate('UseCaseGridView'),
                events: {
                    "click button.btn-windowcontrols-destroy": "back"
                },
                templateContext: {
                    viewTitle: function () {
                        var title = this.model.labelIcon || this.model.constructor.labelIcon || "";
                        if (title) {
                            title = "<i class=\"" + Restdude.stripHtml(title) + "\" aria-hidden=\"true\"> </i> ";
                        }
                        title += Restdude.getPathValue(this.model.getLabels(), "plural.label", Restdude.util.getLabel("restdude.words.results"));
                        return title;
                    }
                },
                back: function (e) {
                    Restdude.stopEvent(e);
                    window.history.back();
                },
                initialize: function (options) {
                    Restdude.view.UseCaseItemView.prototype.initialize.apply(this, arguments);

                    this.collection = options.collection || options.model.wrappedCollection;
                    if (!this.collection) {
                        throw "no collection or collection wrapper model was provided";
                    }

                    if (options.callCollectionFetch) {
                        this.callCollectionFetch = options.callCollectionFetch;
                    }
                },
                onGridRendered: function () {

                },
                onCollectionFetched: function () {

                },
                onCollectionFetchFailed: function () {

                },
                onRender: function () {
                    var _self = this;
                    this.backgrid = new Restdude.components.backgrid.Grid({
                        columns: _self.schema,
                        collection: _self.collection,
                    });
                    var $container = this.$el.find(".backgrid-table-container");
                    if ($container.length == 0) {
                        $container = this.$el;
                    }
                    $container.append(this.backgrid.render().$el);
                    var paginator = new Backgrid.Extension.Paginator({
                        windowSize: 10, // Default is 10
                        slideScale: 0.25, // Default is 0.5
                        goBackFirstOnSort: false, // Default is true
                        collection: _self.collection
                    });
                    this.$el.append('<div class="card-block backgrid-paginator-container"> </div>');
                    this.$el.find('.backgrid-paginator-container:first').append(paginator.render().el);
                    this.onGridRendered();

                    if (this.callCollectionFetch) {
                        var fetchOptions = {
                            reset: true,
                            url: _self.collection.url,
                            success: function () {
                                _self.onCollectionFetched();
                                _self.$(".loading-indicator").hide();
                                _self.$(".loading-indicator-back").hide();
                            },
                            error: function () {
                                _self.onCollectionFetchFailed();
                                _self.$(".loading-indicator").hide();
                                _self.$(".loading-indicator-back").hide();
                            }

                        };
                        if (_self.collection.data) {
                            if (_self.collection.data[""] || _self.collection.data[""] == null) {
                                delete _self.collection.data[""];
                            }
                            fetchOptions.data = _self.collection.data;
                            //
                        }
                        _self.collection.fetch(fetchOptions);
                        this.callCollectionFetch = false;
                    } else {
                        _self.$(".loading-indicator").hide();
                        _self.$(".loading-indicator-back").hide();
                    }

                },

            },
            // static members
            {
                typeName: "Restdude.view.UseCaseGridView",
                schemaType: "backgrid",
            });


        /*
         * Use-case driven form view.
         * @fires UseCaseFormView#model:sync when the model is persisted successfully
         */
        Restdude.view.JsonSchemaView = Restdude.view.UseCaseItemView.extend({
                template: Restdude.getTemplate('JsonSchemaView'),
                initialize: function (options) {
                    console.log("JsonSchemaView initialize, options: ");
                    console.log(options);
                    Restdude.view.UseCaseItemView.prototype.initialize.apply(this, arguments);
                },
                buildSchema: function () {
                    return this.options.jsonSchema;
                },
                onRender: function () {
                    console.log("JsonSchemaView onRender, model: ");
                    console.log(this.model.attributes);
                    console.log("JsonSchemaView onRender,container: ");
                    var container = this.$el.find("div.jsonSchemaView:first");
                    console.log(container);
                    console.log("JsonSchemaView onRender, schema: ");
                    console.log(this.schema);
                    container.alpaca({
                        "data": this.model.attributes,
                        "schema": this.schema,
                        //"options" : {Object} field options (optional),
                        "view": "bootstrap-edit-horizontal",//{Object|String} field view (object or id reference) (optional),
                        //"render": {Function} callback function for replacing default rendering method (optional),
                        //"postRender": {Function} callback function for post-rendering  (optional),
                        //"error": {Function} callback function for error handling  (optional),
                        //"connector": {Alpaca.Connector} connector for retrieving or storing data, schema, options, view and templates. (optional)
                    });
                }
            },
            // static members
            {
                typeName: "Restdude.view.JsonSchemaView",
            });
        /*
         * Use-case driven form view.
         * @fires UseCaseFormView#model:sync when the model is persisted successfully
         */
        Restdude.view.UseCaseFormView = Restdude.view.UseCaseItemView.extend({
            /**
             *
             * @param  {[String]} the templates key, usually one of {"horizontal", "inline", "vertical"}
             * @return {[type]} the compiled templates (form, field) from Restdude.util.formTemplates
             */
            getFormTemplates: function (templatesKey) {
                templatesKey || (templatesKey = this.formTemplatesKey || "vertical");
                return this.constructor.getFormTemplates(templatesKey)
            },

            //keyPropertyCopy : "name",
            labelPropertyCopy: "title",
            modal: false,
            hasDraft: false,
            // Define view template
            formTitle: "options.formTitle",
            template: Restdude.getTemplate('UseCaseFormView'),
            mergableOptions: ["addToCollection", "submitButton", "formTemplatesKey", "formTemplates", 'fieldsInitiallyShown', "formControlSize", "placeHolderLabelsOnly"],
            events: {
                "click button.submit": "commit",
                "submit form": "commit",
                "click button.cancel": "cancel",
                "submit": "commitOnEnter",
                "keypress input[type=password]": "commitOnEnter",
                "keypress input[type=text]": "commitOnEnter",
                "click .addLazyField": "addLazyField",
            },
            templateContext: {
                viewTitle: function () {
                    var title = "";
                    if (this.options.title) {
                        title = this.options.title;
                    }
                    else {
                        title = "<i class=\"fa fa-search\" aria-hidden=\"true\"></i>";
                        if (this.useCaseContext.key.indexOf("search") == 0) {
                            title += Restdude.util.getLabel("restdude.words.searchFor");
                        }
                        else if (this.useCaseContext.key.indexOf("create") == 0) {
                            title += Restdude.util.getLabel("restdude.words.new") + " " + Restdude.getPathValue(this.model.getLabels(), "singular.label", "");
                        }
                        else if (this.useCaseContext.key.indexOf("update") == 0) {
                            title += Restdude.util.getLabel("restdude.words.edit") + " " + this.model.toHtmlSafeString();
                        }
                        else {
                            title += this.model.toHtmlSafeString();
                        }
                    }


                    return title;
                }
            },
            addLazyField: function (e) {
                var fieldKey = $(e.currentTarget).data("field");
                this.form.renderLazyField(fieldKey);
            },
            initialize: function (options) {
                Restdude.view.UseCaseItemView.prototype.initialize.apply(this, arguments);
                this.searchResultsCollection = this.model.wrappedCollection;
                // get the form/field templates
                if (!this.formTemplates) {
                    this.formTemplates = this.getFormTemplates();
                }

            },
            commitOnEnter: function (e) {
                if (e.keyCode != 13) {
                    return;
                } else {
                    this.commit(e);
                }
            },
            isFormValid: function () {
                var isValid = false;
                if (window.Placeholders) {
                    Placeholders.disable();
                }
                var errors = this.form.commit({
                    validate: true
                });
                if (errors) {
                    var errorMsg = "" + errors._others;
                    if (window.Placeholders) {
                        Placeholders.enable();
                    }
                    if (this.modal) {
                        $(".modal-body").scrollTop(0);
                    }
                } else {
                    isValid = true;
                }

                return isValid;
            },
            removeDraft: function (draftKey) {
                draftKey || (draftKey = this.getDraftKey());
                sessionStorage.removeItem(draftKey);
                this.hasDraft = false;
            },
            saveDraft: function (draftKey) {
                draftKey || (draftKey = this.getDraftKey());
                // keep a session draft of changes
                var draft = this.form.getDraft();
                delete draft.currentStepIndex;
                sessionStorage.setItem(draftKey, JSON.stringify(draft));
                this.hasDraft = true;
            },
            getDraft: function (draftKey) {
                draftKey || (draftKey = this.getDraftKey());
                var draft = sessionStorage.getItem(draftKey);
                return draft;
            },
            commit: function (e) {
                var _this = this;
                Restdude.stopEvent(e);
                if (!this.isFormValid()) {
                    return false;
                }
                // if no validation errors
                else {
                    // Case: create/update
                    if (_this.useCaseContext.key.indexOf("search") != 0) {
                        // persist changes
                        var draftKey = _this.getDraftKey();
                        _this.stopListening(this.form, 'change');
                        _this.model.save(null, {
                            success: function (model, response) {
                                if (_this.enableDrafts) {
                                    _this.removeDraft();
                                    _this.removeDraft(draftKey);
                                }
                                // trigger model:sync
                                _this.triggerMethod("model:sync", {
                                    model: _this.model,
                                    view: _this,
                                    collection: _this.collection
                                });
                            },
                            error: function () {
                                alert("Failed persisting changes");
                            }
                        });
                    } else {
                        // Case: search
                        var newData = this.form.toJson();
                        if (newData) {

                        }
                        this.searchResultsCollection.data = newData;
                        this.searchResultsCollection.fetch({
                            reset: true,
                            data: newData,
                            success: function () {
                                // signal successful retreival of search results
                                // for the currently active layout to handle presentation
                                //
                                //Restdude.vent.trigger("genericFormSearched", _this.model);
                            },

                            // Generic error, show an alert.
                            error: function (model, response) {
                                alert("Failed retreiving search results");
                            }

                        });

                    }
                }
                // search entities?
            },
            cancel: function () {
                console.log("cancel");
                window.history.back();
            },
            onRender: function () {
                var _self = this;
                // get appropriate schema
                var formSchema = this.schema;
                // TODO: add a property in generic model to flag view behavior (i.e. get add http:.../form-schema to the model before rendering)
                if (formSchema) {
                    _self.renderForm(formSchema);
                } else {
                    var fetchScemaUrl = Restdude.getBaseUrl() + "/" + _self.model.getPathFragment() + '/' + (_self.model.isNew() ? "new" : _self.model.get(Restdude.config.idAttribute));

                    _self.model.fetch({
                        url: fetchScemaUrl,
                        success: function (model, response, options) {
                            _self.renderForm();
                        },
                        error: function (model, response, options) {
                            alert("Error fetching model from server");
                        }
                    });
                }

            },
            renderForm: function (formSchema) {
                var _self = this;

                var formSubmitButton = this.submitButton;
                if (!formSubmitButton) {
                    formSubmitButton = _self.model.getFormSubmitButton ? _self.model.getFormSubmitButton() : false;
                }
                if (!formSubmitButton) {
                    if (_self.useCaseContext.key.indexOf("search") == 0) {
                        formSubmitButton = "<i class=\"glyphicon glyphicon-search\"></i>&nbsp;Search";
                    } else if (_self.useCaseContext.key.indexOf("create") == 0 || _self.useCaseContext.key.indexOf("update") == 0) {
                        formSubmitButton = "<i class=\"fa fa-floppy-o\"></i>&nbsp;Save";
                    } else {
                        formSubmitButton = "Submit";
                    }
                }
                //;(_self.formSchemaKey);

                // render form
                if (Restdude.session.searchData && (!_self.searchResultsCollection.data)) {
                    _self.model.set(Restdude.session.searchData);
                    _self.searchResultsCollection.data = Restdude.session.searchData;
                }
                else if (this.enableDrafts) {
                    var draft = _self.getDraft();
                    if (draft) {
                        draft = JSON.parse(draft);
                        _self.model.set(draft);
                    }
                }
                var formOptions = {
                    model: _self.model,
                    schema: formSchema,
                    formClassName: _self.formTemplates.formClassName,
                    template: _self.formTemplates.form,
                    fieldTemplate: _self.formTemplates.field,
                    fieldsetTemplate: _self.formTemplates.fieldset,
                    fieldsInitiallyShown: _self.fieldsInitiallyShown,
                    formControlSize: _self.formControlSize,
                    placeHolderLabelsOnly: _self.placeHolderLabelsOnly,
                };

                // model driven submit button?
                if (formSubmitButton) {
                    formOptions.submitButton = formSubmitButton;
                }
                this.form = new Restdude.backboneform.Form(formOptions);
                //this.$el.append(this.form.el);
                this.form.setElement(this.$el.find("form:first").first()).render();

                // flag changed
                if (this.enableDrafts) {
                    this.listenTo(this.form, 'change', function () {
                        _self.hasDraft = true;
                    });
                }

                // proxy model events to parent layout
                this.listenTo(this.form, "all", function (eventName) {
                    _self.triggerMethod("form:" + eventName, {
                        model: _self.model,
                        view: _self,
                        collection: _self.collection
                    });
                });
            },
            onDomRefresh: function () {
                this.$el.find('input, select').filter(':visible:enabled:first').focus();
                this.onFormRendered();
            },
            onFormRendered: function () {

            },
            getFormData: function getFormData($form) {
                var unindexed_array = $form.serializeArray();
                var indexed_array = {};

                $.map(unindexed_array, function (n, i) {
                    indexed_array[n['name']] = n['value'];
                });

                return indexed_array;
            },
            getDraftKey: function () {
                return this.model.getPathFragment() + '/' + this.model.get(Restdude.config.idAttribute) + '/' + this.formSchemaKey;
            },
            onBeforeDestroy: function (e) {
                this.hasDraft && this.saveDraft();
                this.form && this.form.close();
            },
        }, {
            // static members
            typeName: "Restdude.view.UseCaseFormView",
            schemaType: "form",
            /**
             * Returns a Backbone.Form template
             * @param  {[Restdude.view.UseCaseFormView]} the form view instance
             * @param  {[String]} the template key, usually one of {"horizontal", "inline", "vertical"}
             * @return {[type]} the compiled templates e.g. (form : t1, field : t2)
             */
            getFormTemplates: function (templatesKey) {
                return Restdude.util.formTemplates[templatesKey];
            },
        });

        Restdude.view.SearchBoxFormView = Restdude.view.UseCaseFormView.extend({
            buildSchema: function () {
                var _this = this, typeAheadSources = [],
                    schema = Restdude.view.UseCaseFormView.prototype.buildSchema.apply(this, arguments);

                console.log("SCHEMA: ");
                console.log(schema);
                // hide givne fields and use them to set the searchbox data sources
                var label, fields = [];
                _.each(schema, function (field, key) {
                    console.log("hiding field: " + key);
                    field.type = "Hidden";
                    label = field.title || field.titleHTML;
                    fields.push(key);
                    typeAheadSources.push({
                        displayKey: key,
                        name: key,
                        source: _this.model.getTypeaheadSource({
                            query: "?properties=" + key + "&direction=ASC&" + key + "=%25wildcard%25"
                        }),
                        limit: 30,
                        //templates: {
                        //	header: '<strong class="tt-tag-heading tt-tag-heading2">' + label + '</strong>',
                        //	empty: '<i class="tt-tag-heading tt-tag-heading2">' + label + ': none</i>'
                        //},
                        template: Restdude.util.formTemplates.vertical.field
                    })
                });
                // add searchbox
                schema.searchBox = {
                    type: Restdude.backboneform.SearchBox,
                    typeaheadSource: typeAheadSources,
                    fields: fields,
                };
                console.log("SCHEMA new: ");
                console.log(schema);
                return schema;
            }
        });


        Restdude.view.UserProfileView = Restdude.view.View.extend({
                template: Restdude.getTemplate('userProfile'),
            },
            // static members
            {
                typeName: "Restdude.view.UserProfileView",
            });

        Restdude.collection.TabCollection = Backbone.Collection.extend({
                initialize: function () {
                    if (!Restdude.model.TabModel) {
                        Restdude.model.TabModel = Restdude.model.GenericModel.extend({
                            getPathFragment: function () {
                                return null;
                            }
                        });

                        Restdude.model.TabModel.getTypeName = function (instance) {
                            return "TabModel";
                        };
                    }
                    this.model = Restdude.model.GenericModel;
                    this.listenTo('add', this.onModelAdded, this);
                    this.listenTo('remove', this.onModelRemoved, this);
                },
                onModelAdded: function (model, collection, options) {
                    //_self.tabKeys[model.get(Restdude.config.idAttribute] = model;
                },
                onModelRemoved: function (model, collection, options) {
                    //_self.tabKeys[model.get(Restdude.config.idAttribute] = null;
                },
            },
            // static members
            {
                typeName: "Restdude.view.TabCollection",
            });

        Restdude.view.TabLabelsCollectionView = Backbone.Marionette.CollectionView.extend({
                className: 'nav nav-pills',
                tagName: 'ul',
                itemTemplate: Restdude.getTemplate('tab-label'),
                childViewContainer: '.nav-tabs',
                initialize: function (options) {
                    Marionette.CollectionView.prototype.initialize.apply(this, arguments);

                },
                getItemView: function (item) {
                    var _this = this;
                    return Backbone.Marionette.View.extend({
                        tagName: 'li',
                        className: 'restdude-tab-label',
                        id: "restdude-tab-label-" + item.get(Restdude.config.idAttribute),
                        template: _this.itemTemplate,
                        events: {
                            "click .show-tab": "viewTab",
                            "click .destroy-tab": "destroyTab"
                        },
                        /**
                         this.listenTo(Restdude.vent, "layout:viewModel", function(itemModel) {
					_this.showItemViewForModel(itemModel, "view")
				}, this);
                         */
                        viewTab: function (e) {
                            Restdude.stopEvent(e);
                            RestdudeApp.vent.trigger("viewTab", this.model);
                        },
                        destroyTab: function (e) {
                            Restdude.stopEvent(e);
                            //					this.model.collection.remove(this.model);
                            this.destroy();
                            RestdudeApp.vent.trigger("viewTab", {
                                id: "Search"
                            });
                        },
                    });
                },
            },
            // static members
            {
                typeName: "Restdude.view.TabLabelsCollectionView",
            });

        Restdude.view.TabContentsCollectionView = Backbone.Marionette.CollectionView.extend({
                tagName: 'div',
                getItemView: function (item) {
                    var someItemSpecificView = item.getItemViewType ? item.getItemViewType() : null;
                    if (!someItemSpecificView) {
                        someItemSpecificView = Restdude.view.UseCaseFormView;
                    }
                    return someItemSpecificView;
                },
                buildItemView: function (item, ItemViewClass) {

                    var options = {
                        model: item
                    };
                    if (item && item.wrappedCollection) {
                        options.searchResultsCollection = item.wrappedCollection;
                    }
                    // do custom stuff here

                    var view = new ItemViewClass(options);

                    // more custom code working off the view instance

                    return view;
                },
            },
            // static members
            {
                typeName: "Restdude.view.TabContentsCollectionView",
            });

        return Restdude;
    });
