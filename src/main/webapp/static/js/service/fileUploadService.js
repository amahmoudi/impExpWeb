'use strict';
loadFileApp.service('fileUpload', ['$http','$q', '$timeout', '$window', function ($http,$q, $timeout, $window) {
	return {
    uploadFileToUrl :function(file, uploadUrl){
        var fd = new FormData();
        fd.append('file', file);
        fd.append('filename', file.name);
        $http.post(uploadUrl, fd, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        }).then(
				function(response){
					return response;
		})
    },
    download: function(name) {

        var defer = $q.defer();

        $timeout(function() {
                $window.location = 'download?name=' + name;

            }, 1000)
            .then(function() {
                defer.resolve('success');
            }, function() {
                defer.reject('error');
            });
        return defer.promise;
    }
 
};
}]);