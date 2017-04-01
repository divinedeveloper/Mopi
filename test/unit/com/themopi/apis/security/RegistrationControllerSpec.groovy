package com.themopi.apis.security

import grails.plugin.springsecurity.*
import grails.test.mixin.*
import spock.lang.Specification

import com.themopi.account.User
import com.themopi.utils.GenerateDataUtils

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(RegistrationController)
@Mock([com.themopi.account.UserService, com.themopi.account.User, com.themopi.account.Role,com.themopi.account.UserRole, com.themopi.account.RegistrationToken, com.themopi.user.Profile, com.themopi.user.Address, com.themopi.user.UserDistribution,SpringSecurityService])
class RegistrationControllerSpec extends Specification {

	
	def setup() {
    }

    def cleanup() {
	}

	/**
	 * testing for valid signup jsonpayload
	 */
 	void "test signUp action for valid signup data"(){
		given:"gennerate random values for email, name and username and password"
		def stringValue = new GenerateDataUtils().randomString()
		def emailValue = new GenerateDataUtils().generateRandomEmail()
				
		and: "mock user service and pass arguments to addUser method"
		def mockUserService = mockFor(com.themopi.account.UserService)
		mockUserService.demand.addUser{ a, b -> return new User(email: emailValue, name: stringValue, username: stringValue, roleId: 4, isActive: true)}
		controller.userService = mockUserService.createMock()
		
		and: "set request headers and json payload"
		request.contentType = 'application/json'
		request['X-Auth-Token'] = null
		request.json = '''{"email":'''+emailValue+''',
						"password":'''+stringValue+''', 
						"password2":'''+stringValue+''',
						"roleId":'''+4+''', 
						"name":'''+stringValue+''',
						"username":'''+stringValue+
						'''}'''
						
		when:"signup action is invoked"
		controller.signUp()
		
		then:
		def result = controller.response.json
		assert result.error == false
		assert result.resp.email == emailValue
		assert result.resp.name == stringValue
		assert result.resp.username == stringValue
		assert result.resp.roleId == 4
		assert result.resp.isActive == true
		assert controller.response.status == 200
	}
	 
	 /**
	  * testing for invalid signup jsonpayload
	  */
	  void "test signUp action if all signup fields are blank or null"(){
		 given:"gennerate random values for email, name and username and password"
		 def stringValue = new GenerateDataUtils().randomString()
		 def emailValue = new GenerateDataUtils().generateRandomEmail()
				 
		 and: "mock user service and pass arguments to addUser method"
		 def mockUserService = mockFor(com.themopi.account.UserService)
		 int errorCode = grailsApplication.config.customExceptions.account.fourZeroZero.user.errorCode
		 int status = grailsApplication.config.customExceptions.account.fourZeroZero.user.status
		 String message = grailsApplication.config.customExceptions.account.fourZeroZero.user.message
		 String extendedMessage = grailsApplication.config.customExceptions.account.fourZeroZero.user.extendedMessage
		 String moreInfo = grailsApplication.config.customExceptions.account.fourZeroZero.user.moreInfo
		 mockUserService.demand.addUser{ a, b -> throw new com.themopi.exceptions.AccountException(status,errorCode,message,extendedMessage,moreInfo)}
		 controller.userService = mockUserService.createMock()
		 
		 and: "set request headers and blank signup fields"
		 request.contentType = 'application/json'
		 request['X-Auth-Token'] = null
		 request.json = '''{"email":"",
		 					"password":"",
		 					"password2":"",
		 					"roleId":4,
		 					"name":"",
		 					"username":""
		 					}'''
						
		when:"signup action is invoked"
		controller.signUp()
		
		then: "test error response"
		def result = controller.response.json
		println "result is "+result
		assert result.status == status
		assert result.errorCode == errorCode
		assert result.message == message
		assert result.extendedMessage == extendedMessage
		assert result.error == true
		assert controller.response.status == status
		
		when: "signup fields are null"
		mockUserService.demand.addUser{ a, b -> throw new com.themopi.exceptions.AccountException(status,errorCode,message,extendedMessage,moreInfo)}
		request.contentType = 'application/json'
		request['X-Auth-Token'] = null
		request.json = '''{"email":null,
		 					"password":null,
		 					"password2":null,
		 					"roleId":4,
		 					"name":null,
		 					"username":null
		 					}'''
		controller.signUp()
		
		then: "test error response"
		def result1 = controller.response.json
		println "result is "+result
		assert result1.status == status
		assert result1.errorCode == errorCode
		assert result1.message == message
		assert result1.extendedMessage == extendedMessage
		assert result1.error == true
		assert controller.response.status == status
	}
	  
	  /**
	   * testing if email already exists
	   */
	   void "test if email already exists"(){
		  given:"gennerate random values for email, name and username and password"
		  def stringValue = new GenerateDataUtils().randomString()
		  def emailValue = new GenerateDataUtils().generateRandomEmail()
				  
		  and: "mock user service and pass arguments to addUser method"
		  def mockUserService = mockFor(com.themopi.account.UserService)
		  mockForConstraintsTests(User, [new User(email: emailValue, name: stringValue, username: stringValue, roleId: 4, isActive: true)])
		  
		  int errorCode = grailsApplication.config.customExceptions.account.fourZeroNine.userCreate.errorCode
		  int status = grailsApplication.config.customExceptions.account.fourZeroNine.userCreate.status
		  String message = grailsApplication.config.customExceptions.account.fourZeroNine.userCreate.message
		  String extendedMessage = grailsApplication.config.customExceptions.account.fourZeroNine.userCreate.extendedMessage
		  String moreInfo = grailsApplication.config.customExceptions.account.fourZeroNine.userCreate.moreInfo
		  mockUserService.demand.addUser{ a, b -> throw new com.themopi.exceptions.AccountException(status,errorCode,message,extendedMessage,moreInfo)}
		  controller.userService = mockUserService.createMock()
		  
		  and: "set request headers and json payload"
		  request.contentType = 'application/json'
		  request['X-Auth-Token'] = null
		  request.json = '''{"email":'''+emailValue+''',
						"password":'''+stringValue+''', 
						  "password2":'''+stringValue+''',
						"roleId":'''+4+''', 
						  "name":'''+stringValue+''',
						"username":'''+stringValue+
						  '''}'''
						
		when:"signup action is invoked"
		controller.signUp()
		
		then:
		def result = controller.response.json
		println "result is "+result
		assert result.status == status
		assert result.errorCode == errorCode
		assert result.message == message
		assert result.extendedMessage == extendedMessage
		assert result.error == true
		assert controller.response.status == status
	}
	   
	   /**
		* testing if username already exists
		*/
		void "test if username already exists"(){
		   given:"gennerate random values for email, name and username and password"
		   def stringValue = new GenerateDataUtils().randomString()
		   def emailValue = new GenerateDataUtils().generateRandomEmail()
				   
		   and: "mock user service and pass arguments to addUser method"
		   def mockUserService = mockFor(com.themopi.account.UserService)
		   mockForConstraintsTests(User, [new User(email: emailValue, name: stringValue, username: stringValue, roleId: 4, isActive: true)])
		   
		   int errorCode = grailsApplication.config.customExceptions.account.fourZeroNine.username.errorCode
		   int status = grailsApplication.config.customExceptions.account.fourZeroNine.username.status
		   String message = grailsApplication.config.customExceptions.account.fourZeroNine.username.message
		   String extendedMessage = grailsApplication.config.customExceptions.account.fourZeroNine.username.extendedMessage
		   String moreInfo = grailsApplication.config.customExceptions.account.fourZeroNine.username.moreInfo
		   mockUserService.demand.addUser{ a, b -> throw new com.themopi.exceptions.AccountException(status,errorCode,message,extendedMessage,moreInfo)}
		   controller.userService = mockUserService.createMock()
		   
		   and: "set request headers and json payload"
		   request.contentType = 'application/json'
		   request['X-Auth-Token'] = null
		   request.json = '''{"email":'''+emailValue+''',
						"password":'''+stringValue+''', 
						   "password2":'''+stringValue+''',
						"roleId":'''+4+''', 
						   "name":'''+stringValue+''',
						"username":'''+stringValue+
						   '''}'''
						
		when:"signup action is invoked"
		controller.signUp()
		
		then:
		def result = controller.response.json
		println "result is "+result
		assert result.status == status
		assert result.errorCode == errorCode
		assert result.message == message
		assert result.extendedMessage == extendedMessage
		assert result.error == true
		assert controller.response.status == status
	}
		
		/**
		 * testing if role id is not found out of 1,2,3,4
		 */
		 void "test if role id not found"(){
			given:"gennerate random values for email, name and username and password"
			def stringValue = new GenerateDataUtils().randomString()
			def emailValue = new GenerateDataUtils().generateRandomEmail()
					
			and: "mock user service and pass arguments to addUser method"
			def mockUserService = mockFor(com.themopi.account.UserService)
						
			int errorCode = grailsApplication.config.customExceptions.account.fourZeroFour.role.errorCode
			int status = grailsApplication.config.customExceptions.account.fourZeroFour.role.status
			String message = grailsApplication.config.customExceptions.account.fourZeroFour.role.message
			String extendedMessage = grailsApplication.config.customExceptions.account.fourZeroFour.role.extendedMessage
			String moreInfo = grailsApplication.config.customExceptions.account.fourZeroFour.role.moreInfo
			mockUserService.demand.addUser{ a, b -> throw new com.themopi.exceptions.AccountException(status,errorCode,message,extendedMessage,moreInfo)}
			controller.userService = mockUserService.createMock()
			
			and: "set request headers and json payload"
			request.contentType = 'application/json'
			request['X-Auth-Token'] = null
			request.json = '''{"email":'''+emailValue+''',
						"password":'''+stringValue+''', 
							"password2":'''+stringValue+''',
						"roleId":'''+8+''', 
							"name":'''+stringValue+''',
						"username":'''+stringValue+
							'''}'''
						
		when:"signup action is invoked"
		controller.signUp()
		
		then:
		def result = controller.response.json
		println "result is "+result
		assert result.status == status
		assert result.errorCode == errorCode
		assert result.message == message
		assert result.extendedMessage == extendedMessage
		assert result.error == true
		assert controller.response.status == status
	}
}
