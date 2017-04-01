// locations to search for config files that get merged into the main config;
// config files can be ConfigSlurper scripts, Java properties files, or classes
// in the classpath in ConfigSlurper format

// grails.config.locations = [ "classpath:${appName}-config.properties",
//                             "classpath:${appName}-config.groovy",
//                             "file:${userHome}/.grails/${appName}-config.properties",
//                             "file:${userHome}/.grails/${appName}-config.groovy"]

// if (System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
// }

grails.project.groupId = appName // change this to alter the default package name and Maven publishing destination

// The ACCEPT header will not be used for content negotiation for user agents containing the following strings (defaults to the 4 major rendering engines)
grails.mime.disable.accept.header.userAgents = ['Gecko', 'WebKit', 'Presto', 'Trident']
grails.mime.types = [ // the first one is the default format
    all:           '*/*', // 'all' maps to '*' or the first available format in withFormat
    atom:          'application/atom+xml',
    css:           'text/css',
    csv:           'text/csv',
    form:          'application/x-www-form-urlencoded',
    html:          ['text/html','application/xhtml+xml'],
    js:            'text/javascript',
    json:          ['application/json', 'text/json'],
    multipartForm: 'multipart/form-data',
    rss:           'application/rss+xml',
    text:          'text/plain',
    hal:           ['application/hal+json','application/hal+xml'],
    xml:           ['text/xml', 'application/xml']
]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// Legacy setting for codec used to encode data with ${}
grails.views.default.codec = "html"
grails.resources.rewrite.css = false
grails.resources.adhoc.includes = [
	'/images/**', '/css/**', '/js/**', '/partials/**', '/img/**', '/json/**', '/fonts/**'
]
// The default scope for controllers. May be prototype, session or singleton.
// If unspecified, controllers are prototype scoped.
//grails.controllers.defaultScope = 'singleton'
grails.controllers.defaultScope = 'prototype'
// GSP settings
grails {
    views {
        gsp {
            encoding = 'UTF-8'
            htmlcodec = 'xml' // use xml escaping instead of HTML4 escaping
            codecs {
                expression = 'html' // escapes values inside ${}
                scriptlet = 'html' // escapes output from scriptlets in GSPs
                taglib = 'none' // escapes output from taglibs
                staticparts = 'none' // escapes output from static template parts
            }
        }
        // escapes all not-encoded output at final stage of outputting
        // filteringCodecForContentType.'text/html' = 'html'
    }

	//for sending mail
	mail {
		host = "smtp.webfaction.com"
		port = 587
		username = "rkgtech_dnr"
		password = "pa\$\$word"
		props = ["mail.debug":"true",
				 "mail.smtp.auth":"true"]
	  }
}


grails.converters.encoding = "UTF-8"
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []
// whether to disable processing of multi part requests
grails.web.disable.multipart=false
grails.resources.mappers.bundle.enabled = false
// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']

// configure auto-caching of queries by default (if false you can cache individual queries with 'cache: true')
grails.hibernate.cache.queries = false

// configure passing transaction's read-only attribute to Hibernate session, queries and criterias
// set "singleSession = false" OSIV mode in hibernate configuration after enabling
grails.hibernate.pass.readonly = false
// configure passing read-only to OSIV session by default, requires "singleSession = false" OSIV mode
grails.hibernate.osiv.readonly = false

environments {
    development {
        grails.logging.jul.usebridge = true
		//myapp.baseUrl = "http://1.186.127.199:5800/mopi"
		//myapp.tempImageFolderPath="/Users/nisostech/Development/images"
		myapp.imageBaseUsrl = "http://mopistatic.rkgtechllc.com/"
		myapp.baseUrl = "http://localhost:8080/mopi"
//		myapp.tempImageFolderPath="/home/nisos/images"
		//myapp.baseUrl = "http://localhost:8080/mopi"
		myapp.tempImageFolderPath="C:\\Users\\lenovo\\Desktop\\temp\\"
		myapp.small="O_10x10"
		myapp.medium="T_100x100"
		myapp.big="H_200x200"
		myapp.smallsize=10
		myapp.mediumsize=100
		myapp.bigsize=200
		
    }
    production {
        grails.logging.jul.usebridge = false
		myapp.baseUrl = "http://mopi.rkgtechllc.com"
		//myapp.baseUrl = "https://122.248.211.139"
		myapp.tempImageFolderPath="/home/ubuntu/images/"
		myapp.imageBaseUsrl = "http://mopistatic.rkgtechllc.com/"
		myapp.small="_10x10"
		myapp.medium="_100x100"
		myapp.big="_200x200"
		myapp.smallsize=10
		myapp.mediumsize=100
		myapp.bigsize=200
        // TODO: grails.serverURL = "http://www.changeme.com"
    }
	test {
		myapp.tempIdFileFolderPath="C:\\Users\\nisosadmin\\Desktop\\tempTextFile\\"
		grails.logging.jul.usebridge = true
		//myapp.baseUrl = "http://1.186.127.199:5800/mopi"
		//myapp.tempImageFolderPath="/Users/nisostech/Development/images"
		myapp.imageBaseUsrl = "http://mopistatic.rkgtechllc.com/"
		myapp.baseUrl = "http://localhost:8080/mopi"
//		myapp.tempImageFolderPath="/home/nisos/images"
		//myapp.baseUrl = "http://localhost:8080/mopi"
		myapp.tempImageFolderPath="C:\\Users\\lenovo\\Desktop\\temp\\"
		myapp.small="O_10x10"
		myapp.medium="T_100x100"
		myapp.big="H_200x200"
		myapp.smallsize=10
		myapp.mediumsize=100
		myapp.bigsize=200
		
	}
}

log4j = {
	debug "grails.app.controllers"
	debug "grails.app.domain"
	//debug 'com.odobo'
	//debug 'grails.app.controllers.*'
	//debug 'grails.app.services.*'
	//debug 'grails.app.domain.*'
	//debug 'grails.app.controllers.com.odobo'
	//debug 'grails.app.services.com.odobo'
	debug 'org.pac4j'
	debug 'org.springframework.security'
}
// log4j configuration
log4j.main = {
    // Example of changing the log pattern for the default console appender:
    //
    //appenders {
    //    console name:'stdout', layout:pattern(conversionPattern: '%c{2} %m%n')
    //}
	debug "grails.app.controllers"
	debug "grails.app.domain"
	debug "grails.app.services"
	debug "grails.app.domain"
	debug 'grails.app.contorllers.*'
	debug 'grails.app.services.*'
	debug 'grails.app.domain.*'
	//debug 'com.odobo'
	//debug 'grails.app.controllers.com.odobo'
	//debug 'grails.app.services.com.odobo'
	debug 'grails.app.services.com.themopi.account.*'
	debug 'org.pac4j'
	debug 'org.springframework.security'
    error  'org.codehaus.groovy.grails.web.servlet',        // controllers
           'org.codehaus.groovy.grails.web.pages',          // GSP
           'org.codehaus.groovy.grails.web.sitemesh',       // layouts
           'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
           'org.codehaus.groovy.grails.web.mapping',        // URL mapping
           'org.codehaus.groovy.grails.commons',            // core / classloading
           'org.codehaus.groovy.grails.plugins',            // plugins
           'org.codehaus.groovy.grails.orm.hibernate',      // hibernate integration
           'org.springframework',
           'org.hibernate',
           'net.sf.ehcache.hibernate',
		   'grails.app.contorllers.*',
		   'grails.app.services.*',
		   'grails.app.domain.*'
}

//creatSurveyReportFlagConstant
myapp.creatSurveyReportFlagConstant=10

//email configurations

myapp.forgotPassword = "/#/resetpassword/"
myapp.confirmation = "/api/v1/register/confirmation?giftVerificationToken="
myapp.verifyRegistration="/api/v1/register/account/verification/"
myapp.email.from="dnr@rkgtechllc.com"
myapp.registrationSubject = "Welcome to TheMopiApp"
myapp.registrationEmail = '''Hi, </br> Welcome to TheMopiApp! </br>Please <a href='$link'>click Here</a> to confirm your registration'''
myapp.newEmail = '''Hi, </br> Your email has been changed from $oldEmail to $newEmail'''
myapp.newEmailSubject = "You TheMopiApp email has been changed."
// aws -sdk configuration
grails.plugin.awssdk.accessKey = "AKIAJYUWW7CECGMR2JDQ"
grails.plugin.awssdk.secretKey = "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"
bucketName ="mopi-tmp-media" 

myapp.commentReportCountThreshold = 10

//Spring Security REST properties
grails.plugin.springsecurity.rest.login.active = true
grails.plugin.springsecurity.rest.login.endpointUrl = "/api/login"
grails.plugin.springsecurity.rest.login.failureStatusCode =  401

//decide the json response for the login url , if login is successful
grails.plugin.springsecurity.rest.token.rendering.usernamePropertyName ="email"
grails.plugin.springsecurity.rest.token.rendering.authoritiesPropertyName = "roles"
grails.plugin.springsecurity.rest.token.validation.enableAnonymousAccess = true


//decide the authentication  keys for json 
grails.plugin.springsecurity.rest.login.usernamePropertyName = "email"
grails.plugin.springsecurity.rest.login.passwordPropertyName = "password"

//logout url
grails.plugin.springsecurity.rest.logout.endpointUrl= '/api/logout'

//random generation token
grails.plugin.springsecurity.rest.token.generation.useSecureRandom = true

//decide whether token will be store in database or cache
grails.plugin.springsecurity.rest.token.storage.useGorm = true

//decide the authentication token table details .
grails.plugin.springsecurity.rest.token.storage.gorm.tokenDomainClassName = 'com.themopi.account.AuthenticationToken'
grails.plugin.springsecurity.rest.token.storage.gorm.tokenValuePropertyName = "authToken"
grails.plugin.springsecurity.rest.token.storage.gorm.usernamePropertyName = 'username'

//decide whether it will use params or not if using json then make below parameter false
grails.plugin.springsecurity.rest.login.useRequestParamsCredentials	= false
grails.plugin.springsecurity.rest.login.useJsonCredentials	= true

grails.plugin.springsecurity.filterChain.chainMap = [
	'/api/v1/logout': 'anonymousAuthenticationFilter',
	'/api/v1/login/social': 'anonymousAuthenticationFilter',
	'/api/v1/register/**':'anonymousAuthenticationFilter',
	'/api/v1/surveys': 'JOINED_FILTERS,-anonymousAuthenticationFilter,-exceptionTranslationFilter,-authenticationProcessingFilter,-securityContextPersistenceFilter',  // Stateless chain
	'/api/v1/surveys/**': 'JOINED_FILTERS,-anonymousAuthenticationFilter,-exceptionTranslationFilter,-authenticationProcessingFilter,-securityContextPersistenceFilter',
	'/api/v1/tags/**': 'JOINED_FILTERS,-anonymousAuthenticationFilter,-exceptionTranslationFilter,-authenticationProcessingFilter,-securityContextPersistenceFilter',
	'/api/v1/**': 'JOINED_FILTERS,-anonymousAuthenticationFilter,-exceptionTranslationFilter,-authenticationProcessingFilter,-securityContextPersistenceFilter', // Stateless chain
	//'/**': 'anonymousAuthenticationFilter,restTokenValidationFilter,restExceptionTranslationFilter,filterInvocationInterceptor'
	'/**': 'JOINED_FILTERS,-restTokenValidationFilter,-restExceptionTranslationFilter',                                          // Traditional chain
]
grails.plugin.springsecurity.rest.token.validation.useBearerToken = false
grails.plugin.springsecurity.rest.token.validation.headerName = 'X-Auth-Token'

//mopi search 
myapp.distanceInKilometerForClosestSearch = 150
appsearch{
	trending{
		maxLimit = 100
		createdAfterInDays = 7
		distance = 100 //in km
	}
	sponsor{
		maxLimit = 100
		createdAfterInDays = 15
	}
	explore{
		maxLimit = 500
	}
}
//cors configurations

cors.url.pattern = '/api/*'
cors.headers = ['Access-Control-Allow-Origin': '*']
cors.enabled = true

// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.dao.reflectionSaltSourceProperty = 'salt'
grails.plugin.springsecurity.userLookup.userDomainClassName = 'com.themopi.account.User'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'com.themopi.account.UserRole'
grails.plugin.springsecurity.authority.className = 'com.themopi.account.Role'
grails.plugin.springsecurity.controllerAnnotations.staticRules = [
	'/':                              ['permitAll'],
	'/index':                         ['permitAll'],
	'/index.gsp':                     ['permitAll'],
	'/assets/**':                     ['permitAll'],
	'/**/js/**':                      ['permitAll'],
	'/**/css/**':                     ['permitAll'],
	'/**/images/**':                  ['permitAll'],
	'/**/favicon.ico':                ['permitAll']
]


grails.plugin.springsecurity.interceptUrlMap = [
	'/':                  ['permitAll'],
	'/index':             ['permitAll'],
	'/index.gsp':         ['permitAll'],
	'/assets/**':         ['permitAll'],
	'/**/js/**':          ['permitAll'],
	'/**/css/**':         ['permitAll'],
	'/**/images/**':      ['permitAll'],
	'/**/favicon.ico':    ['permitAll'],
	'/login/**':          ['permitAll'],
	'/logout/**':         ['permitAll'],
	'/dashboard/**':	  ['permitAll']

]


grails {
	plugin {
		facebooksdk {
			app = [
				id: 518383671632152,
				permissions: [],
				secret: 'dfc19239efe7ed1e419d301d3217cc67'
			]
		}
	}
}

email.send = "on"   //value will be "on" or "off"
myapp.facebook.appID= "737598589646877"
myapp.facebook.secret= "db67dea029be86e5401c183259c62f02"//"dfc19239efe7ed1e419d301d3217cc67"
myapp.googlePlus.clientSecret=""

parse.parseApplicationId='az3Utl8vlhjLIOSx5OizyGxeI8SB65b3nJmEFcdH'
parse.parseRestApiKey ='O4f4qrShH1WBCofHiLx3qESi6n03SmCDDj2MRaGb'
//Exceptions Messages
customExceptions{
			security{
				fourZeroFour{
					logout{
						errorCode=4404
						status = 404
						message="Authorization token not found."
						extendedMessage="Trying to find authorization token failed. No entry available"
						moreInfo="User may not have logged in"
					}
				}
			}
			timeline{
				fourZeroFour{
					timeline{
						errorCode=4404
						status = 404
						message="Please create valid timeline."
						extendedMessage="Please create valid timeline."
						moreInfo="Please check isActive or user or action type valid timeline."
					}
				}
				fourZeroZero{
					timeline{
						errorCode=4400
						status = 400
						message="Please update valid timeline."
						extendedMessage="Please update valid timeline."
						moreInfo="Please check isActive or user or action type valid timeline."
					}
				}
				fourZeroOne{
					timeline{
						errorCode=4401
						status = 401
						message="No timeline are availables for this user."
						extendedMessage="No timeline are availables for this user."
						moreInfo="No timeline are availables for this user."
					}
				}
			}
			password{
				fourZeroZero{
					password{
						errorCode=10400
						status = 400
						message="New password cannot be the same as the last password used."
						extendedMessage="New password cannot be the same as the last password used."
						moreInfo="Please try a different password."
					}
				}
			}
			image{
				fourZeroZero{
					image{
						errorCode=10400
						status = 400
						message="Image was not found in your request."
						extendedMessage="User didn't submit am image."
						moreInfo="Sorry, an image was not found with your request."
					}
				}
			}
			account{
				fourZeroFour{
					forgotpassword{
						errorCode=3404
						status = 404
						message="User not found."
						extendedMessage="Could not find the user in our database"
						moreInfo="User not available in our database"
					}
					role{
						errorCode = 3404
						status = 404
						message = "Role not found."
						extendedMessage= "Trying to assign a role that doesn't exists."
						moreInfo = "This user role doesn't exist in the application."
					}
					user{
						errorCode = 3404
						status = 404
						message = "Account was not found."
						extendedMessage= "Operation failed to find user using this email address."
						moreInfo = "An account with the email in your request does not exist."
					}
					invalidEmail{
						errorCode = 3404
						status = 404
						message = "Invalid Email. Please try with new email."
						extendedMessage= "Email doesn't contain '@' and '.'."
						moreInfo = "Email doesn't contain '@' and '.'."
					}
					socailLoginType {
						errorCode = 3404
						status = 404
						message = "Facebook and Google+ login is only allowed."
						extendedMessage= "Wrong providerString param is provided , other than 'facebook' and 'google'."
						moreInfo = "Wrong providerString param is provided , other than 'facebook' and 'google'."
					}
					invalidAccessToken {
						errorCode = 3404
						status = 404
						message = "AccessToken is not valid."
						extendedMessage= "Either AccessToken is expired or wrong excess token is used."
						moreInfo = "Unable to retrieve profile of user."
					}
					registration{
						errorCode = 3404
						status = 404
						message = "Account was not found."
						extendedMessage= "Operation failed to find user associated with this token."
						moreInfo = "Account associated with the token  in this request does not exist."
					}
					verificationToken{
						errorCode=3404
						status = 404
						message="Verification Token not saved"
						extendedMessage="Error while saving verification token for user"
						moreInfo="Error while saving verification token for user"
					}
					survey{
						errorCode = 5404
						status = 404
						message = "Survey not found."
						extendedMessage= "Operation failed while trying to find a survey ."
						moreInfo = "Survey associated with the survey id  in the request does not exist."
					}
					giftVerificationToken{
						errorCode=3404
						status = 404
						message="Gift Verification Token not found"
						extendedMessage="Error while getting gift verification token for user"
						moreInfo="Error while getting gift verification token for user"
					}
				}
				fourZeroOne{
					notAuthorized{
						errorCode = 3401
						status = 401
						message = "You are not authorized to perform this action."
						extendedMessage= "Trying to perform an action that is not allowed for this user role"
						moreInfo = "Role specific error: You are authenticated but not authorized to perform this action"
					}
					
				}
				fourZeroZero{
					userRole{
						errorCode = 3400
						status = 400
						message = "Assigning a role to user failed."
						extendedMessage= "Trying to assign a role to user failed. The user or role does not exist"
						moreInfo = "User or role does not exist"
					}
					profileNotComplete{
						errorCode = 3400
						status = 400
						message = "Bad Request. Please complete your profile."
						extendedMessage= "Bad Request. Please complete your profile. Hometown is not available."
						moreInfo = "Bad Request. Please complete your profile. Hometown is not available."
						
					}
					user{
						errorCode = 3400
						status = 400
						message = "Validation error. Creating a user failed"
						extendedMessage= "There is a validation error in creating the user. Fields failed validation checks."
						moreInfo = "There is a validation error in creating the user. Fields failed validation checks."

					}

					hashTag{
						errorCode = 6400
						status = 400
						message = "Validation error. Failed to create the tag"
						extendedMessage= "There is a validation error in creating the tag. Fields failed validation checks."
						moreInfo = "There is a validation error in creating the user. Fields failed validation checks."

					}
					address{
						errorCode = 3400
						status = 400
						message = "Validation error. Operation to create/update address failed"
						extendedMessage= "There is a validation error in creating/updating the address. Fields failed validation checks."
						moreInfo = "There is a validation error in creating/updating the address. Fields failed validation checks."

					}
					profile{
						errorCode = 3400
						status = 400
						message = "Validation error.Operation to create/update profile failed"
						extendedMessage= "There is a validation error in creating/updating the profile. Fields failed validation checks."
						moreInfo = "There is a validation error in creating/updating the profile.Fields failed validation checks"

					}
					forgotpassword{
						errorCode=3400
						status = 400
						message="Validation error in password"
						extendedMessage="Password cannot be blank or null"
						moreInfo="Resetting password failed with validation errors"
					}
					userPassword{
						errorCode=3400
						status = 400
						message="Validation error. Operation to update user password failed"
						extendedMessage="Error while saving new password for user. Validation error. "
						moreInfo="Error while saving new password for user. Validation error. "
					}
					giftVerificationToken{
						errorCode=3400
						status = 400
						message="Gift Verification Token not saved"
						extendedMessage="Error while saving gift verification token for user"
						moreInfo="Error while saving gift verification token for user"
					}
					userGiftConfirmation{
						errorCode=3400
						status = 400
						message="Error while making user eligible for gifts."
						extendedMessage="Error while making user eligible for gifts"
						moreInfo="Error while making user eligible for gifts"
					}
				}
				fourZeroNine{
					userCreate{
						errorCode = 3409
						status = 409
						message = "Email already exists.Please try another email."
						extendedMessage= "This email already exists in our system. Email must be unique."
						moreInfo = "Unique email constraint error."
					}
					username{
						errorCode = 3409
						status = 409
						message = "Username already exists. Please try another username."
						extendedMessage= "Username email already exists. Username unique constraint failure."
						moreInfo = "Unique username constraint error."
				    }

				}
			}
			
			connection{
				fourZeroNine{
					connection{
						errorCode = 9403
						status = 403
						message = "You have already requested to follow the user  ."
						extendedMessage= "A follow request already exists from you to this user."
						moreInfo = "A follow request already exists."

					}
					self{
						errorCode = 9403
						status = 403
						message = "You can't follow yourself."
						extendedMessage= "You can't follow yourself."
						moreInfo = "You can't follow yourself."
					}
				}
				fourZeroZero{
					connection{
						errorCode = 9400
						status = 400
						message = "This follow request was not found"
						extendedMessage= "This follow request was not found."
						moreInfo = "This follow request was not found."

					}
				}
			}
			survey{
				fourZeroFour{
					address{
						errorCode = 3400
						status = 400
						message = "Validation error. Operation to create/update location failed"
						extendedMessage= "There is a validation error in creating/updating the location. Fields failed validation checks."
						moreInfo = "There is a validation error in creating/updating the location. Fields failed validation checks."

					}
					like{
						errorCode = 3400
						status = 400
						message = "Validation error. Operation to like/unlike survey failed"
						extendedMessage= "There is a validation error in like/unlike survey. Fields failed validation checks."
						moreInfo = "There is a validation error in like/unlike survey. Fields failed validation checks."

					}
					uploadimage{
						errorCode = 3400
						status = 400
						message = "Upload image action failed"
						extendedMessage= "There is a validation error in upload image action"
						moreInfo = "There is a validation error in upload image action"
					}
					survey{
						errorCode = 5404
						status = 404
						message = "This survey was not found."
						extendedMessage= "Operation failed to find Survey ."
						moreInfo = "Survey with the mention id in the request does not exist."
					}
					surveyQuestion {
						errorCode = 5404
						status = 404
						message = "Question doesn't exist."
						extendedMessage= "Operation failed. Question with specifiedId doesn't exist."
						moreInfo = "Operation failed. Question doesn't exist. Response is null."
					}
				}
				fourZeroZero{
					survey{
						errorCode = 5400
						status = 400
						message = "Validation error. Failed to create survey"
						extendedMessage= "There is a validation error in creating the survey. Fields failed the validation checks."
						moreInfo = "There is a validation error in creating the survey. Fields failed the validation checks."

					}
					exploreOffset{
						errorCode = 5400
						status = 400
						message = "Bad Request . Maximum number of surveys returned will 500."
						extendedMessage= "There is a validation error in searching the surveys where searchCategoryType is 500 . Offset can't be greater than or equal to 500."
						moreInfo = "There is a validation error in searching the surveys where searchCategoryType is 500 . Offset can't be greater than or equal to 500."
					}
					surveyActiveStatus {
						errorCode = 5400
						status = 400
						message = "Validation error. Couldn't update survey status to active because a survey must have at least one question."
						extendedMessage= "There is a validation error in updating the survey. User can activate the status to active only if the survey has at least one question."
						moreInfo = "There is a validation error in updating the survey. Fields failed validation checks."
					}
					surveyQuestionCount {
						errorCode = 5400
						status = 400
						message = "Couldn't add more questions. Survey can have maximum 15 questions."
						extendedMessage= "Operation failed. Survey can have maximum 15 questions."
						moreInfo = "Operation failed. Survey can have maximum 15 questions."
					}
					multipleChoice{
						errorCode = 5400
						status = 400
						message = "Couldn't add more questions. A question must have at least 2 options."
						extendedMessage= "Operation failed. A question must have at least 2 options."
						moreInfo = "Operation failed. Question must have at least 2 options."

					}

					multipleChoiceOption{
						errorCode = 5400
						status = 400
						message = "Could not add more choices to this question. A question must have min 2 and max 6 choices."
						extendedMessage= "Operation failed. A question must have min 2 and max 6 choices."
						moreInfo = "Operation failed. A question must have min 2 and max 6 choices."

					}

					startDateValidation{
						errorCode = 5400
						status = 400
						message = "Survey start date cannot be in the past"
						extendedMessage= "Validation error. Survey cannot start in the past"
						moreInfo = "Validation error. Survey cannot start in the past"

					}

					startEndDateValidation{
						errorCode = 5400
						status = 400
						message = "Start date cannot be null if end date is specified"
						extendedMessage= "Validation error. Start date cannot be null if end date is specified"
						moreInfo = "Validation error. Start date cannot be null if end date is specified"

					}
					surveyReports{
						errorCode = 5400
						status = 400
						message = "Survey Reports should not be empty in this survey"
						extendedMessage= "Survey Reports should not be empty For Survey"
						moreInfo = "Survey Reports should not be empty For Survey"

					}
					startDateGreaterThanEndDate{
						errorCode = 5400
						status = 400
						message = "Start date must be greater than end date"
						extendedMessage= "Validation error. Start date must be greater than end date"
						moreInfo = "Validation error. Start date must be greater than end date"

					}
					invalidCountry{
						errorCode = 5400
						status = 400
						message = "Please select Country from drop down list"
						extendedMessage= "Validation error. Country names are not valid and dont have lat, long values"
						moreInfo = "Validation error. Country names are not valid and dont have lat, long values"

					}
					invalidCity{
						errorCode = 5400
						status = 400
						message = "Please select City from drop down list"
						extendedMessage= "Validation error. City data is not valid and dont have lat, long values"
						moreInfo = "Validation error. City data is not valid and dont have lat, long values"

					}
					QuestionQuery{
						errorCode = 5400
						status = 400
						message = "Question query cannot be blank or null"
						extendedMessage= "Question query cannot be blank or null"
						moreInfo = "Question query cannot be blank or null"

					}
				}
				fourZeroThree{
					survey{
						errorCode = 5403
						status = 403
						message = "Survey can be updated only in draft mode"
						extendedMessage= "Update operation failed. Survey can be updated only in draft mode"
						moreInfo = "Update operation failed. Survey can be updated only in draft mode"

					}
					updateSurvey{
						errorCode = 5403
						status = 403
						message = "Only a survey owner can update a survey"
						extendedMessage= "Update operation failed. Survey can be updated only by its owner"
						moreInfo = "Update operation failed. Survey can be updated only by its owner"

					}
					surveyActiveStatus {
						errorCode = 5403
						status = 403
						message = "Could not change status back to draft mode"
						extendedMessage= "There is a restriction. Could not change status back to draft mode"
						moreInfo = "Sorry, you cannot not change status back to draft mode"
					}
					image{
						errorCode = 5403
						status = 403
						message = "Survey Image can be updated only in draft mode"
						extendedMessage= "Update operation failed. Survey Image can be updated only in draft mode"
						moreInfo = "Update operation failed. Survey Image can be updated only in draft mode"

					}
					questionImage{
						errorCode = 5403
						status = 403
						message = "Question Image can be updated only if survey is in draft mode"
						extendedMessage= "Update operation failed. Question Image can be updated only if survey is in draft mode"
						moreInfo = "Update operation failed. Question Image can be updated only if survey is in draft mode"

					}
					optionImage{
						errorCode = 5403
						status = 403
						message = "Option Image can be updated only if survey is in draft mode"
						extendedMessage= "Update operation failed. Option Image can be updated only if survey is in draft mode"
						moreInfo = "Update operation failed. Option Image can be updated only if survey is in draft mode"

					}
				}
			}
			comment{
				fourZeroFour{
					comment{
						errorCode = 7404
						status = 404
						message = "Comment not found or it is already hidden"
						extendedMessage= "Operation failed. Comment with specified Id doesn't exist or is already hidden"
						moreInfo = "Operation failed. Comment with specified Id doesn't exist or is already hidden"

					}
				}
				fourZeroZero{
					comment{
						errorCode = 7400
						status = 400
						message = "Validation error. Failed to create comment"
						extendedMessage= "There is a validation error in creating the comment. Fields failed the validation checks."
						moreInfo = "There is a validation error in creating the comment. Fields failed with validation checks."

					}
					commentReport{
						errorCode = 7400
						status = 400
						message = "Validation error. Failed to create comment report"
						extendedMessage= "There is a validation error in creating the comment report. Fields failed the validations."
						moreInfo = "There is a validation error in creating the comment report. Fields failed the validations."

					}
				}
				fourZeroThree{
					comment{
						errorCode = 7403
						status = 403
						message = "Authorization failed. User not allowed to hide a comment"
						extendedMessage= "User is not authorized to hide a comment"
						moreInfo = "User is not authorized to hide a comment"

					}
				}

			}
			surveyResponse{
				fourZeroThree{
					responseexists{
						errorCode = 8403
						status = 403
						message = "You have already responded to this query."
						extendedMessage= "A response to particular question of this survey already exists."
						moreInfo = "A response to particular question of this survey already exists."

					}
				}
				fourZeroFour{
					surveyResponse{
						errorCode = 8400
						status = 400
						message = "Response was not saved for this particular question"
						extendedMessage= "Bad request. Not able to save the Response"
						moreInfo = "Bad request. Not able to save the Response."

					}
					
				}
				fourZeroZero{
					commentQuestion{
						errorCode = 8400
						status = 400
						message = "Bad Request. Question is not a comments question"
						extendedMessage= "Bad request. Result was not returned as question type is not of type text"
						moreInfo = "Bad request. Result was not returned as question type is not of type text"
					}
					surveyStatus{
						errorCode = 8400
						status = 400
						message = "Bad Request. You are not allowed to respond to this question. Question is either not Active or has been flagged for removal"
						extendedMessage= "Bad request. Question is either not Active or has been flagged for removal."
						moreInfo = "Bad request. Question is either not Active or has been flagged for removal."
					
					}
					answerNotAvailable{
						errorCode = 8400
						status = 400
						message = "Bad Request.You have entered wrong answer."
						extendedMessage= "Bad request. Either choice is not available in case of binary/multiplchoice or in case of scale value must be between 1 and 10."
						moreInfo = "Bad request. Either choice is not available in case of binary/multiplchoice or in case of scale value must be between 1 and 10."
					}
				}
			}
			dashboard{
				fourZeroFour{}
				fourZeroZero{
					dashboard{
						errorCode = 7400
						status = 400
						message = "No Data Found"
						extendedMessage= "No Data Found"
						moreInfo = "Please Enter New Data "

					}
				}
			}
			gift{
				fourZeroThree{
					gift{
						errorCode = 3403
						status = 403
						message = "Gift cannot be created as survey is active or flagged."
						extendedMessage= "Gift cannot be created as survey is active or flagged."
						moreInfo = "Gift cannot be created as survey is active or flagged."
					}
					image{
						errorCode = 3403
						status = 403
						message = "Gift Image can be updated only when survey is in draft mode"
						extendedMessage= "Update operation failed. Gift Image can be updated only when survey is in draft mode"
						moreInfo = "Update operation failed. Gift Image can be updated only when survey is in draft mode"

					}
				}
				fourZeroFour{
					gift{
						errorCode = 3404
						status = 404
						message = "Gift already Deleted. Please try another gift."
						extendedMessage= "Gift is already Deleted. Please try another gift."
						moreInfo = "Please try another gift."
					}
				}
				fourZeroZero{
					gift{
						errorCode = 7400
						status = 400
						message = "Validation error. Failed to create gift"
						extendedMessage= "There is a validation error in creating the gift. Fields failed the validation checks."
						moreInfo = "There is a validation error in creating the gift. Fields failed the validation checks."

					}
				}
				fourZeroNine{
					gift{
						errorCode = 3409
						status = 409
						message = "Gift code already exists.Please try another gift code."
						extendedMessage= "Gift code is already exists. Gift code unique constraint."
						moreInfo = "Unique gift code constraint error."
					}
				}
			
			}
			error{
				fiveZeroZero{
					errorCode = 11500
					status = 500
					message = "Internal server error. Something went wrong."
					extendedMessage= "Internal server error. Something went wrong in api mostly run time exceptions"
					moreInfo = "Internal server error. Something went wrong in api mostly run time exceptions"
				}
			}
			
		}
