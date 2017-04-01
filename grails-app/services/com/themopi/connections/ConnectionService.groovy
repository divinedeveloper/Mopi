package com.themopi.connections

import grails.transaction.Transactional
import com.themopi.account.*
import com.themopi.exceptions.AccountException
import com.themopi.user.Followers
import com.themopi.achievements.*

@Transactional
class ConnectionService {
	static transactional = 'mongo'

	def userService
	def grailsApplication
	def pointService
	def timelineService
	/**
	 * request for following
	 * @param userId
	 * @param tokenValue
	 * @return
	 */
	def followUser(userId,tokenValue){
		def followee  = userService.findUserByUserId(userId)
		def user = userService.getUserAndRoleByAuthToken(tokenValue)?.user
		def followerObject = Followers.findByUserAndFollowee(user,followee)
		if(user == followee){
			int errorCode = grailsApplication.config.customExceptions.connection.fourZeroNine.self.errorCode
			int status = grailsApplication.config.customExceptions.connection.fourZeroNine.self.status
			String message = grailsApplication.config.customExceptions.connection.fourZeroNine.self.message
			String extendedMessage = grailsApplication.config.customExceptions.connection.fourZeroNine.self.extendedMessage
			String moreInfo = grailsApplication.config.customExceptions.connection.fourZeroNine.self.moreInfo

			throw new AccountException(status,errorCode,message,extendedMessage,moreInfo)
		}
		if(followerObject == null){
			Followers follow = new Followers(
					requested:true,
					user:user,
					followee:followee,
					follower:user,
					status:"FOLLOW_REQUESTED"
					)
			if(!follow.save()){
				follow.errors.each{ println it  }
				return true
			}
			pointService.followingAUser(user)
			return false
		}else{
			int errorCode = grailsApplication.config.customExceptions.connection.fourZeroNine.connection.errorCode
			int status = grailsApplication.config.customExceptions.connection.fourZeroNine.connection.status
			String message = grailsApplication.config.customExceptions.connection.fourZeroNine.connection.message
			String extendedMessage = grailsApplication.config.customExceptions.connection.fourZeroNine.connection.extendedMessage
			String moreInfo = grailsApplication.config.customExceptions.connection.fourZeroNine.connection.moreInfo

			throw new AccountException(status,errorCode,message,extendedMessage,moreInfo)
		}
	}
	/**
	 * created by ranjit
	 * get the list of followers
	 * @param tokenValue
	 * @param limit
	 * @param offset
	 * @return
	 */
	def getApprovedPeopleFollowingMe(tokenValue,limit,offset,searchParam){
		println "hi i m in get approvedFollowers of Login User service--"
		def user = userService.getUserAndRoleByAuthToken(tokenValue)?.user
		
		def followerList = User.createCriteria().list(){
			if(searchParam != null && searchParam != ""){
				or{
					ilike("name","%"+searchParam+"%")
					ilike("email","%"+searchParam+"%")
					ilike("username","%"+searchParam+"%")
				}
			}
		}
		
		def followers = Followers.createCriteria().list(max:limit,offset:offset){
			eq("followee",user)
			eq("status","FOLLOW_APPROVED")
			inList("follower",followerList)
		}
		//def followees = Followers.findAllByFollowerAndAllowFollowing(user?.id,true,['max':limit,'offset':offset])
		println followers.email
		return followers
		
	}
	/**
	 * get the list of followers
	 * @param tokenValue
	 * @param limit
	 * @param offset
	 * @return
	 */
	def getPeopleFollowingMe(userId,limit,offset,searchParam){
		def user = userService.findUserByUserId(userId)
		println "user-name = "+user.name
		println "id == "+ user.id

		def followerList = User.createCriteria().list(){
			if(searchParam != null && searchParam != ""){
				or{
					ilike("name","%"+searchParam+"%")
					ilike("email","%"+searchParam+"%")
					ilike("username","%"+searchParam+"%")
				}
			}
		}

		def followers = Followers.createCriteria().list(max:limit,offset:offset){
			eq("followee",user)
			eq("allowFollowing",true)
			inList("follower",followerList)
		}
		//def followees = Followers.findAllByFollowerAndAllowFollowing(user?.id,true,['max':limit,'offset':offset])
		println followers.email
		return followers
	}

	/**
	 * get the followers and followee
	 * @param tokenValue
	 * @param limit
	 * @param offset
	 * @return
	 */
	def getConnections(userId,limit,offset,searchParam){
		def user = userService.findUserByUserId(userId)
		def connectionList = User.createCriteria().list(){
			if(searchParam != null && searchParam != ""){
				or{
					ilike("name","%"+searchParam+"%")
					ilike("email","%"+searchParam+"%")
					ilike("username","%"+searchParam+"%")
				}
			}
		}

		def connections = Followers.createCriteria().list(max:limit,offset:offset){
			eq("allowFollowing",true)
			or{
				and{
					eq("followee",user)
					inList("follower",connectionList)
				}
				and{
					eq("follower",user)
					inList("followee",connectionList)
				}
			}
		}
		//def connections = query.list('max':limit,'offset':offset)
		//def connections = Followers.findAllByUserOrFollowee(user,['max':limit,'offset':offset])
		return connections
	}

	/**
	 * get the list of followee
	 * @param tokenValue
	 * @param limit
	 * @param offset
	 * @return
	 */
	def getPeopleFollowedByMe(userId,limit,offset,searchParam){
		def user = userService.findUserByUserId(userId)
		println "user-name 1 = "+user.name
		println "id == "+ user.id + "  searchParam=    "+searchParam
		def followeeList = User.createCriteria().list(){
			if(searchParam != null && searchParam != ""){
				or{
					ilike("name","%"+searchParam+"%")
					ilike("email","%"+searchParam+"%")
					ilike("username","%"+searchParam+"%")
				}
			}
		}

		def followees = Followers.createCriteria().list(max:limit,offset:offset){
			eq("follower",user)
			eq("allowFollowing",true)
			inList("followee",followeeList)
		}
		//def followees = Followers.findAllByFollowerAndAllowFollowing(user?.id,true,['max':limit,'offset':offset])
		println followees.email
		return followees
	}

	/**
	 * get the list of people who requested to follow
	 * @param tokenValue
	 * @param limit
	 * @param offset
	 * @return
	 */
	def getPeopleRequestedMe(tokenValue,limit,offset){
		def user = userService.getUserAndRoleByAuthToken(tokenValue)?.user
		def followees = Followers.findAllByFolloweeAndRequested(user,true,['max':limit,'offset':offset])
		return followees
	}

	/**
	 * approve a following request
	 * @param requestId
	 * @param tokenValue
	 * @return
	 */
	def allowFollow(request,requestId,tokenValue){
		def followRequest = Followers.findById(requestId)
		def user = userService.getUserAndRoleByAuthToken(tokenValue)?.user
		if(followRequest == null){
			int errorCode = grailsApplication.config.customExceptions.connection.fourZeroZero.connection.errorCode
			int status = grailsApplication.config.customExceptions.connection.fourZeroZero.connection.status
			String message = grailsApplication.config.customExceptions.connection.fourZeroZero.connection.message
			String extendedMessage = grailsApplication.config.customExceptions.connection.fourZeroZero.connection.extendedMessage
			String moreInfo = grailsApplication.config.customExceptions.connection.fourZeroZero.connection.moreInfo

			throw new AccountException(status,errorCode,message,extendedMessage,moreInfo)
		}
		if(user?.id == followRequest?.followee?.id){
			followRequest.allowFollowing = true
			followRequest.requested = false
			followRequest.status = "FOLLOW_APPROVED"
			if(followRequest.save(true)){
				//increase followers count 
				increaseCountOnApproveRequest(followRequest?.follower,followRequest?.followee)
				// give point to the user 
				pointService.allowAUserToFollow(user)
			}

			def surveyId=null
			println "request.action---"+request.getJSON().action
			println "--surveyid -----"+surveyId
			//create timeline for approving follow request
			timelineService.createCommentSurveyTimeline(request,followRequest.follower.id,surveyId)
			println "timeline is completed on connection end"
		}else{
			int errorCode = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.errorCode
			int status = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.status
			String message = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.message
			String extendedMessage = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.extendedMessage
			String moreInfo = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.moreInfo

			throw new AccountException(status,errorCode,message,extendedMessage,moreInfo)
		}
	}

	/**
	 * delete a following request
	 * @param requestId
	 * @param tokenValue
	 * @return
	 */
	def disAllowFollow(requestId,tokenValue){
		println "disAllowFollow"
		def user = userService.getUserAndRoleByAuthToken(tokenValue)?.user
		def followRequest = Followers.findById(requestId)
		if(followRequest == null){
			int errorCode = grailsApplication.config.customExceptions.connection.fourZeroZero.connection.errorCode
			int status = grailsApplication.config.customExceptions.connection.fourZeroZero.connection.status
			String message = grailsApplication.config.customExceptions.connection.fourZeroZero.connection.message
			String extendedMessage = grailsApplication.config.customExceptions.connection.fourZeroZero.connection.extendedMessage
			String moreInfo = grailsApplication.config.customExceptions.connection.fourZeroZero.connection.moreInfo

			throw new AccountException(status,errorCode,message,extendedMessage,moreInfo)
		}
		if(user?.id == followRequest?.followee?.id || user?.id == followRequest?.follower?.id){
			decreaseCountOnDeleteRequest(followRequest?.follower,followRequest?.followee)
			followRequest.delete(flush:true)
		}else{
			int errorCode = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.errorCode
			int status = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.status
			String message = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.message
			String extendedMessage = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.extendedMessage
			String moreInfo = grailsApplication.config.customExceptions.account.fourZeroOne.notAuthorized.moreInfo

			throw new AccountException(status,errorCode,message,extendedMessage,moreInfo)
		}
	}

	def increaseCountOnApproveRequest(follower,followee){
		follower.profile.followingCount = (follower.profile.followingCount == null || follower.profile.followingCount == 0)?1:follower.profile.followingCount+1
		followee.profile.followersCount = (followee.profile.followersCount == null || followee.profile.followersCount == 0)?1:followee.profile.followersCount+1

		follower.save()
		followee.save()
	}

	def decreaseCountOnDeleteRequest(follower,followee){
		follower.profile.followingCount = (follower.profile.followingCount == null || follower.profile.followingCount == 0)?0:follower.profile.followingCount-1
		followee.profile.followersCount = (followee.profile.followersCount == null || followee.profile.followersCount == 0)?0:followee.profile.followersCount-1

		follower.save()
		followee.save()
	}
	def serviceMethod() {
	}
}
