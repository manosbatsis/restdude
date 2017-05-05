// app/models/user.js
import DS from 'ember-data';

export default DS.Model.extend({

username: DS.attr('string'),
email: DS.attr('string'),
password: DS.attr('string'),
passwordConfirmation: DS.attr('string'),
permissions: DS.atrr('number')
});
