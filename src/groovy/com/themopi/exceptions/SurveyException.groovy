package com.themopi.exceptions

class SurveyException extends BaseException {
	public SurveyException(int status,int errorCode,String message, String extendedMessage ,String moreInfo){
		super(status,errorCode,message, extendedMessage ,moreInfo)
	}
}
