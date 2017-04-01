package com.themopi.apis.security

import com.themopi.exceptions.*;
import com.themopi.apis.security.LogoutService;
import org.codehaus.groovy.grails.web.json.JSONObject

/**
 * @author Abhimanyu
 * This program is deleted the Authorization token on logout 
 * Verify if token exist or not . if token doesn't exist it send http status 404 and token not found error.
 */
class LogoutController {
    def logoutService
	/*
	 * This function verify if authentication token exist or not. if not exist , it send http status 404 else deleted the token 
	 */
	def logout(){
		try{
			def tokenValue = request.getHeader("X-Auth-Token");
		    log.debug(tokenValue)
			
			logoutService.removeToken(tokenValue)
			log.debug("after removetoken")
			response.status=200;
			JSONObject json = new JSONObject();
			json.put("status",200)
			json.put("success",true)
			render json
		}catch(SecurityException e){
			log.error e.errorResponse()
			response.status = e.errorResponse().status
			render e.errorResponse();
		}
		
	}
   
}
