package com.themopi.apis

import grails.converters.*
import java.util.Map;
import com.themopi.exceptions.HashTagException


import org.springframework.security.access.annotation.Secured

@Secured('permitAll')
class HashTagController {
	def hashTagService
	Map responseObject = [:]
	def index() { }
	
    /**
     * 
     * find hashtags using name in the params
     * @return json response hastag object
     */
	def getHashTagByName(){
		try{
			def hashTags = hashTagService.findHashTag(params.name)
			responseObject.error = false
			responseObject.resp = hashTags
			response.setStatus(200)
			render responseObject as JSON
		}catch(HashTagException e){
			log.error e.errorResponse()
			response.setStatus(e.errorResponse().status)
			render e.errorResponse()
		}
	}
}
