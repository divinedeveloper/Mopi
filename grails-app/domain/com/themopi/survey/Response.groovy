package com.themopi.survey

import com.themopi.account.User

class Response {
	
	Question answerForQuestion
	Survey   answerForSurvey
	User	answerBy
	String answer
	
	
	static embedded = ['answerForQuestion','answerBy']
	
	static belongsTo = [survey: Survey, user: User , question:Question]
 
	static constraints = {
		
	}
	
}
