// app/adapters/confirmation-email.js
import ApplicationAdapter from './application';
import config from "../config/environment";

export default RESTAdapter.extend({

  namespace: `${config.namespaceConfirm}`,

});
