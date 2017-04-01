package com.themopi.apis

import com.themopi.survey.Gift;

import grails.test.mixin.TestFor
import spock.lang.Specification
import grails.plugin.springsecurity.SpringSecurityService
import grails.test.mixin.*
import spock.lang.Unroll

import com.themopi.utils.GenerateDataUtils

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Gift)
class GiftSpec extends Specification {

    def setup() {
		mockForConstraintsTests(Gift, [new Gift(text:"gift",code:"RA124")])
		
    }

    def cleanup() {
    }

   @Unroll("gift #field is #error using #val")
	def "test text constraints for gift"() {
		given:
		/*def springSecurityService = mockFor(SpringSecurityService,true)
		springSecurityService.demand.encodePassword(){String pwd -> return null}*/
		
		when: "pass dynamic fields and values to gift object"
		def obj = new Gift("$field": val)
		//obj.springSecurityService= springSecurityService.createMock()
		
		then: "call validate constraints method"
		validateConstraints(obj, field, error)

		where: "pass field names and values"
		error			|	field			|	val
		'valid'			|	'text'			|	new GenerateDataUtils().randomString()
		'valid'			|	'code'			|	new GenerateDataUtils().randomString()
		/*'blank'    		| 	'code' 			| 	''*/
		'unique'		|	'code'			|	'RA124'
		'valid'			|	'text'			|	null
		'valid'			|	'dateCreated'	| 	new Date()
		'valid'			|	'lastUpdated'	| 	new Date()
		'valid'			|	'maxNo'			|	99
		'valid'			|	'dateCreated'	| 	null
		'valid'			|	'lastUpdated'	| 	null
		'valid'			|	'maxNo'			|	null
		'valid'			|	'user'			|	null
		'valid'			|	'type'			|	null
		'valid'			|	'type'			|	new GenerateDataUtils().randomString()
		'valid'			|	'imageUrlG'		|	null
		'valid'			|	'imageUrlG'		|	new GenerateDataUtils().getImageUrlG()
		
		
	}
	
	/*@Unroll("gift #field is #error using #val")
	def "test code constraints for gift"() {
		given:
		def springSecurityService = mockFor(SpringSecurityService,true)
		springSecurityService.demand.encodePassword(){String pwd -> return null}
		
		when: "pass dynamic fields and values to gift object"
		def obj = new Gift("$field": val)
		//obj.springSecurityService= springSecurityService.createMock()
		
		then: "call validate constraints method"
		validateConstraints(obj, field, error)

		where: "pass field names and values"
		
		error		|	field			|	val
		'valid'		|	'code'			|	new GenerateDataUtils().randomString()
		'unique'	|	'code'			|	'RA124'
	}*/
	
	/*@Unroll("gift #field is #error using #val")
	def "test code constraints for unique gift"() {
		given:
		def springSecurityService = mockFor(SpringSecurityService,true)
		springSecurityService.demand.encodePassword(){String pwd -> return null}
		
		when: "pass dynamic fields and values to gift object"
		def obj = new Gift("$field": val)
		//obj.springSecurityService= springSecurityService.createMock()
		
		then: "call validate constraints method"
		validateConstraints(obj, field, error)

		where: "pass field names and values"
		
		error		|	field			|	val
		'unique'	|	'code'			|	'RA124'
	}*/
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
