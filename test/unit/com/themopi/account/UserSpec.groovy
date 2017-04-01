package com.themopi.account

import grails.plugin.springsecurity.SpringSecurityService
import grails.test.mixin.*
import spock.lang.Specification
import spock.lang.Unroll

import com.themopi.utils.GenerateDataUtils

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestFor(User)
class UserSpec extends Specification {

    def setup() {
//		String email = new GenerateDataUtils().generateRandomEmail()
//		String username = new GenerateDataUtils().randomString()
		//mock a user with some data (put unique violations in here so they can be tested, the others aren't needed)
		mockForConstraintsTests(User, [new User(email:"abc@rkgtechllc.com",username:"rkgtechllc")])
    }

    def cleanup() {
    }

	@Unroll("test user constraint for #field is #error")
	def "test all constraints for user"() {
		given:
		def springSecurityService = mockFor(SpringSecurityService,true)
		springSecurityService.demand.encodePassword(){String pwd -> return null}
		
		when: "pass dynamic fields and values to user object"
		def obj = new User("$field": val)
		obj.springSecurityService= springSecurityService.createMock()
		
		then: "call validate constraints method"
		validateConstraints(obj, field, error)

		where: "pass field names and values"
		error		|	field				|	val
		'email'		|	'email'				|	new GenerateDataUtils().randomString()
		'unique'	|	'email'				|	'abc@rkgtechllc.com'
		'nullable'	|	'email'				|	null
		'unique'	|	'username'			|	'rkgtechllc'
		'nullable'	|	'username'			|	null
		'nullable'	|	'password'			|	null
		'nullable'	|	'name'				|	null
		'valid'		|	'gPlusAccessToken'	|	null
		'valid'		|	'fbAccessToken'		|	null
		'valid'		|	'fbUID'				|	null
		'valid'		|	'gPlusUID'			|	null
		'valid'		|	'deletedAt'			|	null
		'valid'		|	'fbLogin'			|	null
		'valid'		|	'gPlusLogin'		|	null
		'valid'		|	'tokenValue'		|	null
		'valid'		|	'address'			|	null
		'valid'		|	'profile'			|	null
		'valid'		|	'roleAuthority'		|	null
		'valid'		|	'roleId'			|	null
		
	}
	
	@Unroll("user #field is #error using #val")
	def "test email constraints for user"() {
		given:
		def springSecurityService = mockFor(SpringSecurityService,true)
		springSecurityService.demand.encodePassword(){String pwd -> return null}
		
		when: "pass dynamic fields and values to user object"
		def obj = new User("$field": val)
		obj.springSecurityService= springSecurityService.createMock()
		
		then: "call validate constraints method"
		validateConstraints(obj, field, error)

		where: "pass field names and values"
		error		|	field				|	val
		'nullable'	|	'email'				|	null
		'email'		|	'email'				|	new GenerateDataUtils().randomString()
		'valid'		|	'email'				|	new GenerateDataUtils().generateRandomEmail()
		'unique'	|	'email'				|	'abc@rkgtechllc.com'
	}
	
	@Unroll("user #field is #error using #val")
	def "test username constraints for user"() {
		given:
		def springSecurityService = mockFor(SpringSecurityService,true)
		springSecurityService.demand.encodePassword(){String pwd -> return null}
		
		when: "pass dynamic fields and values to user object"
		def obj = new User("$field": val)
		obj.springSecurityService= springSecurityService.createMock()
		
		then: "call validate constraints method"
		validateConstraints(obj, field, error)

		where: "pass field names and values"
		error		|	field				|	val
		'nullable'	|	'username'			|	null
		'valid'		|	'username'			|	new GenerateDataUtils().randomString()
		'unique'	|	'username'			|	'rkgtechllc'
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
