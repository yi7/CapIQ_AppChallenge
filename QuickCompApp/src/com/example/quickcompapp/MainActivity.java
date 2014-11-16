package com.example.quickcompapp;

import java.io.Serializable;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
//import android.os.StrictMode;
// Must add commons-codec-1.9.jar to build path

public class MainActivity extends Activity {
	
	private Button mGraphButton;
	private Button mTableButton;
	
	//private GDSSDKResponse[] sdkResponse;
	private String returned;
	
	private static final String TAG = "MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build();
		//StrictMode.setThreadPolicy(policy);
		String function = "GDSHE";
		String identifier = "AMZN";
		String mnemonic = "IQ_QUICK_COMP";
		try {
			returned = new LoadData(function, identifier, mnemonic).execute().get();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		mGraphButton = (Button)findViewById(R.id.graph_button);
        mGraphButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Intent i = new Intent(MainActivity.this, GraphActivity.class);
				//startActivity( i );
				/*try{
					Rows[] rows = sdkResponse[0].getRows();
					for( Rows r : rows ) {
						//Log.d( TAG, r.getRow()[0] );
					}
				} catch( Exception e ) {
					Log.d( TAG, "test" );
				}*/
			}
		});
		
        mTableButton = (Button)findViewById(R.id.table_button);
        mTableButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, TableActivity.class);
				
				try{
					i.putExtra( "json", returned );
					startActivity( i );
					
				} catch( Exception e ) {
					Log.d( TAG, "error" );
				}
			}
		});
	}
	
	/*public void setResponse( String returned ) {
		this.returned = returned;
	}*/
	
	/*public class LoadData extends AsyncTask<String, Void, String> {
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
			MainActivity.this.setResponse( result );
		}
		
	}*/

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
