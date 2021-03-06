package com.themopi

import com.themopi.account.User
import com.themopi.exceptions.HashTagException
import com.themopi.survey.HashTag
import grails.transaction.Transactional

@Transactional
class HashTagService {
	static transactional = 'mongo'
	def grailsApplication
	def serviceMethod() {
	}
	/**
	 * create and returns hashtag . Associate hashtag with user who created the hashtag
	 * @param name
	 * @param user
	 * @return HashTag
	 */
	def createHashTag(String name , User user){
		HashTag hashTag = new HashTag(
				name:name,
				User:user
				)
		if(!hashTag.save()){
			hashTag.errors.each{ println it }
			int errorCode = grailsApplication.config.customExceptions.account.fourZeroZero.hashTag.errorCode
			int status = grailsApplication.config.customExceptions.account.fourZeroZero.hashTag.status
			String message = grailsApplication.config.customExceptions.account.fourZeroZero.hashTag.message
			String extendedMessage = grailsApplication.config.customExceptions.account.fourZeroZero.hashTag.extendedMessage
			String moreInfo = grailsApplication.config.customExceptions.account.fourZeroZero.hashTag.moreInfo

			throw new HashTagException(status,errorCode,message,extendedMessage,moreInfo)
		}
		return hashTag
	}

	/**
	 * find all the hastags using like query 
	 * @param name
	 * @return hashTag object lis
	 */
	def findHashTag(String name){
		List hashTags = []
		hashTags = HashTag.findAllByNameIlike("%"+name+"%")
		return hashTags
	}
}
