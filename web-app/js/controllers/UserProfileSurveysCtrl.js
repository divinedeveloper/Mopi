app.controller('UserProfileSurveysCtrl',['$scope','$filter','$routeParams','$route','$rootScope','$location','$injector','ngTableParams','$modal','Restangular','Notification','$timeout',function($scope, $filter,$routeParams,$route,$rootScope,$location,$injector,ngTableParams,$modal,Restangular,Notification,$timeout) {
	
//    /*Get Admin/Sponsor user surveys data start*/
//    $scope.current=function(title){
////Users Survey ends for admin
//
//		// $scope.urlpath.surveydetails=$scope.environment=="Development"?users/$routeParams.userid/surveys?offset=0&limit=10 : $scope.staticjsonpath;
//	
//console.log("asd")
//	if(title=="User's Survey" && $rootScope.userdata.resp.roleId==1 || $rootScope.userdata.resp.roleId==2)
//		{
//	
//		
//		//$timeout(function() {
//			$scope.pageurl="static/partials/templates/adminusersurvey.html"
//		$scope.usersurvey = new ngTableParams({
//		page : 1, // show first page
//		count : 2
//	// count per page
//	}, {
//		total : 0, // length of data
//		getData : function($defer, params) {
//		    var offset= (params.$params.page - 1) * params.$params.count
//            var limit= params.$params.count
//			Restangular.one('users/'+$routeParams.userid+'/surveys?offset='+offset+'&limit='+limit+'').get().then(function (data) {
//				 if(!data.error){
//        			 params.total(data.resp.count)
//        			 $defer.resolve(data.resp.results);
//        		 }
//		    	
//		    });
//		}
//	
//	});
//		//},2000);
//		//Users Survey ends for admin
//	} 
//	else if(title=="User's Survey" && $rootScope.userdata.resp.roleId==3){
//		$scope.pageurl="static/partials/templates/sponsorusersurvey.html"
//			//Users Survey starts for sponsoradmin
//		$scope.sponsorusersurvey =  new ngTableParams({
//			page : 1, // show first page
//			count : 2
//		// count per page
//		}, {
//			total : 0, // length of data
//			getData : function($defer, params) {
//			    var offset= (params.$params.page - 1) * params.$params.count
//	            var limit= params.$params.count
//				Restangular.one('users/'+$routeParams.userid+'/surveys?offset='+offset+'&limit='+limit+'').get().then(function (data) {
//					 if(!data.error){
//	        			 params.total(data.resp.count)
//	        			 $defer.resolve(data.resp.results);
//	        		 }
//			    	
//			    });
//			}
//		
//		});
//			
//		
//		//Users Survey ends for sponsoradmin
//    } 
//	else if(title=="Surveys Taken" && $rootScope.userdata.resp.roleId==1 || $rootScope.userdata.resp.roleId==2){
//	    //Surveys Taken starts
//		$scope.pageurl="static/partials/templates/adminusersurveytaken.html"
//		//$scope.pageurl="static/partials/templates/sponsorusersurveytaken.html"
//			console.log("asd")
//    	$scope.surveytaken = new ngTableParams({
//    		page : 1, // show first page
//    		count : 2
//    	// count per page
//    	}, {
//    		total : 0, // length of data
//    		getData : function($defer, params) {
//    			Restangular.one($scope.staticjsonpath+'/adminsurveytaken.json').get().then(function (data) {
//    				 if(!data.error){
//            			 params.total(data.resp.count)
//            			 $defer.resolve(data.resp);
//            		 }
//    		    	
//    		    });
//    		}
//    	
//    	});
//    	//Surveys Taken ends
//    }else if(title=="Surveys Taken" && $rootScope.userdata.resp.roleId==3){
//    	
//     $scope.pageurl="static/partials/templates/sponsorusersurveytaken.html"
//    	
//    }
//		
//	
//}
//    /*Get Admin/Sponsor user surveys data end*/
//    
//    
//    
//
//    
//    
//    /*Create Tabs as per user role ends*/
//    if($scope.userstatus=="created"  && $rootScope.userdata.resp.roleId==1 || $rootScope.userdata.resp.roleId==2 ){
// 
//	      $scope.tabs = [
//	                     {
//	                       title: "User's Survey",
//	                       pageurl: "static/partials/templates/adminusersurvey.html"
//	               
//	                     }, {
//	                       title: "Surveys Taken",
//	                       pageurl: "static/partials/templates/adminusersurveytaken.html"
//	                       
//	                      
//	                     }
//	                   ];
//	     // $scope.current("User's Survey")
//	}else if($scope.userstatus=="created"  && $rootScope.userdata.resp.roleId==3){
//		
//	      $scope.tabs = [
//	                     {
//	                       title: "User's Survey",
//	                       pageurl: "static/partials/templates/sponsorusersurvey.html"
//	                     
//	                     }, {
//	                       title: "Surveys Taken",
//	                       pageurl: "static/partials/templates/sponsorusersurveytaken.html"
//	                  
//	                      
//	                     }
//	                   ];
//	     // $scope.current("User's Survey")
//	}
//
///*Create Tabs as per user role ends*/
	
	
}]);