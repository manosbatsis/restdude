<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="../includes/head.jsp" %>
</head>
<!-- BODY options, add following classes to body to change options
		1. 'compact-nav'     	  - Switch sidebar to minified version (width 50px)
		2. 'sidebar-nav'		  - Navigation on the left
			2.1. 'sidebar-off-canvas'	- Off-Canvas
				2.1.1 'sidebar-off-canvas-push'	- Off-Canvas which move content
				2.1.2 'sidebar-off-canvas-with-shadow'	- Add shadow to body elements
		3. 'fixed-nav'			  - Fixed navigation
		4. 'navbar-fixed'		  - Fixed navbar
	-->

<body class="navbar-fixed fixed-nav">
<!-- Placed at the end of the document so the pages load faster -->
<script data-main="${basePath}/js/main" id="restdude-script-main" src="${basePath}/js/lib/require.js">
</script>
</body>
</html>