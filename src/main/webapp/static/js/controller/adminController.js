'use strict';
adminApp.controller('adminCtrl',['$scope', '$http','ngDialog',function ($scope, $http, ngDialog) {
	
	$scope.showModal = false;
    $scope.toggleModal = function(){
    $scope.showModal = !$scope.showModal;
    };
    
    $scope.addUser = function(mail) {
    	   $http.post('/createUser?eMail='+mail)
    	   .success(function(data){
    		   $scope.showModal = false;
        	   ngDialog.open({
        		   template: '<div id="popup1">'+ data.message+'</div>' ,
        		   plain: true // with "true" the template accepte string as param
        		   });
            })
            .error(function(data){
            	$scope.showModal = false;
            	 ngDialog.open({ 
            		 template: '<div id="popup1">'+ data.message+'</div>' ,
          		   plain: true // with "true" the template accepte string as param
            		 });
            });
    };
    
    var paginationOptions = {
      sort: null
    };

    $scope.gridOptionsEmpl = {
      paginationPageSizes: [25,50,100,500,1000,2000],
      paginationPageSize: 25,
      useExternalPagination: true,
      useExternalSorting: true,
      enableGridMenu: true,
      enableCellSelection: true,
      enableColumnResizing: true,
      enableFiltering: true,
      rowSelection: 'multiple',
      rowDeselection: true,
      enableSelectAll: true,
      exporterCsvFilename: 'myFile.csv',
      exporterPdfDefaultStyle: {fontSize: 9},
      exporterPdfTableStyle: {margin: [30, 30, 30, 30]},
      exporterPdfTableHeaderStyle: {fontSize: 10, bold: true, italics: true, color: 'red'},
      exporterPdfHeader: { text: "My Header", style: 'headerStyle' },
      exporterPdfFooter: function ( currentPage, pageCount ) {
        return { text: currentPage.toString() + ' of ' + pageCount.toString(), style: 'footerStyle' };
      },
      exporterPdfCustomFormatter: function ( docDefinition ) {
        docDefinition.styles.headerStyle = { fontSize: 22, bold: true };
        docDefinition.styles.footerStyle = { fontSize: 10, bold: true };
        return docDefinition;
      },
      exporterPdfOrientation: 'portrait',
      exporterPdfPageSize: 'LETTER',
      exporterPdfMaxGridWidth: 500,
      exporterCsvLinkElement: angular.element(document.querySelectorAll(".custom-csv-link-location")),
      columnDefs: [
        { name: 'userEmail', minWidth: 200},
        { name: 'dateDownloadFile' ,minWidth: 150},
        { name: 'dateUploadFile',minWidth: 150 },
        { name: 'userEnabled',minWidth: 100, enableSorting: false , visible:false},
        { name: 'userPassword',minWidth: 150, enableSorting: false , visible:false},
        { name: 'adminEnabled',minWidth: 150, enableSorting: false , visible:false},
        { name: 'view file' ,minWidth: 150, enableSorting: false},
        { name: 'delete file' ,minWidth: 150, enableSorting: false}
      ],
      exporterAllDataFn: function() {
        return getPage(1, $scope.gridOptionsEmpl.totalItems)
        .then(function() {
          $scope.gridOptionsEmpl.useExternalPagination = false;
          $scope.gridOptionsEmpl.useExternalSorting = false;
          getPage = null;
        });
      },
      onRegisterApi: function(gridApi) {
        $scope.gridApi = gridApi;
        $scope.gridApi.core.on.sortChanged($scope, function(grid, sortColumns) {
          if(getPage) {
            if (sortColumns.length > 0) {
              paginationOptions.sort = sortColumns[0].sort.direction;
            } else {
              paginationOptions.sort = null;
            }
            getPage(grid.options.paginationCurrentPage, grid.options.paginationPageSize, paginationOptions.sort)
          }
        });
        gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
          if(getPage) {
            getPage(newPage, pageSize);
          }
        });
      }
    };

    var getPage = function(curPage, pageSize) {
      var url = '/listFileUploaded';
      var _scope = $scope;
      return $http.get(url)
      .success(function (data) {
        var firstRow = (curPage - 1) * pageSize;
        $scope.gridOptionsEmpl.totalItems = 100;
        $scope.gridOptionsEmpl.data = data.slice(firstRow, firstRow + pageSize)
      });
    };

    getPage(1, $scope.gridOptionsEmpl.paginationPageSize);
    
}]);


adminApp.directive('modal', function () {
    return {
      template: '<div class="modal fade">' + 
          '<div class="modal-dialog">' + 
            '<div class="modal-content">' + 
              '<div class="modal-header">' + 
                '<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>' + 
                '<h4 class="modal-title">{{ title }}</h4>' + 
              '</div>' + 
              '<div class="modal-body" ng-transclude></div>' + 
            '</div>' + 
          '</div>' + 
        '</div>',
      restrict: 'E',
      transclude: true,
      replace:true,
      scope:true,
      link: function postLink(scope, element, attrs) {
        scope.title = attrs.title;

        scope.$watch(attrs.visible, function(value){
          if(value == true)
            $(element).modal('show');
          else
            $(element).modal('hide');
        });

        $(element).on('shown.bs.modal', function(){
          scope.$apply(function(){
            scope.$parent[attrs.visible] = true;
          });
        });

        $(element).on('hidden.bs.modal', function(){
          scope.$apply(function(){
            scope.$parent[attrs.visible] = false;
          });
        });
      }
    };
  });
