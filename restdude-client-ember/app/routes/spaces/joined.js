import BaseSearch  from './base-search';

export default BaseSearch.extend( {
  afterModel: function(model, transition) {
    this._super(model, transition);
    const links = model.meta.documentLinks;
    console.log("joined afterModel, links: ");
    console.log(links);

  },

});
