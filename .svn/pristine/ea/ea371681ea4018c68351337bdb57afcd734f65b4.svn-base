package com.themopi.apis.error


import org.springframework.security.access.annotation.Secured

import org.codehaus.groovy.grails.web.json.JSONObject;

@Secured('permitAll')
class ErrorController {

    def grailsApplication
	
	/**
	 * In any api, if there are server error like 500 internal server error
	 * throw error response object 
	 * @return
	 */
    def sendErrorResponse() { 
		
		def moreInfoOfException = request.exception
		
		int errorCode = grailsApplication.config.customExceptions.error.fiveZeroZero.errorCode
		int status = grailsApplication.config.customExceptions.error.fiveZeroZero.status
		String message = grailsApplication.config.customExceptions.error.fiveZeroZero.message
		String extendedMessage = grailsApplication.config.customExceptions.error.fiveZeroZero.extendedMessage
		String moreInfo = grailsApplication.config.customExceptions.error.fiveZeroZero.moreInfo
		
		JSONObject errorJson = new JSONObject();
		errorJson.put("status",status);
		errorJson.put("errorCode",errorCode);
		errorJson.put("message",message);
		errorJson.put("extendedMessage",extendedMessage);
		errorJson.put("moreInfo",moreInfoOfException);
		errorJson.put("error",true);

		log.error errorJson
		response.setStatus(status)
		render errorJson
	}
}
