import ApplicationAdapter from './application';
import config from "../config/environment";

export default ApplicationAdapter.extend({
  namespace: `${config.namespaceAuth}`,
  
  urlForQueryRecord(query) {
    if (query.me) {
      delete query.me;
      return `${this._super(...arguments)}/me`;
    }

    return this._super(...arguments);
  }

});
