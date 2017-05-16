import DS from "ember-data";
import DataAdapterMixin from "ember-simple-auth/mixins/data-adapter-mixin";

export default DS.JSONAPIAdapter.extend(DataAdapterMixin, {
  host: `${config.host}`,
  namespace: `${config.namespace}`,
  authorizer: 'authorizer:custom'
});
