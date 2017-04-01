package com.mopi.imageupload

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestFor(ProfileimageuploadController)
class ProfileimageuploadControllerSpec extends Specification {

	def setup() {
    }

    def cleanup() {
    }

    void "test succesful profile image upload"() {
		given: "setting request headers"
		request['X-Auth-Token'] = java.util.UUID.randomUUID()
		params.userId = 9
		
//		and:""
//		def mockUserService = mockFor(com.themopi.account.UserService)
//		mockUserService.demand.getUserAndRoleByAuthToken{tokenValue -> throw new AccountException(status,errorCode,message,extendedMessage,moreInfo) }
//		controller.userService = mockUserService.createMock()
				
		when:""
		
		then: ""
		
    }
}
