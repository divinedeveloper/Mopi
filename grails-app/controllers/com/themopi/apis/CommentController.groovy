package com.themopi.apis

import grails.converters.*

import org.springframework.security.access.annotation.Secured

import com.themopi.account.User
import com.themopi.account.UserRole
import com.themopi.exceptions.CommentException
import com.themopi.exceptions.SurveyException
import com.themopi.survey.Comment
import com.themopi.survey.Survey

@Secured('permitAll')
class CommentController {
	
	def commentService
	def surveyService
	def userService
	Map responseObject = [:]

    /**
     * method is to create a comment
     * takes json payload of comment data
     * returns commnet object as json	
     * @return
     */
	
	def addComment(){
		try{
			//Create a comment and also create  timeline
			Comment comment = commentService.createComment(request,params.surveyId)
			responseObject.error = false
			responseObject.resp = comment
			response.setStatus(200)
			render responseObject as JSON
		}catch(SurveyException e){
			log.error e.errorResponse()
			response.setStatus(e.errorResponse().status)
			render e.errorResponse()
		}
		catch(CommentException e){
			log.error e.errorResponse()
			response.setStatus(e.errorResponse().status)
			render e.errorResponse()
		}
	}
	/**
	 * returns all comments for a particular survey
	 * with pagination
	 * along with total comment count on a survey
	 * @return
	 */
	def getAllComments(){
		try{
			Survey survey = surveyService.getSurveyById(params.surveyId)
			def comments =  commentService.findAllComments(survey, params.offset, params.limit, params.flag)

			responseObject.error = false
			responseObject.resp = comments
			response.setStatus(200)
			render responseObject as JSON
		}catch(SurveyException e){
			log.error e.errorResponse()
			response.setStatus(e.errorResponse().status)
			render e.errorResponse()
		}
	}
	
	/**
	 * only super admin and admin can
	 * hide particular comment which are flagged based on comment id 
	 * @return
	 */
	def hideComment(){
		try{
			println params
			println params.activity
			commentService.hideCommentById(request.getHeader("X-Auth-Token"), request.getJSON().flaggedCommentId,params.surveyId)
			responseObject.error = false
			responseObject.resp = "success"
			response.setStatus(200)
			render responseObject as JSON
		}catch(CommentException e){
			log.error e.errorResponse()
			response.setStatus(e.errorResponse().status)
			render e.errorResponse()
		}
	}
	
	/**
	 * report a particular comment based on id
	 * if comment report count greater than 10
	 * autohide that comment
	 * @return
	 */
	def reportComment(){
		try{
			commentService.reportCommentById(request.getHeader("X-Auth-Token"), params.surveyId, params.commentId)
			responseObject.error = false
			responseObject.resp = "success"
			response.setStatus(200)
			render responseObject as JSON
		}catch(SurveyException e){
			log.error e.errorResponse()
			response.setStatus(e.errorResponse().status)
			render e.errorResponse()
		}
		catch(CommentException e){
			log.error e.errorResponse()
			response.setStatus(e.errorResponse().status)
			render e.errorResponse()
		}
	}
	
	
}
