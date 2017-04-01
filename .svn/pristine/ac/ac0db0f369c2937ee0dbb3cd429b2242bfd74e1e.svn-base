package com.themopi.apis.security

import grails.converters.JSON
import grails.plugin.springsecurity.ui.*

import org.codehaus.groovy.grails.web.json.JSONObject
import org.springframework.security.access.annotation.Secured

import com.themopi.account.*
import com.themopi.exceptions.AccountException;
import com.themopi.usermanagement.*


@Secured('permitAll')
class RegistrationController  {
	static scope = "prototype"
	
	def userService
	def responseObject = [:]
	def grailsApplication
	
	
	/*
	 * this function is use to create user account 
	 * 
	 */
	def signUp() {
		try{
			println "check in"
			JSONObject userParams = request.getJSON()
			println params.providerString
			log.debug(request.getJSON())
			User user = userService.addUser(request.getJSON(),request.getHeader("X-Auth-Token"))
			//send email to receive gifts
			/*if(user != null){
				userService.sendGiftVerificationToken(user)
			}*/
			responseObject.error = false
			responseObject.resp = user
			response.setStatus(200)
			render responseObject as JSON
		}catch(AccountException e){
			log.error e.errorResponse()
			response.setStatus(e.errorResponse().status)
			render e.errorResponse()
		}
	}
	
	
	/*
	 * this function is use to delete a particular user using userId
	 */
	def deleteUser(){
		try{
			log.debug params.userId
			userService.deleteUser(params.userId)
			response.setStatus(204)
			responseObject.resp = "User Deleted Successfully"
			responseObject.error = false
			responseObject.status = 204
			render responseObject as JSON
		}catch(AccountException e){
		  log.error e.errorResponse();
		  response.setStatus(e.errorResponse().status)
		  render e.errorResponse()
		}
	}
	
	
	/*
	 * function to Update Account details .
	 * Profile , User and Address
	 * accpets userId as Params and Json PayLoad for Update as body
	 */
	def updateUser(){
		try{
			User user = userService.updateUser(params.userId,request.getJSON(),request.getHeader("X-Auth-Token"))
			responseObject.error = false
			responseObject.resp = user
			response.setStatus(200)
			render responseObject as JSON
		}catch(AccountException e){
			log.error e.errorResponse();
			response.setStatus(e.errorResponse().status)
			render e.errorResponse()
		}
	}
	
	
	/*
	 * this function returns user 
	 * accepts userId as params
	 */
	def findUser(){
		try{
			User user = userService.findUserByUserId(params.userId)
			responseObject.error = false
			responseObject.resp = user
			response.setStatus(200)
			render responseObject as JSON
			
		}catch(AccountException e){
			log.error e.errorResponse();
			response.setStatus(e.errorResponse().status)
			render e.errorResponse()
		}
	}
	
	
	/*
	 * function returns roles available in the system
	 */
	def role() {
//		List roles = Role.findAll()
		println "-----------role admin------------"
		List roles = Role.findAllByAuthorityNotEqual("ROLE_SUPERADMIN")
		render roles as JSON
	}
	/**
	 * 
	 * @return loggedIn user details
	 */
	def findLoggedInUser(){
		try{
			def userRole = userService.getUserAndRoleByAuthToken(request.getHeader("X-Auth-Token"))
			responseObject.error = false
			responseObject.resp = userRole.user
			responseObject.role = userRole.role
			response.setStatus(200)
			render responseObject as JSON
			
		}catch(AccountException e){
			log.error e.errorResponse();
			response.setStatus(e.errorResponse().status)
			render e.errorResponse()
		}
		
	}
	
	
    def index() { 
		JSONObject json = new JSONObject();
		render json
	}
	
	def confirmUserForGift(){
		try{
			userService.userEligibleForGift(params.giftVerificationToken)
			responseObject.error = false
			response.setStatus(200)
			render responseObject as JSON
			
		}catch(AccountException e){
			log.error e.errorResponse();
			response.setStatus(e.errorResponse().status)
			render e.errorResponse()
		}
	}
	
  def verifyRegistration(){
	  try{
		  def success = userService.verifyRegistration(params.token)
		  if(success){
			  redirect(uri: grailsApplication.config.myapp.baseUrl+"/#/login")
		  }
	  }catch(AccountException e){
	  	log.error e.errorResponse();
		  response.setStatus(e.errorResponse().status)
		  render e.errorResponse()
	  }
  }
  
  def updateStatus(){
	  try{
		  def tokenValue  =  request.getHeader("X-Auth-Token")
//		  def statusUpdated = userService.updateStatus(tokenValue,request.getJSON(),params.userId)
		  User user = userService.updateStatus(tokenValue,request.getJSON(),params.userId)
//		  println "statusUpdated is "+statusUpdated
		  responseObject.error = false
		  responseObject.resp = user
		  response.setStatus(200)
		  render responseObject as JSON
		  
	  }catch(AccountException e){
	  	  log.error e.errorResponse();
		  response.setStatus(e.errorResponse().status)
		  render e.errorResponse()
	  }
  }
  
}


