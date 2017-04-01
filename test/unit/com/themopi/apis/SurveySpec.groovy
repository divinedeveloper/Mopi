package com.themopi.apis

import com.themopi.account.User
import com.themopi.survey.Gift
import com.themopi.survey.Survey;

import grails.test.mixin.TestFor
import spock.lang.Specification
import spock.lang.Unroll
import com.themopi.utils.GenerateDataUtils

import grails.plugin.springsecurity.SpringSecurityService

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Survey)
class SurveySpec extends Specification {

    def setup() {
		mockForConstraintsTests(Survey, [new Survey(name:new GenerateDataUtils().randomString(),surveyCode:new GenerateDataUtils().randomString())])
    }

    def cleanup() {
    }

   @Unroll("test survey constraint for #field is #error")
	def "test all constraints for survey"() {
		given:
		/*def springSecurityService = mockFor(SpringSecurityService,true)
		springSecurityService.demand.encodePassword(){String pwd -> return null}
		*/
		when: "pass dynamic fields and values to user object"
		def obj = new Survey("$field": val)
		//obj.springSecurityService= springSecurityService.createMock()
		
		then: "call validate constraints method"
		validateConstraints(obj, field, error)

		where: "pass field names and values"
		error		|	field				|	val
		'valid'		|	'name'				|	new GenerateDataUtils().randomString()
		'valid'     |	'surveyCode'		|	new GenerateDataUtils().randomString()
		/*'valid'		|	'lastUpdatedBy'		|	new User()*/
		'nullable'	|	'lastUpdatedBy'		|	null
		/*'valid'		|	'gift'				|	new Gift()*/
		/*'nullable'	|	'gift'				|	null*/
		/*'valid'		|	'user'				|	new User()*/
		/*'nullable'	|	'surveyType'		|	{sponsered,nonsponsered}*/
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
