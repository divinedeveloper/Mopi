package com.themopi.apis

import com.themopi.exceptions.AccountException
import com.themopi.exceptions.ResponseException
import com.themopi.exceptions.SurveyException
import grails.converters.JSON

import org.codehaus.groovy.grails.web.json.JSONArray
import org.springframework.security.access.annotation.Secured

@Secured('permitAll')
class ResponseController {
	def responseService
	def responseObject = [:]
	def index() {
	}

	/**
	 * @PARAMS surveyId and userId
	 * @return returns responses of user on a particular survey
	 */
	def getAllResponseByUserAndSurveyId(){
		try{
			def responseObj = responseService.getAllResponseByUserAndSurveyId(params.surveyId,params.userId)
			responseObject.error = false
			responseObject.resp = responseObj
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
		}catch(ResponseException e){
			log.error e.errorResponse()
			response.setStatus(e.errorResponse().status)
			render e.errorResponse()
		}

	}

	/**
	 * @params surveyId,questionId
	 * @JSON_PAYLOAD {"answer":"<answer value>"}
	 * save the response of user for a question and a survey 
	 * @return the response object
	 */
	
	def addResponse(){
		try{
			String tokenValue = request.getHeader("X-Auth-Token")
			def responseObj  = responseService.addResponse(tokenValue,params.surveyId,params.questionId,request.getJSON())
			responseObject.error = false
			responseObject.resp = responseObj
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
		}catch(ResponseException e){
			log.error e.errorResponse()
			response.setStatus(e.errorResponse().status)
			render e.errorResponse()
		}
	}
	
	/**
	 * @params questionId,surveyId , chartType = <"age/gender/occupation/income/no >
	 * @return graph data for question based on user property age , sex , occupation and income
	 * it also returns general graph data based on answer if no charType value is provided
	 * chartType = no is provided in case of comment question 
	 */
	def getQuestionResultChart(){
		try{
			def result  = responseService.getGraph(params.questionId,params.surveyId,params.chartType)
			println "result === "+result
			response.setStatus(200)
			render result as JSON
		}catch(ResponseException e){
			log.error e.errorResponse()
			response.setStatus(e.errorResponse().status)
			render e.errorResponse()
		}catch(SurveyException e){
			log.error e.errorResponse()
			response.setStatus(e.errorResponse().status)
			render e.errorResponse()
		}
	}
	
	
}
