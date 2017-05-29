// app/adapters/members.js
import ApplicationAdapter from './application';
import config from "../config/environment";

export default ApplicationAdapter.extend({
  pathForType: function(type) {
   return this._super(Ember.String.camelize(type));
 },
  namespace: `${config.namespaceMembers}`,

});
