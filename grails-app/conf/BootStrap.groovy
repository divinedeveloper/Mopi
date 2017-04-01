import grails.converters.*
import grails.plugin.springsecurity.authentication.dao.NullSaltSource

import org.codehaus.groovy.grails.web.json.JSONObject

import com.themopi.account.*
import com.themopi.survey.*
import com.themopi.user.*
class BootStrap {
	def saltSource
	def springSecurityService
	def grailsApplication
    def init = { servletContext ->
		if(Role.list().size() == 0){
			new Role(authority:"ROLE_SUPERADMIN",displayAuthority:"Mopi Superadmin").save()
			new Role(authority:"ROLE_ADMIN",displayAuthority:"Mopi Support").save()
			new Role(authority:"ROLE_SPONSEREDADMIN",displayAuthority:"Mopi Sponsor").save()
			new Role(authority:"ROLE_USER",displayAuthority:"Mopi User").save()
		}
				
		if(User.list().size() == 0){
			String salt = saltSource instanceof NullSaltSource ? null : "admin@mopi.com"
			String encodedPassword = springSecurityService.encodePassword('!mopi@2014',salt)
			def superUser = new User(email:"admin@mopi.com",
				    				  password:encodedPassword,
									  name:"SUPERADMIN",
									  accountLocked: false,
									  enabled: true,
									  accountExpired:false,
									  passwordExpired:false,
									  isActive:true,
									  username:"SUPERADMIN",
									  tokenValue:null,
									  roleId: new Long(1),
									  roleAuthority:"ROLE_SUPERADMIN",
									  salt:"admin@mopi.com"
									  )
			superUser.save()
			superUser.errors.each{
				println it
			}
			def role = new UserRole(user:superUser,role:Role.findWhere(authority:'ROLE_SUPERADMIN')).save();
			}
		
	
		JSON.registerObjectMarshaller(Survey) {
			def output = [:]
			output['id'] = it?.id
			output['dateCreated'] = it?.dateCreated
			output['endTime'] = it?.endTime
			output['lastUpdated'] = it?.lastUpdated
			output['questionCount']=it?.questions?.size()
			output['questionList']=it?.questions
			output['responses'] = it?.respCount
			output['startTime'] = it?.startTime
			output['name']=it?.name
			output['surveyCode']=it?.surveyCode
			output['isActive'] = it?.isActive
			output['lastUpdatedBy'] = ["id":it?.lastUpdatedBy.id,"name":it?.lastUpdatedBy?.name]
			output['visibility'] = it?.visibility
			output['visibleTo'] = it?.visibleTo.toString()
			output['cityList']=[]
			output['countryList']=[]
			output['cityWithLocation']=it?.visiblityCity
			output['countryWithLocation']=it?.visiblityCountry
			output['status']=it?.status.toString()
			output['HashTag']= it?.hashTag?.name
			output['surveyType']=it?.surveyType.toString()
			output['user'] = ["id":it?.user.id,"name":it?.user?.name]
			output['surveyFlaggedCount']=(it?.surveyFlaggedCount == null)?0:it?.surveyFlaggedCount
			output['commentFlaggedCount']=(it?.commentFlaggedCount == null)?0:it?.commentFlaggedCount
			output['createdByUserId']=it?.user?.id
			output['createdByUserName']=it?.user?.name
			output['imageUrl']=it?.imageUrl
			output['commentCount']=it?.commentCount
			output['gift']=it?.gift
			output['individuals']=it?.individuals
		
			return output;
			
			}
		JSON.registerObjectMarshaller(Gift) {
			def gift = [:]
			gift['id'] = it?.id
			gift['type'] = it?.type
			gift['imageUrlG'] = it?.imageUrlG
			gift['code'] = it?.code
			gift['text']= it?.text
			gift['isAvailable'] = it?.isAvailable
			gift['maxNo'] = (it.maxNo == null)?"unlimited":it.maxNo
			gift['dateCreated'] = it?.dateCreated
			gift['lastUpdated']= it?.lastUpdated
			gift['user']= it?.user
			
			return gift
		}
		
		JSON.registerObjectMarshaller(Question) {
			def question = [:]
			question['id'] = it?.id
			question['name'] = it?.query
			question['dateCreated'] = it?.dateCreated
			question['imageUrlQ'] = it?.imageUrlQ
			question['isActive']= it?.isActive
			question['lastUpdated']=it?.lastUpdated
			question['option'] = (it?.questionType?.toString() == "multiplechoice")?null:it?.option
			question['options'] = (it?.questionType?.toString() == "multiplechoice")?it?.options:it?.option
			question['questionType']=it?.questionType?.toString()
			question['responseCount']=it?.questionResponseCount()
			
			return question
		}
		
		JSON.registerObjectMarshaller(SurveyCity){
			def output = [:]
			output['country'] = it.country
			output['countryCode'] = it.countryCode
			output['latitude'] = it.location?.lat
			output['longitude']= it.location?.long
			output['name']=it.name
			output['state']=it.state
			return output
		}
		
		JSON.registerObjectMarshaller(SurveyCountry){
			def output = [:]
			output['countryCode'] = it.countryCode
			output['latitude'] = it.location?.lat
			output['longitude']= it.location?.long
			output['name']=it.name
			return output
	    }
		JSON.registerObjectMarshaller(User) {
			def output = [:]
			output['id'] = it?.id
			output['country']=it?.address?.country?.name
			output['countryCode']=it?.address?.country?.code
			output['city']=it?.address?.city?.name
//			output['dob']=it?.profile?.dob
			output['email']=it?.email
			output['gender']=it?.profile?.gender?.toString()
			output['homeTown']=it?.address?.homeTown
			output['incomeLevel']=it?.profile?.incomeLevel
			output['lat']=it?.address?.location?.lat
			output['long']=it?.address?.location?.long
			output['mobile']=it?.profile?.mobile
			output['name']=it?.name
			output['username']=it?.username
			output['occupation']=it?.profile?.occupation
			output['phoneNumber']=it?.profile?.phoneNumber
			output['roleName']=it?.roleAuthority
			output['roleDisplay'] = it?.getAuthorities()[0]?.displayAuthority
			output['roleId']=it?.roleId
			output['age']=it?.profile?.age
			output['religion']=it?.profile?.religion
			output['description']=it?.profile?.description
			output['state']=it?.address?.state?.name
			output['status']=it?.isActive
			output['imageUrl']=it?.profile?.imageUrlP			
			output['surveyCount']=it?.surveyCount
			
			return output;
			
			}
		
		JSON.registerObjectMarshaller(Response) {
			def output = [:]
			output['id'] = it?.id
			output['answer']=it?.answer
			output['userid']=it?.answerBy?.id
			output['age']=it?.answerBy?.profile?.age
			output['gender']=it?.answerBy?.profile?.gender.toString()
			output['homeTown']=it?.answerBy?.address?.homeTown
			output['incomeLevel']=it?.answerBy?.profile?.incomeLevel
			output['name']=it?.user?.name
			output['query']=it?.answerForQuestion?.query
			output['questionType']=it?.answerForQuestion?.questionType.toString()
			output['questionId']=it?.answerForQuestion?.id
			output['surveyId']=it?.survey?.id
			output['surveyName'] = it?.survey?.name
			
			return output;
			
			}
			
			
		JSON.registerObjectMarshaller(Comment) {
			def comment = [:]
			comment['id'] = it?.id
			comment['comment'] = it?.comment
			comment['dateCreated'] = it?.dateCreated
			comment['lastUpdated'] = it?.lastUpdated
			comment['surveyId']= it?.commentOnSurvey?.id
			comment['surveyName']= it?.commentOnSurvey?.name
			comment['userId'] = it?.commentedBy?.id
			comment['name']= it?.commentedBy?.name
			comment['imageUrl']= it?.commentedBy?.imageUrl
			comment['commentReportCount']=it?.commentReportCount


			return comment
		}
		
		JSON.registerObjectMarshaller(SurveysResponded) {
			def output = [:]
			output['surveyId'] = it?.survey?.id
			output['commentCount'] = it?.survey?.commentCount
			output['imageUrl'] = it?.survey?.imageUrl
			output['surveyName'] = it?.survey?.name
			output['userId'] = it?.user?.id
			output['username'] = it?.user?.name
			output['responses'] = it?.survey?.respCount
			
			return output
		}
		
		JSON.registerObjectMarshaller(UserDistribution) {
			def output = [:]
			output['country'] = it?.countryName
			output['male'] = it?.male
			output['female'] = it?.female
			output['unknown'] = it?.other
			output['code'] = it?.countryCode
			output['total'] = it?.male + it?.female + it?.other
			
			return output
		}
		
		
		JSON.registerObjectMarshaller(Followers) {
			def output = [:]
			output['id']=it?.id
			output['followerId'] = it?.follower?.id
			output['follower'] = it?.follower?.name
			output['followee'] = it?.followee?.name
			output['followeeId'] = it?.followee?.id
			output['followeeStatus'] = it?.status?.toString()
			output['followeeImageUrl']= it?.followee?.profile?.imageUrlP
			output['followerImageUrl']= it?.follower?.profile?.imageUrlP
			
			return output
		}
		
		JSON.registerObjectMarshaller(Timeline) {
			def output = [:]
			output['id']=it?.id			
			output['performedOnName'] = new JSONObject(it?.performedOn).name 
			output['performedOnId'] = new JSONObject(it?.performedOn).id
			output['url'] = grailsApplication.config.myapp.baseUrl.toString() + "/" + it?.type + "s/" + new JSONObject(it?.performedOn).id
			output['action'] = it?.action
			output['isActive'] = it?.isActive
			output['type'] = it?.type
			output['user']= it?.user
			
			return output
		}
	}
		
	
    
    def destroy = {
    }
}
