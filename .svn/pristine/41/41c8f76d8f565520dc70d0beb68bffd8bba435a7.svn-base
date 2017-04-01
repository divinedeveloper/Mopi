package com.themopi.account

class VerificationToken {
	
	String email , verificationToken
	
	static belongsTo = [user:User]

    static constraints = {
		email email:true,blank:false
		verificationToken blank:false
    }
}
