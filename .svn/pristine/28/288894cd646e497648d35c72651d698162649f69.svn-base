package com.mopi.apis.dashboard

import java.util.Map;

import com.themopi.survey.SurveyReports
import grails.converters.JSON
import com.themopi.exceptions.SurveyException
import org.json.JSONObject
import org.springframework.security.access.annotation.Secured;
@Secured('permitAll')
class DashboardController {

	
	def dashboardService
	Map responseObject = [:]
    def index() { }
	
	def getDashboard() 
	{ 
		try{
			JSONObject dashboardJsonlist=dashboardService.getAllList(request);
			responseObject.error = false
			responseObject.resp = dashboardJsonlist.map
			response.setStatus(200)
			render responseObject as JSON
		}catch(Exception e){
			log.error e.errorResponse()
			response.setStatus(e.errorResponse().status)
			render e.errorResponse()
		}
		
	}
	
	
	
	def graphUserDistribution(){
		def graphData = dashboardService.getGraph(params.type);
		render graphData as JSON
	}
	
	def mapUserDistribution(){
		def mapData = dashboardService.getMapData();
		responseObject.error = false
		responseObject.resp = mapData
		response.setStatus(200)
		render responseObject as JSON
		
	}
	
}
