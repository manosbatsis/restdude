// app/models/user.js
import DS from 'ember-data';

export default DS.Model.extend({

username: DS.attr(),
email: DS.attr(),
password: DS.attr(),
passwordConfirmation: DS.attr(),
permissions: DS.attr()
});
