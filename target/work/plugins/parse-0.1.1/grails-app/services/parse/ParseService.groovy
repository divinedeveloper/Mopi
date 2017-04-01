package parse

import javax.net.ssl.HttpsURLConnection

class ParseService {

	static transactional = false

	def grailsApplication

	String sendNotification(String requestData) {
		HttpsURLConnection conn = getConnection()
		String response = ""
		try {

			conn.setDoOutput(true)
			DataOutputStream wr = new DataOutputStream(conn.getOutputStream())
			wr.writeBytes(requestData)
			wr.flush()
			wr.close()
			response= conn.getResponseMessage()
			log.debug(" Response Received :$response")
		}
		finally {
			conn?.disconnect()
		}
		return response
	}

	private HttpsURLConnection getConnection() throws IOException{
		HttpsURLConnection conn = new URL("https://api.parse.com/1/push").openConnection()

		 // Allow Inputs
		conn.setDoInput(true)

		// Allow Outputs
		conn.setDoOutput(true)

		// Don't use a cached copy.
		conn.setUseCaches(false)

		// Use a post method.
		conn.setRequestMethod("POST")
		conn.setRequestProperty("Connection", "Keep-Alive")

		def parseConfig = grailsApplication.config.parse

		conn.setRequestProperty("X-Parse-Application-Id", parseConfig.parseApplicationId)//"8rOOQ5Q2a54cTdhwcohTgenTeDsVojZToRHktguz")
		conn.setRequestProperty("X-Parse-REST-API-Key", parseConfig.parseRestApiKey)// "p9u9RbMM088BPqa7u1L5gRdGcRmSnVYtaoipsKsK")
		conn.setRequestProperty("Content-Type", "application/json")
		return conn
	}
}
