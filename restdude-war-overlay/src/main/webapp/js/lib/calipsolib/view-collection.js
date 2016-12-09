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
define(
    ["lib/calipsolib/view-item", 'underscore', 'handlebars', 'backbone', 'marionette', 'moment'],
    function (Calipso, _, Handlebars, Backbone, Marionette, moment) {


        Calipso.view.TemplateBasedCollectionView = Marionette.CompositeView.extend(
            /** @lends Calipso.view.TemplateBasedCollectionView.prototype */
            {
                //template : Calipso.getTemplate("templateBasedCollectionView"),//_.template('<div id="calipsoTemplateBasedCollectionLayout-collectionViewRegion"></div>'),
                tagName: "ul",
                attributes: {},
                template: _.template(''),
                childView: Calipso.view.TemplateBasedItemView,
                pollCollectionAfterDestroy: false,
                childViewOptions: {
                    tagName: "li",
                },
                attributes: function () {
                    return this.getOption("attributes");
                },
                getTemplate: function () {
                    return this.getOption("template");
                },
                initialize: function (models, options) {
                    Marionette.CompositeView.prototype.initialize.apply(this, arguments);
                    options = options || {};
                    if (!this.collection && options.model && options.model.isSearchable()) {
                        this.collection = options.model.wrappedCollection;
                        //console.log("TemplateBasedCollectionLayout#initialize, got options.model.wrappedCollection: " + this.collection + ", url: " + this.collection.url);
                    }
                    //console.log("TemplateBasedCollectionView#initialize, collection: " + this.collection);
                },
                /*
                 onShow : function() {
                 var _self = this;
                 var show = true;
                 // poll collection?
                 if (this.collection.getTypeName && this.collection.getTypeName() == "Calipso.collection.PollingCollection") {
                 if (this.options.pollOptions) {
                 // Specify custom options for the plugin.
                 // You can also call this function inside the collection's initialize function and pass the
                 // options for the plugin when instantiating a new collection.
                 this.collection.configure(this.options.pollOptions);
                 }
                 // initialize polling if needed
                 if (!this.collection.isFetching()) {
                 this.collection.startFetching();
                 }
                 }
                 // fetch collection?
                 else if (this.options.forceFetch) {
                 show = false;
                 //console.log("TemplateBasedCollectionView#onShow,  size: " + this.collection.length);
                 _self.collection.fetch({
                 url : _self.collection.url,
                 success : function(collection, response, options) {
                 console.log("TemplateBasedCollectionView#onShow#renderCollectionItems,  size: " + collection.length);
                 //Backbone.Marionette.CompositeView.prototype.onShow.apply(_self);
                 },
                 error : function(collection, response, options) {
                 alert("failed fetching collection");
                 }
                 });
                 }
                 if(show) {
                 Backbone.Marionette.CompositeView.prototype.onShow.apply(this);
                 }

                 },*/
                /**
                 * Stop polling the collection if appropriate
                 */
                onBeforeDestroy: function () {
                    if (!this.pollCollectionAfterDestroy) {
                        if (this.collection.getTypeName && this.collection.getTypeName() == "Calipso.collection.PollingCollection") {
                            //console.log("TemplateBasedCollectionView#onBeforeDestroy, stop polling for collection URL: " + this.collection.url);
                            this.collection.stopFetching();
                            this.collection.reset();
                            this.collection = null;
                        }
                    }
                },
                /** use the template defined by the child if any
                 buildChildView: function(child, ChildViewClass, childViewOptions){
				  var options = _.extend({}, childViewOptions);
				  options.model = child;
				("buildChildView, childViewOptions.template: "+childViewOptions.template);
				  if(child.childViewTemplate){
					  options.template = child.childViewTemplate;
				  }
				  return new ChildViewClass(options);
				},*/
                getTypeName: function () {
                    return this.constructor.getTypeName();
                }
            }, {
                typeName: "Calipso.view.TemplateBasedCollectionView",
                getTypeName: function () {
                    return this.typeName;
                }
            });
        return Calipso;
    });
