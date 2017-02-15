FacebookAuthenticatable = Ember.Mixin.create({
  isAuthenticatedWithFacebook: function() {
    return !Ember.isEmpty(this.get("facebookToken")) && !Ember.isEmpty(this.get("facebookId"));
  },

  facebookAuthorize: function(successCallback) {
    $.post("api/v1/authorizations", {
      facebook_id: this.get("facebookId"), facebook_token: this.get("facebookToken"), source: "facebook"
    }).then(this.processServerResponse).then(successCallback);
  },

  facebookAuthenticate: function(authResponse) {
    this.setProperties({
      facebookToken: authResponse.accessToken,
      facebookId: authResponse.userID
    });
  },

  resetFacebook: function() {
    this.setProperties({
      facebookToken: null,
      facebookId: null
    });
  }
});