App.AuthManager = Ember.Object.extend(BasicAuthenticatable, FacebookAuthenticatable, {
  init: function() {
    this._super();
    var authToken = Cookies.get("auth_token");
    var authUserId = Cookies.get("auth_user_id");
    if (!Ember.isEmpty(authToken) && !Ember.isEmpty(authUserId)) {
      this.authenticate(authToken, authUserId);
    }
  },

  isAuthenticated: function() {
    return !Ember.isEmpty(this.get("authToken")) && !Ember.isEmpty(this.get("userId"));
  },

  authenticate: function(authToken, userId) {
    this.setProperties({
      authToken: authToken,
      userId:   userId
    });
    this.updateAdapterHeader();
  },

  updateAdapterHeader: function() {
    DS.ActiveModelAdapter.reopen({
      headers: {
        "Authorization": "Bearer " + this.get("authToken")
      }
    });
  },

  reset: function() {
    this.setProperties({
      authToken: null,
      userId: null
    });
    this.removeCookies();
    this.resetFacebook();
  },

  setCookies: function() {
    Cookies.set("auth_token", this.get("authToken"));
    Cookies.set("auth_user_id", this.get("userId"));
  },

  removeCookies: function() {
    Cookies.remove("auth_token");
    Cookies.remove("auth_user_id");
  },

  authenticationObserver: function() {
    if (Ember.isEmpty(this.get("authToken"))) {
      this.removeCookies();
    } else {
      this.setCookies();
    }
  }.observes("authToken")
});