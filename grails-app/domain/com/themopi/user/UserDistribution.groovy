package com.themopi.user

import com.themopi.location.*

class UserDistribution {
	Integer male = 0
	Integer female = 0
	Integer other = 0
	String 	countryName
	String  countryCode
		
	static belongsTo = [country:Country]	
    static constraints = {
		countryCode nullable:true
    }
}
