
package com.themopi.apis.security

import grails.test.mixin.*
import spock.lang.Specification
import spock.lang.Unroll

import com.themopi.account.User
import com.themopi.exceptions.AccountException
import com.themopi.utils.GenerateDataUtils

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(ForgotPasswordController)
@Mock(User)
class ForgotPasswordControllerSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    /**
	 * if X-Auth-Token is blank or authtoken object is null
	 * code should throw 404 auth-token not found exception
	 */
	@Unroll("test email constraint for email is #email")
    void "test if email is blank"() {
		given: "setting request headers"
		request.contentType = 'application/json'
		params.email = email
		
		and: "mock user service and throw account exception"		
		int errorCode =  grailsApplication.config.customExceptions.account.fourZeroFour.forgotpassword.errorCode
		int status =  grailsApplication.config.customExceptions.account.fourZeroFour.forgotpassword.status
		def message =  grailsApplication.config.customExceptions.account.fourZeroFour.forgotpassword.message
		def extendedMessage =  grailsApplication.config.customExceptions.account.fourZeroFour.forgotpassword.extendedMessage
		def moreInfo =  grailsApplication.config.customExceptions.account.fourZeroFour.forgotpassword.moreInfo
		def mockUserService = mockFor(com.themopi.account.UserService)
//		def mockForgotPasswordService = mockFor(com.themopi.apis.security.ForgotPasswordService)
		
		
		
		mockUserService.demand.findUserByEmail{email -> throw new AccountException(status,errorCode,message,extendedMessage,moreInfo) }
		controller.userService = mockUserService.createMock()
				
		when: "logout action is invoked"
		controller.forgotPassword()
		
		then: "test error response from controller"
		def result = controller.response.json
		println "result is "+result 
		assert result.status == grailsApplication.config.customExceptions.account.fourZeroFour.forgotpassword.status
		assert result.errorCode == grailsApplication.config.customExceptions.account.fourZeroFour.forgotpassword.errorCode
		assert result.message == grailsApplication.config.customExceptions.account.fourZeroFour.forgotpassword.message
		assert result.extendedMessage == grailsApplication.config.customExceptions.account.fourZeroFour.forgotpassword.extendedMessage	
		
		where: "pass email values"
		email	| _
		''		| _
		null	| _
		new GenerateDataUtils().generateRandomEmail() | _
    }
}
