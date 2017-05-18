import DS from "ember-data";
import DataAdapterMixin from "ember-simple-auth/mixins/data-adapter-mixin";
<<<<<<< HEAD
import config from "super-rentals/config/environment";
=======
import config from "./config/environment";
>>>>>>> origin/master

export default DS.JSONAPIAdapter.extend(DataAdapterMixin, {

  host: `${config.host}`,
  namespace: `${config.namespace}`,
<<<<<<< HEAD
  authorizer: `${config.authorizer}`,

  urlForQueryRecord(query) {
    if (query.me) {
      delete query.me;
      return `${this._super(...arguments)}/me`;
    }

    return this._super(...arguments);
  }
=======
  authorizer: `${config.authorizer}`
>>>>>>> origin/master
});
