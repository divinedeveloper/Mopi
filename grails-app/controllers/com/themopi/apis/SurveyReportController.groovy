package com.themopi.apis

import java.util.Map;

import com.themopi.account.User
import com.themopi.account.UserRole
import com.themopi.exceptions.SurveyException
import com.themopi.survey.Survey
import com.themopi.survey.SurveyReports


import grails.converters.*

import org.springframework.security.access.annotation.Secured;


@Secured('permitAll')
class SurveyReportController {

	def userService
	def surveyService
	Map responseObject = [:]
    def index() { }
	def creatSurveyReport()
	{
		try{
			String tokenValue = request.getHeader('X-Auth-Token')
			Survey survey = surveyService.getSurveyById(params.surveyId)
			UserRole userRole = userService.getUserAndRoleByAuthToken(tokenValue);
			User user = userRole.user
			println "user------"+user
			println "survey----"+survey
			def sre=SurveyReports.findAllByUserAndSurvey(user,survey)
			
			if(sre==null || sre.equals(null) || sre.equals("null") || sre=="null")
			{
				int errorCode = 300
				int status = 3001
				String message = "Survey is already reported by the user"
				String extendedMessage = "Survey is already reported by the user"
				String moreInfo = "Survey is already reported by the user"
				throw new SurveyException(status,errorCode,message,extendedMessage,moreInfo)
			}
			
			
			
			if(survey.status=="Draft" || survey.status.toString()=="Draft")
			{
				int errorCode = 300
				int status = 3001
				String message = "Survey is in draft mode"
				String extendedMessage = "Survey is in draft mode is not reported by any user"
				String moreInfo = "Survey is in draft mode is not reported by any user"
				throw new SurveyException(status,errorCode,message,extendedMessage,moreInfo)
				
			}
			SurveyReports surveyReports = surveyService.createSurveyReport(request,params.surveyId,tokenValue)
			if(surveyReports==null || surveyReports.equals(null) || surveyReports.equals("null") || surveyReports=="null")
			{
				responseObject.error = true
				responseObject.resp = "Only Active Survey can be reported"
			}
			else
			{
				responseObject.error = false
				responseObject.resp = surveyReports
			}
			
			response.setStatus(200)
			render responseObject as JSON
		}catch(SurveyException e){
			log.error e.errorResponse()
			response.setStatus(e.errorResponse().status)
			render e.errorResponse()
		}
	}
	/*def deleteSurveyReport()
	{
		try{
			SurveyReports survey = surveyService.deleteSurveyReport(request)
			responseObject.error = false
			responseObject.resp = survey
			response.setStatus(200)
			render responseObject as JSON
		}catch(SurveyException e){
			log.error e.errorResponse()
			response.setStatus(e.errorResponse().status)
			render e.errorResponse()
		}
	}*/
	def getSurveyReportBySurvey()
	{
		try{
			String tokenValue = request.getHeader('X-Auth-Token')
			def survey = surveyService.getSurveyReportBySurvey(params.surveyId,tokenValue)
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
}
