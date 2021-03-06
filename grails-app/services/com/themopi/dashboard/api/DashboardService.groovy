package com.themopi.dashboard.api


import com.themopi.account.User
import com.themopi.location.Country
import com.themopi.survey.Survey
import com.themopi.user.UserDistribution
import grails.transaction.Transactional

import org.json.JSONObject

@Transactional
class DashboardService {

	def grailsApplication
	def userService
	JSONObject dashboardJsonlist;
	def getAllList(request) {
		try {
			def now = new Date()
			dashboardJsonlist = new JSONObject();
			int errorCode = grailsApplication.config.customExceptions.dashboard.fourZeroZero.dashboard.errorCode
			int status = grailsApplication.config.customExceptions.dashboard.fourZeroZero.dashboard.status
			String message = grailsApplication.config.customExceptions.dashboard.fourZeroZero.dashboard.message
			String extendedMessage = grailsApplication.config.customExceptions.dashboard.fourZeroZero.dashboard.extendedMessage
			String moreInfo = grailsApplication.config.customExceptions.dashboard.fourZeroZero.dashboard.moreInfo
			String tokenValue = request.getHeader('X-Auth-Token')

			def c = User.createCriteria()
			def results = c.list() {
				'in'("roleAuthority", [
					"ROLE_USER",
					"ROLE_SPONSEREDADMIN"
				])
				eq("isActive",true)
			}
			/*println "---results.size()---"+results.size()*/
			dashboardJsonlist.put("TotalUser", results.size());

			def userInLast30Days = c.list() {
				'in'("roleAuthority", [
					"ROLE_USER",
					"ROLE_SPONSEREDADMIN"
				])
				between('dateCreated',now-30, now)
				eq("isActive",true)
			}
			/*println "---- userInLast30Days.size()---"+userInLast30Days.size()*/
			dashboardJsonlist.put("RecentUser", userInLast30Days.size());


			println Survey.count
			dashboardJsonlist.put("TotalSurvey", Survey.count);
			/*println "---"
			 println Survey.count*/
			dashboardJsonlist.put("RecentSurvey", Survey.countByDateCreatedBetween(now-30, now));


			println "---dashboardJsonlist---"+dashboardJsonlist
			return dashboardJsonlist;
		}
		catch(Exception e) {
		}
	}

	/**
	 * Function to return data for graphs on dashboard
	 * @param type = age/country/occupation/incomeLevel/point/gender
	 * @return
	 */

	def getGraph(type){
		if(type == "country"){
			def match=['$match':['$or':[
						['roleAuthority':'ROLE_USER'],
						['roleAuthority':'ROLE_SPONSEREDADMIN']
					]]]
			def matchactive = ['$match':['isActive':true]]
			def group2 = ['$group':['_id':'$address.embedCountry',count: ['$sum': 1]]]
			def surveys = User.collection.aggregate(matchactive,match,group2)
			return ["error":false,'result':surveys.commandResult.result]
		}else if(type == "age"){
			def match=['$match':['$or':[
						['roleAuthority':'ROLE_USER'],
						['roleAuthority':'ROLE_SPONSEREDADMIN']
					]]]
			def matchactive = ['$match':['isActive':true]]
			def group2 = ['$group':['_id':'$profile.age',count: ['$sum': 1]]]
			def surveys = User.collection.aggregate(matchactive,match,group2)
			return ["error":false,'result':surveys.commandResult.result]
		}else if(type == "income"){
			def match=['$match':['$or':[
						['roleAuthority':'ROLE_USER'],
						['roleAuthority':'ROLE_SPONSEREDADMIN']
					]]]
			def matchactive = ['$match':['isActive':true]]
			def group2 = ['$group':['_id':'$profile.incomeLevel',count: ['$sum': 1]]]
			def surveys = User.collection.aggregate(matchactive,match,group2)
			return ["error":false,'result':surveys.commandResult.result]
		}else if(type == "gender"){
			def match=['$match':['$or':[
						['roleAuthority':'ROLE_USER'],
						['roleAuthority':'ROLE_SPONSEREDADMIN']
					]]]
			def group2 = ['$group':['_id':'$profile.gender',count: ['$sum': 1]]]
			def matchactive = ['$match':['isActive':true]]
			def surveys = User.collection.aggregate(matchactive,match,group2)
			return ["error":false,'result':surveys.commandResult.result]
		}else if(type == "occupation"){
			def match=['$match':['$or':[
						['roleAuthority':'ROLE_USER'],
						['roleAuthority':'ROLE_SPONSEREDADMIN']
					]]]
			def matchactive = ['$match':['isActive':true]]
			def group2 = ['$group':['_id':'$profile.occupation',count: ['$sum': 1]]]
			def surveys = User.collection.aggregate(matchactive,match,group2)
			return ["error":false,'result':surveys.commandResult.result]
		}else if(type == "religion"){
			def match=['$match':['$or':[
						['roleAuthority':'ROLE_USER'],
						['roleAuthority':'ROLE_SPONSEREDADMIN']
					]]]
			def matchactive = ['$match':['isActive':true]]
			def group2 = ['$group':['_id':'$profile.religion',count: ['$sum': 1]]]
			def surveys = User.collection.aggregate(matchactive,match,group2)
			def result = surveys.commandResult.result
			def value = result.getAt("")
			println "value for def = ==  "+value
			return ["error":false,'result':surveys.commandResult.result]
		}else if(type == "points"){
			def match=['$match':['$or':[
						['roleAuthority':'ROLE_USER'],
						['roleAuthority':'ROLE_SPONSEREDADMIN']
					]]]
			def matchactive = ['$match':['isActive':true]]
			def group2 = ['$group':['_id':'$profile.points',count: ['$sum': 1]]]
			def surveys = User.collection.aggregate(matchactive,match,group2)
			return ["error":false,'result':surveys.commandResult.result]
		}
	}

	def getMapData(){
		List userDisributionList = UserDistribution.findAll()
		return userDisributionList
	}


	def addUserDistribution(gender,country,prevCountry,prevGender,user,code){
		if(user.isActive == true){
			println "gender in add disrtibution  = "+gender
			println "country = "+country?.name
			println "prevCountry = "+prevCountry?.name
			println "prevGender  = "+prevGender
			println "user  = "+user

			UserDistribution userDistributionObject  = UserDistribution.findByCountry(country)
			if(userDistributionObject != null && country?.name == prevCountry?.name){
				println "1 == =="
				if(prevGender == null && gender != null){
					switch(gender){
						case 'Male':
							userDistributionObject.male = userDistributionObject.male+1
							userDistributionObject.countryCode = code
							break;
						case 'Female':
							userDistributionObject.female = userDistributionObject.female+1
							userDistributionObject.countryCode = code
							break;
						default:
							userDistributionObject.other = userDistributionObject.other+1
							userDistributionObject.countryCode = code
							break;
					}
				}else{
					switch(gender){
						case 'Male':
							if(prevGender == 'Female'){
								userDistributionObject.female = (userDistributionObject.female == 0)?0:userDistributionObject.female-1
								userDistributionObject.male = userDistributionObject.male+1
								userDistributionObject.countryCode = code
							}else if(prevGender != 'Male' ){
								userDistributionObject.other = (userDistributionObject.other == 0)?0:userDistributionObject.other-1
								userDistributionObject.male = userDistributionObject.male+1
								userDistributionObject.countryCode = code
							}
							break;

						case 'Female':
							if(prevGender == 'Male'){
								userDistributionObject.male = (userDistributionObject.male == 0)?0:userDistributionObject.male-1
								userDistributionObject.female = userDistributionObject.female+1
								userDistributionObject.countryCode = code
							}else if(prevGender != 'Female'){
								userDistributionObject.other = userDistributionObject.other-1
								userDistributionObject.female = userDistributionObject.female+1
								userDistributionObject.countryCode = code
							}
							break;
						default:
							if(prevGender == 'Male'){
								userDistributionObject.male = (userDistributionObject.male == 0)?0:userDistributionObject.male-1
								userDistributionObject.other = userDistributionObject.other+1
								userDistributionObject.countryCode = code
							}else if(prevGender == 'Female'){
								userDistributionObject.other = userDistributionObject.other+1
								userDistributionObject.female = (userDistributionObject.female == 0)?0:userDistributionObject.female-1
								userDistributionObject.countryCode = code
							}
							break;
					}
				}
				userDistributionObject.save()
			}else if(userDistributionObject != null && country?.name != prevCountry?.name && prevCountry?.name != null){
				println "2 ===== "
				UserDistribution oldUserDistributionObject  = UserDistribution.findByCountry(prevCountry)
				println "prevGender ================ "+prevGender +"  "+gender
				if(oldUserDistributionObject !=  null){
					println "prevGender ================ "+oldUserDistributionObject
					switch(gender){
						case 'Male':
							if(prevGender == 'Female'){
								oldUserDistributionObject.female = (oldUserDistributionObject.female == 0)?0:oldUserDistributionObject.female-1
								userDistributionObject.male = userDistributionObject.male+1
								userDistributionObject.countryCode = code
							}else if(prevGender == 'Male'){
								oldUserDistributionObject.male = (oldUserDistributionObject.male == 0)?0:oldUserDistributionObject.male-1
								userDistributionObject.male = userDistributionObject.male+1
								userDistributionObject.countryCode = code
							}else if(prevGender == 'Unknown'){
								oldUserDistributionObject.other = (oldUserDistributionObject.other == 0)?0:oldUserDistributionObject.other-1
								userDistributionObject.male = userDistributionObject.male+1
								userDistributionObject.countryCode = code
							}
							break;

						case 'Female':
							if(prevGender == 'Male'){
								oldUserDistributionObject.male = (oldUserDistributionObject.male == 0)?0:oldUserDistributionObject.male-1
								userDistributionObject.female = userDistributionObject.female+1
								userDistributionObject.countryCode = code
							}else if(prevGender == 'Female'){
								oldUserDistributionObject.female = (oldUserDistributionObject.female == 0)?0:oldUserDistributionObject.female-1
								userDistributionObject.female = userDistributionObject.female+1
								userDistributionObject.countryCode = code
							}else if(prevGender == 'Unknown'){
								userDistributionObject.female = userDistributionObject.female+1
								oldUserDistributionObject.other = (oldUserDistributionObject.other == 0)?0:oldUserDistributionObject.other-1
								userDistributionObject.countryCode = code
							}
							break;
						default:
							if(prevGender == 'Male'){
								oldUserDistributionObject.male = (oldUserDistributionObject.male == 0)?0:oldUserDistributionObject.male-1
								userDistributionObject.other = userDistributionObject.other+1
								userDistributionObject.countryCode = code
							}else if(prevGender == 'Female'){
								userDistributionObject.other = userDistributionObject.other+1
								oldUserDistributionObject.female = (oldUserDistributionObject.female == 0)?0:oldUserDistributionObject.female-1
								userDistributionObject.countryCode = code
							}else if(prevGender == 'Unknown'){
								userDistributionObject.other = userDistributionObject.other+1
								oldUserDistributionObject.other = (oldUserDistributionObject.other == 0)?0:oldUserDistributionObject.other-1
								userDistributionObject.countryCode = code
							}
							break;
					}
					oldUserDistributionObject?.save()
					userDistributionObject.save()
				}else{
					switch(gender){
						case 'Male':
							userDistributionObject.male = userDistributionObject.male+1
							userDistributionObject.countryCode = code
							break;
						case 'Female':
							userDistributionObject.female = userDistributionObject.female+1
							userDistributionObject.countryCode = code
							break;
						default:
							userDistributionObject.other = userDistributionObject.other+1
							userDistributionObject.countryCode = code
							break;
					}
				}
			}else if(userDistributionObject != null && prevCountry?.name == null){
				if(prevGender == null && gender != null){
					switch(gender){
						case 'Male':
							userDistributionObject.male = userDistributionObject.male+1
							userDistributionObject.countryCode = code
							break;
						case 'Female':
							userDistributionObject.female = userDistributionObject.female+1
							userDistributionObject.countryCode = code
							break;
						default:
							userDistributionObject.other = userDistributionObject.other+1
							userDistributionObject.countryCode = code
							break;
					}
				}else{
					switch(gender){
						case 'Male':
							if(prevGender == 'Female'){
								userDistributionObject.male = userDistributionObject.male+1
								userDistributionObject.female = (userDistributionObject.female == 0)?0:userDistributionObject.female-1
								userDistributionObject.countryCode = code
							}else if(prevGender != 'Unknown'){
								userDistributionObject.other = (userDistributionObject.other == 0)?0:userDistributionObject.other-1
								userDistributionObject.male = userDistributionObject.male+1
								userDistributionObject.countryCode = code
							}else if(prevGender != 'Male'){
								userDistributionObject.male = userDistributionObject.male+1
								userDistributionObject.countryCode = code
							}
							break;

						case 'Female':
							if(prevGender == 'Male'){
								userDistributionObject.female = userDistributionObject.female+1
								userDistributionObject.male = (userDistributionObject.male == 0)?0:userDistributionObject.male-1
								userDistributionObject.countryCode = code
							}else if(prevGender != 'Female'){
								userDistributionObject.female = userDistributionObject.female+1
								userDistributionObject.countryCode = code
							}
							break;
						default:
							if(prevGender == 'Male'){
								userDistributionObject.other = userDistributionObject.other+1
								userDistributionObject.male = (userDistributionObject.male == 0)?0:userDistributionObject.male-1
								userDistributionObject.countryCode = code
							}else if(prevGender == 'Female'){
								userDistributionObject.other = userDistributionObject.other+1
								userDistributionObject.female = (userDistributionObject.female == 0)?0:userDistributionObject.female-1
								userDistributionObject.countryCode = code
							}
							break;
					}
				}

				userDistributionObject.save()
			}else if(userDistributionObject == null  && prevCountry?.name != null && country?.name != null ){
				println "3 == "+prevCountry+ "  "+country
				UserDistribution oldUserDistributionObject  = UserDistribution.findByCountry(prevCountry)
				switch(prevGender){
					case 'Male':
						oldUserDistributionObject.male = (oldUserDistributionObject.male == 0)?0:oldUserDistributionObject.male-1
						break;

					case 'Female':
						oldUserDistributionObject.female = (oldUserDistributionObject.female == 0)?0:oldUserDistributionObject.female-1
						break;
					default:
						oldUserDistributionObject.other = (oldUserDistributionObject.other == 0)?0:oldUserDistributionObject.other-1
						break;
				}
				if(!oldUserDistributionObject?.save()){
					oldUserDistributionObject.errors.each{ log.error it }
				}
				addNewUserDistribution(country,gender,code)
			}else if(userDistributionObject == null  && prevCountry?.name == null && country?.name != null){
				println "I am here boss 4 ===="
				addNewUserDistribution(country,gender,code)
			}
		}
	}

	def substractUserDistribution(Country country,String gender, String code){
		if(country != null){
			UserDistribution userDistributionObject  = UserDistribution.findByCountry(country)
			if(userDistributionObject != null){
				switch(gender){
					case 'Male':
						userDistributionObject.male = userDistributionObject.male-1
						userDistributionObject.countryCode = code
						break;
					case 'Female':
						userDistributionObject.female = userDistributionObject.female-1
						userDistributionObject.countryCode = code
						break;
					default:
						userDistributionObject.other = userDistributionObject.other-1
						userDistributionObject.countryCode = code
						break;
				}
				if(!userDistributionObject.save()){
					userDistributionObject.errors.each{ println it }
				}
			}
		}
	}

	def addUserDistribution(Country country,String gender, String code){
		UserDistribution userDistributionObject  = UserDistribution.findByCountry(country)
		if(userDistributionObject == null){
			addNewUserDistribution(country,gender,code)
		}else{
			if(country != null){
				switch(gender){
					case 'Male':
						userDistributionObject.male = userDistributionObject.male+1
						userDistributionObject.countryCode = code
						break;
					case 'Female':
						userDistributionObject.female = userDistributionObject.female+1
						userDistributionObject.countryCode = code
						break;
					default:
						userDistributionObject.other = userDistributionObject.other+1
						userDistributionObject.countryCode = code
						break;
				}
				if(!userDistributionObject.save()){
					userDistributionObject.errors.each{ println it }
				}
			}
		}
	}
	def addNewUserDistribution(Country country ,String gender,String code){
		if(country != null)
			switch(gender){
				case 'Male':
					UserDistribution userDistribution = new UserDistribution(
					male:1,
					country:country,
					countryName:country.name,
					countryCode:code
					)
					if(!userDistribution.save()){
						userDistribution.errors.each{ println it }
					}
					break;
				case 'Female':
					UserDistribution userDistribution = new UserDistribution(
					female:1,
					country:country,
					countryName:country.name,
					countryCode:code
					)
					if(!userDistribution.save()){
						userDistribution.errors.each{ println it }
					}
					break;
				default:
					UserDistribution userDistribution = new UserDistribution(
					other:1,
					country:country,
					countryName:country.name,
					countryCode:code
					)
					if(!userDistribution.save()){
						userDistribution.errors.each{ println it }
					}
					break;
			}
	}
}
