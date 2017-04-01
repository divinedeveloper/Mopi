package com.themopi.user

import com.themopi.account.User
import java.util.Date;

class Followers {

//	User user
	
	Date dateCreated, lastUpdated
	boolean requested = true
	Status status
	boolean allowFollowing = false
	User follower
	User followee
	
	enum Status{
		FOLLOW_REQUESTED,FOLLOW_APPROVED
	}
	static belongsTo = [user: User]
    static constraints = {
//		user  blank: true, nullable: true
	}
	
}
