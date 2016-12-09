/*
 * Copyright (c) 2007 - 2016 Manos Batsis
 *
 * This file is part of Calipso, a software platform by www.Abiss.gr.
 *
 * Calipso is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Calipso is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Calipso. If not, see http://www.gnu.org/licenses/agpl.html
 */
define(['vent', 'backbone-forms', 'backgrid', 'bootstrap-markdown', 'backbone-forms-bootstrap3'],
    function (vent, BackboneForm, Backgrid, Markdown) {
        var ViewInTabCell = Backgrid.StringCell.extend({

            className: "view-in-tab-cell",
            events: {
                "click": "viewRow"
            },
            viewRow: function (e) {
                console.log("ViewInTabCell#editRow ");
                e.stopPropagation();
                e.preventDefault();
                var rowModel = this.model;
                console.log("editRow, rowModel: " + rowModel.getTypeName());
                vent.trigger("collectionView:viewItem", rowModel);


//				Backbone.history.navigate("client/"+rowModel.get("apiUrlSegment")+"/"+rowModel.get("id"), {
//					trigger : true
//				});
            },

            render: function () {
                this.$el.empty();
                var model = this.model;
                var formattedValue = this.formatter.fromRaw(model.get(this.column.get("name")), model);
                this.$el.append($("<a>", {
                    tabIndex: -1,
                    title: formattedValue
                }).text(formattedValue));
                this.delegateEvents();
                return this;
            }

        });
        return ViewInTabCell;
    });