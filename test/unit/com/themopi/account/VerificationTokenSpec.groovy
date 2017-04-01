package com.themopi.account

import grails.test.mixin.*
import spock.lang.Specification
import spock.lang.Unroll

import com.themopi.account.VerificationToken
import com.themopi.utils.GenerateDataUtils

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(VerificationToken)
@Mock(com.themopi.account.User)
class VerificationTokenSpec extends Specification {

    def setup() {
		mockForConstraintsTests(VerificationToken)
    }

    def cleanup() {
		
    }

    @Unroll("test verificationToken domain constraint for #field is #error")
	def "test all constraints for verificationToken Domain"() {
		when: "pass dynamic fields and values to verificationToken object"
//		def obj = new VerificationToken("$field": val)
		def emailValue = new GenerateDataUtils().generateRandomEmail()
		def stringValue = new GenerateDataUtils().randomString()
		User user = new User(
						email:emailValue,
						password:stringValue,
						name:stringValue,
						accountLocked: false,
						enabled: true,
						accountExpired:false,
						passwordExpired:false,
						isActive:true,
						username:stringValue,
						tokenValue:null,
						roleAuthority:"ROLE_USER",
						roleId: new Long(4),
						salt:emailValue
						) 
		def obj = new VerificationToken("$field": val, user: user)
//		user.save(failOnError: true, flush:true)
		
			
		then: "call validate constraints method"
		validateConstraints(obj, field, error)
		
		where: "pass field names and values"
		error		|	field				|	val
		'email'		|	'email'				|	new GenerateDataUtils().randomString()
		'nullable'	|	'email'				|	null
		'nullable'	|	'verificationToken'	|	null
	}
	
	@Unroll("verificationToken #field is #error using #val")
	def "test email constraints for verificationToken"() {
		when: "pass dynamic fields and values to user object"
		def emailValue = new GenerateDataUtils().generateRandomEmail()
		def stringValue = new GenerateDataUtils().randomString()
		User user = new User(
						email:emailValue,
						password:stringValue,
						name:stringValue,
						accountLocked: false,
						enabled: true,
						accountExpired:false,
						passwordExpired:false,
						isActive:true,
						username:stringValue,
						tokenValue:null,
						roleAuthority:"ROLE_USER",
						roleId: new Long(4),
						salt:emailValue
						) 
		def obj = new VerificationToken("$field": val, user: user)
		
		then: "call validate constraints method"
		validateConstraints(obj, field, error)

		where: "pass field names and values"
		error		|	field				|	val
		'nullable'	|	'email'				|	null
		'email'		|	'email'				|	new GenerateDataUtils().randomString()
		'valid'		|	'email'				|	new GenerateDataUtils().generateRandomEmail()
	}
	
	private void validateConstraints(obj, field, error) {
		def validated = obj.validate()
		if (error && error != 'valid') {
			assert !validated
			assert obj.errors[field]
			assert error == obj.errors[field]
			
		} else {
			assert !obj.errors[field]
		}
	}
}
