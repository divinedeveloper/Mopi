package com.themopi.mail

import grails.plugin.asyncmail.AsynchronousMailService;
import grails.transaction.Transactional

@Transactional
class SendMailService {
	def grailsApplication
    AsynchronousMailService asynchronousMailService
	def mail(fromEmail,email,fromSubject,htmlBody){
		println "grailsApplication.config.email.send = "+grailsApplication.config.email.send
		if(grailsApplication.config.email.send == "on"){
		asynchronousMailService.sendMail {
			from fromEmail
			to email
			subject fromSubject;
			html htmlBody;
			}
		}
	}
    def serviceMethod() {

    }
}
