package com.themopi.customizePasswordSalt

import com.odobo.grails.plugin.springsecurity.rest.token.storage.GormTokenStorageService
import com.odobo.grails.plugin.springsecurity.rest.token.storage.TokenNotFoundException
import com.odobo.grails.plugin.springsecurity.rest.token.storage.TokenStorageService;

import grails.transaction.Transactional
import grails.plugin.springsecurity.SpringSecurityUtils

import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.plugins.support.aware.GrailsApplicationAware
import org.springframework.security.core.userdetails.UserDetailsService
import com.themopi.account.*


@Transactional
class CustomGormTokenStorageService implements TokenStorageService, GrailsApplicationAware {
//This service is not used any where
	GrailsApplication grailsApplication
	
		CustomUserDetailsService customUserDetailsService
	
		Object loadUserByToken(String tokenValue) throws TokenNotFoundException {
			def conf = SpringSecurityUtils.securityConfig
			String usernamePropertyName = conf.rest.token.storage.gorm.usernamePropertyName
			def existingToken = findExistingToken(tokenValue)
	
			if (existingToken) {
				def username = existingToken."${usernamePropertyName}"
				
				return customUserDetailsService.loadUserByUsername(username)
			}
	
			throw new TokenNotFoundException("Token hjj  hjhhjhj ${tokenValue} not found")
	
		}
		
		void storeToken(String tokenValue, Object principal) {
			def conf = SpringSecurityUtils.securityConfig
			String tokenClassName = conf.rest.token.storage.gorm.tokenDomainClassName
			String tokenValuePropertyName = conf.rest.token.storage.gorm.tokenValuePropertyName
			String usernamePropertyName = conf.rest.token.storage.gorm.usernamePropertyName
			def dc = grailsApplication.getClassForName(tokenClassName)
			println "dc===================="+dc
			//TODO check at startup, not here
			if (!dc) {
				throw new IllegalArgumentException("The specified token domain class '$tokenClassName' is not a domain class ")
			}
	
			dc.withTransaction { status ->
				def newTokenObject = dc.newInstance((tokenValuePropertyName): tokenValue, (usernamePropertyName): principal.username)
				println "newTokenObject === "+newTokenObject
				newTokenObject.save(flush:true)
				
					newTokenObject.errors.each{
					println	"==================================="+it
					
				}
					println "newTokenObject ===== "+newTokenObject
			}
		}
	
		void removeToken(String tokenValue) throws TokenNotFoundException {
			def existingToken = findExistingToken(tokenValue)
	
			if (existingToken) {
				existingToken.delete()
			} else {
				throw new TokenNotFoundException("Token ${tokenValue} not found")
			}
	
		}
		
		
		private findExistingToken(String tokenValue) {
			    println AuthenticationToken.findWhere((tokenValuePropertyName): tokenValue)
				return AuthenticationToken.findWhere((tokenValuePropertyName): tokenValue)
			
		}
    def serviceMethod() {

    }
}




