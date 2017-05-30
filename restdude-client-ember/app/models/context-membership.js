import DS from 'ember-data';

export default DS.Model.extend({
  admin: DS.attr('boolean'),
  user: DS.attr(),//DS.internalModel('user'),
  createdDate: DS.attr('utc'),
  space: DS.belongsTo('space'),
});
