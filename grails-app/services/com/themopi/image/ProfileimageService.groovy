package com.themopi.image

import com.themopi.account.User
import com.themopi.account.UserRole
import com.themopi.exceptions.ImageUploadException
import com.themopi.user.Profile
import grails.transaction.Transactional

@Transactional
class ProfileimageService {
	static transactional = 'mongo'
	
	def grailsApplication
	def userService
	def surveyService
	
    def uploadImage(request,imageUrl,userId) {
		
		int errorCode = grailsApplication.config.customExceptions.gift.fourZeroNine.gift.errorCode
		int status = grailsApplication.config.customExceptions.gift.fourZeroNine.gift.status
		String message = grailsApplication.config.customExceptions.gift.fourZeroNine.gift.message
		String extendedMessage = grailsApplication.config.customExceptions.gift.fourZeroNine.gift.extendedMessage
		String moreInfo = grailsApplication.config.customExceptions.gift.fourZeroNine.gift.moreInfo
		String tokenValue = request.getHeader('X-Auth-Token')
		UserRole userRole = userService.getUserAndRoleByAuthToken(tokenValue);
		User user = User.findById(userId)
		println " user is  : " + user
		
		
		def proTemp=null
		try
		{
			if(user.profile == null)
			{
				Profile profile = new Profile()
				if(!profile.save())
				{
					profile.errors.each{
						println it
					}
				//throw new AccountException(status,errorCode,message,extendedMessage,moreInfo)
				}
			user.profile = profile
			if(!user.save()){
				profile.errors.each{
					println it
				}
			}
			}
			
			user.profile.imageUrlP=imageUrl
		}
		catch(Exception e)
		{
			
		}
		
		
		if(!user.save()){
			profile.errors.each{
				println it
			}
		}
		return imageUrl
    }
}