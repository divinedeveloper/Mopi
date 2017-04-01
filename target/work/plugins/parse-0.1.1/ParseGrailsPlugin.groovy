class ParseGrailsPlugin {
	def version = "0.1.1"
	def grailsVersion = "2.0 > *"
	def pluginExcludes = [
		"grails-app/controllers/parse/TestController.groovy"
	]
	def title = "Parse Plugin"
	def description = '''\
Uses parse.com API to provide code for mobile platforms including push notifications and other features.
Currently supports push notifications to Android. Plan to support other API in next releases.
'''

	def documentation = "http://grails.org/plugin/parse"

	def license = "APACHE"
//	def organization = [name: "LISNx", url: "http://www.lisnx.com/"]
	def developers = [
		[name: "Srinivas Jaini", email: "srinivasjaini@gmail.com"],
		[name: "Vijay Yalla"]
	]
	def issueManagement = [system: "GITHUB", url: "https://github.com/srinivasjaini/parse/issues"]
	def scm = [url: "https://github.com/srinivasjaini/parse"]
}
