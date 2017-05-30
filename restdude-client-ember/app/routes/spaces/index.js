import BaseAuthenticated  from '../base-authenticated';

export default BaseAuthenticated.extend( {
  actions: {
    remove: function(model) {
      if(confirm('Are you sure?')) {
        model.destroyRecord();
      }
    }
  },
  model: function(params) {
    return this.store.query('space', {
      page: {
        number: params.number,
        size: params.size
      }
    });
  },
  queryParams: {
    page: {
      refreshModel: true
    },
    size: {
      refreshModel: true
    }
  },
  afterModel: function(model, transition) {
    const links = model.meta.documentLinks;
    console.log("afterModel, links: ");
    console.log(links);

  }
});
