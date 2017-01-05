/*
 * Copyright (c) 2007 - 2016 Manos Batsis
 *
 * This file is part of Restdude, a software platform by www.Abiss.gr.
 *
 * Restdude is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Restdude is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Restdude. If not, see http://www.gnu.org/licenses/agpl.html
 */
define(['marionette', 'restdude', 'controllers/MainController'], function (Marionette, Restdude, MainController) {

    /**
     * This is an example of extending Restdude.Approuter in your domain.
     *  Any appRoutes you define here will be merged with
     * the super type routes.
     */
    return Restdude.AppRouter.extend({

            controller: new MainController(),
            appRoutes: {
                // custom/explicit routes here
            }
        },
        {
            // static members
            getTypeName: function () {
                return "MainRouter"
            }
        });

});
