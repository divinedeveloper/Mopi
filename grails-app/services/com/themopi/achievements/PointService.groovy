package com.themopi.achievements

import grails.transaction.Transactional
import com.themopi.account.User
@Transactional
class PointService {

	def responseService 
	def badgeService
	def addPoints(userObject,points){
		if(userObject != null){
			userObject?.profile?.points = (userObject?.profile?.points == null)?points:userObject?.profile?.points+points
			if(!userObject?.save()){
				userObject.errors.each{
					log.error it
				}
			}else{
				badgeService.assignBadgeName(userObject)
			}
		}
	}
	
	def postingASurvey(user,survey){
		User userObject = user
		addPoints(userObject,3)
	}
	
	def answeringASurvey(user,survey){
		def responseList = responseService.findResponseBySurveyAndUser(survey,user)
		if(responseList.size() == 1){
			addPoints(user,2)
		}
		if(responseList.size() == survey?.questions?.size()){
			println "gift =========== "+survey.gift
			if(survey.gift != null && user.isEligibleForGift == true && ((survey?.gift?.maxNo > survey.giftDistributed )|| (survey?.gift == null))){
				def giftList = user.gift
				giftList.add(survey.gift)
				user.gift = giftList
				
				survey.giftDistributed = (survey.giftDistributed == null || survey.giftDistributed == 0)?1:survey.giftDistributed+1
				survey.save()
				if(!user.save()){
					user.errors.each{
						println it
					}
				}
			}
		}
		if(survey?.user?.roleName == "ROLE_SPONSEREDADMIN"){
			if(responseList.size() == survey?.questions?.size()){
				addPoints(user,3)
				if(survey.gift != null){
					def giftList = user.gift
					giftList.add(survey.gift)
					user.gift = giftList
					user.save()
				}
			}
		}
	}
	
	def followingAUser(user){
		addPoints(user,1)
	}
	
	
	def allowAUserToFollow(user){
		addPoints(user,2)
	}
	
	
    def serviceMethod() {

    }
}
