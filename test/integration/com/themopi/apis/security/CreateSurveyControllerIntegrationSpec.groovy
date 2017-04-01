package com.themopi.apis.security


import java.util.Date;
import java.util.Map;

import org.json.JSONArray
import org.json.JSONObject

import com.themopi.account.AuthenticationToken
import com.themopi.account.Role
import com.themopi.account.User
import com.themopi.account.UserRole
import com.themopi.apis.SurveyController
import com.themopi.location.City
import com.themopi.location.Country
import com.themopi.location.State
import com.themopi.survey.Survey
import com.themopi.user.Address
import com.themopi.user.Profile
import com.themopi.user.Profile.Gender;
import com.themopi.utils.GenerateDataUtils
import com.themopi.utils.TestingSetup

import grails.converters.JSON
import spock.lang.*
import grails.converters.*
import grails.plugin.springsecurity.authentication.dao.NullSaltSource

/**
 *
 */

class CreateSurveyControllerIntegrationSpec extends Specification {

	def grailsApplication
	def springSecurityService
	def saltSource
	
	def setupSpec()
	{
		
	}
    def setup() {
		println "----c----"
    }

    def cleanup() {
		println "----c--vv--"
    }

	/*
	 * create new survey
	 */
    void "test create Survey test"() {
		
		given: "login controller"
		GenerateDataUtils gDatautils = new GenerateDataUtils();
		String stringValue = gDatautils.randomString();
		println "stringValue---"+stringValue
		String emailValue = gDatautils.generateRandomEmail();
		println "emailValue--"+emailValue
		TestingSetup testingSetup = new TestingSetup()
		
		String salt = saltSource instanceof NullSaltSource ? null : stringValue
		String encodedPassword = springSecurityService.encodePassword("password",salt)
		
		User user = testingSetup.createUser(encodedPassword)
		
		long userIdT = user.id
		int iflag = testingSetup.writeUserIdToFile(userIdT)
		/*println "userIdT---"+userIdT
		def jsonBuilderUser = new groovy.json.JsonBuilder()
			jsonBuilderUser.user(
				userId: userIdT
			)
			println("Using just named arguments")
			println(jsonBuilderUser.toPrettyString())
		//GetId object=new GetId(surveyId:surveyIdT)
		def converterUser = jsonBuilderUser as JSON
		String pathUser = grailsApplication.config.myapp.tempIdFileFolderPath+"user.json"
		converterUser.render(new java.io.FileWriter(pathUser));*/
		
		testingSetup.assignRoleUser(user.id,"ROLE_USER")
		user = testingSetup.updateProfile(user.id)
		user = testingSetup.updateAddress(user.id)
		
		/*
		User user = new User(
			email: stringValue+"@mopi.com",
			password:encodedPassword,
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
			salt:stringValue+"@mopi.com"
			).save(flush:true)
	
			println "user.id---"+user.id
			
			long userIdT = user.id
			println "userIdT---"+userIdT
			def jsonBuilderUser = new groovy.json.JsonBuilder()
				jsonBuilderUser.user(
					userId: userIdT
				)
				println("Using just named arguments")
				println(jsonBuilderUser.toPrettyString())
			//GetId object=new GetId(surveyId:surveyIdT)
			def converterUser = jsonBuilderUser as JSON
			String pathUser = grailsApplication.config.myapp.tempIdFileFolderPath+"user.json"
			converterUser.render(new java.io.FileWriter(pathUser));
			
			User uUser = User.findByName(stringValue);
			
			Profile profile = new Profile(
				description:"Hhhhhhh",
				mobile:9898099090, 
				phonenumber:9898099090,
				gender:"Male",
				occupation:"Student",
				religion:"Hindu",
				incomeLevel:"0-10000",
				age:"18-30",
				dob:new Date()
				).save()
			uUser.profile = profile
		
			Country country = Country.findByName("India")
			State state = State.findByName("Maharashtra")
			City city =City.findByName("Navi Mumbai")
			Address address = new Address(
				homeTown:"CBD Belapur",
				location:[lat:19.0300657,long:73.03511379999998],
				city:city,
				state:state,
				country:country,
				embedCountry:country?.name,
				embedState:state?.name,
				embedCity:city?.name
				).save(flush:true)
			uUser.address = address
			uUser.save()
			Role role = Role.findByAuthority("ROLE_USER")
			UserRole userRole = new UserRole(user:user,role:role).save(flush:true)*/
			/*user.roleAuthority = role.authority
			user.roleId = role.id
			user.save()*/
			
			String tokenValue = java.util.UUID.randomUUID()
			AuthenticationToken authenticationToken = new AuthenticationToken(
				username: user.username,
				authToken: tokenValue
				).save(flush:true)
		/**
		 * here authToken hard coded 
		 * further we put auth token from calling auth token
		 */
		String status = "Inactive"
		String visibility = "EveryOne"
		SurveyController surveyController = new SurveyController();
		String surveyName = new GenerateDataUtils().generateSurveyName();
		surveyController.request.addHeader("contentType","application/json")
		surveyController.request.addHeader("X-Auth-Token",tokenValue)
		surveyController.request.content = '''{"name":'''+surveyName+''',
												"visiblity":true,
											    "startTime":"2014-12-29",
											    "endTime":"2014-12-30",
											    "visibleTo":'''+visibility+''',
											    "status":'''+status+''',
											    "cityList":["India","Pakistan"],
											    "countryList":["Mumbai","Delhi"]
											    }'''
		when: "create survey action is invoked"
		surveyController.add()
		println "survey created-------------"
		then: "testing error response from create controller"
		def resultSurvey = surveyController.response.json
		println "result is "+resultSurvey
		String surveyIdStr = surveyController.response.json.resp.id
			println "surveyController.response.json.resp.id---"+surveyController.response.json.resp.id
			println "surveyId--"+surveyIdStr
			int surveyIdT = Integer.parseInt(surveyIdStr)
			println "surveyIdT---"+surveyIdT
			def jsonBuilder = new groovy.json.JsonBuilder()
				jsonBuilder.surveys(
				    surveyId: surveyIdT
				)
				println("Using just named arguments")
				println(jsonBuilder.toPrettyString())
			//GetId object=new GetId(surveyId:surveyIdT)
			def converter = jsonBuilder as JSON
			String path = grailsApplication.config.myapp.tempIdFileFolderPath+"survey.json"
			println "path is %%%%%%%%%%"+path
            converter.render(new java.io.FileWriter(path));
			println "-----------survey created-------------"
			assert surveyController.response.json.error == false
			/*assert surveyController.response.json.resp.name == surveyName
			assert surveyController.response.json.resp.visibleTo == visibility*/
			/*assert surveyController.response.json.resp.status == status*/
    }
	/*
	 * update survey after only create survey
	 */
	void "test update Survey test"() {
		
		given: "login controller"
		//get auth token to send in request header
		/**
		 * here authToken hard coded
		 * further we put auth token from calling auth token
		 */
		String pathUser = grailsApplication.config.myapp.tempIdFileFolderPath+"user.json"
		def jsonUser = JSON.parse(new FileInputStream(pathUser), "UTF-8")
		int userId = jsonUser.content.user.userId
		println "got userId---"+userId
		User userNameObj=User.findById(userId)
		String username = userNameObj.username
		GenerateDataUtils gDatautils = new GenerateDataUtils();
		String tokenValue = java.util.UUID.randomUUID()
		AuthenticationToken authenticationToken = new AuthenticationToken(
			username: username,
			authToken: tokenValue
			).save(flush:true)
		//String accessToken = "d34l1gfteuenjmapg2m9d5qu4kvn3hq7"
		String status = "Draft"
		String visibility = "EveryOne"
		String path = grailsApplication.config.myapp.tempIdFileFolderPath+"survey.json"
		def json = JSON.parse(new FileInputStream(path), "UTF-8")
		int surveyId = json.content.surveys.surveyId
		println "got surveyId---"+surveyId
		SurveyController surveyController = new SurveyController();
		String surveyName = new GenerateDataUtils().generateSurveyName();
		surveyController.request.addHeader("contentType","application/json")
		surveyController.request.addHeader("X-Auth-Token",tokenValue)
		surveyController.request.content = '''{"name":'''+surveyName+''',
												"visiblity":true,
											    "startTime":"2014-11-29",
											    "endTime":"2014-11-30",
											    "visibleTo":'''+visibility+''',
												"status":'''+status+''',
											    "cityList":["India","Pakistan"],
											    "countryList":["Mumbai","Delhi"]
											    }'''
		surveyController.params.surveyId=surveyId
		when: "create survey action is invoked"
		surveyController.update()
		println "survey created-------------"
		then: "testing error response from create controller"
		def resultSurvey = surveyController.response.json
		println "-----------survey updated-------------"+resultSurvey
		assert surveyController.response.json.error == false
		assert surveyController.response.json.resp.name == surveyName
		assert surveyController.response.json.resp.visibleTo == visibility
		assert surveyController.response.json.resp.status == status
	}
	
	// * Test add one binary question to survey
	 
	void "test add binary question to Survey test"() {
		
		given: "login controller"
		//get auth token to send in request header
		/**
		 * here authToken hard coded
		 * further we put auth token from calling auth token
		 */
		String pathUser = grailsApplication.config.myapp.tempIdFileFolderPath+"user.json"
		def jsonUser = JSON.parse(new FileInputStream(pathUser), "UTF-8")
		int userId = jsonUser.content.user.userId
		println "got userId---"+userId
		User userNameObj=User.findById(userId)
		String username = userNameObj.username
		GenerateDataUtils gDatautils = new GenerateDataUtils();
		String tokenValue = java.util.UUID.randomUUID()
		AuthenticationToken authenticationToken = new AuthenticationToken(
			username: username,
			authToken: tokenValue
			).save(flush:true)
		//String accessToken = "d34l1gfteuenjmapg2m9d5qu4kvn3hq7"
		String path = grailsApplication.config.myapp.tempIdFileFolderPath+"survey.json"
		def json = JSON.parse(new FileInputStream(path), "UTF-8")
		int surveyId = json.content.surveys.surveyId
		println "got surveyId---"+surveyId
		SurveyController surveyController = new SurveyController();
		String surveyName = new GenerateDataUtils().generateSurveyName();
		surveyController.request.addHeader("contentType","application/json")
		surveyController.request.addHeader("X-Auth-Token",tokenValue)
		surveyController.request.content = '''{"query":"updated 1st question ?",
											   "questionType":"binary",
											   "option":[yes,no]
											  }'''
		surveyController.params.surveyId=surveyId
		when: "create survey action is invoked"
		surveyController.addQuestions()
		println "survey created-------------"
		then: "testing error response from create controller"
		def result = surveyController.response.json
		println "-----------Binary Question created-------------"+result
		String questionIdStr = surveyController.response.json.resp.id
			println "surveyController.response.json.resp.id---"+surveyController.response.json.resp.id
			println "surveyId--"+questionIdStr
			int questionIdT = Integer.parseInt(questionIdStr)
			println "questionIdT---"+questionIdT
			def jsonBuilder = new groovy.json.JsonBuilder()
				jsonBuilder.question(
					questionId: questionIdT
				)
				println("Using just named arguments")
				println(jsonBuilder.toPrettyString())
			//GetId object=new GetId(questionId:questionIdT)
			def converter = jsonBuilder as JSON
			String pathQ = grailsApplication.config.myapp.tempIdFileFolderPath+"binaryQuestion.json"
			converter.render(new java.io.FileWriter(pathQ));
		
		assert surveyController.response.json.error == false
		
		/*assert surveyController.response.json.resp.status == status*/
	}
	
	 //* Test add all binary question to survey
	 
	void "test add all binary question to Survey test"() {
		
		given: "login controller"
		//get auth token to send in request header
		/**
		 * here authToken hard coded
		 * further we put auth token from calling auth token
		 */
		String pathUser = grailsApplication.config.myapp.tempIdFileFolderPath+"user.json"
		def jsonUser = JSON.parse(new FileInputStream(pathUser), "UTF-8")
		int userId = jsonUser.content.user.userId
		println "got userId---"+userId
		User userNameObj=User.findById(userId)
		String username = userNameObj.username
		GenerateDataUtils gDatautils = new GenerateDataUtils();
		String tokenValue = java.util.UUID.randomUUID()
		AuthenticationToken authenticationToken = new AuthenticationToken(
			username: username,
			authToken: tokenValue
			).save(flush:true)
		//String accessToken = "d34l1gfteuenjmapg2m9d5qu4kvn3hq7"
		String path = grailsApplication.config.myapp.tempIdFileFolderPath+"survey.json"
		println "path"
		def json = JSON.parse(new FileInputStream(path), "UTF-8")
		int surveyId = json.content.surveys.surveyId
		println "got surveyId---"+surveyId
		SurveyController surveyController = new SurveyController();
		String surveyName = new GenerateDataUtils().generateSurveyName();
		surveyController.request.addHeader("contentType","application/json")
		surveyController.request.addHeader("X-Auth-Token",tokenValue)
		surveyController.request.content = '''{questions:[ {"query":"1st question ?",
												"questionType":"binary",     
												"option":[yes,no]
											  },
											  {
											    "query":"2nd question ?",
											    "questionType":"binary",     
											    "option":[yes,no]
											  }]
											}'''
		surveyController.params.surveyId=surveyId
		when: "create survey action is invoked"
		surveyController.addAllQuestions()
		println "addAllQuestions created-------------"
		then: "testing error response from create controller"
		def resultSurvey = surveyController.response.json
		println "-----------survey updated-------------"+resultSurvey
		assert surveyController.response.json.error == false
		/*assert surveyController.response.json.resp.status == status*/
	}
	
	// * Test add one  Multiple questions to survey
	 
	void "test add  Multiple questions to Survey test"() {
		
		given: "login controller"
		//get auth token to send in request header
		/**
		 * here authToken hard coded
		 * further we put auth token from calling auth token
		 */
		String pathUser = grailsApplication.config.myapp.tempIdFileFolderPath+"user.json"
		def jsonUser = JSON.parse(new FileInputStream(pathUser), "UTF-8")
		int userId = jsonUser.content.user.userId
		println "got userId---"+userId
		User userNameObj=User.findById(userId)
		String username = userNameObj.username
		GenerateDataUtils gDatautils = new GenerateDataUtils();
		String tokenValue = java.util.UUID.randomUUID()
		AuthenticationToken authenticationToken = new AuthenticationToken(
			username: username,
			authToken: tokenValue
			).save(flush:true)
		String accessToken = "d34l1gfteuenjmapg2m9d5qu4kvn3hq7"
		String path = grailsApplication.config.myapp.tempIdFileFolderPath+"survey.json"
		def json = JSON.parse(new FileInputStream(path), "UTF-8")
		int surveyId = json.content.surveys.surveyId
		println "got surveyId---"+surveyId
		SurveyController surveyController = new SurveyController();
		String surveyName = new GenerateDataUtils().generateSurveyName();
		surveyController.request.addHeader("contentType","application/json")
		surveyController.request.addHeader("X-Auth-Token",tokenValue)
		surveyController.request.content = '''{"query":"is this test question 6666 ?",
											   "questionType":"multiplechoice",
											   "option":[{id:1,value:"No",image:""},{id:2,value:"yes",image:""}]
											 }'''
		surveyController.params.surveyId=surveyId
		when: "create survey action is invoked"
		surveyController.addQuestions()
		println "add  Multiple questions-------------"
		then: "testing error response from create controller"
		def result = surveyController.response.json
		println "-----------Binary Question created-------------"+result
		String questionIdStr = surveyController.response.json.resp.id
			println "surveyController.response.json.resp.id---"+surveyController.response.json.resp.id
			println "surveyId--"+questionIdStr
			int questionIdT = Integer.parseInt(questionIdStr)
			println "questionIdT---"+questionIdT
			def jsonBuilder = new groovy.json.JsonBuilder()
				jsonBuilder.question(
					questionId: questionIdT
				)
				println("Using just named arguments")
				println(jsonBuilder.toPrettyString())
			//GetId object=new GetId(questionId:questionIdT)
			def converter = jsonBuilder as JSON
			String pathQ = grailsApplication.config.myapp.tempIdFileFolderPath+"multipleQuestion.json"
			converter.render(new java.io.FileWriter(pathQ));
		assert surveyController.response.json.error == false
		
		/*assert surveyController.response.json.resp.status == status*/
	}
	
	 //* Test add all  Multiple questions to survey
	 
	void "test add all  Multiple questions to Survey test"() {
		
		given: "login controller"
		//get auth token to send in request header
		/**
		 * here authToken hard coded
		 * further we put auth token from calling auth token
		 */
		String pathUser = grailsApplication.config.myapp.tempIdFileFolderPath+"user.json"
		def jsonUser = JSON.parse(new FileInputStream(pathUser), "UTF-8")
		int userId = jsonUser.content.user.userId
		println "got userId---"+userId
		User userNameObj=User.findById(userId)
		String username = userNameObj.username
		GenerateDataUtils gDatautils = new GenerateDataUtils();
		String tokenValue = java.util.UUID.randomUUID()
		AuthenticationToken authenticationToken = new AuthenticationToken(
			username: username,
			authToken: tokenValue
			).save(flush:true)
		//String accessToken = "d34l1gfteuenjmapg2m9d5qu4kvn3hq7"
		String path = grailsApplication.config.myapp.tempIdFileFolderPath+"survey.json"
		def json = JSON.parse(new FileInputStream(path), "UTF-8")
		int surveyId = json.content.surveys.surveyId
		println "got surveyId---"+surveyId
		SurveyController surveyController = new SurveyController();
		String surveyName = new GenerateDataUtils().generateSurveyName();
		surveyController.request.addHeader("contentType","application/json")
		surveyController.request.addHeader("X-Auth-Token",tokenValue)
		surveyController.request.content = '''{questions:[ {"query":"1st question ?",
												"questionType":"binary",     
												"option":[yes,no]
											  },
											  {
											    "query":"2nd question ?",
											    "questionType":"binary",     
											    "option":[yes,no]
											  }]
											}'''
		surveyController.params.surveyId=surveyId
		when: "create survey action is invoked"
		surveyController.addAllQuestions()
		println "add all Multiple questions -------------"
		then: "testing error response from create controller"
		def resultSurvey = surveyController.response.json
		println "-----------survey updated-------------"+resultSurvey
		assert surveyController.response.json.error == false
		/*assert surveyController.response.json.resp.status == status*/
	}
	
	// * Test add one  Multiple questions to survey
	 
	void "test add  Slider questions to Survey test"() {
		
		given: "login controller"
		//get auth token to send in request header
		/**
		 * here authToken hard coded
		 * further we put auth token from calling auth token
		 */
		String pathUser = grailsApplication.config.myapp.tempIdFileFolderPath+"user.json"
		def jsonUser = JSON.parse(new FileInputStream(pathUser), "UTF-8")
		int userId = jsonUser.content.user.userId
		println "got userId---"+userId
		User userNameObj=User.findById(userId)
		String username = userNameObj.username
		GenerateDataUtils gDatautils = new GenerateDataUtils();
		String tokenValue = java.util.UUID.randomUUID()
		AuthenticationToken authenticationToken = new AuthenticationToken(
			username: username,
			authToken: tokenValue
			).save(flush:true)
		//String accessToken = "d34l1gfteuenjmapg2m9d5qu4kvn3hq7"
		String path = grailsApplication.config.myapp.tempIdFileFolderPath+"survey.json"
		def json = JSON.parse(new FileInputStream(path), "UTF-8")
		int surveyId = json.content.surveys.surveyId
		println "got surveyId---"+surveyId
		SurveyController surveyController = new SurveyController();
		String surveyName = new GenerateDataUtils().generateSurveyName();
		surveyController.request.addHeader("contentType","application/json")
		surveyController.request.addHeader("X-Auth-Token",tokenValue)
		surveyController.request.content = '''{
											   "query":"Question1",
											   "questionType":"scale",
											   "option":["Ugly","Bad"]
											  }'''
		surveyController.params.surveyId=surveyId
		when: "create survey action is invoked"
		surveyController.addQuestions()
		println "add  Slider questions-------------"
		then: "testing error response from create controller"
		def result = surveyController.response.json
		println "-----------Binary Question created-------------"+result
		String questionIdStr = surveyController.response.json.resp.id
			println "surveyController.response.json.resp.id---"+surveyController.response.json.resp.id
			println "surveyId--"+questionIdStr
			int questionIdT = Integer.parseInt(questionIdStr)
			println "questionIdT---"+questionIdT
			def jsonBuilder = new groovy.json.JsonBuilder()
				jsonBuilder.question(
					questionId: questionIdT
				)
				println("Using just named arguments")
				println(jsonBuilder.toPrettyString())
			//GetId object=new GetId(questionId:questionIdT)
			def converter = jsonBuilder as JSON
			String pathQ = grailsApplication.config.myapp.tempIdFileFolderPath+"sliderQuestion.json"
			converter.render(new java.io.FileWriter(pathQ));
		assert surveyController.response.json.error == false
		
		/*assert surveyController.response.json.resp.status == status*/
	}
	
	// * Test add all  Slider questions to survey
	 
	void "test add all  Slider questions to Survey test"() {
		
		given: "login controller"
		//get auth token to send in request header
		/**
		 * here authToken hard coded
		 * further we put auth token from calling auth token
		 */
		String pathUser = grailsApplication.config.myapp.tempIdFileFolderPath+"user.json"
		def jsonUser = JSON.parse(new FileInputStream(pathUser), "UTF-8")
		int userId = jsonUser.content.user.userId
		println "got userId---"+userId
		User userNameObj=User.findById(userId)
		String username = userNameObj.username
		GenerateDataUtils gDatautils = new GenerateDataUtils();
		String tokenValue = java.util.UUID.randomUUID()
		AuthenticationToken authenticationToken = new AuthenticationToken(
			username: username,
			authToken: tokenValue
			).save(flush:true)
		//String accessToken = "d34l1gfteuenjmapg2m9d5qu4kvn3hq7"
		String path = grailsApplication.config.myapp.tempIdFileFolderPath+"survey.json"
		def json = JSON.parse(new FileInputStream(path), "UTF-8")
		int surveyId = json.content.surveys.surveyId
		println "got surveyId---"+surveyId
		SurveyController surveyController = new SurveyController();
		String surveyName = new GenerateDataUtils().generateSurveyName();
		surveyController.request.addHeader("contentType","application/json")
		surveyController.request.addHeader("X-Auth-Token",tokenValue)
		surveyController.request.content = '''{questions:[ {
											   "query":"Question1",
											   "questionType":"scale",
											   "option":["Ugly","Bad"]
											 },
											 {
											   "query":"Question2",
											   "questionType":"scale",
											   "option":["Ugly","Bad"]
											 }]
											}'''
		surveyController.params.surveyId=surveyId
		when: "create survey action is invoked"
		surveyController.addAllQuestions()
		println "add all Slider questions -------------"
		then: "testing error response from create controller"
		def resultSurvey = surveyController.response.json
		println "-----------survey updated-------------"+resultSurvey
		assert surveyController.response.json.error == false
		/*assert surveyController.response.json.resp.status == status*/
	}
	
	 //* Test add one  Text questions to survey
	 
	void "test add Text questions to Survey test"() {
		
		given: "login controller"
		//get auth token to send in request header
		/**
		 * here authToken hard coded
		 * further we put auth token from calling auth token
		 */
		String pathUser = grailsApplication.config.myapp.tempIdFileFolderPath+"user.json"
		def jsonUser = JSON.parse(new FileInputStream(pathUser), "UTF-8")
		int userId = jsonUser.content.user.userId
		println "got userId---"+userId
		User userNameObj=User.findById(userId)
		String username = userNameObj.username
		GenerateDataUtils gDatautils = new GenerateDataUtils();
		String tokenValue = java.util.UUID.randomUUID()
		AuthenticationToken authenticationToken = new AuthenticationToken(
			username: username,
			authToken: tokenValue
			).save(flush:true)
		//String accessToken = "d34l1gfteuenjmapg2m9d5qu4kvn3hq7"
		String path = grailsApplication.config.myapp.tempIdFileFolderPath+"survey.json"
		def json = JSON.parse(new FileInputStream(path), "UTF-8")
		int surveyId = json.content.surveys.surveyId
		println "got surveyId---"+surveyId
		SurveyController surveyController = new SurveyController();
		String surveyName = new GenerateDataUtils().generateSurveyName();
		surveyController.request.addHeader("contentType","application/json")
		surveyController.request.addHeader("X-Auth-Token",tokenValue)
		surveyController.request.content = '''{
											  	"query":"Question1",
											  	"questionType":"text",
											   	"option":[ ]
										 	  }'''
		surveyController.params.surveyId=surveyId
		when: "create survey action is invoked"
		surveyController.addQuestions()
		println "add  Text questions-------------"
		then: "testing error response from create controller"
		def result = surveyController.response.json
		println "-----------Binary Question created-------------"+result
		String questionIdStr = surveyController.response.json.resp.id
			println "surveyController.response.json.resp.id---"+surveyController.response.json.resp.id
			println "surveyId--"+questionIdStr
			int questionIdT = Integer.parseInt(questionIdStr)
			println "questionIdT---"+questionIdT
			def jsonBuilder = new groovy.json.JsonBuilder()
				jsonBuilder.question(
					questionId: questionIdT
				)
				println("Using just named arguments")
				println(jsonBuilder.toPrettyString())
			//GetId object=new GetId(questionId:questionIdT)
			def converter = jsonBuilder as JSON
			String pathQ = grailsApplication.config.myapp.tempIdFileFolderPath+"textQuestion.json"
			converter.render(new java.io.FileWriter(pathQ));
		assert surveyController.response.json.error == false
		
		/*assert surveyController.response.json.resp.status == status*/
	}
	
	 //* Test add all text questions to survey
	 
	void "test add all Text questions to Survey test"() {
		
		given: "login controller"
		//get auth token to send in request header
		/**
		 * here authToken hard coded
		 * further we put auth token from calling auth token
		 */
		String pathUser = grailsApplication.config.myapp.tempIdFileFolderPath+"user.json"
		def jsonUser = JSON.parse(new FileInputStream(pathUser), "UTF-8")
		int userId = jsonUser.content.user.userId
		println "got userId---"+userId
		User userNameObj=User.findById(userId)
		String username = userNameObj.username
		GenerateDataUtils gDatautils = new GenerateDataUtils();
		String tokenValue = java.util.UUID.randomUUID()
		AuthenticationToken authenticationToken = new AuthenticationToken(
			username: username,
			authToken: tokenValue
			).save(flush:true)
		//String accessToken = "d34l1gfteuenjmapg2m9d5qu4kvn3hq7"
		String path = grailsApplication.config.myapp.tempIdFileFolderPath+"survey.json"
		def json = JSON.parse(new FileInputStream(path), "UTF-8")
		int surveyId = json.content.surveys.surveyId
		println "got surveyId---"+surveyId
		SurveyController surveyController = new SurveyController();
		String surveyName = new GenerateDataUtils().generateSurveyName();
		surveyController.request.addHeader("contentType","application/json")
		surveyController.request.addHeader("X-Auth-Token",tokenValue)
		surveyController.request.content = '''{questions:[{
												  "query":"Question1",
												  "questionType":"text",
												   "option":[ ]
												 }, 
												 {
												  "query":"Question2",
												  "questionType":"text",
												   "option":[ ]
												 }]
												}'''
		surveyController.params.surveyId=surveyId
		when: "create survey action is invoked"
		surveyController.addAllQuestions()
		println "add all Text questions -------------"
		then: "testing error response from create controller"
		def resultSurvey = surveyController.response.json
		println "-----------survey updated-------------"+resultSurvey
		assert surveyController.response.json.error == false
		/*assert surveyController.response.json.resp.status == status*/
	}
	
	 //* Get survey by Id
	 
	void "test get Survey test"() {
		
		given: "login controller"
		//get auth token to send in request header
		/**
		 * here authToken hard coded
		 * further we put auth token from calling auth token
		 */
		String pathUser = grailsApplication.config.myapp.tempIdFileFolderPath+"user.json"
		def jsonUser = JSON.parse(new FileInputStream(pathUser), "UTF-8")
		int userId = jsonUser.content.user.userId
		println "got userId---"+userId
		User userNameObj=User.findById(userId)
		String username = userNameObj.username
		String tokenValue = java.util.UUID.randomUUID()
		AuthenticationToken authenticationToken = new AuthenticationToken(
			username: username,
			authToken: tokenValue
			).save(flush:true)
		String accessToken = "d34l1gfteuenjmapg2m9d5qu4kvn3hq7"
		String path = grailsApplication.config.myapp.tempIdFileFolderPath+"survey.json"
		def json = JSON.parse(new FileInputStream(path), "UTF-8")
		int surveyId = json.content.surveys.surveyId
		println "got surveyId---"+surveyId
		SurveyController surveyController = new SurveyController();
		String surveyName = new GenerateDataUtils().generateSurveyName();
		surveyController.request.addHeader("contentType","application/json")
		surveyController.request.addHeader("X-Auth-Token",tokenValue)
		surveyController.params.surveyId=surveyId
		
		when: "create survey action is invoked"
		surveyController.getSurvey()
		println "survey created-------------"
		then: "testing error response from create controller"
		def resultSurvey = surveyController.response.json
		println "-----------survey updated-------------"+resultSurvey
		assert surveyController.response.json.error == false
		
	}
	
	//* Test update one binary question to survey
	 
	void "test update binary question to Survey test"() {
		
		given: "login controller"
		//get auth token to send in request header
		/**
		 * here authToken hard coded
		 * further we put auth token from calling auth token
		 */
		String pathUser = grailsApplication.config.myapp.tempIdFileFolderPath+"user.json"
		def jsonUser = JSON.parse(new FileInputStream(pathUser), "UTF-8")
		int userId = jsonUser.content.user.userId
		println "got userId---"+userId
		User userNameObj=User.findById(userId)
		String username = userNameObj.username
		GenerateDataUtils gDatautils = new GenerateDataUtils();
		String tokenValue = java.util.UUID.randomUUID()
		AuthenticationToken authenticationToken = new AuthenticationToken(
			username: username,
			authToken: tokenValue
			).save(flush:true)
		String accessToken = "d34l1gfteuenjmapg2m9d5qu4kvn3hq7"
		String path = grailsApplication.config.myapp.tempIdFileFolderPath+"survey.json"
		def json = JSON.parse(new FileInputStream(path), "UTF-8")
		int surveyId = json.content.surveys.surveyId
		println "got surveyId---"+surveyId
		String pathQ = grailsApplication.config.myapp.tempIdFileFolderPath+"binaryQuestion.json"
		def jsonQuestion = JSON.parse(new FileInputStream(pathQ), "UTF-8")
		int questionId = jsonQuestion.content.question.questionId
		println "questionId---"+questionId
		SurveyController surveyController = new SurveyController();
		String surveyName = new GenerateDataUtils().generateSurveyName();
		surveyController.request.addHeader("contentType","application/json")
		surveyController.request.addHeader("X-Auth-Token",tokenValue)
		surveyController.request.content = '''{"query":"updated 1st question ?",
											   "questionType":"binary",
											   "option":[yes,no]
											  }'''
		surveyController.params.surveyId=surveyId
		surveyController.params.questionId=questionId
		when: "create survey action is invoked"
		surveyController.editQuestion()
		println "survey created-------------"
		then: "testing error response from create controller"
		def result = surveyController.response.json
		println "-----------Binary Question created-------------"+result
		
		assert surveyController.response.json.error == false
		/*assert surveyController.response.json.resp.status == status*/
	}
	
	 //* Test update one slider question to survey
	 
	void "test update slider question to Survey test"() {
		
		given: "login controller"
		//get auth token to send in request header
		/**
		 * here authToken hard coded
		 * further we put auth token from calling auth token
		 */
		String pathUser = grailsApplication.config.myapp.tempIdFileFolderPath+"user.json"
		def jsonUser = JSON.parse(new FileInputStream(pathUser), "UTF-8")
		int userId = jsonUser.content.user.userId
		println "got userId---"+userId
		User userNameObj=User.findById(userId)
		String username = userNameObj.username
		GenerateDataUtils gDatautils = new GenerateDataUtils();
		String tokenValue = java.util.UUID.randomUUID()
		AuthenticationToken authenticationToken = new AuthenticationToken(
			username: username,
			authToken: tokenValue
			).save(flush:true)
		String accessToken = "d34l1gfteuenjmapg2m9d5qu4kvn3hq7"
		String path = grailsApplication.config.myapp.tempIdFileFolderPath+"survey.json"
		def json = JSON.parse(new FileInputStream(path), "UTF-8")
		int surveyId = json.content.surveys.surveyId
		println "got surveyId---"+surveyId
		String pathQ = grailsApplication.config.myapp.tempIdFileFolderPath+"sliderQuestion.json"
		def jsonQuestion = JSON.parse(new FileInputStream(pathQ), "UTF-8")
		int questionId = jsonQuestion.content.question.questionId
		println "questionId---"+questionId
		SurveyController surveyController = new SurveyController();
		String surveyName = new GenerateDataUtils().generateSurveyName();
		surveyController.request.addHeader("contentType","application/json")
		surveyController.request.addHeader("X-Auth-Token",tokenValue)
		surveyController.request.content = '''{
											   "query":"update Question1",
											   "questionType":"scale",
											   "option":["Ugly","Bad"]
											  }'''
		surveyController.params.surveyId=surveyId
		surveyController.params.questionId=questionId
		when: "create survey action is invoked"
		surveyController.editQuestion()
		println "survey created-------------"
		then: "testing error response from create controller"
		def result = surveyController.response.json
		println "-----------Binary Question created-------------"+result
		
		assert surveyController.response.json.error == false
		/*assert surveyController.response.json.resp.status == status*/
	}
	
	// * Test add one textQuestion question to survey
	 
	void "test update text question to Survey test"() {
		
		given: "login controller"
		//get auth token to send in request header
		/**
		 * here authToken hard coded
		 * further we put auth token from calling auth token
		 */
		String pathUser = grailsApplication.config.myapp.tempIdFileFolderPath+"user.json"
		def jsonUser = JSON.parse(new FileInputStream(pathUser), "UTF-8")
		int userId = jsonUser.content.user.userId
		println "got userId---"+userId
		User userNameObj=User.findById(userId)
		String username = userNameObj.username
		GenerateDataUtils gDatautils = new GenerateDataUtils();
		String tokenValue = java.util.UUID.randomUUID()
		AuthenticationToken authenticationToken = new AuthenticationToken(
			username: username,
			authToken: tokenValue
			).save(flush:true)
		String accessToken = "d34l1gfteuenjmapg2m9d5qu4kvn3hq7"
		String path = grailsApplication.config.myapp.tempIdFileFolderPath+"survey.json"
		def json = JSON.parse(new FileInputStream(path), "UTF-8")
		int surveyId = json.content.surveys.surveyId
		println "got surveyId---"+surveyId
		String pathQ = grailsApplication.config.myapp.tempIdFileFolderPath+"textQuestion.json"
		def jsonQuestion = JSON.parse(new FileInputStream(pathQ), "UTF-8")
		int questionId = jsonQuestion.content.question.questionId
		println "questionId---"+questionId
		SurveyController surveyController = new SurveyController();
		String surveyName = new GenerateDataUtils().generateSurveyName();
		surveyController.request.addHeader("contentType","application/json")
		surveyController.request.addHeader("X-Auth-Token",tokenValue)
		surveyController.request.content = '''{"query":"update Question1",
											  	"questionType":"text",
											   	"option":[ ]
										 	  }'''
		surveyController.params.surveyId=surveyId
		surveyController.params.questionId=questionId
		when: "create survey action is invoked"
		surveyController.editQuestion()
		println "survey created-------------"
		then: "testing error response from create controller"
		def result = surveyController.response.json
		println "-----------Binary Question created-------------"+result
		
		assert surveyController.response.json.error == false
		/*assert surveyController.response.json.resp.status == status*/
	}
	
	 //* Test update one multipleQuestion question to survey
	 
	void "test update multipleQuestion question to Survey test"() {
		
		given: "login controller"
		//get auth token to send in request header
		/**
		 * here authToken hard coded
		 * further we put auth token from calling auth token
		 */
		String pathUser = grailsApplication.config.myapp.tempIdFileFolderPath+"user.json"
		def jsonUser = JSON.parse(new FileInputStream(pathUser), "UTF-8")
		int userId = jsonUser.content.user.userId
		println "got userId---"+userId
		User userNameObj=User.findById(userId)
		String username = userNameObj.username
		GenerateDataUtils gDatautils = new GenerateDataUtils();
		String tokenValue = java.util.UUID.randomUUID()
		AuthenticationToken authenticationToken = new AuthenticationToken(
			username: username,
			authToken: tokenValue
			).save(flush:true)
		String accessToken = "d34l1gfteuenjmapg2m9d5qu4kvn3hq7"
		String path = grailsApplication.config.myapp.tempIdFileFolderPath+"survey.json"
		def json = JSON.parse(new FileInputStream(path), "UTF-8")
		int surveyId = json.content.surveys.surveyId
		println "got surveyId---"+surveyId
		String pathQ = grailsApplication.config.myapp.tempIdFileFolderPath+"multipleQuestion.json"
		def jsonQuestion = JSON.parse(new FileInputStream(pathQ), "UTF-8")
		int questionId = jsonQuestion.content.question.questionId
		println "questionId---"+questionId
		SurveyController surveyController = new SurveyController();
		String surveyName = new GenerateDataUtils().generateSurveyName();
		surveyController.request.addHeader("contentType","application/json")
		surveyController.request.addHeader("X-Auth-Token",tokenValue)
		surveyController.request.content = '''{"query":"update is this test question 6666 ?",
											   "questionType":"multiplechoice",
											   "option":[{id:1,value:"No",image:""},{id:2,value:"yes",image:""}]
											 }'''
		surveyController.params.surveyId=surveyId
		surveyController.params.questionId=questionId
		when: "create survey action is invoked"
		surveyController.editQuestion()
		println "survey created-------------"
		then: "testing error response from create controller"
		def result = surveyController.response.json
		println "-----------Binary Question created-------------"+result
		
		assert surveyController.response.json.error == false
		/*assert surveyController.response.json.resp.status == status*/
	}
	
	 //* Test delete question to survey
	 
	void "test delete Question question to Survey test"() {
		
		given: "login controller"
		//get auth token to send in request header
		/**
		 * here authToken hard coded
		 * further we put auth token from calling auth token
		 */
		String pathUser = grailsApplication.config.myapp.tempIdFileFolderPath+"user.json"
		def jsonUser = JSON.parse(new FileInputStream(pathUser), "UTF-8")
		int userId = jsonUser.content.user.userId
		println "got userId---"+userId
		User userNameObj=User.findById(userId)
		String username = userNameObj.username
		GenerateDataUtils gDatautils = new GenerateDataUtils();
		String tokenValue = java.util.UUID.randomUUID()
		AuthenticationToken authenticationToken = new AuthenticationToken(
			username: username,
			authToken: tokenValue
			).save(flush:true)
		String accessToken = "d34l1gfteuenjmapg2m9d5qu4kvn3hq7"
		String path = grailsApplication.config.myapp.tempIdFileFolderPath+"survey.json"
		def json = JSON.parse(new FileInputStream(path), "UTF-8")
		int surveyId = json.content.surveys.surveyId
		println "got surveyId---"+surveyId
		String pathQ = grailsApplication.config.myapp.tempIdFileFolderPath+"multipleQuestion.json"
		def jsonQuestion = JSON.parse(new FileInputStream(pathQ), "UTF-8")
		int questionId = jsonQuestion.content.question.questionId
		println "questionId---"+questionId
		SurveyController surveyController = new SurveyController();
		String surveyName = new GenerateDataUtils().generateSurveyName();
		surveyController.request.addHeader("contentType","application/json")
		surveyController.request.addHeader("X-Auth-Token",tokenValue)
		
		surveyController.params.surveyId=surveyId
		surveyController.params.questionId=questionId
		when: "create survey action is invoked"
		surveyController.deleteQuestion()
		println "quetion try to delete-------------"
		then: "testing error response from create controller"
		def result = surveyController.response.json
		println "-----------Question deleted-------------"+result
		
		assert surveyController.response.json.error == false
		/*assert surveyController.response.json.resp.status == status*/
	}
	
	// * Get all survey
	 
	void "test get All Survey test"() {
		
		given: "login controller"
		//get auth token to send in request header
		/**
		 * here authToken hard coded
		 * further we put auth token from calling auth token
		 */
		String pathUser = grailsApplication.config.myapp.tempIdFileFolderPath+"user.json"
		def jsonUser = JSON.parse(new FileInputStream(pathUser), "UTF-8")
		int userId = jsonUser.content.user.userId
		println "got userId---"+userId
		User userNameObj=User.findById(userId)
		String username = userNameObj.username
		GenerateDataUtils gDatautils = new GenerateDataUtils();
		String tokenValue = java.util.UUID.randomUUID()
		AuthenticationToken authenticationToken = new AuthenticationToken(
			username: username,
			authToken: tokenValue
			).save(flush:true)
		String accessToken = "d34l1gfteuenjmapg2m9d5qu4kvn3hq7"
		SurveyController surveyController = new SurveyController();
		surveyController.request.addHeader("contentType","application/json")
		surveyController.request.addHeader("X-Auth-Token",tokenValue)
		
		surveyController.params.offset = gDatautils.genereateOffset();
		surveyController.params.limit = gDatautils.genereateLimit();
		when: "create survey action is invoked"
		surveyController.allSurvey()
		println "--------------Get all survey -------------"
		then: "testing error response from create controller"
		def resultSurvey = surveyController.response.json
		println "-----------survey updated-------------"+resultSurvey
		assert surveyController.response.json.error == false
		
	}
	
	// * Delete Survey
	 
	void "test delete Survey test"() {
		
		given: "login controller"
		//get auth token to send in request header
		/**
		 * here authToken hard coded
		 * further we put auth token from calling auth token
		 */
		String pathUser = grailsApplication.config.myapp.tempIdFileFolderPath+"user.json"
		def jsonUser = JSON.parse(new FileInputStream(pathUser), "UTF-8")
		int userId = jsonUser.content.user.userId
		println "got userId---"+userId
		User userNameObj=User.findById(userId)
		String username = userNameObj.username
		GenerateDataUtils gDatautils = new GenerateDataUtils();
		String tokenValue = java.util.UUID.randomUUID()
		AuthenticationToken authenticationToken = new AuthenticationToken(
			username: username,
			authToken: tokenValue
			).save(flush:true)
		String accessToken = "d34l1gfteuenjmapg2m9d5qu4kvn3hq7"
		String path = grailsApplication.config.myapp.tempIdFileFolderPath+"survey.json"
		def json = JSON.parse(new FileInputStream(path), "UTF-8")
		int surveyId = json.content.surveys.surveyId
		println "got surveyId---"+surveyId
		SurveyController surveyController = new SurveyController();
		surveyController.request.addHeader("contentType","application/json")
		surveyController.request.addHeader("X-Auth-Token",tokenValue)
		surveyController.params.surveyId=surveyId
		when: "delete survey action is invoked"
		surveyController.deleteSurvey()
		println "--------------Delete survey " + surveyId + "-------------"
		File file=new File(path);
		if(file.exists())
		{
			file.delete();
			println "file is deleted from directory-"
		}
		then: "testing error response from create controller"
		def resultSurvey = surveyController.response.json
		println "-----------survey updated-------------"+resultSurvey
		assert surveyController.response.json.error == false
		
	}
	
}
