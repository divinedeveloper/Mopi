(function() {
	'use strict';
	angular
			.module('app.surveyform', [])
			.controller(
					'SurveyformCtrl',
					[
							'$scope',
							'$filter',
							'ngTableParams','$modal','$http',
							function($scope, $filter, ngTableParams,$modal,$http) {
						
								
								  $http.get('json/formwidget.json').success(function(data){
									  
									  $scope.formdata=data.response
									  
								  });
								
							} ]);

}).call(this);

//# sourceMappingURL=TableCtrl.js.map
