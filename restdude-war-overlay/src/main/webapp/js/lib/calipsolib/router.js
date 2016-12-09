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
    ['underscore', 'backbone', 'marionette', 'lib/calipsolib/controller'],
    function (_, Backbone, Marionette, Calipso) {

        /**
         * Base class for your Application Router
         */
        Calipso.AppRouter = Marionette.AppRouter.extend({

                //controller : new Calipso.Controller(),

                appRoutes: {

                    '': 'home',
                    '_=_': 'toHome',
                    '#_=_': 'toHome',
                    'home': 'home',
                    'register': 'register',
                    'login': 'login',
                    'logout': 'logout',
                    'myProfile': 'myProfile',

                    'userDetails/:useCaseKey': 'showUserDetailsView',
                    'userDetails/:useCaseKey/': 'showUserDetailsView',
                    'userDetails/:useCaseKey?*queryString': 'showUserDetailsView',
                    'userDetails/:useCaseKey/?*queryString': 'showUserDetailsView',

                    'page/:templateName': 'templatePage',
                    'useCases/:pathFragment': 'showEntitySearch',
                    'useCases/:pathFragment/': 'showEntitySearch',
                    'useCases/:pathFragment/?*queryString': 'showEntitySearch',
                    'useCases/:pathFragment?*queryString': 'showEntitySearch',

                    'useCases/:pathFragment/:modelId': 'showEntityView',
                    'useCases/:pathFragment/:modelId/:useCaseKey': 'showUseCaseView',

                    '*path': 'notFoundRoute'
                }

            },
            {
                // static members
                getTypeName: function () {
                    return "Calipso.AppRouter"
                }
            });

        // override the extend function to mere own and child routes for new type
        Calipso.AppRouter.extend = function (child) {
            var router = Marionette.AppRouter.extend.apply(this, arguments);
            router.prototype.appRoutes = _.extend({}, this.prototype.appRoutes, child.appRoutes);
            return router;
        };

        return Calipso;

    });
