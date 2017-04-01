package com.themopi.utils

import com.mongodb.util.JSONParser
import org.json.JSONArray
import org.json.JSONObject
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;



class JsonReadWrite {
	String tempDir = "C:\\Users\\PREMPRAKASH\\Desktop\\tempTextFile";
	def write()
	{
		File g = new File(tempDir+File.separator+'jsonObject.txt')
        g.createNewFile()
        def json = new JsonBuilder()
        json {
            "result" result
            }       
        g.setText(json.toString())
	}

	def read()
	{
		def json = new JsonBuilder()
		File f = new File(tempDir+File.separator+'jsonObject.txt')
		def slurper = new JsonSlurper()
		def jsonText = f.getText()
		json = slurper.parseText( jsonText )
	}
}
