package com.themopi.image

import com.themopi.account.UserRole
import com.themopi.exceptions.ImageUploadException
import com.themopi.survey.Survey
import grails.transaction.Transactional
import javax.servlet.http.Part
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.output.*;

@Transactional
class SurveyimageService {
	static transactional = 'mongo'
	
	def S3UploadService
	def grailsApplication
	def userService
	def surveyService
   def uploadImage(request,imageUrl,surveyId) {
	   
	   try
	   {
		   int errorCode = grailsApplication.config.customExceptions.survey.fourZeroFour.uploadimage.errorCode
		   int status = grailsApplication.config.customExceptions.survey.fourZeroFour.uploadimage.status
		   String message = grailsApplication.config.customExceptions.survey.fourZeroFour.uploadimage.message
		   String extendedMessage = grailsApplication.config.customExceptions.survey.fourZeroFour.uploadimage.extendedMessage
		   String moreInfo = grailsApplication.config.customExceptions.survey.fourZeroFour.uploadimage.moreInfo
		   String tokenValue = request.getHeader('X-Auth-Token')
		   UserRole userRole = userService.getUserAndRoleByAuthToken(tokenValue);
		   Survey survey = surveyService.getSurveyById(surveyId)
		   survey.imageUrl=imageUrl
		   println "abhimanyu ==="+ survey.imageUrl + " imageUrl == "+imageUrl
		   if(!survey.save()){
			   survey.errors.each{
				   println it
			   }
		   }
		   println "abhimanyu ==="+ survey.imageUrl + " imageUrl == "+imageUrl
		   return survey
	   }
	   catch(Exception e)
	   {
		   
	   }
	  
    }
}
