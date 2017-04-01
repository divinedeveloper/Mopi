package com.themopi.account

import java.util.Map;

import com.themopi.survey.Gift
import com.themopi.user.*
import com.themopi.user.Profile


/**
 * @author nisos
 * This Domain Class contains the user account information
 */
class User {

	transient springSecurityService

	String username,name,gPlusAccessToken,fbAccessToken,tokenValue
	String password,fbUID,gPlusUID
	String email
	String roleAuthority
	String salt
	
	Long roleId
	
	
	boolean fbLogin,gPlusLogin,isActive
	boolean enabled = true
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired
	boolean isEligibleForGift = false
	
	Date lastUpdated
	Date dateCreated
	Date deletedAt

	Profile profile
	Address address
	List<Gift> gift = []
	
	int surveyCount = 0
	
	static transients = ['springSecurityService']
	static embedded = ['address','profile','gift']
	static hasMany = [gift:Gift,followers:Followers]

	static constraints = {
		username blank: false, unique: true
		password blank: true
		name blank:false
		gPlusAccessToken nullable:true
		fbAccessToken nullable:true
		fbUID	nullable:true
		gPlusUID nullable:true
		email	blank:false ,email:true , unique: true
		deletedAt nullable:true
		fbLogin nullable:true
		gPlusLogin nullable:true
		tokenValue nullable:true
		address nullable:true
		profile nullable:true
		roleAuthority nullable:true
		roleId nullable:true
			
	}

	static mapping = {
		password column: '`password`'
	}

	Set<Role> getAuthorities() {
		UserRole.findAllByUser(this).collect { it.role }
	}

	String getSalt() {
		if (!this.salt) {
			this.salt = this.email //will return email for the first time , if there is no salt but this condition is not achievable
		}
		this.salt
	}
	/*def beforeInsert() {
		encodePassword()
	}*/

/*	def beforeUpdate() {
		if (isDirty('password')) {
			encodePassword()
		}
	}*/

	/*protected void encodePassword() {
		password = springSecurityService?.passwordEncoder ? springSecurityService.encodePassword(password) : password
	}*/
}
