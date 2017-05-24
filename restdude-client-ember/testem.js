/*jshint node:true*/
module.exports = {
  "framework": "qunit",
  "test_page": "tests/index.html?hidepassed",
  "disable_watching": true,
  "launch_in_ci": [
    "PhantomJS"
  ],
  "launch_in_dev": [
    "PhantomJS",
    "Chrome"
  ],
  "proxies": {
    "/api": {
      "target": "http://localhost:8080/restdude/api/auth/jwt/access",
      "onlyContentTypes": ["xml", "json"]
    }
  }
};
