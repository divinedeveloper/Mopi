package com.themopi.apis

import com.themopi.exceptions.AccountException
import com.themopi.exceptions.SurveyException
import grails.converters.JSON

import org.springframework.security.access.annotation.Secured;
@Secured('permitAll')
class SurveyLikeController {

	def surveyLikeService
	def responseObject = [:]
	def index() {
	}

	def likeASurvey(){
		try{
			
			def tokenValue = request.getHeader("X-Auth-Token")
			// like/unlike a survey and adding a timeline
			def like = surveyLikeService.addLikeUnlike(request,tokenValue,params.surveyId)
			responseObject.error = false
			responseObject.resp = like
			response.setStatus(200)
			render responseObject as JSON
		}catch(AccountException e){
			log.error e.errorResponse()
			response.setStatus(e.errorResponse().status)
			render e.errorResponse()
		}catch(SurveyException e){
			log.error e.errorResponse()
			response.setStatus(e.errorResponse().status)
			render e.errorResponse()
		}
	}



	def usersLikedTheSurvey(){
		try{
			def userList = surveyLikeService.findUserWhoLikedTheSurvey(params.surveyId,params.limit,params.offset)
			responseObject.error = false
			responseObject.resp = userList
			response.setStatus(200)
			render responseObject as JSON
		}catch(SurveyException e){
			log.error e.errorResponse()
			response.setStatus(e.errorResponse().status)
			render e.errorResponse()
		}
	}


	def countLike(){
		try{
			def count = surveyLikeService.likeCountOnSurvey(params.surveyId)
			responseObject.error = false
			responseObject.resp = count
			response.setStatus(200)
			render responseObject as JSON
		}catch(SurveyException e){
			log.error e.errorResponse()
			response.setStatus(e.errorResponse().status)
			render e.errorResponse()
		}
	}
}
