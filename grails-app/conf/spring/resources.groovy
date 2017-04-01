// Place your Spring DSL code here

beans = {
	userDetailsService(com.themopi.customizePasswordSalt.CustomUserDetailsService){
		grailsApplication = ref('grailsApplication')
	}
	
	/*basicAuthenticationEntryPoint(com.themopi.cors.CustomAuthenticationEntryPoint) { bean ->
		realmName = 'Grails Realm'
	}*/
}