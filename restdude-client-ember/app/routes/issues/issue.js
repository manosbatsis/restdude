import Ember from 'ember';

export default Ember.Route.extend({
  model() {
    return [{
      title: ' NameOfIssue '
    },
    {
      createdBy:{ username: 'user29',avatarUrl: ''},
      createdDate: '30/1/2017',
      content: 'content issue content issue content issue content issue '
    },
    {
      createdBy:{ username: 'user30',avatarUrl: ''},
      createdDate: '1/2/2017',
      content: 'content issue content issue content issue content issue '
    },
    {
      createdBy:{ username: 'user31',avatarUrl: ''},
      createdDate: '2/2/2017',
      content: 'content issue content issue content issue content issue '
    },
    {
      createdBy:{ username: 'user32',avatarUrl: ''},
      createdDate: '3/2/2017',
      content: 'content issue content issue content issue content issue '
    }];
  }
});
