package com.themopi.image

import grails.transaction.Transactional

import com.amazonaws.services.s3.model.*
import com.amazonaws.services.s3.transfer.*
@Transactional
class S3UploadService {
	static transactional = 'mongo'
	
	def amazonWebService
	def grailsApplication
	
	def uploadFile(String path,String fileName)
	{
		try
		{
		    int count = 0
			path = path+fileName
			Upload upload = amazonWebService.transferManager.upload(new PutObjectRequest(grailsApplication.config.bucketName, fileName, new File(path)))
			while (!upload.done) 
			{
			     println "Transfer: $upload.description"
			     println "  - State: $upload.state"
			     println "  - Progress: $upload.progress.bytesTransfered"
			     // Do work while we wait for our upload to complete�
			     Thread.sleep(500)
				 count = count+500
				 if(count == 3000){
					 break;
				 }
			}
		}
		catch(Exception e)
		{
			log.debug"Error in Uploading file is "+e
		}
		finally
		{
			return upload
		}
	}
	
	def uploadFiles(String path,String fileName) {

		try
		{
			println path
			println fileName
			MultipleFileUpload upload = amazonWebService.transferManager.uploadDirectory(grailsApplication.config.bucketName,fileName,new File(path),false)
			while (!upload.done) {
				log.debug "Transfer: $upload.description"
				log.debug "  - State: $upload.state"
				log.debug "  - Progress: $upload.progress.bytesTransfered"
				// Do work while we wait for our upload to complete…
				Thread.sleep(500)
			}
		}
		catch(Exception e)
		{
			log.debug"Error in Uploading file is "+e
		}
		finally
		{
			return upload
		}
	}
    def serviceMethod() {

    }
	def deleteFile(String pathname,String originalFileName)
	{
		File fl=new File(pathname+originalFileName);
		if(fl.exists())
		{
			fl.delete()
		}
	}
}
