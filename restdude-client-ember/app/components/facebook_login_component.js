(function(d, s, id){
   var js, fjs = d.getElementsByTagName(s)[0];
   if (d.getElementById(id)) {return;}
   js = d.createElement(s); js.id = id;
   js.src = "//connect.facebook.net/en_US/all.js";
   fjs.parentNode.insertBefore(js, fjs);
 }(document, 'script', 'facebook-jssdk'));

App.FacebookLoginComponent = Ember.Component.extend({
  init: function() {
    if(Ember.isEmpty(App.facebookAppId)) {
      console.error("Ember App facebookAppId is not set");
    } else if(Ember.isEmpty(this.get("authManager"))) {
      console.error("Ember App authManager is not set");
    }

    this.set("appId", App.facebookAppId);
    this._super();
    _.bindAll(this,
      "handleFacebookAuthChange",
      "handleLoginStatus",
      "facebookLogin",
      "initFacebook");
  },

  click: function() {
    if(this.get("authManager").isAuthenticatedWithFacebook()) {
      this.sendAction();
    } else {
      FB.getLoginStatus(this.handleLoginStatus);
    }
  },

  facebookLogin: function(response) {
    if(response.status === "connected") {
      this.get("authManager").facebookAuthenticate(response.authResponse);
      this.sendAction();
    }
  },

  handleLoginStatus: function(response) {
    if(response.status === "connected") {
      this.facebookLogin(response);
    } else {
      FB.login(this.facebookLogin);
    }
  },

  initFacebook: function() {
    FB.init({
      appId      : this.get("appId"),
      status     : true
    });
    this.authResponseSubscribe();
  },

  facebookAsyncInit: function() {
    window.fbAsyncInit = this.initFacebook;
  }.on("didInsertElement"),

  handleFacebookAuthChange: function(response) {
    if (response.status === "connected") {
      this.get("authManager").facebookAuthenticate(response.authResponse);
    } else {
      this.get("authManager").resetFacebook();
    }
  },

  authResponseSubscribe: function() {
    FB.Event.subscribe("auth.authResponseChange", this.handleFacebookAuthChange);
  }
});