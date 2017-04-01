package com.themopi.apis

import java.util.Map;

import grails.converters.*

import org.springframework.security.access.annotation.Secured

import com.themopi.account.User
import com.themopi.account.UserRole
import com.themopi.exceptions.GiftException
import com.themopi.exceptions.SurveyException
import com.themopi.survey.Gift

@Secured('permitAll')
class GiftController {

	def giftService
	Map responseObject = [:]
	def userService
	
	
	def addGift(){
		try{
			
			int i=0
			Gift gift = giftService.createGift(request,params.surveyId,i)
			responseObject.error = false
			responseObject.resp = gift
			response.setStatus(200)
			render responseObject as JSON
		}catch(SurveyException e){
			log.error e.errorResponse()
			response.setStatus(e.errorResponse().status)
			render e.errorResponse()
		}
		catch(GiftException e){
			log.error e.errorResponse()
			response.setStatus(e.errorResponse().status)
			render e.errorResponse()
		}
	}
	
	def updateGift(){
		try{
			
			Gift gift = giftService.createGift(request,params.surveyId,params.giftId)
			responseObject.error = false
			responseObject.resp = gift
			response.setStatus(200)
			render responseObject as JSON
		}catch(SurveyException e){
			log.error e.errorResponse()
			response.setStatus(e.errorResponse().status)
			render e.errorResponse()
		}
		catch(GiftException e){
			log.error e.errorResponse()
			response.setStatus(e.errorResponse().status)
			render e.errorResponse()
		}
	}
	def deleteGift(){
		try{
			
			/*println params.surveyId
			println params.giftId*/
			def gift = giftService.deleteGiftsAction(request,params.surveyId,params.giftId)
			println "return get gift--"+gift
			responseObject.error = false
			responseObject.resp = gift
			response.setStatus(200)
			render responseObject as JSON
		}catch(SurveyException es){
			println "-----SurveyException----"+es
			log.error es.errorResponse()
			response.setStatus(es.errorResponse().status)
			render es.errorResponse()
		}
		catch(GiftException eg){
			println "-----GiftException----"+eg
			log.error eg.errorResponse()
			response.setStatus(eg.errorResponse().status)
			render eg.errorResponse()
		}
	}
}
