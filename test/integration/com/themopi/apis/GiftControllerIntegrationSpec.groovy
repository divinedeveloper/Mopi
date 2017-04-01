package com.themopi.apis



import com.themopi.account.AuthenticationToken
import com.themopi.account.User
import com.themopi.apis.security.GetId;
import com.themopi.survey.Survey
import com.themopi.utils.GenerateDataUtils
import com.themopi.utils.TestingSetup

import grails.test.spock.IntegrationSpec
import spock.lang.*

import grails.converters.*
import grails.plugin.springsecurity.authentication.dao.NullSaltSource

/**
 *
 */
class GiftControllerIntegrationSpec extends IntegrationSpec {

  //get auth token to send in request header
	/**
	 * here authToken hard coded
	 * further we put auth token from calling auth token
	 */
	def springSecurityService
	def saltSource
	def grailsApplication
	GetId currentIDsObj = new GetId();
	def surveyIdD
    def setup() {
		String accessToken = "d34l1gfteuenjmapg2m9d5qu4kvn3hq7"
		
		
    }

    def cleanup() {
		currentIDsObj=null;
    }

	void "test valid gift"()
	{
		def grailsApplication
		
		given: "login controller"
			
			println "----gift controller---"
			GenerateDataUtils gDatautils = new GenerateDataUtils();
			TestingSetup testingSetup = new TestingSetup()
			String stringValue = gDatautils.randomString();
			
			String salt = saltSource instanceof NullSaltSource ? null : stringValue
			String encodedPassword = springSecurityService.encodePassword("password",salt)
			
			User user = testingSetup.createUser(encodedPassword)
			
			long userIdT = user.id
			println "userIdT---"+userIdT
			/*def jsonBuilderUser = new groovy.json.JsonBuilder()
				jsonBuilderUser.user(
					userId: userIdT
				)
				println("Using just named arguments")
				println(jsonBuilderUser.toPrettyString())
			def converterUser = jsonBuilderUser as JSON
			String pathUser = grailsApplication.config.myapp.tempIdFileFolderPath+"user.json"
			converterUser.render(new java.io.FileWriter(pathUser));*/
			
			testingSetup.assignRoleUser(user.id,"ROLE_USER")
			user = testingSetup.updateProfile(user.id)
			user = testingSetup.updateAddress(user.id)
			Survey survey = testingSetup.createSurvey(user.id)
			
			String tokenValue = java.util.UUID.randomUUID()
			AuthenticationToken authenticationToken = new AuthenticationToken(
				username: user.username,
				authToken: tokenValue
				).save(flush:true)
			
			String giftCode = gDatautils.generateGiftCode()
			String giftText = gDatautils.generateGiftName()
			int maxNo = gDatautils.generateGiftMaxNo()
			GiftController giftController = new GiftController()
			giftController.request.addHeader("contentType","application/json")
			giftController.request.addHeader("X-Auth-Token",tokenValue)
			giftController.request.setMethod("POST");
			giftController.request.content = '''{"code":'''+giftCode+''',
												 "text":'''+giftText+''',
												 "maxNo":'''+maxNo+'''
												}'''
			giftController.params.surveyId=survey.id
			giftController.params.giftId=0
			
		when: "addGift action is invoked"
			/*def json = JSON.parse(new FileInputStream("C://Users//PREMPRAKASH//Desktop//tempTextFile//survey.json"), "UTF-8")
			println "json.content.surveyId----"+json.content.surveyId
			String surveyIdString=json.content.surveyId;
			int surveyId=Integer.parseInt(surveyIdString);*/
			
			giftController.addGift()
			println "gift created-------------"
		then: "testing success response from gift controller"
			def result = giftController.response.json
			println "gift checked create or not --"+result
			assert giftController.response.json.error == false
			assert giftController.response.json.resp.code == giftCode
			assert giftController.response.json.resp.text == giftText
			assert giftController.response.json.resp.maxNo == maxNo
	}
}
