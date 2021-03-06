package com.themopi.surveyactivity

import com.themopi.account.User
import com.themopi.account.UserRole
import com.themopi.survey.*
import grails.transaction.Transactional

@Transactional
class UserSurveyService {
	static transactional = 'mongo'
	
	def userService
	/**
	 * Get surveys posted by logedInUser
	 * @param tokenValue
	 * @param limit
	 * @param offset
	 * @return
	 */
	
	def getMySurvey(tokenValue,offset,limit){
		UserRole userRole = userService.getUserAndRoleByAuthToken(tokenValue);
		def count = Survey.countByUser(userRole?.user)
		if(offset == "" || limit == ""){
			List surveys = Survey.findAllByUser(userRole?.user)
			return ["results":surveys,"count":count]
		}else{
			List surveys = Survey.findAllByUser(userRole?.user,[max:limit,offset:offset])
			return ["results":surveys,"count":count]
		}
	}
	
	
	/**
	 * Find the Surveys on which user have responded
	 * @param tokenValue
	 * @param offset
	 * @param limit
	 * @return
	 */
	def getSurveyRespondedByUser(tokenValue,userId,offset,limit){
		User user = userService.findUserByUserId(userId);
		if(offset == "" || limit == ""){
			def surveys = SurveysResponded.findAllByUser(user,[max:10,offset:0])
			def count = SurveysResponded.countByUser(user)
			return ["results":surveys,"count":count]
		}else{
		def surveys = SurveysResponded.findAllByUser(user,[max:limit,offset:offset])
		def count = SurveysResponded.countByUser(user)
		return ["results":surveys,"count":count]
		}
	}
	

	/**
	 * get survey posted by user using userId
	 * @param userId
	 * @param limit
	 * @param offset
	 * @return
	 */
	def getUserSurveyByUserId(userId,offset,limit){
		User user  = userService.findUserByUserId(userId)
		List surveys = Survey.findAllByUser(user,[max:limit,offset:offset])
		def  count = Survey.countByUser(user)
		return ["results":surveys,"count":count]
	}
	
	
    def serviceMethod() {

    }
}
