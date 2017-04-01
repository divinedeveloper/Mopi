package com.themopi.surveyactivity

import grails.transaction.Transactional
import com.themopi.account.*
import com.themopi.exceptions.SurveyException
import com.themopi.survey.SurveyLikes

@Transactional
class SurveyLikeService {
	static transactional = 'mongo'
	
	def userService
	def surveyService
	def timelineService
	def addLikeUnlike(request,tokenValue,surveyId){
		def user = userService. getUserAndRoleByAuthToken(tokenValue)?.user
		def survey = surveyService.getSurveyById(surveyId)
		def tempuser = survey.user
		def surveyLikeObj = SurveyLikes.findByUserAndSurvey(user,survey)
		if(surveyLikeObj != null){
			surveyLikeObj.delete(flush:true)
			request.getJSON().action="Unlike"
			println "request.action---"+request.getJSON().action
			timelineService.createCommentSurveyTimeline(request,tempuser.id,surveyId)
			return ['like':false]
		}
		SurveyLikes surveyLikes = new SurveyLikes(
			likedBy:user,
			likedOn:survey,
			user:user,
			survey:survey
			)
		if(!surveyLikes.save()){
			surveyLikes.errors.each{
				log.error it
			}
			int errorCode = grailsApplication.config.customExceptions.survey.fourZeroFour.like.errorCode
			int status = grailsApplication.config.customExceptions.survey.fourZeroFour.like.status
			String message = grailsApplication.config.customExceptions.survey.fourZeroFour.like.message
			String extendedMessage = grailsApplication.config.customExceptions.survey.fourZeroFour.like.extendedMessage
			String moreInfo = grailsApplication.config.customExceptions.survey.fourZeroFour.like.moreInfo

			throw new SurveyException(status,errorCode,message,extendedMessage,moreInfo)
		}
		//creating timeline for like a survey
		timelineService.createCommentSurveyTimeline(request,tempuser.id,surveyId)
		return ['like':true]
	}
	
	
	def findUserWhoLikedTheSurvey(surveyId,limit,offset){
		def survey = surveyService.getSurveyById(surveyId)
		def surveyLikesList = SurveyLikes.findAllBySurvey(survey,['max':limit,'offset':offset])
		def surveyLikeCount = SurveyLikes.countBySurvey(survey)
		def userList = []
		if(surveyLikesList.size() != 0){
			userList = surveyLikesList.user
		}
		
		return ['users':userList,'total':surveyLikeCount]
	}
	
	
	def likeCountOnSurvey(surveyId){
		def survey = surveyService.getSurveyById(surveyId)
		def surveyLikeCount = SurveyLikes.countBySurvey(survey)
		return surveyLikeCount
	}
	
    def serviceMethod() {

    }
}
