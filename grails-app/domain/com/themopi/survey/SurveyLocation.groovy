package com.themopi.survey

import com.themopi.account.UserRole;
import com.themopi.location.City
import com.themopi.location.Country

class SurveyLocation {
	Survey survey
	Map location
	Country country
	City city
	SurveyType surveyType
	enum SurveyType{
		SPONSOR,GENERAL
	}
	static mapping = {
		location geoIndex:'2d', indexAttributes:[min:-500, max:500]
	}
	static belongsTo = [survey:Survey]
    static constraints = {
		country nullable:true
		city nullable:true
    }
	
	
}
