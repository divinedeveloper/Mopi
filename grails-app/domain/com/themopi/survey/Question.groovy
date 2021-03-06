package com.themopi.survey

import com.google.gson.JsonArray
import com.themopi.account.User
import java.util.Date;
import org.json.JSONArray
import org.json.JSONObject

class Question {
	
	User lastUpdatedBy
	
	//type will be enum
	String  query, imageUrlQ
	QuestionType questionType
	
	List option = []
	List<Options> options = []
	boolean isActive
	
	Date dateCreated
	Date lastUpdated, deletedAt
  
	enum QuestionType{
		multiplechoice,scale,text,binary
	}
	
	static hasMany = [option:String,options:Options]
	static embeded = ['options']
    static constraints = {
		questionType blank: false, nullable: false
		options blank:true
		query blank: true, nullable: true
		option blank: false, nullable: false
		imageUrlQ nullable:true
		deletedAt nullable:true
		
	}
	
	def questionResponseCount(){
		def response = Response.countByAnswerForQuestion(this)
		if(response  == null) return 0;
		return response
	}
	
	static mapping = {
		version false
	}

}

class Options{
	 Long id
	 String value 
	 String image
	 
	 static constraints = {
		 image nullable:true
	 }
	 
	 static mapping = {
		 version false
	 }
}

