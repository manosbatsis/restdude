// app/controllers/user.js
import Ember from 'ember';
import { deserializeQueryString } from 'ember-slack-search-input';
    export default Ember.Controller.extend({
      queryParams: ['search'],
      search: "",
    auth: Ember.inject.service('session'),
    actions: {
      filterByUser(param) {
        if (param !== '') {
          return this.get('store').query('user', { username: param });
        } else {
          return this.get('store').findAll('user');
        }
      },
    logout(){
    this.get('auth').invalidate();
  },
  search() {
    let queryString = this.get('queryString');
    console.log(queryString); // `before:2000-23-23 lorem`
    let modifiers = deserializeQueryString(queryString);
    let before = modifiers['before:'];
    let { model, fullText, modifier, value } = before[0]; //first occurance of `before:`
    console.log(model); // moment date
    console.log(fullText); // before:2000-23-23
    console.log(modifier); // before:
    console.log(value); // 2000-23-23
  }
 }
});
