package com.themopi.apis.security

import com.google.api.client.auth.oauth2.TokenResponseException
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse
import com.google.api.client.http.HttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson.JacksonFactory
import com.google.api.services.oauth2.Oauth2
import com.google.api.services.oauth2.Oauth2.Tokeninfo
import com.google.api.services.oauth2.model.Userinfoplus
import com.google.api.services.oauth2.model.Tokeninfo
import com.restfb.DefaultFacebookClient
import com.restfb.FacebookClient
import com.restfb.types.User
import com.themopi.account.AuthenticationToken
import com.themopi.exceptions.AccountException
import org.json.JSONObject
import org.springframework.security.access.annotation.Secured

import grails.converters.JSON


@Secured('permitAll')
class LoginController {
    def userService
	def grailsApplication
    def index() { }
	
	private int getAsciValue(String str){
			StringBuilder sb = new StringBuilder();
			for (char c : str.toCharArray())
			sb.append((int)c);
		
			BigInteger mInt = new BigInteger(sb.toString());
			return mInt
	}
	
	def login() {
	try{
		def returnObject = [:]
		String socialId = request.getJSON().uid;
		String code = request.getJSON().token;
		String email = request.getJSON().email
		String username = null
		println "json ==== "+request.getJSON()
		com.themopi.account.User mopiUser = com.themopi.account.User.findByEmail(email)
		
		Map userDetails = [:]
		userDetails.email = email
		userDetails.id = socialId
		
		if(email.contains("@") && email.contains(".")){
		String emailAscii = getAsciValue(email.substring(email.indexOf('@')+1, email.lastIndexOf('.')-1))
		username = email.substring(0, email.indexOf('@'))+emailAscii
		println "username === "+username+"  "+email.substring(email.indexOf('@')+1, email.lastIndexOf('.'))
		}else{
		 // throw exception for invalid email
		    int errorCode = grailsApplication.config.customExceptions.account.fourZeroFour.invalidEmail.errorCode
			int status = grailsApplication.config.customExceptions.account.fourZeroFour.invalidEmail.status
			String message = grailsApplication.config.customExceptions.account.fourZeroFour.invalidEmail.message
			String extendedMessage = grailsApplication.config.customExceptions.account.fourZeroFour.invalidEmail.extendedMessage
			String moreInfo = grailsApplication.config.customExceptions.account.fourZeroFour.invalidEmail.moreInfo

			throw new AccountException(status,errorCode,message,extendedMessage,moreInfo)
		}
		
			
		if(params.providerString.equals("google")){
			
			GoogleCredential credential = new GoogleCredential().setAccessToken(code);
			Oauth2 oauth2 = new Oauth2.Builder(new NetHttpTransport(), new JacksonFactory(), credential).setApplicationName(
					 "Oauth2").build();
			Userinfoplus userInfo = oauth2.userinfo().get().execute();
			  Tokeninfo tokenInfo = oauth2.tokeninfo()
				  .setAccessToken(code).execute();
			userInfo.toPrettyString();
			userDetails.username = username
			userDetails.name = userInfo.getName()
			userDetails.loginType = "google"
			userDetails.accessToken =code
			if(userInfo != null && userInfo.getId() == socialId){
				if(mopiUser == null){
					mopiUser = com.themopi.account.User.findByGPlusUID(socialId)
				}
				
				if(mopiUser == null){
					mopiUser = userService.addSocialUser(userDetails)
					returnObject.error = false
					returnObject.user = mopiUser
					if(mopiUser != null){
					returnObject.authorization = getAuthenticationTokenObject(username)
					}
					println returnObject
					response.setStatus(200)
					render returnObject as JSON
				}else{
				    userService.updateSocialUser(mopiUser,userDetails)
					returnObject.error = false
					returnObject.user = mopiUser
					if(mopiUser != null){
					returnObject.authorization = getAuthenticationTokenObject(username)
					}
					println returnObject
					response.setStatus(200)
					render returnObject as JSON
				}
			}else{
			int errorCode = grailsApplication.config.customExceptions.account.fourZeroFour.invalidAccessToken.errorCode
			int status = grailsApplication.config.customExceptions.account.fourZeroFour.invalidAccessToken.status
			String message = grailsApplication.config.customExceptions.account.fourZeroFour.invalidAccessToken.message
			String extendedMessage = grailsApplication.config.customExceptions.account.fourZeroFour.invalidAccessToken.extendedMessage
			String moreInfo = grailsApplication.config.customExceptions.account.fourZeroFour.invalidAccessToken.moreInfo
	
			throw new AccountException(status,errorCode,message,extendedMessage,moreInfo)
			}
			
		}else if(params.providerString.equals("facebook")){
		//User name: User[about=null bio=null birthday=null birthdayAsDate=null currency=null education=[] email=tushariscoolster@gmail.com favoriteAthletes=[] favoriteTeams=[] firstName=Tushar gender=male hometown=null hometownName=null id=10205656185590941 interestedIn=[] languages=[] lastName=Borole link=https://www.facebook.com/app_scoped_user_id/10205656185590941/ locale=en_US location=null meetingFor=[] metadata=null middleName=null name=Tushar Borole picture=null political=null quotes=null relationshipStatus=null religion=null significantOther=null sports=[] thirdPartyId=null timezone=5.5 type=null updatedTime=Tue Oct 07 22:08:02 IST 2014 username=null verified=true website=null work=[]]

			FacebookClient facebookClient = new DefaultFacebookClient(code, grailsApplication.config.myapp.facebook.secret);
			User user = facebookClient.fetchObject("me", User.class);
			println "user obtained from facebook is "+user //remove this after unit tests
			userDetails.username = username
			userDetails.name = user.name
			userDetails.loginType = "facebook"
			userDetails.accessToken =code
					
			if(user != null && user.id == socialId){
				if(mopiUser == null){
					mopiUser = com.themopi.account.User.findByFbUID(socialId)
				}
				if(mopiUser == null){
					mopiUser = userService.addSocialUser(userDetails)
					returnObject.error = false
					returnObject.user = mopiUser
					if(mopiUser != null){
					returnObject.authorization = getAuthenticationTokenObject(username)
					}
					println returnObject
					response.setStatus(200)
					render returnObject as JSON
				}else{
					userService.updateSocialUser(mopiUser,userDetails)
					returnObject.error = false
					returnObject.user = mopiUser
					if(mopiUser != null){
					returnObject.authorization = getAuthenticationTokenObject(username)
					}
					println returnObject
					response.setStatus(200)
					render returnObject as JSON
				}
			}else{
			int errorCode = grailsApplication.config.customExceptions.account.fourZeroFour.invalidAccessToken.errorCode
			int status = grailsApplication.config.customExceptions.account.fourZeroFour.invalidAccessToken.status
			String message = grailsApplication.config.customExceptions.account.fourZeroFour.invalidAccessToken.message
			String extendedMessage = grailsApplication.config.customExceptions.account.fourZeroFour.invalidAccessToken.extendedMessage
			String moreInfo = grailsApplication.config.customExceptions.account.fourZeroFour.invalidAccessToken.moreInfo
	
			throw new AccountException(status,errorCode,message,extendedMessage,moreInfo)
			
			}
		}else{
		int errorCode = grailsApplication.config.customExceptions.account.fourZeroFour.socailLoginType.errorCode
		int status = grailsApplication.config.customExceptions.account.fourZeroFour.socailLoginType.status
		String message = grailsApplication.config.customExceptions.account.fourZeroFour.socailLoginType.message
		String extendedMessage = grailsApplication.config.customExceptions.account.fourZeroFour.socailLoginType.extendedMessage
		String moreInfo = grailsApplication.config.customExceptions.account.fourZeroFour.socailLoginType.moreInfo

		throw new AccountException(status,errorCode,message,extendedMessage,moreInfo)

		}
	}catch(AccountException e){
			log.error e.errorResponse()
			response.setStatus(e.errorResponse().status)
			render e.errorResponse()
		}
		
	}
	
	def getAuthenticationTokenObject(username){
		AuthenticationToken authToken = new AuthenticationToken(
			username:username,
			authToken: java.util.UUID.randomUUID()
			)
		authToken.save(flush:true)
		return authToken
	}
	
}
