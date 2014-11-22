package com.example.quickcompapp;

import java.util.Arrays;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.example.quickcompapp.Access.LoadData;
import com.example.quickcompapp.Access.ParseData;
import com.example.quickcompapp.Response.GDSSDKResponse;
import com.example.quickcompapp.Response.Rows;

public class TableChartActivity extends Activity {
	
	final Context context = this;
	
	private Button mBackButton;
	private Button mClearButton;
	private Button mAddButton;
	private Button mRemoveButton;
	
	private static final String TAG = "TableActivity";
	private String[] overviewM = {"IQ_MARKETCAP","IQ_CLOSEPRICE","IQ_TOTAL_REV","IQ_BASIC_EPS_INCL","IQ_NI_MARGIN"};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_tablechart);
		
		mBackButton = (Button)findViewById(R.id.back_button);
        mBackButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(TableChartActivity.this, MainActivity.class);
				startActivity( i );
			}
		});
        
        mClearButton = (Button)findViewById(R.id.clear_button);
        mClearButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		});
        
        mAddButton = (Button)findViewById(R.id.add_button);
        mAddButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
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
        
        mRemoveButton = (Button)findViewById(R.id.remove_button);
        mRemoveButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
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
		
        Parcelable[] test = getIntent().getParcelableArrayExtra("rows");
        Rows[] rows = new Rows[test.length];
        rows = Arrays.copyOf(test, test.length, Rows[].class);
		
		for( Rows r : rows ) {
			int cutIndex = r.getRow()[0].indexOf(':');
			String[] tableRow = new String[6];
			GDSSDKResponse[] tempResponse=null;
			try {
				int i = 0;
				tableRow[i++] = r.getRow()[0].substring(cutIndex+1);
				for( String mnemonic: overviewM ) {
					String tempJson = new LoadData("GDSHE", r.getRow()[0].substring(cutIndex+1), mnemonic, "STARTRANK:'1'").execute().get();
					ParseData tempGson = new ParseData( tempJson );
					tempGson.createGson();
					tempResponse = tempGson.getSdkResponse();
					tableRow[i++] = tempResponse[0].getRows()[0].getRow()[0];
				}
				
			} catch ( Exception e ) {
				// TODO Auto-generated catch block
				Log.d( TAG, "error with try in for loop" );
				e.printStackTrace();
			}
			
			createTableRow( tableRow );
		}
	}
	
	public void createTableRow( String[] rowData ) {
		
		for( int i = 0; i < rowData.length; i++ ) {
			String temp;
			if( rowData[i].equals("Data Unavailable")) {
				rowData[i] = "-----";
			}
			else if( rowData[i].indexOf('.') > 0 ) {
				temp = rowData[i].substring( 0, rowData[i].length() - 4 );
				rowData[i] = temp;
			}
		}
		
		TableLayout tl = (TableLayout)findViewById(R.id.main_table);
		TableRow tr = new TableRow(this);
		LayoutParams lp = new LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		tr.setLayoutParams(lp);
		
		for( int i = 0; i < rowData.length; i++ ) {
			
			TextView data = new TextView(this);
			data.setLayoutParams(lp);
			data.setTextColor(Color.WHITE);
			data.setText( rowData[i] );
			tr.addView(data);
			
			TextView space = new TextView(this);
			space.setLayoutParams(lp);
			space.setText( "   " );
			tr.addView(space);
		}
		
		tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	}
}
