package com.themopi.exceptions

import org.codehaus.groovy.grails.web.json.JSONObject;

class SecurityException extends BaseException {
		
	public SecurityException(int status,int errorCode,String message, String extendedMessage ,String moreInfo){
		super(status,errorCode,message, extendedMessage ,moreInfo)
	}
	
}
