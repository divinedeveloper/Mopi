package com.themopi.exceptions

class ImageException extends BaseException{
	public ImageException(int status,int errorCode,String message, String extendedMessage ,String moreInfo){
		super(status,errorCode,message, extendedMessage ,moreInfo)
	}

}
