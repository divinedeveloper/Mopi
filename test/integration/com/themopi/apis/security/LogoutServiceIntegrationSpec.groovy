package com.themopi.apis.security



import spock.lang.*

import com.themopi.account.AuthenticationToken
import com.themopi.utils.GenerateDataUtils

/**
 *
 */
class LogoutServiceIntegrationSpec extends Specification {

	def logoutService
	def grailsApplication
	
    def setup() {
    }

    def cleanup() {
    }

    /**
	 * If X-Auth-Token is blank or null
	 * throw security exception
	 */
	void "test if X-Auth-Token is blank or null"() {
		given: "Pass blank token value"
		String tokenValue = ""
	
		when: "removeToken method is invoked"
		logoutService.removeToken(tokenValue)
		
		then: "throw security exception"		
		thrown(com.themopi.exceptions.SecurityException)
				
		when: "tokenValue is null and removeToken method is invoked"
		tokenValue = null
		logoutService.removeToken(tokenValue)
		
		then: "throw security exception"
		thrown(com.themopi.exceptions.SecurityException)
				
	}
	
	/**
	 * If X-Auth-Token is valid and found in database
	 * delete its record
	 */
    void "test if token is valid delete it from database"() {
		given: "Generating token and saving Authentication Token"
		String tokenValue = java.util.UUID.randomUUID()
		new AuthenticationToken(
			username: new GenerateDataUtils().randomString(),
			authToken: tokenValue
			).save(flush:true)
		
		when: "removeToken method is invoked"
		logoutService.removeToken(tokenValue)
		
		then: "token object will be null"
		def authToken = AuthenticationToken.findByAuthToken(tokenValue)
		authToken == null
    }
}
