package com.themopi.apis.security


import com.themopi.account.User
import com.themopi.exceptions.AccountException
import org.codehaus.groovy.grails.web.json.JSONObject
import org.springframework.security.access.annotation.Secured


@Secured('permitAll')
class ForgotPasswordController {
	/*
	 * To set new password if user has forgot password 	 
	 */
	
	def userService
	def forgotPasswordService
	def grailsApplication
		

	/**
	 * This api takes and checks if user email is available in Database. If it exists it creates a verification token and sends it to user
	 * @return
	 */
	def forgotPassword(){
		try{
			String email = params.email
			log.debug(email)
			User user = userService.findUserByEmail(email)
			log.debug(user)

			String verificationToken = UUID.randomUUID()
			forgotPasswordService.saveVerificationToken(email, verificationToken, user)

			String link = grailsApplication.config.myapp.baseUrl + grailsApplication.config.myapp.forgotPassword + verificationToken
			log.debug(link)
//			String subject = "Forgot password mail"
			forgotPasswordService.sendMailToUser(email, link)
			response.status=200;
			JSONObject json = new JSONObject();
			json.put("status",200)
			json.put("error",false)
			render json
		}catch(AccountException e){
			log.error e.errorResponse()
			response.status = e.errorResponse().status
			render e.errorResponse();
		}
	}
	
	
	def verifyToken(){
		log.debug params
		log.debug params.verificationToken
	}
	
	/**
	 * This method will take and match verification token and if token matches new password  will be saved for that user
	 * @return
	 */
	def changePassword(){
		try{
			JSONObject userParams = request.getJSON()
//			log.debug(userParams)
			userService.findUserByVerificationToken(userParams)
			JSONObject json = new JSONObject();
			json.put("status",200)
			json.put("error",false)
			response.setStatus(200)
			render json
			
		}catch(AccountException e){
			log.error e.errorResponse()
			response.status = e.errorResponse().status
			render e.errorResponse();
		}
		
	}
}
