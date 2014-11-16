package com.example.quickcompapp;

import android.os.AsyncTask;

public class LoadData extends AsyncTask<String, Void, String> {
	String returned;
	
	String function;
	String identifier;
	String mnemonic;
	
	public LoadData( String function, String identifier, String mnemonic ) {
		this.function = function;
		this.identifier = identifier;
		this.mnemonic = mnemonic;
	}
	
	@Override
	protected String doInBackground(String... arg0) {
		GetData data = new GetData();
		data.setUrlParameters( function, identifier, mnemonic, "stub" );
		// TODO Auto-generated method stub
		try {
			returned = data.getJSON();
			
        } catch (Exception e) {
            e.printStackTrace();
        }
		return returned;
	}
	
	protected void onPostExecute( String result ) {
		//MainActivity.this.setResponse( result );
	}
}
