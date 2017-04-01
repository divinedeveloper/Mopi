package com.themopi.survey

import com.themopi.account.User

class CommentReports {
	
	User reportedBy
	
	Comment commentedOn
	
	static embedded = ['reportedBy','commentedOn']
	
	static belongsTo = [user: User,comment:Comment]
	
    static constraints = {
		reportedBy blank: false, nullable: false
		commentedOn blank: true, nullable:false
	}
	
}
