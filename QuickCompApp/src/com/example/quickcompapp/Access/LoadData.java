package com.example.quickcompapp.Access;

import android.os.AsyncTask;

public class LoadData extends AsyncTask<String, Void, String> {
	String returned;
	
	String function;
	String identifier;
	String mnemonic;
	String properties;
	
	public LoadData( String function, String identifier, String mnemonic, String properties ) {
		this.function = function;
		this.identifier = identifier;
		this.mnemonic = mnemonic;
		this.properties = properties;
	}

	@Override
	protected String doInBackground(String... arg0) {
		GetData data = new GetData();
		data.setUrlParameters( function, identifier, mnemonic, properties );
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
