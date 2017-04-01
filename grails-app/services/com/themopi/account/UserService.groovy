package com.themopi.account

import grails.plugin.asyncmail.AsynchronousMailService
import grails.plugin.springsecurity.authentication.dao.NullSaltSource
import grails.transaction.Transactional
import groovy.text.SimpleTemplateEngine

import java.text.SimpleDateFormat

import com.themopi.*
import com.themopi.exceptions.AccountException
import com.themopi.exceptions.PasswordChangeExceptio
import com.themopi.location.City
import com.themopi.location.Country
import com.themopi.location.State
import com.themopi.user.Address
import com.themopi.user.Profile
//import com.themopi.account.User


@Transactional
class UserService {

	/**
	 * This class contains CRUD functionality for user
	 * @param body
	 * @return
	 */
	
	static transactional = 'mongo'
	def grailsApplication
	def saltSource
	def springSecurityService
	def locationService
	def forgotPasswordService
	def sendMailService
	def dashboardService
	
	AsynchronousMailService asynchronousMailService
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	protected String evaluate(s, binding) {
		def engine = new groovy.text.SimpleTemplateEngine()
		def template = engine.createTemplate(s).make(binding)
		println "template == "+template.toString()
		return template.toString()
	}
	
	
	def checkIfAUthorizedtoCreateUserOfSpecifiedRole(tokenValue,role){
		AuthenticationToken authObj = AuthenticationToken.findByAuthToken(tokenValue);
		User loggedInUser = User.findByUsername(authObj?.username);
		UserRole loggedInUserRole = UserRole.findByUser(loggedInUser);
		if((loggedInUserRole == null && role?.authority != "ROLE_USER") || ((loggedInUserRole?.role?.authority != "ROLE_SUPERADMIN") && (role?.authority != "ROLE_USER"))){
			int errorCode = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.errorCode
			int status = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.status
			String message = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.message
			String extendedMessage = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.extendedMessage
			String moreInfo = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.moreInfo

			throw new AccountException(status,errorCode,message,extendedMessage,moreInfo)
		}
		
	}
	
	
	/*
	 *  function check the email availablity (call function checkemail)
	 *  validate and encode password (call function validateAndEncodePassword
	 *  accepts json sent in email to create user (email , password , password2 and name is compulsory)
	 *  throw AccountException if there is any exception
	 *  return user object when successfully created
	 */

	def addUser(body,tokenValue){

		def role
		boolean checkEmail = checkEmail(body.email);
		boolean checkUsername = checkUsername(body.username)
		String password = validateAndEncodePassword(body.password,body.password2,body.email)
		if(body.roleId == "" || body.roleId == null){
			role = findRoleByAuthority("ROLE_USER")
		}else{
			
			println "roleId == "+body
			role = findRoleByRoleID(body.roleId?.toLong())
//			checkIfAUthorizedtoCreateUserOfSpecifiedRole(tokenValue,role)
			println "role == "+role
			if(role == null){
				int errorCode = grailsApplication.config.customExceptions.account.fourZeroFour.role.errorCode
				int status = grailsApplication.config.customExceptions.account.fourZeroFour.role.status
				String message = grailsApplication.config.customExceptions.account.fourZeroFour.role.message
				String extendedMessage = grailsApplication.config.customExceptions.account.fourZeroFour.role.extendedMessage
				String moreInfo = grailsApplication.config.customExceptions.account.fourZeroFour.role.moreInfo

				throw new AccountException(status,errorCode,message,extendedMessage,moreInfo)
			}
			checkIfAUthorizedtoCreateUserOfSpecifiedRole(tokenValue,role)
		}
		println role
		if(checkEmail && checkUsername){
			if(role.authority == "ROLE_USER"){
				User user = new User(
						email:body.email,
						password:password,
						name:body.name,
						accountLocked: false,
						enabled: true,
						accountExpired:false,
						passwordExpired:false,
						isActive:true,
						username:body.username,
						tokenValue:null,
						roleAuthority:role?.authority,
						roleId: role?.id,
						salt:body.email
						)

				if(!user.save()){
					user.errors.each{
						println  it
					}
					int errorCode = grailsApplication.config.customExceptions.account.fourZeroZero.user.errorCode
					int status = grailsApplication.config.customExceptions.account.fourZeroZero.user.status
					String message = grailsApplication.config.customExceptions.account.fourZeroZero.user.message
					String extendedMessage = grailsApplication.config.customExceptions.account.fourZeroZero.user.extendedMessage
					String moreInfo = grailsApplication.config.customExceptions.account.fourZeroZero.user.moreInfo

					throw new  AccountException(status,errorCode,message,extendedMessage,moreInfo)
				}else{
					boolean roleAdded = addRoleToUser(user,role);
					if(roleAdded){
						generateRegistrationToken(user) //sending mopi user registration email and if he clicks on it becomes eligible for gifts
						return user
					}
				}
			}else{
				User user = new User(
						email:body.email,
						password:password,
						name:body.name,
						accountLocked: true,
						enabled: false,
						accountExpired:false,
						passwordExpired:false,
						isActive:false, //it will be true after clicking on registration link for admin and sponsor
						username:body.username,
						tokenValue:null,
						roleAuthority:role?.authority,
						roleId: role?.id,
						salt:body.email
						)

				if(!user.save()){
					user.errors.each{
						println  it
					}
					int errorCode = grailsApplication.config.customExceptions.account.fourZeroZero.user.errorCode
					int status = grailsApplication.config.customExceptions.account.fourZeroZero.user.status
					String message = grailsApplication.config.customExceptions.account.fourZeroZero.user.message
					String extendedMessage = grailsApplication.config.customExceptions.account.fourZeroZero.user.extendedMessage
					String moreInfo = grailsApplication.config.customExceptions.account.fourZeroZero.user.moreInfo

					throw new  AccountException(status,errorCode,message,extendedMessage,moreInfo)
				}else{
					boolean roleAdded = addRoleToUser(user,role);
					if(roleAdded){
						generateRegistrationToken(user)
						return user
					}else{
					    user.delete()
						int errorCode = grailsApplication.config.customExceptions.account.fourZeroZero.user.errorCode
						int status = grailsApplication.config.customExceptions.account.fourZeroZero.user.status
						String message = grailsApplication.config.customExceptions.account.fourZeroZero.user.message
						String extendedMessage = grailsApplication.config.customExceptions.account.fourZeroZero.user.extendedMessage
						String moreInfo = grailsApplication.config.customExceptions.account.fourZeroZero.user.moreInfo
	
						throw new  AccountException(status,errorCode,message,extendedMessage,moreInfo)
					}
				}
			}
		}
	}
	
	
	def addSocialUser(body){
		def role
		boolean checkEmail = checkEmail(body.email);
		boolean checkUsername = checkUsername(body.username)
		role = findRoleByAuthority("ROLE_USER")
		println role
		if(checkEmail && checkUsername){
				User user = new User(
						email:body.email,
						name:body.name,
						accountLocked: false,
						enabled: true,
						accountExpired:false,
						password:body.accessToken,
						passwordExpired:false,
						isActive:true,
						username:body.username,
						tokenValue:null,
						roleAuthority:role?.authority,
						roleId: role?.id,
						salt:body.email
						)

				if(!user.save()){
					user.errors.each{
						println  it
					}
					int errorCode = grailsApplication.config.customExceptions.account.fourZeroZero.user.errorCode
					int status = grailsApplication.config.customExceptions.account.fourZeroZero.user.status
					String message = grailsApplication.config.customExceptions.account.fourZeroZero.user.message
					String extendedMessage = grailsApplication.config.customExceptions.account.fourZeroZero.user.extendedMessage
					String moreInfo = grailsApplication.config.customExceptions.account.fourZeroZero.user.moreInfo

					throw new  AccountException(status,errorCode,message,extendedMessage,moreInfo)
				}else{
					boolean roleAdded = addRoleToUser(user,role);
					if(roleAdded){
						generateRegistrationToken(user) //sending mopi user registration email and if he clicks on it becomes eligible for gifts
						updateSocialUser(user,body)
						return user
					}
				}
			
		}
	}
	
	
	def updateSocialUser(mopiUser,userDetails){
		if(userDetails.loginType == "google"){
			mopiUser.gPlusLogin = true
			mopiUser.gPlusUID = userDetails.id
			mopiUser.gPlusAccessToken = userDetails.accessToken
		}else{
			mopiUser.fbLogin = true
			mopiUser.fbUID = userDetails.id
			mopiUser.fbAccessToken = userDetails.accessToken
		}
		mopiUser.save()
	}
	
	
	def generateRegistrationToken(user){
		def regUniqueToken = UUID.randomUUID()
		RegistrationToken regToken = new RegistrationToken(
			 regToken:regUniqueToken,
			 user:user
			)
		if(!regToken.save()){
			user.delete(flush:true)
			int errorCode = grailsApplication.config.customExceptions.account.fourZeroZero.user.errorCode
			int status = grailsApplication.config.customExceptions.account.fourZeroZero.user.status
			String message = grailsApplication.config.customExceptions.account.fourZeroZero.user.message
			String extendedMessage = grailsApplication.config.customExceptions.account.fourZeroZero.user.extendedMessage
			String moreInfo = grailsApplication.config.customExceptions.account.fourZeroZero.user.moreInfo

			throw new  AccountException(status,errorCode,message,extendedMessage,moreInfo)
		}else{
			String email = user.email
			String link = grailsApplication.config.myapp.baseUrl.toString() + grailsApplication.config.myapp.verifyRegistration.toString() + regUniqueToken
			String subject = grailsApplication.config.myapp.registrationSubject
			def body = grailsApplication.config.myapp.registrationEmail
			println "body ============ "+body
			
			if(body.contains('$')){
				body = evaluate(body, [link:link])
				println "body ============ "+body
			}
			sendMailService.mail(grailsApplication.config.myapp.email.from,email,subject,body.toString())	
		
		}
	}
	
	def verifyRegistration(token){
		RegistrationToken registrationToken = RegistrationToken.findByRegToken(token)
		if(registrationToken != null && registrationToken?.user != null){
			println "user account  = "+registrationToken.user.accountLocked
			registrationToken.user.accountLocked = false
			registrationToken.user.enabled = true
			registrationToken.user.isActive = true //setting active to true for admin and sponsors after verifying email
			registrationToken.user.isEligibleForGift = true //making user eligible for gifts
			if(registrationToken.user.save(flush:true)){
//				println "registrationToken.user.address.country = "+registrationToken?.user?.address?.country
//				println "gender === "+registrationToken.user.profile.gender.toString()
//				println "code  ==  "+registrationToken.user.address.country.code
				
				if(registrationToken?.user?.roleAuthority == "ROLE_SPONSEREDADMIN")
				dashboardService.addUserDistribution(registrationToken?.user?.address?.country,registrationToken?.user?.profile?.gender.toString(),registrationToken?.user?.address?.country?.code)
			
				registrationToken.delete(flush:true)
				return true
			}
			//gender,country,prevCountry,prevGender,user,body.countryCode
			//	dashboardService.addUserDistribution(registrationToken.user.profile.gender,registrationToken.user.address.country,registrationToken.user.address.country,null,registrationToken.user,registrationToken.user.address.country.code)
		}
		
		int status = grailsApplication.config.customExceptions.account.fourZeroFour.registration.status
		int errorCode = grailsApplication.config.customExceptions.account.fourZeroFour.registration.errorCode
		String message = grailsApplication.config.customExceptions.account.fourZeroFour.registration.message
		String extendedMessage = grailsApplication.config.customExceptions.account.fourZeroFour.registration.extendedMessage
		String moreInfo = grailsApplication.config.customExceptions.account.fourZeroFour.registration.moreInfo

		throw new AccountException(status,errorCode,message,extendedMessage,moreInfo)
		
	}

	/*
	 * function to associate role to a user
	 */
	boolean addRoleToUser(user,role){
		int errorCode = grailsApplication.config.customExceptions.account.fourZeroZero.userRole.errorCode
		int status = grailsApplication.config.customExceptions.account.fourZeroZero.userRole.status
		String message = grailsApplication.config.customExceptions.account.fourZeroZero.userRole.message
		String extendedMessage = grailsApplication.config.customExceptions.account.fourZeroZero.userRole.extendedMessage
		String moreInfo = grailsApplication.config.customExceptions.account.fourZeroZero.userRole.moreInfo

		UserRole userRole = new UserRole(user:user,role:role)
		println  userRole
		user.roleAuthority = role.authority
		user.roleId = role.id
		user.save()
		if(!userRole.save()){
			userRole.errors.each{
				println  it
			}
			user.delete(flush:true)
			throw new AccountException(status,errorCode,message,extendedMessage,moreInfo)
		}else{
			return true
		}
	}


	/*
	 * function to find role using Id
	 */
	Role findRoleByRoleID(roleId){
		Role role = Role.findById(roleId);
		return role;
	}

	/*
	 * function to find role using Authority
	 */
	Role findRoleByAuthority(authority){
		Role role = Role.findByAuthority(authority)
		return role
	}

	
	boolean checkUsername(username){
		println  "username ======= "+username
		int errorCode = grailsApplication.config.customExceptions.account.fourZeroNine.username.errorCode
		int status = grailsApplication.config.customExceptions.account.fourZeroNine.username.status
		String message = grailsApplication.config.customExceptions.account.fourZeroNine.username.message
		String extendedMessage = grailsApplication.config.customExceptions.account.fourZeroNine.username.extendedMessage
		String moreInfo = grailsApplication.config.customExceptions.account.fourZeroNine.username.moreInfo

		User user = User.findByUsername(username)
		println  "user = "+user
		if(user == null){
			return true
		}else{
			throw new AccountException(status,errorCode,message,extendedMessage,moreInfo)
		}
	}
	/*
	 * function to check email availability to create account.
	 * accepts: email
	 * It returns boolean value :
	 * true : email is available
	 * false : email is not available
	 */

	boolean checkEmail(email){
		println  "email = "+email
		int errorCode = grailsApplication.config.customExceptions.account.fourZeroNine.userCreate.errorCode
		int status = grailsApplication.config.customExceptions.account.fourZeroNine.userCreate.status
		String message = grailsApplication.config.customExceptions.account.fourZeroNine.userCreate.message
		String extendedMessage = grailsApplication.config.customExceptions.account.fourZeroNine.userCreate.extendedMessage
		String moreInfo = grailsApplication.config.customExceptions.account.fourZeroNine.userCreate.moreInfo

		User user = User.findByEmail(email)
		println  "user = "+user
		if(user == null){
			return true
		}else{
			throw new AccountException(status,errorCode,message,extendedMessage,moreInfo)
		}
	}

	/*
	 * This function validate the password (call validatePassword()), if valid encode the password using email as salt
	 */
	String validateAndEncodePassword(password,password2,email){
		String encodedPassword
		boolean validate = validatePassword(password,password2);
		if(validate){
			String salt = saltSource instanceof NullSaltSource ? null : email
			encodedPassword = springSecurityService.encodePassword(password,salt)
		}
		return encodedPassword
	}

	/*
	 * This function validate the password
	 * accept password and password2
	 * return true if password is valid else throw new AccountException
	 */
	boolean validatePassword(password,password2){
		println  "password = "+password+" password2 = "+password2
		if((password == password2)&&(password != null || password != "")){
			return true
		}else{
			int errorCode = grailsApplication.config.customExceptions.account.fourZeroZero.user.errorCode
			int status = grailsApplication.config.customExceptions.account.fourZeroZero.user.status
			String message = grailsApplication.config.customExceptions.account.fourZeroZero.user.message
			String extendedMessage = grailsApplication.config.customExceptions.account.fourZeroZero.user.extendedMessage
			String moreInfo = grailsApplication.config.customExceptions.account.fourZeroZero.user.moreInfo

			throw new AccountException(status,errorCode,message,extendedMessage,moreInfo)
		}
	}

	/*
	 * function to delete User
	 *
	 */
	def deleteUser(userId){
		User user = User.findByIdAndIsActive(userId,true)
		if(user == null){
			int errorCode = grailsApplication.config.customExceptions.account.fourZeroFour.user.errorCode
			int status = grailsApplication.config.customExceptions.account.fourZeroFour.user.status
			String message = grailsApplication.config.customExceptions.account.fourZeroFour.user.message
			String extendedMessage = grailsApplication.config.customExceptions.account.fourZeroFour.user.extendedMessage
			String moreInfo = grailsApplication.config.customExceptions.account.fourZeroFour.user.moreInfo

			throw new AccountException(status,errorCode,message,extendedMessage,moreInfo)
		}else{
			user.isActive = false;
			user.enabled = false
			user.accountExpired = true
			user.accountLocked = true
			user.deletedAt = new Date();
			user.save(flush:true)
		}
	}

	/*
	 * function to update user details
	 * Accepts userId as params and User update details as Json Payload
	 *
	 */
	def updateUser(userId,body,tokenValue){
		
		String gender ,prevGender
		Country country = null,prevCountry = null
		
		//user can be updated by only himself and superadmin
		def userRole = getUserAndRoleByAuthToken(tokenValue)
		
		if(!(userRole?.role?.authority == "ROLE_SUPERADMIN" || userRole?.user?.id == userId?.toLong())){
			int errorCode = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.errorCode
			int status = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.status
			String message = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.message
			String extendedMessage = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.extendedMessage
			String moreInfo = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.moreInfo

			throw new AccountException(status,errorCode,message,extendedMessage,moreInfo)
			
		}
		
			
		User user = User.findById(userId)
		AuthenticationToken authObj = AuthenticationToken.findByUsername(user?.username);
		
		if(user == null){
			int errorCode = grailsApplication.config.customExceptions.account.fourZeroFour.user.errorCode
			int status = grailsApplication.config.customExceptions.account.fourZeroFour.user.status
			String message = grailsApplication.config.customExceptions.account.fourZeroFour.user.message
			String extendedMessage = grailsApplication.config.customExceptions.account.fourZeroFour.user.extendedMessage
			String moreInfo = grailsApplication.config.customExceptions.account.fourZeroFour.user.moreInfo

			throw new AccountException(status,errorCode,message,extendedMessage,moreInfo)
		}else{
		    
			gender = body.isNull('gender')?null:body.gender
			prevGender = user?.profile?.gender?.toString()
			
			println "country t == "+user?.address?.embedCountry
			println "country tx == "+user?.address?.country
			prevCountry = Country.findByName(user?.address?.embedCountry)
			
			if(user.email != body.email){
				checkEmail(body.email)
			}
			if(user.username  != body.username){
				checkUsername(body.username)
			}
			def oldEmail = user.email
			def newEmail = body.email
			user = updateProfileObject(user,body)
			user = updateAddressObject(user,body)
			println "user after profile and address == "+user
			user.email = body.email
			user.name = body.name
			user.username = body.username
			authObj?.username = body.username
			authObj?.save()
			//			user.isActive= (body.isNull('status'))?true:body.status
			user.isActive= (user?.roleAuthority == "ROLE_USER")?true:body.status
			if(!user.save(flush:true)){
				user.errors.each{
					println it
				}
				int errorCode = grailsApplication.config.customExceptions.account.fourZeroZero.user.errorCode
				int status = grailsApplication.config.customExceptions.account.fourZeroZero.user.status
				String message = grailsApplication.config.customExceptions.account.fourZeroZero.user.message
				String extendedMessage = grailsApplication.config.customExceptions.account.fourZeroZero.user.extendedMessage
				String moreInfo = grailsApplication.config.customExceptions.account.fourZeroZero.user.moreInfo

				throw new AccountException(status,errorCode,message,extendedMessage,moreInfo)
			}else{
			
				country = Country.findByName(body.country)
				println "country = "+country
				if(user.roleAuthority == "ROLE_SPONSEREDADMIN" || user.roleAuthority == "ROLE_USER"){
					println "gender = "+gender
					println "country = "+country
					println "prevCountry = "+prevCountry
					println "prevGender = "+prevGender
					println "user = "+user
					println "countryCode = "+body.countryCode
					dashboardService.addUserDistribution(gender,country,prevCountry,prevGender,user,body.countryCode)
				}
				if(oldEmail != newEmail){
					def email = [oldEmail,newEmail]
					String subject = grailsApplication.config.myapp.newEmailSubject
					def emailBody = grailsApplication.config.myapp.newEmail
					println "body ============ "+body
					
					if(emailBody.contains('$')){
						emailBody = evaluate(emailBody, [oldEmail:oldEmail,newEmail:newEmail])
						println "body ============ "+emailBody
					}
					sendMailService.mail(grailsApplication.config.myapp.email.from,email,subject,emailBody)
				
				}
			}
			return user
		}
	}
	/*
	 * function to update user's address . throws account exception if address validationfailed
	 *
	 */
	def updateAddressObject(user,body){
		int errorCode = grailsApplication.config.customExceptions.account.fourZeroZero.address.errorCode
		int status = grailsApplication.config.customExceptions.account.fourZeroZero.address.status
		String message = grailsApplication.config.customExceptions.account.fourZeroZero.address.message
		String extendedMessage = grailsApplication.config.customExceptions.account.fourZeroZero.address.extendedMessage
		String moreInfo = grailsApplication.config.customExceptions.account.fourZeroZero.address.moreInfo
		
        def countryName = (body.isNull('country'))?null:body.country
		Country country = locationService.checkCountry(countryName,body.countryCode);
		State 	state = locationService.checkState(country, (body.isNull('state'))?null:body.state);
		City  city = locationService.checkCity(state,(body.isNull('city'))?null:body.city);
		if(user.address == null){
			Address address = new Address(
					homeTown:body.homeTown,
					location:[lat:body.lat,long:body.long],
					city:city,
					state:state,
					country:country,
					embedCountry:country?.name,
					embedState:state?.name,
					embedCity:city?.name
					)
			if(!address.save()){
				address.errors.each{
					println it
				}

				throw new AccountException(status,errorCode,message,extendedMessage,moreInfo)
			}
			user.address = address
		}else{
			Address address = user.address
			address.homeTown = body.homeTown
			address.city = city
			address.state = state
			address.country = country
			address.embedCity = city?.name
			address.embedState = state?.name
			address.embedCountry = country?.name
			address.location = [lat:body.lat,long:body.long]
			if(!address.save()){
				address.errors.each{
					println it
				}
				throw new AccountException(status,errorCode,message,extendedMessage,moreInfo)
			}
			user.address = address

		}
		return user


	}

	/*
	 * function to update user's profile . throws account exception if profile validation failed.
	 *
	 */
	def updateProfileObject(user,body){
		int errorCode = grailsApplication.config.customExceptions.account.fourZeroZero.profile.errorCode
		int status = grailsApplication.config.customExceptions.account.fourZeroZero.profile.status
		String message = grailsApplication.config.customExceptions.account.fourZeroZero.profile.message
		String extendedMessage = grailsApplication.config.customExceptions.account.fourZeroZero.profile.extendedMessage
		String moreInfo = grailsApplication.config.customExceptions.account.fourZeroZero.profile.moreInfo
		println body
		println"body == "+body.dob
		def dob
		if(body.isNull(dob) || body.dob == ""){
			dob  = null
		}
		else{
			dob = formatter.parse(body.dob)
		}
		println dob
		if(user.profile == null){
			
			
			Profile profile = new Profile(
					description:(body.isNull('description'))?null:body.description,
					mobile:(body.isNull('mobile'))?null:body.mobile,  // add check for null
					phonenumber:body.phoneNumber,
					gender:(body.isNull('gender'))?"Unknown":body.gender,
					occupation:(body.isNull('occupation'))?"Others":body.occupation,
					religion:(body.isNull('religion'))?"Others":body.religion,
					incomeLevel:(body.isNull('incomeLevel'))?"Unknown":body.incomeLevel,
					age:(body.isNull('age'))?"Unknown":body.age,
					dob:dob
					)
			if(!profile.save()){
				profile.errors.each{
					println it
				}
				throw new AccountException(status,errorCode,message,extendedMessage,moreInfo)
			}

			user.profile = profile
		}else{
			println "old profile === "
			Profile profile = user.profile
			println profile
			profile.description=(body.isNull('description'))?null:body.description
			profile.mobile= (body.isNull('mobile'))?null:body.mobile //add ternary check
			profile.phoneNumber=body.phoneNumber
			profile.gender= (body.isNull('gender'))?null:body.gender
			profile.occupation=(body.isNull('occupation'))?null:body.occupation
			profile.religion = (body.isNull('religion'))?null:body.religion
			profile.incomeLevel=(body.isNull('incomeLevel'))?"":body.incomeLevel
			profile.age = (body.isNull('age'))?"":body.age
			profile.dob=dob

			if(!profile.save()){
				profile.errors.each{
					println it
				}
				throw new AccountException(status,errorCode,message,extendedMessage,moreInfo)
			}
			user.profile = profile
			
		}
		if(!user.save()){
			user.errors.each{
				println it
			}
		}
		
		return user
	}

	def findUserByUsername(String username){
		println(username)
		int errorCode =  grailsApplication.config.customExceptions.account.fourZeroFour.forgotpassword.errorCode
		int status =  grailsApplication.config.customExceptions.account.fourZeroFour.forgotpassword.status
		def message =  grailsApplication.config.customExceptions.account.fourZeroFour.forgotpassword.message
		def extendedMessage =  grailsApplication.config.customExceptions.account.fourZeroFour.forgotpassword.extendedMessage
		def moreInfo =  grailsApplication.config.customExceptions.account.fourZeroFour.forgotpassword.moreInfo
		println "username  ============ "+username
		User user = User.findByUsername(username)
		println "username  ============ "+user
		if(user == null){
			throw new AccountException(status,errorCode,message,extendedMessage,moreInfo)
			
			}else{
			return user
			
		}
	}
	/**
	 * find user from email
	 * @param email
	 * @return
	 */
	def findUserByEmail(String email){
		println(email)
		int errorCode =  grailsApplication.config.customExceptions.account.fourZeroFour.forgotpassword.errorCode
		int status =  grailsApplication.config.customExceptions.account.fourZeroFour.forgotpassword.status
		def message =  grailsApplication.config.customExceptions.account.fourZeroFour.forgotpassword.message
		def extendedMessage =  grailsApplication.config.customExceptions.account.fourZeroFour.forgotpassword.extendedMessage
		def moreInfo =  grailsApplication.config.customExceptions.account.fourZeroFour.forgotpassword.moreInfo
		User user = User.findByEmail(email)
		if(user == null){
			throw new AccountException(status,errorCode,message,extendedMessage,moreInfo)
		}else{
			return user
		}
	}

	/**
	 * find user from email
	 * @param email
	 * @return
	 */
	def findUserByUserId(UserId){

		int errorCode =  grailsApplication.config.customExceptions.account.fourZeroFour.forgotpassword.errorCode
		int status =  grailsApplication.config.customExceptions.account.fourZeroFour.forgotpassword.status
		def message =  grailsApplication.config.customExceptions.account.fourZeroFour.forgotpassword.message
		def extendedMessage =  grailsApplication.config.customExceptions.account.fourZeroFour.forgotpassword.extendedMessage
		def moreInfo =  grailsApplication.config.customExceptions.account.fourZeroFour.forgotpassword.moreInfo
		User user = User.findById(UserId)
		if(user == null){
			throw new AccountException(status,errorCode,message,extendedMessage,moreInfo)
		}else{
			return user
		}
	}


	/**
	 * find user by verification token
	 * if user not found throw 404 exception
	 * if user found delete verification token
	 * encode and set new password for that user
	 */
	def findUserByVerificationToken(requestParams){
		println requestParams
		VerificationToken tokenObject = VerificationToken.findByVerificationToken(requestParams.token)
		println(tokenObject)
		if(tokenObject == null){
			int errorCode =  grailsApplication.config.customExceptions.account.fourZeroFour.forgotpassword.errorCode
			int status =  grailsApplication.config.customExceptions.account.fourZeroFour.forgotpassword.status
			def message =  grailsApplication.config.customExceptions.account.fourZeroFour.forgotpassword.message
			def extendedMessage =  grailsApplication.config.customExceptions.account.fourZeroFour.forgotpassword.extendedMessage
			def moreInfo =  grailsApplication.config.customExceptions.account.fourZeroFour.forgotpassword.moreInfo
			println("errorCode="+errorCode+"  message="+message)
			throw new AccountException(status,errorCode,message,extendedMessage,moreInfo)
		}else{
			// encode validate and save new password
			String password = validateAndEncodePassword(requestParams.newPassword, requestParams.newPassword2, tokenObject.user.salt)
			User user = tokenObject.user

			user.password = password
			if(!user.save()){
				user.errors.each{
					println it
				}
				int errorCode =  grailsApplication.config.customExceptions.account.fourZeroZero.userPassword.errorCode
				int status =  grailsApplication.config.customExceptions.account.fourZeroZero.userPassword.status
				def message =  grailsApplication.config.customExceptions.account.fourZeroZero.userPassword.message
				def extendedMessage =  grailsApplication.config.customExceptions.account.fourZeroZero.userPassword.extendedMessage
				def moreInfo =  grailsApplication.config.customExceptions.account.fourZeroZero.userPassword.moreInfo
				println("errorCode="+errorCode+"  message="+message)
				throw new AccountException(status,errorCode,message,extendedMessage,moreInfo)
			}
			tokenObject.delete(flush:true)
		}
	}



	/*
	 *  function to get User and Role using X-Auth-Token
	 */
	UserRole getUserAndRoleByAuthToken(String tokenValue){
		AuthenticationToken authObj = AuthenticationToken.findByAuthToken(tokenValue);
		println ("username ===== "+authObj.username)
		User user = findUserByUsername(authObj?.username);
		println ("user ===== "+user.email)
		
		if(user == null){
			int errorCode = grailsApplication.config.customExceptions.account.fourZeroFour.user.errorCode
			int status = grailsApplication.config.customExceptions.account.fourZeroFour.user.status
			String message = grailsApplication.config.customExceptions.account.fourZeroFour.user.message
			String extendedMessage = grailsApplication.config.customExceptions.account.fourZeroFour.user.extendedMessage
			String moreInfo = grailsApplication.config.customExceptions.account.fourZeroFour.user.moreInfo

			throw new AccountException(status,errorCode,message,extendedMessage,moreInfo)
		}
		UserRole userRole = UserRole.findByUser(user);
		return userRole
	}

	
	/**
	 * saves gift verification token for that particular user
	 * @param email
	 * @param verificationToken
	 * @return
	 */
	def saveGiftVerificationToken(String email, String verificationToken, User user) {
		log.debug(verificationToken)
		GiftVerificationToken verificationTokenObject = new GiftVerificationToken(email: email, giftVerificationToken: verificationToken, user: user)
		if(!verificationTokenObject.save()){
			verificationTokenObject.errors.each{
				println it
			}
			int errorCode =  grailsApplication.config.customExceptions.account.fourZeroZero.giftVerificationToken.errorCode
			int status =  grailsApplication.config.customExceptions.account.fourZeroZero.giftVerificationToken.status
			def message =  grailsApplication.config.customExceptions.account.fourZeroZero.giftVerificationToken.message
			def extendedMessage =  grailsApplication.config.customExceptions.account.fourZeroZero.giftVerificationToken.extendedMessage
			def moreInfo =  grailsApplication.config.customExceptions.account.fourZeroZero.giftVerificationToken.moreInfo
			log.debug("errorCode="+errorCode+"  message="+message)
			throw new AccountException(status,errorCode,message,extendedMessage,moreInfo)
		}
	}


	/**
	 * find user by gift verification token
	 * if user not found throw 404 exception
	 * if user found set is eligible for gifts field as true
	 * save that user
	 */
	def userEligibleForGift(String verificationToken){
		println verificationToken
		GiftVerificationToken tokenObject = GiftVerificationToken.findByGiftVerificationToken(verificationToken)
		println(tokenObject)
		if(tokenObject == null){
			int errorCode =  grailsApplication.config.customExceptions.account.fourZeroFour.giftVerificationToken.errorCode
			int status =  grailsApplication.config.customExceptions.account.fourZeroFour.giftVerificationToken.status
			def message =  grailsApplication.config.customExceptions.account.fourZeroFour.giftVerificationToken.message
			def extendedMessage =  grailsApplication.config.customExceptions.account.fourZeroFour.giftVerificationToken.extendedMessage
			def moreInfo =  grailsApplication.config.customExceptions.account.fourZeroFour.giftVerificationToken.moreInfo
			println("errorCode="+errorCode+"  message="+message)
			throw new AccountException(status,errorCode,message,extendedMessage,moreInfo)
		}else{

			User user = tokenObject.user

			user.isEligibleForGift = true
			if(!user.save()){
				user.errors.each{
					println it
				}
				int errorCode =  grailsApplication.config.customExceptions.account.fourZeroZero.userGiftConfirmation.errorCode
				int status =  grailsApplication.config.customExceptions.account.fourZeroZero.userGiftConfirmation.status
				def message =  grailsApplication.config.customExceptions.account.fourZeroZero.userGiftConfirmation.message
				def extendedMessage =  grailsApplication.config.customExceptions.account.fourZeroZero.userGiftConfirmation.extendedMessage
				def moreInfo =  grailsApplication.config.customExceptions.account.fourZeroZero.userGiftConfirmation.moreInfo
				println("errorCode="+errorCode+"  message="+message)
				throw new AccountException(status,errorCode,message,extendedMessage,moreInfo)
			}

		}
	}
	
	/**
	 * perform Inactive / Active state of user 
	 * 
	 * @param tokenValue
	 * @param body JSONPAYLOAD {"status":true/false}
	 * @param userId
	 * @return
	 */
	def updateStatus(tokenValue,body,userId){
		def userRole = getUserAndRoleByAuthToken(tokenValue)
		User user = findUserByUserId(userId)
		if(userRole?.role?.authority == "ROLE_SUPERADMIN" || userRole?.user?.id == userId?.toLong()){
			println "body == "+body.status
//			boolean status = body.status
			Boolean status = new Boolean(body.status)
			
			def newStatus = body.status
			def oldStatus = user.isActive
			if(status == false && user.isActive == true){
								
				dashboardService.substractUserDistribution(user?.address?.country,user?.profile?.gender?.toString(),user?.address?.country?.code)
			}else if(status == true && user.isActive == false){
			
			  dashboardService.addUserDistribution(user?.address?.country,user?.profile?.gender?.toString(),user?.address?.country?.code)
			}
			if(status){
				println "body t == "+body.status
				user?.isActive = true
				user?.AccountLocked =  false
				user?.enabled = true
				
			}else {
				
				user?.isActive = false
				user?.AccountLocked =  true
				user?.enabled = false
				
				List authenticationObjects = AuthenticationToken.findAllByUsername(user.username)
				authenticationObjects.each.{
					it.delete(flush:true)
				}
			}
		}else{
			int errorCode = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.errorCode
			int status = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.status
			String message = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.message
			String extendedMessage = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.extendedMessage
			String moreInfo = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.moreInfo

			throw new AccountException(status,errorCode,message,extendedMessage,moreInfo)
		}
		
		println userRole?.user
		if(!user?.save()){
			user?.errors.each{
				println it
			}
		}
//		return user?.isActive
		return user
	}
	
	def updateUserPassword(tokenValue,newpassword)
	{
		String encodedPassword
		def userRole = getUserAndRoleByAuthToken(tokenValue)
		User user = userRole.user
		println "-----------password--------"+user.password
		String salt = user.salt
		println "salt-"+salt
		encodedPassword = springSecurityService.encodePassword(newpassword,salt)
		println "encodedPassword--"+encodedPassword
		if(encodedPassword==user.password){
			int errorCode = grailsApplication.config.customExceptions.password.fourZeroOne.password.errorCode
			int status = grailsApplication.config.customExceptions.password.fourZeroOne.password.status
			String message = grailsApplication.config.customExceptions.password.fourZeroOne.password.message
			String extendedMessage = grailsApplication.config.customExceptions.password.fourZeroOne.password.extendedMessage
			String moreInfo = grailsApplication.config.customExceptions.password.fourZeroOne.password.moreInfo
			throw new PasswordChangeExceptio(status,errorCode,message,extendedMessage,moreInfo)
		}else{
			user.password=encodedPassword
			if(!user.save()){
				return "Password not change"
			}else{
				println "user password saved----"
				return "Succesfully Password change"
			}
			
		}
	}
}
