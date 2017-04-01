package com.themopi.gift.api

import com.themopi.account.User
import com.themopi.account.UserRole
import com.themopi.exceptions.GiftException
import com.themopi.exceptions.SurveyException
import com.themopi.survey.Gift
import com.themopi.survey.Survey
import grails.transaction.Transactional

@Transactional
class GiftService {

	def grailsApplication
	def userService
	def surveyService
	
    /*def serviceMethod() {

    }*/
	def createGift(request, surveyId, giftId) {
		
		int errorCode = grailsApplication.config.customExceptions.gift.fourZeroNine.gift.errorCode
		int status = grailsApplication.config.customExceptions.gift.fourZeroNine.gift.status
		String message = grailsApplication.config.customExceptions.gift.fourZeroNine.gift.message
		String extendedMessage = grailsApplication.config.customExceptions.gift.fourZeroNine.gift.extendedMessage
		String moreInfo = grailsApplication.config.customExceptions.gift.fourZeroNine.gift.moreInfo
		String tokenValue = request.getHeader('X-Auth-Token')
		UserRole userRole = userService.getUserAndRoleByAuthToken(tokenValue);
		Survey survey = surveyService.getSurveyById(surveyId)
		
		if("Active".equals(survey?.status.toString()) || "flagged".equals(survey?.status.toString())){
			
			errorCode = grailsApplication.config.customExceptions.gift.fourZeroThree.gift.errorCode
			status = grailsApplication.config.customExceptions.gift.fourZeroThree.gift.status
			message = grailsApplication.config.customExceptions.gift.fourZeroThree.gift.message
			extendedMessage = grailsApplication.config.customExceptions.gift.fourZeroThree.gift.extendedMessage
			moreInfo = grailsApplication.config.customExceptions.gift.fourZeroThree.gift.moreInfo
			
			throw new GiftException(status,errorCode,message,extendedMessage,moreInfo)
		}
		
		
		if(request.getJSON().code == null || request.getJSON().code == ""){
			throw new GiftException(status,errorCode,message,extendedMessage,moreInfo)
		}
		Gift gift
		if(giftId==1)
		{
			gift = Gift.findById(giftId)
			gift.delete()
			deleteGiftFromSurvey(survey)
			gift=null
		}
		else
		{
			if(giftId==0)
			{
				gift = new Gift(
					text: request.getJSON().text,
					user:userRole.user,
					code:request.getJSON().code,
					maxNo:request.getJSON().isNull('maxNo')?null:request.getJSON().maxNo,
					isAvailable: true,
					)
				
			} 
			else
			{
				gift = Gift.findById(giftId)
				gift.text = request.getJSON().text
				gift.user=userRole.user
				gift.code=request.getJSON().code
				gift.maxNo=request.getJSON().isNull('maxNo')?null:request.getJSON().maxNo
			}
			
			if(!gift.save()){
				gift.errors.each{ println it}
				throw new GiftException(status,errorCode,message,extendedMessage,moreInfo)
			}else{
			
				
					assignGiftToSurvey(survey,gift)
				
			}
		}
		return gift
	}
	
	def assignGiftToSurvey(survey,gift){
		try
		{
			survey.gift = gift
			survey.save()
		}
		catch(SurveyException sv)
		{
			
		}
	}
	def deleteGiftFromSurvey(survey) {
		try
		{
			survey.gift = ""
			survey.save()
		}
		catch(SurveyException sv)
		{
			
		}
	}
	def deleteGiftsAction(request,surveyId,giftId) {
		println "---in service deleteGift------"
		println request
		println surveyId
		println giftId
		int errorCode = grailsApplication.config.customExceptions.gift.fourZeroFour.gift.errorCode
		int status = grailsApplication.config.customExceptions.gift.fourZeroFour.gift.status
		String message = grailsApplication.config.customExceptions.gift.fourZeroFour.gift.message
		String extendedMessage = grailsApplication.config.customExceptions.gift.fourZeroFour.gift.extendedMessage
		String moreInfo = grailsApplication.config.customExceptions.gift.fourZeroFour.gift.moreInfo
		String tokenValue = request.getHeader('X-Auth-Token')
		println "---tokenValue---"+tokenValue
		UserRole userRole = userService.getUserAndRoleByAuthToken(tokenValue);
		println "---userRole---"+userRole
		User user = userRole.user
		println "---user---"+user
		Survey survey = surveyService.getSurveyById(surveyId)
		println "---survey---"+survey
		Gift gift = Gift.findById(giftId)
		println "---gift---"+gift
		if(survey == null || gift == null || user == null){
			throw new GiftException(status,errorCode,message,extendedMessage,moreInfo)
		}
		println "-------checking all -----------"
		try
		{
			survey.gift = null
			survey.save()
			
			gift.delete()
			
			return "Gift Deleted"
			
		}
		catch(SurveyException sv)
		{
			return "Gift Not Deleted"
		}
		catch(Exception e)
		{
			throw new GiftException(status,errorCode,message,extendedMessage,moreInfo)
		}
	}
	
	/**
	 * function to get Gift By ID
	 *
	 * @param giftId
	 * @return
	 */

	def getGiftById(giftId){
		println "-----------giftId------------"
		println giftId
		Gift gift = Gift.findById(giftId)
		println gift
		if(gift == null){
			int errorCode = grailsApplication.config.customExceptions.gift.fourZeroFour.gift.errorCode
			int status = grailsApplication.config.customExceptions.gift.fourZeroFour.gift.status
			String message = grailsApplication.config.customExceptions.gift.fourZeroFour.gift.message
			String extendedMessage = grailsApplication.config.customExceptions.gift.fourZeroFour.gift.extendedMessage
			String moreInfo = grailsApplication.config.customExceptions.gift.fourZeroFour.gift.moreInfo

			throw new GiftException(status,errorCode,message,extendedMessage,moreInfo)
		}
		return gift
	}
}
