package com.themopi.location

import com.themopi.user.Address;

class State {
	
	String name
	Country country
	static hasMany = [city: City]
	static belongsTo = [country: Country]
    static constraints = {
		name blank: false, nullable: false
		country blank: false , nullable:false
	}
	
	
}
