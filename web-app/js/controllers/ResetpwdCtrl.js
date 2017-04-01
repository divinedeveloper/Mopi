app.controller('ResetpwdCtrl', [
		'$scope','$http','$routeParams','Restangular','toaster',"$location",'$rootScope',
		function($scope,$http,$routeParams,Restangular,toaster,$location,$rootScope) {
			
			$scope.breadCrumb.hideBreadCrumb = true
	        console.log($scope.breadCrumb.hideBreadCrumb)
			
$scope.errorstatus=""
delete $http.defaults.headers.common['X-Auth-Token']

			$scope.resetpassword = function(data) {
	$scope.errorstatus=""
				if (data.newPassword == data.newPassword2) {
					data.token=$routeParams.token
					Restangular.all('register/forgotpassword/reset').post(data).then(function(data) {
					
						toaster.pop('success', "Reset Password", "Password reset succesfully");
						 $location.path("login")
					}, function() {
			
						toaster.pop('error', "Reset Password", "Error in Reseting password,Please try again");
					  })
				}else{
					
					$scope.errorstatus="Password must be same"
				}
			}

		} ])
