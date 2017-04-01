app.controller('UserProfileCtrl',['$scope','$filter','$routeParams','$route','$rootScope','$location','$injector','ngTableParams','$modal','Restangular','toaster','$upload',function($scope, $filter,$routeParams,$route,$rootScope,$location,$injector,ngTableParams,$modal,Restangular,toaster,$upload) {


	$scope.breadCrumb.hideBreadCrumb = false

	if($rootScope.userdata.resp.roleId==2 || $rootScope.userdata.resp.roleId==3){
		$rootScope.breadcrum=[{"name":"Profile","url":"userprofile/"+$routeParams.userid}]
	}else{
		$rootScope.breadcrum=[{"name":"User","url":"user"},
		                      {"name":"Profile","url":"userprofile/"+$routeParams.userid}]
	}

	$scope.user={}
	$scope.urlpath={}
	$scope.pageurl={}
	$scope.emailexist=false
	$scope.unameexist=false

	$scope.statususer=[{"status":true,"name":"Active"},
	               {"status":false,"name":"Inactive"}]
	$scope.genders=[{"name":"Male","id":1},
	                {"name":"Female","id":2}]
	$scope.agegroup=[{"name":"18-30","id":1},
	                 {"name":"31-40","id":2},
	                 {"name":"41-50","id":3},
	                 {"name":"51-60","id":4},
	                 {"name":"60+","id":5}]
	$scope.incomegroup=[{"incomes":"0-10000","id":1},
	                    {"incomes":"10001-50000","id":2},
	                    {"incomes":"50001-100000","id":3},
	                    {"incomes":"100000+","id":4}]
	$scope.relegions=[{"name":"Atheist","id":1},
	                  {"name":"Buddhist","id":2},
	                  {"name":"Christian","id":3},
	                  {"name":"Hindu","id":4},
	                  {"name":"Muslim","id":5}]
	$scope.occupations=[{"name":"Art & Design","id":1},
	                    {"name":"Business","id":2},
	                    {"name":"Education","id":3},
	                    {"name":"Engineering","id":4},
	                    {"name":"Health","id":5},
	                    {"name":"Hospitality & Culinary","id":6},
	                    {"name":"Law & Criminal Justice","id":7},
	                    {"name":"Liberal Arts","id":8},
	                    {"name":"Science","id":9},
	                    {"name":"Social Science","id":10},
	                    {"name":"Technology & IT","id":11},
	                    {"name":"Vocational","id":12},
	                    {"name":"Others","id":13}]
	console.log($rootScope.userdata)

	$scope.userstatus = $routeParams.userid == 'newuser' ? 'new': 'created'

		if ($scope.userstatus == "new") {
			$scope.edit = true

		}
		else if($scope.userstatus=="created"){
			$scope.edit=false
			  /*Get user data starts*/

			Restangular.one('users/'+$routeParams.userid).get().then(function (data) {
				if(!data.error){
		    	$scope.user=data.resp;
		    	//copy user object to a new scope object
		    	$scope.masterUser= angular.copy(data.resp);
		    	/*console.log("master user is")
		    	console.log($scope.masterUser)*/
				}
		    });


		    	/*Get user data ends*/

		}
	 /* Injector for validation starts */
	var $validationProvider = $injector.get('$validation');

    $scope.useradd = {
            requiredCallback: 'required',
            checkValid: $validationProvider.checkValid,
            submit: function (form) {
                $validationProvider.validate(form);
            },
            reset: function (form) {
                $validationProvider.reset(form);
            }
        };

        /* Injector for validation ends */

    /*Get all roles starts*/
    Restangular.one('roles').get().then(function (data) {
    	$scope.rolelist=data;

    });


    	/*Get all roles ends*/


    /*Question Image Upload function starts*/
	$scope.userimageuploadfun=function(userid,toShowToaster){
		//if($scope.imageupload){
		$scope.upload = $upload.upload({
	        url: 'api/v1/user/'+userid+'/profile/uploadImage',
	        file: $scope.imageupload,
	        fileFormDataName: 'myfile',
	      }).progress(function(evt) {
	        console.log('percent: ' + parseInt(100.0 * evt.loaded / evt.total));
	      }).success(function(data, status, headers, config) {
	        // file is uploaded successfully
	    	  if(!data.error){
	    		  if(toShowToaster =='created'){
	    			  toaster.pop('success', "User", "User updated succesfully");
	    		  }
	    			
   				 $location.path('userprofile/'+userid)
   				$route.reload()

	    	  }
	        console.log(data);
	      })
		//}

	}

	/*Question Image Upload function ends*/









		  /*Create/Register User function starts*/

    //Create Accout starts
	    $scope.registeruser=function(primary,details,validity){
	    	console.log(details)
	    	if(validity==true){

	    	if(details){
	    	var result = details;
//look for locality tag and administrative_area_level_1
var city = "";
var state = "";
var countryCode="";
console.log(result.address_components.length)
for(var i=0, len=result.address_components.length; i<len; i++) {
	console.log("innn")
var ac = result.address_components[i];
if(ac.types.indexOf("locality") >= 0) city = ac.long_name;
if(ac.types.indexOf("administrative_area_level_1") >= 0) state = ac.long_name;
if(ac.types.indexOf("country") >= 0) country = ac.long_name;
if(ac.types.indexOf("country") >= 0) countryCode = ac.short_name;
}
//only report if we got Good Stuff
if(city != '' && state != '') {
console.log(city);
console.log(state);
console.log(country);
console.log(countryCode)
}
	    	}
	 if($scope.userstatus == "new" && primary.roleId){
Restangular.all('register').post({"email":primary.email,
	    		"password":primary.password,
	    		"password2":primary.password,
	    		"name":primary.name,
	    		"username":primary.username,
	    		"roleId":primary.roleId}).then(function (data) {
	    			console.log(data)


	    			if(!data.error){

	    		$scope.updateuser(primary,details,data)
	    		toaster.pop('success', "User", "User added succesfully");
	    		}

	    	}, function error(data) {
	    		console.log(data)
    			if(data.data.error){
    			if(data.data.message=="Account already exist.Please try another email."){
    				$scope.emailexist=true
    			}else if(data.data.message=="Username already exist.Please try another username."){
    				$scope.unameexist=true
    				
    			}
    			toaster.pop('error', "User", data.data.message);
    		}

            });
		 } else if($scope.userstatus == "created"){
	    		$scope.updateuser(primary,details,'created')
	    	}
	 	}

	    }
	  //Create Accout ends

	 //Update User function starts
	    $scope.updateuser=function(primary,details,data){
	    	console.log(primary.incomeLevel)
	    	console.log(primary.occupation)
	    	console.log(primary.religion)
	    	console.log(primary.age)
	    	if(details){
		    	var result = details;
	//look for locality tag and administrative_area_level_1
	var city = "";
	var state = "";
	var countryCode="";
	for(var i=0, len=result.address_components.length; i<len; i++) {
	var ac = result.address_components[i];
	if(ac.types.indexOf("locality") >= 0) city = ac.long_name;
	if(ac.types.indexOf("administrative_area_level_1") >= 0) state = ac.long_name;
	if(ac.types.indexOf("country") >= 0) country = ac.long_name;
	if(ac.types.indexOf("country") >= 0) countryCode = ac.short_name;
	}
	//only report if we got Good Stuff
	if(city != '' && state != '') {
	console.log(city);
	console.log(state);
	console.log(country);
	console.log(countryCode);
	}
		    	}
	    	var userid = data =='created' ? primary.id : data.resp.id
	    			//var dob = moment(primary.dob).format("YYYY-MM-DD");
	    			var toShowToaster = data
	    			
	    			Restangular.one('users/'+ userid).customPUT({"email":primary.email,
	    					    	    "name":primary.name,
	    					    	    "username":primary.username,
	    					    		"description":primary.description==undefined ? null : primary.description,
	    					    		"mobile":primary.mobile,
	    					    		"status":primary.status==undefined ? false : primary.status,
	    					    		"gender":primary.gender==undefined ? "Unknown" : primary.gender,
	    					    		"age":primary.age==undefined ? null:primary.age,
	    					    		"homeTown":details==undefined ? primary.homeTown:details.address_components[0].long_name,
	    					    		"city":details==undefined ? primary.city : city,
	    					    		"state":details==undefined ? primary.state : state,
	    					    		"country":details==undefined ? primary.country : country,
	    					    		"countryCode":details==undefined ? primary.countryCode : countryCode,
	    					    		"incomeLevel":primary.incomeLevel==undefined ? null:primary.incomeLevel,
	    					    		"occupation":primary.occupation==undefined ? null : primary.occupation,
	    					    		"religion":primary.religion==undefined ? null : primary.religion,
	    					    		"lat":details==undefined ? primary.lat : details.geometry.location.lat(),
	    					    		"long":details==undefined ? primary.long :details.geometry.location.lng()}).then(function (data) {
	    					    			console.log(data)
	    					    			if(!data.error){

	    					    				$scope.edit=false
	    					    				if($scope.imageupload){
	    					    				$scope.userimageuploadfun(data.resp.id, toShowToaster)
	    					    				}else{
	    					    					if(toShowToaster =='created'){
	    					    						toaster.pop('success', "User", "User updated succesfully");
	    					    					}
	    					    					
	    					    	   				 $location.path('userprofile/'+userid)
	    					    	   				$route.reload()
	    					    				}
	    					    			}

	    					    	}, function error(data) {
	    					    		console.log(data)
	    				    			if(data.data.error){
	    				    			toaster.pop('error', "User", data.data.message);
	    				    		}

	    				            });
	    }

		 //Update User function ends

	    /*Create/Register User function ends*/

	    /*Change Status User starts*/
	    $scope.changestatus=function(data){

	    	Restangular.all('users/'+data.id+'/status').post({"status":data.status}).then(function (data) {
	    		console.log(data)
	    		console.log(data.resp.status)
//				Notification.error({message: 'Deleted Successfull'});
				$location.path("user")
			});

	    }
	    /*Change Status User ends*/


		/*File select for image uploading starts*/
		  $scope.onFileSelect = function($files) {
			  //if($scope.edit==true){
			    //$files: an array of files selected, each file has name, size, and type.
			    for (var i = 0; i < $files.length; i++) {
			      var file = $files[i];
			      $scope.imageupload=file
	                var reader = new FileReader();
	                reader.onload = function (e) {
	                    $('#userimage').attr('src', e.target.result);
	                }
	                reader.readAsDataURL(file);
			    }
		 // }
			  };
			  /*File select for image uploading ends*/

	    /*Create Tabs as per user role ends*/
	    if($scope.userstatus=="created"  && $rootScope.userdata.resp.roleId==1 || $rootScope.userdata.resp.roleId==2 ){

		      $scope.tabs = [
		                     {
		                       title: "User's Survey",
		                       pageurl: "static/partials/templates/adminusersurvey.html"

		                     }, {
		                       title: "Surveys Taken",
		                       pageurl: "static/partials/templates/adminusersurveytaken.html"


		                     }
		                   ];
		     // $scope.current("User's Survey")
		}else if($scope.userstatus=="created"  && $rootScope.userdata.resp.roleId==3){

		      $scope.tabs = [
		                     {
		                       title: "My Surveys",
		                       pageurl: "static/partials/templates/sponsorusersurvey.html"

		                     }, {
		                       title: "Surveys I have Taken",
		                       pageurl: "static/partials/templates/sponsorusersurveytaken.html"


		                     }
		                   ];
		     // $scope.current("User's Survey")
		}
		$scope.redirectUserTo = function(userStatus){
			if(userStatus == "new"){
				$location.path("/user")
			}else{
				$scope.edit = false;
				angular.copy($scope.masterUser, $scope.user);
//				$route.reload()
			}
		}
	/*Create Tabs as per user role ends*/

}]);

app.controller('UsersurveysCtrl',['$scope','$filter','$routeParams','$route','$rootScope','$location','$injector','ngTableParams','$modal','Restangular',function($scope, $filter,$routeParams,$route,$rootScope,$location,$injector,ngTableParams,$modal,Restangular) {

	if($rootScope.userdata.resp.roleId==1 || $rootScope.userdata.resp.roleId==2)
	{


	//$timeout(function() {
		//$scope.pageurl="static/partials/templates/adminusersurvey.html"
	$scope.usersurvey = new ngTableParams({
	page : 1, // show first page
	count : 5
// count per page
}, {
	total : 0, // length of data
	getData : function($defer, params) {
	    var offset= (params.$params.page - 1) * params.$params.count
        var limit= params.$params.count
		Restangular.one('users/'+$routeParams.userid+'/surveys?offset='+offset+'&limit='+limit+'').get().then(function (data) {
			 if(!data.error){
    			 params.total(data.resp.count)
    			 $defer.resolve(data.resp.results);
    		 }

	    }, function error(data) {
    		console.log(data)
			if(data.data.error){
			toaster.pop('error', "Surveys", data.data.message);
		}

        });
	}

});
	//},2000);
	//Users Survey ends for admin
}
else if($rootScope.userdata.resp.roleId==3){
	//$scope.pageurl="static/partials/templates/sponsorusersurvey.html"
		//Users Survey starts for sponsoradmin
	$scope.sponsorusersurvey =  new ngTableParams({
		page : 1, // show first page
		count : 5
	// count per page
	}, {
		total : 0, // length of data
		getData : function($defer, params) {
		    var offset= (params.$params.page - 1) * params.$params.count
            var limit= params.$params.count
			Restangular.one('users/'+$routeParams.userid+'/surveys?offset='+offset+'&limit='+limit+'').get().then(function (data) {
				 if(!data.error){
        			 params.total(data.resp.count)
        			 $defer.resolve(data.resp.results);
        		 }

		    }, function error(data) {
	    		console.log(data)
				if(data.data.error){
				toaster.pop('error', "Surveys", data.data.message);
			}

	        });
		}

	});


	//Users Survey ends for sponsoradmin
}



}]);


app.controller('UsersurveystakenCtrl',['$scope','$filter','$routeParams','$route','$rootScope','$location','$injector','ngTableParams','$modal','Restangular',function($scope, $filter,$routeParams,$route,$rootScope,$location,$injector,ngTableParams,$modal,Restangular) {


	if($rootScope.userdata.resp.roleId==1 || $rootScope.userdata.resp.roleId==2){
	    //Surveys Taken starts
		$scope.surveyaccords=[]
	       $scope.current=1
	        $scope.maxSize = 5;

			$scope.limit=5;
			  $scope.status = {
			    isFirstOpen: true,
			    isFirstDisabled: false
			  };
			 $scope.oneAtATime = true;
			 $scope.getSurveys=function(){
				Restangular.one('users/'+$routeParams.userid+'/surveys/response?offset='+($scope.current - 1) * $scope.limit+'&limit='+$scope.limit+'').get().then(function (data) {
					 if(!data.error){
						 console.log(data.resp.count)
						 $scope.surveyaccords=[]
						 $scope.surveylistcount=data.resp.count
	        			console.log(data.resp.results);

						 angular.forEach(data.resp.results, function(value, key) {
							 console.log(value.surveyName)
							  $scope.surveyaccords.push({title:value.surveyName,
								  						 pageurl:"static/partials/templates/sponsorusersurveytakenlist.html",
								  						 content:value
								       		     });

						 });


	        		 }
			    	console.log($scope.surveyaccords)

			    }, function error(data) {
		    		console.log(data)
					if(data.data.error){
					toaster.pop('error', "Surveys", data.data.message);
				}

		        });

	}
			 $scope.getSurveys()

			  $scope.$watch('surveyaccords', function(groups){
				    angular.forEach(groups, function(group, idx){
				      if (group.open) {
				    	  $scope.questionlist={}
				     Restangular.one('users/'+$routeParams.userid+'/surveys/'+group.content.surveyId+'/response').get().then(function (data) {
				    	console.log(data)

				    	if(!data.error){
				    		$scope.questionlist=data.resp

				    	}
				     }, function error(data) {
				    		console.log(data)
							if(data.data.error){
							toaster.pop('error', "Surveys", data.data.message);
						}

				        });

				     }
				    })
				  }, true);


    	//Surveys Taken ends
    }else if($rootScope.userdata.resp.roleId==3){

		$scope.surveyaccords=[]
       $scope.current=1
        $scope.maxSize = 5;

		$scope.limit=5;
		  $scope.status = {
		    isFirstOpen: true,
		    isFirstDisabled: false
		  };
		 $scope.oneAtATime = true;
		 $scope.getSurveys=function(){
			Restangular.one('users/'+$routeParams.userid+'/surveys/response?offset='+($scope.current - 1) * $scope.limit+'&limit='+$scope.limit+'').get().then(function (data) {
				 if(!data.error){
					 console.log(data.resp.count)
					 $scope.surveyaccords=[]
					 $scope.surveylistcount=data.resp.count
        			console.log(data.resp.results);

					 angular.forEach(data.resp.results, function(value, key) {
						 console.log(value.surveyName)
						  $scope.surveyaccords.push({title:value.surveyName,
							  						 pageurl:"static/partials/templates/sponsorusersurveytakenlist.html",
							  						 content:value
							       		     });

					 });


        		 }
		    	console.log($scope.surveyaccords)

		    }, function error(data) {
	    		console.log(data)
				if(data.data.error){
				toaster.pop('error', "Surveys", data.data.message);
			}

	        });

}
		 $scope.getSurveys()

		  $scope.$watch('surveyaccords', function(groups){
			    angular.forEach(groups, function(group, idx){
			      if (group.open) {
			    	  $scope.questionlist={}
			     Restangular.one('users/'+$routeParams.userid+'/surveys/'+group.content.surveyId+'/response').get().then(function (data) {
			    	console.log(data)

			    	if(!data.error){
			    		$scope.questionlist=data.resp

			    	}
			     });

			     }
			    })
			  }, true);

    }
	


}]);
