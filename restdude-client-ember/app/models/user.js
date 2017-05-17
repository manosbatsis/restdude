// app/models/user.js
import DS from "ember-data";

export default DS.Model.extend({
  pk: DS.attr('string'),
  username: DS.attr('string'),
  firstName: DS.attr('string'),
  lastName: DS.attr('string'),
  email: DS.attr('string'),
  password: DS.attr('string'),
  passwordConfirmation: DS.attr('string')
});
