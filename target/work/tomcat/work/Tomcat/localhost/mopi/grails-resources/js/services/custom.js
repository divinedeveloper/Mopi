app.factory('dataService', function ($q, $timeout,ipCookie,$http,Restangular,$rootScope,$filter,$location) {
    return { 
        data : {},
        load : function(auth,role) {
        	
        	var defer = $q.defer();
        	if(auth==true){
        	if(angular.isDefined(ipCookie('logininfo'))){
        		console.log(ipCookie('logininfo').access_token)
        	$http.defaults.headers.common['X-Auth-Token']=ipCookie('logininfo').access_token
        	$http.defaults.headers.common['Content-Type']="application/json"
        	}
        	 Restangular.one('users/loggedInUser').get().then(function (data) {
        		 $rootScope.userdata=data        
        		if($filter("filter")(role,data.resp.roleName).length>0){
        			
        			  defer.resolve(data);	
        		}
        		
   		  }, function error(data) {
	    		if(data.status=="401"){
	    			$location.path("login")
	    		}
    		
            
            });
        	}
        	if(auth==false){
        		defer.resolve();
        		
        	}
        	
         
            return defer.promise;
        }
    };
});

app.factory('dataFactory', ['$http','Restangular', function($http,Restangular) {


	  var dataFactory = {};

    dataFactory.updatecreateQuestion= function (surveyid,postdata,questionid) {
    	
    	if(questionid=="newquestion"){
        return  Restangular.all("surveys/"+surveyid+"/questions").post(postdata).then(function (data) {
        	$location.path("question/"+data.resp.id+"/"+surveyid)
		  })
    	}else{
    		return Restangular.all("surveys/"+surveyid+"/questions/"+questionid).customPUT(postdata).then(
					function(data) {},function(){});	
    		
    	}
    };
 


    return dataFactory;
}]);

app.service('tags', function($q,Restangular,$http,$environment,$filter) {
	var countries=[]
//	  Restangular.one('../../static/json/country.json').get().then(function (data) {
//		  cities=data
//		})
		 Restangular.one('../../static/json/newcountries.json').get().then(function (data) {
		console.log(data[0].name.common)
		countries=data
		})
			  this.load = function(params) {
				  var temptags=[]
				  var deferred = $q.defer();
				  Restangular.one('tags',params).get().then(function (data) {
		        angular.forEach(data.resp,function(value,index){
		        	temptags.push({"text":value.name}) 	
		        })
		   		 
			    
			    deferred.resolve(temptags);
				  })
			    return deferred.promise;
			  },
			  this.countries = function(params) {
				  var tempcountries=[]
				  var tempcountries=$filter('filter')(countries, params)
				  angular.forEach(tempcountries,function(value){
					  console.log(value)
					  value.text=value.name.common
					  value.latitude=value.latlng[0]
					  value.longitude=value.latlng[1]
					  value.countryCode=value.cca2
					  
				  })
				  var deferred = $q.defer();
					deferred.resolve(tempcountries);
					
			    return deferred.promise;
			  };
			  ;
			});