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
define(['vent', 'restdude/component/backgrid-view-row-cell'],
    function (vent, ViewRowCell) {
        var ViewRowMaximizedCell = ViewRowCell.extend({
            className: "view-row-maximized-cell",
            initialize: function (options) {
                ViewRowCell.prototype.initialize.apply(this, arguments);
                this.viewRowEvent = "collectionView:viewItemMaximized";
            }
        });
        return ViewRowMaximizedCell;
    });