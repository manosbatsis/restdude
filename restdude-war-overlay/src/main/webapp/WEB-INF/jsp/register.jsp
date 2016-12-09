<%@ page import="com.restdude.util.ConfigurationFactory" %>
<!DOCTYPE html>
<%@page contentType="text/html;charset=UTF-8" %>
<html lang="en">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
    <%
    String basePath = ConfigurationFactory
            .getConfiguration()
            .getString(
                    ConfigurationFactory.BASE_URL);
    pageContext.setAttribute("basePath", basePath);
%>
<html>
<head>
    <title></title>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>

    <meta name="description" content=""/>
    <meta name="author" content=""/>
    <link href="${basePath}/css/bootstrap.css" rel="stylesheet"/>
    <link href="${basePath}/css/font-awesome/css/font-awesome.css"
          rel="stylesheet"/>
    <!-- SB Admin -->
    <link href="${basePath}/css/plugins/morris/morris-0.4.3.min.css"
          rel="stylesheet"/>
    <link href="${basePath}/css/plugins/timeline/timeline.css"
          rel="stylesheet"/>
    <!-- SB Admin CSS - Include with every page -->
    <link href="${basePath}/css/sb-admin.css" rel="stylesheet"/>


    <link href="${basePath}/css/bootstrap-markdown.css" rel="stylesheet"/>
    <link href="${basePath}/css/calipso.css" rel="stylesheet"/>
    <link href="${basePath}/css/style.css" rel="stylesheet"/>
    <link href="${basePath}/css/basic.css" rel="stylesheet"/>
    <link href="${basePath}/css/calendar.css" rel="stylesheet"/>
    <link href="${basePath}/css/select2.css" rel="stylesheet"/>
    <link href="${basePath}/css/bootstrap-select2.css" rel="stylesheet"/>
</head>
<body>
<!-- div class="page-header">
        <h1><spring:message code="label.user.registration.page.title"/></h1>
    </div-->
<sec:authorize access="isAnonymous()">
    <div class="panel panel-default">
        <div class="panel-body">
            <form:form action="/register" commandName="user" method="POST" enctype="utf8" role="form">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <c:if test="${user.signInProvider != null}">
                    <form:hidden path="signInProvider"/>
                </c:if>
                <div class="row">
                    <div id="form-group-userName" class="form-group col-lg-4">
                        <label class="control-label" for="user-userName"><spring:message
                                code="label.user.userName"/>:</label>
                        <form:input id="user-userName" path="userName" cssClass="form-control"/>
                        <form:errors id="error-userName" path="userName" cssClass="help-block"/>
                    </div>
                </div>
                <div class="row">
                    <div id="form-group-firstName" class="form-group col-lg-4">
                        <label class="control-label" for="user-firstName"><spring:message
                                code="label.user.firstName"/>:</label>
                        <form:input id="user-firstName" path="firstName" cssClass="form-control"/>
                        <form:errors id="error-firstName" path="firstName" cssClass="help-block"/>
                    </div>
                </div>
                <div class="row">
                    <div id="form-group-lastName" class="form-group col-lg-4">
                        <label class="control-label" for="user-lastName"><spring:message
                                code="label.user.lastName"/>:</label>
                        <form:input id="user-lastName" path="lastName" cssClass="form-control"/>
                        <form:errors id="error-lastName" path="lastName" cssClass="help-block"/>
                    </div>
                </div>
                <div class="row">
                    <div id="form-group-email" class="form-group col-lg-4">
                        <label class="control-label" for="user-email"><spring:message code="label.user.email"/>:</label>
                        <form:input id="user-email" path="email" cssClass="form-control"/>
                        <form:errors id="error-email" path="email" cssClass="help-block"/>
                    </div>
                </div>
                <c:if test="${user.signInProvider == null}">
                    <div class="row">
                        <div id="form-group-password" class="form-group col-lg-4">
                            <label class="control-label" for="user-password"><spring:message
                                    code="label.user.password"/>:</label>
                            <form:password id="user-password" path="password" cssClass="form-control"/>
                            <form:errors id="error-password" path="password" cssClass="help-block"/>
                        </div>
                    </div>
                    <div class="row">
                        <div id="form-group-passwordVerification" class="form-group col-lg-4">
                            <label class="control-label" for="user-passwordVerification"><spring:message
                                    code="label.user.passwordVerification"/>:</label>
                            <form:password id="user-passwordVerification" path="passwordVerification"
                                           cssClass="form-control"/>
                            <form:errors id="error-passwordVerification" path="passwordVerification"
                                         cssClass="help-block"/>
                        </div>
                    </div>
                </c:if>
                <button type="submit" class="btn btn-default"><spring:message
                        code="label.user.registration.submit.button"/></button>
            </form:form>
        </div>
    </div>
</sec:authorize>
<sec:authorize access="isAuthenticated()">
    <p><spring:message code="text.registration.page.authenticated.user.help"/></p>
</sec:authorize>
</body>
</html>