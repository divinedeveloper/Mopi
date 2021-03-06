package com.themopi.apis

import java.util.Map;

import com.themopi.account.User
import com.themopi.account.UserRole
import com.themopi.exceptions.AccountException

import grails.converters.JSON
import org.springframework.security.access.annotation.Secured

@Secured('permitAll')
class UserController {
	def UserSurveyService
	def userService
	Map responseObject = [:]
    def index() { }
	
	def loggedInUserSurvey(){
		try{
		def tokenValue = request.getHeader("X-Auth-Token")
		def surveys = UserSurveyService.getMySurvey(tokenValue,params.offset,params.limit)
		responseObject.error = false
			responseObject.resp = surveys
			response.setStatus(200)
			render responseObject as JSON
		}catch(AccountException e){
			log.error e.errorResponse()
			response.setStatus(e.errorResponse().status)
			render e.errorResponse()
		}
		
	}
	
	def getUserSurveyByUserId(){
		try{
			def surveys = UserSurveyService.getUserSurveyByUserId(params.userId,params.offset,params.limit)
			responseObject.error = false
			responseObject.resp = surveys
			response.setStatus(200)
			render responseObject as JSON
		}catch(AccountException e){
			log.error e.errorResponse()
			response.setStatus(e.errorResponse().status)
			render e.errorResponse()
		}
		
	}
	
	
	def getSurveyRespondedByUser(){
		try{
		def tokenValue = request.getHeader("X-Auth-Token")
		def surveys = UserSurveyService.getSurveyRespondedByUser(tokenValue,params.userId,params.offset,params.limit)
		responseObject.error = false
			responseObject.resp = surveys
			response.setStatus(200)
			render responseObject as JSON
		}catch(AccountException e){
			log.error e.errorResponse()
			response.setStatus(e.errorResponse().status)
			render e.errorResponse()
		}
	}
	
	def updatePassword()
	{
		try{
			def tokenValue = request.getHeader("X-Auth-Token")
			println "----------request.password--"+request.getJSON().password
			println "tokenValue--"+tokenValue
			def user = userService.updateUserPassword(tokenValue,request.getJSON().password)
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
}
