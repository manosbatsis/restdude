import DS from 'ember-data';
import ActiveModelAdapter from 'active-model-adapter'; 
import DataAdapterMixin from 'ember-simple-auth/mixins/data-adapter-mixin';

export default DS.JSONAPIAdapter.extend({
  namespace: 'api'
});

export default DS.ActiveModelAdapter.extend(DataAdapterMixin, {  
  namespace: 'api/v1',
  host: 'http://localhost:4200',
  authorizer: 'authorizer:devise'
});

export default DS.JSONAPIAdapter.extend(DataAdapterMixin, {
  authorizer: 'authorizer:oauth2'
});
