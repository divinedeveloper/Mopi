app.controller('SurveychartdetailsCtrl', [
			                             		'$scope',
			                             		'ngTableParams',
			                             		'$routeParams',
			                             		'Restangular',
			                             		'tags',
			                             		'$location','$injector','$timeout','$filter','constantvalue','$rootScope',
			                             		function($scope, ngTableParams, $routeParams, Restangular, tags,$location,$injector,$timeout,$filter,constantvalue,$rootScope) {
			
			                             			$scope.graphconstant={};
			                             			$scope.graphconstant.age= constantvalue.age
			                             			$scope.graphconstant.income=constantvalue.income
			                             			$scope.graphconstant.gender= constantvalue.gender
			                             			$scope.graphconstant.occupation=  constantvalue.occupation
			                             			$rootScope.breadcrum=[{"name":"Survey","url":"survey"},
			            							                      {"name":"SurveyDetails","url":"surveydetails/"+$routeParams.surveyid},
			            							                      {"name":"Summary","url":"surveychartdetails/"+$routeParams.surveyid}]
			                             			$scope.breadCrumb.hideBreadCrumb = false
			                             			$scope.pieChart={}
			                             			
			                             			  $scope.piechart=function(type){
			                             				  $scope[type]={}
	                							    	  $scope[type].data=[]
			                							Restangular.one("surveys",$routeParams.surveyid).one("questions",$scope.question).one("image").get({"chartType":type}).then(function (data) {
			                								if(!data.error){
			                								$scope[type].commenttype=data.result
			                								$scope[type].length=data.result.length
			                								console.log($scope[type])
			                								$scope[type].data1=$scope.formatteddata($scope.graphconstant[type],data.result)
			                						console.log($scope[type].data1)			              
			                			        		    	  angular.forEach(data.result,function(value){
			                             		    		  if(value._id!=null){
			                             		    		  $scope[type].data.push({
			                             		                  label: value._id,
			                             		                  data: value.count
			                             		                })
			                             		    		  }

			                             		    	  })
			                									
//			                								$scope[type].data = [
//			                							        {
//			                							          label: type,
//			                							          data: $scope[type].data1,
//			                							          bars: {
//			                							            order: 0
//			                							          }
//			                							        }
//			                							      ];
			                								console.log($scope[type].data)
			                							}
			                									})


			                             		    	  }
			                             			
			                             		      $scope.pieChart.options = {
			                             			        series: {
			                             			          pie: {
			                             			            show: true,
			                             			           radius: 1,
			                             			           label: {
			                             			                show: true,
//			                             			                combine: {
//			                             			                    color: '#999',
//			                             			                    threshold: 0.1
//			                             			                },
			                             			                radius: 3/4,
			                             			                formatter: function(label, series){
			                             			                    return '<div style="font-size:8pt;text-align:center;padding:2px;color:white;">'+label+'<br/>'+Math.round(series.percent)+'%</div>';
			                             			                },
			                             			                background: { opacity: 0.5 }
			                             			            }
			                             			          }
			                             			        },
			                             			        legend: {
			                             			          show: false
			                             			        },
			                             			        grid: {
			                             			         hoverable: true,
			                             			          clickable: true
			                             			        },
			                             			        colors: [$scope.color.primary, $scope.color.success, $scope.color.info, $scope.color.warning, $scope.color.danger],
			                             			        tooltip: true,
			                             			       tooltipOpts: {
			                             			          content: "%p.0%, %s",
			                             			          defaultTheme: false
			                             			        }
			                             			      }; 			
			                             			
			                             			$scope.formatteddata=function(clientdata,serverdata){

			                             			      
			                             		        var tempformated=[]
			                             		    	angular.forEach(clientdata,function(value){
			                             		    	var filtereddata=$filter("filter")(serverdata,value._id)
			                             		  
			                             		    			tempformated.push([value._id,filtereddata.length>0?filtereddata[0].count:0])
			                             		    			
			                             		    		
			                             					
			                             				})
			                             				console.log(tempformated)
			                             				return tempformated
			                             		     }
			                             			  $scope.barchartoptions = {
			                          				        series: {
			                          				          stack: true,
			                          				          bars: {
			                          				            show: true,
			                          				            fill: 1,
			                          				            barWidth: 0.2,
			                          				            align: "center",
			                          				            horizontal: false
			                          				          }
			                          				        },
			                          				      xaxis: {
			                          						mode: "categories",
			                          						tickLength: 0,
			                          						rotateTicks: 140,
			                          						tickFormatter: function (value, axis) {
			                          		                    return value
			                          		                } 
			                          					},
			                          					yaxis:{
			                          						tickDecimals:0
			                          					},
			                          				        grid: {
			                          				          hoverable: true,
			                          				          borderWidth: 1,
			                          				          borderColor: "#eeeeee"
			                          				        },
			                          				        tooltip: true,
			                          				        tooltipOpts: {
			                          				          defaultTheme: false
			                          				        },
			                          				        colors: [$scope.color.success, $scope.color.info, $scope.color.warning, $scope.color.danger]
			                          				      };
			                          					  
			           
			
			
					$scope.urlpath={}
					$scope.urlpath.surveydetails=$scope.environment=="Development"?"surveys/"+$routeParams.surveyid:$scope.staticjsonpath+"/surveydetails.json";
				
				
					  
					
				
						
						$scope.chart=function(type){
							$scope[type]={}
							//$scope[type].data='';
//							$scope[type].data1=''
							Restangular.one("surveys",$routeParams.surveyid).one("questions",$scope.question).one("image").get({"chartType":type}).then(function (data) {
								
								if(!data.error){
								$scope[type].commenttype=data.result
								console.log($scope[type])
								$scope[type].data1=$scope.formatteddata($scope.graphconstant[type],data.result)
								console.log($scope[type].data1)
								if(type=='gender'){
									$scope[type].data2=$scope.gformatteddata($scope.graphconstant[type],data.result)	
									console.log($scope[type].data2)
									console.log("gender")
									$scope[type].datas = [
								{
								label: type,
								data: $scope[type].data2,
								bars: {
								order: 0
								}
								}
								];
									
								}else{								  
								$scope[type].data = [
							        {
							          label: type,
							          data: $scope[type].data1,
							          bars: {
							            order: 0
							          }
							        }
							      ];
								}
								
							}
									})
							
							
						}
					
					
						Restangular.one($scope.urlpath.surveydetails).get().then(function (data) {
		
						  $scope.survey=data.resp
						  if(data.resp.questionList[0] !=undefined || data.resp.questionList[0] !=null){
						  $scope.question=data.resp.questionList[0].id
						  $scope.chart("no")
						  $scope.chart("income")
						   $scope.chart("age")
						    $scope.chart("gender")
						    $scope.piechart("occupation")
//					       
						    $scope.piechart("overall")
						}
					  })
					  
					/*income by sex end*/
					  
					  
					  $scope.gformatteddata=function(clientdata,serverdata){
							   var genderformated=[]
								var fg=1
								  for(var i=0;i<clientdata.length;i++){
									  for(var j=0;j<serverdata.length;j++){
										  if(clientdata[i]._id == serverdata[j]._id){
											 genderformated.push([serverdata[j]._id,serverdata[j].count])
											  fg=0
											  break
										  }
										  else {
											  fg=1
										  }
										  
									  }
									  if(fg==1)
										  {
										  console.log(clientdata[i]._id )
										  genderformated.push([clientdata[i]._id,0])
										  }
									  console.log(genderformated)
								  }
					    		return genderformated
						}
				    
					  
				} ])
