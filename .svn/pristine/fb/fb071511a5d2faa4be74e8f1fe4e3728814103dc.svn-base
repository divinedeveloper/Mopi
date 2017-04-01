package com.themopi.apis.security



import grails.test.spock.IntegrationSpec
import spock.lang.*

import com.themopi.account.User
import com.themopi.utils.GenerateDataUtils

/**
 *
 */
class RegistrationControllerIntegrationSpec extends IntegrationSpec {
	def logoutService
	def grailsApplication

    def setup() {
    }

    def cleanup() {
		//find user by email and delete it
		/*User user = User.findByEmail("testinguser1@rkgtechllc.com")
		if(user != null){
			user.delete(flush:true)
		}*/
    }

	/**
	 * testing for valid signup jsonpayload
	 */
    void "test valid signup data"() {
		given: "Initializing registration controller and setting request headers"
		def registrationController = new RegistrationController()
		registrationController.request.addHeader("contentType","application/json")
		
		and: "generating random data and passing valid json"
		def stringValue = new GenerateDataUtils().randomString()
		def emailValue = new GenerateDataUtils().generateRandomEmail()
		registrationController.request.content = '''{"email":'''+emailValue+''',
													"password":'''+stringValue+''', 
													"password2":'''+stringValue+''',
													"roleId":'''+4+''', 
													"name":'''+stringValue+''',
													"username":'''+stringValue+
													'''}'''
		when: "signup action is invoked"
		registrationController.signUp()
		
		then: "testing success response from controller"
		def result = registrationController.response.json
		assert registrationController.response.status == 200
		assert registrationController.response.json.error == false
		assert registrationController.response.json.resp.email == emailValue
		assert registrationController.response.json.resp.roleId == 4
		assert registrationController.response.json.resp.name == stringValue
		assert registrationController.response.json.resp.username == stringValue
	}
	
	/**
	 * testing for invalid signup jsonpayload
	 */
	void "test if signup data is blank or null"() {
		given: "Initializing registration controller and setting request headers"
		def registrationController = new RegistrationController()
		registrationController.request.addHeader("contentType","application/json")
		
		and: "passing blank email, username, name in json data"
		registrationController.request.content = '''{"email":"",
												 "password":"",
												 "password2":"",
												 "roleId":4,
												 "name":"",
												 "username":""
												}'''
		when: "signup action is invoked"
		registrationController.signUp()
		
		then: "testing error response from controller"
		def result = registrationController.response.json
		assert registrationController.response.status == 400
		assert result.status == grailsApplication.config.customExceptions.account.fourZeroZero.user.status
		assert result.error == true
		assert result.errorCode == grailsApplication.config.customExceptions.account.fourZeroZero.user.errorCode
		assert result.message == grailsApplication.config.customExceptions.account.fourZeroZero.user.message
		assert result.extendedMessage == grailsApplication.config.customExceptions.account.fourZeroZero.user.extendedMessage
		
		when: "passing null email, username, name in json data"
		registrationController.request.content = '''{"email":null,
		 					"password":null,
		 					"password2":null,
		 					"roleId":4,
		 					"name":null,
		 					"username":null
		 					}'''
		registrationController.signUp()
		
		then: "testing error response from controller"
		def result1 = registrationController.response.json
		assert registrationController.response.status == 400
		assert result1.status == grailsApplication.config.customExceptions.account.fourZeroZero.user.status
		assert result1.error == true
		assert result1.errorCode == grailsApplication.config.customExceptions.account.fourZeroZero.user.errorCode
		assert result1.message == grailsApplication.config.customExceptions.account.fourZeroZero.user.message
		assert result1.extendedMessage == grailsApplication.config.customExceptions.account.fourZeroZero.user.extendedMessage
	}
	
	/**
	 * testing if email already exists
	 */
	 void "test if email already exists"(){
		given:"generate random values for email, name and username and password"
		def stringValue = new GenerateDataUtils().randomString()
		def emailValue = new GenerateDataUtils().generateRandomEmail()
				
		and: "initialize controller and save a user with an email"
		def registrationController = new RegistrationController()
		registrationController.request.addHeader("contentType","application/json")
		
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
			).save(flush:true)
		
		and: "set request headers and json payload"
		registrationController.request.addHeader("contentType","application/json")
		registrationController.request.content = '''{"email":'''+emailValue+''',
						"password":'''+stringValue+''', 
						"password2":'''+stringValue+''',
						"roleId":'''+4+''', 
						"name":'''+stringValue+''',
						"username":'''+stringValue+
						'''}'''
						
		when:"signup action is invoked"
		registrationController.signUp()
		
		then: "test error response"
		def result = registrationController.response.json
		println "result is "+result
		assert result.status == grailsApplication.config.customExceptions.account.fourZeroNine.userCreate.status
		assert result.errorCode == grailsApplication.config.customExceptions.account.fourZeroNine.userCreate.errorCode
		assert result.message == grailsApplication.config.customExceptions.account.fourZeroNine.userCreate.message
		assert result.extendedMessage == grailsApplication.config.customExceptions.account.fourZeroNine.userCreate.extendedMessage
		assert result.error == true
		assert registrationController.response.status == grailsApplication.config.customExceptions.account.fourZeroNine.userCreate.status
	}
	 
	 /**
	  * testing if username already exists
	  */
	  void "test if username already exists"(){
		 given:"generate random values for email, name and username and password"
		 def stringValue = new GenerateDataUtils().randomString()
		 def emailValue = new GenerateDataUtils().generateRandomEmail()
		 def newEmailValue = new GenerateDataUtils().generateRandomEmail()
				 
		 and: "initialize controller and save a user with an email"
		 def registrationController = new RegistrationController()
		 registrationController.request.addHeader("contentType","application/json")
		 
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
			 ).save(flush:true)
		 
		 and: "set request headers and json payload"
		 registrationController.request.addHeader("contentType","application/json")
		 registrationController.request.content = '''{"email":'''+newEmailValue+''',
						"password":'''+stringValue+''', 
						 "password2":'''+stringValue+''',
						"roleId":'''+4+''', 
						 "name":'''+stringValue+''',
						"username":'''+stringValue+
						 '''}'''
						
		when:"signup action is invoked"
		registrationController.signUp()
		
		then: "test error response"
		def result = registrationController.response.json
		println "result is "+result
		assert result.status == grailsApplication.config.customExceptions.account.fourZeroNine.username.status
		assert result.errorCode == grailsApplication.config.customExceptions.account.fourZeroNine.username.errorCode
		assert result.message == grailsApplication.config.customExceptions.account.fourZeroNine.username.message
		assert result.extendedMessage == grailsApplication.config.customExceptions.account.fourZeroNine.username.extendedMessage
		assert result.error == true
		assert registrationController.response.status == grailsApplication.config.customExceptions.account.fourZeroNine.username.status
	}
	  
	  /**
	   * testing if username already exists
	   */
	   void "test if roleId not found"(){
		  given:"generate random values for email, name and username and password"
		  def stringValue = new GenerateDataUtils().randomString()
		  def emailValue = new GenerateDataUtils().generateRandomEmail()
		  				  
		  and: "initialize controller"
		  def registrationController = new RegistrationController()
		  registrationController.request.addHeader("contentType","application/json")
		  
		  		  
		  and: "set request headers and json payload"
		  registrationController.request.addHeader("contentType","application/json")
		  registrationController.request.content = '''{"email":'''+emailValue+''',
						"password":'''+stringValue+''', 
						  "password2":'''+stringValue+''',
						"roleId":'''+9+''', 
						  "name":'''+stringValue+''',
						"username":'''+stringValue+
						  '''}'''
						
		when:"signup action is invoked"
		registrationController.signUp()
		
		then: "test error response"
		def result = registrationController.response.json
		println "result is "+result
		assert result.status == grailsApplication.config.customExceptions.account.fourZeroFour.role.status
		assert result.errorCode == grailsApplication.config.customExceptions.account.fourZeroFour.role.errorCode
		assert result.message == grailsApplication.config.customExceptions.account.fourZeroFour.role.message
		assert result.extendedMessage == grailsApplication.config.customExceptions.account.fourZeroFour.role.extendedMessage
		assert result.error == true
		assert registrationController.response.status == grailsApplication.config.customExceptions.account.fourZeroFour.role.status
	}
}
