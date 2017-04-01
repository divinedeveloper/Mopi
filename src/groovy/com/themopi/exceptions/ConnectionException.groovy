package com.themopi.exceptions

class ConnectionException extends BaseException {
	public ConnectionException(int status,int errorCode,String message, String extendedMessage , String moreInfo){
		super(status,errorCode,message, extendedMessage ,moreInfo);
	}
}