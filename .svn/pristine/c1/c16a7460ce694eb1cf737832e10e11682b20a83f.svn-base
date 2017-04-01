
package com.themopi.apis.security

import grails.test.mixin.*
import spock.lang.Specification

import com.themopi.account.AuthenticationToken

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(LogoutController)
@Mock([AuthenticationToken])
class LogoutControllerSpec extends Specification {

    def setup() {
    }

    def cleanup() {		
    }

	/**
	 * if X-Auth-Token is blank or authtoken object is null
	 * code should throw 404 auth-token not found exception
	 */
    void "test if X-Auth-Token is blank"() {
		given: "setting request headers"
		request.contentType = 'application/json'
		request.addHeader("X-Auth-Token", "")
		
		and: "mock logout service and throw security exception"		
		def errorCode =  grailsApplication.config.customExceptions.security.fourZeroFour.logout.errorCode
		def message =  grailsApplication.config.customExceptions.security.fourZeroFour.logout.message
		def extendedMessage =  grailsApplication.config.customExceptions.security.fourZeroFour.logout.extendedMessage
		def moreInfo =  grailsApplication.config.customExceptions.security.fourZeroFour.logout.moreInfo
		def mockLogoutService = mockFor(com.themopi.apis.security.LogoutService)
		mockLogoutService.demand.removeToken{tokenValue -> throw new com.themopi.exceptions.SecurityException(404,4004,message,extendedMessage,moreInfo)  }
		controller.logoutService = mockLogoutService.createMock()
				
		when: "logout action is invoked"
		controller.logout()
		
		then: "test error response from controller"
		def result = controller.response.json
		assert result.status == 404
		assert result.errorCode == 4004
		assert result.message == message
		assert result.extendedMessage == extendedMessage		
    }
	
	/**
	 * valid X-Auth-Token and authtoken object is found
	 * test success json 
	 */
	void "test if X-Auth-Token is valid"(){
		given: "setting request headers"
		request.contentType = 'application/json'
		request.addHeader("X-Auth-Token", java.util.UUID.randomUUID())
		
		and: "mock logout service and pass arguments to remove token method"
		def mockLogoutService = mockFor(com.themopi.apis.security.LogoutService)
		mockLogoutService.demand.removeToken{tokenValue -> }
		controller.logoutService = mockLogoutService.createMock() 
		
		when: "logout action is invoked"
		controller.logout()
		
		then: "test success response from controller"
		def result = controller.response.json
		assert result.status == 200
		assert result.success == true		
	}
}
