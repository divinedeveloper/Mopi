package com.themopi.user

import com.themopi.account.User
import java.util.Date;

class Timeline {
	
	User performedBy
	
	String action
	
	String type
	 
	Boolean isActive
	
	Date dateCreated, lastUpdated
	
	//this domain will use dynamic field performedOn 
	
	static belongsTo = [user: User]
	
    static constraints = {
		
    }
	
	
}
