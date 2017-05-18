export default function() {

  // These comments are here to help you get started. Feel free to delete them.

  /*
    Config (with defaults).

    Note: these only affect routes defined *after* them!
   */
  this.passthrough();
  let users = [
    {
      type: 'users',
      id: 'restdude1',
      attributes: {
        username: "nick",
        email: "nick@restdude",
        password: "12",
        passwordConfirmation: "12",
        permissions: 777
      }
    },
    {
      type: 'users',
      id: 'restdude2',
      attributes: {
        username: "manos",
        email: "manos@restdude",
        password: "123",
        passwordConfirmation: "123",
        permissions: 777
      }
    },
    {
      type: 'users',
      id: 'restdude2',
      attributes: {
        username: "erik",
        email: "erik@restdude",
        password: "1234",
        passwordConfirmation: "1234",
        permissions: 777
      }
    }
  ];


  ///////////////////
/*
  this.get('/users', function(db, request) {
    if (request.queryParams.email !== undefined) {
      let filteredUsers = users.filter(function (i) {
        return i.attributes.email.toLowerCase().indexOf(request.queryParams.email.toLowerCase()) !== -1;
      });
      return { data: filteredUsers };
    } else {
      return { data: users };
    }
  });

  this.get('/users/:id', function (db, request) {
    return { data: users.find((user) => request.params.id === user.id) };
  });


this.get('/users/:id');

// PATCH /users/:id
this.patch('/users/:id', function(db) {
  let attrs = this.normalizedRequestAttrs();
  let userId = attrs.id;
  let user = db.users.find(userId);


  return user.update(attrs);
});
*/
  // this.urlPrefix = '';    // make this `http://localhost:8080`, for example, if your API is on a different server
  // this.timing = 400;      // delay for each request, automatically set to 0 during testing

  /*
    Shorthand cheatsheet:

    this.get('/posts');
    this.post('/posts');
    this.get('/posts/:id');
    this.put('/posts/:id'); // or this.patch
    this.del('/posts/:id');
  */
};
