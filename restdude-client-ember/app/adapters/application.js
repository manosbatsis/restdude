// app/adapters/application.js
import DS from "ember-data";
import DataAdapterMixin from "ember-simple-auth/mixins/data-adapter-mixin";
import config from "./config/environment";

export default DS.JSONAPIAdapter.extend(DataAdapterMixin, {
  host: `${config.host}`,
  namespace: `${config.namespace}`,
  authorizer: 'authorizer:custom'
});
