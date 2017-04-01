package com.themopi.timeline.api

import java.awt.List;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.mongodb.core.query.Update.Each;

import grails.converters.JSON
import grails.transaction.Transactional

import com.themopi.account.User
import com.themopi.account.UserRole
import com.themopi.exceptions.TimelineException
import com.themopi.survey.Survey
import com.themopi.user.Followers
import com.themopi.user.Timeline

@Transactional
class TimelineService {

	def userService
	def surveyService
    def serviceMethod() {}
	
	/*int errorCode = grailsApplication.config.customExceptions.timeline.fourZeroFour.timeline.errorCode
	 int status = grailsApplication.config.customExceptions.timeline.fourZeroFour.timeline.status
	 String message = grailsApplication.config.customExceptions.timeline.fourZeroFour.timeline.message
	 String extendedMessage = grailsApplication.config.customExceptions.timeline.fourZeroFour.timeline.extendedMessage
	 String moreInfo = grailsApplication.config.customExceptions.timeline.fourZeroFour.timeline.moreInfo
	 */
	def createCommentSurveyTimeline(request,userId,surveyId)
	{
		try
		{
			String tokenValue = request.getHeader('X-Auth-Token')
			UserRole userRole = userService.getUserAndRoleByAuthToken(tokenValue);
			//create timeline
			def timeline = new Timeline(
				action:request.getJSON().action,
				type:request.getJSON().type,
				isActive:request.getJSON().isActive,
				dateCreated:new Date(),
				lastUpdated:new Date()
				);
			if(surveyId==null || surveyId.equals(null) || surveyId.equals("null") || surveyId=="null")
			{
				// when user follows another user so performedOn will be user (login user)
				User user = userRole.user;
				User performedByUser = User.findById(userId);
				
				timeline.performedBy=performedByUser
				
				timeline.user=user
				timeline['performedOn'] = new JSON(user).toString();
				timeline.performedOnId=userId
			}
			else
			{
				// when user comment/like a survey so performedOn will be survey
				User performedByUser = userRole.user;
				//User user = User.findById(userId);
				
				timeline.performedBy=performedByUser
				
				Survey survey = surveyService.getSurveyById(surveyId)
				timeline.user=survey.user
				timeline['performedOn'] = new JSON(survey).toString();
				timeline.performedOnId=surveyId
			}
			if(!timeline.save())
			{
				return "Not Save"
			}
			else
			{
				return timeline;
			}
		}
		catch(TimelineException et)
		{
			return "Error occuring When saving"
		}
		catch(Exception e)
		{
			return "Error occuring When saving"
		}
	}
	
	def getFollowingUserTimeline(request)
	{
		String tokenValue = request.getHeader('X-Auth-Token')
		UserRole userRole = userService.getUserAndRoleByAuthToken(tokenValue);
		User user = userRole.user;
		//find all the user whom login user is following 
		def followee=Followers.findAllByFollower(user)
		def followers=Followers.findAllByFollowee(user)
		println "------followee-----s---" + followee
		println "------followee.followee-----s---" + followee.followee
		
		
		//AndDateCreated
		//get all timeline for activities performedBy all users been followed by loggedin user (Activity Following screen)
		/*Presently we will get:
		 * The users whom loggedin user is following 
		 * 1. liked a survey
		 * 2. commented on a survey
		 * 3. followed someone
		*/
		List<Timeline> timeline = new ArrayList<Timeline>();
		List<Timeline> timeline1 = new ArrayList<Timeline>();
		timeline = Timeline.findAllByPerformedByInList(followee.followee)
		timeline1 = Timeline.findAllByPerformedByInList(followers.follower)
		println "------timeline----"+timeline
		println "----timeline1----"+timeline1
		/*HashSet <Timeline> newTimelineSet = new HashSet <Timeline>();
		for(int i=0;i<timeline.size();i++)
		{
			newTimelineSet.add(timeline[i])
		}
		for(int i=0;i<timeline1.size();i++)
		{
			newTimelineSet.add(timeline1[i])
		}
		for(int i=0;i<newTimelineSet.size();i++)
		{
			println "-----"+newTimelineSet[i]
		}*/
		println "---ff--ch----"
		timeline.addAll(timeline1)
		println "---------------timeline --" + timeline
		return timeline
	}
	
	def getUserTimeline(request)
	{
		String tokenValue = request.getHeader('X-Auth-Token')
		UserRole userRole = userService.getUserAndRoleByAuthToken(tokenValue);
		User user = userRole.user;
		//find all the user to who are following login user 
		def followers=Followers.findAllByFollowee(user)
		println "------followers-----s---" + followers
		println "------followers.followers-----s---" + followers.follower
		//get all timeline for activities performedBy followers of login user (Activity You screen)
		/*Presently we will get:
		 * 1. Someone started following you
		 * 2. From your followers who liked your survey
		 * 3. From your followers who commented on your survey
		*/
		//def timeline1=Timeline.findAllByPerformedByInList(followers.follower)
		def timeline=Timeline.findAllByPerformedByNotEqualAndUser(user,user)
		println "timeline----"+timeline
		
		return timeline
	}
	
	
}
