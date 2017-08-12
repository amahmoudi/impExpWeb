'use strict';
loadFileApp.controller('loadFileCtrl', ['$scope', '$http', 'fileUpload','ngDialog','$window'  ,function($scope, $http, fileUpload,ngDialog, $window){
	
    this.download = function(fileName) {
        fileUpload.download(fileName)
            .then(function(success) {
                console.log('success : ' + success);
            }, function(error) {
                console.log('error : ' + error);
            });
    };
	
    $scope.uploadFile = function(){
        var file = $scope.myFile;
        if(file==undefined){
     	   ngDialog.open({
    		   template: '<div id="popup1"> Please enter a new file</div>' ,
    		   plain: true // with "true" the template accepte string as param
    		   });
     	   return 
        }
        var uploadUrl = "/fileUpload";
        var fd = new FormData();
        fd.append('file', file);
        fd.append('filename', file.name);
        $http.post(uploadUrl, fd, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        })
       .success(function(data){
    	   ngDialog.open({
    		   template: '<div id="popup1">'+ data.message+'</div>' ,
    		   plain: true // with "true" the template accepte string as param
    		   });
        })
        .error(function(data){
        	 ngDialog.open({ 
        		 template: '<div id="popup1">'+ data.message+'</div>' ,
      		   plain: true // with "true" the template accepte string as param
        		 });
        });
    };
    
    
}]);


loadFileApp.directive('fileModel', ['$parse', function ($parse) {
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            var model = $parse(attrs.fileModel);
            var modelSetter = model.assign;
            
            element.bind('change', function(){
                scope.$apply(function(){
                    modelSetter(scope, element[0].files[0]);
                });
            });
        }
    };
}]);

