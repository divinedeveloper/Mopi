


app.config(['$routeProvider','RestangularProvider','$environmentProvider',
							function($routeProvider,RestangularProvider,$environmentProvider) {
			//RestangularProvider.setBaseUrl('//192.168.1.3/mopi/api')
			

			
								var routes, setRoutes				
								routes=[{"url":'login',"page":'static/partials/login',"auth":false,"role":["ROLE_SUPERADMIN","ROLE_SPONSEREDADMIN","ROLE_ADMIN"]},
								        {"url":'forgetpassword',"page":'static/partials/forgetpassword',"auth":false,"role":["ROLE_SUPERADMIN","ROLE_SPONSEREDADMIN","ROLE_ADMIN"]},
								        {"url":'resetpassword/:token',"page":'static/partials/resetpassword',"auth":false,"role":["ROLE_SUPERADMIN","ROLE_SPONSEREDADMIN","ROLE_ADMIN"]},
								        {"url":'dashboard',"page":'static/partials/dashboard',"auth":true,"role":["ROLE_SUPERADMIN"]},
								        {"url":'user',"page":'static/partials/user',"auth":true,"role":["ROLE_SUPERADMIN"]},
								        {"url":'userprofile/:userid',"page":'static/partials/userprofile',"auth":true,"role":["ROLE_SUPERADMIN","ROLE_SPONSEREDADMIN","ROLE_ADMIN"]},
								        {"url":'ui/typography',"page":'static/partials/ui/typography',"auth":true,"role":["ROLE_SUPERADMIN"]},
								        {"url":'ui/buttons',"page":'static/partials/ui/buttons',"auth":true,"role":["ROLE_SUPERADMIN"]},
								        {"url":'ui/widgets',"page":'static/partials/ui/widgets',"auth":true,"role":["ROLE_SUPERADMIN"]},
								        {"url":'ui/grids',"page":'static/partials/ui/grids',"auth":true,"role":["ROLE_SUPERADMIN"]},
								        {"url":'ui/boxes',"page":'static/partials/ui/boxes',"auth":true,"role":["ROLE_SUPERADMIN"]},
								        {"url":'ui/components',"page":'static/partials/ui/components',"auth":true,"role":["ROLE_SUPERADMIN"]},
								        {"url":'ui/boxes',"page":'static/partials/ui/boxes',"auth":true,"role":["ROLE_SUPERADMIN"]},
								        {"url":'ui/timeline',"page":'static/partials/ui/timeline',"auth":true,"role":["ROLE_SUPERADMIN"]},
								        {"url":'ui/nested-lists',"page":'static/partials/ui/nested-lists',"auth":true,"role":["ROLE_SUPERADMIN"]},
								        {"url":'ui/maps',"page":'static/partials/ui/maps',"auth":true,"role":["ROLE_SUPERADMIN"]},
								        {"url":'ui/pricing-tables',"page":'static/partials/ui/pricing-tables',"auth":true,"role":["ROLE_SUPERADMIN"]},
								        {"url":'tables/static',"page":'static/partials/tables/static',"auth":true,"role":["ROLE_SUPERADMIN"]},
								        {"url":'tables/responsive',"page":'static/partials/tables/responsive',"auth":true,"role":["ROLE_SUPERADMIN"]},
								        {"url":'tables/dynamic',"page":'static/partials/tables/dynamic',"auth":true,"role":["ROLE_SUPERADMIN"]},
								        {"url":'forms/elements',"page":'static/partials/forms/elements',"auth":true,"role":["ROLE_SUPERADMIN"]},
								        {"url":'forms/layouts',"page":'static/partials/forms/layouts',"auth":true,"role":["ROLE_SUPERADMIN"]},
								        {"url":'forms/validation',"page":'static/partials/forms/validation',"auth":true,"role":["ROLE_SUPERADMIN"]},
								        {"url":'forms/wizard',"page":'static/partials/forms/wizard',"auth":true,"role":["ROLE_SUPERADMIN"]},
								        {"url":'charts/charts',"page":'static/partials/charts/charts',"auth":true,"role":["ROLE_SUPERADMIN"]},
								        {"url":'charts/flot',"page":'static/partials/charts/flot',"auth":true,"role":["ROLE_SUPERADMIN"]},
								        {"url":'pages/404',"page":'static/partials/pages/404',"auth":true,"role":["ROLE_SUPERADMIN"]},
								        {"url":'pages/500',"page":'static/partials/pages/500',"auth":true,"role":["ROLE_SUPERADMIN"]},
								        {"url":'pages/blank',"page":'static/partials/pages/blank',"auth":true,"role":["ROLE_SUPERADMIN"]},
								        {"url":'pages/forgot-password',"page":'static/partials/pages/forgot-password',"auth":true,"role":["ROLE_SUPERADMIN"]},
								        {"url":'pages/invoice',"page":'static/partials/pages/invoice',"auth":true,"role":["ROLE_SUPERADMIN"]},
								        {"url":'pages/lock-screen',"page":'static/partials/pages/lock-screen',"auth":true,"role":["ROLE_SUPERADMIN"]},
								        {"url":'pages/profile',"page":'static/partials/pages/profile',"auth":true,"role":["ROLE_SUPERADMIN"]},
								        {"url":'pages/signin',"page":'static/partials/pages/signin',"auth":true,"role":["ROLE_SUPERADMIN"]},
								        {"url":'pages/signup',"page":'static/partials/pages/signup',"auth":true,"role":["ROLE_SUPERADMIN"]},
								        {"url":'mail/compose',"page":'static/partials/mail/compose',"auth":true,"role":["ROLE_SUPERADMIN"]},
								        {"url":'mail/inbox',"page":'static/partials/mail/inbox',"auth":true,"role":["ROLE_SUPERADMIN"]},
								        {"url":'mail/single',"page":'static/partials/mail/single',"auth":true,"role":["ROLE_SUPERADMIN"]},
								        {"url":'tasks/tasks',"page":'static/partials/tasks/tasks',"auth":true,"role":["ROLE_SUPERADMIN"]},
								        {"url":'survey',"page":'static/partials/survey',"auth":true,"role":["ROLE_SUPERADMIN","ROLE_SPONSEREDADMIN","ROLE_ADMIN"]},
								        {"url":'surveyform',"page":'static/partials/surveyform',"auth":true,"role":["ROLE_SUPERADMIN","ROLE_SPONSEREDADMIN","ROLE_ADMIN"]},
								        {"url":'surveydetails/:surveyid',"page":'static/partials/surveydetails',"auth":true,"role":["ROLE_SUPERADMIN","ROLE_SPONSEREDADMIN","ROLE_ADMIN"]},
								        {"url":'question/:questionid/:surveyid',"page":'static/partials/questionpage',"auth":true,"role":["ROLE_SUPERADMIN","ROLE_SPONSEREDADMIN","ROLE_ADMIN"]},
								        {"url":'surveychartdetails/:surveyid',"page":'static/partials/surveychartdetails',"auth":true,"role":["ROLE_SUPERADMIN","ROLE_SPONSEREDADMIN","ROLE_ADMIN"]},
								        {"url":'comments/:surveyid',"page":'static/partials/comments',"auth":true,"role":["ROLE_SUPERADMIN","ROLE_SPONSEREDADMIN","ROLE_ADMIN"]},
								        {"url":'gift/:surveyid/:giftid',"page":'static/partials/gift',"auth":true,"role":["ROLE_SUPERADMIN","ROLE_SPONSEREDADMIN","ROLE_ADMIN"]}
								       ]
								setRoutes = function(route) {
									var config, url;
									url = '/' + route.url;
									config = {
										templateUrl : route.page
												+ '.html',
												 resolve: {
											            load: function ($route, dataService,$rootScope) {
											         if(!angular.isDefined($rootScope.userdata)){
											        	
											                return dataService.load(route.auth,route.role);
											         }
											            }
											        }
									};
									$routeProvider.when(url, config);
									return $routeProvider;
								};
								routes.forEach(function(route) {
									return setRoutes(route);
								});
								return $routeProvider.when('/', {
									redirectTo : '/login'
								}).when('/404', {
									templateUrl : 'views/pages/404.html'
								}).otherwise({
									redirectTo : '/404'
								});
							} ]);
	