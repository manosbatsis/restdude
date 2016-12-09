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
    ["lib/calipsolib/view-collection", 'underscore', 'handlebars', 'backbone', 'marionette', 'moment', 'backbone-forms', 'backgrid', 'coreui-app'],
    function (Calipso, _, Handlebars, Backbone, BackboneMarionette, moment, BackboneForms, Backgrid, CoreUiApp) {

        var Marionette = Backbone.Marionette;


        //////////////////////////////////////////////////
        // Layouts
        //////////////////////////////////////////////////

        Calipso.view.AppRootView = Calipso.view.View.extend({
                tagName: "body",
                className: "navbar-fixed fixed-nav ",
                template: Calipso.getTemplate('AppRootView'),
                regions: {
                    headerRegion: "#calipsoHeaderRegion",
                    sidebarRegion: "#calipsoSidebarRegion",
                    mainContentRegion: "#calipsoMainContentRegion",
                    modalRegion: Calipso.view.ModalRegion,
                    footerRegion: "#calipsoFooterRegion"
                },
                events: {
                    "click .navbar-toggler": "navbarToggle",
                    "click .sidebar-close": "sidebarClose",
                    "click .aside-toggle": "asideToggle",
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


        Calipso.view.HomeLayout = Calipso.view.View.extend({
                template: Calipso.getTemplate('HomeLayout'),
                onRender: function () {
                    var _this = this;
                }
            },
            // static members
            {
                typeName: "HomeLayout",
            });

        Calipso.view.UseCaseLayout = Calipso.view.View.extend({
            // regionName : viewType
            regionViewTypes: {},
            childViewEvents: {
                "model:sync": "onModelSync"
            },
            events: {
                "click  .layout-showCreateFormModal": "showCreateFormModal"
            },

            showCreateFormModal: function (e) {
                Calipso.stopEvent(e);
                Calipso.vent.trigger("modal:showUseCaseContext", this.model.constructor.getUseCaseContext({
                    key: "create",
                    addToCollection: this.model.wrappedCollection
                }));
            },
            initialize: function (options) {
                Calipso.view.View.prototype.initialize.apply(this, arguments);
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
                        var viewOptions = _.extend(this.childViewOptions, {
                            regionName: regionName,
                            regionPath: _this.regionPath + "/" + regionName
                        });
                        _this.showChildView(regionName, childUseCase.createView({viewOptions: viewOptions}));
                    }
                });
            },
            showChildView: function (regionName, view) {
                var _this = this;

                // bind to view events according to childViewEvents hash
                _.each(this.childViewEvents, function (method, eventName, list) {
                    _this.listenTo(view, eventName, function (options) {
                        // if method is own method name
                        if (_.isString(method) && _this[method]) {
                            _this[method](options);
                        }
                        // if method is a function
                        else if (_.isFunction(method)) {
                            method(options);
                        }
                    });
                });
                Calipso.view.View.prototype.showChildView.apply(this, arguments);
            },
            onModelSync: function (args) {
                // execute next useCase by default
                if (this.closeModalOnSync) {
                    Calipso.vent.trigger("modal:destroy");
                }
                else {
                    this.nextUseCase();
                }
            },
            nextUseCase: function () {
                if (this.useCaseContext.defaultNext) {
                    Calipso.navigate("/useCases/" + this.model.getPathFragment() + '/' + this.useCaseContext.defaultNext, {
                        trigger: true
                    })
                } else {
                    throw "Use case does not define a defaultNext";
                }
            }
        }, {
            typeName: "Calipso.view.UseCaseLayout",
        });

        Calipso.view.ModalLayout = Calipso.view.UseCaseLayout.extend({
            template: Calipso.getTemplate('modal-layout'),
            events: {
                "click a.modal-close": "closeModal",
                "click button.modal-close": "closeModal"
            },
            regions: {
                modalBodyRegion: ".modal-body"
            },
            childView: null,
            initialize: function (options) {
                Calipso.view.UseCaseLayout.prototype.initialize.apply(this, arguments);
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
                Calipso.stopEvent(e);
                Calipso.vent.trigger("modal:close");
            }

        }, {
            typeName: "Calipso.view.ModalLayout"
        });

        /*Calipso.view.HeaderNotificationsRegion = Backbone.Marionette.Region.extend({
         el : "#calipsoHeaderView-notificationsRegion",
         attachHtml : function(view) {
         this.$el.clear().append('<a href="#" data-toggle="dropdown" class="dropdown-toggle">' + '<i class="fa fa-bell fa-fw"></i>' + '<sup class="badge badge-primary badge-notifications-count hidden"></sup>' + '<i class="fa fa-caret-down"></i>', view.el);
         }
         });
         Calipso.view.HeaderNotificationsRegion.prototype.attachHtml = function(view) {
         this.$el.clear().append('<a href="#" data-toggle="dropdown" class="dropdown-toggle">' + '<i class="fa fa-bell fa-fw"></i>' + '<sup class="badge badge-primary badge-notifications-count hidden"></sup>' + '<i class="fa fa-caret-down"></i>', view.el);
         };*/

        Calipso.view.SidebarView = Calipso.view.View.extend(
            /** @lends Calipso.view.HeaderView.prototype */
            {
                tagName: "nav",
                className: "sidebar-nav",
                template: Calipso.getTemplate('sidebar'),
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
                    cUrl = cUrl.substring((Calipso.getBaseUrl() + "/client").length);

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
        Calipso.view.HeaderView = Calipso.view.View.extend(
            /** @lends Calipso.view.HeaderView.prototype */
            {
                tagName: "div",
                className: "container-fluid",
                template: Calipso.getTemplate('header'),
                events: {
                    //"click a.login" : "login",
                    //"click a.register" : "register",
                    "click a.logout": "logout",
                    "click a.locale": "changeLocale",
                },
                regions: {

                    menuRegion: "#calipsoHeaderView-menuRegion",
                    notificationsRegion: "#calipsoHeaderView-notificationsRegion"
                    //					notificationsRegion : {
                    //						// appends the notifications without clearing the link,
                    //						// fixes HTML structure issue
                    //						regionClass : Calipso.view.HeaderNotificationsRegion
                    //					}
                },

                initialize: function (options) {
                    Calipso.view.View.prototype.initialize.apply(this, arguments);
                    this.mergeOptions(options);
                },
                changeLocale: function (e) {
                    Calipso.stopEvent(e);
                    Calipso.changeLocale($(e.currentTarget).data("locale"));
                },
                /*
                 TODO
                 onRender : function() {

                 if (Calipso.util.isAuthenticated()) {
                 // load and render notifications list
                 var notifications = new Calipso.collection.PollingCollection([], {
                 url : Calipso.getBaseUrl() + "/api/rest/baseNotifications",
                 model : Calipso.model.BaseNotificationModel
                 });

                 var notificationsView = new Calipso.view.TemplateBasedCollectionView({
                 tagName : "ul",
                 className : "dropdown-menu dropdown-notifications",
                 template : Calipso.getTemplate("headerNotificationsCollectionView"),
                 childViewOptions : {
                 template : Calipso.getTemplate("headerNotificationsItemView"),
                 },
                 collection : notifications,
                 });
                 this.showChildView("notificationsRegion", notificationsView);
                 // update counter badges
                 Calipso.updateBadges(".badge-notifications-count", Calipso.session.userDetails ? Calipso.session.userDetails.get("notificationCount") : 0);
                 }

                 },
                 */
            }, {
                typeName: "Calipso.view.HeaderView"
            });


        Calipso.view.TabLayout = Calipso.view.View.extend({
                template: Calipso.getTemplate('tabbed-layout'),
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
                    Calipso.view.View.prototype.initialize.apply(this, arguments);
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
                    var TabButtonItemView = Calipso.view.TemplateBasedItemView.extend({
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
                    var TabButtonsCollectionView = Calipso.view.TemplateBasedCollectionView.extend({
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

                    var BaseItemViewType = _this.collection.model.getItemViewType() || Calipso.view.TemplateBasedItemView;
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
                        var TabPanelsCollectionView = Calipso.view.TemplateBasedCollectionView.extend({
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
                typeName: "Calipso.view.TabLayout",
            });

// TODO: remove? sum all related types to tabs file?
        Calipso.collection.TabCollection = Backbone.Collection.extend({
                initialize: function () {
                    if (!Calipso.model.TabModel) {
                        Calipso.model.TabModel = Calipso.model.GenericModel.extend({
                            getPathFragment: function () {
                                return null;
                            }
                        });

                        Calipso.model.TabModel.getTypeName = function (instance) {
                            return "TabModel";
                        };
                    }
                    this.model = Calipso.model.GenericModel;
                    this.listenTo('add', this.onModelAdded, this);
                    this.listenTo('remove', this.onModelRemoved, this);
                },
                onModelAdded: function (model, collection, options) {
                    //_self.tabKeys[model.get("id")] = model;
                },
                onModelRemoved: function (model, collection, options) {
                    //_self.tabKeys[model.get("id")] = null;
                },
            },
            // static members
            {
                typeName: "Calipso.view.TabCollection",
            });

        Calipso.view.TabLabelsCollectionView = Backbone.Marionette.CollectionView.extend({
                className: 'nav nav-pills',
                tagName: 'ul',
                itemTemplate: Calipso.getTemplate('tab-label'),
                childViewContainer: '.nav-tabs',
                initialize: function (options) {
                    Marionette.CollectionView.prototype.initialize.apply(this, arguments);

                },
                getItemView: function (item) {
                    var _this = this;
                    return Backbone.Marionette.ItemView.extend({
                        tagName: 'li',
                        className: 'calipso-tab-label',
                        id: "calipso-tab-label-" + item.get("id"),
                        template: _this.itemTemplate,
                        events: {
                            "click .show-tab": "viewTab",
                            "click .destroy-tab": "destroyTab"
                        },
                        /**
                         this.listenTo(Calipso.vent, "layout:viewModel", function(itemModel) {
					_this.showItemViewForModel(itemModel, "view")
				}, this);
                         */
                        viewTab: function (e) {
                            Calipso.stopEvent(e);
                            CalipsoApp.vent.trigger("viewTab", this.model);
                        },
                        destroyTab: function (e) {
                            Calipso.stopEvent(e);
                            //					this.model.collection.remove(this.model);
                            this.destroy();
                            CalipsoApp.vent.trigger("viewTab", {
                                id: "Search"
                            });
                        },
                    });
                },
            },
            // static members
            {
                typeName: "Calipso.view.TabLabelsCollectionView",
            });

        Calipso.view.TabContentsCollectionView = Backbone.Marionette.CollectionView.extend({
                tagName: 'div',
                getItemView: function (item) {
                    var someItemSpecificView = item.getItemViewType ? item.getItemViewType() : null;
                    if (!someItemSpecificView) {
                        someItemSpecificView = Calipso.view.UseCaseFormView;
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
                typeName: "Calipso.view.TabContentsCollectionView",
            });

        Calipso.view.AppLayout = Calipso.view.View.extend({
                tagName: "div",
                template: Calipso.getTemplate('applayout'),
                regions: {
                    navRegion: "#calipsoAppLayoutNavRegion",
                    contentRegion: "#calipsoAppLayoutContentRegion"
                }
            },
            // static members
            {
                typeName: "Calipso.view.AppLayout",
            });

        Calipso.view.BrowseLayout = Calipso.view.UseCaseLayout.extend({
                template: Calipso.getTemplate('UseCaseLayout'),
                regions: {
                    contentRegion: ".contentRegion"
                },
                regionViewTypes: {
                    contentRegion: Calipso.view.UseCaseFormView,
                },
            },
            // static members
            {
                typeName: "Calipso.view.BrowseLayout"
            });

        Calipso.view.UserProfileLayout = Calipso.view.BrowseLayout.extend({
                regionViewTypes: {
                    contentRegion: Calipso.view.UserProfileView,
                },
            },
            // static members
            {
                typeName: "Calipso.view.UserProfileLayout"
            });

        Calipso.view.UseCaseSearchLayout = Calipso.view.UseCaseLayout.extend({
                template: Calipso.getTemplate('UseCaseSearchLayout'),
                regions: {
                    formRegion: ".criteriaEntryRegion",
                    //searchBoxRegion : ".searchBoxRegion",
                    contentRegion: ".contentRegion"
                },
                regionViewTypes: {
                    formRegion: Calipso.view.UseCaseFormView,
                    searchBoxRegion: Calipso.view.SearchBoxFormView,
                    contentRegion: Calipso.view.UseCaseGridView
                },
                initialize: function (options) {
                    Calipso.view.UseCaseLayout.prototype.initialize.apply(this, arguments);
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
                            Calipso.navigate(this.useCaseContext.getRouteUrl() + "?" + q, {
                                trigger: false
                            })
                        }
                    });
                },
            },
            // static members
            {
                typeName: "Calipso.view.UseCaseSearchLayout"
            });

        Calipso.view.DefaulfModalLayout = Calipso.view.UseCaseLayout.extend({
                template: Calipso.getTemplate('modal-layout'),
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
                    Calipso.stopEvent(e);
                    Calipso.vent.trigger("modal:close");
                }
            },
            // static members
            {
                typeName: "Calipso.view.DefaulfModalLayout"
            });

        Calipso.view.SmallPageLayout = Calipso.view.BrowseLayout.extend({
            template: Calipso.getTemplate('SmallPageLayout'),
        });

        Calipso.view.UserDetailsLayout = Calipso.view.SmallPageLayout.extend(
            /** @lends Calipso.view.UserDetailsLayout.prototype */
            {
                initialize: function (options) {
                    Calipso.view.SmallPageLayout.prototype.initialize.apply(this, arguments);
                    this.model.set(Calipso.getHttpUrlParams());
                },
                onModelSync: function (options) {
                    /*
                     // if successful login
                     if (this.model.get("id")) {
                     // TODO: add 'forward' HTTP/URL param in controller cases
                     var fw = Calipso.domain.fw || "/home";
                     Calipso.domain.fw = null;
                     Calipso.navigate(fw, {
                     trigger : true
                     });
                     }
                     // else just follow useCase.defaultNext configuration
                     else {
                     if (this.useCaseContext.defaultNext) {
                     var url = '/' + this.model.getPathFragment() + '/' + this.useCaseContext.defaultNext;
                     var email = this.model.get("email") || Calipso.getHttpUrlParams()["email"];
                     if(email){
                     url += ("?email=" + email);
                     }
                     Calipso.navigate(url, {
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
                typeName: "Calipso.view.UserDetailsLayout"
            });

        Calipso.view.UserAccountLayout = Calipso.view.UserDetailsLayout.extend(
            /** @lends Calipso.view.UserDetailsLayout.prototype */
            {
                onModelSync: function (options) {

                    // TODO: handle from (and reuse) layout
                    if (this.useCaseContext.defaultNext) {
                        Calipso.navigate("/useCases/" + this.model.getPathFragment() + '/' + this.useCaseContext.defaultNext, {
                            trigger: true
                        })
                    } else {
                        Calipso.session.userDetails = Calipso.model.UserDetailsModel.create(this.model.attributes);

                        Calipso.app.updateHeaderFooter();
                        Calipso.navigate("/myProfile", {
                            trigger: true
                        });
                    }
                },
            },
            // static members
            {
                typeName: "Calipso.view.UserAccountLayout"
            });


        Calipso.view.UserInvitationsLayout = Calipso.view.BrowseLayout.extend(
            /** @lends Calipso.view.UserInvitationsLayout.prototype */
            {
                onModelSync: function (options) {
                    var pageView = new Calipso.view.TemplateBasedItemView({
                        template: Calipso.getTemplate("UserInvitationResults"),
                        tagName: "div",
                        model: this.model
                    });
                    this.showChildView("contentRegion", pageView);

                },
            }, {
                // static members
                typeName: "UserInvitationsLayout"
            });

        Calipso.view.UserRegistrationLayout = Calipso.view.SmallPageLayout.extend(
            /** @lends Calipso.view.UserRegistrationLayout.prototype */
            {
                onModelSync: function (options) {
                    Calipso.navigate("/page/userRegistrationSubmitted", {
                        trigger: true
                    });
                },
            }, {
                // static members
                typeName: "UserRegistrationLayout"
            });
    });
