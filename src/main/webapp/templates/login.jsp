<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html xmlns:th="http://www.thymeleaf.org" xmlns:tiles="http://www.thymeleaf.org">
  <head>
    <title tiles:fragment="title">TEST BLUESCALE</title>
    <link href="<c:url value='/static/css/bootstrap.css' />" rel="stylesheet"></link>  
    <link href="<c:url value='/static/css/login.css' />" rel="stylesheet"></link>
</head>
<body>
    <div class="container">
	    <div class="row">
	        <div class="col-sm-6 col-md-4 col-md-offset-4">
	            <div class="account-wall">
	                <img class="profile-img" src="<c:url value='/static/img/blsc.png' />"  alt="">
	                <form name="f" th:action="@{/login}" method="post">
	                <input id="username" name="username" type="text" class="form-control" placeholder="username" required autofocus />
	                <input id="password" name="password" type="password" class="form-control" placeholder="Password" required />
	                <button class="btn btn-lg btn-primary btn-block" type="submit"> Sign in</button>
	                <div class="checkbox">   
	                <label class="checkbox pull-left">
	                    <input type="checkbox" value="remember-me" />
	                    Remember me
	                </label>
	                </div>
	                <a href="#" class="pull-right need-help">Need help? </a><span class="clearfix"></span>
	                </form>
	            </div>
	        </div>
	    </div>
	</div>
</body>
</html>