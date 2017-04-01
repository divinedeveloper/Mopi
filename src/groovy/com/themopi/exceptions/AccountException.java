package com.themopi.exceptions;

import org.codehaus.groovy.grails.web.json.JSONObject;

public class AccountException extends BaseException {
	
	public AccountException(int status,int errorCode,String message, String extendedMessage , String moreInfo){
		super(status,errorCode,message, extendedMessage ,moreInfo);
	}
}
