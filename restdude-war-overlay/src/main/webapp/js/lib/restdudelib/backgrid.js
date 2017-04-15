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
    ["lib/restdudelib/util", 'underscore', 'handlebars', 'moment', 'backbone', 'backbone.paginator',
        'marionette',
        'backgrid', 'backgrid-moment', 'backgrid-text', 'backgrid-paginator',
        'bootstrap-switch'],
    function (Restdude, _, Handlebars, moment, Backbone, PageableCollection,
              BackboneMarionette,
              Backgrid, BackgridMoment, BackgridText, BackgridPaginator,
              BootstrapSwitch) {

        var labels = Restdude.util.getLabels();

        /*
         Overwrite backgrid prototype members
         */
        // Override column defaults globally
        Backgrid.Column.prototype.defaults.sortType = "toggle";
        // make backgrid tables responsive
        var BackgridCellInitialize = Backgrid.Cell.prototype.initialize;
        Backgrid.Cell.prototype.initialize = function () {
            BackgridCellInitialize.apply(this, arguments);
            this.$el.attr("data-title", this.column.get("label"));
        }

        /*
         Extend backgrid types
         */
        Restdude.components.backgrid = {};


        Restdude.components.backgrid.Caption = Backbone.View.extend({
            /** @property */
            tagName: "caption",

            initialize: function (options) {
                Backbone.View.prototype.initialize.apply(this, arguments);
                this.collection = options.collection;

                var _this = this;
                this.listenTo(this.collection, 'reset', function (model) {
                    _this.render();
                });
            },
            getResultsInfo: function () {
                var resultsInfo = _.extend({}, this.collection.state);
                var pastResults = (resultsInfo.pageSize * (resultsInfo.currentPage - resultsInfo.firstPage));
                resultsInfo.pageStart = pastResults + 1;
                resultsInfo.pageEnd = pastResults + this.collection.length;
                return resultsInfo;
            },
            render: function () {
                var resultsInfo = this.getResultsInfo();
                this.$el.empty().append(labels.restdude.words.showing + " " + resultsInfo.pageStart + " - " +
                    resultsInfo.pageEnd + " " + labels.restdude.words.of + " " + resultsInfo.totalRecords + " &#160;" +
                    '<button class="btn btn-primary btn-sm layout-showCreateFormModal"><i class="fa fa-plus-square fa-fw" aria-hidden="true"></i>&#160;' + labels.restdude.words.create + " " + labels.restdude.words.new + '</button>');
                return this;
            },
        });


        Restdude.components.backgrid.SmartHighlightRow = Backgrid.Row.extend({
            initialize: function () {
                Backgrid.Row.prototype.initialize.apply(this, arguments);
                /*this.listenTo(this.model, 'change', function(model) {
                 this.$el.toggleClass('bg-warning', model.hasChanged());
                 });*/
                this.listenTo(this.model, 'sync', function (model) {
                    // creating an empty element and applying our class to it to get bootstrap class bg color
                    var origBg = this.$el.css("background-color");
                    var bgcolor = $("<div>").appendTo("body").addClass("bg-success").css("background-color");

                    this.$el.removeClass('bg-warning');
                    this.$el.animate({
                        backgroundColor: bgcolor
                    }, {
                        queue: true,
                        duration: 1500
                    });
                    this.$el.animate({
                        backgroundColor: origBg
                    }, {
                        queue: true,
                        duration: 5000
                    });
                });
                this.listenTo(this.model, 'added', function (model) {
                    // creating an empty element and applying our class to it to get bootstrap class bg color
                    var origBg = this.$el.css("background-color");
                    var bgcolor = $("<div>").appendTo("body").addClass("bg-success").css("background-color");
                    this.$el.removeClass('bg-warning');
                    this.$el.animate({
                        backgroundColor: bgcolor
                    }, {
                        queue: true,
                        duration: 1500
                    });
                    this.$el.animate({
                        backgroundColor: origBg
                    }, {
                        queue: true,
                        duration: 5000
                    });
                });
            }
        });


        Restdude.components.backgrid.ViewRowCell = Backgrid.UriCell.extend(
            /** @lends Restdude.components.backgrid.RelatedModelCell.prototype */
            {
                className: "view-row-model-cell",
                target: "",
                pathFragment: null,
                useCase: "view",
                initialize: function (options) {
                    Backgrid.UriCell.prototype.initialize.apply(this, arguments);
                    this.pathFragment = options.column.get("pathFragment") || this.model.getPathFragment();
                    if (!this.pathFragment) {
                        throw "Required option missing: pathFragment";
                    }
                },
                render: function () {
                    this.$el.empty();

                    var modelId = Restdude.getObjectProperty(this.model, "pk", Restdude.getObjectProperty(this.model, "id"));
                    var text = this.model.get(this.column.get("name"));

                    if(modelId){

                        this.$el.append($("<a>", {
                            "class" : "view-row-model-cell-link",
                            tabIndex: -1,
                            href: "/useCases/" + this.pathFragment + "/" + modelId + "/" + this.useCase,
                            title: this.title || text,
                            target: this.target,
                            "data-id" : modelId,
                            "data-fragment" : this.pathFragment,
                        }).text(text));

                        this.delegateEvents();
                    }
                    else{
                        this.$el.append($("<span>", {}).text(text));
                    }

                    return this;
                }
            });

        Restdude.components.backgrid.RelatedModelCell = Backgrid.UriCell.extend(
        /** @lends Restdude.components.backgrid.RelatedModelCell.prototype */
        {
            className: "related-model-cell",
            target: "",
            pathFragment: null,
            useCase: "view",
            initialize: function (options) {
                Backgrid.UriCell.prototype.initialize.apply(this, arguments);
                this.pathFragment = options.column.get("pathFragment");
                if (!this.pathFragment) {
                    throw "Required option missing: pathFragment";
                }
            },
            render: function () {
                this.$el.empty();
                var rawValue = this.model.get(this.column.get("name"));
                if(rawValue){

                    var isSimple = _.isString(rawValue) || _.isNumber(rawValue);
                    var text = isSimple ? '<i class="fa fa-info-circle" aria-hidden="true"></i>' : (rawValue.name || rawValue.title || rawValue);
                    this.$el.append($("<a>", {
                        tabIndex: -1,
                        href: "/useCases/" + this.pathFragment + "/" + (isSimple ? rawValue : (rawValue[Restdude.config.idAttribute] || rawValue)) + "/" + this.useCase,
                        title: this.title || isSimple ? rawValue : (rawValue.description || text),
                        target: this.target
                    }).html(text));
                }

                return this;
            }
        });


        Restdude.components.backgrid.TextCell = Backgrid.StringCell.extend(
            /** @lends Restdude.components.backgrid.TextCell.prototype */
            {
                className: "text-cell",
                tooltipStarted: false,
                /** @property */
                events: {
                    "mouseenter": "addTooltipToTruncated",
                },
                initialize: function (options) {
                    Backgrid.StringCell.prototype.initialize.apply(this, arguments);
                },
                render: function () {
                    this.$el.empty();
                    var model = this.model;
                    this.$el.html("<span>" +
                        this.formatter.fromRaw(model.get(this.column.get("name")), model) +
                        "</span>");
                    this.delegateEvents();
                    return this;
                },
                addTooltipToTruncated: function () {
                    if (this.el.offsetWidth < this.el.scrollWidth) {
                        if (!this.tooltipStarted) {
                            this.tooltipStarted = true;
                            var $span = this.$("span:first");
                            $span.tooltip({
                                title: this.$el.text(),
                                placement: "bottom",
                                // container: 'body'
                            });
                            $span.tooltip('show');
                        }
                    }
                },
            });

        //

        Restdude.components.backgrid.BooleanCell = Backgrid.BooleanCell.extend(
            /** @lends Restdude.components.backgrid.BootstrapSwitchCell.prototype */
            {
                render: function () {
                    Backgrid.BooleanCell.prototype.render.apply(this, arguments);
                    this.$el.find("input").addClass("form-check");
                    return this;
                }
            });

        Restdude.components.backgrid.BooleanIconCell = Backgrid.StringCell.extend(
            /** @lends Restdude.components.backgrid.BootstrapSwitchCell.prototype */
            {
                render: function () {
                    this.$el.empty();
                    var model = this.model, column = this.column;
                    var editable = Backgrid.callByNeed(column.editable(), column, model);
                    var checked = model.get(column.get("name"));
                    this.$el.html('<span class="' + (checked ? "text-success" : "text-danger") + '">' +
                        '<li class="' + (checked ? "fa fa-check-square" : "fa fa-minus-square") + '" aria-hidden="true"> </li></span>');
                    this.delegateEvents();
                    return this;
                }
            });

        Restdude.components.backgrid.BootstrapSwitchCell = Backgrid.BooleanCell.extend(
            /** @lends Restdude.components.backgrid.BootstrapSwitchCell.prototype */
            {
                render: function () {
                    Backgrid.BooleanCell.prototype.render.apply(this, arguments);
                    var _this = this;
                    setTimeout(function () {
                        _this.$el.find("input").bootstrapSwitch();
                    }, 250);
                    return this;
                }
            });

        Restdude.components.backgrid.CsvExportCell = Backgrid.Cell.extend(
            /** @lends Restdude.components.backgrid.CsvExportCell.prototype */
            {
                className: "modal-button-cell renderable",
                render: function () {
                    var url = Restdude.getBaseUrl() + "/api/rest/" + this.model.getPathFragment() + "/" + this.model.get(Restdude.config.idAttribute) + "/csv";
                    this.$el.html("<a class='btn btn-xs btn-link' href='" + url + "'title='Export as Spreadsheet'><i class='fa fa-download'></i></a>");
                    //this.delegateEvents();
                    return this;
                }
            });


        Restdude.components.backgrid.ViewRowInModaLCell = Backgrid.StringCell.extend(
            /** @lends Restdude.components.backgrid.ViewRowInModaLCell.prototype */
            {
                className: "view-row-cell",
                initialize: function (options) {
                    Backgrid.StringCell.prototype.initialize.apply(this, arguments);
                    this.viewRowEvent = "layout:viewModel";
                },
                events: {
                    "click": "viewRow"
                },
                viewRow: function (e) {
                    Restdude.stopEvent(e);
                    var useCaseContext = this.model.getUseCaseContext({
                        key: "view"
                    });
                    Restdude.vent.trigger("modal:showUseCaseContext", useCaseContext);
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

        Restdude.components.backgrid.EditRowCell = Backgrid.Cell.extend(
            /** @lends Restdude.components.backgrid.EditRowCell.prototype */
            {
                tagName: "td",
                className: "modal-button-cell modal-button-cell-edit",
                events: {
                    "click": "editEntry",
                    "click button": "editEntry",
                },
                editEntry: function (e) {
                    Restdude.stopEvent(e);
                    var useCaseContext = this.model.getUseCaseContext({
                        key: "update"
                    });
                    Restdude.app.mainContentRegion.show(useCaseContext.createView());
                },
                render: function () {
                    this.$el.html("<button class='btn btn-xs btn-link' title='Edit entry'><i class='fa fa-pencil-square-o'></i></button>");
                    //this.delegateEvents();
                    return this;
                }
            });

        Restdude.components.backgrid.EditRowInModalCell = Restdude.components.backgrid.EditRowCell.extend(
            /** @lends Restdude.components.backgrid.EditRowCell.prototype */
            {
                editEntry: function (e) {
                    Restdude.stopEvent(e);
                    var useCaseContext = this.model.getUseCaseContext({
                        key: "update"
                    });
                    Restdude.vent.trigger("modal:showUseCaseContext", useCaseContext);
                }
            });

        Restdude.components.backgrid.ChildStringAttributeCell = Backgrid.StringCell.extend({
            render: function () {
                var path = this.column.get("path");
                if (!path) {
                    path = this.column.get("name");
                }
                var result = Restdude.getPathValue(this.model, path);
                if (!(_.isUndefined(result) || _.isNull(result))) {
                    this.$el.text(result);
                }
                this.delegateEvents();
                return this;
            },
        });

        Restdude.components.backgrid.ChildNumberAttributeCell = Backgrid.NumberCell.extend({
            render: function () {
                var path = this.column.get("path");
                if (!path) {
                    path = this.column.get("name");
                }
                var result = Restdude.getPathValue(this.model, path);
                if (!(_.isUndefined(result) || _.isNull(result))) {
                    this.$el.text(this.formatter.fromRaw(result));
                }
                this.delegateEvents();
                return this;
            },
        });

        Restdude.components.backgrid.CreateNewHeaderCell = Backgrid.HeaderCell.extend({
            tagName: "th",
            className: "renderable backgrid-create-new-header-cell",
            events: {
                "click": "createNewForManualEdit"
            },
            initialize: function (options) {
                Backgrid.HeaderCell.prototype.initialize.apply(this, arguments);
            },
            createNewForManualEdit: function (e) {
                Restdude.stopEvent(e);
                Restdude.vent.trigger("layout:createModel", {
                    modelType: this.collection.model
                });
            },
            render: function () {
                var html = $("<button title='Create new' class='btn btn-xs btn-success'><i class='fa fa-file-text fa-fw'></i>&nbsp;New</button>");
                this.$el.html(html);
                //this.delegateEvents();
                return this;
            }
        });

        Restdude.components.backgrid.IconHeaderCell = Backgrid.HeaderCell.extend({
            className: "actions",
            icon: "fa fa-cog",
            render: function () {
                this.$el.html("<i class='" + this.icon + "'></i>");
                return this;
            }
        });

        Restdude.components.backgrid.ActionsIconCell = Restdude.components.backgrid.IconHeaderCell.extend({
            icon: "fa fa-cog",
        });

        Restdude.components.backgrid.CreateNewInModalHeaderCell = Restdude.components.backgrid.CreateNewHeaderCell.extend({
            createNewForManualEdit: function (e) {
                Restdude.stopEvent(e);
                Restdude.vent.trigger("modal:showUseCaseContext", {
                    useCaseKey: "create",
                    modelType: this.collection.model,
                    childViewOptions: {
                        addToCollection: this.collection
                    }
                });
            },
        });

        Restdude.components.backgrid.Grid = Backgrid.Grid.extend({
            className: "backgrid table table-striped responsive-table",
            caption: Restdude.components.backgrid.Caption,
            emptyText: labels.restdude.grid.emptyText,
            initialize: function (options) {
                var _this = this;
                options.emptyText || (options.emptyText = this.emptyText);
                options.row || (options.row = Restdude.components.backgrid.SmartHighlightRow);
                Backgrid.Grid.prototype.initialize.apply(this, arguments);
                this.caption = options.caption || this.caption;
            },
            render: function () {
                Backgrid.Grid.prototype.render.apply(this, arguments);
                if (this.caption) {
                    this.$el.prepend(new this.caption({
                        collection: this.collection
                    }).render().$el);
                }
                return this;
            },
        });

    });