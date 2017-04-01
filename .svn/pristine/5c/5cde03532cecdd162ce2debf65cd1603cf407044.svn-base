package com.themopi.survey

import com.themopi.account.User

import java.util.Date;
import java.util.List;

class Comment {	 
	
	User commentedBy
	
	String comment
	
	Survey commentOnSurvey
	
	Integer commentReportCount = 0
	
	Date dateCreated, lastUpdated
	
	boolean visibility = true
	
	static embedded = ['commentedBy','commentOnSurvey']
	
	static belongsTo = [survey: Survey , user:User]

    static constraints = {
		commentOnSurvey blank: false, nullable: false
		commentReportCount default:0, nullable:true
	}
	
}
