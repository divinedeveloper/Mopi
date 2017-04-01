package com.themopi.survey

import com.themopi.account.User
import java.util.List;

class SurveyReports {
    User reportedBy
	Survey reportedOn	
	String text
	Boolean isDeleted
	static belongsTo = [survey: Survey,user:User]
	
	static embedded = ['reportedBy','reportedOn']

    static constraints = {
		text nullable:true
		isDeleted nullable:false
	}
		
}
