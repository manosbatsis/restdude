import DS from 'ember-data';

export default DS.Model.extend({
  name: DS.attr('string'),
  title: DS.attr('string'),
  description: DS.attr('string'),
  avatarUrl: DS.attr('string'),
  bannerUrl: DS.attr('string'),
  visibility: DS.attr('string', { defaultValue: "CLOSED" }),
  createdDate: DS.attr('utc'),
  owner: DS.belongsTo('user'),
  memberships: DS.hasMany('context-membership'),
  membershipRequests: DS.hasMany('context-membership-request')

});
