// app/adapters/application.js
import Ember from "ember";
import DS from "ember-data";
import {singularize, pluralize} from 'ember-inflector';
import DataAdapterMixin from "ember-simple-auth/mixins/data-adapter-mixin";
import ENV from "../config/environment";

export default DS.JSONAPIAdapter.extend(DataAdapterMixin, {

  host: `${ENV.host}`,
  namespace: `${ENV.namespace}`,
  authorizer: `${ENV.authorizer}`,
  headers: {
    'Accept': 'application/vnd.api+json;charset=UTF-8',
    'Content-type': 'application/vnd.api+json;charset=UTF-8',
   // 'x-vendor-appid': '123',
   // 'x-vendor-secret': '12345'
  },

  pathForType: function(type) {
    console.log("pathForType, type: " + type);
    const newType = Ember.String.camelize(type);
    console.log("pathForType newType: " + newType);
    return pluralize(newType);
  },
  ajax: function(url, type, hash) {

    if (ENV.APP.usingCors) {
      if (hash === undefined) { hash = {}; }

      hash.crossDomain = true;

      if (ENV.APP.corsWithCreds) {
        hash.xhrFields = { withCredentials: true };
      }
    }

    console.log("ajax custom headers");
    if (this.headers !== undefined) {
      var headers = this.headers;
      if (hash) {
        hash.beforeSend = function (xhr) {
          Ember.keys(headers).forEach(function(key) {
            xhr.setRequestHeader(key, headers[key]);
          });
        };
      }
    }
    return this._super(url, type, hash);
  },

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
