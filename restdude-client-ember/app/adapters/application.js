// app/adapters/application.js
import DS from 'ember-data';
import DataAdapterMixin from 'ember-simple-auth/mixins/data-adapter-mixin';

export default DS.JSONAPIAdapter.extend(DataAdapterMixin, {
  namespace: 'restdude/api/rest',
  //namespace: 'api/auth/jwt/access',
  host: 'http://localhost:8080/api/auth/jwt/access',
  authorizer: 'authorizer:token'
});
