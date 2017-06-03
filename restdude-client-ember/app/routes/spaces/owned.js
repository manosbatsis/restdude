import Ember  from 'ember';
import BaseSearch  from './base-search';

export default BaseSearch.extend( {
  sessionAccount: Ember.inject.service(),
  afterModel: function(model, transition) {
    this._super(model, transition);
    const links = model.meta.documentLinks;
    console.log("owned afterModel, links: ");
    console.log(links);

  },
  model: function(params) {
    var user =  this.get('sessionAccount.account');
    console.log("joined#model, user: ");
    console.log(user);
    if(!user || !user.id){
      throw "No user ID was found in session";
    }
    return this.store.query('space', {
      page: {
        number: params.number,
        size: params.size,
      },
      owner: user.id
    });
  },

});
