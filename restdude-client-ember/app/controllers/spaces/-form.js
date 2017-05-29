
import Ember from 'ember';

const { Controller, computed, RSVP, A, run } = Ember;

export default Controller.extend({

  options: computed(function() {
    return A('SECRET CLOSED OPEN PUBLIC'
            .split(' ').map((option) => ({ abbrev: option })));
  }),

  optionsState: computed(function() {
    return this.get('options').objectAt(4);
  })


});
