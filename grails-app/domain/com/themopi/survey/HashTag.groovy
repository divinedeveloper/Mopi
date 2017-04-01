package com.themopi.survey

import com.themopi.account.User

class HashTag {
	
	String name
	
//	Survey surveyHashtagOwner
	
	static belongsTo = [User: User]
	
    static constraints = {
		name blank: false, nullable: false
	}
	
	
}
