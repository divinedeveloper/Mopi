package com.themopi.survey

import com.themopi.account.User

class SurveysResponded {

	User user
	Survey survey
	static belongsTo = [user:User,survey:Survey]
    static constraints = {
		user nullable:false
		survey nullable:false
    }
}
