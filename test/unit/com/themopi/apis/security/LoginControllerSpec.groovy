package com.themopi.apis.security

import grails.test.mixin.*
import spock.lang.Specification

import com.restfb.DefaultFacebookClient
import com.restfb.DefaultJsonMapper
import com.restfb.DefaultWebRequestor
import com.restfb.FacebookClient
import com.restfb.FacebookClient.AccessToken
import com.restfb.WebRequestor.Response
import com.themopi.account.AuthenticationToken;
import com.themopi.account.User

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(LoginController)
@Mock(User)
class LoginControllerSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

   /* void "test valid facebook signup"() {
		given: "setting request headers and json payload"
		request.contentType = 'application/json'
		AccessToken accessTokenObj = new DefaultFacebookClient().obtainAppAccessToken(grailsApplication.config.myapp.facebook.appID, grailsApplication.config.myapp.facebook.secret);
		println "My application access token: "+accessTokenObj
		
		String accessToken = new String(accessTokenObj.accessToken);
		println "accessToken : "+accessToken
		request.content = '''{
							"email": "sohambannerjee8@gmail.com",
							"token": '''+accessToken+''',
							"uid": "5454545455454"
						}'''
		params.providerString = "facebook"
		
		and: "mocking "
//		FacebookClient facebookClient = new DefaultFacebookClient(finalAccessToken);
		FacebookClient facebookClient = new DefaultFacebookClient(accessToken);
		  
				
		when: "login action is invoked"
		controller.login()
		
		
		then: "test response"
		println "response is "+controller.response.json

    }
	
	void "test another valid facebook signup"() {
		given: "setting request headers and json payload"
		request.contentType = 'application/json'
		AccessToken accessTokenObj = new DefaultFacebookClient().obtainAppAccessToken(grailsApplication.config.myapp.facebook.appID, grailsApplication.config.myapp.facebook.secret);
		println "My application access token: "+accessTokenObj
		
		String accessToken = new String(accessTokenObj.accessToken);
		println "accessToken : "+accessToken

		request.content = '''{
							"email": "sohambannerjee8@gmail.com",
							"token": '''+accessToken+''',
							"uid": "5454545455454"
						}'''
		params.providerString = "facebook"
		
		and: "mocking "
//		FacebookClient facebookClient = new DefaultFacebookClient(finalAccessToken);
		FacebookClient facebookClient = new DefaultFacebookClient(accessToken,
			
			  // A one-off DefaultWebRequestor for testing that returns a hardcoded JSON
			  // object instead of hitting the Facebook API endpoint URL
			
			  new DefaultWebRequestor() {
				@Override
				public Response executeGet(String url) throws IOException {
				  return new Response(HttpURLConnection.HTTP_OK,
					"{'name':'myname', 'fbAccessToken':'737598589646877|WuQY7f_4l6C5-bQXCqrAgGLVIbU'}");
				}
			  }, new DefaultJsonMapper());
		  
				
		when: "login action is invoked"
//		controller.login()
		// Make an API request using the mocked WebRequestor
		
		User user = facebookClient.fetchObject("ignored", User.class);
		println "user  is "+user 
		
		then: "test response"
		assert "myname".equals(user.name);
		assert "737598589646877|WuQY7f_4l6C5-bQXCqrAgGLVIbU".equals(user.fbAccessToken);
    }*/
}
