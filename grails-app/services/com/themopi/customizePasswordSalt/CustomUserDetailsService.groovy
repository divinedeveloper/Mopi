package com.themopi.customizePasswordSalt

import grails.plugin.springsecurity.userdetails.GormUserDetailsService
import grails.plugin.springsecurity.userdetails.GrailsUser
import grails.transaction.Transactional
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.userdetails.GrailsUser
import grails.plugin.springsecurity.userdetails.GrailsUserDetailsService
import org.springframework.security.core.authority.GrantedAuthorityImpl
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException

import com.themopi.account.User
import com.themopi.customizePasswordSalt.CustomUserDetails;

@Transactional
class CustomUserDetailsService extends GormUserDetailsService {

 static final List NO_ROLES = [new GrantedAuthorityImpl(SpringSecurityUtils.NO_ROLE)]

   UserDetails loadUserByUsername(String username, boolean loadRoles)
			throws UsernameNotFoundException {
				return loadUserByUsername(username)
   }

   UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

	  User.withTransaction { status ->

		 User user = User.findByUsername(username)
		 if(user == null){
			 user = User.findByEmail(username)
		 }
		 if (!user) throw new UsernameNotFoundException(
					  'User not found', username)

		 def authorities = user.authorities.collect {
			 new GrantedAuthorityImpl(it.authority)
		 }

		 return new CustomUserDetails(user.username, user.password, user.enabled,
			!user.accountExpired, !user.passwordExpired,
			!user.accountLocked, authorities ?: NO_ROLES, user.id,
			user.salt)
	  }
   }
}

