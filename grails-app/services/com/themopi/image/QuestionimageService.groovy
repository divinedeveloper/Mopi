package com.themopi.image

import com.amazonaws.services.s3.transfer.Upload
import com.themopi.account.UserRole
import com.themopi.exceptions.ImageException
import com.themopi.exceptions.ImageUploadException
import com.themopi.survey.Options
import com.themopi.survey.Question;
import com.themopi.survey.Survey
import javax.imageio.ImageIO
import grails.transaction.Transactional
import java.awt.image.BufferedImage

@Transactional
class QuestionimageService {
	static transactional = 'mongo'
	
	def grailsApplication
	def userService
	def questionService
	def surveyService
	def s3UploadService
	def hdImageService
	
     def uploadImage(request,imageUrl,questionId,surveyId) {
		
		
		String tokenValue = request.getHeader('X-Auth-Token')
		UserRole userRole = userService.getUserAndRoleByAuthToken(tokenValue);
		def question = Question.findById(questionId)
		println "----question-----question----"+question
		question.imageUrlQ=imageUrl
	   if(!question.save()){
			question.errors.each{
				println it
			}
		}
	   
	   ///
	   println "-----------------"
	   println "surveyId--"+surveyId
	   def questionList = []
	   Question tquestion = null
	   //Survey survey = Survey.findById(surveyId)
	   Survey survey = surveyService.getSurveyById(surveyId)
	   println "survey---"+survey
	   survey.questions.each{
		   println it.id
		   if( it.id == questionId.toInteger()){
			   tquestion = it
			   println it
		   }else{
			   questionList.add(it)
		   }
	   }

	   

	   questionList.add(question)
	  
	   survey.questions = questionList
	   if(!survey.save()){
		   survey.errors.each{
			   println it
		   }
	   }
	   
	   println survey.questions
	   return question
	   ////
	   //return imageUrl
    }
	 
	 
	 def multipleChoiceImage(uploadedFile,surveyId,questionId,optionId){
		 def imageUrl =  null 
		 if(uploadedFile == null){
			 int errorCode = grailsApplication.config.customExceptions.image.fourZeroZero.image.errorCode
			 int status = grailsApplication.config.customExceptions.image.fourZeroZero.image.status
			 String message = grailsApplication.config.customExceptions.image.fourZeroZero.image.message
			 String extendedMessage = grailsApplication.config.customExceptions.image.fourZeroZero.image.extendedMessage
			 String moreInfo = grailsApplication.config.customExceptions.image.fourZeroZero.image.moreInfo
			 
			 throw new ImageException(status,errorCode,message, extendedMessage ,moreInfo)
		 }
		 String pathname = grailsApplication.config.myapp.tempImageFolderPath.toString()
		 String originalFileExtension = uploadedFile.originalFilename.substring(uploadedFile.originalFilename.lastIndexOf("."))
		 def date = new Date()
		 String originalFileName =  date.getTime().toString()+uploadedFile.originalFilename.toString();
		 byte[] logoBytes10 = hdImageService.scale(uploadedFile.getInputStream(), grailsApplication.config.myapp.smallsize, grailsApplication.config.myapp.smallsize)
		 byte[] logoBytes100 = hdImageService.scale(uploadedFile.getInputStream(), grailsApplication.config.myapp.mediumsize, grailsApplication.config.myapp.mediumsize)
		 byte[] logoBytes200 = hdImageService.scale(uploadedFile.getInputStream(), grailsApplication.config.myapp.bigsize, grailsApplication.config.myapp.bigsize)
		 
		 try {
			 BufferedImage bf = hdImageService.getBufferedImage(logoBytes10)
			 ImageIO.write(bf, "jpg", new File(pathname+grailsApplication.config.myapp.small+originalFileName));
			 BufferedImage bf1 = hdImageService.getBufferedImage(logoBytes100)
			 ImageIO.write(bf1, "jpg", new File(pathname+grailsApplication.config.myapp.medium+originalFileName));
			 BufferedImage bf2 = hdImageService.getBufferedImage(logoBytes200)
			 ImageIO.write(bf2, "jpg", new File(pathname+grailsApplication.config.myapp.big+originalFileName));
		 } catch (IOException e) {
		 println "image check failed"
			 e.printStackTrace();
		 }
		 println "------------------"+pathname
		 uploadedFile.transferTo(new File(pathname+originalFileName))
		 pathname = pathname
		 println originalFileName
		 println pathname
		 Upload upload = s3UploadService.uploadFile(pathname,originalFileName)
		 println "upload----"+upload
		 Upload upload1 = s3UploadService.uploadFile(pathname,grailsApplication.config.myapp.small+originalFileName)
		 println "upload1----"+upload1
		 Upload upload2 = s3UploadService.uploadFile(pathname,grailsApplication.config.myapp.medium+originalFileName)
		 println "upload2----"+upload2
		 Upload upload3 = s3UploadService.uploadFile(pathname,grailsApplication.config.myapp.big+originalFileName)
		 println "upload3----"+upload3
		 
		 def question = uploadImageForQuestionOption(surveyId,questionId,optionId,grailsApplication.config.myapp.imageBaseUsrl+originalFileName);
		 
		 s3UploadService.deleteFile(pathname,grailsApplication.config.myapp.small+originalFileName)
		 s3UploadService.deleteFile(pathname,grailsApplication.config.myapp.medium+originalFileName)
		 s3UploadService.deleteFile(pathname,grailsApplication.config.myapp.big+originalFileName)
		 s3UploadService.deleteFile(pathname,originalFileName)
		 
		/* File fl=new File(pathname+originalFileName);
		 if(fl.exists())
		 {
			 fl.delete()
		 }*/
		 
		 return question
	 }
	 
	 
	 //first extract question from survey
	 //iterate over question list which is of type MCQ(QuestionIdprovided by user)
	 //and add remaining question to other questionlist
	 //extract options from Mcq question 
	 //remove optionId provided by user to upload image and assign image url to it
	 // and add remaining options to other options list
	 //then assign new options list to question and save question
	 //add MCQ question to question list and assign question list to survey questions
	 //save survey
	 def uploadImageForQuestionOption(surveyId,questionId,optionId,imageUrl){
		 def questionList = []
		 println "originalFIleName  == ="+imageUrl
		 Survey survey = surveyService.getSurveyById(surveyId)
		 Question question 
		 survey.questions.each{
			 println it.id
			 if( it.id == questionId.toInteger()){
				 question = it
				 println it
			 }else{
				 questionList.add(it)
			 }
		 }
		 println "add image url to option-------------"
		 def opts = []
		 question.options.each{
			 println "it ====== "+it.id.getClass()
			 if(optionId.toInteger() != it?.id){
			 	 opts.add(it)
			 }else{
			 Options opt = it
			 it.image = imageUrl 
			 opts.add(opt)
			 }
		 }
		 
		 question.options = opts
		 question.save()
		 questionList.add(question)
		 survey.questions = questionList
		 if(!survey.save()){
			survey.errors.each{
				println it
			}
		}
		
		return question
		 
	 }
}
