package com.themopi.apis.security

import grails.plugin.asyncmail.AsynchronousMailService
import grails.transaction.Transactional

import org.springframework.security.access.annotation.Secured

import com.themopi.account.User
import com.themopi.account.VerificationToken
import com.themopi.exceptions.AccountException


@Transactional
@Secured('permitAll')
class ForgotPasswordService {
	static transactional = 'mongo'
	
	/**
	 * This class has CRUD functionality for forgotpassword
	 */
	
	def sendMailService
	def grailsApplication
	
	/**
	 * saves verification token for that particular user
	 * @param email
	 * @param verificationToken
	 * @return
	 */
	def saveVerificationToken(String email, String verificationToken, User user) {
		log.debug(verificationToken)
		VerificationToken verificationTokenObject = new VerificationToken(email: email, verificationToken: verificationToken, user: user)
		if(!verificationTokenObject.save()){
			verificationTokenObject.errors.each{
				println it
			}
			int errorCode =  grailsApplication.config.customExceptions.account.fourZeroFour.verificationToken.errorCode
			int status =  grailsApplication.config.customExceptions.account.fourZeroFour.verificationToken.status
			def message =  grailsApplication.config.customExceptions.account.fourZeroFour.verificationToken.message
			def extendedMessage =  grailsApplication.config.customExceptions.account.fourZeroFour.verificationToken.extendedMessage
			def moreInfo =  grailsApplication.config.customExceptions.account.fourZeroFour.verificationToken.moreInfo
			log.debug("errorCode="+errorCode+"  message="+message)
			throw new AccountException(status,errorCode,message,extendedMessage,moreInfo)
		}
	}

    /**
     * send email to user with link 
     * @param email
     * @param link
     * @return
     */
	def sendMailToUser(String email, String link){
		String subject = grailsApplication.config.myapp.forgotPasswordSubject
		def body = grailsApplication.config.myapp.forgotPasswordEmail 
		
		if(body.contains('$')){
			body = evaluate(body, [link:link])
			println "body ============ "+body
		}
		
		sendMailService.mail(grailsApplication.config.myapp.email.from,email,subject,body.toString())
					
	/*	asynchronousMailService.sendMail {
			from 'dnr@rkgtechllc.com'
			to email
			subject 'Forgot password mail';
			html '<body><a href='+link+'>Click Here</a></body>';
//			attachBytes 'test.txt', 'text/plain', byteBuffer;
			// Additional asynchronous parameters (optional)
//			beginDate new Date(System.currentTimeMillis()+60000)    // Starts after one minute, default current date
//			endDate new Date(System.currentTimeMillis()+3600000)   // Must be sent in one hour, default infinity
//			maxAttemptsCount 3;   // Max 3 attempts to send, default 1
//			attemptInterval 300000;    // Minimum five minutes between attempts, default 300000 ms
//			delete true;    // Marks the message for deleting after sent
//			immediate true;    // Run the send job after the message was created
//			priority 10;   // If priority is greater then message will be sent faster
		}*/
	}
	
	protected String evaluate(s, binding) {
		def engine = new groovy.text.SimpleTemplateEngine()
		def template = engine.createTemplate(s).make(binding)
		println "template == "+template.toString()
		return template.toString()
	}
}
