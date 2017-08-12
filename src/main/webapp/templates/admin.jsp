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
        <link rel="stylesheet" href="http://ui-grid.info/release/ui-grid.css" type="text/css">
        <link href="<c:url value='/static/css/app.css' />" rel="stylesheet"/>
    </head>
    <body ng-app="adminApp">
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
                <h3></h3>
                <div ng-controller = "adminCtrl as ctrl" >
                <article>
  					<button ng-click="toggleModal()" class="btn btn-default">Create user access</button>
  					<modal title="Create user access" visible="showModal">
   						 <form role="form" >
   						 <div class="control-group" ng-class="{true: 'error'}[submitted && form.email.$invalid]">
					        <label class="control-label" for="email">Email address</label>
					        <div class="controls">
					            <input type="email" name="email" class="form-control" ng-model="email" required />
					            <span class="help-inline" ng-show="submitted && form.email.$error.required">Required</span>
					            <span class="help-inline" ng-show="submitted && form.email.$error.email">Invalid email</span>
					        </div>
   						 </div>
   
   						 <button type="submit" class="btn btn-default" ng-click="addUser(email)">Submit</button>
					    </form>
  					</modal>
                </article>
				<div class="line"></div>
                <h3>recieved Tests:</h3>
                <article>
  			 <div ui-grid="gridOptionsEmpl" ui-grid-pagination ui-grid-selection ui-grid-exporter class="grid"></div>
                </article>
				</div>
            </section>
        <footer>
           <div class="line"></div>
           <p>Copyright 2016 - Bluescale</p>
        </footer>
		</section>
		<script src="http://code.jquery.com/jquery-1.10.2.js"></script>
		<script src="<c:url value='/static/js/module/bootstrap.min.js' />"></script>
        <!-- JavaScript Includes -->
        <script src="http://ajax.googleapis.com/ajax/libs/angularjs/1.4.3/angular.js"></script>
		<script src="http://ajax.googleapis.com/ajax/libs/angularjs/1.4.3/angular-touch.js"></script>
		<script src="http://ajax.googleapis.com/ajax/libs/angularjs/1.4.3/angular-animate.js"></script>
		
		<script src="http://ui-grid.info/docs/grunt-scripts/csv.js"></script>
		<script src="http://ui-grid.info/docs/grunt-scripts/pdfmake.js"></script>
		<script src="http://ui-grid.info/docs/grunt-scripts/vfs_fonts.js"></script>
		<script src="http://ui-grid.info/release/ui-grid.js"></script>
		
		<script src="<c:url value='/static/js/app.js' />"></script>
        <script src="<c:url value='/static/js/controller/adminController.js' />"></script>
  		<script src="<c:url value='/static/js/module/ngDialog.min.js' />"></script>
    </body>
</html>
