package com.themopi.apis.security



import com.themopi.apis.SurveyController
import com.themopi.apis.GiftController;
import com.themopi.survey.Survey
import com.themopi.utils.GenerateDataUtils
import spock.lang.*




/**
 *
 */
class GiftControllerIntegrationSpec extends Specification {

	
	def setup() {
		println "----c----"
	}

	def cleanup() {
		println "----c--vv--"
	}
	
   /* void "test valid addGift"()
	{
		given: "login controller"
		//get auth token to send in request header
		*//**
		 * here authToken hard coded 
		 * further we put auth token from calling auth token
		 *//*
		
		String accessToken = "d34l1gfteuenjmapg2m9d5qu4kvn3hq7"
		SurveyController surveyController = new SurveyController();
		String surveyName = new GenerateDataUtils().generateSurveyName();
		
		surveyController.request.addHeader("contentType","application/json")
		surveyController.request.addHeader("X-Auth-Token","d34l1gfteuenjmapg2m9d5qu4kvn3hq7")
		
		
		
		surveyController.request.content = '''{"name":"gifted survey",
												"visiblity":true,
											    "startTime":"2014-11-29",
											    "endTime":"2014-11-30",
											    "visibleTo":"EveryOne",
											    "status":"Inactive",
											    "cityList":["India","Pakistan"],
											    "countryList":["Mumbai","Delhi"]
											    }'''
		when: "create survey action is invoked"
		surveyController.add()
		println "survey created-------------"
		then: "testing error response from create controller"
		def resultSurvey = surveyController.response.json
		println "result is "+resultSurvey
		def giftController = new GiftController()
		giftController.params.surveyId=resultSurvey.id
		giftController.params.giftId=0
		giftController.request.addHeader("contentType","application/json")
		giftController.request.addHeader("X-Auth-Token","d34l1gfteuenjmapg2m9d5qu4kvn3hq7")
		giftController.request.content = '''{"code":"SUN1ww002",
											 "imageUrl":"ABC/i.png",
											 "type":"ABC",
											 "text":"ABC",
											 "maxNo":10}'''
		
		
		  when: "addGift action is invoked"
		 //giftController.addGift()
		  println "gift created-------------"
		  then: "testing success response from gift controller"
		 // def result = giftController.response.json
			//println "survey checked create or not --"+result
	}*/
}
