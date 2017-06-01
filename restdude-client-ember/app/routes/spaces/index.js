import BaseSearch  from './base-search';

export default BaseSearch.extend( {
  beforeModel: function(model, transition) {

    this.transitionTo('spaces.joined');

  }
});
