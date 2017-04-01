package com.themopi.connections

import com.themopi.exceptions.AccountException
import com.themopi.exceptions.ConnectionException
import grails.converters.JSON

import org.springframework.security.access.annotation.Secured;
@Secured('permitAll')
class ConnectionController {
	def userService
	def connectionService
	def responseObject = [:]
    def index() { }
	
	def follow(){
		try{
			def status  = connectionService.followUser(params.userId,request.getHeader("X-Auth-Token"))
			responseObject.error = status
			response.setStatus(201)
			render responseObject as JSON
		}catch(AccountException e){
			log.error e.errorResponse()
			response.status = e.errorResponse().status
			render e.errorResponse();
		}catch(ConnectionException e){
			log.error e.errorResponse()
			response.status = e.errorResponse().status
			render e.errorResponse();
		}
	}
	
	def getApprovedFollowing(){
		try{
			println "hi i m in get approvedFollowers of Login User"
			def followersList = connectionService.getApprovedPeopleFollowingMe(request.getHeader("X-Auth-Token"),params.limit,params.offset,params.searchParam)
			responseObject.error = false
			responseObject.resp = followersList
			response.setStatus(200)
			render responseObject as JSON
		}catch(AccountException e){
			log.error e.errorResponse()
			response.status = e.errorResponse().status
			render e.errorResponse();
		}
	}
	
	def getFollowing(){
		try{
			def followersList = connectionService.getPeopleFollowingMe(params.userId,params.limit,params.offset,params.searchParam)
			responseObject.error = false
			responseObject.resp = followersList
			response.setStatus(200)
			render responseObject as JSON
		}catch(AccountException e){
			log.error e.errorResponse()
			response.status = e.errorResponse().status
			render e.errorResponse();
		}
	}
	
	
	def getFollowedByMe(){
		try{
			def followersList = connectionService.getPeopleFollowedByMe(params.userId,params.limit,params.offset,params.searchParam)
			responseObject.error = false
			responseObject.resp = followersList
			response.setStatus(200)
			render responseObject as JSON
		}catch(AccountException e){
			log.error e.errorResponse()
			response.status = e.errorResponse().status
			render e.errorResponse();
		}
	}
	
	def getFollowingRequestToMe(){
		try{
			def followersList = connectionService.getPeopleRequestedMe(request.getHeader("X-Auth-Token"),params.limit,params.offset)
			responseObject.error = false
			responseObject.resp = followersList
			response.setStatus(200)
			render responseObject as JSON
		}catch(AccountException e){
			log.error e.errorResponse()
			response.status = e.errorResponse().status
			render e.errorResponse();
		}
	}
	
	def allowFollowing(){
		try{
			//allow a user for following and also create timeline for requesting user
			def status  = connectionService.allowFollow(request,params.requestId,request.getHeader("X-Auth-Token"))
			responseObject.error = false
			response.setStatus(201)
			render responseObject as JSON
		}catch(AccountException e){
			log.error e.errorResponse()
			response.status = e.errorResponse().status
			render e.errorResponse();
		}catch(ConnectionException e){
			log.error e.errorResponse()
			response.status = e.errorResponse().status
			render e.errorResponse();
		}
	}
	
	def disAllowFollowing(){
		try{
			println "-------------------"
			def status  = connectionService.disAllowFollow(params.requestId,request.getHeader("X-Auth-Token"))
			responseObject.error = false
			responseObject.resp = "Following Request is Deleted Successfuly"
			response.setStatus(201)
			render responseObject as JSON
		}catch(AccountException e){
			log.error e.errorResponse()
			response.status = e.errorResponse().status
			render e.errorResponse();
		}catch(ConnectionException e){
			log.error e.errorResponse()
			response.status = e.errorResponse().status
			render e.errorResponse();
		}
	}
	
	def userConnections(){
		try{
			def connectionList = connectionService.getConnections(params.userId,params.limit,params.offset,params.searchParam)
			responseObject.error = false
			responseObject.resp = connectionList
			response.setStatus(200)
			render responseObject as JSON
		}catch(AccountException e){
			log.error e.errorResponse()
			response.status = e.errorResponse().status
			render e.errorResponse();
		}
	}
}
