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
    ["lib/restdudelib/view-collection", 'underscore', 'handlebars', 'backbone', 'marionette', 'moment', 'backbone-forms', 'backgrid', 'coreui-app'],
    function (Restdude, _, Handlebars, Backbone, BackboneMarionette, moment, BackboneForms, Backgrid, CoreUiApp) {

        var Marionette = Backbone.Marionette;


        //////////////////////////////////////////////////
        // Layouts
        //////////////////////////////////////////////////

        Restdude.view.AppRootView = Restdude.view.View.extend({
                tagName: "body",
                className: "navbar-fixed fixed-nav ",
                template: Restdude.getTemplate('AppRootView'),
                regions: {
                    headerRegion: "#restdudeHeaderRegion",
                    sidebarRegion: "#restdudeSidebarRegion",
                    mainContentRegion: "#restdudeMainContentRegion",
                    modalRegion: Restdude.view.ModalRegion,
                    footerRegion: "#restdudeFooterRegion"
                },
                events: {
                    "click .navbar-toggler": "navbarToggle",
                    "click .sidebar-close": "sidebarClose",
                    "click .aside-toggle": "asideToggle",
                },

                initialize: function (options) {
                    Restdude.view.View.prototype.initialize.apply(this, arguments);

                    console.log("AppRootView.initialize, options: ");
                    console.log(options);
                    console.log("AppRootView.initialize, events: ");
                    console.log(this.events);
                    console.log("AppRootView.initialize, triggers: ");
                    console.log(this.triggers);
                },
                onChildviewLayoutReplace: function (options) {
                    console.log(this.getTypeName() + ".onChildviewLayoutReplace, arguments: ");
                    console.log(arguments);
                },
                resizeBroadcast: function () {

                    var timesRun = 0;
                    var interval = setInterval(function () {
                        timesRun += 1;
                        if (timesRun === 5) {
                            clearInterval(interval);
                        }
                        window.dispatchEvent(new Event('resize'));
                    }, 62.5);
                },
                asideToggle: function (e) {
                    $('body').toggleClass('aside-menu-open');

                    //resize charts
                    this.resizeBroadcast();
                },
                sidebarClose: function (e) {
                    $('body').toggleClass('sidebar-opened').parent().toggleClass('sidebar-opened');
                },
                navbarToggle: function (e) {

                    var bodyClass = localStorage.getItem('body-class');
                    var $link = $(e.currentTarget);
                    if ($link.hasClass('layout-toggler') && $('body').hasClass('sidebar-off-canvas')) {
                        $('body').toggleClass('sidebar-opened').parent().toggleClass('sidebar-opened');
                        //resize charts
                        this.resizeBroadcast();

                    } else if ($link.hasClass('layout-toggler') && ($('body').hasClass('sidebar-nav') || bodyClass == 'sidebar-nav')) {
                        $('body').toggleClass('sidebar-nav');
                        localStorage.setItem('body-class', 'sidebar-nav');
                        if (bodyClass == 'sidebar-nav') {
                            localStorage.clear();
                        }
                        //resize charts
                        this.resizeBroadcast();
                    } else {
                        $('body').toggleClass('mobile-open');
                    }
                },
            },
            // static members
            {
                typeName: "AppRootView",
            });


        Restdude.view.HomeLayout = Restdude.view.View.extend({
                template: Restdude.getTemplate('HomeLayout'),
                onRender: function () {
                    var _this = this;
                }
            },
            // static members
            {
                typeName: "HomeLayout",
            });

        Restdude.view.UseCaseLayout = Restdude.view.View.extend({
            // regionName : viewType
            regionViewTypes: {},
            //childViewEvents: {
            //    "model:sync": "onModelSync",
            //},
            events: {
                "click  .layout-showCreateFormModal": "showCreateFormModal"
            },

            showCreateFormModal: function (e) {
                Restdude.stopEvent(e);
                Restdude.vent.trigger("modal:showUseCaseContext", this.model.constructor.getUseCaseContext({
                    key: "create",
                    addToCollection: this.model.wrappedCollection
                }));
            },
            initialize: function (options) {
                Restdude.view.View.prototype.initialize.apply(this, arguments);
            },
            onRender: function () {
                var _this = this;
                var childUseCase;
                _.each(this.regionViewTypes, function (ViewType, regionName, list) {
                    // only show existing regions as they may be added contitionally
                    if (_this.getRegion(regionName)) {
                        // spawn child usecase
                        childUseCase = _this.useCaseContext.getChildContext(regionName, ViewType);
                        // display a preconfigured view that matches the region and usecase config
                        _this.showChildView(regionName, childUseCase.createView(this.childViewOptions));
                    }
                });
            },

            onModelShow: function (options) {

                console.log(this.getTypeName() + ".onModelShow, arguments: ");
                console.log(arguments);

                options.region =  this.regionName;
                this.triggerMethod("layout:replace", options);
            },
            onChildviewModelShow: function (options) {

                console.log(this.getTypeName() + ".onChildviewModelShow, arguments: ");
                console.log(arguments);

                options.region =  this.regionName;
                this.triggerMethod("layout:replace", options);
            },

            onModelSync: function (args) {

                console.log(this.getTypeName() + ".onModelSync, args: ");
                console.log(args);
                // execute next useCase by default
                if (this.closeModalOnSync) {
                    Restdude.vent.trigger("modal:destroy");
                }
                else {
                    this.nextUseCase();
                }
            },

            nextUseCase: function () {
                if (this.useCaseContext.defaultNext) {
                    Restdude.navigate("/useCases/" + this.model.getPathFragment() + '/' + this.useCaseContext.defaultNext, {
                        trigger: true
                    })
                } else {
                    throw "Use case does not define a defaultNext";
                }
            }
        }, {
            typeName: "Restdude.view.UseCaseLayout",
        });

        Restdude.view.ModalLayout = Restdude.view.UseCaseLayout.extend({
            template: Restdude.getTemplate('modal-layout'),
            events: {
                "click a.modal-close": "closeModal",
                "click button.modal-close": "closeModal"
            },
            regions: {
                modalBodyRegion: ".modal-body"
            },
            childView: null,
            initialize: function (options) {
                Restdude.view.UseCaseLayout.prototype.initialize.apply(this, arguments);
                var _this = this;
                if (options.childView) {
                    this.childView = options.childView;
                    this.childView.modal = true;
                }
            },
            onRender: function () {
                // render child view
                this.showChildView("modalBodyRegion", this.childView);
            },
            closeModal: function (e) {
                Restdude.stopEvent(e);
                Restdude.vent.trigger("modal:close");
            }

        }, {
            typeName: "Restdude.view.ModalLayout"
        });

        /*Restdude.view.HeaderNotificationsRegion = Backbone.Marionette.Region.extend({
         el : "#restdudeHeaderView-notificationsRegion",
         attachHtml : function(view) {
         this.$el.clear().append('<a href="#" data-toggle="dropdown" class="dropdown-toggle">' + '<i class="fa fa-bell fa-fw"></i>' + '<sup class="badge badge-primary badge-notifications-count hidden"></sup>' + '<i class="fa fa-caret-down"></i>', view.el);
         }
         });
         Restdude.view.HeaderNotificationsRegion.prototype.attachHtml = function(view) {
         this.$el.clear().append('<a href="#" data-toggle="dropdown" class="dropdown-toggle">' + '<i class="fa fa-bell fa-fw"></i>' + '<sup class="badge badge-primary badge-notifications-count hidden"></sup>' + '<i class="fa fa-caret-down"></i>', view.el);
         };*/

        Restdude.view.SidebarView = Restdude.view.View.extend(
            /** @lends Restdude.view.SidebarView.prototype */
            {
                tagName: "nav",
                className: "sidebar-nav",
                template: Restdude.getTemplate('sidebar'),
                events: {
                    "click ul.nav a": "toggleDropDown",
                },

                toggleDropDown: function (e) {

                    var $link = $(e.currentTarget);
                    if ($link.hasClass('nav-dropdown-toggle')) {
                        $link.parent().removeClass('nt').toggleClass('open');
                    }

                },
                onRender: function () {
                    var _this = this;
                    var cUrl = String(window.location);
                    cUrl = cUrl.substring((Restdude.getBaseUrl() + "/client").length);

                    // ignore anchor
                    if (cUrl.substr(cUrl.length - 1) == '#') {
                        cUrl = cUrl.slice(0, -1);
                    }
                    // ignore params
                    var q = cUrl.indexOf("?");
                    if (q > 0) {
                        cUrl = cUrl.substring(0, q);
                    }
                    // Add class .active to current link
                    // TODO
                    /*
                     this.$el.find('ul.nav a').each(function () {



                     if ($($(this))[0].href == cUrl) {
                     $(this).addClass('active');

                     $(this).parents('ul').parent().addClass('open');
                     }
                     });
                     */

                }
            }
        );
        Restdude.view.HeaderView = Restdude.view.View.extend(
            /** @lends Restdude.view.HeaderView.prototype */
            {
                tagName: "div",
                className: "container-fluid",
                template: Restdude.getTemplate('header'),
                //events: {
                    //"click a.login" : "login",
                    //"click a.register" : "register",
                //"click a.logout": "logout",
                //"click a.locale": "changeLocale",
                //},
                regions: {

                    menuRegion: "#restdudeHeaderView-menuRegion",
                    notificationsRegion: "#restdudeHeaderView-notificationsRegion"
                    //					notificationsRegion : {
                    //						// appends the notifications without clearing the link,
                    //						// fixes HTML structure issue
                    //						regionClass : Restdude.view.HeaderNotificationsRegion
                    //					}
                },

                initialize: function (options) {
                    Restdude.view.View.prototype.initialize.apply(this, arguments);
                    this.mergeOptions(options);
                },

                /*
                 TODO
                 onRender : function() {

                 if (Restdude.util.isAuthenticated()) {
                 // load and render notifications list
                 var notifications = new Restdude.collection.PollingCollection([], {
                 url : Restdude.getBaseUrl() + "/api/rest/baseNotifications",
                 model : Restdude.model.BaseNotificationModel
                 });

                 var notificationsView = new Restdude.view.TemplateBasedCollectionView({
                 tagName : "ul",
                 className : "dropdown-menu dropdown-notifications",
                 template : Restdude.getTemplate("headerNotificationsCollectionView"),
                 childViewOptions : {
                 template : Restdude.getTemplate("headerNotificationsItemView"),
                 },
                 collection : notifications,
                 });
                 this.showChildView("notificationsRegion", notificationsView);
                 // update counter badges
                 Restdude.updateBadges(".badge-notifications-count", Restdude.session.userDetails ? Restdude.session.userDetails.get("notificationCount") : 0);
                 }

                 },
                 */
            }, {
                typeName: "Restdude.view.HeaderView"
            });


        Restdude.view.TabLayout = Restdude.view.View.extend({
                template: Restdude.getTemplate('tabbed-layout'),
                tabClass: "nav nav-tabs",
                idProperty: "id",
                showOnselect: false,
                buttonTextProperty: "name",
                events: {
                    "click a[data-toggle=\"tab\"]": "showTabContent"
                },
                regions: {
                    tabLabelsRegion: '.region-nav-tabs',
                    tabContentsRegion: '.region-tab-content'
                },
                initialize: function (options) {
                    Restdude.view.View.prototype.initialize.apply(this, arguments);
                    this.mergeOptions(options);
                },
                /**
                 * Redraws the selected tab content when
                 * options.showOnselect is true
                 */
                showTabContent: function (e) {
                    if (this.options.showOnselect) {
                        var $link = $(e.currentTarget);
                        this.showChildView("tabContentsRegion", new this.itemViewType({
                            model: this.options.collection.at($link.data("collectionIndex"))
                        }));
                    }
                },
                onRender: function () {
                    var _this = this;
                    if (this.collection.length > 0) {
                        for (var i = 0; i < this.collection.length; i++) {
                            var modelItem = this.collection.at(i);
                            modelItem.set("tabActive", i == 0 ? true : false);
                            modelItem.set("collectionIndex", i);
                        }
                    }

                    var buttonTextProperty = this.getOption("buttonTextProperty");
                    var idProperty = this.getOption("idProperty");
                    var TabButtonItemView = Restdude.view.TemplateBasedItemView.extend({
                        template: _.template('<a href="#tab<%= ' + idProperty + ' %>" ' + ' <% if (tabActive != undefined && tabActive){ %> class="active" <% } %>' + 'aria-controls="tab<%= ' + idProperty + ' %>" role="tab" ' + 'data-toggle="tab" data-collection-index="<%=collectionIndex%>"><%= ' + buttonTextProperty + ' %></a>'),
                        tagName: "li",

                        attributes: function () {
                            // Return model data
                            return {
                                role: "presentation",
                                class: this.model.get("tabActive") ? " active" : "",
                            };
                        }
                    });
                    var TabButtonsCollectionView = Restdude.view.TemplateBasedCollectionView.extend({
                        tagName: "ul",
                        className: _this.getOption("tabClass"),
                        attributes: {
                            role: "tablist"
                        },
                        childView: TabButtonItemView
                    });
                    this.showChildView("tabLabelsRegion", new TabButtonsCollectionView({
                        collection: this.collection
                    }));

                    var BaseItemViewType = _this.collection.model.getItemViewType() || Restdude.view.TemplateBasedItemView;
                    _this.itemViewType = BaseItemViewType.extend({
                        tagName: "div",
                        template: _this.collection.model.getItemViewTemplate(),
                        attributes: function () {
                            // Return model data
                            return {
                                id: "tab" + this.model.get(idProperty),
                                role: "tabpanel",
                                class: "tab-pane" + (this.model.get("tabActive") ? " active" : ""),

                            };
                        }
                    });

                    var tabPanelsView;
                    if (_this.options.showOnselect) {
                        tabPanelsView = new _this.itemViewType({
                            model: _this.options.collection.at(0)
                        });
                    } else {
                        var TabPanelsCollectionView = Restdude.view.TemplateBasedCollectionView.extend({
                            tagName: "div",
                            template: _.template(''),
                            className: "tab-content",
                            childView: ItemViewType,
                        });
                        tabPanelsView = new TabPanelsCollectionView({
                            collection: this.collection
                        });
                    }
                    this.showChildView("tabContentsRegion", tabPanelsView);

                },
            },
            // static members
            {
                typeName: "Restdude.view.TabLayout",
            });

// TODO: remove? sum all related types to tabs file?
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
                    return Backbone.Marionette.ItemView.extend({
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

        Restdude.view.AppLayout = Restdude.view.View.extend({
                tagName: "div",
                template: Restdude.getTemplate('applayout'),
                regions: {
                    navRegion: "#restdudeAppLayoutNavRegion",
                    contentRegion: "#restdudeAppLayoutContentRegion"
                }
            },
            // static members
            {
                typeName: "Restdude.view.AppLayout",
            });

        Restdude.view.BrowseLayout = Restdude.view.UseCaseLayout.extend({
                template: Restdude.getTemplate('UseCaseLayout'),
                regions: {
                    contentRegion: ".contentRegion",
                    contentChildRegion: ".contentChildRegion"
                },
                regionViewTypes: {
                    contentRegion: Restdude.view.UseCaseFormView,
                },
            },
            // static members
            {
                typeName: "Restdude.view.BrowseLayout"
            });

        Restdude.view.JsonSchemaLayout = Restdude.view.UseCaseLayout.extend({
                template: Restdude.getTemplate('UseCaseLayout'),
                regions: {
                    contentRegion: ".contentRegion"
                },
                regionViewTypes: {
                    contentRegion: Restdude.view.JsonSchemaView,
                },
                onRender: function () {
                    var _this = this;
                    var url = Restdude.getBaseUrl() + '/api/rest/' + this.model.getPathFragment() + "/jsonschema";
                    $.getJSON(url, {})
                        .done(function (data) {
                            console.log("JsonSchemaLayout.onRender.success, data:");
                            console.log(data);
                            var childUseCase;
                            _.each(_this.regionViewTypes, function (ViewType, regionName, list) {
                                // only show existing regions as they may be added contitionally
                                if (_this.getRegion(regionName)) {
                                    // spawn child usecase
                                    childUseCase = _this.useCaseContext.getChildContext(regionName, ViewType);
                                    // display a preconfigured view that matches the region and usecase config
                                    var viewOptions = _.extend(_this.childViewOptions, {
                                        regionName: regionName,
                                        regionPath: _this.regionPath + "/" + regionName,
                                    });
                                    console.log("JsonSchemaLayout.onRender.success, viewOptions:");
                                    console.log(viewOptions);
                                    _this.showChildView(regionName, childUseCase.createView({
                                        viewOptions: viewOptions,
                                        jsonSchema: data
                                    }));
                                }
                            });
                        });

                },
            },
            // static members
            {
                typeName: "Restdude.view.JsonSchemaLayout"
            });

        Restdude.view.UserProfileLayout = Restdude.view.BrowseLayout.extend({
                regionViewTypes: {
                    contentRegion: Restdude.view.UserProfileView,
                },
            },
            // static members
            {
                typeName: "Restdude.view.UserProfileLayout"
            });



        Restdude.view.UseCaseSearchLayout = Restdude.view.UseCaseLayout.extend({
            template: Restdude.getTemplate('UseCaseSearchLayout'),
            regions: {
                formRegion: ".criteriaEntryRegion",
                //searchBoxRegion : ".searchBoxRegion",
                contentRegion: ".contentRegion"
            },
            regionViewTypes: {
                formRegion: Restdude.view.UseCaseFormView,
                searchBoxRegion: Restdude.view.SearchBoxFormView,
                contentRegion: Restdude.view.UseCaseGridView
            },
            initialize: function (options) {
                Restdude.view.UseCaseLayout.prototype.initialize.apply(this, arguments);
                // show searchbox region if appropriate
                this.mergeOptions(options, ['fieldsSearchBox']);
                if (this.fieldsSearchBox) {
                    this.addRegions({
                        searchBoxRegion: {selector: ".searchBoxRegion"}
                    });
                }
                // listenTo search responces
                var collection = options.collection || options.model.wrappedCollection;
                var _this = this;
                this.listenTo(collection, 'reset', function () {
                    if (_this.regionPath == "/") {
                        var q = $.param(collection.data);
                        Restdude.navigate(this.useCaseContext.getRouteUrl() + "?" + q, {
                            trigger: false
                        })
                    }
                });
            },
        },
        // static members
        {
            typeName: "Restdude.view.UseCaseSearchLayout"
        });

        Restdude.view.TopicLayout = Restdude.view.BrowseLayout.extend({
            template: Restdude.getTemplate('TopicLayout'),
        },
        // static members
        {
            typeName: "Restdude.view.TopicLayout"
        });

        Restdude.view.DefaulfModalLayout = Restdude.view.UseCaseLayout.extend({
                template: Restdude.getTemplate('modal-layout'),
                events: {
                    "click a.modal-close": "closeModal",
                    "click button.modal-close": "closeModal"
                },
                regions: {
                    modalBodyRegion: ".modal-body"
                },
                onRender: function () {
                    // render child view
                    this.showChildView("modalBodyRegion", this.options.childView);
                },
                closeModal: function (e) {
                    Restdude.stopEvent(e);
                    Restdude.vent.trigger("modal:close");
                }
            },
            // static members
            {
                typeName: "Restdude.view.DefaulfModalLayout"
            });

        Restdude.view.SmallPageLayout = Restdude.view.BrowseLayout.extend({
            template: Restdude.getTemplate('SmallPageLayout'),
        });

        Restdude.view.UserDetailsLayout = Restdude.view.SmallPageLayout.extend(
            /** @lends Restdude.view.UserDetailsLayout.prototype */
            {
                initialize: function (options) {
                    Restdude.view.SmallPageLayout.prototype.initialize.apply(this, arguments);
                    this.model.set(Restdude.getHttpUrlParams());
                },
                onModelSync: function (options) {
                    /*
                     // if successful login
                     if (this.model.get(Restdude.config.idAttribute) {
                     // TODO: add 'forward' HTTP/URL param in controller cases
                     var fw = Restdude.domain.fw || "/home";
                     Restdude.domain.fw = null;
                     Restdude.navigate(fw, {
                     trigger : true
                     });
                     }
                     // else just follow useCase.defaultNext configuration
                     else {
                     if (this.useCaseContext.defaultNext) {
                     var url = '/' + this.model.getPathFragment() + '/' + this.useCaseContext.defaultNext;
                     var email = this.model.get("email") || Restdude.getHttpUrlParams()["email"];
                     if(email){
                     url += ("?email=" + email);
                     }
                     Restdude.navigate(url, {
                     trigger : true
                     })
                     } else {
                     console.log("Use case does not define a defaultNext");
                     }
                     }
                     */
                },
            },
            // static members
            {
                typeName: "Restdude.view.UserDetailsLayout"
            });

        Restdude.view.UserAccountLayout = Restdude.view.UserDetailsLayout.extend(
            /** @lends Restdude.view.UserDetailsLayout.prototype */
            {
                onModelSync: function (options) {

                    // TODO: handle from (and reuse) layout
                    if (this.useCaseContext.defaultNext) {
                        Restdude.navigate("/useCases/" + this.model.getPathFragment() + '/' + this.useCaseContext.defaultNext, {
                            trigger: true
                        })
                    } else {
                        Restdude.session.userDetails = Restdude.model.UserDetailsModel.create(this.model.attributes);

                        Restdude.app.updateHeaderFooter();
                        Restdude.navigate("/myProfile", {
                            trigger: true
                        });
                    }
                },
            },
            // static members
            {
                typeName: "Restdude.view.UserAccountLayout"
            });


        Restdude.view.UserInvitationsLayout = Restdude.view.BrowseLayout.extend(
            /** @lends Restdude.view.UserInvitationsLayout.prototype */
            {
                onModelSync: function (options) {
                    var pageView = new Restdude.view.TemplateBasedItemView({
                        template: Restdude.getTemplate("UserInvitationResults"),
                        tagName: "div",
                        model: this.model
                    });
                    this.showChildView("contentRegion", pageView);

                },
            }, {
                // static members
                typeName: "UserInvitationsLayout"
            });

        Restdude.view.UserRegistrationLayout = Restdude.view.SmallPageLayout.extend(
            /** @lends Restdude.view.UserRegistrationLayout.prototype */
            {
                onModelSync: function (options) {
                    Restdude.navigate("/page/userRegistrationSubmitted", {
                        trigger: true
                    });
                },
            }, {
                // static members
                typeName: "UserRegistrationLayout"
            });
    });
