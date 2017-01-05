/*
 *
 * Restdude
 * -------------------------------------------------------------------
 * Module restdude-war-overlay, https://manosbatsis.github.io/restdude/restdude-war-overlay
 *
 * Full stack, high level framework for horizontal, model-driven application hackers.
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
define(["lib/restdudelib/form", "lib/restdudelib/backgrid", 'underscore', 'handlebars', 'moment', 'backbone', 'backgrid', 'backbone-forms', 'backbone-forms-bootstrap3', 'marionette',
        'bloodhound', 'typeahead', 'bootstrap-datetimepicker', 'bootstrap-switch', 'intlTelInput'],
    function (Restdude, RestdudeBackgrid, _, Handlebars, moment, Backbonel, Backgrid, BackboneForms, BackboneFormsBootstrap, BackboneMarionette, Bloodhoud, Typeahead, BackboneDatetimepicker, BootstrapSwitch, intlTelInput) {

        Restdude.fields = {};

        var Marionette = Backbone.Marionette;
        // Base attribute dataType
        Restdude.fields.Base = Marionette.Object.extend({
            hideNonEmpty: false
        }, {});
        /*
         Restdude.fields.Base.extend = function(protoProps, staticProps) {
         // Call default extend method
         var extended = Backbone.Marionette.extend.call(this, protoProps, staticProps);
         // Add a usable super method for better inheritance
         extended.prototype._super = this.prototype;
         // Apply new or different defaults on top of the original
         if (protoProps.defaults && this.prototype.defaults) {
         extended.prototype.defaults = _.deepExtend({}, this.prototype.defaults, extended.prototype.defaults);
         }
         return extended;
         };
         */

        Restdude.fields.RelatedModel = Restdude.fields.Base.extend({}, {
            "form": {
                type: Restdude.backboneform.TypeaheadObject, //'Text'
                minLength: 1,
                typeaheadSource: {
                    displayKey: "name",
                }
            },
            "backgrid": {
                editable: false,
                sortable: true,
                cell: Restdude.components.backgrid.RelatedModelCell
            },
        });

        Restdude.fields.Hidden = Restdude.fields.hidden = Restdude.fields.Base.extend({}, {
            "form": {
                type: "Hidden",
                validators: [/*'required'*/],
            }
        });
        Restdude.fields.String = Restdude.fields.string = Restdude.fields.Base.extend({}, {
            "backgrid": {
                editable: false,
                sortable: true,
                cell: "Text",
            },
            "form": {
                type: "Text",
                validators: [/*'required'*/],
            }
        });
        Restdude.fields.SimpleSearch = Restdude.fields.Base.extend({}, {
            "form": {
                type: "Text",
                validators: [/*'required'*/],
            }
        });
        Restdude.fields.Text = Restdude.fields.text = Restdude.fields.String.extend({}, {
            "backgrid": {
                editable: false,
                sortable: true,
                cell: Restdude.components.backgrid.TextCell,
            },
            "form": {
                type: "TextArea",
                validators: [/*'required'*/],
            }
        });
        Restdude.fields.Boolean = Restdude.fields.boolean = Restdude.fields.bool = Restdude.fields.Base.extend({}, {
            "form": {
                type: "Checkbox",
                validators: [/*'required'*/],
            },
            "backgrid": {
                editable: false,
                sortable: true,
                cell: Restdude.components.backgrid.BooleanIconCell,
            },
        });
        Restdude.fields.Integer = Restdude.fields.integer = Restdude.fields.int = Restdude.fields.Base.extend({}, {
            "backgrid": {
                editable: false,
                sortable: true,
                cell: "Integer",
            },
            "form": {
                type: Restdude.backboneform.Number,
                validators: [/*'required'*/],
            }
        });
        Restdude.fields.Decimal = Restdude.fields.decimal = Restdude.fields.Float = Restdude.fields.float = Restdude.fields.Base.extend({}, {
            "backgrid": {
                editable: false,
                sortable: false,
                cell: "Number",
            },
            "form": {
                type: Restdude.backboneform.Number,
                validators: [/*'required'*/],
            }
        });
        Restdude.fields.Money = Restdude.fields.money = Restdude.fields.Decimal.extend({}, {
            "backgrid": {
                editable: false,
                sortable: false,
                cell: "Number",
            },
            "form": {
                type: "Number",
                validators: [/*'required'*/],
            }
        });
        Restdude.fields.Datetime = Restdude.fields.DateTime = Restdude.fields.datetime = Restdude.fields.Integer.extend({}, {
            "backgrid": {
                editable: false,
                sortable: false,
                cell: "Datetime",
            },
            "form": {
                type: Restdude.backboneform.Datetimepicker,
                validators: [/*'required'*/],
                config: {
                    locale: Restdude.util.getLocale(),
                    format: 'YYYY-MM-DD HH:mm',
                    viewMode: 'months',
                    widgetPositioning: {
                        //horizontal : "right"
                    }
                },
            }
        });
        Restdude.fields.Date = Restdude.fields.date = Restdude.fields.Datetime.extend({}, {
            "backgrid": {
                editable: false,
                sortable: false,
                cell: "Date",
            },
            "form": {
                type: Restdude.backboneform.Datetimepicker,
                validators: [/*'required'*/],
                config: {
                    locale: Restdude.util.getLocale(),
                    format: 'YYYY-MM-DD',
                    viewMode: 'months',
                    widgetPositioning: {
                        //horizontal : "right"
                    }
                },
            }
        });
        Restdude.fields.Time = Restdude.fields.time = Restdude.fields.Datetime.extend({}, {
            "backgrid": {
                editable: false,
                sortable: false,
                cell: "Time",
            },
            "form": {
                type: Restdude.backboneform.Datetimepicker,
                validators: [/*'required'*/],
                config: {
                    locale: Restdude.util.getLocale(),
                    format: 'HH:mm',
                    //viewMode : 'months',
                    widgetPositioning: {
                        //horizontal : "right"
                    }
                }
            }
        });

        Restdude.fields.EnumValue = Restdude.fields.enumValue = Restdude.fields.String.extend({}, {
            "form": {
                type: Backbone.Form.editors.ModelSelect2,
            }
        });
        Restdude.fields.Lov = Restdude.fields.lov = Restdude.fields.Base.extend({}, {
            form: {
                type: 'List',
                itemType: 'NestedModel',
            }
        });

        Restdude.fields.List = Restdude.fields.list = Restdude.fields.Base.extend({}, {
            "form": {
                type: Backbone.Form.editors.ModelSelect2,
            }
        });

        Restdude.fields.Email = Restdude.fields.email = Restdude.fields.String.extend({}, {
            "backgrid": {
                editable: false,
                sortable: false,
                cell: "Email"
            },
            "form": {
                type: "Text",
                validators: ['email'],
            },
        });

        Restdude.fields.Tel = Restdude.fields.tel = Restdude.fields.String.extend({}, {
            "backgrid": {
                editable: false,
                sortable: false,
                cell: "Text"
            },
            "form": {
                type: Restdude.backboneform.Tel,
                validators: [Restdude.backboneform.validators.digitsOnly]
            }
        });
        Restdude.fields.CsvExport = Restdude.fields.csvExport = Restdude.fields.Base.extend({}, {
            "backgrid": {
                editable: false,
                sortable: false,
                cell: Restdude.components.backgrid.CsvExportCell,
                headerCell: Restdude.components.backgrid.IconHeaderCell.extend({
                    icon: "fa fa-file-excel-o",
                })
            },
        });

        Restdude.fields.Link = Restdude.fields.link = Restdude.fields.String.extend({}, {});

        Restdude.fields.File = Restdude.fields.file = Restdude.fields.Base.extend({}, {});

        Restdude.fields.Image = Restdude.fields.image = Restdude.fields.img = Restdude.fields.File.extend({}, {});

        Restdude.fields.Color = Restdude.fields.color = Restdude.fields.Colour = Restdude.fields.colour = Restdude.fields.String.extend({}, {});

        Restdude.fields.Json = Restdude.fields.json = Restdude.fields.Text.extend({}, {});

        Restdude.fields.Markdown = Restdude.fields.markdown = Restdude.fields.md = Restdude.fields.Text.extend({}, {});

        Restdude.fields.Html = Restdude.fields.html = Restdude.fields.Text.extend({}, {});

        Restdude.fields.Csv = Restdude.fields.csv = Restdude.fields.Text.extend({}, {});

        Restdude.fields.Password = Restdude.fields.password = Restdude.fields.pwd = Restdude.fields.Base.extend({}, {
            "form": {
                type: Restdude.backboneform.Password,
                validators: ['required'],
            }
        });

        Restdude.fields.ConfirmPassword = Restdude.fields.Base.extend({}, {
            "form": {
                type: 'Password',
                validators: ['required', {
                    type: 'match',
                    field: 'password',
                    message: 'Passwords must match!'
                }],
            }
        });

        Restdude.fields.CurrentPassword = Restdude.fields.Base.extend({}, {
            "form": {
                type: 'Password',
                validators: ['required', function checkPassword(value, formValues) {
                    // verify current password
                    var userDetails = new Restdude.Model({
                        email: Restdude.session.userDetails.get("email"),
                        password: value
                    });
                    userDetails.save(null, {
                        async: false,
                        url: new Restdude.model.UserDetailsModel().url() + "/verification",
                    });
                    var err = {
                        type: 'password',
                        message: 'Incorrect current password'
                    };
                    if (!userDetails.get("id")) {
                        return err;
                    }
                }], //valida
            }
        });

        Restdude.fields.Edit = Restdude.fields.edit = Restdude.fields.Base.extend({}, {
            "backgrid": {
                label: "edit",
                editable: false,
                sortable: false,
                cell: Restdude.components.backgrid.EditRowInModalCell,
                headerCell: Restdude.components.backgrid.ActionsIconCell
            },
            "form": null
        });

        return Restdude;

    });