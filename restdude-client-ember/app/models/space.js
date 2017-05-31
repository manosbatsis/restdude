import DS from 'ember-data';
import BaseContext from "./base-context";

export default BaseContext.extend({
  avatarUrl: DS.attr('string'),
  bannerUrl: DS.attr('string'),
  visibility: DS.attr('string', { defaultValue: "CLOSED" }),
  createdDate: DS.attr('utc'),
  owner: DS.belongsTo('user'),
  memberships: DS.hasMany('context-membership'),
  membershipRequests: DS.hasMany('context-membership-request')

});
