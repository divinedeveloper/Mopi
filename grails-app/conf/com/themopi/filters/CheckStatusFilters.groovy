package com.themopi.filters

import org.codehaus.groovy.grails.web.json.JSONObject

import com.themopi.account.UserRole

class CheckStatusFilters {

	def userService
	def grailsApplication
    def filters = {
        checkStatusOfSponsorUser(controller:'logout|login|error|template', action:'signUp|verifyRegistration', invert: true) {
            before = {
				println "Inside check status of sponsor filter "
				String tokenValue = request.getHeader('X-Auth-Token')
				println "tokenValue is "+tokenValue
				if(tokenValue != null && tokenValue != ""){
				UserRole userRole = userService.getUserAndRoleByAuthToken(tokenValue);
				if(userRole.user.isActive == false){
					println "throw a 401 error response so client will redirect to login page"
					
					int errorCode = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.errorCode
					int status = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.status
					String message = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.message
					String extendedMessage = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.extendedMessage
					String moreInfo = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.moreInfo
					
					JSONObject errorJson = new JSONObject();
					errorJson.put("status",status);
					errorJson.put("errorCode",errorCode);
					errorJson.put("message",message);
					errorJson.put("extendedMessage",extendedMessage);
					errorJson.put("moreInfo",moreInfo);
					errorJson.put("error",true);
			
					log.error errorJson
					response.setStatus(status)
					render errorJson
					return false
				}
            }
//				println "before return statment"
//				return true

            }
            after = { Map model ->

            }
            afterView = { Exception e ->

            }
        }
    }
}
