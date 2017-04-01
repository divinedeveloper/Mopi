package com.mopi.imageupload

import grails.converters.JSON
import java.awt.image.BufferedImage

import org.json.JSONObject
import org.springframework.security.access.annotation.Secured

import com.amazonaws.services.s3.transfer.Upload
import com.themopi.exceptions.AccountException
import com.themopi.exceptions.ImageException
import com.themopi.exceptions.SurveyException
import javax.imageio.ImageIO
@Secured('permitAll')
class QuestionimageuploadController {

	def s3UploadService
	def hdImageService
	def questionimageService
	def responseObject = [:]
	def grailsApplication
	def userService
	def surveyService
	
    def index() { }
	def uploadImage()
	{
		JSONObject dashboardJsonlist = new JSONObject();
		def imageUrl=null
		
		try{
			//survey image can be updated only by owner of survey
			def userRole = userService.getUserAndRoleByAuthToken(request.getHeader("X-Auth-Token"))
			def surveyId = params.surveyId
			println "checking    --       surveyId---"+surveyId
			def survey = surveyService.getSurveyById(surveyId)
			boolean isOwner = surveyService.checkOwnerOfSurvey(survey, userRole)
			if(!isOwner){
				int errorCode = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.errorCode
				int status = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.status
				String message = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.message
				String extendedMessage = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.extendedMessage
				String moreInfo = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.moreInfo
	
				throw new AccountException(status,errorCode,message,extendedMessage,moreInfo)
				
			}
			
			if("Active".equals(survey?.status.toString()) || "flagged".equals(survey?.status.toString())){
				
				int errorCode = grailsApplication.config.customExceptions.survey.fourZeroThree.questionImage.errorCode
				int status = grailsApplication.config.customExceptions.survey.fourZeroThree.questionImage.status
				String message = grailsApplication.config.customExceptions.survey.fourZeroThree.questionImage.message
				String extendedMessage = grailsApplication.config.customExceptions.survey.fourZeroThree.questionImage.extendedMessage
				String moreInfo = grailsApplication.config.customExceptions.survey.fourZeroThree.questionImage.moreInfo
				
				throw new SurveyException(status,errorCode,message,extendedMessage,moreInfo)
			}
			
			
				def uploadedFile = request.getFile("myfile")
				if(uploadedFile == null || uploadedFile.empty){
					int errorCode = grailsApplication.config.customExceptions.image.fourZeroZero.image.errorCode
					int status = grailsApplication.config.customExceptions.image.fourZeroZero.image.status
					String message = grailsApplication.config.customExceptions.image.fourZeroZero.image.message
					String extendedMessage = grailsApplication.config.customExceptions.image.fourZeroZero.image.extendedMessage
					String moreInfo = grailsApplication.config.customExceptions.image.fourZeroZero.image.moreInfo
					
					throw new ImageException(status,errorCode,message, extendedMessage ,moreInfo)
				}
				
					try
					{
						String pathname = grailsApplication.config.myapp.tempImageFolderPath
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
						
						println "---------1111111111---------"+pathname
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
						
						imageUrl=questionimageService.uploadImage(request,grailsApplication.config.myapp.imageBaseUsrl+originalFileName,params.questionId,params.surveyId);
					
						if(imageUrl!=null)
						{
							imageUrl = grailsApplication.config.myapp.imageBaseUsrl+originalFileName
						}
						println "imageUrl------sending --"+imageUrl
						s3UploadService.deleteFile(pathname,grailsApplication.config.myapp.small+originalFileName)
						s3UploadService.deleteFile(pathname,grailsApplication.config.myapp.medium+originalFileName)
						s3UploadService.deleteFile(pathname,grailsApplication.config.myapp.big+originalFileName)
						s3UploadService.deleteFile(pathname,originalFileName)
						
					}
					catch(Exception e)
					{
						//imageUrl = grailsApplication.config.myapp.imageBaseUsrl+uploadedFile.originalFilename+originalFileExtension
						println e.printStackTrace();
					}
				
				
			dashboardJsonlist.put("imageUrl", imageUrl)
			responseObject.error = false
			responseObject.resp = dashboardJsonlist.map
			response.setStatus(200)
			render responseObject as JSON
		}catch(ImageException e){
			log.error e.errorResponse()
			response.setStatus(e.errorResponse().status)
			render e.errorResponse()
		}
		catch(AccountException e){
			log.error e.errorResponse();
			response.setStatus(e.errorResponse().status)
			render e.errorResponse()
		}
		catch(SurveyException e){
			log.error e.errorResponse()
			response.setStatus(e.errorResponse().status)
			render e.errorResponse()
		}
	
	
	}
	
	def multiplechoiceQuestionImageUpload(){
		try{
			def userRole = userService.getUserAndRoleByAuthToken(request.getHeader("X-Auth-Token"))
			def surveyId = params.surveyId
			println "surveyId----"+surveyId
			def survey = surveyService.getSurveyById(surveyId)
			println "survey---"+survey
			boolean isOwner = surveyService.checkOwnerOfSurvey(survey, userRole)
			if(!isOwner){
				int errorCode = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.errorCode
				int status = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.status
				String message = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.message
				String extendedMessage = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.extendedMessage
				String moreInfo = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.moreInfo
	
				throw new AccountException(status,errorCode,message,extendedMessage,moreInfo)
				
			}
			
			if("Active".equals(survey?.status.toString()) || "flagged".equals(survey?.status.toString())){
				
				int errorCode = grailsApplication.config.customExceptions.survey.fourZeroThree.optionImage.errorCode
				int status = grailsApplication.config.customExceptions.survey.fourZeroThree.optionImage.status
				String message = grailsApplication.config.customExceptions.survey.fourZeroThree.optionImage.message
				String extendedMessage = grailsApplication.config.customExceptions.survey.fourZeroThree.optionImage.extendedMessage
				String moreInfo = grailsApplication.config.customExceptions.survey.fourZeroThree.optionImage.moreInfo
				
				throw new SurveyException(status,errorCode,message,extendedMessage,moreInfo)
			}
			
			def uploadedFile = request.getFile("myfile")
			println "uploadedFile---"+uploadedFile
			println params.surveyId
			println params.questionId
			println params.optionId
			def question = questionimageService.multipleChoiceImage(uploadedFile,params.surveyId,params.questionId,params.optionId)
			responseObject.error = false
			responseObject.resp = question
			response.setStatus(200)
			render responseObject as JSON
		}catch(ImageException e){
			log.error e.errorResponse()
			response.setStatus(e.errorResponse().status)
			render e.errorResponse()
		}
		catch(AccountException e){
			log.error e.errorResponse();
			response.setStatus(e.errorResponse().status)
			render e.errorResponse()
		}
		catch(SurveyException e){
			log.error e.errorResponse()
			response.setStatus(e.errorResponse().status)
			render e.errorResponse()
		}
	}
}

