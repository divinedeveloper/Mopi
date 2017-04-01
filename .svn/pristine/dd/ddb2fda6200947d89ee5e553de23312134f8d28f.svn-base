package com.themopi.apis

import com.themopi.user.Address;

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Address)
class AddressSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "address Domain Test"() {
		Address address = new Address(
			homeTown:"CBD Belapur",
			location:[lat:19.0300657,long:73.03511379999998],
			city:"Navi Mumbai",
			state:"Maharashtra",
			country:"India",
			embedCountry:country?.name,
			embedState:state?.name,
			embedCity:city?.name
			).save(flush:true)
    }
}
