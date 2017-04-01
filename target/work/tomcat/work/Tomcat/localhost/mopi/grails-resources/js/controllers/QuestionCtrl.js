app.controller('QuestionCtrl', [
		'$scope',
		'ngTableParams',
		'$routeParams',
		'Restangular',
		'tags',
		'$location','$injector','$filter','toaster','$upload','$rootScope',
		function($scope, ngTableParams, $routeParams, Restangular, tags,$location,$injector,$filter,toaster,$upload,$rootScope) {
		
			
			$rootScope.breadcrum=[{"name":"Survey","url":"survey"},
			                      {"name":"SurveyDetails","url":"surveydetails/"+$routeParams.surveyid},
			                      {"name":"Questions","url":"question/newquestion/"+$routeParams.surveyid}]
			
			$scope.noquestion=false
			$scope.breadCrumb.hideBreadCrumb = false
			/*Question Image Upload function starts*/
			$scope.questionimageuploadfun=function(surveyid,questionid,type){
				//if($scope.imageupload){
				$scope.upload = $upload.upload({
			        url: 'api/v1/surveys/'+surveyid+'/question/'+questionid+'/uploadImage',
			        file: $scope.imageupload, 
			        fileFormDataName: 'myfile',
			      }).progress(function(evt) {
			        console.log('percent: ' + parseInt(100.0 * evt.loaded / evt.total));
			      }).success(function(data, status, headers, config) {
			        // file is uploaded successfully
			    	  if(!data.error && type=="new"){
//			    		  $location.path("question/"+questionid+"/"+surveyid)
			    		  $location.path("surveydetails/"+surveyid)
			    		  toaster.pop('success', "Question", "Question added succesfully");
							
			    	  }
			        console.log(data);
			      })
				//}
				
			}
			
			/*Question Image Upload function ends*/
			
			/*File select for image uploading starts*/
			  $scope.onFileSelect = function($files,type) {
				  console.log($files)
				  console.log(type)
				    //$files: an array of files selected, each file has name, size, and type.
				    for (var i = 0; i < $files.length; i++) {
				      var file = $files[i];
				      $scope.imageupload=file
		                var reader = new FileReader();
		                reader.onload = function (e) {
		                    $('#giftimage'+type).attr('src', e.target.result);
		                }
		                reader.readAsDataURL(file);			
				    }
				  
				  };
				  /*File select for image uploading ends*/
			
		/*Update function for Comment/Slider/Binary type questions starts*/		  
			 $scope.updatefunction=function(surveyid,postdata,questionid){
			    
			    	
			    	if(questionid=="newquestion"){
			        return  Restangular.all("surveys/"+surveyid+"/questions").post(postdata).then(function (data) {
			        	
			        	console.log(data)
			        	if($scope.imageupload){
			        	$scope.questionimageuploadfun(surveyid,data.resp.id,'new')
			        	}else{
			        		toaster.pop('success', "Question", "Question added succesfully");
//			        		$location.path("question/"+data.resp.id+"/"+surveyid)
			        		$location.path("surveydetails/"+surveyid)
			        	}
			        	
					  })
			    	}else{
			    		return Restangular.all("surveys/"+surveyid+"/questions/"+questionid).customPUT(postdata).then(
								function(data) {
									if($scope.imageupload){
									$scope.questionimageuploadfun(surveyid,questionid,'edit')
									}
									toaster.pop('success', "Question", "Question updated succesfully");
									$location.path("surveydetails/"+surveyid)
								},function(){});	
			    		
			    	}
			 }
			 /*Update function for Comment/Slider/Binary type questions ends*/
			 
			 

			
			$scope.question=[{"name":"Binary","pageurl":"static/partials/binaryquestion.html","bindname":"binary","id":0},
			                 {"name":"Slider","pageurl":"static/partials/sliderquestion.html","bindname":"scale","id":1},
			                 {"name":"MCQ","pageurl":"static/partials/mcqquestion.html","bindname":"multiplechoice","id":2},
			                 {"name":"Comment","pageurl":"static/partials/commentquestiontype.html","bindname":"text","id":3}]
			
			$scope.questionid=$routeParams.questionid;
			if($scope.questionid!="newquestion"){
				  Restangular.one("surveys/"+$routeParams.surveyid+"/questions/"+$routeParams.questionid+"").get().then(function (data) {
$scope.questiondetails=data.resp
$scope.question[$filter("filter")($scope.question,{"bindname":data.resp.questionType})[0].id].active=true
$scope.$broadcast('update');

					  })
				}
			
		}])
		
		
		app.controller('BinaryCtrl', [
		'$scope',
		'ngTableParams',
		'$routeParams',
		'Restangular',
		'tags',
		'$location','$injector','$filter',"$location","dataFactory","toaster",
		function($scope, ngTableParams, $routeParams, Restangular,dataFactory, tags,$location,$injector,$filter,$location,toaster) {
			$scope.typeoption=""
				$scope.edit=false
				console.log($scope.noquestion)
$scope.binaryquestionoptions=[{"name":"Yes/No","bind":"yes/no"},{"name":"None","bind":"none"}]
		
			   $scope.$on('update', function(event, mass) {
				   $scope.userinput={}
				   if($scope.questiondetails.option!=null || $scope.questiondetails.option!=undefined){
				   $scope.userinput.one=$scope.questiondetails.option[0]
					$scope.userinput.two=$scope.questiondetails.option[1]
				   }
			   });
			
			
			$scope.saveQuestion=function(){
				if($scope.questiondetails!=null || $scope.questiondetails!=undefined){
$scope.options=[]
$scope.options=$scope.typeoption.bind=="none"?[$scope.userinput.one,$scope.userinput.two]:["yes","no"]
$scope.sendoption={"query":$scope.questiondetails.name,
		  "questionType":'binary',  
		  "option":$scope.options
		 }

	$scope.updatefunction($routeParams.surveyid,$scope.sendoption,$scope.questionid)

			}else{
				console.log("elsee")
				$scope.noquestion=true
				toaster.pop('warning', "Question", "Please Enter Question Name");
			}	
			}
			
			
						
		}])
				app.controller('SliderCtrl', [
		'$scope',
		'ngTableParams',
		'$routeParams',
		'Restangular',
		'tags',
		'$location','$injector','$filter','dataFactory','toaster',
		function($scope, ngTableParams, $routeParams, Restangular,dataFactory, tags,$location,$injector,$filter,toaster) {

				$scope.edit=true

				  $scope.$on('update', function(event, mass) {
					   $scope.userinput={}
					   if($scope.questiondetails.option!=null || $scope.questiondetails.option!=undefined){
					   $scope.userinput.one=$scope.questiondetails.option[0]
						$scope.userinput.two=$scope.questiondetails.option[1]
					   }
				   });
			
			
			$scope.saveQuestion=function(){
				if($scope.questiondetails!=null || $scope.questiondetails!=undefined){
				$scope.options=[]

					$scope.options=[$scope.userinput.one,$scope.userinput.two]

				$scope.updatefunction($routeParams.surveyid,{"query":$scope.questiondetails.name,
					  "questionType":'scale',  
					  "option":$scope.options
					 },$scope.questionid)
				}else{
					
					$scope.noquestion=true
					toaster.pop('warning', "Question", "Please Enter Question Name");
				}
			}
		}])
		
		
		app.controller('MCQquestionCtrl', [
		'$scope',
		'ngTableParams',
		'$routeParams',
		'Restangular',
		'tags',
		'$location','$injector','$filter','dataFactory','toaster','$upload','$timeout','$location','$modal',
		function($scope, ngTableParams, $routeParams, Restangular,dataFactory, tags,$location,$injector,$filter,toaster,$upload,$timeout,$location,$modal) {

				$scope.edit=true
				$scope.enterchoice=false
//$scope.mcqquestion=""
		
				$scope.mcqoptions=[]
				
			/*Open modal box starts*/
			    $scope.openmodal=function(type,index,data){
		if(type=='edit'){
			$scope.qtype='edit'
			$scope.editmode=true
			$scope.index=index
					$scope.mcq=data
		}else if(type=='new'){
			
			$scope.qtype='new'
			$scope.editmode=false
			$scope.mcq=''
		}
	            $scope.mcqmodal = $modal({
	                scope: $scope,
	                template: 'static/partials/modaltemplates/modaladdmultiplechoice.html',
	                show: true
	            });
	 
	        }
				/*Open modal box ends*/	
	  $scope.$on('update', function(event, mass) {
		   if($scope.questiondetails!=null || $scope.questiondetails!=undefined){
		   $scope.mcqoptions=$scope.questiondetails.options
		   }
	   });
	  
	  /*Add mcq options start*/
				$scope.addmcqstns=function(data,index,type){
					$scope.enterchoice=false
					if(type=="new" && data){
						$scope.mcq={}
						
							$scope.mcqmodal.$promise.then($scope.mcqmodal.hide);
					$scope.mcqoptions.push({"file":$scope.tempimagefile,"image":$scope.tempimage,"value":data.value})
					$scope.tempimagefile=null
					$scope.tempimage=null
					
					}else if(type=="edit" && data.value){
					
						$scope.mcqmodal.$promise.then($scope.mcqmodal.hide);
					$scope.mcqoptions[index]={"file":$scope.tempimagefile,"image":$scope.tempimage==undefined ?data.image:$scope.tempimage,"value":data.value}
					$scope.tempimagefile=null
					$scope.tempimage=null
	
					} else if(data==undefined || data.value==null || data.value==''){
						$scope.enterchoice=true
						toaster.pop('warning', "Question", "Please Enter Choice");
					}
				}
				 /*Add mcq options ends*/
				
				/*Cancel modal box starts*/
				$scope.cancel=function(data,type){
					console.log(data.value)
					$scope.imageupload=null
					$scope.tempimagefile=null
					$scope.tempimage = null
					
					if( type=='edit' && data.value==undefined || data.value==''){
						$scope.enterchoice=true
					}else if( type=='new') {
						$scope.enterchoice=false
						$scope.mcqmodal.$promise.then($scope.mcqmodal.hide);
					}else{
						$scope.mcqmodal.$promise.then($scope.mcqmodal.hide);
					}
					
				}
				
				
				/*Cancel modal box ends*/
				
				/*Save mcq question to show starts*/
			$scope.saveQuestion=function(data){
				console.log(data.length)
				if(data.length>=2 && data.length<=6 && $scope.questiondetails!=undefined){
				$scope.senddata=[]
angular.forEach(data,function(value,key){
	$scope.senddata.push({"id":key+1,"value":value.value,"image":value.image,"file":value.file})
});
				$scope.mcqupdatefunction($routeParams.surveyid,{"query":$scope.questiondetails.name,
					  "questionType":'multiplechoice',  
					  "option":$scope.senddata
					 },$scope.questionid,$scope.senddata)
				}else if($scope.questiondetails==undefined || $scope.questiondetails==null){
					
					$scope.noquestion=true
					toaster.pop('warning', "Question", "Please Enter Question Name");
				}
				else if(data.length<2){
					toaster.pop('warning', "Question", "Please Add Minimum 2 Choices");
				}
				else if(data.length>6){
					toaster.pop('warning', "Question", "Maximum 6 Choices can be added");
				}
			}
			/*Save mcq question to show ends*/	
			
			/*Delete mcq options starts*/
			$scope.deletemcq=function(index){
				//if($scope.mcqoptions.length>2){
					$scope.mcqoptions.splice(index,1)
				//} else if($scope.mcqoptions.length<=2){
				//	$scope.mcqoptions.splice(index,1)
				//	toaster.pop('warning', "Question", "Multiple ");
				//}
				//angular.forEach($scope.mcqoptions,function(value,key){
				
					
				//});
				
			}
			
			/*Delete mcq options ends*/
			
			 /*Update function for MCQ type questions starts*/	
			 
			 $scope.mcqupdatefunction=function(surveyid,postdata,questionid,questions){
				    
			    	
			    	if(questionid=="newquestion"){
			        return  Restangular.all("surveys/"+surveyid+"/questions").post(postdata).then(function (data) {
			        	
			        	console.log(data)
			        	if($scope.imageupload){
			        	$scope.mcqimageuploadfun(surveyid,data,'new',questions)
			        	}else{
			        		toaster.pop('success', "Question", "Question added succesfully");
//			        		$location.path("question/"+data.resp.id+"/"+surveyid)
			        		$location.path("surveydetails/"+surveyid)
			        	}
			        	
					  })
			    	}else{
			    		return Restangular.all("surveys/"+surveyid+"/questions/"+questionid).customPUT(postdata).then(
								function(data) {
									if($scope.imageupload){
									$scope.mcqimageuploadfun(surveyid,data,'edit',questions)
									}
									toaster.pop('success', "Question", "Question updated succesfully");
									$location.path("surveydetails/"+surveyid)
								},function(){});	
			    		
			    	}
			 }
			 
			 /*Update function for MCQ type questions ends*/	
			 
			 $scope.imagesendata=[]
				/*Question Image Upload function starts*/
				$scope.mcqimageuploadfun=function(surveyid,datas,type,questions){
					console.log(questions)
					//$scope.imagesendata.push({""})
					angular.forEach(datas.resp.options,function(value,key){
						console.log(key)
						if(questions[key].file !=undefined){
						$timeout(function() {
					$scope.upload = $upload.upload({
				        url: 'api/v1/surveys/'+surveyid+'/questions/'+datas.resp.id+'/options/'+value.id,
				      //  file: $scope.imageupload, 
				        file: questions[key].file,
				        fileFormDataName: 'myfile',
				      }).progress(function(evt) {
				        //console.log('percent: ' + parseInt(100.0 * evt.loaded / evt.total));
				      }).success(function(data, status, headers, config) {
				        // file is uploaded successfully
				    	  if(!data.error && type=="new" && key+1==questions.length){
//				    		  $location.path("question/"+datas.resp.id+"/"+surveyid)
				    		  $location.path("surveydetails/"+surveyid)
				    		  toaster.pop('success', "Question", "Question added succesfully");
								
				    	  }
				        console.log(data);
				      })
					},1000);
					}
					});
				}
				
				/*Question Image Upload function ends*/
				
				/*File select for image uploading starts*/
				  $scope.mcqonFileSelect = function($files) {
					  console.log($files)
					  $scope.tempimage=""
					    //$files: an array of files selected, each file has name, size, and type.
					    for (var i = 0; i < $files.length; i++) {
					      var file = $files[i];
					      $scope.imageupload=file
					      $scope.tempimagefile=file
			                var reader = new FileReader();
			                reader.onload = function (e) {
			                	$scope.tempimage=e.target.result
			                    $('#mcqimage').attr('src', e.target.result);
			                }
			                reader.readAsDataURL(file);			
					    }
					  
					  };
					  /*File select for image uploading ends*/ 
			 
		}])
		
			app.controller('CommentQuestionCtrl', [
		'$scope',
		'ngTableParams',
		'$routeParams',
		'Restangular',
		'tags',
		'$location','$injector','$filter','dataFactory','toaster',
		function($scope, ngTableParams, $routeParams,dataFactory, Restangular, tags,$location,$injector,$filter,toaster) {

				$scope.edit=true
$scope.mcqquestion=""
			
	  $scope.$on('update', function(event, mass) {
		   if($scope.questiondetails.option!=null || $scope.questiondetails.option!=undefined){
		   $scope.userinput=$scope.questiondetails.option[0]
		   }
		   
	   });
			
			$scope.saveQuestion=function(){
				if($scope.questiondetails!=null || $scope.questiondetails!=undefined){
				$scope.updatefunction($routeParams.surveyid,{"query":$scope.questiondetails.name,
					  "questionType":'text',  
					  "option":[$scope.userinput],
					 },$scope.questionid)
				}else{
					$scope.noquestion=true
					toaster.pop('warning', "Question", "Please Enter Question Name");
				}
			}
			

		}])