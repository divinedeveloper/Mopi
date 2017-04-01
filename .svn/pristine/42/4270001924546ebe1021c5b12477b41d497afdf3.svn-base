package com.themopi.apis

import com.themopi.location.Country;

import grails.test.mixin.TestFor
import spock.lang.Specification
import grails.plugin.springsecurity.SpringSecurityService
import grails.test.mixin.*
import spock.lang.Unroll

import com.themopi.utils.GenerateDataUtils

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Country)
class CountrySpec extends Specification {

    def setup() {
		mockForConstraintsTests(Country, [new Country(name:"India",code:"In")])
		
    }

    def cleanup() {
    }

   @Unroll("country #field is #error using #val")
	def "test name constraints for Country"() {
		given:
		/*def springSecurityService = mockFor(SpringSecurityService,true)
		springSecurityService.demand.encodePassword(){String pwd -> return null}*/
		
		when: "pass dynamic fields and values to country object"
		def obj = new Country("$field": val)
		//obj.springSecurityService= springSecurityService.createMock()
		
		then: "call validate constraints method"
		validateConstraints(obj, field, error)

		where: "pass field names and values"
		error			|	field			|	val
		'valid'			|	'name'			|	new GenerateDataUtils().randomString()
		//'blank'    		| 	'name' 			| 	''
		'unique'		|	'name'			|	'India'
		'valid'			|	'code'			|	new GenerateDataUtils().randomString()
		//'blank'    		| 	'code' 			| 	''
		'unique'		|	'code'			|	'In'
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
