package com.themopi

import com.themopi.location.City
import com.themopi.location.Country
import com.themopi.location.State
import grails.transaction.Transactional

@Transactional
class LocationService {
	static transactional = 'mongo'
	
	Country checkCountry(name,code){
		Country country = Country.findByName(name)
		if(country == null && name != null){
			country = new Country(
				 name : name,
				 code : code
				)
			country.save(flush:true)
		}
		return country		
	}
	
	State checkState(Country country,String name){
		println "Country  ==========================    IN LOcation = "+country
		State state = State.findByName(name)
		if(state == null && name != null && country != null){
			state = new State(
				name:name,
				country:country
				)
			state.save(flush:true)
		}
		return state
	}
	
	City checkCity(State state,String name){
		City city = City.findByName(name);
		if(city == null && name != null && state != null){
			city = new City(
				name:name,
				state:state
				)
			city.save()
		}
		return city;
	}
	
	
    def serviceMethod() {

    }
}
