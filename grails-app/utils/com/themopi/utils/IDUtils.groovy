package com.themopi.utils

class IDUtils {
	
	  def NUMBER_OF_CHARS = 5
	  def CHARS = ('0'..'9') +
			  ('a'..'h') +
			  ('j'..'k') +
			  ('m'..'z') +
			  ('A'..'H') +
			  ('J'..'K') +
			  ('M'..'Z')
	
	  def random = new Random()
	
	  def generateID() {
		def id = ""
		for ( i in 1..NUMBER_OF_CHARS ) {
		  id += CHARS[random.nextInt(CHARS.size())]
		}
		return id
	  }
	
	}
