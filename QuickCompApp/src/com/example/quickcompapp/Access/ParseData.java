package com.example.quickcompapp.Access;

import com.example.quickcompapp.Response.GDSSDKResponse;
import com.example.quickcompapp.Response.Response;
import com.google.gson.Gson;

public class ParseData {
	
	private String json;
	private GDSSDKResponse[] sdkResponse;
	
	public ParseData( String json ){
		this.json = json;
	}
	
	public void createGson() {
		Gson gson = new Gson();
		Response response = gson.fromJson( json, Response.class );
		sdkResponse = response.getGDSSDKResponse();
	}
	
	public GDSSDKResponse[] getSdkResponse() {
		return sdkResponse;
	}

}
