package com.themopi.user

import java.util.Date;

import com.themopi.survey.Gift;

class Profile {
	
	Gender gender
	
	Long  phoneNumber, mobile
	Long points = 0

	String religion,incomeLevel, description, occupation, badge, age, imageUrlP
	
	Date dob, dateCreated, lastUpdated
	
	int followersCount =0  , followingCount = 0
	enum Gender{
		Male, Female , Unknown
	}
	
	static mapping = {
		version false
	}
	static constraints = {
		dob max : new Date(),nullable:true
		incomeLevel nullable:true, inList: ["0-10000", "10001-50000", "50001-100000","100000+","Unknown"]
		age nullable:true, inList: ["18-30", "31-40", "41-50","51-60", "60+","Unknown"]
		phoneNumber nullable:true
		mobile nullable:true
		badge nullable:true
		religion nullable:true
		description nullable:true
		occupation nullable:true
		gender nullable:true
		imageUrlP nullable:true
		
	}	
	
}
