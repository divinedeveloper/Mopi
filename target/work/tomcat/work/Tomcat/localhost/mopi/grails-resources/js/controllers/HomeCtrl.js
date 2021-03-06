app.controller('HomeCtrl',['configsetting','$scope', '$http','$rootScope', 'Restangular','$location','$route','$environment','ipCookie','$timeout',function(configsetting,$scope,$http,$rootScope,Restangular,$location,$route,$environment,ipCookie,$timeout) {
	
	 Restangular.setBaseUrl($environment.url)
$scope.environment=configsetting.environment
	$scope.staticjsonpath=configsetting.jsonurl
/*$rootScope.breadcrum=[{"name":"Home","url":"dashboard"},
                      {"name":"Dashboard","url":"dashboard"},
                      {"name":"temp","url":"user"}]*/
//	if(ipCookie('logininfo')){
    $scope.authenticationfunction = function (data) {
    	if(angular.isDefined($rootScope.userdata) && $rootScope.userdata !=null){
        for (i = 0; i < data.length; i++) {
            if (data[i] == $rootScope.userdata.resp.roleName) {
                return true
                break;
            }
     
        }
    	}
        /* return true*/
    }
	 
	 $scope.breadCrumb = {}
	 $scope.breadCrumb.hideBreadCrumb = false
	
	

//}
	
	 $scope.isActive = function (route) {
         return route === $location.path();
         console.log($location.path())
     }	 
	 
$scope.reload=function(page){
	
	$location.path(page)
	$route.reload()
	
}
	  $scope.logout=function(){
		
		  Restangular.all('logout').post().then(function (data) {
			  if(data.status=="200"){
		 window.localStorage.removeItem("X-Auth-Token")
		 ipCookie.remove('logininfo')
			  $location.path('/login')
			  $rootScope.userdata = null
		  }
		  });
		  
	  }
    
    var $window;
    $window = $(window);
    $scope.main = {
      brand: 'Mopi',
      name: 'Lisa Doe'
    };
    $scope.pageTransitionOpts = [
      {
        name: 'Fade up',
        "class": 'animate-fade-up'
      }, {
        name: 'Scale up',
        "class": 'ainmate-scale-up'
      }, {
        name: 'Slide in from right',
        "class": 'ainmate-slide-in-right'
      }, {
        name: 'Flip Y',
        "class": 'animate-flip-y'
      }
    ];
    $scope.admin = {
      layout: 'wide',
      menu: 'vertical',
      fixedHeader: true,
      fixedSidebar: true,
      pageTransition: $scope.pageTransitionOpts[0],
      skin: '11'
    };
    $scope.$watch('admin', function(newVal, oldVal) {
      if (newVal.menu === 'horizontal' && oldVal.menu === 'vertical') {
        $rootScope.$broadcast('nav:reset');
        return;
      }
      if (newVal.fixedHeader === false && newVal.fixedSidebar === true) {
        if (oldVal.fixedHeader === false && oldVal.fixedSidebar === false) {
          $scope.admin.fixedHeader = true;
          $scope.admin.fixedSidebar = true;
        }
        if (oldVal.fixedHeader === true && oldVal.fixedSidebar === true) {
          $scope.admin.fixedHeader = false;
          $scope.admin.fixedSidebar = false;
        }
        return;
      }
      if (newVal.fixedSidebar === true) {
        $scope.admin.fixedHeader = true;
      }
      if (newVal.fixedHeader === false) {
        $scope.admin.fixedSidebar = false;
      }
    }, true);
    $scope.color = {
      primary: '#5B90BF',
      success: '#A3BE8C',
      info: '#7FABD2',
      infoAlt: '#B48EAD',
      warning: '#EBCB8B',
      danger: '#BF616A',
      gray: '#DCDCDC'
    };
    
   
    
}]);