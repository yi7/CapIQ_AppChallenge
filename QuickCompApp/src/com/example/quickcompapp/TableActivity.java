package com.example.quickcompapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class TableActivity extends Activity {
	private static final String TAG = "TableActivity";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_table);
		
		Intent i = getIntent();
		GDSSDKResponse[] sdkResponse = (GDSSDKResponse[])i.getSerializableExtra("sdkResponse");
		Log.d( TAG, sdkResponse[0].getNumRows());
	}
}
