package com.example.quickcompapp;

import java.util.ArrayList;
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
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.example.quickcompapp.Access.LoadData;
import com.example.quickcompapp.Access.ParseData;
import com.example.quickcompapp.Response.GDSSDKResponse;
import com.example.quickcompapp.Response.Rows;
//import android.os.StrictMode;
// Must add commons-codec-1.9.jar to build path

public class MainActivity extends Activity {
	
	private Button mLineChartButton;
	private Button mTableChartButton;
	private Button mBarChartButton;
	
	final Context context = this;
	
	private ArrayList<Rows[]> tickerList = new ArrayList<Rows[]>();
	
	private static final String TAG = "MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
        mTableChartButton = (Button)findViewById(R.id.table_button);
        mTableChartButton.setOnClickListener(new View.OnClickListener() {
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
									String returned=""; GDSSDKResponse[] sdkResponse=null;
									try {
										returned = new LoadData("GDSHE", identifier, "IQ_QUICK_COMP", "STARTRANK:'1',ENDRANK:'5'").execute().get();
										ParseData pd = new ParseData( returned );
										pd.createGson();
										sdkResponse = pd.getSdkResponse();
									} catch (InterruptedException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									} catch (ExecutionException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									
									try{
										Intent i = new Intent(MainActivity.this, TableChartActivity.class);
										i.putExtra("rows", sdkResponse[0].getRows());
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
