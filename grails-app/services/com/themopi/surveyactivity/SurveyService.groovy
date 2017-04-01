package com.themopi.surveyactivity

import grails.transaction.Transactional

import java.text.Format
import java.text.SimpleDateFormat
import org.codehaus.groovy.grails.web.json.JSONArray


import com.themopi.account.*
import com.themopi.exceptions.SurveyException
import com.themopi.location.City
import com.themopi.location.Country
import com.themopi.location.State
import com.themopi.survey.Gift
import com.themopi.survey.HashTag
import com.themopi.survey.Options
import com.themopi.survey.Question
import com.themopi.survey.Survey
import com.themopi.survey.*
import com.themopi.utils.IDUtils
import com.themopi.achievements.*

@Transactional
class SurveyService {
	static transactional = 'mongo'

	def userService
	def hashTagService
	def grailsApplication
	def locationService
	def pointService

	IDUtils idGenrator = new IDUtils();
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");


	def serviceMethod() {
	}


	/**
	 * function to find the survey type for survey
	 *
	 * @param userRole
	 * @param body (json payload)
	 * @return (survey type sponsered/nonsponsered)
	 */

	def findSurveyType(userRole,body){
		def surveyType = body.surveyType
		println "surveyType  "+surveyType
		if((surveyType != null)){
			println "in func"+surveyType
			return surveyType
		}
		String survey = "sponsered"
		println survey
		if(userRole?.role?.authority == "ROLE_SPONSEREDADMIN"){
			println survey
			return survey
		}
		survey = "nonsponsered"
		println survey
		return survey
	}

	/**
	 * function accepts the request from the request
	 * Extract the X-Auth-Token from the header
	 * Determine the type of the Survey
	 * Create new survey
	 * @param request
	 * @return survey object
	 */
	def createSurvey(request){
		int errorCode = grailsApplication.config.customExceptions.survey.fourZeroZero.survey.errorCode
		int status = grailsApplication.config.customExceptions.survey.fourZeroZero.survey.status
		String message = grailsApplication.config.customExceptions.survey.fourZeroZero.survey.message
		String extendedMessage = grailsApplication.config.customExceptions.survey.fourZeroZero.survey.extendedMessage
		String moreInfo = grailsApplication.config.customExceptions.survey.fourZeroZero.survey.moreInfo
		if(request.getJSON().name  == null || request.getJSON().name == ""){
			throw new SurveyException(status,errorCode,message,extendedMessage,moreInfo)
		}
		String tokenValue = request.getHeader('X-Auth-Token')
		UserRole userRole = userService.getUserAndRoleByAuthToken(tokenValue);
		User user=userRole.user
		println "Profile Complete == "+user?.address?.homeTown
		if(user?.address?.homeTown == null){
			errorCode = grailsApplication.config.customExceptions.account.fourZeroZero.profileNotComplete.errorCode
			status = grailsApplication.config.customExceptions.account.fourZeroZero.profileNotComplete.status
			message = grailsApplication.config.customExceptions.account.fourZeroZero.profileNotComplete.message
			extendedMessage = grailsApplication.config.customExceptions.account.fourZeroZero.profileNotComplete.extendedMessage
			moreInfo = grailsApplication.config.customExceptions.account.fourZeroZero.profileNotComplete.moreInfo

			throw new SurveyException(status,errorCode,message,extendedMessage,moreInfo)
		}
		def surveyCode = idGenrator.generateID()+"-"+userRole?.user?.name;
		/*if(user.isActive==false)
		 {
		 redirect(uri: grailsApplication.config.myapp.baseUrl+"/#/login")
		 }*/
		String surveyType = findSurveyType(userRole,request.getJSON())
		def individualArray = request.getJSON().individualArray;
		println surveyType+"            "+request.getJSON()
		println "role == "+userRole.user
		def countryObjectList = request.getJSON().countryList
		if(request.getJSON().isNull('countryList')){
			countryObjectList = []
		}
		//sponsered            [countryWithLocation:[[name:India, longitude:77, latitude:20]], tags:[[text:ajay]], cityWithLocation:[[name:Chennai, state:Tamil Nadu, long:80.22522779999997, lat:13.0597049, country:India]], tempclist:[], cityList:[Chennai], visibleTo:Followers, countryList:[India], visibility:false, name:cbjbvx nz, countrytag:[[region:Asia, text:India, callingCode:[91], capital:New Delhi, altSpellings:[IN, BhÄ�rat, Republic of India, Bharat Ganrajya], currency:[INR], languages:[hin:Hindi, eng:English], subregion:Southern Asia, nativeLanguage:hin, area:3287590, cca2:IN, cca3:IND, name:[native:[common:à¤­à¤¾à¤°à¤¤, official:à¤­ï¿½ï¿½ï¿½à¤°à¤¤ à¤—à¤£à¤°à¤¾à¤œà¥�à¤¯], common:India, official:Republic of India], latlng:[20, 77], translations:[deu:Indien, rus:Ð˜Ð½Ð´Ð¸Ñ�, ita:India, hrv:Indija, fra:Inde, jpn:ã‚¤ãƒ³ãƒ‰, spa:India, nld:India], relevance:3, ccn3:356, longitude:77, demonym:Indian, latitude:20, tld:[.in], borders:[AFG, BGD, BTN, MMR, CHN, NPL, PAK, LKA]]], hashTags:[ajay]]

		JSONArray jsonArrayCountryObject = request.getJSON().countryWithLocation
		JSONArray jsonArrayCityObject = request.getJSON().cityWithLocation

		//if country latitude and longitude doesnt exist throw error starts

		jsonArrayCountryObject.each{

			/*if(it.longitude == null && it.latitude == null){
			 println "throw exception 1"
			 //throw exception
			 }*/

			if(it.isNull('longitude') && it.isNull('latitude')){

				errorCode = grailsApplication.config.customExceptions.survey.fourZeroZero.invalidCountry.errorCode
				status = grailsApplication.config.customExceptions.survey.fourZeroZero.invalidCountry.status
				message = grailsApplication.config.customExceptions.survey.fourZeroZero.invalidCountry.message
				extendedMessage = grailsApplication.config.customExceptions.survey.fourZeroZero.invalidCountry.extendedMessage
				moreInfo = grailsApplication.config.customExceptions.survey.fourZeroZero.invalidCountry.moreInfo

				throw new SurveyException(status,errorCode,message,extendedMessage,moreInfo)

			}

		}

		//if country latitude and longitude doesnt exist throw error ends

		//if city latitude and longitude doesnt exist throw error starts

		jsonArrayCityObject.each{

			/*if(it.longitude == null && it.latitude == null){
			 println "throw exception 1"
			 //throw exception
			 }*/

			if(it.isNull('longitude') && it.isNull('latitude')){

				errorCode = grailsApplication.config.customExceptions.survey.fourZeroZero.invalidCity.errorCode
				status = grailsApplication.config.customExceptions.survey.fourZeroZero.invalidCity.status
				message = grailsApplication.config.customExceptions.survey.fourZeroZero.invalidCity.message
				extendedMessage = grailsApplication.config.customExceptions.survey.fourZeroZero.invalidCity.extendedMessage
				moreInfo = grailsApplication.config.customExceptions.survey.fourZeroZero.invalidCity.moreInfo

				throw new SurveyException(status,errorCode,message,extendedMessage,moreInfo)

			}

		}

		//if city latitude and longitude doesnt exist throw error ends

		def cityObjectList = request.getJSON().cityList
		if(request.getJSON().isNull('cityList')){
			cityObjectList = []
		}
		println " request.getJSON()  --- "+ request.getJSON().imageUrl

		// validate start and end dates
		String stringStartDate = (request.getJSON().isNull('startTime'))?null:request.getJSON().startTime
		String stringEndDate = (request.getJSON().isNull('endTime'))?null:request.getJSON().endTime

		boolean flag = validateStartAndEndDate(stringStartDate,  stringEndDate)



		Survey survey = new Survey(
				name:request.getJSON().name,
				surveyType:surveyType,
				surveyCode:surveyCode,
				lastUpdatedBy:userRole.user,
				status:"Draft",
				visibility:(request.getJSON().isNull('visiblity'))?true:request.getJSON().visiblity , //need to handle visiblity option later
				startTime:(request.getJSON().isNull('startTime'))?null:formatter.parse(request.getJSON().startTime),
				endTime:(request.getJSON().isNull('endTime'))?null:formatter.parse(request.getJSON().endTime),
				user:userRole.user,
				dateCreated: new Date(),
				//	visiblityCountry:countryObjectList,
				//visiblityCity:cityObjectList,
				visibleTo:(request.getJSON().isNull('visibleTo'))?"EveryOne":request.getJSON().visibleTo
				)
		if(request.getJSON().visibleTo == "Individual"){
			survey = addIndividualsToWhomSurveyIsVisible(survey,individualArray)
		}

		def hashTags  = request.getJSON().hashTags


		if(!survey.save()){
			survey.errors.each{ println "ERROR IN SURVEY CREATION == "+ it; }

			throw new SurveyException(status,errorCode,message,extendedMessage,moreInfo)
		}else{
			incrementSurveyCount(userRole.user)
		}
		if(hashTags == null){
			hashTags = []
		}
		hashTags.add(surveyCode)
		if(hashTags != null && hashTags.size() != 0){
			
			survey = getHashTagAddedToSurvey(hashTags,userRole,survey);
			survey.save()
			updateSurveyLocationAvailablityCity(survey,jsonArrayCityObject)
		}
		pointService.postingASurvey(userRole.user,survey)

		updateSurveyLocationAvailablityCity(survey,jsonArrayCityObject )
		updateSurveyLocationAvailablityCountry(survey,jsonArrayCountryObject)
		return survey
	}


	def addIndividualsToWhomSurveyIsVisible(survey,individualArray){
		def individualsToWhomSurveyIsVisible = []
		individualArray.each{
			User user= User.findById(it)
			individualsToWhomSurveyIsVisible.add(user)
		}
		survey.individuals = individualsToWhomSurveyIsVisible
		return survey
	}
	/**
	 * function accepts the request from the request
	 * Extract the X-Auth-Token from the header
	 * Determine the type of the Survey
	 * update survey onlyif it is in draft mode
	 * @param request
	 * @return survey object
	 */
	def updateSurvey(request,surveyId){
		Survey survey = getSurveyById(surveyId)

		println "survey---+survey"+request.getJSON()
		def individualArray = request.getJSON().individualArray;
		String tokenValue = request.getHeader('X-Auth-Token')
		UserRole userRole = userService.getUserAndRoleByAuthToken(tokenValue);

		//check owner of survey
		boolean isOwner = checkOwnerOfSurvey(survey, userRole)

		if(!isOwner){
			println "check owner"
			int errorCode = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.errorCode
			int status = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.status
			String message = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.message
			String extendedMessage = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.extendedMessage
			String moreInfo = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.moreInfo

			throw new SurveyException(status,errorCode,message,extendedMessage,moreInfo)
		}
		//if("Draft".equals(survey?.status.toString()) && survey.isEditable == true){

		if(survey.isEditable == true){

			if(survey.questions.size == 0 && request.getJSON().status == "Active"){
				int errorCode = grailsApplication.config.customExceptions.survey.fourZeroZero.surveyActiveStatus.errorCode
				int status = grailsApplication.config.customExceptions.survey.fourZeroZero.surveyActiveStatus.status
				String message = grailsApplication.config.customExceptions.survey.fourZeroZero.surveyActiveStatus.message
				String extendedMessage = grailsApplication.config.customExceptions.survey.fourZeroZero.surveyActiveStatus.extendedMessage
				String moreInfo = grailsApplication.config.customExceptions.survey.fourZeroZero.surveyActiveStatus.moreInfo

				throw new SurveyException(status,errorCode,message,extendedMessage,moreInfo)
			}

			if(request.getJSON().name  == null || request.getJSON().name == ""){
				int errorCode = grailsApplication.config.customExceptions.survey.fourZeroZero.survey.errorCode
				int status = grailsApplication.config.customExceptions.survey.fourZeroZero.survey.status
				String message = grailsApplication.config.customExceptions.survey.fourZeroZero.survey.message
				String extendedMessage = grailsApplication.config.customExceptions.survey.fourZeroZero.survey.extendedMessage
				String moreInfo = grailsApplication.config.customExceptions.survey.fourZeroZero.survey.moreInfo

				throw new SurveyException(status,errorCode,message,extendedMessage,moreInfo)
			}

			String surveyType = findSurveyType(userRole,request.getJSON())

			def countryObjectList = request.getJSON().countryList
			if(request.getJSON().isNull('countryList')){
				countryObjectList = []
			}

			def cityObjectList = request.getJSON().cityList
			if(request.getJSON().isNull('cityList')){
				cityObjectList = []
			}


			survey.name=request.getJSON().name
			survey.surveyType=surveyType
			survey.lastUpdatedBy=userRole.user
			survey.status= request.getJSON().status
			survey.visibility = (request.getJSON().isNull('visibility'))?true:request.getJSON().visibility //need to handle visiblity option later
			survey.startTime = (request.getJSON().isNull('startTime'))?null:formatter.parse(request.getJSON().startTime)
			survey.endTime = (request.getJSON().isNull('endTime'))?null:formatter.parse(request.getJSON().endTime)
			survey.visibleTo= (request.getJSON().isNull('visibleTo'))?"EveryOne":request.getJSON().visibleTo

			if(request.getJSON().visibleTo == "Individual"){
				survey = addIndividualsToWhomSurveyIsVisible(survey,individualArray)
			}
			if(survey.gift==null)
			{
				survey.gift = survey.gift
			}
			else
			{
				survey.gift.id=survey.gift.id
				survey.gift.code=survey.gift.code
				survey.gift.isAvailable=survey.gift.isAvailable
				survey.gift.maxNo=survey.gift.maxNo
				survey.gift.text=survey.gift.text
				survey.gift.user=survey.user

			}
			def hashTags  = request.getJSON().hashTags
			if(hashTags != null && hashTags.size() != 0){
				survey = getHashTagAddedToSurvey(hashTags,userRole,survey);
			}

			if(!survey.save()){
				survey.errors.each{ println  it; }
				throw new SurveyException(status,errorCode,message,extendedMessage,moreInfo)
			}
			JSONArray jsonArrayCountryObject = request.getJSON().countryWithLocation
			JSONArray jsonArrayCityObject = request.getJSON().cityWithLocation

			updateSurveyLocationAvailablityCity(survey,jsonArrayCityObject )
			updateSurveyLocationAvailablityCountry(survey,jsonArrayCountryObject)
			return survey


		}else if("Active".equals(survey?.status.toString()) || "flagged".equals(survey?.status.toString())){
			survey = updateOnlyHashtag(request,survey, userRole)

			return survey

		}else{
			int errorCode = grailsApplication.config.customExceptions.survey.fourZeroThree.survey.errorCode
			int status = grailsApplication.config.customExceptions.survey.fourZeroThree.survey.status
			String message = grailsApplication.config.customExceptions.survey.fourZeroThree.survey.message
			String extendedMessage = grailsApplication.config.customExceptions.survey.fourZeroThree.survey.extendedMessage
			String moreInfo = grailsApplication.config.customExceptions.survey.fourZeroThree.survey.moreInfo

			throw new SurveyException(status,errorCode,message,extendedMessage,moreInfo)
		}
	}
	/**
	 * function to update the status of the Survey
	 *status cannot be changed from other than draft mode back to draft mode
	 * @param body
	 * @param surveyId
	 * @return
	 */

	def updateSurveyStatus(body,surveyId,tokenValue){
		Survey survey = getSurveyById(surveyId)

		UserRole userRole = userService.getUserAndRoleByAuthToken(tokenValue);
		boolean isOwner = checkOwnerOfSurvey(survey, userRole)

		if(!isOwner){
			if(!(userRole?.user?.roleAuthority == "ROLE_SUPERADMIN" || userRole?.user?.roleAuthority == "ROLE_ADMIN")){
				int errorCode = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.errorCode
				int status = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.status
				String message = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.message
				String extendedMessage = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.extendedMessage
				String moreInfo = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.moreInfo

				throw new SurveyException(status,errorCode,message,extendedMessage,moreInfo)

			}
		}

		//cannot change status of survey from active and any other state back to draft mode
		if(!("Draft".equals(survey?.status.toString())) && body.status == "Draft"){
			int errorCode = grailsApplication.config.customExceptions.survey.fourZeroThree.surveyActiveStatus.errorCode
			int status = grailsApplication.config.customExceptions.survey.fourZeroThree.surveyActiveStatus.status
			String message = grailsApplication.config.customExceptions.survey.fourZeroThree.surveyActiveStatus.message
			String extendedMessage = grailsApplication.config.customExceptions.survey.fourZeroThree.surveyActiveStatus.extendedMessage
			String moreInfo = grailsApplication.config.customExceptions.survey.fourZeroThree.surveyActiveStatus.moreInfo

			throw new SurveyException(status,errorCode,message,extendedMessage,moreInfo)
		}

		if(survey.questions.size == 0 && body.status == "Active"){
			int errorCode = grailsApplication.config.customExceptions.survey.fourZeroZero.surveyActiveStatus.errorCode
			int status = grailsApplication.config.customExceptions.survey.fourZeroZero.surveyActiveStatus.status
			String message = grailsApplication.config.customExceptions.survey.fourZeroZero.surveyActiveStatus.message
			String extendedMessage = grailsApplication.config.customExceptions.survey.fourZeroZero.surveyActiveStatus.extendedMessage
			String moreInfo = grailsApplication.config.customExceptions.survey.fourZeroZero.surveyActiveStatus.moreInfo

			throw new SurveyException(status,errorCode,message,extendedMessage,moreInfo)
		}
		if(body.status == "Active" && survey.isEditable == true ){
			survey.isEditable = false
		}

		survey.status = body.status
		survey.save()
		return survey
	}

	/**
	 * function to get SurveyByID
	 *
	 * @param surveyId
	 * @return
	 */

	def getSurveyById(surveyId){
		println "-----------surveyId------------"
		println surveyId
		Survey survey = Survey.findById(surveyId)
		println survey
		if(survey == null){
			int errorCode = grailsApplication.config.customExceptions.survey.fourZeroFour.survey.errorCode
			int status = grailsApplication.config.customExceptions.survey.fourZeroFour.survey.status
			String message = grailsApplication.config.customExceptions.survey.fourZeroFour.survey.message
			String extendedMessage = grailsApplication.config.customExceptions.survey.fourZeroFour.survey.extendedMessage
			String moreInfo = grailsApplication.config.customExceptions.survey.fourZeroFour.survey.moreInfo

			throw new SurveyException(status,errorCode,message,extendedMessage,moreInfo)
		}
		return survey
	}

	/**
	 * Add hashtags to the survey
	 *
	 * @param hashTags
	 * @param userRole
	 * @param survey
	 * @return
	 */
	public Survey getHashTagAddedToSurvey(hashTags,userRole,survey){
		def hashTagList = []
		//hashTags.add(survey.surveyCode)
		for(int i = 0;i < hashTags.size();i++){
			HashTag hashTag = HashTag.findByName(hashTags[i]);
			println hashTag
			if(hashTag == null){
				hashTag = hashTagService.createHashTag(hashTags[i],userRole.user)

			}
			hashTagList.add(hashTag)
		}
		println hashTagList
		survey.hashTag = hashTagList
		return survey

		/*	for(int i = 0;i < hashTags.size();i++){
		 HashTag hashTag = HashTag.findByName(hashTags[i]);
		 if(hashTag == null){
		 hashTag = hashTagService.createHashTag(hashTags[i],userRole.user)
		 }
		 println hashTag
		 SurveyHashTag  surveyHashTag = SurveyHashTag.findBySurveyAndHashTag(survey,hashTag)
		 println survey
		 println hashTag
		 if(surveyHashTag == null){
		 SurveyHashTag	surveyHashTagNew = new SurveyHashTag(
		 survey:survey,
		 hashTag:hashTag
		 )
		 if(!surveyHashTagNew.save()){
		 surveyHashTagNew.errors.each{ println it }
		 }
		 }*/

	}

	def checkActionAllowedOnlyIfSurveyIsNotActive(survey){
		if("Active".equals(survey?.status.toString())){
			int errorCode = grailsApplication.config.customExceptions.survey.fourZeroThree.survey.errorCode
			int status = grailsApplication.config.customExceptions.survey.fourZeroThree.survey.status
			String message = grailsApplication.config.customExceptions.survey.fourZeroThree.survey.message
			String extendedMessage = grailsApplication.config.customExceptions.survey.fourZeroThree.survey.extendedMessage
			String moreInfo = grailsApplication.config.customExceptions.survey.fourZeroThree.survey.moreInfo

			throw new SurveyException(status,errorCode,message,extendedMessage,moreInfo)
		}
	}

	/**
	 *
	 * Function to add question to a Survey
	 * @param body (Json Payload)
	 * @param surveyId
	 * @param tokenValue
	 * @return
	 */
	def addQuestionToSurvey(body,surveyId,tokenValue){
		JSONArray options
		def opts = []
		Survey survey = getSurveyById(surveyId)
		checkActionAllowedOnlyIfSurveyIsNotActive(survey)

		UserRole userRole = userService.getUserAndRoleByAuthToken(tokenValue);

		//check owner of survey
		boolean isOwner = checkOwnerOfSurvey(survey, userRole)

		if(!isOwner){
			int errorCode = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.errorCode
			int status = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.status
			String message = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.message
			String extendedMessage = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.extendedMessage
			String moreInfo = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.moreInfo

			throw new SurveyException(status,errorCode,message,extendedMessage,moreInfo)
		}

		if(survey.questions.size() == 15){
			int errorCode = grailsApplication.config.customExceptions.survey.fourZeroZero.surveyQuestionCount.errorCode
			int status = grailsApplication.config.customExceptions.survey.fourZeroZero.surveyQuestionCount.status
			String message = grailsApplication.config.customExceptions.survey.fourZeroZero.surveyQuestionCount.message
			String extendedMessage = grailsApplication.config.customExceptions.survey.fourZeroZero.surveyQuestionCount.extendedMessage
			String moreInfo = grailsApplication.config.customExceptions.survey.fourZeroZero.surveyQuestionCount.moreInfo

			throw new SurveyException(status,errorCode,message,extendedMessage,moreInfo)
		}
		
		//We have to change exception error message into draft mode
		
		if(survey.status=="Draft" || survey.status.toString()=="Draft"){
		}
		else
		{
			println "---------checking survey is active----------"
			int errorCode = grailsApplication.config.customExceptions.survey.fourZeroZero.surveyQuestionCount.errorCode
			int status = grailsApplication.config.customExceptions.survey.fourZeroZero.surveyQuestionCount.status
			String message = grailsApplication.config.customExceptions.survey.fourZeroZero.surveyQuestionCount.message
			String extendedMessage = grailsApplication.config.customExceptions.survey.fourZeroZero.surveyQuestionCount.extendedMessage
			String moreInfo = grailsApplication.config.customExceptions.survey.fourZeroZero.surveyQuestionCount.moreInfo

			throw new SurveyException(status,errorCode,message,extendedMessage,moreInfo)
		}
		def option = []
		//myapp.baseUrl = "http://1.186.127.199:5800/mopi"
		//myapp.tempImageFolderPath="/Users/nisostech/Development/images"
		
		if(body.isNull("query") || body.query == ""){
			
			int errorCode = grailsApplication.config.customExceptions.survey.fourZeroZero.QuestionQuery.errorCode
			int status = grailsApplication.config.customExceptions.survey.fourZeroZero.QuestionQuery.status
			String message = grailsApplication.config.customExceptions.survey.fourZeroZero.QuestionQuery.message
			String extendedMessage = grailsApplication.config.customExceptions.survey.fourZeroZero.QuestionQuery.extendedMessage
			String moreInfo = grailsApplication.config.customExceptions.survey.fourZeroZero.QuestionQuery.moreInfo

			throw new SurveyException(status,errorCode,message,extendedMessage,moreInfo)
		}
		
		if(body.questionType == "binary"){
			option = body.option

			if(body.option.size() < 2){
				int errorCode = grailsApplication.config.customExceptions.survey.fourZeroZero.multipleChoice.errorCode
				int status = grailsApplication.config.customExceptions.survey.fourZeroZero.multipleChoice.status
				String message = grailsApplication.config.customExceptions.survey.fourZeroZero.multipleChoice.message
				String extendedMessage = grailsApplication.config.customExceptions.survey.fourZeroZero.multipleChoice.extendedMessage
				String moreInfo = grailsApplication.config.customExceptions.survey.fourZeroZero.multipleChoice.moreInfo

				throw new SurveyException(status,errorCode,message,extendedMessage,moreInfo)
			}
		}else if(body.questionType == "scale") {
			option = body.option
			if(body.option.size() < 2){
				int errorCode = grailsApplication.config.customExceptions.survey.fourZeroZero.multipleChoice.errorCode
				int status = grailsApplication.config.customExceptions.survey.fourZeroZero.multipleChoice.status
				String message = grailsApplication.config.customExceptions.survey.fourZeroZero.multipleChoice.message
				String extendedMessage = grailsApplication.config.customExceptions.survey.fourZeroZero.multipleChoice.extendedMessage
				String moreInfo = grailsApplication.config.customExceptions.survey.fourZeroZero.multipleChoice.moreInfo

				throw new SurveyException(status,errorCode,message,extendedMessage,moreInfo)
			}
		}else if(body.questionType == "multiplechoice"){
			options = body.option
			println "======== ============="+option.getClass()+body.option.getClass()

			options.each{
				println "it ====== "+it.id.getClass()
				Options opt = new Options(
						id :it?.id,
						value : it?.value
						)
				opts.add(opt)
			}
			println "opts ==== "+opts
			if(body.option.size() < 2 || body.option.size() > 6){
				int errorCode = grailsApplication.config.customExceptions.survey.fourZeroZero.multipleChoiceOption.errorCode
				int status = grailsApplication.config.customExceptions.survey.fourZeroZero.multipleChoiceOption.status
				String message = grailsApplication.config.customExceptions.survey.fourZeroZero.multipleChoiceOption.message
				String extendedMessage = grailsApplication.config.customExceptions.survey.fourZeroZero.multipleChoiceOption.extendedMessage
				String moreInfo = grailsApplication.config.customExceptions.survey.fourZeroZero.multipleChoiceOption.moreInfo

				throw new SurveyException(status,errorCode,message,extendedMessage,moreInfo)
			}
		}

		Question question = new Question(
				query:body.query,
				questionType:body.questionType,
				lastUpdatedBy:userRole.user,
				option:body.option,
				options:opts,
				isActive:(body.isNull("isActive")||body.isActive == "")?true:body.isActive
				)

		if(!question.save()){
			question.errors.each{ println it }
		}else{
			println "question save === "+question.findAll()
			survey.questions.add(question)
			survey.save()
			if(!survey.save()){
				survey.errors.each{ log.error it }
			}else{
				println "Survey is saved"
			}
		}

		return question
	}

	//Also used to update add all question
	/**
	 *
	 * Function to add question to a Survey
	 * @param body (Json Payload)
	 * @param surveyId
	 * @param tokenValue
	 * @return
	 */
	def addAllQuestionToSurvey(requestBody,surveyId,tokenValue){
		
		def questionList = []
		Survey survey = getSurveyById(surveyId)
		checkActionAllowedOnlyIfSurveyIsNotActive(survey)

		UserRole userRole = userService.getUserAndRoleByAuthToken(tokenValue);

		//check owner of survey
		boolean isOwner = checkOwnerOfSurvey(survey, userRole)

		if(!isOwner){
			int errorCode = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.errorCode
			int status = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.status
			String message = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.message
			String extendedMessage = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.extendedMessage
			String moreInfo = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.moreInfo

			throw new SurveyException(status,errorCode,message,extendedMessage,moreInfo)
		}
		def bodyList = requestBody.questions
		//if(body?.size() == 15)

		if(bodyList?.size() == 15){
			int errorCode = grailsApplication.config.customExceptions.survey.fourZeroZero.surveyQuestionCount.errorCode
			int status = grailsApplication.config.customExceptions.survey.fourZeroZero.surveyQuestionCount.status
			String message = grailsApplication.config.customExceptions.survey.fourZeroZero.surveyQuestionCount.message
			String extendedMessage = grailsApplication.config.customExceptions.survey.fourZeroZero.surveyQuestionCount.extendedMessage
			String moreInfo = grailsApplication.config.customExceptions.survey.fourZeroZero.surveyQuestionCount.moreInfo

			throw new SurveyException(status,errorCode,message,extendedMessage,moreInfo)
		}
		if(survey.status=="Draft" || survey.status.toString()=="Draft"){
		}
		else
		{
			println "---------checking survey is active----------"
			int errorCode = grailsApplication.config.customExceptions.survey.fourZeroZero.surveyQuestionCount.errorCode
			int status = grailsApplication.config.customExceptions.survey.fourZeroZero.surveyQuestionCount.status
			String message = grailsApplication.config.customExceptions.survey.fourZeroZero.surveyQuestionCount.message
			String extendedMessage = grailsApplication.config.customExceptions.survey.fourZeroZero.surveyQuestionCount.extendedMessage
			String moreInfo = grailsApplication.config.customExceptions.survey.fourZeroZero.surveyQuestionCount.moreInfo

			throw new SurveyException(status,errorCode,message,extendedMessage,moreInfo)
		}
		println "Body ============== "+bodyList
		def option = []
		bodyList.each{
		def body  = it
		JSONArray options
		def opts = []
		println "Body ============== "+body
		
		if(body.isNull("query") || body.query == ""){
			
			int errorCode = grailsApplication.config.customExceptions.survey.fourZeroZero.QuestionQuery.errorCode
			int status = grailsApplication.config.customExceptions.survey.fourZeroZero.QuestionQuery.status
			String message = grailsApplication.config.customExceptions.survey.fourZeroZero.QuestionQuery.message
			String extendedMessage = grailsApplication.config.customExceptions.survey.fourZeroZero.QuestionQuery.extendedMessage
			String moreInfo = grailsApplication.config.customExceptions.survey.fourZeroZero.QuestionQuery.moreInfo

			throw new SurveyException(status,errorCode,message,extendedMessage,moreInfo)
		}
		
		if(body.questionType == "binary"){
			
			option = body.option

			if(body.option.size() < 2){
				int errorCode = grailsApplication.config.customExceptions.survey.fourZeroZero.multipleChoice.errorCode
				int status = grailsApplication.config.customExceptions.survey.fourZeroZero.multipleChoice.status
				String message = grailsApplication.config.customExceptions.survey.fourZeroZero.multipleChoice.message
				String extendedMessage = grailsApplication.config.customExceptions.survey.fourZeroZero.multipleChoice.extendedMessage
				String moreInfo = grailsApplication.config.customExceptions.survey.fourZeroZero.multipleChoice.moreInfo

				throw new SurveyException(status,errorCode,message,extendedMessage,moreInfo)
			}
		}else if(body.questionType == "scale") {
			option = body.option
			if(body.option.size() < 2){
				int errorCode = grailsApplication.config.customExceptions.survey.fourZeroZero.multipleChoice.errorCode
				int status = grailsApplication.config.customExceptions.survey.fourZeroZero.multipleChoice.status
				String message = grailsApplication.config.customExceptions.survey.fourZeroZero.multipleChoice.message
				String extendedMessage = grailsApplication.config.customExceptions.survey.fourZeroZero.multipleChoice.extendedMessage
				String moreInfo = grailsApplication.config.customExceptions.survey.fourZeroZero.multipleChoice.moreInfo

				throw new SurveyException(status,errorCode,message,extendedMessage,moreInfo)
			}
		}else if(body.questionType == "multiplechoice"){
			options = body.option
			options.each{
				Options opt = new Options(
						id :it?.id,
						value : it?.value
						)
				opts.add(opt)
			}
			println "opts ==== "+opts
			if(body.option.size() < 2 || body.option.size() > 6){
				int errorCode = grailsApplication.config.customExceptions.survey.fourZeroZero.multipleChoiceOption.errorCode
				int status = grailsApplication.config.customExceptions.survey.fourZeroZero.multipleChoiceOption.status
				String message = grailsApplication.config.customExceptions.survey.fourZeroZero.multipleChoiceOption.message
				String extendedMessage = grailsApplication.config.customExceptions.survey.fourZeroZero.multipleChoiceOption.extendedMessage
				String moreInfo = grailsApplication.config.customExceptions.survey.fourZeroZero.multipleChoiceOption.moreInfo

				throw new SurveyException(status,errorCode,message,extendedMessage,moreInfo)
			}
		}

		Question question = new Question(
				query:body.query,
				questionType:body.questionType,
				lastUpdatedBy:userRole.user,
				option:body.option,
				options:opts,
				isActive:(body.isNull("isActive")||body.isActive == "")?true:body.isActive
				)

		if(!question.save()){
			question.errors.each{ println it }
		}else{
			println "question save === "+question.findAll()
			questionList.add(question)
			
		}
		}
        
		survey.questions = questionList
		survey.save()
		if(!survey.save()){
			survey.errors.each{ log.error it }
		}else{
			println "Survey is saved"
		}
		return survey.questions
	}
	
	
	/**
	 *
	 * function particular question of a survey
	 * @param surveyId
	 * @param tokenValue
	 * @param questionId
	 * @return
	 */
	def findQuestionInSurvey(surveyId,tokenValue,questionId){
		Survey survey = getSurveyById(surveyId)
		Question question = null
		println "surveys == "+survey.questions

		survey.questions.each{
			println it.id
			if( it.id == questionId.toInteger()){
				question = it
				println it
			}
		}
		if(question == null){
			int errorCode = grailsApplication.config.customExceptions.survey.fourZeroFour.surveyQuestion.errorCode
			int status = grailsApplication.config.customExceptions.survey.fourZeroFour.surveyQuestion.status
			String message = grailsApplication.config.customExceptions.survey.fourZeroFour.surveyQuestion.message
			String extendedMessage = grailsApplication.config.customExceptions.survey.fourZeroFour.surveyQuestion.extendedMessage
			String moreInfo = grailsApplication.config.customExceptions.survey.fourZeroFour.surveyQuestion.moreInfo

			throw new SurveyException(status,errorCode,message,extendedMessage,moreInfo)
		}
		return question
	}


	/**
	 *
	 * function to edit a question of a particular server
	 * @param body (Json PayLoad)
	 * @param surveyId
	 * @param tokenValue
	 * @param questionId
	 * @return
	 */

	def editQuestionInSurvey(body,surveyId,tokenValue,questionId){
		println "9090909090-09-0900000"
		println body
		println "--body.query--"+body.query
		println body.questionType
		println body.option
		println surveyId
		println tokenValue
		println questionId
		println "--jh90909090-------------"
		JSONArray options
		def opts = []
		Survey survey = getSurveyById(surveyId)
		checkActionAllowedOnlyIfSurveyIsNotActive(survey)
		UserRole userRole = userService.getUserAndRoleByAuthToken(tokenValue);

		//check owner of survey
		boolean isOwner = checkOwnerOfSurvey(survey, userRole)

		if(!isOwner){
			int errorCode = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.errorCode
			int status = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.status
			String message = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.message
			String extendedMessage = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.extendedMessage
			String moreInfo = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.moreInfo

			throw new SurveyException(status,errorCode,message,extendedMessage,moreInfo)
		}

		def option = []
		if(body.questionType == "binary"){
			option = body.option
			if(body.option.size() < 2){
				int errorCode = grailsApplication.config.customExceptions.survey.fourZeroZero.multipleChoice.errorCode
				int status = grailsApplication.config.customExceptions.survey.fourZeroZero.multipleChoice.status
				String message = grailsApplication.config.customExceptions.survey.fourZeroZero.multipleChoice.message
				String extendedMessage = grailsApplication.config.customExceptions.survey.fourZeroZero.multipleChoice.extendedMessage
				String moreInfo = grailsApplication.config.customExceptions.survey.fourZeroZero.multipleChoice.moreInfo

				throw new SurveyException(status,errorCode,message,extendedMessage,moreInfo)
			}
		}else if(body.questionType == "scale") {
			option = body.option
			if(body.option.size() < 2){
				int errorCode = grailsApplication.config.customExceptions.survey.fourZeroZero.multipleChoice.errorCode
				int status = grailsApplication.config.customExceptions.survey.fourZeroZero.multipleChoice.status
				String message = grailsApplication.config.customExceptions.survey.fourZeroZero.multipleChoice.message
				String extendedMessage = grailsApplication.config.customExceptions.survey.fourZeroZero.multipleChoice.extendedMessage
				String moreInfo = grailsApplication.config.customExceptions.survey.fourZeroZero.multipleChoice.moreInfo

				throw new SurveyException(status,errorCode,message,extendedMessage,moreInfo)
			}
		}else if(body.questionType == "multiplechoice"){
			options = body.option

			options.each{
				println "it ====== "+it.id.getClass()
				Options opt = new Options(
						id :it?.id,
						value : it?.value,
						image:it?.image
						)
				opts.add(opt)
			}
			println "opts ==== "+opts
			if(body.option.size() < 2 || body.option.size() > 6){
				int errorCode = grailsApplication.config.customExceptions.survey.fourZeroZero.multipleChoiceOption.errorCode
				int status = grailsApplication.config.customExceptions.survey.fourZeroZero.multipleChoiceOption.status
				String message = grailsApplication.config.customExceptions.survey.fourZeroZero.multipleChoiceOption.message
				String extendedMessage = grailsApplication.config.customExceptions.survey.fourZeroZero.multipleChoiceOption.extendedMessage
				String moreInfo = grailsApplication.config.customExceptions.survey.fourZeroZero.multipleChoiceOption.moreInfo

				throw new SurveyException(status,errorCode,message,extendedMessage,moreInfo)
			}
		}
		println body.questionType
		def questionList = []
		Question question = Question.findById(questionId);
		survey.questions.each{
			println it.id
			if( it.id == questionId.toInteger()){
				question = it
				println it
			}else{
				questionList.add(it)
			}
		}
		println "question checking ----"
		println question
		question.query=body.query
		question.questionType=body.questionType
		question.lastUpdatedBy=userRole.user
		question.option=option
		question.options=opts
		question.isActive=(body.isNull("isActive")||body.isActive == "")?true:body.isActive

		questionList.add(question)
		if(!question.save(flush:true)){
			question.errors.each{ println it }
		}
		survey.questions = questionList
		if(!survey.save()){
			survey.errors.each{ println it }
		}

		println survey.questions
		return question

	}



	/**
	 *
	 * function to edit a question of a particular server
	 * @param body (Json PayLoad)
	 * @param surveyId
	 * @param tokenValue
	 * @param questionId
	 * @return
	 */

	def deleteQuestionInSurvey(surveyId,tokenValue,questionId){
		Survey survey = getSurveyById(surveyId)
		checkActionAllowedOnlyIfSurveyIsNotActive(survey)
		UserRole userRole = userService.getUserAndRoleByAuthToken(tokenValue);

		//check owner of survey
		boolean isOwner = checkOwnerOfSurvey(survey, userRole)

		if(!isOwner){
			int errorCode = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.errorCode
			int status = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.status
			String message = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.message
			String extendedMessage = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.extendedMessage
			String moreInfo = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.moreInfo

			throw new SurveyException(status,errorCode,message,extendedMessage,moreInfo)
		}

		def questionList = []
		Question question = null
		survey.questions.each{
			println it.id
			if( it.id == questionId.toInteger()){
				question = it
				println it
			}else{
				questionList.add(it)
			}
		}
		survey.questions = questionList
		if(!survey.save()){
			survey.errors.each{ println it }
		}
		println survey.questions
		return survey.questions
	}
	/**
	 *
	 * returns all the survey
	 * support pagination
	 */

	List findAllSurvey(offset,limit){
		offset = (offset == null )?0:offset
		limit  = (limit == null)?10:limit
		List surveys = Survey.createCriteria().list(max:limit, offset:offset){  }
		return surveys
	}

	/**
	 * function to delete user and decrease the survey count of user by 1
	 *
	 * @param surveyId
	 * @return
	 */
	def deleteSurvey(surveyId, tokenValue){
		println surveyId
		Survey survey = getSurveyById(surveyId)

		UserRole userRole = userService.getUserAndRoleByAuthToken(tokenValue);
		boolean isOwner = checkOwnerOfSurvey(survey, userRole)

		if(!isOwner){
			if(!(userRole?.user?.roleAuthority == "ROLE_SUPERADMIN" || userRole?.user?.roleAuthority == "ROLE_ADMIN")){
				int errorCode = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.errorCode
				int status = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.status
				String message = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.message
				String extendedMessage = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.extendedMessage
				String moreInfo = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.moreInfo

				throw new SurveyException(status,errorCode,message,extendedMessage,moreInfo)

			}
		}

		if(survey.isActive){
			survey.isActive = false
			survey.status = "hidden"
			if(!survey.save(flush:true)){
				survey.errors.each{ println it  }
			}else{
				decrementSurveyCount(survey)
				return "Survey Is Succefully deleted"
			}
		}
	}

	/*
	 * function to increase the survey count of user by 1
	 */
	def incrementSurveyCount(user){
		user.surveyCount  = user.surveyCount + 1
		user.save()
	}

	/*
	 * function to decrease the survey count of user by 1
	 */
	def decrementSurveyCount(survey){
		survey.user.surveyCount = survey.user.surveyCount - 1;
		survey.user.save()
	}

	/**
	 * if survey is in active mode only hash tags can be updated
	 * @param request
	 * @param survey
	 * @param userRole
	 * @return
	 */
	def updateOnlyHashtag(request,survey, userRole){
		def hashTags  = request.getJSON().hashTags
		if(hashTags == null){
			hashTags = []
		}
		hashTags.add(survey.surveyCode)
		if(hashTags != null && hashTags.size() != 0){
			survey = getHashTagAddedToSurvey(hashTags,userRole,survey);
			survey.save()
		}
		return survey
	}

	/**
	 * check if user is owner of survey
	 * if yes returns true
	 * else return false
	 * @param survey
	 * @param userRole
	 * @return
	 */
	def checkOwnerOfSurvey(survey, userRole){
		boolean flag = false;

		if(survey.user.id == userRole.user.id){
			flag = true
			return flag
		}else{
			return flag
		}

	}

	/**
	 * validation for start and end date
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	def validateStartAndEndDate(String startTime, String endTime){
		if(endTime != null){
			if(startTime != null){
				boolean flag = startDateGreaterThanCurrentDate(startTime)
				if(flag){
					return startDateGreaterThanEndDate(startTime, endTime)
				}
			}else{
				int errorCode = grailsApplication.config.customExceptions.survey.fourZeroZero.startEndDateValidation.errorCode
				int status = grailsApplication.config.customExceptions.survey.fourZeroZero.startEndDateValidation.status
				String message = grailsApplication.config.customExceptions.survey.fourZeroZero.startEndDateValidation.message
				String extendedMessage = grailsApplication.config.customExceptions.survey.fourZeroZero.startEndDateValidation.extendedMessage
				String moreInfo = grailsApplication.config.customExceptions.survey.fourZeroZero.startEndDateValidation.moreInfo

				throw new SurveyException(status,errorCode,message,extendedMessage,moreInfo)
			}
		}else{
			if(startTime == null){
				return true
			}else{
				return startDateGreaterThanCurrentDate(startTime)
			}
		}
	}

	/**
	 * method checks whether start date is greater than end date
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	def startDateGreaterThanEndDate(String startTime, String endTime){
		Format formatter = new SimpleDateFormat("yyyy-MM-dd")
		Date parsedStartDate = formatter.parseObject(startTime)
		Date parsedEndDate = formatter.parseObject(endTime)

		if(parsedEndDate >= parsedStartDate){
			return true
		}else{
			int errorCode = grailsApplication.config.customExceptions.survey.fourZeroZero.startDateGreaterThanEndDate.errorCode
			int status = grailsApplication.config.customExceptions.survey.fourZeroZero.startDateGreaterThanEndDate.status
			String message = grailsApplication.config.customExceptions.survey.fourZeroZero.startDateGreaterThanEndDate.message
			String extendedMessage = grailsApplication.config.customExceptions.survey.fourZeroZero.startDateGreaterThanEndDate.extendedMessage
			String moreInfo = grailsApplication.config.customExceptions.survey.fourZeroZero.startDateGreaterThanEndDate.moreInfo

			throw new SurveyException(status,errorCode,message,extendedMessage,moreInfo)
		}
	}

	/**
	 * method checks whether start date is greater than end date
	 * @param startTime
	 * @return
	 */
	def startDateGreaterThanCurrentDate(String startTime){
		Date currentDate = new Date()
		Format formatter = new SimpleDateFormat("yyyy-MM-dd")
		String currentFormattedDate = formatter.format(currentDate)
		Date parsedCurrentDate = formatter.parseObject(currentFormattedDate)

		//			String startFormattedDate = formatter.format(startTime)
		Date parsedStartDate = formatter.parseObject(startTime)
		println "parsedStartDate is "+parsedStartDate
		if(parsedStartDate >= parsedCurrentDate){
			return true
		}else{
			int errorCode = grailsApplication.config.customExceptions.survey.fourZeroZero.startDateValidation.errorCode
			int status = grailsApplication.config.customExceptions.survey.fourZeroZero.startDateValidation.status
			String message = grailsApplication.config.customExceptions.survey.fourZeroZero.startDateValidation.message
			String extendedMessage = grailsApplication.config.customExceptions.survey.fourZeroZero.startDateValidation.extendedMessage
			String moreInfo = grailsApplication.config.customExceptions.survey.fourZeroZero.startDateValidation.moreInfo

			throw new SurveyException(status,errorCode,message,extendedMessage,moreInfo)
		}
	}

	def createSurveyReport(request,surveyId,tokenValue)
	{
		println "------------i m in createSurveyReport of createSurveyReport service-----------"
		Survey survey = getSurveyById(surveyId)
		UserRole userRole = userService.getUserAndRoleByAuthToken(tokenValue);
		User user = userRole.user

		//SurveyReports surveyReport1=SurveyReports.findByReportedByAndReportedOn(user,survey)
		/*if(surveyReport!=null || !surveyReport.equals(null) || surveyReport!="null" || !surveyReport.equals("null") || surveyReport!="")
		 {
		 int errorCode = grailsApplication.config.customExceptions.survey.fourZeroZero.surveyReports.errorCode
		 int status = grailsApplication.config.customExceptions.survey.fourZeroZero.surveyReports.status
		 String message = "Survey is already Reported by Login User"
		 String extendedMessage = "Only Survey can be reported One time through login user"
		 String moreInfo = "Only Survey can be reported One time through login user"
		 throw new SurveyException(status,errorCode,message,extendedMessage,moreInfo)
		 }*/
		println "survey.status.toString()--"+survey.status.toString()
		try
		{
			if(survey.status=="Active" || survey.status.toString()=="Active" || survey.status=="flagged" || survey.status.toString()=="flagged")
			{
				println "-----------------we ar in survey status mode--------------"
				SurveyReports surveyReports = new SurveyReports(reportedBy:user,reportedOn:survey,user:user,survey:survey,
				text:(request.getJSON().isNull('text'))?true:request.getJSON().text,isDeleted:true);
				if(!surveyReports.save(flush:true)){
					surveyReports.errors.each{ println it  }
				}else{
					println "surveyReports saved"
					survey.surveyFlaggedCount=survey.surveyFlaggedCount+1
					if(!survey.save(flush:true)){
						survey.errors.each{ println it  }
					}else{
						println "for surveyReports survey update"
					}
					if(survey.surveyFlaggedCount > grailsApplication.config.myapp.creatSurveyReportFlagConstant)
					{
						survey.status = "Autohidden"
						if(!survey.save(flush:true)){
							survey.errors.each{ println it  }
						}else{
							println "for surveyReports survey update"
						}
					}
					/*else
					 {
					 survey.status = "flagged"
					 }if(survey.status=="Active" || survey.status.toString()=="Active")
					 {
					 }*/
				}

				return surveyReports
			}
			else
			{
				int errorCode = 200
				int status = 201
				String message = "Survey Reported is not created"
				String extendedMessage = "Survey Reported is not created"
				String moreInfo = "Survey Reported is not created"
				throw new SurveyException(status,errorCode,message,extendedMessage,moreInfo)
			}
		}
		catch(Exception e)
		{
			int errorCode = grailsApplication.config.customExceptions.survey.fourZeroZero.surveyReports.errorCode
			int status = grailsApplication.config.customExceptions.survey.fourZeroZero.surveyReports.status
			String message = grailsApplication.config.customExceptions.survey.fourZeroZero.surveyReports.message
			String extendedMessage = grailsApplication.config.customExceptions.survey.fourZeroZero.surveyReports.extendedMessage
			String moreInfo = grailsApplication.config.customExceptions.survey.fourZeroZero.surveyReports.moreInfo

			if(survey.status.toString()=="Active" || survey.status.toString()=="Active")
			{
				message="Survey is not Active"
				extendedMessage="Only Active Survey can be reported"
				moreInfo="Only Active Survey can be reported"
			}
			throw new SurveyException(status,errorCode,message,extendedMessage,moreInfo)
		}
	}

	def getSurveyReportBySurvey(surveyId,tokenValue)
	{
		Survey survey = getSurveyById(surveyId)
		UserRole userRole = userService.getUserAndRoleByAuthToken(tokenValue);
		User user = userRole.user
		def surveyReports = null
		try
		{
			surveyReports = SurveyReports.findAllBySurvey(survey)
			return surveyReports
		}
		catch(Exception e)
		{
			int errorCode = grailsApplication.config.customExceptions.survey.fourZeroZero.surveyReports.errorCode
			int status = grailsApplication.config.customExceptions.survey.fourZeroZero.surveyReports.status
			String message = grailsApplication.config.customExceptions.survey.fourZeroZero.surveyReports.message
			String extendedMessage = grailsApplication.config.customExceptions.survey.fourZeroZero.surveyReports.extendedMessage
			String moreInfo = grailsApplication.config.customExceptions.survey.fourZeroZero.surveyReports.moreInfo

			throw new SurveyException(status,errorCode,message,extendedMessage,moreInfo)
		}
	}

	/**
	 * @author Abhimanyu
	 * This function is use to embed city list where Survey will be available
	 * It Creates Object SurveyCity class (define in SurveyDomain.groovy) and embed that object in survey
	 */
	def getListCityObjectsForSurveyAvailablity(survey,cityList){
		def cityObjectList = []
		def cityEmbededList = []
		def willHomeCityWillbeAdded = true
		City homeCity = City.findByName(survey.user.address.embedCity)
		//cityWithLocation:[[name:Dallas, state:Texas, long:-96.80045109999998, lat:32.7801399, country:United States]]
		println "user cityList == "+survey.user.address
		
		cityList.each{
			Country country = locationService.checkCountry(it.country,it.countryCode);
			State 	state = locationService.checkState(country, (it.state == null)?null:it.state);
			City  city = locationService.checkCity(state,(it.name == null )?null:it.name);
			if(homeCity.name == it.name ){
				println "name ============= "+it.name
				willHomeCityWillbeAdded = false
			}
			if(city){
				cityObjectList.add(city)
				/*SurveyLocation surveyLoc = new SurveyLocation(
						survey:survey,
						city:city,
						location:[lat:it.latitude,long:it.longitude],
						surveyType:"SPONSOR"
				 )*/
				SurveyCity surveyCity = new SurveyCity(
						name:it.name,
						country:it.country,
						state:it.state,
						//latitude:it.latitude,
						//longitude:it.longitude,
						location:[lat:it.latitude,long:it.longitude],
						countryCode:it.countryCode
						)
				cityEmbededList.add(surveyCity)
				/*if(!surveyLoc.save()){
					SurveyLocation.errors.each{ println it }
				}*/
				 }
			}
		

       println " willHomeCityWillbeAdded = "+willHomeCityWillbeAdded +"    ===== "+homeCity.name+" cityEmbededList == "+cityEmbededList.size()
		
		
	if(cityEmbededList.size() == 0 ){
			State state = homeCity.state
		Country country = Country.findByName(survey.user.address.embedCountry)
		println "COUNTRY COUNTRY  ==== "+country
			println " willHomeCityWillbeAdded 2 = "+willHomeCityWillbeAdded+" ==== "+country?.name+" ==== "+state?.name
			
		SurveyCity surveyCity = new SurveyCity(
					name:homeCity.name,
					country:country?.name,
					state:state?.name,
					location:survey.user.address.location,
					countryCode:country.code
					)
		cityEmbededList.add(surveyCity)
		}
		println "cityEmbededList == "+cityEmbededList
		survey.visiblityCity = cityEmbededList
		survey.save()
		return cityObjectList
	}

	/**
	 * @author Abhimanyu
	 * This function is use to embed country list where Survey will be available
	 * It Creates Object SurveyCountry class (define in SurveyDomain.groovy) and embed that object in survey
	 */
	def getListCountryObjectsForSurveyAvailablity(survey,countryList){
		def countryObjectList = []
		def countryEmbededList = []
		def willHomeCountryWillbeAdded = true
		Country homeCountry = Country.findByName(survey.user.address.embedCountry)
		//cityWithLocation:[[name:Dallas, state:Texas, long:-96.80045109999998, lat:32.7801399, country:United States]]
		println "user countryList == "+survey.user.address.embedCountry+ "   homeCountry == "+homeCountry+ " countryList == "+countryList
		countryList.each{
			if(it?.name == homeCountry?.name){
				willHomeCountryWillbeAdded = false
			}
			Country country = locationService.checkCountry(it.name,it.countryCode);
			if(country){
				countryObjectList.add(country)
				/*SurveyLocation surveyLoc = new SurveyLocation(
						survey:survey,
						country:country,
						location:[lat:it.latitude,long:it.longitude],
						surveyType:"SPONSOR"
				 )*/

				SurveyCountry surveyCountry = new SurveyCountry(
						name:it.name,
						countryCode:it.countryCode,
						location:[lat:it.latitude,long:it.longitude]
						)
				countryEmbededList.add(surveyCountry)

				/*if(!surveyLoc.save()){
					SurveyLocation.errors.each{ println it }
				 }*/
				
			}
		}
		println " willHomeCountryWillbeAdded = "+willHomeCountryWillbeAdded+" countryEmbededList == "+countryEmbededList
		if(countryEmbededList.size() == 0 && homeCountry != null){
		SurveyCountry surveyCountry = new SurveyCountry(
					name:homeCountry?.name,
					countryCode:homeCountry?.code,
					location:survey.user.address.location
					)
		countryEmbededList.add(surveyCountry)
		}
		survey.visiblityCountry = countryEmbededList
		survey.save()
		return countryObjectList
	}


	def updateSurveyLocationAvailablityCity(survey,cityList){
		def cityObjectList = getListCityObjectsForSurveyAvailablity(survey,cityList)
		/*def deleteList = SurveyLocation.createCriteria().list(){
			not {'in' ("city",cityObjectList)}
			eq("survey",survey)
		}
		println "DeleteList ===== "+deleteList
		deleteList.each{ it.delete() }*/

	}

	def updateSurveyLocationAvailablityCountry(survey,countryList){
		def countryObjectList = getListCountryObjectsForSurveyAvailablity(survey,countryList)
		/*def deleteList = SurveyLocation.createCriteria().list(){
			not {'in' ("country",countryObjectList)}
			eq("survey",survey)
		}
		println "DeleteList ===== "+deleteList
		deleteList.each{ it.delete() }*/

	}
}
