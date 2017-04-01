package com.themopi.utils

import org.json.JSONArray
import org.json.JSONObject

import com.themopi.account.AuthenticationToken
import com.themopi.account.Role
import com.themopi.account.User
import com.themopi.account.UserRole
import com.themopi.apis.SurveyController
import com.themopi.location.City
import com.themopi.location.Country
import com.themopi.location.State
import com.themopi.survey.Survey
import com.themopi.user.Address
import com.themopi.user.Profile
import com.themopi.user.Profile.Gender;
import com.themopi.utils.GenerateDataUtils
import com.themopi.utils.TestingSetup
import grails.util.Holders
import grails.converters.JSON
import spock.lang.*
import grails.converters.*
import grails.plugin.springsecurity.authentication.dao.NullSaltSource


class TestingSetup {
	
	def grailsApplication
	
	def createUser(String encodedPassword)
	{
		def springSecurityService
		def saltSource
		GenerateDataUtils gDatautils1 = new GenerateDataUtils();
		String stringValue = gDatautils1.randomString();
		println "--------------ggg----------stringValue--------"+stringValue
		/*String salt = saltSource instanceof NullSaltSource ? null : stringValue
		String encodedPassword = springSecurityService.encodePassword("password",salt)*/
		
		User user = new User(
			email: stringValue+"@mopi.com",
			password:encodedPassword,
			name:stringValue,
			accountLocked: false,
			enabled: true,
			accountExpired:false,
			passwordExpired:false,
			isActive:true,
			username:stringValue,
			tokenValue:null,
			roleAuthority:"ROLE_USER",
			roleId: new Long(4),
			salt:stringValue+"@mopi.com"
			).save(flush:true)
	
			return user
	}
	def writeUserIdToFile(long uid)
	{
		
		def config = Holders.config
		long userIdT = uid
		println "userIdT---"+userIdT
		def jsonBuilderUser = new groovy.json.JsonBuilder()
			jsonBuilderUser.user(
				userId: userIdT
			)
			println("Using just named arguments")
			println(jsonBuilderUser.toPrettyString())
		//GetId object=new GetId(surveyId:surveyIdT)
		def converterUser = jsonBuilderUser as JSON
		String pathUser = config.myapp.tempIdFileFolderPath+"user.json"
		converterUser.render(new java.io.FileWriter(pathUser));
		return 1
	}
	def assignRoleUser(long uid,String roleUser)
	{
		User user = User.findById(uid)
		Role role = Role.findByAuthority(roleUser)
		UserRole userRole = new UserRole(user:user,role:role).save(flush:true)
	}
	def updateProfile(long uid)
	{
		User uUser = User.findById(uid)
		
		Profile profile = new Profile(
			description:"Hhhhhhh",
			mobile:9898099090,
			phonenumber:9898099090,
			gender:"Male",
			occupation:"Student",
			religion:"Hindu",
			incomeLevel:"0-10000",
			age:"18-30"/*,
			dob:new Date()*/
			).save()
		uUser.profile = profile
		uUser.save()
		return uUser
	}
	
	def updateAddress(long uid)
	{
		User uUser = User.findById(uid)
		Country country = Country.findByName("India")
		State state = State.findByName("Maharashtra")
		City city =City.findByName("Navi Mumbai")
		Address address = new Address(
			homeTown:"CBD Belapur",
			/*location:[lat:19.0300657,long:73.03511379999998],*/
			city:city,
			state:state,
			country:country,
			embedCountry:country?.name,
			embedState:state?.name,
			embedCity:city?.name
			).save(flush:true)
		uUser.address = address
		uUser.save()
		return uUser
	}

	def createSurvey(long uid)
	{
		User uUser = User.findById(uid)
		GenerateDataUtils gDatautils1 = new GenerateDataUtils();
		String stringValue = gDatautils1.randomString();
		//sponsered,nonsponsered
		Survey survey = new Survey(
			name:stringValue,
			surveyType:"sponsered",
			surveyCode:stringValue,
			lastUpdatedBy:uUser,
			status:"Draft",
			visibility:true,
			startTime:new Date(),
			endTime:new Date(),
			user:uUser,
			dateCreated: new Date(),
			visibleTo:"EveryOne"
			).save(flush:true)
			
		return survey
	}
}
