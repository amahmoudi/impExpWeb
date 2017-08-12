<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>Test</title>
        <link href="<c:url value='/static/css/ngDialog-theme-default.css' />" rel="stylesheet"/>
        <link href="<c:url value='/static/css/ngDialog.css' />" rel="stylesheet"/>
       <link rel="stylesheet" media="all" type="text/css" href="static/css/bootstrap.css" />
      <link rel="stylesheet" media="all" type="text/css" href="static/css/styles.css" />
        <link href="<c:url value='/static/css/app.css' />" rel="stylesheet"/>
    </head>
    <body ng-app="loadFileApp">
    	<section id="page">
            <header>
                <hgroup>
                    <h1>
                    <img src="https://www.bluescale.com/sites/default/files/logo_white.png" id="logo">
                    </h1>
                </hgroup>
                    <nav class="clear">
                    <div class="ulClass">
                    <strong>
                      ${currentUserName}
                      </strong>
                      <nav class="clear">
                      <a href="<c:url value="/logout" />">Logout</a>
                    </div>
                </nav>
            </header>
            <section id="articles"> 
                <div class="line"></div>
                <h3>download the file:</h3>
                <div ng-controller = "loadFileCtrl as ctrl">
                <article>
                <form  enctype="multipart/form-data">
    				<button  class="customButton" ng-click="ctrl.download('${fileName}')" >download</button>
				</form>
                </article>
                <h3>Send The Result:</h3>
                <article>
                  <form  enctype="multipart/form-data">
    					<input type="file" class="custom-file-input" file-model="myFile">
  			 			 <button class="customButton" ng-click="uploadFile()">upload me</button>
				</form>
                </article>
				</div>
            </section>
        <footer>
           <div class="line"></div>
           <p>Copyright 2016 - Bluescale</p>
        </footer>
		</section>
        <!-- JavaScript Includes -->
        <script src="http://ajax.googleapis.com/ajax/libs/angularjs/1.4.3/angular.js"></script>
		<script src="http://ajax.googleapis.com/ajax/libs/angularjs/1.4.3/angular-touch.js"></script>
		<script src="http://ajax.googleapis.com/ajax/libs/angularjs/1.4.3/angular-animate.js"></script>
		<script src="<c:url value='/static/js/app.js' />"></script>
		<script src="<c:url value='/static/js/service/fileUploadService.js' />"></script>
        <script src="<c:url value='/static/js/controller/loadFileController.js' />"></script>
  		<script src="<c:url value='/static/js/module/ngDialog.min.js' />"></script>
    </body>
</html>
