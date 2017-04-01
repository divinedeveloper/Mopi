package com.themopi.image

import com.themopi.account.UserRole
import com.themopi.exceptions.ImageUploadException
import com.themopi.survey.Gift
import com.themopi.survey.Survey
import grails.transaction.Transactional
import javax.servlet.http.Part
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.output.*;

@Transactional
class GiftimageService {
	static transactional = 'mongo'
	
	def S3UploadService
	def grailsApplication
	def userService
	def surveyService
   def uploadImage(request,imageUrlt,giftId,surveyId) {
	   
	   def imageUrl=imageUrlt
	   try
	   {
		   //println "----------------------"+imageUrl
		   int errorCode = grailsApplication.config.customExceptions.survey.fourZeroFour.uploadimage.errorCode
		   int status = grailsApplication.config.customExceptions.survey.fourZeroFour.uploadimage.status
		   String message = grailsApplication.config.customExceptions.survey.fourZeroFour.uploadimage.message
		   String extendedMessage = grailsApplication.config.customExceptions.survey.fourZeroFour.uploadimage.extendedMessage
		   String moreInfo = grailsApplication.config.customExceptions.survey.fourZeroFour.uploadimage.moreInfo
		   String tokenValue = request.getHeader('X-Auth-Token')
		  // println "----------3------------"
		   UserRole userRole = userService.getUserAndRoleByAuthToken(tokenValue);
		  // println "-----------------------------"
		   Gift gift = Gift.findById(giftId)
		  // println "----gift-----gift----"+gift
		  
		   
		   
		   gift.imageUrlG=imageUrl
		  if(!gift.save()){
			   gift.errors.each{
				   println it
			   }
		   }
		  Survey survey = surveyService.getSurveyById(surveyId)
		 /* def survey = Survey.findById(surveyId)*/
		  //println "----survey-----survey----"+survey
		  //survey.gift=null
		 // println "---survey.gift---"+survey.gift.imageUlrG
		  survey.gift=gift
		  //survey.save()
		  if(!survey.save(flush:true)){
			  survey.errors.each{
				  println it
			  }
		  }
		  else
		  {
			  println "survey update"
		  }
		 /* println "---survey.gift.imageUrlG---"+survey.gift.imageUrlG
		  
		  println "successfully---------------------------"*/
	   }
	   catch(Exception e)
	   {
		   println "eeee"
	   }
	   //println "vvvv"+imageUrl
	   return imageUrl
	}
}
