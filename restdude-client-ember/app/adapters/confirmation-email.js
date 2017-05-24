// app/adapters/confirmation-email.js
//import ApplicationAdapter from './application';
import config from "../config/environment";
import DS from "ember-data";

export default DS.RESTAdapter.extend({

  namespace: `${config.namespaceConfirm}`,

});
