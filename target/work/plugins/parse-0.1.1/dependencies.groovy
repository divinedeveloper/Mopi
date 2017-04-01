grails.project.work.dir = 'target'

grails.project.dependency.resolution = {

	inherits 'global'
	log 'warn'

	repositories {

		
		grailsCentral()
		mavenCentral()
				 // uncomment the below to enable remote dependency resolution
				 // from public Maven repositories
		mavenLocal()
		//mavenCentral()
		//mavenRepo "http://snapshots.repository.codehaus.org"
				 //mavenRepo "http://repository.codehaus.org"
				 //mavenRepo "http://download.java.net/maven/2/"
	}
	plugins {
		build ':release:2.2.1', ':rest-client-builder:1.0.3', {
			export = false
		}
	}
}
