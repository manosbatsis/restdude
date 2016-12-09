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
define(['backbone-bootstrap-modal', 'backbone-forms', 'backgrid', 'bootstrap-markdown', 'backbone-forms-bootstrap3'],
    function (BackboneBootstrapModal, BackboneForm, Backgrid, Markdown) {

        var EditInModalCell = Backgrid.Cell.extend({
            tagName: "td class='modal-button-cell modal-button-cell-edit'",
            template: _.template('<button class="btn btn-xs btn-link"><span class="fa fa-pencil-square-o"></span></button>'),
            events: {
                "click": "editRow"
            },
            markdownEditorIds: [], // initialise an empty array
            editRow: function (e) {
                var thisModal = this;
                console.log("editRow ");
                e.stopPropagation();
                e.preventDefault();
                var rowModel = this.model;
                console.log("editRow, rowModel: " + rowModel);
                var rowModelSchema = rowModel.isNew() ? rowModel.schema("create") : rowModel.schema("update");
                console.log("editRow, rowModelSchema: " + rowModelSchema);

                var form = new Backbone.Form({
                    model: rowModel,
                    schema: rowModelSchema,
                });
                console.log("editRow, form: " + form);
                // keep note of markdown editors we need to initialize after rendering
                form.listenTo('source:render', function (form, markdownEditor, extra) {
                    console.log('source:render fired, id: "' + markdownEditor.id + '".');
                    thisModal.markdownEditorIds.push(markdownEditor.id);
                    console.log("modal.on shown.bs.modal, markdownEditorIds: " + thisModal.markdownEditorIds);
                });
                console.log("editRow, rowModel: " + rowModel);
                var modalTitle = rowModel.isNew() ? "New" : rowModel.get("name");
                console.log("editRow, modal title: " + modalTitle);
                var modal = new Backbone.BootstrapModal({
                    animate: true,
                    title: modalTitle,
                    content: form,
                    okBtn: 'save',
                });
                console.log("editRow, modal: " + modal);
                modal.listenTo('shown', function (e) {
                    console.log("modal.on shown, markdownEditorIds: " + thisModal.markdownEditorIds);
                    // initialize markdown editors
                    $('textarea[data-provide="markdown"]').each(function () {
                        var $this = $(this);
                        if ($.inArray($this.attr('id'), thisModal.markdownEditorIds) > -1) {
                            if ($this.data('markdown')) {
                                $this.data('markdown').showEditor()
                            }
                            else {
                                $this.markdown($this.data())
                            }
                        }
                    });
                });

                modal.on('ok', function (e) {
                    console.log("modal.on ok");
                    var errs = form.commit();
                    if (errs) {
                        return modal.preventClose();
                    }
                    var sUrl = rowModelSchema.url;
                    sUrl = sUrl + (base.charAt(sUrl.length - 1) === '/' ? '' : '/') + encodeURIComponent(this.id);
                    rowModelSchema.save({}, {url: sUrl});
                });
                console.log("opening modal: " + modal);
                modal.open();

            },
            render: function () {
                this.$el.html(this.template());
                this.delegateEvents();
                return this;
            }
        });
        return EditInModalCell;
    });