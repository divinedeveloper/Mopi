package com.themopi.account

import grails.test.mixin.*
import spock.lang.Specification

import com.themopi.utils.GenerateDataUtils

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(AuthenticationToken)
class AuthenticationTokenSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

	/**
	 * if username and authToken field are blank or null
	 * it should have validation errors
	 */
    void "test username and authtoken should not be blank or null"() {
		when: "fields are blank"
		AuthenticationToken authToken = new AuthenticationToken(
			username:"",
			authToken: ""
			)
		
		then: "validation should fail and have errors"
		!authToken.validate()
		authToken.hasErrors()
		authToken.errors.errorCount != 0
		
		when: "fields are null"
		authToken = new AuthenticationToken(
			username:null,
			authToken: null
			)
		
		then: "validation should fail and have errors"
		!authToken.validate()
		authToken.hasErrors()
		authToken.errors.errorCount != 0
		
		cleanup:
		authToken = null
    }
	
	/**
	 * if valid username and authToken are passed
	 * then authToken object is valid 
	 */
	void "test valid username and authtoken"(){
		when: "passing valid username and authToken"
		AuthenticationToken authToken = new AuthenticationToken(
			username: new GenerateDataUtils().randomString(),
			authToken: java.util.UUID.randomUUID()
			)
		
		then: "validation should fail and have errors"
		authToken.validate()
		!authToken.hasErrors()
		authToken.errors.errorCount == 0
		
		cleanup:
		authToken = null
		
	}
}
