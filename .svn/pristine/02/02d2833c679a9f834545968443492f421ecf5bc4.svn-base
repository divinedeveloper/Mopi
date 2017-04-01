package com.themopi.pushnotification

import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.DefaultHttpClient
import org.json.JSONObject
import org.apache.http.util.EntityUtils;
import org.springframework.security.access.annotation.Secured

@Secured('permitAll')
class NotificationController {
	def parseService
	def grailsApplication
	
	//def parseConfig = grailsApplication.config.parse;
	//parse.parseApplicationId='az3Utl8vlhjLIOSx5OizyGxeI8SB65b3nJmEFcdH'
	//parse.parseRestApiKey ='O4f4qrShH1WBCofHiLx3qESi6n03SmCDDj2MRaGb'
	 String APPLICATION_ID = 'az3Utl8vlhjLIOSx5OizyGxeI8SB65b3nJmEFcdH' // parseConfig.parseApplicationId;
	 String REST_API_KEY = 'O4f4qrShH1WBCofHiLx3qESi6n03SmCDDj2MRaGb' //parseConfig.parseRestApiKey;
	 String PUSH_URL = "https://api.parse.com/1/push";
	
    def index() {
		println "notification ===================== "
		//String[] channels = {"_EveryOne"}
		String type = "ios";
		Map<String, String> data = new HashMap<String, String>();
		data.put("alert", "push data testing 2");
	
		try {
			sendPost(type, data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//render parseService.sendNotification("Test message")
	}
	
	private void sendPost(String type, Map<String, String> data) throws Exception {
		JSONObject jo = new JSONObject();
		jo.put("channels", ['Everyone']);
		if(type != null) {
			//??type?????android?ios???
			JSONObject json = new JSONObject()
			
			jo.put("type",'ios');
		}
		jo.put("data", data);
	
		pushData(jo);
	}
	
	private void pushData(JSONObject postData) throws Exception {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpResponse response = null;
		HttpEntity entity = null;
		String responseString = null;
		HttpPost httpost = new HttpPost(PUSH_URL);
		httpost.addHeader("X-Parse-Application-Id", APPLICATION_ID);
		httpost.addHeader("X-Parse-REST-API-Key", REST_API_KEY);
		httpost.addHeader("Content-Type", "application/json");
		StringEntity reqEntity = new StringEntity(postData.toString());
		httpost.setEntity(reqEntity);
		response = httpclient.execute(httpost);
		entity = response.getEntity();
		
		if (entity != null) {
			 responseString = EntityUtils.toString(response.getEntity());
		}
	
		System.out.println(responseString);
	}
}
