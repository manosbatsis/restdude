import DS from 'ember-data';

export default DS.Model.extend({
  name: DS.attr('string'),
  legalName: DS.attr('string'),
  email: DS.attr('email')
});
