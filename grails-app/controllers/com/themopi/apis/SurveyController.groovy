package com.themopi.apis


import com.themopi.account.User
import com.themopi.account.UserRole
import com.themopi.exceptions.SurveyException
import com.themopi.survey.Question
import com.themopi.survey.Survey
import com.themopi.survey.SurveyReports
import com.themopi.surveyactivity.*
import grails.converters.*


import org.springframework.security.access.annotation.Secured;
import org.springframework.web.multipart.MultipartHttpServletRequest
import org.springframework.web.multipart.commons.CommonsMultipartFile

@Secured('permitAll')
class SurveyController {
	def surveyService
	Map responseObject = [:]
	def userService
	def grailsApplication
	
	def index() { }

	
	/**
	 * Action is use to create survey. 
	 * Accepts Json playload to create survey
	 * @return survey object
	 */
	@Secured(['ROLE_SPONSEREDADMIN', 'ROLE_USER'])
	def add(){
		try{
			Survey survey = surveyService.createSurvey(request)
			responseObject.error = false
			responseObject.resp = survey
			response.setStatus(200)
			render responseObject as JSON
			return
		}catch(SurveyException e){
			log.error e.errorResponse()
			response.setStatus(e.errorResponse().status)
			render e.errorResponse()
			return
		}
	}

	/**
	 * Action is use to update survey.
	 * Accepts Json playload to create survey
	 * @return survey object
	 */
	@Secured(['ROLE_SPONSEREDADMIN', 'ROLE_USER'])
	def update(){
		try{
			Survey survey = surveyService.updateSurvey(request,params.surveyId)
			responseObject.error = false
			responseObject.resp = survey
			response.setStatus(200)
			render responseObject as JSON
		}catch(SurveyException e){
			log.error e.errorResponse()
			response.setStatus(e.errorResponse().status)
			render e.errorResponse()
		}
	}


	/**
	 * Action is use to update survey status only.
	 * Accepts Json playload to update survey status (surveyId and status)
	 * @return survey object
	 */
	def updateStatus(){
		try{
			String tokenValue = request.getHeader('X-Auth-Token')
			Survey survey = surveyService.updateSurveyStatus(request.getJSON(),params.surveyId, tokenValue)
			responseObject.error = false
			responseObject.resp = survey
			response.setStatus(200)
			render responseObject as JSON
		}catch(SurveyException e){
			log.error e.errorResponse()
			response.setStatus(e.errorResponse().status)
			render e.errorResponse()
		}
	}

	/**
	 * Action to return survey requested
	 * @return requested Survey 
	 */
	def getSurvey(){
		try{
			Survey survey = surveyService.getSurveyById(params.surveyId)
			responseObject.error = false
			responseObject.resp = survey
			response.setStatus(200)
			render responseObject as JSON
		}catch(SurveyException e){
			log.error e.errorResponse()
			response.setStatus(e.errorResponse().status)
			render e.errorResponse()
		}
	}

	/**
	 * Action to add questions to the survey
	 * @return
	 */
	@Secured(['ROLE_SPONSEREDADMIN', 'ROLE_USER'])
	def addQuestions(){
		try{
			
			println "addQuestions"
			String tokenValue = request.getHeader('X-Auth-Token')
			def questions = surveyService.addQuestionToSurvey(request.getJSON(),params.surveyId,tokenValue)
			responseObject.error = false
			responseObject.resp = questions
			response.setStatus(200)
			render responseObject as JSON
		}catch(SurveyException e){
			log.error e.errorResponse()
			response.setStatus(e.errorResponse().status)
			render e.errorResponse()
		}
	}

	/**
	 * Action to add all questions to the survey
	 * Also used to edit questions
	 * @return
	 */
	@Secured(['ROLE_SPONSEREDADMIN', 'ROLE_USER'])
	def addAllQuestions(){
		try{
			println "addQuestions"
			String tokenValue = request.getHeader('X-Auth-Token')
			def questions = surveyService.addAllQuestionToSurvey(request.getJSON(),params.surveyId,tokenValue)
			responseObject.error = false
			responseObject.resp = questions
			response.setStatus(200)
			render responseObject as JSON
		}catch(SurveyException e){
			log.error e.errorResponse()
			response.setStatus(e.errorResponse().status)
			render e.errorResponse()
		}
	}
	
	/**
	 * Action to edit a particular question of a Survey
	 * @return
	 */
	@Secured(['ROLE_SPONSEREDADMIN', 'ROLE_USER'])
	def editQuestion(){
		
		try{
			String tokenValue = request.getHeader('X-Auth-Token')
			def questions = surveyService.editQuestionInSurvey(request.getJSON(),params.surveyId,tokenValue,params.questionId)
			responseObject.error = false
			responseObject.resp = questions
			response.setStatus(200)
			render responseObject as JSON
		}catch(SurveyException e){
			log.error e.errorResponse()
			response.setStatus(e.errorResponse().status)
			render e.errorResponse()
		}
	}

	/**
	 * Action to delete a particular question of a Survey
	 * @return
	 */
	def deleteQuestion(){
		
		try{
			String tokenValue = request.getHeader('X-Auth-Token')
			def questions = surveyService.deleteQuestionInSurvey(params.surveyId,tokenValue,params.questionId)
			responseObject.error = false
			responseObject.resp = questions
			response.setStatus(200)
			render responseObject as JSON
		}catch(SurveyException e){
			log.error e.errorResponse()
			response.setStatus(e.errorResponse().status)
			render e.errorResponse()
		}
	}

	/**
	 * 
	 * Action to get All Survey
	 */

	def allSurvey(){
		try{
		List surveys = surveyService.findAllSurvey(params.offset,params.limit);
		responseObject.error = false
		responseObject.resp = surveys
		responseObject.count = Survey.count()
		response.setStatus(200)
		render responseObject as JSON
		}catch(SurveyException e){
		log.error e.errorResponse()
		response.setStatus(e.errorResponse().status)
		render e.errorResponse()
	 }
	}
	
	/**
	 * 
	 * 
	 */
	def deleteSurvey(){
		try{
			String tokenValue = request.getHeader('X-Auth-Token')
				surveyService.deleteSurvey(params.surveyId, tokenValue)
				responseObject.error = false
				responseObject.resp = "Survey Delete Successfully"
				response.setStatus(201)
				render responseObject as JSON
			}catch(SurveyException e){
				log.error e.errorResponse()
				response.setStatus(e.errorResponse().status)
				render e.errorResponse()
			}
	}
	
	
	def findQuestion(){
		try{
			String tokenValue = request.getHeader('X-Auth-Token')
			Question questions = surveyService.findQuestionInSurvey(params.surveyId,tokenValue,params.questionId)
			responseObject.error = false
			responseObject.resp = questions
			response.setStatus(200)
			render responseObject as JSON
		}catch(SurveyException e){
			log.error e.errorResponse()
			response.setStatus(e.errorResponse().status)
			render e.errorResponse()
		}
	}
	
	
	
}
