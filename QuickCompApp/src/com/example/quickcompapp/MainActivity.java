package com.example.quickcompapp;

import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
//import android.os.StrictMode;
// Must add commons-codec-1.9.jar to build path

public class MainActivity extends Activity {
	
	private Button mGraphButton;
	private Button mTableButton;
	
	final Context context = this;
	private String returned;
	
	private static final String TAG = "MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build();
		//StrictMode.setThreadPolicy(policy);
		
		
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
				//get prompts.xml view
				LayoutInflater li = LayoutInflater.from(context);
				View promptsView = li.inflate(R.layout.prompts, null );
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
				
				//set prompts.xml to alertdialog builder
				alertDialogBuilder.setView(promptsView);
				final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
				
				//set dialog message
				alertDialogBuilder
					.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									String identifier = userInput.getText().toString();
									try {
										returned = new LoadData("GDSHE", identifier, "IQ_QUICK_COMP").execute().get();
									} catch (InterruptedException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									} catch (ExecutionException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									
									Intent i = new Intent(MainActivity.this, TableActivity.class);
									try{
										i.putExtra( "json", returned );
										startActivity( i );
										
									} catch( Exception e ) {
										Log.d( TAG, "error" );
									}
								}
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									dialog.cancel();
								}
					});
				
				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
				
				
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
