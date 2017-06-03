export default function() {
this.get('/issue-comments');
this.get('/issue-comments/:id');
this.post('/issue-comments');
this.del('/issue-comments/:id');
this.patch('/issue-comments/:id');
this.get('/issues');
this.get('/issues/:id');
this.post('/issues');
this.del('/issues/:id');
this.patch('/issues/:id');
this.get('/websites');
this.get('/websites/:id');
this.post('/websites');
this.del('/websites/:id');
this.patch('/websites/:id');
this.get('/space-apps');
this.get('/space-apps/:id');
this.post('/space-apps');
this.del('/space-apps/:id');
this.patch('/space-apps/:id');
this.get('/hosts');
this.get('/hosts/:id');
this.post('/hosts');
this.del('/hosts/:id');
this.patch('/hosts/:id');
this.get('/spaces');
this.get('/spaces/:id');
this.post('/spaces');
this.del('/spaces/:id');
this.patch('/spaces/:id');
this.get('/testmodels');
this.get('/testmodels/:id');
this.post('/testmodels');
this.del('/testmodels/:id');
this.patch('/testmodels/:id');
this.get('/users');
this.get('/users/:id');
this.post('/users');
this.del('/users/:id');
this.patch('/users/:id');
this.get('/organizations');
this.get('/organizations/:id');
this.post('/organizations');
this.del('/organizations/:id');
this.patch('/organizations/:id');

  // These comments are here to help you get started. Feel free to delete them.

  /*
    Config (with defaults).

    Note: these only affect routes defined *after* them!
  */

  // this.urlPrefix = '';    // make this `http://localhost:8080`, for example, if your API is on a different server
  // this.namespace = '';    // make this `api`, for example, if your API is namespaced
  // this.timing = 400;      // delay for each request, automatically set to 0 during testing

  /*
    Shorthand cheatsheet:

    this.get('/posts');
    this.post('/posts');
    this.get('/posts/:id');
    this.put('/posts/:id'); // or this.patch
    this.del('/posts/:id');

    http://www.ember-cli-mirage.com/docs/v0.2.0-beta.7/shorthands/
  */
}
