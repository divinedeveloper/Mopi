package com.mopi.imageupload



import grails.plugin.springsecurity.authentication.dao.NullSaltSource

import org.codehaus.groovy.grails.plugins.testing.GrailsMockMultipartFile

import spock.lang.*

import com.themopi.account.AuthenticationToken
import com.themopi.account.User
import com.themopi.utils.GenerateDataUtils
import com.themopi.utils.TestingSetup

/**
 *
 */
class ProfileimageuploadControllerIntegrationSpec extends Specification {

	def grailsApplication
	def springSecurityService
	def saltSource
	def profileimageService
	
    def setup() {
    }

    def cleanup() {
    }

    void "test should upload profile image for a user and give image url in success response"() {
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
		println "user created is "+user
		
		long userIdT = user.id
		println "user id is "+user.id
		int iflag = testingSetup.writeUserIdToFile(userIdT)
		
		testingSetup.assignRoleUser(user.id,"ROLE_USER")
		user = testingSetup.updateProfile(user.id)
//		user = testingSetup.updateAddress(user.id)
		String tokenValue = java.util.UUID.randomUUID()
		AuthenticationToken authenticationToken = new AuthenticationToken(
			username: user.username,
			authToken: tokenValue
			).save(flush:true)
		
		and: "set request params"
		ProfileimageuploadController profileImageController = new ProfileimageuploadController();
		profileImageController.request.addHeader("X-Auth-Token",tokenValue)
		
		profileImageController.params.userId = user.id
				
		File testFile = new File("C:\\Users\\nisosadmin\\Downloads\\zooplaImg.png")
		println "testFile is "+testFile
		def myfile = new GrailsMockMultipartFile('myfile', testFile.bytes)
		println "file object is "+myfile
		profileImageController.request.addFile myfile
		
//		profileImageController.profileimageService = profileimageService
		
		when: "upload image action is invoked"
		profileImageController.uploadImage()
		
		then: "test success response"
		println "response is "+profileImageController.response.json
    }
}
