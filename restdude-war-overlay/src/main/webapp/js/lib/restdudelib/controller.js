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
    ["lib/restdudelib/uifield", "lib/restdudelib/view", 'underscore', 'backbone', 'marionette'],
    function (Restdude, RestdudeView, _, Backbone, BackboneMarionette) {

        // //////////////////////////////////////
        // Controller
        // //////////////////////////////////////
        var Marionette = Backbone.Marionette;
        Restdude.Controller = Marionette.Object.extend({
            showView: function (view) {
                Restdude.app.view.showChildView("mainContentRegion", view);
            },
            toHome: function () {
                Restdude.navigate("home", {
                    trigger: true
                });
            },
            home: function () {
                /*if (!Restdude.util.isAuthenticated()) {
                 this._redir("userDetails/login");
                 }
                 else{*/
                this.showView(new Restdude.view.HomeLayout());
                //}
            },
            _redir: function (route, forwardAfter) {
                var url = Restdude.app.config.contextPath + "client/" + route;
                Restdude.app.fw = forwardAfter;
                //consolelog("AbstractController#_redir to " + url);
                Restdude.navigate(firstLevelFragment, {
                    trigger: true
                });

            },
            _ensureLoggedIn: function () {
                var pass = Restdude.util.isAuthenticated();
                if (!pass) {
                    Restdude.app.fw = Backbone.history.getFragment();
                    console.log("_ensureLoggedIn FW: " + Restdude.app.fw);
                    Restdude.navigate("userDetails/login", {
                        trigger: true
                    });
                }
                return pass;
            },
            myProfile: function () {
                if (this._ensureLoggedIn()) {
                    this.showUseCaseView("users", Restdude.session.userDetails.get(Restdude.config.idAttribute), "view", null);
                }
            },
            login: function () {
                var fw = Restdude.util.isAuthenticated() ? "home" : "userDetails/login";
                Restdude.navigate(fw, {
                    trigger: true
                });
            },
            logout: function () {
                console.log("controller logout");
                Restdude.session.logout();
            },
            register: function () {
                var fw = Restdude.util.isAuthenticated() ? "home" : "useCases/accounts/create";
                Restdude.navigate(fw, {
                    trigger: true
                });
            },
            showEntitySearch: function (pathFragment, httpParams) {
                this.showUseCaseView(pathFragment, null, "search", httpParams);
            },
            showEntityView: function (pathFragment, modelId) {
                this.showUseCaseView(pathFragment, modelId, "view", null);
            },
            showUserDetailsView: function (useCaseKey, httpParams) {
                // temp line
                this.showUseCaseView("userDetails", null, useCaseKey, httpParams);
            },
            showUseCaseView: function (pathFragment, modelId, useCaseKey, httpParams) {
                httpParams = Restdude.getHttpUrlParams(httpParams);
                var _self = this;
                var qIndex = modelId ? modelId.indexOf("?") : -1;
                if (qIndex > -1) {
                    modelId = modelId.substring(0, qIndex);
                }
                // build the model instance representing the current request
                $.when(Restdude.util.getUseCaseFactory(pathFragment)).done(
                    function (UseCaseFactory) {
                        // check for usecase routes for new instances

                        if (UseCaseFactory.hasUseCase(modelId)) {
                            useCaseKey = modelId;
                            modelId = null;
                        }

                        // check if model type is public
                        if (UseCaseFactory.isPublic() || _self._ensureLoggedIn()) {
                            var useCaseContext = UseCaseFactory.getUseCaseContext({
                                key: useCaseKey, modelId: modelId, httpParams: httpParams, pathFragment: pathFragment
                            });

                            // TODO: move fetch logic to  useCase
                            var model = useCaseContext.model;
                            var skipDefaultSearch = model.skipDefaultSearch && model.wrappedCollection && model.wrappedCollection.hasCriteria();

                            var renderFetchable = function () {
                                _self.showView(useCaseContext.createView({regionName: "/", regionPath: "/"}));
                            };
                            var fetchable = useCaseContext.getFetchable();
                            if ((model.get(Restdude.config.idAttribute) || fetchable.length == 0 ) && model.getTypeName() != "Restdude.model.UserDetailsModel"/*
                             && (!model.wrappedCollection || (!skipDefaultSearch && fetchable.length == 0))
                             */) {
                                console.log("FETCHING");
                                fetchable.fetch({
                                    data: fetchable.data
                                }).then(renderFetchable);
                            } else {
                                console.log("FETCHED");
                                renderFetchable();
                            }
                        }
                    }
                );

            },
            notFoundRoute: function () {
                this.showView(new Restdude.view.NotFoundView());

            },
            /**
             * route for template-based pages ('page/:templateName')
             * @member BacRestdude.controller.AbstractController
             * @param {string} formattedData
             */
            templatePage: function (templateName) {
                var pageView = new Restdude.view.TemplateBasedItemView({
                    template: Restdude.getTemplate(templateName),
                    tagName: "div"
                });
                this.showView(pageView);
                //Restdude.vent.trigger("domain:show", pageView);
            },
            tryExplicitRoute: function (pathFragment, secondaryRoutePart) {
                if (typeof this[pathFragment] == 'function') {
                    // render explicit route
                    this[pathFragment](secondaryRoutePart);
                }
            },
            notFoundRoute: function (path) {
                this.showView(new Restdude.view.NotFoundView());
            },
            editItem: function (item) {
                //console.log("MainController#editItem, item: " + item);
            }

        });

        return Restdude;

    });
