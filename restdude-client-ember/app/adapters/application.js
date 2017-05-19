// app/adapters/application.js
import DS from "ember-data";
import DataAdapterMixin from "ember-simple-auth/mixins/data-adapter-mixin";
import config from "../config/environment";

export default DS.JSONAPIAdapter.extend(DataAdapterMixin, {

  host: `${config.host}`,
  namespace: `${config.namespace}`,
  authorizer: `${config.authorizer}`,

  // TODO: description  and param documentation
  /**
   * This method overrides DataAdapterMixin.urlForQueryRecord() to...
   *
   * @augments DataAdapterMixin
   * @param query
   * @returns {string}
   */
  urlForQueryRecord(query) {
    if (query.me) {
      delete query.me;
      return `${this._super(...arguments)}/me`;
    }

    return this._super(...arguments);
  }

});
