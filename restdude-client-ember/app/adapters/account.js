// app/adapters/account.js
import ApplicationAdapter from './application';
import config from "../config/environment";

export default ApplicationAdapter.extend({

  namespace: `${config.namespaceAuth}`,

});
