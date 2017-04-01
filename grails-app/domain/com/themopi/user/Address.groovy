package com.themopi.user

import com.themopi.location.City;
import com.themopi.location.Country;
import com.themopi.location.State;

class Address {
	
	String homeTown
	Map location
	String embedCountry
	String embedState
	String embedCity
	
	static belongsTo = [country: Country, state: State, city: City]
	
	static mapping = {
		location geoIndex:'2d', indexAttributes:[min:-500, max:500]
	}
    static constraints = {
		homeTown nullable:false
		location blank:true
		embedCountry nullable:true
		embedState nullable:true
		embedCity nullable:true
		country nullable:true
		state nullable:true
		city nullable:true
		
		
    }
	
}
