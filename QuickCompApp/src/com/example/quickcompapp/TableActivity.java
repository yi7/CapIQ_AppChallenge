package com.example.quickcompapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.google.gson.Gson;

public class TableActivity extends Activity {
	private static final String TAG = "TableActivity";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_table);
		
		//Intent i = getIntent();
		//GDSSDKResponse[] sdkResponse = (GDSSDKResponse[])i.getSerializableExtra("sdkResponse");
		//Log.d( TAG, sdkResponse[0].getRows()[0].getRow()[0] + "");
		
		String returned = getIntent().getStringExtra("json");
		GDSSDKResponse[] sdkResponse;
		try{
			Gson gson = new Gson();
			Response response = gson.fromJson( returned, Response.class );
			sdkResponse = response.getGDSSDKResponse();
			//Log.d( TAG, sdkResponse[0].getRows()[0].getRow()[0] + "");
			for( Rows r : sdkResponse[0].getRows() ) {
				int cutIndex = r.getRow()[0].indexOf(':');
				//Log.d( TAG, r.getRow()[0].substring(cutIndex+1) );
				createTableRow( r.getRow()[0].substring(cutIndex+1) );
			}
		} catch( Exception e ) {
			Log.d(TAG, "Error");
		}
		
	}
	
	public void createTableRow( String company ) {
		TableLayout tl = (TableLayout)findViewById(R.id.main_table);
		TableRow tr = new TableRow(this);
		LayoutParams lp = new LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		tr.setLayoutParams(lp);
		
		TextView companies = new TextView(this);
		companies.setLayoutParams(lp);
		companies.setText( company );
		tr.addView(companies);
		
		tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	}
}
