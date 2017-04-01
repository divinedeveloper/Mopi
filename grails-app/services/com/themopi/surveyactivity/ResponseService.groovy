package com.themopi.surveyactivity

import grails.transaction.Transactional

import com.themopi.account.User
import com.themopi.account.UserRole
import com.themopi.account.UserService
import com.themopi.exceptions.ResponseException
import com.themopi.exceptions.SurveyException
import com.themopi.survey.Question
import com.themopi.survey.Response
import com.themopi.survey.Survey
import com.themopi.survey.SurveysResponded
import com.themopi.achievements.*

@Transactional
class ResponseService {
	static transactional = 'mongo'
	
    def userService
	def surveyService
	def grailsApplication
	def pointService
	
	def surveyById(surveyId){
		Survey surveyObj = Survey.findById(surveyId)
		if(surveyObj == null){
			
			int errorCode = grailsApplication.config.customExceptions.survey.fourZeroFour.survey.errorCode
			int status = grailsApplication.config.customExceptions.survey.fourZeroFour.survey.status
			String message = grailsApplication.config.customExceptions.survey.fourZeroFour.survey.message
			String extendedMessage = grailsApplication.config.customExceptions.survey.fourZeroFour.survey.extendedMessage
			String moreInfo = grailsApplication.config.customExceptions.survey.fourZeroFour.survey.moreInfo
			throw new SurveyException(status,errorCode,message,extendedMessage,moreInfo)
		}
		
		return surveyObj
	}
	
	
	def addResponse(tokenValue,surveyId,questionId,body){
		
		UserRole userRole = userService.getUserAndRoleByAuthToken(tokenValue);
		User user = userRole?.user
		Survey surveyObj = surveyById(surveyId)
       	println "surveyObj === "+surveyObj.status
	    if(surveyObj.status.toString() != "Active" && surveyObj.status.toString() != "flagged"){
			int errorCode = grailsApplication.config.customExceptions.surveyResponse.fourZeroZero.surveyStatus.errorCode
			int status = grailsApplication.config.customExceptions.surveyResponse.fourZeroZero.surveyStatus.status
			String message = grailsApplication.config.customExceptions.surveyResponse.fourZeroZero.surveyStatus.message
			String extendedMessage = grailsApplication.config.customExceptions.surveyResponse.fourZeroZero.surveyStatus.extendedMessage
			String moreInfo = grailsApplication.config.customExceptions.surveyResponse.fourZeroZero.surveyStatus.moreInfo
			throw new ResponseException(status,errorCode,message,extendedMessage,moreInfo)
		
		}
		println questionId
		
		
		Question question = surveyService.findQuestionInSurvey(surveyId,tokenValue,questionId)
		
		checkAnswerAvailablity(question,body.answer)
		Response responseDet =findResponseByQuestionAndUser(question,user)
		if(responseDet != null){
			int errorCode = grailsApplication.config.customExceptions.surveyResponse.fourZeroThree.responseexists.errorCode
			int status = grailsApplication.config.customExceptions.surveyResponse.fourZeroThree.responseexists.status
			String message = grailsApplication.config.customExceptions.surveyResponse.fourZeroThree.responseexists.message
			String extendedMessage = grailsApplication.config.customExceptions.surveyResponse.fourZeroThree.responseexists.extendedMessage
			String moreInfo = grailsApplication.config.customExceptions.surveyResponse.fourZeroThree.responseexists.moreInfo
			throw new ResponseException(status,errorCode,message,extendedMessage,moreInfo)
		}
		Response responseObj  = new Response(
			answerForQuestion:question,
			answerForSurvey:surveyObj,
			answerBy:user,
			answer:body.answer,
			user:user,
			question:question,
			survey:surveyObj
			)
		if(!responseObj.save(flush:true)){
			responseObj.errors.each{
			println it
			int errorCode = grailsApplication.config.customExceptions.surveyResponse.fourZeroFour.surveyResponse.errorCode
			int status = grailsApplication.config.customExceptions.surveyResponse.fourZeroFour.surveyResponse.status
			String message = grailsApplication.config.customExceptions.surveyResponse.fourZeroFour.surveyResponse.message
			String extendedMessage = grailsApplication.config.customExceptions.surveyResponse.fourZeroFour.surveyResponse.extendedMessage
			String moreInfo = grailsApplication.config.customExceptions.surveyResponse.fourZeroFour.surveyResponse.moreInfo
			throw new ResponseException(status,errorCode,message,extendedMessage,moreInfo)
			}
		}else{
			def listResponse = findResponseBySurveyAndUser(surveyObj,user)
			if(listResponse.size() == 1){
				surveyObj?.respCount = (surveyObj?.respCount == null || surveyObj?.respCount == 0)?1: surveyObj?.respCount+1
				surveyObj.save()
				
				SurveysResponded surveyResponded = new SurveysResponded(
						user:user,
						survey:surveyObj
					)
				surveyResponded.save()
				
			}
		}
		
		pointService.answeringASurvey(user,surveyObj)
		return responseObj
	}
	
	def checkAnswerAvailablity(question,answer){
		List option = question.option
		if(question.questionType.toString() == "binary"){
			if(question.option.contains(answer) == false){
				int errorCode = grailsApplication.config.customExceptions.surveyResponse.fourZeroZero.answerNotAvailable.errorCode
				int status = grailsApplication.config.customExceptions.surveyResponse.fourZeroZero.answerNotAvailable.status
				String message = grailsApplication.config.customExceptions.surveyResponse.fourZeroZero.answerNotAvailable.message
				String extendedMessage = grailsApplication.config.customExceptions.surveyResponse.fourZeroZero.answerNotAvailable.extendedMessage
				String moreInfo = grailsApplication.config.customExceptions.surveyResponse.fourZeroZero.answerNotAvailable.moreInfo
				throw new ResponseException(status,errorCode,message,extendedMessage,moreInfo)
		
			}
		}else if(question.questionType.toString() == "multiplechoice"){
		    boolean available = false
		    question.options.each{
				if(it.value == answer){
					available = true
				}
			}
			if(available == false){
				int errorCode = grailsApplication.config.customExceptions.surveyResponse.fourZeroZero.answerNotAvailable.errorCode
				int status = grailsApplication.config.customExceptions.surveyResponse.fourZeroZero.answerNotAvailable.status
				String message = grailsApplication.config.customExceptions.surveyResponse.fourZeroZero.answerNotAvailable.message
				String extendedMessage = grailsApplication.config.customExceptions.surveyResponse.fourZeroZero.answerNotAvailable.extendedMessage
				String moreInfo = grailsApplication.config.customExceptions.surveyResponse.fourZeroZero.answerNotAvailable.moreInfo
				throw new ResponseException(status,errorCode,message,extendedMessage,moreInfo)
		
			}
		}else if(question.questionType.toString() == "scale"){
	      List possibleAnswer = ["1","2","3","4","5","6","7","8","9","10"]
		  println possibleAnswer
		  println possibleAnswer.getClass()
		      if(possibleAnswer.contains(answer) == false){
				  int errorCode = grailsApplication.config.customExceptions.surveyResponse.fourZeroZero.answerNotAvailable.errorCode
				  int status = grailsApplication.config.customExceptions.surveyResponse.fourZeroZero.answerNotAvailable.status
				  String message = grailsApplication.config.customExceptions.surveyResponse.fourZeroZero.answerNotAvailable.message
				  String extendedMessage = grailsApplication.config.customExceptions.surveyResponse.fourZeroZero.answerNotAvailable.extendedMessage
				  String moreInfo = grailsApplication.config.customExceptions.surveyResponse.fourZeroZero.answerNotAvailable.moreInfo
				  throw new ResponseException(status,errorCode,message,extendedMessage,moreInfo)
		
			  }
		}
	}
	
	def findResponseByQuestionAndUser(question,user){
		Response responseDet = Response.findByQuestionAndUser(question,user)
		return responseDet
	}
	
	def getAllResponseByUserAndSurveyId(surveyId,userId){
		Survey surveyObj = surveyById(surveyId)
		User user  = userService.findUserByUserId(userId)
		return findResponseBySurveyAndUser(surveyObj,user)
	}
	
	def findResponseBySurveyAndUser(survey,user){
		List responseDet = Response.findAllBySurveyAndUser(survey,user)
		return responseDet
	}
	
	
	def getGraph(questionId,surveyId,chartType){
		def survey = surveyService.getSurveyById(surveyId)
		def question = surveyService.findQuestionInSurvey(surveyId,null,questionId)
		
		println "Survey ============= "+survey
		if(chartType == "age"){
			return resultByAge(questionId)
		}else if(chartType == "gender"){
			return resultByGender(questionId)
		}else if(chartType == "occupation"){
			return resultByOccupation(questionId)
		}else if(chartType == "income"){
			return resultByIncomeLevel(questionId)
		}else if(chartType == "no"){ //no is for text type question
			return resultOfCommentQuestion(questionId)
		}else if(chartType == "overall"){
			def match=['$match':['question':questionId.toLong()]]
			def group=['$group':['_id':'$answer',count: ['$sum': 1]]]
			def surveys = Response.collection.aggregate(match,group)
			return ["error":false,'result':surveys.commandResult.result]
		}else {
			def match=['$match':['question':questionId.toLong()]]
			def group=['$group':['_id':'$answer',count: ['$sum': 1]]]
			def surveys = Response.collection.aggregate(match,group)
			return ["error":false,'result':surveys.commandResult.result]
		}
	}
	def resultByIncomeLevel(questionId){
		def match=['$match':['question':questionId.toLong()]]
		def group=['$group':['_id':['user':'$answerBy']]]
		def group2 = ['$group':['_id':'$_id.user.profile.incomeLevel',count: ['$sum': 1]]]
		def surveys = Response.collection.aggregate(match,group,group2)
		return ["error":false,'result':surveys.commandResult.result]
		}
	
	def resultByOccupation(questionId){
		def match=['$match':['question':questionId.toLong()]]
		def group=['$group':['_id':['user':'$answerBy']]]
		def group2 = ['$group':['_id':'$_id.user.profile.occupation',count: ['$sum': 1]]]
		def surveys = Response.collection.aggregate(match,group,group2)
		return ["error":false,'result':surveys.commandResult.result]
	}
	
	def resultByGender(questionId){
		def match=['$match':['question':questionId.toLong()]]
		def group=['$group':['_id':['user':'$answerBy']]]
		def group2 = ['$group':['_id':'$_id.user.profile.gender',count: ['$sum': 1]]]
		def surveys = Response.collection.aggregate(match,group,group2)
		return ["error":false,'result':surveys.commandResult.result]
	}
	
	def resultByAge(questionId){
		def match=['$match':['question':questionId.toLong()]]
		def group=['$group':['_id':['user':'$answerBy']]]
		def group2 = ['$group':['_id':'$_id.user.profile.age',count: ['$sum': 1]]]
		def surveys = Response.collection.aggregate(match,group,group2)
		return ["error":false,'result':surveys.commandResult.result]
	}
	
	def resultOfCommentQuestion(questionId){
		Question question = Question.findById(questionId.toLong())
		println "question === "+question?.questionType
		
		if(question?.questionType?.toString() == 'text'){
			List responseList  = Response.findAllByQuestion(question)
			return ["error":false,'result':responseList]
		}else{
			int errorCode = grailsApplication.config.customExceptions.surveyResponse.fourZeroZero.commentQuestion.errorCode
			int status = grailsApplication.config.customExceptions.surveyResponse.fourZeroZero.commentQuestion.status
			String message = grailsApplication.config.customExceptions.surveyResponse.fourZeroZero.commentQuestion.message
			String extendedMessage = grailsApplication.config.customExceptions.surveyResponse.fourZeroZero.commentQuestion.extendedMessage
			String moreInfo = grailsApplication.config.customExceptions.surveyResponse.fourZeroZero.commentQuestion.moreInfo
			throw new ResponseException(status,errorCode,message,extendedMessage,moreInfo)
		}
	}
    def serviceMethod() {

    }
}
