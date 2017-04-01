/*enviornment: Development/Mock*/
app.value("configsetting",  {
		environment: 'Development', 
  jsonurl: "../../static/json/static"
});
app.config(['$routeProvider','RestangularProvider','$environmentProvider',
							function($routeProvider,RestangularProvider,$environmentProvider) {
		   $environmentProvider.environments = [
		                                        {
		                                            name: 'local',
		                                            pattern: /localhost/,
		                                            url: 'http://localhost:8080/mopi/api/v1/',
		                                            loginurl:'http://localhost:8080/mopi/api/',
		                                            analyticsAppId: 'UA-XXXXXXXX-1',
		                                            facebookAppId: '12345678901234',
		                                            googleid:'AIzaSyBScO9luOtjbx6vyU4qwVo6QQ-YvEvugMM'
		                                        },
		                                        {
		                                            name: 'stage',
		                                            pattern: /192.168.1.5:5850/,
		                                            url: 'https://192.168.1.5:5850/api/v1/',
		                                            analyticsAppId: 'UA-XXXXXXXX-1',
		                                            facebookAppId: '12345678901234',
		                                            loginurl:'https://192.168.1.5:5850/api/',
		                                            googleid:'AIzaSyBScO9luOtjbx6vyU4qwVo6QQ-YvEvugMM'
		                                        },
		                                        {
		                                            name: 'mac',
		                                            pattern: /1.186.217.29/,
		                                            url: 'http://1.186.217.29:5850/api/v1/',
		                                            analyticsAppId: 'UA-XXXXXXXX-1',
		                                            facebookAppId: '12345678901234',
		                                            loginurl:'http://1.186.217.29:5850/api/',
		                                            googleid:'AIzaSyBScO9luOtjbx6vyU4qwVo6QQ-YvEvugMM'
		                                        },
		                                        {
		                                            name: 'prod',
		                                            pattern: /mopi.rkgtechllc.com/,
		                                            url: 'https://mopi.rkgtechllc.com/api/v1/',
		                                            analyticsAppId: 'UA-XXXXXXXX-1',
		                                            facebookAppId: '12345678901234',
		                                            loginurl:'https://mopi.rkgtechllc.com/api/',
		                                            googleid:'AIzaSyBScO9luOtjbx6vyU4qwVo6QQ-YvEvugMM'
		                                        },
		                                        {
		                                            name: 'stagingaws',
		                                            pattern: /54.169.76.126/,
		                                            url: 'http://54.169.76.126/api/v1/',
		                                            analyticsAppId: 'UA-XXXXXXXX-1',
		                                            facebookAppId: '12345678901234',
		                                            loginurl:'http://54.169.76.126/api/',
		                                            googleid:'AIzaSyBScO9luOtjbx6vyU4qwVo6QQ-YvEvugMM'
		                                        }

		                                    ];
		
	}])
	
	
