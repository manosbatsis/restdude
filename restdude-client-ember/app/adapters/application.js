// app/adapters/application.js
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
  authorizer: `${config.authorizer}`

});
