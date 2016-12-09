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
define(["lib/calipsolib/form", "lib/calipsolib/backgrid", 'underscore', 'handlebars', 'moment', 'backbone', 'backgrid', 'backbone-forms', 'backbone-forms-bootstrap3', 'marionette',
        'bloodhound', 'typeahead', 'bootstrap-datetimepicker', 'bootstrap-switch', 'intlTelInput'],
    function (Calipso, CalipsoBackgrid, _, Handlebars, moment, Backbonel, Backgrid, BackboneForms, BackboneFormsBootstrap, BackboneMarionette, Bloodhoud, Typeahead, BackboneDatetimepicker, BootstrapSwitch, intlTelInput) {

        Calipso.fields = {};

        var Marionette = Backbone.Marionette;
        // Base attribute dataType
        Calipso.fields.Base = Marionette.Object.extend({
            hideNonEmpty: false
        }, {});
        /*
         Calipso.fields.Base.extend = function(protoProps, staticProps) {
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

        Calipso.fields.RelatedModel = Calipso.fields.Base.extend({}, {
            "form": {
                type: Calipso.backboneform.TypeaheadObject, //'Text'
                minLength: 1,
                typeaheadSource: {
                    displayKey: "name",
                }
            },
            "backgrid": {
                editable: false,
                sortable: true,
                cell: Calipso.components.backgrid.RelatedModelCell
            },
        });

        Calipso.fields.Hidden = Calipso.fields.hidden = Calipso.fields.Base.extend({}, {
            "form": {
                type: "Hidden",
                validators: [/*'required'*/],
            }
        });
        Calipso.fields.String = Calipso.fields.string = Calipso.fields.Base.extend({}, {
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
        Calipso.fields.SimpleSearch = Calipso.fields.Base.extend({}, {
            "form": {
                type: "Text",
                validators: [/*'required'*/],
            }
        });
        Calipso.fields.Text = Calipso.fields.text = Calipso.fields.String.extend({}, {
            "backgrid": {
                editable: false,
                sortable: true,
                cell: Calipso.components.backgrid.TextCell,
            },
            "form": {
                type: "TextArea",
                validators: [/*'required'*/],
            }
        });
        Calipso.fields.Boolean = Calipso.fields.boolean = Calipso.fields.bool = Calipso.fields.Base.extend({}, {
            "form": {
                type: "Checkbox",
                validators: [/*'required'*/],
            },
            "backgrid": {
                editable: false,
                sortable: true,
                cell: Calipso.components.backgrid.BooleanIconCell,
            },
        });
        Calipso.fields.Integer = Calipso.fields.integer = Calipso.fields.int = Calipso.fields.Base.extend({}, {
            "backgrid": {
                editable: false,
                sortable: true,
                cell: "Integer",
            },
            "form": {
                type: Calipso.backboneform.Number,
                validators: [/*'required'*/],
            }
        });
        Calipso.fields.Decimal = Calipso.fields.decimal = Calipso.fields.Float = Calipso.fields.float = Calipso.fields.Base.extend({}, {
            "backgrid": {
                editable: false,
                sortable: false,
                cell: "Number",
            },
            "form": {
                type: Calipso.backboneform.Number,
                validators: [/*'required'*/],
            }
        });
        Calipso.fields.Money = Calipso.fields.money = Calipso.fields.Decimal.extend({}, {
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
        Calipso.fields.Datetime = Calipso.fields.DateTime = Calipso.fields.datetime = Calipso.fields.Integer.extend({}, {
            "backgrid": {
                editable: false,
                sortable: false,
                cell: "Datetime",
            },
            "form": {
                type: Calipso.backboneform.Datetimepicker,
                validators: [/*'required'*/],
                config: {
                    locale: Calipso.util.getLocale(),
                    format: 'YYYY-MM-DD HH:mm',
                    viewMode: 'months',
                    widgetPositioning: {
                        //horizontal : "right"
                    }
                },
            }
        });
        Calipso.fields.Date = Calipso.fields.date = Calipso.fields.Datetime.extend({}, {
            "backgrid": {
                editable: false,
                sortable: false,
                cell: "Date",
            },
            "form": {
                type: Calipso.backboneform.Datetimepicker,
                validators: [/*'required'*/],
                config: {
                    locale: Calipso.util.getLocale(),
                    format: 'YYYY-MM-DD',
                    viewMode: 'months',
                    widgetPositioning: {
                        //horizontal : "right"
                    }
                },
            }
        });
        Calipso.fields.Time = Calipso.fields.time = Calipso.fields.Datetime.extend({}, {
            "backgrid": {
                editable: false,
                sortable: false,
                cell: "Time",
            },
            "form": {
                type: Calipso.backboneform.Datetimepicker,
                validators: [/*'required'*/],
                config: {
                    locale: Calipso.util.getLocale(),
                    format: 'HH:mm',
                    //viewMode : 'months',
                    widgetPositioning: {
                        //horizontal : "right"
                    }
                }
            }
        });

        Calipso.fields.EnumValue = Calipso.fields.enumValue = Calipso.fields.String.extend({}, {
            "form": {
                type: Backbone.Form.editors.ModelSelect2,
            }
        });
        Calipso.fields.Lov = Calipso.fields.lov = Calipso.fields.Base.extend({}, {
            form: {
                type: 'List',
                itemType: 'NestedModel',
            }
        });

        Calipso.fields.List = Calipso.fields.list = Calipso.fields.Base.extend({}, {
            "form": {
                type: Backbone.Form.editors.ModelSelect2,
            }
        });

        Calipso.fields.Email = Calipso.fields.email = Calipso.fields.String.extend({}, {
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

        Calipso.fields.Tel = Calipso.fields.tel = Calipso.fields.String.extend({}, {
            "backgrid": {
                editable: false,
                sortable: false,
                cell: "Text"
            },
            "form": {
                type: Calipso.backboneform.Tel,
                validators: [Calipso.backboneform.validators.digitsOnly]
            }
        });
        Calipso.fields.CsvExport = Calipso.fields.csvExport = Calipso.fields.Base.extend({}, {
            "backgrid": {
                editable: false,
                sortable: false,
                cell: Calipso.components.backgrid.CsvExportCell,
                headerCell: Calipso.components.backgrid.IconHeaderCell.extend({
                    icon: "fa fa-file-excel-o",
                })
            },
        });

        Calipso.fields.Link = Calipso.fields.link = Calipso.fields.String.extend({}, {});

        Calipso.fields.File = Calipso.fields.file = Calipso.fields.Base.extend({}, {});

        Calipso.fields.Image = Calipso.fields.image = Calipso.fields.img = Calipso.fields.File.extend({}, {});

        Calipso.fields.Color = Calipso.fields.color = Calipso.fields.Colour = Calipso.fields.colour = Calipso.fields.String.extend({}, {});

        Calipso.fields.Json = Calipso.fields.json = Calipso.fields.Text.extend({}, {});

        Calipso.fields.Markdown = Calipso.fields.markdown = Calipso.fields.md = Calipso.fields.Text.extend({}, {});

        Calipso.fields.Html = Calipso.fields.html = Calipso.fields.Text.extend({}, {});

        Calipso.fields.Csv = Calipso.fields.csv = Calipso.fields.Text.extend({}, {});

        Calipso.fields.Password = Calipso.fields.password = Calipso.fields.pwd = Calipso.fields.Base.extend({}, {
            "form": {
                type: Calipso.backboneform.Password,
                validators: ['required'],
            }
        });

        Calipso.fields.ConfirmPassword = Calipso.fields.Base.extend({}, {
            "form": {
                type: 'Password',
                validators: ['required', {
                    type: 'match',
                    field: 'password',
                    message: 'Passwords must match!'
                }],
            }
        });

        Calipso.fields.CurrentPassword = Calipso.fields.Base.extend({}, {
            "form": {
                type: 'Password',
                validators: ['required', function checkPassword(value, formValues) {
                    // verify current password
                    var userDetails = new Calipso.Model({
                        email: Calipso.session.userDetails.get("email"),
                        password: value
                    });
                    userDetails.save(null, {
                        async: false,
                        url: new Calipso.model.UserDetailsModel().url() + "/verification",
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

        Calipso.fields.Edit = Calipso.fields.edit = Calipso.fields.Base.extend({}, {
            "backgrid": {
                label: "edit",
                editable: false,
                sortable: false,
                cell: Calipso.components.backgrid.EditRowInModalCell,
                headerCell: Calipso.components.backgrid.ActionsIconCell
            },
            "form": null
        });

        return Calipso;

    });