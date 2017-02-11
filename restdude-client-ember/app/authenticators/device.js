// app/authenticators/devise.js
import Devise from 'ember-simple-auth/authenticators/devise';

export default Devise.extend({  
  serverTokenEndpoint: 'http://localhost:4200/user/sign_in'
});