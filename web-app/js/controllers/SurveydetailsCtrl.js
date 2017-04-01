app.controller('SurveydetailsCtrl', [
'$timeout',
		'$scope',
		'ngTableParams',
		'$routeParams',
		'Restangular',
		'tags',
		'$location','$injector','toaster','$rootScope','$upload','$route','$filter',
		function($timeout,$scope, ngTableParams, $routeParams, Restangular, tags,$location,$injector,toaster,$rootScope,$upload,$route,$filter) {
			$scope.urlpath={}
			$scope.surveyid=$routeParams.surveyid
			$rootScope.breadcrum=[{"name":"Survey","url":"survey"},
			                      {"name":"SurveyDetails","url":"surveydetails/"+$routeParams.surveyid}]
			$scope.breadCrumb.hideBreadCrumb = false
			$scope.surveystatus = $routeParams.surveyid == 'newsurvey' ? 'new'
					: 'created'

						   // Injector
			            var $validationProvider = $injector.get('$validation');


			            // Initial Value
			            $scope.form = {
			                requiredCallback: 'required',
			                checkValid: $validationProvider.checkValid,
			                submit: function (form) {
			                	console.log(form)
			                    // angular validation 1.2 can reduce this
								// procedure, just focus on your action
			                    $validationProvider.validate(form);
			                },
			                reset: function () {
			                    // angular validation 1.2 can reduce this
								// procedure, just focus on your action
			                    // $validationProvider.reset(form);
			                }
			            };
			            console.log($rootScope.userdata)
			$scope.visibleto = [ {
				"name" : "Public",
				"value" : "EveryOne"
			}, {
				"name" : "Followers",
				"value" : "Followers"
			}]
			
			$scope.showResults = [{
				"name" : "Yes",
				"value" : true
			},{
				"name" : "No",
				"value" : false
			}]           

			$scope.survey = {}

			if ($scope.surveystatus == "new") {
				$scope.edit = true
				$scope.survey.cityList=[]
				$scope.survey.cityWithLocation=[]
				

			}
			$scope.question  = [ {
				name : 'Question 1',
				date : '27-7-2014',
				userreplied : 50,
				usercreated : "Tushar"
			}, {
				name : 'Question 2',
				date : '27-7-2014',
				userreplied : 50,
				usercreated : "Namdeo"
			}, {
				name : 'Question 3',
				date : '27-7-2014',
				userreplied : 50,
				usercreated : "Sunil"
			}, {
				name : 'Question 4',
				date : '27-7-2014',
				userreplied : 50,
				usercreated : "Dony"
			}, {
				name : 'Question 5',
				date : '27-7-2014',
				userreplied : 50,
				usercreated : "Test user"
			} ];
	
			$scope.status=[{"status":"Active","name":"Active"},
			               {"status":"Draft","name":"Draft"},
			               {"status":"hidden","name":"Hidden"},
			               {"status":"flagged","name":"Flagged"},
			               {"status":"Autohidden","name":"Autohidden"}
			               ]

			  $scope.options = {
				    types: '(cities)'
				    };
			
			$scope.setfocus=function(){
				angular.element('#focusset').focus()
			}
			
			
	/*Set Survey end date as start date starts*/
			$scope.setenddate=function(date){
				console.log($scope.survey.startTime)
				//$scope.survey.endTime=''
					//$scope.emindate=date
					//$scope.survey.endTime=''
				if($scope.survey.endTime){
				    if( new Date($scope.survey.endTime) < $scope.survey.startTime ){
				    	$scope.survey.endTime = null;
				    	$scope.survey.emindate = $scope.survey.startTime
				    	console.log($scope.survey.emindate)
				    }
				  } 
//				else {
				    // just set the end date
//					  $scope.survey.endTime = '';
//					  console.log("in else")
					  $scope.survey.emindate = $scope.survey.startTime
				    
//				  }
			}
//			$scope.setenddate=function(date){
//				console.log($scope.survey.startTime)
			
			/*$scope.$watch('survey.startTime', function(newVal, oldVal){ 
				 if(!newVal) return;

//				  // if the new start date is bigger than the current end date .. update the end date
//				  if($scope.survey.endTime){
//				    if( $scope.survey.endTime < $scope.survey.startTime ){
//				      //$scope.survey.endTime = newVal;
//				    	$scope.survey.emindate = newVal
//				    }
//				  } else {
//				    // just set the end date
//					  $scope.survey.endTime = '';
//					  console.log("in else")
//					  $scope.emindate = newVal
//				    
//				  }
				 
				 if(newVal!=oldVal){
					 $scope.survey.endTime = null;
					 $scope.emindate = newVal
				 }
			});*/
//			}
			/*Set Survey end date as start date ends*/
			
		/*Survey Image Upload function starts*/
			$scope.surveyimageuploadfun=function(surveyid,type){
				//if($scope.imageupload){
				$scope.upload = $upload.upload({
			        url: 'api/v1/surveys/'+surveyid+'/uploadImage',
			        file: $scope.imageupload, 
			        fileFormDataName: 'myfile',
			      }).progress(function(evt) {
			        console.log('percent: ' + parseInt(100.0 * evt.loaded / evt.total));
			      }).success(function(data, status, headers, config) {
			        // file is uploaded successfully
			    	  if(!data.error && type=="new"){
			    		  $location.path("surveydetails/" + data.resp.id)
								 toaster.pop('success', "Survey  added succesfully");
							
			    	  }
			    	  console.log("after image upload success");
			    	  console.log(data.resp.imageUrl)
			    	  $scope.survey.imageUrl = data.resp.imageUrl 
			        console.log(data);
			      })
				//}
				
			}
			
			/*Survey Image Upload function ends*/
		
			$scope.createSurvey = function(validity) {
				if(validity==true){
				$scope.survey.hashTags = []
				$scope.survey.countryList=[]
				$scope.survey.countryWithLocation=[]
				$scope.survey.tempclist=[]
				if ($scope.survey.startTime)
					$scope.survey.startTime = moment($scope.survey.startTime)
							.format("YYYY-MM-DD")

				if ($scope.survey.endTime)
					$scope.survey.endTime = moment($scope.survey.endTime)
							.format("YYYY-MM-DD")
				angular.forEach($scope.survey.tags, function(value, index) {
					$scope.survey.hashTags.push(value.text);
					
				})
				angular.forEach($scope.survey.countrytag, function(value, index) {
					//$scope.survey.countryList.push(value.text);
					console.log(value.latlng)
					$scope.survey.countryWithLocation.push({"latitude":value.latitude,"longitude":value.longitude,"name":value.text,"countryCode":value.countryCode})
				})
				console.log($scope.survey.tempclist)
if ($scope.surveystatus == "new") {

				Restangular.all('surveys').post($scope.survey).then(
						function(data) {
							$scope.edit=false
							if($scope.imageupload){
							$scope.surveyimageuploadfun(data.resp.id,'new')
							}else{
								 toaster.pop('success', "Survey  added succesfully");
								  $location.path("surveydetails/" + data.resp.id)
									
							}
						
							
						},function(data){
							console.log(data)
							toaster.pop('error',"Survey",data.data.message);
//							$route.reload()
						});
}
				if ($scope.surveystatus == "created") {
					Restangular.all('surveys/'+$routeParams.surveyid).customPUT($scope.survey).then(
							function(data) {
							$scope.edit=false
							$scope.user.location = ""
							$scope.survey.cityWithLocation = data.resp.cityWithLocation
							if($scope.imageupload){
							$scope.surveyimageuploadfun($routeParams.surveyid,'edit')
							}
//							$scope.imageupload = null
//							$route.reload()
							//copying survey to master survey
							$scope.masterSurvey= angular.copy(data.resp);
							$location.path("surveydetails/" + data.resp.id)
							toaster.pop('success', "Survey", "Survey updated succesfully");
							
							});
	}
			}
			}

			$scope.loadTags = function(query) {
				return tags.load(query);
			};
			$scope.loadcountries= function(query) {
				
				console.log(query)
				
				return tags.countries(query);
			};

			if($scope.surveystatus=="created"){
				$scope.urlpath.surveydetails=$scope.environment=="Development"?"surveys/"+$routeParams.surveyid:$scope.staticjsonpath+"/surveydetails.json";
				
			  Restangular.one($scope.urlpath.surveydetails).get().then(function (data) {
				 var hashtagformat=[] 
				 var  hashtagcity=[]
				 
				  angular.forEach(data.resp.HashTag,function(value){
					  hashtagformat.push({"text":value})	  
				  })
//				  $scope.count = hashtagformat.length
				   angular.forEach(data.resp.countryWithLocation,function(value){
					  hashtagcity.push({"text":value.name,"latitude":value.latitude,"longitude":value.longitude,"countryCode":value.countryCode})	  
				  })
   
				  data.resp.tags=hashtagformat
				  data.resp.countrytag=hashtagcity
				  $scope.survey=data.resp
				  $scope.masterSurvey= angular.copy(data.resp);
				 
//				  $scope.user.location = null
			  })
			}
			
			$scope.$watch("details", function (nv,ov) {
			
				 if (nv != ov ) {
					console.log(nv)
					
						    	var result = nv;
//look for locality tag and administrative_area_level_1
var state = "";
var countryCode="";
var country="";
console.log(result.address_components.length)
for(var i=0, len=result.address_components.length; i<len; i++) {

var ac = result.address_components[i];
if(ac.types.indexOf("administrative_area_level_1") >= 0) state = ac.long_name;
if(ac.types.indexOf("country") >= 0) country = ac.long_name;
if(ac.types.indexOf("country") >= 0) countryCode = ac.short_name;
}
//only report if we got Good Stuff
if(state != '') {

console.log(state);
console.log(country);
console.log(countryCode)

}
console.log("df")
console.log($scope.survey.cityWithLocation)
console.log($filter("filter")($scope.survey.cityWithLocation,nv.name))
/*if(angular.isDefined($scope.survey.cityWithLocation)){
	
					$scope.survey.cityWithLocation.push({"latitude":nv.geometry.location.lat(),"longitude":nv.geometry.location.lng(),"name":nv.name,"state":state,"country":country,"countryCode":countryCode})
				 }*/
				 //$scope.survey.cityList.push(nv.name);

if($filter("filter")($scope.survey.cityWithLocation,nv.name).length==0 && angular.isDefined($scope.survey.cityWithLocation)){
	
	$scope.survey.cityWithLocation.push({"latitude":nv.geometry.location.lat(),"longitude":nv.geometry.location.lng(),"name":nv.name,"state":state,"country":country,"countryCode":countryCode})
}else{
	
	console.log("error")
}
				$scope.user.location = null
					console.log($scope.user.location)
				 }
			  });
		
		
			$scope.changeStatus=function(){
				Restangular.all('surveys/'+$routeParams.surveyid+'/status').customPUT({"status":$scope.survey.status}).then(
							function(data) {
							$scope.edit=false
							toaster.pop('success', "Survey", "Survey updated succesfully");
							$route.reload()
							},function(data){
								console.log(data)
								toaster.pop('error',"Survey",data.data.message);
								$route.reload()
							});
			}
			
			/*Question redirect function starts only for sponsoradmin*/
			$scope.questionredirect=function(status,qid,surveyid,mode){
				console.log(status)
				if(status==true && mode !='Active' && mode !='flagged'){
					
					$location.path('question/'+qid+'/'+surveyid)
				}
				
			}
			/*Question redirect function ends  only for sponsoradmin*/
			
			
			
			/*Delete survey starts*/
			$scope.deleteSurvey=function(){
			
				Restangular.one("surveys", $routeParams.surveyid).customDELETE().then(function(){
					toaster.pop('success', "Survey", "Survey deleted succesfully");
					
				},function(data){
					toaster.pop('error',"Survey", data.data.message);
				});
			}
		/*Delete survey ends*/
			
			/*File select for image uploading starts*/
			  $scope.onFileSelect = function($files) {
				    //$files: an array of files selected, each file has name, size, and type.
				    for (var i = 0; i < $files.length; i++) {
				      var file = $files[i];
				      $scope.imageupload=file
		                var reader = new FileReader();
		                reader.onload = function (e) {
		                    $('#giftimage').attr('src', e.target.result);
		                }
		                reader.readAsDataURL(file);			
				    }
				  
				  };
				  /*File select for image uploading ends*/
				  
		$scope.redirectTo = function(surveyStatus){
			if(surveyStatus == "new"){
				$location.path("/survey")
			}else{
				$scope.edit=false; 
				if(angular.isDefined($scope.user)){
					$scope.user.location = ""
				}
				angular.copy($scope.masterSurvey, $scope.survey);
			}
		}
		
		/*Delete question starts*/
		$scope.deleteQuestion=function(questionId){
		
			Restangular.one("surveys/"+$routeParams.surveyid+"/questions/"+questionId).customDELETE().then(function(data){
				$scope.survey.questionList = data.resp
				console.log(data.resp)
				toaster.pop('success', "Question", "Question deleted succesfully");
				
			},function(data){
				toaster.pop('error',"Question", data.data.message);
			});
		}
		/*Delete question ends*/
		
		$scope.bodyClick = function(){
			if(angular.isDefined($scope.user)){
				console.log($scope.user.location)
				$scope.user.location = ""
				console.log($scope.user.location)
			}
			
		}
	
		}])