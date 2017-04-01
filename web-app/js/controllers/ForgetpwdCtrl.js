app.controller('ForgetpwdCtrl',
		[
				'$scope',
				'$http',
				'Restangular',
				'$location',
				'$injector',
				'toaster',
				'$location',
				'$rootScope',
				function($scope, $http, Restangular, $location, $injector,
						toaster,$location,$rootScope) {
					
					$scope.breadCrumb.hideBreadCrumb = true
			        console.log($scope.breadCrumb.hideBreadCrumb)

					/* Injector for validation starts */
					var $validationProvider = $injector.get('$validation');

					$scope.forgotvalidation = {
						requiredCallback : 'required',
						checkValid : $validationProvider.checkValid,
						submit : function(form) {
							$validationProvider.validate(form);
						},
						reset : function(form) {
							$validationProvider.reset(form);
						}
					};

					/* Injector for validation ends */

					$scope.backlogin = function() {
						$location.path('/login')
					}
					/* Send email to reset password starts */
					$scope.reset = function(data, validity) {
						console.log(data)
						if (validity == true) {
							Restangular.all(
									'register/forgotpassword?email='
											+ data.email).post().then(
									function(data) {
										$location.path('login')
										toaster.pop('success', "Please Check your email");
									},function(){
										toaster.pop('error', "Please enter valid email");
										
									});

						}

					}

					/*Send email to reset password ends*/
				} ]);