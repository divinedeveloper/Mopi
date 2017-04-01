package com.themopi.survey

import com.themopi.account.UserRole;
//not using this domain
class SurveyHashTag {
    	
	static belongsTo = [hashTag:HashTag,survey:Survey]
	
	static SurveyHashTag get(long surveyId, long hashTagId) {
		UserRole.where {
			user == Survey.load(surveyId) &&
			role == HashTag.load(hashTagId)
		}.get()
	}
	
    static constraints = {
    }
}
