// app/controllers/user.js
import Ember from 'ember';
    export default Ember.Controller.extend({
    auth: Ember.inject.service('session'),
    actions: {
    logout(){
    this.get('auth').invalidate();
   }
 }
});