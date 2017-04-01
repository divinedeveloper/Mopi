package com.themopi.utils

import java.util.Random;

class GenerateDataUtils {
	static final String POSSIBLE_STRING = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	static final String ROLEID = "234";
	static final String MAXNO = "1234567890";
	static Random random = new Random();

	/**
	 * Generates alphanumeric string of 5 characters 
	 * @return
	 * alphanumeric string of 5 chars
	 */
	def randomString() {
		def randomString = ""
		for(int i=0; i < 5;i++)
		randomString += POSSIBLE_STRING.charAt( random.nextInt(POSSIBLE_STRING.length()));
		return randomString
	}
	
	/**
	 * Gives role Id to be assigned for user
	 * @return
	 * either 2 or 3 or 4 as string
	 */
	def roleIdGenerator() {
		def randomRoleId = ""
		randomRoleId += ROLEID.charAt( random.nextInt(ROLEID.length()));
		return Integer.parseInt(randomRoleId)
	}
	
	def generateRandomEmail(){
		String email= ""
		email =  randomString()+"@rkgtechllc.com"
		return email
	   }
	def generateSurveyName(){
		def survey= ""
		survey =  "Survey" + ROLEID.charAt( random.nextInt(ROLEID.length()))
		return survey
	}
	def genereateOffset()
	{
		return 1;
	}
	def genereateLimit()
	{
		return 10;
	}
	
	def generateGiftCode()
	{
		String randomString = ""
		for(int i=0; i < 5;i++)
		randomString += POSSIBLE_STRING.charAt( random.nextInt(POSSIBLE_STRING.length()));
		return randomString
	}
	def	generateGiftName()
	{
		String randomString = ""
		for(int i=0; i < 5;i++)
		randomString += POSSIBLE_STRING.charAt( random.nextInt(POSSIBLE_STRING.length()));
		return randomString
	}
	def generateGiftMaxNo()
	{
		String randomString = ""
		for(int i=0; i < 2;i++)
		randomString += MAXNO.charAt( random.nextInt(MAXNO.length()));
		int maxNo = Integer.parseInt(randomString)
		return maxNo
	}
	def getImageUrlG()
	{
		return "https://rkgtechllc.com/mopi.jpg"
	}
}
