<%@ page import="com.restdude.util.ConfigurationFactory" %>
<!DOCTYPE html>
<%@page contentType="text/html;charset=UTF-8" %>
<html lang="en">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%
    String basePath = ConfigurationFactory
            .getConfiguration()
            .getString(ConfigurationFactory.BASE_URL);
    pageContext.setAttribute("basePath", basePath);
%>
<!-- Title &amp; Meta -->
<title>Frontend tests</title>
<meta charset="utf-8">

<!-- Stylesheets -->
<link rel="stylesheet" href="libs/mocha/mocha.css">
</head>

<body>

<div id="mocha"></div>

<!-- Testing Libraries -->
<script src="libs/mocha/mocha.js"></script>
<script src="libs/chai/chai.js"></script>
<script src="libs/sinon/sinon.js"></script>
<script>

</script>

<!-- Libs -->
<script src="../jstestapp/libs/jquery/jquery-1.8.3.min.js"></script>
<script src="../jstestapp/libs/underscore/underscore-min.js"></script>
<script src="../jstestapp/libs/backbone/backbone-min.js"></script>

<!-- Source files -->
<script src="../jstestapp/src/app.js"></script>
<script src="../jstestapp/src/models/user.js"></script>

<!-- Test -->
<script src="models/user.test.js"></script>

<script id="calipso-script-main" src="${basePath}/js/lib/require.js">
    <!-- -->
</script>
<script>
    // load common shim etc. see github.com/requirejs/example-multipage-shim
    require(['${basePath}/js/config.js'], function (config) {
        // load main domain
        require(['${basePath}/jstest/spec-runner.js']);
    });
</script>
</body>
</html>