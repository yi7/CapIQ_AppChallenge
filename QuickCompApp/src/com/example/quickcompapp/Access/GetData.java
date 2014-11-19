package com.example.quickcompapp.Access;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.codec.binary.Base64;
// Must add commons-codec-1.9.jar to build path

public class GetData {
	
	private String urlParameters;
	
	public void setUrlParameters(String function, String identifier, String mnemonic, String properties ) {
		urlParameters = "inputRequests="
				 + "{inputRequests:"
				 	+ "["
				 		+ "{function:"+ "\'" + function + "\'"
				 		+ ",identifier:" + "\'" + identifier + "\'"
				 		+ ",mnemonic:" + "\'" + mnemonic + "\'"
				 		+ ",properties:{STARTRANK:'1',ENDRANK:'5'}},"
				 	+ "]"
				 + "}";
	}
	
	public String getJSON() throws Exception {
		
		String username = "SPCIQNJIT1@spcapitaliq.com"; // ENTER USERNAME HERE
		String password = "aamyySPno51"; // ENTER PASSWORD HERE

		String url = "https://sdk.gds.standardandpoors.com/gdssdk/rest/v2/clientservice.json";
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
		
		String usernamePass = username + ":" + password;
		String encoding = new String(Base64.encodeBase64(usernamePass.getBytes()));
		
		// Request header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", "Apache-HttpClient/4.1.1 (java 1.5)");
		con.setRequestProperty("Accept-Encoding", "gzip,deflate");
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		con.setRequestProperty("Authorization", "Basic " + encoding);
		//con.setRequestProperty("Content-Length","1575");
		con.setRequestProperty("Host","sdk.gds.standardandpoors.com");
		con.setRequestProperty("Connection","Keep-Alive");

		// Request body
		/*String urlParameters = "inputRequests="
							 + "{inputRequests:"
							 	+ "["
							 		+ "{function:'GDSHE',identifier:'GOOGL',mnemonic:'IQ_QUICK_COMP',properties:{STARTRANK:'1',ENDRANK:'5'}},"
							 	+ "]"
							 + "}";*/

		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();
		
		BufferedReader in = new BufferedReader( new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		String responseString = response.toString();		
		return responseString;
 
	}

}
