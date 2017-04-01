package com.themopi.apis.security

import grails.test.mixin.*
import spock.lang.Specification

import com.themopi.account.AuthenticationToken
import com.themopi.utils.GenerateDataUtils

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(LogoutService)
@Mock(com.themopi.account.AuthenticationToken)
class LogoutServiceSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

	/**
	 * If X-Auth-Token is blank or null
	 * throw security exception
	 */
    void "test X-Auth-Token is blank or null"() {
		given: "Pass blank token value"
		String tokenValue = ""
		
		when: "removeToken method is invoked"
		service.removeToken(tokenValue)
		
		then: "throw security exception"
		thrown(com.themopi.exceptions.SecurityException)
				
		when: "tokenValue is null and removeToken method is invoked"
		tokenValue = null
		service.removeToken(tokenValue)
		
		then: "throw security exception"
		thrown(com.themopi.exceptions.SecurityException)
				
    }
	
	/**
	 * If X-Auth-Token is valid but not present in database
	 * throw security exception
	 */
	void "test if X-Auth-Token is valid but not found in database "(){
		given: "Passing randomly generated token value which is not saved in database"
		String tokenValue = java.util.UUID.randomUUID()
		
		when: "removeToken method is invoked"
		service.removeToken(tokenValue)
		
		then: "throw security exception"
		thrown(com.themopi.exceptions.SecurityException)
	}
	
	/**
	 * If X-Auth-Token is valid and found in database
	 * token object will be null
	 */
	void "test if X-Auth-Token is valid and found in database "(){
		given: "Creating Authentication Token and saving in database"
		String tokenValue = java.util.UUID.randomUUID()
		new AuthenticationToken(
			username: new GenerateDataUtils().randomString(),
			authToken: tokenValue
			).save(failOnError: true)
		
		when: "removeToken method is invoked"
		service.removeToken(tokenValue)
		
		then: "authToken object must be null"
		def authToken = AuthenticationToken.findByAuthToken(tokenValue)
		authToken == null
	}
}
