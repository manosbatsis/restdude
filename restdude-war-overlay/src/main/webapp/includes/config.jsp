<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.restdude.util.ConfigurationFactory" %>
<%@ page import="com.restdude.util.HttpUtil" %>
<%@ page import="org.apache.commons.configuration.Configuration" %>
<%@ page import="javax.servlet.http.HttpServletRequest" %>
<%
    HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();
    String basePath = HttpUtil.setBaseUrl(req);
    //System.out.println("config.jsp, basePath: " + basePath);
    Configuration configuration = ConfigurationFactory.getConfiguration();
    String appName = configuration.getString(ConfigurationFactory.APP_NAME);
    String appVersion = configuration.getString(ConfigurationFactory.APP_VERSION);

    pageContext.setAttribute("basePath", basePath);
    pageContext.setAttribute("appName", appName);
    pageContext.setAttribute("appVersion", appVersion);

%>