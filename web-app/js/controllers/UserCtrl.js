//(function() {
//	'use strict';
app.controller('UserCtrl',['$scope','$filter','ngTableParams','$modal','Restangular','$timeout','$rootScope',function($scope, $filter, ngTableParams,$modal,Restangular,$timeout,$rootScope) {
								$scope.type={}
								$scope.search={}
								 $scope.result = '';
							    $scope.options = {};
							    //$scope.search.query=''
							    //$scope.details = '';
								
							    $scope.breadCrumb.hideBreadCrumb = false
							    
							    $rootScope.breadcrum=[{"name":"User","url":"user"}]
							   /* $rootScope.breadcrum=[{"name":"User","url":"user"},
			                      {"name":"Dashboard","url":"dashboard"},
			                      {"name":"temp","url":"user"}]*/
							    
								$scope.statususer=[{"status":true,"name":"Active"},
									               {"status":false,"name":"Inactive"}]
								//$scope.search.status=$scope.statususer[0].status
							    /*Get all roles starts*/
							    Restangular.one('roles').get().then(function (data) {
							    	$scope.rolelist=data;
							    	//$scope.search.roleId=data[0].id
							    });
							    
							    
							    	/*Get all roles ends*/	
							    	
	
							    	
								/*Get all users list starts*/
								//$scope.getusers=function(searchdata){
								
	
								    $scope.tableParams = new ngTableParams({
								        page: 1,            // show first page
								        count: 10,          // count per page
								   
								    }, {
								        total: 0, // length of data
								        getData: function($defer, params) {
								        	$timeout(function() {
								        	    var offset= (params.$params.page - 1) * params.$params.count
						                        var limit= params.$params.count
						                        var roleId=$scope.search.roleId==undefined ? "" : $scope.search.roleId
						                        var query=$scope.search.query==undefined  ? "" : $scope.search.query
						                        var status= $scope.search.status==undefined ? "" : $scope.search.status
								       
								        	 Restangular.one('users/search?roleId='+roleId+'&query='+query+'&status='+status+'&offset='+offset+'&limit='+limit+'').get().then(function (data) {
											    
								        		 console.log(data.resp)
								        		 if(!data.error){
								        			 params.total(data.resp.count)
								        			 $defer.resolve(data.resp.users);
								        		 }
											    });
								        	 },1000);
								        }
								       
								    }); 
									
								//}
								
									//$scope.tableParams.reload();
								
								/*Get all users list ends*/	
								
							    /* Search Users starts*/
					
							     
							      
							      /* Search User ends*/
								

									
				
								 
								      
									 
    						} ]);

//}).call(this);