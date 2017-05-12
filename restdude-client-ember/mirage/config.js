export default function() {

  // These comments are here to help you get started. Feel free to delete them.

  /*
    Config (with defaults).

    Note: these only affect routes defined *after* them!
   */

  this.namespace = '/api';
  let rentals = [
    {
      type: 'rentals',
      id: 'grand-old-mansion',
      attributes: {
        title: "Grand Old Mansion",
        owner: "Veruca Salt",
        city: "San Francisco",
        type: "Estate",
        bedrooms: 15,
        image: "https://upload.wikimedia.org/wikipedia/commons/c/cb/Crane_estate_(5).jpg",
        description: "This grand old mansion sits on over 100 acres of rolling hills and dense redwood forests."
      }
    },
    {
      type: 'rentals',
      id: 'urban-living',
      attributes: {
        title: "Urban Living",
        owner: "Mike Teavee",
        city: "Seattle",
        type: "Condo",
        bedrooms: 1,
        image: "https://upload.wikimedia.org/wikipedia/commons/0/0e/Alfonso_13_Highrise_Tegucigalpa.jpg",
        description: "A commuters dream. This rental is within walking distance of 2 bus stops and the Metro."
      }
    },
    {
      type: 'rentals',
      id: 'downtown-charm',
      attributes: {
        title: "Downtown Charm",
        owner: "Violet Beauregarde",
        city: "Portland",
        type: "Apartment",
        bedrooms: 3,
        image: "https://upload.wikimedia.org/wikipedia/commons/f/f7/Wheeldon_Apartment_Building_-_Portland_Oregon.jpg",
        description: "Convenience is at your doorstep with this charming downtown rental. Great restaurants and active night life are within a few feet."
      }
    }
  ];

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

  this.get('/rentals', function(db, request) {
    if (request.queryParams.city !== undefined) {
      let filteredRentals = rentals.filter(function (i) {
        return i.attributes.city.toLowerCase().indexOf(request.queryParams.city.toLowerCase()) !== -1;
      });
      return { data: filteredRentals };
    } else {
      return { data: rentals };
    }
  });

  this.get('/rentals/:id', function (db, request) {
    return { data: rentals.find((rental) => request.params.id === rental.id) };
  });

  ///////////////////

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


  // POST /token
  this.post('/token-auth', (db, request) => {
    let json = JSON.parse(request.requestBody);

    //let { models } = db.user.where({ username: json.username, password: json.password });

    if (json.username === "admin" && json.password === "12"){

      return{
        "access_token":"PA$$WORD",
        "token_type": "bearer"
      };
    }else {

      var body = { errors: 'Email or password is invalid'};
      return new Mirage.Response(401,{},body);
    }

    /*if (models.length > 0) {
      return {
        // token encoded at https://jwt.io/
        token: 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJwYXNzd29yZCI6InBhc3N3b3JkIiwidXNlcm5hbWUiOiJvd25lckBjb2RlY29ycHMub3JnIiwidXNlcl9pZCI6MSwiZXhwIjo3MjAwMDAwMH0.LxkkKMcQoccAA0pphgRfXPSLdyaCawlK1gB3yPCht2s',
        user_id: models[0].id
      };
    } else {
      let errorDetail = `Your password doesn't match the email ${json.username}.`;
      return new Mirage.Response(401, {}, {
        errors: [
          {
            id: 'UNAUTHORIZED',
            title: '401 Unauthorized',
            detail: errorDetail,
            status: 401
          }
        ]
      });
    }*/
});

this.get('/users/:id');

// PATCH /users/:id
this.patch('/users/:id', function(db) {
  let attrs = this.normalizedRequestAttrs();
  let userId = attrs.id;
  let user = db.users.find(userId);


  return user.update(attrs);
});

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
