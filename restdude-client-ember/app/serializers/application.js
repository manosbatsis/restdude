// app/serializers/application.js
import Ember from 'ember';
import DS from 'ember-data';

export default DS.JSONAPISerializer.extend({
  keyForAttribute(key) { return key; },
  /*keyForRelationship: function(key) {
      return Ember.String.decamelize(key);
  }*/
  normalizeQueryResponse(store, clazz, payload) {
    const result = this._super(...arguments);
    result.meta = result.meta || {};

    if (payload.links) {
      result.meta.documentLinks = payload.links;
    }
    console.log("normalizeQueryResponse, documentLinks: ");
    console.log(result.meta.documentLinks);

    return result;
  }
});
