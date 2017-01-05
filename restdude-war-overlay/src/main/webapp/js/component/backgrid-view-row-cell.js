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
define(['vent', 'backbone-forms', 'backgrid', 'bootstrap-markdown',
    'backbone-forms-bootstrap3'], function (vent, BackboneForm, Backgrid,
                                            Markdown) {
    var ViewRowCell = Backgrid.StringCell.extend({
        className: "view-row-cell",
        initialize: function (options) {
            Backgrid.StringCell.prototype.initialize.apply(this, arguments);
            this.viewRowEvent = "collectionView:viewItem";
        },
        events: {
            "click": "viewRow"
        },
        viewRow: function (e) {
            console.log("ViewRowCell#viewRow, rowModel: "
                + this.model.getTypeName());
            e.stopPropagation();
            e.preventDefault();

            vent.trigger(this.viewRowEvent, this.model);
            // var rowModel = this.model;
            // Backbone.history.navigate("client/"+rowModel.get("apiUrlSegment")+"/"+rowModel.get("id"),
            // {
            // trigger : true
            // });
        },
        render: function () {
            this.$el.empty();
            var model = this.model;
            var formattedValue = this.formatter.fromRaw(model.get(this.column
                .get("name")), model);
            this.$el.append($("<a>", {
                tabIndex: -1,
                title: formattedValue
            }).text(formattedValue));
            this.delegateEvents();
            return this;
        }

    });
    return ViewRowCell;
});