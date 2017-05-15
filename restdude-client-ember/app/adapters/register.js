import DS from 'ember-data';
import DataAdapterMixin from 'ember-simple-auth/mixins/data-adapter-mixin';

export default DS.JSONAPIAdapter.extend(DataAdapterMixin, {
  //namespace: 'restdude/api/rest',
  host: 'http://localhost:8080/restdude/api/rest',
  namespace: 'api/auth/account',
  authorizer: 'authorizer:token'
});
