package com.themopi.apis.security



import grails.test.spock.IntegrationSpec
import spock.lang.*

import com.themopi.account.AuthenticationToken
import com.themopi.account.User
import com.themopi.utils.GenerateDataUtils

/**
 *
 */
class LogoutControllerIntegrationSpec extends IntegrationSpec {

	def logoutService
	def grailsApplication
	
    def setup() {
	}

    def cleanup() {				
    }

	/**
	 * if X-Auth-Token is blank or authtoken object is null
	 * code should throw 404 auth-token not found exception 
	 */
	void "test if X-Auth-Token is blank"(){
		given: "Initializing logout controller and setting request headers"
		def logoutController = new LogoutController()
		logoutController.request.contentType  = "application/json"
		
		and: "setting blank X-Auth-Token"
		logoutController.request.addHeader("X-Auth-Token", "")
		
		and: "defining logoutService"
		logoutController.logoutService = logoutService
				
		when: "logout action is invoked"
		logoutController.logout()
		
		then: "An error response is thrown"
		def result = logoutController.response.json
		logoutController.response.json.status == 404
		logoutController.response.json.error == true
		logoutController.response.json.errorCode == 4004
		logoutController.response.json.message == grailsApplication.config.customExceptions.security.fourZeroFour.logout.message
		logoutController.response.json.extendedMessage == grailsApplication.config.customExceptions.security.fourZeroFour.logout.extendedMessage
		logoutController.response.status == 404				
	}
	
	/**
	 * If X-Auth-Token is valid but not present in database
	 * code should throw 404 auth-token not found exception
	 */
	void "test invalid logout"(){
		given: "Initializing logout controller and setting request headers"
		def logoutController = new LogoutController()
		logoutController.request.contentType  = "application/json"
		
		and: "setting random X-Auth-Token which is not in database"
		logoutController.request.addHeader("X-Auth-Token", java.util.UUID.randomUUID())
		
		and: "defining logoutService"
		logoutController.logoutService = logoutService
				
		when: "logout action is invoked"
		logoutController.logout()
		
		then: "An error response is thrown"
		def result = logoutController.response.json
		logoutController.response.json.status == 404
		logoutController.response.json.error == true
		logoutController.response.json.errorCode == 4004
		logoutController.response.json.message == grailsApplication.config.customExceptions.security.fourZeroFour.logout.message
		logoutController.response.json.extendedMessage == grailsApplication.config.customExceptions.security.fourZeroFour.logout.extendedMessage
		logoutController.response.status == 404		
	}
	
	/**
	 * valid X-Auth-Token and authtoken object is found
	 * test success json 
	 */
     void "test valid logout"(){		 
		given: "saving auth token"
		String tokenValue = java.util.UUID.randomUUID()
		new AuthenticationToken(
			username: new GenerateDataUtils().randomString(),
			authToken: tokenValue
			).save(flush:true)
							
		and: "Initializing logout controller and setting request headers"
		def logoutController = new LogoutController()
		logoutController.request.contentType  = "application/json"
		
		and: "setting valid X-Auth-Token"
		logoutController.request.addHeader("X-Auth-Token", tokenValue)
		
		and: "defining logoutService"
		logoutController.logoutService = logoutService
		
		when: "logout action is invoked"
		logoutController.logout()
		
		then: "testing success response from controller"
		def result = logoutController.response.json
		logoutController.response.status == 200
		result.status == 200
		result.success == true	
		def authToken = AuthenticationToken.findByAuthToken(tokenValue)
		authToken == null
	}
	
	
}
